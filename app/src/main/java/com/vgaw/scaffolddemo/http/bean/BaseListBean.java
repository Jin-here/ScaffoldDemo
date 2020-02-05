package com.vgaw.scaffolddemo.http.bean;

import java.util.List;

public class BaseListBean<T> {
    private List<T> list;
    private PageBean pageInfo;

    public BaseListBean() {}

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public PageBean getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageBean pageInfo) {
        this.pageInfo = pageInfo;
    }

    @Override
    public String toString() {
        return "BaseListBean{" +
                "list=" + list +
                ", pageInfo=" + pageInfo +
                '}';
    }
}
