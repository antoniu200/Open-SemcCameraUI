package com.sonyericsson.cameracommon.rotatableview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import com.sonyericsson.cameracommon.utility.RotationUtil;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class RotatableToast extends FrameLayout implements Animation.AnimationListener {
    private static final long FADEOUT_OFFSET_LONG = 4500;
    private static final long FADEOUT_OFFSET_SHORT = 2500;
    public static final int LENGTH_LONG = 1;
    public static final int LENGTH_SHORT = 0;
    public static final String TAG = "RotatableToast";
    private static ToastLayoutParams mLayoutParamsLandscape;
    private static ToastLayoutParams mLayoutParamsPortrait;
    private ViewGroup mBackgroundView;
    private ViewGroup mBaseView;
    private int mDuration;
    private Animation mFadeoutAnimation;
    Handler mHandler;
    private ViewGroup mLayoutRoot;
    private int mSensorOrientation;
    private final Runnable mStartAnimation;
    private TextView mTextView;
    private ToastPosition mToastPosition;

    public enum ToastPosition {
        TOP,
        CENTER,
        BOTTOM
    }

    @Override // android.view.animation.Animation.AnimationListener
    public void onAnimationRepeat(Animation animation) {
    }

    public static class ToastLayoutParams {
        public final Rect bottomContainer;
        public final Rect topContainer;

        public ToastLayoutParams(int i, int i2, Rect rect, Rect rect2) {
            this.topContainer = rect;
            int i3 = (-i) / 2;
            int i4 = (-i2) / 2;
            this.topContainer.offset(i3, i4);
            this.bottomContainer = rect2;
            this.bottomContainer.offset(i3, i4);
        }
    }

    public static void setToastLayoutParams(ToastLayoutParams toastLayoutParams, ToastLayoutParams toastLayoutParams2) {
        mLayoutParamsLandscape = toastLayoutParams;
        mLayoutParamsPortrait = toastLayoutParams2;
    }

    public RotatableToast(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mBaseView = null;
        this.mBackgroundView = null;
        this.mTextView = null;
        this.mSensorOrientation = 2;
        this.mDuration = 0;
        this.mToastPosition = ToastPosition.CENTER;
        this.mHandler = new Handler();
        this.mStartAnimation = new Runnable() { // from class: com.sonyericsson.cameracommon.rotatableview.RotatableToast.1
            @Override // java.lang.Runnable
            public void run() {
                RotatableToast.this.mBaseView.startAnimation(RotatableToast.this.mFadeoutAnimation);
            }
        };
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        if (CamLog.VERBOSE) {
            CamLog.d("onFinishInflate() is called.");
        }
        super.onFinishInflate();
        this.mLayoutRoot = (ViewGroup) ((Activity) getContext()).getWindow().getDecorView();
        this.mBaseView = (ViewGroup) findViewById(R.id.rotatable_toast_base);
        this.mBackgroundView = (ViewGroup) findViewById(R.id.rotatable_toast_text_background);
        this.mTextView = (TextView) findViewById(R.id.rotatable_toast_text);
        this.mFadeoutAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.rotatable_toast_anime);
    }

    public void setTextResId(int i) {
        this.mTextView.setText(i);
    }

    public void setSensorOrientation(int i) {
        if (CamLog.VERBOSE) {
            CamLog.d("setSensorOrientation(" + i + ")");
        }
        this.mSensorOrientation = i;
        updateTextMaxWidth();
        updatePosition();
    }

    public void setDuration(int i) {
        this.mDuration = i;
    }

    public int getDuration() {
        return this.mDuration;
    }

    public void setToastPosition(ToastPosition toastPosition) {
        this.mToastPosition = toastPosition;
    }

    private void updateTextMaxWidth() {
        if (this.mSensorOrientation == 1) {
            this.mTextView.setMaxWidth(getResources().getDimensionPixelSize(R.dimen.rotatable_toast_max_width_for_port));
        } else {
            this.mTextView.setMaxWidth(getResources().getDimensionPixelSize(R.dimen.rotatable_toast_max_width_for_land));
        }
    }

    private void updatePosition() {
        float angle = RotationUtil.getAngle(this.mSensorOrientation);
        if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            angle += 90.0f;
        }
        this.mBackgroundView.setRotation(angle);
        if (getContainerRect() != null) {
            this.mBackgroundView.setTranslationX(r0.centerX());
            this.mBackgroundView.setTranslationY(r0.centerY());
        } else {
            this.mBackgroundView.setTranslationX(0.0f);
            this.mBackgroundView.setTranslationY(0.0f);
        }
    }

    private Rect getContainerRect() {
        ToastLayoutParams toastLayoutParams = this.mSensorOrientation == 1 ? mLayoutParamsPortrait : mLayoutParamsLandscape;
        if (toastLayoutParams == null) {
            return null;
        }
        switch (this.mToastPosition) {
        }
        return null;
    }

    public void show() {
        if (CamLog.VERBOSE) {
            CamLog.d("show: visibility: " + getVisibility());
        }
        this.mBaseView.setContentDescription(this.mTextView.getText());
        addToWindow();
        updateTextMaxWidth();
        updatePosition();
        long j = this.mDuration == 1 ? FADEOUT_OFFSET_LONG : FADEOUT_OFFSET_SHORT;
        this.mFadeoutAnimation.setAnimationListener(this);
        this.mHandler.postDelayed(this.mStartAnimation, j);
    }

    public void hide() {
        this.mBaseView.clearAnimation();
    }

    public void hideImmediately() {
        this.mBaseView.setVisibility(4);
        Animation animation = this.mBaseView.getAnimation();
        if (animation != null) {
            if (CamLog.VERBOSE) {
                CamLog.d("Animation.hasStarted: " + animation.hasStarted() + ", Animation.hasEnded: " + animation.hasEnded());
            }
            if (!animation.hasStarted() && !animation.hasEnded()) {
                this.mBaseView.clearAnimation();
                removeFromWindow();
                return;
            } else {
                this.mBaseView.clearAnimation();
                return;
            }
        }
        this.mHandler.removeCallbacks(this.mStartAnimation);
        removeFromWindow();
    }

    private void addToWindow() {
        if (CamLog.VERBOSE) {
            CamLog.d("addToWindow: Add this view to window.");
        }
        if (getParent() != null) {
            this.mLayoutRoot.removeView(this);
        }
        this.mLayoutRoot.addView(this);
    }

    private void removeFromWindow() {
        if (CamLog.VERBOSE) {
            CamLog.d("removeFromWindow: Remove this view from window.");
        }
        if (getParent() != null) {
            this.mLayoutRoot.removeView(this);
        }
    }

    @Override // android.view.animation.Animation.AnimationListener
    public void onAnimationEnd(Animation animation) {
        if (CamLog.VERBOSE) {
            CamLog.d("onAnimationEnd() is called.");
        }
        removeFromWindow();
    }

    @Override // android.view.animation.Animation.AnimationListener
    public void onAnimationStart(Animation animation) {
        if (CamLog.VERBOSE) {
            CamLog.d("onAnimationStart() is called.");
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean requestSendAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
        if (CamLog.VERBOSE) {
            CamLog.d("requestSendAccessibilityEvent: ecent = " + accessibilityEvent.toString());
        }
        if (this.mBaseView.getContentDescription() == null) {
            return false;
        }
        accessibilityEvent.setEventType(64);
        boolean zRequestSendAccessibilityEvent = super.requestSendAccessibilityEvent(view, accessibilityEvent);
        this.mBaseView.setContentDescription(null);
        return zRequestSendAccessibilityEvent;
    }

    public static RotatableToast inflate(Activity activity) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        if (layoutInflater == null) {
            throw new AssertionError("LayoutInflater not found.");
        }
        return (RotatableToast) layoutInflater.inflate(R.layout.rotatable_toast, (ViewGroup) null);
    }
}
