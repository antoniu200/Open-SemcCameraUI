// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.cameracommon.opengl;

import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.sonyericsson.android.camera.util.CamLog;
import android.opengl.Matrix;
import android.opengl.GLUtils;
import android.opengl.GLES20;
import android.graphics.Bitmap;
import java.nio.FloatBuffer;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import android.util.AttributeSet;
import android.content.Context;
import android.opengl.GLSurfaceView;

public class ExtendedGlSurfaceView extends GLSurfaceView
{
    public static final boolean IS_OPENGL_DEBUG = false;
    private static final int TARGET_OPEN_GL_ES_VERSION = 2;
    
    public ExtendedGlSurfaceView(final Context context) {
        super(context);
        this.setEGLContextClientVersion(2);
        this.setDebugFlags(3);
    }
    
    public ExtendedGlSurfaceView(final Context context, final AttributeSet set) {
        super(context, set);
        this.setEGLContextClientVersion(2);
        this.setDebugFlags(3);
    }
    
    public static ByteBuffer allocByteBuffer(final byte[] src) {
        final ByteBuffer order = ByteBuffer.allocateDirect(src.length * 8 / 8).order(ByteOrder.nativeOrder());
        order.put(src);
        order.position();
        return order;
    }
    
    public static FloatBuffer allocFloatBuffer(final float[] src) {
        final FloatBuffer floatBuffer = ByteBuffer.allocateDirect(src.length * 32 / 8).order(ByteOrder.nativeOrder()).asFloatBuffer();
        floatBuffer.put(src);
        floatBuffer.position();
        return floatBuffer;
    }
    
    public static void bindTextureAndBitmap(final int n, final Bitmap bitmap) {
        GLES20.glBindTexture(3553, n);
        GLES20.glTexParameterf(3553, 10240, 9729.0f);
        GLES20.glTexParameterf(3553, 10241, 9729.0f);
        GLES20.glTexParameteri(3553, 10242, 33071);
        GLES20.glTexParameteri(3553, 10243, 33071);
        GLUtils.texImage2D(3553, 0, 6408, bitmap, 0);
        GLES20.glBindTexture(3553, 0);
    }
    
    public static void checkGlErrorWithException() throws OpenGlException {
        final int glGetError = GLES20.glGetError();
        if (glGetError != 0) {
            throw new OpenGlException(String.format("OpenGL error [0x%08x].", glGetError));
        }
    }
    
    private static float[] get4x4RotationMatrix(final float n, final float n2, final float n3, final float n4) {
        final float[] array = new float[16];
        Matrix.setIdentityM(array, 0);
        Matrix.rotateM(array, 0, n, n2, n3, n4);
        return array;
    }
    
    private static float[] get4x4ScalingMatrix(final float n, final float n2, final float n3) {
        final float[] array = new float[16];
        Matrix.setIdentityM(array, 0);
        Matrix.scaleM(array, 0, n, n2, n3);
        return array;
    }
    
    private static float[] get4x4TranslationMatrix(final float n, final float n2, final float n3) {
        final float[] array = new float[16];
        Matrix.setIdentityM(array, 0);
        Matrix.translateM(array, 0, n, n2, n3);
        return array;
    }
    
    public static boolean isGlErrorOccured() {
        final int glGetError = GLES20.glGetError();
        if (glGetError != 0) {
            CamLog.e("TraceLog", String.format("OpenGL error [0x%08x].", glGetError));
            return true;
        }
        return false;
    }
    
    static String loadShaderSourceCodesFrom(final Context ex, final int[] array) {
        final StringBuilder sb = new StringBuilder();
        int i = 0;
        String str = null;
        try {
            while (i < array.length) {
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(((Context)ex).getResources().openRawResource(array[i])));
                try {
                    for (str = bufferedReader.readLine(); str != null; str = bufferedReader.readLine()) {
                        sb.append(str);
                    }
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    ++i;
                    str = (String)bufferedReader;
                }
                catch (final IOException ex) {
                    str = (String)bufferedReader;
                }
            }
            return sb.toString();
        }
        catch (final IOException ex2) {}
        CamLog.e("Can not load shader file.", ex);
        if (str != null) {
            try {
                ((BufferedReader)str).close();
            }
            catch (final IOException ex3) {
                CamLog.e("Fail to close BufferedReader.", ex3);
            }
        }
        return sb.toString();
    }
    
    public static void rotate(final float[] array, final float n, final float n2, final float n3) {
        Matrix.multiplyMM(array, 0, get4x4RotationMatrix(n * 180.0f / 3.1415927f, 1.0f, 0.0f, 0.0f), 0, array, 0);
        Matrix.multiplyMM(array, 0, get4x4RotationMatrix(n2 * 180.0f / 3.1415927f, 0.0f, 1.0f, 0.0f), 0, array, 0);
        Matrix.multiplyMM(array, 0, get4x4RotationMatrix(n3 * 180.0f / 3.1415927f, 0.0f, 0.0f, 1.0f), 0, array, 0);
    }
    
    public static void scale(final float[] array, final float n, final float n2, final float n3) {
        Matrix.multiplyMM(array, 0, get4x4ScalingMatrix(n, n2, n3), 0, array, 0);
    }
    
    public static void translate(final float[] array, final float n, final float n2, final float n3) {
        Matrix.multiplyMM(array, 0, get4x4TranslationMatrix(n, n2, n3), 0, array, 0);
    }
}
