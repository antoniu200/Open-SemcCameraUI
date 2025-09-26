// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.selectabledialog;

import android.widget.FrameLayout$LayoutParams;
import com.sonyericsson.android.camera.util.CamLog;
import android.support.annotation.Px;
import android.graphics.Rect;
import android.view.ViewParent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.annotation.AttrRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.support.annotation.NonNull;
import android.view.ViewConfiguration;
import android.view.VelocityTracker;
import android.widget.OverScroller;
import android.content.Context;
import android.widget.FrameLayout;

public class ScrollContainer extends FrameLayout
{
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
    
    public ScrollContainer(@NonNull final Context context) {
        super(context);
        this.currentStatus = Status.OPENED;
        this.mOnScrollListener = null;
        this.mActivePointerId = -1;
        this.init();
    }
    
    public ScrollContainer(@NonNull final Context context, @Nullable final AttributeSet set) {
        super(context, set);
        this.currentStatus = Status.OPENED;
        this.mOnScrollListener = null;
        this.mActivePointerId = -1;
        this.init();
    }
    
    public ScrollContainer(@NonNull final Context context, @Nullable final AttributeSet set, @AttrRes final int n) {
        super(context, set, n);
        this.currentStatus = Status.OPENED;
        this.mOnScrollListener = null;
        this.mActivePointerId = -1;
        this.init();
    }
    
    private void completeMove() {
        if (this.getScrollY() <= -this.mSettingDefaultHeight / 2) {
            this.scrollToExit();
        }
        else {
            this.scrollToOpen();
        }
    }
    
    private void endDrag() {
        this.mIsBeingDragged = false;
    }
    
    private void fling(final int n) {
        if (this.getChildCount() > 0) {
            this.mScroller.fling(0, this.getScrollY(), 0, n, 0, 0, 0, Math.max(0, this.getChildAt(0).getHeight() - this.mSettingDefaultHeight), 0, 0);
            this.postInvalidateOnAnimation();
        }
    }
    
    private void flingWithDispatch(final int n) {
        if ((this.getScrollY() > 0 || n > 0) && (this.getScrollY() < this.getScrollRange() || n < 0)) {
            this.fling(n);
        }
    }
    
    private int getScrollRange() {
        final int childCount = this.getChildCount();
        int max = 0;
        if (childCount > 0) {
            max = Math.max(0, this.getChildAt(0).getHeight() - this.mSettingDefaultHeight);
        }
        return max;
    }
    
    private boolean inChild(final int n, final int n2) {
        final int childCount = this.getChildCount();
        final boolean b = false;
        if (childCount > 0) {
            final int scrollY = this.getScrollY();
            final View child = this.getChildAt(0);
            boolean b2 = b;
            if (n2 >= child.getTop() - scrollY) {
                b2 = b;
                if (n2 < child.getBottom() - scrollY) {
                    b2 = b;
                    if (n >= child.getLeft()) {
                        b2 = b;
                        if (n < child.getRight()) {
                            b2 = true;
                        }
                    }
                }
            }
            return b2;
        }
        return false;
    }
    
    private void init() {
        this.mContext = this.getContext();
        this.mViewConfiguration = ViewConfiguration.get(this.mContext);
        this.mTouchSlop = this.mViewConfiguration.getScaledTouchSlop();
        this.mMinimumVelocity = this.mViewConfiguration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = this.mViewConfiguration.getScaledMaximumFlingVelocity();
        this.mScroller = new OverScroller(this.mContext);
    }
    
