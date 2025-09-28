package com.sonyericsson.android.camera.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class GestureShutterView extends View {
    private static final float ANIMATION_PROGRESS_END = 1.0f;
    private static final float ANIMATION_PROGRESS_START = 0.0f;
    private static final float ARC_ANGLE_OFFSET = 45.0f;
    private static final int ARC_NUM = 4;
    private static final int CONFIRMING_DURATION_MILLIS = 100;
    private static final int PROCEED_DURATION_MILLIS = 200;
    private static final float STARTING_ALPHA = 0.5f;
    private static final int STROKE_COLOR = -1;
    private static final int STROKE_WIDTH_DP = 4;
    private int mAnimationDuration;
    private AnimationStatusListener mAnimationListener;
    private final RectF mCircleFrame;
    private long mLastTimeStamp;
    private final Paint mPaint;
    private AnimationState mState;
    private float mTimeProgress;
    private AnimationType mType;

    public interface AnimationStatusListener {
        void handleConfirmingFinished();

        void handleProceedFinished();

        void handleRewindFinished();
    }

    private enum AnimationType {
        NONE,
        GROWING_CIRCLE,
        CONFRIMING
    }

    private static int calcConfirmingAlpha() {
        return 255;
    }

    private enum AnimationState {
        IDLE(0),
        PROCEEDING(1),
        REWINDING(-1);

        final int sign;

        AnimationState(int i) {
            this.sign = i;
        }
    }

    public GestureShutterView(Context context) {
        this(context, null, 0);
    }

    public GestureShutterView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public GestureShutterView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mLastTimeStamp = -1L;
        this.mTimeProgress = 0.0f;
        this.mAnimationDuration = 200;
        this.mState = AnimationState.IDLE;
        this.mType = AnimationType.NONE;
        this.mCircleFrame = new RectF();
        this.mAnimationListener = null;
        this.mPaint = new Paint();
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mPaint.setColor(-1);
        this.mPaint.setStrokeWidth(context.getResources().getDisplayMetrics().density * 4.0f);
    }

    public synchronized void updateFrame(RectF rectF) {
        this.mCircleFrame.set(rectF);
    }

    public synchronized void startProceed(RectF rectF) {
        this.mState = AnimationState.PROCEEDING;
        this.mType = AnimationType.GROWING_CIRCLE;
        this.mLastTimeStamp = -1L;
        this.mAnimationDuration = 200;
        updateFrame(rectF);
        invalidate();
    }

    public synchronized void startRewind() {
        if (this.mType == AnimationType.GROWING_CIRCLE) {
            this.mState = AnimationState.REWINDING;
            this.mLastTimeStamp = -1L;
            this.mAnimationDuration = 200;
            invalidate();
        }
    }

    public synchronized void startConfirming() {
        this.mState = AnimationState.PROCEEDING;
        this.mType = AnimationType.CONFRIMING;
        this.mLastTimeStamp = -1L;
        this.mAnimationDuration = 100;
        invalidate();
    }

    public synchronized void reset() {
        this.mState = AnimationState.IDLE;
        this.mType = AnimationType.NONE;
        this.mLastTimeStamp = -1L;
        this.mTimeProgress = 0.0f;
        invalidate();
    }

    public synchronized void setListener(AnimationStatusListener animationStatusListener) {
        this.mAnimationListener = animationStatusListener;
    }

    protected void onRewindFinished() {
        if (this.mAnimationListener != null) {
            this.mAnimationListener.handleRewindFinished();
        }
    }

    protected void onProceedFinished() {
        if (this.mAnimationListener != null) {
            this.mAnimationListener.handleProceedFinished();
        }
    }

    protected void onConfirmingFinished() {
        if (this.mAnimationListener != null) {
            this.mAnimationListener.handleConfirmingFinished();
        }
    }

    @Override // android.view.View
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (this.mLastTimeStamp > 0) {
            this.mTimeProgress += (this.mState.sign * (jCurrentTimeMillis - this.mLastTimeStamp)) / this.mAnimationDuration;
        }
        this.mLastTimeStamp = jCurrentTimeMillis;
        float f = this.mTimeProgress;
        if (f < 0.0f) {
            f = 0.0f;
        }
        if (f > ANIMATION_PROGRESS_END) {
            f = 1.0f;
        }
        switch (this.mType) {
            case GROWING_CIRCLE:
                float f2 = f * STARTING_ALPHA;
                this.mPaint.setAlpha((int) ((STARTING_ALPHA + f2) * 255.0f));
                for (int i = 0; i < 4; i++) {
                    canvas.drawArc(this.mCircleFrame, ARC_ANGLE_OFFSET + ((i - f2) * 90.0f), (90.0f * f) + 0.01f, false, this.mPaint);
                }
                break;
            case CONFRIMING:
                this.mPaint.setAlpha(calcConfirmingAlpha());
                canvas.drawArc(this.mCircleFrame, 0.0f, 360.0f, false, this.mPaint);
                break;
        }
        if (this.mTimeProgress >= ANIMATION_PROGRESS_END && this.mState == AnimationState.PROCEEDING) {
            AnimationType animationType = this.mType;
            reset();
            if (animationType == AnimationType.GROWING_CIRCLE) {
                onProceedFinished();
            } else if (animationType == AnimationType.CONFRIMING) {
                onConfirmingFinished();
            }
        } else if (this.mTimeProgress <= 0.0f && this.mState == AnimationState.REWINDING) {
            reset();
            onRewindFinished();
        } else if (this.mState != AnimationState.IDLE) {
            invalidate();
        }
    }
}
