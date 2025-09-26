// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.selectabledialog;

import android.widget.FrameLayout;
import com.sonyericsson.android.camera.view.modeselector.view.CapturingModePanelAttributes;
import com.sonyericsson.android.camera.util.CamLog;
import android.widget.LinearLayout$LayoutParams;
import android.view.View;
import android.widget.LinearLayout;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import android.widget.FrameLayout$LayoutParams;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.support.annotation.NonNull;
import android.content.Context;
import android.view.View$OnClickListener;

public class ModeSelectorView extends AbsDialogScrollView
{
    private static final int COLUMN_NUM = 3;
    private View$OnClickListener mOnItemClickListener;
    
    public ModeSelectorView(@NonNull final Context context) {
        super(context);
    }
    
    @Override
    protected void addContent(final String s, final ArrayAdapter arrayAdapter) {
        if (this.mArranges.isEmpty()) {
            final GridArrange gridArrange = new GridArrange(arrayAdapter);
            this.mArranges.add((ContentArrange)gridArrange);
            this.addContentView(((ContentArrange)gridArrange).getView());
            ((ContentArrange)gridArrange).getView().getLayoutParams().width = -1;
            ((ContentArrange)gridArrange).getView().getLayoutParams().height = -1;
        }
        else {
            ((ContentArrange)this.mArranges.get(0)).updateItems(arrayAdapter);
        }
    }
    
    @Override
    protected int getInitialDisplayHeight() {
        if (this.isPortrait()) {
            return this.calculateInitialDisplayHeight(2131165441);
        }
        return this.mParams.maxHeightLandscape;
    }
    
    @Override
    protected int getScrollableContentLength() {
        if (this.mArranges.isEmpty()) {
            return 0;
        }
        int computeArrangeHeight = ((ContentArrange)this.mArranges.get(0)).computeArrangeHeight();
        if (!this.isPortrait()) {
            final int initialDisplayHeight = this.getInitialDisplayHeight();
            if (initialDisplayHeight > computeArrangeHeight) {
                computeArrangeHeight = initialDisplayHeight;
            }
        }
        return computeArrangeHeight;
    }
    
    @Override
    protected void onInitializeScroll() {
    }
    
    protected void setOnItemClickListener(final View$OnClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    
    public void updateDefaultScrollPosition() {
        if (this.getScrollStatus() == ScrollContainer.Status.CLOSING) {
            return;
        }
        super.updateDefaultScrollPosition();
    }
    
    @Override
    public void updateRotatableLayout(final ViewGroup viewGroup, int n, int max) {
        final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)this.mSomcScroller.getLayoutParams();
        if (this.isPortrait()) {
            frameLayout$LayoutParams.height = this.mParams.maxHeightPortrait;
        }
        else {
            frameLayout$LayoutParams.height = this.mParams.maxHeightLandscape;
        }
        final FrameLayout$LayoutParams frameLayout$LayoutParams2 = (FrameLayout$LayoutParams)this.getLayoutParams();
        if (this.isPortrait()) {
            frameLayout$LayoutParams2.width = viewGroup.getMeasuredHeight();
        }
        else {
            frameLayout$LayoutParams2.width = viewGroup.getMeasuredHeight() + LayoutDependencyResolver.getNavigationBarMargin(this.getContext());
        }
        final int height = frameLayout$LayoutParams2.height;
        if (this.isPortrait()) {
            n = this.mParams.maxHeightPortrait;
        }
        else {
            n = this.mParams.maxHeightLandscape;
        }
        n = height - n;
        max = Math.max(0, Math.min(this.mBottomMarginHint, height - (max + n)));
        if (this.isPortrait()) {
            frameLayout$LayoutParams2.setMargins(this.mParams.leftMarginPortrait, 0, this.mParams.rightMarginPortrait, this.mParams.bottomMarginPortrait);
            this.mSomcScroller.setViewMargin(this.mParams.maxHeightPortrait - this.getInitialDisplayHeight() + n + max);
        }
        else {
            frameLayout$LayoutParams2.setMargins(this.mParams.leftMarginLandscape, 0, 0, this.mParams.bottomMarginLandscape);
            this.mSomcScroller.setViewMargin(this.mParams.maxHeightLandscape - this.getInitialDisplayHeight());
        }
        this.setPivotX(this.getLayoutParams().width / 2.0f);
        this.setPivotY(this.getLayoutParams().height - this.getLayoutParams().width / 2.0f);
        this.setRotation(RotationUtil.getAngle(this.mOrientation));
        this.getLayoutParams().height = Math.max(viewGroup.getMeasuredWidth(), viewGroup.getMeasuredHeight());
        ((FrameLayout$LayoutParams)this.getLayoutParams()).gravity = (0x50 | this.mParams.horizontalGavity.value);
        this.requestLayout();
    }
    
    protected class GridArrange extends ContentArrange
    {
        private int mItemMargin;
        final ModeSelectorView this$0;
        
        protected GridArrange(final ModeSelectorView this$0, final ArrayAdapter arrayAdapter) {
            this.this$0 = this$0;
            super(arrayAdapter);
            this.setup();
            this.fetchItems();
        }
        
