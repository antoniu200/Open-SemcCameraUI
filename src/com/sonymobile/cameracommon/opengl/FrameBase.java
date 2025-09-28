package com.sonymobile.cameracommon.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.view.View;
import com.sonyericsson.android.camera.util.CamLog;
import java.nio.FloatBuffer;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class FrameBase extends RenderBase implements AlphaBlendable {
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

    protected abstract void doRender();

    protected FrameBase(Context context, View view) {
        super(context, view);
        this.mShaderProgram = 0;
        this.mVertexBuffers = new int[1];
        this.mTexCoordBuffers = new int[1];
        this.mAlpha = 1.0f;
    }

    public void setShaderProgram(int i) {
        this.mShaderProgram = i;
        try {
            initializeShaderProgram();
        } catch (OpenGlException e) {
            CamLog.e("OpenGL initialize Error.", e);
        }
    }

    protected void initializeShaderProgram() throws OpenGlException {
        this.mVertexInGLSL = GLES20.glGetAttribLocation(this.mShaderProgram, ShaderProgramFactory.GLSL_FIELD_ID_VERTEX);
        ExtendedGlSurfaceView.checkGlErrorWithException();
        this.mTexCoordInGLSL = GLES20.glGetAttribLocation(this.mShaderProgram, ShaderProgramFactory.GLSL_FIELD_ID_TEXTURE_COORD);
        ExtendedGlSurfaceView.checkGlErrorWithException();
        checkAndBindAttriLocation();
        this.mMvpMatrixInGLSL = GLES20.glGetUniformLocation(this.mShaderProgram, ShaderProgramFactory.GLSL_FIELD_ID_VERTEX_MVP_MATRIX);
        ExtendedGlSurfaceView.checkGlErrorWithException();
        int[] iArr = new int[1];
        GLES20.glGetProgramiv(this.mShaderProgram, 35714, iArr, 0);
        ExtendedGlSurfaceView.checkGlErrorWithException();
        if (iArr[0] == 0) {
            CamLog.e("TimeShiftSlider.initializeYuv2RgbShader():[Program link Error]");
            throw new OpenGlException("TimeShiftSlider.initializeYuv2RgbShader():[Program link Error]");
        }
        initializeVertexAndTextureCoordinatesBuffer();
    }

    private void checkAndBindAttriLocation() throws OpenGlException {
        if (this.mVertexInGLSL == -1 || this.mTexCoordInGLSL == -1) {
            CamLog.e("checkAndBindAttriLocation: mVertexInGLSL = " + this.mVertexInGLSL + "mTexCoordInGLSL = " + this.mTexCoordInGLSL);
            this.mVertexInGLSL = 0;
            GLES20.glBindAttribLocation(this.mShaderProgram, this.mVertexInGLSL, ShaderProgramFactory.GLSL_FIELD_ID_VERTEX);
            ExtendedGlSurfaceView.checkGlErrorWithException();
            this.mTexCoordInGLSL = 1;
            GLES20.glBindAttribLocation(this.mShaderProgram, this.mTexCoordInGLSL, ShaderProgramFactory.GLSL_FIELD_ID_TEXTURE_COORD);
            ExtendedGlSurfaceView.checkGlErrorWithException();
            GLES20.glLinkProgram(this.mShaderProgram);
            ExtendedGlSurfaceView.checkGlErrorWithException();
        }
    }

    protected void initializeVertexAndTextureCoordinatesBuffer() {
        float[] fArr = {getWidthNorm() * (-1.0f), getHeightNorm() * 1.0f, 0.0f, getWidthNorm() * (-1.0f), getHeightNorm() * (-1.0f), 0.0f, getWidthNorm() * 1.0f, getHeightNorm() * 1.0f, 0.0f, 1.0f * getWidthNorm(), (-1.0f) * getHeightNorm(), 0.0f};
        GLES20.glGenBuffers(this.mVertexBuffers.length, this.mVertexBuffers, 0);
        GLES20.glGenBuffers(this.mTexCoordBuffers.length, this.mTexCoordBuffers, 0);
        updateVertexBuffer(fArr);
        updateTextureBuffer(new float[]{0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f});
    }

    public void updateVertexBuffer(float[] fArr) {
        FloatBuffer floatBufferAllocFloatBuffer = ExtendedGlSurfaceView.allocFloatBuffer(fArr);
        GLES20.glBindBuffer(34962, this.mVertexBuffers[0]);
        GLES20.glBufferData(34962, 4 * floatBufferAllocFloatBuffer.limit(), floatBufferAllocFloatBuffer, 35048);
        GLES20.glBindBuffer(34962, 0);
    }

    public void updateTextureBuffer(float[] fArr) {
        FloatBuffer floatBufferAllocFloatBuffer = ExtendedGlSurfaceView.allocFloatBuffer(fArr);
        GLES20.glBindBuffer(34962, this.mTexCoordBuffers[0]);
        GLES20.glBufferData(34962, 4 * floatBufferAllocFloatBuffer.limit(), floatBufferAllocFloatBuffer, 35048);
        GLES20.glBindBuffer(34962, 0);
    }

    @Override // com.sonymobile.cameracommon.opengl.RenderBase
    public void render() {
        if (isVisible()) {
            if (!enableLocalFunctions()) {
                CamLog.e("render():[Enable functions failed.]");
                return;
            }
            doRender();
            if (disableLocalFunctions()) {
                return;
            }
            CamLog.e("render():[Disable functions failed.]");
        }
    }

    protected boolean enableLocalFunctions() {
        if (this.mVertexInGLSL != -1) {
            GLES20.glEnableVertexAttribArray(this.mVertexInGLSL);
        }
        if (this.mTexCoordInGLSL != -1) {
            GLES20.glEnableVertexAttribArray(this.mTexCoordInGLSL);
        }
        if (enableShaderProgram()) {
            return true;
        }
        CamLog.e("enableFunctions():[Enable shader program failed.]");
        return false;
    }

    private boolean enableShaderProgram() {
        if (this.mShaderProgram == 0) {
            CamLog.e(".enableYuv2RgbShaderProgram():[Program is Invalid]");
            return false;
        }
        GLES20.glUseProgram(this.mShaderProgram);
        GLES20.glValidateProgram(this.mShaderProgram);
        if (!ExtendedGlSurfaceView.isGlErrorOccured()) {
            return true;
        }
        CamLog.e(".enableYuv2RgbShaderProgram():[Program Error]");
        return false;
    }

    protected boolean disableLocalFunctions() {
        if (this.mVertexInGLSL != -1) {
            GLES20.glDisableVertexAttribArray(this.mVertexInGLSL);
        }
        if (this.mTexCoordInGLSL == -1) {
            return true;
        }
        GLES20.glDisableVertexAttribArray(this.mTexCoordInGLSL);
        return true;
    }

    protected void finalizeShaderProgram() {
        this.mShaderProgram = 0;
        finalizeVertexAndTextureCoordinatesBuffer();
    }

    private void finalizeVertexAndTextureCoordinatesBuffer() {
        GLES20.glDeleteBuffers(this.mVertexBuffers.length, this.mVertexBuffers, 0);
        GLES20.glDeleteBuffers(this.mTexCoordBuffers.length, this.mTexCoordBuffers, 0);
    }

    @Override // com.sonymobile.cameracommon.opengl.AlphaBlendable
    public void setAlpha(float f) {
        this.mAlpha = f;
    }
}
