package com.test.service.bark;

import com.iminling.common.json.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author yslao@outlook.com
 * @since 5/18/24
 */
@Service
@RequiredArgsConstructor
public class BarkService {

    private final RestTemplate restTemplate;

    private String deviceKey = "3xwXx3EQUMWPpdxxxdfjke6";

    private String url = "http://127.0.0.1/push";
    
    public void sendNotice() {
        BarkRequest request = new BarkRequest();
        request.setTitle("node notice");
        request.setBody("我是主内容，请注意。");
        request.setDeviceKey(deviceKey);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, httpHeaders), String.class);
        System.out.println(exchange.getBody());
    }

}
