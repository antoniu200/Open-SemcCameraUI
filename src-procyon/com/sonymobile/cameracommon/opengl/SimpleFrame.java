// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.cameracommon.opengl;

import java.nio.FloatBuffer;
import java.nio.Buffer;
import android.opengl.Matrix;
import com.sonyericsson.android.camera.util.CamLog;
import android.opengl.GLES20;
import android.view.View;
import android.content.Context;

public class SimpleFrame extends FrameBase
{
    public static final String TAG = "SimpleFrame";
    private float[] mColor;
    private int mSimpleColorInGLSL;
    
    public SimpleFrame(final Context context, final View view) {
        super(context, view);
        this.mSimpleColorInGLSL = 0;
        this.mColor = new float[3];
    }
    
    @Override
    protected void doRender() {
        GLES20.glBindBuffer(34962, this.mVertexBuffers[0]);
        GLES20.glVertexAttribPointer(this.mVertexInGLSL, 3, 5126, false, 0, 0);
        GLES20.glBindBuffer(34962, 0);
        GLES20.glUniform4f(this.mSimpleColorInGLSL, this.mColor[0], this.mColor[1], this.mColor[2], this.mAlpha);
        if (ExtendedGlSurfaceView.isGlErrorOccured()) {
            CamLog.e("clearAndResetSurface():[Texture binder Error]");
        }
        final float[] array = new float[16];
        Matrix.setIdentityM(array, 0);
        Matrix.multiplyMM(array, 0, this.mSequencedLocalMatrix, 0, array, 0);
        Matrix.multiplyMM(array, 0, this.mGlobalMatrix, 0, array, 0);
        GLES20.glUniformMatrix4fv(this.mMvpMatrixInGLSL, 1, false, array, 0);
        GLES20.glDrawArrays(5, 0, 4);
        if (ExtendedGlSurfaceView.isGlErrorOccured()) {
            CamLog.e("clearAndResetSurface():[Draw frame Error]");
        }
    }
    
    @Override
    protected void initializeShaderProgram() throws OpenGlException {
        this.mSimpleColorInGLSL = GLES20.glGetUniformLocation(this.mShaderProgram, "simpleColor");
        ExtendedGlSurfaceView.checkGlErrorWithException();
        super.initializeShaderProgram();
    }
    
    @Override
    public void release() {
        super.release();
        this.finalizeShaderProgram();
    }
    
    public void setColor(final float n, final float n2, final float n3, final float mAlpha) {
        this.mColor[0] = n;
        this.mColor[1] = n2;
        this.mColor[2] = n3;
        this.mAlpha = mAlpha;
    }
    
    @Override
    public void updateVertexBuffer(final float[] array) {
        final FloatBuffer allocFloatBuffer = ExtendedGlSurfaceView.allocFloatBuffer(array);
        GLES20.glGenBuffers(this.mVertexBuffers.length, this.mVertexBuffers, 0);
        GLES20.glBindBuffer(34962, this.mVertexBuffers[0]);
        GLES20.glBufferData(34962, 4 * allocFloatBuffer.limit(), (Buffer)allocFloatBuffer, 35048);
        GLES20.glBindBuffer(34962, 0);
    }
}
