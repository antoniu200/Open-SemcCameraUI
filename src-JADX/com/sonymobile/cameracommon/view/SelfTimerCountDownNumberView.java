package com.sonymobile.cameracommon.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.PowerManager;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.google.android.gms.common.ConnectionResult;
import com.sonyericsson.android.camera.Constants;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.settings.SelfTimerInterface;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SelfTimerCountDownNumberView extends ImageView {
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

    private float getAnimationRatio(int i) {
        return 250.0f / i;
    }

    static /* synthetic */ int access$410(SelfTimerCountDownNumberView selfTimerCountDownNumberView) {
        int i = selfTimerCountDownNumberView.mCurrentCount;
        selfTimerCountDownNumberView.mCurrentCount = i - 1;
        return i;
    }

    public SelfTimerCountDownNumberView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mValueAnimator = null;
        this.mCurrentCount = 0;
        this.mIsCountDownStarted = false;
        this.handler = new Handler();
        this.mUpdateCountDownNumberTask = null;
        this.mAnimationEventHandler = new AnimationEventHandler();
        this.mCountDownIconId = -1;
    }

    public SelfTimerCountDownNumberView(Context context) {
        super(context);
        this.mValueAnimator = null;
        this.mCurrentCount = 0;
        this.mIsCountDownStarted = false;
        this.handler = new Handler();
        this.mUpdateCountDownNumberTask = null;
        this.mAnimationEventHandler = new AnimationEventHandler();
        this.mCountDownIconId = -1;
    }

    public SelfTimerCountDownNumberView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mValueAnimator = null;
        this.mCurrentCount = 0;
        this.mIsCountDownStarted = false;
        this.handler = new Handler();
        this.mUpdateCountDownNumberTask = null;
        this.mAnimationEventHandler = new AnimationEventHandler();
        this.mCountDownIconId = -1;
    }

    @Override // android.widget.ImageView, android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void startAnimation() {
        if (((PowerManager) getContext().getSystemService("power")).isPowerSaveMode()) {
            this.mValueAnimator.removeAllListeners();
        } else {
            this.mValueAnimator.addListener(this.mAnimationEventHandler);
        }
        if (!this.mValueAnimator.isRunning()) {
            this.mValueAnimator.start();
        }
        if (this.mIsCountDownStarted) {
            return;
        }
        startCountDownNumber();
    }

    public void setIsTenDigit(boolean z) {
        this.mIsTenDigit = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setImage() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        if (this.mCurrentCount > 9) {
            if (this.mIsTenDigit) {
                layoutParams.rightMargin = getResources().getDimensionPixelSize(R.dimen.viewfinder_countdown_text_margin);
                if (getVisibility() != 0) {
                    setVisibility(0);
                }
            } else {
                layoutParams.leftMargin = getResources().getDimensionPixelSize(R.dimen.viewfinder_countdown_text_margin);
            }
        } else if (this.mIsTenDigit) {
            layoutParams.rightMargin = 0;
            if (getVisibility() != 8) {
                setVisibility(8);
            }
        } else {
            layoutParams.leftMargin = 0;
        }
        setLayoutParams(layoutParams);
        if (this.mCountDownIconId != -1) {
            setImageResource(this.mCountDownIconId);
            return;
        }
        if (this.mIsTenDigit) {
            switch (this.mCurrentCount / 10) {
                case 0:
                    break;
                case 1:
                    setImageResource(R.drawable.cam_selftimer_countdown_1_icn);
                    break;
                case 2:
                    setImageResource(R.drawable.cam_selftimer_countdown_2_icn);
                    break;
                case 3:
                    setImageResource(R.drawable.cam_selftimer_countdown_3_icn);
                    break;
                default:
                    if (this.mValueAnimator != null) {
                        CamLog.e("setImage() [Irregal Value = " + this.mValueAnimator.getAnimatedValue() + "]");
                        break;
                    } else {
                        CamLog.e("setImage() : mCurrentCount = " + this.mCurrentCount);
                        break;
                    }
            }
        }
        switch (this.mCurrentCount % 10) {
            case 0:
                if (this.mCurrentCount > 9) {
                    setImageResource(R.drawable.cam_selftimer_countdown_0_icn);
                    return;
                }
                break;
            case 1:
                setImageResource(R.drawable.cam_selftimer_countdown_1_icn);
                return;
            case 2:
                setImageResource(R.drawable.cam_selftimer_countdown_2_icn);
                return;
            case 3:
                setImageResource(R.drawable.cam_selftimer_countdown_3_icn);
                return;
            case 4:
                setImageResource(R.drawable.cam_selftimer_countdown_4_icn);
                return;
            case 5:
                setImageResource(R.drawable.cam_selftimer_countdown_5_icn);
                return;
            case 6:
                setImageResource(R.drawable.cam_selftimer_countdown_6_icn);
                return;
            case 7:
                setImageResource(R.drawable.cam_selftimer_countdown_7_icn);
                return;
            case 8:
                setImageResource(R.drawable.cam_selftimer_countdown_8_icn);
                return;
            case 9:
                setImageResource(R.drawable.cam_selftimer_countdown_9_icn);
                return;
        }
        if (this.mValueAnimator != null) {
            CamLog.e("setImage() [Irregal Value = " + this.mValueAnimator.getAnimatedValue() + "]");
            return;
        }
        CamLog.e("setImage() : mCurrentCount = " + this.mCurrentCount);
    }

    private int getDuration(SelfTimerInterface selfTimerInterface) {
        if (selfTimerInterface.getDurationInMillisecond() < 1000) {
            return Constants.INTERVAL_OPEN_CAMERA;
        }
        if (selfTimerInterface.getDurationInMillisecond() == 1500) {
            return ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED;
        }
        return 1000;
    }

    public void setSelfTimer(SelfTimerInterface selfTimerInterface) {
        this.mCountDownInitNum = selfTimerInterface.getDurationInMillisecond() / 1000;
        setAnimator(selfTimerInterface);
    }

    public void cancelSelfTimerAnimation() {
        this.mValueAnimator.end();
        stopCountDownNumber();
    }

    @Override // android.widget.ImageView, android.view.View
    public void setVisibility(int i) {
        super.setVisibility(i);
    }

    private void setAnimator(SelfTimerInterface selfTimerInterface) {
        this.mCountDownIconId = selfTimerInterface.getCountDownIconId();
        int duration = getDuration(selfTimerInterface);
        float animationRatio = getAnimationRatio(duration);
        this.mValueAnimator = ValueAnimator.ofInt(0, 255);
        this.mValueAnimator.setInterpolator(new AlphaAccelerateInterpolator(animationRatio));
        this.mValueAnimator.setRepeatCount(this.mCountDownInitNum - 1);
        this.mValueAnimator.setRepeatMode(1);
        this.mValueAnimator.setDuration(duration);
        this.mValueAnimator.addUpdateListener(new ValueAnimationUpdater());
    }

    private class ValueAnimationUpdater implements ValueAnimator.AnimatorUpdateListener {
        private ValueAnimationUpdater() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            if (SelfTimerCountDownNumberView.this.mValueAnimator == null || SelfTimerCountDownNumberView.this.mValueAnimator.getAnimatedValue() == null) {
                return;
            }
            SelfTimerCountDownNumberView.this.setImageAlpha(((Integer) SelfTimerCountDownNumberView.this.mValueAnimator.getAnimatedValue()).intValue());
            SelfTimerCountDownNumberView.this.invalidate();
        }
    }

    private class AnimationEventHandler implements Animator.AnimatorListener {
        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        private AnimationEventHandler() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            SelfTimerCountDownNumberView.this.setImageAlpha(0);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
            SelfTimerCountDownNumberView.this.setImageAlpha(0);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            SelfTimerCountDownNumberView.this.setImageBitmap(null);
        }
    }

    private static class AlphaAccelerateInterpolator extends AccelerateInterpolator {
        private final float mAnimationRatio;

        AlphaAccelerateInterpolator(float f) {
            this.mAnimationRatio = f;
        }

        @Override // android.view.animation.AccelerateInterpolator, android.animation.TimeInterpolator
        public float getInterpolation(float f) {
            float f2 = f * (1.0f / this.mAnimationRatio);
            if (1.0f <= f2) {
                f2 = 1.0f;
            }
            return super.getInterpolation(f2);
        }
    }

    private void startCountDownNumber() {
        this.mIsCountDownStarted = true;
        this.mUpdateCountDownNumberTask = new UpdateCountDownNumberTask();
        this.mCurrentCount = this.mCountDownInitNum;
        setImage();
        postNextUpdateEvent();
    }

    private void stopCountDownNumber() {
        this.mIsCountDownStarted = false;
        this.handler.removeCallbacks(this.mUpdateCountDownNumberTask);
        this.mUpdateCountDownNumberTask = null;
        setImageBitmap(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postNextUpdateEvent() {
        this.handler.postDelayed(this.mUpdateCountDownNumberTask, 1000L);
    }

    private class UpdateCountDownNumberTask implements Runnable {
        private UpdateCountDownNumberTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            SelfTimerCountDownNumberView.access$410(SelfTimerCountDownNumberView.this);
            SelfTimerCountDownNumberView.this.setImage();
            if (SelfTimerCountDownNumberView.this.mCurrentCount > 1) {
                SelfTimerCountDownNumberView.this.postNextUpdateEvent();
            }
        }
    }
}
