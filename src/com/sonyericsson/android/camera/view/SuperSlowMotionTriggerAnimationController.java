package com.sonyericsson.android.camera.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SuperSlowMotionTriggerAnimationController {
    private Animation mAlphaAnimation;
    private View mBackground;
    private View mLineLandscape;
    private View mLinePortrait;
    private OnAnimationEndListener mListener;
    private FrameLayout mPreviewContainer;
    private View mRoot;

    public interface OnAnimationEndListener {
        void onAnimationEnd();
    }

    public void setup(FrameLayout frameLayout) {
        this.mPreviewContainer = frameLayout;
    }

    public void prepareViews() {
        if (this.mRoot != null || this.mPreviewContainer == null) {
            return;
        }
        Context context = this.mPreviewContainer.getContext();
        this.mRoot = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.trigger_view_layout, this.mPreviewContainer);
        this.mBackground = this.mRoot.findViewById(R.id.background);
        this.mLineLandscape = this.mRoot.findViewById(R.id.line_landscape);
        this.mLinePortrait = this.mRoot.findViewById(R.id.line_portrait);
        this.mAlphaAnimation = AnimationUtils.loadAnimation(context, R.anim.trigger_alpha_animation);
        this.mAlphaAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.sonyericsson.android.camera.view.SuperSlowMotionTriggerAnimationController.1
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                if (SuperSlowMotionTriggerAnimationController.this.mListener != null) {
                    SuperSlowMotionTriggerAnimationController.this.mListener.onAnimationEnd();
                }
                SuperSlowMotionTriggerAnimationController.this.mBackground.setVisibility(8);
                SuperSlowMotionTriggerAnimationController.this.mLineLandscape.setVisibility(8);
                SuperSlowMotionTriggerAnimationController.this.mLinePortrait.setVisibility(8);
            }
        });
    }

    public void start(OnAnimationEndListener onAnimationEndListener, boolean z) {
        if (this.mRoot == null) {
            return;
        }
        this.mBackground.setVisibility(0);
        this.mListener = onAnimationEndListener;
        if (LayoutOrientationResolver.getInstance().getOrientation() == (z ? LayoutOrientationResolver.LayoutOrientationType.LANDSCAPE : LayoutOrientationResolver.LayoutOrientationType.PORTRAIT)) {
            this.mLineLandscape.setVisibility(0);
            this.mLinePortrait.setVisibility(8);
            this.mLineLandscape.startAnimation(createLineAnimation(this.mRoot.getContext(), z));
        } else {
            this.mLineLandscape.setVisibility(8);
            this.mLinePortrait.setVisibility(0);
            this.mLinePortrait.startAnimation(createLineAnimation(this.mRoot.getContext(), z));
        }
        this.mBackground.startAnimation(this.mAlphaAnimation);
    }

    private Animation createLineAnimation(Context context, boolean z) {
        TranslateAnimation translateAnimation;
        if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            if (!z) {
                translateAnimation = new TranslateAnimation(2, 0.0f, 2, 1.0f, 2, 0.0f, 2, 0.0f);
            } else {
                translateAnimation = new TranslateAnimation(2, 0.0f, 2, 0.0f, 2, 0.0f, 2, 1.0f);
            }
        } else if (z) {
            translateAnimation = new TranslateAnimation(2, 0.0f, 2, 1.0f, 2, 0.0f, 2, 0.0f);
        } else {
            translateAnimation = new TranslateAnimation(2, 0.0f, 2, 0.0f, 2, 1.0f, 2, 0.0f);
        }
        translateAnimation.setDuration(300L);
        translateAnimation.setFillAfter(false);
        translateAnimation.setInterpolator(context, R.interpolator.trigger_line_animation_interpolator);
        return translateAnimation;
    }
}
