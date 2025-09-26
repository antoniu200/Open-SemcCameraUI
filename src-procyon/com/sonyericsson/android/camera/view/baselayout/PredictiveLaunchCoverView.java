// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.baselayout;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.os.Bundle;
import android.view.View;
import android.view.View$AccessibilityDelegate;
import com.sonymobile.cameracommon.font.FontUtil;
import android.graphics.Canvas;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.Animatable2$AnimationCallback;
import com.sonyericsson.android.camera.research.LocalResearchUtil;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View$OnTouchListener;
import android.widget.FrameLayout;

public class PredictiveLaunchCoverView extends FrameLayout implements View$OnTouchListener
{
    private static final long LOWER_TOUCH_INTERVAL_TIME_MILLIS = 0L;
    private static final double TOUCH_AREA_SIZE_PER_DP = 0.5555555555555556;
    private static final long UPPER_TOUCH_INTERVAL_TIME_MILLIS = 300L;
    private TextView mDescription;
    private int mDescriptionMargin;
    private boolean mExists;
    private ImageView mHoleView;
    private PredictiveLaunchCoverTouchListener mListener;
    private int mOrientation;
    private FrameLayout mSquareBox;
    private FrameLayout mTouchArea;
    private long mTouchDownTime;
    
    public PredictiveLaunchCoverView(final Context context, final AttributeSet set) {
        super(context, set);
        this.mOrientation = 0;
    }
    
    private boolean checkTouchEnabled() {
        final long n = SystemClock.elapsedRealtime() - this.mTouchDownTime;
        return 0L < n && n <= 300L;
    }
    
    public static PredictiveLaunchCoverView inflate(final Context context, final PredictiveLaunchCoverTouchListener mListener, final PredictiveLaunchCoverType predictiveLaunchCoverType) {
        final PredictiveLaunchCoverView predictiveLaunchCoverView = (PredictiveLaunchCoverView)((LayoutInflater)context.getSystemService("layout_inflater")).inflate(2131492903, (ViewGroup)null);
        predictiveLaunchCoverView.mTouchArea.setContentDescription(context.getResources().getText(predictiveLaunchCoverType.mDescriptionResourceId));
        predictiveLaunchCoverView.mDescription.setText(predictiveLaunchCoverType.mDescriptionResourceId);
        predictiveLaunchCoverView.mListener = mListener;
        predictiveLaunchCoverView.mExists = true;
        return predictiveLaunchCoverView;
    }
    
