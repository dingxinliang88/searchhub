package com.youyi.searchhub.controller;

import static com.youyi.searchhub.constant.CommonConstant.ONCE_MAX_PAGE_SIZE;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youyi.searchhub.common.BaseResponse;
import com.youyi.searchhub.common.StatusCode;
import com.youyi.searchhub.exception.BusinessException;
import com.youyi.searchhub.model.dto.ArticleQueryRequest;
import com.youyi.searchhub.model.dto.PictureQueryRequest;
import com.youyi.searchhub.model.dto.SearchRequest;
import com.youyi.searchhub.model.vo.ArticleVO;
import com.youyi.searchhub.model.vo.PictureVO;
import com.youyi.searchhub.model.vo.SearchVO;
import com.youyi.searchhub.service.ArticleService;
import com.youyi.searchhub.service.PictureService;
import com.youyi.searchhub.util.ResultUtil;
import com.youyi.searchhub.util.ThrowUtil;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@RestController
@RequestMapping("/search")
public class SearchController {


    @Resource
    private ArticleService articleService;

    @Resource
    private PictureService pictureService;


    // MySQL => 836ms
    @GetMapping("/all/sync")
    public BaseResponse<SearchVO> searchSync(@Validated SearchRequest searchRequest) {
        // 防止爬虫
        ThrowUtil.throwIf(searchRequest.getPageSize() > ONCE_MAX_PAGE_SIZE,
                StatusCode.PARAMS_ERROR, "一次获取资源过多");

        ArticleQueryRequest articleQueryRequest = new ArticleQueryRequest();
        articleQueryRequest.setSearchText(searchRequest.getSearchText());
        articleQueryRequest.setCurrent(searchRequest.getCurrent());
        articleQueryRequest.setPageSize(searchRequest.getPageSize());
        Page<ArticleVO> articleVOPage = articleService.queryArticleByPage(articleQueryRequest);

        PictureQueryRequest pictureQueryRequest = new PictureQueryRequest();
        pictureQueryRequest.setSearchText(searchRequest.getSearchText());
        pictureQueryRequest.setCurrent(searchRequest.getCurrent());
        pictureQueryRequest.setPageSize(searchRequest.getPageSize());
        Page<PictureVO> pictureVOPage = pictureService.queryPictureByPage(pictureQueryRequest);

        SearchVO searchVO = new SearchVO();
        searchVO.setArticleVOList(articleVOPage.getRecords());
        searchVO.setPictureVOList(pictureVOPage.getRecords());

        return ResultUtil.success(searchVO);
    }

    // MySQL => 870ms
    @GetMapping("/all/async")
    public BaseResponse<SearchVO> searchAsync(@Validated SearchRequest searchRequest) {
        // 防止爬虫
        ThrowUtil.throwIf(searchRequest.getPageSize() > ONCE_MAX_PAGE_SIZE,
                StatusCode.PARAMS_ERROR, "一次获取资源过多");

        CompletableFuture<Page<ArticleVO>> articleTask = CompletableFuture.supplyAsync(
                () -> {
                    ArticleQueryRequest articleQueryRequest = new ArticleQueryRequest();
                    articleQueryRequest.setSearchText(searchRequest.getSearchText());
                    articleQueryRequest.setCurrent(searchRequest.getCurrent());
                    articleQueryRequest.setPageSize(searchRequest.getPageSize());
                    return articleService.queryArticleByPage(articleQueryRequest);
                });

        CompletableFuture<Page<PictureVO>> pictureTask = CompletableFuture.supplyAsync(() -> {
            PictureQueryRequest pictureQueryRequest = new PictureQueryRequest();
            pictureQueryRequest.setSearchText(searchRequest.getSearchText());
            pictureQueryRequest.setCurrent(searchRequest.getCurrent());
            pictureQueryRequest.setPageSize(searchRequest.getPageSize());
            return pictureService.queryPictureByPage(pictureQueryRequest);
        });

        CompletableFuture.allOf(articleTask, pictureTask).join();

        try {
            SearchVO searchVO = new SearchVO();
            searchVO.setArticleVOList(articleTask.get().getRecords());
            searchVO.setPictureVOList(pictureTask.get().getRecords());

            return ResultUtil.success(searchVO);
        } catch (InterruptedException | ExecutionException e) {
            throw new BusinessException(StatusCode.SYSTEM_ERROR, e.getMessage());
        }
    }


}
