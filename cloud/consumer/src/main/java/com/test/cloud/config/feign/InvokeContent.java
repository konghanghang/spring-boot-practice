package com.test.cloud.config.feign;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 保存需要使用的数据
 * @author yslao@outlook.com
 * @since 2021/3/26
 */
public class InvokeContent {

    private Map<String, String[]> parameterNamesMap = new ConcurrentHashMap<>();

    public String[] getParameterNames(String key) {
        return this.parameterNamesMap.get(key);
    }

    public void setParametersName(String key, String[] parameterNames) {
        this.parameterNamesMap.put(key, parameterNames);
    }
}
