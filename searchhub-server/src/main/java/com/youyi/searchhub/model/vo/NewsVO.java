package com.youyi.searchhub.model.vo;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 新闻 VO
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Data
public class NewsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String title;
    private String url;
    private String brief;
    private String image;
    private LocalDateTime focusDate;

}
