package com.sonyericsson.android.camera.view.selectabledialog;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.OverScroller;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.view.selectabledialog.AbsSelectableDialog;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ScrollContainer extends FrameLayout {
    private static final int INVALID_POINTER = -1;
    private static final int MIN_SCROLL_DURATION = 200;
    private static final String TAG = "ScrollContainer";
    private Status currentStatus;
    private int mActivePointerId;
    private int mChildHeight;
    private Context mContext;
    private DIRECTION mDirection;
    private boolean mIsBeingDragged;
    private int mLastMotionY;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private OnScrollListener mOnScrollListener;
    private int mOrientation;
    private AbsSelectableDialog.Params mParams;
    private OverScroller mScroller;
    private int mSettingDefaultHeight;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    private ViewConfiguration mViewConfiguration;
    private int offsetY;

    enum DIRECTION {
        UP,
        DOWN
    }

    public interface OnScrollListener {
        void onScrollFinished(Status status);

        void onScrollProgressChanged(float f);
    }

    public enum Status {
        IDLE,
        EXIT,
        OPENED,
        FULLSCREEN,
        MOVING,
        OPENING,
        CLOSING
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return true;
    }

    public ScrollContainer(@NonNull Context context) {
        super(context);
        this.currentStatus = Status.OPENED;
        this.mOnScrollListener = null;
        this.mActivePointerId = -1;
        init();
    }

    public ScrollContainer(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.currentStatus = Status.OPENED;
        this.mOnScrollListener = null;
        this.mActivePointerId = -1;
        init();
    }

    public ScrollContainer(@NonNull Context context, @Nullable AttributeSet attributeSet, @AttrRes int i) {
        super(context, attributeSet, i);
        this.currentStatus = Status.OPENED;
        this.mOnScrollListener = null;
        this.mActivePointerId = -1;
        init();
    }

    private void init() {
        this.mContext = getContext();
        this.mViewConfiguration = ViewConfiguration.get(this.mContext);
        this.mTouchSlop = this.mViewConfiguration.getScaledTouchSlop();
        this.mMinimumVelocity = this.mViewConfiguration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = this.mViewConfiguration.getScaledMaximumFlingVelocity();
        this.mScroller = new OverScroller(this.mContext);
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if ((motionEvent.getAction() == 2 && this.mIsBeingDragged) || super.onInterceptTouchEvent(motionEvent)) {
            return true;
        }
        int action = motionEvent.getAction() & 255;
        if (action != 6) {
            switch (action) {
                case 0:
                    int y = (int) motionEvent.getY();
                    if (!inChild((int) motionEvent.getX(), y)) {
                        this.mIsBeingDragged = false;
                        recycleVelocityTracker();
                        break;
                    } else {
                        this.mActivePointerId = motionEvent.getPointerId(0);
                        this.mLastMotionY = y;
                        initOrResetVelocityTracker();
                        this.mVelocityTracker.addMovement(motionEvent);
                        this.mScroller.computeScrollOffset();
                        this.mIsBeingDragged = !this.mScroller.isFinished();
                        if (!this.mScroller.isFinished()) {
                            this.mScroller.abortAnimation();
                            break;
                        }
                    }
                    break;
                case 1:
                case 3:
                    this.mIsBeingDragged = false;
                    recycleVelocityTracker();
                    this.mActivePointerId = -1;
                    break;
                case 2:
                    int i = this.mActivePointerId;
                    if (i != -1) {
                        if (motionEvent.findPointerIndex(i) == -1) {
                            Log.e(TAG, "Invalid pointerId=" + i + " in onInterceptTouchEvent");
                            break;
                        } else {
                            initVelocityTrackerIfNotExists();
                            this.mVelocityTracker.addMovement(motionEvent);
                            int y2 = (int) motionEvent.getY();
                            if (Math.abs(y2 - this.mLastMotionY) > this.mTouchSlop) {
                                this.mIsBeingDragged = true;
                                this.mLastMotionY = y2;
                                ViewParent parent = getParent();
                                if (parent != null) {
                                    parent.requestDisallowInterceptTouchEvent(true);
                                    break;
                                }
                            }
                        }
                    }
                    break;
            }
        } else {
            onSecondaryPointerUp(motionEvent);
        }
        return this.mIsBeingDragged;
    }

    private boolean inChild(int i, int i2) {
        if (getChildCount() <= 0) {
            return false;
        }
        int scrollY = getScrollY();
        View childAt = getChildAt(0);
        return i2 >= childAt.getTop() - scrollY && i2 < childAt.getBottom() - scrollY && i >= childAt.getLeft() && i < childAt.getRight();
    }

    private void onSecondaryPointerUp(MotionEvent motionEvent) {
        int action = (motionEvent.getAction() & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
        if (motionEvent.getPointerId(action) == this.mActivePointerId) {
            int i = action == 0 ? 1 : 0;
            this.mLastMotionY = (int) motionEvent.getY(i);
            this.mActivePointerId = motionEvent.getPointerId(i);
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.clear();
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        ViewParent parent;
        View childAt = getChildAt(0);
        Rect rect = new Rect();
        childAt.getGlobalVisibleRect(rect);
        int[] iArr = new int[2];
        getLocationOnScreen(iArr);
        if (motionEvent.getAction() == 0) {
            if (this.mOrientation == 2 && !rect.contains(iArr[0] - ((int) motionEvent.getY()), iArr[1] + ((int) motionEvent.getX()))) {
                return false;
            }
            if (this.mOrientation == 1 && !rect.contains(iArr[0] + ((int) motionEvent.getX()), iArr[1] + ((int) motionEvent.getY()))) {
                return false;
            }
        }
        switch (motionEvent.getActionMasked()) {
            case 0:
                if (getChildCount() == 0) {
                    return false;
                }
                this.mIsBeingDragged = !this.mScroller.isFinished();
                if (this.mIsBeingDragged && (parent = getParent()) != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }
                if (!this.mScroller.isFinished()) {
                    this.mScroller.abortAnimation();
                }
                this.mLastMotionY = (int) motionEvent.getY();
                this.mActivePointerId = motionEvent.getPointerId(0);
                initOrResetVelocityTracker();
                this.mVelocityTracker.addMovement(motionEvent);
                return true;
            case 1:
            case 3:
                this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
                if (this.mIsBeingDragged) {
                    float f = -this.mVelocityTracker.getYVelocity(this.mActivePointerId);
                    this.mDirection = f > 0.0f ? DIRECTION.UP : DIRECTION.DOWN;
                    if (Math.abs(f) > this.mMinimumVelocity) {
                        flingWithDispatch((int) f);
                    }
                }
                endDrag();
                this.mActivePointerId = -1;
                if (this.mDirection == DIRECTION.DOWN && getScrollY() < 0 && Math.abs(-this.mVelocityTracker.getYVelocity()) > 2 * this.mMinimumVelocity) {
                    scrollToExit();
                    return true;
                }
                if (getScrollY() < 0) {
                    completeMove();
                }
                recycleVelocityTracker();
                return true;
            case 2:
                int iFindPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                if (iFindPointerIndex == -1) {
                    Log.e(TAG, "Invalid pointerId=" + this.mActivePointerId + " in onTouchEvent");
                } else {
                    initVelocityTrackerIfNotExists();
                    this.mVelocityTracker.addMovement(motionEvent);
                    int y = (int) motionEvent.getY(iFindPointerIndex);
                    int i = y - this.mLastMotionY;
                    if (!this.mIsBeingDragged && Math.abs(i) > this.mTouchSlop) {
                        ViewParent parent2 = getParent();
                        if (parent2 != null) {
                            parent2.requestDisallowInterceptTouchEvent(true);
                        }
                        this.mIsBeingDragged = true;
                        i = i > 0 ? i - this.mTouchSlop : i + this.mTouchSlop;
                    }
                    int scrollY = getScrollY() - i;
                    if (this.mIsBeingDragged) {
                        this.currentStatus = Status.MOVING;
                        if (scrollY >= getChildAt(0).getMeasuredHeight() - this.mSettingDefaultHeight) {
                            scrollTo(0, getChildAt(0).getMeasuredHeight() - this.mSettingDefaultHeight);
                        } else {
                            scrollTo(0, scrollY);
                            this.mLastMotionY = y;
                        }
                    }
                }
                return true;
            case 4:
            default:
                return true;
            case 5:
                int actionIndex = motionEvent.getActionIndex();
                this.mLastMotionY = (int) motionEvent.getY(actionIndex);
                this.mActivePointerId = motionEvent.getPointerId(actionIndex);
                return true;
            case 6:
                onSecondaryPointerUp(motionEvent);
                this.mLastMotionY = (int) motionEvent.getY(motionEvent.findPointerIndex(this.mActivePointerId));
                return true;
        }
    }

    private void completeMove() {
        if (getScrollY() <= (-this.mSettingDefaultHeight) / 2) {
            scrollToExit();
        } else {
            scrollToOpen();
        }
    }

    public void scrollToExit() {
        int i = -(this.mSettingDefaultHeight + getScrollY());
        if (i == 0) {
            return;
        }
        this.currentStatus = Status.CLOSING;
        this.mScroller.startScroll(0, getScrollY(), 0, i, 200);
        invalidate();
    }

    public void scrollToOpen() {
        int i = -getScrollY();
        if (i == 0) {
            return;
        }
        this.currentStatus = Status.OPENING;
        this.mScroller.startScroll(0, getScrollY(), 0, i, 200);
        invalidate();
    }

    private void flingWithDispatch(int i) {
        if ((getScrollY() > 0 || i > 0) && (getScrollY() < getScrollRange() || i < 0)) {
            fling(i);
        }
    }

    private void fling(int i) {
        if (getChildCount() > 0) {
            this.mScroller.fling(0, getScrollY(), 0, i, 0, 0, 0, Math.max(0, getChildAt(0).getHeight() - this.mSettingDefaultHeight), 0, 0);
            postInvalidateOnAnimation();
        }
    }

    private void endDrag() {
        this.mIsBeingDragged = false;
    }

    private void initOrResetVelocityTracker() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            this.mVelocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    @Override // android.view.View
    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            scrollTo(0, this.mScroller.getCurrY());
            invalidate();
        }
    }

    private int getScrollRange() {
        if (getChildCount() > 0) {
            return Math.max(0, getChildAt(0).getHeight() - this.mSettingDefaultHeight);
        }
        return 0;
    }

    @Override // android.view.View
    public void scrollTo(@Px int i, @Px int i2) {
        float f;
        if (i2 >= this.mChildHeight - this.mSettingDefaultHeight) {
            i2 = this.mChildHeight - this.mSettingDefaultHeight;
        }
        super.scrollTo(i, i2);
        if (CamLog.VERBOSE) {
            CamLog.d("x, y:" + i + ", " + i2);
        }
        if (isPortrait()) {
            f = this.mParams.maxHeightPortrait;
        } else {
            f = this.mParams.maxHeightLandscape;
        }
        if (i2 >= 0) {
            onScrollListener(i2 / (f - this.mSettingDefaultHeight));
        } else {
            onScrollListener(i2 / this.mSettingDefaultHeight);
        }
        float f2 = i2;
        if (f2 < f - this.mSettingDefaultHeight || (i2 == 0 && f - this.mSettingDefaultHeight == 0.0f)) {
            if (i2 <= 0 || f2 >= f - this.mSettingDefaultHeight) {
                if (i2 == 0) {
                    if (this.currentStatus != Status.OPENED) {
                        this.currentStatus = Status.OPENED;
                        onScrollFinished(Status.OPENED);
                    }
                } else if (i2 == (-this.mSettingDefaultHeight) && this.currentStatus != Status.EXIT) {
                    this.currentStatus = Status.EXIT;
                    onScrollFinished(Status.EXIT);
                }
            } else if (this.currentStatus != Status.IDLE) {
                this.currentStatus = Status.IDLE;
            }
        } else if (this.currentStatus != Status.FULLSCREEN) {
            this.currentStatus = Status.FULLSCREEN;
            onScrollFinished(Status.FULLSCREEN);
        }
        if (f2 >= f - this.mSettingDefaultHeight) {
            this.offsetY = (int) (getScrollY() - (f - this.mSettingDefaultHeight));
            if (this.offsetY >= getChildAt(0).getHeight() - this.mParams.maxHeightPortrait) {
                this.offsetY = getChildAt(0).getHeight() - this.mParams.maxHeightPortrait;
            }
        }
        if (CamLog.VERBOSE) {
            CamLog.d("current status:" + this.currentStatus);
        }
    }

    private void onScrollListener(float f) {
        if (this.mOnScrollListener != null) {
            if (f >= 1.0d) {
                f = 1.0f;
            }
            this.mOnScrollListener.onScrollProgressChanged(f);
        }
    }

    public void setOrientation(int i) {
        this.mOrientation = i;
        noAnimationFullScreen();
    }

    public void setSettingDefaultHeight(int i) {
        this.mSettingDefaultHeight = i;
    }

    public void setSettingMenuParams(AbsSelectableDialog.Params params) {
        this.mParams = params;
    }

    private boolean isPortrait() {
        return this.mOrientation == 1;
    }

    public Status getCurrentStatus() {
        switch (this.currentStatus) {
            case FULLSCREEN:
                return Status.FULLSCREEN;
            case OPENED:
                return Status.OPENED;
            case EXIT:
                return Status.EXIT;
            case IDLE:
                return Status.IDLE;
            case MOVING:
                return Status.MOVING;
            case OPENING:
                return Status.OPENING;
            case CLOSING:
                return Status.CLOSING;
            default:
                return Status.OPENED;
        }
    }

    private void onScrollFinished(Status status) {
        if (this.mOnScrollListener != null) {
            this.mOnScrollListener.onScrollFinished(status);
        }
    }

    public void noAnimationFullScreen() {
        if (this.currentStatus == Status.FULLSCREEN) {
            if (isPortrait()) {
                setViewMargin(this.mParams.maxHeightPortrait - this.mSettingDefaultHeight);
                scrollTo(0, (this.mParams.maxHeightPortrait - this.mSettingDefaultHeight) + this.offsetY);
            } else {
                setViewMargin(this.mParams.maxHeightLandscape - this.mSettingDefaultHeight);
                scrollTo(0, (this.mParams.maxHeightLandscape - this.mSettingDefaultHeight) + this.offsetY);
            }
        }
        if (this.currentStatus == Status.IDLE) {
            if (this.mScroller != null && !this.mScroller.isFinished()) {
                this.mScroller.forceFinished(true);
            }
            scrollTo(0, 0);
        }
    }

    public void setChildHeight(int i) {
        this.mChildHeight = i;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.mOnScrollListener = onScrollListener;
    }

    public void setCurrentStatus(Status status) {
        this.currentStatus = status;
    }

    public void setViewMargin(int i) {
        View childAt;
        if (getChildCount() <= 0 || (childAt = getChildAt(0)) == null) {
            return;
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
        layoutParams.height = this.mChildHeight;
        layoutParams.setMargins(0, i, 0, 0);
    }

    public int getScrolledHeight() {
        return this.mSettingDefaultHeight + getScrollY();
    }
}
