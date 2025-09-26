// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.api;

import android.os.IInterface;
import android.os.IBinder;
import java.util.Set;
import com.google.android.gms.common.internal.zzp;
import java.io.PrintWriter;
import java.io.FileDescriptor;
import java.util.Collections;
import java.util.List;
import com.google.android.gms.common.internal.zzf;
import android.os.Looper;
import android.content.Context;
import com.google.android.gms.common.internal.zzx;

public final class Api<O extends ApiOptions>
{
    private final String mName;
    private final zzc<?> zzZM;
    private final zza<?, O> zzaav;
    private final zze<?, O> zzaaw;
    private final zzf<?> zzaax;
    
    public <C extends zzb> Api(final String mName, final zza<C, O> zzaav, final zzc<C> zzZM) {
        zzx.zzb(zzaav, "Cannot construct an Api with a null ClientBuilder");
        zzx.zzb(zzZM, "Cannot construct an Api with a null ClientKey");
        this.mName = mName;
        this.zzaav = zzaav;
        this.zzaaw = null;
        this.zzZM = zzZM;
        this.zzaax = null;
    }
    
    public String getName() {
        return this.mName;
    }
    
    public zza<?, O> zznv() {
        zzx.zza(this.zzaav != null, (Object)"This API was constructed with a SimpleClientBuilder. Use getSimpleClientBuilder");
        return this.zzaav;
    }
    
    public zze<?, O> zznw() {
        zzx.zza(this.zzaaw != null, (Object)"This API was constructed with a ClientBuilder. Use getClientBuilder");
        return this.zzaaw;
    }
    
    public zzc<?> zznx() {
        zzx.zza(this.zzZM != null, (Object)"This API was constructed with a SimpleClientKey. Use getSimpleClientKey");
        return this.zzZM;
    }
    
    public boolean zzny() {
        return this.zzaax != null;
    }
    
    public interface ApiOptions
    {
        public interface HasOptions extends ApiOptions
        {
        }
        
        public static final class NoOptions implements NotRequiredOptions
        {
            private NoOptions() {
            }
        }
        
        public interface NotRequiredOptions extends ApiOptions
        {
        }
        
        public interface Optional extends HasOptions, NotRequiredOptions
        {
        }
    }
    
    public abstract static class zza<T extends zzb, O>
    {
        public int getPriority() {
            return Integer.MAX_VALUE;
        }
        
        public abstract T zza(final Context p0, final Looper p1, final com.google.android.gms.common.internal.zzf p2, final O p3, final GoogleApiClient.ConnectionCallbacks p4, final GoogleApiClient.OnConnectionFailedListener p5);
        
        public List<Scope> zzm(final O o) {
            return Collections.emptyList();
        }
    }
    
    public interface zzb
    {
        void disconnect();
        
        void dump(final String p0, final FileDescriptor p1, final PrintWriter p2, final String[] p3);
        
        boolean isConnected();
        
        void zza(final GoogleApiClient.zza p0);
        
        void zza(final zzp p0);
        
        void zza(final zzp p0, final Set<Scope> p1);
        
        boolean zzlN();
        
        IBinder zznz();
    }
    
    public static final class zzc<C extends zzb>
    {
    }
    
    public interface zzd<T extends IInterface>
    {
        T zzW(final IBinder p0);
        
        void zza(final int p0, final T p1);
        
        String zzfK();
        
        String zzfL();
    }
    
    public interface zze<T extends zzd, O>
    {
        T zzn(final O p0);
        
        int zznA();
    }
    
    public static final class zzf<C extends zzd>
    {
    }
}
