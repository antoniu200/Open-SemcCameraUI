// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.selectabledialog;

import android.widget.FrameLayout$LayoutParams;
import android.widget.FrameLayout;
import android.view.ViewGroup;
import com.sonyericsson.android.camera.CameraActivity;
import android.view.View;
import android.graphics.Rect;
import android.content.Context;

public class SettingMenu extends AbsSelectableDialog
{
    public SettingMenu(final Context context, final Params params, final int n, final boolean b, final SelectableDialogType mDialogType) {
        super(context, params, b);
        this.mDialogType = mDialogType;
        (this.mDialogScrollView = new SettingMenuView(context, mDialogType)).setup(SelectableDialogType.SETTING_MENU == mDialogType, params, this, this.computeWidth(), n, b);
        if (SelectableDialogType.SETTING_MENU == mDialogType && b) {
            this.mDialogScrollView.setScrollStatus(Status.FULLSCREEN);
        }
    }
    
    public SettingMenu(final Context context, final Params params, final SelectableDialogType selectableDialogType) {
        this(context, params, 80, false, selectableDialogType);
    }
    
    private boolean isSettingMenu() {
        return this.mDialogType == SelectableDialogType.SETTING_MENU;
    }
    
    @Override
    public void closeImmediate() {
        if (this.mSettingDialogStack != null && this.isSettingMenu()) {
            this.mSettingDialogStack.removeBottomView();
        }
        super.closeImmediate();
    }
    
    public boolean getGlobalVisibleItemRect(final Rect rect, final Object o) {
        final View itemViewWithTag = this.mDialogScrollView.findItemViewWithTag(o);
        return itemViewWithTag != null && itemViewWithTag.getGlobalVisibleRect(rect);
    }
    
    @Override
    public boolean hitTest(final int n, final int n2) {
        final Rect rect = new Rect();
        if (this.isSettingMenu()) {
            return this.mDialogScrollView.getContentLayout().getGlobalVisibleRect(rect) && rect.contains(n, n2);
        }
        return this.mDialogScrollView.getBackgroundLayout().getGlobalVisibleRect(rect) && rect.contains(n, n2);
    }
    
    @Override
    public boolean isOperationAcceptable() {
        if (this.mDialogScrollView.getScrollStatus() == null) {
            return false;
        }
        switch (SettingMenu$2.$SwitchMap$com$sonyericsson$android$camera$view$selectabledialog$ScrollContainer$Status[this.mDialogScrollView.getScrollStatus().ordinal()]) {
            default: {
                return false;
            }
            case 1:
            case 2:
            case 3: {
                return true;
            }
        }
    }
    
    @Override
    protected void onOrientationChanged(final int n) {
        if (this.isSettingMenu()) {
            if (this.mSettingDialogStack.isSecondLayerDialogOpened() && this.mDialogScrollView.getScrollStatus() == Status.IDLE) {
                this.mSettingDialogStack.reopenSecondLayerDialog();
            }
            if (this.isPortrait()) {
                this.mSettingDialogStack.addBottomView();
            }
            else {
                this.mSettingDialogStack.removeBottomView();
            }
        }
        super.onOrientationChanged(n);
    }
    
    @Override
    public void onScrollFinished(final Status status) {
        if (status == Status.EXIT) {
            this.mDialogScrollView.post((Runnable)new Runnable(this) {
                final SettingMenu this$0;
                
                @Override
                public void run() {
                    ((CameraActivity)this.this$0.mContext).findViewById(2131296371).callOnClick();
                }
            });
        }
    }
    
    @Override
    public void open(final ViewGroup viewGroup) {
        this.openImmediate(viewGroup);
        this.startOpenAnimation();
    }
    
    public void openImmediate(final ViewGroup viewGroup) {
        (this.mParent = (FrameLayout)viewGroup).addView((View)this.mDialogScrollView);
        this.mDialogScrollView.getLayoutParams().width = this.mParent.getMeasuredHeight();
        this.mDialogScrollView.getLayoutParams().height = Math.max(this.mParent.getMeasuredWidth(), this.mParent.getMeasuredHeight());
        ((FrameLayout$LayoutParams)this.mDialogScrollView.getLayoutParams()).gravity = (this.mParams.horizontalGavity.value | 0x50);
        this.adjustLayout();
    }
    
    public void select(final Object o) {
        this.mDialogScrollView.selectTagItem(o);
    }
}
