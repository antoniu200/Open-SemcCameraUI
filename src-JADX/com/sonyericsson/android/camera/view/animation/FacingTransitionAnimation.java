package com.sonyericsson.android.camera.view.animation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.view.baselayout.SwitchAnimationView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class FacingTransitionAnimation {
    private static final String ANIMATION_ALPHA = "alpha";
    private static final String ANIMATION_HOLE_RADIUS = "holeRadius";
    private static final String ANIMATION_RADIUS = "radius";
    private static final String ANIMATION_SCALE_X = "scaleX";
    private static final String ANIMATION_SCALE_Y = "scaleY";
    private static final Interpolator EASE_OUT_IN = new PathInterpolator(0.645f, 0.045f, 0.355f, 1.0f);
    private static final String TAG = "FacingTransitionAnimation";
    private final List<View> mPrimaryShortcutList;
    private final SwitchAnimationView mSwitchAnimationView;
    private final View mViewFinderCover;

    FacingTransitionAnimation(SwitchAnimationView switchAnimationView, View view, List<View> list) {
        this.mSwitchAnimationView = switchAnimationView;
        this.mViewFinderCover = view;
        this.mPrimaryShortcutList = list;
    }

    void resume() {
        if (CamLog.VERBOSE) {
            CamLog.d("resume");
        }
        this.mSwitchAnimationView.setAlpha(0.0f);
        this.mSwitchAnimationView.setRadius(0.0f);
        this.mSwitchAnimationView.setHoleRadius(0.0f);
        for (View view : this.mPrimaryShortcutList) {
            view.setAlpha(1.0f);
            view.setScaleX(1.0f);
            view.setScaleY(1.0f);
        }
    }

    AnimatorSet getSwipeSwitchAnimation() {
        if (CamLog.VERBOSE) {
            CamLog.d("getSwipeSwitchAnimation");
        }
        AnimatorSet animatorSet = new AnimatorSet();
        ArrayList arrayList = new ArrayList();
        arrayList.add(getSwipeSwitchAnimator(this.mSwitchAnimationView, 200));
        Iterator<View> it = this.mPrimaryShortcutList.iterator();
        while (it.hasNext()) {
            arrayList.add(getEaseOutScaleAnimator(it.next(), 200));
        }
        animatorSet.playTogether(arrayList);
        return animatorSet;
    }

    AnimatorSet getDraggingCancelAnimation() {
        if (CamLog.VERBOSE) {
            CamLog.d("getDraggingCancelAnimation");
        }
        AnimatorSet animatorSet = new AnimatorSet();
        ArrayList arrayList = new ArrayList();
        arrayList.add(getDraggingCancelAnimator(this.mSwitchAnimationView, 200));
        arrayList.add(getEaseOutAnimator(this.mViewFinderCover, 200, this.mViewFinderCover.getAlpha()));
        animatorSet.playTogether(arrayList);
        return animatorSet;
    }

    AnimatorSet getAfterSwitchAnimation() {
        if (CamLog.VERBOSE) {
            CamLog.d("getAfterSwitchAnimator");
        }
        AnimatorSet animatorSet = new AnimatorSet();
        ArrayList arrayList = new ArrayList();
        arrayList.add(getAfterSwitchAnimator(this.mSwitchAnimationView, 200));
        Iterator<View> it = this.mPrimaryShortcutList.iterator();
        while (it.hasNext()) {
            arrayList.add(getEaseInScaleAnimator(it.next(), 200));
        }
        animatorSet.playTogether(arrayList);
        return animatorSet;
    }

    private ObjectAnimator getSwipeSwitchAnimator(SwitchAnimationView switchAnimationView, int i) {
        float maxRadius = this.mSwitchAnimationView.getMaxRadius();
        float radius = this.mSwitchAnimationView.getRadius();
        PathInterpolator pathInterpolator = new PathInterpolator(0.39f, 0.575f, 0.565f, 1.0f);
        switchAnimationView.setAlpha(1.0f);
        ObjectAnimator objectAnimatorOfPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(switchAnimationView, PropertyValuesHolder.ofFloat(ANIMATION_RADIUS, radius, maxRadius));
        objectAnimatorOfPropertyValuesHolder.setDuration(i);
        objectAnimatorOfPropertyValuesHolder.setInterpolator(pathInterpolator);
        return objectAnimatorOfPropertyValuesHolder;
    }

    private ObjectAnimator getAfterSwitchAnimator(SwitchAnimationView switchAnimationView, int i) {
        float maxRadius = this.mSwitchAnimationView.getMaxRadius();
        PathInterpolator pathInterpolator = new PathInterpolator(0.55f, 0.055f, 0.675f, 0.19f);
        switchAnimationView.setAlpha(1.0f);
        ObjectAnimator objectAnimatorOfPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(switchAnimationView, PropertyValuesHolder.ofFloat(ANIMATION_HOLE_RADIUS, maxRadius / 10.0f, maxRadius));
        objectAnimatorOfPropertyValuesHolder.setDuration(i);
        objectAnimatorOfPropertyValuesHolder.setInterpolator(pathInterpolator);
        return objectAnimatorOfPropertyValuesHolder;
    }

    private ObjectAnimator getDraggingCancelAnimator(SwitchAnimationView switchAnimationView, int i) {
        float radius = this.mSwitchAnimationView.getRadius();
        float draggingStartRadius = this.mSwitchAnimationView.getDraggingStartRadius();
        PathInterpolator pathInterpolator = new PathInterpolator(0.39f, 0.575f, 0.565f, 1.0f);
        switchAnimationView.setAlpha(1.0f);
        ObjectAnimator objectAnimatorOfPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(switchAnimationView, PropertyValuesHolder.ofFloat(ANIMATION_RADIUS, radius, draggingStartRadius));
        objectAnimatorOfPropertyValuesHolder.setDuration(i);
        objectAnimatorOfPropertyValuesHolder.setInterpolator(pathInterpolator);
        return objectAnimatorOfPropertyValuesHolder;
    }

    private ObjectAnimator getEaseOutAnimator(View view, int i, float f) {
        PathInterpolator pathInterpolator = new PathInterpolator(0.39f, 0.575f, 0.565f, 1.0f);
        ObjectAnimator objectAnimatorOfPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofFloat(ANIMATION_ALPHA, f, 0.0f));
        objectAnimatorOfPropertyValuesHolder.setDuration(i);
        objectAnimatorOfPropertyValuesHolder.setInterpolator(pathInterpolator);
        return objectAnimatorOfPropertyValuesHolder;
    }

    SwitchAnimationView getSwitchAnimationView() {
        return this.mSwitchAnimationView;
    }

    private ObjectAnimator getEaseInScaleAnimator(View view, int i) {
        if (CamLog.VERBOSE) {
            CamLog.d("getEaseInScaleAnimator");
        }
        ObjectAnimator objectAnimatorOfPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofFloat(ANIMATION_ALPHA, 1.0f), PropertyValuesHolder.ofFloat(ANIMATION_SCALE_X, 0.2f, 1.0f), PropertyValuesHolder.ofFloat(ANIMATION_SCALE_Y, 0.2f, 1.0f));
        objectAnimatorOfPropertyValuesHolder.setDuration(i);
        objectAnimatorOfPropertyValuesHolder.setInterpolator(EASE_OUT_IN);
        return objectAnimatorOfPropertyValuesHolder;
    }

    private ObjectAnimator getEaseOutScaleAnimator(View view, int i) {
        if (CamLog.VERBOSE) {
            CamLog.d("getEaseOutScaleAnimator");
        }
        ObjectAnimator objectAnimatorOfPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofFloat(ANIMATION_ALPHA, 0.0f), PropertyValuesHolder.ofFloat(ANIMATION_SCALE_X, 0.0f), PropertyValuesHolder.ofFloat(ANIMATION_SCALE_Y, 0.0f));
        objectAnimatorOfPropertyValuesHolder.setDuration(i);
        objectAnimatorOfPropertyValuesHolder.setInterpolator(EASE_OUT_IN);
        return objectAnimatorOfPropertyValuesHolder;
    }
}
