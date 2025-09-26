// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.baselayout;

import com.sonyericsson.android.camera.util.CamLog;
import java.util.Iterator;
import java.util.ArrayList;
import android.graphics.Point;
import android.content.Context;
import android.view.GestureDetector$OnGestureListener;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.View;
import java.util.List;
import android.view.MotionEvent;

public class ViewFinderGestureDetector
{
    private static final String TAG = "ViewFinderGestureDetector";
    private static final boolean TRACE = false;
    private int mAcceptedDragDirectionFlags;
    private Direction mDragDirection;
    private MotionEvent mDragStartEvent;
    private final List<View> mExclusiveViews;
    private final GestureDetector mGestureDetector;
    private final Rect mGlobalVisibleRect;
    private boolean mIsExclusive;
    private boolean mIsStartDraggingSlopEnabled;
    private OnViewFinderGestureDetectorListener mListener;
    private final int mModeSwitchDragFinishDistanceForFling;
    private final GestureDetector$OnGestureListener mOnGestureListener;
    private final float mStartDraggingMovementSlop;
    private final float mStartDraggingTimeSlop;
    private MotionEvent mTriggerEvent;
    
    public ViewFinderGestureDetector(final Context context) {
        this.mGlobalVisibleRect = new Rect();
        this.mOnGestureListener = (GestureDetector$OnGestureListener)new GestureDetector$OnGestureListener() {
            final ViewFinderGestureDetector this$0;
            
            public boolean onDown(final MotionEvent motionEvent) {
                return false;
            }
            
            public boolean onFling(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2) {
                if (motionEvent == null || motionEvent2 == null) {
                    return false;
                }
                final Direction access$200 = this.this$0.computeDraggingDirection(n, n2);
                if (this.this$0.isDraggingAccepted(access$200)) {
                    final Point point = new Point((int)motionEvent.getX(), (int)motionEvent.getY());
                    final Point point2 = new Point((int)motionEvent2.getX(), (int)motionEvent2.getY());
                    if (ViewFinderGestureDetector$2.$SwitchMap$com$sonyericsson$android$camera$view$baselayout$ViewFinderGestureDetector$Direction[access$200.ordinal()] == 1) {
                        if (this.this$0.mModeSwitchDragFinishDistanceForFling > computeDistance(point2, point)) {
                            return true;
                        }
                    }
                    if (!this.this$0.isDragging()) {
                        this.this$0.notifyOnStartDragging(this.this$0.mTriggerEvent, motionEvent2);
                    }
                    this.this$0.notifyOnFinishDragging(this.this$0.mTriggerEvent, motionEvent2, FinishReason.FLING);
                    return true;
                }
                return false;
            }
            
            public void onLongPress(final MotionEvent motionEvent) {
            }
            
            public boolean onScroll(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2) {
                if (!this.this$0.isDragging()) {
                    final Direction access$200 = this.this$0.computeDraggingDirection(n, n2);
                    if (this.this$0.isStartDraggingAccepted(access$200, motionEvent2)) {
                        this.this$0.resetDragStartEvent(motionEvent2);
                        this.this$0.mDragDirection = access$200;
                        if (this.this$0.isDraggingAccepted(this.this$0.mDragDirection)) {
                            this.this$0.notifyOnStartDragging(this.this$0.mTriggerEvent, this.this$0.mDragStartEvent);
                            return true;
                        }
                    }
                }
                else if (this.this$0.isDraggingAccepted(this.this$0.mDragDirection)) {
                    this.this$0.notifyOnDragging(this.this$0.mDragStartEvent, motionEvent2);
                    return true;
                }
                return false;
            }
            
            public void onShowPress(final MotionEvent motionEvent) {
            }
            
            public boolean onSingleTapUp(final MotionEvent motionEvent) {
                return false;
            }
        };
        this.mGestureDetector = new GestureDetector(context, this.mOnGestureListener);
        this.mExclusiveViews = new ArrayList<View>();
        this.mDragDirection = Direction.NONE;
        this.mIsExclusive = false;
        this.mAcceptedDragDirectionFlags = Direction.NONE.flag;
        this.mStartDraggingTimeSlop = (float)context.getResources().getInteger(2131361813);
        this.mStartDraggingMovementSlop = (float)context.getResources().getDimensionPixelSize(2131165735);
        this.mIsStartDraggingSlopEnabled = true;
        this.mModeSwitchDragFinishDistanceForFling = context.getResources().getDimensionPixelSize(2131165330);
    }
    
    private static int computeDistance(final Point point, final Point point2) {
        final int abs = Math.abs(point2.x - point.x);
        int abs2;
        if (abs > (abs2 = Math.abs(point2.y - point.y))) {
            abs2 = abs;
        }
        return abs2;
    }
    
