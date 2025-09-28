package com.sonymobile.cameracommon.view;

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
public class SelfTimerCountDownCircleView extends View {
    private static final float ANIMATION_TIME_RATIO = 0.6f;
    private static final float ANIMATION_VALUE = 360.0f;
    public static final String TAG = "SelfTimerCountDownCircleView";
    private final int COUNTDOWN_CIRCLE_RADIUS;
    private final int COUNTDOWN_CIRCLE_STROKE_WIDTH;
    private Paint mPaint;
    private ValueAnimator mValueAnimator;

    public SelfTimerCountDownCircleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.COUNTDOWN_CIRCLE_RADIUS = getResources().getDimensionPixelSize(R.dimen.viewfinder_countdown_outside_diameter) / 2;
        this.COUNTDOWN_CIRCLE_STROKE_WIDTH = (getResources().getDimensionPixelSize(R.dimen.viewfinder_countdown_outside_diameter) - getResources().getDimensionPixelSize(R.dimen.viewfinder_countdown_inside_diameter)) / 2;
        this.mValueAnimator = null;
        init(context);
    }

    private SelfTimerCountDownCircleView(Context context) {
        super(context);
        this.COUNTDOWN_CIRCLE_RADIUS = getResources().getDimensionPixelSize(R.dimen.viewfinder_countdown_outside_diameter) / 2;
        this.COUNTDOWN_CIRCLE_STROKE_WIDTH = (getResources().getDimensionPixelSize(R.dimen.viewfinder_countdown_outside_diameter) - getResources().getDimensionPixelSize(R.dimen.viewfinder_countdown_inside_diameter)) / 2;
        this.mValueAnimator = null;
        init(context);
    }

    private SelfTimerCountDownCircleView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.COUNTDOWN_CIRCLE_RADIUS = getResources().getDimensionPixelSize(R.dimen.viewfinder_countdown_outside_diameter) / 2;
        this.COUNTDOWN_CIRCLE_STROKE_WIDTH = (getResources().getDimensionPixelSize(R.dimen.viewfinder_countdown_outside_diameter) - getResources().getDimensionPixelSize(R.dimen.viewfinder_countdown_inside_diameter)) / 2;
        this.mValueAnimator = null;
        init(context);
    }

    private void init(Context context) {
        this.mPaint = new Paint();
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(this.COUNTDOWN_CIRCLE_STROKE_WIDTH);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(getResources().getColor(R.color.viewfinder_countdown_circle_color, null));
    }

    private void setAnimator(SelfTimerInterface selfTimerInterface) {
        this.mValueAnimator = ValueAnimator.ofFloat(0.0f, ANIMATION_VALUE);
        this.mValueAnimator.setInterpolator(new CircleDecelerateInterpolator());
        this.mValueAnimator.setRepeatMode(1);
        this.mValueAnimator.setDuration(getRotateDuration(selfTimerInterface));
        this.mValueAnimator.setRepeatCount((selfTimerInterface.getDurationInMillisecond() / 1000) - 1);
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
        float f = this.COUNTDOWN_CIRCLE_RADIUS - (this.COUNTDOWN_CIRCLE_STROKE_WIDTH / 2);
        float width = canvas.getWidth() / 2.0f;
        float height = canvas.getHeight() / 2.0f;
        canvas.drawArc(new RectF(width - f, height - f, width + f, height + f), -90.0f, ((Float) this.mValueAnimator.getAnimatedValue()).floatValue(), false, this.mPaint);
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
            SelfTimerCountDownCircleView.this.invalidate();
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
