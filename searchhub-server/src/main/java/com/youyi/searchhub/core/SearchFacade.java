package com.youyi.searchhub.core;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
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

    private static final int SYS_CPU_COUNT = Runtime.getRuntime().availableProcessors();

    private static final Executor EXECUTOR_POOL = new ThreadPoolExecutor(
            SYS_CPU_COUNT / 2,
            SYS_CPU_COUNT,
            10000L,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(1000),
            new ThreadFactoryBuilder().setNameFormat("search-thread-pool-%d").build()
    );

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
            searchVO = searchAll(searchText, current, pageSize);
        } else {
            DataSource<?> dataSource = dataSourceFactory.getDataSource(searchType);
            Page<?> dataPage = dataSource.doSearch(searchText, current, pageSize);
            searchVO.setDataList(dataPage.getRecords());
        }
        return searchVO;
    }


    /**
     * 搜索所有类型的内容
     *
     * @param searchText 搜索文本
     * @param current    当前页码
     * @param pageSize   每页显示数量
     * @return 搜索结果的视图对象，包含文章、图片、B站视频和新闻等内容的搜索结果
     */
    private SearchVO searchAll(String searchText, long current, long pageSize) {
        // 对文章、图片、B站视频和新闻进行搜索
        CompletableFuture<Page<ArticleVO>> articleVOPageTask = CompletableFuture.supplyAsync(
                () -> articleDataSource.doSearch(searchText, current,
                        pageSize), EXECUTOR_POOL);
        CompletableFuture<Page<PictureVO>> pictureVOPageTask = CompletableFuture.supplyAsync(
                () -> pictureDataSource.doSearch(searchText, current,
                        pageSize), EXECUTOR_POOL);
        CompletableFuture<Page<BiliVideoVO>> biliVideoVOPageTask = CompletableFuture.supplyAsync(
                () -> biliVideoDataSource.doSearch(searchText, current,
                        pageSize), EXECUTOR_POOL);
        CompletableFuture<Page<NewsVO>> newsVOPageTask = CompletableFuture.supplyAsync(
                () -> newsDataSource.doSearch(searchText, current, pageSize), EXECUTOR_POOL);

        CompletableFuture<Void> allTasks = CompletableFuture.allOf(articleVOPageTask,
                pictureVOPageTask, biliVideoVOPageTask, newsVOPageTask);

        allTasks.join();

        // 创建搜索结果视图对象，并填充搜索到的内容
        SearchVO searchVO = new SearchVO();
        try {
            searchVO.setArticleVOList(articleVOPageTask.get().getRecords());
            searchVO.setPictureVOList(pictureVOPageTask.get().getRecords());
            searchVO.setBiliVideoVOList(biliVideoVOPageTask.get().getRecords());
            searchVO.setNewsVOList(newsVOPageTask.get().getRecords());

            return searchVO;
        } catch (InterruptedException | ExecutionException e) {
            log.error("搜索异常", e);
            return searchVO;
        }
    }


}
