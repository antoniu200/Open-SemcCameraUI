// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.internal;

import android.content.ComponentName;
import android.os.Message;
import android.app.PendingIntent;
import java.util.Collection;
import android.os.DeadObjectException;
import android.os.RemoteException;
import android.os.Bundle;
import android.os.IBinder;
import com.google.android.gms.common.ConnectionResult;
import java.io.PrintWriter;
import java.io.FileDescriptor;
import android.content.ServiceConnection;
import android.util.Log;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import com.google.android.gms.common.api.GoogleApiClient;
import java.util.ArrayList;
import com.google.android.gms.common.GoogleApiAvailability;
import android.os.Looper;
import com.google.android.gms.common.api.Scope;
import java.util.Set;
import android.accounts.Account;
import android.os.Handler;
import android.content.Context;
import com.google.android.gms.common.api.Api;
import android.os.IInterface;

public abstract class zzj<T extends IInterface> implements Api.zzb, zzk.zza
{
    public static final String[] zzafI;
    private final Context mContext;
    final Handler mHandler;
    private final Account zzQd;
    private final Set<Scope> zzTm;
    private final Looper zzaaO;
    private final GoogleApiAvailability zzaaP;
    private final com.google.android.gms.common.internal.zzf zzabI;
    private T zzafA;
    private final ArrayList<zzc<?>> zzafB;
    private zze zzafC;
    private int zzafD;
    private final GoogleApiClient.ConnectionCallbacks zzafE;
    private final GoogleApiClient.OnConnectionFailedListener zzafF;
    private final int zzafG;
    protected AtomicInteger zzafH;
    private final zzl zzafx;
    private zzs zzafy;
    private GoogleApiClient.zza zzafz;
    private final Object zzpd;
    
    static {
        zzafI = new String[] { "service_esmobile", "service_googleme" };
    }
    
    protected zzj(final Context context, final Looper looper, final int n, final com.google.android.gms.common.internal.zzf zzf, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        this(context, looper, zzl.zzal(context), GoogleApiAvailability.getInstance(), n, zzf, zzx.zzw(connectionCallbacks), zzx.zzw(onConnectionFailedListener));
    }
    
    protected zzj(final Context context, final Looper looper, final zzl zzl, final GoogleApiAvailability googleApiAvailability, final int zzafG, final com.google.android.gms.common.internal.zzf zzf, final GoogleApiClient.ConnectionCallbacks zzafE, final GoogleApiClient.OnConnectionFailedListener zzafF) {
        this.zzpd = new Object();
        this.zzafB = new ArrayList<zzc<?>>();
        this.zzafD = 1;
        this.zzafH = new AtomicInteger(0);
        this.mContext = zzx.zzb(context, "Context must not be null");
        this.zzaaO = zzx.zzb(looper, "Looper must not be null");
        this.zzafx = zzx.zzb(zzl, "Supervisor must not be null");
        this.zzaaP = zzx.zzb(googleApiAvailability, "API availability must not be null");
        this.mHandler = new zzb(looper);
        this.zzafG = zzafG;
        this.zzabI = zzx.zzw(zzf);
        this.zzQd = zzf.getAccount();
        this.zzTm = this.zza(zzf.zzoL());
        this.zzafE = zzafE;
        this.zzafF = zzafF;
    }
    
    private Set<Scope> zza(final Set<Scope> set) {
        final Set<Scope> zzb = this.zzb(set);
        if (zzb == null) {
            return zzb;
        }
        final Iterator<Scope> iterator = zzb.iterator();
        while (iterator.hasNext()) {
            if (!set.contains(iterator.next())) {
                throw new IllegalStateException("Expanding scopes is not permitted, use implied scopes instead");
            }
        }
        return zzb;
    }
    
    private boolean zza(final int n, final int n2, final T t) {
        synchronized (this.zzpd) {
            if (this.zzafD != n) {
                return false;
            }
            this.zzb(n2, t);
            return true;
        }
    }
    
    private void zzb(final int zzafD, final T zzafA) {
        boolean b = false;
        if (zzafD == 3 == (zzafA != null)) {
            b = true;
        }
        zzx.zzaa(b);
        synchronized (this.zzpd) {
            this.zzc(this.zzafD = zzafD, this.zzafA = zzafA);
            switch (zzafD) {
                case 3: {
                    this.zzoW();
                    break;
                }
                case 2: {
                    this.zzoX();
                    break;
                }
                case 1: {
                    this.zzoY();
                    break;
                }
            }
        }
    }
    
