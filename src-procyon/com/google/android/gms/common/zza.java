// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common;

import android.os.Looper;
import android.content.ComponentName;
import java.util.concurrent.LinkedBlockingQueue;
import android.os.IBinder;
import java.util.concurrent.BlockingQueue;
import android.content.ServiceConnection;

public class zza implements ServiceConnection
{
    boolean zzZW;
    private final BlockingQueue<IBinder> zzZX;
    
    public zza() {
        this.zzZW = false;
        this.zzZX = new LinkedBlockingQueue<IBinder>();
    }
    
    public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
        this.zzZX.add(binder);
    }
    
    public void onServiceDisconnected(final ComponentName componentName) {
    }
    
    public IBinder zzno() throws InterruptedException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException("BlockingServiceConnection.getService() called on main thread");
        }
        if (this.zzZW) {
            throw new IllegalStateException();
        }
        this.zzZW = true;
        return this.zzZX.take();
    }
}
