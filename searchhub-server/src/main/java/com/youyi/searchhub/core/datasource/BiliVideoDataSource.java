package com.youyi.searchhub.core.datasource;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youyi.searchhub.model.entity.BiliVideo;
import com.youyi.searchhub.model.enums.SearchType;
import com.youyi.searchhub.model.vo.BiliVideoVO;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Component
public class BiliVideoDataSource implements DataSource<BiliVideoVO> {


    private static final String COOKIE_KEY = "buvid3";

    private static final String BILI_INDEX_URL
            = "https://www.bilibili.com/";

    private static final String FETCH_BILI_VIDEO_URL = "https://api.bilibili.com/x/web-interface/search/type?search_type=video&keyword=%s";

    private static final long BILI_VIDEO_PAGE_SIZE = 20L;
    private static final long BILI_VIDEO_CURRENT = 1L;

    @Override
    public Page<BiliVideoVO> doSearch(String searchText, long current, long pageSize) {
        List<BiliVideoVO> biliVideoVOList = new ArrayList<>();
        try (HttpResponse httpResponse = HttpRequest.get(
                        String.format(FETCH_BILI_VIDEO_URL, searchText))
                .cookie(getBiliCookie())
                .execute()) {
            String body = httpResponse.body();
            Map<String, Object> bodyMap = JSONUtil.toBean(body, new TypeReference<>() {
            }, true);
            Optional<JSONObject> optionalData = Optional.ofNullable(
                    (JSONObject) bodyMap.get("data"));
            optionalData.ifPresent(data -> {
                JSONArray result = data.getJSONArray("result");
                for (Object res : result) {
                    JSONObject jsonObject = (JSONObject) res;

                    BiliVideo biliVideo = new BiliVideo();
                    biliVideo.setTitle(jsonObject.getStr("title"));
                    biliVideo.setDescription(jsonObject.getStr("description"));
                    biliVideo.setPic("https:" + jsonObject.getStr("pic"));
                    biliVideo.setVideoUrl(jsonObject.getStr("arcurl"));
                    biliVideoVOList.add(biliVideo.toVO());
                }
            });
        }
        Page<BiliVideoVO> biliVideoVOPage = new Page<>(BILI_VIDEO_CURRENT, BILI_VIDEO_PAGE_SIZE);
        biliVideoVOPage.setRecords(biliVideoVOList);
        return biliVideoVOPage;
    }

    @PostConstruct
    @Override
    public void register() {
        DataSourceRegistry.registry(SearchType.BILI_VIDEO, this);
    }


    private HttpCookie getBiliCookie() {
        HttpCookie cookie;
        try (HttpResponse response = HttpRequest.get(BILI_INDEX_URL).execute()) {
            cookie = response.getCookie(COOKIE_KEY);
        }
        return cookie;
    }
}
