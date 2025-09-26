// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.api;

import android.os.Bundle;
import android.os.Handler;
import com.google.android.gms.internal.zzli;
import com.google.android.gms.internal.zzlp;
import java.util.Collection;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzqu;
import com.google.android.gms.internal.zzme;
import java.util.HashSet;
import java.util.ArrayList;
import com.google.android.gms.internal.zzqx;
import com.google.android.gms.internal.zzqw;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.internal.zzf;
import java.util.Map;
import android.view.View;
import java.util.Set;
import android.accounts.Account;
import com.google.android.gms.internal.zzlm;
import com.google.android.gms.internal.zzlb;
import android.support.v4.app.FragmentActivity;
import android.os.Looper;
import android.content.Context;
import java.io.PrintWriter;
import java.io.FileDescriptor;
import java.util.concurrent.TimeUnit;
import com.google.android.gms.common.ConnectionResult;

public abstract class GoogleApiClient
{
    public abstract ConnectionResult blockingConnect();
    
    public abstract ConnectionResult blockingConnect(final long p0, final TimeUnit p1);
    
    public abstract PendingResult<Status> clearDefaultAccountAndReconnect();
    
    public abstract void connect();
    
    public abstract void disconnect();
    
    public abstract void dump(final String p0, final FileDescriptor p1, final PrintWriter p2, final String[] p3);
    
    public abstract ConnectionResult getConnectionResult(final Api<?> p0);
    
    public Context getContext() {
        throw new UnsupportedOperationException();
    }
    
    public Looper getLooper() {
        throw new UnsupportedOperationException();
    }
    
    public int getSessionId() {
        throw new UnsupportedOperationException();
    }
    
    public abstract boolean hasConnectedApi(final Api<?> p0);
    
    public abstract boolean isConnected();
    
    public abstract boolean isConnecting();
    
    public abstract boolean isConnectionCallbacksRegistered(final ConnectionCallbacks p0);
    
    public abstract boolean isConnectionFailedListenerRegistered(final OnConnectionFailedListener p0);
    
    public abstract void reconnect();
    
    public abstract void registerConnectionCallbacks(final ConnectionCallbacks p0);
    
    public abstract void registerConnectionFailedListener(final OnConnectionFailedListener p0);
    
    public abstract void stopAutoManage(final FragmentActivity p0);
    
    public abstract void unregisterConnectionCallbacks(final ConnectionCallbacks p0);
    
    public abstract void unregisterConnectionFailedListener(final OnConnectionFailedListener p0);
    
    public <C extends Api.zzb> C zza(final Api.zzc<C> zzc) {
        throw new UnsupportedOperationException();
    }
    
    public <A extends Api.zzb, R extends Result, T extends zzlb.zza<R, A>> T zza(final T t) {
        throw new UnsupportedOperationException();
    }
    
    public boolean zza(final Api<?> api) {
        throw new UnsupportedOperationException();
    }
    
    public <A extends Api.zzb, T extends zzlb.zza<? extends Result, A>> T zzb(final T t) {
        throw new UnsupportedOperationException();
    }
    
    public <L> zzlm<L> zzo(final L l) {
        throw new UnsupportedOperationException();
    }
    
    public static final class Builder
    {
        private final Context mContext;
        private Account zzQd;
        private String zzRq;
        private final Set<Scope> zzaaF;
        private int zzaaG;
        private View zzaaH;
        private String zzaaI;
        private final Map<Api<?>, zzf.zza> zzaaJ;
        private final Map<Api<?>, Api.ApiOptions> zzaaK;
        private FragmentActivity zzaaL;
        private int zzaaM;
        private OnConnectionFailedListener zzaaN;
        private Looper zzaaO;
        private GoogleApiAvailability zzaaP;
        private Api.zza<? extends zzqw, zzqx> zzaaQ;
        private final ArrayList<ConnectionCallbacks> zzaaR;
        private final ArrayList<OnConnectionFailedListener> zzaaS;
        private zzqx zzaaT;
        
        public Builder(final Context mContext) {
            this.zzaaF = new HashSet<Scope>();
            this.zzaaJ = new zzme<Api<?>, zzf.zza>();
            this.zzaaK = new zzme<Api<?>, Api.ApiOptions>();
            this.zzaaM = -1;
            this.zzaaP = GoogleApiAvailability.getInstance();
            this.zzaaQ = zzqu.zzRl;
            this.zzaaR = new ArrayList<ConnectionCallbacks>();
            this.zzaaS = new ArrayList<OnConnectionFailedListener>();
            this.mContext = mContext;
            this.zzaaO = mContext.getMainLooper();
            this.zzRq = mContext.getPackageName();
            this.zzaaI = mContext.getClass().getName();
        }
        
