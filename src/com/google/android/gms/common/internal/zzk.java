package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class zzk implements Handler.Callback {
    private final Handler mHandler;
    private final zza zzafP;
    private final ArrayList<GoogleApiClient.ConnectionCallbacks> zzafQ = new ArrayList<>();
    final ArrayList<GoogleApiClient.ConnectionCallbacks> zzafR = new ArrayList<>();
    private final ArrayList<GoogleApiClient.OnConnectionFailedListener> zzafS = new ArrayList<>();
    private volatile boolean zzafT = false;
    private final AtomicInteger zzafU = new AtomicInteger(0);
    private boolean zzafV = false;
    private final Object zzpd = new Object();

    public interface zza {
        boolean isConnected();

        Bundle zzmS();
    }

    public zzk(Looper looper, zza zzaVar) {
        this.zzafP = zzaVar;
        this.mHandler = new Handler(looper, this);
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        if (message.what != 1) {
            Log.wtf("GmsClientEvents", "Don't know how to handle message: " + message.what, new Exception());
            return false;
        }
        GoogleApiClient.ConnectionCallbacks connectionCallbacks = (GoogleApiClient.ConnectionCallbacks) message.obj;
        synchronized (this.zzpd) {
            if (this.zzafT && this.zzafP.isConnected() && this.zzafQ.contains(connectionCallbacks)) {
                connectionCallbacks.onConnected(this.zzafP.zzmS());
            }
        }
        return true;
    }

    public boolean isConnectionCallbacksRegistered(GoogleApiClient.ConnectionCallbacks connectionCallbacks) {
        boolean zContains;
        zzx.zzw(connectionCallbacks);
        synchronized (this.zzpd) {
            zContains = this.zzafQ.contains(connectionCallbacks);
        }
        return zContains;
    }

    public boolean isConnectionFailedListenerRegistered(GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        boolean zContains;
        zzx.zzw(onConnectionFailedListener);
        synchronized (this.zzpd) {
            zContains = this.zzafS.contains(onConnectionFailedListener);
        }
        return zContains;
    }

    public void registerConnectionCallbacks(GoogleApiClient.ConnectionCallbacks connectionCallbacks) {
        zzx.zzw(connectionCallbacks);
        synchronized (this.zzpd) {
            if (this.zzafQ.contains(connectionCallbacks)) {
                Log.w("GmsClientEvents", "registerConnectionCallbacks(): listener " + connectionCallbacks + " is already registered");
            } else {
                this.zzafQ.add(connectionCallbacks);
            }
        }
        if (this.zzafP.isConnected()) {
            this.mHandler.sendMessage(this.mHandler.obtainMessage(1, connectionCallbacks));
        }
    }

    public void registerConnectionFailedListener(GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        zzx.zzw(onConnectionFailedListener);
        synchronized (this.zzpd) {
            if (this.zzafS.contains(onConnectionFailedListener)) {
                Log.w("GmsClientEvents", "registerConnectionFailedListener(): listener " + onConnectionFailedListener + " is already registered");
            } else {
                this.zzafS.add(onConnectionFailedListener);
            }
        }
    }

    public void unregisterConnectionCallbacks(GoogleApiClient.ConnectionCallbacks connectionCallbacks) {
        zzx.zzw(connectionCallbacks);
        synchronized (this.zzpd) {
            if (!this.zzafQ.remove(connectionCallbacks)) {
                Log.w("GmsClientEvents", "unregisterConnectionCallbacks(): listener " + connectionCallbacks + " not found");
            } else if (this.zzafV) {
                this.zzafR.add(connectionCallbacks);
            }
        }
    }

    public void unregisterConnectionFailedListener(GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        zzx.zzw(onConnectionFailedListener);
        synchronized (this.zzpd) {
            if (!this.zzafS.remove(onConnectionFailedListener)) {
                Log.w("GmsClientEvents", "unregisterConnectionFailedListener(): listener " + onConnectionFailedListener + " not found");
            }
        }
    }

    public void zzbG(int i) {
        zzx.zza(Looper.myLooper() == this.mHandler.getLooper(), "onUnintentionalDisconnection must only be called on the Handler thread");
        this.mHandler.removeMessages(1);
        synchronized (this.zzpd) {
            this.zzafV = true;
            ArrayList arrayList = new ArrayList(this.zzafQ);
            int i2 = this.zzafU.get();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                GoogleApiClient.ConnectionCallbacks connectionCallbacks = (GoogleApiClient.ConnectionCallbacks) it.next();
                if (!this.zzafT || this.zzafU.get() != i2) {
                    break;
                } else if (this.zzafQ.contains(connectionCallbacks)) {
                    connectionCallbacks.onConnectionSuspended(i);
                }
            }
            this.zzafR.clear();
            this.zzafV = false;
        }
    }

    public void zzh(Bundle bundle) {
        boolean z = true;
        zzx.zza(Looper.myLooper() == this.mHandler.getLooper(), "onConnectionSuccess must only be called on the Handler thread");
        synchronized (this.zzpd) {
            zzx.zzZ(!this.zzafV);
            this.mHandler.removeMessages(1);
            this.zzafV = true;
            if (this.zzafR.size() != 0) {
                z = false;
            }
            zzx.zzZ(z);
            ArrayList arrayList = new ArrayList(this.zzafQ);
            int i = this.zzafU.get();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                GoogleApiClient.ConnectionCallbacks connectionCallbacks = (GoogleApiClient.ConnectionCallbacks) it.next();
                if (!this.zzafT || !this.zzafP.isConnected() || this.zzafU.get() != i) {
                    break;
                } else if (!this.zzafR.contains(connectionCallbacks)) {
                    connectionCallbacks.onConnected(bundle);
                }
            }
            this.zzafR.clear();
            this.zzafV = false;
        }
    }

    public void zzi(ConnectionResult connectionResult) {
        zzx.zza(Looper.myLooper() == this.mHandler.getLooper(), "onConnectionFailure must only be called on the Handler thread");
        this.mHandler.removeMessages(1);
        synchronized (this.zzpd) {
            ArrayList arrayList = new ArrayList(this.zzafS);
            int i = this.zzafU.get();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener = (GoogleApiClient.OnConnectionFailedListener) it.next();
                if (this.zzafT && this.zzafU.get() == i) {
                    if (this.zzafS.contains(onConnectionFailedListener)) {
                        onConnectionFailedListener.onConnectionFailed(connectionResult);
                    }
                }
                return;
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
}
