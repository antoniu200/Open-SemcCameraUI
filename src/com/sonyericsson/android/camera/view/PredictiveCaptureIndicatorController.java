package com.sonyericsson.android.camera.view;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import com.sonyericsson.cameracommon.rotatableview.EdgeRotatableContainerView;
import com.sonyericsson.cameracommon.utility.ResourceUtil;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PredictiveCaptureIndicatorController {
    private final Activity mActivity;
    private Animation mAnimation;
    private View mBackground;
    private EdgeRotatableContainerView mRoot;
    private final LayoutDependencyResolver.ScreenAspect mScreenAspect;
    private TextView mTextView;
    private int mOrientation = 2;
    private boolean mIsAnimationRunning = false;
    private boolean mVisible = false;

    public PredictiveCaptureIndicatorController(Activity activity, LayoutDependencyResolver.ScreenAspect screenAspect) {
        this.mActivity = activity;
        this.mScreenAspect = screenAspect;
    }

    public void setOrientation(int i) {
        this.mOrientation = i;
        if (this.mRoot != null) {
            this.mRoot.setOrientation(i);
        }
    }

    public void startAnimation() {
        if (this.mRoot == null) {
            initPredictiveCaptureIndicator();
        }
        this.mTextView.announceForAccessibility(this.mActivity.getResources().getString(R.string.cam_strings_predictive_capture_feedback_txt));
        this.mIsAnimationRunning = true;
        update();
        this.mBackground.startAnimation(this.mAnimation);
    }

    public void cancelAnimation() {
        if (this.mRoot == null) {
            return;
        }
        this.mBackground.clearAnimation();
        this.mIsAnimationRunning = false;
        update();
    }

    public void show() {
        this.mVisible = true;
        update();
    }

    public void hide() {
        this.mVisible = false;
        update();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void update() {
        if (this.mRoot == null) {
            return;
        }
        if (this.mIsAnimationRunning && this.mVisible) {
            this.mRoot.setVisibility(0);
        } else {
            this.mRoot.setVisibility(4);
        }
    }

    private void initPredictiveCaptureIndicator() {
        this.mRoot = (EdgeRotatableContainerView) ((ViewStub) this.mActivity.findViewById(R.id.predictive_capture_indicator_container_stub)).inflate();
        this.mRoot.setOrientation(this.mOrientation);
        if (this.mScreenAspect == LayoutDependencyResolver.ScreenAspect.EIGHTEEN_NINE) {
            ((ViewGroup.MarginLayoutParams) this.mRoot.getLayoutParams()).rightMargin = ResourceUtil.getDimensionPixelSize(this.mActivity, this.mActivity.getPackageName(), R.dimen.predictive_capture_indicator_container_right_margin_18_9);
        }
        this.mBackground = this.mActivity.findViewById(R.id.predictive_capture_indicator_background);
        this.mTextView = (TextView) this.mActivity.findViewById(R.id.predictive_capture_indicator_text);
        this.mAnimation = AnimationUtils.loadAnimation(this.mActivity, R.anim.predictive_capture_indicator_fade_out);
        this.mAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.sonyericsson.android.camera.view.PredictiveCaptureIndicatorController.1
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                PredictiveCaptureIndicatorController.this.mIsAnimationRunning = false;
                PredictiveCaptureIndicatorController.this.update();
            }
        });
    }
}
