package com.sonyericsson.android.camera.view.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.PowerManager;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import com.sonyericsson.android.camera.NavigatorContents;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.view.ApplicationNavigator;
import com.sonyericsson.android.camera.view.modeselector.ModeSelectorInternalMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class ModeTransitionAnimation {
    private static final String ANIMATION_ALPHA = "alpha";
    private static final String ANIMATION_SCALE_X = "scaleX";
    private static final String ANIMATION_SCALE_Y = "scaleY";
    private static final float COMPLEMENT_ANIMATION_DURATION_RATIO = 0.5f;
    private static final int VIEW_FINDER_FADE_DURATION = 200;
    private final View mCaptureButton;
    private final ImageView mCurrentModeIndicator;
    private final View mFrontAngleSwitchButton;
    private final View mGridLineView;
    private final View mModeSelectorShortcut;
    private final View mModeShortcutButton;
    private ImageView mModeSwitchImageView;
    private TextView mModeSwitchTextView;
    private final ApplicationNavigator mNavigator;
    private AnimatorSet mPreviousNameSwitchImageAnimatorSet = null;
    private final List<View> mPrimaryShortcutList;
    private final View mSecondaryShortcutLeft;
    private final View mSecondaryShortcutRight;
    private final View mThumbnail;
    private final View mViewFinderCover;
    private static final Interpolator MODE_TRANSITION = new PathInterpolator(0.165f, 0.84f, 0.44f, 1.0f);
    private static final Interpolator EASE_OUT_IN = new PathInterpolator(0.645f, 0.045f, 0.355f, 1.0f);

    ModeTransitionAnimation(ApplicationNavigator applicationNavigator, List<View> list, View view, View view2, View view3, View view4, View view5, View view6, View view7, View view8, View view9) {
        this.mNavigator = applicationNavigator;
        this.mCurrentModeIndicator = applicationNavigator.getCurrentModeIndicatorView();
        this.mPrimaryShortcutList = list;
        this.mSecondaryShortcutLeft = view;
        this.mSecondaryShortcutRight = view2;
        this.mCaptureButton = view3;
        this.mViewFinderCover = view4;
        this.mGridLineView = view5;
        this.mModeSelectorShortcut = view6;
        this.mThumbnail = view8;
        this.mFrontAngleSwitchButton = view9;
        this.mModeShortcutButton = view7;
    }

    void resume() {
        if (CamLog.VERBOSE) {
            CamLog.d("resume");
        }
        if (this.mModeSwitchTextView != null) {
            this.mModeSwitchTextView.setAlpha(0.0f);
            this.mModeSwitchImageView.setAlpha(0.0f);
        }
        for (View view : this.mPrimaryShortcutList) {
            view.setAlpha(1.0f);
            view.setScaleX(1.0f);
            view.setScaleY(1.0f);
        }
        this.mSecondaryShortcutLeft.setAlpha(1.0f);
        this.mSecondaryShortcutLeft.setScaleX(1.0f);
        this.mSecondaryShortcutLeft.setScaleY(1.0f);
        this.mSecondaryShortcutRight.setAlpha(1.0f);
        this.mSecondaryShortcutRight.setScaleX(1.0f);
        this.mSecondaryShortcutRight.setScaleY(1.0f);
        this.mCaptureButton.setAlpha(1.0f);
        this.mCaptureButton.setScaleX(1.0f);
        this.mCaptureButton.setScaleY(1.0f);
        this.mModeSelectorShortcut.setAlpha(1.0f);
        this.mModeSelectorShortcut.setScaleX(1.0f);
        this.mModeSelectorShortcut.setScaleY(1.0f);
        this.mViewFinderCover.setAlpha(0.0f);
        this.mGridLineView.setAlpha(1.0f);
        this.mThumbnail.setAlpha(1.0f);
        this.mThumbnail.setScaleX(1.0f);
        this.mThumbnail.setScaleY(1.0f);
        this.mModeShortcutButton.setAlpha(1.0f);
        this.mModeShortcutButton.setScaleX(1.0f);
        this.mModeShortcutButton.setScaleY(1.0f);
        if (this.mFrontAngleSwitchButton != null) {
            this.mFrontAngleSwitchButton.setAlpha(1.0f);
            this.mFrontAngleSwitchButton.setScaleX(1.0f);
            this.mFrontAngleSwitchButton.setScaleY(1.0f);
        }
    }

    private void setupModeSwitchContainer() {
        this.mModeSwitchImageView = this.mNavigator.getModeSwitchImageView();
        this.mModeSwitchTextView = this.mNavigator.getModeSwitchNameView();
        this.mModeSwitchTextView.setAlpha(0.0f);
        this.mModeSwitchImageView.setAlpha(0.0f);
    }

    private boolean isScaleOut(View view) {
        return view.getScaleX() < 1.0f || view.getScaleY() < 1.0f;
    }

    AnimatorSet getStartAnimation() {
        AnimatorSet animatorSet = new AnimatorSet();
        ArrayList arrayList = new ArrayList();
        if (this.mModeSwitchTextView == null) {
            setupModeSwitchContainer();
        }
        Iterator<View> it = this.mPrimaryShortcutList.iterator();
        while (it.hasNext()) {
            arrayList.add(getEaseOutScaleAnimator(it.next(), 200));
        }
        if (this.mFrontAngleSwitchButton != null) {
            arrayList.add(getEaseOutScaleAnimator(this.mFrontAngleSwitchButton, 200));
            arrayList.add(getEaseOutScaleAnimator(this.mFrontAngleSwitchButton, 200));
        }
        arrayList.add(getEaseOutScaleAnimator(this.mSecondaryShortcutLeft, 200));
        arrayList.add(getEaseOutScaleAnimator(this.mSecondaryShortcutRight, 200));
        arrayList.add(getEaseOutScaleAnimator(this.mCaptureButton, 200));
        arrayList.add(getEaseOutScaleAnimator(this.mModeSelectorShortcut, 200));
        arrayList.add(getEaseOutScaleAnimator(this.mModeShortcutButton, 200));
        arrayList.add(getLinearFadeOutAnimator(this.mGridLineView, 200, 1.0f));
        animatorSet.playTogether(arrayList);
        return animatorSet;
    }

    AnimatorSet getExecuteAnimation(NavigatorContents navigatorContents) {
        AnimatorSet animatorSet = new AnimatorSet();
        ArrayList arrayList = new ArrayList();
        float alpha = this.mViewFinderCover.getAlpha();
        arrayList.add(getModeIconAutoTransitionAnimation(navigatorContents, (int) ((alpha < 1.0f ? (int) ((1.0f - alpha) * 200.0f) : 200) * COMPLEMENT_ANIMATION_DURATION_RATIO)));
        View view = this.mViewFinderCover;
        int i = (int) (200.0f * (1.0f - alpha) * COMPLEMENT_ANIMATION_DURATION_RATIO);
        arrayList.add(getLinearFadeInAnimator(view, i, alpha, 1.0f));
        Iterator<View> it = this.mPrimaryShortcutList.iterator();
        while (it.hasNext()) {
            arrayList.add(getEaseOutScaleAnimator(it.next(), 100));
        }
        if (this.mFrontAngleSwitchButton != null) {
            arrayList.add(getEaseOutScaleAnimator(this.mFrontAngleSwitchButton, 100));
        }
        arrayList.add(getEaseOutScaleAnimator(this.mSecondaryShortcutLeft, 100));
        arrayList.add(getEaseOutScaleAnimator(this.mSecondaryShortcutRight, 100));
        arrayList.add(getEaseOutScaleAnimator(this.mCaptureButton, 100));
        arrayList.add(getEaseOutScaleAnimator(this.mModeSelectorShortcut, 100));
        arrayList.add(getEaseOutScaleAnimator(this.mModeShortcutButton, 100));
        arrayList.add(getLinearFadeOutAnimator(this.mGridLineView, i, alpha));
        animatorSet.playTogether(arrayList);
        return animatorSet;
    }

    AnimatorSet getCancelAnimation(NavigatorContents navigatorContents) {
        AnimatorSet animatorSet = new AnimatorSet();
        ArrayList arrayList = new ArrayList();
        arrayList.add(getModeIconAutoTransitionAnimation(navigatorContents, 200));
        Iterator<View> it = this.mPrimaryShortcutList.iterator();
        while (it.hasNext()) {
            arrayList.add(getEaseInScaleAnimator(it.next(), 200));
        }
        if (this.mFrontAngleSwitchButton != null) {
            arrayList.add(getEaseInScaleAnimator(this.mFrontAngleSwitchButton, 200));
            arrayList.add(getEaseInScaleAnimator(this.mFrontAngleSwitchButton, 200));
        }
        float alpha = this.mViewFinderCover.getAlpha();
        int i = (int) (200.0f * alpha);
        arrayList.add(getLinearFadeOutAnimator(this.mViewFinderCover, i, alpha));
        arrayList.add(getLinearFadeInAnimator(this.mGridLineView, i, 0.0f, 1.0f));
        arrayList.add(getEaseInScaleAnimator(this.mSecondaryShortcutLeft, 200));
        arrayList.add(getEaseInScaleAnimator(this.mSecondaryShortcutRight, 200));
        arrayList.add(getEaseInScaleAnimator(this.mCaptureButton, 200));
        arrayList.add(getEaseInScaleAnimator(this.mModeSelectorShortcut, 200));
        arrayList.add(getEaseInScaleAnimator(this.mModeShortcutButton, 200));
        if (isScaleOut(this.mThumbnail)) {
            arrayList.add(getEaseInScaleAnimator(this.mThumbnail, 200));
        }
        animatorSet.playTogether(arrayList);
        return animatorSet;
    }

    AnimatorSet getFinishAnimation(CapturingMode capturingMode) {
        AnimatorSet animatorSet = new AnimatorSet();
        ArrayList arrayList = new ArrayList();
        if (((PowerManager) this.mModeSwitchTextView.getContext().getSystemService("power")).isPowerSaveMode()) {
            this.mModeSwitchTextView.setVisibility(8);
            this.mModeSwitchImageView.setVisibility(8);
        } else if (!ModeSelectorInternalMode.exists(capturingMode)) {
            this.mNavigator.updateModeSwitchViews();
            arrayList.add(getModeSwitchEaseOutAnimation());
        }
        Iterator<View> it = this.mPrimaryShortcutList.iterator();
        while (it.hasNext()) {
            arrayList.add(getEaseInScaleAnimator(it.next(), 200));
        }
        if (this.mFrontAngleSwitchButton != null) {
            arrayList.add(getEaseInScaleAnimator(this.mFrontAngleSwitchButton, 200));
        }
        arrayList.add(getEaseInScaleAnimator(this.mSecondaryShortcutLeft, 200));
        arrayList.add(getEaseInScaleAnimator(this.mSecondaryShortcutRight, 200));
        arrayList.add(getEaseInScaleAnimator(this.mCaptureButton, 200));
        arrayList.add(getEaseInScaleAnimator(this.mModeSelectorShortcut, 200));
        arrayList.add(getEaseInScaleAnimator(this.mModeShortcutButton, 200));
        if (isScaleOut(this.mThumbnail)) {
            arrayList.add(getEaseInScaleAnimator(this.mThumbnail, 200));
        }
        arrayList.add(getLinearFadeOutAnimator(this.mViewFinderCover, 200, this.mViewFinderCover.getAlpha()));
        arrayList.add(getLinearFadeInAnimator(this.mGridLineView, 200, 0.0f, 1.0f));
        animatorSet.playTogether(arrayList);
        return animatorSet;
    }

    static float getPreviewAlpha(int i, float f) {
        return 1.0f - (1.0f - (Math.abs(i) / f));
    }

    private AnimatorSet getModeSwitchEaseOutAnimation() {
        AnimatorSet animatorSet = new AnimatorSet();
        ArrayList arrayList = new ArrayList();
        if (this.mPreviousNameSwitchImageAnimatorSet != null && this.mPreviousNameSwitchImageAnimatorSet.isRunning()) {
            this.mPreviousNameSwitchImageAnimatorSet.cancel();
        }
        this.mPreviousNameSwitchImageAnimatorSet = animatorSet;
        ObjectAnimator easeOutAnimator = getEaseOutAnimator(this.mModeSwitchTextView, 1000);
        easeOutAnimator.setStartDelay(200L);
        arrayList.add(easeOutAnimator);
        this.mModeSwitchTextView.setVisibility(0);
        this.mModeSwitchTextView.setAlpha(1.0f);
        ObjectAnimator easeOutAnimator2 = getEaseOutAnimator(this.mModeSwitchImageView, 1000);
        easeOutAnimator2.setStartDelay(200L);
        arrayList.add(easeOutAnimator2);
        this.mModeSwitchImageView.setVisibility(0);
        this.mModeSwitchImageView.setAlpha(1.0f);
        animatorSet.playTogether(arrayList);
        return animatorSet;
    }

    private Animator getLinearFadeInAnimator(View view, int i, float f, float f2) {
        ObjectAnimator objectAnimatorOfPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofFloat(ANIMATION_ALPHA, f, f2));
        objectAnimatorOfPropertyValuesHolder.setDuration(i);
        return objectAnimatorOfPropertyValuesHolder;
    }

    private Animator getLinearFadeOutAnimator(View view, int i, float f) {
        ObjectAnimator objectAnimatorOfPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofFloat(ANIMATION_ALPHA, f, 0.0f));
        objectAnimatorOfPropertyValuesHolder.setDuration(i);
        return objectAnimatorOfPropertyValuesHolder;
    }

    private ObjectAnimator getEaseOutAnimator(View view, int i) {
        ObjectAnimator objectAnimatorOfPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofFloat(ANIMATION_ALPHA, 0.0f));
        objectAnimatorOfPropertyValuesHolder.setDuration(i);
        objectAnimatorOfPropertyValuesHolder.setInterpolator(EASE_OUT_IN);
        return objectAnimatorOfPropertyValuesHolder;
    }

    private ObjectAnimator getEaseInScaleAnimator(View view, int i) {
        ObjectAnimator objectAnimatorOfPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofFloat(ANIMATION_ALPHA, 1.0f), PropertyValuesHolder.ofFloat(ANIMATION_SCALE_X, 0.2f, 1.0f), PropertyValuesHolder.ofFloat(ANIMATION_SCALE_Y, 0.2f, 1.0f));
        objectAnimatorOfPropertyValuesHolder.setDuration(i);
        objectAnimatorOfPropertyValuesHolder.setInterpolator(EASE_OUT_IN);
        return objectAnimatorOfPropertyValuesHolder;
    }

    private ObjectAnimator getEaseOutScaleAnimator(View view, int i) {
        ObjectAnimator objectAnimatorOfPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofFloat(ANIMATION_ALPHA, 0.0f), PropertyValuesHolder.ofFloat(ANIMATION_SCALE_X, 0.0f), PropertyValuesHolder.ofFloat(ANIMATION_SCALE_Y, 0.0f));
        objectAnimatorOfPropertyValuesHolder.setDuration(i);
        objectAnimatorOfPropertyValuesHolder.setInterpolator(EASE_OUT_IN);
        return objectAnimatorOfPropertyValuesHolder;
    }

    private ObjectAnimator getModeIconAutoTransitionAnimation(NavigatorContents navigatorContents, int i) {
        int length = (NavigatorContents.values().length - NavigatorContents.indexOf(navigatorContents)) - 1;
        if (CamLog.VERBOSE) {
            CamLog.d("getModeIconAutoTransitionAnimation : " + length);
        }
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(this.mCurrentModeIndicator, "translationY", this.mCurrentModeIndicator.getY(), this.mNavigator.calculateModeIndicatorPosition(length));
        objectAnimatorOfFloat.setDuration(i);
        objectAnimatorOfFloat.setInterpolator(MODE_TRANSITION);
        return objectAnimatorOfFloat;
    }
}
