package com.sonyericsson.android.camera.view.baselayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Animatable2;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.research.LocalResearchUtil;
import com.sonymobile.cameracommon.font.FontUtil;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PredictiveLaunchCoverView extends FrameLayout implements View.OnTouchListener {
    private static final long LOWER_TOUCH_INTERVAL_TIME_MILLIS = 0;
    private static final double TOUCH_AREA_SIZE_PER_DP = 0.5555555555555556d;
    private static final long UPPER_TOUCH_INTERVAL_TIME_MILLIS = 300;
    private TextView mDescription;
    private int mDescriptionMargin;
    private boolean mExists;
    private ImageView mHoleView;
    private PredictiveLaunchCoverTouchListener mListener;
    private int mOrientation;
    private FrameLayout mSquareBox;
    private FrameLayout mTouchArea;
    private long mTouchDownTime;

    public interface PredictiveLaunchCoverTouchListener {
        void onCircleTouched();
    }

    public PredictiveLaunchCoverView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mOrientation = 0;
    }

    public static PredictiveLaunchCoverView inflate(Context context, PredictiveLaunchCoverTouchListener predictiveLaunchCoverTouchListener, PredictiveLaunchCoverType predictiveLaunchCoverType) {
        PredictiveLaunchCoverView predictiveLaunchCoverView = (PredictiveLaunchCoverView) ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.camera_predictive_launch_cover, (ViewGroup) null);
        predictiveLaunchCoverView.mTouchArea.setContentDescription(context.getResources().getText(predictiveLaunchCoverType.mDescriptionResourceId));
        predictiveLaunchCoverView.mDescription.setText(predictiveLaunchCoverType.mDescriptionResourceId);
        predictiveLaunchCoverView.mListener = predictiveLaunchCoverTouchListener;
        predictiveLaunchCoverView.mExists = true;
        return predictiveLaunchCoverView;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        sendStartupPerformanceDataForReadyForUse();
    }

    private void sendStartupPerformanceDataForReadyForUse() {
        LocalResearchUtil.getInstance().stopMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_COLD_BOOT_FROM_LIFTTRIGGER_READY_FOR_USE);
        LocalResearchUtil.getInstance().stopMeasurement(LocalResearchUtil.MeasurementKey.LAUNCH_WARM_BOOT_FROM_LIFTTRIGGER_READY_FOR_USE);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mHoleView = (ImageView) findViewById(R.id.hole_view);
        this.mDescription = (TextView) findViewById(R.id.predictive_launch_description);
        this.mSquareBox = (FrameLayout) findViewById(R.id.square_box);
        this.mDescriptionMargin = getContext().getResources().getDimensionPixelSize(R.dimen.predictive_launch_cover_description_bottom_margin);
        this.mTouchArea = (FrameLayout) findViewById(R.id.touch_area);
        this.mTouchArea.setOnTouchListener(this);
        FontUtil.setRobotoFont(this.mDescription, FontUtil.RobotoFontType.MEDIUM);
        this.mDescription.setAccessibilityDelegate(new View.AccessibilityDelegate() { // from class: com.sonyericsson.android.camera.view.baselayout.PredictiveLaunchCoverView.1
            @Override // android.view.View.AccessibilityDelegate
            public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
                return false;
            }
        });
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.mOrientation == 0 || getVisibility() == 0 || !this.mExists) {
            return;
        }
        updateLayout();
        show();
    }

    @Override // android.view.View
    public boolean performClick() {
        super.performClick();
        return true;
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0:
                this.mTouchDownTime = SystemClock.elapsedRealtime();
                break;
            case 1:
                if (checkTouchEnabled()) {
                    Rect rect = new Rect();
                    this.mTouchArea.getDrawingRect(rect);
                    if (rect.contains((int) motionEvent.getX(), (int) motionEvent.getY()) && this.mListener != null) {
                        this.mListener.onCircleTouched();
                        break;
                    }
                }
                break;
        }
        return true;
    }

    private void show() {
        if (getVisibility() == 0 || !this.mDescription.isAttachedToWindow()) {
            return;
        }
        setVisibility(0);
        this.mDescription.setVisibility(0);
        this.mHoleView.setSelected(false);
        startPredictiveLaunchAnimation(null);
    }

    public void hide(Animatable2.AnimationCallback animationCallback) {
        this.mTouchArea.setOnTouchListener(null);
        this.mDescription.setVisibility(4);
        this.mHoleView.setSelected(true);
        this.mExists = false;
        startPredictiveLaunchAnimation(animationCallback);
    }

    private void startPredictiveLaunchAnimation(Animatable2.AnimationCallback animationCallback) {
        Animatable2 animatable2 = (Animatable2) this.mHoleView.getDrawable().getCurrent();
        if (animatable2.isRunning()) {
            return;
        }
        if (animationCallback != null) {
            animatable2.registerAnimationCallback(animationCallback);
        }
        animatable2.start();
    }

    public boolean exists() {
        return this.mExists;
    }

    public void updateLayout(int i) {
        int i2 = this.mOrientation;
        if (i != 0) {
            this.mOrientation = i;
        }
        if (i2 == 0 || i2 == i) {
            return;
        }
        updateLayout();
    }

    private void updateLayout() {
        if (this.mSquareBox == null || this.mTouchArea == null || this.mDescription == null || !this.mDescription.isAttachedToWindow()) {
            return;
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, getMeasuredWidth());
        layoutParams.gravity = 16;
        this.mSquareBox.setLayoutParams(layoutParams);
        int measuredWidth = (int) (TOUCH_AREA_SIZE_PER_DP * getMeasuredWidth());
        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(measuredWidth, measuredWidth);
        layoutParams2.gravity = 17;
        this.mTouchArea.setLayoutParams(layoutParams2);
        FrameLayout.LayoutParams layoutParams3 = new FrameLayout.LayoutParams(-2, -2);
        if (this.mOrientation == 1) {
            layoutParams3.gravity = 81;
            layoutParams3.bottomMargin = this.mDescriptionMargin;
            this.mDescription.setRotation(0.0f);
        } else {
            layoutParams3.gravity = 19;
            layoutParams3.leftMargin = (this.mDescription.getMeasuredWidth() / (-2)) + (this.mDescription.getMeasuredHeight() / 2) + this.mDescriptionMargin;
            this.mDescription.setRotation(90.0f);
        }
        this.mDescription.setLayoutParams(layoutParams3);
    }

    private boolean checkTouchEnabled() {
        long jElapsedRealtime = SystemClock.elapsedRealtime() - this.mTouchDownTime;
        return 0 < jElapsedRealtime && jElapsedRealtime <= UPPER_TOUCH_INTERVAL_TIME_MILLIS;
    }

    public enum PredictiveLaunchCoverType {
        TOUCH_TO_LAUNCH_AND_CAPTURE(R.string.cam_strings_predictive_launch_guide_2_txt),
        TOUCH_TO_LAUNCH(R.string.cam_strings_predictive_launch_guide_txt);

        public final int mDescriptionResourceId;

        PredictiveLaunchCoverType(int i) {
            this.mDescriptionResourceId = i;
        }
    }
}
