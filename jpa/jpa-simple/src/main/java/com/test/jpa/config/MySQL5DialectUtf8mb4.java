package com.test.jpa.config;

import org.hibernate.dialect.MySQLDialect;

/** The class name is retained for existing configuration; Hibernate 7 targets MySQL 8+. */
public class MySQL5DialectUtf8mb4 extends MySQLDialect {

    @Override
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_unicode_ci ";
    }

}
