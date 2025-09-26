// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.selectabledialog;

import android.animation.TimeInterpolator;
import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.database.DataSetObserver;
import android.widget.FrameLayout$LayoutParams;
import android.view.ViewGroup;
import android.view.MotionEvent;
import com.sonyericsson.android.camera.util.CamLog;
import android.animation.Animator;
import android.animation.Animator$AnimatorListener;
import java.util.Iterator;
import android.view.View;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import android.view.animation.PathInterpolator;
import android.support.annotation.NonNull;
import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.ScrollView;
import android.widget.LinearLayout;
import java.util.List;
import android.widget.FrameLayout;

public abstract class AbsDialogScrollView extends FrameLayout
{
    protected final List<ContentArrange> mArranges;
    protected FrameLayout mBackground;
    protected int mBottomMarginHint;
    protected LinearLayout mContentsBinder;
    private ScrollView mDefaultScroller;
    private final Interpolator mInterporater;
    protected boolean mIsExpandedWhenOpened;
    protected int mOrientation;
    protected AbsSelectableDialog.Params mParams;
    protected ScrollContainer mSomcScroller;
    protected int mTopMarginHint;
    
    public AbsDialogScrollView(@NonNull final Context context) {
        super(context);
        this.mOrientation = 2;
        this.mInterporater = (Interpolator)new PathInterpolator(0.645f, 0.045f, 0.355f, 1.0f);
        this.mArranges = new ArrayList<ContentArrange>();
    }
    
    protected abstract void addContent(final String p0, final ArrayAdapter p1);
    
    protected void addContentView(final View view) {
        this.mContentsBinder.addView(view);
    }
    
    protected int calculateInitialDisplayHeight(int scrollableContentLength) {
        final int dimenToPixel = this.dimenToPixel(scrollableContentLength);
        scrollableContentLength = this.getScrollableContentLength();
        if (dimenToPixel <= scrollableContentLength) {
            scrollableContentLength = dimenToPixel;
        }
        return scrollableContentLength;
    }
    
    protected int dimenToPixel(final int n) {
        return this.getResources().getDimensionPixelSize(n);
    }
    
    protected View findItemViewWithTag(final Object o) {
        final Iterator<ContentArrange> iterator = this.mArranges.iterator();
        while (iterator.hasNext()) {
            final View viewWithTag = iterator.next().findViewWithTag(o);
            if (viewWithTag != null) {
                return viewWithTag;
            }
        }
        return null;
    }
    
    protected AnimationFactory getAnimationFactory() {
        switch (AbsDialogScrollView$3.$SwitchMap$com$sonyericsson$android$camera$view$selectabledialog$AbsSelectableDialog$AnimationType[this.mParams.animationType.ordinal()]) {
            default: {
                return (AnimationFactory)new AnimationFactory(this) {
                    final AbsDialogScrollView this$0;
                    
                    @Override
                    public void close(final int n, final int n2, final Animator$AnimatorListener animator$AnimatorListener) {
                        if (animator$AnimatorListener != null) {
                            animator$AnimatorListener.onAnimationEnd((Animator)null);
                        }
                    }
                    
                    @Override
                    public void open(final int n, final int n2, final Animator$AnimatorListener animator$AnimatorListener) {
                    }
                };
            }
            case 2: {
                return (AnimationFactory)new SliderAnimation();
            }
            case 1: {
                return (AnimationFactory)new FadeAnimation();
            }
        }
    }
    
    protected int getArrangeHeightAtIndex(final int n) {
        if (this.mArranges.isEmpty()) {
            return 0;
        }
        return this.mArranges.get(n).computeHeight();
    }
    
    protected FrameLayout getBackgroundLayout() {
        return this.mBackground;
    }
    
    protected LinearLayout getContentLayout() {
        return this.mContentsBinder;
    }
    
    protected int getInitialDisplayHeight() {
        return this.calculateInitialDisplayHeight(2131165581);
    }
    
