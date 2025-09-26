// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.widget;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.support.annotation.RestrictTo;
import android.support.annotation.NonNull;
import android.os.Parcelable;
import android.view.accessibility.AccessibilityEvent;
import android.graphics.PointF;
import android.util.Log;
import java.util.List;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v7.widget.helper.ItemTouchHelper;

public class LinearLayoutManager extends LayoutManager implements ViewDropHandler, ScrollVectorProvider
{
    static final boolean DEBUG = false;
    public static final int HORIZONTAL = 0;
    public static final int INVALID_OFFSET = Integer.MIN_VALUE;
    private static final float MAX_SCROLL_FACTOR = 0.33333334f;
    private static final String TAG = "LinearLayoutManager";
    public static final int VERTICAL = 1;
    final AnchorInfo mAnchorInfo;
    private int mInitialPrefetchItemCount;
    private boolean mLastStackFromEnd;
    private final LayoutChunkResult mLayoutChunkResult;
    private LayoutState mLayoutState;
    int mOrientation;
    OrientationHelper mOrientationHelper;
    SavedState mPendingSavedState;
    int mPendingScrollPosition;
    int mPendingScrollPositionOffset;
    private boolean mRecycleChildrenOnDetach;
    private boolean mReverseLayout;
    boolean mShouldReverseLayout;
    private boolean mSmoothScrollbarEnabled;
    private boolean mStackFromEnd;
    
    public LinearLayoutManager(final Context context) {
        this(context, 1, false);
    }
    
    public LinearLayoutManager(final Context context, final int orientation, final boolean reverseLayout) {
        this.mOrientation = 1;
        this.mReverseLayout = false;
        this.mShouldReverseLayout = false;
        this.mStackFromEnd = false;
        this.mSmoothScrollbarEnabled = true;
        this.mPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        this.mPendingSavedState = null;
        this.mAnchorInfo = new AnchorInfo();
        this.mLayoutChunkResult = new LayoutChunkResult();
        this.mInitialPrefetchItemCount = 2;
        this.setOrientation(orientation);
        this.setReverseLayout(reverseLayout);
    }
    
    public LinearLayoutManager(final Context context, final AttributeSet set, final int n, final int n2) {
        this.mOrientation = 1;
        this.mReverseLayout = false;
        this.mShouldReverseLayout = false;
        this.mStackFromEnd = false;
        this.mSmoothScrollbarEnabled = true;
        this.mPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        this.mPendingSavedState = null;
        this.mAnchorInfo = new AnchorInfo();
        this.mLayoutChunkResult = new LayoutChunkResult();
        this.mInitialPrefetchItemCount = 2;
        final Properties properties = RecyclerView.LayoutManager.getProperties(context, set, n, n2);
        this.setOrientation(properties.orientation);
        this.setReverseLayout(properties.reverseLayout);
        this.setStackFromEnd(properties.stackFromEnd);
    }
    
