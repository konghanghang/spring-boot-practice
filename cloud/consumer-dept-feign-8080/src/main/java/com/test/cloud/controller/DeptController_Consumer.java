package com.test.cloud.controller;

import com.test.cloud.model.Dept;
import com.test.cloud.service.DeptClientService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class DeptController_Consumer {

    @Resource
    private DeptClientService service;

    @RequestMapping("/consumer/dept/add")
    public boolean add(Dept dept) {
        return service.add(dept);
    }

    @RequestMapping("/consumer/dept/get/{id}")
    public Dept get(@PathVariable Long id) {
        return service.get(id);
    }

    @RequestMapping("/consumer/dept/list")
    public List<Dept> list() {
        return service.list();
    }

}
