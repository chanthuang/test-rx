package com.chant.lib;

import java.util.function.Function;

public interface Operator<R, T> extends Function<Observer<R>, Observer<T>> {
}
