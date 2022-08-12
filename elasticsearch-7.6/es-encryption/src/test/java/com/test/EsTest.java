package com.test;

import com.iminling.common.json.JsonUtil;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.annotation.Resource;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.stats.IndexStats;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequest;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.*;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class EsTest {

    @Resource
    private TransportClient client;

    @Autowired(required = false)
    private RestClient restClient;

    @Autowired(required = false)
    private RestHighLevelClient restHighLevelClient;

    @Resource
    private JestClient jestClient;

    @Test
    public void transportClientTest() throws IOException {
        System.out.println(client.listedNodes().size());
        ActionFuture<IndicesStatsResponse> isr = client.admin().indices().stats(new IndicesStatsRequest().all());
        IndicesAdminClient indicesAdminClient = client.admin().indices();
        Map<String, IndexStats> indexStatsMap = isr.actionGet().getIndices();
        Set<String> set = isr.actionGet().getIndices().keySet();
        set.stream().forEach(System.out::println);

        final String dateFormat = "yyyy-MM-dd HH:mm:ss.SSS || date_time || strict_date_time";

        String yyyyMMddHHmm = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        String index = "test_" + yyyyMMddHHmm;
        CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(index);
        XContentBuilder properties = XContentFactory.jsonBuilder().startObject().startObject("properties");
        properties.startObject("createTime").field("type", "date")
                .field("format",dateFormat).endObject().endObject().endObject();
        createIndexRequestBuilder.addMapping("doc", properties);
        CreateIndexResponse createIndexResponse = createIndexRequestBuilder.execute().actionGet();

        Map<String, Object> map = new HashMap<>();
        map.put("id", 222);
        map.put("name", "admin222");
        map.put("age", 342);
        map.put("createTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        IndexResponse indexResponse = client.prepareIndex(index, "doc").setSource(map).get();
        RestStatus status = indexResponse.status();
        Assertions.assertEquals("CREATED", status.name());


        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(new MatchAllQueryBuilder());
        List<String> indexs = new ArrayList<>();
        indexs.add(index);
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(indexs.toArray(new String[]{})).setTypes("doc")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(boolQueryBuilder)
                .addSort("createTime", SortOrder.DESC);
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        SearchHits hits = searchResponse.getHits();
        long totalHits = hits.getTotalHits().value;
        SearchHit[] searchHits = hits.getHits();
        // List<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            String id = hit.getId();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            sourceAsMap.put("_id", id);
            System.out.println(id + ": " + hit.getSourceAsString());
            // list.add(sourceAsMap);
        }

        /*DeleteResponse deleteResponse = client.prepareDelete("test_202003191311", "doc", "VadA8XABVv2Ey0DPncyR").get();
        RestStatus deleteStatus = deleteResponse.status();
        Assert.assertEquals("OK", deleteStatus.name());*/
    }

    @Test
    public void restClientTest() throws IOException {
        System.out.println(restClient.getNodes().size());
        Request request = new Request("GET", "/_cat/indices");
        request.addParameter("pretty", "true");
        Response response = restClient.performRequest(request);
        System.out.println(EntityUtils.toString(response.getEntity()));

        Request add = new Request("POST", "/test_202003221133/doc");
        Map<String, Object> map = new HashMap<>();
        map.put("id", 333);
        map.put("name", "admin333");
        map.put("age", 343);
        map.put("createTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        add.setEntity(new NStringEntity(
                JsonUtil.obj2Str(map),
                ContentType.APPLICATION_JSON));
        Response addResponse = restClient.performRequest(add);
        System.out.println(EntityUtils.toString(addResponse.getEntity()));
        Request get = new Request("get", "/test_202003221133/_search");
        Response getResponse = restClient.performRequest(get);
        System.out.println(EntityUtils.toString(getResponse.getEntity()));
    }

    @Test
    public void restClientPem() throws IOException {
        System.out.println(restClient.getNodes().size());
        Request request = new Request("GET", "/_cat/indices");
        request.addParameter("pretty", "true");
        Response response = restClient.performRequest(request);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void restHighLevelClientTest() throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit hit : search.getHits().getHits()) {
            System.out.println(hit.getSourceAsString());
        }
    }

    @Test
    public void jestClientTest() throws IOException {
        String searchJson = "{\n" +
                "    \"query\": {\n" +
                "        \"match_all\": {\n" +
                "        }\n" +
                "    }\n" +
                "}";
        //构建一个搜索
        Search search = new Search.Builder(searchJson).addIndex("test_202003221133").addType("doc").build();
        //执行
        SearchResult result = jestClient.execute(search);
        System.out.println(result.getJsonString());
    }

}
