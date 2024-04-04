package com.youyi.searchhub.model.dto;

import com.youyi.searchhub.common.PageRequest;
import java.io.Serial;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 图片查询请求
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PictureQueryRequest extends PageRequest implements Serializable {

    @NotNull
    private String searchText;

    @Serial
    private static final long serialVersionUID = 1L;

}
