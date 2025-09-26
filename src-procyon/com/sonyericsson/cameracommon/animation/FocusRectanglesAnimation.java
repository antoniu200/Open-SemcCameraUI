// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.animation;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.AnimationDrawable;
import android.view.animation.AnimationSet;
import android.view.animation.Animation$AnimationListener;
import android.view.animation.AnimationUtils;
import java.util.HashMap;
import android.view.animation.AlphaAnimation;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import java.util.Map;

public class FocusRectanglesAnimation
{
    public static final String TAG = "FocusRectanglesAnimation";
    private final Map<Animation, View> mAnimationMap;
    private final Context mContext;
    private AlphaAnimation mFadeOutAnimation;
    private final AnimationConfig mObjectConfig;
    private final AnimationConfig mSingleConfig;
    private final AnimationConfig mTouchConfig;
    
    public FocusRectanglesAnimation(final Context mContext) {
        this.mContext = mContext;
        this.mAnimationMap = new HashMap<Animation, View>();
        this.mSingleConfig = new AnimationConfig(2131165338, 2131165337, 2131690319, 2131361800);
        this.mTouchConfig = new AnimationConfig(2131165338, 2131165337, 2131690320, 2131361800);
        this.mObjectConfig = new AnimationConfig(2131165336, 2131165335, 2131690318, 2131361800);
    }
    
    private AlphaAnimation getFadeOutAnimation() {
        if (this.mFadeOutAnimation == null) {
            (this.mFadeOutAnimation = (AlphaAnimation)AnimationUtils.loadAnimation(this.mContext, 2130771985)).setAnimationListener((Animation$AnimationListener)new FadeOutAnimationListener());
        }
        return this.mFadeOutAnimation;
    }
    
    private void playAfFadeOutAnimation(final View view) {
        final AlphaAnimation fadeOutAnimation = this.getFadeOutAnimation();
        fadeOutAnimation.setAnimationListener((Animation$AnimationListener)new FadeOutAnimationListener());
        view.startAnimation((Animation)fadeOutAnimation);
        this.mAnimationMap.put((Animation)fadeOutAnimation, view);
    }
    
    private AnimationSet playTouchDownAnimation(final View view, AnimationSet touchDownAnimation, final AnimationConfig animationConfig) {
        touchDownAnimation = this.getTouchDownAnimation(view, touchDownAnimation, animationConfig);
        view.startAnimation((Animation)touchDownAnimation);
        return touchDownAnimation;
    }
    
    private AnimationSet playTouchUpAnimation(final View view, AnimationSet touchUpAnimation, final AnimationConfig animationConfig) {
        touchUpAnimation = this.getTouchUpAnimation(view, touchUpAnimation, animationConfig);
        view.startAnimation((Animation)touchUpAnimation);
        return touchUpAnimation;
    }
    
    public void cancelAfFocusAnimationObject(final View view) {
        if (view.getWidth() != this.mObjectConfig.mToWidth || view.getHeight() != this.mObjectConfig.mToHeight) {
            view.getLayoutParams().width = this.mObjectConfig.mToWidth;
            view.getLayoutParams().height = this.mObjectConfig.mToHeight;
            view.requestLayout();
        }
    }
    
    public void cancelAfFocusAnimationSingle(final View view) {
        if (view.getWidth() != this.mSingleConfig.mToWidth || view.getHeight() != this.mSingleConfig.mToHeight) {
            view.getLayoutParams().width = this.mSingleConfig.mToWidth;
            view.getLayoutParams().height = this.mSingleConfig.mToHeight;
            view.requestLayout();
        }
    }
    
    public void cancelAfFocusAnimationTouch(final View view) {
        if (view.getWidth() != this.mTouchConfig.mToWidth || view.getHeight() != this.mTouchConfig.mToHeight) {
            view.getLayoutParams().width = this.mTouchConfig.mToWidth;
            view.getLayoutParams().height = this.mTouchConfig.mToHeight;
            view.requestLayout();
        }
    }
    
