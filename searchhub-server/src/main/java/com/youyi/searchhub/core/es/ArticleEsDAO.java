package com.youyi.searchhub.core.es;

import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 文章 ES 操作
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
public interface ArticleEsDAO extends ElasticsearchRepository<ArticleEsDTO, Long> {

    /**
     * 根据文章标题查找Es中的文章数据
     *
     * @param title 文章的标题，用于搜索匹配
     * @return 返回匹配到的文章数据列表，类型为ArticleEsDTO的List
     */
    List<ArticleEsDTO> findByTitle(String title);
}
