// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view;

import com.sonyericsson.cameracommon.utility.RotationUtil;
import android.animation.Animator;
import android.animation.Animator$AnimatorListener;
import android.util.AttributeSet;
import android.content.Context;
import android.animation.ObjectAnimator;
import android.widget.TextView;

public class BurstCountView extends TextView
{
    private final ObjectAnimator mFadeOutAnimator;
    
    public BurstCountView(final Context context, final AttributeSet set) {
        super(context, set);
        (this.mFadeOutAnimator = ObjectAnimator.ofFloat((Object)this, "alpha", new float[] { 1.0f, 0.0f })).setDuration(1000L);
        this.mFadeOutAnimator.addListener((Animator$AnimatorListener)new Animator$AnimatorListener(this) {
            final BurstCountView this$0;
            
            public void onAnimationCancel(final Animator animator) {
                this.this$0.setVisibility(4);
                this.this$0.setAlpha(1.0f);
            }
            
            public void onAnimationEnd(final Animator animator) {
                this.this$0.setVisibility(4);
                this.this$0.setAlpha(1.0f);
            }
            
            public void onAnimationRepeat(final Animator animator) {
            }
            
            public void onAnimationStart(final Animator animator) {
            }
        });
    }
    
    public void hide() {
        if (this.getVisibility() == 0 && !this.mFadeOutAnimator.isRunning()) {
            this.mFadeOutAnimator.start();
        }
    }
    
    public void setUiOrientation(final int n) {
        this.setRotation(RotationUtil.getAngle(n));
    }
    
    public void update(final int i) {
        this.setText((CharSequence)Integer.toString(i));
        if (this.mFadeOutAnimator.isRunning()) {
            this.mFadeOutAnimator.cancel();
        }
        if (this.getVisibility() != 0) {
            this.setAlpha(1.0f);
            this.setVisibility(0);
        }
    }
}
