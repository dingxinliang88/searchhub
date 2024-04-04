package com.youyi.searchhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youyi.searchhub.mapper.ArticleMapper;
import com.youyi.searchhub.model.dto.ArticleQueryRequest;
import com.youyi.searchhub.model.entity.Article;
import com.youyi.searchhub.model.vo.ArticleVO;
import com.youyi.searchhub.service.ArticleService;
import com.youyi.searchhub.util.SqlUtil;
import com.youyi.searchhub.constant.CommonConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
        implements ArticleService {

    @Override
    public Page<ArticleVO> queryArticleByPage(ArticleQueryRequest articleQueryRequest) {
        QueryWrapper<Article> queryWrapper = getQueryWrapper(articleQueryRequest);
        long current = articleQueryRequest.getCurrent();
        long pageSize = articleQueryRequest.getPageSize();
        Page<Article> articlePage = this.page(new Page<>(current, pageSize), queryWrapper);
        return this.getArticleVOPage(articlePage);
    }

    @Override
    public QueryWrapper<Article> getQueryWrapper(ArticleQueryRequest articleQueryRequest) {
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

    @Override
    public Page<ArticleVO> getArticleVOPage(Page<Article> articlePage) {
        List<Article> articleList = articlePage.getRecords();
        List<ArticleVO> articleVOList = articleList.stream().map(article -> {
            ArticleVO articleVO = new ArticleVO();
            BeanUtils.copyProperties(article, articleVO);
            return articleVO;
        }).collect(Collectors.toList());

        long current = articlePage.getCurrent();
        long size = articlePage.getSize();
        long total = articlePage.getTotal();
        Page<ArticleVO> articleVOPage = new Page<>(current, size, total);
        articleVOPage.setRecords(articleVOList);
        return articleVOPage;
    }
}




