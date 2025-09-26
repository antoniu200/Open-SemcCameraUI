// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.mediasaving.location;

import android.location.LocationManager;
import android.content.Context;

public class LocationSettingsReader
{
    public static final String TAG = "LocationSettingsReader";
    boolean mIsGpsLocationAllowed;
    boolean mIsNetworkLocationAllowed;
    
    public static boolean isLocationProviderAllowed(final Context context, final String s) {
        return ((LocationManager)context.getSystemService("location")).isProviderEnabled(s);
    }
    
    private void setIsGpsLocationAllowed(final boolean mIsGpsLocationAllowed) {
        this.mIsGpsLocationAllowed = mIsGpsLocationAllowed;
    }
    
    private void setIsNetworkLocationAllowed(final boolean mIsNetworkLocationAllowed) {
        this.mIsNetworkLocationAllowed = mIsNetworkLocationAllowed;
    }
    
    public boolean getIsGpsLocationAllowed() {
        return this.mIsGpsLocationAllowed;
    }
    
    public boolean getIsNetworkLocationAllowed() {
        return this.mIsNetworkLocationAllowed;
    }
    
    public void readLocationSettings(final Context context) {
        this.setIsGpsLocationAllowed(isLocationProviderAllowed(context, "gps"));
        this.setIsNetworkLocationAllowed(isLocationProviderAllowed(context, "network"));
    }
}
