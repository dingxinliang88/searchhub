package com.youyi.searchhub.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youyi.searchhub.constant.CommonConstant;
import com.youyi.searchhub.core.es.ArticleEsDTO;
import com.youyi.searchhub.core.es.ArticleEsDTO.Fields;
import com.youyi.searchhub.mapper.ArticleMapper;
import com.youyi.searchhub.model.dto.ArticleQueryRequest;
import com.youyi.searchhub.model.entity.Article;
import com.youyi.searchhub.model.vo.ArticleVO;
import com.youyi.searchhub.service.ArticleService;
import com.youyi.searchhub.util.SqlUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Slf4j
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
        implements ArticleService {

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public Page<ArticleVO> queryArticleByPage(ArticleQueryRequest articleQueryRequest) {
        QueryWrapper<Article> queryWrapper = getQueryWrapper(articleQueryRequest);
        long current = articleQueryRequest.getCurrent();
        long pageSize = articleQueryRequest.getPageSize();
        Page<Article> articlePage = this.page(new Page<>(current, pageSize), queryWrapper);
        return this.queryArticleByPage(articlePage);
    }

    @Override
    public Page<ArticleVO> queryArticleByPage(Page<Article> articlePage) {
        List<Article> articleList = articlePage.getRecords();
        List<ArticleVO> articleVOList = articleList.stream().map(Article::toVO)
                .collect(Collectors.toList());

        long current = articlePage.getCurrent();
        long size = articlePage.getSize();
        long total = articlePage.getTotal();
        Page<ArticleVO> articleVOPage = new Page<>(current, size, total);
        articleVOPage.setRecords(articleVOList);
        return articleVOPage;
    }

    @Override
    public Page<ArticleVO> queryArticleFromEs(ArticleQueryRequest articleQueryRequest) {
        NativeSearchQuery searchQuery = getSearchQuery(articleQueryRequest);
        SearchHits<ArticleEsDTO> searchHits = elasticsearchRestTemplate.search(searchQuery,
                ArticleEsDTO.class);

        // 解析结果
        List<Article> resourceList = new ArrayList<>();
        if (searchHits.hasSearchHits()) {
            List<SearchHit<ArticleEsDTO>> searchHitList = searchHits.getSearchHits();

            List<Long> articleIdList = searchHitList.stream()
                    .map(searchHit -> searchHit.getContent().getId()).toList();
            @SuppressWarnings("DataFlowIssue") Map<String, List<SearchHit<ArticleEsDTO>>> highLightMap
                    = searchHitList.stream().collect(Collectors.groupingBy(SearchHit::getId));

            // 从 db 中获取最新数据
            List<Article> articleList = listByIds(articleIdList);
            if (!articleList.isEmpty()) {

                Map<Long, List<Article>> idArticleMap = articleList.stream()
                        .collect(Collectors.groupingBy(Article::getId));
                articleIdList.forEach(articleId -> {
                    if (idArticleMap.containsKey(articleId)) {
                        // 文章在 db 中
                        Article article = idArticleMap.get(articleId).get(0);
                        SearchHit<ArticleEsDTO> articleEsDTOSearchHit = highLightMap.get(
                                String.valueOf(articleId)).get(0);
                        // 设置文章高亮标题和内容
                        List<String> titleWithHighLight = articleEsDTOSearchHit.getHighlightField(
                                Fields.title);
                        article.setTitle(
                                titleWithHighLight.isEmpty() ?
                                        article.getTitle() :
                                        StrUtil.join("", titleWithHighLight)
                        );
                        List<String> contentWithHighLight = articleEsDTOSearchHit.getHighlightField(
                                Fields.content);
                        // TODO 内容完整搜索
                        article.setContent(
                                contentWithHighLight.isEmpty() ?
                                        article.getContent() :
                                        contentWithHighLight.get(0) + "……【点击查看更多】"
                        );

                        resourceList.add(article);
                    } else {
                        // 清空 es 中 db 已删除的数据
                        String delete = elasticsearchRestTemplate.delete(String.valueOf(articleId),
                                ArticleEsDTO.class);
                        log.info("delete article from es, id {} => {}", articleId, delete);
                    }
                });
            }
        }
        // 封装结果
        Page<ArticleVO> articleVOPage = new Page<>(articleQueryRequest.getCurrent(),
                articleQueryRequest.getPageSize());
        articleVOPage.setTotal(searchHits.getTotalHits());
        List<ArticleVO> articleVOList = resourceList.stream().map(Article::toVO).toList();
        articleVOPage.setRecords(articleVOList);
        return articleVOPage;
    }

    QueryWrapper<Article> getQueryWrapper(ArticleQueryRequest articleQueryRequest) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        if (Objects.isNull(articleQueryRequest)) {
            return queryWrapper;
        }
        String searchText = articleQueryRequest.getSearchText();
        if (StringUtils.isNotBlank(searchText)) {
            queryWrapper.like("title", searchText)
                    .or()
                    .like("content", searchText);
        }
        String sortField = articleQueryRequest.getSortField();
        String sortOrder = articleQueryRequest.getSortOrder();
        queryWrapper.orderBy(SqlUtil.validSortField(sortField),
                CommonConstant.SORT_ORDER_ASC.equals(sortOrder), sortField);
        return queryWrapper;
    }

    NativeSearchQuery getSearchQuery(ArticleQueryRequest articleQueryRequest) {
        String searchText = articleQueryRequest.getSearchText();
        long current = articleQueryRequest.getCurrent();
        long pageSize = articleQueryRequest.getPageSize();
        String sortField = articleQueryRequest.getSortField();
        String sortOrder = articleQueryRequest.getSortOrder();

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        // 按照关键词搜索
        if (StrUtil.isNotBlank(searchText)) {
            boolQueryBuilder.should(QueryBuilders.matchQuery(Fields.title, searchText))
                    .should(QueryBuilders.matchQuery(Fields.content, searchText))
                    .minimumShouldMatch(1);
        }

        // 排序
        if (StrUtil.isBlank(sortField) || !SqlUtil.validSortField(sortField)) {
            sortField = CommonConstant.DEFAULT_SORT_FIELD;
        }
        FieldSortBuilder fieldSortBuilder = SortBuilders.fieldSort(sortField);
        fieldSortBuilder.order(
                CommonConstant.SORT_ORDER_ASC.equals(sortOrder) ? SortOrder.ASC : SortOrder.DESC);

        // 分页
        PageRequest pageRequest = PageRequest.of((int) (current - 1), (int) pageSize);

        // 高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field(Fields.title).field(Fields.content)
                .preTags("<font color='red'>").postTags("</font>");

        // TODO 搜索建议

        // 构造查询
        return new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(pageRequest)
                .withHighlightBuilder(highlightBuilder)
                .withSorts(fieldSortBuilder)
                .build();
    }
}




