package com.sonyericsson.android.camera.mediasaving;

import android.location.Location;
import android.os.Build;
import android.text.format.DateFormat;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.storage.RequestFactory;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ExifOption {
    public static final String TAG = "ExifOption";
    public String mDateTime;
    public Location mGPSOption;
    public String mMake;
    public String mModel;
    public int mOrientation = 1;
    public long mPixelXDimension;
    public long mPixelYDimension;
    public byte[] mThumbnailData;
    public long mThumbnailDataLength;

    public static short getExifOrientation(int i) {
        if (i < 0) {
            i += 360;
        }
        if (i == 0) {
            return (short) 1;
        }
        if (i == 90) {
            return (short) 6;
        }
        if (i != 180) {
            return i != 270 ? (short) 1 : (short) 8;
        }
        return (short) 3;
    }

    public static ExifOption create(RequestFactory.RequestBuilder requestBuilder, byte[] bArr) {
        ExifOption exifOption = new ExifOption();
        exifOption.mMake = Build.MANUFACTURER;
        exifOption.mModel = Build.MODEL;
        exifOption.mOrientation = getExifOrientation(requestBuilder.mCommonStatus.orientation);
        exifOption.mDateTime = getExifDate(requestBuilder.getDateTaken());
        exifOption.mPixelXDimension = requestBuilder.mCommonStatus.width;
        exifOption.mPixelYDimension = requestBuilder.mCommonStatus.height;
        exifOption.mGPSOption = requestBuilder.mCommonStatus.location;
        if (CamLog.VERBOSE) {
            String[] strArr = new String[1];
            StringBuilder sb = new StringBuilder();
            sb.append("mThumbnailData is null? ");
            sb.append(exifOption.mThumbnailData == null ? "null" : "not null");
            strArr[0] = sb.toString();
            CamLog.d(strArr);
        }
        if (bArr == null) {
            exifOption.mThumbnailData = new byte[1];
            exifOption.mThumbnailDataLength = 1L;
        } else {
            exifOption.mThumbnailData = bArr;
            exifOption.mThumbnailDataLength = exifOption.mThumbnailData.length;
        }
        if (CamLog.VERBOSE) {
            log(exifOption);
        }
        return exifOption;
    }

    private static void log(ExifOption exifOption) {
        if (CamLog.VERBOSE) {
            CamLog.d("dump of exifOption: ");
            CamLog.d("mMake = " + exifOption.mMake);
            CamLog.d("mModel = " + exifOption.mModel);
            CamLog.d("mOrientation = " + exifOption.mOrientation);
            CamLog.d("mDateTime = " + exifOption.mDateTime);
            CamLog.d("mPixelXDimension = " + exifOption.mPixelXDimension);
            CamLog.d("mPixelYDimension = " + exifOption.mPixelYDimension);
            CamLog.d("mGPSOption = " + exifOption.mGPSOption);
            CamLog.d("mThumbnailDataLength = " + exifOption.mThumbnailDataLength);
        }
    }

    public static String getExifDate(long j) {
        return DateFormat.format("yyyy:MM:dd kk:mm:ss", j).toString();
    }
}
