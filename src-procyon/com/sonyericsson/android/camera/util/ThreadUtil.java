// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import android.support.annotation.NonNull;

public class ThreadUtil
{
    @NonNull
    public static ExecutorService buildExecutor(@NonNull final String s) {
        return buildExecutor(s, 5);
    }
    
    @NonNull
    public static ExecutorService buildExecutor(@NonNull final String s, final int n) {
        return Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory(s, n, false));
    }
    
    @NonNull
    public static ExecutorService buildPoolExecutor(@NonNull final String s, final int n) {
        return buildPoolExecutor(s, n, 5);
    }
    
    @NonNull
    public static ExecutorService buildPoolExecutor(@NonNull final String s, final int nThreads, final int n) {
        return Executors.newFixedThreadPool(nThreads, new NamedThreadFactory(s, n, true));
    }
    
    @NonNull
    public static ScheduledExecutorService buildScheduledExecutor(@NonNull final String s) {
        return Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory(s, 5, false));
    }
    
    @NonNull
    public static ScheduledExecutorService buildScheduledExecutor(@NonNull final String s, final int n) {
        return Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory(s, n, false));
    }
    
    private static class NamedThreadFactory implements ThreadFactory
    {
        private final boolean forPool;
        private int mPooledThreadCount;
        private final String name;
        private final int priority;
        
        public NamedThreadFactory(final String name, final int priority, final boolean forPool) {
            this.name = name;
            this.priority = priority;
            this.forPool = forPool;
            this.mPooledThreadCount = 0;
        }
        
        @Override
        public Thread newThread(final Runnable task) {
            final Thread thread = new Thread(task);
            if (!this.forPool) {
                thread.setName(this.name);
            }
            else {
                final StringBuilder sb = new StringBuilder();
                sb.append(this.name);
                sb.append(":");
                sb.append(this.mPooledThreadCount);
                thread.setName(sb.toString());
                ++this.mPooledThreadCount;
            }
            thread.setPriority(this.priority);
            return thread;
        }
    }
}
