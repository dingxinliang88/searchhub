package com.youyi.searchhub.model.vo;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Data
public class SearchVO implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;

    private List<PictureVO> pictureVOList;

    private List<ArticleVO> articleVOList;

    private List<?> dataList;

}
