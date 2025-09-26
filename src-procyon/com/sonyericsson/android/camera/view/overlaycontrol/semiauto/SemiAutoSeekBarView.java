// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.overlaycontrol.semiauto;

import android.os.SystemClock;
import android.view.animation.Animation$AnimationListener;
import android.view.animation.AlphaAnimation;
import com.sonyericsson.android.camera.CameraActivity;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import android.view.View$OnTouchListener;
import android.widget.LinearLayout;

class SemiAutoSeekBarView extends LinearLayout implements View$OnTouchListener
{
    private static final long FADE_ANIMATION_DURATION = 100L;
    private static final int NON_TRACKING_POSITION = -1;
    private View mArea;
    private int mCurrentProgress;
    private SemiAutoViewFadeAnimation mFadeAnimation;
    private boolean mIsAscending;
    private View mKnob;
    private float mLastPositionY;
    private OnSemiAutoSeekBarChangeListener mListener;
    private int mMaximum;
    private int mMinimum;
    
    public SemiAutoSeekBarView(final Context context) {
        super(context);
        this.mMinimum = 0;
        this.mMaximum = 0;
        this.mLastPositionY = -1.0f;
        this.mCurrentProgress = 0;
        this.mIsAscending = true;
    }
    
    public SemiAutoSeekBarView(final Context context, final AttributeSet set) {
        super(context, set);
        this.mMinimum = 0;
        this.mMaximum = 0;
        this.mLastPositionY = -1.0f;
        this.mCurrentProgress = 0;
        this.mIsAscending = true;
    }
    
    private int convertPositionToProgress(float n) {
        final float strokeRange = this.getStrokeRange();
        int n3;
        if (0.0f < strokeRange) {
            if (this.mIsAscending) {
                n = n - this.getPadding() + this.mKnob.getMeasuredHeight() / 2.0f;
            }
            else {
                n = this.mArea.getMeasuredHeight() - this.getPadding() - this.mKnob.getMeasuredHeight() / 2.0f - n;
            }
            final int n2 = (int)Math.ceil(n / strokeRange) + this.mMinimum;
            if (this.mMaximum < n2) {
                n3 = this.mMaximum;
            }
            else if ((n3 = n2) < this.mMinimum) {
                n3 = this.mMinimum;
            }
        }
        else {
            n3 = 0;
        }
        return n3;
    }
    
    private float convertProgressToPosition(final int n) {
        int n2;
        if (this.mMaximum < n) {
            n2 = this.mMaximum;
        }
        else if ((n2 = n) < this.mMinimum) {
            n2 = this.mMinimum;
        }
        final float strokeRange = this.getStrokeRange();
        float n3;
        if (0.0f <= strokeRange) {
            if ((this.mMaximum - this.mMinimum) % 2 != 0) {
                n3 = (n2 - this.mMinimum) * strokeRange - strokeRange / 2.0f;
            }
            else {
                n3 = (n2 - this.mMinimum) * strokeRange;
            }
        }
        else {
            n3 = -1.0f;
        }
        float n4;
        if (!this.mIsAscending) {
            n4 = n3 + this.getPadding() - this.mKnob.getMeasuredHeight() / 2.0f;
        }
        else {
            n4 = n3 * -1.0f + this.mArea.getMeasuredHeight() - this.getPadding() - this.mKnob.getMeasuredHeight() / 2.0f;
        }
        return n4;
    }
    
    private void doProgress(final float y, final boolean b) {
        this.mKnob.setY(y);
        final int convertPositionToProgress = this.convertPositionToProgress(y);
        if (!b || this.mCurrentProgress != convertPositionToProgress) {
            this.mCurrentProgress = convertPositionToProgress;
            if (this.mListener != null && this.mArea.getMeasuredHeight() > 0) {
                this.mListener.onProgressChanged(this, this.mCurrentProgress, b);
            }
        }
    }
    
    private void doStartTracking() {
        this.mArea.setPressed(true);
        if (this.mListener != null && this.mArea.getMeasuredHeight() > 0) {
            this.mListener.onStartTrackingTouch(this, this.mCurrentProgress);
        }
    }
    
    private void doStopTracking() {
        this.mArea.setPressed(false);
        if (this.mListener != null && this.mArea.getMeasuredHeight() > 0) {
            this.mListener.onStopTrackingTouch(this, this.mCurrentProgress);
        }
    }
    
    private void doTracking(float n) {
        n = this.mKnob.getY() + (n - this.mLastPositionY);
        final float n2 = this.getPadding() - this.mKnob.getMeasuredHeight() / 2.0f;
        if (n < n2) {
            n = n2;
        }
        final float n3 = this.mArea.getMeasuredHeight() - this.getPadding() - this.mKnob.getMeasuredHeight() / 2.0f;
        float n4 = n;
        if (n > n3) {
            n4 = n3;
        }
        this.doProgress(n4, true);
    }
    
    private int getPadding() {
        return this.getResources().getDimensionPixelSize(2131165569);
    }
    
    private float getStrokeRange() {
        if (this.mMaximum <= this.mMinimum) {
            return 0.0f;
        }
        return (this.mArea.getMeasuredHeight() - 2.0f * this.getPadding()) / (this.mMaximum - this.mMinimum);
    }
    
    private void setProgress(final int mCurrentProgress) {
        if (this.mMaximum < mCurrentProgress) {
            this.mCurrentProgress = this.mMaximum;
        }
        else if (mCurrentProgress < this.mMinimum) {
            this.mCurrentProgress = this.mMinimum;
        }
        else {
            this.mCurrentProgress = mCurrentProgress;
        }
        if (!this.isAttachedToWindow()) {
            return;
        }
        final float convertProgressToPosition = this.convertProgressToPosition(this.mCurrentProgress);
        if (-1.0f < convertProgressToPosition) {
            this.doProgress(convertProgressToPosition, false);
        }
    }
    
