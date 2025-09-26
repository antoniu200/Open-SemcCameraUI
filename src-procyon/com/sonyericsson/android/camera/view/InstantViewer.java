// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view;

import android.content.Intent;
import android.view.WindowManager$LayoutParams;
import com.sonymobile.cameracommon.research.ResearchUtil;
import com.sonyericsson.android.camera.setting.UserSettings;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingConstants;
import android.view.KeyEvent;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import java.io.Serializable;
import android.widget.ImageView$ScaleType;
import com.sonyericsson.cameracommon.viewfinder.LayoutDependencyResolver;
import com.sonyericsson.cameracommon.contentsview.ThumbnailFactory;
import com.sonyericsson.cameracommon.storage.ImageLoader;
import java.util.Arrays;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import java.util.List;
import com.sonyericsson.android.camera.controller.album.AlbumLauncher;
import com.sonyericsson.cameracommon.contentsview.PredictiveCaptureStoreInfo;
import android.app.Activity;
import com.sonyericsson.android.camera.util.CamLog;
import android.util.AttributeSet;
import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import android.graphics.Rect;
import com.sonyericsson.cameracommon.review.ReviewWindowListener;
import com.sonyericsson.cameracommon.keytranslator.KeyEventTranslator;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.controller.album.AlbumPreloader;
import android.graphics.Bitmap;
import android.widget.FrameLayout;

public class InstantViewer extends FrameLayout
{
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
    
    public InstantViewer(final Context context) {
        super(context);
        this.mRequestId = -1;
        this.mIsOpened = false;
        this.mCameraActivity = null;
        this.mAlbumBmp = null;
        this.mOrientedPictureSize = new Rect();
        this.mAlbumNotifyReceiver = null;
        this.mCameraActivity = (CameraActivity)this.getContext();
    }
    
    public InstantViewer(final Context context, final AttributeSet set) {
        super(context, set);
        this.mRequestId = -1;
        this.mIsOpened = false;
        this.mCameraActivity = null;
        this.mAlbumBmp = null;
        this.mOrientedPictureSize = new Rect();
        this.mAlbumNotifyReceiver = null;
        this.mCameraActivity = (CameraActivity)this.getContext();
    }
    
    public InstantViewer(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mRequestId = -1;
        this.mIsOpened = false;
        this.mCameraActivity = null;
        this.mAlbumBmp = null;
        this.mOrientedPictureSize = new Rect();
        this.mAlbumNotifyReceiver = null;
        this.mCameraActivity = (CameraActivity)this.getContext();
    }
    
    private void backToViewFinder() {
        this.hide();
    }
    
