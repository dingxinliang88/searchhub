package com.youyi.searchhub.core.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youyi.searchhub.model.dto.ArticleQueryRequest;
import com.youyi.searchhub.model.enums.SearchType;
import com.youyi.searchhub.model.vo.ArticleVO;
import com.youyi.searchhub.service.ArticleService;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Component
public class ArticleDataSource implements DataSource<ArticleVO> {

    @Resource
    private ArticleService articleService;

    @Override
    public Page<ArticleVO> doSearch(String searchText, long current, long pageSize) {
        ArticleQueryRequest articleQueryRequest = new ArticleQueryRequest();
        articleQueryRequest.setSearchText(searchText);
        articleQueryRequest.setCurrent(current);
        articleQueryRequest.setPageSize(pageSize);
        return articleService.queryArticleByPage(articleQueryRequest);
    }

    @Override
    @PostConstruct
    public void register() {
        DataSourceRegistry.registry(SearchType.ARTICLE, this);
    }
}
