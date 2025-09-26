// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.rotatableview;

import android.widget.RelativeLayout$LayoutParams;
import android.widget.ImageView$ScaleType;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class RotatableTextViewInLayout extends RelativeLayout
{
    private static final int ROTATE_DEGREE = -90;
    public static final String TAG = "RotatableTextViewInLayout";
    private ImageView mBackground;
    private int mLayoutHeight;
    private int mLayoutWidth;
    private int mSensorOrientation;
    private TextView mText;
    private int mTextViewHeightOnLandscape;
    private int mTextViewHeightOnPortrait;
    private int mTextViewWidthOnLandscape;
    private int mTextViewWidthOnPortrait;
    
    public RotatableTextViewInLayout(final Context context, final AttributeSet set) {
        super(context, set);
        this.mText = null;
        this.mBackground = null;
        this.mSensorOrientation = 2;
        this.mLayoutWidth = 0;
        this.mLayoutHeight = 0;
        this.mTextViewWidthOnLandscape = 0;
        this.mTextViewHeightOnLandscape = 0;
        this.mTextViewWidthOnPortrait = 0;
        this.mTextViewHeightOnPortrait = 0;
    }
    
    protected void dispatchDraw(final Canvas canvas) {
        if (this.mSensorOrientation == 1) {
            canvas.rotate(-90.0f, this.getWidth() / 2.0f, this.getHeight() / 2.0f);
        }
        super.dispatchDraw(canvas);
    }
    
    protected void onFinishInflate() {
        super.onFinishInflate();
        (this.mText = (TextView)this.findViewById(2131296542)).setGravity(17);
        (this.mBackground = (ImageView)this.findViewById(2131296540)).setScaleType(ImageView$ScaleType.FIT_XY);
    }
    
    protected void onSizeChanged(final int mLayoutWidth, final int mLayoutHeight, final int n, final int n2) {
        this.mLayoutWidth = mLayoutWidth;
        this.mLayoutHeight = mLayoutHeight;
        this.updateLayout();
    }
    
    public void setBackgroundDrawableId(final int imageResource) {
        this.mBackground.setImageResource(imageResource);
    }
    
    public void setSensorOrientation(final int mSensorOrientation) {
        this.mSensorOrientation = mSensorOrientation;
        this.updateLayout();
    }
    
    public void setTextResId(final int text) {
        this.mText.setText(text);
    }
    
    public void setTextSizeOnLandscape(final int mTextViewWidthOnLandscape, final int mTextViewHeightOnLandscape) {
        this.mTextViewWidthOnLandscape = mTextViewWidthOnLandscape;
        this.mTextViewHeightOnLandscape = mTextViewHeightOnLandscape;
    }
    
    public void setTextSizeOnPortrait(final int mTextViewWidthOnPortrait, final int mTextViewHeightOnPortrait) {
        this.mTextViewWidthOnPortrait = mTextViewWidthOnPortrait;
        this.mTextViewHeightOnPortrait = mTextViewHeightOnPortrait;
    }
    
    public void updateLayout() {
        ((RelativeLayout$LayoutParams)this.mText.getLayoutParams()).addRule(13);
        ((RelativeLayout$LayoutParams)this.mBackground.getLayoutParams()).addRule(13);
        if (this.mSensorOrientation == 1) {
            this.mText.getLayoutParams().width = this.mTextViewWidthOnPortrait;
            this.mBackground.getLayoutParams().width = this.mLayoutHeight;
            this.mBackground.getLayoutParams().height = this.mTextViewHeightOnPortrait;
            this.scrollTo(-1 * (this.mLayoutWidth / 2 - this.mTextViewHeightOnPortrait / 2), 0);
        }
        else {
            this.mText.getLayoutParams().width = this.mTextViewWidthOnLandscape;
            this.mBackground.getLayoutParams().width = this.mLayoutWidth;
            this.mBackground.getLayoutParams().height = this.mTextViewHeightOnLandscape;
            this.scrollTo(0, -1 * (this.mLayoutHeight / 2 - this.mTextViewHeightOnLandscape / 2));
        }
        this.requestLayout();
        this.invalidate();
    }
}
