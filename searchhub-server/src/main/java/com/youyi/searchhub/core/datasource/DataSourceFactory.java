package com.youyi.searchhub.core.datasource;

import com.youyi.searchhub.model.enums.SearchType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Component
public class DataSourceFactory implements InitializingBean, ApplicationContextAware {

    private static final Map<Enum<SearchType>, DataSource<?>> DATA_SOURCE_REGISTRY = new ConcurrentHashMap<>(
            16);
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        @SuppressWarnings("rawtypes") Map<String, DataSource> dataSourceMap = applicationContext.getBeansOfType(
                DataSource.class);
        dataSourceMap.forEach(
                (k, v) -> DATA_SOURCE_REGISTRY.put(SearchType.resolve(v.getType()), v));
    }

    public DataSource<?> getDataSource(Enum<SearchType> type) {
        return DATA_SOURCE_REGISTRY.get(type);
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }
}
