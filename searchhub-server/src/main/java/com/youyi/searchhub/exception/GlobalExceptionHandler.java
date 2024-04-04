package com.youyi.searchhub.exception;

import com.youyi.searchhub.common.BaseResponse;
import com.youyi.searchhub.common.StatusCode;
import com.youyi.searchhub.util.ResultUtil;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    private static final String BAD_REQUEST_MSG = "客户端请求参数错误, ";

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException businessException) {
        log.error("BusinessException: ", businessException);
        return ResultUtil.error(businessException.getCode(), businessException.getMessage());
    }

    // <1> 处理 form data方式调用接口校验失败抛出的异常
    @ExceptionHandler(BindException.class)
    public BaseResponse<?> bindExceptionHandler(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return ResultUtil.error(StatusCode.BAD_REQUEST.getCode(),
                BAD_REQUEST_MSG + String.join(" ,", collect));
    }

    // <2> 处理 json 请求体调用接口校验失败抛出的异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<?> methodArgumentNotValidExceptionHandler(
            MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return ResultUtil.error(StatusCode.BAD_REQUEST.getCode(),
                BAD_REQUEST_MSG + String.join(" ,", collect));
    }

    // <3> 处理单个参数校验失败抛出的异常
    @ExceptionHandler(ConstraintViolationException.class)
    public BaseResponse<?> constraintViolationExceptionHandler(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        List<String> collect = constraintViolations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        return ResultUtil.error(StatusCode.BAD_REQUEST.getCode(),
                BAD_REQUEST_MSG + String.join(" ,", collect));
    }


    @ExceptionHandler(Exception.class)
    public BaseResponse<?> otherExceptionHandler(Exception e) {
        log.error("Exception: ", e);
        return ResultUtil.error(StatusCode.SYSTEM_ERROR);
    }
}
