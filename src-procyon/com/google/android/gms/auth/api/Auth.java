// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.api;

import android.os.Parcelable;
import android.os.Bundle;
import com.google.android.gms.auth.api.credentials.PasswordSpecification;
import com.google.android.gms.internal.zzke;
import com.google.android.gms.internal.zzka;
import com.google.android.gms.auth.api.credentials.internal.zzc;
import com.google.android.gms.internal.zzkm;
import com.google.android.gms.common.api.GoogleApiClient;
import android.os.Looper;
import android.content.Context;
import com.google.android.gms.auth.api.consent.zza;
import com.google.android.gms.auth.api.signin.zzd;
import com.google.android.gms.auth.api.signin.zzf;
import com.google.android.gms.internal.zzjz;
import com.google.android.gms.auth.api.signin.GoogleSignInConfig;
import com.google.android.gms.internal.zzkf;
import com.google.android.gms.auth.api.signin.internal.zzb;
import com.google.android.gms.auth.api.signin.internal.zzg;
import com.google.android.gms.internal.zzkb;
import com.google.android.gms.auth.api.credentials.internal.zze;
import com.google.android.gms.internal.zzki;
import com.google.android.gms.auth.api.proxy.ProxyApi;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.common.api.Api;

public final class Auth
{
    public static final Api<AuthCredentialsOptions> CREDENTIALS_API;
    public static final CredentialsApi CredentialsApi;
    public static final Api<zza> PROXY_API;
    public static final ProxyApi ProxyApi;
    public static final Api.zzc<zzki> zzRE;
    public static final Api.zzc<zze> zzRF;
    public static final Api.zzc<zzkb> zzRG;
    public static final Api.zzc<zzg> zzRH;
    public static final Api.zzc<zzb> zzRI;
    public static final Api.zzc<zzkf> zzRJ;
    private static final Api.zza<zzki, zza> zzRK;
    private static final Api.zza<zze, AuthCredentialsOptions> zzRL;
    private static final Api.zza<zzkb, Api.ApiOptions.NoOptions> zzRM;
    private static final Api.zza<zzkf, Api.ApiOptions.NoOptions> zzRN;
    private static final Api.zza<zzg, com.google.android.gms.auth.api.signin.zzg> zzRO;
    private static final Api.zza<zzb, GoogleSignInConfig> zzRP;
    public static final Api<com.google.android.gms.auth.api.signin.zzg> zzRQ;
    public static final Api<GoogleSignInConfig> zzRR;
    public static final Api<Api.ApiOptions.NoOptions> zzRS;
    public static final Api<Api.ApiOptions.NoOptions> zzRT;
    public static final zzjz zzRU;
    public static final zzf zzRV;
    public static final zzd zzRW;
    public static final com.google.android.gms.auth.api.consent.zza zzRX;
    
