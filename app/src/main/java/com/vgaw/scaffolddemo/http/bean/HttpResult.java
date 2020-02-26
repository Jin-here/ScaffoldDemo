package com.vgaw.scaffolddemo.http.bean;

/**
 * Created by caojin on 2017/6/16.
 */

public class HttpResult<T> {
    private Integer code;
    private String msg;
    private T body;

    public static boolean success(HttpResult result) {
        // TODO: 2020/2/28 replace it with your own logic
        return result.code != null && result.code == 0;
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

    @Override
    public String toString() {
        return "HttpResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", body=" + body +
                '}';
    }
}
