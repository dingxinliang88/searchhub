package com.youyi.searchhub.model.dto;

import com.youyi.searchhub.common.PageRequest;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleQueryRequest extends PageRequest implements Serializable {

    /**
     * 搜索关键词（标题、内容）
     */
    private String searchText;

    @Serial
    private static final long serialVersionUID = 1L;
}
