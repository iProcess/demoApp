package com.respect.cache;

/**
 * Created by sjc on 2017/10/22.
 */
public class TestCache {

    public static void main(String[] args) {
        LocalCache cache = LocalCache.createLocalCache();
        cache.put("test", 123);
        System.out.println(cache.get("test"));
    }
}
