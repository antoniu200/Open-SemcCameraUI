package com.sonyericsson.cameracommon.device;

import android.os.SystemClock;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingConstants;
import com.sonyericsson.cameracommon.storage.RequestFactory;
import com.sonymobile.imageprocessor.bypasscamera2.BypassCamera;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class BypassCameraSnapshotInfoFactory {
    private static final int EXIF_THUMBNAIL_HEIGHT = 120;
    private static final int EXIF_THUMBNAIL_QUALITY = 80;
    private static final int EXIF_THUMBNAIL_WIDTH = 160;
    private static final int QUALITY = 2;
    private static final boolean QUALITY_AUTO_CONTROL_ENABLED = true;

    public static BypassCamera.SnapshotInfo create(RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder, int i) {
        return new BypassCamera.SnapshotInfo(true, createExifGpsInfo(photoSavingRequestBuilder), createExifOrientationInfo(photoSavingRequestBuilder), createExifThumbnailInfo(), MediaSavingConstants.JpegQuality.getPlatformQualityFromCameraProfile(2), true, i);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r23v0 */
    /* JADX WARN: Type inference failed for: r23v1, types: [int] */
    /* JADX WARN: Type inference failed for: r23v5 */
    public static BypassCamera.SnapshotInfo.ExifGpsInfo createExifGpsInfo(RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder) {
        String str;
        double d;
        double d2;
        double d3;
        boolean z;
        boolean z2;
        boolean z3;
        ?? r23;
        boolean z4;
        int time;
        String provider = "";
        if (photoSavingRequestBuilder.mCommonStatus.location != null) {
            double latitude = photoSavingRequestBuilder.mCommonStatus.location.getLatitude();
            double longitude = photoSavingRequestBuilder.mCommonStatus.location.getLongitude();
            boolean z5 = (latitude == 0.0d && longitude == 0.0d) ? false : true;
            if (z5) {
                double altitude = photoSavingRequestBuilder.mCommonStatus.location.hasAltitude() ? photoSavingRequestBuilder.mCommonStatus.location.getAltitude() : 0.0d;
                if (photoSavingRequestBuilder.mCommonStatus.location.getProvider() != null) {
                    provider = photoSavingRequestBuilder.mCommonStatus.location.getProvider();
                    z4 = true;
                } else {
                    z4 = false;
                }
                if (photoSavingRequestBuilder.mCommonStatus.location.getTime() != 0) {
                    str = provider;
                    z = z5;
                    z3 = true;
                    d3 = altitude;
                    time = (int) ((photoSavingRequestBuilder.mCommonStatus.location.getTime() + ((SystemClock.elapsedRealtimeNanos() - photoSavingRequestBuilder.mCommonStatus.location.getElapsedRealtimeNanos()) / 1000000)) / 1000);
                } else {
                    str = provider;
                    z = z5;
                    d3 = altitude;
                    z3 = false;
                    time = 0;
                }
                d = latitude;
                d2 = longitude;
                z2 = z4;
                r23 = time;
                return new BypassCamera.SnapshotInfo.ExifGpsInfo(z, d, d2, d3, z2, str, z3, r23);
            }
            str = "";
            z = z5;
            d = 0.0d;
            d2 = 0.0d;
            d3 = 0.0d;
            z2 = false;
        } else {
            str = "";
            d = 0.0d;
            d2 = 0.0d;
            d3 = 0.0d;
            z = false;
            z2 = false;
        }
        z3 = z2;
        r23 = z3;
        return new BypassCamera.SnapshotInfo.ExifGpsInfo(z, d, d2, d3, z2, str, z3, r23);
    }

    private static BypassCamera.SnapshotInfo.ExifOrientationInfo createExifOrientationInfo(RequestFactory.PhotoSavingRequestBuilder photoSavingRequestBuilder) {
        int i = photoSavingRequestBuilder.mCommonStatus.orientation;
        if (i == 0 || i == 90 || i == 180 || i == 270) {
            return new BypassCamera.SnapshotInfo.ExifOrientationInfo(true, photoSavingRequestBuilder.mCommonStatus.orientation);
        }
        return new BypassCamera.SnapshotInfo.ExifOrientationInfo(false, 0);
    }

    private static BypassCamera.SnapshotInfo.ExifThumbnailInfo createExifThumbnailInfo() {
        return new BypassCamera.SnapshotInfo.ExifThumbnailInfo(true, 160, 120, 80);
    }
}
