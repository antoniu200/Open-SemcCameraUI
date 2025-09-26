// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.cameracommon.opengl;

import java.nio.FloatBuffer;
import java.nio.Buffer;
import android.opengl.GLES20;
import com.sonyericsson.android.camera.util.CamLog;
import android.view.View;
import android.content.Context;

public abstract class FrameBase extends RenderBase implements AlphaBlendable
{
    private static final int DEFAULT_TEXCOORD_INDEX = 1;
    private static final int DEFAULT_VERTEX_INDEX = 0;
    private static final int INVALID_INDEX_IN_GLSL = -1;
    public static final String TAG = "FrameBase";
    protected float mAlpha;
    protected int mMvpMatrixInGLSL;
    protected int mShaderProgram;
    protected int[] mTexCoordBuffers;
    protected int mTexCoordInGLSL;
    protected int[] mVertexBuffers;
    protected int mVertexInGLSL;
    
    protected FrameBase(final Context context, final View view) {
        super(context, view);
        this.mShaderProgram = 0;
        this.mVertexBuffers = new int[1];
        this.mTexCoordBuffers = new int[1];
        this.mAlpha = 1.0f;
    }
    
    private void checkAndBindAttriLocation() {
        if (this.mVertexInGLSL == -1 || this.mTexCoordInGLSL == -1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("checkAndBindAttriLocation: mVertexInGLSL = ");
            sb.append(this.mVertexInGLSL);
            sb.append("mTexCoordInGLSL = ");
            sb.append(this.mTexCoordInGLSL);
            CamLog.e(sb.toString());
            this.mVertexInGLSL = 0;
            GLES20.glBindAttribLocation(this.mShaderProgram, this.mVertexInGLSL, "vertex");
            ExtendedGlSurfaceView.checkGlErrorWithException();
            this.mTexCoordInGLSL = 1;
            GLES20.glBindAttribLocation(this.mShaderProgram, this.mTexCoordInGLSL, "texCoord");
            ExtendedGlSurfaceView.checkGlErrorWithException();
            GLES20.glLinkProgram(this.mShaderProgram);
            ExtendedGlSurfaceView.checkGlErrorWithException();
        }
    }
    
    private boolean enableShaderProgram() {
        if (this.mShaderProgram == 0) {
            CamLog.e(".enableYuv2RgbShaderProgram():[Program is Invalid]");
            return false;
        }
        GLES20.glUseProgram(this.mShaderProgram);
        GLES20.glValidateProgram(this.mShaderProgram);
        if (ExtendedGlSurfaceView.isGlErrorOccured()) {
            CamLog.e(".enableYuv2RgbShaderProgram():[Program Error]");
            return false;
        }
        return true;
    }
    
    private void finalizeVertexAndTextureCoordinatesBuffer() {
        GLES20.glDeleteBuffers(this.mVertexBuffers.length, this.mVertexBuffers, 0);
        GLES20.glDeleteBuffers(this.mTexCoordBuffers.length, this.mTexCoordBuffers, 0);
    }
    
    protected boolean disableLocalFunctions() {
        if (this.mVertexInGLSL != -1) {
            GLES20.glDisableVertexAttribArray(this.mVertexInGLSL);
        }
        if (this.mTexCoordInGLSL != -1) {
            GLES20.glDisableVertexAttribArray(this.mTexCoordInGLSL);
        }
        return true;
    }
    
    protected abstract void doRender();
    
    protected boolean enableLocalFunctions() {
        if (this.mVertexInGLSL != -1) {
            GLES20.glEnableVertexAttribArray(this.mVertexInGLSL);
        }
        if (this.mTexCoordInGLSL != -1) {
            GLES20.glEnableVertexAttribArray(this.mTexCoordInGLSL);
        }
        if (!this.enableShaderProgram()) {
            CamLog.e("enableFunctions():[Enable shader program failed.]");
            return false;
        }
        return true;
    }
    
    protected void finalizeShaderProgram() {
        this.mShaderProgram = 0;
        this.finalizeVertexAndTextureCoordinatesBuffer();
    }
    
