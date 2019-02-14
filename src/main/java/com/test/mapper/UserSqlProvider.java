package com.test.mapper;

import com.test.po.User;
import org.apache.ibatis.jdbc.SQL;

public class UserSqlProvider {

    public String insertSelective(User record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("t_user");
        
        if (record.getUserId() != null) {
            sql.VALUES("user_id", "#{userId,jdbcType=VARCHAR}");
        }
        
        if (record.getUsername() != null) {
            sql.VALUES("username", "#{username,jdbcType=VARCHAR}");
        }
        
        if (record.getNickname() != null) {
            sql.VALUES("nickname", "#{nickname,jdbcType=VARCHAR}");
        }
        
        if (record.getSalt() != null) {
            sql.VALUES("salt", "#{salt,jdbcType=VARCHAR}");
        }
        
        if (record.getPassword() != null) {
            sql.VALUES("`password`", "#{password,jdbcType=VARCHAR}");
        }
        
        if (record.getSex() != null) {
            sql.VALUES("sex", "#{sex,jdbcType=TINYINT}");
        }
        
        if (record.getEmail() != null) {
            sql.VALUES("email", "#{email,jdbcType=VARCHAR}");
        }
        
        if (record.getHeadImage() != null) {
            sql.VALUES("head_image", "#{headImage,jdbcType=VARCHAR}");
        }
        
        if (record.getCreateIp() != null) {
            sql.VALUES("create_ip", "#{createIp,jdbcType=VARCHAR}");
        }
        
        if (record.getCreateDate() != null) {
            sql.VALUES("create_date", "#{createDate,jdbcType=BIGINT}");
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(User record) {
        SQL sql = new SQL();
        sql.UPDATE("t_user");
        
        if (record.getUserId() != null) {
            sql.SET("user_id = #{userId,jdbcType=VARCHAR}");
        }
        
        if (record.getUsername() != null) {
            sql.SET("username = #{username,jdbcType=VARCHAR}");
        }
        
        if (record.getNickname() != null) {
            sql.SET("nickname = #{nickname,jdbcType=VARCHAR}");
        }
        
        if (record.getSalt() != null) {
            sql.SET("salt = #{salt,jdbcType=VARCHAR}");
        }
        
        if (record.getPassword() != null) {
            sql.SET("`password` = #{password,jdbcType=VARCHAR}");
        }
        
        if (record.getSex() != null) {
            sql.SET("sex = #{sex,jdbcType=TINYINT}");
        }
        
        if (record.getEmail() != null) {
            sql.SET("email = #{email,jdbcType=VARCHAR}");
        }
        
        if (record.getHeadImage() != null) {
            sql.SET("head_image = #{headImage,jdbcType=VARCHAR}");
        }
        
        if (record.getCreateIp() != null) {
            sql.SET("create_ip = #{createIp,jdbcType=VARCHAR}");
        }
        
        if (record.getCreateDate() != null) {
            sql.SET("create_date = #{createDate,jdbcType=BIGINT}");
        }
        
        sql.WHERE("id = #{id,jdbcType=BIGINT}");
        
        return sql.toString();
    }
}