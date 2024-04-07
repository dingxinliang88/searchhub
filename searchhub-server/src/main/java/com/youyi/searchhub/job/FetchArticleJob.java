package com.youyi.searchhub.job;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.youyi.searchhub.constant.CrawlerConstant;
import com.youyi.searchhub.model.entity.Article;
import com.youyi.searchhub.service.ArticleService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Slf4j
@Component
public class FetchArticleJob {

    @Resource
    private ArticleService articleService;

    private static final AtomicInteger INCR = new AtomicInteger(1);

    // 每天中午 12 点触发
    @Scheduled(cron = "0 0 12 * * ?")
    public void fetchArticle() {
        String baseUrl = CrawlerConstant.CRAWLER_ARTICLE_URL;
        int current = INCR.incrementAndGet();
        String body = CrawlerConstant.CRAWLER_ARTICLE_REQ_PARAMS.formatted(current);

        String resp;
        try (HttpResponse response = HttpRequest.post(baseUrl)
                .body(body)
                .execute()) {
            resp = response.body();
        }

        // TODO 文章去重
        // 解析 resp
        Map<String, Object> respMap = JSONUtil.toBean(resp,
                new TypeReference<>() {
                }, true);

        JSONObject data = (JSONObject) respMap.get("data");
        JSONArray records = data.get("records", JSONArray.class, true);
        List<Article> articleList = new ArrayList<>();
        for (Object recordObj : records) {
            JSONObject record = (JSONObject) recordObj;

            String title = record.getStr("title");
            String content = record.getStr("content");

            Article article = new Article();
            article.setTitle(title);
            article.setContent(content);

            articleList.add(article);
        }

        boolean saveRes = articleService.saveBatch(articleList);
        log.info("save article list in db, res: {}, size: {}", saveRes, articleList.size());

        // [PASS]同步方案三：代码层面同步给 ES
        // 一方面对代码侵入性大，一方面有一致性问题
        // 目前采用方案是：使用 canal 做同步
    }

}
