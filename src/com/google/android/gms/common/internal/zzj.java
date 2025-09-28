package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.zzk;
import com.google.android.gms.common.internal.zzr;
import com.google.android.gms.common.internal.zzs;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class zzj<T extends IInterface> implements Api.zzb, zzk.zza {
    public static final String[] zzafI = {"service_esmobile", "service_googleme"};
    private final Context mContext;
    final Handler mHandler;
    private final Account zzQd;
    private final Set<Scope> zzTm;
    private final Looper zzaaO;
    private final GoogleApiAvailability zzaaP;
    private final com.google.android.gms.common.internal.zzf zzabI;
    private T zzafA;
    private final ArrayList<zzj<T>.zzc<?>> zzafB;
    private zzj<T>.zze zzafC;
    private int zzafD;
    private final GoogleApiClient.ConnectionCallbacks zzafE;
    private final GoogleApiClient.OnConnectionFailedListener zzafF;
    private final int zzafG;
    protected AtomicInteger zzafH;
    private final zzl zzafx;
    private zzs zzafy;
    private GoogleApiClient.zza zzafz;
    private final Object zzpd;

    private abstract class zza extends zzj<T>.zzc<Boolean> {
        public final int statusCode;
        public final Bundle zzafJ;

        protected zza(int i, Bundle bundle) {
            super(true);
            this.statusCode = i;
            this.zzafJ = bundle;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.android.gms.common.internal.zzj.zzc
        /* renamed from: zzc, reason: merged with bridge method [inline-methods] */
        public void zzt(Boolean bool) {
            ConnectionResult connectionResult;
            if (bool == null) {
                zzj.this.zzb(1, null);
                return;
            }
            int i = this.statusCode;
            if (i != 0) {
                if (i == 10) {
                    zzj.this.zzb(1, null);
                    throw new IllegalStateException("A fatal developer error has occurred. Check the logs for further information.");
                }
                zzj.this.zzb(1, null);
                connectionResult = new ConnectionResult(this.statusCode, this.zzafJ != null ? (PendingIntent) this.zzafJ.getParcelable("pendingIntent") : null);
            } else {
                if (zzpf()) {
                    return;
                }
                zzj.this.zzb(1, null);
                connectionResult = new ConnectionResult(8, null);
            }
            zzh(connectionResult);
        }

        protected abstract void zzh(ConnectionResult connectionResult);

        protected abstract boolean zzpf();

        @Override // com.google.android.gms.common.internal.zzj.zzc
        protected void zzpg() {
        }
    }

    final class zzb extends Handler {
        public zzb(Looper looper) {
            super(looper);
        }

        private void zza(Message message) {
            zzc zzcVar = (zzc) message.obj;
            zzcVar.zzpg();
            zzcVar.unregister();
        }

        private boolean zzb(Message message) {
            return message.what == 2 || message.what == 1 || message.what == 5 || message.what == 6;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (zzj.this.zzafH.get() != message.arg1) {
                if (zzb(message)) {
                    zza(message);
                    return;
                }
                return;
            }
            if ((message.what == 1 || message.what == 5 || message.what == 6) && !zzj.this.isConnecting()) {
                zza(message);
                return;
            }
            if (message.what == 3) {
                ConnectionResult connectionResult = new ConnectionResult(message.arg2, null);
                zzj.this.zzafz.zza(connectionResult);
                zzj.this.onConnectionFailed(connectionResult);
                return;
            }
            if (message.what == 4) {
                zzj.this.zzb(4, null);
                if (zzj.this.zzafE != null) {
                    zzj.this.zzafE.onConnectionSuspended(message.arg2);
                }
                zzj.this.onConnectionSuspended(message.arg2);
                zzj.this.zza(4, 1, (int) null);
                return;
            }
            if (message.what == 2 && !zzj.this.isConnected()) {
                zza(message);
                return;
            }
            if (zzb(message)) {
                ((zzc) message.obj).zzph();
                return;
            }
            Log.wtf("GmsClient", "Don't know how to handle message: " + message.what, new Exception());
        }
    }

    protected abstract class zzc<TListener> {
        private TListener mListener;
        private boolean zzafL = false;

        public zzc(TListener tlistener) {
            this.mListener = tlistener;
        }

        public void unregister() {
            zzpi();
            synchronized (zzj.this.zzafB) {
                zzj.this.zzafB.remove(this);
            }
        }

        protected abstract void zzpg();

        public void zzph() {
            TListener tlistener;
            synchronized (this) {
                tlistener = this.mListener;
                if (this.zzafL) {
                    Log.w("GmsClient", "Callback proxy " + this + " being reused. This is not safe.");
                }
            }
            if (tlistener != null) {
                try {
                    zzt(tlistener);
                } catch (RuntimeException e) {
                    zzpg();
                    throw e;
                }
            } else {
                zzpg();
            }
            synchronized (this) {
                this.zzafL = true;
            }
            unregister();
        }

        public void zzpi() {
            synchronized (this) {
                this.mListener = null;
            }
        }

        protected abstract void zzt(TListener tlistener);
    }

    public static final class zzd extends zzr.zza {
        private zzj zzafM;
        private final int zzafN;

        public zzd(zzj zzjVar, int i) {
            this.zzafM = zzjVar;
            this.zzafN = i;
        }

        private void zzpj() {
            this.zzafM = null;
        }

        @Override // com.google.android.gms.common.internal.zzr
        public void zza(int i, IBinder iBinder, Bundle bundle) {
            zzx.zzb(this.zzafM, "onPostInitComplete can be called only once per call to getRemoteService");
            this.zzafM.zza(i, iBinder, bundle, this.zzafN);
            zzpj();
        }

        @Override // com.google.android.gms.common.internal.zzr
        public void zzb(int i, Bundle bundle) {
            zzx.zzb(this.zzafM, "onAccountValidationComplete can be called only once per call to validateAccount");
            this.zzafM.zza(i, bundle, this.zzafN);
            zzpj();
        }
    }

    public final class zze implements ServiceConnection {
        private final int zzafN;

        public zze(int i) {
            this.zzafN = i;
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            zzx.zzb(iBinder, "Expecting a valid IBinder");
            zzj.this.zzafy = zzs.zza.zzaK(iBinder);
            zzj.this.zzbF(this.zzafN);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            zzj.this.mHandler.sendMessage(zzj.this.mHandler.obtainMessage(4, this.zzafN, 1));
        }
    }

    protected class zzf implements GoogleApiClient.zza {
        public zzf() {
        }

        @Override // com.google.android.gms.common.api.GoogleApiClient.zza
        public void zza(ConnectionResult connectionResult) {
            if (connectionResult.isSuccess()) {
                zzj.this.zza((zzp) null, zzj.this.zzTm);
            } else if (zzj.this.zzafF != null) {
                zzj.this.zzafF.onConnectionFailed(connectionResult);
            }
        }

        @Override // com.google.android.gms.common.api.GoogleApiClient.zza
        public void zzb(ConnectionResult connectionResult) {
            throw new IllegalStateException("Legacy GmsClient received onReportAccountValidation callback.");
        }
    }

    protected final class zzg extends zzj<T>.zza {
        public final IBinder zzafO;

        public zzg(int i, IBinder iBinder, Bundle bundle) {
            super(i, bundle);
            this.zzafO = iBinder;
        }

        @Override // com.google.android.gms.common.internal.zzj.zza
        protected void zzh(ConnectionResult connectionResult) {
            if (zzj.this.zzafF != null) {
                zzj.this.zzafF.onConnectionFailed(connectionResult);
            }
            zzj.this.onConnectionFailed(connectionResult);
        }

        @Override // com.google.android.gms.common.internal.zzj.zza
        protected boolean zzpf() throws RemoteException {
            try {
                String interfaceDescriptor = this.zzafO.getInterfaceDescriptor();
                if (!zzj.this.zzfL().equals(interfaceDescriptor)) {
                    Log.e("GmsClient", "service descriptor mismatch: " + zzj.this.zzfL() + " vs. " + interfaceDescriptor);
                    return false;
                }
                IInterface iInterfaceZzW = zzj.this.zzW(this.zzafO);
                if (iInterfaceZzW == null || !zzj.this.zza(2, 3, (int) iInterfaceZzW)) {
                    return false;
                }
                Bundle bundleZzmS = zzj.this.zzmS();
                if (zzj.this.zzafE == null) {
                    return true;
                }
                zzj.this.zzafE.onConnected(bundleZzmS);
                return true;
            } catch (RemoteException unused) {
                Log.w("GmsClient", "service probably died");
                return false;
            }
        }
    }

    protected final class zzh extends zzj<T>.zza {
        public zzh() {
            super(0, null);
        }

        @Override // com.google.android.gms.common.internal.zzj.zza
        protected void zzh(ConnectionResult connectionResult) {
            zzj.this.zzafz.zza(connectionResult);
            zzj.this.onConnectionFailed(connectionResult);
        }

        @Override // com.google.android.gms.common.internal.zzj.zza
        protected boolean zzpf() {
            zzj.this.zzafz.zza(ConnectionResult.zzZY);
            return true;
        }
    }

    protected final class zzi extends zzj<T>.zza {
        public zzi(int i, Bundle bundle) {
            super(i, bundle);
        }

        @Override // com.google.android.gms.common.internal.zzj.zza
        protected void zzh(ConnectionResult connectionResult) {
            zzj.this.zzafz.zzb(connectionResult);
            zzj.this.onConnectionFailed(connectionResult);
        }

        @Override // com.google.android.gms.common.internal.zzj.zza
        protected boolean zzpf() {
            zzj.this.zzafz.zzb(ConnectionResult.zzZY);
            return true;
        }
    }

    protected zzj(Context context, Looper looper, int i, com.google.android.gms.common.internal.zzf zzfVar, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        this(context, looper, zzl.zzal(context), GoogleApiAvailability.getInstance(), i, zzfVar, (GoogleApiClient.ConnectionCallbacks) zzx.zzw(connectionCallbacks), (GoogleApiClient.OnConnectionFailedListener) zzx.zzw(onConnectionFailedListener));
    }

    protected zzj(Context context, Looper looper, zzl zzlVar, GoogleApiAvailability googleApiAvailability, int i, com.google.android.gms.common.internal.zzf zzfVar, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        this.zzpd = new Object();
        this.zzafB = new ArrayList<>();
        this.zzafD = 1;
        this.zzafH = new AtomicInteger(0);
        this.mContext = (Context) zzx.zzb(context, "Context must not be null");
        this.zzaaO = (Looper) zzx.zzb(looper, "Looper must not be null");
        this.zzafx = (zzl) zzx.zzb(zzlVar, "Supervisor must not be null");
        this.zzaaP = (GoogleApiAvailability) zzx.zzb(googleApiAvailability, "API availability must not be null");
        this.mHandler = new zzb(looper);
        this.zzafG = i;
        this.zzabI = (com.google.android.gms.common.internal.zzf) zzx.zzw(zzfVar);
        this.zzQd = zzfVar.getAccount();
        this.zzTm = zza(zzfVar.zzoL());
        this.zzafE = connectionCallbacks;
        this.zzafF = onConnectionFailedListener;
    }

    private Set<Scope> zza(Set<Scope> set) {
        Set<Scope> setZzb = zzb(set);
        if (setZzb == null) {
            return setZzb;
        }
        Iterator<Scope> it = setZzb.iterator();
        while (it.hasNext()) {
            if (!set.contains(it.next())) {
                throw new IllegalStateException("Expanding scopes is not permitted, use implied scopes instead");
            }
        }
        return setZzb;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean zza(int i, int i2, T t) {
        synchronized (this.zzpd) {
            if (this.zzafD != i) {
                return false;
            }
            zzb(i2, t);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zzb(int i, T t) {
        zzx.zzaa((i == 3) == (t != null));
        synchronized (this.zzpd) {
            this.zzafD = i;
            this.zzafA = t;
            zzc(i, t);
            switch (i) {
                case 1:
                    zzoY();
                    break;
                case 2:
                    zzoX();
                    break;
                case 3:
                    zzoW();
                    break;
            }
        }
    }

    private void zzoX() {
        if (this.zzafC != null) {
            Log.e("GmsClient", "Calling connect() while still connected, missing disconnect() for " + zzfK());
            this.zzafx.zzb(zzfK(), this.zzafC, zzoV());
            this.zzafH.incrementAndGet();
        }
        this.zzafC = new zze(this.zzafH.get());
        if (this.zzafx.zza(zzfK(), this.zzafC, zzoV())) {
            return;
        }
        Log.e("GmsClient", "unable to connect to service: " + zzfK());
        this.mHandler.sendMessage(this.mHandler.obtainMessage(3, this.zzafH.get(), 9));
    }

    private void zzoY() {
        if (this.zzafC != null) {
            this.zzafx.zzb(zzfK(), this.zzafC, zzoV());
            this.zzafC = null;
        }
    }

    @Override // com.google.android.gms.common.api.Api.zzb
    public void disconnect() {
        this.zzafH.incrementAndGet();
        synchronized (this.zzafB) {
            int size = this.zzafB.size();
            for (int i = 0; i < size; i++) {
                this.zzafB.get(i).zzpi();
            }
            this.zzafB.clear();
        }
        zzb(1, null);
    }

    @Override // com.google.android.gms.common.api.Api.zzb
    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        int i;
        T t;
        String str2;
        synchronized (this.zzpd) {
            i = this.zzafD;
            t = this.zzafA;
        }
        printWriter.append((CharSequence) str).append("mConnectState=");
        switch (i) {
            case 1:
                str2 = "DISCONNECTED";
                break;
            case 2:
                str2 = "CONNECTING";
                break;
            case 3:
                str2 = "CONNECTED";
                break;
            case 4:
                str2 = "DISCONNECTING";
                break;
            default:
                str2 = "UNKNOWN";
                break;
        }
        printWriter.print(str2);
        printWriter.append(" mService=");
        if (t == null) {
            printWriter.println("null");
        } else {
            printWriter.append((CharSequence) zzfL()).append("@").println(Integer.toHexString(System.identityHashCode(t.asBinder())));
        }
    }

    public final Context getContext() {
        return this.mContext;
    }

    public final Looper getLooper() {
        return this.zzaaO;
    }

    @Override // com.google.android.gms.common.api.Api.zzb, com.google.android.gms.common.internal.zzk.zza
    public boolean isConnected() {
        boolean z;
        synchronized (this.zzpd) {
            z = this.zzafD == 3;
        }
        return z;
    }

    public boolean isConnecting() {
        boolean z;
        synchronized (this.zzpd) {
            z = this.zzafD == 2;
        }
        return z;
    }

    protected void onConnectionFailed(ConnectionResult connectionResult) {
    }

    protected void onConnectionSuspended(int i) {
    }

    protected abstract T zzW(IBinder iBinder);

    protected void zza(int i, Bundle bundle, int i2) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(5, i2, -1, new zzi(i, bundle)));
    }

    protected void zza(int i, IBinder iBinder, Bundle bundle, int i2) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(1, i2, -1, new zzg(i, iBinder, bundle)));
    }

    @Override // com.google.android.gms.common.api.Api.zzb
    public void zza(GoogleApiClient.zza zzaVar) {
        this.zzafz = (GoogleApiClient.zza) zzx.zzb(zzaVar, "Connection progress callbacks cannot be null.");
        zzb(2, null);
    }

    @Override // com.google.android.gms.common.api.Api.zzb
    public void zza(zzp zzpVar) {
        try {
            this.zzafy.zza(new zzd(this, this.zzafH.get()), new ValidateAccountRequest(zzpVar, (Scope[]) this.zzTm.toArray(new Scope[this.zzTm.size()]), this.mContext.getPackageName(), zzpd()));
        } catch (DeadObjectException unused) {
            Log.w("GmsClient", "service died");
            zzbE(1);
        } catch (RemoteException e) {
            Log.w("GmsClient", "Remote exception occurred", e);
        }
    }

    @Override // com.google.android.gms.common.api.Api.zzb
    public void zza(zzp zzpVar, Set<Scope> set) {
        try {
            GetServiceRequest getServiceRequestZzg = new GetServiceRequest(this.zzafG).zzcl(this.mContext.getPackageName()).zzg(zzly());
            if (set != null) {
                getServiceRequestZzg.zzd(set);
            }
            if (zzlN()) {
                getServiceRequestZzg.zzc(zzoI()).zzc(zzpVar);
            } else if (zzpe()) {
                getServiceRequestZzg.zzc(this.zzQd);
            }
            this.zzafy.zza(new zzd(this, this.zzafH.get()), getServiceRequestZzg);
        } catch (DeadObjectException unused) {
            Log.w("GmsClient", "service died");
            zzbE(1);
        } catch (RemoteException e) {
            Log.w("GmsClient", "Remote exception occurred", e);
        }
    }

    protected Set<Scope> zzb(Set<Scope> set) {
        return set;
    }

    public void zzbE(int i) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(4, this.zzafH.get(), i));
    }

    protected void zzbF(int i) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(6, i, -1, new zzh()));
    }

    protected void zzc(int i, T t) {
    }

    protected abstract String zzfK();

    protected abstract String zzfL();

    @Override // com.google.android.gms.common.api.Api.zzb
    public boolean zzlN() {
        return false;
    }

    protected Bundle zzly() {
        return new Bundle();
    }

    @Override // com.google.android.gms.common.internal.zzk.zza
    public Bundle zzmS() {
        return null;
    }

    @Override // com.google.android.gms.common.api.Api.zzb
    public IBinder zznz() {
        if (this.zzafy == null) {
            return null;
        }
        return this.zzafy.asBinder();
    }

    public final Account zzoI() {
        return this.zzQd != null ? this.zzQd : new Account("<<default account>>", GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
    }

    protected final String zzoV() {
        return this.zzabI.zzoO();
    }

    protected void zzoW() {
    }

    public void zzoZ() {
        int iIsGooglePlayServicesAvailable = this.zzaaP.isGooglePlayServicesAvailable(this.mContext);
        if (iIsGooglePlayServicesAvailable == 0) {
            zza(new zzf());
            return;
        }
        zzb(1, null);
        this.zzafz = new zzf();
        this.mHandler.sendMessage(this.mHandler.obtainMessage(3, this.zzafH.get(), iIsGooglePlayServicesAvailable));
    }

    protected final com.google.android.gms.common.internal.zzf zzpa() {
        return this.zzabI;
    }

    protected final void zzpb() {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected. Call connect() and wait for onConnected() to be called.");
        }
    }

    public final T zzpc() throws DeadObjectException {
        T t;
        synchronized (this.zzpd) {
            if (this.zzafD == 4) {
                throw new DeadObjectException();
            }
            zzpb();
            zzx.zza(this.zzafA != null, "Client is connected but service is null");
            t = this.zzafA;
        }
        return t;
    }

    protected Bundle zzpd() {
        return null;
    }

    public boolean zzpe() {
        return false;
    }
}
