package com.sonyericsson.cameracommon.mediasaving.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.parameters.Geotag;
import com.sonyericsson.android.camera.setting.UserSettings;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.view.ViewFinderImpl;
import com.sonyericsson.android.camera.view.messagedialog.DialogId;
import com.sonyericsson.cameracommon.utility.PermissionsUtil;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class GeotagManager {
    private static final String[] REQUEST_LOCATION_PERMISSION = {"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"};
    public static final String TAG = "GeotagManager";
    private boolean mAcquiring;
    private final Context mContext;
    private boolean mIsGeotagPermissionGranted;
    private LocationAcquiredListener mLocationAcquiredListener;
    private GeotagLocationListener mLocationListenerGps;
    private GeotagLocationListener mLocationListenerNetwork;
    private LocationManager mLocationManager;

    public void setIsGeotagPermissionGranted(boolean z) {
        this.mIsGeotagPermissionGranted = z;
    }

    public boolean isGeotagPermissionGranted() {
        return this.mIsGeotagPermissionGranted;
    }

    public static boolean isGeoTagEnabled(UserSettings userSettings, Context context) {
        return isGeoTagEnabled((Geotag) userSettings.get(UserSettingKey.GEO_TAG), context);
    }

    public static boolean isGeoTagEnabled(Geotag geotag, Context context) {
        if (CamLog.VERBOSE) {
            CamLog.d("isGeoTagEnabled: " + geotag);
        }
        if (PermissionsUtil.arePermissionsGranted(context, REQUEST_LOCATION_PERMISSION) && geotag == Geotag.ON) {
            boolean zIsLocationProviderAllowed = LocationSettingsReader.isLocationProviderAllowed(context, "gps");
            boolean zIsLocationProviderAllowed2 = LocationSettingsReader.isLocationProviderAllowed(context, "network");
            if (zIsLocationProviderAllowed || zIsLocationProviderAllowed2) {
                return true;
            }
        }
        return false;
    }

    public GeotagManager(Context context) {
        this.mContext = context;
    }

    public void assignResource() {
        if (this.mLocationListenerGps == null) {
            this.mLocationListenerGps = new GeotagLocationListener(this, "gps");
        }
        if (this.mLocationListenerNetwork == null) {
            this.mLocationListenerNetwork = new GeotagLocationListener(this, "network");
        }
        if (this.mLocationManager == null) {
            this.mLocationManager = (LocationManager) this.mContext.getSystemService("location");
        }
    }

    public void releaseResource() {
        stopReceivingLocationUpdates();
        this.mLocationManager = null;
    }

    public void release() {
        setLocationAcquiredListener(null);
        this.mLocationListenerGps = null;
        this.mLocationListenerNetwork = null;
    }

    public void setLocationAcquiredListener(LocationAcquiredListener locationAcquiredListener) {
        this.mLocationAcquiredListener = locationAcquiredListener;
    }

    public boolean isAcquiring() {
        return this.mAcquiring;
    }

    public synchronized void startLocationUpdates(boolean z, boolean z2) {
        if (CamLog.VERBOSE) {
            String[] strArr = new String[1];
            StringBuilder sb = new StringBuilder();
            sb.append("startLocationUpdates: ");
            sb.append(z || z2);
            strArr[0] = sb.toString();
            CamLog.d(strArr);
        }
        if (z) {
            try {
                try {
                    this.mLocationManager.requestLocationUpdates("gps", 60000L, 0.0f, this.mLocationListenerGps);
                    this.mLocationListenerGps.reset();
                    this.mAcquiring = true;
                    if (CamLog.VERBOSE) {
                        CamLog.d("startLocationUpdates(GPS) started.");
                    }
                } catch (SecurityException e) {
                    CamLog.d("provider can't access.", e);
                    this.mAcquiring = false;
                }
            } catch (IllegalArgumentException e2) {
                if (CamLog.VERBOSE) {
                    CamLog.d("provider does not exist.", e2);
                }
                this.mAcquiring = false;
            }
        }
        if (z2) {
            this.mLocationManager.requestLocationUpdates("network", 60000L, 0.0f, this.mLocationListenerNetwork);
            this.mLocationListenerNetwork.reset();
            this.mAcquiring = true;
            if (CamLog.VERBOSE) {
                CamLog.d("startLocationUpdates(NW) started.");
            }
        }
    }

    public synchronized void stopReceivingLocationUpdates() {
        if (CamLog.VERBOSE) {
            CamLog.d("stopReceivingLocationUpdates: acquiring: " + this.mAcquiring);
        }
        if (this.mAcquiring) {
            if (this.mLocationManager != null) {
                this.mLocationListenerGps.reset();
                this.mLocationListenerNetwork.reset();
                this.mLocationManager.removeUpdates(this.mLocationListenerGps);
                this.mLocationManager.removeUpdates(this.mLocationListenerNetwork);
            }
            this.mAcquiring = false;
            if (CamLog.VERBOSE) {
                CamLog.d("stopReceivingLocationUpdates: stopped.");
            }
        }
    }

    public Location getCurrentLocation() {
        Location locationCurrent = this.mLocationListenerNetwork != null ? this.mLocationListenerNetwork.current() : null;
        Location locationCurrent2 = this.mLocationListenerGps != null ? this.mLocationListenerGps.current() : null;
        if (locationCurrent2 != null) {
            return locationCurrent2;
        }
        if (locationCurrent != null) {
            return locationCurrent;
        }
        return null;
    }

    public void notifyStatus() {
        if (CamLog.VERBOSE) {
            CamLog.d("notifyStatus: listener: " + this.mLocationAcquiredListener);
        }
        if (isDisabled()) {
            updateLocation(Geotag.OFF);
            if (this.mLocationAcquiredListener != null) {
                this.mLocationAcquiredListener.onDisabled();
                return;
            }
            return;
        }
        boolean zIsGpsAcquired = isGpsAcquired();
        boolean zIsNetworkAcquired = isNetworkAcquired();
        if (zIsGpsAcquired || zIsNetworkAcquired) {
            if (this.mLocationAcquiredListener != null) {
                if (CamLog.VERBOSE) {
                    CamLog.d("notifyStatus: onAcquired.");
                }
                this.mLocationAcquiredListener.onAcquired(zIsGpsAcquired, zIsNetworkAcquired);
                return;
            }
            return;
        }
        if (this.mLocationAcquiredListener != null) {
            if (CamLog.VERBOSE) {
                CamLog.d("notifyStatus: onLost.");
            }
            this.mLocationAcquiredListener.onLost();
        }
    }

    public boolean isGpsAcquired() {
        boolean z = (this.mLocationListenerGps == null || this.mLocationListenerGps.current() == null) ? false : true;
        if (CamLog.VERBOSE) {
            CamLog.d("checkLcsAvailable(Gps): " + z);
        }
        return z;
    }

    public boolean isNetworkAcquired() {
        boolean z = (this.mLocationListenerNetwork == null || this.mLocationListenerNetwork.current() == null) ? false : true;
        if (CamLog.VERBOSE) {
            CamLog.d("checkLcsAvailable(NW): " + z);
        }
        return z;
    }

    public void updateLocation(Geotag geotag) {
        assignResource();
        stopReceivingLocationUpdates();
        if (geotag == Geotag.ON) {
            if (this.mLocationAcquiredListener != null) {
                this.mLocationAcquiredListener.onLost();
            }
            startLocationUpdates(LocationSettingsReader.isLocationProviderAllowed(this.mContext, "gps"), LocationSettingsReader.isLocationProviderAllowed(this.mContext, "network"));
        }
    }

    private boolean checkLocationService(Geotag geotag, CameraActivity cameraActivity) {
        if (CamLog.VERBOSE) {
            CamLog.d("checkLocationService(): " + geotag);
        }
        updateLocation(geotag);
        cameraActivity.getStoredSettings().getUserSettings().set(geotag);
        if (geotag != Geotag.ON || isLocationServiceAvailable(cameraActivity)) {
            return true;
        }
        cameraActivity.getStoredSettings().getUserSettings().set(Geotag.ON);
        return false;
    }

    public boolean initGeotag(CameraActivity cameraActivity, boolean z) {
        boolean zCheckLocationService;
        if (CamLog.VERBOSE) {
            CamLog.d("start initGeotag()");
        }
        Geotag geotag = (Geotag) cameraActivity.getStoredSettings().getUserSettings().get(UserSettingKey.GEO_TAG);
        if (CamLog.VERBOSE) {
            CamLog.d("Geotag=" + geotag);
        }
        boolean zArePermissionsGranted = PermissionsUtil.arePermissionsGranted(cameraActivity, REQUEST_LOCATION_PERMISSION);
        if (CamLog.VERBOSE) {
            CamLog.d("locationpermission=" + zArePermissionsGranted);
        }
        if (z && zArePermissionsGranted) {
            zCheckLocationService = checkLocationService(geotag, cameraActivity);
        } else {
            updateLocation(Geotag.OFF);
            zCheckLocationService = true;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("end initGeotag():" + geotag);
        }
        return zCheckLocationService && z;
    }

    public boolean setGeotag(Geotag geotag, final CameraActivity cameraActivity, final ViewFinderImpl viewFinderImpl) {
        if (CamLog.VERBOSE) {
            CamLog.d("setGeotag(): " + geotag);
        }
        cameraActivity.getStoredSettings().getUserSettings().set(geotag);
        if (geotag == Geotag.ON && cameraActivity.checkAndRequestSelfPermissions(13, REQUEST_LOCATION_PERMISSION, new CameraActivity.PermissionCheckCallback() { // from class: com.sonyericsson.cameracommon.mediasaving.location.GeotagManager.1
            @Override // com.sonyericsson.android.camera.CameraActivity.PermissionCheckCallback
            public boolean onPermissionChecked(String[] strArr) {
                if (!PermissionsUtil.arePermissionsGranted(cameraActivity, strArr) || GeotagManager.isLocationServiceAvailable(cameraActivity) || viewFinderImpl == null) {
                    return true;
                }
                viewFinderImpl.showMessageDialog(DialogId.LOCATION_SERVICE_DISABLE_ON_CONTEXTUAL_SETTINGS, new Object[0]);
                return true;
            }
        })) {
            return true;
        }
        return checkLocationService(geotag, cameraActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isLocationServiceAvailable(CameraActivity cameraActivity) {
        cameraActivity.readLocationSettings();
        return cameraActivity.isGpsLocationAllowed() || cameraActivity.isNetworkLocationAllowed();
    }

    public boolean isDisabled() {
        return (this.mLocationListenerGps != null ? this.mLocationListenerGps.isDisabled() : false) && (this.mLocationListenerNetwork != null ? this.mLocationListenerNetwork.isDisabled() : false);
    }
}
