package com.chant.lib;

import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 被观察者
 */
public class Observable<T> {

    private OnSubscribe<T> mOnSubscribe;

    protected Observable(OnSubscribe<T> onSubscribe) {
        mOnSubscribe = onSubscribe;
    }

    public void subscribe(Observer<T> observer) {
        mOnSubscribe.call(observer);
    }

    public <R> Observable<R> map(Function<T, R> func) {
        return new Observable<>(new OnSubscribeMap<>(this, func));
    }

    public Observable<T> filter(Predicate<T> predicate) {
        return new Observable<>(new OnSubscribeFilter<>(this, predicate));
    }

    public static <T> Observable<T> create(OnSubscribe<T> onSubscribe) {
        return new Observable<>(onSubscribe);
    }

    public static <T> Observable<T> fromCallable(Callable<T> callable) {
        return new Observable<>(new OnSubscribe<T>() {
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

    public static <T> Observable<T> from(T[] array) {
        return new Observable<>(new OnSubscribe<T>() {
            @Override
            public void call(Observer<T> observer) {
                boolean done = false;
                try {
                    for (T t : array) {
                        observer.onNext(t);
                    }
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
}



