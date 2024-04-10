package com.youyi.searchhub.core.datasource;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youyi.searchhub.model.entity.News;
import com.youyi.searchhub.model.enums.SearchType;
import com.youyi.searchhub.model.vo.NewsVO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Component
public class NewsDataSource implements DataSource<NewsVO> {

    private static final String FETCH_NEWS_URL = "https://news.cctv.com/2019/07/gaiban/cmsdatainterface/page/news_1.jsonp?cb=news";

    private static final String NEWS_CONTENT_REGEX = "^[^(]*?\\((.*?)\\)$";

    private static final Pattern CONTENT_PATTERN = Pattern.compile(NEWS_CONTENT_REGEX);

    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36";
    private static final String REFERER = "https://news.cctv.com/?spm=C94212.P4YnMod9m2uD.E2XVQsMhlk44.3";


    @Override
    public Page<NewsVO> doSearch(String searchText, long current, long pageSize) {
        long total;
        List<NewsVO> newsVOList = new ArrayList<>();
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

            total = data.getLong("total");
            JSONArray dataList = data.getJSONArray("list");

            for (Object o : dataList) {
                JSONObject item = (JSONObject) o;
                String title = item.getStr("title");
                String url = item.getStr("url");
                String brief = item.getStr("brief");
                String keywords = item.getStr("keywords");
                String image = item.getStr("image");
                LocalDateTime focusDate = item.getLocalDateTime("focus_date", LocalDateTime.now());
                News news = new News(title, url, brief, keywords, image, focusDate);
                newsVOList.add(news.toVO());

                if (newsVOList.size() >= pageSize) {
                    break;
                }
            }
        }

        Page<NewsVO> newsVOPage = new Page<>(current, pageSize, total);
        newsVOPage.setRecords(newsVOList);
        return newsVOPage;
    }

    @Override
    public String getType() {
        return SearchType.NEWS.getType();
    }
}
