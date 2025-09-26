package com.sonyericsson.android.camera.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.view.AutoReviewContent;
import com.sonyericsson.android.camera.view.baselayout.BaseLayout;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import com.sonyericsson.cameracommon.utility.CameraTimer;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import com.sonyericsson.cameracommon.utility.PositionConverter;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class AutoReviewController implements AutoReviewContent.ContentReceiver {
    private static final String TAG = "AutoReviewController";
    private final CameraActivity mActivity;
    private final BaseLayout mBaseLayout;
    private RelativeLayout mContainer;
    private ImageView mImageView;
    private OnAutoReviewEventListener mListener;
    private CameraTimer mTimer;

    public interface OnAutoReviewEventListener {
        void onAutoReviewClosed();
    }

    public AutoReviewController(Context context, BaseLayout baseLayout) {
        this.mActivity = (CameraActivity) context;
        this.mBaseLayout = baseLayout;
    }

    public void setup() {
        int dimensionPixelSize;
        if (isTablet(this.mActivity)) {
            if (isPreviewAspectRatio(1, 1)) {
                this.mImageView = (ImageView) this.mActivity.findViewById(R.id.autoreview_1_1_tablet);
                this.mContainer = (RelativeLayout) this.mActivity.findViewById(R.id.autopreview_container_1_1_tablet);
            } else if (isPreviewAspectRatio(4, 3)) {
                this.mImageView = (ImageView) this.mActivity.findViewById(R.id.autoreview_4_3_tablet);
                this.mContainer = (RelativeLayout) this.mActivity.findViewById(R.id.autopreview_container_4_3_tablet);
            } else {
                this.mImageView = (ImageView) this.mActivity.findViewById(R.id.autoreview_16_9_tablet);
                this.mContainer = (RelativeLayout) this.mActivity.findViewById(R.id.autopreview_container_16_9_tablet);
            }
        } else if (isPreviewAspectRatio(1, 1)) {
            this.mImageView = (ImageView) this.mActivity.findViewById(R.id.autoreview_1_1);
            this.mContainer = (RelativeLayout) this.mActivity.findViewById(R.id.autopreview_container_1_1);
        } else if (isPreviewAspectRatio(4, 3)) {
            this.mImageView = (ImageView) this.mActivity.findViewById(R.id.autoreview_4_3);
            this.mContainer = (RelativeLayout) this.mActivity.findViewById(R.id.autopreview_container_4_3);
        } else {
            this.mImageView = (ImageView) this.mActivity.findViewById(R.id.autoreview_16_9);
            this.mContainer = (RelativeLayout) this.mActivity.findViewById(R.id.autopreview_container_16_9);
        }
        Resources resources = this.mActivity.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (displayMetrics.densityDpi > DisplayMetrics.DENSITY_DEVICE_STABLE) {
            ViewGroup.LayoutParams layoutParams = this.mImageView.getLayoutParams();
            if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
                dimensionPixelSize = ((displayMetrics.widthPixels - resources.getDimensionPixelSize(R.dimen.capture_button_container_side_length)) / 2) - (resources.getDimensionPixelSize(R.dimen.autopreview_shadow_padding) * 2);
            } else {
                dimensionPixelSize = ((displayMetrics.heightPixels - resources.getDimensionPixelSize(R.dimen.capture_button_container_side_length)) / 2) - (resources.getDimensionPixelSize(R.dimen.autopreview_shadow_padding) * 2);
            }
            layoutParams.height = dimensionPixelSize;
            if (isPreviewAspectRatio(1, 1)) {
                layoutParams.width = dimensionPixelSize;
            } else if (isPreviewAspectRatio(4, 3)) {
                layoutParams.width = (4 * dimensionPixelSize) / 3;
            } else {
                layoutParams.width = (16 * dimensionPixelSize) / 9;
            }
            this.mImageView.setLayoutParams(layoutParams);
        }
        this.mImageView.setContentDescription(resources.getString(R.string.cam_strings_accessibility_auto_review_txt));
        this.mImageView.setClickable(false);
        this.mBaseLayout.addViewFinderGestureDetectorExclusiveView(this.mImageView);
    }

    public void show(Uri uri, byte[] bArr, boolean z, long j) throws Throwable {
        if (this.mContainer != null) {
            update(uri, bArr, z);
            this.mContainer.setVisibility(0);
            this.mImageView.setClickable(true);
            startTimer(j);
        }
    }

    public void hide() {
        if (this.mContainer != null) {
            this.mContainer.setVisibility(8);
            this.mImageView.setClickable(false);
            stopTimer();
        }
    }

    private void startTimer(long j) {
        stopTimer();
        if (j > 0) {
            this.mTimer = new CameraTimer(j, j, new PreviewTimerHandler(), TAG, 0L);
            this.mTimer.start();
        }
    }

    private void stopTimer() {
        if (this.mTimer != null) {
            this.mTimer.cancel();
            this.mTimer = null;
        }
    }

    @Override // com.sonyericsson.android.camera.view.AutoReviewContent.ContentReceiver
    public void onReceive(AutoReviewContent autoReviewContent) throws Throwable {
        this.mListener = autoReviewContent.mEventListener;
        this.mImageView.setOnClickListener(autoReviewContent.mClickListener);
        show(autoReviewContent.mUri, autoReviewContent.mData, autoReviewContent.mIsReverse, autoReviewContent.mDuration);
    }

    private class PreviewTimerHandler extends Handler {
        private PreviewTimerHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    AutoReviewController.this.hide();
                    if (AutoReviewController.this.mListener != null) {
                        AutoReviewController.this.mListener.onAutoReviewClosed();
                        break;
                    }
                    break;
            }
        }
    }

    private void update(Uri uri, byte[] bArr, boolean z) throws Throwable {
        Bitmap bitmapConvertBitmap = convertBitmap(this.mActivity, uri, bArr, z);
        if (bitmapConvertBitmap == null || this.mImageView == null) {
            return;
        }
        this.mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        this.mImageView.setImageBitmap(bitmapConvertBitmap);
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x004f  */
    /* JADX WARN: Removed duplicated region for block: B:80:? A[Catch: all -> 0x005e, Throwable -> 0x0060, SYNTHETIC, TRY_LEAVE, TryCatch #3 {Throwable -> 0x0060, blocks: (B:6:0x000d, B:42:0x005a, B:41:0x0056, B:43:0x005d), top: B:62:0x000d }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static android.graphics.Bitmap convertBitmap(android.content.Context r8, android.net.Uri r9, byte[] r10, boolean r11) throws java.lang.Throwable {
        /*
            if (r10 != 0) goto La6
            r0 = 1
            r1 = 0
            android.content.ContentResolver r2 = r8.getContentResolver()     // Catch: java.io.IOException -> L73 java.io.FileNotFoundException -> L8d
            java.io.InputStream r9 = r2.openInputStream(r9)     // Catch: java.io.IOException -> L73 java.io.FileNotFoundException -> L8d
            r2 = 0
            java.io.ByteArrayOutputStream r3 = new java.io.ByteArrayOutputStream     // Catch: java.lang.Throwable -> L5e java.lang.Throwable -> L60
            r3.<init>()     // Catch: java.lang.Throwable -> L5e java.lang.Throwable -> L60
            if (r9 == 0) goto L28
            r4 = 1024(0x400, float:1.435E-42)
            byte[] r4 = new byte[r4]     // Catch: java.lang.Throwable -> L23 java.lang.Throwable -> L26
        L18:
            int r5 = r9.read(r4)     // Catch: java.lang.Throwable -> L23 java.lang.Throwable -> L26
            r6 = -1
            if (r5 == r6) goto L28
            r3.write(r4, r1, r5)     // Catch: java.lang.Throwable -> L23 java.lang.Throwable -> L26
            goto L18
        L23:
            r4 = move-exception
            r5 = r2
            goto L4d
        L26:
            r4 = move-exception
            goto L48
        L28:
            byte[] r4 = r3.toByteArray()     // Catch: java.lang.Throwable -> L23 java.lang.Throwable -> L26
            if (r3 == 0) goto L3a
            r3.close()     // Catch: java.lang.Throwable -> L32 java.lang.Throwable -> L36
            goto L3a
        L32:
            r10 = move-exception
            r3 = r10
            r10 = r4
            goto L62
        L36:
            r10 = move-exception
            r2 = r10
            r10 = r4
            goto L61
        L3a:
            if (r9 == 0) goto L46
            r9.close()     // Catch: java.io.IOException -> L40 java.io.FileNotFoundException -> L43
            goto L46
        L40:
            r9 = move-exception
            r10 = r4
            goto L74
        L43:
            r9 = move-exception
            r10 = r4
            goto L8e
        L46:
            r10 = r4
            goto La6
        L48:
            throw r4     // Catch: java.lang.Throwable -> L49
        L49:
            r5 = move-exception
            r7 = r5
            r5 = r4
            r4 = r7
        L4d:
            if (r3 == 0) goto L5d
            if (r5 == 0) goto L5a
            r3.close()     // Catch: java.lang.Throwable -> L55 java.lang.Throwable -> L5e
            goto L5d
        L55:
            r3 = move-exception
            r5.addSuppressed(r3)     // Catch: java.lang.Throwable -> L5e java.lang.Throwable -> L60
            goto L5d
        L5a:
            r3.close()     // Catch: java.lang.Throwable -> L5e java.lang.Throwable -> L60
        L5d:
            throw r4     // Catch: java.lang.Throwable -> L5e java.lang.Throwable -> L60
        L5e:
            r3 = move-exception
            goto L62
        L60:
            r2 = move-exception
        L61:
            throw r2     // Catch: java.lang.Throwable -> L5e
        L62:
            if (r9 == 0) goto L72
            if (r2 == 0) goto L6f
            r9.close()     // Catch: java.lang.Throwable -> L6a java.io.IOException -> L73 java.io.FileNotFoundException -> L8d
            goto L72
        L6a:
            r9 = move-exception
            r2.addSuppressed(r9)     // Catch: java.io.IOException -> L73 java.io.FileNotFoundException -> L8d
            goto L72
        L6f:
            r9.close()     // Catch: java.io.IOException -> L73 java.io.FileNotFoundException -> L8d
        L72:
            throw r3     // Catch: java.io.IOException -> L73 java.io.FileNotFoundException -> L8d
        L73:
            r9 = move-exception
        L74:
            java.lang.String[] r0 = new java.lang.String[r0]
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "load of auto review image is failed "
            r2.append(r3)
            r2.append(r9)
            java.lang.String r9 = r2.toString()
            r0[r1] = r9
            com.sonyericsson.android.camera.util.CamLog.e(r0)
            goto La6
        L8d:
            r9 = move-exception
        L8e:
            java.lang.String[] r0 = new java.lang.String[r0]
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "load of auto review image is failed "
            r2.append(r3)
            r2.append(r9)
            java.lang.String r9 = r2.toString()
            r0[r1] = r9
            com.sonyericsson.android.camera.util.CamLog.e(r0)
        La6:
            android.graphics.Bitmap r8 = getPreviewBmp(r8, r10, r11)
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sonyericsson.android.camera.view.AutoReviewController.convertBitmap(android.content.Context, android.net.Uri, byte[], boolean):android.graphics.Bitmap");
    }

    private static Bitmap getPreviewBmp(Context context, byte[] bArr, boolean z) throws Resources.NotFoundException {
        int dimensionPixelSize;
        int dimensionPixelSize2;
        if (bArr == null) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
        int i = options.outHeight;
        int i2 = options.outWidth;
        Resources resources = context.getResources();
        if (isTablet(context)) {
            if (isPreviewAspectRatio(1, 1)) {
                dimensionPixelSize = resources.getDimensionPixelSize(R.dimen.autopreview_1_1_height_tablet);
                dimensionPixelSize2 = resources.getDimensionPixelSize(R.dimen.autopreview_1_1_width_tablet);
            } else if (isPreviewAspectRatio(4, 3)) {
                dimensionPixelSize = resources.getDimensionPixelSize(R.dimen.autopreview_4_3_height_tablet);
                dimensionPixelSize2 = resources.getDimensionPixelSize(R.dimen.autopreview_4_3_width_tablet);
            } else {
                dimensionPixelSize = resources.getDimensionPixelSize(R.dimen.autopreview_16_9_height_tablet);
                dimensionPixelSize2 = resources.getDimensionPixelSize(R.dimen.autopreview_16_9_width_tablet);
            }
        } else if (isPreviewAspectRatio(1, 1)) {
            dimensionPixelSize = resources.getDimensionPixelSize(R.dimen.autopreview_1_1_height);
            dimensionPixelSize2 = resources.getDimensionPixelSize(R.dimen.autopreview_1_1_width);
        } else if (isPreviewAspectRatio(4, 3)) {
            dimensionPixelSize = resources.getDimensionPixelSize(R.dimen.autopreview_4_3_height);
            dimensionPixelSize2 = resources.getDimensionPixelSize(R.dimen.autopreview_4_3_width);
        } else {
            dimensionPixelSize = resources.getDimensionPixelSize(R.dimen.autopreview_16_9_height);
            dimensionPixelSize2 = resources.getDimensionPixelSize(R.dimen.autopreview_16_9_width);
        }
        if (i < i2) {
            options.inSampleSize = Math.round(i / dimensionPixelSize);
        } else if (i2 < i) {
            options.inSampleSize = Math.round(i2 / dimensionPixelSize2);
        }
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        Bitmap bitmapDecodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
        if (bitmapDecodeByteArray != null) {
            return z ? reversePreviewBmp(bitmapDecodeByteArray) : bitmapDecodeByteArray;
        }
        CamLog.e("decodeByteArray failed. mImageData: " + bArr.length + ", outSize: " + options.outWidth + ", " + options.outHeight + ", inSampleSize: " + options.inSampleSize);
        return bitmapDecodeByteArray;
    }

    private static Bitmap reversePreviewBmp(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setScale(-1.0f, -1.0f);
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        bitmap.recycle();
        return bitmapCreateBitmap;
    }

    private static boolean isPreviewAspectRatio(int i, int i2) {
        Rect previewSize = PositionConverter.getInstance().getPreviewSize();
        if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            if (previewSize.width() * i != previewSize.height() * i2) {
                return false;
            }
        } else if (previewSize.width() * i2 != previewSize.height() * i) {
            return false;
        }
        return true;
    }

    private static boolean isTablet(Context context) {
        return LayoutDependencyResolver.isTablet(context);
    }

    public boolean isShowing() {
        return this.mContainer != null && this.mContainer.getVisibility() == 0;
    }
}