    private void initOrResetVelocityTracker() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        else {
            this.mVelocityTracker.clear();
        }
    }
    
    private void initVelocityTrackerIfNotExists() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
    }
    
    private boolean isPortrait() {
        final int mOrientation = this.mOrientation;
        boolean b = true;
        if (mOrientation != 1) {
            b = false;
        }
        return b;
    }
    
    private void onScrollFinished(final Status status) {
        if (this.mOnScrollListener != null) {
            this.mOnScrollListener.onScrollFinished(status);
        }
    }
    
    private void onScrollListener(final float n) {
        if (this.mOnScrollListener != null) {
            float n2 = n;
            if (n >= 1.0) {
                n2 = 1.0f;
            }
            this.mOnScrollListener.onScrollProgressChanged(n2);
        }
    }
    
    private void onSecondaryPointerUp(final MotionEvent motionEvent) {
        final int n = (motionEvent.getAction() & 0xFF00) >> 8;
        if (motionEvent.getPointerId(n) == this.mActivePointerId) {
            int n2;
            if (n == 0) {
                n2 = 1;
            }
            else {
                n2 = 0;
            }
            this.mLastMotionY = (int)motionEvent.getY(n2);
            this.mActivePointerId = motionEvent.getPointerId(n2);
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.clear();
            }
        }
    }
    
    private void recycleVelocityTracker() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }
    
    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            this.scrollTo(0, this.mScroller.getCurrY());
            this.invalidate();
        }
    }
    
    public Status getCurrentStatus() {
        switch (ScrollContainer$1.$SwitchMap$com$sonyericsson$android$camera$view$selectabledialog$ScrollContainer$Status[this.currentStatus.ordinal()]) {
            default: {
                return Status.OPENED;
            }
            case 7: {
                return Status.CLOSING;
            }
            case 6: {
                return Status.OPENING;
            }
            case 5: {
                return Status.MOVING;
            }
            case 4: {
                return Status.IDLE;
            }
            case 3: {
                return Status.EXIT;
            }
            case 2: {
                return Status.OPENED;
            }
            case 1: {
                return Status.FULLSCREEN;
            }
        }
    }
    
    public int getScrolledHeight() {
        return this.mSettingDefaultHeight + this.getScrollY();
    }
    
    public void noAnimationFullScreen() {
        if (this.currentStatus == Status.FULLSCREEN) {
            if (this.isPortrait()) {
                this.setViewMargin(this.mParams.maxHeightPortrait - this.mSettingDefaultHeight);
                this.scrollTo(0, this.mParams.maxHeightPortrait - this.mSettingDefaultHeight + this.offsetY);
            }
            else {
                this.setViewMargin(this.mParams.maxHeightLandscape - this.mSettingDefaultHeight);
                this.scrollTo(0, this.mParams.maxHeightLandscape - this.mSettingDefaultHeight + this.offsetY);
            }
        }
        if (this.currentStatus == Status.IDLE) {
            if (this.mScroller != null && !this.mScroller.isFinished()) {
                this.mScroller.forceFinished(true);
            }
            this.scrollTo(0, 0);
        }
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        if (motionEvent.getAction() == 2 && this.mIsBeingDragged) {
            return true;
        }
        if (super.onInterceptTouchEvent(motionEvent)) {
            return true;
        }
        final int n = motionEvent.getAction() & 0xFF;
        if (n != 6) {
            switch (n) {
                case 2: {
                    final int mActivePointerId = this.mActivePointerId;
                    if (mActivePointerId == -1) {
                        break;
                    }
                    if (motionEvent.findPointerIndex(mActivePointerId) == -1) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Invalid pointerId=");
                        sb.append(mActivePointerId);
                        sb.append(" in onInterceptTouchEvent");
                        Log.e("ScrollContainer", sb.toString());
                        break;
                    }
                    this.initVelocityTrackerIfNotExists();
                    this.mVelocityTracker.addMovement(motionEvent);
                    final int mLastMotionY = (int)motionEvent.getY();
                    if (Math.abs(mLastMotionY - this.mLastMotionY) <= this.mTouchSlop) {
                        break;
                    }
                    this.mIsBeingDragged = true;
                    this.mLastMotionY = mLastMotionY;
                    final ViewParent parent = this.getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                        break;
                    }
                    break;
                }
                case 1:
                case 3: {
                    this.mIsBeingDragged = false;
                    this.recycleVelocityTracker();
                    this.mActivePointerId = -1;
                    break;
                }
                case 0: {
                    final int mLastMotionY2 = (int)motionEvent.getY();
                    if (!this.inChild((int)motionEvent.getX(), mLastMotionY2)) {
                        this.mIsBeingDragged = false;
                        this.recycleVelocityTracker();
                        break;
                    }
                    this.mActivePointerId = motionEvent.getPointerId(0);
                    this.mLastMotionY = mLastMotionY2;
                    this.initOrResetVelocityTracker();
                    this.mVelocityTracker.addMovement(motionEvent);
                    this.mScroller.computeScrollOffset();
                    this.mIsBeingDragged = (this.mScroller.isFinished() ^ true);
                    if (!this.mScroller.isFinished()) {
                        this.mScroller.abortAnimation();
                        break;
                    }
                    break;
                }
            }
        }
        else {
            this.onSecondaryPointerUp(motionEvent);
        }
        return this.mIsBeingDragged;
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        final View child = this.getChildAt(0);
        final Rect rect = new Rect();
        child.getGlobalVisibleRect(rect);
        final int[] array = new int[2];
        this.getLocationOnScreen(array);
        if (motionEvent.getAction() == 0) {
            if (this.mOrientation == 2 && !rect.contains(array[0] - (int)motionEvent.getY(), array[1] + (int)motionEvent.getX())) {
                return false;
            }
            if (this.mOrientation == 1 && !rect.contains(array[0] + (int)motionEvent.getX(), array[1] + (int)motionEvent.getY())) {
                return false;
            }
        }
        switch (motionEvent.getActionMasked()) {
            case 6: {
                this.onSecondaryPointerUp(motionEvent);
                this.mLastMotionY = (int)motionEvent.getY(motionEvent.findPointerIndex(this.mActivePointerId));
                break;
            }
            case 5: {
                final int actionIndex = motionEvent.getActionIndex();
                this.mLastMotionY = (int)motionEvent.getY(actionIndex);
                this.mActivePointerId = motionEvent.getPointerId(actionIndex);
                break;
            }
            case 2: {
                final int pointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                if (pointerIndex == -1) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Invalid pointerId=");
                    sb.append(this.mActivePointerId);
                    sb.append(" in onTouchEvent");
                    Log.e("ScrollContainer", sb.toString());
                    break;
                }
                this.initVelocityTrackerIfNotExists();
                this.mVelocityTracker.addMovement(motionEvent);
                final int mLastMotionY = (int)motionEvent.getY(pointerIndex);
                int n;
                final int a = n = mLastMotionY - this.mLastMotionY;
                if (!this.mIsBeingDragged) {
                    n = a;
                    if (Math.abs(a) > this.mTouchSlop) {
                        final ViewParent parent = this.getParent();
                        if (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(true);
                        }
                        this.mIsBeingDragged = true;
                        if (a > 0) {
                            n = a - this.mTouchSlop;
                        }
                        else {
                            n = a + this.mTouchSlop;
                        }
                    }
                }
                final int n2 = this.getScrollY() - n;
                if (!this.mIsBeingDragged) {
                    break;
                }
                this.currentStatus = Status.MOVING;
                if (n2 >= this.getChildAt(0).getMeasuredHeight() - this.mSettingDefaultHeight) {
                    this.scrollTo(0, this.getChildAt(0).getMeasuredHeight() - this.mSettingDefaultHeight);
                    break;
                }
                this.scrollTo(0, n2);
                this.mLastMotionY = mLastMotionY;
                break;
            }
            case 1:
            case 3: {
                this.mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
                if (this.mIsBeingDragged) {
                    final float a2 = -this.mVelocityTracker.getYVelocity(this.mActivePointerId);
                    DIRECTION mDirection;
                    if (a2 > 0.0f) {
                        mDirection = DIRECTION.UP;
                    }
                    else {
                        mDirection = DIRECTION.DOWN;
                    }
                    this.mDirection = mDirection;
                    if (Math.abs(a2) > this.mMinimumVelocity) {
                        this.flingWithDispatch((int)a2);
                    }
                }
                this.endDrag();
                this.mActivePointerId = -1;
                if (this.mDirection == DIRECTION.DOWN && this.getScrollY() < 0 && Math.abs(-this.mVelocityTracker.getYVelocity()) > 2 * this.mMinimumVelocity) {
                    this.scrollToExit();
                    return true;
                }
                if (this.getScrollY() < 0) {
                    this.completeMove();
                }
                this.recycleVelocityTracker();
                break;
            }
            case 0: {
                if (this.getChildCount() == 0) {
                    return false;
                }
                this.mIsBeingDragged = (this.mScroller.isFinished() ^ true);
                if (this.mIsBeingDragged) {
                    final ViewParent parent2 = this.getParent();
                    if (parent2 != null) {
                        parent2.requestDisallowInterceptTouchEvent(true);
                    }
                }
                if (!this.mScroller.isFinished()) {
                    this.mScroller.abortAnimation();
                }
                this.mLastMotionY = (int)motionEvent.getY();
                this.mActivePointerId = motionEvent.getPointerId(0);
                this.initOrResetVelocityTracker();
                this.mVelocityTracker.addMovement(motionEvent);
                break;
            }
        }
        return true;
    }
    
    public void scrollTo(@Px final int i, @Px final int n) {
        int j = n;
        if (n >= this.mChildHeight - this.mSettingDefaultHeight) {
            j = this.mChildHeight - this.mSettingDefaultHeight;
        }
        super.scrollTo(i, j);
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("x, y:");
            sb.append(i);
            sb.append(", ");
            sb.append(j);
            CamLog.d(sb.toString());
        }
        float n2;
        if (this.isPortrait()) {
            n2 = (float)this.mParams.maxHeightPortrait;
        }
        else {
            n2 = (float)this.mParams.maxHeightLandscape;
        }
        if (j >= 0) {
            this.onScrollListener(j / (n2 - this.mSettingDefaultHeight));
        }
        else {
            this.onScrollListener(j / (float)this.mSettingDefaultHeight);
        }
        final float n3 = (float)j;
        if (n3 >= n2 - this.mSettingDefaultHeight && (j != 0 || n2 - this.mSettingDefaultHeight != 0.0f)) {
            if (this.currentStatus != Status.FULLSCREEN) {
                this.currentStatus = Status.FULLSCREEN;
                this.onScrollFinished(Status.FULLSCREEN);
            }
        }
        else if (j > 0 && n3 < n2 - this.mSettingDefaultHeight) {
            if (this.currentStatus != Status.IDLE) {
                this.currentStatus = Status.IDLE;
            }
        }
        else if (j == 0) {
            if (this.currentStatus != Status.OPENED) {
                this.currentStatus = Status.OPENED;
                this.onScrollFinished(Status.OPENED);
            }
        }
        else if (j == -this.mSettingDefaultHeight && this.currentStatus != Status.EXIT) {
            this.currentStatus = Status.EXIT;
            this.onScrollFinished(Status.EXIT);
        }
        if (n3 >= n2 - this.mSettingDefaultHeight) {
            this.offsetY = (int)(this.getScrollY() - (n2 - this.mSettingDefaultHeight));
            if (this.offsetY >= this.getChildAt(0).getHeight() - this.mParams.maxHeightPortrait) {
                this.offsetY = this.getChildAt(0).getHeight() - this.mParams.maxHeightPortrait;
            }
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("current status:");
            sb2.append(this.currentStatus);
            CamLog.d(sb2.toString());
        }
    }
    
    public void scrollToExit() {
        final int n = -(this.mSettingDefaultHeight + this.getScrollY());
        if (n == 0) {
            return;
        }
        this.currentStatus = Status.CLOSING;
        this.mScroller.startScroll(0, this.getScrollY(), 0, n, 200);
        this.invalidate();
    }
    
    public void scrollToOpen() {
        final int n = -this.getScrollY();
        if (n == 0) {
            return;
        }
        this.currentStatus = Status.OPENING;
        this.mScroller.startScroll(0, this.getScrollY(), 0, n, 200);
        this.invalidate();
    }
    
    public void setChildHeight(final int mChildHeight) {
        this.mChildHeight = mChildHeight;
    }
    
    public void setCurrentStatus(final Status currentStatus) {
        this.currentStatus = currentStatus;
    }
    
    public void setOnScrollListener(final OnScrollListener mOnScrollListener) {
        this.mOnScrollListener = mOnScrollListener;
    }
    
    public void setOrientation(final int mOrientation) {
        this.mOrientation = mOrientation;
        this.noAnimationFullScreen();
    }
    
    public void setSettingDefaultHeight(final int mSettingDefaultHeight) {
        this.mSettingDefaultHeight = mSettingDefaultHeight;
    }
    
    public void setSettingMenuParams(final AbsSelectableDialog.Params mParams) {
        this.mParams = mParams;
    }
    
    public void setViewMargin(final int n) {
        if (this.getChildCount() > 0) {
            final View child = this.getChildAt(0);
            if (child != null) {
                final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)child.getLayoutParams();
                frameLayout$LayoutParams.height = this.mChildHeight;
                frameLayout$LayoutParams.setMargins(0, n, 0, 0);
            }
        }
    }
    
    public boolean shouldDelayChildPressedState() {
        return true;
    }
    
    enum DIRECTION
    {
        private static final DIRECTION[] $VALUES;
        
        DOWN, 
        UP;
        
        static {
            $VALUES = new DIRECTION[] { DIRECTION.UP, DIRECTION.DOWN };
        }
    }
    
    public interface OnScrollListener
    {
        void onScrollFinished(final Status p0);
        
        void onScrollProgressChanged(final float p0);
    }
    
    public enum Status
    {
        private static final Status[] $VALUES;
        
        CLOSING, 
        EXIT, 
        FULLSCREEN, 
        IDLE, 
        MOVING, 
        OPENED, 
        OPENING;
        
        static {
            $VALUES = new Status[] { Status.IDLE, Status.EXIT, Status.OPENED, Status.FULLSCREEN, Status.MOVING, Status.OPENING, Status.CLOSING };
        }
    }
}
