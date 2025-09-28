package com.sonyericsson.cameracommon.rotatableview;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class RotatableImageView extends ImageView {
    public static final String TAG = "RotatableImageView";
    private int mFixRotation;
    private int mHeight;
    private boolean mPrepared;
    private int mSensorOrientation;
    private int mWidth;

    public RotatableImageView(Context context) {
        super(context);
        this.mSensorOrientation = 2;
        this.mFixRotation = 0;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mPrepared = false;
        setScaleType(ImageView.ScaleType.MATRIX);
    }

    public RotatableImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mSensorOrientation = 2;
        this.mFixRotation = 0;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mPrepared = false;
        setScaleType(ImageView.ScaleType.MATRIX);
    }

    public RotatableImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mSensorOrientation = 2;
        this.mFixRotation = 0;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mPrepared = false;
        setScaleType(ImageView.ScaleType.MATRIX);
    }

    public void setWidthHeight(int i, int i2) {
        this.mWidth = i;
        this.mHeight = i2;
        this.mPrepared = true;
    }

    protected boolean isPrepared() {
        return this.mPrepared;
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (isPrepared()) {
            return;
        }
        setWidthHeight(getWidth(), getHeight());
        update();
    }

    public void setSensorOrientation(int i) {
        this.mSensorOrientation = i;
        update();
    }

    public void update() {
        int i;
        int i2;
        if (isPrepared()) {
            int width = getWidth();
            int height = getHeight();
            Matrix matrix = new Matrix();
            int i3 = this.mSensorOrientation;
            if (this.mFixRotation != 0) {
                i3 = this.mFixRotation;
            }
            if (getDrawable() != null) {
                matrix.preScale(this.mWidth / getDrawable().getIntrinsicWidth(), this.mHeight / getDrawable().getIntrinsicHeight());
            }
            if (i3 == 1) {
                float f = width;
                float f2 = height;
                matrix.postTranslate((-f) / 2.0f, (-f2) / 2.0f);
                matrix.postRotate(-90);
                matrix.postTranslate(f2 / 2.0f, f / 2.0f);
                i = this.mHeight;
                i2 = this.mWidth;
            } else {
                i = this.mWidth;
                i2 = this.mHeight;
            }
            setImageMatrix(matrix);
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.height = i2;
            layoutParams.width = i;
            setLayoutParams(layoutParams);
            requestLayout();
        }
    }

    @Override // android.widget.ImageView
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        update();
    }

    @Override // android.widget.ImageView
    public void setImageResource(int i) {
        super.setImageResource(i);
        update();
    }

    @Override // android.widget.ImageView
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        update();
    }

    public void fixRotation(int i) {
        this.mFixRotation = i;
        update();
    }

    public void clearFixedRotate(int i) {
        this.mFixRotation = 0;
        update();
    }
}
