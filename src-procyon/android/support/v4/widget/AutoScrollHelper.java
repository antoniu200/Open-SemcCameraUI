// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.widget;

import android.view.animation.AnimationUtils;
import android.view.MotionEvent;
import android.os.SystemClock;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.content.res.Resources;
import android.view.animation.AccelerateInterpolator;
import android.support.annotation.NonNull;
import android.view.ViewConfiguration;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.View$OnTouchListener;

public abstract class AutoScrollHelper implements View$OnTouchListener
{
    private static final int DEFAULT_ACTIVATION_DELAY;
    private static final int DEFAULT_EDGE_TYPE = 1;
    private static final float DEFAULT_MAXIMUM_EDGE = Float.MAX_VALUE;
    private static final int DEFAULT_MAXIMUM_VELOCITY_DIPS = 1575;
    private static final int DEFAULT_MINIMUM_VELOCITY_DIPS = 315;
    private static final int DEFAULT_RAMP_DOWN_DURATION = 500;
    private static final int DEFAULT_RAMP_UP_DURATION = 500;
    private static final float DEFAULT_RELATIVE_EDGE = 0.2f;
    private static final float DEFAULT_RELATIVE_VELOCITY = 1.0f;
    public static final int EDGE_TYPE_INSIDE = 0;
    public static final int EDGE_TYPE_INSIDE_EXTEND = 1;
    public static final int EDGE_TYPE_OUTSIDE = 2;
    private static final int HORIZONTAL = 0;
    public static final float NO_MAX = Float.MAX_VALUE;
    public static final float NO_MIN = 0.0f;
    public static final float RELATIVE_UNSPECIFIED = 0.0f;
    private static final int VERTICAL = 1;
    private int mActivationDelay;
    private boolean mAlreadyDelayed;
    boolean mAnimating;
    private final Interpolator mEdgeInterpolator;
    private int mEdgeType;
    private boolean mEnabled;
    private boolean mExclusive;
    private float[] mMaximumEdges;
    private float[] mMaximumVelocity;
    private float[] mMinimumVelocity;
    boolean mNeedsCancel;
    boolean mNeedsReset;
    private float[] mRelativeEdges;
    private float[] mRelativeVelocity;
    private Runnable mRunnable;
    final ClampedScroller mScroller;
    final View mTarget;
    
    static {
        DEFAULT_ACTIVATION_DELAY = ViewConfiguration.getTapTimeout();
    }
    
    public AutoScrollHelper(@NonNull final View mTarget) {
        this.mScroller = new ClampedScroller();
        this.mEdgeInterpolator = (Interpolator)new AccelerateInterpolator();
        this.mRelativeEdges = new float[] { 0.0f, 0.0f };
        this.mMaximumEdges = new float[] { Float.MAX_VALUE, Float.MAX_VALUE };
        this.mRelativeVelocity = new float[] { 0.0f, 0.0f };
        this.mMinimumVelocity = new float[] { 0.0f, 0.0f };
        this.mMaximumVelocity = new float[] { Float.MAX_VALUE, Float.MAX_VALUE };
        this.mTarget = mTarget;
        final DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        final int n = (int)(1575.0f * displayMetrics.density + 0.5f);
        final int n2 = (int)(315.0f * displayMetrics.density + 0.5f);
        final float n3 = (float)n;
        this.setMaximumVelocity(n3, n3);
        final float n4 = (float)n2;
        this.setMinimumVelocity(n4, n4);
        this.setEdgeType(1);
        this.setMaximumEdges(Float.MAX_VALUE, Float.MAX_VALUE);
        this.setRelativeEdges(0.2f, 0.2f);
        this.setRelativeVelocity(1.0f, 1.0f);
        this.setActivationDelay(AutoScrollHelper.DEFAULT_ACTIVATION_DELAY);
        this.setRampUpDuration(500);
        this.setRampDownDuration(500);
    }
    
