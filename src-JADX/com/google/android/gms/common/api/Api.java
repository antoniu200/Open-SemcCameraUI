package com.google.android.gms.common.api;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.zzp;
import com.google.android.gms.common.internal.zzx;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class Api<O extends ApiOptions> {
    private final String mName;
    private final zzc<?> zzZM;
    private final zza<?, O> zzaav;
    private final zze<?, O> zzaaw;
    private final zzf<?> zzaax;

    public interface ApiOptions {

        public interface HasOptions extends ApiOptions {
        }

        public static final class NoOptions implements NotRequiredOptions {
            private NoOptions() {
            }
        }

        public interface NotRequiredOptions extends ApiOptions {
        }

        public interface Optional extends HasOptions, NotRequiredOptions {
        }
    }

    public static abstract class zza<T extends zzb, O> {
        public int getPriority() {
            return Integer.MAX_VALUE;
        }

        public abstract T zza(Context context, Looper looper, com.google.android.gms.common.internal.zzf zzfVar, O o, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener);

        public List<Scope> zzm(O o) {
            return Collections.emptyList();
        }
    }

    public interface zzb {
        void disconnect();

        void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr);

        boolean isConnected();

        void zza(GoogleApiClient.zza zzaVar);

        void zza(zzp zzpVar);

        void zza(zzp zzpVar, Set<Scope> set);

        boolean zzlN();

        IBinder zznz();
    }

    public static final class zzc<C extends zzb> {
    }

    public interface zzd<T extends IInterface> {
        T zzW(IBinder iBinder);

        void zza(int i, T t);

        String zzfK();

        String zzfL();
    }

    public interface zze<T extends zzd, O> {
        T zzn(O o);

        int zznA();
    }

    public static final class zzf<C extends zzd> {
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <C extends zzb> Api(String str, zza<C, O> zzaVar, zzc<C> zzcVar) {
        zzx.zzb(zzaVar, "Cannot construct an Api with a null ClientBuilder");
        zzx.zzb(zzcVar, "Cannot construct an Api with a null ClientKey");
        this.mName = str;
        this.zzaav = zzaVar;
        this.zzaaw = null;
        this.zzZM = zzcVar;
        this.zzaax = null;
    }

    public String getName() {
        return this.mName;
    }

    public zza<?, O> zznv() {
        zzx.zza(this.zzaav != null, "This API was constructed with a SimpleClientBuilder. Use getSimpleClientBuilder");
        return this.zzaav;
    }

    public zze<?, O> zznw() {
        zzx.zza(this.zzaaw != null, "This API was constructed with a ClientBuilder. Use getClientBuilder");
        return this.zzaaw;
    }

    public zzc<?> zznx() {
        zzx.zza(this.zzZM != null, "This API was constructed with a SimpleClientKey. Use getSimpleClientKey");
        return this.zzZM;
    }

    public boolean zzny() {
        return this.zzaax != null;
    }
}
