package com.youyi.searchhub.crawl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * 爬取测试
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Slf4j
public class CrawlNewsTest {

    private static final String FETCH_NEWS_URL = "https://news.cctv.com/2019/07/gaiban/cmsdatainterface/page/news_1.jsonp?cb=news";

    private static final String NEWS_CONTENT_REGEX = "^[^(]*?\\((.*?)\\)$";

    private static final Pattern CONTENT_PATTERN = Pattern.compile(NEWS_CONTENT_REGEX);

    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36";
    private static final String REFERER = "https://news.cctv.com/?spm=C94212.P4YnMod9m2uD.E2XVQsMhlk44.3";


    @Test
    void testCrawl() {
        try (HttpResponse httpResponse = HttpRequest.get(FETCH_NEWS_URL)
                .header(Header.USER_AGENT, USER_AGENT)
                .header(Header.REFERER, REFERER)
                .execute()) {
            String originRes = httpResponse.body();
            Matcher matcher = CONTENT_PATTERN.matcher(originRes);
            if (!matcher.find()) {
                throw new RuntimeException("获取新闻数据失败");
            }

            String contentJson = matcher.group(1); // 内容 JSON
            Map<String, Object> contentMap = JSONUtil.toBean(contentJson, new TypeReference<>() {
            }, true);

            JSONObject data = (JSONObject) contentMap.get("data");

            int total = data.getInt("total");
            log.info("total: {}", total);
            JSONArray dataList = data.getJSONArray("list");

            for (Object o : dataList) {
                JSONObject item = (JSONObject) o;
                String title = item.getStr("title");
                String url = item.getStr("url");
                String brief = item.getStr("brief");
                String keywords = item.getStr("keywords");
                // log.info("title: {}, url: {}, brief: {}, keywords: {}", title, url, brief,
                //         keywords);
            }


        }

    }

}
