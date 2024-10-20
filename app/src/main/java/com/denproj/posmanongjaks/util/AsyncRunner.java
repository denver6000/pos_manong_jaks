package com.denproj.posmanongjaks.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AsyncRunner {

    private final static ExecutorService mExecutorService = Executors.newSingleThreadExecutor();
    private final static Handler mHandler = new Handler(Looper.getMainLooper());


    public static <T> void runAsync(Runner<T> runner) {
        mExecutorService.execute(() -> {
            try {

                T result = runner.onBackground();
                runner.onFinished(result);

                mHandler.post(() -> {
                    runner.onUI(result);
                });

            } catch (Exception e) {
                mHandler.post(() -> {
                    runner.onError(e);
                });
            }
        });
    }

    public static <T> void runAsync(MiniRunner<T> miniRunner) {
        mExecutorService.execute(() -> {
            try {
                T result = miniRunner.onBackground();
                mHandler.post(() -> miniRunner.onUI(result));
            } catch (Exception e) {
                mHandler.post(() -> miniRunner.onError(e));
            }

        });
    }

    public interface MiniRunner<T>{
        T onBackground() throws Exception;
        void onUI(T result);
        void onError(Exception e);
    }

    public interface Runner<T> {
        T onBackground() throws Exception;
        void onFinished(T result);
        void onUI(T result);
        void onError(Exception e);
    }


}

