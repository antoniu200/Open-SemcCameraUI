// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.animation;

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import android.animation.AnimatorSet;
import com.sonyericsson.android.camera.util.CamLog;
import android.animation.TimeInterpolator;
import android.animation.PropertyValuesHolder;
import android.animation.ObjectAnimator;
import android.view.animation.PathInterpolator;
import com.sonyericsson.android.camera.view.baselayout.SwitchAnimationView;
import android.view.View;
import java.util.List;
import android.view.animation.Interpolator;

class FacingTransitionAnimation
{
    private static final String ANIMATION_ALPHA = "alpha";
    private static final String ANIMATION_HOLE_RADIUS = "holeRadius";
    private static final String ANIMATION_RADIUS = "radius";
    private static final String ANIMATION_SCALE_X = "scaleX";
    private static final String ANIMATION_SCALE_Y = "scaleY";
    private static final Interpolator EASE_OUT_IN;
    private static final String TAG = "FacingTransitionAnimation";
    private final List<View> mPrimaryShortcutList;
    private final SwitchAnimationView mSwitchAnimationView;
    private final View mViewFinderCover;
    
    static {
        EASE_OUT_IN = (Interpolator)new PathInterpolator(0.645f, 0.045f, 0.355f, 1.0f);
    }
    
    FacingTransitionAnimation(final SwitchAnimationView mSwitchAnimationView, final View mViewFinderCover, final List<View> mPrimaryShortcutList) {
        this.mSwitchAnimationView = mSwitchAnimationView;
        this.mViewFinderCover = mViewFinderCover;
        this.mPrimaryShortcutList = mPrimaryShortcutList;
    }
    
    private ObjectAnimator getAfterSwitchAnimator(final SwitchAnimationView switchAnimationView, final int n) {
        final float maxRadius = this.mSwitchAnimationView.getMaxRadius();
        final PathInterpolator interpolator = new PathInterpolator(0.55f, 0.055f, 0.675f, 0.19f);
        switchAnimationView.setAlpha(1.0f);
        final ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder((Object)switchAnimationView, new PropertyValuesHolder[] { PropertyValuesHolder.ofFloat("holeRadius", new float[] { maxRadius / 10.0f, maxRadius }) });
        ofPropertyValuesHolder.setDuration((long)n);
        ofPropertyValuesHolder.setInterpolator((TimeInterpolator)interpolator);
        return ofPropertyValuesHolder;
    }
    
    private ObjectAnimator getDraggingCancelAnimator(final SwitchAnimationView switchAnimationView, final int n) {
        final float radius = this.mSwitchAnimationView.getRadius();
        final float draggingStartRadius = this.mSwitchAnimationView.getDraggingStartRadius();
        final PathInterpolator interpolator = new PathInterpolator(0.39f, 0.575f, 0.565f, 1.0f);
        switchAnimationView.setAlpha(1.0f);
        final ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder((Object)switchAnimationView, new PropertyValuesHolder[] { PropertyValuesHolder.ofFloat("radius", new float[] { radius, draggingStartRadius }) });
        ofPropertyValuesHolder.setDuration((long)n);
        ofPropertyValuesHolder.setInterpolator((TimeInterpolator)interpolator);
        return ofPropertyValuesHolder;
    }
    
    private ObjectAnimator getEaseInScaleAnimator(final View view, final int n) {
        if (CamLog.VERBOSE) {
            CamLog.d("getEaseInScaleAnimator");
        }
        final ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder((Object)view, new PropertyValuesHolder[] { PropertyValuesHolder.ofFloat("alpha", new float[] { 1.0f }), PropertyValuesHolder.ofFloat("scaleX", new float[] { 0.2f, 1.0f }), PropertyValuesHolder.ofFloat("scaleY", new float[] { 0.2f, 1.0f }) });
        ofPropertyValuesHolder.setDuration((long)n);
        ofPropertyValuesHolder.setInterpolator((TimeInterpolator)FacingTransitionAnimation.EASE_OUT_IN);
        return ofPropertyValuesHolder;
    }
    
