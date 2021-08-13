/*
 * Copyright 2019 Mek Global Limited
 */

package com.alibaba.csp.sentinel.dashboard.rule.apollo.cache;

import com.alibaba.csp.sentinel.dashboard.config.SentinelDashboardThreadPoolConfig;
import com.alibaba.csp.sentinel.dashboard.rule.apollo.repository.ApolloRepository;
import com.ctrip.framework.apollo.openapi.dto.OpenNamespaceDTO;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;


/**
 * @author darren
 */
@Component
public class ApolloConfigLocalCache implements LocalCache<String, List<OpenNamespaceDTO>> {
    private final Logger log = LoggerFactory.getLogger(LocalCacheWrapper.class);

    private AsyncLoadingCache<String, List<OpenNamespaceDTO>> symbolCache;

    @Value("${kucoin.sentinelApolloLocalCache.initialCapacity:100}")
    private int initialCapacity;

    @Value("${kucoin.sentinelApolloLocalCache.maximumSize:500}")
    private int maximumSize;

    @Value("${kucoin.sentinelApolloLocalCache.expireAfterWrite:3600}")
    private int expireAfterWrite;

    @Value("${kucoin.sentinelApolloLocalCache.refreshAfterWrite:600}")
    private int refreshAfterWrite;

    @Resource(name = SentinelDashboardThreadPoolConfig.LOCAL_CACHE_POOL_EXECUTOR)
    private ThreadPoolTaskExecutor localCacheOperationPool;

    @Autowired
    private ApolloRepository apolloRepository;


    @PostConstruct
    public void buildCache() {
        symbolCache = Caffeine.newBuilder().initialCapacity(initialCapacity).maximumSize(maximumSize)
                .executor(localCacheOperationPool)
                .expireAfterWrite(expireAfterWrite, TimeUnit.SECONDS).refreshAfterWrite(refreshAfterWrite, TimeUnit.SECONDS)
                .buildAsync((key, executor) -> CompletableFuture.supplyAsync(()
                        -> buildConfig(key), executor));
    }

    private List<OpenNamespaceDTO> buildConfig(String key) {
        return apolloRepository.getNamespaces(key);
    }

    @Override
    public List<OpenNamespaceDTO> getCacheValue(String key) {
        return new LocalCacheWrapper<>(key, symbolCache.get(key))
                .getCacheValue(15, TimeUnit.SECONDS);
    }

    @Override
    public LocalCacheWrapper<String, List<OpenNamespaceDTO>> getCacheWrapper(String key) {
        return new LocalCacheWrapper(key, symbolCache.get(key));
    }

    @Override
    public void refreshCacheValue(String key) {
        List<OpenNamespaceDTO> list = apolloRepository.getNamespaces(key);
        if (!CollectionUtils.isEmpty(list)) {
            symbolCache.synchronous().put(key, list);
        } else {
            log.warn(" SymbolLocalCache get value is null, key is {} ", key);
        }
    }

    @Override
    public String getLocalCacheType() {
        return "APOLLO_NAMESPACE_LOCAL_CACHE";
    }

    @Override
    public void deleteCache(String key) {
        symbolCache.synchronous().invalidate(key);
    }

    @Override
    public void deleteAllCache() {
        symbolCache.synchronous().invalidateAll();
    }
}
