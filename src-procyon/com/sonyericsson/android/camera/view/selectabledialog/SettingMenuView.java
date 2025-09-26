// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.selectabledialog;

import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.FrameLayout;
import android.database.DataSetObserver;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;
import java.util.Iterator;
import com.sonyericsson.cameracommon.widget.CategorySwitch;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import android.widget.ArrayAdapter;
import android.view.View;
import com.sonyericsson.android.camera.view.setting.dialogitem.SettingDialogItem;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import android.graphics.Rect;
import android.view.ViewGroup;
import android.view.ViewGroup$LayoutParams;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import android.widget.FrameLayout$LayoutParams;
import android.support.annotation.NonNull;
import android.content.Context;

public class SettingMenuView extends AbsDialogScrollView
{
    private final boolean mIsSettingMenu;
    private final AbsSelectableDialog.SelectableDialogType mMenuDialogType;
    
    public SettingMenuView(@NonNull final Context context, final AbsSelectableDialog.SelectableDialogType mMenuDialogType) {
        super(context);
        this.mMenuDialogType = mMenuDialogType;
        this.mIsSettingMenu = (mMenuDialogType == AbsSelectableDialog.SelectableDialogType.SETTING_MENU);
    }
    
    private void adjustMonochromeLayout(final int n, final int n2) {
        final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.getLayoutParams();
        final float angle = RotationUtil.getAngle(this.mOrientation);
        final int navigationBarMargin = LayoutDependencyResolver.getNavigationBarMargin(this.getContext());
        final int height = this.getLayoutParams().height;
        final int width = this.getLayoutParams().width;
        final int dimenToPixel = this.dimenToPixel(2131165451);
        final int dimenToPixel2 = this.dimenToPixel(2131165450);
        if (this.isPortrait()) {
            if (angle != this.getRotation()) {
                final float pivotX = width / 2.0f;
                this.setPivotX(pivotX);
                this.setPivotY(height - pivotX);
                this.setRotation(angle);
                if (this.mBottomMarginHint == 0) {
                    layoutParams.leftMargin = height - width - this.mTopMarginHint - dimenToPixel - navigationBarMargin - dimenToPixel2;
                    layoutParams.bottomMargin = dimenToPixel2;
                }
                else {
                    final int leftMargin = this.mTopMarginHint - (width - n2) + dimenToPixel2;
                    if (n2 + leftMargin < width) {
                        layoutParams.leftMargin = leftMargin;
                    }
                    else {
                        layoutParams.leftMargin = height - width - dimenToPixel2 - navigationBarMargin;
                    }
                    layoutParams.bottomMargin = (width - n) / 2;
                }
            }
        }
        else if (angle != this.getRotation()) {
            final float pivotX2 = width / 2.0f;
            this.setPivotX(pivotX2);
            this.setPivotY(height - pivotX2);
            this.setRotation(angle);
            if (this.mBottomMarginHint == 0) {
                layoutParams.leftMargin = height - this.mTopMarginHint - n - navigationBarMargin;
                layoutParams.bottomMargin = dimenToPixel2 * 2 + dimenToPixel;
            }
            else {
                layoutParams.leftMargin = height - this.mBottomMarginHint + (this.mBottomMarginHint - n) / 2 - navigationBarMargin;
                if (this.mTopMarginHint > n2 + dimenToPixel2) {
                    layoutParams.bottomMargin = this.mTopMarginHint - n2 - dimenToPixel2;
                }
                else {
                    layoutParams.bottomMargin = dimenToPixel2;
                }
            }
        }
        else if (this.mBottomMarginHint == 0) {
            layoutParams.leftMargin = height - this.mTopMarginHint - n - navigationBarMargin;
            layoutParams.bottomMargin = dimenToPixel2 * 2 + dimenToPixel;
        }
        else {
            layoutParams.leftMargin = height - this.mBottomMarginHint + (this.mBottomMarginHint - n - navigationBarMargin) / 2;
            if (this.mTopMarginHint > n2 + dimenToPixel2) {
                layoutParams.bottomMargin = this.mTopMarginHint - n2 - dimenToPixel2;
            }
            else {
                layoutParams.bottomMargin = dimenToPixel2;
            }
        }
        this.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
    }
    
