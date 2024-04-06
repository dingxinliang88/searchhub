package com.youyi.searchhub.service;

import static org.junit.jupiter.api.Assertions.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youyi.searchhub.model.dto.ArticleQueryRequest;
import com.youyi.searchhub.model.vo.ArticleVO;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Slf4j
@SpringBootTest
class ArticleServiceTest {

    @Resource
    private ArticleService articleService;

    @Test
    void queryArticleFromEs() {
        ArticleQueryRequest articleQueryRequest = new ArticleQueryRequest();
        articleQueryRequest.setSearchText("MySQL");
        articleQueryRequest.setCurrent(1);
        articleQueryRequest.setPageSize(10);

        Page<ArticleVO> articleVOPage = articleService.queryArticleFromEs(articleQueryRequest);
        log.info("articleVOPage: {}", articleVOPage.getRecords());
    }
}