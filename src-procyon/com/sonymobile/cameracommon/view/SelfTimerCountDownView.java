// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.cameracommon.view;

import android.view.View;
import android.widget.TextView;
import android.widget.LinearLayout;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import com.sonyericsson.cameracommon.settings.SelfTimerInterface;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.FrameLayout;

public class SelfTimerCountDownView extends FrameLayout
{
    boolean mIsHideHintText;
    private SelfTimerCountDownNumberView mLeftSelfTimerCountDownNumberView;
    private SelfTimerCountDownNumberView mLeftSelfTimerCountDownNumberViewPort;
    private SelfTimerCountDownNumberView mRightSelfTimerCountDownNumberView;
    private SelfTimerCountDownNumberView mRightSelfTimerCountDownNumberViewPort;
    private SelfTimerCountDownCircleView mSelfTimerCountDownCircleView;
    private SelfTimerCountDownCircleView mSelfTimerCountDownCircleViewPort;
    
    public SelfTimerCountDownView(final Context context, final AttributeSet set) {
        super(context, set);
        this.mIsHideHintText = false;
    }
    
    public void cancelSelfTimerCountDownAnimation() {
        if (this.mSelfTimerCountDownCircleView != null) {
            this.mSelfTimerCountDownCircleView.cancelSelfTimerAnimation();
        }
        if (this.mSelfTimerCountDownCircleViewPort != null) {
            this.mSelfTimerCountDownCircleViewPort.cancelSelfTimerAnimation();
        }
        if (this.mLeftSelfTimerCountDownNumberView != null) {
            this.mLeftSelfTimerCountDownNumberView.cancelSelfTimerAnimation();
        }
        if (this.mRightSelfTimerCountDownNumberView != null) {
            this.mRightSelfTimerCountDownNumberView.cancelSelfTimerAnimation();
        }
        if (this.mLeftSelfTimerCountDownNumberViewPort != null) {
            this.mLeftSelfTimerCountDownNumberViewPort.cancelSelfTimerAnimation();
        }
        if (this.mRightSelfTimerCountDownNumberViewPort != null) {
            this.mRightSelfTimerCountDownNumberViewPort.cancelSelfTimerAnimation();
        }
    }
    
    public void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
    }
    
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mSelfTimerCountDownCircleView = (SelfTimerCountDownCircleView)this.findViewById(2131296578);
        this.mSelfTimerCountDownCircleViewPort = (SelfTimerCountDownCircleView)this.findViewById(2131296579);
        (this.mLeftSelfTimerCountDownNumberView = (SelfTimerCountDownNumberView)this.findViewById(2131296446)).setIsTenDigit(true);
        (this.mRightSelfTimerCountDownNumberView = (SelfTimerCountDownNumberView)this.findViewById(2131296537)).setIsTenDigit(false);
        (this.mLeftSelfTimerCountDownNumberViewPort = (SelfTimerCountDownNumberView)this.findViewById(2131296447)).setIsTenDigit(true);
        (this.mRightSelfTimerCountDownNumberViewPort = (SelfTimerCountDownNumberView)this.findViewById(2131296538)).setIsTenDigit(false);
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n2, n2);
    }
    
    public void setSelfTimer(final SelfTimerInterface selfTimerInterface) {
        if (this.mSelfTimerCountDownCircleView != null) {
            this.mSelfTimerCountDownCircleView.setSelfTimer(selfTimerInterface);
        }
        if (this.mSelfTimerCountDownCircleViewPort != null) {
            this.mSelfTimerCountDownCircleViewPort.setSelfTimer(selfTimerInterface);
        }
        if (this.mLeftSelfTimerCountDownNumberView != null) {
            this.mLeftSelfTimerCountDownNumberView.setSelfTimer(selfTimerInterface);
        }
        if (this.mRightSelfTimerCountDownNumberView != null) {
            this.mRightSelfTimerCountDownNumberView.setSelfTimer(selfTimerInterface);
        }
        if (this.mLeftSelfTimerCountDownNumberViewPort != null) {
            this.mLeftSelfTimerCountDownNumberViewPort.setSelfTimer(selfTimerInterface);
        }
        if (this.mRightSelfTimerCountDownNumberViewPort != null) {
            this.mRightSelfTimerCountDownNumberViewPort.setSelfTimer(selfTimerInterface);
        }
        this.mIsHideHintText = (selfTimerInterface.getCountDownIconId() != -1);
    }
    
    public void setSensorOrientation(final int n) {
        this.setRotation(RotationUtil.getAngle(n));
        final LinearLayout linearLayout = (LinearLayout)this.findViewById(2131296581);
        final LinearLayout linearLayout2 = (LinearLayout)this.findViewById(2131296582);
        if (n == 2) {
            ((View)linearLayout).setVisibility(0);
            ((View)linearLayout2).setVisibility(4);
        }
        else {
            ((View)linearLayout).setVisibility(4);
            ((View)linearLayout2).setVisibility(0);
        }
    }
    
    public void startSelfTimerCountDownAnimation(final boolean b) {
        final TextView textView = (TextView)this.findViewById(2131296585);
        final TextView textView2 = (TextView)this.findViewById(2131296586);
        if (b && !this.mIsHideHintText) {
            textView.setVisibility(0);
            textView2.setVisibility(0);
        }
        else {
            textView.setVisibility(4);
            textView2.setVisibility(4);
        }
        if (this.mLeftSelfTimerCountDownNumberView != null) {
            this.mLeftSelfTimerCountDownNumberView.startAnimation();
        }
        if (this.mRightSelfTimerCountDownNumberView != null) {
            this.mRightSelfTimerCountDownNumberView.startAnimation();
        }
        if (this.mLeftSelfTimerCountDownNumberViewPort != null) {
            this.mLeftSelfTimerCountDownNumberViewPort.startAnimation();
        }
        if (this.mRightSelfTimerCountDownNumberViewPort != null) {
            this.mRightSelfTimerCountDownNumberViewPort.startAnimation();
        }
        if (this.mSelfTimerCountDownCircleView != null) {
            this.mSelfTimerCountDownCircleView.startAnimation();
        }
        if (this.mSelfTimerCountDownCircleViewPort != null) {
            this.mSelfTimerCountDownCircleViewPort.startAnimation();
        }
    }
}
