package com.google.android.gms.internal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import com.google.android.gms.common.GoogleApiAvailability;
import com.sonyericsson.android.camera.view.modeselector.CameraCommonProviderConstants;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
abstract class zzll extends BroadcastReceiver {
    protected Context mContext;

    zzll() {
    }

    public static <T extends zzll> T zza(Context context, T t) {
        return (T) zza(context, t, GoogleApiAvailability.getInstance());
    }

    public static <T extends zzll> T zza(Context context, T t, GoogleApiAvailability googleApiAvailability) {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
        intentFilter.addDataScheme(CameraCommonProviderConstants.CapturingModeColumns.PACKAGE);
        context.registerReceiver(t, intentFilter);
        t.mContext = context;
        if (googleApiAvailability.zzj(context, "com.google.android.gms")) {
            return t;
        }
        t.zzoi();
        t.unregister();
        return null;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Uri data = intent.getData();
        if ("com.google.android.gms".equals(data != null ? data.getSchemeSpecificPart() : null)) {
            zzoi();
            unregister();
        }
    }

    public synchronized void unregister() {
        if (this.mContext != null) {
            this.mContext.unregisterReceiver(this);
        }
        this.mContext = null;
    }

    protected abstract void zzoi();
}
