// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.rotatableview;

import android.view.accessibility.AccessibilityEvent;
import android.view.animation.AnimationUtils;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import android.view.LayoutInflater;
import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import com.sonyericsson.android.camera.util.CamLog;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.TextView;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.ViewGroup;
import android.view.animation.Animation$AnimationListener;
import android.widget.FrameLayout;

public class RotatableToast extends FrameLayout implements Animation$AnimationListener
{
    private static final long FADEOUT_OFFSET_LONG = 4500L;
    private static final long FADEOUT_OFFSET_SHORT = 2500L;
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
    
    public RotatableToast(final Context context, final AttributeSet set) {
        super(context, set);
        this.mBaseView = null;
        this.mBackgroundView = null;
        this.mTextView = null;
        this.mSensorOrientation = 2;
        this.mDuration = 0;
        this.mToastPosition = ToastPosition.CENTER;
        this.mHandler = new Handler();
        this.mStartAnimation = new Runnable() {
            final RotatableToast this$0;
            
            @Override
            public void run() {
                this.this$0.mBaseView.startAnimation(this.this$0.mFadeoutAnimation);
            }
        };
    }
    
    private void addToWindow() {
        if (CamLog.VERBOSE) {
            CamLog.d("addToWindow: Add this view to window.");
        }
        if (this.getParent() != null) {
            this.mLayoutRoot.removeView((View)this);
        }
        this.mLayoutRoot.addView((View)this);
    }
    
    private Rect getContainerRect() {
        ToastLayoutParams toastLayoutParams;
        if (this.mSensorOrientation == 1) {
            toastLayoutParams = RotatableToast.mLayoutParamsPortrait;
        }
        else {
            toastLayoutParams = RotatableToast.mLayoutParamsLandscape;
        }
        if (toastLayoutParams == null) {
            return null;
        }
        switch (RotatableToast$2.$SwitchMap$com$sonyericsson$cameracommon$rotatableview$RotatableToast$ToastPosition[this.mToastPosition.ordinal()]) {
            default: {
                return null;
            }
            case 2: {
                return toastLayoutParams.bottomContainer;
            }
            case 1: {
                return toastLayoutParams.topContainer;
            }
        }
    }
    
    public static RotatableToast inflate(final Activity activity) {
        final LayoutInflater layoutInflater = activity.getLayoutInflater();
        if (layoutInflater == null) {
            throw new AssertionError((Object)"LayoutInflater not found.");
        }
        return (RotatableToast)layoutInflater.inflate(2131492990, (ViewGroup)null);
    }
    
    private void removeFromWindow() {
        if (CamLog.VERBOSE) {
            CamLog.d("removeFromWindow: Remove this view from window.");
        }
        if (this.getParent() != null) {
            this.mLayoutRoot.removeView((View)this);
        }
    }
    
    public static void setToastLayoutParams(final ToastLayoutParams mLayoutParamsLandscape, final ToastLayoutParams mLayoutParamsPortrait) {
        RotatableToast.mLayoutParamsLandscape = mLayoutParamsLandscape;
        RotatableToast.mLayoutParamsPortrait = mLayoutParamsPortrait;
    }
    
