package com.vgaw.scaffold.http.bean;

import java.util.List;

public class HttpListContent<T> {
    private List<T> list;
    private HttpPageInfo pageInfo;

    public HttpListContent() {}

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public HttpPageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(HttpPageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    @Override
    public String toString() {
        return "HttpListContent{" +
                "list=" + list +
                ", pageInfo=" + pageInfo +
                '}';
    }
}
