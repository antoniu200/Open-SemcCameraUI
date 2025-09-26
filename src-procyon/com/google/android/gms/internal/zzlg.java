// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import com.google.android.gms.common.internal.zzx;
import android.os.Looper;
import com.google.android.gms.signin.internal.zze;
import com.google.android.gms.common.internal.zzt;
import com.google.android.gms.signin.internal.AuthAccountResult;
import java.lang.ref.WeakReference;
import com.google.android.gms.signin.internal.zzb;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.GoogleApiClient;
import java.util.HashMap;
import java.util.Collection;
import com.google.android.gms.common.api.Scope;
import java.util.Collections;
import java.util.Iterator;
import android.app.PendingIntent;
import android.util.Log;
import com.google.android.gms.common.internal.ResolveAccountResponse;
import java.util.HashSet;
import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.Map;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.common.internal.zzp;
import java.util.Set;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.GoogleApiAvailability;
import android.content.Context;

public class zzlg implements zzlj
{
    private final Context mContext;
    private final GoogleApiAvailability zzaaP;
    private final Api.zza<? extends zzqw, zzqx> zzaaQ;
    private final Set<Api.zzc> zzabA;
    private zzqw zzabB;
    private int zzabC;
    private boolean zzabD;
    private boolean zzabE;
    private zzp zzabF;
    private boolean zzabG;
    private boolean zzabH;
    private final com.google.android.gms.common.internal.zzf zzabI;
    private final Map<Api<?>, Integer> zzabJ;
    private ArrayList<Future<?>> zzabK;
    private final zzli zzabr;
    private final Lock zzabt;
    private ConnectionResult zzabu;
    private int zzabv;
    private int zzabw;
    private boolean zzabx;
    private int zzaby;
    private final Bundle zzabz;
    
    public zzlg(final zzli zzabr, final com.google.android.gms.common.internal.zzf zzabI, final Map<Api<?>, Integer> zzabJ, final GoogleApiAvailability zzaaP, final Api.zza<? extends zzqw, zzqx> zzaaQ, final Lock zzabt, final Context mContext) {
        this.zzabw = 0;
        this.zzabx = false;
        this.zzabz = new Bundle();
        this.zzabA = new HashSet<Api.zzc>();
        this.zzabK = new ArrayList<Future<?>>();
        this.zzabr = zzabr;
        this.zzabI = zzabI;
        this.zzabJ = zzabJ;
        this.zzaaP = zzaaP;
        this.zzaaQ = zzaaQ;
        this.zzabt = zzabt;
        this.mContext = mContext;
    }
    
    private void zzY(final boolean b) {
        if (this.zzabB != null) {
            if (((Api.zzb)this.zzabB).isConnected() && b) {
                this.zzabB.zzCe();
            }
            ((Api.zzb)this.zzabB).disconnect();
            this.zzabF = null;
        }
    }
    
    private void zza(final ResolveAccountResponse resolveAccountResponse) {
        if (!this.zzbn(0)) {
            return;
        }
        final ConnectionResult zzpr = resolveAccountResponse.zzpr();
        if (zzpr.isSuccess()) {
            this.zzabF = resolveAccountResponse.zzpq();
            this.zzabE = true;
            this.zzabG = resolveAccountResponse.zzps();
            this.zzabH = resolveAccountResponse.zzpt();
        }
        else {
            if (!this.zze(zzpr)) {
                this.zzf(zzpr);
                return;
            }
            this.zznV();
        }
        this.zznQ();
    }
    
    private boolean zza(final int n, final int n2, final ConnectionResult connectionResult) {
        boolean b = false;
        if (n2 == 1 && !this.zzd(connectionResult)) {
            return false;
        }
        if (this.zzabu == null || n < this.zzabv) {
            b = true;
        }
        return b;
    }
    
    private void zzb(final ConnectionResult zzabu, final Api<?> api, final int n) {
        if (n != 2) {
            final int priority = api.zznv().getPriority();
            if (this.zza(priority, n, zzabu)) {
                this.zzabu = zzabu;
                this.zzabv = priority;
            }
        }
        this.zzabr.zzach.put(api.zznx(), zzabu);
    }
    