    private Direction computeDraggingDirection(final float n, final float n2) {
        if (Math.abs(n) > Math.abs(n2)) {
            return Direction.HORIZONTAL;
        }
        if (Math.abs(n) < Math.abs(n2)) {
            return Direction.VERTICAL;
        }
        return Direction.NONE;
    }
    
    private boolean isAccepted(final MotionEvent motionEvent) {
        return this.mTriggerEvent != null && this.mTriggerEvent.getActionIndex() == motionEvent.getActionIndex();
    }
    
    private boolean isDragging() {
        return this.mDragDirection != Direction.NONE;
    }
    
    private boolean isDraggingAccepted(final Direction direction) {
        final Direction none = Direction.NONE;
        boolean b = false;
        if (direction == none) {
            return false;
        }
        if ((this.mAcceptedDragDirectionFlags & direction.flag) == direction.flag) {
            b = true;
        }
        return b;
    }
    
    private boolean isEnabled() {
        return this.mAcceptedDragDirectionFlags != Direction.NONE.flag;
    }
    
    private boolean isExclusiveViewEvent(final MotionEvent motionEvent) {
        final Iterator<View> iterator = this.mExclusiveViews.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getGlobalVisibleRect(this.mGlobalVisibleRect) && this.mGlobalVisibleRect.contains((int)motionEvent.getX(), (int)motionEvent.getY())) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isStartDraggingAccepted(final Direction direction, final MotionEvent motionEvent) {
        if (this.mTriggerEvent == null) {
            return false;
        }
        if (this.mIsStartDraggingSlopEnabled) {
            switch (ViewFinderGestureDetector$2.$SwitchMap$com$sonyericsson$android$camera$view$baselayout$ViewFinderGestureDetector$Direction[direction.ordinal()]) {
                default: {
                    return false;
                }
                case 2: {
                    if (Math.abs(this.mTriggerEvent.getY() - motionEvent.getY()) < this.mStartDraggingMovementSlop) {
                        return false;
                    }
                    break;
                }
                case 1: {
                    if (Math.abs(this.mTriggerEvent.getX() - motionEvent.getX()) < this.mStartDraggingMovementSlop) {
                        return false;
                    }
                    break;
                }
            }
            if (motionEvent.getEventTime() - this.mTriggerEvent.getDownTime() < this.mStartDraggingTimeSlop) {
                return false;
            }
        }
        return true;
    }
    
    private void notifyOnDragging(final MotionEvent motionEvent, final MotionEvent motionEvent2) {
        if (this.mListener != null && motionEvent != null && motionEvent2 != null) {
            this.mListener.onDragging(motionEvent, motionEvent2);
        }
    }
    
    private void notifyOnFinishDragging(final MotionEvent motionEvent, final MotionEvent motionEvent2, final FinishReason finishReason) {
        if (this.mListener != null && motionEvent != null && motionEvent2 != null) {
            this.mListener.onFinishDragging(motionEvent, motionEvent2, finishReason);
        }
    }
    
    private void notifyOnStartDragging(final MotionEvent motionEvent, final MotionEvent motionEvent2) {
        if (this.mListener != null && motionEvent != null && motionEvent2 != null) {
            this.mListener.onStartDragging(motionEvent, motionEvent2);
        }
    }
    
    private void resetDragStartEvent(final MotionEvent motionEvent) {
        if (this.mDragStartEvent != null) {
            this.mDragStartEvent.recycle();
            this.mDragStartEvent = null;
        }
        if (motionEvent != null) {
            this.mDragStartEvent = MotionEvent.obtain(motionEvent);
        }
    }
    
    private void resetTriggerEvent(final MotionEvent motionEvent) {
        if (this.mTriggerEvent != null) {
            this.mTriggerEvent.recycle();
            this.mTriggerEvent = null;
        }
        if (motionEvent != null) {
            this.mTriggerEvent = MotionEvent.obtain(motionEvent);
        }
    }
    
    private void trace(final String s) {
        CamLog.e(s);
    }
    
