package com.test;

import java.io.IOException;
import javax.annotation.Resource;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class EsTest {

    @Resource
    private RestClient restClient;

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void restClientTest() throws IOException {
        System.out.println(restClient.getNodes().size());
        Request request = new Request("GET", "/_cat/indices");
        request.addParameter("pretty", "true");
        Response response = restClient.performRequest(request);
        System.out.println(EntityUtils.toString(response.getEntity()));

        /*Request add = new Request("POST", "/test_202003221133/doc");
        Map<String, Object> map = new HashMap<>();
        map.put("id", 333);
        map.put("name", "admin333");
        map.put("age", 343);
        map.put("createTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        add.setEntity(new NStringEntity(
                JSON.toJSONString(map),
                ContentType.APPLICATION_JSON));
        Response addResponse = restClient.performRequest(add);
        System.out.println(EntityUtils.toString(addResponse.getEntity()));
        Request get = new Request("get", "/test_202003221133/_search");
        Response getResponse = restClient.performRequest(get);
        System.out.println(EntityUtils.toString(getResponse.getEntity()));*/
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

}
