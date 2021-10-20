package com.design.hehavior.chain.handler;

/**
 * @author yslao@outlook.com
 * @since 2021/6/12
 */
public class Client {

    static class HandlerA extends Handler{
        @Override
        protected void handlerProcess() {
            System.out.println("handler by a");
        }
    }

    static class HandlerB extends Handler{
        @Override
        protected void handlerProcess() {
            System.out.println("handler by b");
        }
    }

    static class HandlerC extends Handler{
        @Override
        protected void handlerProcess() {
            System.out.println("handler by c");
        }
    }

    public static void main(String[] args) {
        Handler handlerA = new HandlerA();
        Handler handlerB = new HandlerB();
        Handler handlerC = new HandlerC();

        handlerA.setSuccessor(handlerB);
        handlerB.setSuccessor(handlerC);

        handlerA.execute();
    }

}