    private int computeScrollExtent(final State state) {
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0) {
            return 0;
        }
        this.ensureLayoutState();
        return ScrollbarHelper.computeScrollExtent(state, this.mOrientationHelper, this.findFirstVisibleChildClosestToStart(this.mSmoothScrollbarEnabled ^ true, true), this.findFirstVisibleChildClosestToEnd(this.mSmoothScrollbarEnabled ^ true, true), this, this.mSmoothScrollbarEnabled);
    }
    
    private int computeScrollOffset(final State state) {
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0) {
            return 0;
        }
        this.ensureLayoutState();
        return ScrollbarHelper.computeScrollOffset(state, this.mOrientationHelper, this.findFirstVisibleChildClosestToStart(this.mSmoothScrollbarEnabled ^ true, true), this.findFirstVisibleChildClosestToEnd(this.mSmoothScrollbarEnabled ^ true, true), this, this.mSmoothScrollbarEnabled, this.mShouldReverseLayout);
    }
    
    private int computeScrollRange(final State state) {
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0) {
            return 0;
        }
        this.ensureLayoutState();
        return ScrollbarHelper.computeScrollRange(state, this.mOrientationHelper, this.findFirstVisibleChildClosestToStart(this.mSmoothScrollbarEnabled ^ true, true), this.findFirstVisibleChildClosestToEnd(this.mSmoothScrollbarEnabled ^ true, true), this, this.mSmoothScrollbarEnabled);
    }
    
    private View findFirstPartiallyOrCompletelyInvisibleChild(final Recycler recycler, final State state) {
        return this.findOnePartiallyOrCompletelyInvisibleChild(0, ((RecyclerView.LayoutManager)this).getChildCount());
    }
    
    private View findFirstReferenceChild(final Recycler recycler, final State state) {
        return this.findReferenceChild(recycler, state, 0, ((RecyclerView.LayoutManager)this).getChildCount(), state.getItemCount());
    }
    
    private View findFirstVisibleChildClosestToEnd(final boolean b, final boolean b2) {
        if (this.mShouldReverseLayout) {
            return this.findOneVisibleChild(0, ((RecyclerView.LayoutManager)this).getChildCount(), b, b2);
        }
        return this.findOneVisibleChild(((RecyclerView.LayoutManager)this).getChildCount() - 1, -1, b, b2);
    }
    
    private View findFirstVisibleChildClosestToStart(final boolean b, final boolean b2) {
        if (this.mShouldReverseLayout) {
            return this.findOneVisibleChild(((RecyclerView.LayoutManager)this).getChildCount() - 1, -1, b, b2);
        }
        return this.findOneVisibleChild(0, ((RecyclerView.LayoutManager)this).getChildCount(), b, b2);
    }
    
    private View findLastPartiallyOrCompletelyInvisibleChild(final Recycler recycler, final State state) {
        return this.findOnePartiallyOrCompletelyInvisibleChild(((RecyclerView.LayoutManager)this).getChildCount() - 1, -1);
    }
    
    private View findLastReferenceChild(final Recycler recycler, final State state) {
        return this.findReferenceChild(recycler, state, ((RecyclerView.LayoutManager)this).getChildCount() - 1, -1, state.getItemCount());
    }
    
    private View findPartiallyOrCompletelyInvisibleChildClosestToEnd(final Recycler recycler, final State state) {
        View view;
        if (this.mShouldReverseLayout) {
            view = this.findFirstPartiallyOrCompletelyInvisibleChild(recycler, state);
        }
        else {
            view = this.findLastPartiallyOrCompletelyInvisibleChild(recycler, state);
        }
        return view;
    }
    
    private View findPartiallyOrCompletelyInvisibleChildClosestToStart(final Recycler recycler, final State state) {
        View view;
        if (this.mShouldReverseLayout) {
            view = this.findLastPartiallyOrCompletelyInvisibleChild(recycler, state);
        }
        else {
            view = this.findFirstPartiallyOrCompletelyInvisibleChild(recycler, state);
        }
        return view;
    }
    
    private View findReferenceChildClosestToEnd(final Recycler recycler, final State state) {
        View view;
        if (this.mShouldReverseLayout) {
            view = this.findFirstReferenceChild(recycler, state);
        }
        else {
            view = this.findLastReferenceChild(recycler, state);
        }
        return view;
    }
    
    private View findReferenceChildClosestToStart(final Recycler recycler, final State state) {
        View view;
        if (this.mShouldReverseLayout) {
            view = this.findLastReferenceChild(recycler, state);
        }
        else {
            view = this.findFirstReferenceChild(recycler, state);
        }
        return view;
    }
    
    private int fixLayoutEndGap(int n, final Recycler recycler, final State state, final boolean b) {
        final int n2 = this.mOrientationHelper.getEndAfterPadding() - n;
        if (n2 > 0) {
            final int n3 = -this.scrollBy(-n2, recycler, state);
            if (b) {
                n = this.mOrientationHelper.getEndAfterPadding() - (n + n3);
                if (n > 0) {
                    this.mOrientationHelper.offsetChildren(n);
                    return n + n3;
                }
            }
            return n3;
        }
        return 0;
    }
    
    private int fixLayoutStartGap(int n, final Recycler recycler, final State state, final boolean b) {
        final int n2 = n - this.mOrientationHelper.getStartAfterPadding();
        if (n2 > 0) {
            final int n3 = -this.scrollBy(n2, recycler, state);
            if (b) {
                n = n + n3 - this.mOrientationHelper.getStartAfterPadding();
                if (n > 0) {
                    this.mOrientationHelper.offsetChildren(-n);
                    return n3 - n;
                }
            }
            return n3;
        }
        return 0;
    }
    
    private View getChildClosestToEnd() {
        int n;
        if (this.mShouldReverseLayout) {
            n = 0;
        }
        else {
            n = ((RecyclerView.LayoutManager)this).getChildCount() - 1;
        }
        return ((RecyclerView.LayoutManager)this).getChildAt(n);
    }
    
    private View getChildClosestToStart() {
        int n;
        if (this.mShouldReverseLayout) {
            n = ((RecyclerView.LayoutManager)this).getChildCount() - 1;
        }
        else {
            n = 0;
        }
        return ((RecyclerView.LayoutManager)this).getChildAt(n);
    }
    
    private void layoutForPredictiveAnimations(final Recycler recycler, final State state, final int n, final int n2) {
        if (state.willRunPredictiveAnimations() && ((RecyclerView.LayoutManager)this).getChildCount() != 0 && !state.isPreLayout() && this.supportsPredictiveItemAnimations()) {
            final List<ViewHolder> scrapList = recycler.getScrapList();
            final int size = scrapList.size();
            final int position = ((RecyclerView.LayoutManager)this).getPosition(((RecyclerView.LayoutManager)this).getChildAt(0));
            int i = 0;
            int mExtra2;
            int mExtra = mExtra2 = 0;
            while (i < size) {
                final ViewHolder viewHolder = scrapList.get(i);
                if (!viewHolder.isRemoved()) {
                    final int layoutPosition = viewHolder.getLayoutPosition();
                    int n3 = 1;
                    if (layoutPosition < position != this.mShouldReverseLayout) {
                        n3 = -1;
                    }
                    if (n3 == -1) {
                        mExtra += this.mOrientationHelper.getDecoratedMeasurement(viewHolder.itemView);
                    }
                    else {
                        mExtra2 += this.mOrientationHelper.getDecoratedMeasurement(viewHolder.itemView);
                    }
                }
                ++i;
            }
            this.mLayoutState.mScrapList = scrapList;
            if (mExtra > 0) {
                this.updateLayoutStateToFillStart(((RecyclerView.LayoutManager)this).getPosition(this.getChildClosestToStart()), n);
                this.mLayoutState.mExtra = mExtra;
                this.mLayoutState.mAvailable = 0;
                this.mLayoutState.assignPositionFromScrapList();
                this.fill(recycler, this.mLayoutState, state, false);
            }
            if (mExtra2 > 0) {
                this.updateLayoutStateToFillEnd(((RecyclerView.LayoutManager)this).getPosition(this.getChildClosestToEnd()), n2);
                this.mLayoutState.mExtra = mExtra2;
                this.mLayoutState.mAvailable = 0;
                this.mLayoutState.assignPositionFromScrapList();
                this.fill(recycler, this.mLayoutState, state, false);
            }
            this.mLayoutState.mScrapList = null;
        }
    }
    
    private void logChildren() {
        Log.d("LinearLayoutManager", "internal representation of views on the screen");
        for (int i = 0; i < ((RecyclerView.LayoutManager)this).getChildCount(); ++i) {
            final View child = ((RecyclerView.LayoutManager)this).getChildAt(i);
            final StringBuilder sb = new StringBuilder();
            sb.append("item ");
            sb.append(((RecyclerView.LayoutManager)this).getPosition(child));
            sb.append(", coord:");
            sb.append(this.mOrientationHelper.getDecoratedStart(child));
            Log.d("LinearLayoutManager", sb.toString());
        }
        Log.d("LinearLayoutManager", "==============");
    }
    
    private void recycleByLayoutState(final Recycler recycler, final LayoutState layoutState) {
        if (layoutState.mRecycle && !layoutState.mInfinite) {
            if (layoutState.mLayoutDirection == -1) {
                this.recycleViewsFromEnd(recycler, layoutState.mScrollingOffset);
            }
            else {
                this.recycleViewsFromStart(recycler, layoutState.mScrollingOffset);
            }
        }
    }
    
    private void recycleChildren(final Recycler recycler, final int n, int i) {
        if (n == i) {
            return;
        }
        int j;
        if (i > (j = n)) {
            --i;
            while (i >= n) {
                ((RecyclerView.LayoutManager)this).removeAndRecycleViewAt(i, recycler);
                --i;
            }
        }
        else {
            while (j > i) {
                ((RecyclerView.LayoutManager)this).removeAndRecycleViewAt(j, recycler);
                --j;
            }
        }
    }
    
    private void recycleViewsFromEnd(final Recycler recycler, int i) {
        int childCount = ((RecyclerView.LayoutManager)this).getChildCount();
        if (i < 0) {
            return;
        }
        final int n = this.mOrientationHelper.getEnd() - i;
        if (this.mShouldReverseLayout) {
            View child;
            for (i = 0; i < childCount; ++i) {
                child = ((RecyclerView.LayoutManager)this).getChildAt(i);
                if (this.mOrientationHelper.getDecoratedStart(child) < n || this.mOrientationHelper.getTransformedStartWithDecoration(child) < n) {
                    this.recycleChildren(recycler, 0, i);
                    return;
                }
            }
        }
        else {
            View child2;
            for (i = --childCount; i >= 0; --i) {
                child2 = ((RecyclerView.LayoutManager)this).getChildAt(i);
                if (this.mOrientationHelper.getDecoratedStart(child2) < n || this.mOrientationHelper.getTransformedStartWithDecoration(child2) < n) {
                    this.recycleChildren(recycler, childCount, i);
                    return;
                }
            }
        }
    }
    
    private void recycleViewsFromStart(final Recycler recycler, final int n) {
        if (n < 0) {
            return;
        }
        int childCount = ((RecyclerView.LayoutManager)this).getChildCount();
        if (this.mShouldReverseLayout) {
            for (int i = --childCount; i >= 0; --i) {
                final View child = ((RecyclerView.LayoutManager)this).getChildAt(i);
                if (this.mOrientationHelper.getDecoratedEnd(child) > n || this.mOrientationHelper.getTransformedEndWithDecoration(child) > n) {
                    this.recycleChildren(recycler, childCount, i);
                    return;
                }
            }
        }
        else {
            for (int j = 0; j < childCount; ++j) {
                final View child2 = ((RecyclerView.LayoutManager)this).getChildAt(j);
                if (this.mOrientationHelper.getDecoratedEnd(child2) > n || this.mOrientationHelper.getTransformedEndWithDecoration(child2) > n) {
                    this.recycleChildren(recycler, 0, j);
                    return;
                }
            }
        }
    }
    
    private void resolveShouldLayoutReverse() {
        if (this.mOrientation != 1 && this.isLayoutRTL()) {
            this.mShouldReverseLayout = (this.mReverseLayout ^ true);
        }
        else {
            this.mShouldReverseLayout = this.mReverseLayout;
        }
    }
    
    private boolean updateAnchorFromChildren(final Recycler recycler, final State state, final AnchorInfo anchorInfo) {
        final int childCount = ((RecyclerView.LayoutManager)this).getChildCount();
        boolean b = false;
        if (childCount == 0) {
            return false;
        }
        final View focusedChild = ((RecyclerView.LayoutManager)this).getFocusedChild();
        if (focusedChild != null && anchorInfo.isViewValidAsAnchor(focusedChild, state)) {
            anchorInfo.assignFromViewAndKeepVisibleRect(focusedChild, ((RecyclerView.LayoutManager)this).getPosition(focusedChild));
            return true;
        }
        if (this.mLastStackFromEnd != this.mStackFromEnd) {
            return false;
        }
        View view;
        if (anchorInfo.mLayoutFromEnd) {
            view = this.findReferenceChildClosestToEnd(recycler, state);
        }
        else {
            view = this.findReferenceChildClosestToStart(recycler, state);
        }
        if (view != null) {
            anchorInfo.assignFromView(view, ((RecyclerView.LayoutManager)this).getPosition(view));
            if (!state.isPreLayout() && this.supportsPredictiveItemAnimations()) {
                if (this.mOrientationHelper.getDecoratedStart(view) >= this.mOrientationHelper.getEndAfterPadding() || this.mOrientationHelper.getDecoratedEnd(view) < this.mOrientationHelper.getStartAfterPadding()) {
                    b = true;
                }
                if (b) {
                    int mCoordinate;
                    if (anchorInfo.mLayoutFromEnd) {
                        mCoordinate = this.mOrientationHelper.getEndAfterPadding();
                    }
                    else {
                        mCoordinate = this.mOrientationHelper.getStartAfterPadding();
                    }
                    anchorInfo.mCoordinate = mCoordinate;
                }
            }
            return true;
        }
        return false;
    }
    
    private boolean updateAnchorFromPendingData(final State state, final AnchorInfo anchorInfo) {
        final boolean preLayout = state.isPreLayout();
        boolean mLayoutFromEnd = false;
        if (preLayout || this.mPendingScrollPosition == -1) {
            return false;
        }
        if (this.mPendingScrollPosition < 0 || this.mPendingScrollPosition >= state.getItemCount()) {
            this.mPendingScrollPosition = -1;
            this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
            return false;
        }
        anchorInfo.mPosition = this.mPendingScrollPosition;
        if (this.mPendingSavedState != null && this.mPendingSavedState.hasValidAnchor()) {
            anchorInfo.mLayoutFromEnd = this.mPendingSavedState.mAnchorLayoutFromEnd;
            if (anchorInfo.mLayoutFromEnd) {
                anchorInfo.mCoordinate = this.mOrientationHelper.getEndAfterPadding() - this.mPendingSavedState.mAnchorOffset;
            }
            else {
                anchorInfo.mCoordinate = this.mOrientationHelper.getStartAfterPadding() + this.mPendingSavedState.mAnchorOffset;
            }
            return true;
        }
        if (this.mPendingScrollPositionOffset == Integer.MIN_VALUE) {
            final View viewByPosition = this.findViewByPosition(this.mPendingScrollPosition);
            if (viewByPosition != null) {
                if (this.mOrientationHelper.getDecoratedMeasurement(viewByPosition) > this.mOrientationHelper.getTotalSpace()) {
                    anchorInfo.assignCoordinateFromPadding();
                    return true;
                }
                if (this.mOrientationHelper.getDecoratedStart(viewByPosition) - this.mOrientationHelper.getStartAfterPadding() < 0) {
                    anchorInfo.mCoordinate = this.mOrientationHelper.getStartAfterPadding();
                    anchorInfo.mLayoutFromEnd = false;
                    return true;
                }
                if (this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(viewByPosition) < 0) {
                    anchorInfo.mCoordinate = this.mOrientationHelper.getEndAfterPadding();
                    return anchorInfo.mLayoutFromEnd = true;
                }
                int decoratedStart;
                if (anchorInfo.mLayoutFromEnd) {
                    decoratedStart = this.mOrientationHelper.getDecoratedEnd(viewByPosition) + this.mOrientationHelper.getTotalSpaceChange();
                }
                else {
                    decoratedStart = this.mOrientationHelper.getDecoratedStart(viewByPosition);
                }
                anchorInfo.mCoordinate = decoratedStart;
            }
            else {
                if (((RecyclerView.LayoutManager)this).getChildCount() > 0) {
                    if (this.mPendingScrollPosition < ((RecyclerView.LayoutManager)this).getPosition(((RecyclerView.LayoutManager)this).getChildAt(0)) == this.mShouldReverseLayout) {
                        mLayoutFromEnd = true;
                    }
                    anchorInfo.mLayoutFromEnd = mLayoutFromEnd;
                }
                anchorInfo.assignCoordinateFromPadding();
            }
            return true;
        }
        anchorInfo.mLayoutFromEnd = this.mShouldReverseLayout;
        if (this.mShouldReverseLayout) {
            anchorInfo.mCoordinate = this.mOrientationHelper.getEndAfterPadding() - this.mPendingScrollPositionOffset;
        }
        else {
            anchorInfo.mCoordinate = this.mOrientationHelper.getStartAfterPadding() + this.mPendingScrollPositionOffset;
        }
        return true;
    }
    
    private void updateAnchorInfoForLayout(final Recycler recycler, final State state, final AnchorInfo anchorInfo) {
        if (this.updateAnchorFromPendingData(state, anchorInfo)) {
            return;
        }
        if (this.updateAnchorFromChildren(recycler, state, anchorInfo)) {
            return;
        }
        anchorInfo.assignCoordinateFromPadding();
        int mPosition;
        if (this.mStackFromEnd) {
            mPosition = state.getItemCount() - 1;
        }
        else {
            mPosition = 0;
        }
        anchorInfo.mPosition = mPosition;
    }
    
    private void updateLayoutState(int n, final int mAvailable, final boolean b, final State state) {
        this.mLayoutState.mInfinite = this.resolveIsInfinite();
        this.mLayoutState.mExtra = this.getExtraLayoutSpace(state);
        this.mLayoutState.mLayoutDirection = n;
        int n2 = -1;
        if (n == 1) {
            final LayoutState mLayoutState = this.mLayoutState;
            mLayoutState.mExtra += this.mOrientationHelper.getEndPadding();
            final View childClosestToEnd = this.getChildClosestToEnd();
            final LayoutState mLayoutState2 = this.mLayoutState;
            if (!this.mShouldReverseLayout) {
                n2 = 1;
            }
            mLayoutState2.mItemDirection = n2;
            this.mLayoutState.mCurrentPosition = ((RecyclerView.LayoutManager)this).getPosition(childClosestToEnd) + this.mLayoutState.mItemDirection;
            this.mLayoutState.mOffset = this.mOrientationHelper.getDecoratedEnd(childClosestToEnd);
            n = this.mOrientationHelper.getDecoratedEnd(childClosestToEnd) - this.mOrientationHelper.getEndAfterPadding();
        }
        else {
            final View childClosestToStart = this.getChildClosestToStart();
            final LayoutState mLayoutState3 = this.mLayoutState;
            mLayoutState3.mExtra += this.mOrientationHelper.getStartAfterPadding();
            final LayoutState mLayoutState4 = this.mLayoutState;
            if (this.mShouldReverseLayout) {
                n2 = 1;
            }
            mLayoutState4.mItemDirection = n2;
            this.mLayoutState.mCurrentPosition = ((RecyclerView.LayoutManager)this).getPosition(childClosestToStart) + this.mLayoutState.mItemDirection;
            this.mLayoutState.mOffset = this.mOrientationHelper.getDecoratedStart(childClosestToStart);
            n = -this.mOrientationHelper.getDecoratedStart(childClosestToStart) + this.mOrientationHelper.getStartAfterPadding();
        }
        this.mLayoutState.mAvailable = mAvailable;
        if (b) {
            final LayoutState mLayoutState5 = this.mLayoutState;
            mLayoutState5.mAvailable -= n;
        }
        this.mLayoutState.mScrollingOffset = n;
    }
    
    private void updateLayoutStateToFillEnd(final int mCurrentPosition, final int mOffset) {
        this.mLayoutState.mAvailable = this.mOrientationHelper.getEndAfterPadding() - mOffset;
        final LayoutState mLayoutState = this.mLayoutState;
        int mItemDirection;
        if (this.mShouldReverseLayout) {
            mItemDirection = -1;
        }
        else {
            mItemDirection = 1;
        }
        mLayoutState.mItemDirection = mItemDirection;
        this.mLayoutState.mCurrentPosition = mCurrentPosition;
        this.mLayoutState.mLayoutDirection = 1;
        this.mLayoutState.mOffset = mOffset;
        this.mLayoutState.mScrollingOffset = Integer.MIN_VALUE;
    }
    
    private void updateLayoutStateToFillEnd(final AnchorInfo anchorInfo) {
        this.updateLayoutStateToFillEnd(anchorInfo.mPosition, anchorInfo.mCoordinate);
    }
    
    private void updateLayoutStateToFillStart(int n, final int mOffset) {
        this.mLayoutState.mAvailable = mOffset - this.mOrientationHelper.getStartAfterPadding();
        this.mLayoutState.mCurrentPosition = n;
        final LayoutState mLayoutState = this.mLayoutState;
        if (this.mShouldReverseLayout) {
            n = 1;
        }
        else {
            n = -1;
        }
        mLayoutState.mItemDirection = n;
        this.mLayoutState.mLayoutDirection = -1;
        this.mLayoutState.mOffset = mOffset;
        this.mLayoutState.mScrollingOffset = Integer.MIN_VALUE;
    }
    
    private void updateLayoutStateToFillStart(final AnchorInfo anchorInfo) {
        this.updateLayoutStateToFillStart(anchorInfo.mPosition, anchorInfo.mCoordinate);
    }
    
    @Override
    public void assertNotInLayoutOrScroll(final String s) {
        if (this.mPendingSavedState == null) {
            super.assertNotInLayoutOrScroll(s);
        }
    }
    
    @Override
    public boolean canScrollHorizontally() {
        return this.mOrientation == 0;
    }
    
    @Override
    public boolean canScrollVertically() {
        final int mOrientation = this.mOrientation;
        boolean b = true;
        if (mOrientation != 1) {
            b = false;
        }
        return b;
    }
    
    @Override
    public void collectAdjacentPrefetchPositions(int a, int n, final State state, final LayoutPrefetchRegistry layoutPrefetchRegistry) {
        if (this.mOrientation != 0) {
            a = n;
        }
        if (((RecyclerView.LayoutManager)this).getChildCount() != 0 && a != 0) {
            this.ensureLayoutState();
            if (a > 0) {
                n = 1;
            }
            else {
                n = -1;
            }
            this.updateLayoutState(n, Math.abs(a), true, state);
            this.collectPrefetchPositionsForLayoutState(state, this.mLayoutState, layoutPrefetchRegistry);
        }
    }
    
    @Override
    public void collectInitialPrefetchPositions(final int n, final LayoutPrefetchRegistry layoutPrefetchRegistry) {
        final SavedState mPendingSavedState = this.mPendingSavedState;
        int n2 = -1;
        boolean b;
        int n3;
        if (mPendingSavedState != null && this.mPendingSavedState.hasValidAnchor()) {
            b = this.mPendingSavedState.mAnchorLayoutFromEnd;
            n3 = this.mPendingSavedState.mAnchorPosition;
        }
        else {
            this.resolveShouldLayoutReverse();
            b = this.mShouldReverseLayout;
            if (this.mPendingScrollPosition == -1) {
                if (b) {
                    n3 = n - 1;
                }
                else {
                    n3 = 0;
                }
            }
            else {
                n3 = this.mPendingScrollPosition;
            }
        }
        if (!b) {
            n2 = 1;
        }
        final int n4 = 0;
        for (int n5 = n3, n6 = n4; n6 < this.mInitialPrefetchItemCount && n5 >= 0 && n5 < n; n5 += n2, ++n6) {
            layoutPrefetchRegistry.addPosition(n5, 0);
        }
    }
    
    void collectPrefetchPositionsForLayoutState(final State state, final LayoutState layoutState, final LayoutPrefetchRegistry layoutPrefetchRegistry) {
        final int mCurrentPosition = layoutState.mCurrentPosition;
        if (mCurrentPosition >= 0 && mCurrentPosition < state.getItemCount()) {
            layoutPrefetchRegistry.addPosition(mCurrentPosition, Math.max(0, layoutState.mScrollingOffset));
        }
    }
    
    @Override
    public int computeHorizontalScrollExtent(final State state) {
        return this.computeScrollExtent(state);
    }
    
    @Override
    public int computeHorizontalScrollOffset(final State state) {
        return this.computeScrollOffset(state);
    }
    
    @Override
    public int computeHorizontalScrollRange(final State state) {
        return this.computeScrollRange(state);
    }
    
    @Override
    public PointF computeScrollVectorForPosition(int n) {
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0) {
            return null;
        }
        boolean b = false;
        final int position = ((RecyclerView.LayoutManager)this).getPosition(((RecyclerView.LayoutManager)this).getChildAt(0));
        final int n2 = 1;
        if (n < position) {
            b = true;
        }
        n = n2;
        if (b != this.mShouldReverseLayout) {
            n = -1;
        }
        if (this.mOrientation == 0) {
            return new PointF((float)n, 0.0f);
        }
        return new PointF(0.0f, (float)n);
    }
    
    @Override
    public int computeVerticalScrollExtent(final State state) {
        return this.computeScrollExtent(state);
    }
    
    @Override
    public int computeVerticalScrollOffset(final State state) {
        return this.computeScrollOffset(state);
    }
    
    @Override
    public int computeVerticalScrollRange(final State state) {
        return this.computeScrollRange(state);
    }
    
    int convertFocusDirectionToLayoutDirection(final int n) {
        int n2 = -1;
        int n3 = Integer.MIN_VALUE;
        if (n == 17) {
            if (this.mOrientation != 0) {
                n2 = Integer.MIN_VALUE;
            }
            return n2;
        }
        if (n == 33) {
            if (this.mOrientation != 1) {
                n2 = Integer.MIN_VALUE;
            }
            return n2;
        }
        if (n == 66) {
            if (this.mOrientation == 0) {
                n3 = 1;
            }
            return n3;
        }
        if (n == 130) {
            if (this.mOrientation == 1) {
                n3 = 1;
            }
            return n3;
        }
        switch (n) {
            default: {
                return Integer.MIN_VALUE;
            }
            case 2: {
                if (this.mOrientation == 1) {
                    return 1;
                }
                if (this.isLayoutRTL()) {
                    return -1;
                }
                return 1;
            }
            case 1: {
                if (this.mOrientation == 1) {
                    return -1;
                }
                if (this.isLayoutRTL()) {
                    return 1;
                }
                return -1;
            }
        }
    }
    
    LayoutState createLayoutState() {
        return new LayoutState();
    }
    
    void ensureLayoutState() {
        if (this.mLayoutState == null) {
            this.mLayoutState = this.createLayoutState();
        }
    }
    
    int fill(final Recycler recycler, final LayoutState layoutState, final State state, final boolean b) {
        final int mAvailable = layoutState.mAvailable;
        if (layoutState.mScrollingOffset != Integer.MIN_VALUE) {
            if (layoutState.mAvailable < 0) {
                layoutState.mScrollingOffset += layoutState.mAvailable;
            }
            this.recycleByLayoutState(recycler, layoutState);
        }
        int n = layoutState.mAvailable + layoutState.mExtra;
        final LayoutChunkResult mLayoutChunkResult = this.mLayoutChunkResult;
        while ((layoutState.mInfinite || n > 0) && layoutState.hasMore(state)) {
            mLayoutChunkResult.resetInternal();
            this.layoutChunk(recycler, state, layoutState, mLayoutChunkResult);
            if (mLayoutChunkResult.mFinished) {
                break;
            }
            layoutState.mOffset += mLayoutChunkResult.mConsumed * layoutState.mLayoutDirection;
            int n2 = 0;
            Label_0175: {
                if (mLayoutChunkResult.mIgnoreConsumed && this.mLayoutState.mScrapList == null) {
                    n2 = n;
                    if (state.isPreLayout()) {
                        break Label_0175;
                    }
                }
                layoutState.mAvailable -= mLayoutChunkResult.mConsumed;
                n2 = n - mLayoutChunkResult.mConsumed;
            }
            if (layoutState.mScrollingOffset != Integer.MIN_VALUE) {
                layoutState.mScrollingOffset += mLayoutChunkResult.mConsumed;
                if (layoutState.mAvailable < 0) {
                    layoutState.mScrollingOffset += layoutState.mAvailable;
                }
                this.recycleByLayoutState(recycler, layoutState);
            }
            n = n2;
            if (!b) {
                continue;
            }
            n = n2;
            if (mLayoutChunkResult.mFocusable) {
                break;
            }
        }
        return mAvailable - layoutState.mAvailable;
    }
    
    public int findFirstCompletelyVisibleItemPosition() {
        final View oneVisibleChild = this.findOneVisibleChild(0, ((RecyclerView.LayoutManager)this).getChildCount(), true, false);
        int position;
        if (oneVisibleChild == null) {
            position = -1;
        }
        else {
            position = ((RecyclerView.LayoutManager)this).getPosition(oneVisibleChild);
        }
        return position;
    }
    
    public int findFirstVisibleItemPosition() {
        final View oneVisibleChild = this.findOneVisibleChild(0, ((RecyclerView.LayoutManager)this).getChildCount(), false, true);
        int position;
        if (oneVisibleChild == null) {
            position = -1;
        }
        else {
            position = ((RecyclerView.LayoutManager)this).getPosition(oneVisibleChild);
        }
        return position;
    }
    
    public int findLastCompletelyVisibleItemPosition() {
        final int childCount = ((RecyclerView.LayoutManager)this).getChildCount();
        int position = -1;
        final View oneVisibleChild = this.findOneVisibleChild(childCount - 1, -1, true, false);
        if (oneVisibleChild != null) {
            position = ((RecyclerView.LayoutManager)this).getPosition(oneVisibleChild);
        }
        return position;
    }
    
    public int findLastVisibleItemPosition() {
        final int childCount = ((RecyclerView.LayoutManager)this).getChildCount();
        int position = -1;
        final View oneVisibleChild = this.findOneVisibleChild(childCount - 1, -1, false, true);
        if (oneVisibleChild != null) {
            position = ((RecyclerView.LayoutManager)this).getPosition(oneVisibleChild);
        }
        return position;
    }
    
    View findOnePartiallyOrCompletelyInvisibleChild(final int n, final int n2) {
        this.ensureLayoutState();
        int n3;
        if (n2 > n) {
            n3 = 1;
        }
        else if (n2 < n) {
            n3 = -1;
        }
        else {
            n3 = 0;
        }
        if (n3 == 0) {
            return ((RecyclerView.LayoutManager)this).getChildAt(n);
        }
        int n4;
        int n5;
        if (this.mOrientationHelper.getDecoratedStart(((RecyclerView.LayoutManager)this).getChildAt(n)) < this.mOrientationHelper.getStartAfterPadding()) {
            n4 = 16644;
            n5 = 16388;
        }
        else {
            n4 = 4161;
            n5 = 4097;
        }
        View view;
        if (this.mOrientation == 0) {
            view = this.mHorizontalBoundCheck.findOneViewWithinBoundFlags(n, n2, n4, n5);
        }
        else {
            view = this.mVerticalBoundCheck.findOneViewWithinBoundFlags(n, n2, n4, n5);
        }
        return view;
    }
    
    View findOneVisibleChild(final int n, final int n2, final boolean b, final boolean b2) {
        this.ensureLayoutState();
        int n3 = 320;
        int n4;
        if (b) {
            n4 = 24579;
        }
        else {
            n4 = 320;
        }
        if (!b2) {
            n3 = 0;
        }
        View view;
        if (this.mOrientation == 0) {
            view = this.mHorizontalBoundCheck.findOneViewWithinBoundFlags(n, n2, n4, n3);
        }
        else {
            view = this.mVerticalBoundCheck.findOneViewWithinBoundFlags(n, n2, n4, n3);
        }
        return view;
    }
    
    View findReferenceChild(final Recycler recycler, final State state, int i, final int n, final int n2) {
        this.ensureLayoutState();
        final int startAfterPadding = this.mOrientationHelper.getStartAfterPadding();
        final int endAfterPadding = this.mOrientationHelper.getEndAfterPadding();
        int n3;
        if (n > i) {
            n3 = 1;
        }
        else {
            n3 = -1;
        }
        View view = null;
        View view2 = null;
        while (i != n) {
            final View child = ((RecyclerView.LayoutManager)this).getChildAt(i);
            final int position = ((RecyclerView.LayoutManager)this).getPosition(child);
            View view3 = view;
            View view4 = view2;
            if (position >= 0) {
                view3 = view;
                view4 = view2;
                if (position < n2) {
                    if (((LayoutParams)child.getLayoutParams()).isItemRemoved()) {
                        view3 = view;
                        if ((view4 = view2) == null) {
                            view4 = child;
                            view3 = view;
                        }
                    }
                    else {
                        if (this.mOrientationHelper.getDecoratedStart(child) < endAfterPadding && this.mOrientationHelper.getDecoratedEnd(child) >= startAfterPadding) {
                            return child;
                        }
                        view3 = view;
                        view4 = view2;
                        if (view == null) {
                            view3 = child;
                            view4 = view2;
                        }
                    }
                }
            }
            i += n3;
            view = view3;
            view2 = view4;
        }
        if (view != null) {
            view2 = view;
        }
        return view2;
    }
    
    @Override
    public View findViewByPosition(final int n) {
        final int childCount = ((RecyclerView.LayoutManager)this).getChildCount();
        if (childCount == 0) {
            return null;
        }
        final int n2 = n - ((RecyclerView.LayoutManager)this).getPosition(((RecyclerView.LayoutManager)this).getChildAt(0));
        if (n2 >= 0 && n2 < childCount) {
            final View child = ((RecyclerView.LayoutManager)this).getChildAt(n2);
            if (((RecyclerView.LayoutManager)this).getPosition(child) == n) {
                return child;
            }
        }
        return super.findViewByPosition(n);
    }
    
    @Override
    public LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(-2, -2);
    }
    
    protected int getExtraLayoutSpace(final State state) {
        if (state.hasTargetScrollPosition()) {
            return this.mOrientationHelper.getTotalSpace();
        }
        return 0;
    }
    
    public int getInitialPrefetchItemCount() {
        return this.mInitialPrefetchItemCount;
    }
    
    public int getOrientation() {
        return this.mOrientation;
    }
    
    public boolean getRecycleChildrenOnDetach() {
        return this.mRecycleChildrenOnDetach;
    }
    
    public boolean getReverseLayout() {
        return this.mReverseLayout;
    }
    
    public boolean getStackFromEnd() {
        return this.mStackFromEnd;
    }
    
    @Override
    public boolean isAutoMeasureEnabled() {
        return true;
    }
    
    protected boolean isLayoutRTL() {
        final int layoutDirection = ((RecyclerView.LayoutManager)this).getLayoutDirection();
        boolean b = true;
        if (layoutDirection != 1) {
            b = false;
        }
        return b;
    }
    
    public boolean isSmoothScrollbarEnabled() {
        return this.mSmoothScrollbarEnabled;
    }
    
    void layoutChunk(final Recycler recycler, final State state, final LayoutState layoutState, final LayoutChunkResult layoutChunkResult) {
        final View next = layoutState.next(recycler);
        if (next == null) {
            layoutChunkResult.mFinished = true;
            return;
        }
        final LayoutParams layoutParams = (LayoutParams)next.getLayoutParams();
        if (layoutState.mScrapList == null) {
            if (this.mShouldReverseLayout == (layoutState.mLayoutDirection == -1)) {
                ((RecyclerView.LayoutManager)this).addView(next);
            }
            else {
                ((RecyclerView.LayoutManager)this).addView(next, 0);
            }
        }
        else if (this.mShouldReverseLayout == (layoutState.mLayoutDirection == -1)) {
            ((RecyclerView.LayoutManager)this).addDisappearingView(next);
        }
        else {
            ((RecyclerView.LayoutManager)this).addDisappearingView(next, 0);
        }
        ((RecyclerView.LayoutManager)this).measureChildWithMargins(next, 0, 0);
        layoutChunkResult.mConsumed = this.mOrientationHelper.getDecoratedMeasurement(next);
        int paddingLeft;
        int mOffset;
        int mOffset2;
        int n3;
        if (this.mOrientation == 1) {
            int n;
            if (this.isLayoutRTL()) {
                n = ((RecyclerView.LayoutManager)this).getWidth() - ((RecyclerView.LayoutManager)this).getPaddingRight();
                paddingLeft = n - this.mOrientationHelper.getDecoratedMeasurementInOther(next);
            }
            else {
                paddingLeft = ((RecyclerView.LayoutManager)this).getPaddingLeft();
                n = this.mOrientationHelper.getDecoratedMeasurementInOther(next) + paddingLeft;
            }
            if (layoutState.mLayoutDirection == -1) {
                mOffset = layoutState.mOffset;
                final int n2 = layoutState.mOffset - layoutChunkResult.mConsumed;
                mOffset2 = n;
                n3 = n2;
            }
            else {
                final int mOffset3 = layoutState.mOffset;
                mOffset = layoutState.mOffset + layoutChunkResult.mConsumed;
                mOffset2 = n;
                n3 = mOffset3;
            }
        }
        else {
            final int paddingTop = ((RecyclerView.LayoutManager)this).getPaddingTop();
            final int n4 = this.mOrientationHelper.getDecoratedMeasurementInOther(next) + paddingTop;
            if (layoutState.mLayoutDirection == -1) {
                mOffset2 = layoutState.mOffset;
                final int mOffset4 = layoutState.mOffset;
                final int mConsumed = layoutChunkResult.mConsumed;
                final int n5 = paddingTop;
                mOffset = n4;
                paddingLeft = mOffset4 - mConsumed;
                n3 = n5;
            }
            else {
                final int mOffset5 = layoutState.mOffset;
                mOffset2 = layoutState.mOffset + layoutChunkResult.mConsumed;
                mOffset = n4;
                n3 = paddingTop;
                paddingLeft = mOffset5;
            }
        }
        ((RecyclerView.LayoutManager)this).layoutDecoratedWithMargins(next, paddingLeft, n3, mOffset2, mOffset);
        if (layoutParams.isItemRemoved() || layoutParams.isItemChanged()) {
            layoutChunkResult.mIgnoreConsumed = true;
        }
        layoutChunkResult.mFocusable = next.hasFocusable();
    }
    
    void onAnchorReady(final Recycler recycler, final State state, final AnchorInfo anchorInfo, final int n) {
    }
    
    @Override
    public void onDetachedFromWindow(final RecyclerView recyclerView, final Recycler recycler) {
        super.onDetachedFromWindow(recyclerView, recycler);
        if (this.mRecycleChildrenOnDetach) {
            ((RecyclerView.LayoutManager)this).removeAndRecycleAllViews(recycler);
            recycler.clear();
        }
    }
    
    @Override
    public View onFocusSearchFailed(View view, int convertFocusDirectionToLayoutDirection, final Recycler recycler, final State state) {
        this.resolveShouldLayoutReverse();
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0) {
            return null;
        }
        convertFocusDirectionToLayoutDirection = this.convertFocusDirectionToLayoutDirection(convertFocusDirectionToLayoutDirection);
        if (convertFocusDirectionToLayoutDirection == Integer.MIN_VALUE) {
            return null;
        }
        this.ensureLayoutState();
        this.ensureLayoutState();
        this.updateLayoutState(convertFocusDirectionToLayoutDirection, (int)(0.33333334f * this.mOrientationHelper.getTotalSpace()), false, state);
        this.mLayoutState.mScrollingOffset = Integer.MIN_VALUE;
        this.mLayoutState.mRecycle = false;
        this.fill(recycler, this.mLayoutState, state, true);
        if (convertFocusDirectionToLayoutDirection == -1) {
            view = this.findPartiallyOrCompletelyInvisibleChildClosestToStart(recycler, state);
        }
        else {
            view = this.findPartiallyOrCompletelyInvisibleChildClosestToEnd(recycler, state);
        }
        View view2;
        if (convertFocusDirectionToLayoutDirection == -1) {
            view2 = this.getChildClosestToStart();
        }
        else {
            view2 = this.getChildClosestToEnd();
        }
        if (!view2.hasFocusable()) {
            return view;
        }
        if (view == null) {
            return null;
        }
        return view2;
    }
    
    @Override
    public void onInitializeAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        if (((RecyclerView.LayoutManager)this).getChildCount() > 0) {
            accessibilityEvent.setFromIndex(this.findFirstVisibleItemPosition());
            accessibilityEvent.setToIndex(this.findLastVisibleItemPosition());
        }
    }
    
    @Override
    public void onLayoutChildren(final Recycler recycler, final State state) {
        final SavedState mPendingSavedState = this.mPendingSavedState;
        int n = -1;
        if ((mPendingSavedState != null || this.mPendingScrollPosition != -1) && state.getItemCount() == 0) {
            ((RecyclerView.LayoutManager)this).removeAndRecycleAllViews(recycler);
            return;
        }
        if (this.mPendingSavedState != null && this.mPendingSavedState.hasValidAnchor()) {
            this.mPendingScrollPosition = this.mPendingSavedState.mAnchorPosition;
        }
        this.ensureLayoutState();
        this.mLayoutState.mRecycle = false;
        this.resolveShouldLayoutReverse();
        final View focusedChild = ((RecyclerView.LayoutManager)this).getFocusedChild();
        if (this.mAnchorInfo.mValid && this.mPendingScrollPosition == -1 && this.mPendingSavedState == null) {
            if (focusedChild != null && (this.mOrientationHelper.getDecoratedStart(focusedChild) >= this.mOrientationHelper.getEndAfterPadding() || this.mOrientationHelper.getDecoratedEnd(focusedChild) <= this.mOrientationHelper.getStartAfterPadding())) {
                this.mAnchorInfo.assignFromViewAndKeepVisibleRect(focusedChild, ((RecyclerView.LayoutManager)this).getPosition(focusedChild));
            }
        }
        else {
            this.mAnchorInfo.reset();
            this.mAnchorInfo.mLayoutFromEnd = (this.mShouldReverseLayout ^ this.mStackFromEnd);
            this.updateAnchorInfoForLayout(recycler, state, this.mAnchorInfo);
            this.mAnchorInfo.mValid = true;
        }
        int extraLayoutSpace = this.getExtraLayoutSpace(state);
        int n2;
        if (this.mLayoutState.mLastScrollDelta >= 0) {
            n2 = extraLayoutSpace;
            extraLayoutSpace = 0;
        }
        else {
            n2 = 0;
        }
        final int n3 = extraLayoutSpace + this.mOrientationHelper.getStartAfterPadding();
        final int n4 = n2 + this.mOrientationHelper.getEndPadding();
        int mExtra = n3;
        int mExtra2 = n4;
        if (state.isPreLayout()) {
            mExtra = n3;
            mExtra2 = n4;
            if (this.mPendingScrollPosition != -1) {
                mExtra = n3;
                mExtra2 = n4;
                if (this.mPendingScrollPositionOffset != Integer.MIN_VALUE) {
                    final View viewByPosition = this.findViewByPosition(this.mPendingScrollPosition);
                    mExtra = n3;
                    mExtra2 = n4;
                    if (viewByPosition != null) {
                        int n5;
                        if (this.mShouldReverseLayout) {
                            n5 = this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(viewByPosition) - this.mPendingScrollPositionOffset;
                        }
                        else {
                            n5 = this.mPendingScrollPositionOffset - (this.mOrientationHelper.getDecoratedStart(viewByPosition) - this.mOrientationHelper.getStartAfterPadding());
                        }
                        if (n5 > 0) {
                            mExtra = n3 + n5;
                            mExtra2 = n4;
                        }
                        else {
                            mExtra2 = n4 - n5;
                            mExtra = n3;
                        }
                    }
                }
            }
        }
        Label_0450: {
            if (this.mAnchorInfo.mLayoutFromEnd) {
                if (!this.mShouldReverseLayout) {
                    break Label_0450;
                }
            }
            else if (this.mShouldReverseLayout) {
                break Label_0450;
            }
            n = 1;
        }
        this.onAnchorReady(recycler, state, this.mAnchorInfo, n);
        ((RecyclerView.LayoutManager)this).detachAndScrapAttachedViews(recycler);
        this.mLayoutState.mInfinite = this.resolveIsInfinite();
        this.mLayoutState.mIsPreLayout = state.isPreLayout();
        int n6;
        int mOffset3;
        if (this.mAnchorInfo.mLayoutFromEnd) {
            this.updateLayoutStateToFillStart(this.mAnchorInfo);
            this.mLayoutState.mExtra = mExtra;
            this.fill(recycler, this.mLayoutState, state, false);
            final int mOffset = this.mLayoutState.mOffset;
            final int mCurrentPosition = this.mLayoutState.mCurrentPosition;
            int mExtra3 = mExtra2;
            if (this.mLayoutState.mAvailable > 0) {
                mExtra3 = mExtra2 + this.mLayoutState.mAvailable;
            }
            this.updateLayoutStateToFillEnd(this.mAnchorInfo);
            this.mLayoutState.mExtra = mExtra3;
            final LayoutState mLayoutState = this.mLayoutState;
            mLayoutState.mCurrentPosition += this.mLayoutState.mItemDirection;
            this.fill(recycler, this.mLayoutState, state, false);
            final int mOffset2 = this.mLayoutState.mOffset;
            n6 = mOffset;
            mOffset3 = mOffset2;
            if (this.mLayoutState.mAvailable > 0) {
                final int mAvailable = this.mLayoutState.mAvailable;
                this.updateLayoutStateToFillStart(mCurrentPosition, mOffset);
                this.mLayoutState.mExtra = mAvailable;
                this.fill(recycler, this.mLayoutState, state, false);
                n6 = this.mLayoutState.mOffset;
                mOffset3 = mOffset2;
            }
        }
        else {
            this.updateLayoutStateToFillEnd(this.mAnchorInfo);
            this.mLayoutState.mExtra = mExtra2;
            this.fill(recycler, this.mLayoutState, state, false);
            final int mOffset4 = this.mLayoutState.mOffset;
            final int mCurrentPosition2 = this.mLayoutState.mCurrentPosition;
            int mExtra4 = mExtra;
            if (this.mLayoutState.mAvailable > 0) {
                mExtra4 = mExtra + this.mLayoutState.mAvailable;
            }
            this.updateLayoutStateToFillStart(this.mAnchorInfo);
            this.mLayoutState.mExtra = mExtra4;
            final LayoutState mLayoutState2 = this.mLayoutState;
            mLayoutState2.mCurrentPosition += this.mLayoutState.mItemDirection;
            this.fill(recycler, this.mLayoutState, state, false);
            final int n7 = n6 = this.mLayoutState.mOffset;
            mOffset3 = mOffset4;
            if (this.mLayoutState.mAvailable > 0) {
                final int mAvailable2 = this.mLayoutState.mAvailable;
                this.updateLayoutStateToFillEnd(mCurrentPosition2, mOffset4);
                this.mLayoutState.mExtra = mAvailable2;
                this.fill(recycler, this.mLayoutState, state, false);
                mOffset3 = this.mLayoutState.mOffset;
                n6 = n7;
            }
        }
        int n8 = n6;
        int n9 = mOffset3;
        if (((RecyclerView.LayoutManager)this).getChildCount() > 0) {
            if (this.mShouldReverseLayout ^ this.mStackFromEnd) {
                final int fixLayoutEndGap = this.fixLayoutEndGap(mOffset3, recycler, state, true);
                final int n10 = n6 + fixLayoutEndGap;
                final int fixLayoutStartGap = this.fixLayoutStartGap(n10, recycler, state, false);
                n8 = n10 + fixLayoutStartGap;
                n9 = mOffset3 + fixLayoutEndGap + fixLayoutStartGap;
            }
            else {
                final int fixLayoutStartGap2 = this.fixLayoutStartGap(n6, recycler, state, true);
                final int n11 = mOffset3 + fixLayoutStartGap2;
                final int fixLayoutEndGap2 = this.fixLayoutEndGap(n11, recycler, state, false);
                n8 = n6 + fixLayoutStartGap2 + fixLayoutEndGap2;
                n9 = n11 + fixLayoutEndGap2;
            }
        }
        this.layoutForPredictiveAnimations(recycler, state, n8, n9);
        if (!state.isPreLayout()) {
            this.mOrientationHelper.onLayoutComplete();
        }
        else {
            this.mAnchorInfo.reset();
        }
        this.mLastStackFromEnd = this.mStackFromEnd;
    }
    
    @Override
    public void onLayoutCompleted(final State state) {
        super.onLayoutCompleted(state);
        this.mPendingSavedState = null;
        this.mPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        this.mAnchorInfo.reset();
    }
    
    @Override
    public void onRestoreInstanceState(final Parcelable parcelable) {
        if (parcelable instanceof SavedState) {
            this.mPendingSavedState = (SavedState)parcelable;
            ((RecyclerView.LayoutManager)this).requestLayout();
        }
    }
    
    @Override
    public Parcelable onSaveInstanceState() {
        if (this.mPendingSavedState != null) {
            return (Parcelable)new SavedState(this.mPendingSavedState);
        }
        final SavedState savedState = new SavedState();
        if (((RecyclerView.LayoutManager)this).getChildCount() > 0) {
            this.ensureLayoutState();
            final boolean mAnchorLayoutFromEnd = this.mLastStackFromEnd ^ this.mShouldReverseLayout;
            savedState.mAnchorLayoutFromEnd = mAnchorLayoutFromEnd;
            if (mAnchorLayoutFromEnd) {
                final View childClosestToEnd = this.getChildClosestToEnd();
                savedState.mAnchorOffset = this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(childClosestToEnd);
                savedState.mAnchorPosition = ((RecyclerView.LayoutManager)this).getPosition(childClosestToEnd);
            }
            else {
                final View childClosestToStart = this.getChildClosestToStart();
                savedState.mAnchorPosition = ((RecyclerView.LayoutManager)this).getPosition(childClosestToStart);
                savedState.mAnchorOffset = this.mOrientationHelper.getDecoratedStart(childClosestToStart) - this.mOrientationHelper.getStartAfterPadding();
            }
        }
        else {
            savedState.invalidateAnchor();
        }
        return (Parcelable)savedState;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    @Override
    public void prepareForDrop(@NonNull final View view, @NonNull final View view2, int position, int position2) {
        this.assertNotInLayoutOrScroll("Cannot drop a view during a scroll or layout calculation");
        this.ensureLayoutState();
        this.resolveShouldLayoutReverse();
        position = ((RecyclerView.LayoutManager)this).getPosition(view);
        position2 = ((RecyclerView.LayoutManager)this).getPosition(view2);
        if (position < position2) {
            position = 1;
        }
        else {
            position = -1;
        }
        if (this.mShouldReverseLayout) {
            if (position == 1) {
                this.scrollToPositionWithOffset(position2, this.mOrientationHelper.getEndAfterPadding() - (this.mOrientationHelper.getDecoratedStart(view2) + this.mOrientationHelper.getDecoratedMeasurement(view)));
            }
            else {
                this.scrollToPositionWithOffset(position2, this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(view2));
            }
        }
        else if (position == -1) {
            this.scrollToPositionWithOffset(position2, this.mOrientationHelper.getDecoratedStart(view2));
        }
        else {
            this.scrollToPositionWithOffset(position2, this.mOrientationHelper.getDecoratedEnd(view2) - this.mOrientationHelper.getDecoratedMeasurement(view));
        }
    }
    
    boolean resolveIsInfinite() {
        return this.mOrientationHelper.getMode() == 0 && this.mOrientationHelper.getEnd() == 0;
    }
    
    int scrollBy(int n, final Recycler recycler, final State state) {
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0 || n == 0) {
            return 0;
        }
        this.mLayoutState.mRecycle = true;
        this.ensureLayoutState();
        int n2;
        if (n > 0) {
            n2 = 1;
        }
        else {
            n2 = -1;
        }
        final int abs = Math.abs(n);
        this.updateLayoutState(n2, abs, true, state);
        final int n3 = this.mLayoutState.mScrollingOffset + this.fill(recycler, this.mLayoutState, state, false);
        if (n3 < 0) {
            return 0;
        }
        if (abs > n3) {
            n = n2 * n3;
        }
        this.mOrientationHelper.offsetChildren(-n);
        return this.mLayoutState.mLastScrollDelta = n;
    }
    
    @Override
    public int scrollHorizontallyBy(final int n, final Recycler recycler, final State state) {
        if (this.mOrientation == 1) {
            return 0;
        }
        return this.scrollBy(n, recycler, state);
    }
    
    @Override
    public void scrollToPosition(final int mPendingScrollPosition) {
        this.mPendingScrollPosition = mPendingScrollPosition;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        if (this.mPendingSavedState != null) {
            this.mPendingSavedState.invalidateAnchor();
        }
        ((RecyclerView.LayoutManager)this).requestLayout();
    }
    
    public void scrollToPositionWithOffset(final int mPendingScrollPosition, final int mPendingScrollPositionOffset) {
        this.mPendingScrollPosition = mPendingScrollPosition;
        this.mPendingScrollPositionOffset = mPendingScrollPositionOffset;
        if (this.mPendingSavedState != null) {
            this.mPendingSavedState.invalidateAnchor();
        }
        ((RecyclerView.LayoutManager)this).requestLayout();
    }
    
    @Override
    public int scrollVerticallyBy(final int n, final Recycler recycler, final State state) {
        if (this.mOrientation == 0) {
            return 0;
        }
        return this.scrollBy(n, recycler, state);
    }
    
    public void setInitialPrefetchItemCount(final int mInitialPrefetchItemCount) {
        this.mInitialPrefetchItemCount = mInitialPrefetchItemCount;
    }
    
    public void setOrientation(final int n) {
        if (n != 0 && n != 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invalid orientation:");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
        this.assertNotInLayoutOrScroll(null);
        if (n != this.mOrientation || this.mOrientationHelper == null) {
            this.mOrientationHelper = OrientationHelper.createOrientationHelper(this, n);
            this.mAnchorInfo.mOrientationHelper = this.mOrientationHelper;
            this.mOrientation = n;
            ((RecyclerView.LayoutManager)this).requestLayout();
        }
    }
    
    public void setRecycleChildrenOnDetach(final boolean mRecycleChildrenOnDetach) {
        this.mRecycleChildrenOnDetach = mRecycleChildrenOnDetach;
    }
    
    public void setReverseLayout(final boolean mReverseLayout) {
        this.assertNotInLayoutOrScroll(null);
        if (mReverseLayout == this.mReverseLayout) {
            return;
        }
        this.mReverseLayout = mReverseLayout;
        ((RecyclerView.LayoutManager)this).requestLayout();
    }
    
    public void setSmoothScrollbarEnabled(final boolean mSmoothScrollbarEnabled) {
        this.mSmoothScrollbarEnabled = mSmoothScrollbarEnabled;
    }
    
    public void setStackFromEnd(final boolean mStackFromEnd) {
        this.assertNotInLayoutOrScroll(null);
        if (this.mStackFromEnd == mStackFromEnd) {
            return;
        }
        this.mStackFromEnd = mStackFromEnd;
        ((RecyclerView.LayoutManager)this).requestLayout();
    }
    
    @Override
    boolean shouldMeasureTwice() {
        return ((RecyclerView.LayoutManager)this).getHeightMode() != 1073741824 && ((RecyclerView.LayoutManager)this).getWidthMode() != 1073741824 && ((RecyclerView.LayoutManager)this).hasFlexibleChildInBothOrientations();
    }
    
    @Override
    public void smoothScrollToPosition(final RecyclerView recyclerView, final State state, final int targetPosition) {
        final LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext());
        ((RecyclerView.SmoothScroller)linearSmoothScroller).setTargetPosition(targetPosition);
        ((RecyclerView.LayoutManager)this).startSmoothScroll(linearSmoothScroller);
    }
    
    @Override
    public boolean supportsPredictiveItemAnimations() {
        return this.mPendingSavedState == null && this.mLastStackFromEnd == this.mStackFromEnd;
    }
    
    void validateChildOrder() {
        final StringBuilder sb = new StringBuilder();
        sb.append("validating child count ");
        sb.append(((RecyclerView.LayoutManager)this).getChildCount());
        Log.d("LinearLayoutManager", sb.toString());
        if (((RecyclerView.LayoutManager)this).getChildCount() < 1) {
            return;
        }
        final boolean b = false;
        boolean b2 = false;
        final int position = ((RecyclerView.LayoutManager)this).getPosition(((RecyclerView.LayoutManager)this).getChildAt(0));
        final int decoratedStart = this.mOrientationHelper.getDecoratedStart(((RecyclerView.LayoutManager)this).getChildAt(0));
        if (this.mShouldReverseLayout) {
            for (int i = 1; i < ((RecyclerView.LayoutManager)this).getChildCount(); ++i) {
                final View child = ((RecyclerView.LayoutManager)this).getChildAt(i);
                final int position2 = ((RecyclerView.LayoutManager)this).getPosition(child);
                final int decoratedStart2 = this.mOrientationHelper.getDecoratedStart(child);
                if (position2 < position) {
                    this.logChildren();
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("detected invalid position. loc invalid? ");
                    if (decoratedStart2 < decoratedStart) {
                        b2 = true;
                    }
                    sb2.append(b2);
                    throw new RuntimeException(sb2.toString());
                }
                if (decoratedStart2 > decoratedStart) {
                    this.logChildren();
                    throw new RuntimeException("detected invalid location");
                }
            }
        }
        else {
            for (int j = 1; j < ((RecyclerView.LayoutManager)this).getChildCount(); ++j) {
                final View child2 = ((RecyclerView.LayoutManager)this).getChildAt(j);
                final int position3 = ((RecyclerView.LayoutManager)this).getPosition(child2);
                final int decoratedStart3 = this.mOrientationHelper.getDecoratedStart(child2);
                if (position3 < position) {
                    this.logChildren();
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("detected invalid position. loc invalid? ");
                    boolean b3 = b;
                    if (decoratedStart3 < decoratedStart) {
                        b3 = true;
                    }
                    sb3.append(b3);
                    throw new RuntimeException(sb3.toString());
                }
                if (decoratedStart3 < decoratedStart) {
                    this.logChildren();
                    throw new RuntimeException("detected invalid location");
                }
            }
        }
    }
    
    static class AnchorInfo
    {
        int mCoordinate;
        boolean mLayoutFromEnd;
        OrientationHelper mOrientationHelper;
        int mPosition;
        boolean mValid;
        
        AnchorInfo() {
            this.reset();
        }
        
        void assignCoordinateFromPadding() {
            int mCoordinate;
            if (this.mLayoutFromEnd) {
                mCoordinate = this.mOrientationHelper.getEndAfterPadding();
            }
            else {
                mCoordinate = this.mOrientationHelper.getStartAfterPadding();
            }
            this.mCoordinate = mCoordinate;
        }
        
        public void assignFromView(final View view, final int mPosition) {
            if (this.mLayoutFromEnd) {
                this.mCoordinate = this.mOrientationHelper.getDecoratedEnd(view) + this.mOrientationHelper.getTotalSpaceChange();
            }
            else {
                this.mCoordinate = this.mOrientationHelper.getDecoratedStart(view);
            }
            this.mPosition = mPosition;
        }
        
        public void assignFromViewAndKeepVisibleRect(final View view, int a) {
            final int totalSpaceChange = this.mOrientationHelper.getTotalSpaceChange();
            if (totalSpaceChange >= 0) {
                this.assignFromView(view, a);
                return;
            }
            this.mPosition = a;
            if (this.mLayoutFromEnd) {
                a = this.mOrientationHelper.getEndAfterPadding() - totalSpaceChange - this.mOrientationHelper.getDecoratedEnd(view);
                this.mCoordinate = this.mOrientationHelper.getEndAfterPadding() - a;
                if (a > 0) {
                    final int decoratedMeasurement = this.mOrientationHelper.getDecoratedMeasurement(view);
                    final int mCoordinate = this.mCoordinate;
                    final int startAfterPadding = this.mOrientationHelper.getStartAfterPadding();
                    final int n = mCoordinate - decoratedMeasurement - (startAfterPadding + Math.min(this.mOrientationHelper.getDecoratedStart(view) - startAfterPadding, 0));
                    if (n < 0) {
                        this.mCoordinate += Math.min(a, -n);
                    }
                }
            }
            else {
                final int decoratedStart = this.mOrientationHelper.getDecoratedStart(view);
                a = decoratedStart - this.mOrientationHelper.getStartAfterPadding();
                this.mCoordinate = decoratedStart;
                if (a > 0) {
                    final int n2 = this.mOrientationHelper.getEndAfterPadding() - Math.min(0, this.mOrientationHelper.getEndAfterPadding() - totalSpaceChange - this.mOrientationHelper.getDecoratedEnd(view)) - (decoratedStart + this.mOrientationHelper.getDecoratedMeasurement(view));
                    if (n2 < 0) {
                        this.mCoordinate -= Math.min(a, -n2);
                    }
                }
            }
        }
        
        boolean isViewValidAsAnchor(final View view, final State state) {
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            return !layoutParams.isItemRemoved() && layoutParams.getViewLayoutPosition() >= 0 && layoutParams.getViewLayoutPosition() < state.getItemCount();
        }
        
        void reset() {
            this.mPosition = -1;
            this.mCoordinate = Integer.MIN_VALUE;
            this.mLayoutFromEnd = false;
            this.mValid = false;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("AnchorInfo{mPosition=");
            sb.append(this.mPosition);
            sb.append(", mCoordinate=");
            sb.append(this.mCoordinate);
            sb.append(", mLayoutFromEnd=");
            sb.append(this.mLayoutFromEnd);
            sb.append(", mValid=");
            sb.append(this.mValid);
            sb.append('}');
            return sb.toString();
        }
    }
    
    protected static class LayoutChunkResult
    {
        public int mConsumed;
        public boolean mFinished;
        public boolean mFocusable;
        public boolean mIgnoreConsumed;
        
        void resetInternal() {
            this.mConsumed = 0;
            this.mFinished = false;
            this.mIgnoreConsumed = false;
            this.mFocusable = false;
        }
    }
    
    static class LayoutState
    {
        static final int INVALID_LAYOUT = Integer.MIN_VALUE;
        static final int ITEM_DIRECTION_HEAD = -1;
        static final int ITEM_DIRECTION_TAIL = 1;
        static final int LAYOUT_END = 1;
        static final int LAYOUT_START = -1;
        static final int SCROLLING_OFFSET_NaN = Integer.MIN_VALUE;
        static final String TAG = "LLM#LayoutState";
        int mAvailable;
        int mCurrentPosition;
        int mExtra;
        boolean mInfinite;
        boolean mIsPreLayout;
        int mItemDirection;
        int mLastScrollDelta;
        int mLayoutDirection;
        int mOffset;
        boolean mRecycle;
        List<ViewHolder> mScrapList;
        int mScrollingOffset;
        
        LayoutState() {
            this.mRecycle = true;
            this.mExtra = 0;
            this.mIsPreLayout = false;
            this.mScrapList = null;
        }
        
        private View nextViewFromScrapList() {
            for (int size = this.mScrapList.size(), i = 0; i < size; ++i) {
                final View itemView = this.mScrapList.get(i).itemView;
                final LayoutParams layoutParams = (LayoutParams)itemView.getLayoutParams();
                if (!layoutParams.isItemRemoved()) {
                    if (this.mCurrentPosition == layoutParams.getViewLayoutPosition()) {
                        this.assignPositionFromScrapList(itemView);
                        return itemView;
                    }
                }
            }
            return null;
        }
        
        public void assignPositionFromScrapList() {
            this.assignPositionFromScrapList(null);
        }
        
        public void assignPositionFromScrapList(View nextViewInLimitedList) {
            nextViewInLimitedList = this.nextViewInLimitedList(nextViewInLimitedList);
            if (nextViewInLimitedList == null) {
                this.mCurrentPosition = -1;
            }
            else {
                this.mCurrentPosition = ((LayoutParams)nextViewInLimitedList.getLayoutParams()).getViewLayoutPosition();
            }
        }
        
        boolean hasMore(final State state) {
            return this.mCurrentPosition >= 0 && this.mCurrentPosition < state.getItemCount();
        }
        
        void log() {
            final StringBuilder sb = new StringBuilder();
            sb.append("avail:");
            sb.append(this.mAvailable);
            sb.append(", ind:");
            sb.append(this.mCurrentPosition);
            sb.append(", dir:");
            sb.append(this.mItemDirection);
            sb.append(", offset:");
            sb.append(this.mOffset);
            sb.append(", layoutDir:");
            sb.append(this.mLayoutDirection);
            Log.d("LLM#LayoutState", sb.toString());
        }
        
        View next(final Recycler recycler) {
            if (this.mScrapList != null) {
                return this.nextViewFromScrapList();
            }
            final View viewForPosition = recycler.getViewForPosition(this.mCurrentPosition);
            this.mCurrentPosition += this.mItemDirection;
            return viewForPosition;
        }
        
        public View nextViewInLimitedList(final View view) {
            final int size = this.mScrapList.size();
            View view2 = null;
            int n = Integer.MAX_VALUE;
            int n2 = 0;
            View view3;
            while (true) {
                view3 = view2;
                if (n2 >= size) {
                    break;
                }
                final View itemView = this.mScrapList.get(n2).itemView;
                final LayoutParams layoutParams = (LayoutParams)itemView.getLayoutParams();
                View view4 = view2;
                int n3 = n;
                if (itemView != view) {
                    if (layoutParams.isItemRemoved()) {
                        view4 = view2;
                        n3 = n;
                    }
                    else {
                        final int n4 = (layoutParams.getViewLayoutPosition() - this.mCurrentPosition) * this.mItemDirection;
                        if (n4 < 0) {
                            view4 = view2;
                            n3 = n;
                        }
                        else {
                            view4 = view2;
                            if (n4 < (n3 = n)) {
                                if (n4 == 0) {
                                    view3 = itemView;
                                    break;
                                }
                                view4 = itemView;
                                n3 = n4;
                            }
                        }
                    }
                }
                ++n2;
                view2 = view4;
                n = n3;
            }
            return view3;
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static class SavedState implements Parcelable
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        boolean mAnchorLayoutFromEnd;
        int mAnchorOffset;
        int mAnchorPosition;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<SavedState>() {
                public SavedState createFromParcel(final Parcel parcel) {
                    return new SavedState(parcel);
                }
                
                public SavedState[] newArray(final int n) {
                    return new SavedState[n];
                }
            };
        }
        
        public SavedState() {
        }
        
        SavedState(final Parcel parcel) {
            this.mAnchorPosition = parcel.readInt();
            this.mAnchorOffset = parcel.readInt();
            final int int1 = parcel.readInt();
            boolean mAnchorLayoutFromEnd = true;
            if (int1 != 1) {
                mAnchorLayoutFromEnd = false;
            }
            this.mAnchorLayoutFromEnd = mAnchorLayoutFromEnd;
        }
        
        public SavedState(final SavedState savedState) {
            this.mAnchorPosition = savedState.mAnchorPosition;
            this.mAnchorOffset = savedState.mAnchorOffset;
            this.mAnchorLayoutFromEnd = savedState.mAnchorLayoutFromEnd;
        }
        
        public int describeContents() {
            return 0;
        }
        
        boolean hasValidAnchor() {
            return this.mAnchorPosition >= 0;
        }
        
        void invalidateAnchor() {
            this.mAnchorPosition = -1;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            parcel.writeInt(this.mAnchorPosition);
            parcel.writeInt(this.mAnchorOffset);
            parcel.writeInt((int)(this.mAnchorLayoutFromEnd ? 1 : 0));
        }
    }
}
