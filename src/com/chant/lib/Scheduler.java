package com.chant.lib;

import java.util.concurrent.Executor;

public class Scheduler {

    private Executor executor;

    public Scheduler(Executor executor) {
        this.executor = executor;
    }

    public Worker createWorker() {
        return new Worker(executor);
    }

    public static class Worker {
        private Executor executor;

        public Worker(Executor executor) {
            this.executor = executor;
        }

        public void schedule(Runnable r) {
            executor.execute(r);
        }
    }
}
