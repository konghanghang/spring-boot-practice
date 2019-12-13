package com.test.es.service.impl;

import com.test.es.model.MappingFieldInfo;
import com.test.es.service.IEsService;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Service
public class EsServiceImpl implements IEsService {

    @Resource
    private TransportClient client;

    private final String dateFormat = "yyyy-MM-dd HH:mm:ss.SSS || date_time || strict_date_time";

    @Override
    public boolean existIndex(String index) {
        IndicesExistsResponse indicesExistsResponse = client.admin().indices().exists(new IndicesExistsRequest(index)).actionGet();
        return indicesExistsResponse.isExists();
    }

    @Override
    public boolean deleteIndex(String index) {
        DeleteIndexRequestBuilder deleteIndexRequestBuilder = client.admin().indices().prepareDelete(index);
        AcknowledgedResponse acknowledgedResponse = deleteIndexRequestBuilder.execute().actionGet();
        return acknowledgedResponse.isAcknowledged();
    }

    @Override
    public boolean createIndexWithCommonMapping(String index, String type) throws IOException {
        CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(index);
        XContentBuilder properties = XContentFactory.jsonBuilder().startObject().startObject("properties");
        properties.startObject("crateTime").field("type", "date")
                .field("format",dateFormat).endObject().endObject().endObject();
        createIndexRequestBuilder.addMapping(type, properties);
        CreateIndexResponse createIndexResponse = createIndexRequestBuilder.execute().actionGet();
        return createIndexResponse.isAcknowledged();
    }

    @Override
    public boolean createIndexWithCustomMapping(String index, String type, List<MappingFieldInfo> list) throws IOException {
        CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(index);
        XContentBuilder properties = XContentFactory.jsonBuilder().startObject().startObject("properties");
        for (MappingFieldInfo info : list) {
            properties.startObject(info.getName()).field("type", info.getType());
            if (!StringUtils.isEmpty(info.getFormatType())) {
                properties.field("format",info.getFormatType());
            }
            properties.endObject();
        }
        properties.endObject().endObject();
        createIndexRequestBuilder.addMapping(type, properties);
        CreateIndexResponse createIndexResponse = createIndexRequestBuilder.execute().actionGet();
        return createIndexResponse.isAcknowledged();
    }
}
