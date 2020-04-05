package com.test.controller;

import com.test.annotation.Login;
import com.test.vo.AccountModel;
import com.test.vo.TestVo;
import com.test.vo.UserVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class IndexController {

    @RequestMapping("/index")
    public String index(UserVo name) {
        System.out.println("index controller");
        return "index";
    }

    @PostMapping("/paramsMapTest")
    public Object paramsMapTest(@RequestParam("myName") String myName,
                                @RequestParam("myAge") int myAge){
        return String.format("name = %s,age = %d", myName, myAge);
    }

    @PostMapping("/modelAttributeTest")
    public Object modelAttributeTest(AccountModel accountModel){
        return accountModel.toString();
    }

    @RequestMapping("/customResovlerTest")
    @Login
    public Object customResovlerTest(AccountModel accountModel, TestVo testVo){
        return accountModel.toString() + "---" + testVo.toString();
    }

}
