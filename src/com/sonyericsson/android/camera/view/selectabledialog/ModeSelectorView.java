package com.sonyericsson.android.camera.view.selectabledialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import com.sonyericsson.android.camera.view.modeselector.view.CapturingModePanelAttributes;
import com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView;
import com.sonyericsson.android.camera.view.selectabledialog.ScrollContainer;
import com.sonyericsson.cameracommon.utility.RotationUtil;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ModeSelectorView extends AbsDialogScrollView {
    private static final int COLUMN_NUM = 3;
    private View.OnClickListener mOnItemClickListener;

    @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView
    protected void onInitializeScroll() {
    }

    public ModeSelectorView(@NonNull Context context) {
        super(context);
    }

    protected void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.mOnItemClickListener = onClickListener;
    }

    @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView
    protected void addContent(String str, ArrayAdapter arrayAdapter) {
        if (this.mArranges.isEmpty()) {
            GridArrange gridArrange = new GridArrange(arrayAdapter);
            this.mArranges.add(gridArrange);
            addContentView(gridArrange.getView());
            gridArrange.getView().getLayoutParams().width = -1;
            gridArrange.getView().getLayoutParams().height = -1;
            return;
        }
        this.mArranges.get(0).updateItems(arrayAdapter);
    }

    @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView
    protected int getInitialDisplayHeight() {
        if (isPortrait()) {
            return calculateInitialDisplayHeight(R.dimen.mode_selector_default_height);
        }
        return this.mParams.maxHeightLandscape;
    }

    @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView
    protected int getScrollableContentLength() {
        int initialDisplayHeight;
        if (this.mArranges.isEmpty()) {
            return 0;
        }
        int iComputeArrangeHeight = this.mArranges.get(0).computeArrangeHeight();
        return (!isPortrait() && (initialDisplayHeight = getInitialDisplayHeight()) > iComputeArrangeHeight) ? initialDisplayHeight : iComputeArrangeHeight;
    }

    @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView
    public void updateDefaultScrollPosition() {
        if (getScrollStatus() == ScrollContainer.Status.CLOSING) {
            return;
        }
        super.updateDefaultScrollPosition();
    }

    @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView
    public void updateRotatableLayout(ViewGroup viewGroup, int i, int i2) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mSomcScroller.getLayoutParams();
        if (isPortrait()) {
            layoutParams.height = this.mParams.maxHeightPortrait;
        } else {
            layoutParams.height = this.mParams.maxHeightLandscape;
        }
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) getLayoutParams();
        if (isPortrait()) {
            layoutParams2.width = viewGroup.getMeasuredHeight();
        } else {
            layoutParams2.width = viewGroup.getMeasuredHeight() + LayoutDependencyResolver.getNavigationBarMargin(getContext());
        }
        int i3 = layoutParams2.height;
        int i4 = i3 - (isPortrait() ? this.mParams.maxHeightPortrait : this.mParams.maxHeightLandscape);
        int iMax = Math.max(0, Math.min(this.mBottomMarginHint, i3 - (i2 + i4)));
        if (isPortrait()) {
            layoutParams2.setMargins(this.mParams.leftMarginPortrait, 0, this.mParams.rightMarginPortrait, this.mParams.bottomMarginPortrait);
            this.mSomcScroller.setViewMargin((this.mParams.maxHeightPortrait - getInitialDisplayHeight()) + i4 + iMax);
        } else {
            layoutParams2.setMargins(this.mParams.leftMarginLandscape, 0, 0, this.mParams.bottomMarginLandscape);
            this.mSomcScroller.setViewMargin(this.mParams.maxHeightLandscape - getInitialDisplayHeight());
        }
        setPivotX(getLayoutParams().width / 2.0f);
        setPivotY(getLayoutParams().height - (getLayoutParams().width / 2.0f));
        setRotation(RotationUtil.getAngle(this.mOrientation));
        getLayoutParams().height = Math.max(viewGroup.getMeasuredWidth(), viewGroup.getMeasuredHeight());
        ((FrameLayout.LayoutParams) getLayoutParams()).gravity = 80 | this.mParams.horizontalGavity.value;
        requestLayout();
    }

    protected class GridArrange extends AbsDialogScrollView.ContentArrange {
        private int mItemMargin;

        protected GridArrange(ArrayAdapter arrayAdapter) {
            super(arrayAdapter);
            setup();
            fetchItems();
        }

        @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView.ContentArrange
        protected void setup() {
            this.container = new FrameLayout(ModeSelectorView.this.getContext());
            this.container.setPadding(0, ModeSelectorView.this.dimenToPixel(R.dimen.mode_selector_grid_top_margin), 0, 0);
            LinearLayout linearLayout = new LinearLayout(ModeSelectorView.this.getContext());
            this.container.addView(linearLayout);
            linearLayout.getLayoutParams().width = -1;
            linearLayout.getLayoutParams().height = -2;
            if (ModeSelectorView.this.mParams.panelBackgroundColor != 0) {
                this.container.setBackgroundColor(ModeSelectorView.this.mParams.panelBackgroundColor);
            }
            linearLayout.setOrientation(1);
            this.mRowItems = new LinearLayout(ModeSelectorView.this.getContext());
            this.mRowItems.setMotionEventSplittingEnabled(false);
            linearLayout.addView(this.mRowItems);
            this.mRowItems.setOrientation(1);
            this.mRowItems.getLayoutParams().width = -1;
            this.mRowItems.getLayoutParams().height = -2;
            int navigationBarMargin = ModeSelectorView.this.getLayoutParams().width;
            if (!ModeSelectorView.this.isPortrait()) {
                navigationBarMargin -= LayoutDependencyResolver.getNavigationBarMargin(ModeSelectorView.this.getContext());
            }
            this.mItemMargin = (navigationBarMargin - (ModeSelectorView.this.mParams.itemWidth * 3)) / 4;
        }

        @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView.ContentArrange
        protected int computeHeight() {
            if (!ModeSelectorView.this.mIsExpandedWhenOpened) {
                return ModeSelectorView.this.getScrolledHeight();
            }
            return ModeSelectorView.this.getScrollableContentLength();
        }

        @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView.ContentArrange
        protected int computeArrangeHeight() {
            return (ModeSelectorView.this.mParams.itemHeight * ((int) Math.ceil(getItemCount() / 3.0d))) + this.container.getPaddingTop();
        }

        @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView.ContentArrange
        protected void fetchItems() {
            this.mRowItems.removeAllViews();
            for (int i = 0; i < this.mAdapter.getCount(); i++) {
                addItemView(this.mAdapter.getView(i, null, getPositionItemContainerView(i)), i);
                if (CamLog.VERBOSE) {
                    CamLog.d("Add new item, size:" + this.mRowItems.getChildCount());
                }
            }
            int count = this.mAdapter.getCount();
            LinearLayout lastRowContainer = getLastRowContainer();
            int childCount = lastRowContainer.getChildCount();
            int i2 = count % 3;
            if (i2 == 0 || childCount <= i2) {
                return;
            }
            lastRowContainer.removeViews(childCount, -(childCount - i2));
        }

        @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView.ContentArrange
        protected void addItemView(View view, int i) {
            view.setTag(this.mAdapter.getItem(i));
            view.setBackgroundResource(R.drawable.setting_item_selector);
            view.setOnClickListener(ModeSelectorView.this.mOnItemClickListener);
            LinearLayout linearLayout = null;
            if (this.mRowItems.getChildCount() > 0) {
                LinearLayout lastRowContainer = getLastRowContainer();
                if (3 > lastRowContainer.getChildCount()) {
                    linearLayout = lastRowContainer;
                }
            }
            if (linearLayout == null) {
                linearLayout = new LinearLayout(ModeSelectorView.this.getContext());
                linearLayout.setMotionEventSplittingEnabled(false);
                linearLayout.setGravity(3);
                this.mRowItems.addView(linearLayout);
                linearLayout.getLayoutParams().width = -1;
                linearLayout.getLayoutParams().height = -2;
            }
            ((LinearLayout.LayoutParams) view.getLayoutParams()).leftMargin = this.mItemMargin;
            linearLayout.addView(view);
        }

        @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView.ContentArrange
        protected View getPositionItemView(int i) {
            return getPositionItemContainerView(i).getChildAt(getPositionItemsColumnNum(i) - 1);
        }

        @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView.ContentArrange
        protected View findViewWithTag(Object obj) {
            for (int i = 0; i < this.mRowItems.getChildCount(); i++) {
                LinearLayout rowContainer = getRowContainer(i);
                for (int i2 = 0; i2 < rowContainer.getChildCount(); i2++) {
                    View childAt = rowContainer.getChildAt(i2);
                    if ((childAt.getTag() instanceof CapturingModePanelAttributes) && ((CapturingModePanelAttributes) childAt.getTag()).getModeName().equals(obj)) {
                        return childAt;
                    }
                }
            }
            return null;
        }

        @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView.ContentArrange
        protected ViewGroup getPositionItemContainerView(int i) {
            return getRowContainer(getPositionItemsRowCount(i) - 1);
        }

        private int getPositionItemsRowCount(int i) {
            return (int) Math.ceil((i + 1) / 3.0d);
        }

        private int getPositionItemsColumnNum(int i) {
            return (i + 1) - ((getPositionItemsRowCount(i) - 1) * 3);
        }

        private LinearLayout getRowContainer(int i) {
            return (LinearLayout) this.mRowItems.getChildAt(i);
        }

        private LinearLayout getLastRowContainer() {
            return getRowContainer(this.mRowItems.getChildCount() - 1);
        }
    }
}
