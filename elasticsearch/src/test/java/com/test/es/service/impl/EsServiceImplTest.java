package com.test.es.service.impl;

import com.alibaba.fastjson.JSON;
import com.test.es.model.MappingFieldInfo;
import com.test.es.service.IEsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsServiceImplTest {

    @Resource
    private IEsService esService;

    @Test
    public void existIndex() {
        boolean common_20191213 = esService.existIndex("common_20191213");
        System.out.println(common_20191213);
    }

    @Test
    public void deleteIndex() {
        boolean common_20191213 = esService.deleteIndex("common_20191213");
        System.out.println(common_20191213);
    }

    @Test
    public void createIndexWithCommonMapping() throws IOException {
        boolean indexWithCommonMapping = esService.createIndexWithCommonMapping("common_20191213", "doc");
        System.out.println(indexWithCommonMapping);
    }

    @Test
    public void createIndexWithCustomMapping() throws IOException {
        List<MappingFieldInfo> list = new ArrayList<>();
        MappingFieldInfo fieldInfo = new MappingFieldInfo();
        fieldInfo.setName("id").setType("long");
        list.add(fieldInfo);
        MappingFieldInfo fieldInfo1 = new MappingFieldInfo();
        fieldInfo1.setName("name").setType("keyword");
        list.add(fieldInfo1);
        MappingFieldInfo fieldInfo2 = new MappingFieldInfo();
        fieldInfo2.setName("createTime").setType("date").setFormatType("yyyy-MM-dd HH:mm:ss.SSS || date_time || strict_date_time");
        list.add(fieldInfo2);
        boolean indexWithCustomMapping = esService.createIndexWithCustomMapping("custom_20191213", "doc", list);
        System.out.println(indexWithCustomMapping);
    }

    @Test
    public void insertRecord() {
        // es_test
        LocalDateTime local = LocalDateTime.now();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", i + 1);
            map.put("name", "admin" + i);
            map.put("age", i + 1);
            map.put("createTime", local.minusMinutes(i + 1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
            esService.insertRecord(map, "es_test", "doc");
        }
    }

    @Test
    public void insertRecordBulk() {
        List<Map<String, Object>> list = new ArrayList<>();
        LocalDateTime local = LocalDateTime.now();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", i + 1);
            map.put("name", "admin" + i);
            map.put("age", i + 1);
            map.put("createTime", local.minusMinutes(i + 1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
            list.add(map);
        }
        System.out.println(esService.insertRecordBulk(list, "es_test"));
    }

    @Test
    public void deleteRecordById() {
        List<String> indexs = new ArrayList<>();
        indexs.add("es_test");
        List<Map<String, Object>> doc = esService.query(indexs, "doc", null);
        doc.stream().forEach(c -> esService.deleteRecordById("es_test", "doc", c.get("_id").toString()));
    }

    @Test
    public void query() {
        List<String> indexs = new ArrayList<>();
        indexs.add("es_test");
        List<Map<String, Object>> doc = esService.query(indexs, "doc", null);
        doc.stream().forEach(c -> System.out.println(JSON.toJSONString(c)));
    }
}
