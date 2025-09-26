// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.animation;

import com.sonyericsson.android.camera.view.modeselector.ModeSelectorInternalMode;
import android.os.PowerManager;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.NavigatorContents;
import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.PropertyValuesHolder;
import android.animation.ObjectAnimator;
import android.view.animation.PathInterpolator;
import java.util.List;
import android.animation.AnimatorSet;
import com.sonyericsson.android.camera.view.ApplicationNavigator;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;
import android.view.animation.Interpolator;

class ModeTransitionAnimation
{
    private static final String ANIMATION_ALPHA = "alpha";
    private static final String ANIMATION_SCALE_X = "scaleX";
    private static final String ANIMATION_SCALE_Y = "scaleY";
    private static final float COMPLEMENT_ANIMATION_DURATION_RATIO = 0.5f;
    private static final Interpolator EASE_OUT_IN;
    private static final Interpolator MODE_TRANSITION;
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
    private AnimatorSet mPreviousNameSwitchImageAnimatorSet;
    private final List<View> mPrimaryShortcutList;
    private final View mSecondaryShortcutLeft;
    private final View mSecondaryShortcutRight;
    private final View mThumbnail;
    private final View mViewFinderCover;
    
    static {
        MODE_TRANSITION = (Interpolator)new PathInterpolator(0.165f, 0.84f, 0.44f, 1.0f);
        EASE_OUT_IN = (Interpolator)new PathInterpolator(0.645f, 0.045f, 0.355f, 1.0f);
    }
    
    ModeTransitionAnimation(final ApplicationNavigator mNavigator, final List<View> mPrimaryShortcutList, final View mSecondaryShortcutLeft, final View mSecondaryShortcutRight, final View mCaptureButton, final View mViewFinderCover, final View mGridLineView, final View mModeSelectorShortcut, final View mModeShortcutButton, final View mThumbnail, final View mFrontAngleSwitchButton) {
        this.mPreviousNameSwitchImageAnimatorSet = null;
        this.mNavigator = mNavigator;
        this.mCurrentModeIndicator = mNavigator.getCurrentModeIndicatorView();
        this.mPrimaryShortcutList = mPrimaryShortcutList;
        this.mSecondaryShortcutLeft = mSecondaryShortcutLeft;
        this.mSecondaryShortcutRight = mSecondaryShortcutRight;
        this.mCaptureButton = mCaptureButton;
        this.mViewFinderCover = mViewFinderCover;
        this.mGridLineView = mGridLineView;
        this.mModeSelectorShortcut = mModeSelectorShortcut;
        this.mThumbnail = mThumbnail;
        this.mFrontAngleSwitchButton = mFrontAngleSwitchButton;
        this.mModeShortcutButton = mModeShortcutButton;
    }
    
    private ObjectAnimator getEaseInScaleAnimator(final View view, final int n) {
        final ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder((Object)view, new PropertyValuesHolder[] { PropertyValuesHolder.ofFloat("alpha", new float[] { 1.0f }), PropertyValuesHolder.ofFloat("scaleX", new float[] { 0.2f, 1.0f }), PropertyValuesHolder.ofFloat("scaleY", new float[] { 0.2f, 1.0f }) });
        ofPropertyValuesHolder.setDuration((long)n);
        ofPropertyValuesHolder.setInterpolator((TimeInterpolator)ModeTransitionAnimation.EASE_OUT_IN);
        return ofPropertyValuesHolder;
    }
    
    private ObjectAnimator getEaseOutAnimator(final View view, final int n) {
        final ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder((Object)view, new PropertyValuesHolder[] { PropertyValuesHolder.ofFloat("alpha", new float[] { 0.0f }) });
        ofPropertyValuesHolder.setDuration((long)n);
        ofPropertyValuesHolder.setInterpolator((TimeInterpolator)ModeTransitionAnimation.EASE_OUT_IN);
        return ofPropertyValuesHolder;
    }
    
    private ObjectAnimator getEaseOutScaleAnimator(final View view, final int n) {
        final ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder((Object)view, new PropertyValuesHolder[] { PropertyValuesHolder.ofFloat("alpha", new float[] { 0.0f }), PropertyValuesHolder.ofFloat("scaleX", new float[] { 0.0f }), PropertyValuesHolder.ofFloat("scaleY", new float[] { 0.0f }) });
        ofPropertyValuesHolder.setDuration((long)n);
        ofPropertyValuesHolder.setInterpolator((TimeInterpolator)ModeTransitionAnimation.EASE_OUT_IN);
        return ofPropertyValuesHolder;
    }
    
    private Animator getLinearFadeInAnimator(final View view, final int n, final float n2, final float n3) {
        final ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder((Object)view, new PropertyValuesHolder[] { PropertyValuesHolder.ofFloat("alpha", new float[] { n2, n3 }) });
        ofPropertyValuesHolder.setDuration((long)n);
        return (Animator)ofPropertyValuesHolder;
    }
    
