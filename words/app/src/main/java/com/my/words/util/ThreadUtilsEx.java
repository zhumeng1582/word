package com.my.words.util;

import com.blankj.utilcode.util.ThreadUtils;

import java.util.concurrent.TimeUnit;

public class ThreadUtilsEx {

    public static <T> void executeByCached(final ThreadUtils.SimpleTask<T> task) {
        ThreadUtils.executeByCached(task);
    }

    public static <T> void executeByCachedAtFixRate(ThreadUtils.SimpleTask<T> task,
                                                    long period,
                                                    TimeUnit unit) {
        ThreadUtils.executeByCachedAtFixRate(task, period, unit);
    }

    public static <T> void executeByCachedWithDelay(ThreadUtils.SimpleTask<T> task,
                                                    long period,
                                                    TimeUnit unit) {
        ThreadUtils.executeByCachedWithDelay(task, period, unit);
    }

    public static void executeThread(Runnable runnable) {
        new Thread(runnable).start();//启动线程
    }

    public static void executeThreadDelay(Runnable runnable, int delay) {
        runOnUiThreadDelayed(() -> {
            new Thread(runnable).start();//启动线程
        }, delay);

    }

    public static void runOnUiThreadDelayed(Runnable runnable, int delay) {
        ThreadUtils.runOnUiThreadDelayed(runnable, delay);
    }

    public static void runOnUiThread(Runnable runnable) {
        ThreadUtils.runOnUiThread(runnable);
    }
}
