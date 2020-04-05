package com.test.model;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.Hashtable;

/**
 * 状态枚举类
 * @author konghang
 */
public enum MessageCode {

    RESULT_OK(200, "请求成功"),
    RESULT_FAIL(1, "存在未错误"),
    CORE_SERVICE_FAIL(2, "服务错误"),

    INTERNET_ERROR_400(400, "Bad Request!"),
    INTERNET_ERROR_405(405, "Method Not Allowed"),
    INTERNET_ERROR_406(406, "Not Acceptable"),
    INTERNET_ERROR_500(500, "系统异常"),

    /*token*/
    TOKEN_NULL(401, "token为空"),
    TOKEN_EXPIRED(401, "token过期"),
    TOKEN_INVALID_CLAIM(401, "token过期(Claim),请重新登录"),
    TOKEN_ERROR(401, "token校验失败"),
    TOKEN_SIGNATURE_ERROR(401, "签名校验失败"),
    PERMISSION_DENY(403, "无权操作"),

    SYSTEM_BEGIN(91000, "系统模块开始"),

    TRANSACTION_ERROR(92000, "事务异常"),

    SYSTEM_ERROR(91500, "系统错误"),

    SYSTEM_FAIL(91599, "系统相关"),

    ERROR_MAX(999999999, "错误");

    @Setter @Getter private int code;
    @Setter @Getter private String message;

    MessageCode(int id, String msg) {
        this.code = id;
        this.message = msg;
    }

    public Hashtable<Object, Object> toJson() {
        Hashtable<Object, Object> json = new Hashtable<Object, Object>();
        json.put("error", code);
        json.put("msg", message);
        return json;
    }

    @Override
    public String toString() {
        JSON.toJSONString(toJson());
        return JSON.toJSONString(toJson());
    }

    public static Integer getErrorIdByErrorMsg(String errorMsg) {
        for (MessageCode messageCode : MessageCode.values()) {
            if (errorMsg.equals(messageCode.getMessage())) {
                return messageCode.getCode();
            }
        }
        return null;
    }
}
