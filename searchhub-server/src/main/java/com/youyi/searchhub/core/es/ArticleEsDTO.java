package com.youyi.searchhub.core.es;

import com.youyi.searchhub.model.entity.Article;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Data
@FieldNameConstants
@Document(indexName = "article")
public class ArticleEsDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ssXXX";

    /**
     * id
     */
    @Id
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
    @Field(name = "create_time", index = false, store = true, type = FieldType.Date, format = {},
            pattern = DATE_TIME_PATTERN)
    private Date createTime;

    /**
     * 是否删除 0 - 未删除， 1 - 已删除
     */
    private Integer deleted;

    public static ArticleEsDTO objToDto(Article article) {
        if (Objects.isNull(article)) {
            return null;
        }

        ArticleEsDTO articleEsDTO = new ArticleEsDTO();
        articleEsDTO.setId(article.getId());
        articleEsDTO.setTitle(article.getTitle());
        articleEsDTO.setContent(article.getContent());
        articleEsDTO.setCreateTime(article.getCreateTime());
        articleEsDTO.setDeleted(article.getDeleted());
        return articleEsDTO;
    }

    public static Article dtoToObj(ArticleEsDTO articleEsDTO) {
        if (Objects.isNull(articleEsDTO)) {
            return null;
        }
        Article article = new Article();
        article.setId(articleEsDTO.getId());
        article.setTitle(articleEsDTO.getTitle());
        article.setContent(articleEsDTO.getContent());
        article.setCreateTime(articleEsDTO.getCreateTime());
        article.setDeleted(articleEsDTO.getDeleted());
        return article;
    }
}
