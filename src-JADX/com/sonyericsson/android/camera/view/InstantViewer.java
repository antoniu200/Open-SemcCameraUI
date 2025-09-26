package com.sonyericsson.android.camera.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.controller.album.AlbumLauncher;
import com.sonyericsson.android.camera.controller.album.AlbumPreloader;
import com.sonyericsson.android.camera.setting.UserSettings;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.contentsview.PredictiveCaptureStoreInfo;
import com.sonyericsson.cameracommon.contentsview.ThumbnailFactory;
import com.sonyericsson.cameracommon.keytranslator.KeyEventTranslator;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingConstants;
import com.sonyericsson.cameracommon.review.ReviewWindowListener;
import com.sonyericsson.cameracommon.storage.ImageLoader;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import com.sonyericsson.cameracommon.viewfinder.LayoutDependencyResolver;
import com.sonymobile.cameracommon.research.ResearchUtil;
import java.util.Arrays;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class InstantViewer extends FrameLayout {
    private static final String ACTION_FAST_VIEW_MODE_LAUNCHED = "com.sonyericsson.album.intent.action.FAST_VIEW_MODE_LAUNCHED";
    public static final String TAG = "InstantViewer";
    private Bitmap mAlbumBmp;
    private AlbumNotifyReceiver mAlbumNotifyReceiver;
    private AlbumPreloader mAlbumPreloader;
    private CameraActivity mCameraActivity;
    private boolean mIsOpened;
    protected KeyEventTranslator mKeyEventTranslator;
    private ReviewWindowListener mListener;
    private Rect mOrientedPictureSize;
    protected ImageView mPictureImage;
    private int mRequestId;
    private Uri mUri;

    public InstantViewer(Context context) {
        super(context);
        this.mRequestId = -1;
        this.mIsOpened = false;
        this.mCameraActivity = null;
        this.mAlbumBmp = null;
        this.mOrientedPictureSize = new Rect();
        this.mAlbumNotifyReceiver = null;
        this.mCameraActivity = (CameraActivity) getContext();
    }

    public InstantViewer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mRequestId = -1;
        this.mIsOpened = false;
        this.mCameraActivity = null;
        this.mAlbumBmp = null;
        this.mOrientedPictureSize = new Rect();
        this.mAlbumNotifyReceiver = null;
        this.mCameraActivity = (CameraActivity) getContext();
    }

    public InstantViewer(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mRequestId = -1;
        this.mIsOpened = false;
        this.mCameraActivity = null;
        this.mAlbumBmp = null;
        this.mOrientedPictureSize = new Rect();
        this.mAlbumNotifyReceiver = null;
        this.mCameraActivity = (CameraActivity) getContext();
    }

    public void setup(UserSettings userSettings) {
        setup(new KeyEventTranslator(userSettings));
    }

    private void setup(KeyEventTranslator keyEventTranslator) {
        this.mKeyEventTranslator = keyEventTranslator;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        if (CamLog.VERBOSE) {
            CamLog.d("onFinishInflate.");
        }
        super.onFinishInflate();
        this.mPictureImage = (ImageView) findViewById(R.id.auto_review_picture_image);
    }

    public Uri getUri() {
        return this.mUri;
    }

    public void setUri(Uri uri) {
        this.mUri = uri;
    }

    public int getRequestId() {
        return this.mRequestId;
    }

    public void clear() {
        this.mUri = null;
        this.mRequestId = -1;
        if (this.mAlbumBmp != null) {
            this.mAlbumBmp.recycle();
            this.mAlbumBmp = null;
        }
    }

    public static void launchAlbum(Activity activity, Uri uri, String str, boolean z, PredictiveCaptureStoreInfo predictiveCaptureStoreInfo) {
        AlbumLauncher.launchAlbum(activity, uri, str, -1, false, z, predictiveCaptureStoreInfo);
    }

    public static void launchAlbumSecure(Activity activity, List<Uri> list, List<String> list2, PredictiveCaptureStoreInfo predictiveCaptureStoreInfo, long[] jArr) {
        AlbumLauncher.launchAlbumSecure(activity, list, list2, predictiveCaptureStoreInfo, jArr);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        if (CamLog.VERBOSE) {
            CamLog.d("onAttachedToWindow.");
        }
        super.onAttachedToWindow();
        setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        if (CamLog.VERBOSE) {
            CamLog.d("onDetachedFromWindow.");
        }
        this.mListener = null;
        super.onDetachedFromWindow();
    }

    public boolean open(Uri uri, String str, int i, int i2, boolean z, ReviewWindowListener reviewWindowListener, int i3) {
        String str2;
        if (CamLog.VERBOSE) {
            StringBuilder sb = new StringBuilder();
            sb.append("open() 1 mimetype:");
            str2 = str;
            sb.append(str2);
            CamLog.d(sb.toString());
        } else {
            str2 = str;
        }
        this.mListener = reviewWindowListener;
        this.mRequestId = i3;
        String string = uri != null ? uri.toString() : null;
        if (!setupScreen(this.mCameraActivity, (string == null || !string.startsWith(MediaSavingConstants.EXTENDED_PHOTO_STORAGE_URI.toString())) ? uri : Uri.parse(string.replaceFirst(MediaSavingConstants.EXTENDED_PHOTO_STORAGE_URI.toString(), MediaSavingConstants.STANDARD_PHOTO_STORAGE_URI.toString())), null, null, str2, i, i2, z, this.mAlbumBmp)) {
            return false;
        }
        show();
        if (this.mListener != null) {
            this.mListener.onReviewWindowOpen();
        }
        return true;
    }

    public boolean open(byte[] bArr, String str, String str2, int i, int i2, boolean z, ReviewWindowListener reviewWindowListener, int i3) {
        String str3;
        if (CamLog.VERBOSE) {
            StringBuilder sb = new StringBuilder();
            sb.append("open() 2 mimetype:");
            str3 = str2;
            sb.append(str3);
            CamLog.d(sb.toString());
        } else {
            str3 = str2;
        }
        this.mListener = reviewWindowListener;
        this.mRequestId = i3;
        if (!setupScreen(this.mCameraActivity, null, bArr, str, str3, i, i2, z, this.mAlbumBmp)) {
            return false;
        }
        show();
        if (this.mListener != null) {
            this.mListener.onReviewWindowOpen();
        }
        return true;
    }

    public void show() {
        if (CamLog.VERBOSE) {
            CamLog.d("show()");
        }
        WindowManager.LayoutParams attributes = this.mCameraActivity.getWindow().getAttributes();
        attributes.rotationAnimation = 2;
        this.mCameraActivity.getWindow().setAttributes(attributes);
        if (CameraActivity.LayoutOrientation.ReverseLandscape == this.mCameraActivity.getLayoutOrientation()) {
            this.mCameraActivity.setRequestedOrientation(1);
        }
        this.mCameraActivity.setRequestedOrientation(2);
        this.mIsOpened = true;
        showScreen();
        requestFocus();
        ResearchUtil.getInstance().setViewerLaunched();
        registerAlbumNotifyReceiver();
    }

    public void hide() {
        if (CamLog.VERBOSE) {
            CamLog.d("hide()");
        }
        if (this.mCameraActivity != null && this.mCameraActivity.getRequestedOrientation() != 3) {
            if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
                this.mCameraActivity.setRequestedOrientation(1);
            } else {
                this.mCameraActivity.setRequestedOrientation(0);
            }
        }
        clearFocus();
        hideScreen();
        clearScreen();
        this.mIsOpened = false;
        if (this.mListener != null) {
            this.mListener.onReviewWindowClose();
        }
        unregisterAlbumNotifyReceiver();
    }

    public boolean setAlbumBitmap(Uri uri) {
        if (this.mAlbumBmp != null) {
            this.mAlbumBmp.recycle();
            this.mAlbumBmp = null;
        }
        if (this.mAlbumPreloader == null) {
            return false;
        }
        this.mAlbumBmp = this.mAlbumPreloader.getBitmap(uri);
        return this.mAlbumBmp != null;
    }

    public boolean isAlbumBitmapSetting() {
        return this.mAlbumBmp != null;
    }

    private boolean setupScreen(Activity activity, Uri uri, byte[] bArr, String str, String str2, int i, int i2, boolean z, Bitmap bitmap) throws Throwable {
        int width;
        int height;
        if (CamLog.VERBOSE) {
            String[] strArr = new String[1];
            StringBuilder sb = new StringBuilder();
            sb.append("setupScreen() uri:");
            sb.append(uri);
            sb.append(", picture imageData: ");
            sb.append(bArr == null ? "null" : Integer.valueOf(Arrays.hashCode(bArr)));
            sb.append(", picture videoPath: ");
            sb.append(str);
            sb.append(", picture mime: ");
            sb.append(str2);
            sb.append(", reviewOrientation: ");
            sb.append(i);
            sb.append(", pictureOrientation: ");
            sb.append(i2);
            sb.append(" ,isFront:");
            sb.append(z);
            sb.append(" ,dispBmp:");
            sb.append(bitmap);
            strArr[0] = sb.toString();
            CamLog.d(strArr);
        }
        if (bitmap == null) {
            if (uri != null) {
                bitmap = new ImageLoader(getContext(), uri, i2).load();
            } else if (bArr != null) {
                bitmap = new ImageLoader(getContext(), bArr, i2).load();
            } else {
                bitmap = (str == null || !(MediaSavingConstants.MEDIA_TYPE_MPEG4_MIME.equals(str2) || MediaSavingConstants.MEDIA_TYPE_3GP_MIME.equals(str2))) ? null : ThumbnailFactory.createVideoThumbnail(str);
            }
        }
        if (bitmap == null) {
            CamLog.e("Cannot create the image.");
            return false;
        }
        this.mOrientedPictureSize = new Rect(LayoutDependencyResolver.getSurfaceRect(activity, bitmap.getWidth() / bitmap.getHeight()));
        if (MediaSavingConstants.MEDIA_TYPE_JPEG_MIME.equals(str2)) {
            width = this.mOrientedPictureSize.width();
            height = this.mOrientedPictureSize.height();
        } else {
            width = bitmap.getWidth();
            height = bitmap.getHeight();
        }
        Bitmap bitmapCreateScaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        this.mPictureImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        this.mPictureImage.setImageBitmap(bitmapCreateScaledBitmap);
        this.mUri = uri;
        return true;
    }

    private void showScreen() {
        if (CamLog.VERBOSE) {
            CamLog.d("show: visibility: " + getVisibility());
        }
        setVisible(true);
    }

    private void hideScreen() {
        if (CamLog.VERBOSE) {
            CamLog.d("hide: visibility: " + getVisibility());
        }
        setVisible(false);
    }

    private void setVisible(boolean z) {
        if (CamLog.VERBOSE) {
            CamLog.d("setVisible: " + z);
        }
        setVisibility(z ? 0 : 4);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (CamLog.VERBOSE) {
            CamLog.d("onKeyDown: " + i);
        }
        switch (this.mKeyEventTranslator.translateKeyCode(i)) {
            case SHUTTER:
            case FOCUS:
            case FOCUS_AND_SHUTTER_UP_KEY:
            case FOCUS_AND_SHUTTER_DOWN_KEY:
                if (keyEvent.getRepeatCount() == 0) {
                    backToViewFinder();
                }
                return true;
            case VOLUME:
                return false;
            case BACK:
            case MENU:
                return true;
            default:
                return false;
        }
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (CamLog.VERBOSE) {
            CamLog.d("onKeyUp: " + i);
        }
        if (i != 4 && i != 82) {
            return false;
        }
        backToViewFinder();
        return true;
    }

    public void clearScreen() {
        this.mPictureImage.setImageBitmap(null);
        if (this.mAlbumBmp != null) {
            this.mAlbumBmp.recycle();
            this.mAlbumBmp = null;
        }
    }

    private void backToViewFinder() {
        hide();
    }

    public boolean isOpened() {
        return this.mIsOpened;
    }

    public void setUiOrientation(int i) {
        if (CamLog.VERBOSE) {
            CamLog.d("setUiOrientation: " + i);
        }
    }

    private void registerAlbumNotifyReceiver() {
        if (this.mAlbumNotifyReceiver != null) {
            unregisterAlbumNotifyReceiver();
        }
        if (CamLog.VERBOSE) {
            CamLog.d("register AlbumNotifyReceiver");
        }
        if (this.mCameraActivity != null) {
            this.mAlbumNotifyReceiver = new AlbumNotifyReceiver();
            this.mCameraActivity.registerReceiver(this.mAlbumNotifyReceiver, new IntentFilter("com.sonyericsson.album.intent.action.FAST_VIEW_MODE_LAUNCHED"));
        }
    }

    private void unregisterAlbumNotifyReceiver() {
        if (this.mAlbumNotifyReceiver != null) {
            if (CamLog.VERBOSE) {
                CamLog.d("unregister AlbumNotifyReceiver");
            }
            if (this.mCameraActivity != null) {
                this.mCameraActivity.unregisterReceiver(this.mAlbumNotifyReceiver);
            }
            this.mAlbumNotifyReceiver = null;
        }
    }

    private class AlbumNotifyReceiver extends BroadcastReceiver {
        public static final String TAG = "AlbumNotifyReceiver";

        private AlbumNotifyReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("com.sonyericsson.album.intent.action.FAST_VIEW_MODE_LAUNCHED".equals(intent.getAction())) {
                if (CamLog.VERBOSE) {
                    CamLog.d("onReceive()");
                }
                InstantViewer.this.hide();
            }
        }
    }

    public void createAlbumPreloader() {
        if (this.mAlbumPreloader == null) {
            this.mAlbumPreloader = new AlbumPreloader(this.mCameraActivity);
            this.mAlbumPreloader.prewarmAlbum();
        }
    }

    public void prepareBitmap(Uri uri) {
        if (this.mAlbumPreloader != null) {
            this.mAlbumPreloader.prepareBitmap(uri);
        }
    }

    public void releaseAlbumPreloader() {
        if (this.mAlbumPreloader != null) {
            this.mAlbumPreloader.release();
            this.mAlbumPreloader = null;
        }
    }
}
