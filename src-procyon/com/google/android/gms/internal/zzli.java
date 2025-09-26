// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import android.os.DeadObjectException;
import java.lang.ref.WeakReference;
import android.os.Message;
import java.io.Writer;
import java.io.StringWriter;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import java.io.PrintWriter;
import java.io.FileDescriptor;
import android.os.Handler;
import java.util.concurrent.atomic.AtomicReference;
import com.google.android.gms.common.api.PendingResult;
import java.util.concurrent.TimeUnit;
import android.app.PendingIntent;
import com.google.android.gms.common.internal.zzx;
import android.os.RemoteException;
import android.os.IBinder$DeathRecipient;
import android.os.IBinder;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.ResultCallback;
import java.util.Iterator;
import com.google.android.gms.common.internal.zzac;
import android.os.Bundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Collections;
import java.util.WeakHashMap;
import java.util.HashSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayList;
import com.google.android.gms.common.api.zza;
import com.google.android.gms.common.api.Scope;
import java.util.Set;
import com.google.android.gms.common.ConnectionResult;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import com.google.android.gms.common.internal.zzk;
import java.util.concurrent.locks.Condition;
import java.util.Map;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.GoogleApiAvailability;
import android.os.Looper;
import android.content.Context;
import com.google.android.gms.common.api.GoogleApiClient;

public final class zzli extends GoogleApiClient
{
    private final Context mContext;
    private final int zzaaM;
    private final Looper zzaaO;
    private final GoogleApiAvailability zzaaP;
    final Api.zza<? extends zzqw, zzqx> zzaaQ;
    final com.google.android.gms.common.internal.zzf zzabI;
    final Map<Api<?>, Integer> zzabJ;
    private final Condition zzabY;
    final zzk zzabZ;
    private final Lock zzabt;
    final Queue<zzf<?>> zzaca;
    private volatile boolean zzacb;
    private long zzacc;
    private long zzacd;
    private final zza zzace;
    zzd zzacf;
    final Map<Api.zzc<?>, Api.zzb> zzacg;
    final Map<Api.zzc<?>, ConnectionResult> zzach;
    Set<Scope> zzaci;
    private volatile zzlj zzacj;
    private ConnectionResult zzack;
    private final Set<zzlm<?>> zzacl;
    final Set<zzf<?>> zzacm;
    private com.google.android.gms.common.api.zza zzacn;
    private final zze zzaco;
    private final ConnectionCallbacks zzacp;
    private final zzk.zza zzacq;
    
