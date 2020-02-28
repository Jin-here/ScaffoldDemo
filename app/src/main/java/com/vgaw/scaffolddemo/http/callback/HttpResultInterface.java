package com.vgaw.scaffolddemo.http.callback;

public interface HttpResultInterface<T> {
    void onSuccess(T data);

    void onFail(String msg);

    void onFinished();
}