        private LinearLayout getLastRowContainer() {
            return this.getRowContainer(this.mRowItems.getChildCount() - 1);
        }
        
        private int getPositionItemsColumnNum(final int n) {
            return n + 1 - (this.getPositionItemsRowCount(n) - 1) * 3;
        }
        
        private int getPositionItemsRowCount(final int n) {
            return (int)Math.ceil((n + 1) / 3.0);
        }
        
        private LinearLayout getRowContainer(final int n) {
            return (LinearLayout)this.mRowItems.getChildAt(n);
        }
        
        @Override
        protected void addItemView(final View view, int childCount) {
            view.setTag(this.mAdapter.getItem(childCount));
            view.setBackgroundResource(2131231546);
            view.setOnClickListener(this.this$0.mOnItemClickListener);
            childCount = this.mRowItems.getChildCount();
            Object lastRowContainer;
            final Object o = lastRowContainer = null;
            if (childCount > 0) {
                lastRowContainer = this.getLastRowContainer();
                if (3 <= ((LinearLayout)lastRowContainer).getChildCount()) {
                    lastRowContainer = o;
                }
            }
            LinearLayout linearLayout;
            if ((linearLayout = (LinearLayout)lastRowContainer) == null) {
                linearLayout = new LinearLayout(this.this$0.getContext());
                linearLayout.setMotionEventSplittingEnabled(false);
                linearLayout.setGravity(3);
                this.mRowItems.addView((View)linearLayout);
                linearLayout.getLayoutParams().width = -1;
                linearLayout.getLayoutParams().height = -2;
            }
            ((LinearLayout$LayoutParams)view.getLayoutParams()).leftMargin = this.mItemMargin;
            linearLayout.addView(view);
        }
        
        @Override
        protected int computeArrangeHeight() {
            return this.this$0.mParams.itemHeight * (int)Math.ceil(((ContentArrange)this).getItemCount() / 3.0) + this.container.getPaddingTop();
        }
        
        @Override
        protected int computeHeight() {
            if (!this.this$0.mIsExpandedWhenOpened) {
                return this.this$0.getScrolledHeight();
            }
            return this.this$0.getScrollableContentLength();
        }
        
        @Override
        protected void fetchItems() {
            this.mRowItems.removeAllViews();
            for (int i = 0; i < this.mAdapter.getCount(); ++i) {
                this.addItemView(this.mAdapter.getView(i, (View)null, this.getPositionItemContainerView(i)), i);
                if (CamLog.VERBOSE) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Add new item, size:");
                    sb.append(this.mRowItems.getChildCount());
                    CamLog.d(sb.toString());
                }
            }
            final int count = this.mAdapter.getCount();
            final LinearLayout lastRowContainer = this.getLastRowContainer();
            final int childCount = lastRowContainer.getChildCount();
            final int n = count % 3;
            if (n != 0 && childCount > n) {
                lastRowContainer.removeViews(childCount, -(childCount - n));
            }
        }
        
        @Override
        protected View findViewWithTag(final Object anObject) {
            for (int i = 0; i < this.mRowItems.getChildCount(); ++i) {
                final LinearLayout rowContainer = this.getRowContainer(i);
                for (int j = 0; j < ((ViewGroup)rowContainer).getChildCount(); ++j) {
                    final View child = ((ViewGroup)rowContainer).getChildAt(j);
                    if (child.getTag() instanceof CapturingModePanelAttributes && ((CapturingModePanelAttributes)child.getTag()).getModeName().equals(anObject)) {
                        return child;
                    }
                }
            }
            return null;
        }
        
        @Override
        protected ViewGroup getPositionItemContainerView(final int n) {
            return (ViewGroup)this.getRowContainer(this.getPositionItemsRowCount(n) - 1);
        }
        
        @Override
        protected View getPositionItemView(final int n) {
            return this.getPositionItemContainerView(n).getChildAt(this.getPositionItemsColumnNum(n) - 1);
        }
        
        @Override
        protected void setup() {
            (this.container = (ViewGroup)new FrameLayout(this.this$0.getContext())).setPadding(0, this.this$0.dimenToPixel(2131165448), 0, 0);
            final LinearLayout linearLayout = new LinearLayout(this.this$0.getContext());
            this.container.addView((View)linearLayout);
            linearLayout.getLayoutParams().width = -1;
            linearLayout.getLayoutParams().height = -2;
            if (this.this$0.mParams.panelBackgroundColor != 0) {
                this.container.setBackgroundColor(this.this$0.mParams.panelBackgroundColor);
            }
            linearLayout.setOrientation(1);
            (this.mRowItems = new LinearLayout(this.this$0.getContext())).setMotionEventSplittingEnabled(false);
            linearLayout.addView((View)this.mRowItems);
            this.mRowItems.setOrientation(1);
            this.mRowItems.getLayoutParams().width = -1;
            this.mRowItems.getLayoutParams().height = -2;
            int width = this.this$0.getLayoutParams().width;
            if (!this.this$0.isPortrait()) {
                width -= LayoutDependencyResolver.getNavigationBarMargin(this.this$0.getContext());
            }
            this.mItemMargin = (width - this.this$0.mParams.itemWidth * 3) / 4;
        }
    }
}
