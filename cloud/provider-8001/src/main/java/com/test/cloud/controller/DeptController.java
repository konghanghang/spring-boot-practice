package com.test.cloud.controller;

import com.test.cloud.model.Dept;
import com.test.cloud.service.IDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/dept")
public class DeptController {

    @Resource
    private IDeptService deptService;
    @Autowired
    private DiscoveryClient discoveryClient;

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

    @GetMapping("/discovery")
    public Object discovery() {
        List<String> services = discoveryClient.getServices();
        for (String service : services) {
            System.out.println("***" + service + "****");
        }
        List<ServiceInstance> instances = discoveryClient.getInstances("provider-dept");
        for (ServiceInstance instance : instances) {
            System.out.println(instance.getInstanceId() + "\t" + instance.getHost() + "\t" + instance.getScheme() + "\t" + instance.getServiceId() + "\t" + instance.getPort() + "\t" + instance.getUri());
        }
        return discoveryClient;
    }

}
