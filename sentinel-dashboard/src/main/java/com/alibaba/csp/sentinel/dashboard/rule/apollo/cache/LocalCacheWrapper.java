/*
 * Copyright 2019 Mek Global Limited
 */

package com.alibaba.csp.sentinel.dashboard.rule.apollo.cache;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: yi.yang
 * @date: Created by yi.yang on 2020/3/19.
 * @Description: 本地缓存装饰对象，通过该对象可以获取异步对象
 */
public class LocalCacheWrapper<K, V> {
    private final Logger log = LoggerFactory.getLogger(LocalCacheWrapper.class);

    /**
     * 缓存Key
     */
    private K key;

    /**
     * 缓存值
     */
    private CompletableFuture<V> valueFuture;

    /**
     * 获取缓存值
     *
     * @param timeout  超时时间
     * @param timeUnit
     * @return
     */
    public V getCacheValue(long timeout, TimeUnit timeUnit) {
        try {
            return valueFuture.get(timeout, timeUnit);
        } catch (InterruptedException e) {
            log.error(" getCacheValue InterruptedException , parameter is [{}]", key, e);
        } catch (ExecutionException e) {
            log.error(" getCacheValue ExecutionException , parameter is [{}]", key, e);
        } catch (TimeoutException e) {
            log.error(" getCacheValue TimeoutException , parameter is [{}]", key, e);
        }

        return null;
    }

    public LocalCacheWrapper(K key, CompletableFuture<V> valueFuture) {
        this.key = key;
        this.valueFuture = valueFuture;
    }
}
