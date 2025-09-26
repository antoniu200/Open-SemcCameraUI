// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.mediasaving.location;

import android.location.LocationListener;
import com.sonyericsson.android.camera.view.messagedialog.DialogId;
import com.sonyericsson.android.camera.view.ViewFinderImpl;
import android.location.Location;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.setting.UserSettings;
import com.sonyericsson.cameracommon.utility.PermissionsUtil;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.configuration.parameters.Geotag;
import com.sonyericsson.android.camera.CameraActivity;
import android.location.LocationManager;
import android.content.Context;

public class GeotagManager
{
    private static final String[] REQUEST_LOCATION_PERMISSION;
    public static final String TAG = "GeotagManager";
    private boolean mAcquiring;
    private final Context mContext;
    private boolean mIsGeotagPermissionGranted;
    private LocationAcquiredListener mLocationAcquiredListener;
    private GeotagLocationListener mLocationListenerGps;
    private GeotagLocationListener mLocationListenerNetwork;
    private LocationManager mLocationManager;
    
    static {
        REQUEST_LOCATION_PERMISSION = new String[] { "android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION" };
    }
    
    public GeotagManager(final Context mContext) {
        this.mContext = mContext;
    }
    
    private boolean checkLocationService(final Geotag obj, final CameraActivity cameraActivity) {
        final boolean verbose = CamLog.VERBOSE;
        boolean b = false;
        if (verbose) {
            final StringBuilder sb = new StringBuilder();
            sb.append("checkLocationService(): ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        this.updateLocation(obj);
        cameraActivity.getStoredSettings().getUserSettings().set(obj);
        if (obj == Geotag.ON && !isLocationServiceAvailable(cameraActivity)) {
            cameraActivity.getStoredSettings().getUserSettings().set(Geotag.ON);
        }
        else {
            b = true;
        }
        return b;
    }
    
    public static boolean isGeoTagEnabled(final Geotag obj, final Context context) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("isGeoTagEnabled: ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        if (PermissionsUtil.arePermissionsGranted(context, GeotagManager.REQUEST_LOCATION_PERMISSION) && obj == Geotag.ON) {
            final boolean locationProviderAllowed = LocationSettingsReader.isLocationProviderAllowed(context, "gps");
            final boolean locationProviderAllowed2 = LocationSettingsReader.isLocationProviderAllowed(context, "network");
            if (locationProviderAllowed || locationProviderAllowed2) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isGeoTagEnabled(final UserSettings userSettings, final Context context) {
        return isGeoTagEnabled((Geotag)userSettings.get(UserSettingKey.GEO_TAG), context);
    }
    
    private static boolean isLocationServiceAvailable(final CameraActivity cameraActivity) {
        cameraActivity.readLocationSettings();
        return cameraActivity.isGpsLocationAllowed() || cameraActivity.isNetworkLocationAllowed();
    }
    
    public void assignResource() {
        if (this.mLocationListenerGps == null) {
            this.mLocationListenerGps = new GeotagLocationListener(this, "gps");
        }
        if (this.mLocationListenerNetwork == null) {
            this.mLocationListenerNetwork = new GeotagLocationListener(this, "network");
        }
        if (this.mLocationManager == null) {
            this.mLocationManager = (LocationManager)this.mContext.getSystemService("location");
        }
    }
    
    public Location getCurrentLocation() {
        Location current;
        if (this.mLocationListenerNetwork != null) {
            current = this.mLocationListenerNetwork.current();
        }
        else {
            current = null;
        }
        Location current2;
        if (this.mLocationListenerGps != null) {
            current2 = this.mLocationListenerGps.current();
        }
        else {
            current2 = null;
        }
        if (current2 != null) {
            return current2;
        }
        if (current != null) {
            return current;
        }
        return null;
    }
    
    public boolean initGeotag(final CameraActivity cameraActivity, final boolean b) {
        if (CamLog.VERBOSE) {
            CamLog.d("start initGeotag()");
        }
        final Geotag geotag = (Geotag)cameraActivity.getStoredSettings().getUserSettings().get(UserSettingKey.GEO_TAG);
        final boolean verbose = CamLog.VERBOSE;
        final boolean b2 = false;
        if (verbose) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Geotag=");
            sb.append(geotag);
            CamLog.d(sb.toString());
        }
        final boolean permissionsGranted = PermissionsUtil.arePermissionsGranted((Context)cameraActivity, GeotagManager.REQUEST_LOCATION_PERMISSION);
        if (CamLog.VERBOSE) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("locationpermission=");
            sb2.append(permissionsGranted);
            CamLog.d(sb2.toString());
        }
        boolean checkLocationService;
        if (b && permissionsGranted) {
            checkLocationService = this.checkLocationService(geotag, cameraActivity);
        }
        else {
            this.updateLocation(Geotag.OFF);
            checkLocationService = true;
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("end initGeotag():");
            sb3.append(geotag);
            CamLog.d(sb3.toString());
        }
        boolean b3 = b2;
        if (checkLocationService) {
            b3 = b2;
            if (b) {
                b3 = true;
            }
        }
        return b3;
    }
    
    public boolean isAcquiring() {
        return this.mAcquiring;
    }
    
    public boolean isDisabled() {
        final GeotagLocationListener mLocationListenerGps = this.mLocationListenerGps;
        final boolean b = false;
        final boolean b2 = mLocationListenerGps != null && this.mLocationListenerGps.isDisabled();
        final boolean b3 = this.mLocationListenerNetwork != null && this.mLocationListenerNetwork.isDisabled();
        boolean b4 = b;
        if (b2) {
            b4 = b;
            if (b3) {
                b4 = true;
            }
        }
        return b4;
    }
    
    public boolean isGeotagPermissionGranted() {
        return this.mIsGeotagPermissionGranted;
    }
    
    public boolean isGpsAcquired() {
        final boolean b = this.mLocationListenerGps != null && this.mLocationListenerGps.current() != null;
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("checkLcsAvailable(Gps): ");
            sb.append(b);
            CamLog.d(sb.toString());
        }
        return b;
    }
    
