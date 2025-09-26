package com.sonyericsson.android.camera.util;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class BackgroundWorker {
    private static final int SHUTDOWN_TIMEOUT_MILLISECONDS = 2000;
    private static final String THREAD_NAME = "BgWorker";
    private final ExecutorService mExecutor;
    private final Handler mHandler;
    private Looper mLooper = null;

    public BackgroundWorker(String str) throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        this.mExecutor = ThreadUtil.buildExecutor(TextUtils.isEmpty(str) ? THREAD_NAME : str);
        this.mExecutor.execute(new Runnable() { // from class: com.sonyericsson.android.camera.util.BackgroundWorker.1
            @Override // java.lang.Runnable
            public void run() {
                Looper.prepare();
                BackgroundWorker.this.mLooper = Looper.myLooper();
                countDownLatch.countDown();
                Looper.loop();
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException unused) {
        }
        this.mHandler = new Handler(this.mLooper);
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    public void quit() throws InterruptedException {
        this.mLooper.quitSafely();
        this.mExecutor.shutdown();
        this.mExecutor.awaitTermination(2000L, TimeUnit.MILLISECONDS);
    }
}
