package com.google.android.gms.internal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class zzlk {
    private static final ExecutorService zzacD = Executors.newFixedThreadPool(2, new zza());

    private static final class zza implements ThreadFactory {
        private final ThreadFactory zzacE;
        private AtomicInteger zzacF;

        private zza() {
            this.zzacE = Executors.defaultThreadFactory();
            this.zzacF = new AtomicInteger(0);
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            Thread threadNewThread = this.zzacE.newThread(runnable);
            threadNewThread.setName("GAC_Executor[" + this.zzacF.getAndIncrement() + "]");
            return threadNewThread;
        }
    }

    public static ExecutorService zzoj() {
        return zzacD;
    }
}
