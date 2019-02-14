package com.test.service;

import com.test.dao.IUserDao;
import com.test.po.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements IUserService {

    private Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Resource
    private IUserDao userDao;

    @Override
    public User getUserByUsername(String username) {
        return userDao.getUserByUserName(username);
    }

}
