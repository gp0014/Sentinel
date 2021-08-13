/*
 * Copyright 2019 Mek Global Limited
 */

package com.alibaba.csp.sentinel.dashboard.rule.apollo.cache;


/**
 * @author: yi.yang
 * @date: Created by yi.yang on 2020/3/9.
 * @Description:
 */
public interface LocalCache<K, V> {

    /**
     * 获取本地缓存
     *
     * @param key
     * @return
     */
    V getCacheValue(K key);

    /**
     * 主动刷新本地缓存
     *
     * @param key
     */
    void refreshCacheValue(K key);

    /**
     * 删除某一个本地缓存
     *
     * @param key
     */
    void deleteCache(K key);

    /**
     * 清空所有本地缓存
     *
     * @param
     */
    void deleteAllCache();

    /**
     * 获取缓存包装对象
     *
     * @param key
     * @return
     */
    LocalCacheWrapper<K, V> getCacheWrapper(K key);

    /**
     * 获取本地缓存类型
     *
     * @return
     */
    String getLocalCacheType();
}