    public void addExclusiveView(final View view) {
        this.mExclusiveViews.add(view);
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            if (this.mListener != null) {
                this.mListener.onDown(motionEvent);
            }
            this.mDragDirection = Direction.NONE;
            this.resetTriggerEvent(motionEvent);
            if (this.isExclusiveViewEvent(motionEvent)) {
                this.mIsExclusive = true;
                return false;
            }
        }
        if (this.mIsExclusive) {
            final int action = motionEvent.getAction();
            if (action != 1 && action != 3) {
                return false;
            }
            this.mIsExclusive = false;
            this.resetTriggerEvent(null);
            this.resetDragStartEvent(null);
            return false;
        }
        else {
            if (!this.isEnabled()) {
                return false;
            }
            if (motionEvent.getPointerCount() > 1) {
                if (this.isDraggingAccepted(this.mDragDirection)) {
                    this.notifyOnFinishDragging(this.mDragStartEvent, motionEvent, FinishReason.CANCEL);
                }
                this.mDragDirection = Direction.NONE;
                return false;
            }
            if (!this.isAccepted(motionEvent)) {
                return false;
            }
            final boolean onTouchEvent = this.mGestureDetector.onTouchEvent(motionEvent);
            final int action2 = motionEvent.getAction();
            if (action2 != 1) {
                if (action2 == 3) {
                    if (this.isDraggingAccepted(this.mDragDirection)) {
                        this.notifyOnFinishDragging(this.mTriggerEvent, motionEvent, FinishReason.CANCEL);
                    }
                    this.mDragDirection = Direction.NONE;
                    this.resetTriggerEvent(null);
                    this.resetDragStartEvent(null);
                }
            }
            else {
                if (this.isDraggingAccepted(this.mDragDirection)) {
                    this.notifyOnFinishDragging(this.mTriggerEvent, motionEvent, FinishReason.UP);
                }
                this.mDragDirection = Direction.NONE;
                this.resetTriggerEvent(null);
                this.resetDragStartEvent(null);
            }
            return onTouchEvent;
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        this.mIsExclusive = false;
        if (!this.isEnabled()) {
            this.resetTriggerEvent(null);
            this.resetDragStartEvent(null);
            return false;
        }
        if (!this.isAccepted(motionEvent)) {
            return false;
        }
        if (motionEvent.getAction() == 0) {
            this.mDragDirection = Direction.NONE;
            this.resetTriggerEvent(motionEvent);
        }
        final boolean onTouchEvent = this.mGestureDetector.onTouchEvent(motionEvent);
        final int action = motionEvent.getAction();
        final boolean b = true;
        if (action != 3) {
            boolean b2 = b;
            switch (action) {
                default: {
                    b2 = onTouchEvent;
                    return b2;
                }
                case 1: {
                    break;
                }
                case 0: {
                    return b2;
                }
            }
        }
        if (this.isDraggingAccepted(this.mDragDirection)) {
            this.notifyOnFinishDragging(this.mTriggerEvent, motionEvent, FinishReason.UP);
        }
        this.mDragDirection = Direction.NONE;
        this.resetTriggerEvent(null);
        this.resetDragStartEvent(null);
        return b;
    }
    
    public void setAcceptDragDirection(final Direction... array) {
        int i = 0;
        this.mAcceptedDragDirectionFlags = 0;
        while (i < array.length) {
            this.mAcceptedDragDirectionFlags |= array[i].flag;
            ++i;
        }
        if (!this.mDragDirection.isAccepted(this.mAcceptedDragDirectionFlags)) {
            this.mDragDirection = Direction.NONE;
        }
    }
    
    public void setOnGestureDetectorListener(final OnViewFinderGestureDetectorListener mListener) {
        this.mListener = mListener;
    }
    
    public void setStartDraggingSlopEnabled(final boolean mIsStartDraggingSlopEnabled) {
        this.mIsStartDraggingSlopEnabled = mIsStartDraggingSlopEnabled;
    }
    
    public enum Direction
    {
        private static final Direction[] $VALUES;
        
        HORIZONTAL(2), 
        NONE(0), 
        VERTICAL(1);
        
        int flag;
        
        static {
            $VALUES = new Direction[] { Direction.NONE, Direction.VERTICAL, Direction.HORIZONTAL };
        }
        
        private Direction(final int flag) {
            this.flag = flag;
        }
        
        private boolean isAccepted(final int n) {
            return (n & this.flag) == this.flag;
        }
    }
    
    public enum FinishReason
    {
        private static final FinishReason[] $VALUES;
        
        CANCEL, 
        FLING, 
        UP;
        
        static {
            $VALUES = new FinishReason[] { FinishReason.CANCEL, FinishReason.UP, FinishReason.FLING };
        }
    }
    
    public interface OnViewFinderGestureDetectorListener
    {
        void onDown(final MotionEvent p0);
        
        void onDragging(final MotionEvent p0, final MotionEvent p1);
        
        void onFinishDragging(final MotionEvent p0, final MotionEvent p1, final FinishReason p2);
        
        void onStartDragging(final MotionEvent p0, final MotionEvent p1);
    }
}
