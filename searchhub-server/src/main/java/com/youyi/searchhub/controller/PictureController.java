package com.youyi.searchhub.controller;

import static com.youyi.searchhub.constant.CommonConstant.ONCE_MAX_PAGE_SIZE;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youyi.searchhub.common.BaseResponse;
import com.youyi.searchhub.common.StatusCode;
import com.youyi.searchhub.model.dto.PictureQueryRequest;
import com.youyi.searchhub.model.vo.PictureVO;
import com.youyi.searchhub.service.PictureService;
import com.youyi.searchhub.util.ResultUtil;
import com.youyi.searchhub.util.ThrowUtil;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 图片接口
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Slf4j
@RestController
@RequestMapping("/picture")
public class PictureController {

    @Resource
    private PictureService pictureService;

    @GetMapping("/query/page")
    public BaseResponse<Page<PictureVO>> queryArticleByPage(
            @Validated PictureQueryRequest pictureQueryRequest) {
        // 防止爬虫
        ThrowUtil.throwIf(pictureQueryRequest.getPageSize() > ONCE_MAX_PAGE_SIZE,
                StatusCode.PARAMS_ERROR, "一次获取资源过多");
        Page<PictureVO> pictureVOPage = pictureService.queryPictureByPage(pictureQueryRequest);
        return ResultUtil.success(pictureVOPage);
    }
}
