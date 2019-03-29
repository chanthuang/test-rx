package com.chant.lib;

public class OnSubscribeLift<T, R> implements OnSubscribe<R> {

    private Observable<T> mSource;
    private Operator<R, T> mOperator;

    public OnSubscribeLift(Observable<T> mSource, Operator<R, T> mOperator) {
        this.mSource = mSource;
        this.mOperator = mOperator;
    }

    @Override
    public void call(Observer<R> observer) {
        Observer<T> operatedObserver = mOperator.apply(observer);
        mSource.subscribe(operatedObserver);
    }

}
