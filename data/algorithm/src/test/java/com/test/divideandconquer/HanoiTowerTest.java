package com.test.divideandconquer;

import org.junit.jupiter.api.Test;

/**
 * 汉诺塔测试
 * @author yslao@outlook.com
 * @since 2021/4/1
 */
class HanoiTowerTest {

    @Test
    void hanoiTower() {
        HanoiTower hanoiTower = new HanoiTower();
        hanoiTower.hanoiTower(5, 'A', 'B', 'C');
    }

}