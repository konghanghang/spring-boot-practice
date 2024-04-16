package com.tools.groovy;

import groovy.lang.GroovyObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class GroovyService implements InitializingBean {

    private List<GroovyScrip> groovyScrips = new ArrayList<>();

    private Map<String, GroovyScrip> groovyScripMap = new ConcurrentHashMap<>();

    public String evalScript(String key) {
        GroovyScrip groovyScrip = groovyScripMap.get(key);
        if (groovyScrip == null) {
            return null;
        }
        GroovyObject groovyObject = GroovyEngineUtil.getGroovyObject(groovyScrip);
        if (Objects.isNull(groovyObject)) {
            return null;
        }
        String param1 = "", param2 = "";
        return GroovyEngineUtil.evalScript(groovyObject, param1, param2);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 查询所有的需要添加的groovy脚本
        // groovyScrips.add(null);
        // 仅仅初始化key和脚本的对应关系，提升启动速度
        groovyScrips.forEach(groovyScrip -> {
            if (groovyScrip != null) {
                groovyScripMap.put(groovyScrip.getKey(), groovyScrip);
            }
        });
    }
}
