package com.test.cloud2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.cloud.model.Dept;
import com.test.cloud2.dao.DeptMapper;
import com.test.cloud2.service.IDeptService;
import org.springframework.stereotype.Service;

@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {
}