    public AnimationConfig getObjectAnimationConfig() {
        return this.mObjectConfig;
    }
    
    public AnimationConfig getTouchAnimationConfig() {
        return this.mTouchConfig;
    }
    
    public AnimationSet getTouchDownAnimation(final View view, final AnimationSet set, final AnimationConfig animationConfig) {
        AnimationSet set2 = set;
        if (set == null) {
            set2 = (AnimationSet)AnimationUtils.loadAnimation(this.mContext, 2130771986);
        }
        return set2;
    }
    
    public AnimationSet getTouchUpAnimation(final View view, final AnimationSet set, final AnimationConfig animationConfig) {
        AnimationSet set2 = set;
        if (set == null) {
            set2 = (AnimationSet)AnimationUtils.loadAnimation(this.mContext, 2130771987);
        }
        return set2;
    }
    
    public void playAfFadeOutAnimationObject(final View view) {
        this.playAfFadeOutAnimation(view);
    }
    
    public void playAfFadeOutAnimationSingle(final View view) {
        this.playAfFadeOutAnimation(view);
    }
    
    public void playAfFadeOutAnimationTouch(final View view) {
        this.playAfFadeOutAnimation(view);
    }
    
    public void playAfFocusInAnimationSingle(final View view) {
        this.startFocusAnimation(view, 2131230865);
    }
    
    public void playAfFocusInAnimationTouch(final View view, final int n) {
        this.startFocusAnimation(view, n);
    }
    
    public void playTouchDownAnimation(final View view) {
        this.startFocusAnimation(view, 2131230862);
    }
    
    public void playTouchUpAnimation(final View view) {
        this.startFocusAnimation(view, 2131230866);
    }
    
    public void startFocusAnimation(final View view, final int backgroundResource) {
        view.setBackgroundResource(backgroundResource);
        final Drawable background = view.getBackground();
        if (background instanceof AnimationDrawable) {
            ((AnimationDrawable)background).start();
        }
        else if (view.getAnimation() != null) {
            view.clearAnimation();
        }
    }
    
    public void stopFocusAnimation(final View view) {
        final Drawable background = view.getBackground();
        if (background instanceof AnimationDrawable) {
            ((AnimationDrawable)background).stop();
        }
    }
    
    public class AnimationConfig
    {
        public final int mDuration;
        public final int mFromHeight;
        public final int mFromWidth;
        public final int mToHeight;
        public final int mToWidth;
        final FocusRectanglesAnimation this$0;
        
        public AnimationConfig(final FocusRectanglesAnimation this$0, final int n, final int n2, final int n3, final int n4) {
            this.this$0 = this$0;
            final float floatValue = Float.valueOf(this$0.mContext.getResources().getString(n3));
            this.mToWidth = this$0.mContext.getResources().getDimensionPixelSize(n);
            this.mToHeight = this$0.mContext.getResources().getDimensionPixelSize(n2);
            this.mFromWidth = (int)(this.mToWidth * floatValue);
            this.mFromHeight = (int)(this.mToHeight * floatValue);
            this.mDuration = this$0.mContext.getResources().getInteger(n4);
        }
    }
    
    private class FadeOutAnimationListener implements Animation$AnimationListener
    {
        final FocusRectanglesAnimation this$0;
        
        private FadeOutAnimationListener(final FocusRectanglesAnimation this$0) {
            this.this$0 = this$0;
        }
        
        public void onAnimationEnd(final Animation animation) {
            final View view = this.this$0.mAnimationMap.get(animation);
            if (view != null) {
                view.setVisibility(4);
            }
            this.this$0.mAnimationMap.remove(animation);
        }
        
        public void onAnimationRepeat(final Animation animation) {
        }
        
        public void onAnimationStart(final Animation animation) {
        }
    }
}
