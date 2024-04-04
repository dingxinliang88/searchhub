package com.youyi.searchhub.exception;

import com.youyi.searchhub.common.BaseResponse;
import com.youyi.searchhub.common.StatusCode;
import com.youyi.searchhub.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理类
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException businessException) {
        log.error("BusinessException: ", businessException);
        return ResultUtil.error(businessException.getCode(), businessException.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse<?> otherExceptionHandler(Exception e) {
        log.error("Exception: ", e);
        return ResultUtil.error(StatusCode.SYSTEM_ERROR);
    }
}
