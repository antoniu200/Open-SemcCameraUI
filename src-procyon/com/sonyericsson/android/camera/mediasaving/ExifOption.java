// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.mediasaving;

import android.text.format.DateFormat;
import com.sonyericsson.android.camera.util.CamLog;
import android.os.Build;
import com.sonyericsson.cameracommon.storage.RequestFactory;
import android.location.Location;

public class ExifOption
{
    public static final String TAG = "ExifOption";
    public String mDateTime;
    public Location mGPSOption;
    public String mMake;
    public String mModel;
    public int mOrientation;
    public long mPixelXDimension;
    public long mPixelYDimension;
    public byte[] mThumbnailData;
    public long mThumbnailDataLength;
    
    public ExifOption() {
        this.mOrientation = 1;
    }
    
    public static ExifOption create(final RequestFactory.RequestBuilder requestBuilder, final byte[] mThumbnailData) {
        final ExifOption exifOption = new ExifOption();
        exifOption.mMake = Build.MANUFACTURER;
        exifOption.mModel = Build.MODEL;
        exifOption.mOrientation = getExifOrientation(requestBuilder.mCommonStatus.orientation);
        exifOption.mDateTime = getExifDate(requestBuilder.getDateTaken());
        exifOption.mPixelXDimension = requestBuilder.mCommonStatus.width;
        exifOption.mPixelYDimension = requestBuilder.mCommonStatus.height;
        exifOption.mGPSOption = requestBuilder.mCommonStatus.location;
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("mThumbnailData is null? ");
            String str;
            if (exifOption.mThumbnailData == null) {
                str = "null";
            }
            else {
                str = "not null";
            }
            sb.append(str);
            CamLog.d(sb.toString());
        }
        if (mThumbnailData == null) {
            exifOption.mThumbnailData = new byte[1];
            exifOption.mThumbnailDataLength = 1L;
        }
        else {
            exifOption.mThumbnailData = mThumbnailData;
            exifOption.mThumbnailDataLength = exifOption.mThumbnailData.length;
        }
        if (CamLog.VERBOSE) {
            log(exifOption);
        }
        return exifOption;
    }
    
    public static String getExifDate(final long n) {
        return DateFormat.format((CharSequence)"yyyy:MM:dd kk:mm:ss", n).toString();
    }
    
    public static short getExifOrientation(final int n) {
        int n2 = n;
        if (n < 0) {
            n2 = n + 360;
        }
        short n3 = 1;
        if (n2 != 0) {
            if (n2 != 90) {
                if (n2 != 180) {
                    if (n2 != 270) {
                        n3 = n3;
                    }
                    else {
                        n3 = 8;
                    }
                }
                else {
                    n3 = 3;
                }
            }
            else {
                n3 = 6;
            }
        }
        return n3;
    }
    
    private static void log(final ExifOption exifOption) {
        if (CamLog.VERBOSE) {
            CamLog.d("dump of exifOption: ");
            final StringBuilder sb = new StringBuilder();
            sb.append("mMake = ");
            sb.append(exifOption.mMake);
            CamLog.d(sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("mModel = ");
            sb2.append(exifOption.mModel);
            CamLog.d(sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("mOrientation = ");
            sb3.append(exifOption.mOrientation);
            CamLog.d(sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("mDateTime = ");
            sb4.append(exifOption.mDateTime);
            CamLog.d(sb4.toString());
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("mPixelXDimension = ");
            sb5.append(exifOption.mPixelXDimension);
            CamLog.d(sb5.toString());
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("mPixelYDimension = ");
            sb6.append(exifOption.mPixelYDimension);
            CamLog.d(sb6.toString());
            final StringBuilder sb7 = new StringBuilder();
            sb7.append("mGPSOption = ");
            sb7.append(exifOption.mGPSOption);
            CamLog.d(sb7.toString());
            final StringBuilder sb8 = new StringBuilder();
            sb8.append("mThumbnailDataLength = ");
            sb8.append(exifOption.mThumbnailDataLength);
            CamLog.d(sb8.toString());
        }
    }
}
