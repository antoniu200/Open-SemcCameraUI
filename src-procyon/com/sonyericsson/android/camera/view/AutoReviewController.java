// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view;

import android.os.Message;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.util.DisplayMetrics;
import android.widget.ImageView$ScaleType;
import android.os.Handler;
import android.graphics.Matrix;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import android.graphics.Rect;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import com.sonyericsson.cameracommon.utility.PositionConverter;
import android.content.res.Resources;
import android.graphics.Bitmap$Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory$Options;
import java.io.InputStream;
import com.sonyericsson.android.camera.util.CamLog;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import android.graphics.Bitmap;
import android.net.Uri;
import android.content.Context;
import com.sonyericsson.cameracommon.utility.CameraTimer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.sonyericsson.android.camera.view.baselayout.BaseLayout;
import com.sonyericsson.android.camera.CameraActivity;

public class AutoReviewController implements ContentReceiver
{
    private static final String TAG = "AutoReviewController";
    private final CameraActivity mActivity;
    private final BaseLayout mBaseLayout;
    private RelativeLayout mContainer;
    private ImageView mImageView;
    private OnAutoReviewEventListener mListener;
    private CameraTimer mTimer;
    
    public AutoReviewController(final Context context, final BaseLayout mBaseLayout) {
        this.mActivity = (CameraActivity)context;
        this.mBaseLayout = mBaseLayout;
    }
    
    public static Bitmap convertBitmap(final Context context, Uri byteArray, byte[] t, final boolean b) {
        Object o = t;
        if (t == null) {
            Throwable t2 = t;
            o = t;
            try {
                final InputStream openInputStream = context.getContentResolver().openInputStream((Uri)byteArray);
                t2 = null;
                final Object o2 = o = null;
                byteArray = (IOException)t;
                Label_0256: {
                    try {
                        o = o2;
                        byteArray = (IOException)t;
                        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    Label_0101:
                        while (true) {
                            if (openInputStream != null) {
                                try {
                                    byteArray = (IOException)(Object)new byte[1024];
                                    while (true) {
                                        final int read = openInputStream.read((byte[])(Object)byteArray);
                                        if (read == -1) {
                                            break Label_0101;
                                        }
                                        byteArrayOutputStream.write((byte[])(Object)byteArray, 0, read);
                                    }
                                }
                                catch (final Throwable t2) {
                                Label_0132_Outer:
                                    while (true) {
                                        try {
                                            throw t2;
                                        }
                                        finally {}
                                        byteArray = (IOException)(Object)byteArrayOutputStream.toByteArray();
                                        iftrue(Label_0132:)(byteArrayOutputStream == null);
                                    Block_17:
                                        while (true) {
                                            Block_16: {
                                                break Block_16;
                                                iftrue(Label_0163:)(openInputStream == null);
                                                break Block_17;
                                            }
                                            try {
                                                byteArrayOutputStream.close();
                                            }
                                            catch (final Throwable t) {}
                                            continue;
                                        }
                                        try {
                                            openInputStream.close();
                                        }
                                        catch (final IOException t) {
                                            o = byteArray;
                                        }
                                        catch (final FileNotFoundException t) {
                                            o = byteArray;
                                        }
                                        Label_0163: {
                                            continue Label_0132_Outer;
                                        }
                                    }
                                }
                                finally {
                                    t2 = null;
                                }
                                if (byteArrayOutputStream != null) {
                                    if (t2 != null) {
                                        o = o2;
                                        byteArray = (IOException)t;
                                        try {
                                            byteArrayOutputStream.close();
                                        }
                                        catch (final Throwable exception) {
                                            o = o2;
                                            byteArray = (IOException)t;
                                            t2.addSuppressed(exception);
                                        }
                                    }
                                    else {
                                        o = o2;
                                        byteArray = (IOException)t;
                                        byteArrayOutputStream.close();
                                    }
                                }
                                o = o2;
                                byteArray = (IOException)t;
                                throw;
                            }
                            continue Label_0101;
                        }
                    }
                    catch (final Throwable o) {
                        byteArray = (IOException)t;
                        t = (Throwable)o;
                    }
                    finally {
                        t = (Throwable)o;
                        break Label_0256;
                    }
                    o = t;
                    throw t;
                }
                if (openInputStream != null) {
                    if (t != null) {
                        t2 = byteArray;
                        o = byteArray;
                        try {
                            openInputStream.close();
                        }
                        catch (final Throwable exception2) {
                            t2 = byteArray;
                            o = byteArray;
                            t.addSuppressed(exception2);
                        }
                    }
                    else {
                        t2 = byteArray;
                        o = byteArray;
                        openInputStream.close();
                    }
                }
                t2 = byteArray;
                o = byteArray;
            }
            catch (final IOException byteArray) {
                o = t2;
            }
            catch (final FileNotFoundException ex) {}
            final StringBuilder sb = new StringBuilder();
            sb.append("load of auto review image is failed ");
            sb.append(byteArray);
            CamLog.e(sb.toString());
        }
        return getPreviewBmp(context, (byte[])o, b);
    }
    
