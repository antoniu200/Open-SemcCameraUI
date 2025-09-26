// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.cameracommon.view;

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

public class SelfTimerCountDownCircleView extends View
{
    private static final float ANIMATION_TIME_RATIO = 0.6f;
    private static final float ANIMATION_VALUE = 360.0f;
    public static final String TAG = "SelfTimerCountDownCircleView";
    private final int COUNTDOWN_CIRCLE_RADIUS;
    private final int COUNTDOWN_CIRCLE_STROKE_WIDTH;
    private Paint mPaint;
    private ValueAnimator mValueAnimator;
    
    private SelfTimerCountDownCircleView(final Context context) {
        super(context);
        this.COUNTDOWN_CIRCLE_RADIUS = this.getResources().getDimensionPixelSize(2131165715) / 2;
        this.COUNTDOWN_CIRCLE_STROKE_WIDTH = (this.getResources().getDimensionPixelSize(2131165715) - this.getResources().getDimensionPixelSize(2131165714)) / 2;
        this.mValueAnimator = null;
        this.init(context);
    }
    
    public SelfTimerCountDownCircleView(final Context context, final AttributeSet set) {
        super(context, set);
        this.COUNTDOWN_CIRCLE_RADIUS = this.getResources().getDimensionPixelSize(2131165715) / 2;
        this.COUNTDOWN_CIRCLE_STROKE_WIDTH = (this.getResources().getDimensionPixelSize(2131165715) - this.getResources().getDimensionPixelSize(2131165714)) / 2;
        this.mValueAnimator = null;
        this.init(context);
    }
    
    private SelfTimerCountDownCircleView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.COUNTDOWN_CIRCLE_RADIUS = this.getResources().getDimensionPixelSize(2131165715) / 2;
        this.COUNTDOWN_CIRCLE_STROKE_WIDTH = (this.getResources().getDimensionPixelSize(2131165715) - this.getResources().getDimensionPixelSize(2131165714)) / 2;
        this.mValueAnimator = null;
        this.init(context);
    }
    
    private void drawArc(final Canvas canvas) {
        if (this.mValueAnimator != null && this.mValueAnimator.getAnimatedValue() != null && this.mValueAnimator.isRunning()) {
            final float n = (float)(this.COUNTDOWN_CIRCLE_RADIUS - this.COUNTDOWN_CIRCLE_STROKE_WIDTH / 2);
            final float n2 = canvas.getWidth() / 2.0f;
            final float n3 = canvas.getHeight() / 2.0f;
            canvas.drawArc(new RectF(n2 - n, n3 - n, n2 + n, n3 + n), -90.0f, (float)this.mValueAnimator.getAnimatedValue(), false, this.mPaint);
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
    
    private void init(final Context context) {
        (this.mPaint = new Paint()).setStyle(Paint$Style.STROKE);
        this.mPaint.setStrokeWidth((float)this.COUNTDOWN_CIRCLE_STROKE_WIDTH);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(this.getResources().getColor(2131099775, (Resources$Theme)null));
    }
    
    private void setAnimator(final SelfTimerInterface selfTimerInterface) {
        (this.mValueAnimator = ValueAnimator.ofFloat(new float[] { 0.0f, 360.0f })).setInterpolator((TimeInterpolator)new CircleDecelerateInterpolator());
        this.mValueAnimator.setRepeatMode(1);
        this.mValueAnimator.setDuration((long)this.getRotateDuration(selfTimerInterface));
        this.mValueAnimator.setRepeatCount(selfTimerInterface.getDurationInMillisecond() / 1000 - 1);
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
        final SelfTimerCountDownCircleView this$0;
        
        private ValueAnimationUpdater(final SelfTimerCountDownCircleView this$0) {
            this.this$0 = this$0;
        }
        
        public void onAnimationUpdate(final ValueAnimator valueAnimator) {
            this.this$0.invalidate();
        }
    }
}
