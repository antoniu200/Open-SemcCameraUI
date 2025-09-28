package com.sonymobile.cameracommon.opengl;

import android.content.Context;
import android.opengl.GLES20;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.util.CamLog;
import java.io.IOException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ShaderProgramFactory {
    public static final String GLSL_FIELD_ID_ALPHA_MASK_TEXTURE = "uAlphaMaskTexture";
    public static final String GLSL_FIELD_ID_ATTRIB_TEXCOORD = "aTexCoord";
    public static final String GLSL_FIELD_ID_ATTRIB_VERTEX = "aVertex";
    public static final String GLSL_FIELD_ID_BLURRED_YUV_FRAME_BLUR_SPREAD = "uBlurSpread";
    public static final String GLSL_FIELD_ID_BLURRED_YUV_FRAME_CENTER_WEIGHT = "uCenterWeight";
    public static final String GLSL_FIELD_ID_BLURRED_YUV_FRAME_LOD = "uLod";
    public static final String GLSL_FIELD_ID_BLURRED_YUV_FRAME_SPREAD_OFFSET = "uSpreadOffset";
    public static final String GLSL_FIELD_ID_BLURRED_YUV_FRAME_TEXTURE_HEIGHT = "uTextureHeight";
    public static final String GLSL_FIELD_ID_BLURRED_YUV_FRAME_TEXTURE_WIDTH = "uTextureWidth";
    public static final String GLSL_FIELD_ID_MASK_TEXTURE_COORD = "aMaskTexCoord";
    public static final String GLSL_FIELD_ID_SAMPLER2D_TEXTURE = "sTexture";
    public static final String GLSL_FIELD_ID_SIMPLE_COLOR = "simpleColor";
    public static final String GLSL_FIELD_ID_TEXTURE_COORD = "texCoord";
    public static final String GLSL_FIELD_ID_TEXTURE_RGB = "uTextureRgb";
    public static final String GLSL_FIELD_ID_TEXTURE_U = "textureU";
    public static final String GLSL_FIELD_ID_TEXTURE_V = "textureV";
    public static final String GLSL_FIELD_ID_TEXTURE_VU = "textureVu";
    public static final String GLSL_FIELD_ID_TEXTURE_Y = "textureY";
    public static final String GLSL_FIELD_ID_UNIFORM_ALPHA = "uAlpha";
    public static final String GLSL_FIELD_ID_UNIFORM_MVPMATRIX = "uMvpMatrix";
    public static final String GLSL_FIELD_ID_VERTEX = "vertex";
    public static final String GLSL_FIELD_ID_VERTEX_ALPHA = "vertexAlpha";
    public static final String GLSL_FIELD_ID_VERTEX_MVP_MATRIX = "mvpMatrix";
    public static final String TAG = "ShaderProgramFactory";

    public static int createYuvFrameShaderProgram(Context context) throws OpenGlException {
        return createShaderProgram(context, new int[]{R.raw.opengl_yuv_frame_vertex}, new int[]{R.raw.opengl_yuv_frame_fragment});
    }

    public static int createVertexAlphYuvFrameShaderProgram(Context context) throws OpenGlException {
        return createShaderProgram(context, new int[]{R.raw.opengl_vertex_alpha_yuv_frame_vertex}, new int[]{R.raw.opengl_vertex_alpha_yuv_frame_fragment});
    }

    public static int createRgbFrameShaderProgram(Context context) throws OpenGlException {
        return createShaderProgram(context, new int[]{R.raw.opengl_rgb_frame_vertex}, new int[]{R.raw.opengl_rgb_frame_fragment});
    }

    public static int createSimpleFrameShaderProgram(Context context) throws OpenGlException {
        return createShaderProgram(context, new int[]{R.raw.opengl_simpleframe_vertex}, new int[]{R.raw.opengl_simpleframe_fragment});
    }

    public static int createCopyFrameShaderProgram(Context context) throws OpenGlException {
        return createShaderProgram(context, new int[]{R.raw.opengl_copyframe_vertex}, new int[]{R.raw.opengl_copyframe_fragment});
    }

    public static int createBlurredYuvFrameShaderProgram(Context context) throws OpenGlException {
        return createShaderProgram(context, new int[]{R.raw.opengl_blurred_yuv_frame_vertex}, new int[]{R.raw.opengl_blurred_yuv_frame_fragment});
    }

    public static int createAlphaMaskedYuvFrameShaderProgram(Context context) throws OpenGlException {
        return createShaderProgram(context, new int[]{R.raw.opengl_alpha_masked_yuv_frame_vertex}, new int[]{R.raw.opengl_alpha_masked_yuv_frame_fragment});
    }

    public static int createAlphaMaskedBlurredYuvFrameShaderProgram(Context context) throws OpenGlException {
        return createShaderProgram(context, new int[]{R.raw.opengl_alpha_masked_blurred_yuv_frame_vertex}, new int[]{R.raw.opengl_alpha_masked_blurred_yuv_frame_fragment});
    }

    public static int createShaderProgramFromClientApplicationContext(Context context, int i, int i2) {
        return createShaderProgram(context, new int[]{i}, new int[]{i2});
    }

    public static int createShaderProgramFromClientApplicationContext(Context context, int[] iArr, int[] iArr2) {
        return createShaderProgram(context, iArr, iArr2);
    }

    private static int createShaderProgram(Context context, int[] iArr, int[] iArr2) throws IOException {
        int iGlCreateShader;
        int iGlCreateShader2;
        int[] iArr3 = new int[1];
        int i = 0;
        try {
            String strLoadShaderSourceCodesFrom = ExtendedGlSurfaceView.loadShaderSourceCodesFrom(context, iArr);
            iGlCreateShader2 = GLES20.glCreateShader(35633);
            try {
                ExtendedGlSurfaceView.checkGlErrorWithException();
                GLES20.glShaderSource(iGlCreateShader2, strLoadShaderSourceCodesFrom);
                ExtendedGlSurfaceView.checkGlErrorWithException();
                GLES20.glCompileShader(iGlCreateShader2);
                ExtendedGlSurfaceView.checkGlErrorWithException();
                GLES20.glGetShaderiv(iGlCreateShader2, 35713, iArr3, 0);
                ExtendedGlSurfaceView.checkGlErrorWithException();
                if (iArr3[0] == 0) {
                    CamLog.e("ShaderProgramFactory.createShaderProgram():[VS Compile Error]");
                    CamLog.e(GLES20.glGetShaderInfoLog(iGlCreateShader2));
                    throw new OpenGlException("ShaderProgramFactory.createShaderProgram():[VS Compile Error]");
                }
                String strLoadShaderSourceCodesFrom2 = ExtendedGlSurfaceView.loadShaderSourceCodesFrom(context, iArr2);
                iGlCreateShader = GLES20.glCreateShader(35632);
                try {
                    ExtendedGlSurfaceView.checkGlErrorWithException();
                    GLES20.glShaderSource(iGlCreateShader, strLoadShaderSourceCodesFrom2);
                    ExtendedGlSurfaceView.checkGlErrorWithException();
                    GLES20.glCompileShader(iGlCreateShader);
                    ExtendedGlSurfaceView.checkGlErrorWithException();
                    GLES20.glGetShaderiv(iGlCreateShader, 35713, iArr3, 0);
                    ExtendedGlSurfaceView.checkGlErrorWithException();
                    if (iArr3[0] == 0) {
                        CamLog.e("ShaderProgramFactory.createShaderProgram():[FS Compile Error]");
                        CamLog.e(GLES20.glGetShaderInfoLog(iGlCreateShader));
                        throw new OpenGlException("ShaderProgramFactory.createShaderProgram():[FS Compile Error]");
                    }
                    int iGlCreateProgram = GLES20.glCreateProgram();
                    try {
                        GLES20.glAttachShader(iGlCreateProgram, iGlCreateShader2);
                        ExtendedGlSurfaceView.checkGlErrorWithException();
                        GLES20.glAttachShader(iGlCreateProgram, iGlCreateShader);
                        ExtendedGlSurfaceView.checkGlErrorWithException();
                        GLES20.glDeleteShader(iGlCreateShader2);
                        ExtendedGlSurfaceView.checkGlErrorWithException();
                        GLES20.glDeleteShader(iGlCreateShader);
                        ExtendedGlSurfaceView.checkGlErrorWithException();
                        GLES20.glLinkProgram(iGlCreateProgram);
                        ExtendedGlSurfaceView.checkGlErrorWithException();
                        if (iArr3[0] != 0) {
                            return iGlCreateProgram;
                        }
                        CamLog.e("ShaderProgramFactory.createShaderProgram():[Program link Error]");
                        throw new OpenGlException("ShaderProgramFactory.createShaderProgram():[Program link Error]");
                    } catch (OpenGlException e) {
                        i = iGlCreateProgram;
                        e = e;
                        CamLog.e("Fail to create ShaderProgram.", e);
                        if (iGlCreateShader2 != 0) {
                            GLES20.glDeleteShader(iGlCreateShader2);
                        }
                        if (iGlCreateShader != 0) {
                            GLES20.glDeleteShader(iGlCreateShader);
                        }
                        deleteShaderProgram(i);
                        throw e;
                    }
                } catch (OpenGlException e2) {
                    e = e2;
                }
            } catch (OpenGlException e3) {
                e = e3;
                iGlCreateShader = 0;
            }
        } catch (OpenGlException e4) {
            e = e4;
            iGlCreateShader = 0;
            iGlCreateShader2 = 0;
        }
    }

    public static void deleteShaderProgram(int i) {
        if (i != 0) {
            GLES20.glDeleteProgram(i);
            if (ExtendedGlSurfaceView.isGlErrorOccured()) {
                CamLog.e("deleteShaderProgram():[Delete Program Error]");
            }
        }
    }
}
