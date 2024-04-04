package com.youyi.searchhub.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youyi.searchhub.common.BaseResponse;
import com.youyi.searchhub.common.StatusCode;
import com.youyi.searchhub.model.dto.ArticleQueryRequest;
import com.youyi.searchhub.model.vo.ArticleVO;
import com.youyi.searchhub.service.ArticleService;
import com.youyi.searchhub.util.ResultUtil;
import com.youyi.searchhub.util.ThrowUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.youyi.searchhub.constant.CommonConstant.ONCE_MAX_PAGE_SIZE;

/**
 * 文章接口
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Slf4j
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @GetMapping("/query/page")
    public BaseResponse<Page<ArticleVO>> queryArticleByPage(ArticleQueryRequest articleQueryRequest) {
        // 防止爬虫
        ThrowUtil.throwIf(articleQueryRequest.getPageSize() > ONCE_MAX_PAGE_SIZE,
                StatusCode.PARAMS_ERROR, "一次获取资源过多");
        Page<ArticleVO> articleVOPage = articleService.queryArticleByPage(articleQueryRequest);
        return ResultUtil.success(articleVOPage);
    }
}
