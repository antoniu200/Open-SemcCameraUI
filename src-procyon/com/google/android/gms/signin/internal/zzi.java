// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.signin.internal;

import java.util.Collections;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import com.google.android.gms.common.internal.ResolveAccountResponse;
import com.google.android.gms.common.internal.ResolveAccountRequest;
import com.google.android.gms.common.internal.zzt;
import android.app.PendingIntent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.AuthAccountRequest;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.api.Scope;
import java.util.Set;
import com.google.android.gms.common.internal.zzp;
import android.os.IInterface;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.os.Parcelable;
import com.google.android.gms.common.internal.BinderWrapper;
import android.os.Bundle;
import com.google.android.gms.common.api.GoogleApiClient;
import android.os.Looper;
import android.content.Context;
import com.google.android.gms.internal.zzqx;
import java.util.concurrent.ExecutorService;
import com.google.android.gms.internal.zzqw;
import com.google.android.gms.common.internal.zzj;

public class zzi extends zzj<zzf> implements zzqw
{
    private final boolean zzaVl;
    private final ExecutorService zzaVm;
    private final zzqx zzaaT;
    private final com.google.android.gms.common.internal.zzf zzabI;
    private Integer zzafj;
    
    public zzi(final Context context, final Looper looper, final boolean zzaVl, final com.google.android.gms.common.internal.zzf zzabI, final zzqx zzaaT, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener, final ExecutorService zzaVm) {
        super(context, looper, 44, zzabI, connectionCallbacks, onConnectionFailedListener);
        this.zzaVl = zzaVl;
        this.zzabI = zzabI;
        this.zzaaT = zzaaT;
        this.zzafj = zzabI.zzoR();
        this.zzaVm = zzaVm;
    }
    
    public static Bundle zza(final zzqx zzqx, final Integer n, final ExecutorService executorService) {
        final Bundle bundle = new Bundle();
        bundle.putBoolean("com.google.android.gms.signin.internal.offlineAccessRequested", zzqx.zzCf());
        bundle.putBoolean("com.google.android.gms.signin.internal.idTokenRequested", zzqx.zzlY());
        bundle.putString("com.google.android.gms.signin.internal.serverClientId", zzqx.zzmb());
        if (zzqx.zzCg() != null) {
            bundle.putParcelable("com.google.android.gms.signin.internal.signInCallbacks", (Parcelable)new BinderWrapper(((com.google.android.gms.signin.internal.zzd.zza)new zza(zzqx, executorService)).asBinder()));
        }
        if (n != null) {
            bundle.putInt("com.google.android.gms.common.internal.ClientSettings.sessionId", (int)n);
        }
        bundle.putBoolean("com.google.android.gms.signin.internal.usePromptModeForAuthCode", zzqx.zzCh());
        bundle.putBoolean("com.google.android.gms.signin.internal.forceCodeForRefreshToken", zzqx.zzma());
        return bundle;
    }
    
    @Override
    public void connect() {
        this.zza(new zzj.zzf());
    }
    
    @Override
    public void zzCe() {
        try {
            this.zzpc().zzjq(this.zzafj);
        }
        catch (final RemoteException ex) {
            Log.w("SignInClientImpl", "Remote service probably died when clearAccountFromSessionStore is called");
        }
    }
    
    @Override
    public void zza(final zzp ex, final Set<Scope> set, final zze zze) {
        zzx.zzb(zze, "Expecting a valid ISignInCallbacks");
        try {
            this.zzpc().zza(new AuthAccountRequest((zzp)ex, set), zze);
        }
        catch (final RemoteException ex) {
            Log.w("SignInClientImpl", "Remote service probably died when authAccount is called");
            try {
                zze.zza(new ConnectionResult(8, null), new AuthAccountResult());
            }
            catch (final RemoteException ex2) {
                Log.wtf("SignInClientImpl", "ISignInCallbacks#onAuthAccount should be executed from the same process, unexpected RemoteException.", (Throwable)ex);
            }
        }
    }
    
