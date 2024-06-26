package com.youyi.searchhub.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import com.youyi.searchhub.model.vo.ArticleVO;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 文章
 *
 * @TableName article
 */
@TableName(value = "article")
@Data
public class Article implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否删除 0 - 未删除， 1 - 已删除
     */
    @TableLogic
    private Integer deleted;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public ArticleVO toVO() {
        ArticleVO articleVO = new ArticleVO();
        articleVO.setId(id);
        articleVO.setTitle(title);
        articleVO.setContent(content);
        articleVO.setCreateTime(createTime);
        return articleVO;
    }
}