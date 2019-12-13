package com.test.es.service.impl;

import com.test.es.model.MappingFieldInfo;
import com.test.es.service.IEsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
}
