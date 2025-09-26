// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.widget;

import android.util.DisplayMetrics;
import android.content.Context;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.DecelerateInterpolator;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.widget.Scroller;

public abstract class SnapHelper extends OnFlingListener
{
    static final float MILLISECONDS_PER_INCH = 100.0f;
    private Scroller mGravityScroller;
    RecyclerView mRecyclerView;
    private final OnScrollListener mScrollListener;
    
    public SnapHelper() {
        this.mScrollListener = new OnScrollListener() {
            boolean mScrolled = false;
            final SnapHelper this$0;
            
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int n) {
                super.onScrollStateChanged(recyclerView, n);
                if (n == 0 && this.mScrolled) {
                    this.mScrolled = false;
                    this.this$0.snapToTargetExistingView();
                }
            }
            
            @Override
            public void onScrolled(final RecyclerView recyclerView, final int n, final int n2) {
                if (n != 0 || n2 != 0) {
                    this.mScrolled = true;
                }
            }
        };
    }
    
    private void destroyCallbacks() {
        this.mRecyclerView.removeOnScrollListener(this.mScrollListener);
        this.mRecyclerView.setOnFlingListener(null);
    }
    
    private void setupCallbacks() throws IllegalStateException {
        if (this.mRecyclerView.getOnFlingListener() != null) {
            throw new IllegalStateException("An instance of OnFlingListener already set.");
        }
        this.mRecyclerView.addOnScrollListener(this.mScrollListener);
        this.mRecyclerView.setOnFlingListener((RecyclerView.OnFlingListener)this);
    }
    
    private boolean snapFromFling(@NonNull final LayoutManager layoutManager, int targetSnapPosition, final int n) {
        if (!(layoutManager instanceof ScrollVectorProvider)) {
            return false;
        }
        final SmoothScroller scroller = this.createScroller(layoutManager);
        if (scroller == null) {
            return false;
        }
        targetSnapPosition = this.findTargetSnapPosition(layoutManager, targetSnapPosition, n);
        if (targetSnapPosition == -1) {
            return false;
        }
        scroller.setTargetPosition(targetSnapPosition);
        layoutManager.startSmoothScroll(scroller);
        return true;
    }
    
    public void attachToRecyclerView(@Nullable final RecyclerView mRecyclerView) throws IllegalStateException {
        if (this.mRecyclerView == mRecyclerView) {
            return;
        }
        if (this.mRecyclerView != null) {
            this.destroyCallbacks();
        }
        this.mRecyclerView = mRecyclerView;
        if (this.mRecyclerView != null) {
            this.setupCallbacks();
            this.mGravityScroller = new Scroller(this.mRecyclerView.getContext(), (Interpolator)new DecelerateInterpolator());
            this.snapToTargetExistingView();
        }
    }
    
    @Nullable
    public abstract int[] calculateDistanceToFinalSnap(@NonNull final LayoutManager p0, @NonNull final View p1);
    
    public int[] calculateScrollDistance(final int n, final int n2) {
        this.mGravityScroller.fling(0, 0, n, n2, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return new int[] { this.mGravityScroller.getFinalX(), this.mGravityScroller.getFinalY() };
    }
    
    @Nullable
    protected SmoothScroller createScroller(final LayoutManager layoutManager) {
        return this.createSnapScroller(layoutManager);
    }
    
    @Deprecated
    @Nullable
    protected LinearSmoothScroller createSnapScroller(final LayoutManager layoutManager) {
        if (!(layoutManager instanceof ScrollVectorProvider)) {
            return null;
        }
        return new LinearSmoothScroller(this, this.mRecyclerView.getContext()) {
            final SnapHelper this$0;
            
            @Override
            protected float calculateSpeedPerPixel(final DisplayMetrics displayMetrics) {
                return 100.0f / displayMetrics.densityDpi;
            }
            
            @Override
            protected void onTargetFound(final View view, final State state, final Action action) {
                if (this.this$0.mRecyclerView == null) {
                    return;
                }
                final int[] calculateDistanceToFinalSnap = this.this$0.calculateDistanceToFinalSnap(this.this$0.mRecyclerView.getLayoutManager(), view);
                final int a = calculateDistanceToFinalSnap[0];
                final int a2 = calculateDistanceToFinalSnap[1];
                final int calculateTimeForDeceleration = this.calculateTimeForDeceleration(Math.max(Math.abs(a), Math.abs(a2)));
                if (calculateTimeForDeceleration > 0) {
                    action.update(a, a2, calculateTimeForDeceleration, (Interpolator)this.mDecelerateInterpolator);
                }
            }
        };
    }
    
    @Nullable
    public abstract View findSnapView(final LayoutManager p0);
    
    public abstract int findTargetSnapPosition(final LayoutManager p0, final int p1, final int p2);
    
    @Override
    public boolean onFling(final int a, final int a2) {
        final RecyclerView.LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
        final boolean b = false;
        if (layoutManager == null) {
            return false;
        }
        if (this.mRecyclerView.getAdapter() == null) {
            return false;
        }
        final int minFlingVelocity = this.mRecyclerView.getMinFlingVelocity();
        if (Math.abs(a2) <= minFlingVelocity) {
            final boolean b2 = b;
            if (Math.abs(a) <= minFlingVelocity) {
                return b2;
            }
        }
        boolean b2 = b;
        if (this.snapFromFling(layoutManager, a, a2)) {
            b2 = true;
        }
        return b2;
    }
    
    void snapToTargetExistingView() {
        if (this.mRecyclerView == null) {
            return;
        }
        final RecyclerView.LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
        if (layoutManager == null) {
            return;
        }
        final View snapView = this.findSnapView(layoutManager);
        if (snapView == null) {
            return;
        }
        final int[] calculateDistanceToFinalSnap = this.calculateDistanceToFinalSnap(layoutManager, snapView);
        if (calculateDistanceToFinalSnap[0] != 0 || calculateDistanceToFinalSnap[1] != 0) {
            this.mRecyclerView.smoothScrollBy(calculateDistanceToFinalSnap[0], calculateDistanceToFinalSnap[1]);
        }
    }
}
