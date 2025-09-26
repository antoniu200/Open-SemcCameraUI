package com.sonyericsson.android.camera.view;

import android.net.Uri;
import android.view.View;
import com.sonyericsson.android.camera.view.AutoReviewController;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class AutoReviewContent {
    public View.OnClickListener mClickListener = null;
    public byte[] mData;
    public long mDuration;
    public AutoReviewController.OnAutoReviewEventListener mEventListener;
    public boolean mIsPhoto;
    public boolean mIsReverse;
    public Uri mUri;

    public interface ContentReceiver {
        void onReceive(AutoReviewContent autoReviewContent);
    }
}
