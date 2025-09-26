// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.tutorial;

import android.graphics.Matrix;
import android.util.AttributeSet;
import android.content.Context;
import android.view.TextureView;

public class TutorialVideoView extends TextureView
{
    private float mTextureAspectRatio;
    private float mVideoAspectRatio;
    
    public TutorialVideoView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        this.mTextureAspectRatio = this.getHeight() / (float)this.getWidth();
    }
    
    public void setVideoAspectRatio(final float mVideoAspectRatio) {
        this.mVideoAspectRatio = mVideoAspectRatio;
    }
    
    public void updateScale() {
        final float mVideoAspectRatio = this.mVideoAspectRatio;
        final float mTextureAspectRatio = this.mTextureAspectRatio;
        float n = 1.0f;
        float n2;
        if (mVideoAspectRatio > mTextureAspectRatio) {
            n = this.mVideoAspectRatio / this.mTextureAspectRatio;
            n2 = 1.0f;
        }
        else {
            n2 = this.mTextureAspectRatio / this.mVideoAspectRatio;
        }
        final int n3 = this.getWidth() / 2;
        final int n4 = this.getHeight() / 2;
        final Matrix transform = new Matrix();
        transform.postScale(n2, n, (float)n3, (float)n4);
        this.setTransform(transform);
    }
}
