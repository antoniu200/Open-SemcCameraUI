// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.cameracommon.view;

import android.animation.Animator;
import android.view.animation.AccelerateInterpolator;
import android.animation.Animator$AnimatorListener;
import android.os.PowerManager;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import com.sonyericsson.android.camera.util.CamLog;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import android.animation.ValueAnimator$AnimatorUpdateListener;
import android.animation.TimeInterpolator;
import com.sonyericsson.cameracommon.settings.SelfTimerInterface;
import android.util.AttributeSet;
import android.content.Context;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.widget.ImageView;

public class SelfTimerCountDownNumberView extends ImageView
{
    private static final int ANIMATION_FADING_DURATION_MS = 250;
    private static final int ANIMATION_VALUE = 255;
    private static final int SECOND_COUNT_MILLIS = 1000;
    public static final String TAG = "SelfTimerCountDownNumberView";
    private Handler handler;
    private AnimationEventHandler mAnimationEventHandler;
    private int mCountDownIconId;
    private int mCountDownInitNum;
    private int mCurrentCount;
    private boolean mIsCountDownStarted;
    private boolean mIsTenDigit;
    private UpdateCountDownNumberTask mUpdateCountDownNumberTask;
    private ValueAnimator mValueAnimator;
    
    public SelfTimerCountDownNumberView(final Context context) {
        super(context);
        this.mValueAnimator = null;
        this.mCurrentCount = 0;
        this.mIsCountDownStarted = false;
        this.handler = new Handler();
        this.mUpdateCountDownNumberTask = null;
        this.mAnimationEventHandler = new AnimationEventHandler();
        this.mCountDownIconId = -1;
    }
    
    public SelfTimerCountDownNumberView(final Context context, final AttributeSet set) {
        super(context, set);
        this.mValueAnimator = null;
        this.mCurrentCount = 0;
        this.mIsCountDownStarted = false;
        this.handler = new Handler();
        this.mUpdateCountDownNumberTask = null;
        this.mAnimationEventHandler = new AnimationEventHandler();
        this.mCountDownIconId = -1;
    }
    
