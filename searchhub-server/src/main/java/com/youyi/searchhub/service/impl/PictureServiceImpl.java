package com.youyi.searchhub.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youyi.searchhub.model.dto.PictureQueryRequest;
import com.youyi.searchhub.model.entity.Picture;
import com.youyi.searchhub.model.vo.PictureVO;
import com.youyi.searchhub.service.PictureService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Slf4j
@Service
public class PictureServiceImpl implements PictureService {

    @Override
    public Page<PictureVO> queryPictureByPage(PictureQueryRequest req) {
        String searchText = req.getSearchText();
        long current = req.getCurrent();
        long pageSize = req.getPageSize();

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
}