    private void startFadeEffectAnimation(final boolean b) {
        float n = 0.0f;
        float n2;
        if (b) {
            n2 = 0.0f;
        }
        else {
            n2 = 1.0f;
        }
        if (b) {
            n = 1.0f;
        }
        long duration = 100L;
        if (this.mFadeAnimation != null && this.mFadeAnimation.isRunning()) {
            this.mFadeAnimation.cancel();
            final float access$100 = this.mFadeAnimation.getCurrentProgress();
            if (b) {
                n2 = 1.0f - access$100;
            }
            else {
                n2 = access$100;
            }
            duration = (long)(100.0f * access$100);
        }
        (this.mFadeAnimation = new SemiAutoViewFadeAnimation(n2, n)).setDuration(duration);
        this.startAnimation((Animation)this.mFadeAnimation);
    }
    
    public void hide() {
        if (8 == this.getVisibility()) {
            return;
        }
        if (this.mFadeAnimation != null) {
            this.mFadeAnimation.cancel();
            this.mFadeAnimation = null;
        }
        this.setVisibility(8);
    }
    
    public void moveToCenterProgress() {
        if (this.mMinimum < this.mMaximum) {
            final int progress = (this.mMaximum + this.mMinimum) / 2;
            if (this.mCurrentProgress != progress) {
                this.setProgress(progress);
            }
        }
    }
    
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        (this.mArea = this.findViewById(2131296591)).setOnTouchListener((View$OnTouchListener)this);
        this.mKnob = this.findViewById(2131296592);
    }
    
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        if (this.getVisibility() != 0) {
            return false;
        }
        final float y = motionEvent.getY();
        switch (motionEvent.getAction()) {
            case 2: {
                if (!this.mArea.isPressed()) {
                    return false;
                }
                ((CameraActivity)this.getContext()).restartAutoPowerOffTimer();
                this.doTracking(y);
                this.mLastPositionY = y;
                break;
            }
            case 1:
            case 3: {
                this.doStopTracking();
                this.mLastPositionY = -1.0f;
                break;
            }
            case 0: {
                this.doStartTracking();
                this.mLastPositionY = y;
                break;
            }
        }
        return true;
    }
    
    public void onWindowFocusChanged(final boolean b) {
        if (!b && this.mArea.isPressed()) {
            this.doStopTracking();
            this.mLastPositionY = -1.0f;
        }
        super.onWindowFocusChanged(b);
    }
    
    public void setAscending(final boolean mIsAscending) {
        this.mIsAscending = mIsAscending;
    }
    
    public void setMaximum(final int mMaximum) {
        this.mMaximum = mMaximum;
    }
    
    public void setMinimum(final int mMinimum) {
        this.mMinimum = mMinimum;
    }
    
    public void setOnSemiAutoSeekBarChangeListener(final OnSemiAutoSeekBarChangeListener mListener) {
        this.mListener = mListener;
    }
    
    public void setSeekBarResource(final int backgroundResource) {
        this.findViewById(2131296591).setBackgroundResource(backgroundResource);
    }
    
    public void setTextForAccessibility(final int n) {
        this.setContentDescription((CharSequence)this.getResources().getString(n));
    }
    
    public void setUiOrientation(final int n) {
    }
    
    public void setVisibility(final int visibility) {
        if (visibility == this.getVisibility()) {
            return;
        }
        if (visibility == 0) {
            this.mArea.setOnTouchListener((View$OnTouchListener)this);
        }
        else if (this.mArea.isPressed()) {
            this.mArea.setOnTouchListener((View$OnTouchListener)null);
            this.doStopTracking();
            this.mLastPositionY = -1.0f;
        }
        super.setVisibility(visibility);
    }
    
    public void show(final boolean b) {
        if (this.getVisibility() == 0) {
            return;
        }
        this.setVisibility(0);
        if (b) {
            this.startFadeEffectAnimation(true);
        }
        else if (this.mFadeAnimation != null) {
            this.mFadeAnimation.cancel();
            this.mFadeAnimation = null;
        }
    }
    
    public interface OnSemiAutoSeekBarChangeListener
    {
        void onProgressChanged(final SemiAutoSeekBarView p0, final int p1, final boolean p2);
        
        void onStartTrackingTouch(final SemiAutoSeekBarView p0, final int p1);
        
        void onStopTrackingTouch(final SemiAutoSeekBarView p0, final int p1);
    }
    
    private static class SemiAutoViewFadeAnimation extends AlphaAnimation implements Animation$AnimationListener
    {
        private long mStartTime;
        
        private SemiAutoViewFadeAnimation(final float n, final float n2) {
            super(n, n2);
            this.mStartTime = -1L;
            this.setAnimationListener((Animation$AnimationListener)this);
        }
        
        private float getCurrentProgress() {
            final long n = SystemClock.uptimeMillis() - this.mStartTime;
            if (n < this.getDuration()) {
                return n / (float)this.getDuration();
            }
            return -1.0f;
        }
        
        private boolean isRunning() {
            return 0L < this.mStartTime && SystemClock.uptimeMillis() - this.mStartTime < this.getDuration();
        }
        
        public void onAnimationEnd(final Animation animation) {
        }
        
        public void onAnimationRepeat(final Animation animation) {
        }
        
        public void onAnimationStart(final Animation animation) {
            this.mStartTime = SystemClock.uptimeMillis();
        }
    }
}
