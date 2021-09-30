package com.test.controller;

import com.test.vo.AccountModel;
import com.test.vo.TestVo;
import com.test.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@Api(tags = "首页模块")
@RestController
@RequestMapping
public class IndexController {

    @RequestMapping("/index")
    @ApiOperation(value = "首页", httpMethod = "GET")
    public String index(UserVo name) {
        System.out.println("index controller");
        return "index";
    }

    @PostMapping("/paramsMapTest")
    @ApiOperation(value = "参数测试")
    public Object paramsMapTest(@RequestParam("myName") String myName,
                                @RequestParam("myAge") int myAge){
        return String.format("name = %s,age = %d", myName, myAge);
    }

    @PostMapping("/modelAttributeTest")
    @ApiOperation(value = "model参数测试")
    public Object modelAttributeTest(AccountModel accountModel){
        return accountModel.toString();
    }

    @RequestMapping(value = "/customResovlerTest")
    @ApiOperation(value = "自定义参数测试", httpMethod = "POST")
    public Object customResovlerTest(@RequestBody AccountModel accountModel, TestVo testVo){
        return accountModel.toString() + "---" + testVo.toString();
    }

}