        public Builder(final Context context, final ConnectionCallbacks e, final OnConnectionFailedListener e2) {
            this(context);
            zzx.zzb(e, "Must provide a connected listener");
            this.zzaaR.add(e);
            zzx.zzb(e2, "Must provide a connection failed listener");
            this.zzaaS.add(e2);
        }
        
        private <O extends Api.ApiOptions> void zza(final Api<O> api, final O o, int i, final Scope... array) {
            final int n = 0;
            boolean b = true;
            if (i != 1) {
                if (i != 2) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Invalid resolution mode: '");
                    sb.append(i);
                    sb.append("', use a constant from GoogleApiClient.ResolutionMode");
                    throw new IllegalArgumentException(sb.toString());
                }
                b = false;
            }
            final HashSet set = new HashSet(api.zznv().zzm(o));
            int length;
            for (length = array.length, i = n; i < length; ++i) {
                set.add(array[i]);
            }
            this.zzaaJ.put(api, new zzf.zza(set, b));
        }
        
        private void zza(final zzlp zzlp, final GoogleApiClient googleApiClient) {
            zzlp.zza(this.zzaaM, googleApiClient, this.zzaaN);
        }
        
        private GoogleApiClient zznC() {
            final zzli zzli = new zzli(this.mContext.getApplicationContext(), this.zzaaO, this.zznB(), this.zzaaP, this.zzaaQ, this.zzaaK, this.zzaaR, this.zzaaS, this.zzaaM);
            final zzlp zza = zzlp.zza(this.zzaaL);
            if (zza == null) {
                new Handler(this.mContext.getMainLooper()).post((Runnable)new Runnable(this, zzli) {
                    final GoogleApiClient zzWT;
                    final Builder zzaaU;
                    
                    @Override
                    public void run() {
                        if (!this.zzaaU.zzaaL.isFinishing()) {
                            if (this.zzaaU.zzaaL.getSupportFragmentManager().isDestroyed()) {
                                return;
                            }
                            this.zzaaU.zza(zzlp.zzb(this.zzaaU.zzaaL), this.zzWT);
                        }
                    }
                });
                return zzli;
            }
            this.zza(zza, zzli);
            return zzli;
        }
        
        public Builder addApi(final Api<? extends Api.ApiOptions.NotRequiredOptions> api) {
            zzx.zzb(api, "Api must not be null");
            this.zzaaK.put(api, null);
            this.zzaaF.addAll(api.zznv().zzm(null));
            return this;
        }
        
        public <O extends Api.ApiOptions.HasOptions> Builder addApi(final Api<O> api, final O o) {
            zzx.zzb(api, "Api must not be null");
            zzx.zzb(o, "Null options are not permitted for this Api");
            this.zzaaK.put(api, (Api.ApiOptions)o);
            this.zzaaF.addAll(api.zznv().zzm(o));
            return this;
        }
        
        public <O extends Api.ApiOptions.HasOptions> Builder addApiIfAvailable(final Api<O> api, final O o, final Scope... array) {
            zzx.zzb(api, "Api must not be null");
            zzx.zzb(o, "Null options are not permitted for this Api");
            this.zzaaK.put(api, (Api.ApiOptions)o);
            this.zza(api, o, 1, array);
            return this;
        }
        
        public Builder addApiIfAvailable(final Api<? extends Api.ApiOptions.NotRequiredOptions> api, final Scope... array) {
            zzx.zzb(api, "Api must not be null");
            this.zzaaK.put(api, null);
            this.zza((Api<Api.ApiOptions>)api, null, 1, array);
            return this;
        }
        
        public Builder addConnectionCallbacks(final ConnectionCallbacks e) {
            zzx.zzb(e, "Listener must not be null");
            this.zzaaR.add(e);
            return this;
        }
        
        public Builder addOnConnectionFailedListener(final OnConnectionFailedListener e) {
            zzx.zzb(e, "Listener must not be null");
            this.zzaaS.add(e);
            return this;
        }
        
        public Builder addScope(final Scope scope) {
            zzx.zzb(scope, "Scope must not be null");
            this.zzaaF.add(scope);
            return this;
        }
        
        public GoogleApiClient build() {
            zzx.zzb(this.zzaaK.isEmpty() ^ true, (Object)"must call addApi() to add at least one API");
            if (this.zzaaM >= 0) {
                return this.zznC();
            }
            return new zzli(this.mContext, this.zzaaO, this.zznB(), this.zzaaP, this.zzaaQ, this.zzaaK, this.zzaaR, this.zzaaS, -1);
        }
        
