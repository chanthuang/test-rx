package com.chant.lib;

import java.util.function.Predicate;

public class OnSubscribeFilter<T> implements OnSubscribe<T> {

    private Observable<T> mSource;
    private Predicate<T> mPredicate;
    private boolean done = false;

    public OnSubscribeFilter(Observable<T> source, Predicate<T> predicate) {
        mSource = source;
        mPredicate = predicate;
    }

    @Override
    public void call(Observer<T> observer) {
        ObserverFilter<T> observerFilter = new ObserverFilter<>(observer, mPredicate);
        mSource.subscribe(observerFilter);
    }

    static class ObserverFilter<T> implements Observer<T> {

        private boolean done = false;
        private Observer<T> mActual;
        private Predicate<T> mPredicate;

        public ObserverFilter(Observer<T> mActual, Predicate<T> mPredicate) {
            this.mActual = mActual;
            this.mPredicate = mPredicate;
        }

        @Override
        public void onNext(T t) {
            if (done) return;

            boolean result = false;

            try {
                result = mPredicate.test(t);
            } catch (Exception e) {
                onError(e);
            }

            if (result) {
                mActual.onNext(t);
            }
        }

        @Override
        public void onError(Throwable e) {
            mActual.onError(e);
            done = true;
        }

        @Override
        public void onCompleted() {
            if (!done) {
                mActual.onCompleted();
            }
        }
    }

}
