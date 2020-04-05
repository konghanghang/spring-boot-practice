package com.test.exception;

import com.test.model.MessageCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户认证异常类
 * @author konghang
 */
public class AuthorizeException extends RuntimeException {

    @Getter @Setter private MessageCode messageCode;

    public AuthorizeException(){
        super(MessageCode.PERMISSION_DENY.getMessage());
        this.messageCode = MessageCode.PERMISSION_DENY;
    }

    public AuthorizeException(MessageCode messageCode){
        super(messageCode.getMessage());
        this.messageCode = messageCode;
    }

}
