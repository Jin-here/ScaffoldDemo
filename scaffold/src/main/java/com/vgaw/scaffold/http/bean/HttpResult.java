package com.vgaw.scaffold.http.bean;

/**
 * Created by caojin on 2017/6/16.
 */

public class HttpResult<T> {
    private Integer code;
    private String msg;
    private T body;

    public static boolean success(HttpResult httpResult) {
        // TODO: 2020/2/28 replace with your own logic
        return false;
    }

    public HttpResult() {}

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
