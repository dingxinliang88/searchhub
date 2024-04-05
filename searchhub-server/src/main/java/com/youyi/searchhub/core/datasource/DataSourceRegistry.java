package com.youyi.searchhub.core.datasource;

import com.youyi.searchhub.model.enums.SearchType;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据源注册器
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
public class DataSourceRegistry {


    private static final Map<Enum<SearchType>, DataSource<?>> DATA_SOURCE_REGISTRY = new ConcurrentHashMap<>(
            16);

    public static void registry(Enum<SearchType> type, DataSource<?> dataSource) {
        DATA_SOURCE_REGISTRY.put(type, dataSource);
    }

    public static DataSource<?> getDataSource(Enum<SearchType> type) {
        DataSource<?> dataSource = DATA_SOURCE_REGISTRY.get(type);
        if (Objects.isNull(dataSource)) {
            throw new NullPointerException("未找到对应的数据源");
        }
        return dataSource;
    }
}
