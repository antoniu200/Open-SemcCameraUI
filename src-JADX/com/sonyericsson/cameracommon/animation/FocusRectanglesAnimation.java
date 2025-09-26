package com.sonyericsson.cameracommon.animation;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import com.sonyericsson.android.camera.R;
import java.util.HashMap;
import java.util.Map;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class FocusRectanglesAnimation {
    public static final String TAG = "FocusRectanglesAnimation";
    private final Context mContext;
    private AlphaAnimation mFadeOutAnimation;
    private final Map<Animation, View> mAnimationMap = new HashMap();
    private final AnimationConfig mSingleConfig = new AnimationConfig(R.dimen.focus_rect_single_width, R.dimen.focus_rect_single_height, R.string.focus_indicator_animation_mag_single, R.integer.focus_indicator_animation_focusin_duration);
    private final AnimationConfig mTouchConfig = new AnimationConfig(R.dimen.focus_rect_single_width, R.dimen.focus_rect_single_height, R.string.focus_indicator_animation_mag_touch, R.integer.focus_indicator_animation_focusin_duration);
    private final AnimationConfig mObjectConfig = new AnimationConfig(R.dimen.focus_rect_object_width, R.dimen.focus_rect_object_height, R.string.focus_indicator_animation_mag_object, R.integer.focus_indicator_animation_focusin_duration);

    public class AnimationConfig {
        public final int mDuration;
        public final int mFromHeight;
        public final int mFromWidth;
        public final int mToHeight;
        public final int mToWidth;

        public AnimationConfig(int i, int i2, int i3, int i4) {
            float fFloatValue = Float.valueOf(FocusRectanglesAnimation.this.mContext.getResources().getString(i3)).floatValue();
            this.mToWidth = FocusRectanglesAnimation.this.mContext.getResources().getDimensionPixelSize(i);
            this.mToHeight = FocusRectanglesAnimation.this.mContext.getResources().getDimensionPixelSize(i2);
            this.mFromWidth = (int) (this.mToWidth * fFloatValue);
            this.mFromHeight = (int) (this.mToHeight * fFloatValue);
            this.mDuration = FocusRectanglesAnimation.this.mContext.getResources().getInteger(i4);
        }
    }

    public FocusRectanglesAnimation(Context context) {
        this.mContext = context;
    }

    public AnimationConfig getObjectAnimationConfig() {
        return this.mObjectConfig;
    }

    public AnimationConfig getTouchAnimationConfig() {
        return this.mTouchConfig;
    }

    public void playAfFocusInAnimationSingle(View view) {
        startFocusAnimation(view, 2131230865);
    }

    public void playTouchDownAnimation(View view) {
        startFocusAnimation(view, 2131230862);
    }

    public void playTouchUpAnimation(View view) {
        startFocusAnimation(view, 2131230866);
    }

    public void playAfFadeOutAnimationSingle(View view) {
        playAfFadeOutAnimation(view);
    }

    public void playAfFocusInAnimationTouch(View view, int i) {
        startFocusAnimation(view, i);
    }

    public void startFocusAnimation(View view, int i) {
        view.setBackgroundResource(i);
        Drawable background = view.getBackground();
        if (background instanceof AnimationDrawable) {
            ((AnimationDrawable) background).start();
        } else if (view.getAnimation() != null) {
            view.clearAnimation();
        }
    }

    public void stopFocusAnimation(View view) {
        Drawable background = view.getBackground();
        if (background instanceof AnimationDrawable) {
            ((AnimationDrawable) background).stop();
        }
    }

    public void playAfFadeOutAnimationTouch(View view) {
        playAfFadeOutAnimation(view);
    }

    public void playAfFadeOutAnimationObject(View view) {
        playAfFadeOutAnimation(view);
    }

    private void playAfFadeOutAnimation(View view) {
        AlphaAnimation fadeOutAnimation = getFadeOutAnimation();
        fadeOutAnimation.setAnimationListener(new FadeOutAnimationListener());
        view.startAnimation(fadeOutAnimation);
        this.mAnimationMap.put(fadeOutAnimation, view);
    }

    private AnimationSet playTouchDownAnimation(View view, AnimationSet animationSet, AnimationConfig animationConfig) {
        AnimationSet touchDownAnimation = getTouchDownAnimation(view, animationSet, animationConfig);
        view.startAnimation(touchDownAnimation);
        return touchDownAnimation;
    }

    private AnimationSet playTouchUpAnimation(View view, AnimationSet animationSet, AnimationConfig animationConfig) {
        AnimationSet touchUpAnimation = getTouchUpAnimation(view, animationSet, animationConfig);
        view.startAnimation(touchUpAnimation);
        return touchUpAnimation;
    }

    public AnimationSet getTouchDownAnimation(View view, AnimationSet animationSet, AnimationConfig animationConfig) {
        return animationSet == null ? (AnimationSet) AnimationUtils.loadAnimation(this.mContext, R.anim.focus_touch_down) : animationSet;
    }

    public AnimationSet getTouchUpAnimation(View view, AnimationSet animationSet, AnimationConfig animationConfig) {
        return animationSet == null ? (AnimationSet) AnimationUtils.loadAnimation(this.mContext, R.anim.focus_touch_up) : animationSet;
    }

    private AlphaAnimation getFadeOutAnimation() {
        if (this.mFadeOutAnimation == null) {
            this.mFadeOutAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(this.mContext, R.anim.focus_indicator_fade_out);
            this.mFadeOutAnimation.setAnimationListener(new FadeOutAnimationListener());
        }
        return this.mFadeOutAnimation;
    }

    private class FadeOutAnimationListener implements Animation.AnimationListener {
        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
        }

        private FadeOutAnimationListener() {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            View view = (View) FocusRectanglesAnimation.this.mAnimationMap.get(animation);
            if (view != null) {
                view.setVisibility(4);
            }
            FocusRectanglesAnimation.this.mAnimationMap.remove(animation);
        }
    }

    public void cancelAfFocusAnimationObject(View view) {
        if (view.getWidth() == this.mObjectConfig.mToWidth && view.getHeight() == this.mObjectConfig.mToHeight) {
            return;
        }
        view.getLayoutParams().width = this.mObjectConfig.mToWidth;
        view.getLayoutParams().height = this.mObjectConfig.mToHeight;
        view.requestLayout();
    }

    public void cancelAfFocusAnimationSingle(View view) {
        if (view.getWidth() == this.mSingleConfig.mToWidth && view.getHeight() == this.mSingleConfig.mToHeight) {
            return;
        }
        view.getLayoutParams().width = this.mSingleConfig.mToWidth;
        view.getLayoutParams().height = this.mSingleConfig.mToHeight;
        view.requestLayout();
    }

    public void cancelAfFocusAnimationTouch(View view) {
        if (view.getWidth() == this.mTouchConfig.mToWidth && view.getHeight() == this.mTouchConfig.mToHeight) {
            return;
        }
        view.getLayoutParams().width = this.mTouchConfig.mToWidth;
        view.getLayoutParams().height = this.mTouchConfig.mToHeight;
        view.requestLayout();
    }
}
