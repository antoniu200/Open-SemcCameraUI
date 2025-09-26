// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera;

import com.sonymobile.sidetouchgesturedetector.DoubleTapInfo;
import com.sonymobile.sidetouchgesturedetector.SideTouchUtils;
import android.view.MotionEvent;
import android.content.Context;
import com.sonymobile.sidetouchgesturedetector.SideTouchGestureDetector;
import com.sonymobile.sidetouchgesturedetector.DynamicAreaFilter;

public class SideTouchEventDetector
{
    private static final boolean mIsZoomEnabled = false;
    private final DynamicAreaFilter mDynamicAreaFilter;
    private final OnSideTouchGestureListener mOnSideTouchGestureListener;
    private int mOrientation;
    private final SideTouchGestureDetector mSideTouchGestureDetector;
    
    public SideTouchEventDetector(final Context context, final OnSideTouchGestureListener mOnSideTouchGestureListener) {
        this.mDynamicAreaFilter = new DynamicAreaFilter(context);
        this.mSideTouchGestureDetector = new SideTouchGestureDetector(context, (SideTouchGestureDetector.OnGestureListener)new CameraSideTouchGestureListener());
        this.mOnSideTouchGestureListener = mOnSideTouchGestureListener;
    }
    
    private boolean isInSideTouchValidArea(final MotionEvent motionEvent) {
        return this.mOrientation != 1 || motionEvent.getY(motionEvent.getActionIndex()) <= this.mDynamicAreaFilter.getValidScreenHeight(motionEvent);
    }
    
    public boolean onSideTouchEvent(final MotionEvent motionEvent, final int mOrientation) {
        this.mOrientation = mOrientation;
        this.mDynamicAreaFilter.onSideTouchEvent(motionEvent);
        return this.mSideTouchGestureDetector.onSideTouchEvent(motionEvent);
    }
    
    public void unregister() {
        this.mSideTouchGestureDetector.unregisterSettingsObserver();
        this.mDynamicAreaFilter.unregisterSettingsObserver();
    }
    
    private class CameraSideTouchGestureListener implements OnGestureListener, OnDoubleTapListener
    {
        private int mCurrentGestureId;
        private int mCurrentSide;
        private SideTouchEventDetector.State mCurrentState;
        final SideTouchEventDetector this$0;
        
        private CameraSideTouchGestureListener(final SideTouchEventDetector this$0) {
            this.this$0 = this$0;
            this.mCurrentGestureId = -1;
            this.mCurrentSide = 0;
            this.mCurrentState = SideTouchEventDetector.State.IDLING;
        }
        
        private boolean finishGestureTracking(final int n) {
            if (this.mCurrentGestureId != n) {
                return false;
            }
            this.mCurrentGestureId = -1;
            this.mCurrentSide = 0;
            if (this.mCurrentState == SideTouchEventDetector.State.SCROLLING) {
                this.this$0.mOnSideTouchGestureListener.onScrollEnd();
            }
            this.this$0.mOnSideTouchGestureListener.onGestureFinished();
            return true;
        }
        
        private boolean startGestureTracking(final int mCurrentGestureId, final MotionEvent motionEvent) {
            if (this.mCurrentGestureId != -1) {
                return false;
            }
            if (!this.this$0.isInSideTouchValidArea(motionEvent)) {
                return false;
            }
            this.mCurrentGestureId = mCurrentGestureId;
            this.mCurrentSide = SideTouchUtils.getLogicalScreenSide(motionEvent);
            this.this$0.mOnSideTouchGestureListener.onGestureStart();
            return true;
        }
        
        @Override
        public void onCancel(final MotionEvent motionEvent) {
        }
        
        @Override
        public void onDoubleTap(final int n, final MotionEvent motionEvent) {
            if (this.mCurrentState == SideTouchEventDetector.State.IDLING && this.startGestureTracking(n, motionEvent)) {
                this.mCurrentState = SideTouchEventDetector.State.DOUBLE_TAPPING;
            }
        }
        
        @Override
        public void onDoubleTapForLearning(final DoubleTapInfo doubleTapInfo) {
        }
        
        @Override
        public void onDown(final int n, final MotionEvent motionEvent) {
        }
        
        @Override
        public void onFling(final int n, final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n2, final float n3) {
            if (this.mCurrentState == SideTouchEventDetector.State.DOUBLE_TAPPING && this.finishGestureTracking(n)) {
                this.mCurrentState = SideTouchEventDetector.State.IDLING;
            }
        }
        
        @Override
        public void onGestureFinished(final int n) {
            if (this.mCurrentState != SideTouchEventDetector.State.IDLING && this.finishGestureTracking(n)) {
                this.mCurrentState = SideTouchEventDetector.State.IDLING;
            }
        }
        
