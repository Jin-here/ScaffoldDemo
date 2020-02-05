package com.vgaw.scaffolddemo.http.bean;

/**
 * Created by caojin on 2017/6/16.
 */

public class ResponseOverview {
    private Integer code;
    private String msg;
    private String body;

    public ResponseOverview() {}

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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "ResponseOverview{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