    public zzli(final Context mContext, final Looper zzaaO, final com.google.android.gms.common.internal.zzf zzabI, final GoogleApiAvailability zzaaP, final Api.zza<? extends zzqw, zzqx> zzaaQ, final Map<Api<?>, Api.ApiOptions> map, final ArrayList<ConnectionCallbacks> list, final ArrayList<OnConnectionFailedListener> list2, int n) {
        this.zzabt = new ReentrantLock();
        this.zzaca = new LinkedList<zzf<?>>();
        this.zzacc = 120000L;
        this.zzacd = 5000L;
        this.zzacg = new HashMap<Api.zzc<?>, Api.zzb>();
        this.zzach = new HashMap<Api.zzc<?>, ConnectionResult>();
        this.zzaci = new HashSet<Scope>();
        this.zzack = null;
        this.zzacl = Collections.newSetFromMap(new WeakHashMap<zzlm<?>, Boolean>());
        this.zzacm = Collections.newSetFromMap(new ConcurrentHashMap<zzf<?>, Boolean>(16, 0.75f, 2));
        this.zzaco = (zze)new zze() {
            final zzli zzacr;
            
            @Override
            public void zzc(final zzf<?> zzf) {
                this.zzacr.zzacm.remove(zzf);
                if (zzf.zznF() != null && this.zzacr.zzacn != null) {
                    this.zzacr.zzacn.remove(zzf.zznF());
                }
            }
        };
        this.zzacp = new ConnectionCallbacks() {
            final zzli zzacr;
            
            @Override
            public void onConnected(final Bundle bundle) {
                this.zzacr.zzabt.lock();
                try {
                    this.zzacr.zzacj.onConnected(bundle);
                }
                finally {
                    this.zzacr.zzabt.unlock();
                }
            }
            
            @Override
            public void onConnectionSuspended(final int n) {
                this.zzacr.zzabt.lock();
                try {
                    this.zzacr.zzacj.onConnectionSuspended(n);
                }
                finally {
                    this.zzacr.zzabt.unlock();
                }
            }
        };
        this.zzacq = new zzk.zza() {
            final zzli zzacr;
            
            @Override
            public boolean isConnected() {
                return this.zzacr.isConnected();
            }
            
            @Override
            public Bundle zzmS() {
                return null;
            }
        };
        this.mContext = mContext;
        this.zzabZ = new zzk(zzaaO, this.zzacq);
        this.zzaaO = zzaaO;
        this.zzace = new zza(zzaaO);
        this.zzaaP = zzaaP;
        this.zzaaM = n;
        this.zzabJ = new HashMap<Api<?>, Integer>();
        this.zzabY = this.zzabt.newCondition();
        this.zzacj = new zzlh(this);
        final Iterator<ConnectionCallbacks> iterator = list.iterator();
        while (iterator.hasNext()) {
            this.zzabZ.registerConnectionCallbacks(iterator.next());
        }
        final Iterator<OnConnectionFailedListener> iterator2 = list2.iterator();
        while (iterator2.hasNext()) {
            this.zzabZ.registerConnectionFailedListener(iterator2.next());
        }
        final Map<Api<?>, com.google.android.gms.common.internal.zzf.zza> zzoM = zzabI.zzoM();
        for (final Api api : map.keySet()) {
            final Api.ApiOptions value = map.get(api);
            n = 0;
            if (zzoM.get(api) != null) {
                if (((com.google.android.gms.common.internal.zzf.zza)zzoM.get(api)).zzafk) {
                    n = 1;
                }
                else {
                    n = 2;
                }
            }
            this.zzabJ.put(api, n);
            zzac zzac;
            if (api.zzny()) {
                zzac = zza((Api.zze<?, Object>)api.zznw(), value, mContext, zzaaO, zzabI, this.zzacp, this.zza(api, n));
            }
            else {
                zzac = zza((Api.zza<zzac, Object>)api.zznv(), value, mContext, zzaaO, zzabI, this.zzacp, this.zza(api, n));
            }
            this.zzacg.put(api.zznx(), zzac);
        }
        this.zzabI = zzabI;
        this.zzaaQ = zzaaQ;
    }
    
    private void resume() {
        this.zzabt.lock();
        try {
            if (this.zzoc()) {
                this.connect();
            }
        }
        finally {
            this.zzabt.unlock();
        }
    }
    
    private static <C extends Api.zzb, O> C zza(final Api.zza<C, O> zza, final Object o, final Context context, final Looper looper, final com.google.android.gms.common.internal.zzf zzf, final ConnectionCallbacks connectionCallbacks, final OnConnectionFailedListener onConnectionFailedListener) {
        return zza.zza(context, looper, zzf, (O)o, connectionCallbacks, onConnectionFailedListener);
    }
    
    private OnConnectionFailedListener zza(final Api<?> api, final int n) {
        return new OnConnectionFailedListener(this, api, n) {
            final zzli zzacr;
            final Api zzacs;
            final int zzact;
            
            @Override
            public void onConnectionFailed(final ConnectionResult connectionResult) {
                this.zzacr.zzabt.lock();
                try {
                    this.zzacr.zzacj.zza(connectionResult, this.zzacs, this.zzact);
                }
                finally {
                    this.zzacr.zzabt.unlock();
                }
            }
        };
    }
    
    private static <C extends Api.zzd, O> zzac zza(final Api.zze<C, O> zze, final Object o, final Context context, final Looper looper, final com.google.android.gms.common.internal.zzf zzf, final ConnectionCallbacks connectionCallbacks, final OnConnectionFailedListener onConnectionFailedListener) {
        return new zzac(context, looper, zze.zznA(), connectionCallbacks, onConnectionFailedListener, zzf, zze.zzn((O)o));
    }
    
    private void zza(final GoogleApiClient googleApiClient, final zzlo zzlo, final boolean b) {
        zzlx.zzagw.zzb(googleApiClient).setResultCallback(new ResultCallback<Status>(this, zzlo, b, googleApiClient) {
            final GoogleApiClient zzWT;
            final zzli zzacr;
            final zzlo zzacv;
            final boolean zzacw;
            
            public void zzo(final Status status) {
                if (status.isSuccess() && this.zzacr.isConnected()) {
                    this.zzacr.reconnect();
                }
                this.zzacv.zzb(status);
                if (this.zzacw) {
                    this.zzWT.disconnect();
                }
            }
        });
    }
    
