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
        return Observable.create(new OnSubscribeMap<>(this, func));
    }

    public Observable<T> filter(Predicate<T> predicate) {
        return Observable.create(new OnSubscribeFilter<>(this, predicate));
    }

    public <R> Observable<R> lift(Operator<R, T> operator) {
        return Observable.create(new OnSubscribeLift<>(this, operator));
    }

    public Observable<T> subscribeOn(Scheduler scheduler) {
        return Observable.create(new OnSubscribe<T>() {
            @Override
            public void call(Observer<T> observer) {
                scheduler.createWorker().schedule(new Runnable() {
                    @Override
                    public void run() {
                        mOnSubscribe.call(observer);
                    }
                });
            }
        });
    }

    public Observable<T> observeOn(Scheduler scheduler) {
        return Observable.create(new OnSubscribe<T>() {
            @Override
            public void call(Observer<T> observer) {

                mOnSubscribe.call(new Observer<T>() {
                    private Scheduler.Worker worker = scheduler.createWorker();
                    @Override
                    public void onNext(T t) {
                        worker.schedule(new Runnable() {
                            @Override
                            public void run() {
                                observer.onNext(t);
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        worker.schedule(new Runnable() {
                            @Override
                            public void run() {
                                observer.onError(e);
                            }
                        });
                    }

                    @Override
                    public void onCompleted() {
                        worker.schedule(new Runnable() {
                            @Override
                            public void run() {
                                observer.onCompleted();
                            }
                        });
                    }
                });
            }
        });
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



