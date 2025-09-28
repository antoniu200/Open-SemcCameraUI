package com.google.android.gms.auth.api;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.credentials.PasswordSpecification;
import com.google.android.gms.auth.api.credentials.internal.zzc;
import com.google.android.gms.auth.api.credentials.internal.zze;
import com.google.android.gms.auth.api.proxy.ProxyApi;
import com.google.android.gms.auth.api.signin.GoogleSignInConfig;
import com.google.android.gms.auth.api.signin.internal.zzb;
import com.google.android.gms.auth.api.signin.internal.zzg;
import com.google.android.gms.auth.api.signin.zzd;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.internal.zzjz;
import com.google.android.gms.internal.zzka;
import com.google.android.gms.internal.zzkb;
import com.google.android.gms.internal.zzke;
import com.google.android.gms.internal.zzkf;
import com.google.android.gms.internal.zzki;
import com.google.android.gms.internal.zzkm;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class Auth {
    public static final Api.zzc<zzki> zzRE = new Api.zzc<>();
    public static final Api.zzc<zze> zzRF = new Api.zzc<>();
    public static final Api.zzc<zzkb> zzRG = new Api.zzc<>();
    public static final Api.zzc<zzg> zzRH = new Api.zzc<>();
    public static final Api.zzc<zzb> zzRI = new Api.zzc<>();
    public static final Api.zzc<zzkf> zzRJ = new Api.zzc<>();
    private static final Api.zza<zzki, zza> zzRK = new Api.zza<zzki, zza>() { // from class: com.google.android.gms.auth.api.Auth.1
        @Override // com.google.android.gms.common.api.Api.zza
        public zzki zza(Context context, Looper looper, zzf zzfVar, zza zzaVar, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
            return new zzki(context, looper, zzfVar, zzaVar, connectionCallbacks, onConnectionFailedListener);
        }
    };
    private static final Api.zza<zze, AuthCredentialsOptions> zzRL = new Api.zza<zze, AuthCredentialsOptions>() { // from class: com.google.android.gms.auth.api.Auth.2
        @Override // com.google.android.gms.common.api.Api.zza
        public zze zza(Context context, Looper looper, zzf zzfVar, AuthCredentialsOptions authCredentialsOptions, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
            return new zze(context, looper, zzfVar, authCredentialsOptions, connectionCallbacks, onConnectionFailedListener);
        }
    };
    private static final Api.zza<zzkb, Api.ApiOptions.NoOptions> zzRM = new Api.zza<zzkb, Api.ApiOptions.NoOptions>() { // from class: com.google.android.gms.auth.api.Auth.3
        @Override // com.google.android.gms.common.api.Api.zza
        /* renamed from: zzc, reason: merged with bridge method [inline-methods] */
        public zzkb zza(Context context, Looper looper, zzf zzfVar, Api.ApiOptions.NoOptions noOptions, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
            return new zzkb(context, looper, zzfVar, connectionCallbacks, onConnectionFailedListener);
        }
    };
    private static final Api.zza<zzkf, Api.ApiOptions.NoOptions> zzRN = new Api.zza<zzkf, Api.ApiOptions.NoOptions>() { // from class: com.google.android.gms.auth.api.Auth.4
        @Override // com.google.android.gms.common.api.Api.zza
        /* renamed from: zzd, reason: merged with bridge method [inline-methods] */
        public zzkf zza(Context context, Looper looper, zzf zzfVar, Api.ApiOptions.NoOptions noOptions, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
            return new zzkf(context, looper, zzfVar, connectionCallbacks, onConnectionFailedListener);
        }
    };
    private static final Api.zza<zzg, com.google.android.gms.auth.api.signin.zzg> zzRO = new Api.zza<zzg, com.google.android.gms.auth.api.signin.zzg>() { // from class: com.google.android.gms.auth.api.Auth.5
        @Override // com.google.android.gms.common.api.Api.zza
        public zzg zza(Context context, Looper looper, zzf zzfVar, com.google.android.gms.auth.api.signin.zzg zzgVar, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
            return new zzg(context, looper, zzfVar, zzgVar, connectionCallbacks, onConnectionFailedListener);
        }
    };
    private static final Api.zza<zzb, GoogleSignInConfig> zzRP = new Api.zza<zzb, GoogleSignInConfig>() { // from class: com.google.android.gms.auth.api.Auth.6
        @Override // com.google.android.gms.common.api.Api.zza
        public zzb zza(Context context, Looper looper, zzf zzfVar, GoogleSignInConfig googleSignInConfig, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
            return new zzb(context, looper, zzfVar, googleSignInConfig, connectionCallbacks, onConnectionFailedListener);
        }
    };
    public static final Api<zza> PROXY_API = new Api<>("Auth.PROXY_API", zzRK, zzRE);
    public static final Api<AuthCredentialsOptions> CREDENTIALS_API = new Api<>("Auth.CREDENTIALS_API", zzRL, zzRF);
    public static final Api<com.google.android.gms.auth.api.signin.zzg> zzRQ = new Api<>("Auth.SIGN_IN_API", zzRO, zzRH);
    public static final Api<GoogleSignInConfig> zzRR = new Api<>("Auth.GOOGLE_SIGN_IN_API", zzRP, zzRI);
    public static final Api<Api.ApiOptions.NoOptions> zzRS = new Api<>("Auth.ACCOUNT_STATUS_API", zzRM, zzRG);
    public static final Api<Api.ApiOptions.NoOptions> zzRT = new Api<>("Auth.CONSENT_API", zzRN, zzRJ);
    public static final ProxyApi ProxyApi = new zzkm();
    public static final CredentialsApi CredentialsApi = new zzc();
    public static final zzjz zzRU = new zzka();
    public static final com.google.android.gms.auth.api.signin.zzf zzRV = new com.google.android.gms.auth.api.signin.internal.zzf();
    public static final zzd zzRW = new com.google.android.gms.auth.api.signin.internal.zza();
    public static final com.google.android.gms.auth.api.consent.zza zzRX = new zzke();

    public static final class AuthCredentialsOptions implements Api.ApiOptions.Optional {
        private final String zzRY;
        private final PasswordSpecification zzRZ;

        public static class Builder {
            private PasswordSpecification zzRZ = PasswordSpecification.zzSt;
        }

        public Bundle zzly() {
            Bundle bundle = new Bundle();
            bundle.putString("consumer_package", this.zzRY);
            bundle.putParcelable("password_specification", this.zzRZ);
            return bundle;
        }
    }

    public static final class zza implements Api.ApiOptions.Optional {
        private final Bundle zzSa;

        public Bundle zzlE() {
            return new Bundle(this.zzSa);
        }
    }

    private Auth() {
    }
}
