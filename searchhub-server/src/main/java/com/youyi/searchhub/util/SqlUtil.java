package com.youyi.searchhub.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Sql工具
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
public class SqlUtil {

    /**
     * 校验排序字段是否合法，防止sql注入
     *
     * @param sortField 排序字段
     * @return true - 合法
     */
    public static boolean validSortField(String sortField) {
        // 非空 && 不包含特殊字符
        return !StringUtils.isBlank(sortField)
                && !StringUtils.containsAny(sortField, "=", "(", ")", " ");
    }
}