    public boolean isNetworkAcquired() {
        final boolean b = this.mLocationListenerNetwork != null && this.mLocationListenerNetwork.current() != null;
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("checkLcsAvailable(NW): ");
            sb.append(b);
            CamLog.d(sb.toString());
        }
        return b;
    }
    
    public void notifyStatus() {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("notifyStatus: listener: ");
            sb.append(this.mLocationAcquiredListener);
            CamLog.d(sb.toString());
        }
        if (this.isDisabled()) {
            this.updateLocation(Geotag.OFF);
            if (this.mLocationAcquiredListener != null) {
                this.mLocationAcquiredListener.onDisabled();
            }
            return;
        }
        final boolean gpsAcquired = this.isGpsAcquired();
        final boolean networkAcquired = this.isNetworkAcquired();
        if (!gpsAcquired && !networkAcquired) {
            if (this.mLocationAcquiredListener != null) {
                if (CamLog.VERBOSE) {
                    CamLog.d("notifyStatus: onLost.");
                }
                this.mLocationAcquiredListener.onLost();
            }
        }
        else if (this.mLocationAcquiredListener != null) {
            if (CamLog.VERBOSE) {
                CamLog.d("notifyStatus: onAcquired.");
            }
            this.mLocationAcquiredListener.onAcquired(gpsAcquired, networkAcquired);
        }
    }
    
    public void release() {
        this.setLocationAcquiredListener(null);
        this.mLocationListenerGps = null;
        this.mLocationListenerNetwork = null;
    }
    
    public void releaseResource() {
        this.stopReceivingLocationUpdates();
        this.mLocationManager = null;
    }
    
    public boolean setGeotag(final Geotag obj, final CameraActivity cameraActivity, final ViewFinderImpl viewFinderImpl) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setGeotag(): ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        cameraActivity.getStoredSettings().getUserSettings().set(obj);
        return (obj == Geotag.ON && cameraActivity.checkAndRequestSelfPermissions(13, GeotagManager.REQUEST_LOCATION_PERMISSION, (CameraActivity.PermissionCheckCallback)new CameraActivity.PermissionCheckCallback(this, cameraActivity, viewFinderImpl) {
            final GeotagManager this$0;
            final CameraActivity val$activity;
            final ViewFinderImpl val$viewFinder;
            
            @Override
            public boolean onPermissionChecked(final String[] array) {
                if (PermissionsUtil.arePermissionsGranted((Context)this.val$activity, array) && !isLocationServiceAvailable(this.val$activity) && this.val$viewFinder != null) {
                    this.val$viewFinder.showMessageDialog(DialogId.LOCATION_SERVICE_DISABLE_ON_CONTEXTUAL_SETTINGS, new Object[0]);
                }
                return true;
            }
        })) || this.checkLocationService(obj, cameraActivity);
    }
    
    public void setIsGeotagPermissionGranted(final boolean mIsGeotagPermissionGranted) {
        this.mIsGeotagPermissionGranted = mIsGeotagPermissionGranted;
    }
    
    public void setLocationAcquiredListener(final LocationAcquiredListener mLocationAcquiredListener) {
        this.mLocationAcquiredListener = mLocationAcquiredListener;
    }
    
    public void startLocationUpdates(final boolean b, final boolean b2) {
        synchronized (this) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("startLocationUpdates: ");
                sb.append(b || b2);
                CamLog.d(sb.toString());
            }
            while (true) {
                if (b) {
                    try {
                        this.mLocationManager.requestLocationUpdates("gps", 60000L, 0.0f, (LocationListener)this.mLocationListenerGps);
                        this.mLocationListenerGps.reset();
                        this.mAcquiring = true;
                        if (CamLog.VERBOSE) {
                            CamLog.d("startLocationUpdates(GPS) started.");
                        }
                        break Label_0132;
                    }
                    catch (final IllegalArgumentException ex) {
                        if (CamLog.VERBOSE) {
                            CamLog.d("provider does not exist.", ex);
                        }
                        this.mAcquiring = false;
                        return;
                    Block_12:
                        while (true) {
                            this.mLocationManager.requestLocationUpdates("network", 60000L, 0.0f, (LocationListener)this.mLocationListenerNetwork);
                            this.mLocationListenerNetwork.reset();
                            this.mAcquiring = true;
                            iftrue(Label_0222:)(!CamLog.VERBOSE);
                            break Block_12;
                            iftrue(Label_0222:)(!b2);
                            continue;
                        }
                        CamLog.d("startLocationUpdates(NW) started.");
                        return;
                    }
                    catch (final SecurityException ex2) {}
                    final SecurityException ex3;
                    CamLog.d("provider can't access.", ex3);
                    this.mAcquiring = false;
                    Label_0222: {
                        return;
                    }
                }
                continue;
            }
        }
    }
    
    public void stopReceivingLocationUpdates() {
        synchronized (this) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("stopReceivingLocationUpdates: acquiring: ");
                sb.append(this.mAcquiring);
                CamLog.d(sb.toString());
            }
            if (this.mAcquiring) {
                if (this.mLocationManager != null) {
                    this.mLocationListenerGps.reset();
                    this.mLocationListenerNetwork.reset();
                    this.mLocationManager.removeUpdates((LocationListener)this.mLocationListenerGps);
                    this.mLocationManager.removeUpdates((LocationListener)this.mLocationListenerNetwork);
                }
                this.mAcquiring = false;
                if (CamLog.VERBOSE) {
                    CamLog.d("stopReceivingLocationUpdates: stopped.");
                }
            }
        }
    }
    
    public void updateLocation(final Geotag geotag) {
        this.assignResource();
        this.stopReceivingLocationUpdates();
        if (geotag == Geotag.ON) {
            if (this.mLocationAcquiredListener != null) {
                this.mLocationAcquiredListener.onLost();
            }
            this.startLocationUpdates(LocationSettingsReader.isLocationProviderAllowed(this.mContext, "gps"), LocationSettingsReader.isLocationProviderAllowed(this.mContext, "network"));
        }
    }
}
