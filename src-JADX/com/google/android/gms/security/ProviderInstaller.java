package com.google.android.gms.security;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.internal.zzx;
import java.lang.reflect.Method;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ProviderInstaller {
    public static final String PROVIDER_NAME = "GmsCore_OpenSSL";
    private static Method zzaUV;
    private static final GoogleApiAvailability zzacJ = GoogleApiAvailability.getInstance();
    private static final Object zzpy = new Object();

    public interface ProviderInstallListener {
        void onProviderInstallFailed(int i, Intent intent);

        void onProviderInstalled();
    }

    public static void installIfNeeded(Context context) throws GooglePlayServicesRepairableException, GooglePlayServicesNotAvailableException {
        zzx.zzb(context, "Context must not be null");
        zzacJ.zzab(context);
        Context remoteContext = GooglePlayServicesUtil.getRemoteContext(context);
        if (remoteContext == null) {
            Log.e("ProviderInstaller", "Failed to get remote context");
            throw new GooglePlayServicesNotAvailableException(8);
        }
        synchronized (zzpy) {
            try {
                try {
                    if (zzaUV == null) {
                        zzaM(remoteContext);
                    }
                    zzaUV.invoke(null, remoteContext);
                } catch (Exception e) {
                    Log.e("ProviderInstaller", "Failed to install provider: " + e.getMessage());
                    throw new GooglePlayServicesNotAvailableException(8);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static void installIfNeededAsync(final Context context, final ProviderInstallListener providerInstallListener) {
        zzx.zzb(context, "Context must not be null");
        zzx.zzb(providerInstallListener, "Listener must not be null");
        zzx.zzci("Must be called on the UI thread");
        new AsyncTask<Void, Void, Integer>() { // from class: com.google.android.gms.security.ProviderInstaller.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            /* renamed from: zzc, reason: merged with bridge method [inline-methods] */
            public Integer doInBackground(Void... voidArr) {
                int connectionStatusCode;
                try {
                    ProviderInstaller.installIfNeeded(context);
                    connectionStatusCode = 0;
                } catch (GooglePlayServicesNotAvailableException e) {
                    connectionStatusCode = e.errorCode;
                } catch (GooglePlayServicesRepairableException e2) {
                    connectionStatusCode = e2.getConnectionStatusCode();
                }
                return Integer.valueOf(connectionStatusCode);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            /* renamed from: zze, reason: merged with bridge method [inline-methods] */
            public void onPostExecute(Integer num) {
                if (num.intValue() == 0) {
                    providerInstallListener.onProviderInstalled();
                } else {
                    providerInstallListener.onProviderInstallFailed(num.intValue(), ProviderInstaller.zzacJ.zza(context, num.intValue(), "pi"));
                }
            }
        }.execute(new Void[0]);
    }

    private static void zzaM(Context context) throws NoSuchMethodException, ClassNotFoundException {
        zzaUV = context.getClassLoader().loadClass("com.google.android.gms.common.security.ProviderInstallerImpl").getMethod("insertProvider", Context.class);
    }
}
