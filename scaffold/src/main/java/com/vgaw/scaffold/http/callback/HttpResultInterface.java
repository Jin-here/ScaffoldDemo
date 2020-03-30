package com.vgaw.scaffold.http.callback;

public interface HttpResultInterface<T> {
    void onSuccess(T data);

    void onFail(String msg);

    void onFinished();
}