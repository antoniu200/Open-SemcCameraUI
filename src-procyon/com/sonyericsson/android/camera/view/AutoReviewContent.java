// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view;

import android.net.Uri;
import android.view.View$OnClickListener;

public class AutoReviewContent
{
    public View$OnClickListener mClickListener;
    public byte[] mData;
    public long mDuration;
    public AutoReviewController.OnAutoReviewEventListener mEventListener;
    public boolean mIsPhoto;
    public boolean mIsReverse;
    public Uri mUri;
    
    public AutoReviewContent() {
        this.mClickListener = null;
    }
    
    public interface ContentReceiver
    {
        void onReceive(final AutoReviewContent p0);
    }
}
