package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.ResolveAccountResponse;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.common.internal.zzp;
import com.google.android.gms.common.internal.zzt;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzlb;
import com.google.android.gms.internal.zzli;
import com.google.android.gms.signin.internal.AuthAccountResult;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzlg implements zzlj {
    private final Context mContext;
    private final GoogleApiAvailability zzaaP;
    private final Api.zza<? extends zzqw, zzqx> zzaaQ;
    private zzqw zzabB;
    private int zzabC;
    private boolean zzabD;
    private boolean zzabE;
    private zzp zzabF;
    private boolean zzabG;
    private boolean zzabH;
    private final com.google.android.gms.common.internal.zzf zzabI;
    private final Map<Api<?>, Integer> zzabJ;
    private final zzli zzabr;
    private final Lock zzabt;
    private ConnectionResult zzabu;
    private int zzabv;
    private int zzaby;
    private int zzabw = 0;
    private boolean zzabx = false;
    private final Bundle zzabz = new Bundle();
    private final Set<Api.zzc> zzabA = new HashSet();
    private ArrayList<Future<?>> zzabK = new ArrayList<>();

    private static class zza extends com.google.android.gms.signin.internal.zzb {
        private final WeakReference<zzlg> zzabM;

        zza(zzlg zzlgVar) {
            this.zzabM = new WeakReference<>(zzlgVar);
        }

        @Override // com.google.android.gms.signin.internal.zzb, com.google.android.gms.signin.internal.zze
        public void zza(final ConnectionResult connectionResult, AuthAccountResult authAccountResult) {
            final zzlg zzlgVar = this.zzabM.get();
            if (zzlgVar == null) {
                return;
            }
            zzlgVar.zzabr.zza(new zzli.zzb(zzlgVar) { // from class: com.google.android.gms.internal.zzlg.zza.1
                @Override // com.google.android.gms.internal.zzli.zzb
                public void zznO() throws RemoteException {
                    zzlgVar.zzc(connectionResult);
                }
            });
        }
    }

    private static class zzb extends zzt.zza {
        private final WeakReference<zzlg> zzabM;

        zzb(zzlg zzlgVar) {
            this.zzabM = new WeakReference<>(zzlgVar);
        }

        @Override // com.google.android.gms.common.internal.zzt
        public void zzb(final ResolveAccountResponse resolveAccountResponse) {
            final zzlg zzlgVar = this.zzabM.get();
            if (zzlgVar == null) {
                return;
            }
            zzlgVar.zzabr.zza(new zzli.zzb(zzlgVar) { // from class: com.google.android.gms.internal.zzlg.zzb.1
                @Override // com.google.android.gms.internal.zzli.zzb
                public void zznO() throws RemoteException {
                    zzlgVar.zza(resolveAccountResponse);
                }
            });
        }
    }

    private class zzc extends zzi {
        private zzc() {
            super();
        }

        @Override // com.google.android.gms.internal.zzlg.zzi
        public void zznO() {
            zzlg.this.zzabB.zza(zzlg.this.zzabF, zzlg.this.zzabr.zzaci, new zza(zzlg.this));
        }
    }

    private static class zzd implements GoogleApiClient.zza {
        private final WeakReference<zzlg> zzabM;
        private final Api<?> zzabS;
        private final int zzabT;

        public zzd(zzlg zzlgVar, Api<?> api, int i) {
            this.zzabM = new WeakReference<>(zzlgVar);
            this.zzabS = api;
            this.zzabT = i;
        }

        @Override // com.google.android.gms.common.api.GoogleApiClient.zza
        public void zza(ConnectionResult connectionResult) {
            zzlg zzlgVar = this.zzabM.get();
            if (zzlgVar == null) {
                return;
            }
            zzx.zza(Looper.myLooper() == zzlgVar.zzabr.getLooper(), "onReportServiceBinding must be called on the GoogleApiClient handler thread");
            zzlgVar.zzabt.lock();
            try {
                if (zzlgVar.zzbn(0)) {
                    if (!connectionResult.isSuccess()) {
                        zzlgVar.zzb(connectionResult, this.zzabS, this.zzabT);
                    }
                    if (zzlgVar.zznP()) {
                        zzlgVar.zznQ();
                    }
                }
            } finally {
                zzlgVar.zzabt.unlock();
            }
        }

        @Override // com.google.android.gms.common.api.GoogleApiClient.zza
        public void zzb(ConnectionResult connectionResult) {
            zzlg zzlgVar = this.zzabM.get();
            if (zzlgVar == null) {
                return;
            }
            zzx.zza(Looper.myLooper() == zzlgVar.zzabr.getLooper(), "onReportAccountValidation must be called on the GoogleApiClient handler thread");
            zzlgVar.zzabt.lock();
            try {
                if (zzlgVar.zzbn(1)) {
                    if (!connectionResult.isSuccess()) {
                        zzlgVar.zzb(connectionResult, this.zzabS, this.zzabT);
                    }
                    if (zzlgVar.zznP()) {
                        zzlgVar.zznS();
                    }
                }
            } finally {
                zzlgVar.zzabt.unlock();
            }
        }
    }

    private class zze extends zzi {
        private final Map<Api.zzb, GoogleApiClient.zza> zzabU;

        public zze(Map<Api.zzb, GoogleApiClient.zza> map) {
            super();
            this.zzabU = map;
        }

        @Override // com.google.android.gms.internal.zzlg.zzi
        public void zznO() {
            int iIsGooglePlayServicesAvailable = zzlg.this.zzaaP.isGooglePlayServicesAvailable(zzlg.this.mContext);
            if (iIsGooglePlayServicesAvailable != 0) {
                final ConnectionResult connectionResult = new ConnectionResult(iIsGooglePlayServicesAvailable, null);
                zzlg.this.zzabr.zza(new zzli.zzb(zzlg.this) { // from class: com.google.android.gms.internal.zzlg.zze.1
                    @Override // com.google.android.gms.internal.zzli.zzb
                    public void zznO() {
                        zzlg.this.zzf(connectionResult);
                    }
                });
                return;
            }
            if (zzlg.this.zzabD) {
                zzlg.this.zzabB.connect();
            }
            for (Api.zzb zzbVar : this.zzabU.keySet()) {
                zzbVar.zza(this.zzabU.get(zzbVar));
            }
        }
    }

    private class zzf extends zzi {
        private final ArrayList<Api.zzb> zzabX;

        public zzf(ArrayList<Api.zzb> arrayList) {
            super();
            this.zzabX = arrayList;
        }

        @Override // com.google.android.gms.internal.zzlg.zzi
        public void zznO() {
            Set<Scope> setZznX = zzlg.this.zzabr.zzaci;
            if (setZznX.isEmpty()) {
                setZznX = zzlg.this.zznX();
            }
            Iterator<Api.zzb> it = this.zzabX.iterator();
            while (it.hasNext()) {
                it.next().zza(zzlg.this.zzabF, setZznX);
            }
        }
    }

    private class zzg implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
        private zzg() {
        }

        @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
        public void onConnected(Bundle bundle) {
            zzlg.this.zzabB.zza(new zzb(zzlg.this));
        }

        @Override // com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
        public void onConnectionFailed(ConnectionResult connectionResult) {
            zzlg.this.zzabt.lock();
            try {
                if (zzlg.this.zze(connectionResult)) {
                    zzlg.this.zznV();
                    zzlg.this.zznT();
                } else {
                    zzlg.this.zzf(connectionResult);
                }
            } finally {
                zzlg.this.zzabt.unlock();
            }
        }

        @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
        public void onConnectionSuspended(int i) {
        }
    }

    private class zzh extends zzi {
        private final ArrayList<Api.zzb> zzabX;

        public zzh(ArrayList<Api.zzb> arrayList) {
            super();
            this.zzabX = arrayList;
        }

        @Override // com.google.android.gms.internal.zzlg.zzi
        public void zznO() {
            Iterator<Api.zzb> it = this.zzabX.iterator();
            while (it.hasNext()) {
                it.next().zza(zzlg.this.zzabF);
            }
        }
    }

    private abstract class zzi implements Runnable {
        private zzi() {
        }

        /* JADX WARN: Unreachable blocks removed: 1, instructions: 1 */
        @Override // java.lang.Runnable
        public void run() {
            zzlg.this.zzabt.lock();
            try {
                try {
                } catch (RuntimeException e) {
                    zzlg.this.zzabr.zza(e);
                }
                if (Thread.interrupted()) {
                    return;
                }
                zznO();
            } finally {
                zzlg.this.zzabt.unlock();
            }
        }

        protected abstract void zznO();
    }

    public zzlg(zzli zzliVar, com.google.android.gms.common.internal.zzf zzfVar, Map<Api<?>, Integer> map, GoogleApiAvailability googleApiAvailability, Api.zza<? extends zzqw, zzqx> zzaVar, Lock lock, Context context) {
        this.zzabr = zzliVar;
        this.zzabI = zzfVar;
        this.zzabJ = map;
        this.zzaaP = googleApiAvailability;
        this.zzaaQ = zzaVar;
        this.zzabt = lock;
        this.mContext = context;
    }

    private void zzY(boolean z) {
        if (this.zzabB != null) {
            if (this.zzabB.isConnected() && z) {
                this.zzabB.zzCe();
            }
            this.zzabB.disconnect();
            this.zzabF = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zza(ResolveAccountResponse resolveAccountResponse) throws RemoteException {
        if (zzbn(0)) {
            ConnectionResult connectionResultZzpr = resolveAccountResponse.zzpr();
            if (connectionResultZzpr.isSuccess()) {
                this.zzabF = resolveAccountResponse.zzpq();
                this.zzabE = true;
                this.zzabG = resolveAccountResponse.zzps();
                this.zzabH = resolveAccountResponse.zzpt();
            } else {
                if (!zze(connectionResultZzpr)) {
                    zzf(connectionResultZzpr);
                    return;
                }
                zznV();
            }
            zznQ();
        }
    }

    private boolean zza(int i, int i2, ConnectionResult connectionResult) {
        if (i2 != 1 || zzd(connectionResult)) {
            return this.zzabu == null || i < this.zzabv;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zzb(ConnectionResult connectionResult, Api<?> api, int i) {
        if (i != 2) {
            int priority = api.zznv().getPriority();
            if (zza(priority, i, connectionResult)) {
                this.zzabu = connectionResult;
                this.zzabv = priority;
            }
        }
        this.zzabr.zzach.put(api.zznx(), connectionResult);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean zzbn(int i) {
        if (this.zzabw == i) {
            return true;
        }
        Log.i("GoogleApiClientConnecting", this.zzabr.zzog());
        Log.wtf("GoogleApiClientConnecting", "GoogleApiClient connecting is in step " + zzbo(this.zzabw) + " but received callback for step " + zzbo(i), new Exception());
        zzf(new ConnectionResult(8, null));
        return false;
    }

    private String zzbo(int i) {
        switch (i) {
            case 0:
                return "STEP_GETTING_SERVICE_BINDINGS";
            case 1:
                return "STEP_VALIDATING_ACCOUNT";
            case 2:
                return "STEP_AUTHENTICATING";
            case 3:
                return "STEP_GETTING_REMOTE_SERVICE";
            default:
                return "UNKNOWN";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zzc(ConnectionResult connectionResult) throws RemoteException {
        if (zzbn(2)) {
            if (!connectionResult.isSuccess()) {
                if (!zze(connectionResult)) {
                    zzf(connectionResult);
                    return;
                }
                zznV();
            }
            zznT();
        }
    }

    private boolean zzd(ConnectionResult connectionResult) {
        return connectionResult.hasResolution() || this.zzaaP.zzbi(connectionResult.getErrorCode()) != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean zze(ConnectionResult connectionResult) {
        if (this.zzabC != 2) {
            return this.zzabC == 1 && !connectionResult.hasResolution();
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zzf(ConnectionResult connectionResult) {
        zznW();
        zzY(!connectionResult.hasResolution());
        this.zzabr.zzach.clear();
        this.zzabr.zzg(connectionResult);
        if (!this.zzaaP.zzd(this.mContext, connectionResult.getErrorCode())) {
            this.zzabr.zzof();
        }
        if (!this.zzabx && !this.zzabr.zzoc()) {
            this.zzabr.zzabZ.zzi(connectionResult);
        }
        this.zzabx = false;
        this.zzabr.zzabZ.zzpk();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean zznP() {
        ConnectionResult connectionResult;
        this.zzaby--;
        if (this.zzaby > 0) {
            return false;
        }
        if (this.zzaby < 0) {
            Log.i("GoogleApiClientConnecting", this.zzabr.zzog());
            Log.wtf("GoogleApiClientConnecting", "GoogleApiClient received too many callbacks for the given step. Clients may be in an unexpected state; GoogleApiClient will now disconnect.", new Exception());
            connectionResult = new ConnectionResult(8, null);
        } else {
            if (this.zzabu == null) {
                return true;
            }
            connectionResult = this.zzabu;
        }
        zzf(connectionResult);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zznQ() throws RemoteException {
        if (this.zzaby != 0) {
            return;
        }
        if (!this.zzabD) {
            zznT();
        } else if (this.zzabE) {
            zznR();
        }
    }

    private void zznR() {
        ArrayList arrayList = new ArrayList();
        this.zzabw = 1;
        this.zzaby = this.zzabr.zzacg.size();
        for (Api.zzc<?> zzcVar : this.zzabr.zzacg.keySet()) {
            if (!this.zzabr.zzach.containsKey(zzcVar)) {
                arrayList.add(this.zzabr.zzacg.get(zzcVar));
            } else if (zznP()) {
                zznS();
            }
        }
        if (arrayList.isEmpty()) {
            return;
        }
        this.zzabK.add(zzlk.zzoj().submit(new zzh(arrayList)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zznS() {
        this.zzabw = 2;
        this.zzabr.zzaci = zznX();
        this.zzabK.add(zzlk.zzoj().submit(new zzc()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zznT() throws RemoteException {
        ArrayList arrayList = new ArrayList();
        this.zzabw = 3;
        this.zzaby = this.zzabr.zzacg.size();
        for (Api.zzc<?> zzcVar : this.zzabr.zzacg.keySet()) {
            if (!this.zzabr.zzach.containsKey(zzcVar)) {
                arrayList.add(this.zzabr.zzacg.get(zzcVar));
            } else if (zznP()) {
                zznU();
            }
        }
        if (arrayList.isEmpty()) {
            return;
        }
        this.zzabK.add(zzlk.zzoj().submit(new zzf(arrayList)));
    }

    private void zznU() throws RemoteException {
        this.zzabr.zzob();
        zzlk.zzoj().execute(new Runnable() { // from class: com.google.android.gms.internal.zzlg.1
            @Override // java.lang.Runnable
            public void run() {
                zzlg.this.zzaaP.zzac(zzlg.this.mContext);
            }
        });
        if (this.zzabB != null) {
            if (this.zzabG) {
                this.zzabB.zza(this.zzabF, this.zzabH);
            }
            zzY(false);
        }
        Iterator<Api.zzc<?>> it = this.zzabr.zzach.keySet().iterator();
        while (it.hasNext()) {
            this.zzabr.zzacg.get(it.next()).disconnect();
        }
        if (!this.zzabx) {
            this.zzabr.zzabZ.zzh(this.zzabz.isEmpty() ? null : this.zzabz);
        } else {
            this.zzabx = false;
            disconnect();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zznV() {
        this.zzabD = false;
        this.zzabr.zzaci = Collections.emptySet();
        for (Api.zzc<?> zzcVar : this.zzabA) {
            if (!this.zzabr.zzach.containsKey(zzcVar)) {
                this.zzabr.zzach.put(zzcVar, new ConnectionResult(17, null));
            }
        }
    }

    private void zznW() {
        Iterator<Future<?>> it = this.zzabK.iterator();
        while (it.hasNext()) {
            it.next().cancel(true);
        }
        this.zzabK.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Set<Scope> zznX() {
        HashSet hashSet = new HashSet(this.zzabI.zzoK());
        Map<Api<?>, zzf.zza> mapZzoM = this.zzabI.zzoM();
        for (Api<?> api : mapZzoM.keySet()) {
            if (!this.zzabr.zzach.containsKey(api.zznx())) {
                hashSet.addAll(mapZzoM.get(api).zzTm);
            }
        }
        return hashSet;
    }

    @Override // com.google.android.gms.internal.zzlj
    public void begin() {
        this.zzabr.zzabZ.zzpl();
        this.zzabr.zzach.clear();
        this.zzabx = false;
        this.zzabD = false;
        this.zzabu = null;
        this.zzabw = 0;
        this.zzabC = 2;
        this.zzabE = false;
        this.zzabG = false;
        HashMap map = new HashMap();
        boolean z = false;
        for (Api<?> api : this.zzabJ.keySet()) {
            Api.zzb zzbVar = this.zzabr.zzacg.get(api.zznx());
            int iIntValue = this.zzabJ.get(api).intValue();
            z |= api.zznv().getPriority() == 1;
            if (zzbVar.zzlN()) {
                this.zzabD = true;
                if (iIntValue < this.zzabC) {
                    this.zzabC = iIntValue;
                }
                if (iIntValue != 0) {
                    this.zzabA.add(api.zznx());
                }
            }
            map.put(zzbVar, new zzd(this, api, iIntValue));
        }
        if (z) {
            this.zzabD = false;
        }
        if (this.zzabD) {
            this.zzabI.zza(Integer.valueOf(this.zzabr.getSessionId()));
            zzg zzgVar = new zzg();
            this.zzabB = (zzqw) this.zzaaQ.zza(this.mContext, this.zzabr.getLooper(), this.zzabI, this.zzabI.zzoQ(), zzgVar, zzgVar);
        }
        this.zzaby = this.zzabr.zzacg.size();
        this.zzabK.add(zzlk.zzoj().submit(new zze(map)));
    }

    @Override // com.google.android.gms.internal.zzlj
    public void connect() {
        this.zzabx = false;
    }

    @Override // com.google.android.gms.internal.zzlj
    public void disconnect() throws RemoteException {
        Iterator<zzli.zzf<?>> it = this.zzabr.zzaca.iterator();
        while (it.hasNext()) {
            zzli.zzf<?> next = it.next();
            if (next.zznK() != 1) {
                next.cancel();
                it.remove();
            }
        }
        this.zzabr.zznY();
        if (this.zzabu == null && !this.zzabr.zzaca.isEmpty()) {
            this.zzabx = true;
            return;
        }
        zznW();
        zzY(true);
        this.zzabr.zzach.clear();
        this.zzabr.zzg(null);
        this.zzabr.zzabZ.zzpk();
    }

    @Override // com.google.android.gms.internal.zzlj
    public String getName() {
        return "CONNECTING";
    }

    @Override // com.google.android.gms.internal.zzlj
    public void onConnected(Bundle bundle) throws RemoteException {
        if (zzbn(3)) {
            if (bundle != null) {
                this.zzabz.putAll(bundle);
            }
            if (zznP()) {
                zznU();
            }
        }
    }

    @Override // com.google.android.gms.internal.zzlj
    public void onConnectionSuspended(int i) {
        zzf(new ConnectionResult(8, null));
    }

    @Override // com.google.android.gms.internal.zzlj
    public <A extends Api.zzb, R extends Result, T extends zzlb.zza<R, A>> T zza(T t) {
        this.zzabr.zzaca.add(t);
        return t;
    }

    @Override // com.google.android.gms.internal.zzlj
    public void zza(ConnectionResult connectionResult, Api<?> api, int i) throws RemoteException {
        if (zzbn(3)) {
            zzb(connectionResult, api, i);
            if (zznP()) {
                zznU();
            }
        }
    }

    @Override // com.google.android.gms.internal.zzlj
    public <A extends Api.zzb, T extends zzlb.zza<? extends Result, A>> T zzb(T t) {
        throw new IllegalStateException("GoogleApiClient is not connected yet.");
    }
}
