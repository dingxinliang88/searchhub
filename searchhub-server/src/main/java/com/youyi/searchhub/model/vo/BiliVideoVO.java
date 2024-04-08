package com.youyi.searchhub.model.vo;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Data
public class BiliVideoVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String title;

    private String pic;

    private String videoUrl;
}
