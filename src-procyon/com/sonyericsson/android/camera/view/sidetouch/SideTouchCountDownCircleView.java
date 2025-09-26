// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.sidetouch;

import android.view.animation.DecelerateInterpolator;
import android.animation.Animator;
import android.animation.Animator$AnimatorListener;
import android.animation.ValueAnimator$AnimatorUpdateListener;
import android.animation.TimeInterpolator;
import android.content.res.Resources$Theme;
import android.graphics.Paint$Style;
import com.sonyericsson.cameracommon.settings.SelfTimerInterface;
import android.graphics.RectF;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.content.Context;
import android.animation.ValueAnimator;
import android.graphics.Paint;
import android.view.View;

public class SideTouchCountDownCircleView extends View
{
    private static final float ANIMATION_TIME_RATIO = 0.6f;
    private static final float ANIMATION_VALUE = 360.0f;
    private static final int COUNTDOWN_CIRCLE_STROKE_WIDTH = 6;
    public static final String TAG = "SideTouchCountDownCircleView";
    private Paint mPaint;
    private ValueAnimator mValueAnimator;
    
    public SideTouchCountDownCircleView(final Context context, final AttributeSet set) {
        super(context, set);
        this.mValueAnimator = null;
    }
    
    private void drawArc(final Canvas canvas) {
        if (this.mValueAnimator != null && this.mValueAnimator.getAnimatedValue() != null && this.mValueAnimator.isRunning()) {
            final float n = canvas.getWidth() / 2.0f;
            final float n2 = n - 3.0f;
            final float n3 = n - n2;
            final float n4 = n + n2;
            canvas.drawArc(new RectF(n3, n3, n4, n4), 180.0f, (float)this.mValueAnimator.getAnimatedValue(), false, this.mPaint);
        }
    }
    
    private int getRotateDuration(final SelfTimerInterface selfTimerInterface) {
        final int durationInMillisecond = selfTimerInterface.getDurationInMillisecond();
        int n = 1500;
        if (durationInMillisecond < 1000) {
            n = 500;
        }
        else if (selfTimerInterface.getDurationInMillisecond() != 1500) {
            n = 1000;
        }
        return n;
    }
    
    private void init() {
        (this.mPaint = new Paint()).setStyle(Paint$Style.STROKE);
        this.mPaint.setStrokeWidth(6.0f);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(this.getResources().getColor(2131099775, (Resources$Theme)null));
    }
    
    private void setAnimator(final SelfTimerInterface selfTimerInterface) {
        this.init();
        (this.mValueAnimator = ValueAnimator.ofFloat(new float[] { 0.0f, 360.0f })).setInterpolator((TimeInterpolator)new CircleDecelerateInterpolator());
        this.mValueAnimator.setRepeatMode(1);
        this.mValueAnimator.setDuration((long)this.getRotateDuration(selfTimerInterface));
        this.mValueAnimator.setRepeatCount((int)(Math.ceil(selfTimerInterface.getDurationInMillisecond() / 1000.0) - 1.0));
        this.mValueAnimator.addUpdateListener((ValueAnimator$AnimatorUpdateListener)new ValueAnimationUpdater());
        this.mValueAnimator.addListener((Animator$AnimatorListener)new AnimationEventHandler());
    }
    
    public void cancelSelfTimerAnimation() {
        this.mValueAnimator.end();
    }
    
    public void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        this.drawArc(canvas);
    }
    
    public void setSelfTimer(final SelfTimerInterface animator) {
        this.setAnimator(animator);
    }
    
    public void startAnimation() {
        if (this.mValueAnimator != null && !this.mValueAnimator.isRunning()) {
            this.mValueAnimator.start();
        }
    }
    
    private static class AnimationEventHandler implements Animator$AnimatorListener
    {
        public void onAnimationCancel(final Animator animator) {
        }
        
        public void onAnimationEnd(final Animator animator) {
        }
        
        public void onAnimationRepeat(final Animator animator) {
        }
        
        public void onAnimationStart(final Animator animator) {
        }
    }
    
    private static class CircleDecelerateInterpolator extends DecelerateInterpolator
    {
        public float getInterpolation(float n) {
            if (1.0f <= (n *= 1.6666666f)) {
                n = 1.0f;
            }
            return super.getInterpolation(n);
        }
    }
    
    private class ValueAnimationUpdater implements ValueAnimator$AnimatorUpdateListener
    {
        final SideTouchCountDownCircleView this$0;
        
        private ValueAnimationUpdater(final SideTouchCountDownCircleView this$0) {
            this.this$0 = this$0;
        }
        
        public void onAnimationUpdate(final ValueAnimator valueAnimator) {
            this.this$0.invalidate();
        }
    }
}
