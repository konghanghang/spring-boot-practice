package com.test.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class IndexControllerTest {

    @Autowired
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
    }

}