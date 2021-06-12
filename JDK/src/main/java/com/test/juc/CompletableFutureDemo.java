package com.test.juc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author yslao@outlook.com
 * @since 2021/4/8
 */
public class CompletableFutureDemo {

    public void demo() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
            System.out.println("runAsync 运行完成.....");
        });

        CompletableFuture<Integer> integerCompletableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("supplyAsync 运行完成.....");
            int i = 10 / 0;
            return 1024;
        });
        // t是运行结果,u是运行的异常信息
        CompletableFuture<Integer> exceptionally = integerCompletableFuture.whenComplete((t, u) -> {
            System.out.println("t:" + t);
            System.out.println("u:" + u);
        }).exceptionally(f -> {
            // 如果发生异常则走这里
            System.out.println("e:" + f.getMessage());
            return 4;
        });
        System.out.println(exceptionally.get());
    }

}