    private float computeTargetVelocity(final int n, float edgeValue, float n2, float n3) {
        edgeValue = this.getEdgeValue(this.mRelativeEdges[n], n2, this.mMaximumEdges[n], edgeValue);
        final float n4 = fcmpl(edgeValue, 0.0f);
        if (n4 == 0) {
            return 0.0f;
        }
        final float n5 = this.mRelativeVelocity[n];
        n2 = this.mMinimumVelocity[n];
        final float n6 = this.mMaximumVelocity[n];
        n3 *= n5;
        if (n4 > 0) {
            return constrain(edgeValue * n3, n2, n6);
        }
        return -constrain(-edgeValue * n3, n2, n6);
    }
    
    static float constrain(final float n, final float n2, final float n3) {
        if (n > n3) {
            return n3;
        }
        if (n < n2) {
            return n2;
        }
        return n;
    }
    
    static int constrain(final int n, final int n2, final int n3) {
        if (n > n3) {
            return n3;
        }
        if (n < n2) {
            return n2;
        }
        return n;
    }
    
    private float constrainEdgeValue(final float n, final float n2) {
        if (n2 == 0.0f) {
            return 0.0f;
        }
        switch (this.mEdgeType) {
            case 2: {
                if (n < 0.0f) {
                    return n / -n2;
                }
                break;
            }
            case 0:
            case 1: {
                if (n >= n2) {
                    break;
                }
                if (n >= 0.0f) {
                    return 1.0f - n / n2;
                }
                if (this.mAnimating && this.mEdgeType == 1) {
                    return 1.0f;
                }
                break;
            }
        }
        return 0.0f;
    }
    
    private float getEdgeValue(float n, final float n2, float constrainEdgeValue, final float n3) {
        n = constrain(n * n2, 0.0f, constrainEdgeValue);
        constrainEdgeValue = this.constrainEdgeValue(n3, n);
        n = this.constrainEdgeValue(n2 - n3, n) - constrainEdgeValue;
        if (n < 0.0f) {
            n = -this.mEdgeInterpolator.getInterpolation(-n);
        }
        else {
            if (n <= 0.0f) {
                return 0.0f;
            }
            n = this.mEdgeInterpolator.getInterpolation(n);
        }
        return constrain(n, -1.0f, 1.0f);
    }
    
    private void requestStop() {
        if (this.mNeedsReset) {
            this.mAnimating = false;
        }
        else {
            this.mScroller.requestStop();
        }
    }
    
    private void startAnimating() {
        if (this.mRunnable == null) {
            this.mRunnable = new ScrollAnimationRunnable();
        }
        this.mAnimating = true;
        this.mNeedsReset = true;
        if (!this.mAlreadyDelayed && this.mActivationDelay > 0) {
            ViewCompat.postOnAnimationDelayed(this.mTarget, this.mRunnable, this.mActivationDelay);
        }
        else {
            this.mRunnable.run();
        }
        this.mAlreadyDelayed = true;
    }
    
    public abstract boolean canTargetScrollHorizontally(final int p0);
    
    public abstract boolean canTargetScrollVertically(final int p0);
    
    void cancelTargetTouch() {
        final long uptimeMillis = SystemClock.uptimeMillis();
        final MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
        this.mTarget.onTouchEvent(obtain);
        obtain.recycle();
    }
    
    public boolean isEnabled() {
        return this.mEnabled;
    }
    
