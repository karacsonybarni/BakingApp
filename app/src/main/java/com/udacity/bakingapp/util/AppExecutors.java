package com.udacity.bakingapp.util;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler;

        @Override
        public void execute(@NonNull Runnable command) {
            getMainThreadHandler().post(command);
        }

        private Handler getMainThreadHandler() {
            if (mainThreadHandler == null) {
                mainThreadHandler = new Handler(Looper.getMainLooper());
            }
            return mainThreadHandler;
        }
    }

    private static final Object LOCK = new Object();
    private static AppExecutors sInstance;
    private final Executor diskIO;
    private final Executor mainThread;

    private AppExecutors(Executor diskIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.mainThread = mainThread;
    }

    public static AppExecutors getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance =
                        new AppExecutors(
                                Executors.newSingleThreadExecutor(),
                                new MainThreadExecutor());
            }
        }
        return sInstance;
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor mainThread() {
        return mainThread;
    }
}


