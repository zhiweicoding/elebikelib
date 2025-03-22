package xyz.zhiweicoding.bike.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.concurrent.TimeUnit;



/**
 * @Created by zhiwei on 2022/4/4.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        // 自定义 CacheManager 支持按名称加载不同配置的缓存
        CustomCaffeineCacheManager cacheManager = new CustomCaffeineCacheManager();
        cacheManager.setCaches(Arrays.asList(
                buildCache("60s", 60, TimeUnit.SECONDS),
                buildCache("30s", 30, TimeUnit.SECONDS),
                buildCache("3m", 3, TimeUnit.MINUTES),
                buildCache("15m", 15, TimeUnit.MINUTES),
                buildCache("30m", 30, TimeUnit.MINUTES),
                buildCache("1h", 1, TimeUnit.HOURS),
                buildCache("24h", 24, TimeUnit.HOURS),
                buildCache("30d", 30, TimeUnit.DAYS),
                // default 配置，默认过期 1 小时，可根据业务调整
                buildCache("default", 1, TimeUnit.HOURS)
        ));
        return cacheManager;
    }

    private CaffeineCache buildCache(String cacheName, long duration, TimeUnit timeUnit) {
        return new CaffeineCache(cacheName,
                Caffeine.newBuilder()
                        .expireAfterWrite(duration, timeUnit)
                        .maximumSize(1000)
                        .build());
    }

    @Bean
    public KeyGenerator cacheJsonKeyGenerator() {
        return new JsonKeyGenerator();
    }
}

/**
 * 自定义 CacheManager，支持根据预先定义的 Cache 集合来返回对应 Cache 对象
 */
class CustomCaffeineCacheManager implements CacheManager {

    private Collection<? extends Cache> caches = Collections.emptyList();

    @Override
    public Cache getCache(String name) {
        for (Cache cache : caches) {
            if (cache.getName().equals(name)) {
                return cache;
            }
        }
        return null;
    }

    @Override
    public Collection<String> getCacheNames() {
        List<String> names = new ArrayList<>();
        for (Cache cache : caches) {
            names.add(cache.getName());
        }
        return names;
    }

    public void setCaches(Collection<? extends Cache> caches) {
        this.caches = caches;
    }
}