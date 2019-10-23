package com.vgaw.scaffolddemo.http.bean;

/**
 * Created by caojin on 2017/6/16.
 */

public class ResponseOverview {
    private Integer code;
    private String error;
    private String results;

    public ResponseOverview() {}

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "ResponseOverview{" +
                "code=" + code +
                ", error='" + error + '\'' +
                ", results='" + results + '\'' +
                '}';
    }
}