    private void sendStartupPerformanceDataForReadyForUse() {
        LocalResearchUtil.getInstance().stopMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_LIFTTRIGGER_READY_FOR_USE);
        LocalResearchUtil.getInstance().stopMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_LIFTTRIGGER_READY_FOR_USE);
    }
    
    private void show() {
        if (this.getVisibility() != 0 && this.mDescription.isAttachedToWindow()) {
            this.setVisibility(0);
            this.mDescription.setVisibility(0);
            this.mHoleView.setSelected(false);
            this.startPredictiveLaunchAnimation(null);
        }
    }
    
    private void startPredictiveLaunchAnimation(final Animatable2$AnimationCallback animatable2$AnimationCallback) {
        final Animatable2 animatable2 = (Animatable2)this.mHoleView.getDrawable().getCurrent();
        if (!animatable2.isRunning()) {
            if (animatable2$AnimationCallback != null) {
                animatable2.registerAnimationCallback(animatable2$AnimationCallback);
            }
            animatable2.start();
        }
    }
    
    private void updateLayout() {
        if (this.mSquareBox != null && this.mTouchArea != null && this.mDescription != null && this.mDescription.isAttachedToWindow()) {
            final FrameLayout$LayoutParams layoutParams = new FrameLayout$LayoutParams(-1, this.getMeasuredWidth());
            layoutParams.gravity = 16;
            this.mSquareBox.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            final int n = (int)(0.5555555555555556 * this.getMeasuredWidth());
            final FrameLayout$LayoutParams layoutParams2 = new FrameLayout$LayoutParams(n, n);
            layoutParams2.gravity = 17;
            this.mTouchArea.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
            final FrameLayout$LayoutParams layoutParams3 = new FrameLayout$LayoutParams(-2, -2);
            if (this.mOrientation == 1) {
                layoutParams3.gravity = 81;
                layoutParams3.bottomMargin = this.mDescriptionMargin;
                this.mDescription.setRotation(0.0f);
            }
            else {
                layoutParams3.gravity = 19;
                layoutParams3.leftMargin = this.mDescription.getMeasuredWidth() / -2 + this.mDescription.getMeasuredHeight() / 2 + this.mDescriptionMargin;
                this.mDescription.setRotation(90.0f);
            }
            this.mDescription.setLayoutParams((ViewGroup$LayoutParams)layoutParams3);
        }
    }
    
    protected void dispatchDraw(final Canvas canvas) {
        super.dispatchDraw(canvas);
        this.sendStartupPerformanceDataForReadyForUse();
    }
    
    public boolean exists() {
        return this.mExists;
    }
    
    public void hide(final Animatable2$AnimationCallback animatable2$AnimationCallback) {
        this.mTouchArea.setOnTouchListener((View$OnTouchListener)null);
        this.mDescription.setVisibility(4);
        this.mHoleView.setSelected(true);
        this.mExists = false;
        this.startPredictiveLaunchAnimation(animatable2$AnimationCallback);
    }
    
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mHoleView = (ImageView)this.findViewById(2131296420);
        this.mDescription = (TextView)this.findViewById(2131296499);
        this.mSquareBox = (FrameLayout)this.findViewById(2131296624);
        this.mDescriptionMargin = this.getContext().getResources().getDimensionPixelSize(2131165499);
        (this.mTouchArea = (FrameLayout)this.findViewById(2131296669)).setOnTouchListener((View$OnTouchListener)this);
        FontUtil.setRobotoFont(this.mDescription, FontUtil.RobotoFontType.MEDIUM);
        this.mDescription.setAccessibilityDelegate((View$AccessibilityDelegate)new View$AccessibilityDelegate(this) {
            final PredictiveLaunchCoverView this$0;
            
            public boolean performAccessibilityAction(final View view, final int n, final Bundle bundle) {
                return false;
            }
        });
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        if (this.mOrientation != 0 && this.getVisibility() != 0 && this.mExists) {
            this.updateLayout();
            this.show();
        }
    }
    
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 1: {
                if (!this.checkTouchEnabled()) {
                    break;
                }
                final Rect rect = new Rect();
                this.mTouchArea.getDrawingRect(rect);
                if (rect.contains((int)motionEvent.getX(), (int)motionEvent.getY()) && this.mListener != null) {
                    this.mListener.onCircleTouched();
                    break;
                }
                break;
            }
            case 0: {
                this.mTouchDownTime = SystemClock.elapsedRealtime();
                break;
            }
        }
        return true;
    }
    
    public boolean performClick() {
        super.performClick();
        return true;
    }
    
    public void updateLayout(final int mOrientation) {
        final int mOrientation2 = this.mOrientation;
        if (mOrientation != 0) {
            this.mOrientation = mOrientation;
        }
        if (mOrientation2 != 0 && mOrientation2 != mOrientation) {
            this.updateLayout();
        }
    }
    
    public interface PredictiveLaunchCoverTouchListener
    {
        void onCircleTouched();
    }
    
    public enum PredictiveLaunchCoverType
    {
        private static final PredictiveLaunchCoverType[] $VALUES;
        
        TOUCH_TO_LAUNCH(2131690010), 
        TOUCH_TO_LAUNCH_AND_CAPTURE(2131690007);
        
        public final int mDescriptionResourceId;
        
        static {
            $VALUES = new PredictiveLaunchCoverType[] { PredictiveLaunchCoverType.TOUCH_TO_LAUNCH_AND_CAPTURE, PredictiveLaunchCoverType.TOUCH_TO_LAUNCH };
        }
        
        private PredictiveLaunchCoverType(final int mDescriptionResourceId) {
            this.mDescriptionResourceId = mDescriptionResourceId;
        }
    }
}