    @Override
    public void zza(final zzp zzp, final boolean b) {
        try {
            this.zzpc().zza(zzp, this.zzafj, b);
        }
        catch (final RemoteException ex) {
            Log.w("SignInClientImpl", "Remote service probably died when saveDefaultAccount is called");
        }
    }
    
    @Override
    public void zza(final zzt zzt) {
        zzx.zzb(zzt, "Expecting a valid IResolveAccountCallbacks");
        try {
            this.zzpc().zza(new ResolveAccountRequest(this.zzabI.zzoI(), this.zzafj), zzt);
        }
        catch (final RemoteException ex) {
            Log.w("SignInClientImpl", "Remote service probably died when resolveAccount is called");
            try {
                zzt.zzb(new ResolveAccountResponse(8));
            }
            catch (final RemoteException ex2) {
                Log.wtf("SignInClientImpl", "IResolveAccountCallbacks#onAccountResolutionComplete should be executed from the same process, unexpected RemoteException.", (Throwable)ex);
            }
        }
    }
    
    protected zzf zzdO(final IBinder binder) {
        return com.google.android.gms.signin.internal.zzf.zza.zzdN(binder);
    }
    
    @Override
    protected String zzfK() {
        return "com.google.android.gms.signin.service.START";
    }
    
    @Override
    protected String zzfL() {
        return "com.google.android.gms.signin.internal.ISignInService";
    }
    
    @Override
    public boolean zzlN() {
        return this.zzaVl;
    }
    
    @Override
    protected Bundle zzly() {
        final Bundle zza = zza(this.zzaaT, this.zzabI.zzoR(), this.zzaVm);
        if (!this.getContext().getPackageName().equals(this.zzabI.zzoN())) {
            zza.putString("com.google.android.gms.signin.internal.realClientPackageName", this.zzabI.zzoN());
        }
        return zza;
    }
    
    private static class zza extends com.google.android.gms.signin.internal.zzd.zza
    {
        private final ExecutorService zzaVm;
        private final zzqx zzaaT;
        
        public zza(final zzqx zzaaT, final ExecutorService zzaVm) {
            this.zzaaT = zzaaT;
            this.zzaVm = zzaVm;
        }
        
        private GoogleApiClient.ServerAuthCodeCallbacks zzCg() throws RemoteException {
            return this.zzaaT.zzCg();
        }
        
        public void zza(final String s, final String s2, final zzf zzf) throws RemoteException {
            this.zzaVm.submit(new Runnable(this, s, s2, zzf) {
                final String zzaVo;
                final zzf zzaVp;
                final zza zzaVq;
                final String zzaVr;
                
                @Override
                public void run() {
                    try {
                        this.zzaVp.zzaq(this.zzaVq.zzCg().onUploadServerAuthCode(this.zzaVo, this.zzaVr));
                    }
                    catch (final RemoteException ex) {
                        Log.e("SignInClientImpl", "RemoteException thrown when processing uploadServerAuthCode callback", (Throwable)ex);
                    }
                }
            });
        }
        
        public void zza(final String s, final List<Scope> list, final zzf zzf) throws RemoteException {
            this.zzaVm.submit(new Runnable(this, list, s, zzf) {
                final List zzaVn;
                final String zzaVo;
                final zzf zzaVp;
                final zza zzaVq;
                
                @Override
                public void run() {
                    try {
                        final GoogleApiClient.ServerAuthCodeCallbacks.CheckResult onCheckServerAuthorization = this.zzaVq.zzCg().onCheckServerAuthorization(this.zzaVo, Collections.unmodifiableSet((Set<? extends Scope>)new HashSet<Scope>(this.zzaVn)));
                        this.zzaVp.zza(new CheckServerAuthResult(onCheckServerAuthorization.zznD(), onCheckServerAuthorization.zznE()));
                    }
                    catch (final RemoteException ex) {
                        Log.e("SignInClientImpl", "RemoteException thrown when processing checkServerAuthorization callback", (Throwable)ex);
                    }
                }
            });
        }
    }
}
