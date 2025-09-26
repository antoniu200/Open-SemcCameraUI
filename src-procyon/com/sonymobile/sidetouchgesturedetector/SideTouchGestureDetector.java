// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.sidetouchgesturedetector;

import android.os.Message;
import android.provider.Settings$System;
import android.database.ContentObserver;
import java.util.Iterator;
import java.util.Collection;
import android.view.MotionEvent;
import java.util.HashSet;
import android.view.VelocityTracker;
import android.os.Handler;
import java.util.Set;
import android.content.Context;

public class SideTouchGestureDetector
{
    private static final int DEFAULT_DOUBLE_TAP_TIMEOUT = 360;
    private static final int DOUBLE_TAP_MIN_TIME = 30;
    private static final int DOUBLE_TAP_SLOP = 30;
    public static final int INVALID_GESTURE_ID = -1;
    private static final int LONG_PRESS_TIMEOUT = 500;
    private static final int MAXIMUM_FLING_VELOCITY = 8000;
    private static final int MAXIMUM_FLING_VELOCITY_IN_DP = 8000;
    private static final int MINIMUM_FLING_VELOCITY = 50;
    private static final int MINIMUM_FLING_VELOCITY_IN_DP = 50;
    private static final int MSG_LONG_PRESS = 1;
    private static final int MSG_TAP = 2;
    private static final String SETTINGS_KEY_DOUBLE_TAP_TIMEOUT = "somc.side_sense_double_tap_timeout";
    private static final int SOURCE_SIDETOUCH = 536870912;
    private static final int TOUCH_SLOP = 8;
    private static final int TOUCH_SLOP_IN_DP = 14;
    private final Context mContext;
    private DebugListener mDebugListener;
    private DoubleTapInfo mDoubleTapInfo;
    private OnDoubleTapListener mDoubleTapListener;
    private int mDoubleTapMinTime;
    private int mDoubleTapSlopSquare;
    private int mDoubleTapTimeout;
    private int mDoubleTapTimeoutLearning;
    private int mDoubleTapTimeoutReal;
    private DynamicSettings mDynamicSettings;
    private int mGestureIds;
    private Set<Gesture> mGestures;
    private Set<Gesture> mGesturesWaitingPointer;
    private final Handler mHandler;
    private boolean mIsLearningMode;
    private boolean mIsLongpressEnabled;
    private OnGestureListener mListener;
    private int mLongPressTimeout;
    private int mMaximumFlingVelocity;
    private int mMinimumFlingVelocity;
    private int mTouchSlopSquare;
    private VelocityTracker mVelocityTracker;
    
    public SideTouchGestureDetector(final Context context, final OnGestureListener onGestureListener) {
        this(context, onGestureListener, null);
    }
    
    public SideTouchGestureDetector(final Context mContext, final OnGestureListener mListener, final Handler handler) {
        this.mLongPressTimeout = 500;
        this.mDoubleTapTimeout = 360;
        this.mDoubleTapMinTime = 30;
        this.mGestures = new HashSet<Gesture>();
        this.mGesturesWaitingPointer = new HashSet<Gesture>();
        this.mGestureIds = 0;
        this.mDoubleTapInfo = new DoubleTapInfo();
        this.mDoubleTapTimeoutLearning = 1100;
        this.mContext = mContext;
        if (handler != null) {
            this.mHandler = new GestureHandler(handler);
        }
        else {
            this.mHandler = new GestureHandler();
        }
        this.mListener = mListener;
        if (mListener instanceof OnDoubleTapListener) {
            this.setOnDoubleTapListener((OnDoubleTapListener)mListener);
        }
        this.init(mContext);
    }
    
    private int acquireGestureId() {
        final int lowestOneBit = Integer.lowestOneBit(~this.mGestureIds);
        this.mGestureIds |= lowestOneBit;
        return Integer.numberOfTrailingZeros(lowestOneBit);
    }
    
    private int dp2px(final float n) {
        return (int)(n * this.mContext.getResources().getDisplayMetrics().density + 0.5f);
    }
    
