package com.chant.main;

import com.chant.lib.Observable;
import com.chant.lib.Observer;
import com.sun.javafx.tools.packager.Log;

public class Main {

    public static void main(String[] args) {
        Log.setLogger(null);
        Observer<String> stringObserver = new Observer<String>() {
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
        };

        Observable.from(new String[]{"a", "b", "c"})
                .map((s) -> "map: " + s)
                .subscribe(stringObserver);
    }
}
