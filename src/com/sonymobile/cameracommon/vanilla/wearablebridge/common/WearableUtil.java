package com.sonymobile.cameracommon.vanilla.wearablebridge.common;

import android.content.Context;
import com.google.android.gms.common.GooglePlayServicesUtil;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class WearableUtil {
    public static boolean isGooglePlayServiceAvailable(Context context, int i) {
        return GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == 0 && GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE >= i;
    }

    public static boolean isGooglePlayServiceAvailable(Context context) {
        return isGooglePlayServiceAvailable(context, -1);
    }
}
