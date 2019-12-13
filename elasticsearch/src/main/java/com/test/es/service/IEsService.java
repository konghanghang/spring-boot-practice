package com.test.es.service;

import com.test.es.model.MappingFieldInfo;

import java.io.IOException;
import java.util.List;

public interface IEsService {

    boolean existIndex(String index);

    boolean deleteIndex(String index);

    boolean createIndexWithCommonMapping(String index, String type) throws IOException;

    boolean createIndexWithCustomMapping(String index, String type, List<MappingFieldInfo> list) throws IOException;

}
