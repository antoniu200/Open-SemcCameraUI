// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.widget;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.util.Arrays;
import android.view.View$MeasureSpec;
import android.view.ViewGroup$MarginLayoutParams;
import android.view.ViewGroup$LayoutParams;
import android.util.Log;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import android.util.SparseIntArray;
import android.graphics.Rect;

public class GridLayoutManager extends LinearLayoutManager
{
    private static final boolean DEBUG = false;
    public static final int DEFAULT_SPAN_COUNT = -1;
    private static final String TAG = "GridLayoutManager";
    int[] mCachedBorders;
    final Rect mDecorInsets;
    boolean mPendingSpanCountChange;
    final SparseIntArray mPreLayoutSpanIndexCache;
    final SparseIntArray mPreLayoutSpanSizeCache;
    View[] mSet;
    int mSpanCount;
    SpanSizeLookup mSpanSizeLookup;
    
    public GridLayoutManager(final Context context, final int spanCount) {
        super(context);
        this.mPendingSpanCountChange = false;
        this.mSpanCount = -1;
        this.mPreLayoutSpanSizeCache = new SparseIntArray();
        this.mPreLayoutSpanIndexCache = new SparseIntArray();
        this.mSpanSizeLookup = (SpanSizeLookup)new DefaultSpanSizeLookup();
        this.mDecorInsets = new Rect();
        this.setSpanCount(spanCount);
    }
    
    public GridLayoutManager(final Context context, final int spanCount, final int n, final boolean b) {
        super(context, n, b);
        this.mPendingSpanCountChange = false;
        this.mSpanCount = -1;
        this.mPreLayoutSpanSizeCache = new SparseIntArray();
        this.mPreLayoutSpanIndexCache = new SparseIntArray();
        this.mSpanSizeLookup = (SpanSizeLookup)new DefaultSpanSizeLookup();
        this.mDecorInsets = new Rect();
        this.setSpanCount(spanCount);
    }
    
    public GridLayoutManager(final Context context, final AttributeSet set, final int n, final int n2) {
        super(context, set, n, n2);
        this.mPendingSpanCountChange = false;
        this.mSpanCount = -1;
        this.mPreLayoutSpanSizeCache = new SparseIntArray();
        this.mPreLayoutSpanIndexCache = new SparseIntArray();
        this.mSpanSizeLookup = (SpanSizeLookup)new DefaultSpanSizeLookup();
        this.mDecorInsets = new Rect();
        this.setSpanCount(RecyclerView.LayoutManager.getProperties(context, set, n, n2).spanCount);
    }
    
    private void assignSpans(final Recycler recycler, final State state, int i, int n, final boolean b) {
        int n2 = -1;
        int mSpanIndex = 0;
        if (b) {
            n = 1;
            n2 = i;
            i = 0;
        }
        else {
            --i;
            n = -1;
        }
        while (i != n2) {
            final View view = this.mSet[i];
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            layoutParams.mSpanSize = this.getSpanSize(recycler, state, ((RecyclerView.LayoutManager)this).getPosition(view));
            layoutParams.mSpanIndex = mSpanIndex;
            mSpanIndex += layoutParams.mSpanSize;
            i += n;
        }
    }
    
    private void cachePreLayoutSpanMapping() {
        for (int childCount = ((RecyclerView.LayoutManager)this).getChildCount(), i = 0; i < childCount; ++i) {
            final LayoutParams layoutParams = (LayoutParams)((RecyclerView.LayoutManager)this).getChildAt(i).getLayoutParams();
            final int viewLayoutPosition = ((RecyclerView.LayoutParams)layoutParams).getViewLayoutPosition();
            this.mPreLayoutSpanSizeCache.put(viewLayoutPosition, layoutParams.getSpanSize());
            this.mPreLayoutSpanIndexCache.put(viewLayoutPosition, layoutParams.getSpanIndex());
        }
    }
    
    private void calculateItemBorders(final int n) {
        this.mCachedBorders = calculateItemBorders(this.mCachedBorders, this.mSpanCount, n);
    }
    
