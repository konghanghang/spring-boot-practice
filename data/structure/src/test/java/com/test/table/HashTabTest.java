package com.test.table;

import org.junit.jupiter.api.Test;

/**
 * @author yslao@outlook.com
 * @since 2021/1/28
 */
class HashTabTest {

    @Test
    void add() {
        HashTab hashTab = new HashTab(7);
        Emp emp = new Emp(1, "小明1");
        Emp emp2 = new Emp(3, "小明2");
        Emp emp3 = new Emp(5, "小明3");
        Emp emp4 = new Emp(7, "小明4");
        Emp emp5 = new Emp(14, "小明14");
        hashTab.add(emp);
        hashTab.add(emp2);
        hashTab.add(emp3);
        hashTab.add(emp4);
        hashTab.add(emp5);
        hashTab.list();
        hashTab.findById(11);
        hashTab.deleteById(14);
        hashTab.list();
        System.out.println("-----------------");
        Emp emp6 = new Emp(21, "小明14");
        hashTab.add(emp6);
        hashTab.list();
    }
}