package com.chant.lib;

import java.util.concurrent.Callable;

/**
 * 被观察者
 */
public class Observable<T> {

    private OnSubscriber<T> mOnSubscriber;

    protected Observable(OnSubscriber<T> onSubscriber) {
        mOnSubscriber = onSubscriber;
    }

    public void subscribe(Observer<T> observer) {
        mOnSubscriber.call(observer);
    }

    public static <T> Observable<T> create(OnSubscriber<T> onSubscriber) {
        return new Observable<>(onSubscriber);
    }

    public static <T> Observable<T> fromCallable(Callable<T> callable) {
        return new Observable<>(new OnSubscriber<T>() {
            @Override
            public void call(Observer<T> observer) {

                boolean done = false;
                try {
                    observer.onNext(callable.call());
                } catch (Exception e) {
                    observer.onError(e);
                    done = true;
                }
                if (!done) {
                    observer.onCompleted();
                }
            }
        });
    }

    public interface OnSubscriber<T> {
        void call(Observer<T> observer);
    }
}



