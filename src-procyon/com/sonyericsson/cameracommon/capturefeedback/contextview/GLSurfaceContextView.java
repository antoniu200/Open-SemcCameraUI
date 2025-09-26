// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.capturefeedback.contextview;

import android.view.View;
import android.view.SurfaceHolder;
import javax.microedition.khronos.egl.EGLConfig;
import java.util.concurrent.TimeUnit;
import javax.microedition.khronos.opengles.GL10;
import com.sonymobile.cameracommon.opengl.ShaderProgramFactory;
import android.opengl.GLES20;
import com.sonyericsson.android.camera.util.CamLog;
import android.util.AttributeSet;
import android.content.Context;
import android.opengl.Matrix;
import com.sonymobile.cameracommon.opengl.SimpleFrame;
import java.util.concurrent.CountDownLatch;
import com.sonyericsson.android.camera.recorder.utility.ReferenceClock;
import com.sonyericsson.cameracommon.capturefeedback.animation.CaptureFeedbackAnimationCanvas;
import com.sonyericsson.cameracommon.capturefeedback.animation.CaptureFeedbackAnimation;
import com.sonyericsson.cameracommon.capturefeedback.CaptureFeedback;
import android.opengl.GLSurfaceView$Renderer;
import com.sonymobile.cameracommon.opengl.ExtendedGlSurfaceView;

public class GLSurfaceContextView extends ExtendedGlSurfaceView implements GLSurfaceView$Renderer, CaptureFeedback
{
    private static final long ANIMATION_CANCEL_WAITING_DURATION = 100L;
    private static final float CENTER_X_POS = 0.0f;
    private static final float CENTER_Y_POS = 0.0f;
    private static final float CENTER_Z_POS = 0.2f;
    private static final float[] EYE_SIGHT_MATRIX;
    private static final float[] PARALLEL_PROJECTION_MATRIX;
    private static final float[] PERSPECTIVE_PROJECTION_MATRIX;
    private static final float[] ROOT_GM;
    public static final String TAG = "GLSurfaceContextView";
    private CaptureFeedbackAnimation mAnimation;
    private final CaptureFeedbackAnimationCanvas mAnimationCanvas;
    private final ReferenceClock mAnimationElapsedTimeCount;
    private CountDownLatch mCountDownLatch;
    private SimpleFrame mFlashFeedback;
    private final SetInvisibleTask mSetInvisibleTask;
    private int mSimpleFrameShader;
    
