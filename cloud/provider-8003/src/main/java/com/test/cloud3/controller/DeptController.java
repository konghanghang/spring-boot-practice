package com.test.cloud3.controller;

import com.test.cloud.model.Dept;
import com.test.cloud3.service.IDeptService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/dept")
public class DeptController {

    @Resource
    private IDeptService deptService;

    @PostMapping("add")
    public boolean add(@RequestBody Dept dept) {
        return deptService.save(dept);
    }

    @GetMapping("/get/{id}")
    public Dept get(@PathVariable Long id) {
        return deptService.getById(id);
    }

    @GetMapping("list")
    public List<Dept> list() {
        return deptService.list();
    }

}
