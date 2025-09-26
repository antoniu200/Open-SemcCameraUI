// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public abstract class zzlk
{
    private static final ExecutorService zzacD;
    
    static {
        zzacD = Executors.newFixedThreadPool(2, new zza());
    }
    
    public static ExecutorService zzoj() {
        return zzlk.zzacD;
    }
    
    private static final class zza implements ThreadFactory
    {
        private final ThreadFactory zzacE;
        private AtomicInteger zzacF;
        
        private zza() {
            this.zzacE = Executors.defaultThreadFactory();
            this.zzacF = new AtomicInteger(0);
        }
        
        @Override
        public Thread newThread(final Runnable runnable) {
            final Thread thread = this.zzacE.newThread(runnable);
            final StringBuilder sb = new StringBuilder();
            sb.append("GAC_Executor[");
            sb.append(this.zzacF.getAndIncrement());
            sb.append("]");
            thread.setName(sb.toString());
            return thread;
        }
    }
}