    protected void initializeShaderProgram() throws OpenGlException {
        this.mVertexInGLSL = GLES20.glGetAttribLocation(this.mShaderProgram, "vertex");
        ExtendedGlSurfaceView.checkGlErrorWithException();
        this.mTexCoordInGLSL = GLES20.glGetAttribLocation(this.mShaderProgram, "texCoord");
        ExtendedGlSurfaceView.checkGlErrorWithException();
        this.checkAndBindAttriLocation();
        this.mMvpMatrixInGLSL = GLES20.glGetUniformLocation(this.mShaderProgram, "mvpMatrix");
        ExtendedGlSurfaceView.checkGlErrorWithException();
        final int[] array = { 0 };
        GLES20.glGetProgramiv(this.mShaderProgram, 35714, array, 0);
        ExtendedGlSurfaceView.checkGlErrorWithException();
        if (array[0] == 0) {
            CamLog.e("TimeShiftSlider.initializeYuv2RgbShader():[Program link Error]");
            throw new OpenGlException("TimeShiftSlider.initializeYuv2RgbShader():[Program link Error]");
        }
        this.initializeVertexAndTextureCoordinatesBuffer();
    }
    
    protected void initializeVertexAndTextureCoordinatesBuffer() {
        final float widthNorm = this.getWidthNorm();
        final float heightNorm = this.getHeightNorm();
        final float widthNorm2 = this.getWidthNorm();
        final float heightNorm2 = this.getHeightNorm();
        final float widthNorm3 = this.getWidthNorm();
        final float heightNorm3 = this.getHeightNorm();
        final float widthNorm4 = this.getWidthNorm();
        final float heightNorm4 = this.getHeightNorm();
        GLES20.glGenBuffers(this.mVertexBuffers.length, this.mVertexBuffers, 0);
        GLES20.glGenBuffers(this.mTexCoordBuffers.length, this.mTexCoordBuffers, 0);
        this.updateVertexBuffer(new float[] { widthNorm * -1.0f, heightNorm * 1.0f, 0.0f, widthNorm2 * -1.0f, heightNorm2 * -1.0f, 0.0f, widthNorm3 * 1.0f, heightNorm3 * 1.0f, 0.0f, 1.0f * widthNorm4, -1.0f * heightNorm4, 0.0f });
        this.updateTextureBuffer(new float[] { 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f });
    }
    
    @Override
    public void render() {
        if (!this.isVisible()) {
            return;
        }
        if (!this.enableLocalFunctions()) {
            CamLog.e("render():[Enable functions failed.]");
            return;
        }
        this.doRender();
        if (!this.disableLocalFunctions()) {
            CamLog.e("render():[Disable functions failed.]");
        }
    }
    
    @Override
    public void setAlpha(final float mAlpha) {
        this.mAlpha = mAlpha;
    }
    
    public void setShaderProgram(final int mShaderProgram) {
        this.mShaderProgram = mShaderProgram;
        try {
            this.initializeShaderProgram();
        }
        catch (final OpenGlException ex) {
            CamLog.e("OpenGL initialize Error.", ex);
        }
    }
    
    public void updateTextureBuffer(final float[] array) {
        final FloatBuffer allocFloatBuffer = ExtendedGlSurfaceView.allocFloatBuffer(array);
        GLES20.glBindBuffer(34962, this.mTexCoordBuffers[0]);
        GLES20.glBufferData(34962, 4 * allocFloatBuffer.limit(), (Buffer)allocFloatBuffer, 35048);
        GLES20.glBindBuffer(34962, 0);
    }
    
    public void updateVertexBuffer(final float[] array) {
        final FloatBuffer allocFloatBuffer = ExtendedGlSurfaceView.allocFloatBuffer(array);
        GLES20.glBindBuffer(34962, this.mVertexBuffers[0]);
        GLES20.glBufferData(34962, 4 * allocFloatBuffer.limit(), (Buffer)allocFloatBuffer, 35048);
        GLES20.glBindBuffer(34962, 0);
    }
}