    protected ScrollContainer.Status getScrollStatus() {
        if (this.mSomcScroller == null) {
            return null;
        }
        return this.mSomcScroller.getCurrentStatus();
    }
    
    protected abstract int getScrollableContentLength();
    
    protected int getScrolledHeight() {
        if (this.mSomcScroller == null) {
            return 0;
        }
        return this.mSomcScroller.getScrolledHeight();
    }
    
    protected int getTotalArrangeHeight() {
        final boolean empty = this.mArranges.isEmpty();
        int i = 0;
        if (empty) {
            return 0;
        }
        int n = 0;
        while (i < this.mArranges.size()) {
            n += this.mArranges.get(i).computeHeight();
            ++i;
        }
        return n;
    }
    
    protected boolean isPortrait() {
        final int mOrientation = this.mOrientation;
        boolean b = true;
        if (mOrientation != 1) {
            b = false;
        }
        return b;
    }
    
    public void onDetachedFromWindow() {
        final Iterator<ContentArrange> iterator = this.mArranges.iterator();
        while (iterator.hasNext()) {
            iterator.next().release();
        }
        this.mArranges.clear();
        super.onDetachedFromWindow();
    }
    
    protected void onInitializeScroll() {
        if (this.mArranges.size() == 0) {
            if (CamLog.VERBOSE) {
                CamLog.d("panel size is 0");
            }
            return;
        }
        if (this.mIsExpandedWhenOpened) {
            final int computeScrollOffset = this.mArranges.get(0).computeScrollOffset();
            int n;
            if (this.isPortrait()) {
                n = this.mParams.maxHeightPortrait - this.getInitialDisplayHeight();
            }
            else {
                n = this.mParams.maxHeightLandscape - this.getInitialDisplayHeight();
            }
            this.scrollTo(0, computeScrollOffset + n);
        }
        else {
            this.scrollTo(0, 0);
        }
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        return !this.isEnabled() || super.onInterceptTouchEvent(motionEvent);
    }
    
    public void scrollTo(final int n, final int n2) {
        if (this.mSomcScroller != null) {
            this.mSomcScroller.scrollTo(n, n2);
        }
        if (this.mDefaultScroller != null) {
            this.mDefaultScroller.scrollTo(n, n2);
        }
    }
    
    public void selectTagItem(final Object o) {
    }
    
    protected void setBottomMarginHint(final int mBottomMarginHint) {
        if (this.mBottomMarginHint != mBottomMarginHint) {
            this.mBottomMarginHint = mBottomMarginHint;
        }
    }
    
    protected void setScrollStatus(final ScrollContainer.Status currentStatus) {
        this.mSomcScroller.setCurrentStatus(currentStatus);
    }
    
    protected void setTopMarginHint(final int mTopMarginHint) {
        if (this.mTopMarginHint != mTopMarginHint) {
            this.mTopMarginHint = mTopMarginHint;
        }
    }
    
    protected void setUiOrientation(final int n) {
        this.mOrientation = n;
        if (this.mSomcScroller != null) {
            this.mSomcScroller.setOrientation(n);
        }
    }
    
