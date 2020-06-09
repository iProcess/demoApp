package com.respect.cache;

import java.io.Serializable;

/**
 * Created by sjc on 2017/10/22.
 */
public class CacheValue implements Serializable {

    private static final long serialVersionUID = 800800964682488492L;
    private String key;
    private Long createTime;
    private Long timeout;
    private Object value;

    public CacheValue() {
        createTime = System.currentTimeMillis() - 100;// 将创建时间提前100毫秒，防止定时回收任务因上一次执行时间过长导致回收滞后
    }

    /**
     * 判断对象是否已经失效
     *
     * @return 返回 true 失效 false 没有失效
     */
    public boolean isTimeout() {
        boolean ret = true;
        if (value != null) {
            if (timeout == 0) {
                return false;
            } else {
                ret = System.currentTimeMillis() - createTime >= timeout;
                if (ret) {
                    value = null;
                }
            }
        }
        return ret;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public Object getValue() {
        isTimeout();
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