    private void updatePosition() {
        float angle = RotationUtil.getAngle(this.mSensorOrientation);
        if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            angle += 90.0f;
        }
        this.mBackgroundView.setRotation(angle);
        final Rect containerRect = this.getContainerRect();
        if (containerRect != null) {
            this.mBackgroundView.setTranslationX((float)containerRect.centerX());
            this.mBackgroundView.setTranslationY((float)containerRect.centerY());
        }
        else {
            this.mBackgroundView.setTranslationX(0.0f);
            this.mBackgroundView.setTranslationY(0.0f);
        }
    }
    
    private void updateTextMaxWidth() {
        if (this.mSensorOrientation == 1) {
            this.mTextView.setMaxWidth(this.getResources().getDimensionPixelSize(2131165544));
        }
        else {
            this.mTextView.setMaxWidth(this.getResources().getDimensionPixelSize(2131165543));
        }
    }
    
    public int getDuration() {
        return this.mDuration;
    }
    
    public void hide() {
        this.mBaseView.clearAnimation();
    }
    
    public void hideImmediately() {
        this.mBaseView.setVisibility(4);
        final Animation animation = this.mBaseView.getAnimation();
        if (animation != null) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Animation.hasStarted: ");
                sb.append(animation.hasStarted());
                sb.append(", Animation.hasEnded: ");
                sb.append(animation.hasEnded());
                CamLog.d(sb.toString());
            }
            if (!animation.hasStarted() && !animation.hasEnded()) {
                this.mBaseView.clearAnimation();
                this.removeFromWindow();
            }
            else {
                this.mBaseView.clearAnimation();
            }
        }
        else {
            this.mHandler.removeCallbacks(this.mStartAnimation);
            this.removeFromWindow();
        }
    }
    
    public void onAnimationEnd(final Animation animation) {
        if (CamLog.VERBOSE) {
            CamLog.d("onAnimationEnd() is called.");
        }
        this.removeFromWindow();
    }
    
    public void onAnimationRepeat(final Animation animation) {
    }
    
    public void onAnimationStart(final Animation animation) {
        if (CamLog.VERBOSE) {
            CamLog.d("onAnimationStart() is called.");
        }
    }
    
    protected void onFinishInflate() {
        if (CamLog.VERBOSE) {
            CamLog.d("onFinishInflate() is called.");
        }
        super.onFinishInflate();
        this.mLayoutRoot = (ViewGroup)((Activity)this.getContext()).getWindow().getDecorView();
        this.mBaseView = (ViewGroup)this.findViewById(2131296544);
        this.mBackgroundView = (ViewGroup)this.findViewById(2131296546);
        this.mTextView = (TextView)this.findViewById(2131296545);
        this.mFadeoutAnimation = AnimationUtils.loadAnimation(this.getContext(), 2130771990);
    }
    
    public boolean requestSendAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("requestSendAccessibilityEvent: ecent = ");
            sb.append(accessibilityEvent.toString());
            CamLog.d(sb.toString());
        }
        if (this.mBaseView.getContentDescription() == null) {
            return false;
        }
        accessibilityEvent.setEventType(64);
        final boolean requestSendAccessibilityEvent = super.requestSendAccessibilityEvent(view, accessibilityEvent);
        this.mBaseView.setContentDescription((CharSequence)null);
        return requestSendAccessibilityEvent;
    }
    
    public void setDuration(final int mDuration) {
        this.mDuration = mDuration;
    }
    
    public void setSensorOrientation(final int n) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setSensorOrientation(");
            sb.append(n);
            sb.append(")");
            CamLog.d(sb.toString());
        }
        this.mSensorOrientation = n;
        this.updateTextMaxWidth();
        this.updatePosition();
    }
    
    public void setTextResId(final int text) {
        this.mTextView.setText(text);
    }
    
    public void setToastPosition(final ToastPosition mToastPosition) {
        this.mToastPosition = mToastPosition;
    }
    
    public void show() {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("show: visibility: ");
            sb.append(this.getVisibility());
            CamLog.d(sb.toString());
        }
        this.mBaseView.setContentDescription(this.mTextView.getText());
        this.addToWindow();
        this.updateTextMaxWidth();
        this.updatePosition();
        long n;
        if (this.mDuration == 1) {
            n = 4500L;
        }
        else {
            n = 2500L;
        }
        this.mFadeoutAnimation.setAnimationListener((Animation$AnimationListener)this);
        this.mHandler.postDelayed(this.mStartAnimation, n);
    }
    
    public static class ToastLayoutParams
    {
        public final Rect bottomContainer;
        public final Rect topContainer;
        
        public ToastLayoutParams(int n, int n2, Rect topContainer, final Rect bottomContainer) {
            this.topContainer = topContainer;
            topContainer = this.topContainer;
            n = -n / 2;
            n2 = -n2 / 2;
            topContainer.offset(n, n2);
            (this.bottomContainer = bottomContainer).offset(n, n2);
        }
    }
    
    public enum ToastPosition
    {
        private static final ToastPosition[] $VALUES;
        
        BOTTOM, 
        CENTER, 
        TOP;
        
        static {
            $VALUES = new ToastPosition[] { ToastPosition.TOP, ToastPosition.CENTER, ToastPosition.BOTTOM };
        }
    }
}
