package com.sonyericsson.android.camera.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.sonyericsson.cameracommon.utility.RotationUtil;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class BurstCountView extends TextView {
    private final ObjectAnimator mFadeOutAnimator;

    public BurstCountView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mFadeOutAnimator = ObjectAnimator.ofFloat(this, "alpha", 1.0f, 0.0f);
        this.mFadeOutAnimator.setDuration(1000L);
        this.mFadeOutAnimator.addListener(new Animator.AnimatorListener() { // from class: com.sonyericsson.android.camera.view.BurstCountView.1
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                BurstCountView.this.setVisibility(4);
                BurstCountView.this.setAlpha(1.0f);
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                BurstCountView.this.setVisibility(4);
                BurstCountView.this.setAlpha(1.0f);
            }
        });
    }

    public void update(int i) {
        setText(Integer.toString(i));
        if (this.mFadeOutAnimator.isRunning()) {
            this.mFadeOutAnimator.cancel();
        }
        if (getVisibility() != 0) {
            setAlpha(1.0f);
            setVisibility(0);
        }
    }

    public void hide() {
        if (getVisibility() != 0 || this.mFadeOutAnimator.isRunning()) {
            return;
        }
        this.mFadeOutAnimator.start();
    }

    public void setUiOrientation(int i) {
        setRotation(RotationUtil.getAngle(i));
    }
}
