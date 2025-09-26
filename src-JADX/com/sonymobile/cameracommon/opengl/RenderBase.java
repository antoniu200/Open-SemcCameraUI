package com.sonymobile.cameracommon.opengl;

import android.content.Context;
import android.opengl.Matrix;
import android.view.View;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class RenderBase {
    protected static final int FLOAT_SIZE_IN_BYTE = 4;
    private Context mContext;
    protected View mRootView;
    protected float[] mGlobalMatrix = new float[16];
    protected float[] mSequencedLocalMatrix = new float[16];
    private boolean mIsVisible = true;

    public abstract void render();

    protected RenderBase(Context context, View view) {
        this.mContext = null;
        this.mRootView = null;
        this.mContext = context;
        this.mRootView = view;
        Matrix.setIdentityM(this.mGlobalMatrix, 0);
        Matrix.setIdentityM(this.mSequencedLocalMatrix, 0);
    }

    public void updateRootView(View view) {
        this.mRootView = view;
    }

    protected Context getContext() {
        return this.mContext;
    }

    protected float getWidthNorm() {
        if (this.mRootView.getHeight() < this.mRootView.getWidth()) {
            return 1.0f;
        }
        return this.mRootView.getHeight() / this.mRootView.getWidth();
    }

    protected float getHeightNorm() {
        if (this.mRootView.getHeight() < this.mRootView.getWidth()) {
            return this.mRootView.getHeight() / this.mRootView.getWidth();
        }
        return 1.0f;
    }

    public void setGlobalMatrix(float[] fArr) {
        this.mGlobalMatrix = (float[]) fArr.clone();
        Matrix.setIdentityM(this.mSequencedLocalMatrix, 0);
    }

    public float[] getLocalGlobalMatrix() {
        float[] fArr = new float[16];
        Matrix.setIdentityM(fArr, 0);
        Matrix.multiplyMM(fArr, 0, this.mSequencedLocalMatrix, 0, fArr, 0);
        Matrix.multiplyMM(fArr, 0, this.mGlobalMatrix, 0, fArr, 0);
        return fArr;
    }

    public void setVisibility(boolean z) {
        this.mIsVisible = z;
    }

    public boolean isVisible() {
        return this.mIsVisible;
    }

    public void release() {
        this.mContext = null;
        this.mRootView = null;
    }

    public void translate(float f, float f2, float f3) {
        ExtendedGlSurfaceView.translate(this.mSequencedLocalMatrix, f, f2, f3);
    }

    public void rotate(float f, float f2, float f3) {
        ExtendedGlSurfaceView.rotate(this.mSequencedLocalMatrix, f, f2, f3);
    }

    public void scale(float f, float f2, float f3) {
        ExtendedGlSurfaceView.scale(this.mSequencedLocalMatrix, f, f2, f3);
    }
}
