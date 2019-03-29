package com.chant.lib;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class Schedulers {

    private static final Scheduler ioScheduler = new Scheduler(Executors.newSingleThreadExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "io");
        }
    }));

    private static final Scheduler mainScheduler = new Scheduler(Executors.newSingleThreadExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "main");
        }
    }));

    public static Scheduler io() {
        return ioScheduler;
    }

    public static Scheduler main() {
        return mainScheduler;
    }
}
