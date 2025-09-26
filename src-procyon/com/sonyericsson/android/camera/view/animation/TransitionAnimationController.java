// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.animation;

import android.animation.Animator;
import android.animation.Animator$AnimatorListener;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.Iterator;
import android.content.Context;
import com.sonyericsson.android.camera.NavigatorContents;
import java.util.concurrent.LinkedBlockingQueue;
import com.sonyericsson.android.camera.view.baselayout.SwitchAnimationView;
import android.view.View;
import java.util.List;
import com.sonyericsson.android.camera.view.ApplicationNavigator;
import android.animation.AnimatorSet;
import java.util.concurrent.BlockingQueue;

public class TransitionAnimationController
{
    private final FacingTransitionAnimation mFacingAnimation;
    private AnimationRequest mLastRequest;
    private final ModeTransitionAnimation mModeAnimation;
    private final BlockingQueue<AnimatorSet> mQueue;
    
    public TransitionAnimationController(final ApplicationNavigator applicationNavigator, final List<View> list, final View view, final View view2, final View view3, final View view4, final View view5, final View view6, final View view7, final View view8, final View view9, final SwitchAnimationView switchAnimationView) {
        this.mQueue = new LinkedBlockingQueue<AnimatorSet>();
        this.mLastRequest = null;
        this.mModeAnimation = new ModeTransitionAnimation(applicationNavigator, list, view, view2, view3, view4, view5, view6, view7, view8, view9);
        this.mFacingAnimation = new FacingTransitionAnimation(switchAnimationView, view4, list);
    }
    
    private AnimatorSet getModeIconAnimation(final AnimationRequest animationRequest) {
        final int n = TransitionAnimationController$1.$SwitchMap$com$sonyericsson$android$camera$view$animation$AnimationRequest$AnimationDegree[animationRequest.mDegree.ordinal()];
        AnimatorSet set = null;
        if (n != 4) {
            switch (n) {
                default: {
                    set = null;
                    break;
                }
                case 2: {
                    set = this.mModeAnimation.getExecuteAnimation(NavigatorContents.valueOf(animationRequest.mTarget));
                    break;
                }
                case 1: {
                    set = this.mModeAnimation.getStartAnimation();
                    break;
                }
            }
        }
        else {
            set = this.mModeAnimation.getFinishAnimation(animationRequest.mTarget);
        }
        return set;
    }
    
    private AnimatorSet getModeTouchAnimation(final AnimationRequest animationRequest) {
        AnimatorSet set = null;
        switch (TransitionAnimationController$1.$SwitchMap$com$sonyericsson$android$camera$view$animation$AnimationRequest$AnimationDegree[animationRequest.mDegree.ordinal()]) {
            default: {
                set = null;
                break;
            }
            case 4: {
                set = this.mModeAnimation.getFinishAnimation(animationRequest.mTarget);
                break;
            }
            case 3: {
                set = this.mModeAnimation.getCancelAnimation(NavigatorContents.valueOf(animationRequest.mTarget));
                break;
            }
            case 2: {
                if (!this.mQueue.isEmpty()) {
                    this.mQueue.poll().cancel();
                    this.mQueue.clear();
                }
                set = this.mModeAnimation.getExecuteAnimation(NavigatorContents.valueOf(animationRequest.mTarget));
                break;
            }
            case 1: {
                set = this.mModeAnimation.getStartAnimation();
                break;
            }
        }
        return set;
    }
    
    public static float getPreviewAlpha(final Context context, final int n) {
        return ModeTransitionAnimation.getPreviewAlpha(n, (float)getSwipeThreshold(context));
    }
    
    public static int getSwipeThreshold(final Context context) {
        return context.getResources().getDimensionPixelSize(2131165658);
    }
    
    private AnimatorSet getSwitchAnimation(final AnimationRequest animationRequest) {
        AnimatorSet set = null;
        switch (TransitionAnimationController$1.$SwitchMap$com$sonyericsson$android$camera$view$animation$AnimationRequest$AnimationDegree[animationRequest.mDegree.ordinal()]) {
            default: {
                set = null;
                break;
            }
            case 4: {
                set = this.mFacingAnimation.getAfterSwitchAnimation();
                break;
            }
            case 3: {
                set = this.mFacingAnimation.getDraggingCancelAnimation();
                break;
            }
            case 2: {
                set = this.mFacingAnimation.getSwipeSwitchAnimation();
                break;
            }
        }
        return set;
    }
    
    public static int getSwitchSwipeThreshold(final Context context) {
        return context.getResources().getDimensionPixelSize(2131165284);
    }
    
    private boolean verifyLastRequest(final AnimationRequest animationRequest) {
        if (this.mLastRequest == null) {
            return animationRequest.mDegree == AnimationRequest.AnimationDegree.START && this.mQueue.isEmpty();
        }
        if (animationRequest.mType != this.mLastRequest.mType) {
            return false;
        }
        switch (TransitionAnimationController$1.$SwitchMap$com$sonyericsson$android$camera$view$animation$AnimationRequest$AnimationDegree[this.mLastRequest.mDegree.ordinal()]) {
            default: {
                return false;
            }
            case 4: {
                if (animationRequest.mDegree == AnimationRequest.AnimationDegree.START) {
                    return true;
                }
                return false;
            }
            case 2: {
                if (animationRequest.mDegree == AnimationRequest.AnimationDegree.FINISH) {
                    return true;
                }
                return false;
            }
            case 1: {
                if (animationRequest.mDegree == AnimationRequest.AnimationDegree.EXEC || animationRequest.mDegree == AnimationRequest.AnimationDegree.CANCEL) {
                    return true;
                }
                return false;
            }
            case 3: {
                return false;
            }
        }
    }
    
