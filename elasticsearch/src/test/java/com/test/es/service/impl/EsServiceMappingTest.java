package com.test.es.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.iminling.common.json.JsonUtil;
import com.test.es.model.MappingFieldInfo;
import java.util.List;
import java.util.Map;
import org.elasticsearch.Version;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexAction;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkAction;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexAction;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchAction;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.PlainActionFuture;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.IndexMetadata;
import org.elasticsearch.cluster.metadata.MappingMetadata;
import org.elasticsearch.cluster.metadata.Metadata;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.search.SearchHits;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

class EsServiceMappingTest {
    private TransportClient client;
    private EsServiceImpl service;

    @BeforeEach
    void setUp() {
        client = mock(TransportClient.class, RETURNS_DEEP_STUBS);
        service = new EsServiceImpl();
        ReflectionTestUtils.setField(service, "client", client);
    }

    @Test
    void commonAndCustomMappingsUseTheSingleDocumentMapping() throws Exception {
        when(client.admin().indices().prepareCreate(anyString())).thenAnswer(call ->
                new CreateIndexRequestBuilder(client, CreateIndexAction.INSTANCE, call.getArgument(0)));
        when(client.execute(eq(CreateIndexAction.INSTANCE), any(CreateIndexRequest.class)))
                .thenReturn(completed(new CreateIndexResponse(true, true, "sample")));

        assertTrue(service.createIndexWithCommonMapping("sample", "legacy-type"));
        assertTrue(service.createIndexWithCustomMapping("custom", "doc", List.of(
                new MappingFieldInfo().setName("name").setType("keyword"),
                new MappingFieldInfo().setName("created").setType("date").setFormatType("strict_date_time"))));

        var request = ArgumentCaptor.forClass(CreateIndexRequest.class);
        verify(client, times(2)).execute(eq(CreateIndexAction.INSTANCE), request.capture());
        var common = request.getAllValues().get(0);
        assertEquals(List.of("_doc"), List.copyOf(common.mappings().keySet()));
        var commonProperties = JsonUtil.getInstant().readTree(common.mappings().get("_doc"))
                .path("_doc").path("properties");
        assertEquals("date", commonProperties.path("createTime").path("type").asText());
        var custom = JsonUtil.getInstant().readTree(request.getAllValues().get(1).mappings().get("_doc"))
                .path("_doc").path("properties");
        assertEquals("keyword", custom.path("name").path("type").asText());
        assertEquals("strict_date_time", custom.path("created").path("format").asText());
    }

    @Test
    void metadataIncludesEveryIndexAndHandlesImplicitObjectAndEmptyMappings() throws Exception {
        var first = index("first").putMapping(new MappingMetadata("_doc", Map.of("properties", Map.of(
                "name", Map.of("type", "keyword"),
                "address", Map.of("properties", Map.of("city", Map.of("type", "keyword"))))))).build();
        var second = index("second").putMapping(new MappingMetadata("_doc", Map.of("properties",
                Map.of("count", Map.of("type", "long"))))).build();
        var empty = index("empty").build();
        var clusterName = new ClusterName("isolated-test");
        var state = ClusterState.builder(clusterName)
                .metadata(Metadata.builder().put(first, false).put(second, false).put(empty, false)).build();
        when(client.admin().cluster().prepareState().clear().setMetadata(true).execute().actionGet())
                .thenReturn(new ClusterStateResponse(clusterName, state, false));

        var result = JsonUtil.getInstant().valueToTree(service.getIndexMappings());
        assertEquals(3, result.size());
        var byName = new java.util.HashMap<String, tools.jackson.databind.JsonNode>();
        result.forEach(node -> byName.put(node.path("indexName").asText(), node.path("fields")));
        assertEquals(2, byName.get("first").size());
        assertTrue(byName.get("first").toString().contains("\"type\":\"object\""));
        assertEquals("count", byName.get("second").get(0).path("name").asText());
        assertEquals("long", byName.get("second").get(0).path("type").asText());
        assertEquals(0, byName.get("empty").size());
    }

