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
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;

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

    public static Bitmap convertBitmap(Context ctx, Uri uri, byte[] data, boolean flag) throws java.lang.Throwable {

        if (data == null) {
            InputStream in = null;
            ByteArrayOutputStream baos = null;
            try {
                in = ctx.getContentResolver().openInputStream(uri);
                baos = new ByteArrayOutputStream();
                if (in != null) {
                    byte[] buf = new byte[1024];
                    int n;
                    while ((n = in.read(buf)) != -1) {
                        baos.write(buf, 0, n);
                    }
                }
                data = baos.toByteArray();

            } catch (FileNotFoundException e) {
                CamLog.e(new String[] { "load of auto review image is failed " + e });

            } catch (IOException e) {
                CamLog.e(new String[] { "load of auto review image is failed " + e });

            } finally {
                if (baos != null) {
                    try { baos.close(); } catch (Throwable ignore) {}
                }
                if (in != null) {
                    try { in.close(); } catch (Throwable ignore) {}
                }
            }
        }

        // Always delegate to preview creation as in the fallback
        return getPreviewBmp(ctx, data, flag);
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
