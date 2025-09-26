// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.cameracommon.vanilla.wearablebridge.common;

import com.google.android.gms.common.GooglePlayServicesUtil;
import android.content.Context;

public class WearableUtil
{
    public static boolean isGooglePlayServiceAvailable(final Context context) {
        return isGooglePlayServiceAvailable(context, -1);
    }
    
    public static boolean isGooglePlayServiceAvailable(final Context context, final int n) {
        return GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == 0 && GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE >= n;
    }
}
