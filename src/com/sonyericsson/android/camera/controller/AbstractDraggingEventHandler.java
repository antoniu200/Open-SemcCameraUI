package com.sonyericsson.android.camera.controller;

import android.content.Context;
import android.graphics.Point;
import android.view.MotionEvent;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.view.baselayout.ViewFinderGestureDetector;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class AbstractDraggingEventHandler implements ViewFinderGestureDetector.OnViewFinderGestureDetectorListener {
    private static final String TAG = "AbstractDraggingEventHandler";
    private final int mCameraSwitchDragFinishDistance;
    private int mDragStartMargin;
    private final int mModeChangeDragFinishDistance;
    private boolean mIsDragging = false;
    private Direction mDirection = Direction.NONE;

    public enum Direction {
        NONE,
        UP,
        DOWN,
        RIGHT,
        LEFT
    }

    protected abstract boolean canDragging();

    protected abstract void sendCancelEvent(Direction direction);

    protected abstract void sendFinishEvent(Direction direction);

    protected abstract void sendProgressEvent(Direction direction, int i, float f);

    protected abstract boolean sendStartEvent(Direction direction);

    protected abstract void sendTouchDownEvent(MotionEvent motionEvent);

    public AbstractDraggingEventHandler(Context context, int i, int i2) {
        this.mModeChangeDragFinishDistance = i;
        this.mCameraSwitchDragFinishDistance = i2;
        this.mDragStartMargin = context.getResources().getDimensionPixelSize(R.dimen.drag_start_margin);
    }

    private boolean startDragging(Point point, Point point2) {
        if (point.y <= this.mDragStartMargin || this.mIsDragging || !canDragging()) {
            return false;
        }
        this.mDirection = computeDirection(point, point2);
        this.mIsDragging = sendStartEvent(this.mDirection);
        return this.mIsDragging;
    }

    private boolean finishDragging(Point point, Point point2, ViewFinderGestureDetector.FinishReason finishReason) {
        if (!this.mIsDragging) {
            return false;
        }
        this.mIsDragging = false;
        if (computeProgress(this.mDirection, point, point2) >= 1.0f || (finishReason == ViewFinderGestureDetector.FinishReason.FLING && this.mDirection != Direction.RIGHT)) {
            sendFinishEvent(this.mDirection);
            return true;
        }
        sendCancelEvent(this.mDirection);
        return true;
    }

    private boolean dragging(Point point, Point point2) {
        if (!this.mIsDragging) {
            return false;
        }
        float fComputeProgress = computeProgress(this.mDirection, point, point2);
        int iComputeDistance = computeDistance(this.mDirection, point, point2);
        Direction directionComputeDirection = computeDirection(point, point2);
        if (acceptNewDirection(directionComputeDirection)) {
            this.mDirection = directionComputeDirection;
        }
        if (fComputeProgress < 1.0f) {
            sendProgressEvent(this.mDirection, iComputeDistance, fComputeProgress);
            return true;
        }
        if (this.mDirection == Direction.LEFT) {
            return true;
        }
        finishDragging(point, point2, ViewFinderGestureDetector.FinishReason.UP);
        return true;
    }

    private float computeProgress(Direction direction, Point point, Point point2) {
        if (direction == Direction.UP || direction == Direction.DOWN) {
            return Math.abs(computeDistance(direction, point, point2)) / this.mModeChangeDragFinishDistance;
        }
        return Math.abs(computeDistance(direction, point, point2)) / this.mCameraSwitchDragFinishDistance;
    }

    private int computeDistance(Direction direction, Point point, Point point2) {
        if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            if (direction == Direction.UP || direction == Direction.DOWN) {
                return point2.x - point.x;
            }
            if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                return point.y - point2.y;
            }
            return 0;
        }
        if (direction == Direction.UP || direction == Direction.DOWN) {
            return point.y - point2.y;
        }
        if (direction == Direction.LEFT || direction == Direction.RIGHT) {
            return point.x - point2.x;
        }
        return 0;
    }

    private Direction computeDirection(Point point, Point point2) {
        Direction direction = Direction.NONE;
        int iAtan2 = (int) ((Math.atan2(point.x - point2.x, point.y - point2.y) * 180.0d) / 3.141592653589793d);
        if (CamLog.VERBOSE) {
            CamLog.d("dragRotation = " + iAtan2);
        }
        if ((iAtan2 < 45 && iAtan2 > -45) || ((iAtan2 > 135 && iAtan2 <= 180) || (iAtan2 < -135 && iAtan2 >= -180))) {
            if (CamLog.VERBOSE) {
                CamLog.d("VERTICAL dragrotation = " + iAtan2);
            }
            if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
                if (point.y > point2.y) {
                    return Direction.LEFT;
                }
                return point.y < point2.y ? Direction.RIGHT : direction;
            }
            if (point.y > point2.y) {
                return Direction.DOWN;
            }
            return point.y < point2.y ? Direction.UP : direction;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("HORIZONTAL dragrotation = " + iAtan2);
        }
        if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            if (point.x > point2.x) {
                return Direction.UP;
            }
            return point.x < point2.x ? Direction.DOWN : direction;
        }
        if (point.x > point2.x) {
            return Direction.LEFT;
        }
        return point.x < point2.x ? Direction.RIGHT : direction;
    }

    private boolean acceptNewDirection(Direction direction) {
        if (this.mDirection == Direction.UP || this.mDirection == Direction.DOWN) {
            return direction == Direction.UP || direction == Direction.DOWN;
        }
        if (this.mDirection == Direction.RIGHT || this.mDirection == Direction.LEFT) {
            return direction == Direction.RIGHT || direction == Direction.LEFT;
        }
        return false;
    }

    @Override // com.sonyericsson.android.camera.view.baselayout.ViewFinderGestureDetector.OnViewFinderGestureDetectorListener
    public void onDown(MotionEvent motionEvent) {
        sendTouchDownEvent(motionEvent);
    }

    @Override // com.sonyericsson.android.camera.view.baselayout.ViewFinderGestureDetector.OnViewFinderGestureDetectorListener
    public void onDragging(MotionEvent motionEvent, MotionEvent motionEvent2) {
        Point point = new Point((int) motionEvent2.getX(), (int) motionEvent2.getY());
        Point point2 = new Point((int) motionEvent.getX(), (int) motionEvent.getY());
        if (CamLog.VERBOSE) {
            CamLog.e("onDragging() E start:" + point2 + " end:" + point + " isDragging:" + this.mIsDragging + " direction:" + this.mDirection.name());
        }
        boolean zDragging = dragging(point2, point);
        if (CamLog.VERBOSE) {
            CamLog.e("onDragging() X handled:" + zDragging + " isDragging:" + this.mIsDragging + " direction:" + this.mDirection.name());
        }
    }

    @Override // com.sonyericsson.android.camera.view.baselayout.ViewFinderGestureDetector.OnViewFinderGestureDetectorListener
    public void onStartDragging(MotionEvent motionEvent, MotionEvent motionEvent2) {
        Point point = new Point((int) motionEvent2.getX(), (int) motionEvent2.getY());
        Point point2 = new Point((int) motionEvent.getX(), (int) motionEvent.getY());
        if (CamLog.VERBOSE) {
            CamLog.e("onStartDragging() E start:" + point2.y + " end:" + point + " isDragging:" + this.mIsDragging + " direction:" + this.mDirection.name());
        }
        boolean zStartDragging = startDragging(point2, point);
        if (CamLog.VERBOSE) {
            CamLog.e("onStartDragging() X handled:" + zStartDragging + " isDragging:" + this.mIsDragging + " direction:" + this.mDirection.name());
        }
    }

    @Override // com.sonyericsson.android.camera.view.baselayout.ViewFinderGestureDetector.OnViewFinderGestureDetectorListener
    public void onFinishDragging(MotionEvent motionEvent, MotionEvent motionEvent2, ViewFinderGestureDetector.FinishReason finishReason) {
        Point point = new Point((int) motionEvent.getX(), (int) motionEvent.getY());
        Point point2 = new Point((int) motionEvent2.getX(), (int) motionEvent2.getY());
        if (CamLog.VERBOSE) {
            CamLog.e("onFinishDragging() E start:" + point2 + " end:" + point + " reason:" + finishReason.name() + " isDragging:" + this.mIsDragging + " direction:" + this.mDirection.name());
        }
        boolean zFinishDragging = finishDragging(point2, point, finishReason);
        if (CamLog.VERBOSE) {
            CamLog.d("onFinishDragging() X handled:" + zFinishDragging);
        }
    }
}