    public SelfTimerCountDownNumberView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mValueAnimator = null;
        this.mCurrentCount = 0;
        this.mIsCountDownStarted = false;
        this.handler = new Handler();
        this.mUpdateCountDownNumberTask = null;
        this.mAnimationEventHandler = new AnimationEventHandler();
        this.mCountDownIconId = -1;
    }
    
    private float getAnimationRatio(final int n) {
        return 250.0f / n;
    }
    
    private int getDuration(final SelfTimerInterface selfTimerInterface) {
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
    
    private void postNextUpdateEvent() {
        this.handler.postDelayed((Runnable)this.mUpdateCountDownNumberTask, 1000L);
    }
    
    private void setAnimator(final SelfTimerInterface selfTimerInterface) {
        this.mCountDownIconId = selfTimerInterface.getCountDownIconId();
        final int duration = this.getDuration(selfTimerInterface);
        (this.mValueAnimator = ValueAnimator.ofInt(new int[] { 0, 255 })).setInterpolator((TimeInterpolator)new AlphaAccelerateInterpolator(this.getAnimationRatio(duration)));
        this.mValueAnimator.setRepeatCount(this.mCountDownInitNum - 1);
        this.mValueAnimator.setRepeatMode(1);
        this.mValueAnimator.setDuration((long)duration);
        this.mValueAnimator.addUpdateListener((ValueAnimator$AnimatorUpdateListener)new ValueAnimationUpdater());
    }
    
    private void setImage() {
        final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.getLayoutParams();
        if (this.mCurrentCount > 9) {
            if (this.mIsTenDigit) {
                layoutParams.rightMargin = this.getResources().getDimensionPixelSize(2131165717);
                if (this.getVisibility() != 0) {
                    this.setVisibility(0);
                }
            }
            else {
                layoutParams.leftMargin = this.getResources().getDimensionPixelSize(2131165717);
            }
        }
        else if (this.mIsTenDigit) {
            layoutParams.rightMargin = 0;
            if (this.getVisibility() != 8) {
                this.setVisibility(8);
            }
        }
        else {
            layoutParams.leftMargin = 0;
        }
        this.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        if (this.mCountDownIconId != -1) {
            this.setImageResource(this.mCountDownIconId);
        }
        else if (this.mIsTenDigit) {
            switch (this.mCurrentCount / 10) {
                default: {
                    if (this.mValueAnimator != null) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("setImage() [Irregal Value = ");
                        sb.append(this.mValueAnimator.getAnimatedValue());
                        sb.append("]");
                        CamLog.e(sb.toString());
                        break;
                    }
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("setImage() : mCurrentCount = ");
                    sb2.append(this.mCurrentCount);
                    CamLog.e(sb2.toString());
                    break;
                }
                case 0: {
                    break;
                }
                case 3: {
                    this.setImageResource(2131231282);
                    break;
                }
                case 2: {
                    this.setImageResource(2131231281);
                    break;
                }
                case 1: {
                    this.setImageResource(2131231280);
                    break;
                }
            }
        }
        else {
            switch (this.mCurrentCount % 10) {
                case 9: {
                    this.setImageResource(2131231288);
                    return;
                }
                case 8: {
                    this.setImageResource(2131231287);
                    return;
                }
                case 7: {
                    this.setImageResource(2131231286);
                    return;
                }
                case 6: {
                    this.setImageResource(2131231285);
                    return;
                }
                case 5: {
                    this.setImageResource(2131231284);
                    return;
                }
                case 4: {
                    this.setImageResource(2131231283);
                    return;
                }
                case 3: {
                    this.setImageResource(2131231282);
                    return;
                }
                case 2: {
                    this.setImageResource(2131231281);
                    return;
                }
                case 1: {
                    this.setImageResource(2131231280);
                    return;
                }
                case 0: {
                    if (this.mCurrentCount > 9) {
                        this.setImageResource(2131231279);
                        return;
                    }
                    break;
                }
            }
            if (this.mValueAnimator != null) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("setImage() [Irregal Value = ");
                sb3.append(this.mValueAnimator.getAnimatedValue());
                sb3.append("]");
                CamLog.e(sb3.toString());
            }
            else {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("setImage() : mCurrentCount = ");
                sb4.append(this.mCurrentCount);
                CamLog.e(sb4.toString());
            }
        }
    }
    
    private void startCountDownNumber() {
        this.mIsCountDownStarted = true;
        this.mUpdateCountDownNumberTask = new UpdateCountDownNumberTask();
        this.mCurrentCount = this.mCountDownInitNum;
        this.setImage();
        this.postNextUpdateEvent();
    }
    
    private void stopCountDownNumber() {
        this.mIsCountDownStarted = false;
        this.handler.removeCallbacks((Runnable)this.mUpdateCountDownNumberTask);
        this.mUpdateCountDownNumberTask = null;
        this.setImageBitmap((Bitmap)null);
    }
    
    public void cancelSelfTimerAnimation() {
        this.mValueAnimator.end();
        this.stopCountDownNumber();
    }
    
    public void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
    }
    
    public void setIsTenDigit(final boolean mIsTenDigit) {
        this.mIsTenDigit = mIsTenDigit;
    }
    
    public void setSelfTimer(final SelfTimerInterface animator) {
        this.mCountDownInitNum = animator.getDurationInMillisecond() / 1000;
        this.setAnimator(animator);
    }
    
    public void setVisibility(final int visibility) {
        super.setVisibility(visibility);
    }
    
    public void startAnimation() {
        if (((PowerManager)this.getContext().getSystemService("power")).isPowerSaveMode()) {
            this.mValueAnimator.removeAllListeners();
        }
        else {
            this.mValueAnimator.addListener((Animator$AnimatorListener)this.mAnimationEventHandler);
        }
        if (!this.mValueAnimator.isRunning()) {
            this.mValueAnimator.start();
        }
        if (!this.mIsCountDownStarted) {
            this.startCountDownNumber();
        }
    }
    
    private static class AlphaAccelerateInterpolator extends AccelerateInterpolator
    {
        private final float mAnimationRatio;
        
        AlphaAccelerateInterpolator(final float mAnimationRatio) {
            this.mAnimationRatio = mAnimationRatio;
        }
        
        public float getInterpolation(float n) {
            if (1.0f <= (n *= 1.0f / this.mAnimationRatio)) {
                n = 1.0f;
            }
            return super.getInterpolation(n);
        }
    }
    
    private class AnimationEventHandler implements Animator$AnimatorListener
    {
        final SelfTimerCountDownNumberView this$0;
        
        private AnimationEventHandler(final SelfTimerCountDownNumberView this$0) {
            this.this$0 = this$0;
        }
        
        public void onAnimationCancel(final Animator animator) {
        }
        
        public void onAnimationEnd(final Animator animator) {
            this.this$0.setImageBitmap((Bitmap)null);
        }
        
        public void onAnimationRepeat(final Animator animator) {
            this.this$0.setImageAlpha(0);
        }
        
        public void onAnimationStart(final Animator animator) {
            this.this$0.setImageAlpha(0);
        }
    }
    
    private class UpdateCountDownNumberTask implements Runnable
    {
        final SelfTimerCountDownNumberView this$0;
        
        private UpdateCountDownNumberTask(final SelfTimerCountDownNumberView this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            this.this$0.mCurrentCount--;
            this.this$0.setImage();
            if (this.this$0.mCurrentCount > 1) {
                this.this$0.postNextUpdateEvent();
            }
        }
    }
    
    private class ValueAnimationUpdater implements ValueAnimator$AnimatorUpdateListener
    {
        final SelfTimerCountDownNumberView this$0;
        
        private ValueAnimationUpdater(final SelfTimerCountDownNumberView this$0) {
            this.this$0 = this$0;
        }
        
        public void onAnimationUpdate(final ValueAnimator valueAnimator) {
            if (this.this$0.mValueAnimator != null && this.this$0.mValueAnimator.getAnimatedValue() != null) {
                this.this$0.setImageAlpha((int)this.this$0.mValueAnimator.getAnimatedValue());
                this.this$0.invalidate();
            }
        }
    }
}
