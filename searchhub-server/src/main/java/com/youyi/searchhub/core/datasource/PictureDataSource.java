package com.youyi.searchhub.core.datasource;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youyi.searchhub.model.entity.Picture;
import com.youyi.searchhub.model.enums.SearchType;
import com.youyi.searchhub.model.vo.PictureVO;
import com.youyi.searchhub.util.RetryUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Slf4j
@Component
public class PictureDataSource implements DataSource<PictureVO> {

    private static final String FETCH_PIC_URL = "https://cn.bing.com/images/search?q=%s&first=%s";
    private static final String DEFAULT_SEARCH_TEXT = "努力";

    @Override
    public Page<PictureVO> doSearch(String searchText, long current, long pageSize) {
        if (StrUtil.isBlank(searchText)) {
            searchText = DEFAULT_SEARCH_TEXT;
        }
        String baseUrl = String.format(FETCH_PIC_URL, searchText, current);
        Document doc;
        try {
            doc = RetryUtil.retry(() -> Jsoup.connect(baseUrl).get(), 5);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return getPictureVOPage(doc, current, pageSize);
    }

    private Page<PictureVO> getPictureVOPage(Document doc, long current,
            long pageSize) {

        List<PictureVO> pictureVOList = new ArrayList<>((int) pageSize);
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
            pictureVOList.add(picture.toVO());

            if (pictureVOList.size() >= pageSize) {
                break;
            }
        }

        Page<PictureVO> pictureVOPage = new Page<>(current, pageSize, pageSize);
        pictureVOPage.setRecords(pictureVOList);

        return pictureVOPage;
    }


    @Override
    public String getType() {
        return SearchType.PICTURE.getType();
    }
}
