package com.test.cloud.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "dept")
public class Dept {

    @TableId(type = IdType.AUTO)
    private Long deptno;

    private String dname;

    private String dbSource;

}
