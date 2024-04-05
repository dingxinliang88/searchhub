package com.youyi.searchhub.controller;

import static com.youyi.searchhub.constant.CommonConstant.ONCE_MAX_PAGE_SIZE;

import com.youyi.searchhub.common.BaseResponse;
import com.youyi.searchhub.common.StatusCode;
import com.youyi.searchhub.core.SearchFacade;
import com.youyi.searchhub.model.dto.SearchRequest;
import com.youyi.searchhub.model.vo.SearchVO;
import com.youyi.searchhub.util.ResultUtil;
import com.youyi.searchhub.util.ThrowUtil;
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
    private SearchFacade searchFacade;

    @GetMapping("/all")
    public BaseResponse<SearchVO> search(@Validated SearchRequest searchRequest) {
        // 防止爬虫
        ThrowUtil.throwIf(searchRequest.getPageSize() > ONCE_MAX_PAGE_SIZE,
                StatusCode.PARAMS_ERROR, "一次获取资源过多");

        return ResultUtil.success(searchFacade.doSearch(searchRequest));
    }
}