    private boolean zzbn(final int n) {
        if (this.zzabw != n) {
            Log.i("GoogleApiClientConnecting", this.zzabr.zzog());
            final StringBuilder sb = new StringBuilder();
            sb.append("GoogleApiClient connecting is in step ");
            sb.append(this.zzbo(this.zzabw));
            sb.append(" but received callback for step ");
            sb.append(this.zzbo(n));
            Log.wtf("GoogleApiClientConnecting", sb.toString(), (Throwable)new Exception());
            this.zzf(new ConnectionResult(8, null));
            return false;
        }
        return true;
    }
    
    private String zzbo(final int n) {
        switch (n) {
            default: {
                return "UNKNOWN";
            }
            case 3: {
                return "STEP_GETTING_REMOTE_SERVICE";
            }
            case 2: {
                return "STEP_AUTHENTICATING";
            }
            case 1: {
                return "STEP_VALIDATING_ACCOUNT";
            }
            case 0: {
                return "STEP_GETTING_SERVICE_BINDINGS";
            }
        }
    }
    
    private void zzc(final ConnectionResult connectionResult) {
        if (!this.zzbn(2)) {
            return;
        }
        if (!connectionResult.isSuccess()) {
            if (!this.zze(connectionResult)) {
                this.zzf(connectionResult);
                return;
            }
            this.zznV();
        }
        this.zznT();
    }
    
    private boolean zzd(final ConnectionResult connectionResult) {
        return connectionResult.hasResolution() || this.zzaaP.zzbi(connectionResult.getErrorCode()) != null;
    }
    
    private boolean zze(final ConnectionResult connectionResult) {
        final int zzabC = this.zzabC;
        boolean b = true;
        if (zzabC != 2) {
            if (this.zzabC == 1 && !connectionResult.hasResolution()) {
                return true;
            }
            b = false;
        }
        return b;
    }
    
