package com.test.dao;

import com.test.mapper.UserMapper;
import com.test.po.User;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

@Component
public interface IUserDao extends UserMapper {

    @Results(id = "userInfo",value = {
            @Result(column = "user_id",property = "userId", jdbcType = JdbcType.VARCHAR),
            @Result(column = "head_image",property = "headImage", jdbcType = JdbcType.VARCHAR),
            @Result(column = "create_ip",property = "createIp", jdbcType = JdbcType.VARCHAR),
            @Result(column = "create_date",property = "createDate", jdbcType = JdbcType.BIGINT)
    })
    @Select({"select * from t_user where username = #{username}"})
    User getUserByUserName(String username);

}