    static int[] calculateItemBorders(final int[] array, final int n, int n2) {
        int i = 1;
        int[] array2 = null;
        Label_0035: {
            if (array != null && array.length == n + 1) {
                array2 = array;
                if (array[array.length - 1] == n2) {
                    break Label_0035;
                }
            }
            array2 = new int[n + 1];
        }
        final int n3 = 0;
        array2[0] = 0;
        final int n4 = n2 / n;
        final int n5 = n2 % n;
        int n6 = 0;
        n2 = n3;
        while (i <= n) {
            n2 += n5;
            int n7;
            if (n2 > 0 && n - n2 < n5) {
                n7 = n4 + 1;
                n2 -= n;
            }
            else {
                n7 = n4;
            }
            n6 += n7;
            array2[i] = n6;
            ++i;
        }
        return array2;
    }
    
    private void clearPreLayoutSpanMappingCache() {
        this.mPreLayoutSpanSizeCache.clear();
        this.mPreLayoutSpanIndexCache.clear();
    }
    
    private void ensureAnchorIsInCorrectSpan(final Recycler recycler, final State state, final AnchorInfo anchorInfo, int n) {
        final boolean b = n == 1;
        n = this.getSpanIndex(recycler, state, anchorInfo.mPosition);
        if (b) {
            while (n > 0 && anchorInfo.mPosition > 0) {
                --anchorInfo.mPosition;
                n = this.getSpanIndex(recycler, state, anchorInfo.mPosition);
            }
        }
        else {
            int itemCount;
            int i;
            int n2;
            int spanIndex;
            for (itemCount = state.getItemCount(), i = anchorInfo.mPosition; i < itemCount - 1; i = n2, n = spanIndex) {
                n2 = i + 1;
                spanIndex = this.getSpanIndex(recycler, state, n2);
                if (spanIndex <= n) {
                    break;
                }
            }
            anchorInfo.mPosition = i;
        }
    }
    
    private void ensureViewSet() {
        if (this.mSet == null || this.mSet.length != this.mSpanCount) {
            this.mSet = new View[this.mSpanCount];
        }
    }
    
    private int getSpanGroupIndex(final Recycler recycler, final State state, final int i) {
        if (!state.isPreLayout()) {
            return this.mSpanSizeLookup.getSpanGroupIndex(i, this.mSpanCount);
        }
        final int convertPreLayoutPositionToPostLayout = recycler.convertPreLayoutPositionToPostLayout(i);
        if (convertPreLayoutPositionToPostLayout == -1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot find span size for pre layout position. ");
            sb.append(i);
            Log.w("GridLayoutManager", sb.toString());
            return 0;
        }
        return this.mSpanSizeLookup.getSpanGroupIndex(convertPreLayoutPositionToPostLayout, this.mSpanCount);
    }
    
    private int getSpanIndex(final Recycler recycler, final State state, final int i) {
        if (!state.isPreLayout()) {
            return this.mSpanSizeLookup.getCachedSpanIndex(i, this.mSpanCount);
        }
        final int value = this.mPreLayoutSpanIndexCache.get(i, -1);
        if (value != -1) {
            return value;
        }
        final int convertPreLayoutPositionToPostLayout = recycler.convertPreLayoutPositionToPostLayout(i);
        if (convertPreLayoutPositionToPostLayout == -1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:");
            sb.append(i);
            Log.w("GridLayoutManager", sb.toString());
            return 0;
        }
        return this.mSpanSizeLookup.getCachedSpanIndex(convertPreLayoutPositionToPostLayout, this.mSpanCount);
    }
    
    private int getSpanSize(final Recycler recycler, final State state, final int i) {
        if (!state.isPreLayout()) {
            return this.mSpanSizeLookup.getSpanSize(i);
        }
        final int value = this.mPreLayoutSpanSizeCache.get(i, -1);
        if (value != -1) {
            return value;
        }
        final int convertPreLayoutPositionToPostLayout = recycler.convertPreLayoutPositionToPostLayout(i);
        if (convertPreLayoutPositionToPostLayout == -1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:");
            sb.append(i);
            Log.w("GridLayoutManager", sb.toString());
            return 1;
        }
        return this.mSpanSizeLookup.getSpanSize(convertPreLayoutPositionToPostLayout);
    }
    
    private void guessMeasurement(final float n, final int b) {
        this.calculateItemBorders(Math.max(Math.round(n * this.mSpanCount), b));
    }
    
