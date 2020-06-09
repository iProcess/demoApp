package com.respect.cache;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sjc on 2017/10/22.
 */
public class LocalCache implements Serializable {

    private static ConcurrentHashMap<String, CacheValue> cache = new ConcurrentHashMap<String, CacheValue>();
    private static List<String> removeList = new ArrayList<String>();
    private static final long serialVersionUID = -3273454956563078833L;
    private static boolean isRun = false;// 用于防止定时任务叠加执行
    private static Timer timer;
    private static LocalCache localCache;
    private static byte[] lock = new byte[0];
    /**
     * 定时器清理间隔（默认60秒钟执行一次）
     */
    private static Long DEFAULT_TASK_DELAY = 60000L;

    /**
     * 存储有效期,默认存储30分钟
     */
//    private static Long DEFAULT_TIMEOUT = 60000 * 30L;

    private static Long DEFAULT_TIMEOUT = 30000L;

    private LocalCache() {
    }

    public static void setDefaultTimeout(Long timeout) {
        if (timeout > 0) {
            DEFAULT_TIMEOUT = timeout;
        }
    }

    public static void setDefaultClearDelay(Long delay) {
        if (delay >= 500) {
            DEFAULT_TASK_DELAY = delay;
        }
    }

    public static Long getDefaultTimeout() {
        return DEFAULT_TIMEOUT;
    }

    public static Long getDefaultClearDelay() {
        return DEFAULT_TASK_DELAY;
    }

    private static void clearCache() {
        if (!isRun) {
            synchronized (lock) {
                try {
                    if (removeList.isEmpty() == false) {
                        removeList.clear();
                    }
                    isRun = true;
                    long start = System.currentTimeMillis();
                    if (!cache.isEmpty()) {
                        Iterator<Map.Entry<String, CacheValue>> i = cache.entrySet().iterator();
                        while (i.hasNext()) {
                            Map.Entry<String, CacheValue> e = i.next();
                            if (e.getValue() == null) {
                                removeList.add(e.getKey());
                            } else {
                                if (e.getValue().isTimeout()) {
                                    removeList.add(e.getKey());
                                }
                            }
                        }

                        for (String key : removeList) {
                            cache.remove(key);
                        }
                    }
                    int clearSize = removeList.size();
                    removeList.clear();
                    System.out.println("本次清理：" + clearSize + ",当前剩余：" + cache.size() + ",清理用时：" + (System.currentTimeMillis() - start) + "毫秒");
                } finally {
                    isRun = false;
                }
            }

        }
    }

    public static LocalCache createLocalCache() {
        synchronized (lock) {
            if (localCache == null) {
                localCache = new LocalCache();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        clearCache();
                    }
                }, 0, DEFAULT_TASK_DELAY);
            }
        }
        return localCache;
    }

    public boolean containsKey(String key) {
        return get(key) != null;
    }

    public Object get(String key) {
        if (key != null) {
            Object obj = null;
            if (cache.containsKey(key)) {
                obj = cache.get(key);
            }
            if (obj != null) {
                CacheValue cvalue = (CacheValue) obj;
                return cvalue.getValue();
            }
        }
        return null;
    }

    /**
     * 默认缓存 30分钟
     */
    public Object put(String key, Object value) {
        return put(key, value, DEFAULT_TIMEOUT);
    }

    /**
     *
     * @param key
     * @param value
     * @param timeout
     *            单位默认是秒,传入小于0的值则使用默认值(30分钟)，传入0 则永久有效(应用重启后失效,目前没有实现存盘)
     * @return
     */
    public Object put(String key, Object value, Long timeout) {
        if (key == null) {
            return null;
        }
        CacheValue cvalue = new CacheValue();
        cvalue.setKey(key);
        if (timeout > 0) {
            cvalue.setTimeout(timeout * 1000L);
        } else if (timeout < 0) {
            cvalue.setTimeout(DEFAULT_TIMEOUT);
        } else {
            cvalue.setTimeout(0L);
        }
        cvalue.setValue(value);
        return cache.put(key, cvalue);
    }

    public void remove(String key) {
        cache.remove(key);
    }

    public long size() {
        return cache.size();
    }

}