    @Test
    void documentWritesUseDocAndCreatedIsSuccessfulWhileSearchHasNoTypeFilter() {
        when(client.prepareIndex(anyString(), anyString())).thenAnswer(call ->
                new IndexRequestBuilder(client, IndexAction.INSTANCE, call.getArgument(0)).setType(call.getArgument(1)));
        when(client.execute(eq(IndexAction.INSTANCE), any(IndexRequest.class))).thenReturn(completed(
                new IndexResponse(new ShardId("sample", "uuid", 0), "_doc", "1", 0, 1, 1, true)));
        assertTrue(service.insertRecord(Map.of("name", "sample"), "sample", "legacy-type"));
        var indexRequest = ArgumentCaptor.forClass(IndexRequest.class);
        verify(client).execute(eq(IndexAction.INSTANCE), indexRequest.capture());
        assertEquals("_doc", indexRequest.getValue().type());

        when(client.prepareSearch(any(String[].class))).thenAnswer(call ->
                new SearchRequestBuilder(client, SearchAction.INSTANCE).setIndices((String[]) call.getRawArguments()[0]));
        SearchResponse response = mock(SearchResponse.class);
        when(response.getHits()).thenReturn(SearchHits.empty());
        when(client.execute(eq(SearchAction.INSTANCE), any(SearchRequest.class))).thenReturn(completed(response));
        assertEquals(List.of(), service.query(List.of("sample"), "legacy-type", null));
        var searchRequest = ArgumentCaptor.forClass(SearchRequest.class);
        verify(client).execute(eq(SearchAction.INSTANCE), searchRequest.capture());
        assertEquals(0, searchRequest.getValue().types().length);
    }

    @Test
    void bulkWritesReportIndividualFailuresEvenWhenTheResponseStatusIsOk() {
        when(client.prepareIndex(anyString(), anyString())).thenAnswer(call ->
                new IndexRequestBuilder(client, IndexAction.INSTANCE, call.getArgument(0)).setType(call.getArgument(1)));
        when(client.prepareBulk()).thenAnswer(call -> new BulkRequestBuilder(client, BulkAction.INSTANCE));
        var success = BulkItemResponse.success(0, DocWriteRequest.OpType.INDEX,
                new IndexResponse(new ShardId("sample", "uuid", 0), "_doc", "1", 0, 1, 1, true));
        var failure = BulkItemResponse.failure(0, DocWriteRequest.OpType.INDEX,
                new BulkItemResponse.Failure("sample", "_doc", "2", new IllegalArgumentException("invalid field")));
        when(client.execute(eq(BulkAction.INSTANCE), any(BulkRequest.class)))
                .thenReturn(completed(new BulkResponse(new BulkItemResponse[]{success}, 1)),
                        completed(new BulkResponse(new BulkItemResponse[]{failure}, 1)));

        assertTrue(service.insertRecordBulk(List.of(Map.of("name", "sample")), "sample", "legacy-type"));
        assertFalse(service.insertRecordBulk(List.of(Map.of("name", "sample")), "sample", "legacy-type"));
        var requests = ArgumentCaptor.forClass(BulkRequest.class);
        verify(client, times(2)).execute(eq(BulkAction.INSTANCE), requests.capture());
        assertEquals("_doc", requests.getValue().requests().get(0).type());
    }

    private static IndexMetadata.Builder index(String name) {
        return IndexMetadata.builder(name)
                .settings(Settings.builder().put(IndexMetadata.SETTING_VERSION_CREATED, Version.CURRENT))
                .numberOfShards(1).numberOfReplicas(0);
    }

    private static <T> ActionFuture<T> completed(T response) {
        PlainActionFuture<T> future = new PlainActionFuture<>();
        future.onResponse(response);
        return future;
    }
}