    private void zzf(final ConnectionResult connectionResult) {
        this.zznW();
        this.zzY(connectionResult.hasResolution() ^ true);
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
    
    private boolean zznP() {
        --this.zzaby;
        if (this.zzaby > 0) {
            return false;
        }
        ConnectionResult zzabu;
        if (this.zzaby < 0) {
            Log.i("GoogleApiClientConnecting", this.zzabr.zzog());
            Log.wtf("GoogleApiClientConnecting", "GoogleApiClient received too many callbacks for the given step. Clients may be in an unexpected state; GoogleApiClient will now disconnect.", (Throwable)new Exception());
            zzabu = new ConnectionResult(8, null);
        }
        else {
            if (this.zzabu == null) {
                return true;
            }
            zzabu = this.zzabu;
        }
        this.zzf(zzabu);
        return false;
    }
    
    private void zznQ() {
        if (this.zzaby != 0) {
            return;
        }
        if (this.zzabD) {
            if (this.zzabE) {
                this.zznR();
            }
        }
        else {
            this.zznT();
        }
    }
    
    private void zznR() {
        final ArrayList list = new ArrayList();
        this.zzabw = 1;
        this.zzaby = this.zzabr.zzacg.size();
        for (final Api.zzc zzc : this.zzabr.zzacg.keySet()) {
            if (this.zzabr.zzach.containsKey(zzc)) {
                if (!this.zznP()) {
                    continue;
                }
                this.zznS();
            }
            else {
                list.add(this.zzabr.zzacg.get(zzc));
            }
        }
        if (!list.isEmpty()) {
            this.zzabK.add(zzlk.zzoj().submit(new zzh(list)));
        }
    }
    
    private void zznS() {
        this.zzabw = 2;
        this.zzabr.zzaci = this.zznX();
        this.zzabK.add(zzlk.zzoj().submit(new zzc()));
    }
    
    private void zznT() {
        final ArrayList list = new ArrayList();
        this.zzabw = 3;
        this.zzaby = this.zzabr.zzacg.size();
        for (final Api.zzc zzc : this.zzabr.zzacg.keySet()) {
            if (this.zzabr.zzach.containsKey(zzc)) {
                if (!this.zznP()) {
                    continue;
                }
                this.zznU();
            }
            else {
                list.add(this.zzabr.zzacg.get(zzc));
            }
        }
        if (!list.isEmpty()) {
            this.zzabK.add(zzlk.zzoj().submit(new zzf(list)));
        }
    }
    
    private void zznU() {
        this.zzabr.zzob();
        zzlk.zzoj().execute(new Runnable(this) {
            final zzlg zzabL;
            
            @Override
            public void run() {
                this.zzabL.zzaaP.zzac(this.zzabL.mContext);
            }
        });
        if (this.zzabB != null) {
            if (this.zzabG) {
                this.zzabB.zza(this.zzabF, this.zzabH);
            }
            this.zzY(false);
        }
        final Iterator<Api.zzc<?>> iterator = this.zzabr.zzach.keySet().iterator();
        while (iterator.hasNext()) {
            this.zzabr.zzacg.get(iterator.next()).disconnect();
        }
        if (this.zzabx) {
            this.zzabx = false;
            this.disconnect();
            return;
        }
        Bundle zzabz;
        if (this.zzabz.isEmpty()) {
            zzabz = null;
        }
        else {
            zzabz = this.zzabz;
        }
        this.zzabr.zzabZ.zzh(zzabz);
    }
    
    private void zznV() {
        this.zzabD = false;
        this.zzabr.zzaci = Collections.emptySet();
        for (final Api.zzc zzc : this.zzabA) {
            if (!this.zzabr.zzach.containsKey(zzc)) {
                this.zzabr.zzach.put(zzc, new ConnectionResult(17, null));
            }
        }
    }
    
    private void zznW() {
        final Iterator<Future<?>> iterator = this.zzabK.iterator();
        while (iterator.hasNext()) {
            iterator.next().cancel(true);
        }
        this.zzabK.clear();
    }
    
    private Set<Scope> zznX() {
        final HashSet set = new HashSet((Collection<? extends E>)this.zzabI.zzoK());
        final Map<Api<?>, com.google.android.gms.common.internal.zzf.zza> zzoM = this.zzabI.zzoM();
        for (final Api api : zzoM.keySet()) {
            if (!this.zzabr.zzach.containsKey(api.zznx())) {
                set.addAll(((com.google.android.gms.common.internal.zzf.zza)zzoM.get(api)).zzTm);
            }
        }
        return set;
    }
    
    @Override
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
        final HashMap hashMap = new HashMap();
        final Iterator<Api<?>> iterator = this.zzabJ.keySet().iterator();
        boolean b = false;
        while (iterator.hasNext()) {
            final Api api = iterator.next();
            final Api.zzb zzb = this.zzabr.zzacg.get(api.zznx());
            final int intValue = this.zzabJ.get(api);
            b |= (api.zznv().getPriority() == 1);
            if (zzb.zzlN()) {
                this.zzabD = true;
                if (intValue < this.zzabC) {
                    this.zzabC = intValue;
                }
                if (intValue != 0) {
                    this.zzabA.add((Api.zzc)api.zznx());
                }
            }
            hashMap.put(zzb, new zzd(this, api, intValue));
        }
        if (b) {
            this.zzabD = false;
        }
        if (this.zzabD) {
            this.zzabI.zza(this.zzabr.getSessionId());
            final zzg zzg = new zzg();
            this.zzabB = (zzqw)this.zzaaQ.zza(this.mContext, this.zzabr.getLooper(), this.zzabI, this.zzabI.zzoQ(), zzg, zzg);
        }
        this.zzaby = this.zzabr.zzacg.size();
        this.zzabK.add(zzlk.zzoj().submit(new zze(hashMap)));
    }
    
    @Override
    public void connect() {
        this.zzabx = false;
    }
    
    @Override
    public void disconnect() {
        final Iterator<Object> iterator = this.zzabr.zzaca.iterator();
        while (iterator.hasNext()) {
            final zzli.zzf zzf = iterator.next();
            if (zzf.zznK() != 1) {
                zzf.cancel();
                iterator.remove();
            }
        }
        this.zzabr.zznY();
        if (this.zzabu == null && !this.zzabr.zzaca.isEmpty()) {
            this.zzabx = true;
            return;
        }
        this.zznW();
        this.zzY(true);
        this.zzabr.zzach.clear();
        this.zzabr.zzg(null);
        this.zzabr.zzabZ.zzpk();
    }
    
    @Override
    public String getName() {
        return "CONNECTING";
    }
    
    @Override
    public void onConnected(final Bundle bundle) {
        if (!this.zzbn(3)) {
            return;
        }
        if (bundle != null) {
            this.zzabz.putAll(bundle);
        }
        if (this.zznP()) {
            this.zznU();
        }
    }
    
