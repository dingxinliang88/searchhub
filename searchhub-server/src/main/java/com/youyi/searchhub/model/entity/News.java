package com.youyi.searchhub.model.entity;

import com.youyi.searchhub.model.vo.NewsVO;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 新闻
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class News {

    private String title;
    private String url;
    private String brief;
    private String keywords; // 空格分开
    private String image;
    private LocalDateTime focusDate;

    public NewsVO toVO() {
        NewsVO vo = new NewsVO();
        vo.setTitle(title);
        vo.setUrl(url);
        vo.setBrief(brief);
        vo.setImage(image);
        vo.setFocusDate(focusDate);
        return vo;
    }
}