    public boolean isExclusive() {
        return this.mExclusive;
    }
    
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        final boolean mEnabled = this.mEnabled;
        final boolean b = false;
        if (!mEnabled) {
            return false;
        }
        switch (motionEvent.getActionMasked()) {
            case 1:
            case 3: {
                this.requestStop();
                break;
            }
            case 0: {
                this.mNeedsCancel = true;
                this.mAlreadyDelayed = false;
            }
            case 2: {
                this.mScroller.setTargetVelocity(this.computeTargetVelocity(0, motionEvent.getX(), (float)view.getWidth(), (float)this.mTarget.getWidth()), this.computeTargetVelocity(1, motionEvent.getY(), (float)view.getHeight(), (float)this.mTarget.getHeight()));
                if (!this.mAnimating && this.shouldAnimate()) {
                    this.startAnimating();
                    break;
                }
                break;
            }
        }
        boolean b2 = b;
        if (this.mExclusive) {
            b2 = b;
            if (this.mAnimating) {
                b2 = true;
            }
        }
        return b2;
    }
    
    public abstract void scrollTargetBy(final int p0, final int p1);
    
    @NonNull
    public AutoScrollHelper setActivationDelay(final int mActivationDelay) {
        this.mActivationDelay = mActivationDelay;
        return this;
    }
    
    @NonNull
    public AutoScrollHelper setEdgeType(final int mEdgeType) {
        this.mEdgeType = mEdgeType;
        return this;
    }
    
    public AutoScrollHelper setEnabled(final boolean mEnabled) {
        if (this.mEnabled && !mEnabled) {
            this.requestStop();
        }
        this.mEnabled = mEnabled;
        return this;
    }
    
    public AutoScrollHelper setExclusive(final boolean mExclusive) {
        this.mExclusive = mExclusive;
        return this;
    }
    
    @NonNull
    public AutoScrollHelper setMaximumEdges(final float n, final float n2) {
        this.mMaximumEdges[0] = n;
        this.mMaximumEdges[1] = n2;
        return this;
    }
    
    @NonNull
    public AutoScrollHelper setMaximumVelocity(final float n, final float n2) {
        this.mMaximumVelocity[0] = n / 1000.0f;
        this.mMaximumVelocity[1] = n2 / 1000.0f;
        return this;
    }
    
    @NonNull
    public AutoScrollHelper setMinimumVelocity(final float n, final float n2) {
        this.mMinimumVelocity[0] = n / 1000.0f;
        this.mMinimumVelocity[1] = n2 / 1000.0f;
        return this;
    }
    
    @NonNull
    public AutoScrollHelper setRampDownDuration(final int rampDownDuration) {
        this.mScroller.setRampDownDuration(rampDownDuration);
        return this;
    }
    
    @NonNull
    public AutoScrollHelper setRampUpDuration(final int rampUpDuration) {
        this.mScroller.setRampUpDuration(rampUpDuration);
        return this;
    }
    
    @NonNull
    public AutoScrollHelper setRelativeEdges(final float n, final float n2) {
        this.mRelativeEdges[0] = n;
        this.mRelativeEdges[1] = n2;
        return this;
    }
    
    @NonNull
    public AutoScrollHelper setRelativeVelocity(final float n, final float n2) {
        this.mRelativeVelocity[0] = n / 1000.0f;
        this.mRelativeVelocity[1] = n2 / 1000.0f;
        return this;
    }
    
    boolean shouldAnimate() {
        final ClampedScroller mScroller = this.mScroller;
        final int verticalDirection = mScroller.getVerticalDirection();
        final int horizontalDirection = mScroller.getHorizontalDirection();
        return (verticalDirection != 0 && this.canTargetScrollVertically(verticalDirection)) || (horizontalDirection != 0 && this.canTargetScrollHorizontally(horizontalDirection));
    }
    
    private static class ClampedScroller
    {
        private long mDeltaTime;
        private int mDeltaX;
        private int mDeltaY;
        private int mEffectiveRampDown;
        private int mRampDownDuration;
        private int mRampUpDuration;
        private long mStartTime;
        private long mStopTime;
        private float mStopValue;
        private float mTargetVelocityX;
        private float mTargetVelocityY;
        
        ClampedScroller() {
            this.mStartTime = Long.MIN_VALUE;
            this.mStopTime = -1L;
            this.mDeltaTime = 0L;
            this.mDeltaX = 0;
            this.mDeltaY = 0;
        }
        
        private float getValueAt(final long n) {
            if (n < this.mStartTime) {
                return 0.0f;
            }
            if (this.mStopTime >= 0L && n >= this.mStopTime) {
                return 1.0f - this.mStopValue + this.mStopValue * AutoScrollHelper.constrain((n - this.mStopTime) / (float)this.mEffectiveRampDown, 0.0f, 1.0f);
            }
            return 0.5f * AutoScrollHelper.constrain((n - this.mStartTime) / (float)this.mRampUpDuration, 0.0f, 1.0f);
        }
        
        private float interpolateValue(final float n) {
            return -4.0f * n * n + 4.0f * n;
        }
        
        public void computeScrollDelta() {
            if (this.mDeltaTime == 0L) {
                throw new RuntimeException("Cannot compute scroll delta before calling start()");
            }
            final long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            final float interpolateValue = this.interpolateValue(this.getValueAt(currentAnimationTimeMillis));
            final long mDeltaTime = this.mDeltaTime;
            this.mDeltaTime = currentAnimationTimeMillis;
            final float n = (currentAnimationTimeMillis - mDeltaTime) * interpolateValue;
            this.mDeltaX = (int)(this.mTargetVelocityX * n);
            this.mDeltaY = (int)(n * this.mTargetVelocityY);
        }
        
        public int getDeltaX() {
            return this.mDeltaX;
        }
        
        public int getDeltaY() {
            return this.mDeltaY;
        }
        
        public int getHorizontalDirection() {
            return (int)(this.mTargetVelocityX / Math.abs(this.mTargetVelocityX));
        }
        
        public int getVerticalDirection() {
            return (int)(this.mTargetVelocityY / Math.abs(this.mTargetVelocityY));
        }
        
        public boolean isFinished() {
            return this.mStopTime > 0L && AnimationUtils.currentAnimationTimeMillis() > this.mStopTime + this.mEffectiveRampDown;
        }
        
        public void requestStop() {
            final long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            this.mEffectiveRampDown = AutoScrollHelper.constrain((int)(currentAnimationTimeMillis - this.mStartTime), 0, this.mRampDownDuration);
            this.mStopValue = this.getValueAt(currentAnimationTimeMillis);
            this.mStopTime = currentAnimationTimeMillis;
        }
        
        public void setRampDownDuration(final int mRampDownDuration) {
            this.mRampDownDuration = mRampDownDuration;
        }
        
        public void setRampUpDuration(final int mRampUpDuration) {
            this.mRampUpDuration = mRampUpDuration;
        }
        
        public void setTargetVelocity(final float mTargetVelocityX, final float mTargetVelocityY) {
            this.mTargetVelocityX = mTargetVelocityX;
            this.mTargetVelocityY = mTargetVelocityY;
        }
        
        public void start() {
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mStopTime = -1L;
            this.mDeltaTime = this.mStartTime;
            this.mStopValue = 0.5f;
            this.mDeltaX = 0;
            this.mDeltaY = 0;
        }
    }
    
    private class ScrollAnimationRunnable implements Runnable
    {
        final AutoScrollHelper this$0;
        
        ScrollAnimationRunnable(final AutoScrollHelper this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            if (!this.this$0.mAnimating) {
                return;
            }
            if (this.this$0.mNeedsReset) {
                this.this$0.mNeedsReset = false;
                this.this$0.mScroller.start();
            }
            final ClampedScroller mScroller = this.this$0.mScroller;
            if (!mScroller.isFinished() && this.this$0.shouldAnimate()) {
                if (this.this$0.mNeedsCancel) {
                    this.this$0.mNeedsCancel = false;
                    this.this$0.cancelTargetTouch();
                }
                mScroller.computeScrollDelta();
                this.this$0.scrollTargetBy(mScroller.getDeltaX(), mScroller.getDeltaY());
                ViewCompat.postOnAnimation(this.this$0.mTarget, this);
                return;
            }
            this.this$0.mAnimating = false;
        }
    }
}
