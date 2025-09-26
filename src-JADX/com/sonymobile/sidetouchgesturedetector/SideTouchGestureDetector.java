package com.sonymobile.sidetouchgesturedetector;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SideTouchGestureDetector {
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

    public interface DebugListener {
        void onConfigurationLoaded();
    }

    public interface OnDoubleTapListener {
        void onDoubleTap(int i, MotionEvent motionEvent);

        void onDoubleTapForLearning(DoubleTapInfo doubleTapInfo);

        void onSingleTapConfirmed(int i, MotionEvent motionEvent);
    }

    public interface OnGestureListener {
        void onCancel(MotionEvent motionEvent);

        void onDown(int i, MotionEvent motionEvent);

        void onFling(int i, MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2);

        void onGestureFinished(int i);

        void onLongPress(int i, MotionEvent motionEvent);

        void onScroll(int i, MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2);

        void onUp(int i, MotionEvent motionEvent);
    }

    private class GestureHandler extends Handler {
        GestureHandler() {
        }

        GestureHandler(Handler handler) {
            super(handler.getLooper());
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            ((Gesture) message.obj).onHandleMessage(message.what);
        }
    }

    private class DynamicSettings {
        private boolean mRegistered = false;
        private ContentObserver mContentObserver = new ContentObserver(new Handler()) { // from class: com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector.DynamicSettings.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                super.onChange(z);
                DynamicSettings.this.loadSettings();
            }
        };

        public DynamicSettings() {
            registerObserver();
            loadSettings();
        }

        public void registerObserver() {
            if (this.mRegistered) {
                return;
            }
            SideTouchGestureDetector.this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor(SideTouchGestureDetector.SETTINGS_KEY_DOUBLE_TAP_TIMEOUT), false, this.mContentObserver);
            this.mRegistered = true;
        }

        public void unregisterObserver() {
            if (this.mRegistered) {
                SideTouchGestureDetector.this.mContext.getContentResolver().unregisterContentObserver(this.mContentObserver);
                this.mRegistered = false;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void loadSettings() {
            SideTouchGestureDetector.this.setDoubleTapTimeout(Settings.System.getInt(SideTouchGestureDetector.this.mContext.getContentResolver(), SideTouchGestureDetector.SETTINGS_KEY_DOUBLE_TAP_TIMEOUT, SideTouchGestureDetector.DEFAULT_DOUBLE_TAP_TIMEOUT));
            if (SideTouchGestureDetector.this.mDebugListener != null) {
                SideTouchGestureDetector.this.mDebugListener.onConfigurationLoaded();
            }
        }
    }

    public SideTouchGestureDetector(Context context, OnGestureListener onGestureListener) {
        this(context, onGestureListener, null);
    }

    public SideTouchGestureDetector(Context context, OnGestureListener onGestureListener, Handler handler) {
        this.mLongPressTimeout = 500;
        this.mDoubleTapTimeout = DEFAULT_DOUBLE_TAP_TIMEOUT;
        this.mDoubleTapMinTime = 30;
        this.mGestures = new HashSet();
        this.mGesturesWaitingPointer = new HashSet();
        this.mGestureIds = 0;
        this.mDoubleTapInfo = new DoubleTapInfo();
        this.mDoubleTapTimeoutLearning = 1100;
        this.mContext = context;
        if (handler != null) {
            this.mHandler = new GestureHandler(handler);
        } else {
            this.mHandler = new GestureHandler();
        }
        this.mListener = onGestureListener;
        if (onGestureListener instanceof OnDoubleTapListener) {
            setOnDoubleTapListener((OnDoubleTapListener) onGestureListener);
        }
        init(context);
    }

    private void init(Context context) {
        int iDp2px;
        if (this.mListener == null) {
            throw new NullPointerException("OnGestureListener must not be null");
        }
        if (context == null) {
            throw new NullPointerException("Context must not be null");
        }
        this.mIsLongpressEnabled = true;
        this.mDynamicSettings = new DynamicSettings();
        int iDp2px2 = dp2px(14.0f);
        if (context.getResources().getConfiguration().isLayoutSizeAtLeast(4)) {
            iDp2px = dp2px(45.0f);
        } else {
            iDp2px = dp2px(30.0f);
        }
        this.mMinimumFlingVelocity = dp2px(50.0f);
        this.mMaximumFlingVelocity = dp2px(8000.0f);
        this.mTouchSlopSquare = iDp2px2 * iDp2px2;
        this.mDoubleTapSlopSquare = iDp2px * iDp2px;
    }

    public void setOnDoubleTapListener(OnDoubleTapListener onDoubleTapListener) {
        this.mDoubleTapListener = onDoubleTapListener;
    }

    public void setIsLongpressEnabled(boolean z) {
        this.mIsLongpressEnabled = z;
    }

    public boolean isLongpressEnabled() {
        return this.mIsLongpressEnabled;
    }

    public void registerSettingsObserver() {
        if (this.mDynamicSettings != null) {
            this.mDynamicSettings.registerObserver();
        }
    }

    public void unregisterSettingsObserver() {
        if (this.mDynamicSettings != null) {
            this.mDynamicSettings.unregisterObserver();
        }
    }

    public int getDoubleTapTimeout() {
        return this.mDoubleTapTimeout;
    }

    public void setDebugListener(DebugListener debugListener) {
        this.mDebugListener = debugListener;
    }

    public void setLongPressTimeout(int i) {
        this.mLongPressTimeout = i;
    }

    public void setDoubleTapTimeout(int i) {
        this.mDoubleTapTimeout = i;
    }

    public void setDoubleTapMinTime(int i) {
        this.mDoubleTapMinTime = i;
    }

    public void setTouchSlop(float f) {
        int iDp2px = dp2px(f);
        this.mTouchSlopSquare = iDp2px * iDp2px;
    }

    public void setDoubleTapSlop(float f) {
        int iDp2px = dp2px(f);
        this.mDoubleTapSlopSquare = iDp2px * iDp2px;
    }

    public void setMinimumFlingVelocity(float f) {
        this.mMinimumFlingVelocity = dp2px(f);
    }

    public void setMaximumFlingVelocity(float f) {
        this.mMaximumFlingVelocity = dp2px(f);
    }

    public void setLearningMode(boolean z) {
        this.mIsLearningMode = z;
        this.mDoubleTapTimeoutReal = getDoubleTapTimeout();
        setDoubleTapTimeout(this.mDoubleTapTimeoutLearning);
    }

    public void setDoubleTapTimeoutLearning(int i) {
        this.mDoubleTapTimeoutLearning = i;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public boolean onSideTouchEvent(MotionEvent motionEvent) {
        boolean z = false;
        if (!motionEvent.isFromSource(536870912)) {
            return false;
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        switch (motionEvent.getActionMasked()) {
            case 0:
            case 5:
                Iterator<Gesture> it = this.mGesturesWaitingPointer.iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (it.next().onDownEvent(motionEvent)) {
                            z = true;
                        }
                    }
                }
                if (!z) {
                    Iterator<Gesture> it2 = this.mGestures.iterator();
                    while (true) {
                        if (it2.hasNext()) {
                            if (it2.next().onDownEvent(motionEvent)) {
                                z = true;
                            }
                        }
                    }
                    if (!z) {
                        Gesture gesture = new Gesture();
                        this.mGestures.add(gesture);
                        gesture.onDownEvent(motionEvent);
                    }
                }
                return true;
            case 1:
            case 6:
                Iterator<Gesture> it3 = this.mGestures.iterator();
                while (it3.hasNext() && !it3.next().onUpEvent(motionEvent)) {
                }
                return true;
            case 2:
                Iterator<Gesture> it4 = this.mGestures.iterator();
                while (it4.hasNext()) {
                    it4.next().onMoveEvent(motionEvent);
                }
                return true;
            case 3:
                this.mHandler.removeMessages(1);
                this.mHandler.removeMessages(2);
                this.mVelocityTracker.recycle();
                this.mVelocityTracker = null;
                this.mListener.onCancel(motionEvent);
                this.mGestures.addAll(this.mGesturesWaitingPointer);
                this.mGesturesWaitingPointer.clear();
                Iterator<Gesture> it5 = this.mGestures.iterator();
                while (it5.hasNext()) {
                    it5.next().cancel();
                }
                return true;
            case 4:
            default:
                return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int acquireGestureId() {
        int iLowestOneBit = Integer.lowestOneBit(~this.mGestureIds);
        this.mGestureIds |= iLowestOneBit;
        return Integer.numberOfTrailingZeros(iLowestOneBit);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseGestureId(int i) {
        this.mGestureIds = (~(1 << i)) & this.mGestureIds;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void moveToWaitingList(Gesture gesture) {
        if (this.mGestures.remove(gesture)) {
            this.mGesturesWaitingPointer.add(gesture);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeFromWaitingList(Gesture gesture) {
        if (this.mGesturesWaitingPointer.remove(gesture)) {
            this.mGestures.add(gesture);
        }
    }

    private int dp2px(float f) {
        return (int) ((f * this.mContext.getResources().getDisplayMetrics().density) + 0.5f);
    }

    private class Gesture {
        final State doubleTapDownState;
        final State downState;
        final State initialState;
        final State longPressState;
        private MotionEvent mDownEvent;
        private int mGestureId;
        private State mState;
        final State scrollState;
        final State upState;

        private Gesture() {
            this.initialState = new Initial();
            this.downState = new Down();
            this.upState = new Up();
            this.longPressState = new LongPress();
            this.scrollState = new Scroll();
            this.doubleTapDownState = new DoubleTapDown();
            this.mGestureId = -1;
            this.mState = this.initialState;
        }

        boolean onDownEvent(MotionEvent motionEvent) {
            return this.mState.processDown(motionEvent);
        }

        void onMoveEvent(MotionEvent motionEvent) {
            this.mState.processMove(motionEvent);
        }

        boolean onUpEvent(MotionEvent motionEvent) {
            return this.mState.processUp(motionEvent);
        }

        void onHandleMessage(int i) {
            this.mState.processMessage(i);
        }

        void cancel() {
            this.mState.cancel();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setState(State state, MotionEvent motionEvent) {
            if (this.mState != state) {
                this.mState.exit();
                this.mState = state;
                state.enter(motionEvent);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void moveToWaitingList() {
            SideTouchGestureDetector.this.moveToWaitingList(this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void removeFromWaitingList() {
            SideTouchGestureDetector.this.removeFromWaitingList(this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void sendMessageDelayed(int i, long j) {
            removeMessages(i);
            SideTouchGestureDetector.this.mHandler.sendMessageDelayed(SideTouchGestureDetector.this.mHandler.obtainMessage(i, this), j);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void sendMessageAtTime(int i, long j) {
            removeMessages(i);
            SideTouchGestureDetector.this.mHandler.sendMessageAtTime(SideTouchGestureDetector.this.mHandler.obtainMessage(i, this), j);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void removeMessages(int i) {
            SideTouchGestureDetector.this.mHandler.removeMessages(i, this);
        }

        private class State {
            void enter(MotionEvent motionEvent) {
            }

            void exit() {
            }

            boolean processDown(MotionEvent motionEvent) {
                return false;
            }

            void processMessage(int i) {
            }

            void processMove(MotionEvent motionEvent) {
            }

            boolean processUp(MotionEvent motionEvent) {
                return false;
            }

            private State() {
            }

            final void cancel() {
                Gesture.this.setState(Gesture.this.initialState, null);
            }
        }

        private class Initial extends State {
            private Initial() {
                super();
            }

            @Override // com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector.Gesture.State
            void enter(MotionEvent motionEvent) {
                if (Gesture.this.mGestureId != -1) {
                    SideTouchGestureDetector.this.releaseGestureId(Gesture.this.mGestureId);
                    SideTouchGestureDetector.this.mListener.onGestureFinished(Gesture.this.mGestureId);
                    Gesture.this.mGestureId = -1;
                }
                if (Gesture.this.mDownEvent != null) {
                    Gesture.this.mDownEvent.recycle();
                    Gesture.this.mDownEvent = null;
                }
            }

            @Override // com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector.Gesture.State
            void exit() {
                if (Gesture.this.mGestureId == -1) {
                    Gesture.this.mGestureId = SideTouchGestureDetector.this.acquireGestureId();
                }
            }

            @Override // com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector.Gesture.State
            boolean processDown(MotionEvent motionEvent) {
                Gesture.this.setState(Gesture.this.downState, motionEvent);
                SideTouchGestureDetector.this.mListener.onDown(Gesture.this.mGestureId, motionEvent);
                return true;
            }
        }

        private class Down extends State {
            private boolean mDeferConfirmSingleTap;

            private Down() {
                super();
            }

            @Override // com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector.Gesture.State
            void enter(MotionEvent motionEvent) {
                Gesture.this.mDownEvent = MotionEvent.obtain(motionEvent);
                this.mDeferConfirmSingleTap = false;
                if (SideTouchGestureDetector.this.mDoubleTapListener != null) {
                    Gesture.this.sendMessageDelayed(2, SideTouchGestureDetector.this.mDoubleTapTimeout);
                }
                if (SideTouchGestureDetector.this.mIsLongpressEnabled) {
                    Gesture.this.sendMessageAtTime(1, motionEvent.getEventTime() + SideTouchGestureDetector.this.mLongPressTimeout);
                }
            }

            @Override // com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector.Gesture.State
            void processMove(MotionEvent motionEvent) {
                int actionIndex = Gesture.this.mDownEvent.getActionIndex();
                int iFindPointerIndex = motionEvent.findPointerIndex(Gesture.this.mDownEvent.getPointerId(actionIndex));
                int x = (int) (motionEvent.getX(iFindPointerIndex) - Gesture.this.mDownEvent.getX(actionIndex));
                int y = (int) (motionEvent.getY(iFindPointerIndex) - Gesture.this.mDownEvent.getY(actionIndex));
                if ((x * x) + (y * y) > SideTouchGestureDetector.this.mTouchSlopSquare) {
                    Gesture.this.removeMessages(2);
                    Gesture.this.removeMessages(1);
                    Gesture.this.setState(Gesture.this.scrollState, motionEvent);
                }
            }

            @Override // com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector.Gesture.State
            boolean processUp(MotionEvent motionEvent) {
                if (Gesture.this.mDownEvent.getPointerId(Gesture.this.mDownEvent.getActionIndex()) != motionEvent.getPointerId(motionEvent.getActionIndex())) {
                    return false;
                }
                SideTouchGestureDetector.this.mListener.onUp(Gesture.this.mGestureId, motionEvent);
                Gesture.this.removeMessages(1);
                if (SideTouchGestureDetector.this.mDoubleTapListener == null) {
                    Gesture.this.setState(Gesture.this.initialState, motionEvent);
                } else if (!this.mDeferConfirmSingleTap) {
                    Gesture.this.setState(Gesture.this.upState, motionEvent);
                } else {
                    SideTouchGestureDetector.this.mDoubleTapListener.onSingleTapConfirmed(Gesture.this.mGestureId, motionEvent);
                    Gesture.this.removeMessages(2);
                    Gesture.this.setState(Gesture.this.initialState, motionEvent);
                }
                return true;
            }

            @Override // com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector.Gesture.State
            void processMessage(int i) {
                switch (i) {
                    case 1:
                        SideTouchGestureDetector.this.mListener.onLongPress(Gesture.this.mGestureId, Gesture.this.mDownEvent);
                        Gesture.this.setState(Gesture.this.longPressState, null);
                        break;
                    case 2:
                        if (SideTouchGestureDetector.this.mDoubleTapListener != null) {
                            this.mDeferConfirmSingleTap = true;
                            break;
                        }
                        break;
                }
            }
        }

        private class Up extends State {
            private long mUpTime;

            private Up() {
                super();
            }

            @Override // com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector.Gesture.State
            void enter(MotionEvent motionEvent) {
                this.mUpTime = motionEvent.getEventTime();
                Gesture.this.moveToWaitingList();
            }

            @Override // com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector.Gesture.State
            void exit() {
                Gesture.this.removeFromWaitingList();
            }

            @Override // com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector.Gesture.State
            boolean processDown(MotionEvent motionEvent) {
                if (!isConsideredDoubleTap(motionEvent)) {
                    return false;
                }
                Gesture.this.removeMessages(2);
                Gesture.this.setState(Gesture.this.doubleTapDownState, motionEvent);
                SideTouchGestureDetector.this.mDoubleTapListener.onDoubleTap(Gesture.this.mGestureId, motionEvent);
                return true;
            }

            @Override // com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector.Gesture.State
            void processMessage(int i) {
                if (i != 2) {
                    return;
                }
                if (SideTouchGestureDetector.this.mDoubleTapListener != null) {
                    SideTouchGestureDetector.this.mDoubleTapListener.onSingleTapConfirmed(Gesture.this.mGestureId, Gesture.this.mDownEvent);
                }
                Gesture.this.setState(Gesture.this.initialState, null);
            }

            private boolean isConsideredDoubleTap(MotionEvent motionEvent) {
                long eventTime = motionEvent.getEventTime() - this.mUpTime;
                SideTouchGestureDetector.this.mDoubleTapInfo.isLearningMode(SideTouchGestureDetector.this.mIsLearningMode).firstDown(Gesture.this.mDownEvent).firstUpTime(this.mUpTime).secondDown(motionEvent).doubleTapMinTime(SideTouchGestureDetector.this.mDoubleTapMinTime).doubleTapTimeout(SideTouchGestureDetector.this.mDoubleTapTimeout).doubleTapTimeoutReal(SideTouchGestureDetector.this.mDoubleTapTimeoutReal);
                if (eventTime > SideTouchGestureDetector.this.mDoubleTapTimeout || eventTime < SideTouchGestureDetector.this.mDoubleTapMinTime) {
                    SideTouchGestureDetector.this.mDoubleTapListener.onDoubleTapForLearning(SideTouchGestureDetector.this.mDoubleTapInfo);
                    return false;
                }
                int x = ((int) Gesture.this.mDownEvent.getX(Gesture.this.mDownEvent.getActionIndex())) - ((int) motionEvent.getX(motionEvent.getActionIndex()));
                int y = ((int) Gesture.this.mDownEvent.getY(Gesture.this.mDownEvent.getActionIndex())) - ((int) motionEvent.getY(motionEvent.getActionIndex()));
                int i = SideTouchGestureDetector.this.mDoubleTapSlopSquare;
                boolean z = (x * x) + (y * y) < i;
                SideTouchGestureDetector.this.mDoubleTapInfo.doubleTapSlopSquare(i);
                SideTouchGestureDetector.this.mDoubleTapListener.onDoubleTapForLearning(SideTouchGestureDetector.this.mDoubleTapInfo);
                return z;
            }
        }

        private class Scroll extends State {
            private float mLastX;
            private float mLastY;

            private Scroll() {
                super();
            }

            @Override // com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector.Gesture.State
            void enter(MotionEvent motionEvent) {
                int actionIndex = Gesture.this.mDownEvent.getActionIndex();
                float x = Gesture.this.mDownEvent.getX(actionIndex);
                float y = Gesture.this.mDownEvent.getY(actionIndex);
                int iFindPointerIndex = motionEvent.findPointerIndex(Gesture.this.mDownEvent.getPointerId(actionIndex));
                this.mLastX = motionEvent.getX(iFindPointerIndex);
                this.mLastY = motionEvent.getY(iFindPointerIndex);
                SideTouchGestureDetector.this.mListener.onScroll(Gesture.this.mGestureId, Gesture.this.mDownEvent, motionEvent, x - this.mLastX, y - this.mLastY);
            }

            @Override // com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector.Gesture.State
            void processMove(MotionEvent motionEvent) {
                int iFindPointerIndex = motionEvent.findPointerIndex(Gesture.this.mDownEvent.getPointerId(Gesture.this.mDownEvent.getActionIndex()));
                SideTouchGestureDetector.this.mListener.onScroll(Gesture.this.mGestureId, Gesture.this.mDownEvent, motionEvent, this.mLastX - motionEvent.getX(iFindPointerIndex), this.mLastY - motionEvent.getY(iFindPointerIndex));
                this.mLastX = motionEvent.getX(iFindPointerIndex);
                this.mLastY = motionEvent.getY(iFindPointerIndex);
            }

            @Override // com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector.Gesture.State
            boolean processUp(MotionEvent motionEvent) {
                int pointerId = motionEvent.getPointerId(motionEvent.getActionIndex());
                if (Gesture.this.mDownEvent.getPointerId(Gesture.this.mDownEvent.getActionIndex()) != pointerId) {
                    return false;
                }
                SideTouchGestureDetector.this.mVelocityTracker.computeCurrentVelocity(1000, SideTouchGestureDetector.this.mMaximumFlingVelocity);
                float xVelocity = SideTouchGestureDetector.this.mVelocityTracker.getXVelocity(pointerId);
                float yVelocity = SideTouchGestureDetector.this.mVelocityTracker.getYVelocity(pointerId);
                if (Math.abs(xVelocity) > SideTouchGestureDetector.this.mMinimumFlingVelocity || Math.abs(yVelocity) > SideTouchGestureDetector.this.mMinimumFlingVelocity) {
                    SideTouchGestureDetector.this.mListener.onFling(Gesture.this.mGestureId, Gesture.this.mDownEvent, motionEvent, xVelocity, yVelocity);
                }
                SideTouchGestureDetector.this.mListener.onUp(Gesture.this.mGestureId, motionEvent);
                Gesture.this.setState(Gesture.this.initialState, motionEvent);
                return true;
            }
        }

        private class LongPress extends State {
            private LongPress() {
                super();
            }

            @Override // com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector.Gesture.State
            void processMove(MotionEvent motionEvent) {
                int actionIndex = Gesture.this.mDownEvent.getActionIndex();
                int iFindPointerIndex = motionEvent.findPointerIndex(Gesture.this.mDownEvent.getPointerId(actionIndex));
                int x = (int) (motionEvent.getX(iFindPointerIndex) - Gesture.this.mDownEvent.getX(actionIndex));
                int y = (int) (motionEvent.getY(iFindPointerIndex) - Gesture.this.mDownEvent.getY(actionIndex));
                if ((x * x) + (y * y) > SideTouchGestureDetector.this.mTouchSlopSquare) {
                    Gesture.this.setState(Gesture.this.scrollState, motionEvent);
                }
            }

            @Override // com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector.Gesture.State
            boolean processUp(MotionEvent motionEvent) {
                if (Gesture.this.mDownEvent.getPointerId(Gesture.this.mDownEvent.getActionIndex()) != motionEvent.getPointerId(motionEvent.getActionIndex())) {
                    return false;
                }
                SideTouchGestureDetector.this.mListener.onUp(Gesture.this.mGestureId, motionEvent);
                Gesture.this.setState(Gesture.this.initialState, motionEvent);
                return true;
            }
        }

        private class DoubleTapDown extends State {
            private DoubleTapDown() {
                super();
            }

            @Override // com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector.Gesture.State
            void enter(MotionEvent motionEvent) {
                Gesture.this.mDownEvent.recycle();
                Gesture.this.mDownEvent = MotionEvent.obtain(motionEvent);
                if (SideTouchGestureDetector.this.mIsLongpressEnabled) {
                    Gesture.this.sendMessageAtTime(1, motionEvent.getEventTime() + SideTouchGestureDetector.this.mLongPressTimeout);
                }
            }

            @Override // com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector.Gesture.State
            void processMove(MotionEvent motionEvent) {
                int actionIndex = Gesture.this.mDownEvent.getActionIndex();
                int iFindPointerIndex = motionEvent.findPointerIndex(Gesture.this.mDownEvent.getPointerId(actionIndex));
                int x = (int) (motionEvent.getX(iFindPointerIndex) - Gesture.this.mDownEvent.getX(actionIndex));
                int y = (int) (motionEvent.getY(iFindPointerIndex) - Gesture.this.mDownEvent.getY(actionIndex));
                if ((x * x) + (y * y) > SideTouchGestureDetector.this.mTouchSlopSquare) {
                    Gesture.this.removeMessages(1);
                    Gesture.this.setState(Gesture.this.scrollState, motionEvent);
                }
            }

            @Override // com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector.Gesture.State
            boolean processUp(MotionEvent motionEvent) {
                if (Gesture.this.mDownEvent.getPointerId(Gesture.this.mDownEvent.getActionIndex()) != motionEvent.getPointerId(motionEvent.getActionIndex())) {
                    return false;
                }
                SideTouchGestureDetector.this.mListener.onUp(Gesture.this.mGestureId, motionEvent);
                Gesture.this.removeMessages(1);
                Gesture.this.setState(Gesture.this.initialState, motionEvent);
                return true;
            }

            @Override // com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector.Gesture.State
            void processMessage(int i) {
                if (i != 1) {
                    return;
                }
                SideTouchGestureDetector.this.mListener.onLongPress(Gesture.this.mGestureId, Gesture.this.mDownEvent);
                Gesture.this.setState(Gesture.this.longPressState, null);
            }
        }
    }
}
