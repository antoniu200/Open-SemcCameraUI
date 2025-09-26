// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.internal;

import com.google.android.gms.common.ConnectionResult;
import android.os.Bundle;
import java.util.Iterator;
import java.util.Collection;
import android.util.Log;
import android.os.Message;
import android.os.Looper;
import java.util.concurrent.atomic.AtomicInteger;
import com.google.android.gms.common.api.GoogleApiClient;
import java.util.ArrayList;
import android.os.Handler;
import android.os.Handler$Callback;

public final class zzk implements Handler$Callback
{
    private final Handler mHandler;
    private final zza zzafP;
    private final ArrayList<GoogleApiClient.ConnectionCallbacks> zzafQ;
    final ArrayList<GoogleApiClient.ConnectionCallbacks> zzafR;
    private final ArrayList<GoogleApiClient.OnConnectionFailedListener> zzafS;
    private volatile boolean zzafT;
    private final AtomicInteger zzafU;
    private boolean zzafV;
    private final Object zzpd;
    
    public zzk(final Looper looper, final zza zzafP) {
        this.zzafQ = new ArrayList<GoogleApiClient.ConnectionCallbacks>();
        this.zzafR = new ArrayList<GoogleApiClient.ConnectionCallbacks>();
        this.zzafS = new ArrayList<GoogleApiClient.OnConnectionFailedListener>();
        this.zzafT = false;
        this.zzafU = new AtomicInteger(0);
        this.zzafV = false;
        this.zzpd = new Object();
        this.zzafP = zzafP;
        this.mHandler = new Handler(looper, (Handler$Callback)this);
    }
    
    public boolean handleMessage(final Message message) {
        if (message.what == 1) {
            final GoogleApiClient.ConnectionCallbacks o = (GoogleApiClient.ConnectionCallbacks)message.obj;
            synchronized (this.zzpd) {
                if (this.zzafT && this.zzafP.isConnected() && this.zzafQ.contains(o)) {
                    o.onConnected(this.zzafP.zzmS());
                }
                return true;
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Don't know how to handle message: ");
        sb.append(message.what);
        Log.wtf("GmsClientEvents", sb.toString(), (Throwable)new Exception());
        return false;
    }
    
    public boolean isConnectionCallbacksRegistered(final GoogleApiClient.ConnectionCallbacks o) {
        zzx.zzw(o);
        synchronized (this.zzpd) {
            return this.zzafQ.contains(o);
        }
    }
    
    public boolean isConnectionFailedListenerRegistered(final GoogleApiClient.OnConnectionFailedListener o) {
        zzx.zzw(o);
        synchronized (this.zzpd) {
            return this.zzafS.contains(o);
        }
    }
    
    public void registerConnectionCallbacks(final GoogleApiClient.ConnectionCallbacks e) {
        zzx.zzw(e);
        synchronized (this.zzpd) {
            if (this.zzafQ.contains(e)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("registerConnectionCallbacks(): listener ");
                sb.append(e);
                sb.append(" is already registered");
                Log.w("GmsClientEvents", sb.toString());
            }
            else {
                this.zzafQ.add(e);
            }
            monitorexit(this.zzpd);
            if (this.zzafP.isConnected()) {
                this.mHandler.sendMessage(this.mHandler.obtainMessage(1, (Object)e));
            }
        }
    }
    
    public void registerConnectionFailedListener(final GoogleApiClient.OnConnectionFailedListener e) {
        zzx.zzw(e);
        synchronized (this.zzpd) {
            if (this.zzafS.contains(e)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("registerConnectionFailedListener(): listener ");
                sb.append(e);
                sb.append(" is already registered");
                Log.w("GmsClientEvents", sb.toString());
            }
            else {
                this.zzafS.add(e);
            }
        }
    }
    
    public void unregisterConnectionCallbacks(final GoogleApiClient.ConnectionCallbacks e) {
        zzx.zzw(e);
        synchronized (this.zzpd) {
            if (!this.zzafQ.remove(e)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("unregisterConnectionCallbacks(): listener ");
                sb.append(e);
                sb.append(" not found");
                Log.w("GmsClientEvents", sb.toString());
            }
            else if (this.zzafV) {
                this.zzafR.add(e);
            }
        }
    }
    
    public void unregisterConnectionFailedListener(final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        zzx.zzw(onConnectionFailedListener);
        synchronized (this.zzpd) {
            if (!this.zzafS.remove(onConnectionFailedListener)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("unregisterConnectionFailedListener(): listener ");
                sb.append(onConnectionFailedListener);
                sb.append(" not found");
                Log.w("GmsClientEvents", sb.toString());
            }
        }
    }
    
    public void zzbG(final int n) {
        zzx.zza(Looper.myLooper() == this.mHandler.getLooper(), (Object)"onUnintentionalDisconnection must only be called on the Handler thread");
        this.mHandler.removeMessages(1);
        synchronized (this.zzpd) {
            this.zzafV = true;
            final ArrayList list = new ArrayList(this.zzafQ);
            final int value = this.zzafU.get();
            for (final GoogleApiClient.ConnectionCallbacks o : list) {
                if (!this.zzafT) {
                    break;
                }
                if (this.zzafU.get() != value) {
                    break;
                }
                if (!this.zzafQ.contains(o)) {
                    continue;
                }
                o.onConnectionSuspended(n);
            }
            this.zzafR.clear();
            this.zzafV = false;
        }
    }
    
    public void zzh(final Bundle bundle) {
        final Looper myLooper = Looper.myLooper();
        final Looper looper = this.mHandler.getLooper();
        final boolean b = true;
        zzx.zza(myLooper == looper, (Object)"onConnectionSuccess must only be called on the Handler thread");
        synchronized (this.zzpd) {
            zzx.zzZ(this.zzafV ^ true);
            this.mHandler.removeMessages(1);
            this.zzafV = true;
            zzx.zzZ(this.zzafR.size() == 0 && b);
            final ArrayList list = new ArrayList(this.zzafQ);
            final int value = this.zzafU.get();
            for (final GoogleApiClient.ConnectionCallbacks o : list) {
                if (!this.zzafT || !this.zzafP.isConnected()) {
                    break;
                }
                if (this.zzafU.get() != value) {
                    break;
                }
                if (this.zzafR.contains(o)) {
                    continue;
                }
                o.onConnected(bundle);
            }
            this.zzafR.clear();
            this.zzafV = false;
        }
    }
    
    public void zzi(final ConnectionResult connectionResult) {
        zzx.zza(Looper.myLooper() == this.mHandler.getLooper(), (Object)"onConnectionFailure must only be called on the Handler thread");
        this.mHandler.removeMessages(1);
        synchronized (this.zzpd) {
            final ArrayList list = new ArrayList(this.zzafS);
            final int value = this.zzafU.get();
            for (final GoogleApiClient.OnConnectionFailedListener o : list) {
                if (!this.zzafT || this.zzafU.get() != value) {
                    return;
                }
                if (!this.zzafS.contains(o)) {
                    continue;
                }
                o.onConnectionFailed(connectionResult);
            }
        }
    }
    
    public void zzpk() {
        this.zzafT = false;
        this.zzafU.incrementAndGet();
    }
    
    public void zzpl() {
        this.zzafT = true;
    }
    
    public interface zza
    {
        boolean isConnected();
        
        Bundle zzmS();
    }
}
