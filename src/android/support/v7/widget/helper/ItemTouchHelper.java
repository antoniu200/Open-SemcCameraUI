package android.support.v7.widget.helper;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.recyclerview.R;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ItemTouchHelper extends RecyclerView.ItemDecoration implements RecyclerView.OnChildAttachStateChangeListener {
    static final int ACTION_MODE_DRAG_MASK = 16711680;
    private static final int ACTION_MODE_IDLE_MASK = 255;
    static final int ACTION_MODE_SWIPE_MASK = 65280;
    public static final int ACTION_STATE_DRAG = 2;
    public static final int ACTION_STATE_IDLE = 0;
    public static final int ACTION_STATE_SWIPE = 1;
    private static final int ACTIVE_POINTER_ID_NONE = -1;
    public static final int ANIMATION_TYPE_DRAG = 8;
    public static final int ANIMATION_TYPE_SWIPE_CANCEL = 4;
    public static final int ANIMATION_TYPE_SWIPE_SUCCESS = 2;
    private static final boolean DEBUG = false;
    static final int DIRECTION_FLAG_COUNT = 8;
    public static final int DOWN = 2;
    public static final int END = 32;
    public static final int LEFT = 4;
    private static final int PIXELS_PER_SECOND = 1000;
    public static final int RIGHT = 8;
    public static final int START = 16;
    private static final String TAG = "ItemTouchHelper";
    public static final int UP = 1;

    @NonNull
    Callback mCallback;
    private List<Integer> mDistances;
    private long mDragScrollStartTimeInMs;
    float mDx;
    float mDy;
    GestureDetectorCompat mGestureDetector;
    float mInitialTouchX;
    float mInitialTouchY;
    private ItemTouchHelperGestureListener mItemTouchHelperGestureListener;
    private float mMaxSwipeVelocity;
    RecyclerView mRecyclerView;
    int mSelectedFlags;
    private float mSelectedStartX;
    private float mSelectedStartY;
    private int mSlop;
    private List<RecyclerView.ViewHolder> mSwapTargets;
    private float mSwipeEscapeVelocity;
    private Rect mTmpRect;
    VelocityTracker mVelocityTracker;
    final List<View> mPendingCleanup = new ArrayList();
    private final float[] mTmpPosition = new float[2];
    RecyclerView.ViewHolder mSelected = null;
    int mActivePointerId = -1;
    private int mActionState = 0;
    List<RecoverAnimation> mRecoverAnimations = new ArrayList();
    final Runnable mScrollRunnable = new Runnable() { // from class: android.support.v7.widget.helper.ItemTouchHelper.1
        @Override // java.lang.Runnable
        public void run() {
            if (ItemTouchHelper.this.mSelected == null || !ItemTouchHelper.this.scrollIfNecessary()) {
                return;
            }
            if (ItemTouchHelper.this.mSelected != null) {
                ItemTouchHelper.this.moveIfNecessary(ItemTouchHelper.this.mSelected);
            }
            ItemTouchHelper.this.mRecyclerView.removeCallbacks(ItemTouchHelper.this.mScrollRunnable);
            ViewCompat.postOnAnimation(ItemTouchHelper.this.mRecyclerView, this);
        }
    };
    private RecyclerView.ChildDrawingOrderCallback mChildDrawingOrderCallback = null;
    View mOverdrawChild = null;
    int mOverdrawChildPosition = -1;
    private final RecyclerView.OnItemTouchListener mOnItemTouchListener = new RecyclerView.OnItemTouchListener() { // from class: android.support.v7.widget.helper.ItemTouchHelper.2
        @Override // android.support.v7.widget.RecyclerView.OnItemTouchListener
        public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
            int iFindPointerIndex;
            RecoverAnimation recoverAnimationFindAnimation;
            ItemTouchHelper.this.mGestureDetector.onTouchEvent(motionEvent);
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 0) {
                ItemTouchHelper.this.mActivePointerId = motionEvent.getPointerId(0);
                ItemTouchHelper.this.mInitialTouchX = motionEvent.getX();
                ItemTouchHelper.this.mInitialTouchY = motionEvent.getY();
                ItemTouchHelper.this.obtainVelocityTracker();
                if (ItemTouchHelper.this.mSelected == null && (recoverAnimationFindAnimation = ItemTouchHelper.this.findAnimation(motionEvent)) != null) {
                    ItemTouchHelper.this.mInitialTouchX -= recoverAnimationFindAnimation.mX;
                    ItemTouchHelper.this.mInitialTouchY -= recoverAnimationFindAnimation.mY;
                    ItemTouchHelper.this.endRecoverAnimation(recoverAnimationFindAnimation.mViewHolder, true);
                    if (ItemTouchHelper.this.mPendingCleanup.remove(recoverAnimationFindAnimation.mViewHolder.itemView)) {
                        ItemTouchHelper.this.mCallback.clearView(ItemTouchHelper.this.mRecyclerView, recoverAnimationFindAnimation.mViewHolder);
                    }
                    ItemTouchHelper.this.select(recoverAnimationFindAnimation.mViewHolder, recoverAnimationFindAnimation.mActionState);
                    ItemTouchHelper.this.updateDxDy(motionEvent, ItemTouchHelper.this.mSelectedFlags, 0);
                }
            } else if (actionMasked == 3 || actionMasked == 1) {
                ItemTouchHelper.this.mActivePointerId = -1;
                ItemTouchHelper.this.select(null, 0);
            } else if (ItemTouchHelper.this.mActivePointerId != -1 && (iFindPointerIndex = motionEvent.findPointerIndex(ItemTouchHelper.this.mActivePointerId)) >= 0) {
                ItemTouchHelper.this.checkSelectForSwipe(actionMasked, motionEvent, iFindPointerIndex);
            }
            if (ItemTouchHelper.this.mVelocityTracker != null) {
                ItemTouchHelper.this.mVelocityTracker.addMovement(motionEvent);
            }
            return ItemTouchHelper.this.mSelected != null;
        }

        @Override // android.support.v7.widget.RecyclerView.OnItemTouchListener
        public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
            ItemTouchHelper.this.mGestureDetector.onTouchEvent(motionEvent);
            if (ItemTouchHelper.this.mVelocityTracker != null) {
                ItemTouchHelper.this.mVelocityTracker.addMovement(motionEvent);
            }
            if (ItemTouchHelper.this.mActivePointerId == -1) {
                return;
            }
            int actionMasked = motionEvent.getActionMasked();
            int iFindPointerIndex = motionEvent.findPointerIndex(ItemTouchHelper.this.mActivePointerId);
            if (iFindPointerIndex >= 0) {
                ItemTouchHelper.this.checkSelectForSwipe(actionMasked, motionEvent, iFindPointerIndex);
            }
            RecyclerView.ViewHolder viewHolder = ItemTouchHelper.this.mSelected;
            if (viewHolder == null) {
                return;
            }
            if (actionMasked != 6) {
                switch (actionMasked) {
                    case 1:
                        break;
                    case 2:
                        if (iFindPointerIndex >= 0) {
                            ItemTouchHelper.this.updateDxDy(motionEvent, ItemTouchHelper.this.mSelectedFlags, iFindPointerIndex);
                            ItemTouchHelper.this.moveIfNecessary(viewHolder);
                            ItemTouchHelper.this.mRecyclerView.removeCallbacks(ItemTouchHelper.this.mScrollRunnable);
                            ItemTouchHelper.this.mScrollRunnable.run();
                            ItemTouchHelper.this.mRecyclerView.invalidate();
                            return;
                        }
                        return;
                    case 3:
                        if (ItemTouchHelper.this.mVelocityTracker != null) {
                            ItemTouchHelper.this.mVelocityTracker.clear();
                            break;
                        }
                        break;
                    default:
                        return;
                }
                ItemTouchHelper.this.select(null, 0);
                ItemTouchHelper.this.mActivePointerId = -1;
                return;
            }
            int actionIndex = motionEvent.getActionIndex();
            if (motionEvent.getPointerId(actionIndex) == ItemTouchHelper.this.mActivePointerId) {
                ItemTouchHelper.this.mActivePointerId = motionEvent.getPointerId(actionIndex == 0 ? 1 : 0);
                ItemTouchHelper.this.updateDxDy(motionEvent, ItemTouchHelper.this.mSelectedFlags, actionIndex);
            }
        }

        @Override // android.support.v7.widget.RecyclerView.OnItemTouchListener
        public void onRequestDisallowInterceptTouchEvent(boolean z) {
            if (z) {
                ItemTouchHelper.this.select(null, 0);
            }
        }
    };

    public interface ViewDropHandler {
        void prepareForDrop(@NonNull View view, @NonNull View view2, int i, int i2);
    }

    @Override // android.support.v7.widget.RecyclerView.OnChildAttachStateChangeListener
    public void onChildViewAttachedToWindow(@NonNull View view) {
    }

    public ItemTouchHelper(@NonNull Callback callback) {
        this.mCallback = callback;
    }

    private static boolean hitTest(View view, float f, float f2, float f3, float f4) {
        return f >= f3 && f <= f3 + ((float) view.getWidth()) && f2 >= f4 && f2 <= f4 + ((float) view.getHeight());
    }

    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) {
        if (this.mRecyclerView == recyclerView) {
            return;
        }
        if (this.mRecyclerView != null) {
            destroyCallbacks();
        }
        this.mRecyclerView = recyclerView;
        if (recyclerView != null) {
            Resources resources = recyclerView.getResources();
            this.mSwipeEscapeVelocity = resources.getDimension(R.dimen.item_touch_helper_swipe_escape_velocity);
            this.mMaxSwipeVelocity = resources.getDimension(R.dimen.item_touch_helper_swipe_escape_max_velocity);
            setupCallbacks();
        }
    }

    private void setupCallbacks() {
        this.mSlop = ViewConfiguration.get(this.mRecyclerView.getContext()).getScaledTouchSlop();
        this.mRecyclerView.addItemDecoration(this);
        this.mRecyclerView.addOnItemTouchListener(this.mOnItemTouchListener);
        this.mRecyclerView.addOnChildAttachStateChangeListener(this);
        startGestureDetection();
    }

    private void destroyCallbacks() {
        this.mRecyclerView.removeItemDecoration(this);
        this.mRecyclerView.removeOnItemTouchListener(this.mOnItemTouchListener);
        this.mRecyclerView.removeOnChildAttachStateChangeListener(this);
        for (int size = this.mRecoverAnimations.size() - 1; size >= 0; size--) {
            this.mCallback.clearView(this.mRecyclerView, this.mRecoverAnimations.get(0).mViewHolder);
        }
        this.mRecoverAnimations.clear();
        this.mOverdrawChild = null;
        this.mOverdrawChildPosition = -1;
        releaseVelocityTracker();
        stopGestureDetection();
    }

    private void startGestureDetection() {
        this.mItemTouchHelperGestureListener = new ItemTouchHelperGestureListener();
        this.mGestureDetector = new GestureDetectorCompat(this.mRecyclerView.getContext(), this.mItemTouchHelperGestureListener);
    }

    private void stopGestureDetection() {
        if (this.mItemTouchHelperGestureListener != null) {
            this.mItemTouchHelperGestureListener.doNotReactToLongPress();
            this.mItemTouchHelperGestureListener = null;
        }
        if (this.mGestureDetector != null) {
            this.mGestureDetector = null;
        }
    }

    private void getSelectedDxDy(float[] fArr) {
        if ((this.mSelectedFlags & 12) != 0) {
            fArr[0] = (this.mSelectedStartX + this.mDx) - this.mSelected.itemView.getLeft();
        } else {
            fArr[0] = this.mSelected.itemView.getTranslationX();
        }
        if ((this.mSelectedFlags & 3) != 0) {
            fArr[1] = (this.mSelectedStartY + this.mDy) - this.mSelected.itemView.getTop();
        } else {
            fArr[1] = this.mSelected.itemView.getTranslationY();
        }
    }

    @Override // android.support.v7.widget.RecyclerView.ItemDecoration
    public void onDrawOver(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
        float f;
        float f2;
        if (this.mSelected != null) {
            getSelectedDxDy(this.mTmpPosition);
            float f3 = this.mTmpPosition[0];
            f2 = this.mTmpPosition[1];
            f = f3;
        } else {
            f = 0.0f;
            f2 = 0.0f;
        }
        this.mCallback.onDrawOver(canvas, recyclerView, this.mSelected, this.mRecoverAnimations, this.mActionState, f, f2);
    }

    @Override // android.support.v7.widget.RecyclerView.ItemDecoration
    public void onDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
        float f;
        float f2;
        this.mOverdrawChildPosition = -1;
        if (this.mSelected != null) {
            getSelectedDxDy(this.mTmpPosition);
            float f3 = this.mTmpPosition[0];
            f2 = this.mTmpPosition[1];
            f = f3;
        } else {
            f = 0.0f;
            f2 = 0.0f;
        }
        this.mCallback.onDraw(canvas, recyclerView, this.mSelected, this.mRecoverAnimations, this.mActionState, f, f2);
    }

    private void select(ViewHolder selected, int actionState) {
        if (selected == mSelected && actionState == mActionState) {
            return;
        }
        mDragScrollStartTimeInMs = Long.MIN_VALUE;
        final int prevActionState = mActionState;
        // prevent duplicate animations
        endRecoverAnimation(selected, true);
        mActionState = actionState;
        if (actionState == ACTION_STATE_DRAG) {
            // we remove after animation is complete. this means we only elevate the last drag
            // child but that should perform good enough as it is very hard to start dragging a
            // new child before the previous one settles.
            mOverdrawChild = selected.itemView;
            addChildDrawingOrderCallback();
        }
        int actionStateMask = (1 << (DIRECTION_FLAG_COUNT + DIRECTION_FLAG_COUNT * actionState))
                - 1;
        boolean preventLayout = false;
        if (mSelected != null) {
            final ViewHolder prevSelected = mSelected;
            if (prevSelected.itemView.getParent() != null) {
                final int swipeDir = prevActionState == ACTION_STATE_DRAG ? 0
                        : swipeIfNecessary(prevSelected);
                releaseVelocityTracker();
                // find where we should animate to
                final float targetTranslateX, targetTranslateY;
                int animationType;
                switch (swipeDir) {
                    case LEFT:
                    case RIGHT:
                    case START:
                    case END:
                        targetTranslateY = 0;
                        targetTranslateX = Math.signum(mDx) * mRecyclerView.getWidth();
                        break;
                    case UP:
                    case DOWN:
                        targetTranslateX = 0;
                        targetTranslateY = Math.signum(mDy) * mRecyclerView.getHeight();
                        break;
                    default:
                        targetTranslateX = 0;
                        targetTranslateY = 0;
                }
                if (prevActionState == ACTION_STATE_DRAG) {
                    animationType = ANIMATION_TYPE_DRAG;
                } else if (swipeDir > 0) {
                    animationType = ANIMATION_TYPE_SWIPE_SUCCESS;
                } else {
                    animationType = ANIMATION_TYPE_SWIPE_CANCEL;
                }
                getSelectedDxDy(mTmpPosition);
                final float currentTranslateX = mTmpPosition[0];
                final float currentTranslateY = mTmpPosition[1];
                final RecoverAnimation rv = new RecoverAnimation(prevSelected, animationType,
                        prevActionState, currentTranslateX, currentTranslateY,
                        targetTranslateX, targetTranslateY) {
                    @Override
                    public void onAnimationEnd(ValueAnimatorCompat animation) {
                        super.onAnimationEnd(animation);
                        if (this.mOverridden) {
                            return;
                        }
                        if (swipeDir <= 0) {
                            // this is a drag or failed swipe. recover immediately
                            mCallback.clearView(mRecyclerView, prevSelected);
                            // full cleanup will happen on onDrawOver
                        } else {
                            // wait until remove animation is complete.
                            mPendingCleanup.add(prevSelected.itemView);
                            mIsPendingCleanup = true;
                            if (swipeDir > 0) {
                                // Animation might be ended by other animators during a layout.
                                // We defer callback to avoid editing adapter during a layout.
                                postDispatchSwipe(this, swipeDir);
                            }
                        }
                        // removed from the list after it is drawn for the last time
                        if (mOverdrawChild == prevSelected.itemView) {
                            removeChildDrawingOrderCallbackIfNecessary(prevSelected.itemView);
                        }
                    }
                };
                final long duration = mCallback.getAnimationDuration(mRecyclerView, animationType,
                        targetTranslateX - currentTranslateX, targetTranslateY - currentTranslateY);
                rv.setDuration(duration);
                mRecoverAnimations.add(rv);
                rv.start();
                preventLayout = true;
            } else {
                removeChildDrawingOrderCallbackIfNecessary(prevSelected.itemView);
                mCallback.clearView(mRecyclerView, prevSelected);
            }
            mSelected = null;
        }
        if (selected != null) {
            mSelectedFlags =
                    (mCallback.getAbsoluteMovementFlags(mRecyclerView, selected) & actionStateMask)
                            >> (mActionState * DIRECTION_FLAG_COUNT);
            mSelectedStartX = selected.itemView.getLeft();
            mSelectedStartY = selected.itemView.getTop();
            mSelected = selected;
            if (actionState == ACTION_STATE_DRAG) {
                mSelected.itemView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            }
        }
        final ViewParent rvParent = mRecyclerView.getParent();
        if (rvParent != null) {
            rvParent.requestDisallowInterceptTouchEvent(mSelected != null);
        }
        if (!preventLayout) {
            mRecyclerView.getLayoutManager().requestSimpleAnimationsInNextLayout();
        }
        mCallback.onSelectedChanged(mSelected, mActionState);
        mRecyclerView.invalidate();
    }

    void postDispatchSwipe(final RecoverAnimation recoverAnimation, final int i) {
        this.mRecyclerView.post(new Runnable() { // from class: android.support.v7.widget.helper.ItemTouchHelper.4
            @Override // java.lang.Runnable
            public void run() {
                if (ItemTouchHelper.this.mRecyclerView == null || !ItemTouchHelper.this.mRecyclerView.isAttachedToWindow() || recoverAnimation.mOverridden || recoverAnimation.mViewHolder.getAdapterPosition() == -1) {
                    return;
                }
                RecyclerView.ItemAnimator itemAnimator = ItemTouchHelper.this.mRecyclerView.getItemAnimator();
                if ((itemAnimator == null || !itemAnimator.isRunning(null)) && !ItemTouchHelper.this.hasRunningRecoverAnim()) {
                    ItemTouchHelper.this.mCallback.onSwiped(recoverAnimation.mViewHolder, i);
                } else {
                    ItemTouchHelper.this.mRecyclerView.post(this);
                }
            }
        });
    }

    boolean hasRunningRecoverAnim() {
        int size = this.mRecoverAnimations.size();
        for (int i = 0; i < size; i++) {
            if (!this.mRecoverAnimations.get(i).mEnded) {
                return true;
            }
        }
        return false;
    }

    private boolean scrollIfNecessary() {
        if (mSelected == null) {
            mDragScrollStartTimeInMs = Long.MIN_VALUE;
            return false;
        }
        final long now = System.currentTimeMillis();
        final long scrollDuration = mDragScrollStartTimeInMs
                == Long.MIN_VALUE ? 0 : now - mDragScrollStartTimeInMs;
        RecyclerView.LayoutManager lm = mRecyclerView.getLayoutManager();
        if (mTmpRect == null) {
            mTmpRect = new Rect();
        }
        int scrollX = 0;
        int scrollY = 0;
        lm.calculateItemDecorationsForChild(mSelected.itemView, mTmpRect);
        if (lm.canScrollHorizontally()) {
            int curX = (int) (mSelectedStartX + mDx);
            final int leftDiff = curX - mTmpRect.left - mRecyclerView.getPaddingLeft();
            if (mDx < 0 && leftDiff < 0) {
                scrollX = leftDiff;
            } else if (mDx > 0) {
                final int rightDiff =
                        curX + mSelected.itemView.getWidth() + mTmpRect.right
                                - (mRecyclerView.getWidth() - mRecyclerView.getPaddingRight());
                if (rightDiff > 0) {
                    scrollX = rightDiff;
                }
            }
        }
        if (lm.canScrollVertically()) {
            int curY = (int) (mSelectedStartY + mDy);
            final int topDiff = curY - mTmpRect.top - mRecyclerView.getPaddingTop();
            if (mDy < 0 && topDiff < 0) {
                scrollY = topDiff;
            } else if (mDy > 0) {
                final int bottomDiff = curY + mSelected.itemView.getHeight() + mTmpRect.bottom -
                        (mRecyclerView.getHeight() - mRecyclerView.getPaddingBottom());
                if (bottomDiff > 0) {
                    scrollY = bottomDiff;
                }
            }
        }
        if (scrollX != 0) {
            scrollX = mCallback.interpolateOutOfBoundsScroll(mRecyclerView,
                    mSelected.itemView.getWidth(), scrollX,
                    mRecyclerView.getWidth(), scrollDuration);
        }
        if (scrollY != 0) {
            scrollY = mCallback.interpolateOutOfBoundsScroll(mRecyclerView,
                    mSelected.itemView.getHeight(), scrollY,
                    mRecyclerView.getHeight(), scrollDuration);
        }
        if (scrollX != 0 || scrollY != 0) {
            if (mDragScrollStartTimeInMs == Long.MIN_VALUE) {
                mDragScrollStartTimeInMs = now;
            }
            mRecyclerView.scrollBy(scrollX, scrollY);
            return true;
        }
        mDragScrollStartTimeInMs = Long.MIN_VALUE;
        return false;
    }

    private List<RecyclerView.ViewHolder> findSwapTargets(RecyclerView.ViewHolder viewHolder) {
        RecyclerView.ViewHolder viewHolder2 = viewHolder;
        if (this.mSwapTargets == null) {
            this.mSwapTargets = new ArrayList();
            this.mDistances = new ArrayList();
        } else {
            this.mSwapTargets.clear();
            this.mDistances.clear();
        }
        int boundingBoxMargin = this.mCallback.getBoundingBoxMargin();
        int iRound = Math.round(this.mSelectedStartX + this.mDx) - boundingBoxMargin;
        int iRound2 = Math.round(this.mSelectedStartY + this.mDy) - boundingBoxMargin;
        int i = boundingBoxMargin * 2;
        int width = viewHolder2.itemView.getWidth() + iRound + i;
        int height = viewHolder2.itemView.getHeight() + iRound2 + i;
        int i2 = (iRound + width) / 2;
        int i3 = (iRound2 + height) / 2;
        RecyclerView.LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
        int childCount = layoutManager.getChildCount();
        int i4 = 0;
        while (i4 < childCount) {
            View childAt = layoutManager.getChildAt(i4);
            if (childAt != viewHolder2.itemView && childAt.getBottom() >= iRound2 && childAt.getTop() <= height && childAt.getRight() >= iRound && childAt.getLeft() <= width) {
                RecyclerView.ViewHolder childViewHolder = this.mRecyclerView.getChildViewHolder(childAt);
                if (this.mCallback.canDropOver(this.mRecyclerView, this.mSelected, childViewHolder)) {
                    int iAbs = Math.abs(i2 - ((childAt.getLeft() + childAt.getRight()) / 2));
                    int iAbs2 = Math.abs(i3 - ((childAt.getTop() + childAt.getBottom()) / 2));
                    int i5 = (iAbs * iAbs) + (iAbs2 * iAbs2);
                    int size = this.mSwapTargets.size();
                    int i6 = 0;
                    for (int i7 = 0; i7 < size && i5 > this.mDistances.get(i7).intValue(); i7++) {
                        i6++;
                    }
                    this.mSwapTargets.add(i6, childViewHolder);
                    this.mDistances.add(i6, Integer.valueOf(i5));
                }
            }
            i4++;
            viewHolder2 = viewHolder;
        }
        return this.mSwapTargets;
    }

    void moveIfNecessary(RecyclerView.ViewHolder viewHolder) {
        if (!this.mRecyclerView.isLayoutRequested() && this.mActionState == 2) {
            float moveThreshold = this.mCallback.getMoveThreshold(viewHolder);
            int i = (int) (this.mSelectedStartX + this.mDx);
            int i2 = (int) (this.mSelectedStartY + this.mDy);
            if (Math.abs(i2 - viewHolder.itemView.getTop()) >= viewHolder.itemView.getHeight() * moveThreshold || Math.abs(i - viewHolder.itemView.getLeft()) >= viewHolder.itemView.getWidth() * moveThreshold) {
                List<RecyclerView.ViewHolder> listFindSwapTargets = findSwapTargets(viewHolder);
                if (listFindSwapTargets.size() == 0) {
                    return;
                }
                RecyclerView.ViewHolder viewHolderChooseDropTarget = this.mCallback.chooseDropTarget(viewHolder, listFindSwapTargets, i, i2);
                if (viewHolderChooseDropTarget == null) {
                    this.mSwapTargets.clear();
                    this.mDistances.clear();
                    return;
                }
                int adapterPosition = viewHolderChooseDropTarget.getAdapterPosition();
                int adapterPosition2 = viewHolder.getAdapterPosition();
                if (this.mCallback.onMove(this.mRecyclerView, viewHolder, viewHolderChooseDropTarget)) {
                    this.mCallback.onMoved(this.mRecyclerView, viewHolder, adapterPosition2, viewHolderChooseDropTarget, adapterPosition, i, i2);
                }
            }
        }
    }

    @Override // android.support.v7.widget.RecyclerView.OnChildAttachStateChangeListener
    public void onChildViewDetachedFromWindow(@NonNull View view) {
        removeChildDrawingOrderCallbackIfNecessary(view);
        RecyclerView.ViewHolder childViewHolder = this.mRecyclerView.getChildViewHolder(view);
        if (childViewHolder == null) {
            return;
        }
        if (this.mSelected != null && childViewHolder == this.mSelected) {
            select(null, 0);
            return;
        }
        endRecoverAnimation(childViewHolder, false);
        if (this.mPendingCleanup.remove(childViewHolder.itemView)) {
            this.mCallback.clearView(this.mRecyclerView, childViewHolder);
        }
    }

    void endRecoverAnimation(RecyclerView.ViewHolder viewHolder, boolean z) {
        for (int size = this.mRecoverAnimations.size() - 1; size >= 0; size--) {
            RecoverAnimation recoverAnimation = this.mRecoverAnimations.get(size);
            if (recoverAnimation.mViewHolder == viewHolder) {
                recoverAnimation.mOverridden |= z;
                if (!recoverAnimation.mEnded) {
                    recoverAnimation.cancel();
                }
                this.mRecoverAnimations.remove(size);
                return;
            }
        }
    }

    @Override // android.support.v7.widget.RecyclerView.ItemDecoration
    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        rect.setEmpty();
    }

    void obtainVelocityTracker() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
        }
        this.mVelocityTracker = VelocityTracker.obtain();
    }

    private void releaseVelocityTracker() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private RecyclerView.ViewHolder findSwipedView(MotionEvent motionEvent) {
        View viewFindChildView;
        RecyclerView.LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
        if (this.mActivePointerId == -1) {
            return null;
        }
        int iFindPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
        float x = motionEvent.getX(iFindPointerIndex) - this.mInitialTouchX;
        float y = motionEvent.getY(iFindPointerIndex) - this.mInitialTouchY;
        float fAbs = Math.abs(x);
        float fAbs2 = Math.abs(y);
        if (fAbs < this.mSlop && fAbs2 < this.mSlop) {
            return null;
        }
        if (fAbs > fAbs2 && layoutManager.canScrollHorizontally()) {
            return null;
        }
        if ((fAbs2 <= fAbs || !layoutManager.canScrollVertically()) && (viewFindChildView = findChildView(motionEvent)) != null) {
            return this.mRecyclerView.getChildViewHolder(viewFindChildView);
        }
        return null;
    }

    void checkSelectForSwipe(int i, MotionEvent motionEvent, int i2) {
        RecyclerView.ViewHolder viewHolderFindSwipedView;
        int absoluteMovementFlags;
        if (this.mSelected != null || i != 2 || this.mActionState == 2 || !this.mCallback.isItemViewSwipeEnabled() || this.mRecyclerView.getScrollState() == 1 || (viewHolderFindSwipedView = findSwipedView(motionEvent)) == null || (absoluteMovementFlags = (this.mCallback.getAbsoluteMovementFlags(this.mRecyclerView, viewHolderFindSwipedView) & 65280) >> 8) == 0) {
            return;
        }
        float x = motionEvent.getX(i2);
        float y = motionEvent.getY(i2);
        float f = x - this.mInitialTouchX;
        float f2 = y - this.mInitialTouchY;
        float fAbs = Math.abs(f);
        float fAbs2 = Math.abs(f2);
        if (fAbs >= this.mSlop || fAbs2 >= this.mSlop) {
            if (fAbs > fAbs2) {
                if (f < 0.0f && (absoluteMovementFlags & 4) == 0) {
                    return;
                }
                if (f > 0.0f && (absoluteMovementFlags & 8) == 0) {
                    return;
                }
            } else {
                if (f2 < 0.0f && (absoluteMovementFlags & 1) == 0) {
                    return;
                }
                if (f2 > 0.0f && (absoluteMovementFlags & 2) == 0) {
                    return;
                }
            }
            this.mDy = 0.0f;
            this.mDx = 0.0f;
            this.mActivePointerId = motionEvent.getPointerId(0);
            select(viewHolderFindSwipedView, 1);
        }
    }

    View findChildView(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        if (this.mSelected != null) {
            View view = this.mSelected.itemView;
            if (hitTest(view, x, y, this.mSelectedStartX + this.mDx, this.mSelectedStartY + this.mDy)) {
                return view;
            }
        }
        for (int size = this.mRecoverAnimations.size() - 1; size >= 0; size--) {
            RecoverAnimation recoverAnimation = this.mRecoverAnimations.get(size);
            View view2 = recoverAnimation.mViewHolder.itemView;
            if (hitTest(view2, x, y, recoverAnimation.mX, recoverAnimation.mY)) {
                return view2;
            }
        }
        return this.mRecyclerView.findChildViewUnder(x, y);
    }

    public void startDrag(@NonNull RecyclerView.ViewHolder viewHolder) {
        if (!this.mCallback.hasDragFlag(this.mRecyclerView, viewHolder)) {
            Log.e(TAG, "Start drag has been called but dragging is not enabled");
            return;
        }
        if (viewHolder.itemView.getParent() != this.mRecyclerView) {
            Log.e(TAG, "Start drag has been called with a view holder which is not a child of the RecyclerView which is controlled by this ItemTouchHelper.");
            return;
        }
        obtainVelocityTracker();
        this.mDy = 0.0f;
        this.mDx = 0.0f;
        select(viewHolder, 2);
    }

    public void startSwipe(@NonNull RecyclerView.ViewHolder viewHolder) {
        if (!this.mCallback.hasSwipeFlag(this.mRecyclerView, viewHolder)) {
            Log.e(TAG, "Start swipe has been called but swiping is not enabled");
            return;
        }
        if (viewHolder.itemView.getParent() != this.mRecyclerView) {
            Log.e(TAG, "Start swipe has been called with a view holder which is not a child of the RecyclerView controlled by this ItemTouchHelper.");
            return;
        }
        obtainVelocityTracker();
        this.mDy = 0.0f;
        this.mDx = 0.0f;
        select(viewHolder, 1);
    }

    RecoverAnimation findAnimation(MotionEvent motionEvent) {
        if (this.mRecoverAnimations.isEmpty()) {
            return null;
        }
        View viewFindChildView = findChildView(motionEvent);
        for (int size = this.mRecoverAnimations.size() - 1; size >= 0; size--) {
            RecoverAnimation recoverAnimation = this.mRecoverAnimations.get(size);
            if (recoverAnimation.mViewHolder.itemView == viewFindChildView) {
                return recoverAnimation;
            }
        }
        return null;
    }

    void updateDxDy(MotionEvent motionEvent, int i, int i2) {
        float x = motionEvent.getX(i2);
        float y = motionEvent.getY(i2);
        this.mDx = x - this.mInitialTouchX;
        this.mDy = y - this.mInitialTouchY;
        if ((i & 4) == 0) {
            this.mDx = Math.max(0.0f, this.mDx);
        }
        if ((i & 8) == 0) {
            this.mDx = Math.min(0.0f, this.mDx);
        }
        if ((i & 1) == 0) {
            this.mDy = Math.max(0.0f, this.mDy);
        }
        if ((i & 2) == 0) {
            this.mDy = Math.min(0.0f, this.mDy);
        }
    }

    private int swipeIfNecessary(RecyclerView.ViewHolder viewHolder) {
        if (this.mActionState == 2) {
            return 0;
        }
        int movementFlags = this.mCallback.getMovementFlags(this.mRecyclerView, viewHolder);
        int iConvertToAbsoluteDirection = (this.mCallback.convertToAbsoluteDirection(movementFlags, ViewCompat.getLayoutDirection(this.mRecyclerView)) & 65280) >> 8;
        if (iConvertToAbsoluteDirection == 0) {
            return 0;
        }
        int i = (movementFlags & 65280) >> 8;
        if (Math.abs(this.mDx) > Math.abs(this.mDy)) {
            int iCheckHorizontalSwipe = checkHorizontalSwipe(viewHolder, iConvertToAbsoluteDirection);
            if (iCheckHorizontalSwipe > 0) {
                return (i & iCheckHorizontalSwipe) == 0 ? Callback.convertToRelativeDirection(iCheckHorizontalSwipe, ViewCompat.getLayoutDirection(this.mRecyclerView)) : iCheckHorizontalSwipe;
            }
            int iCheckVerticalSwipe = checkVerticalSwipe(viewHolder, iConvertToAbsoluteDirection);
            if (iCheckVerticalSwipe > 0) {
                return iCheckVerticalSwipe;
            }
        } else {
            int iCheckVerticalSwipe2 = checkVerticalSwipe(viewHolder, iConvertToAbsoluteDirection);
            if (iCheckVerticalSwipe2 > 0) {
                return iCheckVerticalSwipe2;
            }
            int iCheckHorizontalSwipe2 = checkHorizontalSwipe(viewHolder, iConvertToAbsoluteDirection);
            if (iCheckHorizontalSwipe2 > 0) {
                return (i & iCheckHorizontalSwipe2) == 0 ? Callback.convertToRelativeDirection(iCheckHorizontalSwipe2, ViewCompat.getLayoutDirection(this.mRecyclerView)) : iCheckHorizontalSwipe2;
            }
        }
        return 0;
    }

    private int checkHorizontalSwipe(RecyclerView.ViewHolder viewHolder, int i) {
        if ((i & 12) == 0) {
            return 0;
        }
        int i2 = this.mDx > 0.0f ? 8 : 4;
        if (this.mVelocityTracker != null && this.mActivePointerId > -1) {
            this.mVelocityTracker.computeCurrentVelocity(1000, this.mCallback.getSwipeVelocityThreshold(this.mMaxSwipeVelocity));
            float xVelocity = this.mVelocityTracker.getXVelocity(this.mActivePointerId);
            float yVelocity = this.mVelocityTracker.getYVelocity(this.mActivePointerId);
            int i3 = xVelocity > 0.0f ? 8 : 4;
            float fAbs = Math.abs(xVelocity);
            if ((i3 & i) != 0 && i2 == i3 && fAbs >= this.mCallback.getSwipeEscapeVelocity(this.mSwipeEscapeVelocity) && fAbs > Math.abs(yVelocity)) {
                return i3;
            }
        }
        float width = this.mRecyclerView.getWidth() * this.mCallback.getSwipeThreshold(viewHolder);
        if ((i & i2) == 0 || Math.abs(this.mDx) <= width) {
            return 0;
        }
        return i2;
    }

    private int checkVerticalSwipe(RecyclerView.ViewHolder viewHolder, int i) {
        if ((i & 3) == 0) {
            return 0;
        }
        int i2 = this.mDy > 0.0f ? 2 : 1;
        if (this.mVelocityTracker != null && this.mActivePointerId > -1) {
            this.mVelocityTracker.computeCurrentVelocity(1000, this.mCallback.getSwipeVelocityThreshold(this.mMaxSwipeVelocity));
            float xVelocity = this.mVelocityTracker.getXVelocity(this.mActivePointerId);
            float yVelocity = this.mVelocityTracker.getYVelocity(this.mActivePointerId);
            int i3 = yVelocity > 0.0f ? 2 : 1;
            float fAbs = Math.abs(yVelocity);
            if ((i3 & i) != 0 && i3 == i2 && fAbs >= this.mCallback.getSwipeEscapeVelocity(this.mSwipeEscapeVelocity) && fAbs > Math.abs(xVelocity)) {
                return i3;
            }
        }
        float height = this.mRecyclerView.getHeight() * this.mCallback.getSwipeThreshold(viewHolder);
        if ((i & i2) == 0 || Math.abs(this.mDy) <= height) {
            return 0;
        }
        return i2;
    }

    private void addChildDrawingOrderCallback() {
        if (Build.VERSION.SDK_INT >= 21) {
            return;
        }
        if (this.mChildDrawingOrderCallback == null) {
            this.mChildDrawingOrderCallback = new RecyclerView.ChildDrawingOrderCallback() { // from class: android.support.v7.widget.helper.ItemTouchHelper.5
                @Override // android.support.v7.widget.RecyclerView.ChildDrawingOrderCallback
                public int onGetChildDrawingOrder(int i, int i2) {
                    if (ItemTouchHelper.this.mOverdrawChild == null) {
                        return i2;
                    }
                    int iIndexOfChild = ItemTouchHelper.this.mOverdrawChildPosition;
                    if (iIndexOfChild == -1) {
                        iIndexOfChild = ItemTouchHelper.this.mRecyclerView.indexOfChild(ItemTouchHelper.this.mOverdrawChild);
                        ItemTouchHelper.this.mOverdrawChildPosition = iIndexOfChild;
                    }
                    return i2 == i + (-1) ? iIndexOfChild : i2 < iIndexOfChild ? i2 : i2 + 1;
                }
            };
        }
        this.mRecyclerView.setChildDrawingOrderCallback(this.mChildDrawingOrderCallback);
    }

    void removeChildDrawingOrderCallbackIfNecessary(View view) {
        if (view == this.mOverdrawChild) {
            this.mOverdrawChild = null;
            if (this.mChildDrawingOrderCallback != null) {
                this.mRecyclerView.setChildDrawingOrderCallback(null);
            }
        }
    }

    public static abstract class Callback {
        private static final int ABS_HORIZONTAL_DIR_FLAGS = 789516;
        public static final int DEFAULT_DRAG_ANIMATION_DURATION = 200;
        public static final int DEFAULT_SWIPE_ANIMATION_DURATION = 250;
        private static final long DRAG_SCROLL_ACCELERATION_LIMIT_TIME_MS = 2000;
        static final int RELATIVE_DIR_FLAGS = 3158064;
        private static final Interpolator sDragScrollInterpolator = new Interpolator() { // from class: android.support.v7.widget.helper.ItemTouchHelper.Callback.1
            @Override // android.animation.TimeInterpolator
            public float getInterpolation(float f) {
                return f * f * f * f * f;
            }
        };
        private static final Interpolator sDragViewScrollCapInterpolator = new Interpolator() { // from class: android.support.v7.widget.helper.ItemTouchHelper.Callback.2
            @Override // android.animation.TimeInterpolator
            public float getInterpolation(float f) {
                float f2 = f - 1.0f;
                return (f2 * f2 * f2 * f2 * f2) + 1.0f;
            }
        };
        private int mCachedMaxScrollSpeed = -1;

        public static int convertToRelativeDirection(int i, int i2) {
            int i3 = i & ABS_HORIZONTAL_DIR_FLAGS;
            if (i3 == 0) {
                return i;
            }
            int i4 = i & (~i3);
            if (i2 == 0) {
                return i4 | (i3 << 2);
            }
            int i5 = i3 << 1;
            return i4 | ((-789517) & i5) | ((i5 & ABS_HORIZONTAL_DIR_FLAGS) << 2);
        }

        public static int makeFlag(int i, int i2) {
            return i2 << (i * 8);
        }

        public boolean canDropOver(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder2) {
            return true;
        }

        public int convertToAbsoluteDirection(int i, int i2) {
            int i3 = i & RELATIVE_DIR_FLAGS;
            if (i3 == 0) {
                return i;
            }
            int i4 = i & (~i3);
            if (i2 == 0) {
                return (i3 >> 2) | i4;
            }
            int i5 = i3 >> 1;
            return ((RELATIVE_DIR_FLAGS & i5) >> 2) | i4 | ((-3158065) & i5);
        }

        public int getBoundingBoxMargin() {
            return 0;
        }

        public float getMoveThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
            return 0.5f;
        }

        public abstract int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder);

        public float getSwipeEscapeVelocity(float f) {
            return f;
        }

        public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
            return 0.5f;
        }

        public float getSwipeVelocityThreshold(float f) {
            return f;
        }

        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        public boolean isLongPressDragEnabled() {
            return true;
        }

        public abstract boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder2);

        public abstract void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i);

        @NonNull
        public static ItemTouchUIUtil getDefaultUIUtil() {
            return ItemTouchUIUtilImpl.INSTANCE;
        }

        public static int makeMovementFlags(int i, int i2) {
            return makeFlag(2, i) | makeFlag(1, i2) | makeFlag(0, i2 | i);
        }

        final int getAbsoluteMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return convertToAbsoluteDirection(getMovementFlags(recyclerView, viewHolder), ViewCompat.getLayoutDirection(recyclerView));
        }

        boolean hasDragFlag(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return (getAbsoluteMovementFlags(recyclerView, viewHolder) & ItemTouchHelper.ACTION_MODE_DRAG_MASK) != 0;
        }

        boolean hasSwipeFlag(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return (getAbsoluteMovementFlags(recyclerView, viewHolder) & 65280) != 0;
        }

        public RecyclerView.ViewHolder chooseDropTarget(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull List<RecyclerView.ViewHolder> list, int i, int i2) {
            int bottom;
            int iAbs;
            int top;
            int iAbs2;
            int left;
            int iAbs3;
            int right;
            int iAbs4;
            int width = viewHolder.itemView.getWidth() + i;
            int height = viewHolder.itemView.getHeight() + i2;
            int left2 = i - viewHolder.itemView.getLeft();
            int top2 = i2 - viewHolder.itemView.getTop();
            int size = list.size();
            RecyclerView.ViewHolder viewHolder2 = null;
            int i3 = -1;
            for (int i4 = 0; i4 < size; i4++) {
                RecyclerView.ViewHolder viewHolder3 = list.get(i4);
                if (left2 > 0 && (right = viewHolder3.itemView.getRight() - width) < 0 && viewHolder3.itemView.getRight() > viewHolder.itemView.getRight() && (iAbs4 = Math.abs(right)) > i3) {
                    viewHolder2 = viewHolder3;
                    i3 = iAbs4;
                }
                if (left2 < 0 && (left = viewHolder3.itemView.getLeft() - i) > 0 && viewHolder3.itemView.getLeft() < viewHolder.itemView.getLeft() && (iAbs3 = Math.abs(left)) > i3) {
                    viewHolder2 = viewHolder3;
                    i3 = iAbs3;
                }
                if (top2 < 0 && (top = viewHolder3.itemView.getTop() - i2) > 0 && viewHolder3.itemView.getTop() < viewHolder.itemView.getTop() && (iAbs2 = Math.abs(top)) > i3) {
                    viewHolder2 = viewHolder3;
                    i3 = iAbs2;
                }
                if (top2 > 0 && (bottom = viewHolder3.itemView.getBottom() - height) < 0 && viewHolder3.itemView.getBottom() > viewHolder.itemView.getBottom() && (iAbs = Math.abs(bottom)) > i3) {
                    viewHolder2 = viewHolder3;
                    i3 = iAbs;
                }
            }
            return viewHolder2;
        }

        public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder != null) {
                ItemTouchUIUtilImpl.INSTANCE.onSelected(viewHolder.itemView);
            }
        }

        private int getMaxDragScroll(RecyclerView recyclerView) {
            if (this.mCachedMaxScrollSpeed == -1) {
                this.mCachedMaxScrollSpeed = recyclerView.getResources().getDimensionPixelSize(R.dimen.item_touch_helper_max_drag_scroll_per_frame);
            }
            return this.mCachedMaxScrollSpeed;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int i, @NonNull RecyclerView.ViewHolder viewHolder2, int i2, int i3, int i4) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof ViewDropHandler) {
                ((ViewDropHandler) layoutManager).prepareForDrop(viewHolder.itemView, viewHolder2.itemView, i3, i4);
                return;
            }
            if (layoutManager.canScrollHorizontally()) {
                if (layoutManager.getDecoratedLeft(viewHolder2.itemView) <= recyclerView.getPaddingLeft()) {
                    recyclerView.scrollToPosition(i2);
                }
                if (layoutManager.getDecoratedRight(viewHolder2.itemView) >= recyclerView.getWidth() - recyclerView.getPaddingRight()) {
                    recyclerView.scrollToPosition(i2);
                }
            }
            if (layoutManager.canScrollVertically()) {
                if (layoutManager.getDecoratedTop(viewHolder2.itemView) <= recyclerView.getPaddingTop()) {
                    recyclerView.scrollToPosition(i2);
                }
                if (layoutManager.getDecoratedBottom(viewHolder2.itemView) >= recyclerView.getHeight() - recyclerView.getPaddingBottom()) {
                    recyclerView.scrollToPosition(i2);
                }
            }
        }

        void onDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, List<RecoverAnimation> list, int i, float f, float f2) {
            int size = list.size();
            for (int i2 = 0; i2 < size; i2++) {
                RecoverAnimation recoverAnimation = list.get(i2);
                recoverAnimation.update();
                int iSave = canvas.save();
                onChildDraw(canvas, recyclerView, recoverAnimation.mViewHolder, recoverAnimation.mX, recoverAnimation.mY, recoverAnimation.mActionState, false);
                canvas.restoreToCount(iSave);
            }
            if (viewHolder != null) {
                int iSave2 = canvas.save();
                onChildDraw(canvas, recyclerView, viewHolder, f, f2, i, true);
                canvas.restoreToCount(iSave2);
            }
        }

        void onDrawOver(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, List<RecoverAnimation> list, int i, float f, float f2) {
            int size = list.size();
            boolean z = false;
            for (int i2 = 0; i2 < size; i2++) {
                RecoverAnimation recoverAnimation = list.get(i2);
                int iSave = canvas.save();
                onChildDrawOver(canvas, recyclerView, recoverAnimation.mViewHolder, recoverAnimation.mX, recoverAnimation.mY, recoverAnimation.mActionState, false);
                canvas.restoreToCount(iSave);
            }
            if (viewHolder != null) {
                int iSave2 = canvas.save();
                onChildDrawOver(canvas, recyclerView, viewHolder, f, f2, i, true);
                canvas.restoreToCount(iSave2);
            }
            for (int i3 = size - 1; i3 >= 0; i3--) {
                RecoverAnimation recoverAnimation2 = list.get(i3);
                if (recoverAnimation2.mEnded && !recoverAnimation2.mIsPendingCleanup) {
                    list.remove(i3);
                } else if (!recoverAnimation2.mEnded) {
                    z = true;
                }
            }
            if (z) {
                recyclerView.invalidate();
            }
        }

        public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            ItemTouchUIUtilImpl.INSTANCE.clearView(viewHolder.itemView);
        }

        public void onChildDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float f, float f2, int i, boolean z) {
            ItemTouchUIUtilImpl.INSTANCE.onDraw(canvas, recyclerView, viewHolder.itemView, f, f2, i, z);
        }

        public void onChildDrawOver(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float f, float f2, int i, boolean z) {
            ItemTouchUIUtilImpl.INSTANCE.onDrawOver(canvas, recyclerView, viewHolder.itemView, f, f2, i, z);
        }

        public long getAnimationDuration(@NonNull RecyclerView recyclerView, int i, float f, float f2) {
            RecyclerView.ItemAnimator itemAnimator = recyclerView.getItemAnimator();
            if (itemAnimator == null) {
                return i == 8 ? 200L : 250L;
            }
            if (i == 8) {
                return itemAnimator.getMoveDuration();
            }
            return itemAnimator.getRemoveDuration();
        }

        public int interpolateOutOfBoundsScroll(@NonNull RecyclerView recyclerView, int i, int i2, int i3, long j) {
            int iSignum = (int) (((int) (((int) Math.signum(i2)) * getMaxDragScroll(recyclerView) * sDragViewScrollCapInterpolator.getInterpolation(Math.min(1.0f, (Math.abs(i2) * 1.0f) / i)))) * sDragScrollInterpolator.getInterpolation(j <= DRAG_SCROLL_ACCELERATION_LIMIT_TIME_MS ? j / 2000.0f : 1.0f));
            return iSignum == 0 ? i2 > 0 ? 1 : -1 : iSignum;
        }
    }

    public static abstract class SimpleCallback extends Callback {
        private int mDefaultDragDirs;
        private int mDefaultSwipeDirs;

        public SimpleCallback(int i, int i2) {
            this.mDefaultSwipeDirs = i2;
            this.mDefaultDragDirs = i;
        }

        public void setDefaultSwipeDirs(int i) {
            this.mDefaultSwipeDirs = i;
        }

        public void setDefaultDragDirs(int i) {
            this.mDefaultDragDirs = i;
        }

        public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            return this.mDefaultSwipeDirs;
        }

        public int getDragDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            return this.mDefaultDragDirs;
        }

        @Override // android.support.v7.widget.helper.ItemTouchHelper.Callback
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(getDragDirs(recyclerView, viewHolder), getSwipeDirs(recyclerView, viewHolder));
        }
    }

    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {
        private boolean mShouldReactToLongPress = true;

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent motionEvent) {
            return true;
        }

        ItemTouchHelperGestureListener() {
        }

        void doNotReactToLongPress() {
            this.mShouldReactToLongPress = false;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent motionEvent) {
            View viewFindChildView;
            RecyclerView.ViewHolder childViewHolder;
            if (this.mShouldReactToLongPress && (viewFindChildView = ItemTouchHelper.this.findChildView(motionEvent)) != null && (childViewHolder = ItemTouchHelper.this.mRecyclerView.getChildViewHolder(viewFindChildView)) != null && ItemTouchHelper.this.mCallback.hasDragFlag(ItemTouchHelper.this.mRecyclerView, childViewHolder) && motionEvent.getPointerId(0) == ItemTouchHelper.this.mActivePointerId) {
                int iFindPointerIndex = motionEvent.findPointerIndex(ItemTouchHelper.this.mActivePointerId);
                float x = motionEvent.getX(iFindPointerIndex);
                float y = motionEvent.getY(iFindPointerIndex);
                ItemTouchHelper.this.mInitialTouchX = x;
                ItemTouchHelper.this.mInitialTouchY = y;
                ItemTouchHelper itemTouchHelper = ItemTouchHelper.this;
                ItemTouchHelper.this.mDy = 0.0f;
                itemTouchHelper.mDx = 0.0f;
                if (ItemTouchHelper.this.mCallback.isLongPressDragEnabled()) {
                    ItemTouchHelper.this.select(childViewHolder, 2);
                }
            }
        }
    }

    private static class RecoverAnimation implements Animator.AnimatorListener {
        final int mActionState;
        final int mAnimationType;
        private float mFraction;
        boolean mIsPendingCleanup;
        final float mStartDx;
        final float mStartDy;
        final float mTargetX;
        final float mTargetY;
        final RecyclerView.ViewHolder mViewHolder;
        float mX;
        float mY;
        boolean mOverridden = false;
        boolean mEnded = false;
        private final ValueAnimator mValueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }

        RecoverAnimation(RecyclerView.ViewHolder viewHolder, int i, int i2, float f, float f2, float f3, float f4) {
            this.mActionState = i2;
            this.mAnimationType = i;
            this.mViewHolder = viewHolder;
            this.mStartDx = f;
            this.mStartDy = f2;
            this.mTargetX = f3;
            this.mTargetY = f4;
            this.mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: android.support.v7.widget.helper.ItemTouchHelper.RecoverAnimation.1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    RecoverAnimation.this.setFraction(valueAnimator.getAnimatedFraction());
                }
            });
            this.mValueAnimator.setTarget(viewHolder.itemView);
            this.mValueAnimator.addListener(this);
            setFraction(0.0f);
        }

        public void setDuration(long j) {
            this.mValueAnimator.setDuration(j);
        }

        public void start() {
            this.mViewHolder.setIsRecyclable(false);
            this.mValueAnimator.start();
        }

        public void cancel() {
            this.mValueAnimator.cancel();
        }

        public void setFraction(float f) {
            this.mFraction = f;
        }

        public void update() {
            if (this.mStartDx == this.mTargetX) {
                this.mX = this.mViewHolder.itemView.getTranslationX();
            } else {
                this.mX = this.mStartDx + (this.mFraction * (this.mTargetX - this.mStartDx));
            }
            if (this.mStartDy == this.mTargetY) {
                this.mY = this.mViewHolder.itemView.getTranslationY();
            } else {
                this.mY = this.mStartDy + (this.mFraction * (this.mTargetY - this.mStartDy));
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (!this.mEnded) {
                this.mViewHolder.setIsRecyclable(true);
            }
            this.mEnded = true;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            setFraction(1.0f);
        }
    }
}
