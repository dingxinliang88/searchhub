package com.youyi.searchhub.exception;

import com.youyi.searchhub.common.StatusCode;
import lombok.Getter;

/**
 * 自定义业务异常类
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 状态码
     */
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(StatusCode statusCode) {
        this(statusCode.getCode(), statusCode.getMessage());
    }

    public BusinessException(StatusCode statusCode, String message) {
        this(statusCode.getCode(), message);
    }

}
