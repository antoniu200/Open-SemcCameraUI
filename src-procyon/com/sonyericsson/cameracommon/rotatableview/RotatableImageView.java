// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.rotatableview;

import android.view.ViewGroup$LayoutParams;
import android.graphics.Matrix;
import android.net.Uri;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView$ScaleType;
import android.content.Context;
import android.widget.ImageView;

public class RotatableImageView extends ImageView
{
    public static final String TAG = "RotatableImageView";
    private int mFixRotation;
    private int mHeight;
    private boolean mPrepared;
    private int mSensorOrientation;
    private int mWidth;
    
    public RotatableImageView(final Context context) {
        super(context);
        this.mSensorOrientation = 2;
        this.mFixRotation = 0;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mPrepared = false;
        this.setScaleType(ImageView$ScaleType.MATRIX);
    }
    
    public RotatableImageView(final Context context, final AttributeSet set) {
        super(context, set);
        this.mSensorOrientation = 2;
        this.mFixRotation = 0;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mPrepared = false;
        this.setScaleType(ImageView$ScaleType.MATRIX);
    }
    
    public RotatableImageView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mSensorOrientation = 2;
        this.mFixRotation = 0;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mPrepared = false;
        this.setScaleType(ImageView$ScaleType.MATRIX);
    }
    
    public void clearFixedRotate(final int n) {
        this.mFixRotation = 0;
        this.update();
    }
    
    public void fixRotation(final int mFixRotation) {
        this.mFixRotation = mFixRotation;
        this.update();
    }
    
    protected boolean isPrepared() {
        return this.mPrepared;
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        if (!this.isPrepared()) {
            this.setWidthHeight(this.getWidth(), this.getHeight());
            this.update();
        }
    }
    
    public void setImageDrawable(final Drawable imageDrawable) {
        super.setImageDrawable(imageDrawable);
        this.update();
    }
    
    public void setImageResource(final int imageResource) {
        super.setImageResource(imageResource);
        this.update();
    }
    
    public void setImageURI(final Uri imageURI) {
        super.setImageURI(imageURI);
        this.update();
    }
    
    public void setSensorOrientation(final int mSensorOrientation) {
        this.mSensorOrientation = mSensorOrientation;
        this.update();
    }
    
    public void setWidthHeight(final int mWidth, final int mHeight) {
        this.mWidth = mWidth;
        this.mHeight = mHeight;
        this.mPrepared = true;
    }
    
    public void update() {
        if (!this.isPrepared()) {
            return;
        }
        final int width = this.getWidth();
        final int height = this.getHeight();
        final Matrix imageMatrix = new Matrix();
        int n = this.mSensorOrientation;
        if (this.mFixRotation != 0) {
            n = this.mFixRotation;
        }
        if (this.getDrawable() != null) {
            imageMatrix.preScale(this.mWidth / (float)this.getDrawable().getIntrinsicWidth(), this.mHeight / (float)this.getDrawable().getIntrinsicHeight());
        }
        int width2;
        int height2;
        if (n == 1) {
            final float n2 = (float)width;
            final float n3 = -n2 / 2.0f;
            final float n4 = (float)height;
            imageMatrix.postTranslate(n3, -n4 / 2.0f);
            imageMatrix.postRotate((float)(-90));
            imageMatrix.postTranslate(n4 / 2.0f, n2 / 2.0f);
            width2 = this.mHeight;
            height2 = this.mWidth;
        }
        else {
            width2 = this.mWidth;
            height2 = this.mHeight;
        }
        this.setImageMatrix(imageMatrix);
        final ViewGroup$LayoutParams layoutParams = this.getLayoutParams();
        layoutParams.height = height2;
        layoutParams.width = width2;
        this.setLayoutParams(layoutParams);
        this.requestLayout();
    }
}
