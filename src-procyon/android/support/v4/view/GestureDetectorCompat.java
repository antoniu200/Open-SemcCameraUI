// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.view;

import android.view.GestureDetector;
import android.os.Message;
import android.view.ViewConfiguration;
import android.view.VelocityTracker;
import android.view.GestureDetector$OnDoubleTapListener;
import android.view.MotionEvent;
import android.os.Build$VERSION;
import android.os.Handler;
import android.view.GestureDetector$OnGestureListener;
import android.content.Context;

public final class GestureDetectorCompat
{
    private final GestureDetectorCompatImpl mImpl;
    
    public GestureDetectorCompat(final Context context, final GestureDetector$OnGestureListener gestureDetector$OnGestureListener) {
        this(context, gestureDetector$OnGestureListener, null);
    }
    
    public GestureDetectorCompat(final Context context, final GestureDetector$OnGestureListener gestureDetector$OnGestureListener, final Handler handler) {
        if (Build$VERSION.SDK_INT > 17) {
            this.mImpl = (GestureDetectorCompatImpl)new GestureDetectorCompatImplJellybeanMr2(context, gestureDetector$OnGestureListener, handler);
        }
        else {
            this.mImpl = (GestureDetectorCompatImpl)new GestureDetectorCompatImplBase(context, gestureDetector$OnGestureListener, handler);
        }
    }
    
    public boolean isLongpressEnabled() {
        return this.mImpl.isLongpressEnabled();
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        return this.mImpl.onTouchEvent(motionEvent);
    }
    
    public void setIsLongpressEnabled(final boolean isLongpressEnabled) {
        this.mImpl.setIsLongpressEnabled(isLongpressEnabled);
    }
    
    public void setOnDoubleTapListener(final GestureDetector$OnDoubleTapListener onDoubleTapListener) {
        this.mImpl.setOnDoubleTapListener(onDoubleTapListener);
    }
    
    interface GestureDetectorCompatImpl
    {
        boolean isLongpressEnabled();
        
        boolean onTouchEvent(final MotionEvent p0);
        
        void setIsLongpressEnabled(final boolean p0);
        
        void setOnDoubleTapListener(final GestureDetector$OnDoubleTapListener p0);
    }
    
    static class GestureDetectorCompatImplBase implements GestureDetectorCompatImpl
    {
        private static final int DOUBLE_TAP_TIMEOUT;
        private static final int LONGPRESS_TIMEOUT;
        private static final int LONG_PRESS = 2;
        private static final int SHOW_PRESS = 1;
        private static final int TAP = 3;
        private static final int TAP_TIMEOUT;
        private boolean mAlwaysInBiggerTapRegion;
        private boolean mAlwaysInTapRegion;
        MotionEvent mCurrentDownEvent;
        boolean mDeferConfirmSingleTap;
        GestureDetector$OnDoubleTapListener mDoubleTapListener;
        private int mDoubleTapSlopSquare;
        private float mDownFocusX;
        private float mDownFocusY;
        private final Handler mHandler;
        private boolean mInLongPress;
        private boolean mIsDoubleTapping;
        private boolean mIsLongpressEnabled;
        private float mLastFocusX;
        private float mLastFocusY;
        final GestureDetector$OnGestureListener mListener;
        private int mMaximumFlingVelocity;
        private int mMinimumFlingVelocity;
        private MotionEvent mPreviousUpEvent;
        boolean mStillDown;
        private int mTouchSlopSquare;
        private VelocityTracker mVelocityTracker;
        
        static {
            LONGPRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
            TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
            DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
        }
        
        GestureDetectorCompatImplBase(final Context context, final GestureDetector$OnGestureListener mListener, final Handler handler) {
            if (handler != null) {
                this.mHandler = new GestureHandler(handler);
            }
            else {
                this.mHandler = new GestureHandler();
            }
            this.mListener = mListener;
            if (mListener instanceof GestureDetector$OnDoubleTapListener) {
                this.setOnDoubleTapListener((GestureDetector$OnDoubleTapListener)mListener);
            }
            this.init(context);
        }
        
        private void cancel() {
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            this.mHandler.removeMessages(3);
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
            this.mIsDoubleTapping = false;
            this.mStillDown = false;
            this.mAlwaysInTapRegion = false;
            this.mAlwaysInBiggerTapRegion = false;
            this.mDeferConfirmSingleTap = false;
            if (this.mInLongPress) {
                this.mInLongPress = false;
            }
        }
        