    private static void zza(final zzf<?> zzf, final com.google.android.gms.common.api.zza zza, final IBinder binder) {
        if (zzf.isReady()) {
            zzf.zza(new zzc((zzf)zzf, zza, binder));
            return;
        }
        Label_0068: {
            if (binder == null || !binder.isBinderAlive()) {
                break Label_0068;
            }
            final zzc zzc = new zzc((zzf)zzf, zza, binder);
            zzf.zza(zzc);
            while (true) {
                try {
                    binder.linkToDeath((IBinder$DeathRecipient)zzc, 0);
                    return;
                    zzf.cancel();
                    zza.remove(zzf.zznF());
                    return;
                    zzf.zza(null);
                    continue;
                }
                catch (final RemoteException ex) {
                    continue;
                }
                break;
            }
        }
    }
    
    private void zzod() {
        this.zzabt.lock();
        try {
            if (this.zzof()) {
                this.connect();
            }
        }
        finally {
            this.zzabt.unlock();
        }
    }
    
    @Override
    public ConnectionResult blockingConnect() {
        zzx.zza(Looper.myLooper() != Looper.getMainLooper(), (Object)"blockingConnect must not be called on the UI thread");
        this.zzabt.lock();
        try {
            this.connect();
            while (this.isConnecting()) {
                ConnectionResult connectionResult;
                try {
                    this.zzabY.await();
                    continue;
                }
                catch (final InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    connectionResult = new ConnectionResult(15, null);
                }
                return connectionResult;
            }
            if (this.isConnected()) {
                return ConnectionResult.zzZY;
            }
            if (this.zzack != null) {
                return this.zzack;
            }
            return new ConnectionResult(13, null);
        }
        finally {
            this.zzabt.unlock();
        }
    }
    
    @Override
    public ConnectionResult blockingConnect(long duration, final TimeUnit timeUnit) {
        zzx.zza(Looper.myLooper() != Looper.getMainLooper(), (Object)"blockingConnect must not be called on the UI thread");
        zzx.zzb(timeUnit, "TimeUnit must not be null");
        this.zzabt.lock();
        try {
            this.connect();
            duration = timeUnit.toNanos(duration);
            while (true) {
                Label_0129: {
                    if (!this.isConnecting()) {
                        break Label_0129;
                    }
                    try {
                        if ((duration = this.zzabY.awaitNanos(duration)) <= 0L) {
                            return new ConnectionResult(14, null);
                        }
                        continue;
                    }
                    catch (final InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        return new ConnectionResult(15, null);
                    }
                }
                if (this.isConnected()) {
                    return ConnectionResult.zzZY;
                }
                if (this.zzack != null) {
                    return this.zzack;
                }
                return new ConnectionResult(13, null);
            }
        }
        finally {
            this.zzabt.unlock();
        }
    }
    
    @Override
    public PendingResult<Status> clearDefaultAccountAndReconnect() {
        zzx.zza(this.isConnected(), (Object)"GoogleApiClient is not connected yet.");
        final zzlo zzlo = new zzlo(this);
        if (this.zzacg.containsKey(zzlx.zzRk)) {
            this.zza(this, zzlo, false);
            return zzlo;
        }
        final AtomicReference atomicReference = new AtomicReference();
        final GoogleApiClient build = new Builder(this.mContext).addApi(zzlx.API).addConnectionCallbacks(new ConnectionCallbacks(this, atomicReference, zzlo) {
            final zzli zzacr;
            final AtomicReference zzacu;
            final zzlo zzacv;
            
            @Override
            public void onConnected(final Bundle bundle) {
                this.zzacr.zza(this.zzacu.get(), this.zzacv, true);
            }
            
            @Override
            public void onConnectionSuspended(final int n) {
            }
        }).addOnConnectionFailedListener(new OnConnectionFailedListener(this, zzlo) {
            final zzli zzacr;
            final zzlo zzacv;
            
            @Override
            public void onConnectionFailed(final ConnectionResult connectionResult) {
                this.zzacv.zzb(new Status(8));
            }
        }).setHandler(this.zzace).build();
        atomicReference.set(build);
        build.connect();
        return zzlo;
    }
    
    @Override
    public void connect() {
        this.zzabt.lock();
        try {
            this.zzacj.connect();
        }
        finally {
            this.zzabt.unlock();
        }
    }
    
    @Override
    public void disconnect() {
        this.zzabt.lock();
        try {
            this.zzof();
            this.zzacj.disconnect();
        }
        finally {
            this.zzabt.unlock();
        }
    }
    
