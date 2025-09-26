// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.controller;

import android.view.MotionEvent;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import com.sonyericsson.android.camera.util.CamLog;
import android.graphics.Point;
import android.content.Context;
import com.sonyericsson.android.camera.view.baselayout.ViewFinderGestureDetector;

public abstract class AbstractDraggingEventHandler implements OnViewFinderGestureDetectorListener
{
    private static final String TAG = "AbstractDraggingEventHandler";
    private final int mCameraSwitchDragFinishDistance;
    private Direction mDirection;
    private int mDragStartMargin;
    private boolean mIsDragging;
    private final int mModeChangeDragFinishDistance;
    
    public AbstractDraggingEventHandler(final Context context, final int mModeChangeDragFinishDistance, final int mCameraSwitchDragFinishDistance) {
        this.mIsDragging = false;
        this.mDirection = Direction.NONE;
        this.mModeChangeDragFinishDistance = mModeChangeDragFinishDistance;
        this.mCameraSwitchDragFinishDistance = mCameraSwitchDragFinishDistance;
        this.mDragStartMargin = context.getResources().getDimensionPixelSize(2131165318);
    }
    
    private boolean acceptNewDirection(final Direction direction) {
        final Direction mDirection = this.mDirection;
        final Direction up = Direction.UP;
        final boolean b = true;
        final boolean b2 = true;
        if (mDirection == up || this.mDirection == Direction.DOWN) {
            boolean b3 = b;
            if (direction != Direction.UP) {
                b3 = (direction == Direction.DOWN && b);
            }
            return b3;
        }
        if (this.mDirection != Direction.RIGHT && this.mDirection != Direction.LEFT) {
            return false;
        }
        boolean b4 = b2;
        if (direction != Direction.RIGHT) {
            b4 = (direction == Direction.LEFT && b2);
        }
        return b4;
    }
    
