package com.design.hehavior.chain.chain;

import java.util.List;

/**
 * @author yslao@outlook.com
 * @since 2021/6/12
 */
public class Chain {

    private List<ChainHandler> handlers;

    private int index = 0;

    public Chain(List<ChainHandler> handlers) {
        this.handlers = handlers;
    }

    public void procced(){
        if (index >= handlers.size()){
            return ;
        }
        handlers.get(index++).execute(this);
    }

}
