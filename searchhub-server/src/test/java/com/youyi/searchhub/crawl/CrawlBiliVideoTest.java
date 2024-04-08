package com.youyi.searchhub.crawl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.youyi.searchhub.model.entity.BiliVideo;
import java.net.HttpCookie;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * 爬取哔哩哔哩视频测试
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Slf4j
public class CrawlBiliVideoTest {

    private static final String COOKIE_KEY = "buvid3";

    private static final String BILI_INDEX_URL
            = "https://www.bilibili.com/";


    @Test
    public void testCrawl() {

        String baseUrl = "https://api.bilibili.com/x/web-interface/search/type?search_type=video&keyword=%s";
        String keyword = "林俊杰";

        try (HttpResponse httpResponse = HttpRequest.get(
                        String.format(baseUrl, keyword))
                .cookie(getBiliCookie())
                .execute()) {
            String body = httpResponse.body();
            Map<String, Object> bodyMap = JSONUtil.toBean(body, new TypeReference<>() {
            }, true);

            JSONObject data = (JSONObject) bodyMap.get("data");
            long page = data.getLong("page");
            long pageSize = data.getLong("pagesize");
            log.info("{} - {}", page, pageSize);
            JSONArray result = data.getJSONArray("result");
            for (Object res : result) {
                JSONObject jsonObject = (JSONObject) res;
                BiliVideo biliVideo = new BiliVideo();
                biliVideo.setTitle(jsonObject.getStr("title"));
                biliVideo.setDescription(jsonObject.getStr("description"));
                biliVideo.setPic("https:" + jsonObject.getStr("pic"));
                biliVideo.setVideoUrl(jsonObject.getStr("arcurl"));
                log.info("biliVideo: {}", biliVideo);
            }
        }
    }

    @Test
    public void testCookie() {
        HttpCookie cookie;
        try (HttpResponse response = HttpRequest.get(BILI_INDEX_URL).execute()) {
            cookie = response.getCookie(COOKIE_KEY);
        }
        log.info("cookie: {}", cookie);
    }

    HttpCookie getBiliCookie() {
        HttpCookie cookie;
        try (HttpResponse response = HttpRequest.get(BILI_INDEX_URL).execute()) {
            cookie = response.getCookie(COOKIE_KEY);
        }
        return cookie;
    }


}