    private Animator getLinearFadeOutAnimator(final View view, final int n, final float n2) {
        final ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder((Object)view, new PropertyValuesHolder[] { PropertyValuesHolder.ofFloat("alpha", new float[] { n2, 0.0f }) });
        ofPropertyValuesHolder.setDuration((long)n);
        return (Animator)ofPropertyValuesHolder;
    }
    
    private ObjectAnimator getModeIconAutoTransitionAnimation(final NavigatorContents navigatorContents, final int n) {
        final int i = NavigatorContents.values().length - NavigatorContents.indexOf(navigatorContents) - 1;
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getModeIconAutoTransitionAnimation : ");
            sb.append(i);
            CamLog.d(sb.toString());
        }
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this.mCurrentModeIndicator, "translationY", new float[] { this.mCurrentModeIndicator.getY(), (float)this.mNavigator.calculateModeIndicatorPosition(i) });
        ofFloat.setDuration((long)n);
        ofFloat.setInterpolator((TimeInterpolator)ModeTransitionAnimation.MODE_TRANSITION);
        return ofFloat;
    }
    
    private AnimatorSet getModeSwitchEaseOutAnimation() {
        final AnimatorSet mPreviousNameSwitchImageAnimatorSet = new AnimatorSet();
        final ArrayList list = new ArrayList();
        if (this.mPreviousNameSwitchImageAnimatorSet != null && this.mPreviousNameSwitchImageAnimatorSet.isRunning()) {
            this.mPreviousNameSwitchImageAnimatorSet.cancel();
        }
        this.mPreviousNameSwitchImageAnimatorSet = mPreviousNameSwitchImageAnimatorSet;
        final ObjectAnimator easeOutAnimator = this.getEaseOutAnimator((View)this.mModeSwitchTextView, 1000);
        ((Animator)easeOutAnimator).setStartDelay(200L);
        list.add(easeOutAnimator);
        this.mModeSwitchTextView.setVisibility(0);
        this.mModeSwitchTextView.setAlpha(1.0f);
        final ObjectAnimator easeOutAnimator2 = this.getEaseOutAnimator((View)this.mModeSwitchImageView, 1000);
        ((Animator)easeOutAnimator2).setStartDelay(200L);
        list.add(easeOutAnimator2);
        this.mModeSwitchImageView.setVisibility(0);
        this.mModeSwitchImageView.setAlpha(1.0f);
        mPreviousNameSwitchImageAnimatorSet.playTogether((Collection)list);
        return mPreviousNameSwitchImageAnimatorSet;
    }
    
    static float getPreviewAlpha(final int a, final float n) {
        return 1.0f - (1.0f - Math.abs(a) / n);
    }
    
    private boolean isScaleOut(final View view) {
        return view.getScaleX() < 1.0f || view.getScaleY() < 1.0f;
    }
    
    private void setupModeSwitchContainer() {
        this.mModeSwitchImageView = this.mNavigator.getModeSwitchImageView();
        (this.mModeSwitchTextView = this.mNavigator.getModeSwitchNameView()).setAlpha(0.0f);
        this.mModeSwitchImageView.setAlpha(0.0f);
    }
    
    AnimatorSet getCancelAnimation(final NavigatorContents navigatorContents) {
        final AnimatorSet set = new AnimatorSet();
        final ArrayList list = new ArrayList();
        list.add(this.getModeIconAutoTransitionAnimation(navigatorContents, 200));
        final Iterator<View> iterator = this.mPrimaryShortcutList.iterator();
        while (iterator.hasNext()) {
            list.add(this.getEaseInScaleAnimator(iterator.next(), 200));
        }
        if (this.mFrontAngleSwitchButton != null) {
            list.add(this.getEaseInScaleAnimator(this.mFrontAngleSwitchButton, 200));
            list.add(this.getEaseInScaleAnimator(this.mFrontAngleSwitchButton, 200));
        }
        final float alpha = this.mViewFinderCover.getAlpha();
        final View mViewFinderCover = this.mViewFinderCover;
        final int n = (int)(200.0f * alpha);
        list.add(this.getLinearFadeOutAnimator(mViewFinderCover, n, alpha));
        list.add(this.getLinearFadeInAnimator(this.mGridLineView, n, 0.0f, 1.0f));
        list.add(this.getEaseInScaleAnimator(this.mSecondaryShortcutLeft, 200));
        list.add(this.getEaseInScaleAnimator(this.mSecondaryShortcutRight, 200));
        list.add(this.getEaseInScaleAnimator(this.mCaptureButton, 200));
        list.add(this.getEaseInScaleAnimator(this.mModeSelectorShortcut, 200));
        list.add(this.getEaseInScaleAnimator(this.mModeShortcutButton, 200));
        if (this.isScaleOut(this.mThumbnail)) {
            list.add(this.getEaseInScaleAnimator(this.mThumbnail, 200));
        }
        set.playTogether((Collection)list);
        return set;
    }
    
    AnimatorSet getExecuteAnimation(final NavigatorContents navigatorContents) {
        final AnimatorSet set = new AnimatorSet();
        final ArrayList list = new ArrayList();
        final float alpha = this.mViewFinderCover.getAlpha();
        int n;
        if (alpha < 1.0f) {
            n = (int)((1.0f - alpha) * 200.0f);
        }
        else {
            n = 200;
        }
        list.add(this.getModeIconAutoTransitionAnimation(navigatorContents, (int)(n * 0.5f)));
        final View mViewFinderCover = this.mViewFinderCover;
        final int n2 = (int)(200.0f * (1.0f - alpha) * 0.5f);
        list.add(this.getLinearFadeInAnimator(mViewFinderCover, n2, alpha, 1.0f));
        final Iterator<View> iterator = this.mPrimaryShortcutList.iterator();
        while (iterator.hasNext()) {
            list.add(this.getEaseOutScaleAnimator(iterator.next(), 100));
        }
        if (this.mFrontAngleSwitchButton != null) {
            list.add(this.getEaseOutScaleAnimator(this.mFrontAngleSwitchButton, 100));
        }
        list.add(this.getEaseOutScaleAnimator(this.mSecondaryShortcutLeft, 100));
        list.add(this.getEaseOutScaleAnimator(this.mSecondaryShortcutRight, 100));
        list.add(this.getEaseOutScaleAnimator(this.mCaptureButton, 100));
        list.add(this.getEaseOutScaleAnimator(this.mModeSelectorShortcut, 100));
        list.add(this.getEaseOutScaleAnimator(this.mModeShortcutButton, 100));
        list.add(this.getLinearFadeOutAnimator(this.mGridLineView, n2, alpha));
        set.playTogether((Collection)list);
        return set;
    }
    
    AnimatorSet getFinishAnimation(final CapturingMode capturingMode) {
        final AnimatorSet set = new AnimatorSet();
        final ArrayList list = new ArrayList();
        if (((PowerManager)this.mModeSwitchTextView.getContext().getSystemService("power")).isPowerSaveMode()) {
            this.mModeSwitchTextView.setVisibility(8);
            this.mModeSwitchImageView.setVisibility(8);
        }
        else if (!ModeSelectorInternalMode.exists(capturingMode)) {
            this.mNavigator.updateModeSwitchViews();
            list.add(this.getModeSwitchEaseOutAnimation());
        }
        final Iterator<View> iterator = this.mPrimaryShortcutList.iterator();
        while (iterator.hasNext()) {
            list.add(this.getEaseInScaleAnimator(iterator.next(), 200));
        }
        if (this.mFrontAngleSwitchButton != null) {
            list.add(this.getEaseInScaleAnimator(this.mFrontAngleSwitchButton, 200));
        }
        list.add(this.getEaseInScaleAnimator(this.mSecondaryShortcutLeft, 200));
        list.add(this.getEaseInScaleAnimator(this.mSecondaryShortcutRight, 200));
        list.add(this.getEaseInScaleAnimator(this.mCaptureButton, 200));
        list.add(this.getEaseInScaleAnimator(this.mModeSelectorShortcut, 200));
        list.add(this.getEaseInScaleAnimator(this.mModeShortcutButton, 200));
        if (this.isScaleOut(this.mThumbnail)) {
            list.add(this.getEaseInScaleAnimator(this.mThumbnail, 200));
        }
        list.add(this.getLinearFadeOutAnimator(this.mViewFinderCover, 200, this.mViewFinderCover.getAlpha()));
        list.add(this.getLinearFadeInAnimator(this.mGridLineView, 200, 0.0f, 1.0f));
        set.playTogether((Collection)list);
        return set;
    }
    
    AnimatorSet getStartAnimation() {
        final AnimatorSet set = new AnimatorSet();
        final ArrayList list = new ArrayList();
        if (this.mModeSwitchTextView == null) {
            this.setupModeSwitchContainer();
        }
        final Iterator<View> iterator = this.mPrimaryShortcutList.iterator();
        while (iterator.hasNext()) {
            list.add(this.getEaseOutScaleAnimator(iterator.next(), 200));
        }
        if (this.mFrontAngleSwitchButton != null) {
            list.add(this.getEaseOutScaleAnimator(this.mFrontAngleSwitchButton, 200));
            list.add(this.getEaseOutScaleAnimator(this.mFrontAngleSwitchButton, 200));
        }
        list.add(this.getEaseOutScaleAnimator(this.mSecondaryShortcutLeft, 200));
        list.add(this.getEaseOutScaleAnimator(this.mSecondaryShortcutRight, 200));
        list.add(this.getEaseOutScaleAnimator(this.mCaptureButton, 200));
        list.add(this.getEaseOutScaleAnimator(this.mModeSelectorShortcut, 200));
        list.add(this.getEaseOutScaleAnimator(this.mModeShortcutButton, 200));
        list.add(this.getLinearFadeOutAnimator(this.mGridLineView, 200, 1.0f));
        set.playTogether((Collection)list);
        return set;
    }
    
    void resume() {
        if (CamLog.VERBOSE) {
            CamLog.d("resume");
        }
        if (this.mModeSwitchTextView != null) {
            this.mModeSwitchTextView.setAlpha(0.0f);
            this.mModeSwitchImageView.setAlpha(0.0f);
        }
        for (final View view : this.mPrimaryShortcutList) {
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
}
