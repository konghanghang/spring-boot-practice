package com.sql.constant;

public enum DatasourceTypeEnum {

    MYSQL(1),
    ORACLE(2),
    SQL_SERVER(3),
    DB2(4),
    SYBASE(5),
    DM(6)
    ;

    private int type;

    private DatasourceTypeEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

}
