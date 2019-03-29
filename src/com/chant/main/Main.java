package com.chant.main;

import com.chant.lib.Observable;
import com.chant.lib.Observer;
import com.sun.javafx.tools.packager.Log;

public class Main {

    public static void main(String[] args) {
        Log.setLogger(null);
        Observer<Integer> integerObserver = new Observer<Integer>() {
            @Override
            public void onNext(Integer i) {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onCompleted() {
            }
        };

        Observable.from(new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9})
                .filter((i -> i % 2 == 0))
                .map((i -> i * 10))
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onNext(Integer i) {
                        Log.info("onNext: " + i);
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
