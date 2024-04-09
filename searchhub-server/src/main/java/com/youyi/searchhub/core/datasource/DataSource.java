package com.youyi.searchhub.core.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 数据源适配器
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
public interface DataSource<T> {

    /**
     * 适配器接口，任何接入系统的数据，都必须能够根据关键词搜索，并且支持分页搜索
     *
     * @param searchText 搜索文本，用于查询条件。
     * @param current    当前页码，用于分页查询。
     * @param pageSize   每页显示的记录数，用于分页查询。
     * @return Page<T> 返回搜索结果的分页信息，包含当前页的记录列表。
     */
    Page<T> doSearch(String searchText, long current, long pageSize);

    String getType();

}
