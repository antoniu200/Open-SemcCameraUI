// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.widget;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import java.util.ArrayList;
import java.util.List;
import android.os.Parcelable;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.accessibility.AccessibilityEvent;
import android.support.annotation.Nullable;
import android.view.ViewGroup$MarginLayoutParams;
import android.view.ViewGroup$LayoutParams;
import android.graphics.PointF;
import android.support.annotation.RestrictTo;
import java.util.Arrays;
import android.view.View$MeasureSpec;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Rect;
import java.util.BitSet;
import android.support.annotation.NonNull;

public class StaggeredGridLayoutManager extends LayoutManager implements ScrollVectorProvider
{
    static final boolean DEBUG = false;
    @Deprecated
    public static final int GAP_HANDLING_LAZY = 1;
    public static final int GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS = 2;
    public static final int GAP_HANDLING_NONE = 0;
    public static final int HORIZONTAL = 0;
    static final int INVALID_OFFSET = Integer.MIN_VALUE;
    private static final float MAX_SCROLL_FACTOR = 0.33333334f;
    private static final String TAG = "StaggeredGridLManager";
    public static final int VERTICAL = 1;
    private final AnchorInfo mAnchorInfo;
    private final Runnable mCheckForGapsRunnable;
    private int mFullSizeSpec;
    private int mGapStrategy;
    private boolean mLaidOutInvalidFullSpan;
    private boolean mLastLayoutFromEnd;
    private boolean mLastLayoutRTL;
    @NonNull
    private final LayoutState mLayoutState;
    LazySpanLookup mLazySpanLookup;
    private int mOrientation;
    private SavedState mPendingSavedState;
    int mPendingScrollPosition;
    int mPendingScrollPositionOffset;
    private int[] mPrefetchDistances;
    @NonNull
    OrientationHelper mPrimaryOrientation;
    private BitSet mRemainingSpans;
    boolean mReverseLayout;
    @NonNull
    OrientationHelper mSecondaryOrientation;
    boolean mShouldReverseLayout;
    private int mSizePerSpan;
    private boolean mSmoothScrollbarEnabled;
    private int mSpanCount;
    Span[] mSpans;
    private final Rect mTmpRect;
    
    public StaggeredGridLayoutManager(final int spanCount, final int mOrientation) {
        this.mSpanCount = -1;
        this.mReverseLayout = false;
        this.mShouldReverseLayout = false;
        this.mPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        this.mLazySpanLookup = new LazySpanLookup();
        this.mGapStrategy = 2;
        this.mTmpRect = new Rect();
        this.mAnchorInfo = new AnchorInfo();
        this.mLaidOutInvalidFullSpan = false;
        this.mSmoothScrollbarEnabled = true;
        this.mCheckForGapsRunnable = new Runnable() {
            final StaggeredGridLayoutManager this$0;
            
            @Override
            public void run() {
                this.this$0.checkForGaps();
            }
        };
        this.mOrientation = mOrientation;
        this.setSpanCount(spanCount);
        this.mLayoutState = new LayoutState();
        this.createOrientationHelpers();
    }
    
    public StaggeredGridLayoutManager(final Context context, final AttributeSet set, final int n, final int n2) {
        this.mSpanCount = -1;
        this.mReverseLayout = false;
        this.mShouldReverseLayout = false;
        this.mPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        this.mLazySpanLookup = new LazySpanLookup();
        this.mGapStrategy = 2;
        this.mTmpRect = new Rect();
        this.mAnchorInfo = new AnchorInfo();
        this.mLaidOutInvalidFullSpan = false;
        this.mSmoothScrollbarEnabled = true;
        this.mCheckForGapsRunnable = new Runnable() {
            final StaggeredGridLayoutManager this$0;
            
            @Override
            public void run() {
                this.this$0.checkForGaps();
            }
        };
        final Properties properties = RecyclerView.LayoutManager.getProperties(context, set, n, n2);
        this.setOrientation(properties.orientation);
        this.setSpanCount(properties.spanCount);
        this.setReverseLayout(properties.reverseLayout);
        this.mLayoutState = new LayoutState();
        this.createOrientationHelpers();
    }
    
    private void appendViewToAllSpans(final View view) {
        for (int i = this.mSpanCount - 1; i >= 0; --i) {
            this.mSpans[i].appendToSpan(view);
        }
    }
    
    private void applyPendingSavedState(final AnchorInfo anchorInfo) {
        if (this.mPendingSavedState.mSpanOffsetsSize > 0) {
            if (this.mPendingSavedState.mSpanOffsetsSize == this.mSpanCount) {
                for (int i = 0; i < this.mSpanCount; ++i) {
                    this.mSpans[i].clear();
                    final int n = this.mPendingSavedState.mSpanOffsets[i];
                    int line;
                    if ((line = n) != Integer.MIN_VALUE) {
                        if (this.mPendingSavedState.mAnchorLayoutFromEnd) {
                            line = n + this.mPrimaryOrientation.getEndAfterPadding();
                        }
                        else {
                            line = n + this.mPrimaryOrientation.getStartAfterPadding();
                        }
                    }
                    this.mSpans[i].setLine(line);
                }
            }
            else {
                this.mPendingSavedState.invalidateSpanInfo();
                this.mPendingSavedState.mAnchorPosition = this.mPendingSavedState.mVisibleAnchorPosition;
            }
        }
        this.mLastLayoutRTL = this.mPendingSavedState.mLastLayoutRTL;
        this.setReverseLayout(this.mPendingSavedState.mReverseLayout);
        this.resolveShouldLayoutReverse();
        if (this.mPendingSavedState.mAnchorPosition != -1) {
            this.mPendingScrollPosition = this.mPendingSavedState.mAnchorPosition;
            anchorInfo.mLayoutFromEnd = this.mPendingSavedState.mAnchorLayoutFromEnd;
        }
        else {
            anchorInfo.mLayoutFromEnd = this.mShouldReverseLayout;
        }
        if (this.mPendingSavedState.mSpanLookupSize > 1) {
            this.mLazySpanLookup.mData = this.mPendingSavedState.mSpanLookup;
            this.mLazySpanLookup.mFullSpanItems = this.mPendingSavedState.mFullSpanItems;
        }
    }
    
    private void attachViewToSpans(final View view, final LayoutParams layoutParams, final LayoutState layoutState) {
        if (layoutState.mLayoutDirection == 1) {
            if (layoutParams.mFullSpan) {
                this.appendViewToAllSpans(view);
            }
            else {
                layoutParams.mSpan.appendToSpan(view);
            }
        }
        else if (layoutParams.mFullSpan) {
            this.prependViewToAllSpans(view);
        }
        else {
            layoutParams.mSpan.prependToSpan(view);
        }
    }
    
    private int calculateScrollDirectionForPosition(final int n) {
        final int childCount = ((RecyclerView.LayoutManager)this).getChildCount();
        int n2 = -1;
        if (childCount == 0) {
            if (this.mShouldReverseLayout) {
                n2 = 1;
            }
            return n2;
        }
        if (n < this.getFirstChildPosition() == this.mShouldReverseLayout) {
            n2 = 1;
        }
        return n2;
    }
    
    private boolean checkSpanForGap(final Span span) {
        if (this.mShouldReverseLayout) {
            if (span.getEndLine() < this.mPrimaryOrientation.getEndAfterPadding()) {
                return span.getLayoutParams(span.mViews.get(span.mViews.size() - 1)).mFullSpan ^ true;
            }
        }
        else if (span.getStartLine() > this.mPrimaryOrientation.getStartAfterPadding()) {
            return span.getLayoutParams(span.mViews.get(0)).mFullSpan ^ true;
        }
        return false;
    }
    