    public void pause() {
        for (final AnimatorSet set : this.mQueue) {
            set.removeAllListeners();
            set.cancel();
        }
        this.mQueue.clear();
        this.mLastRequest = null;
    }
    
    public boolean requestAnimation(final AnimationRequest animationRequest) {
        return this.requestAnimation(animationRequest, null);
    }
    
    public boolean requestAnimation(final AnimationRequest animationRequest, final TransitionAnimationCallback transitionAnimationCallback) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("request source:");
            sb.append(animationRequest.mType);
            sb.append(", type:");
            sb.append(animationRequest.mDegree);
            sb.append(", mFrom:");
            sb.append(animationRequest.mFrom);
            sb.append(", mTarget:");
            sb.append(animationRequest.mTarget);
            CamLog.d(sb.toString());
        }
        if (!this.verifyLastRequest(animationRequest)) {
            return false;
        }
        AnimatorSet set = null;
        switch (TransitionAnimationController$1.$SwitchMap$com$sonyericsson$android$camera$view$animation$AnimationRequest$AnimationType[animationRequest.mType.ordinal()]) {
            default: {
                return false;
            }
            case 5: {
                if (animationRequest.mDegree == AnimationRequest.AnimationDegree.START) {
                    this.mLastRequest = animationRequest;
                    this.mFacingAnimation.getSwitchAnimationView().startDraggingStartedAnimation();
                    return true;
                }
                set = this.getSwitchAnimation(animationRequest);
                break;
            }
            case 4: {
                set = this.getModeIconAnimation(animationRequest);
                break;
            }
            case 3: {
                set = this.getModeIconAnimation(animationRequest);
                break;
            }
            case 2: {
                set = this.getModeTouchAnimation(animationRequest);
                break;
            }
            case 1: {
                set = this.getModeIconAnimation(animationRequest);
                break;
            }
        }
        if (set == null) {
            return false;
        }
        set.addListener((Animator$AnimatorListener)new TransitionAnimatorListener(animationRequest, transitionAnimationCallback));
        try {
            this.mQueue.put(set);
            this.mLastRequest = animationRequest;
            if (this.mQueue.size() == 1) {
                set.start();
            }
            return true;
        }
        catch (final InterruptedException ex) {
            CamLog.e("startAnimation failed.");
            return false;
        }
    }
    
    public void resume() {
        this.mModeAnimation.resume();
        this.mFacingAnimation.resume();
    }
    
    public boolean startSwitchDraggingAnimation(final float n) {
        if (CamLog.VERBOSE) {
            CamLog.d("startDraggingAnimation");
        }
        if (this.mLastRequest != null && this.mLastRequest.mType == AnimationRequest.AnimationType.SWITCH_TOUCH) {
            this.mFacingAnimation.getSwitchAnimationView().startDraggingAnimation(n);
            return true;
        }
        return false;
    }
    
    public interface TransitionAnimationCallback
    {
        void onAnimationFinished();
    }
    
    private class TransitionAnimatorListener implements Animator$AnimatorListener
    {
        private final TransitionAnimationCallback mCallback;
        private final AnimationRequest mRequest;
        final TransitionAnimationController this$0;
        
        private TransitionAnimatorListener(final TransitionAnimationController this$0, final AnimationRequest mRequest, final TransitionAnimationCallback mCallback) {
            this.this$0 = this$0;
            this.mRequest = mRequest;
            this.mCallback = mCallback;
        }
        
        public void onAnimationCancel(final Animator animator) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("TransitionAnimatorListener.onAnimationCancel source:");
                sb.append(this.mRequest.mType);
                sb.append(", type:");
                sb.append(this.mRequest.mDegree);
                sb.append(", mFrom:");
                sb.append(this.mRequest.mFrom);
                sb.append(", mTarget:");
                sb.append(this.mRequest.mTarget);
                CamLog.d(sb.toString());
            }
        }
        
        public void onAnimationEnd(final Animator animator) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("TransitionAnimatorListener.onAnimationEnd source:");
                sb.append(this.mRequest.mType);
                sb.append(", type:");
                sb.append(this.mRequest.mDegree);
                sb.append(", mFrom:");
                sb.append(this.mRequest.mFrom);
                sb.append(", mTarget:");
                sb.append(this.mRequest.mTarget);
                CamLog.d(sb.toString());
            }
            if (this.mCallback != null) {
                this.mCallback.onAnimationFinished();
            }
            this.this$0.mQueue.poll();
            if (this.this$0.mQueue.isEmpty()) {
                if (this.mRequest.mDegree == AnimationRequest.AnimationDegree.FINISH || this.mRequest.mDegree == AnimationRequest.AnimationDegree.CANCEL) {
                    this.this$0.mLastRequest = null;
                }
            }
            else {
                ((AnimatorSet)this.this$0.mQueue.peek()).start();
            }
        }
        
        public void onAnimationRepeat(final Animator animator) {
        }
        
        public void onAnimationStart(final Animator animator) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("TransitionAnimatorListener.onAnimationStart source:");
                sb.append(this.mRequest.mType);
                sb.append(", type:");
                sb.append(this.mRequest.mDegree);
                sb.append(", mFrom:");
                sb.append(this.mRequest.mFrom);
                sb.append(", mTarget:");
                sb.append(this.mRequest.mTarget);
                CamLog.d(sb.toString());
            }
        }
    }
}
