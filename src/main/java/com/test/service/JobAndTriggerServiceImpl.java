package com.test.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.test.dao.JobAndTriggerDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class JobAndTriggerServiceImpl implements IJobAndTriggerService {


    @Resource
    private JobAndTriggerDao jobAndTriggerDao;

    @Override
    public PageInfo<JSONObject> getJobAndTriggerDetails(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<JSONObject> list = jobAndTriggerDao.getJobAndTriggerDetails();
        PageInfo<JSONObject> page = new PageInfo<>(list);
        return page;
    }
}
