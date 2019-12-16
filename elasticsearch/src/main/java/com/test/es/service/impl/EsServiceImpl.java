package com.test.es.service.impl;

import com.test.es.model.EsPage;
import com.test.es.model.MappingFieldInfo;
import com.test.es.service.IEsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EsServiceImpl implements IEsService {

    private final Logger logger = LoggerFactory.getLogger(EsServiceImpl.class);

    @Resource
    private TransportClient client;

    private final String dateFormat = "yyyy-MM-dd HH:mm:ss.SSS || date_time || strict_date_time";
    private final IndicesOptions IGNORE = IndicesOptions.fromOptions(true, true, true, false);

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
        properties.startObject("createTime").field("type", "date")
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

    @Override
    public boolean insertRecord(Map<String, Object> record, String index) {
        return insertRecord(record, index, "doc");
    }

    @Override
    public boolean insertRecord(Map<String, Object> record, String index, String type) {
        IndexResponse indexResponse = client.prepareIndex(index, type).setSource(record).get();
        RestStatus status = indexResponse.status();
        return "ok".equalsIgnoreCase(status.name());
    }

    @Override
    public boolean insertRecordBulk(List<Map<String, Object>> records, String index) {
        return insertRecordBulk(records, index, "doc");
    }

    @Override
    public boolean insertRecordBulk(List<Map<String, Object>> records, String index, String type) {
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        records.stream().forEach(record -> bulkRequestBuilder.add(client.prepareIndex(index, type).setSource(record)));
        BulkResponse response = bulkRequestBuilder.execute().actionGet();
        RestStatus status = response.status();
        return "ok".equalsIgnoreCase(status.name());
    }

    @Override
    public boolean deleteRecordById(String index, String type, String id) {
        DeleteResponse deleteResponse = client.prepareDelete(index, type, id).get();
        RestStatus status = deleteResponse.status();
        return "ok".equalsIgnoreCase(status.name());
    }

    @Override
    public List<Map<String, Object>> query(List<String> index, String type, Map<String, Object> condition) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (condition == null) {
            boolQueryBuilder.must(new MatchAllQueryBuilder());
        } else {
            for (Map.Entry<String, Object> entry : condition.entrySet()) {
                boolQueryBuilder.must(new WildcardQueryBuilder(entry.getKey(), "*" + entry.getValue() + "*"));
            }
        }
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index.toArray(new String[]{})).setTypes(type)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(boolQueryBuilder)
                .setIndicesOptions(IGNORE)
                // .setFrom(0) pageSize * (pageNum - 1)
                // .setSize(10) pageSize
                .addSort("createTime", SortOrder.DESC);
        logger.info("search condition:{}", searchRequestBuilder);
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        SearchHits hits = searchResponse.getHits();
        long totalHits = hits.getTotalHits();
        SearchHit[] searchHits = hits.getHits();
        List<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            String id = hit.getId();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            sourceAsMap.put("_id", id);
            list.add(sourceAsMap);
        }
        return list;
    }

    /**
     * 使用分词查询,并分页
     *
     * @param index          索引名称
     * @param type           类型名称,可传入多个type逗号分隔
     * @param startPage      当前页
     * @param pageSize       每页显示条数
     * @param query          查询条件
     * @param fields         需要显示的字段，逗号分隔（缺省为全部字段）
     * @param sortField      排序字段
     * @param highlightField 高亮字段
     * @return
     */
    private EsPage searchDataPage(String index, String type, int startPage, int pageSize, QueryBuilder query, String fields, String sortField, String highlightField) {
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        if (StringUtils.isNotEmpty(type)) {
            searchRequestBuilder.setTypes(type.split(","));
        }
        searchRequestBuilder.setSearchType(SearchType.QUERY_THEN_FETCH);

        // 需要显示的字段，逗号分隔（缺省为全部字段）
        if (StringUtils.isNotEmpty(fields)) {
            searchRequestBuilder.setFetchSource(fields.split(","), null);
        }

        //排序字段
        if (StringUtils.isNotEmpty(sortField)) {
            searchRequestBuilder.addSort(sortField, SortOrder.DESC);
        }

        // 高亮（xxx=111,aaa=222）
        if (StringUtils.isNotEmpty(highlightField)) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();

            //highlightBuilder.preTags("<span style='color:red' >");//设置前缀
            //highlightBuilder.postTags("</span>");//设置后缀

            // 设置高亮字段
            highlightBuilder.field(highlightField);
            searchRequestBuilder.highlighter(highlightBuilder);
        }

        // searchRequestBuilder.setQuery(QueryBuilders.matchAllQuery());
        searchRequestBuilder.setQuery(query);

        // 分页应用
        searchRequestBuilder.setFrom(startPage).setSize(pageSize);
        // searchRequestBuilder.setSize(pageSize);

        // 设置是否按查询匹配度排序
        searchRequestBuilder.setExplain(true);

        // 打印的内容 可以在 Elasticsearch head 和 Kibana  上执行查询
        logger.info("query condition:\n{}", searchRequestBuilder);

        // 执行搜索,返回搜索响应信息
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

        long totalHits = searchResponse.getHits().totalHits;
        long length = searchResponse.getHits().getHits().length;

        logger.info("共查询到[{}]条数据,处理数据条数[{}]", totalHits, length);

        if (searchResponse.status().getStatus() == HttpStatus.SC_OK) {
            // 解析对象
            List<Map<String, Object>> sourceList = setSearchResponse(searchResponse, highlightField);
            return new EsPage(startPage, pageSize, (int) totalHits, sourceList);
        }
        return null;
    }

    /**
     * 高亮结果集 特殊处理
     *
     * @param searchResponse
     * @param highlightField
     */
    private List<Map<String, Object>> setSearchResponse(SearchResponse searchResponse, String highlightField) {
        List<Map<String, Object>> sourceList = new ArrayList<Map<String, Object>>();
        StringBuffer stringBuffer = new StringBuffer();

        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            searchHit.getSourceAsMap().put("id", searchHit.getId());
            if (StringUtils.isNotEmpty(highlightField)) {
                System.out.println("遍历 高亮结果集，覆盖 正常结果集" + searchHit.getSourceAsMap());
                Text[] text = searchHit.getHighlightFields().get(highlightField).getFragments();
                if (text != null) {
                    for (Text str : text) {
                        stringBuffer.append(str.string());
                    }
                    //遍历 高亮结果集，覆盖 正常结果集
                    searchHit.getSourceAsMap().put(highlightField, stringBuffer.toString());
                }
            }
            sourceList.add(searchHit.getSourceAsMap());
        }
        return sourceList;
    }
}
