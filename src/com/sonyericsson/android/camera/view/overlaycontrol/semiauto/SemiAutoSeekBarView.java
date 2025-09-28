package com.sonyericsson.android.camera.view.overlaycontrol.semiauto;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.R;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class SemiAutoSeekBarView extends LinearLayout implements View.OnTouchListener {
    private static final long FADE_ANIMATION_DURATION = 100;
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

    public interface OnSemiAutoSeekBarChangeListener {
        void onProgressChanged(SemiAutoSeekBarView semiAutoSeekBarView, int i, boolean z);

        void onStartTrackingTouch(SemiAutoSeekBarView semiAutoSeekBarView, int i);

        void onStopTrackingTouch(SemiAutoSeekBarView semiAutoSeekBarView, int i);
    }

    public void setUiOrientation(int i) {
    }

    public SemiAutoSeekBarView(Context context) {
        super(context);
        this.mMinimum = 0;
        this.mMaximum = 0;
        this.mLastPositionY = -1.0f;
        this.mCurrentProgress = 0;
        this.mIsAscending = true;
    }

    public SemiAutoSeekBarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mMinimum = 0;
        this.mMaximum = 0;
        this.mLastPositionY = -1.0f;
        this.mCurrentProgress = 0;
        this.mIsAscending = true;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mArea = findViewById(R.id.semi_auto_seek_area);
        this.mArea.setOnTouchListener(this);
        this.mKnob = findViewById(R.id.semi_auto_seek_knob);
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (getVisibility() != 0) {
            return false;
        }
        float y = motionEvent.getY();
        switch (motionEvent.getAction()) {
            case 0:
                doStartTracking();
                this.mLastPositionY = y;
                break;
            case 1:
            case 3:
                doStopTracking();
                this.mLastPositionY = -1.0f;
                break;
            case 2:
                if (this.mArea.isPressed()) {
                    ((CameraActivity) getContext()).restartAutoPowerOffTimer();
                    doTracking(y);
                    this.mLastPositionY = y;
                    break;
                }
                break;
        }
        return false;
    }

    @Override // android.view.View
    public void onWindowFocusChanged(boolean z) {
        if (!z && this.mArea.isPressed()) {
            doStopTracking();
            this.mLastPositionY = -1.0f;
        }
        super.onWindowFocusChanged(z);
    }

    @Override // android.view.View
    public void setVisibility(int i) {
        if (i == getVisibility()) {
            return;
        }
        if (i == 0) {
            this.mArea.setOnTouchListener(this);
        } else if (this.mArea.isPressed()) {
            this.mArea.setOnTouchListener(null);
            doStopTracking();
            this.mLastPositionY = -1.0f;
        }
        super.setVisibility(i);
    }

    public void show(boolean z) {
        if (getVisibility() == 0) {
            return;
        }
        setVisibility(0);
        if (z) {
            startFadeEffectAnimation(true);
        } else if (this.mFadeAnimation != null) {
            this.mFadeAnimation.cancel();
            this.mFadeAnimation = null;
        }
    }

    public void hide() {
        if (8 == getVisibility()) {
            return;
        }
        if (this.mFadeAnimation != null) {
            this.mFadeAnimation.cancel();
            this.mFadeAnimation = null;
        }
        setVisibility(8);
    }

    public void setOnSemiAutoSeekBarChangeListener(OnSemiAutoSeekBarChangeListener onSemiAutoSeekBarChangeListener) {
        this.mListener = onSemiAutoSeekBarChangeListener;
    }

    public void setMaximum(int i) {
        this.mMaximum = i;
    }

    public void setMinimum(int i) {
        this.mMinimum = i;
    }

    public void setTextForAccessibility(int i) {
        setContentDescription(getResources().getString(i));
    }

    private void setProgress(int i) {
        if (this.mMaximum < i) {
            this.mCurrentProgress = this.mMaximum;
        } else if (i < this.mMinimum) {
            this.mCurrentProgress = this.mMinimum;
        } else {
            this.mCurrentProgress = i;
        }
        if (isAttachedToWindow()) {
            float fConvertProgressToPosition = convertProgressToPosition(this.mCurrentProgress);
            if (-1.0f < fConvertProgressToPosition) {
                doProgress(fConvertProgressToPosition, false);
            }
        }
    }

    public void setSeekBarResource(int i) {
        findViewById(R.id.semi_auto_seek_area).setBackgroundResource(i);
    }

    public void setAscending(boolean z) {
        this.mIsAscending = z;
    }

    private void doStartTracking() {
        this.mArea.setPressed(true);
        if (this.mListener == null || this.mArea.getMeasuredHeight() <= 0) {
            return;
        }
        this.mListener.onStartTrackingTouch(this, this.mCurrentProgress);
    }

    private void doStopTracking() {
        this.mArea.setPressed(false);
        if (this.mListener == null || this.mArea.getMeasuredHeight() <= 0) {
            return;
        }
        this.mListener.onStopTrackingTouch(this, this.mCurrentProgress);
    }

    private void doTracking(float f) {
        float y = this.mKnob.getY() + (f - this.mLastPositionY);
        float padding = getPadding() - (this.mKnob.getMeasuredHeight() / 2.0f);
        if (y >= padding) {
            padding = y;
        }
        float measuredHeight = (this.mArea.getMeasuredHeight() - getPadding()) - (this.mKnob.getMeasuredHeight() / 2.0f);
        if (padding > measuredHeight) {
            padding = measuredHeight;
        }
        doProgress(padding, true);
    }

    private void doProgress(float f, boolean z) {
        this.mKnob.setY(f);
        int iConvertPositionToProgress = convertPositionToProgress(f);
        if (z && this.mCurrentProgress == iConvertPositionToProgress) {
            return;
        }
        this.mCurrentProgress = iConvertPositionToProgress;
        if (this.mListener == null || this.mArea.getMeasuredHeight() <= 0) {
            return;
        }
        this.mListener.onProgressChanged(this, this.mCurrentProgress, z);
    }

    private int getPadding() {
        return getResources().getDimensionPixelSize(R.dimen.seek_bar_view_seek_area_padding);
    }

    private int convertPositionToProgress(float f) {
        float measuredHeight;
        if (0.0f >= getStrokeRange()) {
            return 0;
        }
        if (this.mIsAscending) {
            measuredHeight = (f - getPadding()) + (this.mKnob.getMeasuredHeight() / 2.0f);
        } else {
            measuredHeight = ((this.mArea.getMeasuredHeight() - getPadding()) - (this.mKnob.getMeasuredHeight() / 2.0f)) - f;
        }
        int iCeil = ((int) Math.ceil(measuredHeight / r0)) + this.mMinimum;
        if (this.mMaximum < iCeil) {
            return this.mMaximum;
        }
        return iCeil < this.mMinimum ? this.mMinimum : iCeil;
    }

    private float convertProgressToPosition(int i) {
        float f;
        if (this.mMaximum < i) {
            i = this.mMaximum;
        } else if (i < this.mMinimum) {
            i = this.mMinimum;
        }
        float strokeRange = getStrokeRange();
        if (0.0f > strokeRange) {
            f = -1.0f;
        } else if ((this.mMaximum - this.mMinimum) % 2 != 0) {
            f = ((i - this.mMinimum) * strokeRange) - (strokeRange / 2.0f);
        } else {
            f = (i - this.mMinimum) * strokeRange;
        }
        if (!this.mIsAscending) {
            return (f + getPadding()) - (this.mKnob.getMeasuredHeight() / 2.0f);
        }
        return (((f * (-1.0f)) + this.mArea.getMeasuredHeight()) - getPadding()) - (this.mKnob.getMeasuredHeight() / 2.0f);
    }

    private float getStrokeRange() {
        if (this.mMaximum <= this.mMinimum) {
            return 0.0f;
        }
        return (this.mArea.getMeasuredHeight() - (2.0f * getPadding())) / (this.mMaximum - this.mMinimum);
    }

    public void moveToCenterProgress() {
        int i;
        if (this.mMinimum >= this.mMaximum || this.mCurrentProgress == (i = (this.mMaximum + this.mMinimum) / 2)) {
            return;
        }
        setProgress(i);
    }

    private void startFadeEffectAnimation(boolean z) {
        float f;
        float f2 = z ? 0.0f : 1.0f;
        float f3 = z ? 1.0f : 0.0f;
        long j = FADE_ANIMATION_DURATION;
        if (this.mFadeAnimation == null || !this.mFadeAnimation.isRunning()) {
            f = f2;
        } else {
            this.mFadeAnimation.cancel();
            float currentProgress = this.mFadeAnimation.getCurrentProgress();
            f = z ? 1.0f - currentProgress : currentProgress;
            j = (long) (100.0f * currentProgress);
        }
        this.mFadeAnimation = new SemiAutoViewFadeAnimation(f, f3);
        this.mFadeAnimation.setDuration(j);
        startAnimation(this.mFadeAnimation);
    }

    private static class SemiAutoViewFadeAnimation extends AlphaAnimation implements Animation.AnimationListener {
        private long mStartTime;

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }

        private SemiAutoViewFadeAnimation(float f, float f2) {
            super(f, f2);
            this.mStartTime = -1L;
            setAnimationListener(this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public float getCurrentProgress() {
            long jUptimeMillis = SystemClock.uptimeMillis() - this.mStartTime;
            if (jUptimeMillis < getDuration()) {
                return jUptimeMillis / getDuration();
            }
            return -1.0f;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isRunning() {
            return 0 < this.mStartTime && SystemClock.uptimeMillis() - this.mStartTime < getDuration();
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
            this.mStartTime = SystemClock.uptimeMillis();
        }
    }
}
