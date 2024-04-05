package com.youyi.searchhub.core.datasource;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youyi.searchhub.model.entity.Picture;
import com.youyi.searchhub.model.enums.SearchType;
import com.youyi.searchhub.model.vo.PictureVO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Component
public class PictureDataSource implements DataSource<PictureVO> {

    @Override
    public Page<PictureVO> doSearch(String searchText, long current, long pageSize) {
        String baseUrl = String.format("https://cn.bing.com/images/search?q=%s&first=%s",
                searchText, current);
        Document doc;
        try {
            doc = Jsoup.connect(baseUrl).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    @PostConstruct
    public void register() {
        DataSourceRegistry.registry(SearchType.PICTURE, this);
    }
}
