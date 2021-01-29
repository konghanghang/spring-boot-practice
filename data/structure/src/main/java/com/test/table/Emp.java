package com.test.table;

import lombok.Data;

/**
 * @author yslao@outlook.com
 * @since 2021/1/28
 */
@Data
public class Emp {
    private int id;
    private String name;
    private Emp next;

    public Emp(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
