package com.youyi.searchhub.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youyi.searchhub.model.dto.ArticleQueryRequest;
import com.youyi.searchhub.model.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;
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
     * 组装查询条件
     *
     * @param articleQueryRequest 查询条件
     * @return query wrapper
     */
    QueryWrapper<Article> getQueryWrapper(ArticleQueryRequest articleQueryRequest);

    /**
     * 获取封装后的文章分页信息
     *
     * @param articlePage 原始的文章分页信息
     * @return article page
     */
    Page<ArticleVO> getArticleVOPage(Page<Article> articlePage);
}