    @Override
    public void onConnectionSuspended(final int n) {
        this.zzf(new ConnectionResult(8, null));
    }
    
    @Override
    public <A extends Api.zzb, R extends Result, T extends zzlb.zza<R, A>> T zza(final T t) {
        this.zzabr.zzaca.add((zzli.zzf<?>)t);
        return t;
    }
    
    @Override
    public void zza(final ConnectionResult connectionResult, final Api<?> api, final int n) {
        if (!this.zzbn(3)) {
            return;
        }
        this.zzb(connectionResult, api, n);
        if (this.zznP()) {
            this.zznU();
        }
    }
    
    @Override
    public <A extends Api.zzb, T extends zzlb.zza<? extends Result, A>> T zzb(final T t) {
        throw new IllegalStateException("GoogleApiClient is not connected yet.");
    }
    
    private static class zza extends zzb
    {
        private final WeakReference<zzlg> zzabM;
        
        zza(final zzlg referent) {
            this.zzabM = new WeakReference<zzlg>(referent);
        }
        
        @Override
        public void zza(final ConnectionResult connectionResult, final AuthAccountResult authAccountResult) {
            final zzlg zzlg = this.zzabM.get();
            if (zzlg == null) {
                return;
            }
            zzlg.zzabr.zza((zzli.zzb)new zzli.zzb(this, zzlg, zzlg, connectionResult) {
                final zzlg zzabN;
                final ConnectionResult zzabO;
                final zzlg.zza zzabP;
                
                public void zznO() {
                    this.zzabN.zzc(this.zzabO);
                }
            });
        }
    }
    
    private static class zzb extends zzt.zza
    {
        private final WeakReference<zzlg> zzabM;
        
        zzb(final zzlg referent) {
            this.zzabM = new WeakReference<zzlg>(referent);
        }
        
        public void zzb(final ResolveAccountResponse resolveAccountResponse) {
            final zzlg zzlg = this.zzabM.get();
            if (zzlg == null) {
                return;
            }
            zzlg.zzabr.zza((zzli.zzb)new zzli.zzb(this, zzlg, zzlg, resolveAccountResponse) {
                final zzlg zzabN;
                final ResolveAccountResponse zzabQ;
                final zzlg.zzb zzabR;
                
                public void zznO() {
                    this.zzabN.zza(this.zzabQ);
                }
            });
        }
    }
    
    private class zzc extends zzi
    {
        final zzlg zzabL;
        
        private zzc(final zzlg zzabL) {
        }
        
        public void zznO() {
            this.zzabL.zzabB.zza(this.zzabL.zzabF, this.zzabL.zzabr.zzaci, new zza(this.zzabL));
        }
    }
    
    private abstract class zzi implements Runnable
    {
        final zzlg zzabL;
        
        private zzi(final zzlg zzabL) {
            this.zzabL = zzabL;
        }
        
        @Override
        public void run() {
            this.zzabL.zzabt.lock();
            while (true) {
                try {
                    try {
                        if (Thread.interrupted()) {
                            this.zzabL.zzabt.unlock();
                            return;
                        }
                        this.zznO();
                        this.zzabL.zzabt.unlock();
                        return;
                    }
                    finally {}
                }
                catch (final RuntimeException ex) {
                    this.zzabL.zzabr.zza(ex);
                    continue;
                }
                break;
            }
            this.zzabL.zzabt.unlock();
        }
        
        protected abstract void zznO();
    }
    
    private static class zzd implements GoogleApiClient.zza
    {
        private final WeakReference<zzlg> zzabM;
        private final Api<?> zzabS;
        private final int zzabT;
        
        public zzd(final zzlg referent, final Api<?> zzabS, final int zzabT) {
            this.zzabM = new WeakReference<zzlg>(referent);
            this.zzabS = zzabS;
            this.zzabT = zzabT;
        }
        
        @Override
        public void zza(final ConnectionResult connectionResult) {
            final zzlg zzlg = this.zzabM.get();
            if (zzlg == null) {
                return;
            }
            zzx.zza(Looper.myLooper() == zzlg.zzabr.getLooper(), (Object)"onReportServiceBinding must be called on the GoogleApiClient handler thread");
            zzlg.zzabt.lock();
            try {
                if (!zzlg.zzbn(0)) {
                    return;
                }
                if (!connectionResult.isSuccess()) {
                    zzlg.zzb(connectionResult, this.zzabS, this.zzabT);
                }
                if (zzlg.zznP()) {
                    zzlg.zznQ();
                }
            }
            finally {
                zzlg.zzabt.unlock();
            }
        }
        
