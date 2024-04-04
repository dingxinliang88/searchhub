package com.youyi.searchhub.model.entity;

import com.youyi.searchhub.model.vo.PictureVO;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Data
public class Picture implements Serializable {

    private String title;

    private String url;

    @Serial
    private static final long serialVersionUID = 1L;

    public PictureVO toVO() {
        PictureVO pictureVO = new PictureVO();
        pictureVO.setTitle(title);
        pictureVO.setUrl(url);
        return pictureVO;
    }
}
