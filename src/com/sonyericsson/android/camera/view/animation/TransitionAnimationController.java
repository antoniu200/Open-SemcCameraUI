package com.sonyericsson.android.camera.view.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.view.View;
import com.sonyericsson.android.camera.NavigatorContents;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.view.ApplicationNavigator;
import com.sonyericsson.android.camera.view.animation.AnimationRequest;
import com.sonyericsson.android.camera.view.baselayout.SwitchAnimationView;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TransitionAnimationController {
    private final FacingTransitionAnimation mFacingAnimation;
    private final ModeTransitionAnimation mModeAnimation;
    private final BlockingQueue<AnimatorSet> mQueue = new LinkedBlockingQueue();
    private AnimationRequest mLastRequest = null;

    public interface TransitionAnimationCallback {
        void onAnimationFinished();
    }

    public TransitionAnimationController(ApplicationNavigator applicationNavigator, List<View> list, View view, View view2, View view3, View view4, View view5, View view6, View view7, View view8, View view9, SwitchAnimationView switchAnimationView) {
        this.mModeAnimation = new ModeTransitionAnimation(applicationNavigator, list, view, view2, view3, view4, view5, view6, view7, view8, view9);
        this.mFacingAnimation = new FacingTransitionAnimation(switchAnimationView, view4, list);
    }

    public void resume() {
        this.mModeAnimation.resume();
        this.mFacingAnimation.resume();
    }

    public void pause() {
        for (AnimatorSet animatorSet : this.mQueue) {
            animatorSet.removeAllListeners();
            animatorSet.cancel();
        }
        this.mQueue.clear();
        this.mLastRequest = null;
    }

    public static int getSwipeThreshold(Context context) {
        return context.getResources().getDimensionPixelSize(R.dimen.swipe_threshold);
    }

    public static int getSwitchSwipeThreshold(Context context) {
        return context.getResources().getDimensionPixelSize(R.dimen.camera_switch_swipe_threshold);
    }

    public boolean requestAnimation(AnimationRequest animationRequest) {
        return requestAnimation(animationRequest, null);
    }

    public boolean requestAnimation(AnimationRequest animationRequest, TransitionAnimationCallback transitionAnimationCallback) {
        AnimatorSet modeIconAnimation;
        if (CamLog.DEBUG) {
            CamLog.d("request source:" + animationRequest.mType + ", type:" + animationRequest.mDegree + ", mFrom:" + animationRequest.mFrom + ", mTarget:" + animationRequest.mTarget);
        }
        if (!verifyLastRequest(animationRequest)) {
            return false;
        }
        switch (animationRequest.mType) {
            case MODE_ICON:
                modeIconAnimation = getModeIconAnimation(animationRequest);
                break;
            case MODE_TOUCH:
                modeIconAnimation = getModeTouchAnimation(animationRequest);
                break;
            case MODE_SELECTOR:
                modeIconAnimation = getModeIconAnimation(animationRequest);
                break;
            case MRU_SHORTCUT:
                modeIconAnimation = getModeIconAnimation(animationRequest);
                break;
            case SWITCH_TOUCH:
                if (animationRequest.mDegree == AnimationRequest.AnimationDegree.START) {
                    this.mLastRequest = animationRequest;
                    this.mFacingAnimation.getSwitchAnimationView().startDraggingStartedAnimation();
                    return true;
                }
                modeIconAnimation = getSwitchAnimation(animationRequest);
                break;
            default:
                return false;
        }
        if (modeIconAnimation == null) {
            return false;
        }
        modeIconAnimation.addListener(new TransitionAnimatorListener(animationRequest, transitionAnimationCallback));
        try {
            this.mQueue.put(modeIconAnimation);
            this.mLastRequest = animationRequest;
            if (this.mQueue.size() == 1) {
                modeIconAnimation.start();
            }
            return true;
        } catch (InterruptedException unused) {
            CamLog.e("startAnimation failed.");
            return false;
        }
    }

    private AnimatorSet getModeTouchAnimation(AnimationRequest animationRequest) {
        switch (animationRequest.mDegree) {
            case START:
                return this.mModeAnimation.getStartAnimation();
            case EXEC:
                if (!this.mQueue.isEmpty()) {
                    this.mQueue.poll().cancel();
                    this.mQueue.clear();
                }
                return this.mModeAnimation.getExecuteAnimation(NavigatorContents.valueOf(animationRequest.mTarget));
            case CANCEL:
                return this.mModeAnimation.getCancelAnimation(NavigatorContents.valueOf(animationRequest.mTarget));
            case FINISH:
                return this.mModeAnimation.getFinishAnimation(animationRequest.mTarget);
            default:
                return null;
        }
    }

    private AnimatorSet getModeIconAnimation(AnimationRequest animationRequest) {
        int i = AnonymousClass1.$SwitchMap$com$sonyericsson$android$camera$view$animation$AnimationRequest$AnimationDegree[animationRequest.mDegree.ordinal()];
        if (i != 4) {
            switch (i) {
                case 1:
                    return this.mModeAnimation.getStartAnimation();
                case 2:
                    return this.mModeAnimation.getExecuteAnimation(NavigatorContents.valueOf(animationRequest.mTarget));
                default:
                    return null;
            }
        }
        return this.mModeAnimation.getFinishAnimation(animationRequest.mTarget);
    }

    private AnimatorSet getSwitchAnimation(AnimationRequest animationRequest) {
        switch (animationRequest.mDegree) {
            case EXEC:
                return this.mFacingAnimation.getSwipeSwitchAnimation();
            case CANCEL:
                return this.mFacingAnimation.getDraggingCancelAnimation();
            case FINISH:
                return this.mFacingAnimation.getAfterSwitchAnimation();
            default:
                return null;
        }
    }

    public static float getPreviewAlpha(Context context, int i) {
        return ModeTransitionAnimation.getPreviewAlpha(i, getSwipeThreshold(context));
    }

    public boolean startSwitchDraggingAnimation(float f) {
        if (CamLog.VERBOSE) {
            CamLog.d("startDraggingAnimation");
        }
        if (this.mLastRequest == null || this.mLastRequest.mType != AnimationRequest.AnimationType.SWITCH_TOUCH) {
            return false;
        }
        this.mFacingAnimation.getSwitchAnimationView().startDraggingAnimation(f);
        return true;
    }

    private class TransitionAnimatorListener implements Animator.AnimatorListener {
        private final TransitionAnimationCallback mCallback;
        private final AnimationRequest mRequest;

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        private TransitionAnimatorListener(AnimationRequest animationRequest, TransitionAnimationCallback transitionAnimationCallback) {
            this.mRequest = animationRequest;
            this.mCallback = transitionAnimationCallback;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (CamLog.DEBUG) {
                CamLog.d("TransitionAnimatorListener.onAnimationStart source:" + this.mRequest.mType + ", type:" + this.mRequest.mDegree + ", mFrom:" + this.mRequest.mFrom + ", mTarget:" + this.mRequest.mTarget);
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (CamLog.DEBUG) {
                CamLog.d("TransitionAnimatorListener.onAnimationEnd source:" + this.mRequest.mType + ", type:" + this.mRequest.mDegree + ", mFrom:" + this.mRequest.mFrom + ", mTarget:" + this.mRequest.mTarget);
            }
            if (this.mCallback != null) {
                this.mCallback.onAnimationFinished();
            }
            TransitionAnimationController.this.mQueue.poll();
            if (!TransitionAnimationController.this.mQueue.isEmpty()) {
                ((AnimatorSet) TransitionAnimationController.this.mQueue.peek()).start();
            } else if (this.mRequest.mDegree == AnimationRequest.AnimationDegree.FINISH || this.mRequest.mDegree == AnimationRequest.AnimationDegree.CANCEL) {
                TransitionAnimationController.this.mLastRequest = null;
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (CamLog.DEBUG) {
                CamLog.d("TransitionAnimatorListener.onAnimationCancel source:" + this.mRequest.mType + ", type:" + this.mRequest.mDegree + ", mFrom:" + this.mRequest.mFrom + ", mTarget:" + this.mRequest.mTarget);
            }
        }
    }

    // inside TransitionAnimationController
    private boolean verifyLastRequest(AnimationRequest req) {
        // no previous request → only allow START when queue is empty
        if (mLastRequest == null) {
            return req.mDegree == AnimationRequest.AnimationDegree.START
                    && mQueue.isEmpty();
        }

        // different animation type → reject
        if (req.mType != mLastRequest.mType) {
            return false;
        }

        // allowed sequences based on the last degree
        switch (mLastRequest.mDegree) {
            case START:
                // after START: EXEC or CANCEL is valid
                return req.mDegree == AnimationRequest.AnimationDegree.EXEC
                        || req.mDegree == AnimationRequest.AnimationDegree.CANCEL;

            case EXEC:
                // after EXEC: FINISH is valid
                return req.mDegree == AnimationRequest.AnimationDegree.FINISH;

            case CANCEL:
                // after CANCEL: a fresh START is valid
                return req.mDegree == AnimationRequest.AnimationDegree.START;

            case FINISH:
            default:
                // after FINISH (or anything unexpected): nothing else is valid
                return false;
        }
    }
}