    protected void setup(final boolean b, final AbsSelectableDialog.Params params, final ScrollContainer.OnScrollListener onScrollListener, final int width, final int gravity, final boolean mIsExpandedWhenOpened) {
        this.mIsExpandedWhenOpened = mIsExpandedWhenOpened;
        this.mParams = new AbsSelectableDialog.Params(params);
        inflate(this.getContext(), 2131492994, (ViewGroup)this);
        this.mBackground = (FrameLayout)this.findViewById(2131296573);
        this.mBackground.getLayoutParams().width = width;
        ((FrameLayout$LayoutParams)this.mBackground.getLayoutParams()).gravity = gravity;
        if (this.mParams.dropShadowSpace > 0) {
            this.mBackground.setBackgroundResource(2131231020);
            this.mBackground.setPadding(this.mParams.dropShadowSpace, this.mParams.dropShadowSpace, this.mParams.dropShadowSpace, this.mParams.dropShadowSpace);
        }
        if (b) {
            (this.mSomcScroller = (ScrollContainer)this.findViewById(2131296619)).setSettingMenuParams(this.mParams);
            this.mSomcScroller.setOnScrollListener(onScrollListener);
            this.mSomcScroller.setVisibility(0);
            this.mContentsBinder = (LinearLayout)this.mSomcScroller.findViewById(2131296620);
            this.mSomcScroller.post((Runnable)new Runnable(this) {
                final AbsDialogScrollView this$0;
                
                @Override
                public void run() {
                    this.this$0.onInitializeScroll();
                }
            });
        }
        else {
            (this.mDefaultScroller = (ScrollView)this.findViewById(2131296382)).setVisibility(0);
            if (this.mParams.scrollbarFadeDuration > 0) {
                this.mDefaultScroller.setScrollBarFadeDuration(this.mParams.scrollbarFadeDuration);
            }
            if (this.mParams.scrollBarDefaultDelayBeforeFade > 0) {
                this.mDefaultScroller.setScrollBarDefaultDelayBeforeFade(this.mParams.scrollBarDefaultDelayBeforeFade);
            }
            this.mContentsBinder = (LinearLayout)this.mDefaultScroller.findViewById(2131296383);
        }
    }
    
    protected void startCloseAnimation(final int n, final int n2, final Animator$AnimatorListener animator$AnimatorListener) {
        this.getAnimationFactory().close(n, n2, animator$AnimatorListener);
    }
    
    protected void startOpenAnimation(final int n, final int n2, final Animator$AnimatorListener animator$AnimatorListener) {
        this.getAnimationFactory().open(n, n2, animator$AnimatorListener);
    }
    
    protected void updateDefaultScrollPosition() {
        this.mSomcScroller.setSettingDefaultHeight(this.getInitialDisplayHeight());
        this.mSomcScroller.setChildHeight(this.getScrollableContentLength());
    }
    
    public abstract void updateRotatableLayout(final ViewGroup p0, final int p1, final int p2);
    
    protected interface AnimationFactory
    {
        void close(final int p0, final int p1, final Animator$AnimatorListener p2);
        
        void open(final int p0, final int p1, final Animator$AnimatorListener p2);
    }
    
    protected abstract static class ContentArrange extends DataSetObserver
    {
        protected ViewGroup container;
        protected ArrayAdapter mAdapter;
        protected LinearLayout mRowItems;
        
        protected ContentArrange(final ArrayAdapter mAdapter) {
            (this.mAdapter = mAdapter).registerDataSetObserver((DataSetObserver)this);
        }
        
        protected void addItemView(final View view, final int n) {
            this.mRowItems.addView(view);
        }
        
        protected int computeArrangeHeight() {
            return 0;
        }
        
        protected int computeHeight() {
            return 0;
        }
        
        protected int computeScrollOffset() {
            return 0;
        }
        