    private void zzoX() {
        if (this.zzafC != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Calling connect() while still connected, missing disconnect() for ");
            sb.append(this.zzfK());
            Log.e("GmsClient", sb.toString());
            this.zzafx.zzb(this.zzfK(), (ServiceConnection)this.zzafC, this.zzoV());
            this.zzafH.incrementAndGet();
        }
        this.zzafC = new zze(this.zzafH.get());
        if (!this.zzafx.zza(this.zzfK(), (ServiceConnection)this.zzafC, this.zzoV())) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("unable to connect to service: ");
            sb2.append(this.zzfK());
            Log.e("GmsClient", sb2.toString());
            this.mHandler.sendMessage(this.mHandler.obtainMessage(3, this.zzafH.get(), 9));
        }
    }
    
    private void zzoY() {
        if (this.zzafC != null) {
            this.zzafx.zzb(this.zzfK(), (ServiceConnection)this.zzafC, this.zzoV());
            this.zzafC = null;
        }
    }
    
    @Override
    public void disconnect() {
        this.zzafH.incrementAndGet();
        synchronized (this.zzafB) {
            for (int size = this.zzafB.size(), i = 0; i < size; ++i) {
                this.zzafB.get(i).zzpi();
            }
            this.zzafB.clear();
            monitorexit(this.zzafB);
            this.zzb(1, null);
        }
    }
    
    @Override
    public void dump(String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
        synchronized (this.zzpd) {
            final int zzafD = this.zzafD;
            final IInterface zzafA = this.zzafA;
            monitorexit(this.zzpd);
            printWriter.append(s).append("mConnectState=");
            switch (zzafD) {
                default: {
                    s = "UNKNOWN";
                    break;
                }
                case 4: {
                    s = "DISCONNECTING";
                    break;
                }
                case 3: {
                    s = "CONNECTED";
                    break;
                }
                case 2: {
                    s = "CONNECTING";
                    break;
                }
                case 1: {
                    s = "DISCONNECTED";
                    break;
                }
            }
            printWriter.print(s);
            printWriter.append(" mService=");
            if (zzafA == null) {
                printWriter.println("null");
                return;
            }
            printWriter.append(this.zzfL()).append("@").println(Integer.toHexString(System.identityHashCode(zzafA.asBinder())));
        }
    }
    
    public final Context getContext() {
        return this.mContext;
    }
    
    public final Looper getLooper() {
        return this.zzaaO;
    }
    
    @Override
    public boolean isConnected() {
        synchronized (this.zzpd) {
            return this.zzafD == 3;
        }
    }
    
    public boolean isConnecting() {
        synchronized (this.zzpd) {
            return this.zzafD == 2;
        }
    }
    
    protected void onConnectionFailed(final ConnectionResult connectionResult) {
    }
    
    protected void onConnectionSuspended(final int n) {
    }
    
    protected abstract T zzW(final IBinder p0);
    
    protected void zza(final int n, final Bundle bundle, final int n2) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(5, n2, -1, (Object)new zzi(n, bundle)));
    }
    
    protected void zza(final int n, final IBinder binder, final Bundle bundle, final int n2) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(1, n2, -1, (Object)new zzg(n, binder, bundle)));
    }
    
    @Override
    public void zza(final GoogleApiClient.zza zza) {
        this.zzafz = zzx.zzb(zza, "Connection progress callbacks cannot be null.");
        this.zzb(2, null);
    }
    
    @Override
    public void zza(final zzp zzp) {
        final ValidateAccountRequest validateAccountRequest = new ValidateAccountRequest(zzp, this.zzTm.toArray(new Scope[this.zzTm.size()]), this.mContext.getPackageName(), this.zzpd());
        try {
            this.zzafy.zza(new zzd(this, this.zzafH.get()), validateAccountRequest);
        }
        catch (final RemoteException ex) {
            Log.w("GmsClient", "Remote exception occurred", (Throwable)ex);
        }
        catch (final DeadObjectException ex2) {
            Log.w("GmsClient", "service died");
            this.zzbE(1);
        }
    }
    
    @Override
    public void zza(final zzp zzp, final Set<Scope> set) {
        try {
            final GetServiceRequest zzg = new GetServiceRequest(this.zzafG).zzcl(this.mContext.getPackageName()).zzg(this.zzly());
            if (set != null) {
                zzg.zzd(set);
            }
            if (this.zzlN()) {
                zzg.zzc(this.zzoI()).zzc(zzp);
            }
            else if (this.zzpe()) {
                zzg.zzc(this.zzQd);
            }
            this.zzafy.zza(new zzd(this, this.zzafH.get()), zzg);
        }
        catch (final RemoteException ex) {
            Log.w("GmsClient", "Remote exception occurred", (Throwable)ex);
        }
        catch (final DeadObjectException ex2) {
            Log.w("GmsClient", "service died");
            this.zzbE(1);
        }
    }
    
    protected Set<Scope> zzb(final Set<Scope> set) {
        return set;
    }
    
    public void zzbE(final int n) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(4, this.zzafH.get(), n));
    }
    
    protected void zzbF(final int n) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(6, n, -1, (Object)new zzh()));
    }
    
    protected void zzc(final int n, final T t) {
    }
    
    protected abstract String zzfK();
    
    protected abstract String zzfL();
    
    @Override
    public boolean zzlN() {
        return false;
    }
    
    protected Bundle zzly() {
        return new Bundle();
    }
    
    @Override
    public Bundle zzmS() {
        return null;
    }
    
    @Override
    public IBinder zznz() {
        if (this.zzafy == null) {
            return null;
        }
        return this.zzafy.asBinder();
    }
    
    public final Account zzoI() {
        if (this.zzQd != null) {
            return this.zzQd;
        }
        return new Account("<<default account>>", "com.google");
    }
    
    protected final String zzoV() {
        return this.zzabI.zzoO();
    }
    
    protected void zzoW() {
    }
    
    public void zzoZ() {
        final int googlePlayServicesAvailable = this.zzaaP.isGooglePlayServicesAvailable(this.mContext);
        if (googlePlayServicesAvailable != 0) {
            this.zzb(1, null);
            this.zzafz = new zzf();
            this.mHandler.sendMessage(this.mHandler.obtainMessage(3, this.zzafH.get(), googlePlayServicesAvailable));
            return;
        }
        this.zza(new zzf());
    }
    
    protected final com.google.android.gms.common.internal.zzf zzpa() {
        return this.zzabI;
    }
    
    protected final void zzpb() {
        if (!this.isConnected()) {
            throw new IllegalStateException("Not connected. Call connect() and wait for onConnected() to be called.");
        }
    }
    
    public final T zzpc() throws DeadObjectException {
        synchronized (this.zzpd) {
            if (this.zzafD == 4) {
                throw new DeadObjectException();
            }
            this.zzpb();
            zzx.zza(this.zzafA != null, (Object)"Client is connected but service is null");
            return this.zzafA;
        }
    }
    
    protected Bundle zzpd() {
        return null;
    }
    
    public boolean zzpe() {
        return false;
    }
    
    private abstract class zza extends zzc<Boolean>
    {
        public final int statusCode;
        public final Bundle zzafJ;
        final zzj zzafK;
        
        protected zza(final zzj zzafK, final int statusCode, final Bundle zzafJ) {
            super(true);
            this.statusCode = statusCode;
            this.zzafJ = zzafJ;
        }
        
        protected void zzc(final Boolean b) {
            final PendingIntent pendingIntent = null;
            if (b == null) {
                this.zzafK.zzb(1, null);
                return;
            }
            final int statusCode = this.statusCode;
            ConnectionResult connectionResult;
            if (statusCode != 0) {
                if (statusCode == 10) {
                    this.zzafK.zzb(1, null);
                    throw new IllegalStateException("A fatal developer error has occurred. Check the logs for further information.");
                }
                this.zzafK.zzb(1, null);
                PendingIntent pendingIntent2 = pendingIntent;
                if (this.zzafJ != null) {
                    pendingIntent2 = (PendingIntent)this.zzafJ.getParcelable("pendingIntent");
                }
                connectionResult = new ConnectionResult(this.statusCode, pendingIntent2);
            }
            else {
                if (this.zzpf()) {
                    return;
                }
                this.zzafK.zzb(1, null);
                connectionResult = new ConnectionResult(8, null);
            }
            this.zzh(connectionResult);
        }
        
        protected abstract void zzh(final ConnectionResult p0);
        
        protected abstract boolean zzpf();
        
        @Override
        protected void zzpg() {
        }
    }
    
    final class zzb extends Handler
    {
        final zzj zzafK;
        
        public zzb(final zzj zzafK, final Looper looper) {
            this.zzafK = zzafK;
            super(looper);
        }
        
        private void zza(final Message message) {
            final zzc zzc = (zzc)message.obj;
            zzc.zzpg();
            zzc.unregister();
        }
        
        private boolean zzb(final Message message) {
            final int what = message.what;
            boolean b2;
            final boolean b = b2 = true;
            if (what != 2) {
                b2 = b;
                if (message.what != 1) {
                    b2 = b;
                    if (message.what != 5) {
                        if (message.what == 6) {
                            return true;
                        }
                        b2 = false;
                    }
                }
            }
            return b2;
        }
        
        public void handleMessage(final Message message) {
            if (this.zzafK.zzafH.get() != message.arg1) {
                if (this.zzb(message)) {
                    this.zza(message);
                }
                return;
            }
            if ((message.what == 1 || message.what == 5 || message.what == 6) && !this.zzafK.isConnecting()) {
                this.zza(message);
                return;
            }
            if (message.what == 3) {
                final ConnectionResult connectionResult = new ConnectionResult(message.arg2, null);
                this.zzafK.zzafz.zza(connectionResult);
                this.zzafK.onConnectionFailed(connectionResult);
                return;
            }
            if (message.what == 4) {
                this.zzafK.zzb(4, null);
                if (this.zzafK.zzafE != null) {
                    this.zzafK.zzafE.onConnectionSuspended(message.arg2);
                }
                this.zzafK.onConnectionSuspended(message.arg2);
                this.zzafK.zza(4, 1, null);
                return;
            }
            if (message.what == 2 && !this.zzafK.isConnected()) {
                this.zza(message);
                return;
            }
            if (this.zzb(message)) {
                ((zzc)message.obj).zzph();
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Don't know how to handle message: ");
            sb.append(message.what);
            Log.wtf("GmsClient", sb.toString(), (Throwable)new Exception());
        }
    }
    
    protected abstract class zzc<TListener>
    {
        private TListener mListener;
        final zzj zzafK;
        private boolean zzafL;
        
        public zzc(final zzj zzafK, final TListener mListener) {
            this.zzafK = zzafK;
            this.mListener = mListener;
            this.zzafL = false;
        }
        
        public void unregister() {
            this.zzpi();
            synchronized (this.zzafK.zzafB) {
                this.zzafK.zzafB.remove(this);
            }
        }
        
        protected abstract void zzpg();
        
        public void zzph() {
            synchronized (this) {
                final TListener mListener = this.mListener;
                if (this.zzafL) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Callback proxy ");
                    sb.append(this);
                    sb.append(" being reused. This is not safe.");
                    Log.w("GmsClient", sb.toString());
                }
                monitorexit(this);
                Label_0077: {
                    if (mListener != null) {
                        try {
                            this.zzt(mListener);
                            break Label_0077;
                        }
                        catch (final RuntimeException ex) {
                            this.zzpg();
                            throw ex;
                        }
                    }
                    this.zzpg();
                }
                synchronized (this) {
                    this.zzafL = true;
                    monitorexit(this);
                    this.unregister();
                }
            }
        }
        
        public void zzpi() {
            synchronized (this) {
                this.mListener = null;
            }
        }
        
        protected abstract void zzt(final TListener p0);
    }
    
    public static final class zzd extends zzr.zza
    {
        private zzj zzafM;
        private final int zzafN;
        
        public zzd(final zzj zzafM, final int zzafN) {
            this.zzafM = zzafM;
            this.zzafN = zzafN;
        }
        
        private void zzpj() {
            this.zzafM = null;
        }
        
        public void zza(final int n, final IBinder binder, final Bundle bundle) {
            zzx.zzb(this.zzafM, "onPostInitComplete can be called only once per call to getRemoteService");
            this.zzafM.zza(n, binder, bundle, this.zzafN);
            this.zzpj();
        }
        
        public void zzb(final int n, final Bundle bundle) {
            zzx.zzb(this.zzafM, "onAccountValidationComplete can be called only once per call to validateAccount");
            this.zzafM.zza(n, bundle, this.zzafN);
            this.zzpj();
        }
    }
    
    public final class zze implements ServiceConnection
    {
        final zzj zzafK;
        private final int zzafN;
        
        public zze(final zzj zzafK, final int zzafN) {
            this.zzafK = zzafK;
            this.zzafN = zzafN;
        }
        
        public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
            zzx.zzb(binder, "Expecting a valid IBinder");
            this.zzafK.zzafy = zzs.zza.zzaK(binder);
            this.zzafK.zzbF(this.zzafN);
        }
        
        public void onServiceDisconnected(final ComponentName componentName) {
            this.zzafK.mHandler.sendMessage(this.zzafK.mHandler.obtainMessage(4, this.zzafN, 1));
        }
    }
    
    protected class zzf implements GoogleApiClient.zza
    {
        final zzj zzafK;
        
        public zzf(final zzj zzafK) {
            this.zzafK = zzafK;
        }
        
        @Override
        public void zza(final ConnectionResult connectionResult) {
            if (connectionResult.isSuccess()) {
                this.zzafK.zza(null, this.zzafK.zzTm);
                return;
            }
            if (this.zzafK.zzafF != null) {
                this.zzafK.zzafF.onConnectionFailed(connectionResult);
            }
        }
        
        @Override
        public void zzb(final ConnectionResult connectionResult) {
            throw new IllegalStateException("Legacy GmsClient received onReportAccountValidation callback.");
        }
    }
    
    protected final class zzg extends zza
    {
        final zzj zzafK;
        public final IBinder zzafO;
        
        public zzg(final zzj zzafK, final int n, final IBinder zzafO, final Bundle bundle) {
            this.zzafK = zzafK.super(n, bundle);
            this.zzafO = zzafO;
        }
        
        @Override
        protected void zzh(final ConnectionResult connectionResult) {
            if (this.zzafK.zzafF != null) {
                this.zzafK.zzafF.onConnectionFailed(connectionResult);
            }
            this.zzafK.onConnectionFailed(connectionResult);
        }
        
        @Override
        protected boolean zzpf() {
            try {
                final String interfaceDescriptor = this.zzafO.getInterfaceDescriptor();
                if (!this.zzafK.zzfL().equals(interfaceDescriptor)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("service descriptor mismatch: ");
                    sb.append(this.zzafK.zzfL());
                    sb.append(" vs. ");
                    sb.append(interfaceDescriptor);
                    Log.e("GmsClient", sb.toString());
                    return false;
                }
                final IInterface zzW = this.zzafK.zzW(this.zzafO);
                if (zzW != null && this.zzafK.zza(2, 3, zzW)) {
                    final Bundle zzmS = this.zzafK.zzmS();
                    if (this.zzafK.zzafE != null) {
                        this.zzafK.zzafE.onConnected(zzmS);
                    }
                    return true;
                }
                return false;
            }
            catch (final RemoteException ex) {
                Log.w("GmsClient", "service probably died");
                return false;
            }
        }
    }
    
    protected final class zzh extends zza
    {
        final zzj zzafK;
        
        public zzh(final zzj zzafK) {
            this.zzafK = zzafK.super(0, null);
        }
        
        @Override
        protected void zzh(final ConnectionResult connectionResult) {
            this.zzafK.zzafz.zza(connectionResult);
            this.zzafK.onConnectionFailed(connectionResult);
        }
        
        @Override
        protected boolean zzpf() {
            this.zzafK.zzafz.zza(ConnectionResult.zzZY);
            return true;
        }
    }
    
    protected final class zzi extends zza
    {
        final zzj zzafK;
        
        public zzi(final zzj zzafK, final int n, final Bundle bundle) {
            this.zzafK = zzafK.super(n, bundle);
        }
        
        @Override
        protected void zzh(final ConnectionResult connectionResult) {
            this.zzafK.zzafz.zzb(connectionResult);
            this.zzafK.onConnectionFailed(connectionResult);
        }
        
        @Override
        protected boolean zzpf() {
            this.zzafK.zzafz.zzb(ConnectionResult.zzZY);
            return true;
        }
    }
}