    private ObjectAnimator getEaseOutAnimator(final View view, final int n, final float n2) {
        final PathInterpolator interpolator = new PathInterpolator(0.39f, 0.575f, 0.565f, 1.0f);
        final ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder((Object)view, new PropertyValuesHolder[] { PropertyValuesHolder.ofFloat("alpha", new float[] { n2, 0.0f }) });
        ofPropertyValuesHolder.setDuration((long)n);
        ofPropertyValuesHolder.setInterpolator((TimeInterpolator)interpolator);
        return ofPropertyValuesHolder;
    }
    
    private ObjectAnimator getEaseOutScaleAnimator(final View view, final int n) {
        if (CamLog.VERBOSE) {
            CamLog.d("getEaseOutScaleAnimator");
        }
        final ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder((Object)view, new PropertyValuesHolder[] { PropertyValuesHolder.ofFloat("alpha", new float[] { 0.0f }), PropertyValuesHolder.ofFloat("scaleX", new float[] { 0.0f }), PropertyValuesHolder.ofFloat("scaleY", new float[] { 0.0f }) });
        ofPropertyValuesHolder.setDuration((long)n);
        ofPropertyValuesHolder.setInterpolator((TimeInterpolator)FacingTransitionAnimation.EASE_OUT_IN);
        return ofPropertyValuesHolder;
    }
    
    private ObjectAnimator getSwipeSwitchAnimator(final SwitchAnimationView switchAnimationView, final int n) {
        final float maxRadius = this.mSwitchAnimationView.getMaxRadius();
        final float radius = this.mSwitchAnimationView.getRadius();
        final PathInterpolator interpolator = new PathInterpolator(0.39f, 0.575f, 0.565f, 1.0f);
        switchAnimationView.setAlpha(1.0f);
        final ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder((Object)switchAnimationView, new PropertyValuesHolder[] { PropertyValuesHolder.ofFloat("radius", new float[] { radius, maxRadius }) });
        ofPropertyValuesHolder.setDuration((long)n);
        ofPropertyValuesHolder.setInterpolator((TimeInterpolator)interpolator);
        return ofPropertyValuesHolder;
    }
    
    AnimatorSet getAfterSwitchAnimation() {
        if (CamLog.VERBOSE) {
            CamLog.d("getAfterSwitchAnimator");
        }
        final AnimatorSet set = new AnimatorSet();
        final ArrayList list = new ArrayList();
        list.add(this.getAfterSwitchAnimator(this.mSwitchAnimationView, 200));
        final Iterator<View> iterator = this.mPrimaryShortcutList.iterator();
        while (iterator.hasNext()) {
            list.add(this.getEaseInScaleAnimator(iterator.next(), 200));
        }
        set.playTogether((Collection)list);
        return set;
    }
    
    AnimatorSet getDraggingCancelAnimation() {
        if (CamLog.VERBOSE) {
            CamLog.d("getDraggingCancelAnimation");
        }
        final AnimatorSet set = new AnimatorSet();
        final ArrayList list = new ArrayList();
        list.add(this.getDraggingCancelAnimator(this.mSwitchAnimationView, 200));
        list.add(this.getEaseOutAnimator(this.mViewFinderCover, 200, this.mViewFinderCover.getAlpha()));
        set.playTogether((Collection)list);
        return set;
    }
    
    AnimatorSet getSwipeSwitchAnimation() {
        if (CamLog.VERBOSE) {
            CamLog.d("getSwipeSwitchAnimation");
        }
        final AnimatorSet set = new AnimatorSet();
        final ArrayList list = new ArrayList();
        list.add(this.getSwipeSwitchAnimator(this.mSwitchAnimationView, 200));
        final Iterator<View> iterator = this.mPrimaryShortcutList.iterator();
        while (iterator.hasNext()) {
            list.add(this.getEaseOutScaleAnimator(iterator.next(), 200));
        }
        set.playTogether((Collection)list);
        return set;
    }
    
    SwitchAnimationView getSwitchAnimationView() {
        return this.mSwitchAnimationView;
    }
    
    void resume() {
        if (CamLog.VERBOSE) {
            CamLog.d("resume");
        }
        this.mSwitchAnimationView.setAlpha(0.0f);
        this.mSwitchAnimationView.setRadius(0.0f);
        this.mSwitchAnimationView.setHoleRadius(0.0f);
        for (final View view : this.mPrimaryShortcutList) {
            view.setAlpha(1.0f);
            view.setScaleX(1.0f);
            view.setScaleY(1.0f);
        }
    }
}
