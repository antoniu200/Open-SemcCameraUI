// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.cameracommon.opengl;

import com.sonyericsson.android.camera.util.CamLog;
import android.opengl.GLES20;
import android.content.Context;

public class ShaderProgramFactory
{
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
    
    public static int createAlphaMaskedBlurredYuvFrameShaderProgram(final Context context) throws OpenGlException {
        return createShaderProgram(context, new int[] { 2131623940 }, new int[] { 2131623939 });
    }
    
    public static int createAlphaMaskedYuvFrameShaderProgram(final Context context) throws OpenGlException {
        return createShaderProgram(context, new int[] { 2131623942 }, new int[] { 2131623941 });
    }
    
    public static int createBlurredYuvFrameShaderProgram(final Context context) throws OpenGlException {
        return createShaderProgram(context, new int[] { 2131623944 }, new int[] { 2131623943 });
    }
    
    public static int createCopyFrameShaderProgram(final Context context) throws OpenGlException {
        return createShaderProgram(context, new int[] { 2131623946 }, new int[] { 2131623945 });
    }
    
    public static int createRgbFrameShaderProgram(final Context context) throws OpenGlException {
        return createShaderProgram(context, new int[] { 2131623948 }, new int[] { 2131623947 });
    }
    
    private static int createShaderProgram(final Context context, final int[] array, final int[] array2) {
        final int[] array3 = { 0 };
        int n = 0;
        int glCreateShader;
        int glCreateShader2;
        try {
            final String loadShaderSourceCodes = ExtendedGlSurfaceView.loadShaderSourceCodesFrom(context, array);
            glCreateShader = GLES20.glCreateShader(35633);
            try {
                ExtendedGlSurfaceView.checkGlErrorWithException();
                GLES20.glShaderSource(glCreateShader, loadShaderSourceCodes);
                ExtendedGlSurfaceView.checkGlErrorWithException();
                GLES20.glCompileShader(glCreateShader);
                ExtendedGlSurfaceView.checkGlErrorWithException();
                GLES20.glGetShaderiv(glCreateShader, 35713, array3, 0);
                ExtendedGlSurfaceView.checkGlErrorWithException();
                if (array3[0] == 0) {
                    CamLog.e("ShaderProgramFactory.createShaderProgram():[VS Compile Error]");
                    CamLog.e(GLES20.glGetShaderInfoLog(glCreateShader));
                    throw new OpenGlException("ShaderProgramFactory.createShaderProgram():[VS Compile Error]");
                }
                final String loadShaderSourceCodes2 = ExtendedGlSurfaceView.loadShaderSourceCodesFrom(context, array2);
                glCreateShader2 = GLES20.glCreateShader(35632);
                try {
                    ExtendedGlSurfaceView.checkGlErrorWithException();
                    GLES20.glShaderSource(glCreateShader2, loadShaderSourceCodes2);
                    ExtendedGlSurfaceView.checkGlErrorWithException();
                    GLES20.glCompileShader(glCreateShader2);
                    ExtendedGlSurfaceView.checkGlErrorWithException();
                    GLES20.glGetShaderiv(glCreateShader2, 35713, array3, 0);
                    ExtendedGlSurfaceView.checkGlErrorWithException();
                    if (array3[0] == 0) {
                        CamLog.e("ShaderProgramFactory.createShaderProgram():[FS Compile Error]");
                        CamLog.e(GLES20.glGetShaderInfoLog(glCreateShader2));
                        throw new OpenGlException("ShaderProgramFactory.createShaderProgram():[FS Compile Error]");
                    }
                    final int glCreateProgram = GLES20.glCreateProgram();
                    try {
                        GLES20.glAttachShader(glCreateProgram, glCreateShader);
                        ExtendedGlSurfaceView.checkGlErrorWithException();
                        GLES20.glAttachShader(glCreateProgram, glCreateShader2);
                        ExtendedGlSurfaceView.checkGlErrorWithException();
                        GLES20.glDeleteShader(glCreateShader);
                        ExtendedGlSurfaceView.checkGlErrorWithException();
                        GLES20.glDeleteShader(glCreateShader2);
                        ExtendedGlSurfaceView.checkGlErrorWithException();
                        GLES20.glLinkProgram(glCreateProgram);
                        ExtendedGlSurfaceView.checkGlErrorWithException();
                        if (array3[0] == 0) {
                            CamLog.e("ShaderProgramFactory.createShaderProgram():[Program link Error]");
                            throw new OpenGlException("ShaderProgramFactory.createShaderProgram():[Program link Error]");
                        }
                        return glCreateProgram;
                    }
                    catch (final OpenGlException ex) {
                        n = glCreateProgram;
                    }
                }
                catch (final OpenGlException ex) {}
            }
            catch (final OpenGlException ex) {
                glCreateShader2 = 0;
            }
        }
        catch (final OpenGlException ex) {
            glCreateShader2 = 0;
            glCreateShader = 0;
        }
        final OpenGlException ex;
        CamLog.e("Fail to create ShaderProgram.", ex);
        if (glCreateShader != 0) {
            GLES20.glDeleteShader(glCreateShader);
        }
        if (glCreateShader2 != 0) {
            GLES20.glDeleteShader(glCreateShader2);
        }
        deleteShaderProgram(n);
        throw ex;
    }
    
    public static int createShaderProgramFromClientApplicationContext(final Context context, final int n, final int n2) {
        return createShaderProgram(context, new int[] { n }, new int[] { n2 });
    }
    
    public static int createShaderProgramFromClientApplicationContext(final Context context, final int[] array, final int[] array2) {
        return createShaderProgram(context, array, array2);
    }
    
    public static int createSimpleFrameShaderProgram(final Context context) throws OpenGlException {
        return createShaderProgram(context, new int[] { 2131623950 }, new int[] { 2131623949 });
    }
    
    public static int createVertexAlphYuvFrameShaderProgram(final Context context) throws OpenGlException {
        return createShaderProgram(context, new int[] { 2131623952 }, new int[] { 2131623951 });
    }
    
    public static int createYuvFrameShaderProgram(final Context context) throws OpenGlException {
        return createShaderProgram(context, new int[] { 2131623954 }, new int[] { 2131623953 });
    }
    
    public static void deleteShaderProgram(final int n) {
        if (n != 0) {
            GLES20.glDeleteProgram(n);
            if (ExtendedGlSurfaceView.isGlErrorOccured()) {
                CamLog.e("deleteShaderProgram():[Delete Program Error]");
            }
        }
    }
}
