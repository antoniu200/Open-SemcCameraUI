// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.widget;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import android.support.v4.os.TraceCompat;
import android.support.annotation.Nullable;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Comparator;

final class GapWorker implements Runnable
{
    static final ThreadLocal<GapWorker> sGapWorker;
    static Comparator<Task> sTaskComparator;
    long mFrameIntervalNs;
    long mPostTimeNs;
    ArrayList<RecyclerView> mRecyclerViews;
    private ArrayList<Task> mTasks;
    
    static {
        sGapWorker = new ThreadLocal<GapWorker>();
        GapWorker.sTaskComparator = new Comparator<Task>() {
            @Override
            public int compare(final Task task, final Task task2) {
                final RecyclerView view = task.view;
                final int n = 1;
                final int n2 = 1;
                if (view == null != (task2.view == null)) {
                    int n3;
                    if (task.view == null) {
                        n3 = n2;
                    }
                    else {
                        n3 = -1;
                    }
                    return n3;
                }
                if (task.immediate != task2.immediate) {
                    int n4 = n;
                    if (task.immediate) {
                        n4 = -1;
                    }
                    return n4;
                }
                final int n5 = task2.viewVelocity - task.viewVelocity;
                if (n5 != 0) {
                    return n5;
                }
                final int n6 = task.distanceToItem - task2.distanceToItem;
                if (n6 != 0) {
                    return n6;
                }
                return 0;
            }
        };
    }
    
    GapWorker() {
        this.mRecyclerViews = new ArrayList<RecyclerView>();
        this.mTasks = new ArrayList<Task>();
    }
    
    private void buildTaskList() {
        final int size = this.mRecyclerViews.size();
        int i = 0;
        int minCapacity = 0;
        while (i < size) {
            final RecyclerView recyclerView = this.mRecyclerViews.get(i);
            int n = minCapacity;
            if (recyclerView.getWindowVisibility() == 0) {
                recyclerView.mPrefetchRegistry.collectPrefetchPositionsFromView(recyclerView, false);
                n = minCapacity + recyclerView.mPrefetchRegistry.mCount;
            }
            ++i;
            minCapacity = n;
        }
        this.mTasks.ensureCapacity(minCapacity);
        int j = 0;
        int index = 0;
        while (j < size) {
            final RecyclerView view = this.mRecyclerViews.get(j);
            if (view.getWindowVisibility() == 0) {
                final LayoutPrefetchRegistryImpl mPrefetchRegistry = view.mPrefetchRegistry;
                final int viewVelocity = Math.abs(mPrefetchRegistry.mPrefetchDx) + Math.abs(mPrefetchRegistry.mPrefetchDy);
                for (int k = 0; k < mPrefetchRegistry.mCount * 2; k += 2) {
                    Task e;
                    if (index >= this.mTasks.size()) {
                        e = new Task();
                        this.mTasks.add(e);
                    }
                    else {
                        e = this.mTasks.get(index);
                    }
                    final int distanceToItem = mPrefetchRegistry.mPrefetchArray[k + 1];
                    e.immediate = (distanceToItem <= viewVelocity);
                    e.viewVelocity = viewVelocity;
                    e.distanceToItem = distanceToItem;
                    e.view = view;
                    e.position = mPrefetchRegistry.mPrefetchArray[k];
                    ++index;
                }
            }
            ++j;
        }
        Collections.sort(this.mTasks, GapWorker.sTaskComparator);
    }
    
    private void flushTaskWithDeadline(final Task task, final long n) {
        long n2;
        if (task.immediate) {
            n2 = Long.MAX_VALUE;
        }
        else {
            n2 = n;
        }
        final RecyclerView.ViewHolder prefetchPositionWithDeadline = this.prefetchPositionWithDeadline(task.view, task.position, n2);
        if (prefetchPositionWithDeadline != null && prefetchPositionWithDeadline.mNestedRecyclerView != null && prefetchPositionWithDeadline.isBound() && !prefetchPositionWithDeadline.isInvalid()) {
            this.prefetchInnerRecyclerViewWithDeadline((RecyclerView)prefetchPositionWithDeadline.mNestedRecyclerView.get(), n);
        }
    }
    
