package com.youyi.searchhub.util;

import com.youyi.searchhub.common.StatusCode;
import com.youyi.searchhub.exception.BusinessException;

/**
 * 抛出异常工具类
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
public class ThrowUtil {

    /**
     * 条件成立，抛出异常
     *
     * @param condition        条件
     * @param runtimeException 异常
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition  条件
     * @param statusCode 错误码
     */
    public static void throwIf(boolean condition, StatusCode statusCode) {
        throwIf(condition, new BusinessException(statusCode));
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition  条件
     * @param statusCode 错误码
     * @param message    错误信息
     */
    public static void throwIf(boolean condition, StatusCode statusCode, String message) {
        throwIf(condition, new BusinessException(statusCode, message));
    }

}
