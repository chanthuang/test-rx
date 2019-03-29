package com.chant.lib;

public interface OnSubscribe<T> {
    void call(Observer<T> observer);
}