    private void flushTasksWithDeadline(final long n) {
        for (int i = 0; i < this.mTasks.size(); ++i) {
            final Task task = this.mTasks.get(i);
            if (task.view == null) {
                break;
            }
            this.flushTaskWithDeadline(task, n);
            task.clear();
        }
    }
    
    static boolean isPrefetchPositionAttached(final RecyclerView recyclerView, final int n) {
        for (int unfilteredChildCount = recyclerView.mChildHelper.getUnfilteredChildCount(), i = 0; i < unfilteredChildCount; ++i) {
            final RecyclerView.ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(recyclerView.mChildHelper.getUnfilteredChildAt(i));
            if (childViewHolderInt.mPosition == n && !childViewHolderInt.isInvalid()) {
                return true;
            }
        }
        return false;
    }
    
    private void prefetchInnerRecyclerViewWithDeadline(@Nullable final RecyclerView recyclerView, final long n) {
        if (recyclerView == null) {
            return;
        }
        if (recyclerView.mDataSetHasChangedAfterLayout && recyclerView.mChildHelper.getUnfilteredChildCount() != 0) {
            recyclerView.removeAndRecycleViews();
        }
        final LayoutPrefetchRegistryImpl mPrefetchRegistry = recyclerView.mPrefetchRegistry;
        mPrefetchRegistry.collectPrefetchPositionsFromView(recyclerView, true);
        if (mPrefetchRegistry.mCount != 0) {
            try {
                TraceCompat.beginSection("RV Nested Prefetch");
                recyclerView.mState.prepareForNestedPrefetch(recyclerView.mAdapter);
                for (int i = 0; i < mPrefetchRegistry.mCount * 2; i += 2) {
                    this.prefetchPositionWithDeadline(recyclerView, mPrefetchRegistry.mPrefetchArray[i], n);
                }
            }
            finally {
                TraceCompat.endSection();
            }
        }
    }
    
    private RecyclerView.ViewHolder prefetchPositionWithDeadline(final RecyclerView recyclerView, final int n, final long n2) {
        if (isPrefetchPositionAttached(recyclerView, n)) {
            return null;
        }
        final RecyclerView.Recycler mRecycler = recyclerView.mRecycler;
        try {
            recyclerView.onEnterLayoutOrScroll();
            final RecyclerView.ViewHolder tryGetViewHolderForPositionByDeadline = mRecycler.tryGetViewHolderForPositionByDeadline(n, false, n2);
            if (tryGetViewHolderForPositionByDeadline != null) {
                if (tryGetViewHolderForPositionByDeadline.isBound() && !tryGetViewHolderForPositionByDeadline.isInvalid()) {
                    mRecycler.recycleView(tryGetViewHolderForPositionByDeadline.itemView);
                }
                else {
                    mRecycler.addViewHolderToRecycledViewPool(tryGetViewHolderForPositionByDeadline, false);
                }
            }
            return tryGetViewHolderForPositionByDeadline;
        }
        finally {
            recyclerView.onExitLayoutOrScroll(false);
        }
    }
    
    public void add(final RecyclerView e) {
        this.mRecyclerViews.add(e);
    }
    
    void postFromTraversal(final RecyclerView recyclerView, final int n, final int n2) {
        if (recyclerView.isAttachedToWindow() && this.mPostTimeNs == 0L) {
            this.mPostTimeNs = recyclerView.getNanoTime();
            recyclerView.post((Runnable)this);
        }
        recyclerView.mPrefetchRegistry.setPrefetchVector(n, n2);
    }
    
    void prefetch(final long n) {
        this.buildTaskList();
        this.flushTasksWithDeadline(n);
    }
    
    public void remove(final RecyclerView o) {
        this.mRecyclerViews.remove(o);
    }
    
