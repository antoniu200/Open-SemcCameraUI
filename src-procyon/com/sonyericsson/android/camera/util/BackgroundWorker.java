// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util;

import java.util.concurrent.TimeUnit;
import android.text.TextUtils;
import java.util.concurrent.CountDownLatch;
import android.os.Looper;
import android.os.Handler;
import java.util.concurrent.ExecutorService;

public class BackgroundWorker
{
    private static final int SHUTDOWN_TIMEOUT_MILLISECONDS = 2000;
    private static final String THREAD_NAME = "BgWorker";
    private final ExecutorService mExecutor;
    private final Handler mHandler;
    private Looper mLooper;
    
    public BackgroundWorker(final String s) {
        this.mLooper = null;
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        String s2 = s;
        if (TextUtils.isEmpty((CharSequence)s)) {
            s2 = "BgWorker";
        }
        (this.mExecutor = ThreadUtil.buildExecutor(s2)).execute(new Runnable(this, countDownLatch) {
            final BackgroundWorker this$0;
            final CountDownLatch val$readySignal;
            
            @Override
            public void run() {
                Looper.prepare();
                this.this$0.mLooper = Looper.myLooper();
                this.val$readySignal.countDown();
                Looper.loop();
            }
        });
        while (true) {
            try {
                countDownLatch.await();
                this.mHandler = new Handler(this.mLooper);
            }
            catch (final InterruptedException ex) {
                continue;
            }
            break;
        }
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
