package com.test.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;

public interface IJobAndTriggerService {

    PageInfo<JSONObject> getJobAndTriggerDetails(int pageNum, int pageSize);

}
