package com.youyi.searchhub.model.enums;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Getter
public enum SearchType {


    ARTICLE("article", "文章"),
    PICTURE("picture", "图片"),
    BILI_VIDEO("bili_video", "B站视频"),
    ALL("all", "全部"),
    ;

    final String type;

    final String desc;

    SearchType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static SearchType resolve(String type) {
        if (StrUtil.isBlank(type)) {
            throw new IllegalArgumentException("type is null");
        }
        for (SearchType searchType : SearchType.values()) {
            if (searchType.getType().equals(type)) {
                return searchType;
            }
        }
        throw new IllegalArgumentException("type is not exist");
    }
}