        protected void fetchItems() {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("fetchItems() E prev-items:");
                sb.append(this.mRowItems.getChildCount());
                sb.append(" adapter-size:");
                sb.append(this.mAdapter.getCount());
                CamLog.d(sb.toString());
            }
            if (this.mRowItems.getChildCount() > this.mAdapter.getCount()) {
                this.mRowItems.removeViews(this.mAdapter.getCount(), this.mRowItems.getChildCount() - this.mAdapter.getCount());
            }
            for (int i = 0; i < this.mAdapter.getCount(); ++i) {
                if (i < this.mRowItems.getChildCount()) {
                    this.mAdapter.getView(i, this.getPositionItemView(i), this.getPositionItemContainerView(i));
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Update existing item, size:");
                        sb2.append(this.mRowItems.getChildCount());
                        CamLog.d(sb2.toString());
                    }
                }
                else {
                    this.addItemView(this.mAdapter.getView(i, (View)null, this.getPositionItemContainerView(i)), i);
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("Add new item, size:");
                        sb3.append(this.mRowItems.getChildCount());
                        CamLog.d(sb3.toString());
                    }
                }
            }
            if (CamLog.VERBOSE) {
                CamLog.d("fetchItems() X");
            }
        }
        
        protected View findViewWithTag(final Object o) {
            return null;
        }
        
        protected int getItemCount() {
            return this.mAdapter.getCount();
        }
        
        protected ViewGroup getPositionItemContainerView(final int n) {
            return (ViewGroup)this.mRowItems;
        }
        
        protected View getPositionItemView(final int n) {
            return this.mRowItems.getChildAt(n);
        }
        
        protected View getView() {
            return (View)this.container;
        }
        
        protected void release() {
            this.mAdapter.unregisterDataSetObserver((DataSetObserver)this);
        }
        
        protected abstract void setup();
        
        protected void updateItems(final ArrayAdapter mAdapter) {
            this.mAdapter.unregisterDataSetObserver((DataSetObserver)this);
            (this.mAdapter = mAdapter).registerDataSetObserver((DataSetObserver)this);
            this.fetchItems();
        }
    }
    
    private class FadeAnimation implements AnimationFactory
    {
        private final int mDuration;
        final AbsDialogScrollView this$0;
        
        private FadeAnimation(final AbsDialogScrollView this$0) {
            this.this$0 = this$0;
            this.mDuration = this.this$0.getResources().getInteger(2131361808);
        }
        
        @Override
        public void close(final int n, final int n2, final Animator$AnimatorListener animator$AnimatorListener) {
            final AnimatorSet set = new AnimatorSet();
            set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.this$0.getBackgroundLayout(), "alpha", new float[] { 1.0f, 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.this$0.getBackgroundLayout(), "translationY", new float[] { 0.0f, n2 / 4.0f }) });
            set.setDuration((long)this.mDuration);
            set.setInterpolator((TimeInterpolator)this.this$0.mInterporater);
            if (animator$AnimatorListener != null) {
                set.addListener(animator$AnimatorListener);
            }
            set.start();
        }
        
        @Override
        public void open(final int n, final int n2, final Animator$AnimatorListener animator$AnimatorListener) {
            final AnimatorSet set = new AnimatorSet();
            set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.this$0.getBackgroundLayout(), "alpha", new float[] { 0.0f, 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.this$0.getBackgroundLayout(), "translationY", new float[] { n2 / 4.0f, 0.0f }) });
            set.setDuration((long)this.mDuration);
            set.setInterpolator((TimeInterpolator)this.this$0.mInterporater);
            if (animator$AnimatorListener != null) {
                set.addListener(animator$AnimatorListener);
            }
            set.start();
        }
    }
    
    private class SliderAnimation implements AnimationFactory
    {
        private final int mDuration;
        final AbsDialogScrollView this$0;
        
        private SliderAnimation(final AbsDialogScrollView this$0) {
            this.this$0 = this$0;
            this.mDuration = this.this$0.getResources().getInteger(2131361808);
        }
        
        @Override
        public void close(final int n, final int n2, final Animator$AnimatorListener animator$AnimatorListener) {
            final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this.this$0.getBackgroundLayout(), "translationY", new float[] { 0.0f, (float)n2 });
            ofFloat.setDuration((long)this.mDuration);
            ofFloat.setInterpolator((TimeInterpolator)this.this$0.mInterporater);
            if (animator$AnimatorListener != null) {
                ofFloat.addListener(animator$AnimatorListener);
            }
            ofFloat.start();
        }
        
        @Override
        public void open(final int n, final int n2, final Animator$AnimatorListener animator$AnimatorListener) {
            final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this.this$0.getBackgroundLayout(), "translationY", new float[] { (float)n2, 0.0f });
            ofFloat.setDuration((long)this.mDuration);
            ofFloat.setInterpolator((TimeInterpolator)this.this$0.mInterporater);
            if (animator$AnimatorListener != null) {
                ofFloat.addListener(animator$AnimatorListener);
            }
            ofFloat.start();
        }
    }
}
