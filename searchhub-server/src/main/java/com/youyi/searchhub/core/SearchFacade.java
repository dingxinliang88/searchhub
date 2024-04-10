package com.youyi.searchhub.core;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youyi.searchhub.core.datasource.ArticleDataSource;
import com.youyi.searchhub.core.datasource.BiliVideoDataSource;
import com.youyi.searchhub.core.datasource.DataSource;
import com.youyi.searchhub.core.datasource.DataSourceFactory;
import com.youyi.searchhub.core.datasource.NewsDataSource;
import com.youyi.searchhub.core.datasource.PictureDataSource;
import com.youyi.searchhub.model.dto.SearchRequest;
import com.youyi.searchhub.model.enums.SearchType;
import com.youyi.searchhub.model.vo.ArticleVO;
import com.youyi.searchhub.model.vo.BiliVideoVO;
import com.youyi.searchhub.model.vo.NewsVO;
import com.youyi.searchhub.model.vo.PictureVO;
import com.youyi.searchhub.model.vo.SearchVO;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 搜索门面
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Slf4j
@Component
public class SearchFacade {

    @Resource
    private ArticleDataSource articleDataSource;

    @Resource
    private PictureDataSource pictureDataSource;

    @Resource
    private BiliVideoDataSource biliVideoDataSource;

    @Resource
    private NewsDataSource newsDataSource;

    @Resource
    private DataSourceFactory dataSourceFactory;

    public SearchVO doSearch(SearchRequest searchRequest) {

        SearchType searchType = SearchType.resolve(searchRequest.getType());

        String searchText = searchRequest.getSearchText();
        long current = searchRequest.getCurrent();
        long pageSize = searchRequest.getPageSize();

        SearchVO searchVO = new SearchVO();
        if (SearchType.ALL.equals(searchType)) {
            // TODO 简化搜索所有内容的代码
            // 搜索全部
            Page<ArticleVO> articleVOPage = articleDataSource.doSearch(searchText, current,
                    pageSize);

            Page<PictureVO> pictureVOPage = pictureDataSource.doSearch(searchText, current,
                    pageSize);

            Page<BiliVideoVO> biliVideoVOPage = biliVideoDataSource.doSearch(searchText, current,
                    pageSize);

            Page<NewsVO> newsVOPage = newsDataSource.doSearch(searchText, current, pageSize);

            searchVO.setArticleVOList(articleVOPage.getRecords());
            searchVO.setPictureVOList(pictureVOPage.getRecords());
            searchVO.setBiliVideoVOList(biliVideoVOPage.getRecords());
            searchVO.setNewsVOList(newsVOPage.getRecords());
        } else {
            DataSource<?> dataSource = dataSourceFactory.getDataSource(searchType);
            Page<?> dataPage = dataSource.doSearch(searchText, current, pageSize);
            searchVO.setDataList(dataPage.getRecords());
        }
        return searchVO;
    }

}
