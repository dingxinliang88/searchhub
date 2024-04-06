package com.youyi.searchhub.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.youyi.searchhub.model.dto.ArticleQueryRequest;
import com.youyi.searchhub.model.entity.Article;
import com.youyi.searchhub.model.vo.ArticleVO;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
public interface ArticleService extends IService<Article> {

    /**
     * 分页查询文章
     *
     * @param articleQueryRequest 文章查询请求
     * @return article vo page
     */
    Page<ArticleVO> queryArticleByPage(ArticleQueryRequest articleQueryRequest);

    /**
     * 获取封装后的文章分页信息
     *
     * @param articlePage 原始的文章分页信息
     * @return article page
     */
    Page<ArticleVO> queryArticleByPage(Page<Article> articlePage);


    /**
     * 从Elasticsearch查询文章信息。
     *
     * @param articleQueryRequest 包含查询条件的请求对象，用于指定文章查询的细节，如关键词、分类等。
     * @return 返回一个Page对象，其中包含了查询到的文章结果集，以及分页相关信息（如总记录数、当前页码等）。
     */
    Page<ArticleVO> queryArticleFromEs(ArticleQueryRequest articleQueryRequest);
}

