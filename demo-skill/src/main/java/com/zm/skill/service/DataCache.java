package com.zm.skill.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/11/26.
 */
public class DataCache {

    public final static Cache<String, Object> cached = CacheBuilder.newBuilder()
            .initialCapacity(50)  // 初始大小
            .concurrencyLevel(10) // 并发线程
            .expireAfterWrite(1800, TimeUnit.SECONDS) // 过期时间
            .build();

    public final static String INDEX_KEY = "_INDEX";

    public final static String LIST_KEY = "_LIST";
}
