package com.design.hehavior.chain.handler;

/**
 * @author yslao@outlook.com
 * @since 2021/6/12
 */
public abstract class Handler {

    public Handler successor;

    public void setSuccessor(Handler successor) {
        this.successor = successor;
    }

    public void execute(){
        handlerProcess();
        if (successor != null){
            successor.execute();
        }
    }

    protected abstract void handlerProcess();

}
