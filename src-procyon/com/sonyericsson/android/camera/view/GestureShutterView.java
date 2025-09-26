// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view;

import android.graphics.Canvas;
import android.graphics.Paint$Cap;
import android.graphics.Paint$Style;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class GestureShutterView extends View
{
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
    
    public GestureShutterView(final Context context) {
        this(context, null, 0);
    }
    
    public GestureShutterView(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public GestureShutterView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mLastTimeStamp = -1L;
        this.mTimeProgress = 0.0f;
        this.mAnimationDuration = 200;
        this.mState = AnimationState.IDLE;
        this.mType = AnimationType.NONE;
        this.mCircleFrame = new RectF();
        this.mAnimationListener = null;
        (this.mPaint = new Paint()).setStyle(Paint$Style.STROKE);
        this.mPaint.setStrokeCap(Paint$Cap.ROUND);
        this.mPaint.setColor(-1);
        this.mPaint.setStrokeWidth(context.getResources().getDisplayMetrics().density * 4.0f);
    }
    
    private static int calcConfirmingAlpha() {
        return 255;
    }
    
    protected void onConfirmingFinished() {
        if (this.mAnimationListener != null) {
            this.mAnimationListener.handleConfirmingFinished();
        }
    }
    
    protected void onDraw(final Canvas canvas) {
        synchronized (this) {
            super.onDraw(canvas);
            final long currentTimeMillis = System.currentTimeMillis();
            if (this.mLastTimeStamp > 0L) {
                this.mTimeProgress += this.mState.sign * (currentTimeMillis - this.mLastTimeStamp) / (float)this.mAnimationDuration;
            }
            this.mLastTimeStamp = currentTimeMillis;
            float mTimeProgress;
            if ((mTimeProgress = this.mTimeProgress) < 0.0f) {
                mTimeProgress = 0.0f;
            }
            float n = mTimeProgress;
            if (mTimeProgress > 1.0f) {
                n = 1.0f;
            }
            switch (GestureShutterView$1.$SwitchMap$com$sonyericsson$android$camera$view$GestureShutterView$AnimationType[this.mType.ordinal()]) {
                case 2: {
                    this.mPaint.setAlpha(calcConfirmingAlpha());
                    canvas.drawArc(this.mCircleFrame, 0.0f, 360.0f, false, this.mPaint);
                    break;
                }
                case 1: {
                    final float n2 = n * 0.5f;
                    this.mPaint.setAlpha((int)((0.5f + n2) * 255.0f));
                    for (int i = 0; i < 4; ++i) {
                        canvas.drawArc(this.mCircleFrame, 45.0f + (i - n2) * 90.0f, 90.0f * n + 0.01f, false, this.mPaint);
                    }
                    break;
                }
            }
            if (this.mTimeProgress >= 1.0f && this.mState == AnimationState.PROCEEDING) {
                final AnimationType mType = this.mType;
                this.reset();
                if (mType == AnimationType.GROWING_CIRCLE) {
                    this.onProceedFinished();
                }
                else if (mType == AnimationType.CONFRIMING) {
                    this.onConfirmingFinished();
                }
            }
            else if (this.mTimeProgress <= 0.0f && this.mState == AnimationState.REWINDING) {
                this.reset();
                this.onRewindFinished();
            }
            else if (this.mState != AnimationState.IDLE) {
                this.invalidate();
            }
        }
    }
    
    protected void onProceedFinished() {
        if (this.mAnimationListener != null) {
            this.mAnimationListener.handleProceedFinished();
        }
    }
    
    protected void onRewindFinished() {
        if (this.mAnimationListener != null) {
            this.mAnimationListener.handleRewindFinished();
        }
    }
    
    public void reset() {
        synchronized (this) {
            this.mState = AnimationState.IDLE;
            this.mType = AnimationType.NONE;
            this.mLastTimeStamp = -1L;
            this.mTimeProgress = 0.0f;
            this.invalidate();
        }
    }
    
    public void setListener(final AnimationStatusListener mAnimationListener) {
        synchronized (this) {
            this.mAnimationListener = mAnimationListener;
        }
    }
    
    public void startConfirming() {
        synchronized (this) {
            this.mState = AnimationState.PROCEEDING;
            this.mType = AnimationType.CONFRIMING;
            this.mLastTimeStamp = -1L;
            this.mAnimationDuration = 100;
            this.invalidate();
        }
    }
    
    public void startProceed(final RectF rectF) {
        synchronized (this) {
            this.mState = AnimationState.PROCEEDING;
            this.mType = AnimationType.GROWING_CIRCLE;
            this.mLastTimeStamp = -1L;
            this.mAnimationDuration = 200;
            this.updateFrame(rectF);
            this.invalidate();
        }
    }
    
    public void startRewind() {
        synchronized (this) {
            if (this.mType == AnimationType.GROWING_CIRCLE) {
                this.mState = AnimationState.REWINDING;
                this.mLastTimeStamp = -1L;
                this.mAnimationDuration = 200;
                this.invalidate();
            }
        }
    }
    
    public void updateFrame(final RectF rectF) {
        synchronized (this) {
            this.mCircleFrame.set(rectF);
        }
    }
    
    private enum AnimationState
    {
        private static final AnimationState[] $VALUES;
        
        IDLE(0), 
        PROCEEDING(1), 
        REWINDING(-1);
        
        final int sign;
        
        static {
            $VALUES = new AnimationState[] { AnimationState.IDLE, AnimationState.PROCEEDING, AnimationState.REWINDING };
        }
        
        private AnimationState(final int sign) {
            this.sign = sign;
        }
    }
    
    public interface AnimationStatusListener
    {
        void handleConfirmingFinished();
        
        void handleProceedFinished();
        
        void handleRewindFinished();
    }
    
    private enum AnimationType
    {
        private static final AnimationType[] $VALUES;
        
        CONFRIMING, 
        GROWING_CIRCLE, 
        NONE;
        
        static {
            $VALUES = new AnimationType[] { AnimationType.NONE, AnimationType.GROWING_CIRCLE, AnimationType.CONFRIMING };
        }
    }
}
