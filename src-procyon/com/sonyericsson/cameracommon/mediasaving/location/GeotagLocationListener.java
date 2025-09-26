// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.mediasaving.location;

import android.os.Bundle;
import com.sonyericsson.android.camera.util.CamLog;
import android.location.Location;
import android.location.LocationListener;

public class GeotagLocationListener implements LocationListener
{
    public static final String TAG = "GeotagLocationListener";
    private final GeotagManager mGeotagManager;
    private boolean mIsDisabled;
    private Location mLastLocation;
    public final String mProvider;
    private boolean mValid;
    
    public GeotagLocationListener(final GeotagManager mGeotagManager, final String mProvider) {
        this.mIsDisabled = false;
        this.mGeotagManager = mGeotagManager;
        this.mProvider = mProvider;
        this.mLastLocation = new Location(this.mProvider);
    }
    
    public Location current() {
        if (this.mValid) {
            if (CamLog.VERBOSE) {
                final String mProvider = this.mProvider;
                final StringBuilder sb = new StringBuilder();
                sb.append("current: Lat: ");
                sb.append(this.mLastLocation.getLatitude());
                sb.append(", Lon: ");
                sb.append(this.mLastLocation.getLongitude());
                sb.append(", Alt: ");
                sb.append(this.mLastLocation.getAltitude());
                CamLog.d(mProvider, sb.toString());
            }
            return this.mLastLocation;
        }
        if (CamLog.VERBOSE) {
            CamLog.d(this.mProvider, "current: no location obtained.");
        }
        return null;
    }
    
    public boolean isDisabled() {
        return this.mIsDisabled;
    }
    
    public void onLocationChanged(final Location location) {
        if (CamLog.VERBOSE) {
            final String mProvider = this.mProvider;
            final StringBuilder sb = new StringBuilder();
            sb.append("onLocationChanged: Lat: ");
            sb.append(location.getLatitude());
            sb.append(", Lon: ");
            sb.append(location.getLongitude());
            sb.append(", Alt: ");
            sb.append(location.getAltitude());
            CamLog.d(mProvider, sb.toString());
        }
        this.mIsDisabled = false;
        if (location.getLatitude() == 0.0 && location.getLongitude() == 0.0) {
            return;
        }
        this.mLastLocation.set(location);
        this.mValid = true;
        this.mGeotagManager.notifyStatus();
    }
    
    public void onProviderDisabled(final String str) {
        if (CamLog.VERBOSE) {
            final String mProvider = this.mProvider;
            final StringBuilder sb = new StringBuilder();
            sb.append("onProviderDisabled: ");
            sb.append(str);
            CamLog.d(mProvider, sb.toString());
        }
        this.mValid = false;
        this.mIsDisabled = true;
        this.mGeotagManager.notifyStatus();
    }
    
    public void onProviderEnabled(final String str) {
        if (CamLog.VERBOSE) {
            final String mProvider = this.mProvider;
            final StringBuilder sb = new StringBuilder();
            sb.append("onProviderEnabled: ");
            sb.append(str);
            CamLog.d(mProvider, sb.toString());
        }
        this.mIsDisabled = false;
    }
    
    public void onStatusChanged(String mProvider, final int i, final Bundle bundle) {
        if (CamLog.VERBOSE) {
            mProvider = this.mProvider;
            final StringBuilder sb = new StringBuilder();
            sb.append("onStatusChanged: ");
            sb.append(i);
            CamLog.d(mProvider, sb.toString());
        }
        this.mIsDisabled = false;
        if (i == 0) {
            if (CamLog.VERBOSE) {
                CamLog.d(this.mProvider, "OUT_OF_SERIVICE");
            }
            if (this.mValid) {
                return;
            }
            this.mGeotagManager.notifyStatus();
        }
    }
    
    public void reset() {
        this.mValid = false;
        this.mIsDisabled = false;
    }
}
