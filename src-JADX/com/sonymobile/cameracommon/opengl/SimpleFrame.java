package com.sonymobile.cameracommon.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.View;
import com.sonyericsson.android.camera.util.CamLog;
import java.nio.FloatBuffer;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SimpleFrame extends FrameBase {
    public static final String TAG = "SimpleFrame";
    private float[] mColor;
    private int mSimpleColorInGLSL;

    public SimpleFrame(Context context, View view) {
        super(context, view);
        this.mSimpleColorInGLSL = 0;
        this.mColor = new float[3];
    }

    public void setColor(float f, float f2, float f3, float f4) {
        this.mColor[0] = f;
        this.mColor[1] = f2;
        this.mColor[2] = f3;
        this.mAlpha = f4;
    }

    @Override // com.sonymobile.cameracommon.opengl.RenderBase
    public void release() {
        super.release();
        finalizeShaderProgram();
    }

    @Override // com.sonymobile.cameracommon.opengl.FrameBase
    protected void doRender() {
        GLES20.glBindBuffer(34962, this.mVertexBuffers[0]);
        GLES20.glVertexAttribPointer(this.mVertexInGLSL, 3, 5126, false, 0, 0);
        GLES20.glBindBuffer(34962, 0);
        GLES20.glUniform4f(this.mSimpleColorInGLSL, this.mColor[0], this.mColor[1], this.mColor[2], this.mAlpha);
        if (ExtendedGlSurfaceView.isGlErrorOccured()) {
            CamLog.e("clearAndResetSurface():[Texture binder Error]");
        }
        float[] fArr = new float[16];
        Matrix.setIdentityM(fArr, 0);
        Matrix.multiplyMM(fArr, 0, this.mSequencedLocalMatrix, 0, fArr, 0);
        Matrix.multiplyMM(fArr, 0, this.mGlobalMatrix, 0, fArr, 0);
        GLES20.glUniformMatrix4fv(this.mMvpMatrixInGLSL, 1, false, fArr, 0);
        GLES20.glDrawArrays(5, 0, 4);
        if (ExtendedGlSurfaceView.isGlErrorOccured()) {
            CamLog.e("clearAndResetSurface():[Draw frame Error]");
        }
    }

    @Override // com.sonymobile.cameracommon.opengl.FrameBase
    protected void initializeShaderProgram() throws OpenGlException {
        this.mSimpleColorInGLSL = GLES20.glGetUniformLocation(this.mShaderProgram, ShaderProgramFactory.GLSL_FIELD_ID_SIMPLE_COLOR);
        ExtendedGlSurfaceView.checkGlErrorWithException();
        super.initializeShaderProgram();
    }

    @Override // com.sonymobile.cameracommon.opengl.FrameBase
    public void updateVertexBuffer(float[] fArr) {
        FloatBuffer floatBufferAllocFloatBuffer = ExtendedGlSurfaceView.allocFloatBuffer(fArr);
        GLES20.glGenBuffers(this.mVertexBuffers.length, this.mVertexBuffers, 0);
        GLES20.glBindBuffer(34962, this.mVertexBuffers[0]);
        GLES20.glBufferData(34962, 4 * floatBufferAllocFloatBuffer.limit(), floatBufferAllocFloatBuffer, 35048);
        GLES20.glBindBuffer(34962, 0);
    }
}
