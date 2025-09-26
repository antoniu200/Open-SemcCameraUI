// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view;

import android.view.animation.Animation$AnimationListener;
import android.view.animation.AnimationUtils;
import android.content.Context;
import com.sonyericsson.cameracommon.utility.ResourceUtil;
import android.view.ViewGroup$MarginLayoutParams;
import android.view.ViewStub;
import android.widget.TextView;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import com.sonyericsson.cameracommon.rotatableview.EdgeRotatableContainerView;
import android.view.View;
import android.view.animation.Animation;
import android.app.Activity;

public class PredictiveCaptureIndicatorController
{
    private final Activity mActivity;
    private Animation mAnimation;
    private View mBackground;
    private boolean mIsAnimationRunning;
    private int mOrientation;
    private EdgeRotatableContainerView mRoot;
    private final LayoutDependencyResolver.ScreenAspect mScreenAspect;
    private TextView mTextView;
    private boolean mVisible;
    
    public PredictiveCaptureIndicatorController(final Activity mActivity, final LayoutDependencyResolver.ScreenAspect mScreenAspect) {
        this.mActivity = mActivity;
        this.mScreenAspect = mScreenAspect;
        this.mOrientation = 2;
        this.mIsAnimationRunning = false;
        this.mVisible = false;
    }
    
    private void initPredictiveCaptureIndicator() {
        (this.mRoot = (EdgeRotatableContainerView)((ViewStub)this.mActivity.findViewById(2131296497)).inflate()).setOrientation(this.mOrientation);
        if (this.mScreenAspect == LayoutDependencyResolver.ScreenAspect.EIGHTEEN_NINE) {
            ((ViewGroup$MarginLayoutParams)this.mRoot.getLayoutParams()).rightMargin = ResourceUtil.getDimensionPixelSize((Context)this.mActivity, this.mActivity.getPackageName(), 2131165490);
        }
        this.mBackground = this.mActivity.findViewById(2131296495);
        this.mTextView = (TextView)this.mActivity.findViewById(2131296498);
        (this.mAnimation = AnimationUtils.loadAnimation((Context)this.mActivity, 2130771989)).setAnimationListener((Animation$AnimationListener)new Animation$AnimationListener(this) {
            final PredictiveCaptureIndicatorController this$0;
            
            public void onAnimationEnd(final Animation animation) {
                this.this$0.mIsAnimationRunning = false;
                this.this$0.update();
            }
            
            public void onAnimationRepeat(final Animation animation) {
            }
            
            public void onAnimationStart(final Animation animation) {
            }
        });
    }
    
    private void update() {
        if (this.mRoot == null) {
            return;
        }
        if (this.mIsAnimationRunning && this.mVisible) {
            this.mRoot.setVisibility(0);
        }
        else {
            this.mRoot.setVisibility(4);
        }
    }
    
    public void cancelAnimation() {
        if (this.mRoot == null) {
            return;
        }
        this.mBackground.clearAnimation();
        this.mIsAnimationRunning = false;
        this.update();
    }
    
    public void hide() {
        this.mVisible = false;
        this.update();
    }
    
    public void setOrientation(final int n) {
        this.mOrientation = n;
        if (this.mRoot != null) {
            this.mRoot.setOrientation(n);
        }
    }
    
    public void show() {
        this.mVisible = true;
        this.update();
    }
    
    public void startAnimation() {
        if (this.mRoot == null) {
            this.initPredictiveCaptureIndicator();
        }
        this.mTextView.announceForAccessibility((CharSequence)this.mActivity.getResources().getString(2131690004));
        this.mIsAnimationRunning = true;
        this.update();
        this.mBackground.startAnimation(this.mAnimation);
    }
}