        @Override
        public void onLongPress(final int n, final MotionEvent motionEvent) {
            if (this.mCurrentState == SideTouchEventDetector.State.DOUBLE_TAPPING && this.finishGestureTracking(n)) {
                this.mCurrentState = SideTouchEventDetector.State.IDLING;
            }
        }
        
        @Override
        public void onScroll(final int n, final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n2, final float n3) {
            switch (SideTouchUtils.getLogicalScreenSide(motionEvent)) {
                default: {
                    return;
                }
                case 1:
                case 2: {
                    final int pointerIndex = motionEvent2.findPointerIndex(motionEvent.getPointerId(motionEvent.getActionIndex()));
                    switch (SideTouchEventDetector$1.$SwitchMap$com$sonyericsson$android$camera$SideTouchEventDetector$State[this.mCurrentState.ordinal()]) {
                        case 3: {
                            if (this.finishGestureTracking(n)) {
                                this.mCurrentState = SideTouchEventDetector.State.IDLING;
                                break;
                            }
                            break;
                        }
                        case 2: {
                            if (this.mCurrentGestureId == n) {
                                this.this$0.mOnSideTouchGestureListener.onScroll(make(fromCode(this.mCurrentSide), (int)motionEvent2.getX(pointerIndex), (int)motionEvent2.getY(pointerIndex)));
                                break;
                            }
                            break;
                        }
                        case 1: {}
                    }
                }
            }
        }
        
        @Override
        public void onSingleTapConfirmed(final int n, final MotionEvent motionEvent) {
        }
        
        @Override
        public void onUp(final int n, final MotionEvent motionEvent) {
            if (this.mCurrentGestureId != n) {
                return;
            }
            if (this.mCurrentState == SideTouchEventDetector.State.DOUBLE_TAPPING) {
                this.this$0.mOnSideTouchGestureListener.onDoubleTap(make(motionEvent, motionEvent.getActionIndex()), (int)motionEvent.getX(), (int)motionEvent.getY());
                if (this.finishGestureTracking(n)) {
                    this.mCurrentState = SideTouchEventDetector.State.IDLING;
                }
            }
        }
    }
    
    public interface OnSideTouchGestureListener
    {
        void onDoubleTap(final SideTouchEvent p0, final int p1, final int p2);
        
        void onGestureFinished();
        
        void onGestureStart();
        
        void onScroll(final SideTouchEvent p0);
        
        void onScrollEnd();
        
        void onScrollStart(final SideTouchEvent p0);
    }
    
    public enum SideTouchArea
    {
        private static final SideTouchArea[] $VALUES;
        
        BOTTOM, 
        LEFT, 
        RIGHT, 
        TOP, 
        UNKNOWN;
        
        static {
            $VALUES = new SideTouchArea[] { SideTouchArea.UNKNOWN, SideTouchArea.TOP, SideTouchArea.BOTTOM, SideTouchArea.LEFT, SideTouchArea.RIGHT };
        }
        
        private static SideTouchArea fromCode(final int n) {
            if (n == 4) {
                return SideTouchArea.TOP;
            }
            if (n == 8) {
                return SideTouchArea.BOTTOM;
            }
            switch (n) {
                default: {
                    return SideTouchArea.UNKNOWN;
                }
                case 2: {
                    return SideTouchArea.RIGHT;
                }
                case 1: {
                    return SideTouchArea.LEFT;
                }
            }
        }
        
        private static SideTouchArea fromMotionEvent(final MotionEvent motionEvent, final int n) {
            return fromCode(SideTouchUtils.getLogicalScreenSide(motionEvent, n));
        }
    }
    
    public static class SideTouchEvent
    {
        public final SideTouchArea area;
        public final int position;
        
        private SideTouchEvent(final int position, final SideTouchArea area) {
            this.position = position;
            this.area = area;
        }
        
        private static SideTouchEvent make(final MotionEvent motionEvent, final int n) {
            return make(fromMotionEvent(motionEvent, n), (int)motionEvent.getX(n), (int)motionEvent.getY(n));
        }
        
        private static SideTouchEvent make(final SideTouchArea sideTouchArea, final int n, final int n2) {
            switch (SideTouchEventDetector$1.$SwitchMap$com$sonyericsson$android$camera$SideTouchEventDetector$SideTouchArea[sideTouchArea.ordinal()]) {
                default: {
                    return new SideTouchEvent(0, sideTouchArea);
                }
                case 3:
                case 4: {
                    return new SideTouchEvent(n2, sideTouchArea);
                }
                case 1:
                case 2: {
                    return new SideTouchEvent(n, sideTouchArea);
                }
            }
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("position:");
            sb.append(this.position);
            sb.append(", area:");
            sb.append(this.area.name());
            return sb.toString();
        }
    }
    
    private enum State
    {
        private static final State[] $VALUES;
        
        DOUBLE_TAPPING, 
        IDLING, 
        SCROLLING;
        
        static {
            $VALUES = new State[] { State.IDLING, State.SCROLLING, State.DOUBLE_TAPPING };
        }
    }
}