    static {
        zzRE = new Api.zzc();
        zzRF = new Api.zzc();
        zzRG = new Api.zzc();
        zzRH = new Api.zzc();
        zzRI = new Api.zzc();
        zzRJ = new Api.zzc();
        zzRK = new Api.zza<zzki, zza>() {
            public zzki zza(final Context context, final Looper looper, final com.google.android.gms.common.internal.zzf zzf, final Auth.zza zza, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
                return new zzki(context, looper, zzf, zza, connectionCallbacks, onConnectionFailedListener);
            }
        };
        zzRL = new Api.zza<zze, AuthCredentialsOptions>() {
            public com.google.android.gms.auth.api.credentials.internal.zze zza(final Context context, final Looper looper, final com.google.android.gms.common.internal.zzf zzf, final AuthCredentialsOptions authCredentialsOptions, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
                return new com.google.android.gms.auth.api.credentials.internal.zze(context, looper, zzf, authCredentialsOptions, connectionCallbacks, onConnectionFailedListener);
            }
        };
        zzRM = new Api.zza<zzkb, Api.ApiOptions.NoOptions>() {
            public zzkb zzc(final Context context, final Looper looper, final com.google.android.gms.common.internal.zzf zzf, final NoOptions noOptions, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
                return new zzkb(context, looper, zzf, connectionCallbacks, onConnectionFailedListener);
            }
        };
        zzRN = new Api.zza<zzkf, Api.ApiOptions.NoOptions>() {
            public zzkf zzd(final Context context, final Looper looper, final com.google.android.gms.common.internal.zzf zzf, final NoOptions noOptions, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
                return new zzkf(context, looper, zzf, connectionCallbacks, onConnectionFailedListener);
            }
        };
        zzRO = new Api.zza<zzg, com.google.android.gms.auth.api.signin.zzg>() {
            public zzg zza(final Context context, final Looper looper, final com.google.android.gms.common.internal.zzf zzf, final com.google.android.gms.auth.api.signin.zzg zzg, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
                return new zzg(context, looper, zzf, zzg, connectionCallbacks, onConnectionFailedListener);
            }
        };
        zzRP = new Api.zza<zzb, GoogleSignInConfig>() {
            public com.google.android.gms.auth.api.signin.internal.zzb zza(final Context context, final Looper looper, final com.google.android.gms.common.internal.zzf zzf, final GoogleSignInConfig googleSignInConfig, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
                return new com.google.android.gms.auth.api.signin.internal.zzb(context, looper, zzf, googleSignInConfig, connectionCallbacks, onConnectionFailedListener);
            }
        };
        PROXY_API = new Api<zza>("Auth.PROXY_API", (Api.zza<C, zza>)Auth.zzRK, (Api.zzc<C>)Auth.zzRE);
        CREDENTIALS_API = new Api<AuthCredentialsOptions>("Auth.CREDENTIALS_API", (Api.zza<C, AuthCredentialsOptions>)Auth.zzRL, (Api.zzc<C>)Auth.zzRF);
        zzRQ = new Api<com.google.android.gms.auth.api.signin.zzg>("Auth.SIGN_IN_API", (Api.zza<C, com.google.android.gms.auth.api.signin.zzg>)Auth.zzRO, (Api.zzc<C>)Auth.zzRH);
        zzRR = new Api<GoogleSignInConfig>("Auth.GOOGLE_SIGN_IN_API", (Api.zza<C, GoogleSignInConfig>)Auth.zzRP, (Api.zzc<C>)Auth.zzRI);
        zzRS = new Api<Api.ApiOptions.NoOptions>("Auth.ACCOUNT_STATUS_API", (Api.zza<C, Api.ApiOptions.NoOptions>)Auth.zzRM, (Api.zzc<C>)Auth.zzRG);
        zzRT = new Api<Api.ApiOptions.NoOptions>("Auth.CONSENT_API", (Api.zza<C, Api.ApiOptions.NoOptions>)Auth.zzRN, (Api.zzc<C>)Auth.zzRJ);
        ProxyApi = new zzkm();
        CredentialsApi = new zzc();
        zzRU = new zzka();
        zzRV = new com.google.android.gms.auth.api.signin.internal.zzf();
        zzRW = new com.google.android.gms.auth.api.signin.internal.zza();
        zzRX = new zzke();
    }
    
    private Auth() {
    }
    
    public static final class AuthCredentialsOptions implements Optional
    {
        private final String zzRY;
        private final PasswordSpecification zzRZ;
        
        public Bundle zzly() {
            final Bundle bundle = new Bundle();
            bundle.putString("consumer_package", this.zzRY);
            bundle.putParcelable("password_specification", (Parcelable)this.zzRZ);
            return bundle;
        }
        
        public static class Builder
        {
            private PasswordSpecification zzRZ;
            
            public Builder() {
                this.zzRZ = PasswordSpecification.zzSt;
            }
        }
    }
    
    public static final class zza implements Optional
    {
        private final Bundle zzSa;
        
        public Bundle zzlE() {
            return new Bundle(this.zzSa);
        }
    }
}
