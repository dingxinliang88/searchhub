package com.youyi.searchhub.crawl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.youyi.searchhub.model.entity.Picture;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

/**
 * 爬取图片测试
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Slf4j
public class CrawlPictureTest {


    @Test
    public void testCrawl() throws IOException {
        String baseUrl = "https://cn.bing.com/images/search?q=小黑子&first=1";
        Document doc = Jsoup.connect(baseUrl).get();
        Elements elements = doc.select(".iuscp.isv");
        for (Element element : elements) {
            // 取图片地址 (murl)
            String m = element.select(".iusc").get(0).attr("m");
            Map<String, Object> map = JSONUtil.toBean(m, new TypeReference<>() {
            }, true);
            String url = (String) map.get("murl");
            // 取标题
            String title = element.select(".inflnk").get(0).attr("aria-label");

            Picture picture = new Picture();
            picture.setTitle(title);
            picture.setUrl(url);

            log.info("picture: {}", picture);
        }
    }


}
