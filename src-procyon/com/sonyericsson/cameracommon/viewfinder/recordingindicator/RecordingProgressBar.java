// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.viewfinder.recordingindicator;

import android.content.res.Resources$Theme;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class RecordingProgressBar extends ImageView
{
    public static final String TAG = "RecordingProgressBar";
    private int mProgressBarWidth;
    private Drawable mProgressIcon;
    private int mProgressRatio;
    
    public RecordingProgressBar(final Context context) {
        super(context);
        this.mProgressRatio = 0;
        this.mProgressBarWidth = 0;
    }
    
    public RecordingProgressBar(final Context context, final AttributeSet set) {
        super(context, set);
        this.mProgressRatio = 0;
        this.mProgressBarWidth = 0;
    }
    
    public RecordingProgressBar(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mProgressRatio = 0;
        this.mProgressBarWidth = 0;
    }
    
    public int getProgress() {
        return this.mProgressRatio;
    }
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        final int n = this.getPaddingTop() + this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
        if (CommonUtility.isMirroringRequired(this.getContext())) {
            this.mProgressIcon.setBounds(this.mProgressBarWidth - this.getPaddingRight() - this.mProgressRatio, this.getPaddingTop(), this.mProgressBarWidth - this.getPaddingRight(), n);
        }
        else {
            this.mProgressIcon.setBounds(this.getPaddingLeft(), this.getPaddingTop(), this.getPaddingLeft() + this.mProgressRatio, n);
        }
        this.mProgressIcon.draw(canvas);
    }
    
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mProgressIcon = this.getResources().getDrawable(2131231418, (Resources$Theme)null);
        this.mProgressBarWidth = this.getResources().getDimensionPixelSize(2131165513);
    }
    
    public void setProgress(int mProgressRatio, final int n) {
        if (n != 0) {
            mProgressRatio = (int)(mProgressRatio / (double)n * (this.mProgressBarWidth - this.getPaddingLeft() - this.getPaddingRight()));
        }
        else {
            mProgressRatio = 0;
        }
        this.mProgressRatio = mProgressRatio;
        this.invalidate();
    }
}
