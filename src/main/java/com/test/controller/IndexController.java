package com.test.controller;

import com.test.po.User;
import com.test.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class IndexController {

    @Resource
    IUserService userService;

    @RequestMapping("/index")
    @ResponseBody
    public String index(){
        User user = userService.getUserByUsername("konghang");
        System.out.println(user);
        return "hello";
    }

    @GetMapping("/job")
    public String job(){
        return "/JobManager";
    }

}
