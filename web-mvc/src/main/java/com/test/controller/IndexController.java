package com.test.controller;

import com.test.service.IndexService;
import com.test.service.bark.BarkService;
import com.test.vo.AccountModel;
import com.test.vo.TestVo;
import com.test.vo.UserVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class IndexController {

    private final IndexService indexService;

    private final BarkService barkService;

    @RequestMapping("/index")
    public String index(UserVo userVo) {
        log.info("index, user:{}", userVo);
        return "index";
    }

    @GetMapping("/hello/{name}")
    public String hello(@PathVariable String name) {
        return indexService.hello(name);
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

    @RequestMapping(value = "/customResovlerTest")
    public Object customResovlerTest(@RequestBody AccountModel accountModel, TestVo testVo){
        return accountModel.toString() + "---" + testVo.toString();
    }

    @RequestMapping("/bark")
    public void barkSend() {
        barkService.sendNotice();
    }

}
