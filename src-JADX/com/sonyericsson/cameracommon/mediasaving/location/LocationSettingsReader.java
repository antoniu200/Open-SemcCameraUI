package com.sonyericsson.cameracommon.mediasaving.location;

import android.content.Context;
import android.location.LocationManager;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class LocationSettingsReader {
    public static final String TAG = "LocationSettingsReader";
    boolean mIsGpsLocationAllowed;
    boolean mIsNetworkLocationAllowed;

    public void readLocationSettings(Context context) {
        setIsGpsLocationAllowed(isLocationProviderAllowed(context, "gps"));
        setIsNetworkLocationAllowed(isLocationProviderAllowed(context, "network"));
    }

    public static boolean isLocationProviderAllowed(Context context, String str) {
        return ((LocationManager) context.getSystemService("location")).isProviderEnabled(str);
    }

    private void setIsGpsLocationAllowed(boolean z) {
        this.mIsGpsLocationAllowed = z;
    }

    private void setIsNetworkLocationAllowed(boolean z) {
        this.mIsNetworkLocationAllowed = z;
    }

    public boolean getIsGpsLocationAllowed() {
        return this.mIsGpsLocationAllowed;
    }

    public boolean getIsNetworkLocationAllowed() {
        return this.mIsNetworkLocationAllowed;
    }
}
