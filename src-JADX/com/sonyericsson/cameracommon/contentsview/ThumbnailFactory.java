package com.sonyericsson.cameracommon.contentsview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import com.sonyericsson.android.camera.util.CamLog;
import java.io.IOException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ThumbnailFactory {
    private static final int MAX_NUM_PIXELS_MICRO_THUMBNAIL = 19200;
    public static final String TAG = "ThumbnailFactory";
    public static final int TARGET_SIZE_MICRO_THUMBNAIL = 96;
    private static final int UNCONSTRAINED = -1;

    /* JADX WARN: Removed duplicated region for block: B:36:0x00d2  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00dc  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static android.graphics.Bitmap createMicroThumbnail(com.sonyericsson.cameracommon.contentsview.contents.Content.ContentInfo r7) {
        /*
            Method dump skipped, instructions count: 238
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sonyericsson.cameracommon.contentsview.ThumbnailFactory.createMicroThumbnail(com.sonyericsson.cameracommon.contentsview.contents.Content$ContentInfo):android.graphics.Bitmap");
    }

    public static boolean tryCreateThumbnail(String str) {
        if (CamLog.VERBOSE) {
            CamLog.d("tryCreateThumbnail(" + str + ")");
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(str, options);
            if (!options.mCancel && options.outWidth != -1) {
                if (options.outHeight != -1) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            CamLog.e("createMicroThumbnail() : ", e);
            return false;
        }
    }

    private static Bitmap rotateThumbnail(Bitmap bitmap, int i) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (i == 0) {
            return bitmap;
        }
        try {
            Matrix matrix = new Matrix();
            matrix.setRotate(i, width / 2.0f, height / 2.0f);
            Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
            bitmap.recycle();
            return bitmapCreateBitmap;
        } catch (IllegalArgumentException unused) {
            CamLog.e("IllegalArgumentException : width = " + width + ", height = " + height);
            return bitmap;
        } catch (Exception unused2) {
            CamLog.e("Exception : width = " + width + ", height = " + height);
            return bitmap;
        }
    }

    public static Bitmap createVideoThumbnail(String str) throws IOException {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            try {
                mediaMetadataRetriever.setDataSource(str);
                Bitmap frameAtTime = mediaMetadataRetriever.getFrameAtTime(-1L);
                try {
                    mediaMetadataRetriever.release();
                    return frameAtTime;
                } catch (RuntimeException unused) {
                    CamLog.e("Ignore failures while cleaning up.");
                    return frameAtTime;
                }
            } catch (IllegalArgumentException unused2) {
                CamLog.e("Assume this is a corrupt video file.");
                return null;
            } catch (RuntimeException unused3) {
                CamLog.e("Assume this is a corrupt video file.");
                return null;
            }
        } finally {
            try {
                mediaMetadataRetriever.release();
            } catch (RuntimeException unused4) {
                CamLog.e("Ignore failures while cleaning up.");
            }
        }
    }

    public static Bitmap createVideoThumbnail(Context context, Uri uri) throws IOException {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            try {
                mediaMetadataRetriever.setDataSource(context, uri);
                Bitmap frameAtTime = mediaMetadataRetriever.getFrameAtTime(-1L);
                try {
                    mediaMetadataRetriever.release();
                    return frameAtTime;
                } catch (RuntimeException unused) {
                    CamLog.e("Ignore failures while cleaning up.");
                    return frameAtTime;
                }
            } catch (IllegalArgumentException unused2) {
                CamLog.e("Assume this is a corrupt video file.");
                return null;
            } catch (RuntimeException unused3) {
                CamLog.e("Assume this is a corrupt video file.");
                return null;
            }
        } finally {
            try {
                mediaMetadataRetriever.release();
            } catch (RuntimeException unused4) {
                CamLog.e("Ignore failures while cleaning up.");
            }
        }
    }

    public static Bitmap createVideoThumbnail(Context context, Uri uri, int i) {
        return rotateThumbnail(createVideoThumbnail(context, uri), i);
    }

    private static int computeSampleSize(BitmapFactory.Options options, int i, int i2) {
        int iComputeInitialSampleSize = computeInitialSampleSize(options, i, i2);
        if (iComputeInitialSampleSize > 8) {
            return 8 * ((iComputeInitialSampleSize + 7) / 8);
        }
        int i3 = 1;
        while (i3 < iComputeInitialSampleSize) {
            i3 <<= 1;
        }
        return i3;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int i, int i2) {
        int iMin;
        double d = options.outWidth;
        double d2 = options.outHeight;
        int iCeil = i2 == -1 ? 1 : (int) Math.ceil(Math.sqrt((d * d2) / i2));
        if (i == -1) {
            iMin = 128;
        } else {
            double d3 = i;
            iMin = (int) Math.min(Math.floor(d / d3), Math.floor(d2 / d3));
        }
        if (iMin < iCeil) {
            return iCeil;
        }
        if (i2 == -1 && i == -1) {
            return 1;
        }
        return i == -1 ? iCeil : iMin;
    }
}
