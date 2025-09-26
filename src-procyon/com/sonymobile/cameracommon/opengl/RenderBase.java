// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.cameracommon.opengl;

import android.opengl.Matrix;
import android.view.View;
import android.content.Context;

public abstract class RenderBase
{
    protected static final int FLOAT_SIZE_IN_BYTE = 4;
    private Context mContext;
    protected float[] mGlobalMatrix;
    private boolean mIsVisible;
    protected View mRootView;
    protected float[] mSequencedLocalMatrix;
    
    protected RenderBase(final Context mContext, final View mRootView) {
        this.mContext = null;
        this.mRootView = null;
        this.mGlobalMatrix = new float[16];
        this.mSequencedLocalMatrix = new float[16];
        this.mIsVisible = true;
        this.mContext = mContext;
        this.mRootView = mRootView;
        Matrix.setIdentityM(this.mGlobalMatrix, 0);
        Matrix.setIdentityM(this.mSequencedLocalMatrix, 0);
    }
    
    protected Context getContext() {
        return this.mContext;
    }
    
    protected float getHeightNorm() {
        if (this.mRootView.getHeight() < this.mRootView.getWidth()) {
            return this.mRootView.getHeight() / (float)this.mRootView.getWidth();
        }
        return 1.0f;
    }
    
    public float[] getLocalGlobalMatrix() {
        final float[] array = new float[16];
        Matrix.setIdentityM(array, 0);
        Matrix.multiplyMM(array, 0, this.mSequencedLocalMatrix, 0, array, 0);
        Matrix.multiplyMM(array, 0, this.mGlobalMatrix, 0, array, 0);
        return array;
    }
    
    protected float getWidthNorm() {
        if (this.mRootView.getHeight() < this.mRootView.getWidth()) {
            return 1.0f;
        }
        return this.mRootView.getHeight() / (float)this.mRootView.getWidth();
    }
    
    public boolean isVisible() {
        return this.mIsVisible;
    }
    
    public void release() {
        this.mContext = null;
        this.mRootView = null;
    }
    
    public abstract void render();
    
    public void rotate(final float n, final float n2, final float n3) {
        ExtendedGlSurfaceView.rotate(this.mSequencedLocalMatrix, n, n2, n3);
    }
    
    public void scale(final float n, final float n2, final float n3) {
        ExtendedGlSurfaceView.scale(this.mSequencedLocalMatrix, n, n2, n3);
    }
    
    public void setGlobalMatrix(final float[] array) {
        this.mGlobalMatrix = array.clone();
        Matrix.setIdentityM(this.mSequencedLocalMatrix, 0);
    }
    
    public void setVisibility(final boolean mIsVisible) {
        this.mIsVisible = mIsVisible;
    }
    
    public void translate(final float n, final float n2, final float n3) {
        ExtendedGlSurfaceView.translate(this.mSequencedLocalMatrix, n, n2, n3);
    }
    
    public void updateRootView(final View mRootView) {
        this.mRootView = mRootView;
    }
}