    @Override
    public void run() {
        try {
            TraceCompat.beginSection("RV Prefetch");
            if (this.mRecyclerViews.isEmpty()) {
                return;
            }
            final int size = this.mRecyclerViews.size();
            int i = 0;
            long n = 0L;
            while (i < size) {
                final RecyclerView recyclerView = this.mRecyclerViews.get(i);
                long max = n;
                if (recyclerView.getWindowVisibility() == 0) {
                    max = Math.max(recyclerView.getDrawingTime(), n);
                }
                ++i;
                n = max;
            }
            if (n == 0L) {
                return;
            }
            this.prefetch(TimeUnit.MILLISECONDS.toNanos(n) + this.mFrameIntervalNs);
        }
        finally {
            this.mPostTimeNs = 0L;
            TraceCompat.endSection();
        }
    }
    
    static class LayoutPrefetchRegistryImpl implements LayoutPrefetchRegistry
    {
        int mCount;
        int[] mPrefetchArray;
        int mPrefetchDx;
        int mPrefetchDy;
        
        @Override
        public void addPosition(final int n, final int n2) {
            if (n < 0) {
                throw new IllegalArgumentException("Layout positions must be non-negative");
            }
            if (n2 < 0) {
                throw new IllegalArgumentException("Pixel distance must be non-negative");
            }
            final int n3 = this.mCount * 2;
            if (this.mPrefetchArray == null) {
                Arrays.fill(this.mPrefetchArray = new int[4], -1);
            }
            else if (n3 >= this.mPrefetchArray.length) {
                final int[] mPrefetchArray = this.mPrefetchArray;
                System.arraycopy(mPrefetchArray, 0, this.mPrefetchArray = new int[n3 * 2], 0, mPrefetchArray.length);
            }
            this.mPrefetchArray[n3] = n;
            this.mPrefetchArray[n3 + 1] = n2;
            ++this.mCount;
        }
        
        void clearPrefetchPositions() {
            if (this.mPrefetchArray != null) {
                Arrays.fill(this.mPrefetchArray, -1);
            }
            this.mCount = 0;
        }
        
        void collectPrefetchPositionsFromView(final RecyclerView recyclerView, final boolean mPrefetchMaxObservedInInitialPrefetch) {
            this.mCount = 0;
            if (this.mPrefetchArray != null) {
                Arrays.fill(this.mPrefetchArray, -1);
            }
            final RecyclerView.LayoutManager mLayout = recyclerView.mLayout;
            if (recyclerView.mAdapter != null && mLayout != null && mLayout.isItemPrefetchEnabled()) {
                if (mPrefetchMaxObservedInInitialPrefetch) {
                    if (!recyclerView.mAdapterHelper.hasPendingUpdates()) {
                        mLayout.collectInitialPrefetchPositions(recyclerView.mAdapter.getItemCount(), (RecyclerView.LayoutManager.LayoutPrefetchRegistry)this);
                    }
                }
                else if (!recyclerView.hasPendingAdapterUpdates()) {
                    mLayout.collectAdjacentPrefetchPositions(this.mPrefetchDx, this.mPrefetchDy, recyclerView.mState, (RecyclerView.LayoutManager.LayoutPrefetchRegistry)this);
                }
                if (this.mCount > mLayout.mPrefetchMaxCountObserved) {
                    mLayout.mPrefetchMaxCountObserved = this.mCount;
                    mLayout.mPrefetchMaxObservedInInitialPrefetch = mPrefetchMaxObservedInInitialPrefetch;
                    recyclerView.mRecycler.updateViewCacheSize();
                }
            }
        }
        
        boolean lastPrefetchIncludedPosition(final int n) {
            if (this.mPrefetchArray != null) {
                for (int mCount = this.mCount, i = 0; i < mCount * 2; i += 2) {
                    if (this.mPrefetchArray[i] == n) {
                        return true;
                    }
                }
            }
            return false;
        }
        
        void setPrefetchVector(final int mPrefetchDx, final int mPrefetchDy) {
            this.mPrefetchDx = mPrefetchDx;
            this.mPrefetchDy = mPrefetchDy;
        }
    }
    
    static class Task
    {
        public int distanceToItem;
        public boolean immediate;
        public int position;
        public RecyclerView view;
        public int viewVelocity;
        
        public void clear() {
            this.immediate = false;
            this.viewVelocity = 0;
            this.distanceToItem = 0;
            this.view = null;
            this.position = 0;
        }
    }
}
