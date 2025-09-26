// 
// Decompiled by Procyon v0.6.0
// 

package android.arch.core.executor;

import android.os.Looper;
import java.util.concurrent.Executors;
import android.support.annotation.Nullable;
import android.os.Handler;
import java.util.concurrent.ExecutorService;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class DefaultTaskExecutor extends TaskExecutor
{
    private ExecutorService mDiskIO;
    private final Object mLock;
    @Nullable
    private volatile Handler mMainHandler;
    
    public DefaultTaskExecutor() {
        this.mLock = new Object();
        this.mDiskIO = Executors.newFixedThreadPool(2);
    }
    
    @Override
    public void executeOnDiskIO(final Runnable runnable) {
        this.mDiskIO.execute(runnable);
    }
    
    @Override
    public boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
    
    @Override
    public void postToMainThread(final Runnable runnable) {
        if (this.mMainHandler == null) {
            synchronized (this.mLock) {
                if (this.mMainHandler == null) {
                    this.mMainHandler = new Handler(Looper.getMainLooper());
                }
            }
        }
        this.mMainHandler.post(runnable);
    }
}
