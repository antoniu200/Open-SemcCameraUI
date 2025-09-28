package com.sonyericsson.android.camera.view.tutorial;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.TextureView;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TutorialVideoView extends TextureView {
    private float mTextureAspectRatio;
    private float mVideoAspectRatio;

    public TutorialVideoView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setVideoAspectRatio(float f) {
        this.mVideoAspectRatio = f;
    }

    public void updateScale() {
        float f;
        float f2 = 1.0f;
        if (this.mVideoAspectRatio > this.mTextureAspectRatio) {
            f2 = this.mVideoAspectRatio / this.mTextureAspectRatio;
            f = 1.0f;
        } else {
            f = this.mTextureAspectRatio / this.mVideoAspectRatio;
        }
        int width = getWidth() / 2;
        int height = getHeight() / 2;
        Matrix matrix = new Matrix();
        matrix.postScale(f, f2, width, height);
        setTransform(matrix);
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mTextureAspectRatio = getHeight() / getWidth();
    }
}