    private void measureChild(final View view, int n, final boolean b) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        final Rect mDecorInsets = layoutParams.mDecorInsets;
        final int n2 = mDecorInsets.top + mDecorInsets.bottom + layoutParams.topMargin + layoutParams.bottomMargin;
        final int n3 = mDecorInsets.left + mDecorInsets.right + layoutParams.leftMargin + layoutParams.rightMargin;
        final int spaceForSpanRange = this.getSpaceForSpanRange(layoutParams.mSpanIndex, layoutParams.mSpanSize);
        int n4;
        if (this.mOrientation == 1) {
            n4 = RecyclerView.LayoutManager.getChildMeasureSpec(spaceForSpanRange, n, n3, layoutParams.width, false);
            n = RecyclerView.LayoutManager.getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), ((RecyclerView.LayoutManager)this).getHeightMode(), n2, layoutParams.height, true);
        }
        else {
            n = RecyclerView.LayoutManager.getChildMeasureSpec(spaceForSpanRange, n, n2, layoutParams.height, false);
            n4 = RecyclerView.LayoutManager.getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), ((RecyclerView.LayoutManager)this).getWidthMode(), n3, layoutParams.width, true);
        }
        this.measureChildWithDecorationsAndMargin(view, n4, n, b);
    }
    
    private void measureChildWithDecorationsAndMargin(final View view, final int n, final int n2, final boolean b) {
        final RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)view.getLayoutParams();
        boolean b2;
        if (b) {
            b2 = ((RecyclerView.LayoutManager)this).shouldReMeasureChild(view, n, n2, layoutParams);
        }
        else {
            b2 = ((RecyclerView.LayoutManager)this).shouldMeasureChild(view, n, n2, layoutParams);
        }
        if (b2) {
            view.measure(n, n2);
        }
    }
    
    private void updateMeasurements() {
        int n;
        if (this.getOrientation() == 1) {
            n = ((RecyclerView.LayoutManager)this).getWidth() - ((RecyclerView.LayoutManager)this).getPaddingRight() - ((RecyclerView.LayoutManager)this).getPaddingLeft();
        }
        else {
            n = ((RecyclerView.LayoutManager)this).getHeight() - ((RecyclerView.LayoutManager)this).getPaddingBottom() - ((RecyclerView.LayoutManager)this).getPaddingTop();
        }
        this.calculateItemBorders(n);
    }
    
    @Override
    public boolean checkLayoutParams(final RecyclerView.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }
    
    @Override
    void collectPrefetchPositionsForLayoutState(final State state, final LayoutState layoutState, final LayoutPrefetchRegistry layoutPrefetchRegistry) {
        int mCurrentPosition;
        for (int mSpanCount = this.mSpanCount, n = 0; n < this.mSpanCount && layoutState.hasMore(state) && mSpanCount > 0; mSpanCount -= this.mSpanSizeLookup.getSpanSize(mCurrentPosition), layoutState.mCurrentPosition += layoutState.mItemDirection, ++n) {
            mCurrentPosition = layoutState.mCurrentPosition;
            layoutPrefetchRegistry.addPosition(mCurrentPosition, Math.max(0, layoutState.mScrollingOffset));
        }
    }
    
    @Override
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
                    if (this.getSpanIndex(recycler, state, position) != 0) {
                        view3 = view;
                        view4 = view2;
                    }
                    else if (((RecyclerView.LayoutParams)child.getLayoutParams()).isItemRemoved()) {
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
        if (state.getItemCount() < 1) {
            return 0;
        }
        return this.getSpanGroupIndex(recycler, state, state.getItemCount() - 1) + 1;
    }
    
    @Override
    public int getRowCountForAccessibility(final Recycler recycler, final State state) {
        if (this.mOrientation == 0) {
            return this.mSpanCount;
        }
        if (state.getItemCount() < 1) {
            return 0;
        }
        return this.getSpanGroupIndex(recycler, state, state.getItemCount() - 1) + 1;
    }
    
    int getSpaceForSpanRange(final int n, final int n2) {
        if (this.mOrientation == 1 && this.isLayoutRTL()) {
            return this.mCachedBorders[this.mSpanCount - n] - this.mCachedBorders[this.mSpanCount - n - n2];
        }
        return this.mCachedBorders[n2 + n] - this.mCachedBorders[n];
    }
    
    public int getSpanCount() {
        return this.mSpanCount;
    }
    
    public SpanSizeLookup getSpanSizeLookup() {
        return this.mSpanSizeLookup;
    }
    
    @Override
    void layoutChunk(final Recycler recycler, final State state, final LayoutState layoutState, final LayoutChunkResult layoutChunkResult) {
        final int modeInOther = this.mOrientationHelper.getModeInOther();
        final boolean b = modeInOther != 1073741824;
        int n;
        if (((RecyclerView.LayoutManager)this).getChildCount() > 0) {
            n = this.mCachedBorders[this.mSpanCount];
        }
        else {
            n = 0;
        }
        if (b) {
            this.updateMeasurements();
        }
        final boolean b2 = layoutState.mItemDirection == 1;
        int mSpanCount = this.mSpanCount;
        if (!b2) {
            mSpanCount = this.getSpanIndex(recycler, state, layoutState.mCurrentPosition) + this.getSpanSize(recycler, state, layoutState.mCurrentPosition);
        }
        int n2 = 0;
        int n3;
        for (n3 = 0; n3 < this.mSpanCount && layoutState.hasMore(state) && mSpanCount > 0; ++n3) {
            final int mCurrentPosition = layoutState.mCurrentPosition;
            final int spanSize = this.getSpanSize(recycler, state, mCurrentPosition);
            if (spanSize > this.mSpanCount) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Item at position ");
                sb.append(mCurrentPosition);
                sb.append(" requires ");
                sb.append(spanSize);
                sb.append(" spans but GridLayoutManager has only ");
                sb.append(this.mSpanCount);
                sb.append(" spans.");
                throw new IllegalArgumentException(sb.toString());
            }
            mSpanCount -= spanSize;
            if (mSpanCount < 0) {
                break;
            }
            final View next = layoutState.next(recycler);
            if (next == null) {
                break;
            }
            n2 += spanSize;
            this.mSet[n3] = next;
        }
        if (n3 == 0) {
            layoutChunkResult.mFinished = true;
            return;
        }
        float n4 = 0.0f;
        this.assignSpans(recycler, state, n3, n2, b2);
        int i = 0;
        int n5 = 0;
        while (i < n3) {
            final View view = this.mSet[i];
            if (layoutState.mScrapList == null) {
                if (b2) {
                    ((RecyclerView.LayoutManager)this).addView(view);
                }
                else {
                    ((RecyclerView.LayoutManager)this).addView(view, 0);
                }
            }
            else if (b2) {
                ((RecyclerView.LayoutManager)this).addDisappearingView(view);
            }
            else {
                ((RecyclerView.LayoutManager)this).addDisappearingView(view, 0);
            }
            ((RecyclerView.LayoutManager)this).calculateItemDecorationsForChild(view, this.mDecorInsets);
            this.measureChild(view, modeInOther, false);
            final int decoratedMeasurement = this.mOrientationHelper.getDecoratedMeasurement(view);
            int n6 = n5;
            if (decoratedMeasurement > n5) {
                n6 = decoratedMeasurement;
            }
            final float n7 = 1.0f * this.mOrientationHelper.getDecoratedMeasurementInOther(view) / ((LayoutParams)view.getLayoutParams()).mSpanSize;
            float n8 = n4;
            if (n7 > n4) {
                n8 = n7;
            }
            ++i;
            n5 = n6;
            n4 = n8;
        }
        int mConsumed = n5;
        if (b) {
            this.guessMeasurement(n4, n);
            int n9 = 0;
            int n10 = 0;
            while (true) {
                mConsumed = n10;
                if (n9 >= n3) {
                    break;
                }
                final View view2 = this.mSet[n9];
                this.measureChild(view2, 1073741824, true);
                final int decoratedMeasurement2 = this.mOrientationHelper.getDecoratedMeasurement(view2);
                int n11;
                if (decoratedMeasurement2 > (n11 = n10)) {
                    n11 = decoratedMeasurement2;
                }
                ++n9;
                n10 = n11;
            }
        }
        for (int j = 0; j < n3; ++j) {
            final View view3 = this.mSet[j];
            if (this.mOrientationHelper.getDecoratedMeasurement(view3) != mConsumed) {
                final LayoutParams layoutParams = (LayoutParams)view3.getLayoutParams();
                final Rect mDecorInsets = layoutParams.mDecorInsets;
                final int n12 = mDecorInsets.top + mDecorInsets.bottom + layoutParams.topMargin + layoutParams.bottomMargin;
                final int n13 = mDecorInsets.left + mDecorInsets.right + layoutParams.leftMargin + layoutParams.rightMargin;
                final int spaceForSpanRange = this.getSpaceForSpanRange(layoutParams.mSpanIndex, layoutParams.mSpanSize);
                int n14;
                int n15;
                if (this.mOrientation == 1) {
                    n14 = RecyclerView.LayoutManager.getChildMeasureSpec(spaceForSpanRange, 1073741824, n13, layoutParams.width, false);
                    n15 = View$MeasureSpec.makeMeasureSpec(mConsumed - n12, 1073741824);
                }
                else {
                    n14 = View$MeasureSpec.makeMeasureSpec(mConsumed - n13, 1073741824);
                    n15 = RecyclerView.LayoutManager.getChildMeasureSpec(spaceForSpanRange, 1073741824, n12, layoutParams.height, false);
                }
                this.measureChildWithDecorationsAndMargin(view3, n14, n15, true);
            }
        }
        int k = 0;
        layoutChunkResult.mConsumed = mConsumed;
        int n17;
        int mOffset2;
        int mOffset3;
        int n18;
        if (this.mOrientation == 1) {
            if (layoutState.mLayoutDirection == -1) {
                final int mOffset;
                final int n16 = (mOffset = layoutState.mOffset) - mConsumed;
                n17 = mOffset;
                mOffset2 = n16;
            }
            else {
                n17 = mConsumed + (mOffset2 = layoutState.mOffset);
            }
            mOffset3 = 0;
            n18 = 0;
        }
        else if (layoutState.mLayoutDirection == -1) {
            final int mOffset4 = layoutState.mOffset;
            mOffset2 = 0;
            final int n19 = 0;
            n18 = mOffset4;
            mOffset3 = mOffset4 - mConsumed;
            n17 = n19;
        }
        else {
            mOffset3 = layoutState.mOffset;
            n18 = mConsumed + mOffset3;
            mOffset2 = 0;
            n17 = 0;
        }
        while (k < n3) {
            final View view4 = this.mSet[k];
            final LayoutParams layoutParams2 = (LayoutParams)view4.getLayoutParams();
            int n21 = 0;
            int n22 = 0;
            Label_1059: {
                if (this.mOrientation == 1) {
                    if (this.isLayoutRTL()) {
                        final int n20 = ((RecyclerView.LayoutManager)this).getPaddingLeft() + this.mCachedBorders[this.mSpanCount - layoutParams2.mSpanIndex];
                        final int decoratedMeasurementInOther = this.mOrientationHelper.getDecoratedMeasurementInOther(view4);
                        n21 = n20;
                        n22 = n20 - decoratedMeasurementInOther;
                        break Label_1059;
                    }
                    mOffset3 = ((RecyclerView.LayoutManager)this).getPaddingLeft() + this.mCachedBorders[layoutParams2.mSpanIndex];
                    n18 = this.mOrientationHelper.getDecoratedMeasurementInOther(view4) + mOffset3;
                }
                else {
                    mOffset2 = ((RecyclerView.LayoutManager)this).getPaddingTop() + this.mCachedBorders[layoutParams2.mSpanIndex];
                    n17 = this.mOrientationHelper.getDecoratedMeasurementInOther(view4) + mOffset2;
                }
                final int n23 = mOffset3;
                n21 = n18;
                n22 = n23;
            }
            ((RecyclerView.LayoutManager)this).layoutDecoratedWithMargins(view4, n22, mOffset2, n21, n17);
            if (((RecyclerView.LayoutParams)layoutParams2).isItemRemoved() || ((RecyclerView.LayoutParams)layoutParams2).isItemChanged()) {
                layoutChunkResult.mIgnoreConsumed = true;
            }
            layoutChunkResult.mFocusable |= view4.hasFocusable();
            final int n24 = k + 1;
            final int n25 = n21;
            mOffset3 = n22;
            n18 = n25;
            k = n24;
        }
        Arrays.fill(this.mSet, null);
    }
    
    @Override
    void onAnchorReady(final Recycler recycler, final State state, final AnchorInfo anchorInfo, final int n) {
        super.onAnchorReady(recycler, state, anchorInfo, n);
        this.updateMeasurements();
        if (state.getItemCount() > 0 && !state.isPreLayout()) {
            this.ensureAnchorIsInCorrectSpan(recycler, state, anchorInfo, n);
        }
        this.ensureViewSet();
    }
    
    @Override
    public View onFocusSearchFailed(View view, int n, final Recycler recycler, final State state) {
        final View containingItemView = ((RecyclerView.LayoutManager)this).findContainingItemView(view);
        View view2 = null;
        if (containingItemView == null) {
            return null;
        }
        final LayoutParams layoutParams = (LayoutParams)containingItemView.getLayoutParams();
        final int mSpanIndex = layoutParams.mSpanIndex;
        final int b = layoutParams.mSpanIndex + layoutParams.mSpanSize;
        if (super.onFocusSearchFailed(view, n, recycler, state) == null) {
            return null;
        }
        if (this.convertFocusDirectionToLayoutDirection(n) == 1 != this.mShouldReverseLayout) {
            n = 1;
        }
        else {
            n = 0;
        }
        int i;
        int n2;
        if (n != 0) {
            i = ((RecyclerView.LayoutManager)this).getChildCount() - 1;
            n = -1;
            n2 = -1;
        }
        else {
            n = ((RecyclerView.LayoutManager)this).getChildCount();
            n2 = 1;
            i = 0;
        }
        final boolean b2 = this.mOrientation == 1 && this.isLayoutRTL();
        final int spanGroupIndex = this.getSpanGroupIndex(recycler, state, i);
        final int n3 = -1;
        int mSpanIndex2 = -1;
        int n4 = 0;
        int n5 = 0;
        view = null;
        final int n6 = n;
        n = n3;
        while (i != n6) {
            final int spanGroupIndex2 = this.getSpanGroupIndex(recycler, state, i);
            final View child = ((RecyclerView.LayoutManager)this).getChildAt(i);
            if (child == containingItemView) {
                break;
            }
            if (child.hasFocusable() && spanGroupIndex2 != spanGroupIndex) {
                if (view2 != null) {
                    break;
                }
            }
            else {
                final LayoutParams layoutParams2 = (LayoutParams)child.getLayoutParams();
                final int mSpanIndex3 = layoutParams2.mSpanIndex;
                final int a = layoutParams2.mSpanIndex + layoutParams2.mSpanSize;
                if (child.hasFocusable() && mSpanIndex3 == mSpanIndex && a == b) {
                    return child;
                }
                boolean b4 = false;
                Label_0459: {
                    Label_0323: {
                        if ((child.hasFocusable() || view2 != null) && (child.hasFocusable() || view != null)) {
                            final int n7 = Math.min(a, b) - Math.max(mSpanIndex3, mSpanIndex);
                            if (child.hasFocusable()) {
                                if (n7 > n4) {
                                    break Label_0323;
                                }
                                if (n7 == n4 && b2 == mSpanIndex3 > n) {
                                    break Label_0323;
                                }
                            }
                            else if (view2 == null) {
                                boolean b3 = false;
                                if (((RecyclerView.LayoutManager)this).isViewPartiallyVisible(child, false, true)) {
                                    if (n7 > n5) {
                                        break Label_0323;
                                    }
                                    if (n7 == n5) {
                                        if (mSpanIndex3 > mSpanIndex2) {
                                            b3 = true;
                                        }
                                        if (b2 == b3) {
                                            break Label_0323;
                                        }
                                    }
                                }
                            }
                            b4 = false;
                            break Label_0459;
                        }
                    }
                    b4 = true;
                }
                if (b4) {
                    if (child.hasFocusable()) {
                        n = layoutParams2.mSpanIndex;
                        n4 = Math.min(a, b) - Math.max(mSpanIndex3, mSpanIndex);
                        view2 = child;
                    }
                    else {
                        mSpanIndex2 = layoutParams2.mSpanIndex;
                        final int min = Math.min(a, b);
                        final int max = Math.max(mSpanIndex3, mSpanIndex);
                        view = child;
                        n5 = min - max;
                    }
                }
            }
            i += n2;
        }
        if (view2 != null) {
            view = view2;
        }
        return view;
    }
    
    @Override
    public void onInitializeAccessibilityNodeInfoForItem(final Recycler recycler, final State state, final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        final ViewGroup$LayoutParams layoutParams = view.getLayoutParams();
        if (!(layoutParams instanceof LayoutParams)) {
            super.onInitializeAccessibilityNodeInfoForItem(view, accessibilityNodeInfoCompat);
            return;
        }
        final LayoutParams layoutParams2 = (LayoutParams)layoutParams;
        final int spanGroupIndex = this.getSpanGroupIndex(recycler, state, ((RecyclerView.LayoutParams)layoutParams2).getViewLayoutPosition());
        if (this.mOrientation == 0) {
            accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(layoutParams2.getSpanIndex(), layoutParams2.getSpanSize(), spanGroupIndex, 1, this.mSpanCount > 1 && layoutParams2.getSpanSize() == this.mSpanCount, false));
        }
        else {
            accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(spanGroupIndex, 1, layoutParams2.getSpanIndex(), layoutParams2.getSpanSize(), this.mSpanCount > 1 && layoutParams2.getSpanSize() == this.mSpanCount, false));
        }
    }
    
    @Override
    public void onItemsAdded(final RecyclerView recyclerView, final int n, final int n2) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }
    
    @Override
    public void onItemsChanged(final RecyclerView recyclerView) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }
    
    @Override
    public void onItemsMoved(final RecyclerView recyclerView, final int n, final int n2, final int n3) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }
    
    @Override
    public void onItemsRemoved(final RecyclerView recyclerView, final int n, final int n2) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }
    
    @Override
    public void onItemsUpdated(final RecyclerView recyclerView, final int n, final int n2, final Object o) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }
    
    @Override
    public void onLayoutChildren(final Recycler recycler, final State state) {
        if (state.isPreLayout()) {
            this.cachePreLayoutSpanMapping();
        }
        super.onLayoutChildren(recycler, state);
        this.clearPreLayoutSpanMappingCache();
    }
    
    @Override
    public void onLayoutCompleted(final State state) {
        super.onLayoutCompleted(state);
        this.mPendingSpanCountChange = false;
    }
    
    @Override
    public int scrollHorizontallyBy(final int n, final Recycler recycler, final State state) {
        this.updateMeasurements();
        this.ensureViewSet();
        return super.scrollHorizontallyBy(n, recycler, state);
    }
    
    @Override
    public int scrollVerticallyBy(final int n, final Recycler recycler, final State state) {
        this.updateMeasurements();
        this.ensureViewSet();
        return super.scrollVerticallyBy(n, recycler, state);
    }
    
    @Override
    public void setMeasuredDimension(final Rect rect, int n, int n2) {
        if (this.mCachedBorders == null) {
            super.setMeasuredDimension(rect, n, n2);
        }
        final int n3 = ((RecyclerView.LayoutManager)this).getPaddingLeft() + ((RecyclerView.LayoutManager)this).getPaddingRight();
        final int n4 = ((RecyclerView.LayoutManager)this).getPaddingTop() + ((RecyclerView.LayoutManager)this).getPaddingBottom();
        if (this.mOrientation == 1) {
            n2 = RecyclerView.LayoutManager.chooseSize(n2, rect.height() + n4, ((RecyclerView.LayoutManager)this).getMinimumHeight());
            n = RecyclerView.LayoutManager.chooseSize(n, this.mCachedBorders[this.mCachedBorders.length - 1] + n3, ((RecyclerView.LayoutManager)this).getMinimumWidth());
        }
        else {
            n = RecyclerView.LayoutManager.chooseSize(n, rect.width() + n3, ((RecyclerView.LayoutManager)this).getMinimumWidth());
            n2 = RecyclerView.LayoutManager.chooseSize(n2, this.mCachedBorders[this.mCachedBorders.length - 1] + n4, ((RecyclerView.LayoutManager)this).getMinimumHeight());
        }
        ((RecyclerView.LayoutManager)this).setMeasuredDimension(n, n2);
    }
    
    public void setSpanCount(final int n) {
        if (n == this.mSpanCount) {
            return;
        }
        this.mPendingSpanCountChange = true;
        if (n < 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Span count should be at least 1. Provided ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
        this.mSpanCount = n;
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        ((RecyclerView.LayoutManager)this).requestLayout();
    }
    
    public void setSpanSizeLookup(final SpanSizeLookup mSpanSizeLookup) {
        this.mSpanSizeLookup = mSpanSizeLookup;
    }
    
    @Override
    public void setStackFromEnd(final boolean b) {
        if (b) {
            throw new UnsupportedOperationException("GridLayoutManager does not support stack from end. Consider using reverse layout");
        }
        super.setStackFromEnd(false);
    }
    
    @Override
    public boolean supportsPredictiveItemAnimations() {
        return this.mPendingSavedState == null && !this.mPendingSpanCountChange;
    }
    
    public static final class DefaultSpanSizeLookup extends SpanSizeLookup
    {
        @Override
        public int getSpanIndex(final int n, final int n2) {
            return n % n2;
        }
        
        @Override
        public int getSpanSize(final int n) {
            return 1;
        }
    }
    
    public static class LayoutParams extends RecyclerView.LayoutParams
    {
        public static final int INVALID_SPAN_ID = -1;
        int mSpanIndex;
        int mSpanSize;
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.mSpanIndex = -1;
            this.mSpanSize = 0;
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
            this.mSpanIndex = -1;
            this.mSpanSize = 0;
        }
        
        public LayoutParams(final RecyclerView.LayoutParams layoutParams) {
            super(layoutParams);
            this.mSpanIndex = -1;
            this.mSpanSize = 0;
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
            this.mSpanIndex = -1;
            this.mSpanSize = 0;
        }
        
        public LayoutParams(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
            super(viewGroup$MarginLayoutParams);
            this.mSpanIndex = -1;
            this.mSpanSize = 0;
        }
        
        public int getSpanIndex() {
            return this.mSpanIndex;
        }
        
        public int getSpanSize() {
            return this.mSpanSize;
        }
    }
    
    public abstract static class SpanSizeLookup
    {
        private boolean mCacheSpanIndices;
        final SparseIntArray mSpanIndexCache;
        
        public SpanSizeLookup() {
            this.mSpanIndexCache = new SparseIntArray();
            this.mCacheSpanIndices = false;
        }
        
        int findReferenceIndexFromCache(int n) {
            int n2 = this.mSpanIndexCache.size() - 1;
            int i = 0;
            while (i <= n2) {
                final int n3 = i + n2 >>> 1;
                if (this.mSpanIndexCache.keyAt(n3) < n) {
                    i = n3 + 1;
                }
                else {
                    n2 = n3 - 1;
                }
            }
            n = i - 1;
            if (n >= 0 && n < this.mSpanIndexCache.size()) {
                return this.mSpanIndexCache.keyAt(n);
            }
            return -1;
        }
        
        int getCachedSpanIndex(final int n, int spanIndex) {
            if (!this.mCacheSpanIndices) {
                return this.getSpanIndex(n, spanIndex);
            }
            final int value = this.mSpanIndexCache.get(n, -1);
            if (value != -1) {
                return value;
            }
            spanIndex = this.getSpanIndex(n, spanIndex);
            this.mSpanIndexCache.put(n, spanIndex);
            return spanIndex;
        }
        
        public int getSpanGroupIndex(int n, final int n2) {
            final int spanSize = this.getSpanSize(n);
            int i = 0;
            int n4;
            int n3 = n4 = 0;
            while (i < n) {
                final int spanSize2 = this.getSpanSize(i);
                final int n5 = n3 + spanSize2;
                int n6;
                if (n5 == n2) {
                    n6 = n4 + 1;
                    n3 = 0;
                }
                else {
                    n3 = n5;
                    n6 = n4;
                    if (n5 > n2) {
                        n6 = n4 + 1;
                        n3 = spanSize2;
                    }
                }
                ++i;
                n4 = n6;
            }
            n = n4;
            if (n3 + spanSize > n2) {
                n = n4 + 1;
            }
            return n;
        }
        
        public int getSpanIndex(final int n, final int n2) {
            final int spanSize = this.getSpanSize(n);
            if (spanSize == n2) {
                return 0;
            }
            while (true) {
                Label_0067: {
                    if (!this.mCacheSpanIndices || this.mSpanIndexCache.size() <= 0) {
                        break Label_0067;
                    }
                    int i = this.findReferenceIndexFromCache(n);
                    if (i < 0) {
                        break Label_0067;
                    }
                    int n3 = this.mSpanIndexCache.get(i) + this.getSpanSize(i);
                    ++i;
                    while (i < n) {
                        final int spanSize2 = this.getSpanSize(i);
                        final int n4 = n3 + spanSize2;
                        if (n4 == n2) {
                            n3 = 0;
                        }
                        else if ((n3 = n4) > n2) {
                            n3 = spanSize2;
                        }
                        ++i;
                    }
                    if (spanSize + n3 <= n2) {
                        return n3;
                    }
                    return 0;
                }
                int i = 0;
                int n3 = 0;
                continue;
            }
        }
        
        public abstract int getSpanSize(final int p0);
        
        public void invalidateSpanIndexCache() {
            this.mSpanIndexCache.clear();
        }
        
        public boolean isSpanIndexCacheEnabled() {
            return this.mCacheSpanIndices;
        }
        
        public void setSpanIndexCacheEnabled(final boolean mCacheSpanIndices) {
            this.mCacheSpanIndices = mCacheSpanIndices;
        }
    }
}
