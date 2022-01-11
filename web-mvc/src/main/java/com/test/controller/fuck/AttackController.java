package com.test.controller.fuck;

import com.iminling.core.annotation.EnableResolve;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yslao@outlook.com
 * @since 2022/1/11
 */
@Controller
@EnableResolve
public class AttackController {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * ZQC8WX
     */
    @GetMapping("/login")
    public void login() {
        /*String loginUrl = "http://hdia.auxsteel.xyz/index/user/do_login.shtml";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.clear();
        body.add("tel", "smilele");
        body.add("pwd", "smilele");
        body.add("jizhu", "1");
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(loginUrl, entity, String.class);
        String cookie = response.getHeaders().get("Set-Cookie").get(0);
        System.out.println(cookie);
        System.out.println(cookie.split(";")[0]);*/

        List<String> cookies =new ArrayList<>();
        /* 登录获取Cookie 这里是直接给Cookie，可使用下方的login方法拿到Cookie给入*/
        cookies.add("sb365a9b6=md7t2ufthkc27vj4rllas5vlft; tel=smilele; pwd=smilele; page-limit=20");       //在 header 中存入cookies
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put(HttpHeaders.COOKIE,cookies);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(null, httpHeaders);

        String homeUrl = "http://hdia.auxsteel.xyz/index/my/index";
        ResponseEntity<String> exchange = restTemplate.exchange(homeUrl, HttpMethod.GET, entity, String.class);
        String body = exchange.getBody();
        int index = body.indexOf("邀请码");
        // 获取邀请码
        System.out.println("=============");
        System.out.println(body.substring(index + 4, index + 10));
        System.out.println("=============");
        //System.out.println(body);
    }

    public void register() {
        String code = "ZQC8WX";
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
        System.out.println(String.format("用户名：%s，tel：%s，密码：%s，取款密码：%s，邀请码：%s, result:%s", generatedString, tel, pwd, de, code, s));
        String loginUrl = "http://hdia.auxsteel.xyz/index/user/do_login.shtml";
        body.clear();
        body.add("tel", generatedString);
        body.add("pwd", pwd);
        body.add("jizhu", "1");
        entity = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(loginUrl, entity,
                String.class);
        System.out.println(response.getHeaders().get("Set-Cookie"));
    }

    /**
     * 添加cookie
     */
    public void setReciveAddress() {
        String url = "http://hdia.auxsteel.xyz/index/my/edit_address.shtml";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("shoujianren", "smilele");
        body.add("shouhuohaoma", "smilele");
        body.add("area", "广东省广州市天河区");
        body.add("address", "珠江新城");
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
    }

}