    private Direction computeDirection(final Point point, final Point point2) {
        Direction direction = Direction.NONE;
        final int i = (int)(Math.atan2(point.x - point2.x, point.y - point2.y) * 180.0 / 3.141592653589793);
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("dragRotation = ");
            sb.append(i);
            CamLog.d(sb.toString());
        }
        if ((i < 45 && i > -45) || (i > 135 && i <= 180) || (i < -135 && i >= -180)) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("VERTICAL dragrotation = ");
                sb2.append(i);
                CamLog.d(sb2.toString());
            }
            if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
                if (point.y > point2.y) {
                    direction = Direction.LEFT;
                }
                else if (point.y < point2.y) {
                    direction = Direction.RIGHT;
                }
            }
            else if (point.y > point2.y) {
                direction = Direction.DOWN;
            }
            else if (point.y < point2.y) {
                direction = Direction.UP;
            }
        }
        else {
            if (CamLog.VERBOSE) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("HORIZONTAL dragrotation = ");
                sb3.append(i);
                CamLog.d(sb3.toString());
            }
            if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
                if (point.x > point2.x) {
                    direction = Direction.UP;
                }
                else if (point.x < point2.x) {
                    direction = Direction.DOWN;
                }
            }
            else if (point.x > point2.x) {
                direction = Direction.LEFT;
            }
            else if (point.x < point2.x) {
                direction = Direction.RIGHT;
            }
        }
        return direction;
    }
    
    private int computeDistance(final Direction direction, final Point point, final Point point2) {
        if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            if (direction == Direction.UP || direction == Direction.DOWN) {
                return point2.x - point.x;
            }
            if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                return point.y - point2.y;
            }
        }
        else {
            if (direction == Direction.UP || direction == Direction.DOWN) {
                return point.y - point2.y;
            }
            if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                return point.x - point2.x;
            }
        }
        return 0;
    }
    
    private float computeProgress(final Direction direction, final Point point, final Point point2) {
        if (direction != Direction.UP && direction != Direction.DOWN) {
            return Math.abs(this.computeDistance(direction, point, point2)) / (float)this.mCameraSwitchDragFinishDistance;
        }
        return Math.abs(this.computeDistance(direction, point, point2)) / (float)this.mModeChangeDragFinishDistance;
    }
    
    private boolean dragging(final Point point, final Point point2) {
        if (this.mIsDragging) {
            final float computeProgress = this.computeProgress(this.mDirection, point, point2);
            final int computeDistance = this.computeDistance(this.mDirection, point, point2);
            final Direction computeDirection = this.computeDirection(point, point2);
            if (this.acceptNewDirection(computeDirection)) {
                this.mDirection = computeDirection;
            }
            if (computeProgress < 1.0f) {
                this.sendProgressEvent(this.mDirection, computeDistance, computeProgress);
            }
            else if (this.mDirection != Direction.LEFT) {
                this.finishDragging(point, point2, FinishReason.UP);
            }
            return true;
        }
        return false;
    }
    
    private boolean finishDragging(final Point point, final Point point2, final FinishReason finishReason) {
        if (this.mIsDragging) {
            this.mIsDragging = false;
            if (this.computeProgress(this.mDirection, point, point2) < 1.0f && (finishReason != FinishReason.FLING || this.mDirection == Direction.RIGHT)) {
                this.sendCancelEvent(this.mDirection);
            }
            else {
                this.sendFinishEvent(this.mDirection);
            }
            return true;
        }
        return false;
    }
    
    private boolean startDragging(final Point point, final Point point2) {
        if (point.y > this.mDragStartMargin && !this.mIsDragging && this.canDragging()) {
            this.mDirection = this.computeDirection(point, point2);
            return this.mIsDragging = this.sendStartEvent(this.mDirection);
        }
        return false;
    }
    
    protected abstract boolean canDragging();
    
    @Override
    public void onDown(final MotionEvent motionEvent) {
        this.sendTouchDownEvent(motionEvent);
    }
    
    @Override
    public void onDragging(final MotionEvent motionEvent, final MotionEvent motionEvent2) {
        final Point obj = new Point((int)motionEvent2.getX(), (int)motionEvent2.getY());
        final Point obj2 = new Point((int)motionEvent.getX(), (int)motionEvent.getY());
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onDragging() E start:");
            sb.append(obj2);
            sb.append(" end:");
            sb.append(obj);
            sb.append(" isDragging:");
            sb.append(this.mIsDragging);
            sb.append(" direction:");
            sb.append(this.mDirection.name());
            CamLog.e(sb.toString());
        }
        final boolean dragging = this.dragging(obj2, obj);
        if (CamLog.VERBOSE) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("onDragging() X handled:");
            sb2.append(dragging);
            sb2.append(" isDragging:");
            sb2.append(this.mIsDragging);
            sb2.append(" direction:");
            sb2.append(this.mDirection.name());
            CamLog.e(sb2.toString());
        }
    }
    
    @Override
    public void onFinishDragging(final MotionEvent motionEvent, final MotionEvent motionEvent2, final FinishReason finishReason) {
        final Point obj = new Point((int)motionEvent.getX(), (int)motionEvent.getY());
        final Point obj2 = new Point((int)motionEvent2.getX(), (int)motionEvent2.getY());
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onFinishDragging() E start:");
            sb.append(obj2);
            sb.append(" end:");
            sb.append(obj);
            sb.append(" reason:");
            sb.append(finishReason.name());
            sb.append(" isDragging:");
            sb.append(this.mIsDragging);
            sb.append(" direction:");
            sb.append(this.mDirection.name());
            CamLog.e(sb.toString());
        }
        final boolean finishDragging = this.finishDragging(obj2, obj, finishReason);
        if (CamLog.VERBOSE) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("onFinishDragging() X handled:");
            sb2.append(finishDragging);
            CamLog.d(sb2.toString());
        }
    }
    
    @Override
    public void onStartDragging(final MotionEvent motionEvent, final MotionEvent motionEvent2) {
        final Point obj = new Point((int)motionEvent2.getX(), (int)motionEvent2.getY());
        final Point point = new Point((int)motionEvent.getX(), (int)motionEvent.getY());
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onStartDragging() E start:");
            sb.append(point.y);
            sb.append(" end:");
            sb.append(obj);
            sb.append(" isDragging:");
            sb.append(this.mIsDragging);
            sb.append(" direction:");
            sb.append(this.mDirection.name());
            CamLog.e(sb.toString());
        }
        final boolean startDragging = this.startDragging(point, obj);
        if (CamLog.VERBOSE) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("onStartDragging() X handled:");
            sb2.append(startDragging);
            sb2.append(" isDragging:");
            sb2.append(this.mIsDragging);
            sb2.append(" direction:");
            sb2.append(this.mDirection.name());
            CamLog.e(sb2.toString());
        }
    }
    
    protected abstract void sendCancelEvent(final Direction p0);
    
    protected abstract void sendFinishEvent(final Direction p0);
    
    protected abstract void sendProgressEvent(final Direction p0, final int p1, final float p2);
    
    protected abstract boolean sendStartEvent(final Direction p0);
    
    protected abstract void sendTouchDownEvent(final MotionEvent p0);
    
    public enum Direction
    {
        private static final Direction[] $VALUES;
        
        DOWN, 
        LEFT, 
        NONE, 
        RIGHT, 
        UP;
        
        static {
            $VALUES = new Direction[] { Direction.NONE, Direction.UP, Direction.DOWN, Direction.RIGHT, Direction.LEFT };
        }
    }
}
