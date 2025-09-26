// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.security;

import android.content.Intent;
import android.os.AsyncTask;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.internal.zzx;
import android.content.Context;
import com.google.android.gms.common.GoogleApiAvailability;
import java.lang.reflect.Method;

public class ProviderInstaller
{
    public static final String PROVIDER_NAME = "GmsCore_OpenSSL";
    private static Method zzaUV;
    private static final GoogleApiAvailability zzacJ;
    private static final Object zzpy;
    
    static {
        zzacJ = GoogleApiAvailability.getInstance();
        zzpy = new Object();
    }
    
    public static void installIfNeeded(Context zzpy) throws GooglePlayServicesRepairableException, GooglePlayServicesNotAvailableException {
        zzx.zzb(zzpy, "Context must not be null");
        ProviderInstaller.zzacJ.zzab(zzpy);
        final Context remoteContext = GooglePlayServicesUtil.getRemoteContext(zzpy);
        if (remoteContext == null) {
            Log.e("ProviderInstaller", "Failed to get remote context");
            throw new GooglePlayServicesNotAvailableException(8);
        }
        zzpy = (Context)ProviderInstaller.zzpy;
        monitorenter(zzpy);
        try {
            try {
                if (ProviderInstaller.zzaUV == null) {
                    zzaM(remoteContext);
                }
                ProviderInstaller.zzaUV.invoke(null, remoteContext);
                monitorexit(zzpy);
            }
            finally {
                monitorexit(zzpy);
            }
        }
        catch (final Exception ex) {}
    }
    
    public static void installIfNeededAsync(final Context context, final ProviderInstallListener providerInstallListener) {
        zzx.zzb(context, "Context must not be null");
        zzx.zzb(providerInstallListener, "Listener must not be null");
        zzx.zzci("Must be called on the UI thread");
        new AsyncTask<Void, Void, Integer>(context, providerInstallListener) {
            final ProviderInstallListener zzaUW;
            final Context zzry;
            
            protected Integer zzc(final Void... array) {
                try {
                    ProviderInstaller.installIfNeeded(this.zzry);
                    return 0;
                }
                catch (final GooglePlayServicesNotAvailableException ex) {
                    return ex.errorCode;
                }
                catch (final GooglePlayServicesRepairableException ex2) {
                    return ex2.getConnectionStatusCode();
                }
            }
            
            protected void zze(final Integer n) {
                if (n == 0) {
                    this.zzaUW.onProviderInstalled();
                    return;
                }
                this.zzaUW.onProviderInstallFailed(n, ProviderInstaller.zzacJ.zza(this.zzry, n, "pi"));
            }
        }.execute((Object[])new Void[0]);
    }
    
    private static void zzaM(final Context context) throws ClassNotFoundException, NoSuchMethodException {
        ProviderInstaller.zzaUV = context.getClassLoader().loadClass("com.google.android.gms.common.security.ProviderInstallerImpl").getMethod("insertProvider", Context.class);
    }
    
    public interface ProviderInstallListener
    {
        void onProviderInstallFailed(final int p0, final Intent p1);
        
        void onProviderInstalled();
    }
}
