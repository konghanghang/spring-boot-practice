package com.test.mapper;

import com.test.po.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface UserMapper {
    @Delete({
        "delete from t_user",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into t_user (user_id, username, ",
        "nickname, salt, ",
        "`password`, sex, email, ",
        "head_image, create_ip, ",
        "create_date)",
        "values (#{userId,jdbcType=VARCHAR}, #{username,jdbcType=VARCHAR}, ",
        "#{nickname,jdbcType=VARCHAR}, #{salt,jdbcType=VARCHAR}, ",
        "#{password,jdbcType=VARCHAR}, #{sex,jdbcType=TINYINT}, #{email,jdbcType=VARCHAR}, ",
        "#{headImage,jdbcType=VARCHAR}, #{createIp,jdbcType=VARCHAR}, ",
        "#{createDate,jdbcType=BIGINT})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(User record);

    @InsertProvider(type=UserSqlProvider.class, method="insertSelective")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insertSelective(User record);

    @Select({
        "select",
        "id, user_id, username, nickname, salt, `password`, sex, email, head_image, create_ip, ",
        "create_date",
        "from t_user",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.VARCHAR),
        @Result(column="username", property="username", jdbcType=JdbcType.VARCHAR),
        @Result(column="nickname", property="nickname", jdbcType=JdbcType.VARCHAR),
        @Result(column="salt", property="salt", jdbcType=JdbcType.VARCHAR),
        @Result(column="password", property="password", jdbcType=JdbcType.VARCHAR),
        @Result(column="sex", property="sex", jdbcType=JdbcType.TINYINT),
        @Result(column="email", property="email", jdbcType=JdbcType.VARCHAR),
        @Result(column="head_image", property="headImage", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_ip", property="createIp", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_date", property="createDate", jdbcType=JdbcType.BIGINT)
    })
    User selectByPrimaryKey(Long id);

    @UpdateProvider(type=UserSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(User record);

    @Update({
        "update t_user",
        "set user_id = #{userId,jdbcType=VARCHAR},",
          "username = #{username,jdbcType=VARCHAR},",
          "nickname = #{nickname,jdbcType=VARCHAR},",
          "salt = #{salt,jdbcType=VARCHAR},",
          "`password` = #{password,jdbcType=VARCHAR},",
          "sex = #{sex,jdbcType=TINYINT},",
          "email = #{email,jdbcType=VARCHAR},",
          "head_image = #{headImage,jdbcType=VARCHAR},",
          "create_ip = #{createIp,jdbcType=VARCHAR},",
          "create_date = #{createDate,jdbcType=BIGINT}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(User record);
}