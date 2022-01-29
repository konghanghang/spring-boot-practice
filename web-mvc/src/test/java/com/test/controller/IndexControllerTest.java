package com.test.controller;

import com.test.service.IndexService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(IndexController.class)
class IndexControllerTest {

    @Autowired
    private IndexController indexController;
    @MockBean
    private IndexService indexService;
    @Autowired
    private MockMvc mockMvc;
    /*@Autowired
    private RestTemplate restTemplate;

    @Test
    void testGetObject() {
        String url = "http://127.0.0.1:8080/index";
        String forObject = restTemplate.getForObject(url, String.class);
        System.out.println(forObject);
    }

    @Test
    void testPostObject() {
        String url = "http://127.0.0.1:8080/paramsMapTest";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("myName", "lucy");
        map.add("myAge", "10");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        String forObject = restTemplate.postForObject(url, request, String.class);
        System.out.println(forObject);
    }*/

    @Test
    void indexTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/index")
                .param("name", "lucy")
                .param("age", "10"))
            //.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("index"));
    }

    @Test
    void helloTest() throws Exception {
        Mockito.when(indexService.hello(Mockito.anyString())).thenReturn("hello ok");
        mockMvc.perform(MockMvcRequestBuilders.get("/hello/lucy"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("hello ok"));
    }

}