        public Builder enableAutoManage(final FragmentActivity fragmentActivity, final int zzaaM, final OnConnectionFailedListener zzaaN) {
            zzx.zzb(zzaaM >= 0, (Object)"clientId must be non-negative");
            this.zzaaM = zzaaM;
            this.zzaaL = zzx.zzb(fragmentActivity, "Null activity is not permitted.");
            this.zzaaN = zzaaN;
            return this;
        }
        
        public Builder enableAutoManage(final FragmentActivity fragmentActivity, final OnConnectionFailedListener onConnectionFailedListener) {
            return this.enableAutoManage(fragmentActivity, 0, onConnectionFailedListener);
        }
        
        public Builder requestServerAuthCode(final String s, final ServerAuthCodeCallbacks serverAuthCodeCallbacks) {
            this.zzaaT = new zzqx.zza().zza(s, serverAuthCodeCallbacks).zzCi();
            return this;
        }
        
        public Builder setAccountName(final String s) {
            Account zzQd;
            if (s == null) {
                zzQd = null;
            }
            else {
                zzQd = new Account(s, "com.google");
            }
            this.zzQd = zzQd;
            return this;
        }
        
        public Builder setGravityForPopups(final int zzaaG) {
            this.zzaaG = zzaaG;
            return this;
        }
        
        public Builder setHandler(final Handler handler) {
            zzx.zzb(handler, "Handler must not be null");
            this.zzaaO = handler.getLooper();
            return this;
        }
        
        public Builder setViewForPopups(final View zzaaH) {
            zzx.zzb(zzaaH, "View must not be null");
            this.zzaaH = zzaaH;
            return this;
        }
        
        public Builder useDefaultAccount() {
            return this.setAccountName("<<default account>>");
        }
        
        public zzf zznB() {
            if (this.zzaaK.containsKey(zzqu.API)) {
                zzx.zza(this.zzaaT == null, (Object)"SignIn.API can't be used in conjunction with requestServerAuthCode.");
                this.zzaaT = (zzqx)this.zzaaK.get(zzqu.API);
            }
            final Account zzQd = this.zzQd;
            final Set<Scope> zzaaF = this.zzaaF;
            final Map<Api<?>, zzf.zza> zzaaJ = this.zzaaJ;
            final int zzaaG = this.zzaaG;
            final View zzaaH = this.zzaaH;
            final String zzRq = this.zzRq;
            final String zzaaI = this.zzaaI;
            zzqx zzqx;
            if (this.zzaaT != null) {
                zzqx = this.zzaaT;
            }
            else {
                zzqx = com.google.android.gms.internal.zzqx.zzaUZ;
            }
            return new zzf(zzQd, zzaaF, zzaaJ, zzaaG, zzaaH, zzRq, zzaaI, zzqx);
        }
    }
    
    public interface ConnectionCallbacks
    {
        public static final int CAUSE_NETWORK_LOST = 2;
        public static final int CAUSE_SERVICE_DISCONNECTED = 1;
        
        void onConnected(final Bundle p0);
        
        void onConnectionSuspended(final int p0);
    }
    
    public interface OnConnectionFailedListener
    {
        void onConnectionFailed(final ConnectionResult p0);
    }
    
    public interface ServerAuthCodeCallbacks
    {
        CheckResult onCheckServerAuthorization(final String p0, final Set<Scope> p1);
        
        boolean onUploadServerAuthCode(final String p0, final String p1);
        
        public static class CheckResult
        {
            private Set<Scope> zzTm;
            private boolean zzaaV;
            
            private CheckResult(final boolean zzaaV, final Set<Scope> zzTm) {
                this.zzaaV = zzaaV;
                this.zzTm = zzTm;
            }
            
            public static CheckResult newAuthNotRequiredResult() {
                return new CheckResult(false, null);
            }
            
            public static CheckResult newAuthRequiredResult(final Set<Scope> set) {
                zzx.zzb(set != null && !set.isEmpty(), (Object)"A non-empty scope set is required if further auth is needed.");
                return new CheckResult(true, set);
            }
            
            public boolean zznD() {
                return this.zzaaV;
            }
            
            public Set<Scope> zznE() {
                return this.zzTm;
            }
        }
    }
    
    public interface zza
    {
        void zza(final ConnectionResult p0);
        
        void zzb(final ConnectionResult p0);
    }
}