    private void init(final Context context) {
        if (this.mListener == null) {
            throw new NullPointerException("OnGestureListener must not be null");
        }
        if (context == null) {
            throw new NullPointerException("Context must not be null");
        }
        this.mIsLongpressEnabled = true;
        this.mDynamicSettings = new DynamicSettings();
        final int dp2px = this.dp2px(14.0f);
        int n;
        if (context.getResources().getConfiguration().isLayoutSizeAtLeast(4)) {
            n = this.dp2px(45.0f);
        }
        else {
            n = this.dp2px(30.0f);
        }
        this.mMinimumFlingVelocity = this.dp2px(50.0f);
        this.mMaximumFlingVelocity = this.dp2px(8000.0f);
        this.mTouchSlopSquare = dp2px * dp2px;
        this.mDoubleTapSlopSquare = n * n;
    }
    
    private void moveToWaitingList(final Gesture gesture) {
        if (this.mGestures.remove(gesture)) {
            this.mGesturesWaitingPointer.add(gesture);
        }
    }
    
    private void releaseGestureId(final int n) {
        this.mGestureIds &= ~(1 << n);
    }
    
    private void removeFromWaitingList(final Gesture gesture) {
        if (this.mGesturesWaitingPointer.remove(gesture)) {
            this.mGestures.add(gesture);
        }
    }
    
    public int getDoubleTapTimeout() {
        return this.mDoubleTapTimeout;
    }
    
    public boolean isLongpressEnabled() {
        return this.mIsLongpressEnabled;
    }
    
