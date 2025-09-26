// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.capturefeedback.contextview;

import android.graphics.Color;
import android.graphics.PorterDuff$Mode;
import android.graphics.Canvas;
import com.sonyericsson.cameracommon.capturefeedback.animation.CaptureFeedbackAnimationCanvas;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.concurrent.TimeUnit;
import android.graphics.SurfaceTexture;
import com.sonyericsson.android.camera.util.ThreadUtil;
import android.content.Context;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledExecutorService;
import com.sonyericsson.android.camera.recorder.utility.ReferenceClock;
import com.sonyericsson.cameracommon.capturefeedback.animation.CaptureFeedbackAnimation;
import com.sonyericsson.cameracommon.capturefeedback.CaptureFeedback;
import android.view.TextureView$SurfaceTextureListener;
import android.view.TextureView;

public class TextureContextView extends TextureView implements TextureView$SurfaceTextureListener, CaptureFeedback
{
    private static final long DRAW_INTERVAL_MILLIS = 33L;
    public static final String TAG = "TextureContextView";
    private static final String THREAD_NAME = "ShutterFeedback";
    private CaptureFeedbackAnimation mAnimation;
    private final ReferenceClock mAnimationElapsedTimeCount;
    private final AnimationCanvas mCanvas;
    private final ScheduledExecutorService mExecutor;
    private boolean mIsAnimationRequested;
    private ScheduledFuture<?> mScheduledFuture;
    private final SetInvisibleTask mSetInvisibleTask;
    
    public TextureContextView(final Context context) {
        super(context);
        this.mSetInvisibleTask = new SetInvisibleTask();
        this.mCanvas = new AnimationCanvas();
        this.setSurfaceTextureListener((TextureView$SurfaceTextureListener)this);
        this.mAnimationElapsedTimeCount = new ReferenceClock();
        this.mExecutor = ThreadUtil.buildScheduledExecutor("ShutterFeedback", 10);
    }
    
    public void onPause() {
    }
    
    public void onResume() {
    }
    
    public void onSurfaceTextureAvailable(final SurfaceTexture surfaceTexture, final int n, final int n2) {
        if (this.mIsAnimationRequested) {
            this.mIsAnimationRequested = false;
            this.mScheduledFuture = this.mExecutor.scheduleAtFixedRate(new DrawFrameTask(), 0L, 33L, TimeUnit.MILLISECONDS);
        }
    }
    
    public boolean onSurfaceTextureDestroyed(final SurfaceTexture surfaceTexture) {
        return false;
    }
    
    public void onSurfaceTextureSizeChanged(final SurfaceTexture surfaceTexture, final int n, final int n2) {
    }
    
    public void onSurfaceTextureUpdated(final SurfaceTexture surfaceTexture) {
    }
    
    public void release() {
        if (CamLog.VERBOSE) {
            CamLog.d("release()");
        }
    }
    
    public void start(final CaptureFeedbackAnimation mAnimation) {
        if (CamLog.VERBOSE) {
            CamLog.d("start");
        }
        this.mAnimation = mAnimation;
        this.mAnimationElapsedTimeCount.start();
        this.setVisibility(0);
        if (this.isAvailable()) {
            this.mScheduledFuture = this.mExecutor.scheduleAtFixedRate(new DrawFrameTask(), 0L, 33L, TimeUnit.MILLISECONDS);
        }
        else {
            this.mIsAnimationRequested = true;
        }
    }
    
    private class AnimationCanvas implements CaptureFeedbackAnimationCanvas
    {
        private Canvas mCanvas;
        final TextureContextView this$0;
        
        private AnimationCanvas(final TextureContextView this$0) {
            this.this$0 = this$0;
            this.mCanvas = null;
        }
        
        public void clear() {
            if (this.mCanvas == null) {
                return;
            }
            this.mCanvas.drawColor(0, PorterDuff$Mode.CLEAR);
        }
        
        @Override
        public void drawColor(final float n, final float n2, final float n3, final float n4) {
            if (this.mCanvas == null) {
                return;
            }
            this.mCanvas.drawColor(Color.argb((int)(n * 255.0f), (int)(n2 * 255.0f), (int)(n3 * 255.0f), (int)(255.0f * n4)), PorterDuff$Mode.SRC_OVER);
        }
        
        public boolean lock() {
            this.mCanvas = this.this$0.lockCanvas();
            return this.mCanvas != null;
        }
        
        public void unlock() {
            if (this.mCanvas != null) {
                this.this$0.unlockCanvasAndPost(this.mCanvas);
            }
        }
    }
    
    private class DrawFrameTask implements Runnable
    {
        final TextureContextView this$0;
        
        private DrawFrameTask(final TextureContextView this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            if (!this.this$0.mCanvas.lock()) {
                return;
            }
            this.this$0.mCanvas.clear();
            final CaptureFeedbackAnimation access$400 = this.this$0.mAnimation;
            boolean b = true;
            if (access$400 != null) {
                b = (true ^ access$400.draw(this.this$0.mCanvas, this.this$0.mAnimationElapsedTimeCount.elapsedTimeMillis()));
            }
            this.this$0.mCanvas.unlock();
            if (b) {
                this.this$0.mAnimationElapsedTimeCount.stop();
                this.this$0.post((Runnable)this.this$0.mSetInvisibleTask);
            }
        }
    }
    
    private class SetInvisibleTask implements Runnable
    {
        final TextureContextView this$0;
        
        private SetInvisibleTask(final TextureContextView this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            this.this$0.setVisibility(4);
            this.this$0.mScheduledFuture.cancel(true);
        }
    }
}
