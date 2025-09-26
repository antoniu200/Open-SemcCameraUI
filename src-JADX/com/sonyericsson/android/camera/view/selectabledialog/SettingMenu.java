package com.sonyericsson.android.camera.view.selectabledialog;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.view.selectabledialog.AbsSelectableDialog;
import com.sonyericsson.android.camera.view.selectabledialog.ScrollContainer;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SettingMenu extends AbsSelectableDialog {
    public SettingMenu(Context context, AbsSelectableDialog.Params params, AbsSelectableDialog.SelectableDialogType selectableDialogType) {
        this(context, params, 80, false, selectableDialogType);
    }

    public SettingMenu(Context context, AbsSelectableDialog.Params params, int i, boolean z, AbsSelectableDialog.SelectableDialogType selectableDialogType) {
        super(context, params, z);
        this.mDialogType = selectableDialogType;
        this.mDialogScrollView = new SettingMenuView(context, selectableDialogType);
        this.mDialogScrollView.setup(AbsSelectableDialog.SelectableDialogType.SETTING_MENU == selectableDialogType, params, this, computeWidth(), i, z);
        if (AbsSelectableDialog.SelectableDialogType.SETTING_MENU == selectableDialogType && z) {
            this.mDialogScrollView.setScrollStatus(ScrollContainer.Status.FULLSCREEN);
        }
    }

    @Override // com.sonyericsson.android.camera.view.setting.dialog.SettingDialogInterface
    public void open(ViewGroup viewGroup) {
        openImmediate(viewGroup);
        startOpenAnimation();
    }

    public void openImmediate(ViewGroup viewGroup) {
        this.mParent = (FrameLayout) viewGroup;
        this.mParent.addView(this.mDialogScrollView);
        this.mDialogScrollView.getLayoutParams().width = this.mParent.getMeasuredHeight();
        this.mDialogScrollView.getLayoutParams().height = Math.max(this.mParent.getMeasuredWidth(), this.mParent.getMeasuredHeight());
        ((FrameLayout.LayoutParams) this.mDialogScrollView.getLayoutParams()).gravity = this.mParams.horizontalGavity.value | 80;
        adjustLayout();
    }

    @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsSelectableDialog
    protected void onOrientationChanged(int i) {
        if (isSettingMenu()) {
            if (this.mSettingDialogStack.isSecondLayerDialogOpened() && this.mDialogScrollView.getScrollStatus() == ScrollContainer.Status.IDLE) {
                this.mSettingDialogStack.reopenSecondLayerDialog();
            }
            if (isPortrait()) {
                this.mSettingDialogStack.addBottomView();
            } else {
                this.mSettingDialogStack.removeBottomView();
            }
        }
        super.onOrientationChanged(i);
    }

    public void select(Object obj) {
        this.mDialogScrollView.selectTagItem(obj);
    }

    @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsSelectableDialog, com.sonyericsson.android.camera.view.setting.dialog.SettingDialogInterface
    public boolean hitTest(int i, int i2) {
        Rect rect = new Rect();
        if (isSettingMenu()) {
            if (this.mDialogScrollView.getContentLayout().getGlobalVisibleRect(rect)) {
                return rect.contains(i, i2);
            }
            return false;
        }
        if (this.mDialogScrollView.getBackgroundLayout().getGlobalVisibleRect(rect)) {
            return rect.contains(i, i2);
        }
        return false;
    }

    private boolean isSettingMenu() {
        return this.mDialogType == AbsSelectableDialog.SelectableDialogType.SETTING_MENU;
    }

    @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsSelectableDialog
    public boolean isOperationAcceptable() {
        if (this.mDialogScrollView.getScrollStatus() == null) {
            return false;
        }
        switch (this.mDialogScrollView.getScrollStatus()) {
        }
        return false;
    }

    public boolean getGlobalVisibleItemRect(Rect rect, Object obj) {
        View viewFindItemViewWithTag = this.mDialogScrollView.findItemViewWithTag(obj);
        if (viewFindItemViewWithTag != null) {
            return viewFindItemViewWithTag.getGlobalVisibleRect(rect);
        }
        return false;
    }

    @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsSelectableDialog, com.sonyericsson.android.camera.view.selectabledialog.ScrollContainer.OnScrollListener
    public void onScrollFinished(ScrollContainer.Status status) {
        if (status == ScrollContainer.Status.EXIT) {
            this.mDialogScrollView.post(new Runnable() { // from class: com.sonyericsson.android.camera.view.selectabledialog.SettingMenu.1
                @Override // java.lang.Runnable
                public void run() {
                    ((CameraActivity) SettingMenu.this.mContext).findViewById(R.id.contextual_setting_shortcut).callOnClick();
                }
            });
        }
    }

    @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsSelectableDialog
    public void closeImmediate() {
        if (this.mSettingDialogStack != null && isSettingMenu()) {
            this.mSettingDialogStack.removeBottomView();
        }
        super.closeImmediate();
    }
}
