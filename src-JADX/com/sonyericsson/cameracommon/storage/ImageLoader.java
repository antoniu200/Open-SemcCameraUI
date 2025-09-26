package com.sonyericsson.cameracommon.storage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InvalidObjectException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ImageLoader {
    private static final int FIRST_REDUCE_RATIO_FULL_IMG = 2;
    private static final int FULL_SIZE_MAX_LENGTH = 1025;
    public static final String TAG = "ImageLoader";
    private final Context mContext;
    private final byte[] mImageData;
    private final BitmapFactory.Options mOption;
    private final int mOrientation;
    private final Uri mUri;

    public ImageLoader(Context context, Uri uri, int i) {
        this.mContext = context;
        this.mUri = uri;
        this.mImageData = null;
        this.mOrientation = i;
        this.mOption = new BitmapFactory.Options();
    }

    public ImageLoader(Context context, byte[] bArr, int i) {
        this.mContext = context;
        this.mUri = null;
        this.mImageData = bArr;
        this.mOrientation = i;
        this.mOption = new BitmapFactory.Options();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:100:0x0150 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:102:0x00bc A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:104:0x0177 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:106:0x008c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:112:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:114:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:115:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:116:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x007a A[Catch: all -> 0x005d, IllegalArgumentException -> 0x0061, IOException -> 0x0066, FileNotFoundException -> 0x006b, InvalidObjectException -> 0x0070, TryCatch #13 {FileNotFoundException -> 0x006b, InvalidObjectException -> 0x0070, IOException -> 0x0066, IllegalArgumentException -> 0x0061, all -> 0x005d, blocks: (B:14:0x0044, B:22:0x0076, B:24:0x007a, B:25:0x0082), top: B:111:0x0044 }] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0082 A[Catch: all -> 0x005d, IllegalArgumentException -> 0x0061, IOException -> 0x0066, FileNotFoundException -> 0x006b, InvalidObjectException -> 0x0070, TRY_LEAVE, TryCatch #13 {FileNotFoundException -> 0x006b, InvalidObjectException -> 0x0070, IOException -> 0x0066, IllegalArgumentException -> 0x0061, all -> 0x005d, blocks: (B:14:0x0044, B:22:0x0076, B:24:0x007a, B:25:0x0082), top: B:111:0x0044 }] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00b1 A[Catch: all -> 0x0099, IllegalArgumentException -> 0x009d, IOException -> 0x00a0, FileNotFoundException -> 0x00a4, InvalidObjectException -> 0x00a8, TRY_LEAVE, TryCatch #20 {all -> 0x0099, blocks: (B:27:0x008c, B:28:0x0092, B:43:0x00ad, B:45:0x00b1), top: B:106:0x008c }] */
    /* JADX WARN: Removed duplicated region for block: B:94:0x00eb A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0128 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0186 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public android.graphics.Bitmap load() throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 420
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sonyericsson.cameracommon.storage.ImageLoader.load():android.graphics.Bitmap");
    }

    private void calcBounds(InputStream inputStream, BitmapFactory.Options options) throws InvalidObjectException, FileNotFoundException {
        if (CamLog.VERBOSE) {
            CamLog.d("calcBounds()");
        }
        options.inSampleSize = 2;
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmapDecodeStream = decodeStream(inputStream, options);
        if (bitmapDecodeStream != null && !bitmapDecodeStream.isRecycled()) {
            bitmapDecodeStream.recycle();
        }
        if (options.outWidth == -1 || options.outHeight == -1) {
            CamLog.e("Bitmap read error");
            throw new InvalidObjectException("Failed to calculate bounds of bitmap");
        }
        if (CamLog.VERBOSE) {
            CamLog.d("BMP out height:" + options.outHeight);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("BMP out width:" + options.outWidth);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("Scale ratio:" + options.inSampleSize);
        }
    }

    private Bitmap loadFullSize(InputStream inputStream, BitmapFactory.Options options) throws InvalidObjectException, FileNotFoundException {
        if (CamLog.VERBOSE) {
            CamLog.d("loadFullSize()");
        }
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmapDecodeStream = decodeStream(inputStream, options);
        if (bitmapDecodeStream == null) {
            CamLog.e("loadFullSize: Decode read error");
            throw new InvalidObjectException("Failed to decode full size image");
        }
        CamLog.d("loadFullSize: mOrientation", RotationUtil.orientationToString(this.mOrientation));
        if (this.mOrientation == 0) {
            return bitmapDecodeStream;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(this.mOrientation, bitmapDecodeStream.getWidth() / 2.0f, bitmapDecodeStream.getHeight() / 2.0f);
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmapDecodeStream, 0, 0, bitmapDecodeStream.getWidth(), bitmapDecodeStream.getHeight(), matrix, false);
        bitmapDecodeStream.recycle();
        Bitmap bitmapCopy = bitmapCreateBitmap.copy(Bitmap.Config.ARGB_8888, false);
        bitmapCreateBitmap.recycle();
        return bitmapCopy;
    }

    private Bitmap decodeStream(InputStream inputStream, BitmapFactory.Options options) throws FileNotFoundException {
        Rect rect = new Rect(0, 0, 0, 0);
        if (CamLog.VERBOSE) {
            CamLog.d("Loading full size image started");
        }
        Bitmap bitmapDecodeStream = BitmapFactory.decodeStream(inputStream, rect, options);
        if (CamLog.VERBOSE) {
            CamLog.d("Loading full size image finished");
        }
        return bitmapDecodeStream;
    }

    private int calcRatio(BitmapFactory.Options options, int i, int i2) {
        int i3 = options.outWidth * i;
        int i4 = options.outHeight * i;
        int iMax = Math.max(((i4 + i2) - 1) / i2, ((i3 + i2) - 1) / i2);
        if (iMax == 0) {
            if (CamLog.VERBOSE) {
                CamLog.d("Full size image loading ratio: error");
            }
            return 1;
        }
        if (iMax > 1 && (i3 / iMax > i2 || i4 / iMax > i2)) {
            iMax--;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("Full size image loading ratio:" + iMax);
        }
        return iMax;
    }
}
