package com.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一返回实体
 * @author konghang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultModel<T> {

    private static final int OK = 0;
    private static final int FAIL = 1;

    private int code;
    private T data;
    private String message;

    public ResultModel(MessageCode messageCode){
        this.code = messageCode.getCode();
        this.message = messageCode.getMessage();
    }

    public static ResultModel isOk(){
        return new ResultModel();
    }

    public static ResultModel isFail(){
        return isFail(FAIL);
    }

    public static ResultModel isFail(Integer code){
        return new ResultModel().code(code);
    }

    public static ResultModel isFail(Throwable e){
        return isFail().message(e);
    }

    public static ResultModel isFail(MessageCode messageCode){
        return new ResultModel()
                .code(messageCode.getCode())
                .message(messageCode.getMessage());
    }

    public static ResultModel isFail(String message){
        return new ResultModel().code(MessageCode.INTERNET_ERROR_500.getCode()).message(message);
    }

    public ResultModel code(int code){
        this.setCode(code);
        return this;
    }

    public ResultModel message(Throwable e){
        this.setMessage(e.getMessage());
        return this;
    }

    public ResultModel message(String message){
        this.setMessage(message);
        return this;
    }

    public ResultModel data(T data){
        this.setData(data);
        return this;
    }
}