        @Override
        public void zzb(final ConnectionResult connectionResult) {
            final zzlg zzlg = this.zzabM.get();
            if (zzlg == null) {
                return;
            }
            zzx.zza(Looper.myLooper() == zzlg.zzabr.getLooper(), (Object)"onReportAccountValidation must be called on the GoogleApiClient handler thread");
            zzlg.zzabt.lock();
            try {
                if (!zzlg.zzbn(1)) {
                    return;
                }
                if (!connectionResult.isSuccess()) {
                    zzlg.zzb(connectionResult, this.zzabS, this.zzabT);
                }
                if (zzlg.zznP()) {
                    zzlg.zznS();
                }
            }
            finally {
                zzlg.zzabt.unlock();
            }
        }
    }
    
    private class zze extends zzi
    {
        final zzlg zzabL;
        private final Map<Api.zzb, GoogleApiClient.zza> zzabU;
        
        public zze(final zzlg zzabL, final Map<Api.zzb, GoogleApiClient.zza> zzabU) {
            this.zzabU = zzabU;
        }
        
        public void zznO() {
            final int googlePlayServicesAvailable = this.zzabL.zzaaP.isGooglePlayServicesAvailable(this.zzabL.mContext);
            if (googlePlayServicesAvailable != 0) {
                this.zzabL.zzabr.zza((zzli.zzb)new zzli.zzb(this, this.zzabL, new ConnectionResult(googlePlayServicesAvailable, null)) {
                    final ConnectionResult zzabV;
                    final zzlg.zze zzabW;
                    
                    public void zznO() {
                        this.zzabW.zzabL.zzf(this.zzabV);
                    }
                });
                return;
            }
            if (this.zzabL.zzabD) {
                this.zzabL.zzabB.connect();
            }
            for (final Api.zzb zzb : this.zzabU.keySet()) {
                zzb.zza(this.zzabU.get(zzb));
            }
        }
    }
    
    private class zzf extends zzi
    {
        final zzlg zzabL;
        private final ArrayList<Api.zzb> zzabX;
        
        public zzf(final zzlg zzabL, final ArrayList<Api.zzb> zzabX) {
            this.zzabX = zzabX;
        }
        
        public void zznO() {
            Set<Scope> set;
            if ((set = this.zzabL.zzabr.zzaci).isEmpty()) {
                set = this.zzabL.zznX();
            }
            final Iterator<Api.zzb> iterator = this.zzabX.iterator();
            while (iterator.hasNext()) {
                iterator.next().zza(this.zzabL.zzabF, set);
            }
        }
    }
    
    private class zzg implements ConnectionCallbacks, OnConnectionFailedListener
    {
        final zzlg zzabL;
        
        private zzg(final zzlg zzabL) {
            this.zzabL = zzabL;
        }
        
        @Override
        public void onConnected(final Bundle bundle) {
            this.zzabL.zzabB.zza(new zzb(this.zzabL));
        }
        
        @Override
        public void onConnectionFailed(final ConnectionResult connectionResult) {
            this.zzabL.zzabt.lock();
            try {
                if (this.zzabL.zze(connectionResult)) {
                    this.zzabL.zznV();
                    this.zzabL.zznT();
                }
                else {
                    this.zzabL.zzf(connectionResult);
                }
            }
            finally {
                this.zzabL.zzabt.unlock();
            }
        }
        
        @Override
        public void onConnectionSuspended(final int n) {
        }
    }
    
    private class zzh extends zzi
    {
        final zzlg zzabL;
        private final ArrayList<Api.zzb> zzabX;
        
        public zzh(final zzlg zzabL, final ArrayList<Api.zzb> zzabX) {
            this.zzabX = zzabX;
        }
        
        public void zznO() {
            final Iterator<Api.zzb> iterator = this.zzabX.iterator();
            while (iterator.hasNext()) {
                iterator.next().zza(this.zzabL.zzabF);
            }
        }
    }
}
