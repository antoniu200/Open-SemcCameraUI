// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.device;

import android.location.Location;
import android.os.SystemClock;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingConstants;
import com.sonymobile.imageprocessor.bypasscamera2.BypassCamera;
import com.sonyericsson.cameracommon.storage.RequestFactory;

public class BypassCameraSnapshotInfoFactory
{
    private static final int EXIF_THUMBNAIL_HEIGHT = 120;
    private static final int EXIF_THUMBNAIL_QUALITY = 80;
    private static final int EXIF_THUMBNAIL_WIDTH = 160;
    private static final int QUALITY = 2;
    private static final boolean QUALITY_AUTO_CONTROL_ENABLED = true;
    
    public static BypassCamera.SnapshotInfo create(final RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder, final int n) {
        return new BypassCamera.SnapshotInfo(true, createExifGpsInfo(photoSavingRequestBuilder), createExifOrientationInfo(photoSavingRequestBuilder), createExifThumbnailInfo(), MediaSavingConstants.JpegQuality.getPlatformQualityFromCameraProfile(2), true, n);
    }
    
    public static BypassCamera.SnapshotInfo.ExifGpsInfo createExifGpsInfo(final RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder) {
        String provider = "";
        final Location location = photoSavingRequestBuilder.mCommonStatus.location;
        double altitude = 0.0;
        boolean b;
        if (location != null) {
            final double latitude = photoSavingRequestBuilder.mCommonStatus.location.getLatitude();
            final double longitude = photoSavingRequestBuilder.mCommonStatus.location.getLongitude();
            b = (latitude != 0.0 || longitude != 0.0);
            if (b) {
                if (photoSavingRequestBuilder.mCommonStatus.location.hasAltitude()) {
                    altitude = photoSavingRequestBuilder.mCommonStatus.location.getAltitude();
                }
                boolean b2;
                if (photoSavingRequestBuilder.mCommonStatus.location.getProvider() != null) {
                    provider = photoSavingRequestBuilder.mCommonStatus.location.getProvider();
                    b2 = true;
                }
                else {
                    b2 = false;
                }
                int n;
                boolean b3;
                if (photoSavingRequestBuilder.mCommonStatus.location.getTime() != 0L) {
                    n = (int)((photoSavingRequestBuilder.mCommonStatus.location.getTime() + (SystemClock.elapsedRealtimeNanos() - photoSavingRequestBuilder.mCommonStatus.location.getElapsedRealtimeNanos()) / 1000000L) / 1000L);
                    b3 = true;
                }
                else {
                    b3 = false;
                    n = 0;
                }
                return new BypassCamera.SnapshotInfo.ExifGpsInfo(b, latitude, longitude, altitude, b2, provider, b3, n);
            }
        }
        else {
            b = false;
        }
        provider = "";
        boolean b2 = false;
        altitude = 0.0;
        final double longitude = 0.0;
        final double latitude = 0.0;
        boolean b3 = false;
        int n = 0;
        return new BypassCamera.SnapshotInfo.ExifGpsInfo(b, latitude, longitude, altitude, b2, provider, b3, n);
    }
    
    private static BypassCamera.SnapshotInfo.ExifOrientationInfo createExifOrientationInfo(final RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder) {
        final int orientation = photoSavingRequestBuilder.mCommonStatus.orientation;
        if (orientation != 0 && orientation != 90 && orientation != 180 && orientation != 270) {
            return new BypassCamera.SnapshotInfo.ExifOrientationInfo(false, 0);
        }
        return new BypassCamera.SnapshotInfo.ExifOrientationInfo(true, photoSavingRequestBuilder.mCommonStatus.orientation);
    }
    
    private static BypassCamera.SnapshotInfo.ExifThumbnailInfo createExifThumbnailInfo() {
        return new BypassCamera.SnapshotInfo.ExifThumbnailInfo(true, 160, 120, 80);
    }
}
