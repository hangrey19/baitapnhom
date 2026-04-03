package com.example.baitapnhom.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {
    private static final Executor DISK_IO = Executors.newSingleThreadExecutor();
    private static final Executor MAIN_THREAD = new MainThreadExecutor();

    public static Executor diskIO() {
        return DISK_IO;
    }

    public static Executor mainThread() {
        return MAIN_THREAD;
    }

    private static class MainThreadExecutor implements Executor {
        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    }
}
