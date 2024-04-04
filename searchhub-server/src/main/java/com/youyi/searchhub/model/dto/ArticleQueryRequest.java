package com.youyi.searchhub.model.dto;

import com.youyi.searchhub.common.PageRequest;
import java.io.Serial;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleQueryRequest extends PageRequest implements Serializable {

    /**
     * 搜索关键词（标题、内容）
     */
    @NotNull
    private String searchText;

    @Serial
    private static final long serialVersionUID = 1L;
}
