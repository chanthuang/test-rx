package com.chant.lib;

import java.util.function.Function;

public class OnSubscribeMap<T, R> implements OnSubscribe<R> {

    private Observable<T> mSource;
    private Function<T, R> mFunc;

    public OnSubscribeMap(Observable<T> source, Function<T, R> func) {
        mSource = source;
        mFunc = func;
    }

    @Override
    public void call(Observer<R> observer) {
        ObserverMap<T, R> observerMap = new ObserverMap<>(observer, mFunc);
        mSource.subscribe(observerMap);
    }

    static class ObserverMap<T, R> implements Observer<T> {

        private boolean done = false;
        private Observer<R> mActual;
        private Function<T, R> mFunc;

        public ObserverMap(Observer<R> actual, Function<T, R> mFunc) {
            mActual = actual;
            this.mFunc = mFunc;
        }

        @Override
        public void onNext(T t) {
            if (done) return;
            try {
                R r = mFunc.apply(t);
                mActual.onNext(r);
            } catch (Exception e) {
                mActual.onError(e);
                done = true;
            }
        }

        @Override
        public void onError(Throwable e) {
            mActual.onError(e);
        }

        @Override
        public void onCompleted() {
            if (!done) {
                mActual.onCompleted();
            }
        }
    }
}

