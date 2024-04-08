package com.youyi.searchhub.model.entity;

import com.youyi.searchhub.model.vo.BiliVideoVO;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Data
public class BiliVideo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String title;

    private String description;

    private String pic;

    private String videoUrl;

    public BiliVideoVO toVO() {
        BiliVideoVO biliVideoVO = new BiliVideoVO();
        biliVideoVO.setTitle(title);
        biliVideoVO.setPic(pic);
        biliVideoVO.setVideoUrl(videoUrl);
        return biliVideoVO;
    }
}
