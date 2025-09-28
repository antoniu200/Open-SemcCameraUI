package com.sonyericsson.android.camera.util;

import android.support.annotation.NonNull;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ThreadUtil {
    @NonNull
    public static ExecutorService buildExecutor(@NonNull String str) {
        return buildExecutor(str, 5);
    }

    @NonNull
    public static ExecutorService buildExecutor(@NonNull String str, int i) {
        return Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory(str, i, false));
    }

    @NonNull
    public static ScheduledExecutorService buildScheduledExecutor(@NonNull String str) {
        return Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory(str, 5, false));
    }

    @NonNull
    public static ScheduledExecutorService buildScheduledExecutor(@NonNull String str, int i) {
        return Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory(str, i, false));
    }

    @NonNull
    public static ExecutorService buildPoolExecutor(@NonNull String str, int i) {
        return buildPoolExecutor(str, i, 5);
    }

    @NonNull
    public static ExecutorService buildPoolExecutor(@NonNull String str, int i, int i2) {
        return Executors.newFixedThreadPool(i, new NamedThreadFactory(str, i2, true));
    }

    private static class NamedThreadFactory implements ThreadFactory {
        private final boolean forPool;
        private int mPooledThreadCount = 0;
        private final String name;
        private final int priority;

        public NamedThreadFactory(String str, int i, boolean z) {
            this.name = str;
            this.priority = i;
            this.forPool = z;
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            if (!this.forPool) {
                thread.setName(this.name);
            } else {
                thread.setName(this.name + ":" + this.mPooledThreadCount);
                this.mPooledThreadCount = this.mPooledThreadCount + 1;
            }
            thread.setPriority(this.priority);
            return thread;
        }
    }
}
