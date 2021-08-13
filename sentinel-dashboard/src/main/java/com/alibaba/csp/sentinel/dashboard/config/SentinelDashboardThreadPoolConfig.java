/*
 * Copyright 2019 Mek Global Limited
 */

package com.alibaba.csp.sentinel.dashboard.config;

import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


/**
 * @author darren
 */
@Configuration
public class SentinelDashboardThreadPoolConfig {

    @Value("${kucoin.tradePool.keepAliveSeconds:100}")
    private int keepAliveSeconds;

    @Value("${kucoin.localCacheOperationPool.corePoolSize:10}")
    private int localCacheOperationPoolCorePoolSize;

    @Value("${kucoin.localCacheOperationPool.maxPoolSize:20}")
    private int localCacheOperationMaxPoolSize;

    @Value("${kucoin.localCacheOperationPool.queueCapacity:65535}")
    private int localCacheOperationQueueCapacity;


    /**
     * 本地线程操作线程池
     */
    private static final String LOCAL_CACHE_POOL_NAME = "localCacheOperationPool";
    public static final String LOCAL_CACHE_POOL_EXECUTOR = "localCacheOperationPoolExecutor";

    /**
     * 订单本地缓存操作线程池
     */
    @Bean(LOCAL_CACHE_POOL_EXECUTOR)
    public ThreadPoolTaskExecutor localCacheOperationPoolExecutor() {

        ThreadPoolTaskExecutor workThreadPool = new ThreadPoolTaskExecutor();
        workThreadPool.setKeepAliveSeconds(keepAliveSeconds);
        workThreadPool.setAllowCoreThreadTimeOut(true);
        //设置核心线程数
        workThreadPool.setCorePoolSize(localCacheOperationPoolCorePoolSize);
        //设置最大线程数
        workThreadPool.setMaxPoolSize(localCacheOperationMaxPoolSize);
        //线程池所使用的缓冲队列
        workThreadPool.setQueueCapacity(localCacheOperationQueueCapacity);
        //  线程名称前缀
        workThreadPool.setThreadNamePrefix(LOCAL_CACHE_POOL_NAME);
        //队列满，线程被拒绝执行策略,
        workThreadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        workThreadPool.initialize();
        return workThreadPool;
    }

}
