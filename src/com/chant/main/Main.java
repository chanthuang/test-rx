package com.chant.main;

import com.chant.lib.Observable;
import com.chant.lib.Observer;
import com.chant.lib.Operator;
import com.chant.lib.Schedulers;
import com.sun.javafx.tools.packager.Log;

import java.util.function.Function;

public class Main {

    public static void main(String[] args) {
        Log.setLogger(null);
//        test();
//        testLift();
        testThread();
    }

    private static void test() {
        Observable.from(new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9})
                .filter((i -> i % 2 == 0))
                .map((i -> i * 10))
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) {
                        return String.valueOf(integer);
                    }
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onNext(String s) {
                        Log.info("onNext: " + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.info("onError: " + e.getMessage());
                    }

                    @Override
                    public void onCompleted() {
                        Log.info("onCompleted");
                    }
                });
    }

    private static void testLift() {
        Operator<Integer, Integer> filterEven = new Operator<Integer, Integer>() {
            @Override
            public Observer<Integer> apply(Observer<Integer> integerObserver) {
                return new Observer<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        if (integer % 2 == 0) {
                            integerObserver.onNext(integer);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        integerObserver.onError(e);
                    }

                    @Override
                    public void onCompleted() {
                        integerObserver.onCompleted();
                    }
                };
            }
        };
        Operator<Integer, Integer> mapMultiplay = new Operator<Integer, Integer>() {
            @Override
            public Observer<Integer> apply(Observer<Integer> integerObserver) {
                return new Observer<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        integerObserver.onNext(integer * 10);
                    }

                    @Override
                    public void onError(Throwable e) {
                        integerObserver.onError(e);
                    }

                    @Override
                    public void onCompleted() {
                        integerObserver.onCompleted();
                    }
                };
            }
        };
        Operator<String, Integer> intToString = new Operator<String, Integer>() {
            @Override
            public Observer<Integer> apply(Observer<String> stringObserver) {
                return new Observer<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        stringObserver.onNext(String.valueOf(integer));
                    }

                    @Override
                    public void onError(Throwable e) {
                        stringObserver.onError(e);
                    }

                    @Override
                    public void onCompleted() {
                        stringObserver.onCompleted();
                    }
                };
            }
        };

        Observable.from(new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9})
                .lift(filterEven)
                .lift(mapMultiplay)
                .lift(intToString)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onNext(String s) {
                        Log.info("onNext: " + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.info("onError: " + e.getMessage());
                    }

                    @Override
                    public void onCompleted() {
                        Log.info("onCompleted");
                    }
                });

    }

    private static void testThread() {
        Observable.fromCallable(() -> {
            Log.info("callable: thread=" + Thread.currentThread().getName());
            return 1;
        })
                .subscribeOn(Schedulers.io())
                .map((i -> {
                    Log.info("map: thread=" + Thread.currentThread().getName());
                    return i * 10;
                }))
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) {
                        return String.valueOf(integer);
                    }
                })
                .observeOn(Schedulers.main())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onNext(String s) {
                        Log.info("onNext: " + s + ", thread=" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.info("onError: " + e.getMessage());
                    }

                    @Override
                    public void onCompleted() {
                        Log.info("onCompleted");
                    }
                });


    }
}