    @Override
    public void dump(final String csq, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
        printWriter.append(csq).append("mState=").append(this.zzacj.getName());
        printWriter.append(" mResuming=").print(this.zzacb);
        printWriter.append(" mWorkQueue.size()=").print(this.zzaca.size());
        printWriter.append(" mUnconsumedRunners.size()=").println(this.zzacm.size());
        final StringBuilder sb = new StringBuilder();
        sb.append(csq);
        sb.append("  ");
        final String string = sb.toString();
        for (final Api api : this.zzabJ.keySet()) {
            printWriter.append(csq).append(api.getName()).println(":");
            this.zzacg.get(api.zznx()).dump(string, fileDescriptor, printWriter, array);
        }
    }
    
    @Override
    public ConnectionResult getConnectionResult(final Api<?> api) {
        final Api.zzc zznx = api.zznx();
        this.zzabt.lock();
        try {
            if (!this.isConnected() && !this.zzoc()) {
                throw new IllegalStateException("Cannot invoke getConnectionResult unless GoogleApiClient is connected");
            }
            if (this.zzacg.containsKey(zznx)) {
                ConnectionResult zzZY;
                if (this.zzacg.get(zznx).isConnected()) {
                    zzZY = ConnectionResult.zzZY;
                }
                else if (this.zzach.containsKey(zznx)) {
                    zzZY = this.zzach.get(zznx);
                }
                else {
                    Log.i("GoogleApiClientImpl", this.zzog());
                    final StringBuilder sb = new StringBuilder();
                    sb.append(api.getName());
                    sb.append(" requested in getConnectionResult");
                    sb.append(" is not connected but is not present in the failed connections map");
                    Log.wtf("GoogleApiClientImpl", sb.toString(), (Throwable)new Exception());
                    zzZY = new ConnectionResult(8, null);
                }
                return zzZY;
            }
            this.zzabt.unlock();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(api.getName());
            sb2.append(" was never registered with GoogleApiClient");
            throw new IllegalArgumentException(sb2.toString());
        }
        finally {
            this.zzabt.unlock();
        }
    }
    
    @Override
    public Context getContext() {
        return this.mContext;
    }
    
    @Override
    public Looper getLooper() {
        return this.zzaaO;
    }
    
    @Override
    public int getSessionId() {
        return System.identityHashCode(this);
    }
    
    @Override
    public boolean hasConnectedApi(final Api<?> api) {
        final Api.zzb zzb = this.zzacg.get(api.zznx());
        return zzb != null && zzb.isConnected();
    }
    
    @Override
    public boolean isConnected() {
        return this.zzacj instanceof zzlf;
    }
    
    @Override
    public boolean isConnecting() {
        return this.zzacj instanceof zzlg;
    }
    
    @Override
    public boolean isConnectionCallbacksRegistered(final ConnectionCallbacks connectionCallbacks) {
        return this.zzabZ.isConnectionCallbacksRegistered(connectionCallbacks);
    }
    
    @Override
    public boolean isConnectionFailedListenerRegistered(final OnConnectionFailedListener onConnectionFailedListener) {
        return this.zzabZ.isConnectionFailedListenerRegistered(onConnectionFailedListener);
    }
    
    @Override
    public void reconnect() {
        this.disconnect();
        this.connect();
    }
    
    @Override
    public void registerConnectionCallbacks(final ConnectionCallbacks connectionCallbacks) {
        this.zzabZ.registerConnectionCallbacks(connectionCallbacks);
    }
    
    @Override
    public void registerConnectionFailedListener(final OnConnectionFailedListener onConnectionFailedListener) {
        this.zzabZ.registerConnectionFailedListener(onConnectionFailedListener);
    }
    
    @Override
    public void stopAutoManage(final FragmentActivity fragmentActivity) {
        if (this.zzaaM < 0) {
            throw new IllegalStateException("Called stopAutoManage but automatic lifecycle management is not enabled.");
        }
        final zzlp zza = zzlp.zza(fragmentActivity);
        if (zza == null) {
            new Handler(this.mContext.getMainLooper()).post((Runnable)new Runnable(this, fragmentActivity) {
                final zzli zzacr;
                final FragmentActivity zzacx;
                
                @Override
                public void run() {
                    if (!this.zzacx.isFinishing()) {
                        if (this.zzacx.getSupportFragmentManager().isDestroyed()) {
                            return;
                        }
                        zzlp.zzb(this.zzacx).zzbp(this.zzacr.zzaaM);
                    }
                }
            });
            return;
        }
        zza.zzbp(this.zzaaM);
    }
    
