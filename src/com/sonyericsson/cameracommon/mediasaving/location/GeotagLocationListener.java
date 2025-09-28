package com.sonyericsson.cameracommon.mediasaving.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import com.sonyericsson.android.camera.util.CamLog;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class GeotagLocationListener implements LocationListener {
    public static final String TAG = "GeotagLocationListener";
    private final GeotagManager mGeotagManager;
    private boolean mIsDisabled = false;
    private Location mLastLocation;
    public final String mProvider;
    private boolean mValid;

    public GeotagLocationListener(GeotagManager geotagManager, String str) {
        this.mGeotagManager = geotagManager;
        this.mProvider = str;
        this.mLastLocation = new Location(this.mProvider);
    }

    @Override // android.location.LocationListener
    public void onLocationChanged(Location location) {
        if (CamLog.VERBOSE) {
            CamLog.d(this.mProvider, "onLocationChanged: Lat: " + location.getLatitude() + ", Lon: " + location.getLongitude() + ", Alt: " + location.getAltitude());
        }
        this.mIsDisabled = false;
        if (location.getLatitude() == 0.0d && location.getLongitude() == 0.0d) {
            return;
        }
        this.mLastLocation.set(location);
        this.mValid = true;
        this.mGeotagManager.notifyStatus();
    }

    @Override // android.location.LocationListener
    public void onProviderEnabled(String str) {
        if (CamLog.VERBOSE) {
            CamLog.d(this.mProvider, "onProviderEnabled: " + str);
        }
        this.mIsDisabled = false;
    }

    @Override // android.location.LocationListener
    public void onProviderDisabled(String str) {
        if (CamLog.VERBOSE) {
            CamLog.d(this.mProvider, "onProviderDisabled: " + str);
        }
        this.mValid = false;
        this.mIsDisabled = true;
        this.mGeotagManager.notifyStatus();
    }

    @Override // android.location.LocationListener
    public void onStatusChanged(String str, int i, Bundle bundle) {
        if (CamLog.VERBOSE) {
            CamLog.d(this.mProvider, "onStatusChanged: " + i);
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

    public Location current() {
        if (this.mValid) {
            if (CamLog.VERBOSE) {
                CamLog.d(this.mProvider, "current: Lat: " + this.mLastLocation.getLatitude() + ", Lon: " + this.mLastLocation.getLongitude() + ", Alt: " + this.mLastLocation.getAltitude());
            }
            return this.mLastLocation;
        }
        if (!CamLog.VERBOSE) {
            return null;
        }
        CamLog.d(this.mProvider, "current: no location obtained.");
        return null;
    }

    public void reset() {
        this.mValid = false;
        this.mIsDisabled = false;
    }

    public boolean isDisabled() {
        return this.mIsDisabled;
    }
}
