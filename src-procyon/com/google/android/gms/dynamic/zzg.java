// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.dynamic;

import android.os.IBinder;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.internal.zzx;
import android.content.Context;

public abstract class zzg<T>
{
    private final String zzapA;
    private T zzapB;
    
    protected zzg(final String zzapA) {
        this.zzapA = zzapA;
    }
    
    protected final T zzas(Context remoteContext) throws zza {
        if (this.zzapB == null) {
            zzx.zzw(remoteContext);
            remoteContext = GooglePlayServicesUtil.getRemoteContext(remoteContext);
            if (remoteContext == null) {
                throw new zza("Could not get remote context.");
            }
            final ClassLoader classLoader = remoteContext.getClassLoader();
            try {
                this.zzapB = this.zzd((IBinder)classLoader.loadClass(this.zzapA).newInstance());
            }
            catch (final IllegalAccessException ex) {
                throw new zza("Could not access creator.", ex);
            }
            catch (final InstantiationException ex2) {
                throw new zza("Could not instantiate creator.", ex2);
            }
            catch (final ClassNotFoundException ex3) {
                throw new zza("Could not load creator class.", ex3);
            }
        }
        return this.zzapB;
    }
    
    protected abstract T zzd(final IBinder p0);
    
    public static class zza extends Exception
    {
        public zza(final String message) {
            super(message);
        }
        
        public zza(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
}
