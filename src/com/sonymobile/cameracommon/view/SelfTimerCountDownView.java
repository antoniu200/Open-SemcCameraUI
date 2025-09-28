package com.sonymobile.cameracommon.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.cameracommon.settings.SelfTimerInterface;
import com.sonyericsson.cameracommon.utility.RotationUtil;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SelfTimerCountDownView extends FrameLayout {
    boolean mIsHideHintText;
    private SelfTimerCountDownNumberView mLeftSelfTimerCountDownNumberView;
    private SelfTimerCountDownNumberView mLeftSelfTimerCountDownNumberViewPort;
    private SelfTimerCountDownNumberView mRightSelfTimerCountDownNumberView;
    private SelfTimerCountDownNumberView mRightSelfTimerCountDownNumberViewPort;
    private SelfTimerCountDownCircleView mSelfTimerCountDownCircleView;
    private SelfTimerCountDownCircleView mSelfTimerCountDownCircleViewPort;

    public SelfTimerCountDownView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mIsHideHintText = false;
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mSelfTimerCountDownCircleView = (SelfTimerCountDownCircleView) findViewById(R.id.selftimer_countdown_circle);
        this.mSelfTimerCountDownCircleViewPort = (SelfTimerCountDownCircleView) findViewById(R.id.selftimer_countdown_circle_port);
        this.mLeftSelfTimerCountDownNumberView = (SelfTimerCountDownNumberView) findViewById(R.id.left_selftimer_countdown_text);
        this.mLeftSelfTimerCountDownNumberView.setIsTenDigit(true);
        this.mRightSelfTimerCountDownNumberView = (SelfTimerCountDownNumberView) findViewById(R.id.right_selftimer_countdown_text);
        this.mRightSelfTimerCountDownNumberView.setIsTenDigit(false);
        this.mLeftSelfTimerCountDownNumberViewPort = (SelfTimerCountDownNumberView) findViewById(R.id.left_selftimer_countdown_text_port);
        this.mLeftSelfTimerCountDownNumberViewPort.setIsTenDigit(true);
        this.mRightSelfTimerCountDownNumberViewPort = (SelfTimerCountDownNumberView) findViewById(R.id.right_selftimer_countdown_text_port);
        this.mRightSelfTimerCountDownNumberViewPort.setIsTenDigit(false);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void startSelfTimerCountDownAnimation(boolean z) {
        TextView textView = (TextView) findViewById(R.id.selftimer_hint_text);
        TextView textView2 = (TextView) findViewById(R.id.selftimer_hint_text_port);
        if (z && !this.mIsHideHintText) {
            textView.setVisibility(0);
            textView2.setVisibility(0);
        } else {
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

    public void setSelfTimer(SelfTimerInterface selfTimerInterface) {
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
        this.mIsHideHintText = selfTimerInterface.getCountDownIconId() != -1;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i2, i2);
    }

    public void setSensorOrientation(int i) {
        setRotation(RotationUtil.getAngle(i));
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.selftimer_countdown_layout_base_landscape);
        LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.selftimer_countdown_layout_base_port);
        if (i == 2) {
            linearLayout.setVisibility(0);
            linearLayout2.setVisibility(4);
        } else {
            linearLayout.setVisibility(4);
            linearLayout2.setVisibility(0);
        }
    }
}
