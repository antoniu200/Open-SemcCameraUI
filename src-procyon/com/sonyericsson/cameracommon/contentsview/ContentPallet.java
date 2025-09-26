// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.contentsview;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup$LayoutParams;
import android.widget.RelativeLayout$LayoutParams;
import android.widget.ProgressBar;
import android.widget.ImageView;
import com.sonyericsson.android.camera.util.CamLog;
import android.view.View$OnClickListener;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Bitmap;
import com.sonyericsson.cameracommon.contentsview.contents.Content;
import android.widget.RelativeLayout;

public class ContentPallet extends RelativeLayout
{
    private static final int INSERTANIMATION_DURATION = 300;
    private static final float INSERTANIMATION_FADE_END = 1.0f;
    private static final float INSERTANIMATION_FADE_START = 0.0f;
    private static final float INSERTANIMATION_SCALE_END = 1.0f;
    private static final float INSERTANIMATION_SCALE_START = 0.7f;
    public static final String TAG = "ContentPallet";
    private static final long intervalTime = 3000L;
    private long curTime;
    private final ClickListener mClickListener;
    private Content mContent;
    private boolean mIsRequestHide;
    private int mRequestId;
    private Bitmap mThumbnail;
    private ThumbnailStateListener mThumbnailStateListener;
    
    public ContentPallet(final Context context) {
        super(context);
        this.curTime = 0L;
        this.mThumbnail = null;
        this.mClickListener = new ClickListener();
    }
    
    public ContentPallet(final Context context, final AttributeSet set) {
        super(context, set);
        this.curTime = 0L;
        this.mThumbnail = null;
        this.mClickListener = new ClickListener();
    }
    
    public void cancelRequestHide() {
        this.mIsRequestHide = false;
    }
    
    void disableClick() {
        this.findViewById(2131296369).setOnClickListener((View$OnClickListener)null);
        this.findViewById(2131296367).setClickable(false);
    }
    
    void enableClick() {
        this.findViewById(2131296369).setOnClickListener((View$OnClickListener)this.mClickListener);
        this.findViewById(2131296367).setClickable(true);
    }
    
    public Content getContent() {
        return this.mContent;
    }
    
    public int getRequestId() {
        return this.mRequestId;
    }
    
    public boolean hasContent() {
        return this.mContent != null;
    }
    
    void initialize(final int n, final ThumbnailStateListener mThumbnailStateListener) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setRequestId() has been called. id = ");
            sb.append(n);
            CamLog.d(sb.toString());
        }
        this.mRequestId = n;
        this.mThumbnailStateListener = mThumbnailStateListener;
        this.findViewById(2131296369).setOnClickListener((View$OnClickListener)this.mClickListener);
    }
    
    public boolean isProvisionalContent() {
        final ImageView imageView = (ImageView)this.findViewById(2131296368);
        return !this.hasContent() && imageView.getDrawable() != null;
    }
    
    void release() {
        if (this.mContent != null) {
            if (this.mThumbnail != null && !this.mThumbnail.isRecycled()) {
                this.mThumbnail.recycle();
                this.mThumbnail = null;
            }
            this.mContent = null;
        }
        this.findViewById(2131296369).setOnClickListener((View$OnClickListener)null);
    }
    
    void set(final Content mContent, final Bitmap mThumbnail) {
        if (CamLog.VERBOSE) {
            CamLog.d("set() has been called.");
        }
        this.mContent = mContent;
        final ProgressBar progressBar = (ProgressBar)this.findViewById(2131296367);
        progressBar.setVisibility(4);
        progressBar.setOnClickListener((View$OnClickListener)null);
        final ImageView imageView = (ImageView)this.findViewById(2131296368);
        this.mThumbnail = mThumbnail;
        if (this.mThumbnail != null) {
            imageView.setImageBitmap(this.mThumbnail);
        }
        else {
            final RelativeLayout$LayoutParams layoutParams = new RelativeLayout$LayoutParams(imageView.getLayoutParams());
            layoutParams.addRule(13);
            final ImageView imageView2 = new ImageView(this.getContext());
            imageView2.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            this.addView((View)imageView2);
            imageView2.setImageResource(2131231254);
        }
        if (mContent.shouldShowPlayableIcon()) {
            final ImageView imageView3 = new ImageView(this.getContext());
            this.addView((View)imageView3);
            imageView3.getLayoutParams().width = -2;
            imageView3.getLayoutParams().height = -2;
            ((RelativeLayout$LayoutParams)imageView3.getLayoutParams()).addRule(13);
            imageView3.setImageResource(mContent.getPlayIconResourceId());
        }
        if (mContent.shouldShowExtraIcon()) {
            final LayoutInflater layoutInflater = ((Activity)this.getContext()).getLayoutInflater();
            if (layoutInflater != null) {
                View view;
                if (CommonUtility.isCoreCameraApp(this.getContext())) {
                    view = layoutInflater.inflate(2131492911, (ViewGroup)this);
                }
                else {
                    view = layoutInflater.inflate(2131492910, (ViewGroup)this);
                }
                ((ImageView)view.findViewById(2131296366)).setBackgroundResource(mContent.getExtraIconResourceId());
            }
            else {
                CamLog.w("could not get inflater.");
            }
        }
        this.findViewById(2131296369).setVisibility(0);
        if (this.mIsRequestHide) {
            this.setVisibility(4);
        }
    }
    
    public void setProvisionalContent() {
        ((ImageView)this.findViewById(2131296368)).setImageDrawable((Drawable)new ColorDrawable(this.getContext().getColor(2131099768)));
        this.findViewById(2131296369).setVisibility(0);
    }
    
    private class ClickListener implements View$OnClickListener
    {
        final ContentPallet this$0;
        
        private ClickListener(final ContentPallet this$0) {
            this.this$0 = this$0;
        }
        
        public void onClick(final View view) {
            if (System.currentTimeMillis() - this.this$0.curTime > 3000L && this.this$0.mContent != null && this.this$0.mThumbnailStateListener != null) {
                this.this$0.mThumbnailStateListener.onThumbnailClicked(this.this$0.mContent);
            }
        }
    }
    
    public interface ThumbnailStateListener
    {
        void onThumbnailClicked(final Content p0);
        
        void onThumbnailCreated(final Content p0);
    }
}
