package com.youyi.searchhub.constant;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
public interface CrawlerConstant {

    String CRAWLER_ARTICLE_URL = "https://www.code-nav.cn/api/post/search/page/vo";

    String CRAWLER_ARTICLE_REQ_PARAMS = """
            {
                "current": %s,
                "pageSize": 5,
                "sortField": "createTime",
                "sortOrder": "descend",
                "category": "文章",
                "tags": [],
                "reviewStatus": 1
            }
            """;
}
