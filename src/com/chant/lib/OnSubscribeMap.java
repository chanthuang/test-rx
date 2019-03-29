package com.chant.lib;

import java.util.function.Function;

public class OnSubscribeMap<T, R> implements OnSubscribe<R> {

    private Observable<T> mSource;
    private Function<T, R> mFunc;
    private boolean done = false;

    public OnSubscribeMap(Observable<T> source, Function<T, R> func) {
        mSource = source;
        mFunc = func;
    }

    @Override
    public void call(Observer<R> observer) {
        Observer<T> observer1 = new Observer<T>() {
            @Override
            public void onNext(T t) {
                if (done) return;
                try {
                    R r = mFunc.apply(t);
                    observer.onNext(r);
                } catch (Exception e) {
                    observer.onError(e);
                    done = true;
                }
            }

            @Override
            public void onError(Throwable e) {
                observer.onError(e);
            }

            @Override
            public void onCompleted() {
                if (!done) {
                    observer.onCompleted();
                }
            }
        };
        mSource.subscribe(observer1);
    }
}

