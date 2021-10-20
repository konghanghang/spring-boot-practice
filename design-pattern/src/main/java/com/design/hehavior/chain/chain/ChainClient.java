package com.design.hehavior.chain.chain;

import java.util.Arrays;

/**
 * @author yslao@outlook.com
 * @since 2021/6/12
 */
public class ChainClient {

    static class ChainHandlerA extends ChainHandler{
        @Override
        protected void handlerProcess() {
            System.out.println("chainHandler by a");
        }
    }
    static class ChainHandlerB extends ChainHandler{
        @Override
        protected void handlerProcess() {
            System.out.println("chainHandler by b");
        }
    }
    static class ChainHandlerC extends ChainHandler{
        @Override
        protected void handlerProcess() {
            System.out.println("chainHandler by c");
        }
    }

    public static void main(String[] args) {
        ChainHandler chainHandlerA = new ChainHandlerA();
        ChainHandler chainHandlerB = new ChainHandlerB();
        ChainHandler chainHandlerC = new ChainHandlerC();

        Chain chain = new Chain(Arrays.asList(chainHandlerA,chainHandlerB,chainHandlerC));

        chain.procced();
    }

}