    @Override
    public void unregisterConnectionCallbacks(final ConnectionCallbacks connectionCallbacks) {
        this.zzabZ.unregisterConnectionCallbacks(connectionCallbacks);
    }
    
    @Override
    public void unregisterConnectionFailedListener(final OnConnectionFailedListener onConnectionFailedListener) {
        this.zzabZ.unregisterConnectionFailedListener(onConnectionFailedListener);
    }
    
    @Override
    public <C extends Api.zzb> C zza(final Api.zzc<C> zzc) {
        final Api.zzb zzb = this.zzacg.get(zzc);
        zzx.zzb(zzb, "Appropriate Api was not requested.");
        return (C)zzb;
    }
    
    @Override
    public <A extends Api.zzb, R extends Result, T extends zzlb.zza<R, A>> T zza(final T t) {
        zzx.zzb(t.zznx() != null, (Object)"This task can not be enqueued (it's probably a Batch or malformed)");
        zzx.zzb(this.zzacg.containsKey(t.zznx()), (Object)"GoogleApiClient is not configured to use the API required for this call.");
        this.zzabt.lock();
        try {
            return this.zzacj.zza(t);
        }
        finally {
            this.zzabt.unlock();
        }
    }
    
    void zza(final zzb zzb) {
        this.zzace.sendMessage(this.zzace.obtainMessage(3, (Object)zzb));
    }
    
    void zza(final RuntimeException ex) {
        this.zzace.sendMessage(this.zzace.obtainMessage(4, (Object)ex));
    }
    
    @Override
    public boolean zza(final Api<?> api) {
        return this.zzacg.containsKey(api.zznx());
    }
    
    @Override
    public <A extends Api.zzb, T extends zzlb.zza<? extends Result, A>> T zzb(final T t) {
        zzx.zzb(t.zznx() != null, (Object)"This task can not be executed (it's probably a Batch or malformed)");
        this.zzabt.lock();
        try {
            zzlb.zza<? extends Result, A> zzb;
            if (this.zzoc()) {
                this.zzaca.add((zzf<?>)t);
                while (true) {
                    zzb = t;
                    if (this.zzaca.isEmpty()) {
                        break;
                    }
                    final zzf zzf = this.zzaca.remove();
                    this.zzb((zzf<Api.zzb>)zzf);
                    zzf.zzv(Status.zzabd);
                }
            }
            else {
                zzb = this.zzacj.zzb(t);
            }
            return (T)zzb;
        }
        finally {
            this.zzabt.unlock();
        }
    }
    
     <A extends Api.zzb> void zzb(final zzf<A> zzf) {
        this.zzacm.add(zzf);
        zzf.zza(this.zzaco);
    }
    
    void zzg(final ConnectionResult zzack) {
        this.zzabt.lock();
        try {
            this.zzack = zzack;
            (this.zzacj = new zzlh(this)).begin();
            this.zzabY.signalAll();
        }
        finally {
            this.zzabt.unlock();
        }
    }
    
    void zznY() {
        for (final zzf zzf : this.zzacm) {
            zzf.zza(null);
            if (zzf.zznF() == null) {
                zzf.cancel();
            }
            else {
                zzf.zznJ();
                zza(zzf, this.zzacn, this.zza((Api.zzc<Api.zzb>)zzf.zznx()).zznz());
            }
        }
        this.zzacm.clear();
        final Iterator<zzlm<?>> iterator2 = this.zzacl.iterator();
        while (iterator2.hasNext()) {
            iterator2.next().clear();
        }
        this.zzacl.clear();
    }
    
