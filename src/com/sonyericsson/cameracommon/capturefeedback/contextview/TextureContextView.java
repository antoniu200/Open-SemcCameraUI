package com.sonyericsson.cameracommon.capturefeedback.contextview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.SurfaceTexture;
import android.view.TextureView;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.recorder.utility.ReferenceClock;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.ThreadUtil;
import com.sonyericsson.cameracommon.capturefeedback.CaptureFeedback;
import com.sonyericsson.cameracommon.capturefeedback.animation.CaptureFeedbackAnimation;
import com.sonyericsson.cameracommon.capturefeedback.animation.CaptureFeedbackAnimationCanvas;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TextureContextView extends TextureView implements TextureView.SurfaceTextureListener, CaptureFeedback {
    private static final long DRAW_INTERVAL_MILLIS = 33;
    public static final String TAG = "TextureContextView";
    private static final String THREAD_NAME = "ShutterFeedback";
    private CaptureFeedbackAnimation mAnimation;
    private final ReferenceClock mAnimationElapsedTimeCount;
    private final AnimationCanvas mCanvas;
    private final ScheduledExecutorService mExecutor;
    private boolean mIsAnimationRequested;
    private ScheduledFuture<?> mScheduledFuture;
    private final SetInvisibleTask mSetInvisibleTask;

    @Override // com.sonyericsson.cameracommon.capturefeedback.CaptureFeedback
    public void onPause() {
    }

    @Override // com.sonyericsson.cameracommon.capturefeedback.CaptureFeedback
    public void onResume() {
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    public TextureContextView(Context context) {
        super(context);
        this.mSetInvisibleTask = new SetInvisibleTask();
        this.mCanvas = new AnimationCanvas();
        setSurfaceTextureListener(this);
        this.mAnimationElapsedTimeCount = new ReferenceClock();
        this.mExecutor = ThreadUtil.buildScheduledExecutor(THREAD_NAME, 10);
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
        if (this.mIsAnimationRequested) {
            this.mIsAnimationRequested = false;
            this.mScheduledFuture = this.mExecutor.scheduleAtFixedRate(new DrawFrameTask(), 0L, DRAW_INTERVAL_MILLIS, TimeUnit.MILLISECONDS);
        }
    }

    @Override // com.sonyericsson.cameracommon.capturefeedback.CaptureFeedback
    public void start(CaptureFeedbackAnimation captureFeedbackAnimation) {
        if (CamLog.VERBOSE) {
            CamLog.d(CameraActivity.INTENT_SUBJECT_START);
        }
        this.mAnimation = captureFeedbackAnimation;
        this.mAnimationElapsedTimeCount.start();
        setVisibility(0);
        if (isAvailable()) {
            this.mScheduledFuture = this.mExecutor.scheduleAtFixedRate(new DrawFrameTask(), 0L, DRAW_INTERVAL_MILLIS, TimeUnit.MILLISECONDS);
        } else {
            this.mIsAnimationRequested = true;
        }
    }

    @Override // com.sonyericsson.cameracommon.capturefeedback.CaptureFeedback
    public void release() {
        if (CamLog.VERBOSE) {
            CamLog.d("release()");
        }
    }

    private class DrawFrameTask implements Runnable {
        private DrawFrameTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (TextureContextView.this.mCanvas.lock()) {
                TextureContextView.this.mCanvas.clear();
                CaptureFeedbackAnimation captureFeedbackAnimation = TextureContextView.this.mAnimation;
                boolean zDraw = captureFeedbackAnimation != null ? true ^ captureFeedbackAnimation.draw(TextureContextView.this.mCanvas, TextureContextView.this.mAnimationElapsedTimeCount.elapsedTimeMillis()) : true;
                TextureContextView.this.mCanvas.unlock();
                if (zDraw) {
                    TextureContextView.this.mAnimationElapsedTimeCount.stop();
                    TextureContextView.this.post(TextureContextView.this.mSetInvisibleTask);
                }
            }
        }
    }

    private class SetInvisibleTask implements Runnable {
        private SetInvisibleTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            TextureContextView.this.setVisibility(4);
            TextureContextView.this.mScheduledFuture.cancel(true);
        }
    }

    private class AnimationCanvas implements CaptureFeedbackAnimationCanvas {
        private Canvas mCanvas;

        private AnimationCanvas() {
            this.mCanvas = null;
        }

        public boolean lock() {
            this.mCanvas = TextureContextView.this.lockCanvas();
            return this.mCanvas != null;
        }

        public void unlock() {
            if (this.mCanvas != null) {
                TextureContextView.this.unlockCanvasAndPost(this.mCanvas);
            }
        }

        public void clear() {
            if (this.mCanvas == null) {
                return;
            }
            this.mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        }

        @Override // com.sonyericsson.cameracommon.capturefeedback.animation.CaptureFeedbackAnimationCanvas
        public void drawColor(float f, float f2, float f3, float f4) {
            if (this.mCanvas == null) {
                return;
            }
            this.mCanvas.drawColor(Color.argb((int) (f * 255.0f), (int) (f2 * 255.0f), (int) (f3 * 255.0f), (int) (255.0f * f4)), PorterDuff.Mode.SRC_OVER);
        }
    }
}
