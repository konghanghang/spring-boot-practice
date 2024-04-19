package com.tools.groovy;

import com.iminling.common.crypto.MD5Utils;
import groovy.lang.GroovyObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GroovyService implements InitializingBean {

    private List<GroovyScrip> groovyScrips = new ArrayList<>();

    private Map<String, GroovyScrip> groovyScripMap = new ConcurrentHashMap<>();

    // 动态刷新脚本
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

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

    public void refresh() {
        // 获取所有可用配置
        // groovyScrips.addAll()
        Set<@NonNull String> oldKey = new HashSet<>(groovyScripMap.keySet());
        Set<@NonNull String> oldScriptKey = new HashSet<>(GroovyEngineUtil.getGROOVY_OBJECT_MAP().keySet());
        groovyScrips.forEach(e -> {
            // 如果能删除，则代表是有效Key，否则为无效Key，会在后续清除
            oldKey.remove(e.getKey());
            // 更新缓存
            String md5 = MD5Utils.encode(e.getScript());
            oldScriptKey.remove(md5);
            if (GroovyEngineUtil.getGROOVY_OBJECT_MAP().containsKey(md5)) {
                log.info("{} 已加载，无需重复加载", e.getKey());
            } else {
                GroovyEngineUtil.getGroovyObject(e);
            }
        });
        // 清除无效Key
        oldKey.forEach(groovyScripMap::remove);
        oldScriptKey.forEach(GroovyEngineUtil.getGROOVY_OBJECT_MAP()::remove);
        log.info("配置刷新完毕");
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
        scheduler.scheduleAtFixedRate(this::refresh, 5, 30, TimeUnit.MINUTES);
    }
}