    static {
        EYE_SIGHT_MATRIX = new float[16];
        PERSPECTIVE_PROJECTION_MATRIX = new float[16];
        PARALLEL_PROJECTION_MATRIX = new float[16];
        ROOT_GM = new float[16];
        Matrix.setLookAtM(GLSurfaceContextView.EYE_SIGHT_MATRIX, 0, 0.0f, 0.0f, 100.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        Matrix.orthoM(GLSurfaceContextView.PARALLEL_PROJECTION_MATRIX, 0, -1.0f, 1.0f, -1.0f, 1.0f, 0.0f, 200.0f);
        Matrix.frustumM(GLSurfaceContextView.PERSPECTIVE_PROJECTION_MATRIX, 0, -1.0f, 1.0f, -1.0f, 1.0f, 50.0f, 150.0f);
    }
    
    public GLSurfaceContextView(final Context context, final AttributeSet set) {
        super(context, set);
        this.mFlashFeedback = null;
        this.mSimpleFrameShader = 0;
        this.mSetInvisibleTask = new SetInvisibleTask();
        this.mAnimationCanvas = new AnimationCanvas();
        if (CamLog.VERBOSE) {
            CamLog.d("AnimationContextView()");
        }
        this.mAnimationElapsedTimeCount = new ReferenceClock();
        this.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        this.setZOrderOnTop(true);
        this.setRenderer((GLSurfaceView$Renderer)this);
        this.setRenderMode(0);
        this.getHolder().setFormat(-2);
    }
    
    private void clearSurface() {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(17664);
    }
    
    private void createAllShaders() {
        if (CamLog.VERBOSE) {
            CamLog.d("createAllShaders() : E");
        }
        if (this.mSimpleFrameShader != 0) {
            ShaderProgramFactory.deleteShaderProgram(this.mSimpleFrameShader);
        }
        this.mSimpleFrameShader = ShaderProgramFactory.createSimpleFrameShaderProgram(this.getContext());
        if (CamLog.VERBOSE) {
            CamLog.d("createAllShaders() : X");
        }
    }
    
    private boolean disableGlobalFunctions() {
        GLES20.glDisable(3042);
        GLES20.glDisable(2929);
        return true;
    }
    
    private void doRender() {
        final CountDownLatch mCountDownLatch = this.mCountDownLatch;
        if (mCountDownLatch != null) {
            this.clearSurface();
            if (0L < mCountDownLatch.getCount()) {
                this.mAnimationElapsedTimeCount.stop();
                this.post((Runnable)this.mSetInvisibleTask);
                mCountDownLatch.countDown();
            }
            return;
        }
        this.clearSurface();
        if (this.mFlashFeedback == null) {
            return;
        }
        Matrix.setIdentityM(GLSurfaceContextView.ROOT_GM, 0);
        Matrix.multiplyMM(GLSurfaceContextView.ROOT_GM, 0, GLSurfaceContextView.EYE_SIGHT_MATRIX, 0, GLSurfaceContextView.ROOT_GM, 0);
        Matrix.multiplyMM(GLSurfaceContextView.ROOT_GM, 0, GLSurfaceContextView.PARALLEL_PROJECTION_MATRIX, 0, GLSurfaceContextView.ROOT_GM, 0);
        this.mFlashFeedback.setGlobalMatrix(GLSurfaceContextView.ROOT_GM);
        final CaptureFeedbackAnimation mAnimation = this.mAnimation;
        boolean b = true;
        if (mAnimation != null) {
            GLES20.glBlendFunc(770, 1);
            b = (true ^ mAnimation.draw(this.mAnimationCanvas, this.mAnimationElapsedTimeCount.elapsedTimeMillis()));
            GLES20.glBlendFunc(770, 771);
        }
        final CountDownLatch mCountDownLatch2 = this.mCountDownLatch;
        if (mCountDownLatch2 != null) {
            this.clearSurface();
            if (0L < mCountDownLatch2.getCount()) {
                this.mAnimationElapsedTimeCount.stop();
                this.post((Runnable)this.mSetInvisibleTask);
                mCountDownLatch2.countDown();
            }
            return;
        }
        if (b) {
            this.mAnimationElapsedTimeCount.stop();
            this.post((Runnable)this.mSetInvisibleTask);
        }
    }
    
    private boolean enableGlobalFunctions() {
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(770, 771);
        GLES20.glEnable(2929);
        return true;
    }
    
    public static final void preload() {
    }
    
    private void releaseAllShaders() {
        if (CamLog.VERBOSE) {
            CamLog.d("releaseAllShaders() : E");
        }
        ShaderProgramFactory.deleteShaderProgram(this.mSimpleFrameShader);
        this.mSimpleFrameShader = 0;
        if (CamLog.VERBOSE) {
            CamLog.d("releaseAllShaders() : X");
        }
    }
    
    private void render() {
        if (!this.enableGlobalFunctions()) {
            CamLog.e("render():[Enable functions failed.]");
            return;
        }
        this.doRender();
        if (!this.disableGlobalFunctions()) {
            CamLog.e("render():[Disable functions failed.]");
        }
    }
    
    private void setupDynamicConfig(final int n, final int n2) {
        if (n2 < n) {
            GLES20.glViewport(0, -1 * ((n - n2) / 2), n, n);
        }
        else {
            GLES20.glViewport(-1 * ((n2 - n) / 2), 0, n2, n2);
        }
    }
    
    public void onDrawFrame(final GL10 gl10) {
        this.render();
    }
    
    public void onPause() {
        if (this.isShown() && this.mAnimation != null && this.mCountDownLatch == null) {
            this.mCountDownLatch = new CountDownLatch(1);
            try {
                if (!this.mCountDownLatch.await(100L, TimeUnit.MILLISECONDS)) {
                    CamLog.d("onPause() : timed-out");
                }
            }
            catch (final InterruptedException ex) {
                if (CamLog.VERBOSE) {
                    CamLog.d("onPause() : mCountDownLatch.await() interrupted");
                }
            }
        }
        super.onPause();
    }
    
    public void onSurfaceChanged(final GL10 gl10, final int i, final int j) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onSurfaceChanged() : width = ");
            sb.append(i);
            sb.append(", height = ");
            sb.append(j);
            CamLog.d(sb.toString());
        }
        this.setupRelatedToSurfaceSize();
    }
    
    public void onSurfaceCreated(final GL10 gl10, final EGLConfig eglConfig) {
    }
    
    public void release() {
        if (CamLog.VERBOSE) {
            CamLog.d("release()");
        }
        this.queueEvent((Runnable)new ReleaseTask());
    }
    
    public void setupRelatedToSurfaceSize() {
        this.queueEvent((Runnable)new SetupRelatedToSurfaceSizeTask());
    }
    
    public void start(final CaptureFeedbackAnimation mAnimation) {
        if (CamLog.VERBOSE) {
            CamLog.d("start()");
        }
        this.removeCallbacks((Runnable)this.mSetInvisibleTask);
        this.mCountDownLatch = null;
        this.mAnimation = mAnimation;
        this.mAnimationElapsedTimeCount.start();
        this.setVisibility(0);
        this.setRenderMode(1);
        this.requestRender();
    }
    
    public void surfaceDestroyed(final SurfaceHolder surfaceHolder) {
        if (CamLog.VERBOSE) {
            CamLog.d("surfaceDestroyed() : E");
        }
        this.release();
        super.surfaceDestroyed(surfaceHolder);
        if (CamLog.VERBOSE) {
            CamLog.d("surfaceDestroyed() : X");
        }
    }
    
    private class AnimationCanvas implements CaptureFeedbackAnimationCanvas
    {
        final GLSurfaceContextView this$0;
        
        private AnimationCanvas(final GLSurfaceContextView this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void drawColor(final float n, final float n2, final float n3, final float n4) {
            this.this$0.mFlashFeedback.translate(0.0f, 0.0f, 0.2f);
            this.this$0.mFlashFeedback.setColor(n2, n3, n4, n);
            this.this$0.mFlashFeedback.render();
        }
    }
    
    private class ReleaseTask implements Runnable
    {
        final GLSurfaceContextView this$0;
        
        private ReleaseTask(final GLSurfaceContextView this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            if (this.this$0.mFlashFeedback != null) {
                this.this$0.mFlashFeedback.release();
                this.this$0.mFlashFeedback = null;
            }
            this.this$0.releaseAllShaders();
        }
    }
    
    private class SetInvisibleTask implements Runnable
    {
        final GLSurfaceContextView this$0;
        
        private SetInvisibleTask(final GLSurfaceContextView this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            this.this$0.setVisibility(4);
            this.this$0.setRenderMode(0);
        }
    }
    
    private class SetupRelatedToSurfaceSizeTask implements Runnable
    {
        final GLSurfaceContextView this$0;
        
        private SetupRelatedToSurfaceSizeTask(final GLSurfaceContextView this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            this.this$0.setupDynamicConfig(this.this$0.getWidth(), this.this$0.getHeight());
            if (this.this$0.mFlashFeedback == null) {
                this.this$0.createAllShaders();
                this.this$0.mFlashFeedback = new SimpleFrame(this.this$0.getContext(), (View)this.this$0);
                this.this$0.mFlashFeedback.setColor(0.0f, 0.0f, 0.0f, 0.0f);
                this.this$0.mFlashFeedback.setShaderProgram(this.this$0.mSimpleFrameShader);
                this.this$0.mFlashFeedback.setVisibility(true);
            }
        }
    }
}
