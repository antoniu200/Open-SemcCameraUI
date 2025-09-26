package com.sonyericsson.android.camera.view.sidetouch;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import com.google.android.gms.common.ConnectionResult;
import com.sonyericsson.android.camera.Constants;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.cameracommon.settings.SelfTimerInterface;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SideTouchCountDownCircleView extends View {
    private static final float ANIMATION_TIME_RATIO = 0.6f;
    private static final float ANIMATION_VALUE = 360.0f;
    private static final int COUNTDOWN_CIRCLE_STROKE_WIDTH = 6;
    public static final String TAG = "SideTouchCountDownCircleView";
    private Paint mPaint;
    private ValueAnimator mValueAnimator;

    public SideTouchCountDownCircleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mValueAnimator = null;
    }

    private void init() {
        this.mPaint = new Paint();
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(6.0f);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(getResources().getColor(R.color.viewfinder_countdown_circle_color, null));
    }

    private void setAnimator(SelfTimerInterface selfTimerInterface) {
        init();
        this.mValueAnimator = ValueAnimator.ofFloat(0.0f, ANIMATION_VALUE);
        this.mValueAnimator.setInterpolator(new CircleDecelerateInterpolator());
        this.mValueAnimator.setRepeatMode(1);
        this.mValueAnimator.setDuration(getRotateDuration(selfTimerInterface));
        this.mValueAnimator.setRepeatCount((int) (Math.ceil(selfTimerInterface.getDurationInMillisecond() / 1000.0d) - 1.0d));
        this.mValueAnimator.addUpdateListener(new ValueAnimationUpdater());
        this.mValueAnimator.addListener(new AnimationEventHandler());
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawArc(canvas);
    }

    private int getRotateDuration(SelfTimerInterface selfTimerInterface) {
        if (selfTimerInterface.getDurationInMillisecond() < 1000) {
            return Constants.INTERVAL_OPEN_CAMERA;
        }
        if (selfTimerInterface.getDurationInMillisecond() == 1500) {
            return ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED;
        }
        return 1000;
    }

    private void drawArc(Canvas canvas) {
        if (this.mValueAnimator == null || this.mValueAnimator.getAnimatedValue() == null || !this.mValueAnimator.isRunning()) {
            return;
        }
        float width = canvas.getWidth() / 2.0f;
        float f = width - 3.0f;
        float f2 = width - f;
        float f3 = width + f;
        canvas.drawArc(new RectF(f2, f2, f3, f3), 180.0f, ((Float) this.mValueAnimator.getAnimatedValue()).floatValue(), false, this.mPaint);
    }

    public void setSelfTimer(SelfTimerInterface selfTimerInterface) {
        setAnimator(selfTimerInterface);
    }

    public void cancelSelfTimerAnimation() {
        this.mValueAnimator.end();
    }

    public void startAnimation() {
        if (this.mValueAnimator == null || this.mValueAnimator.isRunning()) {
            return;
        }
        this.mValueAnimator.start();
    }

    private class ValueAnimationUpdater implements ValueAnimator.AnimatorUpdateListener {
        private ValueAnimationUpdater() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            SideTouchCountDownCircleView.this.invalidate();
        }
    }

    private static class AnimationEventHandler implements Animator.AnimatorListener {
        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }

        private AnimationEventHandler() {
        }
    }

    private static class CircleDecelerateInterpolator extends DecelerateInterpolator {
        private CircleDecelerateInterpolator() {
        }

        @Override // android.view.animation.DecelerateInterpolator, android.animation.TimeInterpolator
        public float getInterpolation(float f) {
            float f2 = f * 1.6666666f;
            if (1.0f <= f2) {
                f2 = 1.0f;
            }
            return super.getInterpolation(f2);
        }
    }
}