    private void adjustRotatableLayoutGravityBottom(final ViewGroup viewGroup, int n, final int n2) {
        if (this.mIsSettingMenu) {
            final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)this.mSomcScroller.getLayoutParams();
            if (this.isPortrait()) {
                frameLayout$LayoutParams.height = this.mParams.maxHeightPortrait;
            }
            else {
                frameLayout$LayoutParams.height = this.mParams.maxHeightLandscape;
            }
            this.mSomcScroller.setOrientation(this.mOrientation);
        }
        final int height = this.getLayoutParams().height;
        final int width = this.getLayoutParams().width;
        if (this.isPortrait()) {
            n = this.mParams.maxHeightPortrait;
        }
        else {
            n = this.mParams.maxHeightLandscape;
        }
        n = height - n;
        this.setPadding(0, n, 0, Math.max(0, Math.min(this.mBottomMarginHint, height - (n + n2))));
        final FrameLayout$LayoutParams frameLayout$LayoutParams2 = (FrameLayout$LayoutParams)this.getLayoutParams();
        if (this.isPortrait()) {
            frameLayout$LayoutParams2.setMargins(this.mParams.leftMarginPortrait, 0, this.mParams.rightMarginPortrait, this.mParams.bottomMarginPortrait);
            if (this.mIsSettingMenu && this.mSomcScroller.getCurrentStatus() != ScrollContainer.Status.FULLSCREEN) {
                this.mSomcScroller.setViewMargin(this.mParams.maxHeightPortrait - this.getInitialDisplayHeight());
            }
        }
        else {
            frameLayout$LayoutParams2.setMargins(this.mParams.leftMarginLandscape, 0, (viewGroup.getMeasuredWidth() - viewGroup.getMeasuredHeight()) / 2, this.mParams.bottomMarginLandscape);
            if (this.mIsSettingMenu && this.mSomcScroller.getCurrentStatus() != ScrollContainer.Status.FULLSCREEN) {
                this.mSomcScroller.setViewMargin(this.mParams.maxHeightLandscape - this.getInitialDisplayHeight());
            }
        }
        if (SettingMenuView$2.$SwitchMap$com$sonyericsson$android$camera$view$selectabledialog$AbsSelectableDialog$HorizontalGravity[this.mParams.horizontalGavity.ordinal()] == 1) {
            if (this.isPortrait()) {
                this.setTranslationX((float)(n2 - width));
            }
            else {
                this.setTranslationX(0.0f);
            }
        }
    }
    
    private void adjustRotatableLayoutGravityTop(final ViewGroup viewGroup, int n, final int n2) {
        final Rect rect = new Rect();
        if (!viewGroup.getGlobalVisibleRect(rect)) {
            return;
        }
        Rect rect2 = rect;
        if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            rect2 = new Rect(0, 0, rect.height(), rect.width());
        }
        final int dimenToPixel = this.dimenToPixel(2131165584);
        final int dimenToPixel2 = this.dimenToPixel(2131165584);
        final int dimenToPixel3 = this.dimenToPixel(2131165584);
        final int dimenToPixel4 = this.dimenToPixel(2131165610);
        final int height = this.getLayoutParams().height;
        final int width = this.getLayoutParams().width;
        int n3;
        if (this.isPortrait()) {
            n3 = width - n - dimenToPixel2 + dimenToPixel4;
        }
        else {
            n3 = width - n;
        }
        if (this.isPortrait()) {
            n = this.mParams.maxHeightPortrait;
        }
        else {
            n = this.mParams.maxHeightLandscape;
        }
        int n4;
        if (this.isPortrait()) {
            n4 = rect2.right;
        }
        else {
            n4 = rect2.bottom;
        }
        int n5;
        if (this.isPortrait()) {
            n5 = 0;
        }
        else {
            n5 = height - rect2.bottom;
        }
        if (this.mIsExpandedWhenOpened) {
            if (this.mTopMarginHint + n2 < dimenToPixel4 * 2 + n4) {
                n = this.mTopMarginHint - dimenToPixel4 - dimenToPixel + n5;
            }
            else {
                final int n6 = -n2 + n4 + n5;
                if (n2 == n) {
                    n = n6 - dimenToPixel;
                }
                else {
                    n = n6 + dimenToPixel;
                }
            }
        }
        else if (this.mTopMarginHint + n2 < rect2.right + dimenToPixel4 * 2) {
            n = this.mTopMarginHint - dimenToPixel4 - dimenToPixel;
        }
        else {
            final int n7 = -n2 + rect2.right;
            if (n2 == n) {
                n = n7 - dimenToPixel;
            }
            else {
                n = n7 + dimenToPixel;
            }
        }
        this.setPadding(n3, n, 0, -dimenToPixel3);
        final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)this.getLayoutParams();
        if (!this.isPortrait()) {
            frameLayout$LayoutParams.setMargins(this.mParams.leftMarginLandscape, 0, (viewGroup.getMeasuredWidth() - viewGroup.getMeasuredHeight()) / 2 - dimenToPixel2, this.mParams.bottomMarginLandscape);
        }
        else {
            frameLayout$LayoutParams.setMargins(this.mParams.leftMarginLandscape, 0, this.mParams.rightMarginLandscape, this.mParams.bottomMarginLandscape);
        }
    }
    
    private SettingDialogItem findItemWith(final Object o) {
        final View itemViewWithTag = this.findItemViewWithTag(o);
        if (itemViewWithTag != null && itemViewWithTag.getTag() instanceof SettingDialogItem) {
            return (SettingDialogItem)itemViewWithTag.getTag();
        }
        return null;
    }
    
    @Override
    protected void addContent(final String s, final ArrayAdapter arrayAdapter) {
        final ListArrange listArrange = new ListArrange(s, arrayAdapter, this.mArranges.isEmpty());
        this.mArranges.add((ContentArrange)listArrange);
        this.addContentView(((ContentArrange)listArrange).getView());
        ((ContentArrange)listArrange).getView().getLayoutParams().width = -1;
        ((ContentArrange)listArrange).getView().getLayoutParams().height = -2;
    }
    
    @Override
    protected int getScrollableContentLength() {
        final boolean empty = this.mArranges.isEmpty();
        int i = 0;
        if (empty) {
            return 0;
        }
        int n = 0;
        while (i < this.mArranges.size()) {
            n += ((ContentArrange)this.mArranges.get(i)).computeArrangeHeight();
            ++i;
        }
        return n;
    }
    
    @Override
    public void selectTagItem(final Object o) {
        final SettingDialogItem itemWith = this.findItemWith(o);
        if (itemWith != null) {
            this.post((Runnable)new Runnable(this, o, itemWith) {
                final SettingMenuView this$0;
                final SettingDialogItem val$item;
                final Object val$tag;
                
                @Override
                public void run() {
                    final Iterator<ContentArrange> iterator = this.this$0.mArranges.iterator();
                    int n = 0;
                    while (iterator.hasNext()) {
                        n += ((ContentArrange)iterator.next()).computeHeight();
                    }
                    this.this$0.scrollTo(0, n);
                    if (this.val$tag == UserSettingKey.GEO_TAG) {
                        final View child = ((ViewGroup)this.val$item.getView()).getChildAt(1);
                        if (child instanceof CategorySwitch) {
                            child.callOnClick();
                        }
                    }
                    else if (this.val$tag == UserSettingKey.SIDE_SENSE) {
                        final View child2 = ((ViewGroup)this.val$item.getView()).getChildAt(1);
                        if (child2 instanceof CategorySwitch) {
                            child2.callOnClick();
                        }
                    }
                    else {
                        this.val$item.select(this.val$item.getItem());
                    }
                }
            });
        }
    }
    
    public void updateDefaultScrollPosition() {
        if (this.mIsSettingMenu) {
            super.updateDefaultScrollPosition();
        }
    }
    
    @Override
    public void updateRotatableLayout(final ViewGroup viewGroup, final int n, final int n2) {
        if (this.mMenuDialogType == AbsSelectableDialog.SelectableDialogType.SETTING_MONOCHROME) {
            this.adjustMonochromeLayout(n, n2);
        }
        else {
            if (((FrameLayout$LayoutParams)this.mBackground.getLayoutParams()).gravity == 48) {
                this.adjustRotatableLayoutGravityTop(viewGroup, n, n2);
            }
            else {
                this.adjustRotatableLayoutGravityBottom(viewGroup, n, n2);
            }
            this.setPivotX(this.getLayoutParams().width / 2.0f);
            this.setPivotY(this.getLayoutParams().height - this.getLayoutParams().width / 2.0f);
            this.setRotation(RotationUtil.getAngle(this.mOrientation));
            this.requestLayout();
        }
    }
    
    private class ListArrange extends ContentArrange
    {
        private final boolean mIsTop;
        private final String mTitle;
        private final int mTitleLeftPadding;
        final SettingMenuView this$0;
        
        protected ListArrange(final SettingMenuView this$0, final String mTitle, final ArrayAdapter arrayAdapter, final boolean mIsTop) {
            this.this$0 = this$0;
            super(arrayAdapter);
            this.mTitle = mTitle;
            this.mIsTop = mIsTop;
            this.mTitleLeftPadding = this$0.dimenToPixel(2131165579);
            this.setup();
            ((ContentArrange)this).fetchItems();
        }
        
        @Override
        protected int computeArrangeHeight() {
            final String mTitle = this.mTitle;
            int n = 0;
            int dimenToPixel;
            if (mTitle == null) {
                dimenToPixel = 0;
            }
            else {
                dimenToPixel = this.this$0.dimenToPixel(2131165586);
            }
            final int itemHeight = this.this$0.mParams.itemHeight;
            final int dimenToPixel2 = this.this$0.dimenToPixel(2131165613);
            final int count = this.mAdapter.getCount();
            if (!this.mIsTop) {
                n = dimenToPixel2;
            }
            return dimenToPixel + itemHeight * count + n;
        }
        
        @Override
        protected int computeHeight() {
            if (this.this$0.mIsSettingMenu && !this.this$0.mIsExpandedWhenOpened) {
                return this.this$0.getInitialDisplayHeight();
            }
            return this.computeArrangeHeight();
        }
        
        @Override
        protected int computeScrollOffset() {
            final String mTitle = this.mTitle;
            int n = 0;
            int dimenToPixel;
            if (mTitle == null) {
                dimenToPixel = 0;
            }
            else {
                dimenToPixel = this.this$0.dimenToPixel(2131165586);
            }
            final int itemHeight = this.this$0.mParams.itemHeight;
            final int dimenToPixel2 = this.this$0.dimenToPixel(2131165588);
            final int dimenToPixel3 = this.this$0.dimenToPixel(2131165613);
            int n2 = 0;
            while (n < this.mAdapter.getCount() && !((SettingItem)this.mAdapter.getItem(n)).isSelected()) {
                if (n == 0) {
                    n2 += dimenToPixel + itemHeight;
                }
                else {
                    n2 += itemHeight + dimenToPixel2;
                }
                ++n;
            }
            return n2 + dimenToPixel3;
        }
        
        @Override
        protected View findViewWithTag(final Object o) {
            for (int i = 0; i < this.mRowItems.getChildCount(); ++i) {
                final View child = this.mRowItems.getChildAt(i);
                if (child.getTag() instanceof SettingDialogItem && ((SettingDialogItem)child.getTag()).getItem().compareData(o)) {
                    return child;
                }
            }
            return null;
        }
        
        public void onChanged() {
            ((ContentArrange)this).fetchItems();
        }
        
        public void onInvalidated() {
            ((ContentArrange)this).fetchItems();
        }
        
        @Override
        protected void release() {
            this.mAdapter.unregisterDataSetObserver((DataSetObserver)this);
            for (int i = 0; i < this.mRowItems.getChildCount(); ++i) {
                final View child = this.mRowItems.getChildAt(i);
                if (child.getTag() instanceof SettingDialogItem) {
                    ((SettingDialogItem)child.getTag()).setClickable(false);
                }
            }
        }
        
        @Override
        protected void setup() {
            this.container = (ViewGroup)new FrameLayout(this.this$0.getContext());
            if (!this.mIsTop) {
                this.container.setPadding(0, this.this$0.dimenToPixel(2131165613), 0, 0);
            }
            final LinearLayout linearLayout = new LinearLayout(this.this$0.getContext());
            this.container.addView((View)linearLayout);
            linearLayout.getLayoutParams().width = -1;
            linearLayout.getLayoutParams().height = -2;
            if (this.this$0.mParams.panelBackgroundColor != 0) {
                linearLayout.setBackgroundColor(this.this$0.mParams.panelBackgroundColor);
            }
            linearLayout.setOrientation(1);
            if (this.mTitle != null) {
                final TextView textView = new TextView(this.this$0.getContext());
                linearLayout.addView((View)textView);
                textView.getLayoutParams().width = -1;
                textView.getLayoutParams().height = this.this$0.dimenToPixel(2131165586);
                textView.setText((CharSequence)this.mTitle);
                textView.setContentDescription((CharSequence)this.mTitle);
                textView.setTextColor(this.this$0.getResources().getColor(2131099758));
                textView.setTextSize(1, 14.0f);
                textView.setGravity(16);
                textView.setPaddingRelative(this.mTitleLeftPadding, 0, 0, 0);
            }
            (this.mRowItems = new LinearLayout(this.this$0.getContext())).setMotionEventSplittingEnabled(false);
            linearLayout.addView((View)this.mRowItems);
            this.mRowItems.setOrientation(1);
            this.mRowItems.getLayoutParams().width = -1;
            this.mRowItems.getLayoutParams().height = -2;
        }
    }
}