    private static Bitmap getPreviewBmp(final Context context, final byte[] array, final boolean b) {
        Bitmap reversePreviewBmp;
        if (array != null) {
            final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
            bitmapFactory$Options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(array, 0, array.length, bitmapFactory$Options);
            final int outHeight = bitmapFactory$Options.outHeight;
            final int outWidth = bitmapFactory$Options.outWidth;
            final Resources resources = context.getResources();
            int n;
            int n2;
            if (isTablet(context)) {
                if (isPreviewAspectRatio(1, 1)) {
                    n = resources.getDimensionPixelSize(2131165270);
                    n2 = resources.getDimensionPixelSize(2131165272);
                }
                else if (isPreviewAspectRatio(4, 3)) {
                    n = resources.getDimensionPixelSize(2131165274);
                    n2 = resources.getDimensionPixelSize(2131165276);
                }
                else {
                    n = resources.getDimensionPixelSize(2131165266);
                    n2 = resources.getDimensionPixelSize(2131165268);
                }
            }
            else if (isPreviewAspectRatio(1, 1)) {
                n = resources.getDimensionPixelSize(2131165269);
                n2 = resources.getDimensionPixelSize(2131165271);
            }
            else if (isPreviewAspectRatio(4, 3)) {
                n = resources.getDimensionPixelSize(2131165273);
                n2 = resources.getDimensionPixelSize(2131165275);
            }
            else {
                n = resources.getDimensionPixelSize(2131165265);
                n2 = resources.getDimensionPixelSize(2131165267);
            }
            if (outHeight < outWidth) {
                bitmapFactory$Options.inSampleSize = Math.round(outHeight / (float)n);
            }
            else if (outWidth < outHeight) {
                bitmapFactory$Options.inSampleSize = Math.round(outWidth / (float)n2);
            }
            bitmapFactory$Options.inJustDecodeBounds = false;
            bitmapFactory$Options.inPreferredConfig = Bitmap$Config.RGB_565;
            bitmapFactory$Options.inPurgeable = true;
            final Bitmap decodeByteArray = BitmapFactory.decodeByteArray(array, 0, array.length, bitmapFactory$Options);
            if (decodeByteArray != null) {
                reversePreviewBmp = decodeByteArray;
                if (b) {
                    reversePreviewBmp = reversePreviewBmp(decodeByteArray);
                }
            }
            else {
                final StringBuilder sb = new StringBuilder();
                sb.append("decodeByteArray failed. mImageData: ");
                sb.append(array.length);
                sb.append(", outSize: ");
                sb.append(bitmapFactory$Options.outWidth);
                sb.append(", ");
                sb.append(bitmapFactory$Options.outHeight);
                sb.append(", inSampleSize: ");
                sb.append(bitmapFactory$Options.inSampleSize);
                CamLog.e(sb.toString());
                reversePreviewBmp = decodeByteArray;
            }
        }
        else {
            reversePreviewBmp = null;
        }
        return reversePreviewBmp;
    }
    
    private static boolean isPreviewAspectRatio(final int n, final int n2) {
        final Rect previewSize = PositionConverter.getInstance().getPreviewSize();
        final LayoutOrientationResolver.LayoutOrientationType orientation = LayoutOrientationResolver.getInstance().getOrientation();
        final LayoutOrientationResolver.LayoutOrientationType portrait = LayoutOrientationResolver.LayoutOrientationType.PORTRAIT;
        boolean b = false;
        if (orientation == portrait) {
            if (previewSize.width() * n != previewSize.height() * n2) {
                return b;
            }
        }
        else if (previewSize.width() * n2 != previewSize.height() * n) {
            return b;
        }
        b = true;
        return b;
    }
    
    private static boolean isTablet(final Context context) {
        return LayoutDependencyResolver.isTablet(context);
    }
    
    private static Bitmap reversePreviewBmp(final Bitmap bitmap) {
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        final Matrix matrix = new Matrix();
        matrix.setScale(-1.0f, -1.0f);
        final Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        bitmap.recycle();
        return bitmap2;
    }
    
    private void startTimer(final long n) {
        this.stopTimer();
        if (n > 0L) {
            (this.mTimer = new CameraTimer(n, n, new PreviewTimerHandler(), "AutoReviewController", 0L)).start();
        }
    }
    
    private void stopTimer() {
        if (this.mTimer != null) {
            this.mTimer.cancel();
            this.mTimer = null;
        }
    }
    
