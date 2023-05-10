package com.tools.powermock;

import cn.hutool.core.date.DateUtil;
import java.util.Date;

/**
 * @since 2022/8/10
 */
public class TestService {

    private String name;

    public Date getDate() {
        return new Date();
    }

    public int callInnerGetNumber() {
        return getNumber();
    }

    protected int getNumber() {
        return 0;
    }

    public String getTodayStr() {
        return DateUtil.today();
    }

    public String getName() {
        return name;
    }

}