    void zznZ() {
        final Iterator<Api.zzb> iterator = this.zzacg.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().disconnect();
        }
    }
    
    @Override
    public <L> zzlm<L> zzo(final L l) {
        zzx.zzb(l, "Listener must not be null");
        this.zzabt.lock();
        try {
            final zzlm<Object> zzlm = new zzlm<Object>(this.zzaaO, l);
            this.zzacl.add(zzlm);
            return (zzlm<L>)zzlm;
        }
        finally {
            this.zzabt.unlock();
        }
    }
    
    void zzoa() {
        this.zzabt.lock();
        try {
            (this.zzacj = new zzlg(this, this.zzabI, this.zzabJ, this.zzaaP, this.zzaaQ, this.zzabt, this.mContext)).begin();
            this.zzabY.signalAll();
        }
        finally {
            this.zzabt.unlock();
        }
    }
    
    void zzob() {
        this.zzabt.lock();
        try {
            this.zzof();
            (this.zzacj = new zzlf(this)).begin();
            this.zzabY.signalAll();
        }
        finally {
            this.zzabt.unlock();
        }
    }
    
    boolean zzoc() {
        return this.zzacb;
    }
    
    void zzoe() {
        if (this.zzoc()) {
            return;
        }
        this.zzacb = true;
        if (this.zzacf == null) {
            this.zzacf = zzll.zza(this.mContext.getApplicationContext(), new zzd(this), this.zzaaP);
        }
        this.zzace.sendMessageDelayed(this.zzace.obtainMessage(1), this.zzacc);
        this.zzace.sendMessageDelayed(this.zzace.obtainMessage(2), this.zzacd);
    }
    
    boolean zzof() {
        if (!this.zzoc()) {
            return false;
        }
        this.zzacb = false;
        this.zzace.removeMessages(2);
        this.zzace.removeMessages(1);
        if (this.zzacf != null) {
            this.zzacf.unregister();
            this.zzacf = null;
        }
        return true;
    }
    
    String zzog() {
        final StringWriter out = new StringWriter();
        this.dump("", null, new PrintWriter(out), null);
        return out.toString();
    }
    
    final class zza extends Handler
    {
        final zzli zzacr;
        
        zza(final zzli zzacr, final Looper looper) {
            this.zzacr = zzacr;
            super(looper);
        }
        
        public void handleMessage(final Message message) {
            switch (message.what) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unknown message id: ");
                    sb.append(message.what);
                    Log.w("GoogleApiClientImpl", sb.toString());
                    return;
                }
                case 4: {
                    throw (RuntimeException)message.obj;
                }
                case 3: {
                    ((zzb)message.obj).zzg(this.zzacr);
                    return;
                }
                case 2: {
                    this.zzacr.resume();
                    return;
                }
                case 1: {
                    this.zzacr.zzod();
                }
            }
        }
    }
    
    abstract static class zzb
    {
        private final zzlj zzacy;
        
        protected zzb(final zzlj zzacy) {
            this.zzacy = zzacy;
        }
        
        public final void zzg(final zzli zzli) {
            zzli.zzabt.lock();
            try {
                if (zzli.zzacj != this.zzacy) {
                    return;
                }
                this.zznO();
            }
            finally {
                zzli.zzabt.unlock();
            }
        }
        
        protected abstract void zznO();
    }
    
    private static class zzc implements IBinder$DeathRecipient, zze
    {
        private final WeakReference<com.google.android.gms.common.api.zza> zzacA;
        private final WeakReference<IBinder> zzacB;
        private final WeakReference<zzf<?>> zzacz;
        
        private zzc(final zzf referent, final com.google.android.gms.common.api.zza referent2, final IBinder referent3) {
            this.zzacA = new WeakReference<com.google.android.gms.common.api.zza>(referent2);
            this.zzacz = new WeakReference<zzf<?>>(referent);
            this.zzacB = new WeakReference<IBinder>(referent3);
        }
        
        private void zzoh() {
            final zzf zzf = (zzf)this.zzacz.get();
            final com.google.android.gms.common.api.zza zza = this.zzacA.get();
            if (zza != null && zzf != null) {
                zza.remove(zzf.zznF());
            }
            final IBinder binder = this.zzacB.get();
            if (this.zzacB != null) {
                binder.unlinkToDeath((IBinder$DeathRecipient)this, 0);
            }
        }
        
        public void binderDied() {
            this.zzoh();
        }
        
        public void zzc(final zzf<?> zzf) {
            this.zzoh();
        }
    }
    
    interface zze
    {
        void zzc(final zzf<?> p0);
    }
    
    static class zzd extends zzll
    {
        private WeakReference<zzli> zzacC;
        
        zzd(final zzli referent) {
            this.zzacC = new WeakReference<zzli>(referent);
        }
        
        public void zzoi() {
            final zzli zzli = this.zzacC.get();
            if (zzli == null) {
                return;
            }
            zzli.resume();
        }
    }
    
    interface zzf<A extends Api.zzb>
    {
        void cancel();
        
        boolean isReady();
        
        void zza(final zze p0);
        
        void zzb(final A p0) throws DeadObjectException;
        
        Integer zznF();
        
        void zznJ();
        
        int zznK();
        
        Api.zzc<A> zznx();
        
        void zzv(final Status p0);
        
        void zzw(final Status p0);
    }
}
