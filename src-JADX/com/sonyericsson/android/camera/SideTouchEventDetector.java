package com.sonyericsson.android.camera;

import android.content.Context;
import android.view.MotionEvent;
import com.sonymobile.sidetouchgesturedetector.DoubleTapInfo;
import com.sonymobile.sidetouchgesturedetector.DynamicAreaFilter;
import com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector;
import com.sonymobile.sidetouchgesturedetector.SideTouchUtils;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SideTouchEventDetector {
    private static final boolean mIsZoomEnabled = false;
    private final DynamicAreaFilter mDynamicAreaFilter;
    private final OnSideTouchGestureListener mOnSideTouchGestureListener;
    private int mOrientation;
    private final SideTouchGestureDetector mSideTouchGestureDetector;

    public interface OnSideTouchGestureListener {
        void onDoubleTap(SideTouchEvent sideTouchEvent, int i, int i2);

        void onGestureFinished();

        void onGestureStart();

        void onScroll(SideTouchEvent sideTouchEvent);

        void onScrollEnd();

        void onScrollStart(SideTouchEvent sideTouchEvent);
    }

    private enum State {
        IDLING,
        SCROLLING,
        DOUBLE_TAPPING
    }

    public enum SideTouchArea {
        UNKNOWN,
        TOP,
        BOTTOM,
        LEFT,
        RIGHT;

        /* JADX INFO: Access modifiers changed from: private */
        public static SideTouchArea fromMotionEvent(MotionEvent motionEvent, int i) {
            return fromCode(SideTouchUtils.getLogicalScreenSide(motionEvent, i));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static SideTouchArea fromCode(int i) {
            if (i == 4) {
                return TOP;
            }
            if (i == 8) {
                return BOTTOM;
            }
            switch (i) {
                case 1:
                    return LEFT;
                case 2:
                    return RIGHT;
                default:
                    return UNKNOWN;
            }
        }
    }

    public static class SideTouchEvent {
        public final SideTouchArea area;
        public final int position;

        private SideTouchEvent(int i, SideTouchArea sideTouchArea) {
            this.position = i;
            this.area = sideTouchArea;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static SideTouchEvent make(MotionEvent motionEvent, int i) {
            return make(SideTouchArea.fromMotionEvent(motionEvent, i), (int) motionEvent.getX(i), (int) motionEvent.getY(i));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static SideTouchEvent make(SideTouchArea sideTouchArea, int i, int i2) {
            switch (sideTouchArea) {
                case TOP:
                case BOTTOM:
                    return new SideTouchEvent(i, sideTouchArea);
                case LEFT:
                case RIGHT:
                    return new SideTouchEvent(i2, sideTouchArea);
                default:
                    return new SideTouchEvent(0, sideTouchArea);
            }
        }

        public String toString() {
            return "position:" + this.position + ", area:" + this.area.name();
        }
    }

    public SideTouchEventDetector(Context context, OnSideTouchGestureListener onSideTouchGestureListener) {
        this.mDynamicAreaFilter = new DynamicAreaFilter(context);
        this.mSideTouchGestureDetector = new SideTouchGestureDetector(context, new CameraSideTouchGestureListener());
        this.mOnSideTouchGestureListener = onSideTouchGestureListener;
    }

    public boolean onSideTouchEvent(MotionEvent motionEvent, int i) {
        this.mOrientation = i;
        this.mDynamicAreaFilter.onSideTouchEvent(motionEvent);
        return this.mSideTouchGestureDetector.onSideTouchEvent(motionEvent);
    }

    public void unregister() {
        this.mSideTouchGestureDetector.unregisterSettingsObserver();
        this.mDynamicAreaFilter.unregisterSettingsObserver();
    }

    private class CameraSideTouchGestureListener implements SideTouchGestureDetector.OnGestureListener, SideTouchGestureDetector.OnDoubleTapListener {
        private int mCurrentGestureId;
        private int mCurrentSide;
        private State mCurrentState;

        @Override // com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector.OnGestureListener
        public void onCancel(MotionEvent motionEvent) {
        }

        @Override // com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector.OnDoubleTapListener
        public void onDoubleTapForLearning(DoubleTapInfo doubleTapInfo) {
        }

        @Override // com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector.OnGestureListener
        public void onDown(int i, MotionEvent motionEvent) {
        }

        @Override // com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector.OnDoubleTapListener
        public void onSingleTapConfirmed(int i, MotionEvent motionEvent) {
        }

        private CameraSideTouchGestureListener() {
            this.mCurrentGestureId = -1;
            this.mCurrentSide = 0;
            this.mCurrentState = State.IDLING;
        }

        @Override // com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector.OnGestureListener
        public void onUp(int i, MotionEvent motionEvent) {
            if (this.mCurrentGestureId == i && this.mCurrentState == State.DOUBLE_TAPPING) {
                SideTouchEventDetector.this.mOnSideTouchGestureListener.onDoubleTap(SideTouchEvent.make(motionEvent, motionEvent.getActionIndex()), (int) motionEvent.getX(), (int) motionEvent.getY());
                if (finishGestureTracking(i)) {
                    this.mCurrentState = State.IDLING;
                }
            }
        }

        @Override // com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector.OnGestureListener
        public void onLongPress(int i, MotionEvent motionEvent) {
            if (this.mCurrentState == State.DOUBLE_TAPPING && finishGestureTracking(i)) {
                this.mCurrentState = State.IDLING;
            }
        }

        @Override // com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector.OnDoubleTapListener
        public void onDoubleTap(int i, MotionEvent motionEvent) {
            if (this.mCurrentState == State.IDLING && startGestureTracking(i, motionEvent)) {
                this.mCurrentState = State.DOUBLE_TAPPING;
            }
        }

        @Override // com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector.OnGestureListener
        public void onScroll(int i, MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            switch (SideTouchUtils.getLogicalScreenSide(motionEvent)) {
                case 1:
                case 2:
                    int iFindPointerIndex = motionEvent2.findPointerIndex(motionEvent.getPointerId(motionEvent.getActionIndex()));
                    switch (this.mCurrentState) {
                        case SCROLLING:
                            if (this.mCurrentGestureId == i) {
                                SideTouchEventDetector.this.mOnSideTouchGestureListener.onScroll(SideTouchEvent.make(SideTouchArea.fromCode(this.mCurrentSide), (int) motionEvent2.getX(iFindPointerIndex), (int) motionEvent2.getY(iFindPointerIndex)));
                                break;
                            }
                            break;
                        case DOUBLE_TAPPING:
                            if (finishGestureTracking(i)) {
                                this.mCurrentState = State.IDLING;
                                break;
                            }
                            break;
                    }
            }
        }

        @Override // com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector.OnGestureListener
        public void onFling(int i, MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (this.mCurrentState == State.DOUBLE_TAPPING && finishGestureTracking(i)) {
                this.mCurrentState = State.IDLING;
            }
        }

        @Override // com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector.OnGestureListener
        public void onGestureFinished(int i) {
            if (this.mCurrentState == State.IDLING || !finishGestureTracking(i)) {
                return;
            }
            this.mCurrentState = State.IDLING;
        }

        private boolean startGestureTracking(int i, MotionEvent motionEvent) {
            if (this.mCurrentGestureId != -1 || !SideTouchEventDetector.this.isInSideTouchValidArea(motionEvent)) {
                return false;
            }
            this.mCurrentGestureId = i;
            this.mCurrentSide = SideTouchUtils.getLogicalScreenSide(motionEvent);
            SideTouchEventDetector.this.mOnSideTouchGestureListener.onGestureStart();
            return true;
        }

        private boolean finishGestureTracking(int i) {
            if (this.mCurrentGestureId != i) {
                return false;
            }
            this.mCurrentGestureId = -1;
            this.mCurrentSide = 0;
            if (this.mCurrentState == State.SCROLLING) {
                SideTouchEventDetector.this.mOnSideTouchGestureListener.onScrollEnd();
            }
            SideTouchEventDetector.this.mOnSideTouchGestureListener.onGestureFinished();
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isInSideTouchValidArea(MotionEvent motionEvent) {
        if (this.mOrientation == 1) {
            if (motionEvent.getY(motionEvent.getActionIndex()) > this.mDynamicAreaFilter.getValidScreenHeight(motionEvent)) {
                return false;
            }
        }
        return true;
    }
}
