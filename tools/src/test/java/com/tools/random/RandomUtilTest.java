package com.tools.random;

import java.security.SecureRandom;
import java.util.Random;
import java.util.SplittableRandom;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.Test;

/**
 * 产生随机数
 * @author yslao@outlook.com
 * @since 2022/8/9
 */
class RandomUtilTest {

    @Test
    void randomInts() {
        new Random().ints(5, 1, 10).forEach(System.out::println);
    }

    @Test
    void threadLocalRandomTest() {
        int i = ThreadLocalRandom.current().nextInt(1, 10);
        System.out.println(i);
    }

    /**
     * 线程不安全
     */
    @Test
    void splittableRandomTest() {
        SplittableRandom random = new SplittableRandom();
        int i = random.nextInt(1, 10);
        System.out.println(i);
    }

    @Test
    void secureRandomTest() {
        SecureRandom random = new SecureRandom();
        int i = random.nextInt(10);
        System.out.println(i);
    }

}
