package com.youyi.searchhub.crawl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.youyi.searchhub.MainApplication;
import com.youyi.searchhub.model.entity.Article;
import com.youyi.searchhub.service.ArticleService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 爬取文章测试
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Slf4j
@SpringBootTest(classes = MainApplication.class)
public class CrawlArticleTest {

    @Resource
    private ArticleService articleService;


    @Test
    public void testCrawl() {

        String baseUrl = "https://www.code-nav.cn/api/post/search/page/vo";
        String body = """
                {
                    "current": 1,
                    "pageSize": 5,
                    "sortField": "createTime",
                    "sortOrder": "descend",
                    "category": "文章",
                    "tags": [],
                    "reviewStatus": 1
                }
                """;

        String resp;
        try (HttpResponse response = HttpRequest.post(baseUrl)
                .body(body)
                .execute()) {
            resp = response.body();
        }

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

        // log.info("article list: {}", articleList);
        boolean saveRes = articleService.saveBatch(articleList);
        log.info("save result: {}", saveRes);
    }


}