    private int computeScrollExtent(final State state) {
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0) {
            return 0;
        }
        return ScrollbarHelper.computeScrollExtent(state, this.mPrimaryOrientation, this.findFirstVisibleItemClosestToStart(this.mSmoothScrollbarEnabled ^ true), this.findFirstVisibleItemClosestToEnd(this.mSmoothScrollbarEnabled ^ true), this, this.mSmoothScrollbarEnabled);
    }
    
    private int computeScrollOffset(final State state) {
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0) {
            return 0;
        }
        return ScrollbarHelper.computeScrollOffset(state, this.mPrimaryOrientation, this.findFirstVisibleItemClosestToStart(this.mSmoothScrollbarEnabled ^ true), this.findFirstVisibleItemClosestToEnd(this.mSmoothScrollbarEnabled ^ true), this, this.mSmoothScrollbarEnabled, this.mShouldReverseLayout);
    }
    
    private int computeScrollRange(final State state) {
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0) {
            return 0;
        }
        return ScrollbarHelper.computeScrollRange(state, this.mPrimaryOrientation, this.findFirstVisibleItemClosestToStart(this.mSmoothScrollbarEnabled ^ true), this.findFirstVisibleItemClosestToEnd(this.mSmoothScrollbarEnabled ^ true), this, this.mSmoothScrollbarEnabled);
    }
    
    private int convertFocusDirectionToLayoutDirection(final int n) {
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
    
    private FullSpanItem createFullSpanItemFromEnd(final int n) {
        final FullSpanItem fullSpanItem = new FullSpanItem();
        fullSpanItem.mGapPerSpan = new int[this.mSpanCount];
        for (int i = 0; i < this.mSpanCount; ++i) {
            fullSpanItem.mGapPerSpan[i] = n - this.mSpans[i].getEndLine(n);
        }
        return fullSpanItem;
    }
    
    private FullSpanItem createFullSpanItemFromStart(final int n) {
        final FullSpanItem fullSpanItem = new FullSpanItem();
        fullSpanItem.mGapPerSpan = new int[this.mSpanCount];
        for (int i = 0; i < this.mSpanCount; ++i) {
            fullSpanItem.mGapPerSpan[i] = this.mSpans[i].getStartLine(n) - n;
        }
        return fullSpanItem;
    }
    
    private void createOrientationHelpers() {
        this.mPrimaryOrientation = OrientationHelper.createOrientationHelper(this, this.mOrientation);
        this.mSecondaryOrientation = OrientationHelper.createOrientationHelper(this, 1 - this.mOrientation);
    }
    
    private int fill(final Recycler recycler, final LayoutState layoutState, final State state) {
        this.mRemainingSpans.set(0, this.mSpanCount, true);
        int n;
        if (this.mLayoutState.mInfinite) {
            if (layoutState.mLayoutDirection == 1) {
                n = Integer.MAX_VALUE;
            }
            else {
                n = Integer.MIN_VALUE;
            }
        }
        else if (layoutState.mLayoutDirection == 1) {
            n = layoutState.mEndLine + layoutState.mAvailable;
        }
        else {
            n = layoutState.mStartLine - layoutState.mAvailable;
        }
        this.updateAllRemainingSpans(layoutState.mLayoutDirection, n);
        int n2;
        if (this.mShouldReverseLayout) {
            n2 = this.mPrimaryOrientation.getEndAfterPadding();
        }
        else {
            n2 = this.mPrimaryOrientation.getStartAfterPadding();
        }
        boolean b = false;
        while (layoutState.hasMore(state) && (this.mLayoutState.mInfinite || !this.mRemainingSpans.isEmpty())) {
            final View next = layoutState.next(recycler);
            final LayoutParams layoutParams = (LayoutParams)next.getLayoutParams();
            final int viewLayoutPosition = ((RecyclerView.LayoutParams)layoutParams).getViewLayoutPosition();
            final int span = this.mLazySpanLookup.getSpan(viewLayoutPosition);
            final boolean b2 = span == -1;
            Span nextSpan;
            if (b2) {
                if (layoutParams.mFullSpan) {
                    nextSpan = this.mSpans[0];
                }
                else {
                    nextSpan = this.getNextSpan(layoutState);
                }
                this.mLazySpanLookup.setSpan(viewLayoutPosition, nextSpan);
            }
            else {
                nextSpan = this.mSpans[span];
            }
            layoutParams.mSpan = nextSpan;
            if (layoutState.mLayoutDirection == 1) {
                ((RecyclerView.LayoutManager)this).addView(next);
            }
            else {
                ((RecyclerView.LayoutManager)this).addView(next, 0);
            }
            this.measureChildWithDecorationsAndMargin(next, layoutParams, false);
            int n4;
            int n5;
            if (layoutState.mLayoutDirection == 1) {
                int n3;
                if (layoutParams.mFullSpan) {
                    n3 = this.getMaxEnd(n2);
                }
                else {
                    n3 = nextSpan.getEndLine(n2);
                }
                final int decoratedMeasurement = this.mPrimaryOrientation.getDecoratedMeasurement(next);
                if (b2 && layoutParams.mFullSpan) {
                    final FullSpanItem fullSpanItemFromEnd = this.createFullSpanItemFromEnd(n3);
                    fullSpanItemFromEnd.mGapDir = -1;
                    fullSpanItemFromEnd.mPosition = viewLayoutPosition;
                    this.mLazySpanLookup.addFullSpanItem(fullSpanItemFromEnd);
                }
                n4 = decoratedMeasurement + n3;
                n5 = n3;
            }
            else {
                int n6;
                if (layoutParams.mFullSpan) {
                    n6 = this.getMinStart(n2);
                }
                else {
                    n6 = nextSpan.getStartLine(n2);
                }
                n5 = n6 - this.mPrimaryOrientation.getDecoratedMeasurement(next);
                if (b2 && layoutParams.mFullSpan) {
                    final FullSpanItem fullSpanItemFromStart = this.createFullSpanItemFromStart(n6);
                    fullSpanItemFromStart.mGapDir = 1;
                    fullSpanItemFromStart.mPosition = viewLayoutPosition;
                    this.mLazySpanLookup.addFullSpanItem(fullSpanItemFromStart);
                }
                n4 = n6;
            }
            if (layoutParams.mFullSpan && layoutState.mItemDirection == -1) {
                if (b2) {
                    this.mLaidOutInvalidFullSpan = true;
                }
                else {
                    boolean b3;
                    if (layoutState.mLayoutDirection == 1) {
                        b3 = this.areAllEndsEqual();
                    }
                    else {
                        b3 = this.areAllStartsEqual();
                    }
                    if (b3 ^ true) {
                        final FullSpanItem fullSpanItem = this.mLazySpanLookup.getFullSpanItem(viewLayoutPosition);
                        if (fullSpanItem != null) {
                            fullSpanItem.mHasUnwantedGapAfter = true;
                        }
                        this.mLaidOutInvalidFullSpan = true;
                    }
                }
            }
            this.attachViewToSpans(next, layoutParams, layoutState);
            int n8;
            int n9;
            if (this.isLayoutRTL() && this.mOrientation == 1) {
                int endAfterPadding;
                if (layoutParams.mFullSpan) {
                    endAfterPadding = this.mSecondaryOrientation.getEndAfterPadding();
                }
                else {
                    endAfterPadding = this.mSecondaryOrientation.getEndAfterPadding() - (this.mSpanCount - 1 - nextSpan.mIndex) * this.mSizePerSpan;
                }
                final int decoratedMeasurement2 = this.mSecondaryOrientation.getDecoratedMeasurement(next);
                final int n7 = endAfterPadding;
                n8 = endAfterPadding - decoratedMeasurement2;
                n9 = n7;
            }
            else {
                int startAfterPadding;
                if (layoutParams.mFullSpan) {
                    startAfterPadding = this.mSecondaryOrientation.getStartAfterPadding();
                }
                else {
                    startAfterPadding = nextSpan.mIndex * this.mSizePerSpan + this.mSecondaryOrientation.getStartAfterPadding();
                }
                final int decoratedMeasurement3 = this.mSecondaryOrientation.getDecoratedMeasurement(next);
                final int n10 = startAfterPadding;
                n9 = decoratedMeasurement3 + startAfterPadding;
                n8 = n10;
            }
            if (this.mOrientation == 1) {
                ((RecyclerView.LayoutManager)this).layoutDecoratedWithMargins(next, n8, n5, n9, n4);
            }
            else {
                ((RecyclerView.LayoutManager)this).layoutDecoratedWithMargins(next, n5, n8, n4, n9);
            }
            if (layoutParams.mFullSpan) {
                this.updateAllRemainingSpans(this.mLayoutState.mLayoutDirection, n);
            }
            else {
                this.updateRemainingSpans(nextSpan, this.mLayoutState.mLayoutDirection, n);
            }
            this.recycle(recycler, this.mLayoutState);
            if (this.mLayoutState.mStopInFocusable && next.hasFocusable()) {
                if (layoutParams.mFullSpan) {
                    this.mRemainingSpans.clear();
                }
                else {
                    this.mRemainingSpans.set(nextSpan.mIndex, false);
                }
            }
            b = true;
        }
        final int n11 = 0;
        if (!b) {
            this.recycle(recycler, this.mLayoutState);
        }
        int b4;
        if (this.mLayoutState.mLayoutDirection == -1) {
            b4 = this.mPrimaryOrientation.getStartAfterPadding() - this.getMinStart(this.mPrimaryOrientation.getStartAfterPadding());
        }
        else {
            b4 = this.getMaxEnd(this.mPrimaryOrientation.getEndAfterPadding()) - this.mPrimaryOrientation.getEndAfterPadding();
        }
        int min = n11;
        if (b4 > 0) {
            min = Math.min(layoutState.mAvailable, b4);
        }
        return min;
    }
    
    private int findFirstReferenceChildPosition(final int n) {
        for (int childCount = ((RecyclerView.LayoutManager)this).getChildCount(), i = 0; i < childCount; ++i) {
            final int position = ((RecyclerView.LayoutManager)this).getPosition(((RecyclerView.LayoutManager)this).getChildAt(i));
            if (position >= 0 && position < n) {
                return position;
            }
        }
        return 0;
    }
    
    private int findLastReferenceChildPosition(final int n) {
        for (int i = ((RecyclerView.LayoutManager)this).getChildCount() - 1; i >= 0; --i) {
            final int position = ((RecyclerView.LayoutManager)this).getPosition(((RecyclerView.LayoutManager)this).getChildAt(i));
            if (position >= 0 && position < n) {
                return position;
            }
        }
        return 0;
    }
    
    private void fixEndGap(final Recycler recycler, final State state, final boolean b) {
        final int maxEnd = this.getMaxEnd(Integer.MIN_VALUE);
        if (maxEnd == Integer.MIN_VALUE) {
            return;
        }
        final int n = this.mPrimaryOrientation.getEndAfterPadding() - maxEnd;
        if (n > 0) {
            final int n2 = n - -this.scrollBy(-n, recycler, state);
            if (b && n2 > 0) {
                this.mPrimaryOrientation.offsetChildren(n2);
            }
        }
    }
    
    private void fixStartGap(final Recycler recycler, final State state, final boolean b) {
        final int minStart = this.getMinStart(Integer.MAX_VALUE);
        if (minStart == Integer.MAX_VALUE) {
            return;
        }
        final int n = minStart - this.mPrimaryOrientation.getStartAfterPadding();
        if (n > 0) {
            final int n2 = n - this.scrollBy(n, recycler, state);
            if (b && n2 > 0) {
                this.mPrimaryOrientation.offsetChildren(-n2);
            }
        }
    }
    
    private int getMaxEnd(final int n) {
        int endLine = this.mSpans[0].getEndLine(n);
        int n2;
        for (int i = 1; i < this.mSpanCount; ++i, endLine = n2) {
            final int endLine2 = this.mSpans[i].getEndLine(n);
            if (endLine2 > (n2 = endLine)) {
                n2 = endLine2;
            }
        }
        return endLine;
    }
    
    private int getMaxStart(final int n) {
        int startLine = this.mSpans[0].getStartLine(n);
        int n2;
        for (int i = 1; i < this.mSpanCount; ++i, startLine = n2) {
            final int startLine2 = this.mSpans[i].getStartLine(n);
            if (startLine2 > (n2 = startLine)) {
                n2 = startLine2;
            }
        }
        return startLine;
    }
    
    private int getMinEnd(final int n) {
        int endLine = this.mSpans[0].getEndLine(n);
        int n2;
        for (int i = 1; i < this.mSpanCount; ++i, endLine = n2) {
            final int endLine2 = this.mSpans[i].getEndLine(n);
            if (endLine2 < (n2 = endLine)) {
                n2 = endLine2;
            }
        }
        return endLine;
    }
    
    private int getMinStart(final int n) {
        int startLine = this.mSpans[0].getStartLine(n);
        int n2;
        for (int i = 1; i < this.mSpanCount; ++i, startLine = n2) {
            final int startLine2 = this.mSpans[i].getStartLine(n);
            if (startLine2 < (n2 = startLine)) {
                n2 = startLine2;
            }
        }
        return startLine;
    }
    
    private Span getNextSpan(final LayoutState layoutState) {
        final boolean preferLastSpan = this.preferLastSpan(layoutState.mLayoutDirection);
        int mSpanCount = -1;
        int i;
        int n;
        if (preferLastSpan) {
            i = this.mSpanCount - 1;
            n = -1;
        }
        else {
            i = 0;
            mSpanCount = this.mSpanCount;
            n = 1;
        }
        final int mLayoutDirection = layoutState.mLayoutDirection;
        final Span span = null;
        Span span2 = null;
        if (mLayoutDirection == 1) {
            int n2 = Integer.MAX_VALUE;
            final int startAfterPadding = this.mPrimaryOrientation.getStartAfterPadding();
            while (i != mSpanCount) {
                final Span span3 = this.mSpans[i];
                final int endLine = span3.getEndLine(startAfterPadding);
                int n3;
                if (endLine < (n3 = n2)) {
                    span2 = span3;
                    n3 = endLine;
                }
                i += n;
                n2 = n3;
            }
            return span2;
        }
        int n4 = Integer.MIN_VALUE;
        final int endAfterPadding = this.mPrimaryOrientation.getEndAfterPadding();
        Span span4 = span;
        int n5;
        for (int j = i; j != mSpanCount; j += n, n4 = n5) {
            final Span span5 = this.mSpans[j];
            final int startLine = span5.getStartLine(endAfterPadding);
            if (startLine > (n5 = n4)) {
                span4 = span5;
                n5 = startLine;
            }
        }
        return span4;
    }
    
    private void handleUpdate(int n, final int n2, final int n3) {
        int n4;
        if (this.mShouldReverseLayout) {
            n4 = this.getLastChildPosition();
        }
        else {
            n4 = this.getFirstChildPosition();
        }
        int n5 = 0;
        int n6 = 0;
        Label_0064: {
            int n7;
            if (n3 == 8) {
                if (n >= n2) {
                    n5 = n + 1;
                    n6 = n2;
                    break Label_0064;
                }
                n7 = n2 + 1;
            }
            else {
                n7 = n + n2;
            }
            n5 = n7;
            n6 = n;
        }
        this.mLazySpanLookup.invalidateAfter(n6);
        if (n3 != 8) {
            switch (n3) {
                case 2: {
                    this.mLazySpanLookup.offsetForRemoval(n, n2);
                    break;
                }
                case 1: {
                    this.mLazySpanLookup.offsetForAddition(n, n2);
                    break;
                }
            }
        }
        else {
            this.mLazySpanLookup.offsetForRemoval(n, 1);
            this.mLazySpanLookup.offsetForAddition(n2, 1);
        }
        if (n5 <= n4) {
            return;
        }
        if (this.mShouldReverseLayout) {
            n = this.getFirstChildPosition();
        }
        else {
            n = this.getLastChildPosition();
        }
        if (n6 <= n) {
            ((RecyclerView.LayoutManager)this).requestLayout();
        }
    }
    
    private void measureChildWithDecorationsAndMargin(final View view, int updateSpecWithExtra, int updateSpecWithExtra2, final boolean b) {
        ((RecyclerView.LayoutManager)this).calculateItemDecorationsForChild(view, this.mTmpRect);
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        updateSpecWithExtra = this.updateSpecWithExtra(updateSpecWithExtra, layoutParams.leftMargin + this.mTmpRect.left, layoutParams.rightMargin + this.mTmpRect.right);
        updateSpecWithExtra2 = this.updateSpecWithExtra(updateSpecWithExtra2, layoutParams.topMargin + this.mTmpRect.top, layoutParams.bottomMargin + this.mTmpRect.bottom);
        boolean b2;
        if (b) {
            b2 = ((RecyclerView.LayoutManager)this).shouldReMeasureChild(view, updateSpecWithExtra, updateSpecWithExtra2, layoutParams);
        }
        else {
            b2 = ((RecyclerView.LayoutManager)this).shouldMeasureChild(view, updateSpecWithExtra, updateSpecWithExtra2, layoutParams);
        }
        if (b2) {
            view.measure(updateSpecWithExtra, updateSpecWithExtra2);
        }
    }
    
    private void measureChildWithDecorationsAndMargin(final View view, final LayoutParams layoutParams, final boolean b) {
        if (layoutParams.mFullSpan) {
            if (this.mOrientation == 1) {
                this.measureChildWithDecorationsAndMargin(view, this.mFullSizeSpec, RecyclerView.LayoutManager.getChildMeasureSpec(((RecyclerView.LayoutManager)this).getHeight(), ((RecyclerView.LayoutManager)this).getHeightMode(), ((RecyclerView.LayoutManager)this).getPaddingTop() + ((RecyclerView.LayoutManager)this).getPaddingBottom(), layoutParams.height, true), b);
            }
            else {
                this.measureChildWithDecorationsAndMargin(view, RecyclerView.LayoutManager.getChildMeasureSpec(((RecyclerView.LayoutManager)this).getWidth(), ((RecyclerView.LayoutManager)this).getWidthMode(), ((RecyclerView.LayoutManager)this).getPaddingLeft() + ((RecyclerView.LayoutManager)this).getPaddingRight(), layoutParams.width, true), this.mFullSizeSpec, b);
            }
        }
        else if (this.mOrientation == 1) {
            this.measureChildWithDecorationsAndMargin(view, RecyclerView.LayoutManager.getChildMeasureSpec(this.mSizePerSpan, ((RecyclerView.LayoutManager)this).getWidthMode(), 0, layoutParams.width, false), RecyclerView.LayoutManager.getChildMeasureSpec(((RecyclerView.LayoutManager)this).getHeight(), ((RecyclerView.LayoutManager)this).getHeightMode(), ((RecyclerView.LayoutManager)this).getPaddingTop() + ((RecyclerView.LayoutManager)this).getPaddingBottom(), layoutParams.height, true), b);
        }
        else {
            this.measureChildWithDecorationsAndMargin(view, RecyclerView.LayoutManager.getChildMeasureSpec(((RecyclerView.LayoutManager)this).getWidth(), ((RecyclerView.LayoutManager)this).getWidthMode(), ((RecyclerView.LayoutManager)this).getPaddingLeft() + ((RecyclerView.LayoutManager)this).getPaddingRight(), layoutParams.width, true), RecyclerView.LayoutManager.getChildMeasureSpec(this.mSizePerSpan, ((RecyclerView.LayoutManager)this).getHeightMode(), 0, layoutParams.height, false), b);
        }
    }
    
    private void onLayoutChildren(final Recycler recycler, final State state, final boolean b) {
        final AnchorInfo mAnchorInfo = this.mAnchorInfo;
        if ((this.mPendingSavedState != null || this.mPendingScrollPosition != -1) && state.getItemCount() == 0) {
            ((RecyclerView.LayoutManager)this).removeAndRecycleAllViews(recycler);
            mAnchorInfo.reset();
            return;
        }
        final boolean mValid = mAnchorInfo.mValid;
        final int n = 1;
        final boolean b2 = !mValid || this.mPendingScrollPosition != -1 || this.mPendingSavedState != null;
        if (b2) {
            mAnchorInfo.reset();
            if (this.mPendingSavedState != null) {
                this.applyPendingSavedState(mAnchorInfo);
            }
            else {
                this.resolveShouldLayoutReverse();
                mAnchorInfo.mLayoutFromEnd = this.mShouldReverseLayout;
            }
            this.updateAnchorInfoForLayout(state, mAnchorInfo);
            mAnchorInfo.mValid = true;
        }
        if (this.mPendingSavedState == null && this.mPendingScrollPosition == -1 && (mAnchorInfo.mLayoutFromEnd != this.mLastLayoutFromEnd || this.isLayoutRTL() != this.mLastLayoutRTL)) {
            this.mLazySpanLookup.clear();
            mAnchorInfo.mInvalidateOffsets = true;
        }
        if (((RecyclerView.LayoutManager)this).getChildCount() > 0 && (this.mPendingSavedState == null || this.mPendingSavedState.mSpanOffsetsSize < 1)) {
            if (mAnchorInfo.mInvalidateOffsets) {
                for (int i = 0; i < this.mSpanCount; ++i) {
                    this.mSpans[i].clear();
                    if (mAnchorInfo.mOffset != Integer.MIN_VALUE) {
                        this.mSpans[i].setLine(mAnchorInfo.mOffset);
                    }
                }
            }
            else if (!b2 && this.mAnchorInfo.mSpanReferenceLines != null) {
                for (int j = 0; j < this.mSpanCount; ++j) {
                    final Span span = this.mSpans[j];
                    span.clear();
                    span.setLine(this.mAnchorInfo.mSpanReferenceLines[j]);
                }
            }
            else {
                for (int k = 0; k < this.mSpanCount; ++k) {
                    this.mSpans[k].cacheReferenceLineAndClear(this.mShouldReverseLayout, mAnchorInfo.mOffset);
                }
                this.mAnchorInfo.saveSpanReferenceLines(this.mSpans);
            }
        }
        ((RecyclerView.LayoutManager)this).detachAndScrapAttachedViews(recycler);
        this.mLayoutState.mRecycle = false;
        this.mLaidOutInvalidFullSpan = false;
        this.updateMeasureSpecs(this.mSecondaryOrientation.getTotalSpace());
        this.updateLayoutState(mAnchorInfo.mPosition, state);
        if (mAnchorInfo.mLayoutFromEnd) {
            this.setLayoutStateDirection(-1);
            this.fill(recycler, this.mLayoutState, state);
            this.setLayoutStateDirection(1);
            this.mLayoutState.mCurrentPosition = mAnchorInfo.mPosition + this.mLayoutState.mItemDirection;
            this.fill(recycler, this.mLayoutState, state);
        }
        else {
            this.setLayoutStateDirection(1);
            this.fill(recycler, this.mLayoutState, state);
            this.setLayoutStateDirection(-1);
            this.mLayoutState.mCurrentPosition = mAnchorInfo.mPosition + this.mLayoutState.mItemDirection;
            this.fill(recycler, this.mLayoutState, state);
        }
        this.repositionToWrapContentIfNecessary();
        if (((RecyclerView.LayoutManager)this).getChildCount() > 0) {
            if (this.mShouldReverseLayout) {
                this.fixEndGap(recycler, state, true);
                this.fixStartGap(recycler, state, false);
            }
            else {
                this.fixStartGap(recycler, state, true);
                this.fixEndGap(recycler, state, false);
            }
        }
        int n2 = 0;
        Label_0665: {
            if (b && !state.isPreLayout() && (this.mGapStrategy != 0 && ((RecyclerView.LayoutManager)this).getChildCount() > 0 && (this.mLaidOutInvalidFullSpan || this.hasGapsToFix() != null))) {
                ((RecyclerView.LayoutManager)this).removeCallbacks(this.mCheckForGapsRunnable);
                if (this.checkForGaps()) {
                    n2 = n;
                    break Label_0665;
                }
            }
            n2 = 0;
        }
        if (state.isPreLayout()) {
            this.mAnchorInfo.reset();
        }
        this.mLastLayoutFromEnd = mAnchorInfo.mLayoutFromEnd;
        this.mLastLayoutRTL = this.isLayoutRTL();
        if (n2 != 0) {
            this.mAnchorInfo.reset();
            this.onLayoutChildren(recycler, state, false);
        }
    }
    
    private boolean preferLastSpan(final int n) {
        final int mOrientation = this.mOrientation;
        boolean b = false;
        final boolean b2 = false;
        if (mOrientation == 0) {
            final boolean b3 = n == -1;
            boolean b4 = b2;
            if (b3 != this.mShouldReverseLayout) {
                b4 = true;
            }
            return b4;
        }
        if (n == -1 == this.mShouldReverseLayout == this.isLayoutRTL()) {
            b = true;
        }
        return b;
    }
    
    private void prependViewToAllSpans(final View view) {
        for (int i = this.mSpanCount - 1; i >= 0; --i) {
            this.mSpans[i].prependToSpan(view);
        }
    }
    
    private void recycle(final Recycler recycler, final LayoutState layoutState) {
        if (layoutState.mRecycle && !layoutState.mInfinite) {
            if (layoutState.mAvailable == 0) {
                if (layoutState.mLayoutDirection == -1) {
                    this.recycleFromEnd(recycler, layoutState.mEndLine);
                }
                else {
                    this.recycleFromStart(recycler, layoutState.mStartLine);
                }
            }
            else if (layoutState.mLayoutDirection == -1) {
                final int a = layoutState.mStartLine - this.getMaxStart(layoutState.mStartLine);
                int mEndLine;
                if (a < 0) {
                    mEndLine = layoutState.mEndLine;
                }
                else {
                    mEndLine = layoutState.mEndLine - Math.min(a, layoutState.mAvailable);
                }
                this.recycleFromEnd(recycler, mEndLine);
            }
            else {
                final int a2 = this.getMinEnd(layoutState.mEndLine) - layoutState.mEndLine;
                int mStartLine;
                if (a2 < 0) {
                    mStartLine = layoutState.mStartLine;
                }
                else {
                    mStartLine = Math.min(a2, layoutState.mAvailable) + layoutState.mStartLine;
                }
                this.recycleFromStart(recycler, mStartLine);
            }
        }
    }
    
    private void recycleFromEnd(final Recycler recycler, final int n) {
        for (int i = ((RecyclerView.LayoutManager)this).getChildCount() - 1; i >= 0; --i) {
            final View child = ((RecyclerView.LayoutManager)this).getChildAt(i);
            if (this.mPrimaryOrientation.getDecoratedStart(child) < n || this.mPrimaryOrientation.getTransformedStartWithDecoration(child) < n) {
                return;
            }
            final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
            if (layoutParams.mFullSpan) {
                final int n2 = 0;
                int n3 = 0;
                int j;
                while (true) {
                    j = n2;
                    if (n3 >= this.mSpanCount) {
                        break;
                    }
                    if (this.mSpans[n3].mViews.size() == 1) {
                        return;
                    }
                    ++n3;
                }
                while (j < this.mSpanCount) {
                    this.mSpans[j].popEnd();
                    ++j;
                }
            }
            else {
                if (layoutParams.mSpan.mViews.size() == 1) {
                    return;
                }
                layoutParams.mSpan.popEnd();
            }
            ((RecyclerView.LayoutManager)this).removeAndRecycleView(child, recycler);
        }
    }
    
    private void recycleFromStart(final Recycler recycler, final int n) {
        while (((RecyclerView.LayoutManager)this).getChildCount() > 0) {
            final int n2 = 0;
            final View child = ((RecyclerView.LayoutManager)this).getChildAt(0);
            if (this.mPrimaryOrientation.getDecoratedEnd(child) > n || this.mPrimaryOrientation.getTransformedEndWithDecoration(child) > n) {
                return;
            }
            final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
            if (layoutParams.mFullSpan) {
                int n3 = 0;
                int i;
                while (true) {
                    i = n2;
                    if (n3 >= this.mSpanCount) {
                        break;
                    }
                    if (this.mSpans[n3].mViews.size() == 1) {
                        return;
                    }
                    ++n3;
                }
                while (i < this.mSpanCount) {
                    this.mSpans[i].popStart();
                    ++i;
                }
            }
            else {
                if (layoutParams.mSpan.mViews.size() == 1) {
                    return;
                }
                layoutParams.mSpan.popStart();
            }
            ((RecyclerView.LayoutManager)this).removeAndRecycleView(child, recycler);
        }
    }
    
    private void repositionToWrapContentIfNecessary() {
        if (this.mSecondaryOrientation.getMode() == 1073741824) {
            return;
        }
        final int childCount = ((RecyclerView.LayoutManager)this).getChildCount();
        final int n = 0;
        float max = 0.0f;
        for (int i = 0; i < childCount; ++i) {
            final View child = ((RecyclerView.LayoutManager)this).getChildAt(i);
            final float n2 = (float)this.mSecondaryOrientation.getDecoratedMeasurement(child);
            if (n2 >= max) {
                float b = n2;
                if (((LayoutParams)child.getLayoutParams()).isFullSpan()) {
                    b = 1.0f * n2 / this.mSpanCount;
                }
                max = Math.max(max, b);
            }
        }
        final int mSizePerSpan = this.mSizePerSpan;
        int a = Math.round(max * this.mSpanCount);
        if (this.mSecondaryOrientation.getMode() == Integer.MIN_VALUE) {
            a = Math.min(a, this.mSecondaryOrientation.getTotalSpace());
        }
        this.updateMeasureSpecs(a);
        int j = n;
        if (this.mSizePerSpan == mSizePerSpan) {
            return;
        }
        while (j < childCount) {
            final View child2 = ((RecyclerView.LayoutManager)this).getChildAt(j);
            final LayoutParams layoutParams = (LayoutParams)child2.getLayoutParams();
            if (!layoutParams.mFullSpan) {
                if (this.isLayoutRTL() && this.mOrientation == 1) {
                    child2.offsetLeftAndRight(-(this.mSpanCount - 1 - layoutParams.mSpan.mIndex) * this.mSizePerSpan - -(this.mSpanCount - 1 - layoutParams.mSpan.mIndex) * mSizePerSpan);
                }
                else {
                    final int n3 = layoutParams.mSpan.mIndex * this.mSizePerSpan;
                    final int n4 = layoutParams.mSpan.mIndex * mSizePerSpan;
                    if (this.mOrientation == 1) {
                        child2.offsetLeftAndRight(n3 - n4);
                    }
                    else {
                        child2.offsetTopAndBottom(n3 - n4);
                    }
                }
            }
            ++j;
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
    
    private void setLayoutStateDirection(int n) {
        this.mLayoutState.mLayoutDirection = n;
        final LayoutState mLayoutState = this.mLayoutState;
        final boolean mShouldReverseLayout = this.mShouldReverseLayout;
        final int n2 = 1;
        if (mShouldReverseLayout == (n == -1)) {
            n = n2;
        }
        else {
            n = -1;
        }
        mLayoutState.mItemDirection = n;
    }
    
    private void updateAllRemainingSpans(final int n, final int n2) {
        for (int i = 0; i < this.mSpanCount; ++i) {
            if (!this.mSpans[i].mViews.isEmpty()) {
                this.updateRemainingSpans(this.mSpans[i], n, n2);
            }
        }
    }
    
    private boolean updateAnchorFromChildren(final State state, final AnchorInfo anchorInfo) {
        int mPosition;
        if (this.mLastLayoutFromEnd) {
            mPosition = this.findLastReferenceChildPosition(state.getItemCount());
        }
        else {
            mPosition = this.findFirstReferenceChildPosition(state.getItemCount());
        }
        anchorInfo.mPosition = mPosition;
        anchorInfo.mOffset = Integer.MIN_VALUE;
        return true;
    }
    
    private void updateLayoutState(int totalSpace, final State state) {
        final LayoutState mLayoutState = this.mLayoutState;
        final boolean b = false;
        mLayoutState.mAvailable = 0;
        this.mLayoutState.mCurrentPosition = totalSpace;
        int totalSpace2 = 0;
        Label_0097: {
            if (((RecyclerView.LayoutManager)this).isSmoothScrolling()) {
                final int targetScrollPosition = state.getTargetScrollPosition();
                if (targetScrollPosition != -1) {
                    if (this.mShouldReverseLayout == targetScrollPosition < totalSpace) {
                        totalSpace2 = this.mPrimaryOrientation.getTotalSpace();
                        totalSpace = 0;
                        break Label_0097;
                    }
                    totalSpace = this.mPrimaryOrientation.getTotalSpace();
                    totalSpace2 = 0;
                    break Label_0097;
                }
            }
            totalSpace = 0;
            totalSpace2 = 0;
        }
        if (((RecyclerView.LayoutManager)this).getClipToPadding()) {
            this.mLayoutState.mStartLine = this.mPrimaryOrientation.getStartAfterPadding() - totalSpace;
            this.mLayoutState.mEndLine = this.mPrimaryOrientation.getEndAfterPadding() + totalSpace2;
        }
        else {
            this.mLayoutState.mEndLine = this.mPrimaryOrientation.getEnd() + totalSpace2;
            this.mLayoutState.mStartLine = -totalSpace;
        }
        this.mLayoutState.mStopInFocusable = false;
        this.mLayoutState.mRecycle = true;
        final LayoutState mLayoutState2 = this.mLayoutState;
        boolean mInfinite = b;
        if (this.mPrimaryOrientation.getMode() == 0) {
            mInfinite = b;
            if (this.mPrimaryOrientation.getEnd() == 0) {
                mInfinite = true;
            }
        }
        mLayoutState2.mInfinite = mInfinite;
    }
    
    private void updateRemainingSpans(final Span span, final int n, final int n2) {
        final int deletedSize = span.getDeletedSize();
        if (n == -1) {
            if (span.getStartLine() + deletedSize <= n2) {
                this.mRemainingSpans.set(span.mIndex, false);
            }
        }
        else if (span.getEndLine() - deletedSize >= n2) {
            this.mRemainingSpans.set(span.mIndex, false);
        }
    }
    
    private int updateSpecWithExtra(final int n, final int n2, final int n3) {
        if (n2 == 0 && n3 == 0) {
            return n;
        }
        final int mode = View$MeasureSpec.getMode(n);
        if (mode != Integer.MIN_VALUE && mode != 1073741824) {
            return n;
        }
        return View$MeasureSpec.makeMeasureSpec(Math.max(0, View$MeasureSpec.getSize(n) - n2 - n3), mode);
    }
    
    boolean areAllEndsEqual() {
        final int endLine = this.mSpans[0].getEndLine(Integer.MIN_VALUE);
        for (int i = 1; i < this.mSpanCount; ++i) {
            if (this.mSpans[i].getEndLine(Integer.MIN_VALUE) != endLine) {
                return false;
            }
        }
        return true;
    }
    
    boolean areAllStartsEqual() {
        final int startLine = this.mSpans[0].getStartLine(Integer.MIN_VALUE);
        for (int i = 1; i < this.mSpanCount; ++i) {
            if (this.mSpans[i].getStartLine(Integer.MIN_VALUE) != startLine) {
                return false;
            }
        }
        return true;
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
    
    boolean checkForGaps() {
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0 || this.mGapStrategy == 0 || !((RecyclerView.LayoutManager)this).isAttachedToWindow()) {
            return false;
        }
        int n;
        int n2;
        if (this.mShouldReverseLayout) {
            n = this.getLastChildPosition();
            n2 = this.getFirstChildPosition();
        }
        else {
            n = this.getFirstChildPosition();
            n2 = this.getLastChildPosition();
        }
        if (n == 0 && this.hasGapsToFix() != null) {
            this.mLazySpanLookup.clear();
            ((RecyclerView.LayoutManager)this).requestSimpleAnimationsInNextLayout();
            ((RecyclerView.LayoutManager)this).requestLayout();
            return true;
        }
        if (!this.mLaidOutInvalidFullSpan) {
            return false;
        }
        int n3;
        if (this.mShouldReverseLayout) {
            n3 = -1;
        }
        else {
            n3 = 1;
        }
        final LazySpanLookup mLazySpanLookup = this.mLazySpanLookup;
        ++n2;
        final FullSpanItem firstFullSpanItemInRange = mLazySpanLookup.getFirstFullSpanItemInRange(n, n2, n3, true);
        if (firstFullSpanItemInRange == null) {
            this.mLaidOutInvalidFullSpan = false;
            this.mLazySpanLookup.forceInvalidateAfter(n2);
            return false;
        }
        final FullSpanItem firstFullSpanItemInRange2 = this.mLazySpanLookup.getFirstFullSpanItemInRange(n, firstFullSpanItemInRange.mPosition, n3 * -1, true);
        if (firstFullSpanItemInRange2 == null) {
            this.mLazySpanLookup.forceInvalidateAfter(firstFullSpanItemInRange.mPosition);
        }
        else {
            this.mLazySpanLookup.forceInvalidateAfter(firstFullSpanItemInRange2.mPosition + 1);
        }
        ((RecyclerView.LayoutManager)this).requestSimpleAnimationsInNextLayout();
        ((RecyclerView.LayoutManager)this).requestLayout();
        return true;
    }
    
    @Override
    public boolean checkLayoutParams(final RecyclerView.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    @Override
    public void collectAdjacentPrefetchPositions(int toIndex, int i, final State state, final LayoutPrefetchRegistry layoutPrefetchRegistry) {
        if (this.mOrientation != 0) {
            toIndex = i;
        }
        if (((RecyclerView.LayoutManager)this).getChildCount() != 0 && toIndex != 0) {
            this.prepareLayoutStateForDelta(toIndex, state);
            if (this.mPrefetchDistances == null || this.mPrefetchDistances.length < this.mSpanCount) {
                this.mPrefetchDistances = new int[this.mSpanCount];
            }
            final int n = 0;
            i = 0;
            toIndex = 0;
            while (i < this.mSpanCount) {
                int n2;
                if (this.mLayoutState.mItemDirection == -1) {
                    n2 = this.mLayoutState.mStartLine - this.mSpans[i].getStartLine(this.mLayoutState.mStartLine);
                }
                else {
                    n2 = this.mSpans[i].getEndLine(this.mLayoutState.mEndLine) - this.mLayoutState.mEndLine;
                }
                int n3 = toIndex;
                if (n2 >= 0) {
                    this.mPrefetchDistances[toIndex] = n2;
                    n3 = toIndex + 1;
                }
                ++i;
                toIndex = n3;
            }
            Arrays.sort(this.mPrefetchDistances, 0, toIndex);
            LayoutState mLayoutState;
            for (i = n; i < toIndex && this.mLayoutState.hasMore(state); ++i) {
                layoutPrefetchRegistry.addPosition(this.mLayoutState.mCurrentPosition, this.mPrefetchDistances[i]);
                mLayoutState = this.mLayoutState;
                mLayoutState.mCurrentPosition += this.mLayoutState.mItemDirection;
            }
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
    public PointF computeScrollVectorForPosition(int calculateScrollDirectionForPosition) {
        calculateScrollDirectionForPosition = this.calculateScrollDirectionForPosition(calculateScrollDirectionForPosition);
        final PointF pointF = new PointF();
        if (calculateScrollDirectionForPosition == 0) {
            return null;
        }
        if (this.mOrientation == 0) {
            pointF.x = (float)calculateScrollDirectionForPosition;
            pointF.y = 0.0f;
        }
        else {
            pointF.x = 0.0f;
            pointF.y = (float)calculateScrollDirectionForPosition;
        }
        return pointF;
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
    
    public int[] findFirstCompletelyVisibleItemPositions(final int[] array) {
        int[] array2;
        if (array == null) {
            array2 = new int[this.mSpanCount];
        }
        else {
            array2 = array;
            if (array.length < this.mSpanCount) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Provided int[]'s size must be more than or equal to span count. Expected:");
                sb.append(this.mSpanCount);
                sb.append(", array size:");
                sb.append(array.length);
                throw new IllegalArgumentException(sb.toString());
            }
        }
        for (int i = 0; i < this.mSpanCount; ++i) {
            array2[i] = this.mSpans[i].findFirstCompletelyVisibleItemPosition();
        }
        return array2;
    }
    
    View findFirstVisibleItemClosestToEnd(final boolean b) {
        final int startAfterPadding = this.mPrimaryOrientation.getStartAfterPadding();
        final int endAfterPadding = this.mPrimaryOrientation.getEndAfterPadding();
        int i = ((RecyclerView.LayoutManager)this).getChildCount() - 1;
        View view = null;
        while (i >= 0) {
            final View child = ((RecyclerView.LayoutManager)this).getChildAt(i);
            final int decoratedStart = this.mPrimaryOrientation.getDecoratedStart(child);
            final int decoratedEnd = this.mPrimaryOrientation.getDecoratedEnd(child);
            View view2 = view;
            if (decoratedEnd > startAfterPadding) {
                if (decoratedStart >= endAfterPadding) {
                    view2 = view;
                }
                else {
                    if (decoratedEnd <= endAfterPadding || !b) {
                        return child;
                    }
                    if ((view2 = view) == null) {
                        view2 = child;
                    }
                }
            }
            --i;
            view = view2;
        }
        return view;
    }
    
    View findFirstVisibleItemClosestToStart(final boolean b) {
        final int startAfterPadding = this.mPrimaryOrientation.getStartAfterPadding();
        final int endAfterPadding = this.mPrimaryOrientation.getEndAfterPadding();
        final int childCount = ((RecyclerView.LayoutManager)this).getChildCount();
        View view = null;
        View view2;
        for (int i = 0; i < childCount; ++i, view = view2) {
            final View child = ((RecyclerView.LayoutManager)this).getChildAt(i);
            final int decoratedStart = this.mPrimaryOrientation.getDecoratedStart(child);
            view2 = view;
            if (this.mPrimaryOrientation.getDecoratedEnd(child) > startAfterPadding) {
                if (decoratedStart >= endAfterPadding) {
                    view2 = view;
                }
                else {
                    if (decoratedStart >= startAfterPadding || !b) {
                        return child;
                    }
                    if ((view2 = view) == null) {
                        view2 = child;
                    }
                }
            }
        }
        return view;
    }
    
    int findFirstVisibleItemPositionInt() {
        View view;
        if (this.mShouldReverseLayout) {
            view = this.findFirstVisibleItemClosestToEnd(true);
        }
        else {
            view = this.findFirstVisibleItemClosestToStart(true);
        }
        int position;
        if (view == null) {
            position = -1;
        }
        else {
            position = ((RecyclerView.LayoutManager)this).getPosition(view);
        }
        return position;
    }
    
    public int[] findFirstVisibleItemPositions(final int[] array) {
        int[] array2;
        if (array == null) {
            array2 = new int[this.mSpanCount];
        }
        else {
            array2 = array;
            if (array.length < this.mSpanCount) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Provided int[]'s size must be more than or equal to span count. Expected:");
                sb.append(this.mSpanCount);
                sb.append(", array size:");
                sb.append(array.length);
                throw new IllegalArgumentException(sb.toString());
            }
        }
        for (int i = 0; i < this.mSpanCount; ++i) {
            array2[i] = this.mSpans[i].findFirstVisibleItemPosition();
        }
        return array2;
    }
    
    public int[] findLastCompletelyVisibleItemPositions(final int[] array) {
        int[] array2;
        if (array == null) {
            array2 = new int[this.mSpanCount];
        }
        else {
            array2 = array;
            if (array.length < this.mSpanCount) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Provided int[]'s size must be more than or equal to span count. Expected:");
                sb.append(this.mSpanCount);
                sb.append(", array size:");
                sb.append(array.length);
                throw new IllegalArgumentException(sb.toString());
            }
        }
        for (int i = 0; i < this.mSpanCount; ++i) {
            array2[i] = this.mSpans[i].findLastCompletelyVisibleItemPosition();
        }
        return array2;
    }
    
    public int[] findLastVisibleItemPositions(final int[] array) {
        int[] array2;
        if (array == null) {
            array2 = new int[this.mSpanCount];
        }
        else {
            array2 = array;
            if (array.length < this.mSpanCount) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Provided int[]'s size must be more than or equal to span count. Expected:");
                sb.append(this.mSpanCount);
                sb.append(", array size:");
                sb.append(array.length);
                throw new IllegalArgumentException(sb.toString());
            }
        }
        for (int i = 0; i < this.mSpanCount; ++i) {
            array2[i] = this.mSpans[i].findLastVisibleItemPosition();
        }
        return array2;
    }
    
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        if (this.mOrientation == 0) {
            return new LayoutParams(-2, -1);
        }
        return new LayoutParams(-1, -2);
    }
    
    @Override
    public RecyclerView.LayoutParams generateLayoutParams(final Context context, final AttributeSet set) {
        return new LayoutParams(context, set);
    }
    
    @Override
    public RecyclerView.LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        if (viewGroup$LayoutParams instanceof ViewGroup$MarginLayoutParams) {
            return new LayoutParams((ViewGroup$MarginLayoutParams)viewGroup$LayoutParams);
        }
        return new LayoutParams(viewGroup$LayoutParams);
    }
    
    @Override
    public int getColumnCountForAccessibility(final Recycler recycler, final State state) {
        if (this.mOrientation == 1) {
            return this.mSpanCount;
        }
        return super.getColumnCountForAccessibility(recycler, state);
    }
    
    int getFirstChildPosition() {
        final int childCount = ((RecyclerView.LayoutManager)this).getChildCount();
        int position = 0;
        if (childCount != 0) {
            position = ((RecyclerView.LayoutManager)this).getPosition(((RecyclerView.LayoutManager)this).getChildAt(0));
        }
        return position;
    }
    
    public int getGapStrategy() {
        return this.mGapStrategy;
    }
    
    int getLastChildPosition() {
        final int childCount = ((RecyclerView.LayoutManager)this).getChildCount();
        int position;
        if (childCount == 0) {
            position = 0;
        }
        else {
            position = ((RecyclerView.LayoutManager)this).getPosition(((RecyclerView.LayoutManager)this).getChildAt(childCount - 1));
        }
        return position;
    }
    
    public int getOrientation() {
        return this.mOrientation;
    }
    
    public boolean getReverseLayout() {
        return this.mReverseLayout;
    }
    
    @Override
    public int getRowCountForAccessibility(final Recycler recycler, final State state) {
        if (this.mOrientation == 0) {
            return this.mSpanCount;
        }
        return super.getRowCountForAccessibility(recycler, state);
    }
    
    public int getSpanCount() {
        return this.mSpanCount;
    }
    
    View hasGapsToFix() {
        int n = ((RecyclerView.LayoutManager)this).getChildCount() - 1;
        final BitSet set = new BitSet(this.mSpanCount);
        set.set(0, this.mSpanCount, true);
        final int mOrientation = this.mOrientation;
        int n2 = -1;
        int n3;
        if (mOrientation == 1 && this.isLayoutRTL()) {
            n3 = 1;
        }
        else {
            n3 = -1;
        }
        int n4;
        if (this.mShouldReverseLayout) {
            n4 = -1;
        }
        else {
            n4 = n + 1;
            n = 0;
        }
        int i = n;
        if (n < n4) {
            n2 = 1;
            i = n;
        }
        while (i != n4) {
            final View child = ((RecyclerView.LayoutManager)this).getChildAt(i);
            final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
            if (set.get(layoutParams.mSpan.mIndex)) {
                if (this.checkSpanForGap(layoutParams.mSpan)) {
                    return child;
                }
                set.clear(layoutParams.mSpan.mIndex);
            }
            if (!layoutParams.mFullSpan) {
                final int n5 = i + n2;
                if (n5 != n4) {
                    final View child2 = ((RecyclerView.LayoutManager)this).getChildAt(n5);
                    boolean b = false;
                    Label_0276: {
                        Label_0274: {
                            if (this.mShouldReverseLayout) {
                                final int decoratedEnd = this.mPrimaryOrientation.getDecoratedEnd(child);
                                final int decoratedEnd2 = this.mPrimaryOrientation.getDecoratedEnd(child2);
                                if (decoratedEnd < decoratedEnd2) {
                                    return child;
                                }
                                if (decoratedEnd != decoratedEnd2) {
                                    break Label_0274;
                                }
                            }
                            else {
                                final int decoratedStart = this.mPrimaryOrientation.getDecoratedStart(child);
                                final int decoratedStart2 = this.mPrimaryOrientation.getDecoratedStart(child2);
                                if (decoratedStart > decoratedStart2) {
                                    return child;
                                }
                                if (decoratedStart != decoratedStart2) {
                                    break Label_0274;
                                }
                            }
                            b = true;
                            break Label_0276;
                        }
                        b = false;
                    }
                    if (b && layoutParams.mSpan.mIndex - ((LayoutParams)child2.getLayoutParams()).mSpan.mIndex < 0 != n3 < 0) {
                        return child;
                    }
                }
            }
            i += n2;
        }
        return null;
    }
    
    public void invalidateSpanAssignments() {
        this.mLazySpanLookup.clear();
        ((RecyclerView.LayoutManager)this).requestLayout();
    }
    
    @Override
    public boolean isAutoMeasureEnabled() {
        return this.mGapStrategy != 0;
    }
    
    boolean isLayoutRTL() {
        final int layoutDirection = ((RecyclerView.LayoutManager)this).getLayoutDirection();
        boolean b = true;
        if (layoutDirection != 1) {
            b = false;
        }
        return b;
    }
    
    @Override
    public void offsetChildrenHorizontal(final int n) {
        super.offsetChildrenHorizontal(n);
        for (int i = 0; i < this.mSpanCount; ++i) {
            this.mSpans[i].onOffset(n);
        }
    }
    
    @Override
    public void offsetChildrenVertical(final int n) {
        super.offsetChildrenVertical(n);
        for (int i = 0; i < this.mSpanCount; ++i) {
            this.mSpans[i].onOffset(n);
        }
    }
    
    @Override
    public void onDetachedFromWindow(final RecyclerView recyclerView, final Recycler recycler) {
        super.onDetachedFromWindow(recyclerView, recycler);
        ((RecyclerView.LayoutManager)this).removeCallbacks(this.mCheckForGapsRunnable);
        for (int i = 0; i < this.mSpanCount; ++i) {
            this.mSpans[i].clear();
        }
        recyclerView.requestLayout();
    }
    
    @Nullable
    @Override
    public View onFocusSearchFailed(View containingItemView, int n, final Recycler recycler, final State state) {
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0) {
            return null;
        }
        containingItemView = ((RecyclerView.LayoutManager)this).findContainingItemView(containingItemView);
        if (containingItemView == null) {
            return null;
        }
        this.resolveShouldLayoutReverse();
        final int convertFocusDirectionToLayoutDirection = this.convertFocusDirectionToLayoutDirection(n);
        if (convertFocusDirectionToLayoutDirection == Integer.MIN_VALUE) {
            return null;
        }
        final LayoutParams layoutParams = (LayoutParams)containingItemView.getLayoutParams();
        final boolean mFullSpan = layoutParams.mFullSpan;
        final Span mSpan = layoutParams.mSpan;
        if (convertFocusDirectionToLayoutDirection == 1) {
            n = this.getLastChildPosition();
        }
        else {
            n = this.getFirstChildPosition();
        }
        this.updateLayoutState(n, state);
        this.setLayoutStateDirection(convertFocusDirectionToLayoutDirection);
        this.mLayoutState.mCurrentPosition = this.mLayoutState.mItemDirection + n;
        this.mLayoutState.mAvailable = (int)(0.33333334f * this.mPrimaryOrientation.getTotalSpace());
        this.mLayoutState.mStopInFocusable = true;
        final LayoutState mLayoutState = this.mLayoutState;
        final int n2 = 0;
        mLayoutState.mRecycle = false;
        this.fill(recycler, this.mLayoutState, state);
        this.mLastLayoutFromEnd = this.mShouldReverseLayout;
        if (!mFullSpan) {
            final View focusableViewAfter = mSpan.getFocusableViewAfter(n, convertFocusDirectionToLayoutDirection);
            if (focusableViewAfter != null && focusableViewAfter != containingItemView) {
                return focusableViewAfter;
            }
        }
        if (this.preferLastSpan(convertFocusDirectionToLayoutDirection)) {
            for (int i = this.mSpanCount - 1; i >= 0; --i) {
                final View focusableViewAfter2 = this.mSpans[i].getFocusableViewAfter(n, convertFocusDirectionToLayoutDirection);
                if (focusableViewAfter2 != null && focusableViewAfter2 != containingItemView) {
                    return focusableViewAfter2;
                }
            }
        }
        else {
            for (int j = 0; j < this.mSpanCount; ++j) {
                final View focusableViewAfter3 = this.mSpans[j].getFocusableViewAfter(n, convertFocusDirectionToLayoutDirection);
                if (focusableViewAfter3 != null && focusableViewAfter3 != containingItemView) {
                    return focusableViewAfter3;
                }
            }
        }
        final boolean mReverseLayout = this.mReverseLayout;
        if (convertFocusDirectionToLayoutDirection == -1) {
            n = 1;
        }
        else {
            n = 0;
        }
        if (((mReverseLayout ^ true) ? 1 : 0) == n) {
            n = 1;
        }
        else {
            n = 0;
        }
        if (!mFullSpan) {
            int n3;
            if (n != 0) {
                n3 = mSpan.findFirstPartiallyVisibleItemPosition();
            }
            else {
                n3 = mSpan.findLastPartiallyVisibleItemPosition();
            }
            final View viewByPosition = ((RecyclerView.LayoutManager)this).findViewByPosition(n3);
            if (viewByPosition != null && viewByPosition != containingItemView) {
                return viewByPosition;
            }
        }
        int k = n2;
        if (this.preferLastSpan(convertFocusDirectionToLayoutDirection)) {
            for (int l = this.mSpanCount - 1; l >= 0; --l) {
                if (l != mSpan.mIndex) {
                    int n4;
                    if (n != 0) {
                        n4 = this.mSpans[l].findFirstPartiallyVisibleItemPosition();
                    }
                    else {
                        n4 = this.mSpans[l].findLastPartiallyVisibleItemPosition();
                    }
                    final View viewByPosition2 = ((RecyclerView.LayoutManager)this).findViewByPosition(n4);
                    if (viewByPosition2 != null && viewByPosition2 != containingItemView) {
                        return viewByPosition2;
                    }
                }
            }
        }
        else {
            while (k < this.mSpanCount) {
                int n5;
                if (n != 0) {
                    n5 = this.mSpans[k].findFirstPartiallyVisibleItemPosition();
                }
                else {
                    n5 = this.mSpans[k].findLastPartiallyVisibleItemPosition();
                }
                final View viewByPosition3 = ((RecyclerView.LayoutManager)this).findViewByPosition(n5);
                if (viewByPosition3 != null && viewByPosition3 != containingItemView) {
                    return viewByPosition3;
                }
                ++k;
            }
        }
        return null;
    }
    
    @Override
    public void onInitializeAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        if (((RecyclerView.LayoutManager)this).getChildCount() > 0) {
            final View firstVisibleItemClosestToStart = this.findFirstVisibleItemClosestToStart(false);
            final View firstVisibleItemClosestToEnd = this.findFirstVisibleItemClosestToEnd(false);
            if (firstVisibleItemClosestToStart == null || firstVisibleItemClosestToEnd == null) {
                return;
            }
            final int position = ((RecyclerView.LayoutManager)this).getPosition(firstVisibleItemClosestToStart);
            final int position2 = ((RecyclerView.LayoutManager)this).getPosition(firstVisibleItemClosestToEnd);
            if (position < position2) {
                accessibilityEvent.setFromIndex(position);
                accessibilityEvent.setToIndex(position2);
            }
            else {
                accessibilityEvent.setFromIndex(position2);
                accessibilityEvent.setToIndex(position);
            }
        }
    }
    
    @Override
    public void onInitializeAccessibilityNodeInfoForItem(final Recycler recycler, final State state, final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        final ViewGroup$LayoutParams layoutParams = view.getLayoutParams();
        if (!(layoutParams instanceof LayoutParams)) {
            super.onInitializeAccessibilityNodeInfoForItem(view, accessibilityNodeInfoCompat);
            return;
        }
        final LayoutParams layoutParams2 = (LayoutParams)layoutParams;
        final int mOrientation = this.mOrientation;
        final int n = 1;
        int mSpanCount = 1;
        if (mOrientation == 0) {
            final int spanIndex = layoutParams2.getSpanIndex();
            if (layoutParams2.mFullSpan) {
                mSpanCount = this.mSpanCount;
            }
            accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(spanIndex, mSpanCount, -1, -1, layoutParams2.mFullSpan, false));
        }
        else {
            final int spanIndex2 = layoutParams2.getSpanIndex();
            int mSpanCount2 = n;
            if (layoutParams2.mFullSpan) {
                mSpanCount2 = this.mSpanCount;
            }
            accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(-1, -1, spanIndex2, mSpanCount2, layoutParams2.mFullSpan, false));
        }
    }
    
    @Override
    public void onItemsAdded(final RecyclerView recyclerView, final int n, final int n2) {
        this.handleUpdate(n, n2, 1);
    }
    
    @Override
    public void onItemsChanged(final RecyclerView recyclerView) {
        this.mLazySpanLookup.clear();
        ((RecyclerView.LayoutManager)this).requestLayout();
    }
    
    @Override
    public void onItemsMoved(final RecyclerView recyclerView, final int n, final int n2, final int n3) {
        this.handleUpdate(n, n2, 8);
    }
    
    @Override
    public void onItemsRemoved(final RecyclerView recyclerView, final int n, final int n2) {
        this.handleUpdate(n, n2, 2);
    }
    
    @Override
    public void onItemsUpdated(final RecyclerView recyclerView, final int n, final int n2, final Object o) {
        this.handleUpdate(n, n2, 4);
    }
    
    @Override
    public void onLayoutChildren(final Recycler recycler, final State state) {
        this.onLayoutChildren(recycler, state, true);
    }
    
    @Override
    public void onLayoutCompleted(final State state) {
        super.onLayoutCompleted(state);
        this.mPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        this.mPendingSavedState = null;
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
        savedState.mReverseLayout = this.mReverseLayout;
        savedState.mAnchorLayoutFromEnd = this.mLastLayoutFromEnd;
        savedState.mLastLayoutRTL = this.mLastLayoutRTL;
        final LazySpanLookup mLazySpanLookup = this.mLazySpanLookup;
        int i = 0;
        if (mLazySpanLookup != null && this.mLazySpanLookup.mData != null) {
            savedState.mSpanLookup = this.mLazySpanLookup.mData;
            savedState.mSpanLookupSize = savedState.mSpanLookup.length;
            savedState.mFullSpanItems = this.mLazySpanLookup.mFullSpanItems;
        }
        else {
            savedState.mSpanLookupSize = 0;
        }
        if (((RecyclerView.LayoutManager)this).getChildCount() > 0) {
            int mAnchorPosition;
            if (this.mLastLayoutFromEnd) {
                mAnchorPosition = this.getLastChildPosition();
            }
            else {
                mAnchorPosition = this.getFirstChildPosition();
            }
            savedState.mAnchorPosition = mAnchorPosition;
            savedState.mVisibleAnchorPosition = this.findFirstVisibleItemPositionInt();
            savedState.mSpanOffsetsSize = this.mSpanCount;
            savedState.mSpanOffsets = new int[this.mSpanCount];
            while (i < this.mSpanCount) {
                int n;
                if (this.mLastLayoutFromEnd) {
                    final int endLine = this.mSpans[i].getEndLine(Integer.MIN_VALUE);
                    if ((n = endLine) != Integer.MIN_VALUE) {
                        n = endLine - this.mPrimaryOrientation.getEndAfterPadding();
                    }
                }
                else {
                    final int startLine = this.mSpans[i].getStartLine(Integer.MIN_VALUE);
                    if ((n = startLine) != Integer.MIN_VALUE) {
                        n = startLine - this.mPrimaryOrientation.getStartAfterPadding();
                    }
                }
                savedState.mSpanOffsets[i] = n;
                ++i;
            }
        }
        else {
            savedState.mAnchorPosition = -1;
            savedState.mVisibleAnchorPosition = -1;
            savedState.mSpanOffsetsSize = 0;
        }
        return (Parcelable)savedState;
    }
    
    @Override
    public void onScrollStateChanged(final int n) {
        if (n == 0) {
            this.checkForGaps();
        }
    }
    
    void prepareLayoutStateForDelta(final int a, final State state) {
        int n;
        int layoutStateDirection;
        if (a > 0) {
            n = this.getLastChildPosition();
            layoutStateDirection = 1;
        }
        else {
            n = this.getFirstChildPosition();
            layoutStateDirection = -1;
        }
        this.mLayoutState.mRecycle = true;
        this.updateLayoutState(n, state);
        this.setLayoutStateDirection(layoutStateDirection);
        this.mLayoutState.mCurrentPosition = n + this.mLayoutState.mItemDirection;
        this.mLayoutState.mAvailable = Math.abs(a);
    }
    
    int scrollBy(int n, final Recycler recycler, final State state) {
        if (((RecyclerView.LayoutManager)this).getChildCount() != 0 && n != 0) {
            this.prepareLayoutStateForDelta(n, state);
            final int fill = this.fill(recycler, this.mLayoutState, state);
            if (this.mLayoutState.mAvailable >= fill) {
                if (n < 0) {
                    n = -fill;
                }
                else {
                    n = fill;
                }
            }
            this.mPrimaryOrientation.offsetChildren(-n);
            this.mLastLayoutFromEnd = this.mShouldReverseLayout;
            this.mLayoutState.mAvailable = 0;
            this.recycle(recycler, this.mLayoutState);
            return n;
        }
        return 0;
    }
    
    @Override
    public int scrollHorizontallyBy(final int n, final Recycler recycler, final State state) {
        return this.scrollBy(n, recycler, state);
    }
    
    @Override
    public void scrollToPosition(final int mPendingScrollPosition) {
        if (this.mPendingSavedState != null && this.mPendingSavedState.mAnchorPosition != mPendingScrollPosition) {
            this.mPendingSavedState.invalidateAnchorPositionInfo();
        }
        this.mPendingScrollPosition = mPendingScrollPosition;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        ((RecyclerView.LayoutManager)this).requestLayout();
    }
    
    public void scrollToPositionWithOffset(final int mPendingScrollPosition, final int mPendingScrollPositionOffset) {
        if (this.mPendingSavedState != null) {
            this.mPendingSavedState.invalidateAnchorPositionInfo();
        }
        this.mPendingScrollPosition = mPendingScrollPosition;
        this.mPendingScrollPositionOffset = mPendingScrollPositionOffset;
        ((RecyclerView.LayoutManager)this).requestLayout();
    }
    
    @Override
    public int scrollVerticallyBy(final int n, final Recycler recycler, final State state) {
        return this.scrollBy(n, recycler, state);
    }
    
    public void setGapStrategy(final int mGapStrategy) {
        this.assertNotInLayoutOrScroll(null);
        if (mGapStrategy == this.mGapStrategy) {
            return;
        }
        if (mGapStrategy != 0 && mGapStrategy != 2) {
            throw new IllegalArgumentException("invalid gap strategy. Must be GAP_HANDLING_NONE or GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS");
        }
        this.mGapStrategy = mGapStrategy;
        ((RecyclerView.LayoutManager)this).requestLayout();
    }
    
    @Override
    public void setMeasuredDimension(final Rect rect, int n, int n2) {
        final int n3 = ((RecyclerView.LayoutManager)this).getPaddingLeft() + ((RecyclerView.LayoutManager)this).getPaddingRight();
        final int n4 = ((RecyclerView.LayoutManager)this).getPaddingTop() + ((RecyclerView.LayoutManager)this).getPaddingBottom();
        if (this.mOrientation == 1) {
            n2 = RecyclerView.LayoutManager.chooseSize(n2, rect.height() + n4, ((RecyclerView.LayoutManager)this).getMinimumHeight());
            n = RecyclerView.LayoutManager.chooseSize(n, this.mSizePerSpan * this.mSpanCount + n3, ((RecyclerView.LayoutManager)this).getMinimumWidth());
        }
        else {
            n = RecyclerView.LayoutManager.chooseSize(n, rect.width() + n3, ((RecyclerView.LayoutManager)this).getMinimumWidth());
            n2 = RecyclerView.LayoutManager.chooseSize(n2, this.mSizePerSpan * this.mSpanCount + n4, ((RecyclerView.LayoutManager)this).getMinimumHeight());
        }
        ((RecyclerView.LayoutManager)this).setMeasuredDimension(n, n2);
    }
    
    public void setOrientation(final int mOrientation) {
        if (mOrientation != 0 && mOrientation != 1) {
            throw new IllegalArgumentException("invalid orientation.");
        }
        this.assertNotInLayoutOrScroll(null);
        if (mOrientation == this.mOrientation) {
            return;
        }
        this.mOrientation = mOrientation;
        final OrientationHelper mPrimaryOrientation = this.mPrimaryOrientation;
        this.mPrimaryOrientation = this.mSecondaryOrientation;
        this.mSecondaryOrientation = mPrimaryOrientation;
        ((RecyclerView.LayoutManager)this).requestLayout();
    }
    
    public void setReverseLayout(final boolean b) {
        this.assertNotInLayoutOrScroll(null);
        if (this.mPendingSavedState != null && this.mPendingSavedState.mReverseLayout != b) {
            this.mPendingSavedState.mReverseLayout = b;
        }
        this.mReverseLayout = b;
        ((RecyclerView.LayoutManager)this).requestLayout();
    }
    
    public void setSpanCount(int i) {
        this.assertNotInLayoutOrScroll(null);
        if (i != this.mSpanCount) {
            this.invalidateSpanAssignments();
            this.mSpanCount = i;
            this.mRemainingSpans = new BitSet(this.mSpanCount);
            this.mSpans = new Span[this.mSpanCount];
            for (i = 0; i < this.mSpanCount; ++i) {
                this.mSpans[i] = new Span(i);
            }
            ((RecyclerView.LayoutManager)this).requestLayout();
        }
    }
    
    @Override
    public void smoothScrollToPosition(final RecyclerView recyclerView, final State state, final int targetPosition) {
        final LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext());
        ((RecyclerView.SmoothScroller)linearSmoothScroller).setTargetPosition(targetPosition);
        ((RecyclerView.LayoutManager)this).startSmoothScroll(linearSmoothScroller);
    }
    
    @Override
    public boolean supportsPredictiveItemAnimations() {
        return this.mPendingSavedState == null;
    }
    
    boolean updateAnchorFromPendingData(final State state, final AnchorInfo anchorInfo) {
        final boolean preLayout = state.isPreLayout();
        boolean mLayoutFromEnd = false;
        if (preLayout || this.mPendingScrollPosition == -1) {
            return false;
        }
        if (this.mPendingScrollPosition >= 0 && this.mPendingScrollPosition < state.getItemCount()) {
            if (this.mPendingSavedState != null && this.mPendingSavedState.mAnchorPosition != -1 && this.mPendingSavedState.mSpanOffsetsSize >= 1) {
                anchorInfo.mOffset = Integer.MIN_VALUE;
                anchorInfo.mPosition = this.mPendingScrollPosition;
            }
            else {
                final View viewByPosition = ((RecyclerView.LayoutManager)this).findViewByPosition(this.mPendingScrollPosition);
                if (viewByPosition != null) {
                    int mPosition;
                    if (this.mShouldReverseLayout) {
                        mPosition = this.getLastChildPosition();
                    }
                    else {
                        mPosition = this.getFirstChildPosition();
                    }
                    anchorInfo.mPosition = mPosition;
                    if (this.mPendingScrollPositionOffset != Integer.MIN_VALUE) {
                        if (anchorInfo.mLayoutFromEnd) {
                            anchorInfo.mOffset = this.mPrimaryOrientation.getEndAfterPadding() - this.mPendingScrollPositionOffset - this.mPrimaryOrientation.getDecoratedEnd(viewByPosition);
                        }
                        else {
                            anchorInfo.mOffset = this.mPrimaryOrientation.getStartAfterPadding() + this.mPendingScrollPositionOffset - this.mPrimaryOrientation.getDecoratedStart(viewByPosition);
                        }
                        return true;
                    }
                    if (this.mPrimaryOrientation.getDecoratedMeasurement(viewByPosition) > this.mPrimaryOrientation.getTotalSpace()) {
                        int mOffset;
                        if (anchorInfo.mLayoutFromEnd) {
                            mOffset = this.mPrimaryOrientation.getEndAfterPadding();
                        }
                        else {
                            mOffset = this.mPrimaryOrientation.getStartAfterPadding();
                        }
                        anchorInfo.mOffset = mOffset;
                        return true;
                    }
                    final int n = this.mPrimaryOrientation.getDecoratedStart(viewByPosition) - this.mPrimaryOrientation.getStartAfterPadding();
                    if (n < 0) {
                        anchorInfo.mOffset = -n;
                        return true;
                    }
                    final int mOffset2 = this.mPrimaryOrientation.getEndAfterPadding() - this.mPrimaryOrientation.getDecoratedEnd(viewByPosition);
                    if (mOffset2 < 0) {
                        anchorInfo.mOffset = mOffset2;
                        return true;
                    }
                    anchorInfo.mOffset = Integer.MIN_VALUE;
                }
                else {
                    anchorInfo.mPosition = this.mPendingScrollPosition;
                    if (this.mPendingScrollPositionOffset == Integer.MIN_VALUE) {
                        if (this.calculateScrollDirectionForPosition(anchorInfo.mPosition) == 1) {
                            mLayoutFromEnd = true;
                        }
                        anchorInfo.mLayoutFromEnd = mLayoutFromEnd;
                        anchorInfo.assignCoordinateFromPadding();
                    }
                    else {
                        anchorInfo.assignCoordinateFromPadding(this.mPendingScrollPositionOffset);
                    }
                    anchorInfo.mInvalidateOffsets = true;
                }
            }
            return true;
        }
        this.mPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        return false;
    }
    
    void updateAnchorInfoForLayout(final State state, final AnchorInfo anchorInfo) {
        if (this.updateAnchorFromPendingData(state, anchorInfo)) {
            return;
        }
        if (this.updateAnchorFromChildren(state, anchorInfo)) {
            return;
        }
        anchorInfo.assignCoordinateFromPadding();
        anchorInfo.mPosition = 0;
    }
    
    void updateMeasureSpecs(final int n) {
        this.mSizePerSpan = n / this.mSpanCount;
        this.mFullSizeSpec = View$MeasureSpec.makeMeasureSpec(n, this.mSecondaryOrientation.getMode());
    }
    
    class AnchorInfo
    {
        boolean mInvalidateOffsets;
        boolean mLayoutFromEnd;
        int mOffset;
        int mPosition;
        int[] mSpanReferenceLines;
        boolean mValid;
        final StaggeredGridLayoutManager this$0;
        
        AnchorInfo(final StaggeredGridLayoutManager this$0) {
            this.this$0 = this$0;
            this.reset();
        }
        
        void assignCoordinateFromPadding() {
            int mOffset;
            if (this.mLayoutFromEnd) {
                mOffset = this.this$0.mPrimaryOrientation.getEndAfterPadding();
            }
            else {
                mOffset = this.this$0.mPrimaryOrientation.getStartAfterPadding();
            }
            this.mOffset = mOffset;
        }
        
        void assignCoordinateFromPadding(final int n) {
            if (this.mLayoutFromEnd) {
                this.mOffset = this.this$0.mPrimaryOrientation.getEndAfterPadding() - n;
            }
            else {
                this.mOffset = this.this$0.mPrimaryOrientation.getStartAfterPadding() + n;
            }
        }
        
        void reset() {
            this.mPosition = -1;
            this.mOffset = Integer.MIN_VALUE;
            this.mLayoutFromEnd = false;
            this.mInvalidateOffsets = false;
            this.mValid = false;
            if (this.mSpanReferenceLines != null) {
                Arrays.fill(this.mSpanReferenceLines, -1);
            }
        }
        
        void saveSpanReferenceLines(final Span[] array) {
            final int length = array.length;
            if (this.mSpanReferenceLines == null || this.mSpanReferenceLines.length < length) {
                this.mSpanReferenceLines = new int[this.this$0.mSpans.length];
            }
            for (int i = 0; i < length; ++i) {
                this.mSpanReferenceLines[i] = array[i].getStartLine(Integer.MIN_VALUE);
            }
        }
    }
    
    public static class LayoutParams extends RecyclerView.LayoutParams
    {
        public static final int INVALID_SPAN_ID = -1;
        boolean mFullSpan;
        Span mSpan;
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
        }
        
        public LayoutParams(final RecyclerView.LayoutParams layoutParams) {
            super(layoutParams);
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
        }
        
        public LayoutParams(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
            super(viewGroup$MarginLayoutParams);
        }
        
        public final int getSpanIndex() {
            if (this.mSpan == null) {
                return -1;
            }
            return this.mSpan.mIndex;
        }
        
        public boolean isFullSpan() {
            return this.mFullSpan;
        }
        
        public void setFullSpan(final boolean mFullSpan) {
            this.mFullSpan = mFullSpan;
        }
    }
    
    static class LazySpanLookup
    {
        private static final int MIN_SIZE = 10;
        int[] mData;
        List<FullSpanItem> mFullSpanItems;
        
        private int invalidateFullSpansAfter(final int n) {
            if (this.mFullSpanItems == null) {
                return -1;
            }
            final FullSpanItem fullSpanItem = this.getFullSpanItem(n);
            if (fullSpanItem != null) {
                this.mFullSpanItems.remove(fullSpanItem);
            }
            final int size = this.mFullSpanItems.size();
            int i = 0;
            while (true) {
                while (i < size) {
                    if (this.mFullSpanItems.get(i).mPosition >= n) {
                        if (i != -1) {
                            final FullSpanItem fullSpanItem2 = this.mFullSpanItems.get(i);
                            this.mFullSpanItems.remove(i);
                            return fullSpanItem2.mPosition;
                        }
                        return -1;
                    }
                    else {
                        ++i;
                    }
                }
                i = -1;
                continue;
            }
        }
        
        private void offsetFullSpansForAddition(final int n, final int n2) {
            if (this.mFullSpanItems == null) {
                return;
            }
            for (int i = this.mFullSpanItems.size() - 1; i >= 0; --i) {
                final FullSpanItem fullSpanItem = this.mFullSpanItems.get(i);
                if (fullSpanItem.mPosition >= n) {
                    fullSpanItem.mPosition += n2;
                }
            }
        }
        
        private void offsetFullSpansForRemoval(final int n, final int n2) {
            if (this.mFullSpanItems == null) {
                return;
            }
            for (int i = this.mFullSpanItems.size() - 1; i >= 0; --i) {
                final FullSpanItem fullSpanItem = this.mFullSpanItems.get(i);
                if (fullSpanItem.mPosition >= n) {
                    if (fullSpanItem.mPosition < n + n2) {
                        this.mFullSpanItems.remove(i);
                    }
                    else {
                        fullSpanItem.mPosition -= n2;
                    }
                }
            }
        }
        
        public void addFullSpanItem(final FullSpanItem fullSpanItem) {
            if (this.mFullSpanItems == null) {
                this.mFullSpanItems = new ArrayList<FullSpanItem>();
            }
            for (int size = this.mFullSpanItems.size(), i = 0; i < size; ++i) {
                final FullSpanItem fullSpanItem2 = this.mFullSpanItems.get(i);
                if (fullSpanItem2.mPosition == fullSpanItem.mPosition) {
                    this.mFullSpanItems.remove(i);
                }
                if (fullSpanItem2.mPosition >= fullSpanItem.mPosition) {
                    this.mFullSpanItems.add(i, fullSpanItem);
                    return;
                }
            }
            this.mFullSpanItems.add(fullSpanItem);
        }
        
        void clear() {
            if (this.mData != null) {
                Arrays.fill(this.mData, -1);
            }
            this.mFullSpanItems = null;
        }
        
        void ensureSize(final int a) {
            if (this.mData == null) {
                Arrays.fill(this.mData = new int[Math.max(a, 10) + 1], -1);
            }
            else if (a >= this.mData.length) {
                final int[] mData = this.mData;
                System.arraycopy(mData, 0, this.mData = new int[this.sizeForPosition(a)], 0, mData.length);
                Arrays.fill(this.mData, mData.length, this.mData.length, -1);
            }
        }
        
        int forceInvalidateAfter(final int n) {
            if (this.mFullSpanItems != null) {
                for (int i = this.mFullSpanItems.size() - 1; i >= 0; --i) {
                    if (this.mFullSpanItems.get(i).mPosition >= n) {
                        this.mFullSpanItems.remove(i);
                    }
                }
            }
            return this.invalidateAfter(n);
        }
        
        public FullSpanItem getFirstFullSpanItemInRange(final int n, final int n2, final int n3, final boolean b) {
            if (this.mFullSpanItems == null) {
                return null;
            }
            for (int size = this.mFullSpanItems.size(), i = 0; i < size; ++i) {
                final FullSpanItem fullSpanItem = this.mFullSpanItems.get(i);
                if (fullSpanItem.mPosition >= n2) {
                    return null;
                }
                if (fullSpanItem.mPosition >= n && (n3 == 0 || fullSpanItem.mGapDir == n3 || (b && fullSpanItem.mHasUnwantedGapAfter))) {
                    return fullSpanItem;
                }
            }
            return null;
        }
        
        public FullSpanItem getFullSpanItem(final int n) {
            if (this.mFullSpanItems == null) {
                return null;
            }
            for (int i = this.mFullSpanItems.size() - 1; i >= 0; --i) {
                final FullSpanItem fullSpanItem = this.mFullSpanItems.get(i);
                if (fullSpanItem.mPosition == n) {
                    return fullSpanItem;
                }
            }
            return null;
        }
        
        int getSpan(final int n) {
            if (this.mData != null && n < this.mData.length) {
                return this.mData[n];
            }
            return -1;
        }
        
        int invalidateAfter(final int n) {
            if (this.mData == null) {
                return -1;
            }
            if (n >= this.mData.length) {
                return -1;
            }
            int invalidateFullSpansAfter = this.invalidateFullSpansAfter(n);
            if (invalidateFullSpansAfter == -1) {
                Arrays.fill(this.mData, n, this.mData.length, -1);
                return this.mData.length;
            }
            final int[] mData = this.mData;
            ++invalidateFullSpansAfter;
            Arrays.fill(mData, n, invalidateFullSpansAfter, -1);
            return invalidateFullSpansAfter;
        }
        
        void offsetForAddition(final int fromIndex, final int n) {
            if (this.mData != null && fromIndex < this.mData.length) {
                final int toIndex = fromIndex + n;
                this.ensureSize(toIndex);
                System.arraycopy(this.mData, fromIndex, this.mData, toIndex, this.mData.length - fromIndex - n);
                Arrays.fill(this.mData, fromIndex, toIndex, -1);
                this.offsetFullSpansForAddition(fromIndex, n);
            }
        }
        
        void offsetForRemoval(final int n, final int n2) {
            if (this.mData != null && n < this.mData.length) {
                final int n3 = n + n2;
                this.ensureSize(n3);
                System.arraycopy(this.mData, n3, this.mData, n, this.mData.length - n - n2);
                Arrays.fill(this.mData, this.mData.length - n2, this.mData.length, -1);
                this.offsetFullSpansForRemoval(n, n2);
            }
        }
        
        void setSpan(final int n, final Span span) {
            this.ensureSize(n);
            this.mData[n] = span.mIndex;
        }
        
        int sizeForPosition(final int n) {
            int i;
            for (i = this.mData.length; i <= n; i *= 2) {}
            return i;
        }
        
        static class FullSpanItem implements Parcelable
        {
            public static final Parcelable$Creator<FullSpanItem> CREATOR;
            int mGapDir;
            int[] mGapPerSpan;
            boolean mHasUnwantedGapAfter;
            int mPosition;
            
            static {
                CREATOR = (Parcelable$Creator)new Parcelable$Creator<FullSpanItem>() {
                    public FullSpanItem createFromParcel(final Parcel parcel) {
                        return new FullSpanItem(parcel);
                    }
                    
                    public FullSpanItem[] newArray(final int n) {
                        return new FullSpanItem[n];
                    }
                };
            }
            
            FullSpanItem() {
            }
            
            FullSpanItem(final Parcel parcel) {
                this.mPosition = parcel.readInt();
                this.mGapDir = parcel.readInt();
                final int int1 = parcel.readInt();
                boolean mHasUnwantedGapAfter = true;
                if (int1 != 1) {
                    mHasUnwantedGapAfter = false;
                }
                this.mHasUnwantedGapAfter = mHasUnwantedGapAfter;
                final int int2 = parcel.readInt();
                if (int2 > 0) {
                    parcel.readIntArray(this.mGapPerSpan = new int[int2]);
                }
            }
            
            public int describeContents() {
                return 0;
            }
            
            int getGapForSpan(int n) {
                if (this.mGapPerSpan == null) {
                    n = 0;
                }
                else {
                    n = this.mGapPerSpan[n];
                }
                return n;
            }
            
            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder();
                sb.append("FullSpanItem{mPosition=");
                sb.append(this.mPosition);
                sb.append(", mGapDir=");
                sb.append(this.mGapDir);
                sb.append(", mHasUnwantedGapAfter=");
                sb.append(this.mHasUnwantedGapAfter);
                sb.append(", mGapPerSpan=");
                sb.append(Arrays.toString(this.mGapPerSpan));
                sb.append('}');
                return sb.toString();
            }
            
            public void writeToParcel(final Parcel parcel, final int n) {
                parcel.writeInt(this.mPosition);
                parcel.writeInt(this.mGapDir);
                parcel.writeInt((int)(this.mHasUnwantedGapAfter ? 1 : 0));
                if (this.mGapPerSpan != null && this.mGapPerSpan.length > 0) {
                    parcel.writeInt(this.mGapPerSpan.length);
                    parcel.writeIntArray(this.mGapPerSpan);
                }
                else {
                    parcel.writeInt(0);
                }
            }
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static class SavedState implements Parcelable
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        boolean mAnchorLayoutFromEnd;
        int mAnchorPosition;
        List<FullSpanItem> mFullSpanItems;
        boolean mLastLayoutRTL;
        boolean mReverseLayout;
        int[] mSpanLookup;
        int mSpanLookupSize;
        int[] mSpanOffsets;
        int mSpanOffsetsSize;
        int mVisibleAnchorPosition;
        
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
            this.mVisibleAnchorPosition = parcel.readInt();
            this.mSpanOffsetsSize = parcel.readInt();
            if (this.mSpanOffsetsSize > 0) {
                parcel.readIntArray(this.mSpanOffsets = new int[this.mSpanOffsetsSize]);
            }
            this.mSpanLookupSize = parcel.readInt();
            if (this.mSpanLookupSize > 0) {
                parcel.readIntArray(this.mSpanLookup = new int[this.mSpanLookupSize]);
            }
            final int int1 = parcel.readInt();
            final boolean b = false;
            this.mReverseLayout = (int1 == 1);
            this.mAnchorLayoutFromEnd = (parcel.readInt() == 1);
            boolean mLastLayoutRTL = b;
            if (parcel.readInt() == 1) {
                mLastLayoutRTL = true;
            }
            this.mLastLayoutRTL = mLastLayoutRTL;
            this.mFullSpanItems = parcel.readArrayList(FullSpanItem.class.getClassLoader());
        }
        
        public SavedState(final SavedState savedState) {
            this.mSpanOffsetsSize = savedState.mSpanOffsetsSize;
            this.mAnchorPosition = savedState.mAnchorPosition;
            this.mVisibleAnchorPosition = savedState.mVisibleAnchorPosition;
            this.mSpanOffsets = savedState.mSpanOffsets;
            this.mSpanLookupSize = savedState.mSpanLookupSize;
            this.mSpanLookup = savedState.mSpanLookup;
            this.mReverseLayout = savedState.mReverseLayout;
            this.mAnchorLayoutFromEnd = savedState.mAnchorLayoutFromEnd;
            this.mLastLayoutRTL = savedState.mLastLayoutRTL;
            this.mFullSpanItems = savedState.mFullSpanItems;
        }
        
        public int describeContents() {
            return 0;
        }
        
        void invalidateAnchorPositionInfo() {
            this.mSpanOffsets = null;
            this.mSpanOffsetsSize = 0;
            this.mAnchorPosition = -1;
            this.mVisibleAnchorPosition = -1;
        }
        
        void invalidateSpanInfo() {
            this.mSpanOffsets = null;
            this.mSpanOffsetsSize = 0;
            this.mSpanLookupSize = 0;
            this.mSpanLookup = null;
            this.mFullSpanItems = null;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            parcel.writeInt(this.mAnchorPosition);
            parcel.writeInt(this.mVisibleAnchorPosition);
            parcel.writeInt(this.mSpanOffsetsSize);
            if (this.mSpanOffsetsSize > 0) {
                parcel.writeIntArray(this.mSpanOffsets);
            }
            parcel.writeInt(this.mSpanLookupSize);
            if (this.mSpanLookupSize > 0) {
                parcel.writeIntArray(this.mSpanLookup);
            }
            parcel.writeInt((int)(this.mReverseLayout ? 1 : 0));
            parcel.writeInt((int)(this.mAnchorLayoutFromEnd ? 1 : 0));
            parcel.writeInt((int)(this.mLastLayoutRTL ? 1 : 0));
            parcel.writeList((List)this.mFullSpanItems);
        }
    }
    
    class Span
    {
        static final int INVALID_LINE = Integer.MIN_VALUE;
        int mCachedEnd;
        int mCachedStart;
        int mDeletedSize;
        final int mIndex;
        ArrayList<View> mViews;
        final StaggeredGridLayoutManager this$0;
        
        Span(final StaggeredGridLayoutManager this$0, final int mIndex) {
            this.this$0 = this$0;
            this.mViews = new ArrayList<View>();
            this.mCachedStart = Integer.MIN_VALUE;
            this.mCachedEnd = Integer.MIN_VALUE;
            this.mDeletedSize = 0;
            this.mIndex = mIndex;
        }
        
        void appendToSpan(final View e) {
            final LayoutParams layoutParams = this.getLayoutParams(e);
            layoutParams.mSpan = this;
            this.mViews.add(e);
            this.mCachedEnd = Integer.MIN_VALUE;
            if (this.mViews.size() == 1) {
                this.mCachedStart = Integer.MIN_VALUE;
            }
            if (((RecyclerView.LayoutParams)layoutParams).isItemRemoved() || ((RecyclerView.LayoutParams)layoutParams).isItemChanged()) {
                this.mDeletedSize += this.this$0.mPrimaryOrientation.getDecoratedMeasurement(e);
            }
        }
        
        void cacheReferenceLineAndClear(final boolean b, final int n) {
            int n2;
            if (b) {
                n2 = this.getEndLine(Integer.MIN_VALUE);
            }
            else {
                n2 = this.getStartLine(Integer.MIN_VALUE);
            }
            this.clear();
            if (n2 == Integer.MIN_VALUE) {
                return;
            }
            if ((b && n2 < this.this$0.mPrimaryOrientation.getEndAfterPadding()) || (!b && n2 > this.this$0.mPrimaryOrientation.getStartAfterPadding())) {
                return;
            }
            int n3 = n2;
            if (n != Integer.MIN_VALUE) {
                n3 = n2 + n;
            }
            this.mCachedEnd = n3;
            this.mCachedStart = n3;
        }
        
        void calculateCachedEnd() {
            final View view = this.mViews.get(this.mViews.size() - 1);
            final LayoutParams layoutParams = this.getLayoutParams(view);
            this.mCachedEnd = this.this$0.mPrimaryOrientation.getDecoratedEnd(view);
            if (layoutParams.mFullSpan) {
                final FullSpanItem fullSpanItem = this.this$0.mLazySpanLookup.getFullSpanItem(((RecyclerView.LayoutParams)layoutParams).getViewLayoutPosition());
                if (fullSpanItem != null && fullSpanItem.mGapDir == 1) {
                    this.mCachedEnd += fullSpanItem.getGapForSpan(this.mIndex);
                }
            }
        }
        
        void calculateCachedStart() {
            final View view = this.mViews.get(0);
            final LayoutParams layoutParams = this.getLayoutParams(view);
            this.mCachedStart = this.this$0.mPrimaryOrientation.getDecoratedStart(view);
            if (layoutParams.mFullSpan) {
                final FullSpanItem fullSpanItem = this.this$0.mLazySpanLookup.getFullSpanItem(((RecyclerView.LayoutParams)layoutParams).getViewLayoutPosition());
                if (fullSpanItem != null && fullSpanItem.mGapDir == -1) {
                    this.mCachedStart -= fullSpanItem.getGapForSpan(this.mIndex);
                }
            }
        }
        
        void clear() {
            this.mViews.clear();
            this.invalidateCache();
            this.mDeletedSize = 0;
        }
        
        public int findFirstCompletelyVisibleItemPosition() {
            int n;
            if (this.this$0.mReverseLayout) {
                n = this.findOneVisibleChild(this.mViews.size() - 1, -1, true);
            }
            else {
                n = this.findOneVisibleChild(0, this.mViews.size(), true);
            }
            return n;
        }
        
        public int findFirstPartiallyVisibleItemPosition() {
            int n;
            if (this.this$0.mReverseLayout) {
                n = this.findOnePartiallyVisibleChild(this.mViews.size() - 1, -1, true);
            }
            else {
                n = this.findOnePartiallyVisibleChild(0, this.mViews.size(), true);
            }
            return n;
        }
        
        public int findFirstVisibleItemPosition() {
            int n;
            if (this.this$0.mReverseLayout) {
                n = this.findOneVisibleChild(this.mViews.size() - 1, -1, false);
            }
            else {
                n = this.findOneVisibleChild(0, this.mViews.size(), false);
            }
            return n;
        }
        
        public int findLastCompletelyVisibleItemPosition() {
            int n;
            if (this.this$0.mReverseLayout) {
                n = this.findOneVisibleChild(0, this.mViews.size(), true);
            }
            else {
                n = this.findOneVisibleChild(this.mViews.size() - 1, -1, true);
            }
            return n;
        }
        
        public int findLastPartiallyVisibleItemPosition() {
            int n;
            if (this.this$0.mReverseLayout) {
                n = this.findOnePartiallyVisibleChild(0, this.mViews.size(), true);
            }
            else {
                n = this.findOnePartiallyVisibleChild(this.mViews.size() - 1, -1, true);
            }
            return n;
        }
        
        public int findLastVisibleItemPosition() {
            int n;
            if (this.this$0.mReverseLayout) {
                n = this.findOneVisibleChild(0, this.mViews.size(), false);
            }
            else {
                n = this.findOneVisibleChild(this.mViews.size() - 1, -1, false);
            }
            return n;
        }
        
        int findOnePartiallyOrCompletelyVisibleChild(int i, final int n, final boolean b, final boolean b2, final boolean b3) {
            final int startAfterPadding = this.this$0.mPrimaryOrientation.getStartAfterPadding();
            final int endAfterPadding = this.this$0.mPrimaryOrientation.getEndAfterPadding();
            int n2;
            if (n > i) {
                n2 = 1;
            }
            else {
                n2 = -1;
            }
            while (i != n) {
                final View view = this.mViews.get(i);
                final int decoratedStart = this.this$0.mPrimaryOrientation.getDecoratedStart(view);
                final int decoratedEnd = this.this$0.mPrimaryOrientation.getDecoratedEnd(view);
                boolean b4 = false;
                final boolean b5 = b3 ? (decoratedStart <= endAfterPadding) : (decoratedStart < endAfterPadding);
                Label_0149: {
                    if (b3) {
                        if (decoratedEnd < startAfterPadding) {
                            break Label_0149;
                        }
                    }
                    else if (decoratedEnd <= startAfterPadding) {
                        break Label_0149;
                    }
                    b4 = true;
                }
                if (b5 && b4) {
                    if (b && b2) {
                        if (decoratedStart >= startAfterPadding && decoratedEnd <= endAfterPadding) {
                            return ((RecyclerView.LayoutManager)this.this$0).getPosition(view);
                        }
                    }
                    else {
                        if (b2) {
                            return ((RecyclerView.LayoutManager)this.this$0).getPosition(view);
                        }
                        if (decoratedStart < startAfterPadding || decoratedEnd > endAfterPadding) {
                            return ((RecyclerView.LayoutManager)this.this$0).getPosition(view);
                        }
                    }
                }
                i += n2;
            }
            return -1;
        }
        
        int findOnePartiallyVisibleChild(final int n, final int n2, final boolean b) {
            return this.findOnePartiallyOrCompletelyVisibleChild(n, n2, false, false, b);
        }
        
        int findOneVisibleChild(final int n, final int n2, final boolean b) {
            return this.findOnePartiallyOrCompletelyVisibleChild(n, n2, b, true, false);
        }
        
        public int getDeletedSize() {
            return this.mDeletedSize;
        }
        
        int getEndLine() {
            if (this.mCachedEnd != Integer.MIN_VALUE) {
                return this.mCachedEnd;
            }
            this.calculateCachedEnd();
            return this.mCachedEnd;
        }
        
        int getEndLine(final int n) {
            if (this.mCachedEnd != Integer.MIN_VALUE) {
                return this.mCachedEnd;
            }
            if (this.mViews.size() == 0) {
                return n;
            }
            this.calculateCachedEnd();
            return this.mCachedEnd;
        }
        
        public View getFocusableViewAfter(final int n, int n2) {
            View view = null;
            final View view2 = null;
            View view4;
            if (n2 == -1) {
                final int size = this.mViews.size();
                n2 = 0;
                View view3 = view2;
                while (true) {
                    view4 = view3;
                    if (n2 >= size) {
                        break;
                    }
                    final View view5 = this.mViews.get(n2);
                    if (this.this$0.mReverseLayout) {
                        view4 = view3;
                        if (((RecyclerView.LayoutManager)this.this$0).getPosition(view5) <= n) {
                            break;
                        }
                    }
                    if (!this.this$0.mReverseLayout && ((RecyclerView.LayoutManager)this.this$0).getPosition(view5) >= n) {
                        view4 = view3;
                        break;
                    }
                    view4 = view3;
                    if (!view5.hasFocusable()) {
                        break;
                    }
                    ++n2;
                    view3 = view5;
                }
            }
            else {
                n2 = this.mViews.size() - 1;
                while (true) {
                    view4 = view;
                    if (n2 < 0) {
                        break;
                    }
                    final View view6 = this.mViews.get(n2);
                    if (this.this$0.mReverseLayout) {
                        view4 = view;
                        if (((RecyclerView.LayoutManager)this.this$0).getPosition(view6) >= n) {
                            break;
                        }
                    }
                    if (!this.this$0.mReverseLayout && ((RecyclerView.LayoutManager)this.this$0).getPosition(view6) <= n) {
                        view4 = view;
                        break;
                    }
                    view4 = view;
                    if (!view6.hasFocusable()) {
                        break;
                    }
                    --n2;
                    view = view6;
                }
            }
            return view4;
        }
        
        LayoutParams getLayoutParams(final View view) {
            return (LayoutParams)view.getLayoutParams();
        }
        
        int getStartLine() {
            if (this.mCachedStart != Integer.MIN_VALUE) {
                return this.mCachedStart;
            }
            this.calculateCachedStart();
            return this.mCachedStart;
        }
        
        int getStartLine(final int n) {
            if (this.mCachedStart != Integer.MIN_VALUE) {
                return this.mCachedStart;
            }
            if (this.mViews.size() == 0) {
                return n;
            }
            this.calculateCachedStart();
            return this.mCachedStart;
        }
        
        void invalidateCache() {
            this.mCachedStart = Integer.MIN_VALUE;
            this.mCachedEnd = Integer.MIN_VALUE;
        }
        
        void onOffset(final int n) {
            if (this.mCachedStart != Integer.MIN_VALUE) {
                this.mCachedStart += n;
            }
            if (this.mCachedEnd != Integer.MIN_VALUE) {
                this.mCachedEnd += n;
            }
        }
        
        void popEnd() {
            final int size = this.mViews.size();
            final View view = this.mViews.remove(size - 1);
            final LayoutParams layoutParams = this.getLayoutParams(view);
            layoutParams.mSpan = null;
            if (((RecyclerView.LayoutParams)layoutParams).isItemRemoved() || ((RecyclerView.LayoutParams)layoutParams).isItemChanged()) {
                this.mDeletedSize -= this.this$0.mPrimaryOrientation.getDecoratedMeasurement(view);
            }
            if (size == 1) {
                this.mCachedStart = Integer.MIN_VALUE;
            }
            this.mCachedEnd = Integer.MIN_VALUE;
        }
        
        void popStart() {
            final View view = this.mViews.remove(0);
            final LayoutParams layoutParams = this.getLayoutParams(view);
            layoutParams.mSpan = null;
            if (this.mViews.size() == 0) {
                this.mCachedEnd = Integer.MIN_VALUE;
            }
            if (((RecyclerView.LayoutParams)layoutParams).isItemRemoved() || ((RecyclerView.LayoutParams)layoutParams).isItemChanged()) {
                this.mDeletedSize -= this.this$0.mPrimaryOrientation.getDecoratedMeasurement(view);
            }
            this.mCachedStart = Integer.MIN_VALUE;
        }
        
        void prependToSpan(final View element) {
            final LayoutParams layoutParams = this.getLayoutParams(element);
            layoutParams.mSpan = this;
            this.mViews.add(0, element);
            this.mCachedStart = Integer.MIN_VALUE;
            if (this.mViews.size() == 1) {
                this.mCachedEnd = Integer.MIN_VALUE;
            }
            if (((RecyclerView.LayoutParams)layoutParams).isItemRemoved() || ((RecyclerView.LayoutParams)layoutParams).isItemChanged()) {
                this.mDeletedSize += this.this$0.mPrimaryOrientation.getDecoratedMeasurement(element);
            }
        }
        
        void setLine(final int n) {
            this.mCachedStart = n;
            this.mCachedEnd = n;
        }
    }
}
