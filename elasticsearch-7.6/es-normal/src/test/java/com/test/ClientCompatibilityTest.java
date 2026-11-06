package com.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.iminling.common.json.JsonUtil;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;

/** Runtime linkage checks without addresses, credentials, or a running cluster. */
class ClientCompatibilityTest {

    @Test
    void transportClientLoadsAndClosesOnJava17() {
        try (var client = new PreBuiltTransportClient(Settings.EMPTY)) {
            assertTrue(client.transportAddresses().isEmpty());
            assertTrue(client.connectedNodes().isEmpty());
        }
    }

    @Test
    void searchJsonWorksAlongsideTheApplicationJacksonMapper() {
        String json = new SearchSourceBuilder()
                .query(QueryBuilders.termQuery("name", "sample"))
                .from(5).size(10).toString();
        var tree = JsonUtil.getInstant().readTree(json);
        assertEquals("sample", tree.path("query").path("term").path("name").path("value").asText());
        assertEquals(5, tree.path("from").asInt());
        assertEquals(10, tree.path("size").asInt());
    }
}
