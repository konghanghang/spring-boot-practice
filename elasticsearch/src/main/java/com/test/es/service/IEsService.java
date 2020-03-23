package com.test.es.service;

import com.test.es.model.MappingFieldInfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IEsService {

    boolean existIndex(String index);

    boolean deleteIndex(String index);

    boolean createIndexWithCommonMapping(String index, String type) throws IOException;

    boolean createIndexWithCustomMapping(String index, String type, List<MappingFieldInfo> list) throws IOException;

    boolean insertRecord(Map<String, Object> record, String index);

    boolean insertRecord(Map<String, Object> record, String index, String type);

    boolean insertRecordBulk(List<Map<String, Object>> records, String index);

    boolean insertRecordBulk(List<Map<String, Object>> records, String index, String type);

    boolean deleteRecordById(String index, String type, String id);

    List<Map<String, Object>> query(List<String> index, String type, Map<String, Object> condition);

    Object getIndexMappings();

}