    public boolean onSideTouchEvent(final MotionEvent motionEvent) {
        final boolean fromSource = motionEvent.isFromSource(536870912);
        final int n = 0;
        if (!fromSource) {
            return false;
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        Label_0383: {
            switch (motionEvent.getActionMasked()) {
                case 3: {
                    this.mHandler.removeMessages(1);
                    this.mHandler.removeMessages(2);
                    this.mVelocityTracker.recycle();
                    this.mVelocityTracker = null;
                    this.mListener.onCancel(motionEvent);
                    this.mGestures.addAll(this.mGesturesWaitingPointer);
                    this.mGesturesWaitingPointer.clear();
                    final Iterator<Gesture> iterator = this.mGestures.iterator();
                    while (iterator.hasNext()) {
                        iterator.next().cancel();
                    }
                    break;
                }
                case 2: {
                    final Iterator<Gesture> iterator2 = this.mGestures.iterator();
                    while (iterator2.hasNext()) {
                        iterator2.next().onMoveEvent(motionEvent);
                    }
                    break;
                }
                case 1:
                case 6: {
                    final Iterator<Gesture> iterator3 = this.mGestures.iterator();
                    while (iterator3.hasNext()) {
                        if (iterator3.next().onUpEvent(motionEvent)) {
                            break;
                        }
                    }
                    break;
                }
                case 0:
                case 5: {
                    final Iterator<Gesture> iterator4 = this.mGesturesWaitingPointer.iterator();
                    while (true) {
                    Label_0349_Outer:
                        do {
                            final int n2 = n;
                            if (iterator4.hasNext()) {
                                continue;
                            }
                            if (n2 == 0) {
                                final Iterator<Gesture> iterator5 = this.mGestures.iterator();
                                while (true) {
                                    do {
                                        final int n3 = n2;
                                        if (iterator5.hasNext()) {
                                            continue Label_0349_Outer;
                                        }
                                        if (n3 == 0) {
                                            final Gesture gesture = new Gesture();
                                            this.mGestures.add(gesture);
                                            gesture.onDownEvent(motionEvent);
                                            break Label_0383;
                                        }
                                        break Label_0383;
                                    } while (!iterator5.next().onDownEvent(motionEvent));
                                    final int n3 = 1;
                                    continue;
                                }
                            }
                            break Label_0383;
                        } while (!iterator4.next().onDownEvent(motionEvent));
                        final int n2 = 1;
                        continue;
                    }
                }
            }
        }
        return true;
    }
    
    public void registerSettingsObserver() {
        if (this.mDynamicSettings != null) {
            this.mDynamicSettings.registerObserver();
        }
    }
    
    public void setDebugListener(final DebugListener mDebugListener) {
        this.mDebugListener = mDebugListener;
    }
    
    public void setDoubleTapMinTime(final int mDoubleTapMinTime) {
        this.mDoubleTapMinTime = mDoubleTapMinTime;
    }
    
    public void setDoubleTapSlop(final float n) {
        final int dp2px = this.dp2px(n);
        this.mDoubleTapSlopSquare = dp2px * dp2px;
    }
    
    public void setDoubleTapTimeout(final int mDoubleTapTimeout) {
        this.mDoubleTapTimeout = mDoubleTapTimeout;
    }
    
    public void setDoubleTapTimeoutLearning(final int mDoubleTapTimeoutLearning) {
        this.mDoubleTapTimeoutLearning = mDoubleTapTimeoutLearning;
    }
    
    public void setIsLongpressEnabled(final boolean mIsLongpressEnabled) {
        this.mIsLongpressEnabled = mIsLongpressEnabled;
    }
    
    public void setLearningMode(final boolean mIsLearningMode) {
        this.mIsLearningMode = mIsLearningMode;
        this.mDoubleTapTimeoutReal = this.getDoubleTapTimeout();
        this.setDoubleTapTimeout(this.mDoubleTapTimeoutLearning);
    }
    
    public void setLongPressTimeout(final int mLongPressTimeout) {
        this.mLongPressTimeout = mLongPressTimeout;
    }
    
    public void setMaximumFlingVelocity(final float n) {
        this.mMaximumFlingVelocity = this.dp2px(n);
    }
    
    public void setMinimumFlingVelocity(final float n) {
        this.mMinimumFlingVelocity = this.dp2px(n);
    }
    
    public void setOnDoubleTapListener(final OnDoubleTapListener mDoubleTapListener) {
        this.mDoubleTapListener = mDoubleTapListener;
    }
    
    public void setTouchSlop(final float n) {
        final int dp2px = this.dp2px(n);
        this.mTouchSlopSquare = dp2px * dp2px;
    }
    
    public void unregisterSettingsObserver() {
        if (this.mDynamicSettings != null) {
            this.mDynamicSettings.unregisterObserver();
        }
    }
    
    public interface DebugListener
    {
        void onConfigurationLoaded();
    }
    
    private class DynamicSettings
    {
        private ContentObserver mContentObserver;
        private boolean mRegistered;
        final SideTouchGestureDetector this$0;
        
        public DynamicSettings(final SideTouchGestureDetector this$0) {
            this.this$0 = this$0;
            this.mRegistered = false;
            this.mContentObserver = new ContentObserver(new Handler()) {
                final DynamicSettings this$1;
                
                public void onChange(final boolean b) {
                    super.onChange(b);
                    this.this$1.loadSettings();
                }
            };
            this.registerObserver();
            this.loadSettings();
        }
        
        private void loadSettings() {
            this.this$0.setDoubleTapTimeout(Settings$System.getInt(this.this$0.mContext.getContentResolver(), "somc.side_sense_double_tap_timeout", 360));
            if (this.this$0.mDebugListener != null) {
                this.this$0.mDebugListener.onConfigurationLoaded();
            }
        }
        
        public void registerObserver() {
            if (!this.mRegistered) {
                this.this$0.mContext.getContentResolver().registerContentObserver(Settings$System.getUriFor("somc.side_sense_double_tap_timeout"), false, this.mContentObserver);
                this.mRegistered = true;
            }
        }
        
        public void unregisterObserver() {
            if (this.mRegistered) {
                this.this$0.mContext.getContentResolver().unregisterContentObserver(this.mContentObserver);
                this.mRegistered = false;
            }
        }
    }
    
    private class Gesture
    {
        final State doubleTapDownState;
        final State downState;
        final State initialState;
        final State longPressState;
        private MotionEvent mDownEvent;
        private int mGestureId;
        private State mState;
        final State scrollState;
        final SideTouchGestureDetector this$0;
        final State upState;
        
        private Gesture(final SideTouchGestureDetector this$0) {
            this.this$0 = this$0;
            this.initialState = (State)new Initial();
            this.downState = (State)new Down();
            this.upState = (State)new Up();
            this.longPressState = (State)new LongPress();
            this.scrollState = (State)new Scroll();
            this.doubleTapDownState = (State)new DoubleTapDown();
            this.mGestureId = -1;
            this.mState = this.initialState;
        }
        
        private void moveToWaitingList() {
            this.this$0.moveToWaitingList(this);
        }
        
        private void removeFromWaitingList() {
            this.this$0.removeFromWaitingList(this);
        }
        
        private void removeMessages(final int n) {
            this.this$0.mHandler.removeMessages(n, (Object)this);
        }
        
        private void sendMessageAtTime(final int n, final long n2) {
            this.removeMessages(n);
            this.this$0.mHandler.sendMessageAtTime(this.this$0.mHandler.obtainMessage(n, (Object)this), n2);
        }
        
        private void sendMessageDelayed(final int n, final long n2) {
            this.removeMessages(n);
            this.this$0.mHandler.sendMessageDelayed(this.this$0.mHandler.obtainMessage(n, (Object)this), n2);
        }
        
        private void setState(final State mState, final MotionEvent motionEvent) {
            if (this.mState != mState) {
                this.mState.exit();
                (this.mState = mState).enter(motionEvent);
            }
        }
        
        void cancel() {
            this.mState.cancel();
        }
        
        boolean onDownEvent(final MotionEvent motionEvent) {
            return this.mState.processDown(motionEvent);
        }
        
        void onHandleMessage(final int n) {
            this.mState.processMessage(n);
        }
        
        void onMoveEvent(final MotionEvent motionEvent) {
            this.mState.processMove(motionEvent);
        }
        
        boolean onUpEvent(final MotionEvent motionEvent) {
            return this.mState.processUp(motionEvent);
        }
        
        private class DoubleTapDown extends State
        {
            final Gesture this$1;
            
            private DoubleTapDown(final Gesture this$1) {
            }
            
            @Override
            void enter(final MotionEvent motionEvent) {
                this.this$1.mDownEvent.recycle();
                this.this$1.mDownEvent = MotionEvent.obtain(motionEvent);
                if (this.this$1.this$0.mIsLongpressEnabled) {
                    this.this$1.sendMessageAtTime(1, motionEvent.getEventTime() + this.this$1.this$0.mLongPressTimeout);
                }
            }
            
            @Override
            void processMessage(final int n) {
                if (n == 1) {
                    this.this$1.this$0.mListener.onLongPress(this.this$1.mGestureId, this.this$1.mDownEvent);
                    this.this$1.setState(this.this$1.longPressState, null);
                }
            }
            
            @Override
            void processMove(final MotionEvent motionEvent) {
                final int actionIndex = this.this$1.mDownEvent.getActionIndex();
                final int pointerIndex = motionEvent.findPointerIndex(this.this$1.mDownEvent.getPointerId(actionIndex));
                final int n = (int)(motionEvent.getX(pointerIndex) - this.this$1.mDownEvent.getX(actionIndex));
                final int n2 = (int)(motionEvent.getY(pointerIndex) - this.this$1.mDownEvent.getY(actionIndex));
                if (n * n + n2 * n2 > this.this$1.this$0.mTouchSlopSquare) {
                    this.this$1.removeMessages(1);
                    this.this$1.setState(this.this$1.scrollState, motionEvent);
                }
            }
            
            @Override
            boolean processUp(final MotionEvent motionEvent) {
                if (this.this$1.mDownEvent.getPointerId(this.this$1.mDownEvent.getActionIndex()) != motionEvent.getPointerId(motionEvent.getActionIndex())) {
                    return false;
                }
                this.this$1.this$0.mListener.onUp(this.this$1.mGestureId, motionEvent);
                this.this$1.removeMessages(1);
                this.this$1.setState(this.this$1.initialState, motionEvent);
                return true;
            }
        }
        
        private class State
        {
            final Gesture this$1;
            
            private State(final Gesture this$1) {
                this.this$1 = this$1;
            }
            
            final void cancel() {
                this.this$1.setState(this.this$1.initialState, null);
            }
            
            void enter(final MotionEvent motionEvent) {
            }
            
            void exit() {
            }
            
            boolean processDown(final MotionEvent motionEvent) {
                return false;
            }
            
            void processMessage(final int n) {
            }
            
            void processMove(final MotionEvent motionEvent) {
            }
            
            boolean processUp(final MotionEvent motionEvent) {
                return false;
            }
        }
        
        private class Down extends State
        {
            private boolean mDeferConfirmSingleTap;
            final Gesture this$1;
            
            private Down(final Gesture this$1) {
            }
            
            @Override
            void enter(final MotionEvent motionEvent) {
                this.this$1.mDownEvent = MotionEvent.obtain(motionEvent);
                this.mDeferConfirmSingleTap = false;
                if (this.this$1.this$0.mDoubleTapListener != null) {
                    this.this$1.sendMessageDelayed(2, this.this$1.this$0.mDoubleTapTimeout);
                }
                if (this.this$1.this$0.mIsLongpressEnabled) {
                    this.this$1.sendMessageAtTime(1, motionEvent.getEventTime() + this.this$1.this$0.mLongPressTimeout);
                }
            }
            
            @Override
            void processMessage(final int n) {
                switch (n) {
                    case 2: {
                        if (this.this$1.this$0.mDoubleTapListener != null) {
                            this.mDeferConfirmSingleTap = true;
                            break;
                        }
                        break;
                    }
                    case 1: {
                        this.this$1.this$0.mListener.onLongPress(this.this$1.mGestureId, this.this$1.mDownEvent);
                        this.this$1.setState(this.this$1.longPressState, null);
                        break;
                    }
                }
            }
            
            @Override
            void processMove(final MotionEvent motionEvent) {
                final int actionIndex = this.this$1.mDownEvent.getActionIndex();
                final int pointerIndex = motionEvent.findPointerIndex(this.this$1.mDownEvent.getPointerId(actionIndex));
                final int n = (int)(motionEvent.getX(pointerIndex) - this.this$1.mDownEvent.getX(actionIndex));
                final int n2 = (int)(motionEvent.getY(pointerIndex) - this.this$1.mDownEvent.getY(actionIndex));
                if (n * n + n2 * n2 > this.this$1.this$0.mTouchSlopSquare) {
                    this.this$1.removeMessages(2);
                    this.this$1.removeMessages(1);
                    this.this$1.setState(this.this$1.scrollState, motionEvent);
                }
            }
            
            @Override
            boolean processUp(final MotionEvent motionEvent) {
                if (this.this$1.mDownEvent.getPointerId(this.this$1.mDownEvent.getActionIndex()) != motionEvent.getPointerId(motionEvent.getActionIndex())) {
                    return false;
                }
                this.this$1.this$0.mListener.onUp(this.this$1.mGestureId, motionEvent);
                this.this$1.removeMessages(1);
                if (this.this$1.this$0.mDoubleTapListener != null) {
                    if (!this.mDeferConfirmSingleTap) {
                        this.this$1.setState(this.this$1.upState, motionEvent);
                    }
                    else {
                        this.this$1.this$0.mDoubleTapListener.onSingleTapConfirmed(this.this$1.mGestureId, motionEvent);
                        this.this$1.removeMessages(2);
                        this.this$1.setState(this.this$1.initialState, motionEvent);
                    }
                }
                else {
                    this.this$1.setState(this.this$1.initialState, motionEvent);
                }
                return true;
            }
        }
        
        private class Initial extends State
        {
            final Gesture this$1;
            
            private Initial(final Gesture this$1) {
            }
            
            @Override
            void enter(final MotionEvent motionEvent) {
                if (this.this$1.mGestureId != -1) {
                    this.this$1.this$0.releaseGestureId(this.this$1.mGestureId);
                    this.this$1.this$0.mListener.onGestureFinished(this.this$1.mGestureId);
                    this.this$1.mGestureId = -1;
                }
                if (this.this$1.mDownEvent != null) {
                    this.this$1.mDownEvent.recycle();
                    this.this$1.mDownEvent = null;
                }
            }
            
            @Override
            void exit() {
                if (this.this$1.mGestureId == -1) {
                    this.this$1.mGestureId = this.this$1.this$0.acquireGestureId();
                }
            }
            
            @Override
            boolean processDown(final MotionEvent motionEvent) {
                this.this$1.setState(this.this$1.downState, motionEvent);
                this.this$1.this$0.mListener.onDown(this.this$1.mGestureId, motionEvent);
                return true;
            }
        }
        
        private class LongPress extends State
        {
            final Gesture this$1;
            
            private LongPress(final Gesture this$1) {
            }
            
            @Override
            void processMove(final MotionEvent motionEvent) {
                final int actionIndex = this.this$1.mDownEvent.getActionIndex();
                final int pointerIndex = motionEvent.findPointerIndex(this.this$1.mDownEvent.getPointerId(actionIndex));
                final int n = (int)(motionEvent.getX(pointerIndex) - this.this$1.mDownEvent.getX(actionIndex));
                final int n2 = (int)(motionEvent.getY(pointerIndex) - this.this$1.mDownEvent.getY(actionIndex));
                if (n * n + n2 * n2 > this.this$1.this$0.mTouchSlopSquare) {
                    this.this$1.setState(this.this$1.scrollState, motionEvent);
                }
            }
            
            @Override
            boolean processUp(final MotionEvent motionEvent) {
                if (this.this$1.mDownEvent.getPointerId(this.this$1.mDownEvent.getActionIndex()) != motionEvent.getPointerId(motionEvent.getActionIndex())) {
                    return false;
                }
                this.this$1.this$0.mListener.onUp(this.this$1.mGestureId, motionEvent);
                this.this$1.setState(this.this$1.initialState, motionEvent);
                return true;
            }
        }
        
        private class Scroll extends State
        {
            private float mLastX;
            private float mLastY;
            final Gesture this$1;
            
            private Scroll(final Gesture this$1) {
            }
            
            @Override
            void enter(final MotionEvent motionEvent) {
                final int actionIndex = this.this$1.mDownEvent.getActionIndex();
                final float x = this.this$1.mDownEvent.getX(actionIndex);
                final float y = this.this$1.mDownEvent.getY(actionIndex);
                final int pointerIndex = motionEvent.findPointerIndex(this.this$1.mDownEvent.getPointerId(actionIndex));
                this.mLastX = motionEvent.getX(pointerIndex);
                this.mLastY = motionEvent.getY(pointerIndex);
                this.this$1.this$0.mListener.onScroll(this.this$1.mGestureId, this.this$1.mDownEvent, motionEvent, x - this.mLastX, y - this.mLastY);
            }
            
            @Override
            void processMove(final MotionEvent motionEvent) {
                final int pointerIndex = motionEvent.findPointerIndex(this.this$1.mDownEvent.getPointerId(this.this$1.mDownEvent.getActionIndex()));
                this.this$1.this$0.mListener.onScroll(this.this$1.mGestureId, this.this$1.mDownEvent, motionEvent, this.mLastX - motionEvent.getX(pointerIndex), this.mLastY - motionEvent.getY(pointerIndex));
                this.mLastX = motionEvent.getX(pointerIndex);
                this.mLastY = motionEvent.getY(pointerIndex);
            }
            
            @Override
            boolean processUp(final MotionEvent motionEvent) {
                final int pointerId = motionEvent.getPointerId(motionEvent.getActionIndex());
                if (this.this$1.mDownEvent.getPointerId(this.this$1.mDownEvent.getActionIndex()) != pointerId) {
                    return false;
                }
                this.this$1.this$0.mVelocityTracker.computeCurrentVelocity(1000, (float)this.this$1.this$0.mMaximumFlingVelocity);
                final float xVelocity = this.this$1.this$0.mVelocityTracker.getXVelocity(pointerId);
                final float yVelocity = this.this$1.this$0.mVelocityTracker.getYVelocity(pointerId);
                if (Math.abs(xVelocity) > this.this$1.this$0.mMinimumFlingVelocity || Math.abs(yVelocity) > this.this$1.this$0.mMinimumFlingVelocity) {
                    this.this$1.this$0.mListener.onFling(this.this$1.mGestureId, this.this$1.mDownEvent, motionEvent, xVelocity, yVelocity);
                }
                this.this$1.this$0.mListener.onUp(this.this$1.mGestureId, motionEvent);
                this.this$1.setState(this.this$1.initialState, motionEvent);
                return true;
            }
        }
        
        private class Up extends State
        {
            private long mUpTime;
            final Gesture this$1;
            
            private Up(final Gesture this$1) {
            }
            
            private boolean isConsideredDoubleTap(final MotionEvent motionEvent) {
                final long n = motionEvent.getEventTime() - this.mUpTime;
                this.this$1.this$0.mDoubleTapInfo.isLearningMode(this.this$1.this$0.mIsLearningMode).firstDown(this.this$1.mDownEvent).firstUpTime(this.mUpTime).secondDown(motionEvent).doubleTapMinTime(this.this$1.this$0.mDoubleTapMinTime).doubleTapTimeout(this.this$1.this$0.mDoubleTapTimeout).doubleTapTimeoutReal(this.this$1.this$0.mDoubleTapTimeoutReal);
                final long n2 = this.this$1.this$0.mDoubleTapTimeout;
                boolean b = false;
                if (n <= n2 && n >= this.this$1.this$0.mDoubleTapMinTime) {
                    final int n3 = (int)this.this$1.mDownEvent.getX(this.this$1.mDownEvent.getActionIndex()) - (int)motionEvent.getX(motionEvent.getActionIndex());
                    final int n4 = (int)this.this$1.mDownEvent.getY(this.this$1.mDownEvent.getActionIndex()) - (int)motionEvent.getY(motionEvent.getActionIndex());
                    final int access$3400 = this.this$1.this$0.mDoubleTapSlopSquare;
                    if (n3 * n3 + n4 * n4 < access$3400) {
                        b = true;
                    }
                    this.this$1.this$0.mDoubleTapInfo.doubleTapSlopSquare(access$3400);
                    this.this$1.this$0.mDoubleTapListener.onDoubleTapForLearning(this.this$1.this$0.mDoubleTapInfo);
                    return b;
                }
                this.this$1.this$0.mDoubleTapListener.onDoubleTapForLearning(this.this$1.this$0.mDoubleTapInfo);
                return false;
            }
            
            @Override
            void enter(final MotionEvent motionEvent) {
                this.mUpTime = motionEvent.getEventTime();
                this.this$1.moveToWaitingList();
            }
            
            @Override
            void exit() {
                this.this$1.removeFromWaitingList();
            }
            
            @Override
            boolean processDown(final MotionEvent motionEvent) {
                if (!this.isConsideredDoubleTap(motionEvent)) {
                    return false;
                }
                this.this$1.removeMessages(2);
                this.this$1.setState(this.this$1.doubleTapDownState, motionEvent);
                this.this$1.this$0.mDoubleTapListener.onDoubleTap(this.this$1.mGestureId, motionEvent);
                return true;
            }
            
            @Override
            void processMessage(final int n) {
                if (n == 2) {
                    if (this.this$1.this$0.mDoubleTapListener != null) {
                        this.this$1.this$0.mDoubleTapListener.onSingleTapConfirmed(this.this$1.mGestureId, this.this$1.mDownEvent);
                    }
                    this.this$1.setState(this.this$1.initialState, null);
                }
            }
        }
    }
    
    private class GestureHandler extends Handler
    {
        final SideTouchGestureDetector this$0;
        
        GestureHandler(final SideTouchGestureDetector this$0) {
            this.this$0 = this$0;
        }
        
        GestureHandler(final SideTouchGestureDetector this$0, final Handler handler) {
            this.this$0 = this$0;
            super(handler.getLooper());
        }
        
        public void handleMessage(final Message message) {
            ((Gesture)message.obj).onHandleMessage(message.what);
        }
    }
    
    public interface OnDoubleTapListener
    {
        void onDoubleTap(final int p0, final MotionEvent p1);
        
        void onDoubleTapForLearning(final DoubleTapInfo p0);
        
        void onSingleTapConfirmed(final int p0, final MotionEvent p1);
    }
    
    public interface OnGestureListener
    {
        void onCancel(final MotionEvent p0);
        
        void onDown(final int p0, final MotionEvent p1);
        
        void onFling(final int p0, final MotionEvent p1, final MotionEvent p2, final float p3, final float p4);
        
        void onGestureFinished(final int p0);
        
        void onLongPress(final int p0, final MotionEvent p1);
        
        void onScroll(final int p0, final MotionEvent p1, final MotionEvent p2, final float p3, final float p4);
        
        void onUp(final int p0, final MotionEvent p1);
    }
}