        private void cancelTaps() {
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            this.mHandler.removeMessages(3);
            this.mIsDoubleTapping = false;
            this.mAlwaysInTapRegion = false;
            this.mAlwaysInBiggerTapRegion = false;
            this.mDeferConfirmSingleTap = false;
            if (this.mInLongPress) {
                this.mInLongPress = false;
            }
        }
        
        private void init(final Context context) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null");
            }
            if (this.mListener == null) {
                throw new IllegalArgumentException("OnGestureListener must not be null");
            }
            this.mIsLongpressEnabled = true;
            final ViewConfiguration value = ViewConfiguration.get(context);
            final int scaledTouchSlop = value.getScaledTouchSlop();
            final int scaledDoubleTapSlop = value.getScaledDoubleTapSlop();
            this.mMinimumFlingVelocity = value.getScaledMinimumFlingVelocity();
            this.mMaximumFlingVelocity = value.getScaledMaximumFlingVelocity();
            this.mTouchSlopSquare = scaledTouchSlop * scaledTouchSlop;
            this.mDoubleTapSlopSquare = scaledDoubleTapSlop * scaledDoubleTapSlop;
        }
        
        private boolean isConsideredDoubleTap(final MotionEvent motionEvent, final MotionEvent motionEvent2, final MotionEvent motionEvent3) {
            final boolean mAlwaysInBiggerTapRegion = this.mAlwaysInBiggerTapRegion;
            boolean b = false;
            if (!mAlwaysInBiggerTapRegion) {
                return false;
            }
            if (motionEvent3.getEventTime() - motionEvent2.getEventTime() > GestureDetectorCompatImplBase.DOUBLE_TAP_TIMEOUT) {
                return false;
            }
            final int n = (int)motionEvent.getX() - (int)motionEvent3.getX();
            final int n2 = (int)motionEvent.getY() - (int)motionEvent3.getY();
            if (n * n + n2 * n2 < this.mDoubleTapSlopSquare) {
                b = true;
            }
            return b;
        }
        
        void dispatchLongPress() {
            this.mHandler.removeMessages(3);
            this.mDeferConfirmSingleTap = false;
            this.mInLongPress = true;
            this.mListener.onLongPress(this.mCurrentDownEvent);
        }
        
        @Override
        public boolean isLongpressEnabled() {
            return this.mIsLongpressEnabled;
        }
        
        @Override
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            final int action = motionEvent.getAction();
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(motionEvent);
            final int n = action & 0xFF;
            final boolean b = false;
            final boolean b2 = n == 6;
            int actionIndex;
            if (b2) {
                actionIndex = motionEvent.getActionIndex();
            }
            else {
                actionIndex = -1;
            }
            final int pointerCount = motionEvent.getPointerCount();
            int i = 0;
            float n2 = 0.0f;
            float n3 = 0.0f;
            while (i < pointerCount) {
                if (actionIndex != i) {
                    n2 += motionEvent.getX(i);
                    n3 += motionEvent.getY(i);
                }
                ++i;
            }
            int n4;
            if (b2) {
                n4 = pointerCount - 1;
            }
            else {
                n4 = pointerCount;
            }
            final float n5 = (float)n4;
            final float n6 = n2 / n5;
            final float n7 = n3 / n5;
            boolean b3 = false;
            switch (n) {
                default: {
                    return b;
                }
                case 6: {
                    this.mLastFocusX = n6;
                    this.mDownFocusX = n6;
                    this.mLastFocusY = n7;
                    this.mDownFocusY = n7;
                    this.mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumFlingVelocity);
                    final int actionIndex2 = motionEvent.getActionIndex();
                    final int pointerId = motionEvent.getPointerId(actionIndex2);
                    final float xVelocity = this.mVelocityTracker.getXVelocity(pointerId);
                    final float yVelocity = this.mVelocityTracker.getYVelocity(pointerId);
                    int n8 = 0;
                    while (true) {
                        boolean onScroll = b;
                        if (n8 >= pointerCount) {
                            return onScroll;
                        }
                        if (n8 != actionIndex2) {
                            final int pointerId2 = motionEvent.getPointerId(n8);
                            if (this.mVelocityTracker.getXVelocity(pointerId2) * xVelocity + this.mVelocityTracker.getYVelocity(pointerId2) * yVelocity < 0.0f) {
                                this.mVelocityTracker.clear();
                                onScroll = b;
                                return onScroll;
                            }
                        }
                        ++n8;
                    }
                    break;
                }
                case 5: {
                    this.mLastFocusX = n6;
                    this.mDownFocusX = n6;
                    this.mLastFocusY = n7;
                    this.mDownFocusY = n7;
                    this.cancelTaps();
                    return b;
                }
                case 3: {
                    this.cancel();
                    return b;
                }
                case 2: {
                    if (this.mInLongPress) {
                        return b;
                    }
                    final float a = this.mLastFocusX - n6;
                    final float a2 = this.mLastFocusY - n7;
                    if (this.mIsDoubleTapping) {
                        return false | this.mDoubleTapListener.onDoubleTapEvent(motionEvent);
                    }
                    if (!this.mAlwaysInTapRegion) {
                        if (Math.abs(a) < 1.0f) {
                            final boolean onScroll = b;
                            if (Math.abs(a2) < 1.0f) {
                                return onScroll;
                            }
                        }
                        final boolean onScroll = this.mListener.onScroll(this.mCurrentDownEvent, motionEvent, a, a2);
                        this.mLastFocusX = n6;
                        this.mLastFocusY = n7;
                        return onScroll;
                    }
                    final int n9 = (int)(n6 - this.mDownFocusX);
                    final int n10 = (int)(n7 - this.mDownFocusY);
                    final int n11 = n9 * n9 + n10 * n10;
                    boolean onScroll2;
                    if (n11 > this.mTouchSlopSquare) {
                        onScroll2 = this.mListener.onScroll(this.mCurrentDownEvent, motionEvent, a, a2);
                        this.mLastFocusX = n6;
                        this.mLastFocusY = n7;
                        this.mAlwaysInTapRegion = false;
                        this.mHandler.removeMessages(3);
                        this.mHandler.removeMessages(1);
                        this.mHandler.removeMessages(2);
                    }
                    else {
                        onScroll2 = false;
                    }
                    b3 = onScroll2;
                    if (n11 > this.mTouchSlopSquare) {
                        this.mAlwaysInBiggerTapRegion = false;
                        b3 = onScroll2;
                        break;
                    }
                    break;
                }
                case 1: {
                    this.mStillDown = false;
                    final MotionEvent obtain = MotionEvent.obtain(motionEvent);
                    boolean b4 = false;
                    Label_0850: {
                        if (this.mIsDoubleTapping) {
                            b4 = (this.mDoubleTapListener.onDoubleTapEvent(motionEvent) | false);
                        }
                        else {
                            if (this.mInLongPress) {
                                this.mHandler.removeMessages(3);
                                this.mInLongPress = false;
                            }
                            else {
                                if (this.mAlwaysInTapRegion) {
                                    b4 = this.mListener.onSingleTapUp(motionEvent);
                                    if (this.mDeferConfirmSingleTap && this.mDoubleTapListener != null) {
                                        this.mDoubleTapListener.onSingleTapConfirmed(motionEvent);
                                    }
                                    break Label_0850;
                                }
                                final VelocityTracker mVelocityTracker = this.mVelocityTracker;
                                final int pointerId3 = motionEvent.getPointerId(0);
                                mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumFlingVelocity);
                                final float yVelocity2 = mVelocityTracker.getYVelocity(pointerId3);
                                final float xVelocity2 = mVelocityTracker.getXVelocity(pointerId3);
                                if (Math.abs(yVelocity2) > this.mMinimumFlingVelocity || Math.abs(xVelocity2) > this.mMinimumFlingVelocity) {
                                    b4 = this.mListener.onFling(this.mCurrentDownEvent, motionEvent, xVelocity2, yVelocity2);
                                    break Label_0850;
                                }
                            }
                            b4 = false;
                        }
                    }
                    if (this.mPreviousUpEvent != null) {
                        this.mPreviousUpEvent.recycle();
                    }
                    this.mPreviousUpEvent = obtain;
                    if (this.mVelocityTracker != null) {
                        this.mVelocityTracker.recycle();
                        this.mVelocityTracker = null;
                    }
                    this.mIsDoubleTapping = false;
                    this.mDeferConfirmSingleTap = false;
                    this.mHandler.removeMessages(1);
                    this.mHandler.removeMessages(2);
                    b3 = b4;
                    break;
                }
                case 0: {
                    boolean b5 = false;
                    Label_1043: {
                        if (this.mDoubleTapListener != null) {
                            final boolean hasMessages = this.mHandler.hasMessages(3);
                            if (hasMessages) {
                                this.mHandler.removeMessages(3);
                            }
                            if (this.mCurrentDownEvent != null && this.mPreviousUpEvent != null && hasMessages && this.isConsideredDoubleTap(this.mCurrentDownEvent, this.mPreviousUpEvent, motionEvent)) {
                                this.mIsDoubleTapping = true;
                                b5 = (this.mDoubleTapListener.onDoubleTap(this.mCurrentDownEvent) | false | this.mDoubleTapListener.onDoubleTapEvent(motionEvent));
                                break Label_1043;
                            }
                            this.mHandler.sendEmptyMessageDelayed(3, (long)GestureDetectorCompatImplBase.DOUBLE_TAP_TIMEOUT);
                        }
                        b5 = false;
                    }
                    this.mLastFocusX = n6;
                    this.mDownFocusX = n6;
                    this.mLastFocusY = n7;
                    this.mDownFocusY = n7;
                    if (this.mCurrentDownEvent != null) {
                        this.mCurrentDownEvent.recycle();
                    }
                    this.mCurrentDownEvent = MotionEvent.obtain(motionEvent);
                    this.mAlwaysInTapRegion = true;
                    this.mAlwaysInBiggerTapRegion = true;
                    this.mStillDown = true;
                    this.mInLongPress = false;
                    this.mDeferConfirmSingleTap = false;
                    if (this.mIsLongpressEnabled) {
                        this.mHandler.removeMessages(2);
                        this.mHandler.sendEmptyMessageAtTime(2, this.mCurrentDownEvent.getDownTime() + GestureDetectorCompatImplBase.TAP_TIMEOUT + GestureDetectorCompatImplBase.LONGPRESS_TIMEOUT);
                    }
                    this.mHandler.sendEmptyMessageAtTime(1, this.mCurrentDownEvent.getDownTime() + GestureDetectorCompatImplBase.TAP_TIMEOUT);
                    return b5 | this.mListener.onDown(motionEvent);
                }
            }
            return b3;
        }
        
        @Override
        public void setIsLongpressEnabled(final boolean mIsLongpressEnabled) {
            this.mIsLongpressEnabled = mIsLongpressEnabled;
        }
        
        @Override
        public void setOnDoubleTapListener(final GestureDetector$OnDoubleTapListener mDoubleTapListener) {
            this.mDoubleTapListener = mDoubleTapListener;
        }
        
        private class GestureHandler extends Handler
        {
            final GestureDetectorCompatImplBase this$0;
            
            GestureHandler(final GestureDetectorCompatImplBase this$0) {
                this.this$0 = this$0;
            }
            
            GestureHandler(final GestureDetectorCompatImplBase this$0, final Handler handler) {
                this.this$0 = this$0;
                super(handler.getLooper());
            }
            
            public void handleMessage(final Message obj) {
                switch (obj.what) {
                    default: {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Unknown message ");
                        sb.append(obj);
                        throw new RuntimeException(sb.toString());
                    }
                    case 3: {
                        if (this.this$0.mDoubleTapListener == null) {
                            break;
                        }
                        if (!this.this$0.mStillDown) {
                            this.this$0.mDoubleTapListener.onSingleTapConfirmed(this.this$0.mCurrentDownEvent);
                            break;
                        }
                        this.this$0.mDeferConfirmSingleTap = true;
                        break;
                    }
                    case 2: {
                        this.this$0.dispatchLongPress();
                        break;
                    }
                    case 1: {
                        this.this$0.mListener.onShowPress(this.this$0.mCurrentDownEvent);
                        break;
                    }
                }
            }
        }
    }
    
    static class GestureDetectorCompatImplJellybeanMr2 implements GestureDetectorCompatImpl
    {
        private final GestureDetector mDetector;
        
        GestureDetectorCompatImplJellybeanMr2(final Context context, final GestureDetector$OnGestureListener gestureDetector$OnGestureListener, final Handler handler) {
            this.mDetector = new GestureDetector(context, gestureDetector$OnGestureListener, handler);
        }
        
        @Override
        public boolean isLongpressEnabled() {
            return this.mDetector.isLongpressEnabled();
        }
        
        @Override
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            return this.mDetector.onTouchEvent(motionEvent);
        }
        
        @Override
        public void setIsLongpressEnabled(final boolean isLongpressEnabled) {
            this.mDetector.setIsLongpressEnabled(isLongpressEnabled);
        }
        
        @Override
        public void setOnDoubleTapListener(final GestureDetector$OnDoubleTapListener onDoubleTapListener) {
            this.mDetector.setOnDoubleTapListener(onDoubleTapListener);
        }
    }
}
