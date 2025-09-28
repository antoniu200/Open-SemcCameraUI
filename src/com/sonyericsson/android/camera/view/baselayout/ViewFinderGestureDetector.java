package com.sonyericsson.android.camera.view.baselayout;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ViewFinderGestureDetector {
    private static final String TAG = "ViewFinderGestureDetector";
    private static final boolean TRACE = false;
    private MotionEvent mDragStartEvent;
    private final GestureDetector mGestureDetector;
    private OnViewFinderGestureDetectorListener mListener;
    private final int mModeSwitchDragFinishDistanceForFling;
    private final float mStartDraggingMovementSlop;
    private final float mStartDraggingTimeSlop;
    private MotionEvent mTriggerEvent;
    private final Rect mGlobalVisibleRect = new Rect();
    private final GestureDetector.OnGestureListener mOnGestureListener = new GestureDetector.OnGestureListener() { // from class: com.sonyericsson.android.camera.view.baselayout.ViewFinderGestureDetector.1
        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent motionEvent) {
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public void onShowPress(MotionEvent motionEvent) {
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (!ViewFinderGestureDetector.this.isDragging()) {
                Direction directionComputeDraggingDirection = ViewFinderGestureDetector.this.computeDraggingDirection(f, f2);
                if (!ViewFinderGestureDetector.this.isStartDraggingAccepted(directionComputeDraggingDirection, motionEvent2)) {
                    return false;
                }
                ViewFinderGestureDetector.this.resetDragStartEvent(motionEvent2);
                ViewFinderGestureDetector.this.mDragDirection = directionComputeDraggingDirection;
                if (!ViewFinderGestureDetector.this.isDraggingAccepted(ViewFinderGestureDetector.this.mDragDirection)) {
                    return false;
                }
                ViewFinderGestureDetector.this.notifyOnStartDragging(ViewFinderGestureDetector.this.mTriggerEvent, ViewFinderGestureDetector.this.mDragStartEvent);
                return true;
            }
            if (!ViewFinderGestureDetector.this.isDraggingAccepted(ViewFinderGestureDetector.this.mDragDirection)) {
                return false;
            }
            ViewFinderGestureDetector.this.notifyOnDragging(ViewFinderGestureDetector.this.mDragStartEvent, motionEvent2);
            return true;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (motionEvent == null || motionEvent2 == null) {
                return false;
            }
            Direction directionComputeDraggingDirection = ViewFinderGestureDetector.this.computeDraggingDirection(f, f2);
            if (!ViewFinderGestureDetector.this.isDraggingAccepted(directionComputeDraggingDirection)) {
                return false;
            }
            Point point = new Point((int) motionEvent.getX(), (int) motionEvent.getY());
            Point point2 = new Point((int) motionEvent2.getX(), (int) motionEvent2.getY());
            if (AnonymousClass2.$SwitchMap$com$sonyericsson$android$camera$view$baselayout$ViewFinderGestureDetector$Direction[directionComputeDraggingDirection.ordinal()] != 1 || ViewFinderGestureDetector.this.mModeSwitchDragFinishDistanceForFling <= ViewFinderGestureDetector.computeDistance(point2, point)) {
                if (!ViewFinderGestureDetector.this.isDragging()) {
                    ViewFinderGestureDetector.this.notifyOnStartDragging(ViewFinderGestureDetector.this.mTriggerEvent, motionEvent2);
                }
                ViewFinderGestureDetector.this.notifyOnFinishDragging(ViewFinderGestureDetector.this.mTriggerEvent, motionEvent2, FinishReason.FLING);
            }
            return true;
        }
    };
    private final List<View> mExclusiveViews = new ArrayList();
    private Direction mDragDirection = Direction.NONE;
    private boolean mIsExclusive = false;
    private int mAcceptedDragDirectionFlags = Direction.NONE.flag;
    private boolean mIsStartDraggingSlopEnabled = true;

    public enum FinishReason {
        CANCEL,
        UP,
        FLING
    }

    public interface OnViewFinderGestureDetectorListener {
        void onDown(MotionEvent motionEvent);

        void onDragging(MotionEvent motionEvent, MotionEvent motionEvent2);

        void onFinishDragging(MotionEvent motionEvent, MotionEvent motionEvent2, FinishReason finishReason);

        void onStartDragging(MotionEvent motionEvent, MotionEvent motionEvent2);
    }

    private void trace(String str) {
        CamLog.e(str);
    }

    public enum Direction {
        NONE(0),
        VERTICAL(1),
        HORIZONTAL(2);

        int flag;

        Direction(int i) {
            this.flag = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isAccepted(int i) {
            return (i & this.flag) == this.flag;
        }
    }

    public ViewFinderGestureDetector(Context context) {
        this.mGestureDetector = new GestureDetector(context, this.mOnGestureListener);
        this.mStartDraggingTimeSlop = context.getResources().getInteger(R.integer.viewfinder_gesture_detector_time_slop_millis);
        this.mStartDraggingMovementSlop = context.getResources().getDimensionPixelSize(R.dimen.viewfinder_gesture_detector_movement_slop);
        this.mModeSwitchDragFinishDistanceForFling = context.getResources().getDimensionPixelSize(R.dimen.fling_threshold);
    }

    public void setStartDraggingSlopEnabled(boolean z) {
        this.mIsStartDraggingSlopEnabled = z;
    }

    public void setAcceptDragDirection(Direction... directionArr) {
        this.mAcceptedDragDirectionFlags = 0;
        for (Direction direction : directionArr) {
            this.mAcceptedDragDirectionFlags = direction.flag | this.mAcceptedDragDirectionFlags;
        }
        if (this.mDragDirection.isAccepted(this.mAcceptedDragDirectionFlags)) {
            return;
        }
        this.mDragDirection = Direction.NONE;
    }

    public void setOnGestureDetectorListener(OnViewFinderGestureDetectorListener onViewFinderGestureDetectorListener) {
        this.mListener = onViewFinderGestureDetectorListener;
    }

    public void addExclusiveView(View view) {
        this.mExclusiveViews.add(view);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            if (this.mListener != null) {
                this.mListener.onDown(motionEvent);
            }
            this.mDragDirection = Direction.NONE;
            resetTriggerEvent(motionEvent);
            if (isExclusiveViewEvent(motionEvent)) {
                this.mIsExclusive = true;
                return false;
            }
        }
        if (this.mIsExclusive) {
            int action = motionEvent.getAction();
            if (action != 1 && action != 3) {
                return false;
            }
            this.mIsExclusive = false;
            resetTriggerEvent(null);
            resetDragStartEvent(null);
            return false;
        }
        if (!isEnabled()) {
            return false;
        }
        if (motionEvent.getPointerCount() > 1) {
            if (isDraggingAccepted(this.mDragDirection)) {
                notifyOnFinishDragging(this.mDragStartEvent, motionEvent, FinishReason.CANCEL);
            }
            this.mDragDirection = Direction.NONE;
            return false;
        }
        if (!isAccepted(motionEvent)) {
            return false;
        }
        boolean zOnTouchEvent = this.mGestureDetector.onTouchEvent(motionEvent);
        int action2 = motionEvent.getAction();
        if (action2 == 1) {
            if (isDraggingAccepted(this.mDragDirection)) {
                notifyOnFinishDragging(this.mTriggerEvent, motionEvent, FinishReason.UP);
            }
            this.mDragDirection = Direction.NONE;
            resetTriggerEvent(null);
            resetDragStartEvent(null);
        } else if (action2 == 3) {
            if (isDraggingAccepted(this.mDragDirection)) {
                notifyOnFinishDragging(this.mTriggerEvent, motionEvent, FinishReason.CANCEL);
            }
            this.mDragDirection = Direction.NONE;
            resetTriggerEvent(null);
            resetDragStartEvent(null);
        }
        return zOnTouchEvent;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.mIsExclusive = false;
        if (!isEnabled()) {
            resetTriggerEvent(null);
            resetDragStartEvent(null);
            return false;
        }
        if (!isAccepted(motionEvent)) {
            return false;
        }
        if (motionEvent.getAction() == 0) {
            this.mDragDirection = Direction.NONE;
            resetTriggerEvent(motionEvent);
        }
        boolean zOnTouchEvent = this.mGestureDetector.onTouchEvent(motionEvent);
        int action = motionEvent.getAction();
        if (action != 3) {
            switch (action) {
            }
            return false;
        }
        if (isDraggingAccepted(this.mDragDirection)) {
            notifyOnFinishDragging(this.mTriggerEvent, motionEvent, FinishReason.UP);
        }
        this.mDragDirection = Direction.NONE;
        resetTriggerEvent(null);
        resetDragStartEvent(null);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDragging() {
        return this.mDragDirection != Direction.NONE;
    }

    private boolean isEnabled() {
        return this.mAcceptedDragDirectionFlags != Direction.NONE.flag;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isStartDraggingAccepted(Direction direction, MotionEvent motionEvent) {
        if (this.mTriggerEvent == null) {
            return false;
        }
        if (!this.mIsStartDraggingSlopEnabled) {
            return true;
        }
        switch (direction) {
            case HORIZONTAL:
                if (Math.abs(this.mTriggerEvent.getX() - motionEvent.getX()) < this.mStartDraggingMovementSlop) {
                    return false;
                }
                break;
            case VERTICAL:
                if (Math.abs(this.mTriggerEvent.getY() - motionEvent.getY()) < this.mStartDraggingMovementSlop) {
                    return false;
                }
                break;
            default:
                return false;
        }
        return ((float) (motionEvent.getEventTime() - this.mTriggerEvent.getDownTime())) >= this.mStartDraggingTimeSlop;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDraggingAccepted(Direction direction) {
        return direction != Direction.NONE && (this.mAcceptedDragDirectionFlags & direction.flag) == direction.flag;
    }

    private boolean isAccepted(MotionEvent motionEvent) {
        return this.mTriggerEvent != null && this.mTriggerEvent.getActionIndex() == motionEvent.getActionIndex();
    }

    private boolean isExclusiveViewEvent(MotionEvent motionEvent) {
        Iterator<View> it = this.mExclusiveViews.iterator();
        while (it.hasNext()) {
            if (it.next().getGlobalVisibleRect(this.mGlobalVisibleRect) && this.mGlobalVisibleRect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                return true;
            }
        }
        return false;
    }

    private void resetTriggerEvent(MotionEvent motionEvent) {
        if (this.mTriggerEvent != null) {
            this.mTriggerEvent.recycle();
            this.mTriggerEvent = null;
        }
        if (motionEvent != null) {
            this.mTriggerEvent = MotionEvent.obtain(motionEvent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetDragStartEvent(MotionEvent motionEvent) {
        if (this.mDragStartEvent != null) {
            this.mDragStartEvent.recycle();
            this.mDragStartEvent = null;
        }
        if (motionEvent != null) {
            this.mDragStartEvent = MotionEvent.obtain(motionEvent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Direction computeDraggingDirection(float f, float f2) {
        if (Math.abs(f) > Math.abs(f2)) {
            return Direction.HORIZONTAL;
        }
        if (Math.abs(f) < Math.abs(f2)) {
            return Direction.VERTICAL;
        }
        return Direction.NONE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int computeDistance(Point point, Point point2) {
        int iAbs = Math.abs(point2.x - point.x);
        int iAbs2 = Math.abs(point2.y - point.y);
        return iAbs > iAbs2 ? iAbs : iAbs2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyOnDragging(MotionEvent motionEvent, MotionEvent motionEvent2) {
        if (this.mListener == null || motionEvent == null || motionEvent2 == null) {
            return;
        }
        this.mListener.onDragging(motionEvent, motionEvent2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyOnStartDragging(MotionEvent motionEvent, MotionEvent motionEvent2) {
        if (this.mListener == null || motionEvent == null || motionEvent2 == null) {
            return;
        }
        this.mListener.onStartDragging(motionEvent, motionEvent2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyOnFinishDragging(MotionEvent motionEvent, MotionEvent motionEvent2, FinishReason finishReason) {
        if (this.mListener == null || motionEvent == null || motionEvent2 == null) {
            return;
        }
        this.mListener.onFinishDragging(motionEvent, motionEvent2, finishReason);
    }
}
