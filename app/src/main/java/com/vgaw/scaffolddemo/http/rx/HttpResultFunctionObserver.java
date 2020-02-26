package com.vgaw.scaffolddemo.http.rx;

import com.vgaw.scaffold.view.AppToast;

import io.reactivex.observers.DisposableObserver;

public abstract class HttpResultFunctionObserver<T> extends DisposableObserver<T> implements HttpResultInterface<T> {
    @Override
    public void onFail(String msg) {
        AppToast.show(msg);
    }

    @Override
    public void onFinished() {}

    @Override
    public void onNext(T t) {
        if (t != null) {
            onSuccess(t);
        }
    }

    @Override
    public void onError(Throwable e) {
        onFail(e.getLocalizedMessage());
        onFinished();
    }

    @Override
    public void onComplete() {
        onFinished();
    }
}
