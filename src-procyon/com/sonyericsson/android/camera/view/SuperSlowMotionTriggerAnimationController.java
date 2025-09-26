// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view;

import android.view.animation.Animation$AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.animation.TranslateAnimation;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import android.content.Context;
import android.widget.FrameLayout;
import android.view.View;
import android.view.animation.Animation;

public class SuperSlowMotionTriggerAnimationController
{
    private Animation mAlphaAnimation;
    private View mBackground;
    private View mLineLandscape;
    private View mLinePortrait;
    private OnAnimationEndListener mListener;
    private FrameLayout mPreviewContainer;
    private View mRoot;
    
    private Animation createLineAnimation(final Context context, final boolean b) {
        TranslateAnimation translateAnimation;
        if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            if (!b) {
                translateAnimation = new TranslateAnimation(2, 0.0f, 2, 1.0f, 2, 0.0f, 2, 0.0f);
            }
            else {
                translateAnimation = new TranslateAnimation(2, 0.0f, 2, 0.0f, 2, 0.0f, 2, 1.0f);
            }
        }
        else if (b) {
            translateAnimation = new TranslateAnimation(2, 0.0f, 2, 1.0f, 2, 0.0f, 2, 0.0f);
        }
        else {
            translateAnimation = new TranslateAnimation(2, 0.0f, 2, 0.0f, 2, 1.0f, 2, 0.0f);
        }
        ((Animation)translateAnimation).setDuration(300L);
        ((Animation)translateAnimation).setFillAfter(false);
        ((Animation)translateAnimation).setInterpolator(context, 2131427329);
        return (Animation)translateAnimation;
    }
    
    public void prepareViews() {
        if (this.mRoot == null && this.mPreviewContainer != null) {
            final Context context = this.mPreviewContainer.getContext();
            this.mRoot = ((LayoutInflater)context.getSystemService("layout_inflater")).inflate(2131493021, (ViewGroup)this.mPreviewContainer);
            this.mBackground = this.mRoot.findViewById(2131296316);
            this.mLineLandscape = this.mRoot.findViewById(2131296450);
            this.mLinePortrait = this.mRoot.findViewById(2131296451);
            (this.mAlphaAnimation = AnimationUtils.loadAnimation(context, 2130772003)).setAnimationListener((Animation$AnimationListener)new Animation$AnimationListener(this) {
                final SuperSlowMotionTriggerAnimationController this$0;
                
                public void onAnimationEnd(final Animation animation) {
                    if (this.this$0.mListener != null) {
                        this.this$0.mListener.onAnimationEnd();
                    }
                    this.this$0.mBackground.setVisibility(8);
                    this.this$0.mLineLandscape.setVisibility(8);
                    this.this$0.mLinePortrait.setVisibility(8);
                }
                
                public void onAnimationRepeat(final Animation animation) {
                }
                
                public void onAnimationStart(final Animation animation) {
                }
            });
        }
    }
    
    public void setup(final FrameLayout mPreviewContainer) {
        this.mPreviewContainer = mPreviewContainer;
    }
    
    public void start(final OnAnimationEndListener mListener, final boolean b) {
        if (this.mRoot == null) {
            return;
        }
        this.mBackground.setVisibility(0);
        this.mListener = mListener;
        LayoutOrientationResolver.LayoutOrientationType layoutOrientationType;
        if (b) {
            layoutOrientationType = LayoutOrientationResolver.LayoutOrientationType.LANDSCAPE;
        }
        else {
            layoutOrientationType = LayoutOrientationResolver.LayoutOrientationType.PORTRAIT;
        }
        if (LayoutOrientationResolver.getInstance().getOrientation() == layoutOrientationType) {
            this.mLineLandscape.setVisibility(0);
            this.mLinePortrait.setVisibility(8);
            this.mLineLandscape.startAnimation(this.createLineAnimation(this.mRoot.getContext(), b));
        }
        else {
            this.mLineLandscape.setVisibility(8);
            this.mLinePortrait.setVisibility(0);
            this.mLinePortrait.startAnimation(this.createLineAnimation(this.mRoot.getContext(), b));
        }
        this.mBackground.startAnimation(this.mAlphaAnimation);
    }
    
    public interface OnAnimationEndListener
    {
        void onAnimationEnd();
    }
}