    private void hideScreen() {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("hide: visibility: ");
            sb.append(this.getVisibility());
            CamLog.d(sb.toString());
        }
        this.setVisible(false);
    }
    
    public static void launchAlbum(final Activity activity, final Uri uri, final String s, final boolean b, final PredictiveCaptureStoreInfo predictiveCaptureStoreInfo) {
        AlbumLauncher.launchAlbum(activity, uri, s, -1, false, b, predictiveCaptureStoreInfo);
    }
    
    public static void launchAlbumSecure(final Activity activity, final List<Uri> list, final List<String> list2, final PredictiveCaptureStoreInfo predictiveCaptureStoreInfo, final long[] array) {
        AlbumLauncher.launchAlbumSecure(activity, list, list2, predictiveCaptureStoreInfo, array);
    }
    
    private void registerAlbumNotifyReceiver() {
        if (this.mAlbumNotifyReceiver != null) {
            this.unregisterAlbumNotifyReceiver();
        }
        if (CamLog.VERBOSE) {
            CamLog.d("register AlbumNotifyReceiver");
        }
        if (this.mCameraActivity != null) {
            this.mAlbumNotifyReceiver = new AlbumNotifyReceiver();
            this.mCameraActivity.registerReceiver((BroadcastReceiver)this.mAlbumNotifyReceiver, new IntentFilter("com.sonyericsson.album.intent.action.FAST_VIEW_MODE_LAUNCHED"));
        }
    }
    
    private void setVisible(final boolean b) {
        final boolean verbose = CamLog.VERBOSE;
        int visibility = 0;
        if (verbose) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setVisible: ");
            sb.append(b);
            CamLog.d(sb.toString());
        }
        if (!b) {
            visibility = 4;
        }
        this.setVisibility(visibility);
    }
    
    private void setup(final KeyEventTranslator mKeyEventTranslator) {
        this.mKeyEventTranslator = mKeyEventTranslator;
    }
    
    private boolean setupScreen(final Activity activity, final Uri uri, final byte[] a, final String str, final String s, int i, int j, final boolean b, Bitmap obj) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setupScreen() uri:");
            sb.append(uri);
            sb.append(", picture imageData: ");
            Serializable value;
            if (a == null) {
                value = "null";
            }
            else {
                value = Arrays.hashCode(a);
            }
            sb.append(value);
            sb.append(", picture videoPath: ");
            sb.append(str);
            sb.append(", picture mime: ");
            sb.append(s);
            sb.append(", reviewOrientation: ");
            sb.append(i);
            sb.append(", pictureOrientation: ");
            sb.append(j);
            sb.append(" ,isFront:");
            sb.append(b);
            sb.append(" ,dispBmp:");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        if (obj == null) {
            if (uri != null) {
                obj = new ImageLoader(this.getContext(), uri, j).load();
            }
            else if (a != null) {
                obj = new ImageLoader(this.getContext(), a, j).load();
            }
            else if (str != null && ("video/mp4".equals(s) || "video/3gpp".equals(s))) {
                obj = ThumbnailFactory.createVideoThumbnail(str);
            }
            else {
                obj = null;
            }
        }
        if (obj == null) {
            CamLog.e("Cannot create the image.");
            return false;
        }
        this.mOrientedPictureSize = new Rect(LayoutDependencyResolver.getSurfaceRect((Context)activity, obj.getWidth() / (float)obj.getHeight()));
        if ("image/jpeg".equals(s)) {
            i = this.mOrientedPictureSize.width();
            j = this.mOrientedPictureSize.height();
        }
        else {
            i = obj.getWidth();
            j = obj.getHeight();
        }
        final Bitmap scaledBitmap = Bitmap.createScaledBitmap(obj, i, j, true);
        this.mPictureImage.setScaleType(ImageView$ScaleType.FIT_CENTER);
        this.mPictureImage.setImageBitmap(scaledBitmap);
        this.mUri = uri;
        return true;
    }
    
    private void showScreen() {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("show: visibility: ");
            sb.append(this.getVisibility());
            CamLog.d(sb.toString());
        }
        this.setVisible(true);
    }
    
    private void unregisterAlbumNotifyReceiver() {
        if (this.mAlbumNotifyReceiver != null) {
            if (CamLog.VERBOSE) {
                CamLog.d("unregister AlbumNotifyReceiver");
            }
            if (this.mCameraActivity != null) {
                this.mCameraActivity.unregisterReceiver((BroadcastReceiver)this.mAlbumNotifyReceiver);
            }
            this.mAlbumNotifyReceiver = null;
        }
    }
    
    public void clear() {
        this.mUri = null;
        this.mRequestId = -1;
        if (this.mAlbumBmp != null) {
            this.mAlbumBmp.recycle();
            this.mAlbumBmp = null;
        }
    }
    
    public void clearScreen() {
        this.mPictureImage.setImageBitmap((Bitmap)null);
        if (this.mAlbumBmp != null) {
            this.mAlbumBmp.recycle();
            this.mAlbumBmp = null;
        }
    }
    
    public void createAlbumPreloader() {
        if (this.mAlbumPreloader == null) {
            (this.mAlbumPreloader = new AlbumPreloader((Context)this.mCameraActivity)).prewarmAlbum();
        }
    }
    
    public int getRequestId() {
        return this.mRequestId;
    }
    
    public Uri getUri() {
        return this.mUri;
    }
    
    public void hide() {
        if (CamLog.VERBOSE) {
            CamLog.d("hide()");
        }
        if (this.mCameraActivity != null && this.mCameraActivity.getRequestedOrientation() != 3) {
            if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
                this.mCameraActivity.setRequestedOrientation(1);
            }
            else {
                this.mCameraActivity.setRequestedOrientation(0);
            }
        }
        this.clearFocus();
        this.hideScreen();
        this.clearScreen();
        this.mIsOpened = false;
        if (this.mListener != null) {
            this.mListener.onReviewWindowClose();
        }
        this.unregisterAlbumNotifyReceiver();
    }
    
    public boolean isAlbumBitmapSetting() {
        return this.mAlbumBmp != null;
    }
    
    public boolean isOpened() {
        return this.mIsOpened;
    }
    
    protected void onAttachedToWindow() {
        if (CamLog.VERBOSE) {
            CamLog.d("onAttachedToWindow.");
        }
        super.onAttachedToWindow();
        this.setBackgroundColor(-16777216);
    }
    
    protected void onDetachedFromWindow() {
        if (CamLog.VERBOSE) {
            CamLog.d("onDetachedFromWindow.");
        }
        this.mListener = null;
        super.onDetachedFromWindow();
    }
    
    protected void onFinishInflate() {
        if (CamLog.VERBOSE) {
            CamLog.d("onFinishInflate.");
        }
        super.onFinishInflate();
        this.mPictureImage = (ImageView)this.findViewById(2131296297);
    }
    
    public boolean onKeyDown(final int i, final KeyEvent keyEvent) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onKeyDown: ");
            sb.append(i);
            CamLog.d(sb.toString());
        }
        switch (InstantViewer$1.$SwitchMap$com$sonyericsson$cameracommon$keytranslator$KeyEventTranslator$TranslatedKeyCode[this.mKeyEventTranslator.translateKeyCode(i).ordinal()]) {
            default: {
                return false;
            }
            case 5: {
                return false;
            }
            case 1:
            case 2:
            case 3:
            case 4: {
                if (keyEvent.getRepeatCount() == 0) {
                    this.backToViewFinder();
                    return true;
                }
                return true;
            }
            case 6:
            case 7: {
                return true;
            }
        }
    }
    
    public boolean onKeyUp(final int i, final KeyEvent keyEvent) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onKeyUp: ");
            sb.append(i);
            CamLog.d(sb.toString());
        }
        if (i != 4 && i != 82) {
            return false;
        }
        this.backToViewFinder();
        return true;
    }
    
    public boolean open(Uri parse, final String str, final int n, final int n2, final boolean b, final ReviewWindowListener mListener, final int mRequestId) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("open() 1 mimetype:");
            sb.append(str);
            CamLog.d(sb.toString());
        }
        this.mListener = mListener;
        this.mRequestId = mRequestId;
        String string;
        if (parse != null) {
            string = parse.toString();
        }
        else {
            string = null;
        }
        if (string != null && string.startsWith(MediaSavingConstants.EXTENDED_PHOTO_STORAGE_URI.toString())) {
            parse = Uri.parse(string.replaceFirst(MediaSavingConstants.EXTENDED_PHOTO_STORAGE_URI.toString(), MediaSavingConstants.STANDARD_PHOTO_STORAGE_URI.toString()));
        }
        if (this.setupScreen(this.mCameraActivity, parse, null, null, str, n, n2, b, this.mAlbumBmp)) {
            this.show();
            if (this.mListener != null) {
                this.mListener.onReviewWindowOpen();
            }
            return true;
        }
        return false;
    }
    
    public boolean open(final byte[] array, final String s, final String str, final int n, final int n2, final boolean b, final ReviewWindowListener mListener, final int mRequestId) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("open() 2 mimetype:");
            sb.append(str);
            CamLog.d(sb.toString());
        }
        this.mListener = mListener;
        this.mRequestId = mRequestId;
        if (this.setupScreen(this.mCameraActivity, null, array, s, str, n, n2, b, this.mAlbumBmp)) {
            this.show();
            if (this.mListener != null) {
                this.mListener.onReviewWindowOpen();
            }
            return true;
        }
        return false;
    }
    
    public void prepareBitmap(final Uri uri) {
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
    
    public boolean setAlbumBitmap(final Uri uri) {
        if (this.mAlbumBmp != null) {
            this.mAlbumBmp.recycle();
            this.mAlbumBmp = null;
        }
        final AlbumPreloader mAlbumPreloader = this.mAlbumPreloader;
        boolean b = false;
        if (mAlbumPreloader == null) {
            return false;
        }
        this.mAlbumBmp = this.mAlbumPreloader.getBitmap(uri);
        if (this.mAlbumBmp != null) {
            b = true;
        }
        return b;
    }
    
    public void setUiOrientation(final int i) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setUiOrientation: ");
            sb.append(i);
            CamLog.d(sb.toString());
        }
    }
    
    public void setUri(final Uri mUri) {
        this.mUri = mUri;
    }
    
    public void setup(final UserSettings userSettings) {
        this.setup(new KeyEventTranslator(userSettings));
    }
    
    public void show() {
        if (CamLog.VERBOSE) {
            CamLog.d("show()");
        }
        final WindowManager$LayoutParams attributes = this.mCameraActivity.getWindow().getAttributes();
        attributes.rotationAnimation = 2;
        this.mCameraActivity.getWindow().setAttributes(attributes);
        if (CameraActivity.LayoutOrientation.ReverseLandscape == this.mCameraActivity.getLayoutOrientation()) {
            this.mCameraActivity.setRequestedOrientation(1);
        }
        this.mCameraActivity.setRequestedOrientation(2);
        this.mIsOpened = true;
        this.showScreen();
        this.requestFocus();
        ResearchUtil.getInstance().setViewerLaunched();
        this.registerAlbumNotifyReceiver();
    }
    
    private class AlbumNotifyReceiver extends BroadcastReceiver
    {
        public static final String TAG = "AlbumNotifyReceiver";
        final InstantViewer this$0;
        
        private AlbumNotifyReceiver(final InstantViewer this$0) {
            this.this$0 = this$0;
        }
        
        public void onReceive(final Context context, final Intent intent) {
            if ("com.sonyericsson.album.intent.action.FAST_VIEW_MODE_LAUNCHED".equals(intent.getAction())) {
                if (CamLog.VERBOSE) {
                    CamLog.d("onReceive()");
                }
                this.this$0.hide();
            }
        }
    }
}
