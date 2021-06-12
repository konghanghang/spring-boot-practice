package com.test.chain;

/**
 * @author yslao@outlook.com
 * @since 2021/6/12
 */
public abstract class ChainHandler {

    public void execute(Chain chain){
        handlerProcess();
        chain.procced();
    }

    protected abstract void handlerProcess();

}
