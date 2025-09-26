package com.sonyericsson.cameracommon.contentsview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.contentsview.contents.Content;
import com.sonyericsson.cameracommon.utility.CommonUtility;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ContentPallet extends RelativeLayout {
    private static final int INSERTANIMATION_DURATION = 300;
    private static final float INSERTANIMATION_FADE_END = 1.0f;
    private static final float INSERTANIMATION_FADE_START = 0.0f;
    private static final float INSERTANIMATION_SCALE_END = 1.0f;
    private static final float INSERTANIMATION_SCALE_START = 0.7f;
    public static final String TAG = "ContentPallet";
    private static final long intervalTime = 3000;
    private long curTime;
    private final ClickListener mClickListener;
    private Content mContent;
    private boolean mIsRequestHide;
    private int mRequestId;
    private Bitmap mThumbnail;
    private ThumbnailStateListener mThumbnailStateListener;

    public interface ThumbnailStateListener {
        void onThumbnailClicked(Content content);

        void onThumbnailCreated(Content content);
    }

    public ContentPallet(Context context) {
        super(context);
        this.curTime = 0L;
        this.mThumbnail = null;
        this.mClickListener = new ClickListener();
    }

    public ContentPallet(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.curTime = 0L;
        this.mThumbnail = null;
        this.mClickListener = new ClickListener();
    }

    void initialize(int i, ThumbnailStateListener thumbnailStateListener) {
        if (CamLog.VERBOSE) {
            CamLog.d("setRequestId() has been called. id = " + i);
        }
        this.mRequestId = i;
        this.mThumbnailStateListener = thumbnailStateListener;
        findViewById(R.id.content_thumbnail_frame).setOnClickListener(this.mClickListener);
    }

    void release() {
        if (this.mContent != null) {
            if (this.mThumbnail != null && !this.mThumbnail.isRecycled()) {
                this.mThumbnail.recycle();
                this.mThumbnail = null;
            }
            this.mContent = null;
        }
        findViewById(R.id.content_thumbnail_frame).setOnClickListener(null);
    }

    public int getRequestId() {
        return this.mRequestId;
    }

    void set(Content content, Bitmap bitmap) {
        View viewInflate;
        if (CamLog.VERBOSE) {
            CamLog.d("set() has been called.");
        }
        this.mContent = content;
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.content_progress_bar);
        progressBar.setVisibility(4);
        progressBar.setOnClickListener(null);
        ImageView imageView = (ImageView) findViewById(R.id.content_thumbnail);
        this.mThumbnail = bitmap;
        if (this.mThumbnail != null) {
            imageView.setImageBitmap(this.mThumbnail);
        } else {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(imageView.getLayoutParams());
            layoutParams.addRule(13);
            ImageView imageView2 = new ImageView(getContext());
            imageView2.setLayoutParams(layoutParams);
            addView(imageView2);
            imageView2.setImageResource(R.drawable.cam_photo_stack_file_corrupted_icn);
        }
        if (content.shouldShowPlayableIcon()) {
            ImageView imageView3 = new ImageView(getContext());
            addView(imageView3);
            imageView3.getLayoutParams().width = -2;
            imageView3.getLayoutParams().height = -2;
            ((RelativeLayout.LayoutParams) imageView3.getLayoutParams()).addRule(13);
            imageView3.setImageResource(content.getPlayIconResourceId());
        }
        if (content.shouldShowExtraIcon()) {
            LayoutInflater layoutInflater = ((Activity) getContext()).getLayoutInflater();
            if (layoutInflater != null) {
                if (CommonUtility.isCoreCameraApp((Activity) getContext())) {
                    viewInflate = layoutInflater.inflate(R.layout.content_extra_icon_core_camera, this);
                } else {
                    viewInflate = layoutInflater.inflate(R.layout.content_extra_icon, this);
                }
                ((ImageView) viewInflate.findViewById(R.id.content_extra_icon_image)).setBackgroundResource(content.getExtraIconResourceId());
            } else {
                CamLog.w("could not get inflater.");
            }
        }
        findViewById(R.id.content_thumbnail_frame).setVisibility(0);
        if (this.mIsRequestHide) {
            setVisibility(4);
        }
    }

    public void setProvisionalContent() {
        ((ImageView) findViewById(R.id.content_thumbnail)).setImageDrawable(new ColorDrawable(getContext().getColor(R.color.thumbnail_provisional_content_color)));
        findViewById(R.id.content_thumbnail_frame).setVisibility(0);
    }

    void enableClick() {
        findViewById(R.id.content_thumbnail_frame).setOnClickListener(this.mClickListener);
        findViewById(R.id.content_progress_bar).setClickable(true);
    }

    void disableClick() {
        findViewById(R.id.content_thumbnail_frame).setOnClickListener(null);
        findViewById(R.id.content_progress_bar).setClickable(false);
    }

    public boolean hasContent() {
        return this.mContent != null;
    }

    public Content getContent() {
        return this.mContent;
    }

    public void cancelRequestHide() {
        this.mIsRequestHide = false;
    }

    public boolean isProvisionalContent() {
        return (hasContent() || ((ImageView) findViewById(R.id.content_thumbnail)).getDrawable() == null) ? false : true;
    }

    private class ClickListener implements View.OnClickListener {
        private ClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (System.currentTimeMillis() - ContentPallet.this.curTime <= 3000 || ContentPallet.this.mContent == null || ContentPallet.this.mThumbnailStateListener == null) {
                return;
            }
            ContentPallet.this.mThumbnailStateListener.onThumbnailClicked(ContentPallet.this.mContent);
        }
    }
}