    private void update(final Uri uri, final byte[] array, final boolean b) {
        final Bitmap convertBitmap = convertBitmap((Context)this.mActivity, uri, array, b);
        if (convertBitmap != null && this.mImageView != null) {
            this.mImageView.setScaleType(ImageView$ScaleType.FIT_XY);
            this.mImageView.setImageBitmap(convertBitmap);
        }
    }
    
    public void hide() {
        if (this.mContainer != null) {
            this.mContainer.setVisibility(8);
            this.mImageView.setClickable(false);
            this.stopTimer();
        }
    }
    
    public boolean isShowing() {
        final RelativeLayout mContainer = this.mContainer;
        boolean b = false;
        if (mContainer != null) {
            if (this.mContainer.getVisibility() == 0) {
                b = true;
            }
            return b;
        }
        return false;
    }
    
    @Override
    public void onReceive(final AutoReviewContent autoReviewContent) {
        this.mListener = autoReviewContent.mEventListener;
        this.mImageView.setOnClickListener(autoReviewContent.mClickListener);
        this.show(autoReviewContent.mUri, autoReviewContent.mData, autoReviewContent.mIsReverse, autoReviewContent.mDuration);
    }
    
    public void setup() {
        if (isTablet((Context)this.mActivity)) {
            if (isPreviewAspectRatio(1, 1)) {
                this.mImageView = (ImageView)this.mActivity.findViewById(2131296310);
                this.mContainer = (RelativeLayout)this.mActivity.findViewById(2131296304);
            }
            else if (isPreviewAspectRatio(4, 3)) {
                this.mImageView = (ImageView)this.mActivity.findViewById(2131296312);
                this.mContainer = (RelativeLayout)this.mActivity.findViewById(2131296306);
            }
            else {
                this.mImageView = (ImageView)this.mActivity.findViewById(2131296308);
                this.mContainer = (RelativeLayout)this.mActivity.findViewById(2131296302);
            }
        }
        else if (isPreviewAspectRatio(1, 1)) {
            this.mImageView = (ImageView)this.mActivity.findViewById(2131296309);
            this.mContainer = (RelativeLayout)this.mActivity.findViewById(2131296303);
        }
        else if (isPreviewAspectRatio(4, 3)) {
            this.mImageView = (ImageView)this.mActivity.findViewById(2131296311);
            this.mContainer = (RelativeLayout)this.mActivity.findViewById(2131296305);
        }
        else {
            this.mImageView = (ImageView)this.mActivity.findViewById(2131296307);
            this.mContainer = (RelativeLayout)this.mActivity.findViewById(2131296301);
        }
        final Resources resources = this.mActivity.getResources();
        final DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (displayMetrics.densityDpi > DisplayMetrics.DENSITY_DEVICE_STABLE) {
            final ViewGroup$LayoutParams layoutParams = this.mImageView.getLayoutParams();
            int n;
            if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
                n = (displayMetrics.widthPixels - resources.getDimensionPixelSize(2131165285)) / 2 - resources.getDimensionPixelSize(2131165278) * 2;
            }
            else {
                n = (displayMetrics.heightPixels - resources.getDimensionPixelSize(2131165285)) / 2 - resources.getDimensionPixelSize(2131165278) * 2;
            }
            layoutParams.height = n;
            if (isPreviewAspectRatio(1, 1)) {
                layoutParams.width = n;
            }
            else if (isPreviewAspectRatio(4, 3)) {
                layoutParams.width = 4 * n / 3;
            }
            else {
                layoutParams.width = 16 * n / 9;
            }
            this.mImageView.setLayoutParams(layoutParams);
        }
        this.mImageView.setContentDescription((CharSequence)resources.getString(2131689562));
        this.mImageView.setClickable(false);
        this.mBaseLayout.addViewFinderGestureDetectorExclusiveView((View)this.mImageView);
    }
    
    public void show(final Uri uri, final byte[] array, final boolean b, final long n) {
        if (this.mContainer != null) {
            this.update(uri, array, b);
            this.mContainer.setVisibility(0);
            this.mImageView.setClickable(true);
            this.startTimer(n);
        }
    }
    
    public interface OnAutoReviewEventListener
    {
        void onAutoReviewClosed();
    }
    
    private class PreviewTimerHandler extends Handler
    {
        final AutoReviewController this$0;
        
        private PreviewTimerHandler(final AutoReviewController this$0) {
            this.this$0 = this$0;
        }
        
        public void handleMessage(final Message message) {
            switch (message.what) {
                default: {
                    return;
                }
                case 1: {
                    this.this$0.hide();
                    if (this.this$0.mListener != null) {
                        this.this$0.mListener.onAutoReviewClosed();
                    }
                }
                case 0:
                case 2:
                case 3: {}
            }
        }
    }
}
