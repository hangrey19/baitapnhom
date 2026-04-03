package com.example.baitapnhom.utils;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {

    private static volatile AppExecutors INSTANCE;

    private final Executor diskIO;
    private final Executor mainThread;
    private final Executor networkIO;

    private AppExecutors() {
        diskIO    = Executors.newSingleThreadExecutor();
        networkIO = Executors.newFixedThreadPool(3);
        mainThread = new MainThreadExecutor();
    }

    public static AppExecutors getInstance() {
        if (INSTANCE == null) {
            synchronized (AppExecutors.class) {
                if (INSTANCE == null) INSTANCE = new AppExecutors();
            }
        }
        return INSTANCE;
    }

    /** Background thread – use for all Room queries */
    public Executor diskIO()    { return diskIO; }

    /** Main / UI thread */
    public Executor mainThread(){ return mainThread; }

    /** Network thread */
    public Executor networkIO() { return networkIO; }

    private static class MainThreadExecutor implements Executor {
        private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(Runnable command) { mainThreadHandler.post(command); }
    }
}