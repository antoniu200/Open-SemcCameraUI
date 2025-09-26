package com.sonyericsson.android.camera.view.setting.dialogitem;

import android.view.View;
import com.sonyericsson.android.camera.view.setting.dialog.SettingAdapter;

/* compiled from: SettingDialogItem.java */
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class DrawableStateChanger {
    private static final int DRAWABLE_LEVEL_BOTTOM = 2;
    private static final int DRAWABLE_LEVEL_NORMAL = 0;
    private static final int DRAWABLE_LEVEL_TOP = 1;
    private final SettingAdapter.ItemLayoutParams mParams;
    private View mDividerBottom = null;
    private View mDividerLeft = null;
    private View mDividerRight = null;
    private View mBackground = null;

    public DrawableStateChanger(SettingAdapter.ItemLayoutParams itemLayoutParams) {
        this.mParams = itemLayoutParams;
    }

    public DrawableStateChanger dividerHorizontal(View view) {
        this.mDividerBottom = view;
        return this;
    }

    public DrawableStateChanger dividerVertical(View view, View view2) {
        this.mDividerLeft = view;
        this.mDividerRight = view2;
        return this;
    }

    public DrawableStateChanger background(View view) {
        this.mBackground = view;
        return this;
    }

    public void apply() {
        if (this.mParams != null) {
            if (this.mDividerBottom != null) {
                this.mDividerBottom.setVisibility(0);
            }
            if (this.mDividerLeft != null && this.mDividerRight != null) {
                if (this.mParams.left && this.mParams.right) {
                    this.mDividerRight.setVisibility(8);
                    this.mDividerLeft.setVisibility(8);
                } else if (this.mParams.left) {
                    this.mDividerRight.setVisibility(0);
                    this.mDividerLeft.setVisibility(8);
                } else if (this.mParams.right) {
                    this.mDividerRight.setVisibility(8);
                    this.mDividerLeft.setVisibility(0);
                } else {
                    this.mDividerRight.setVisibility(0);
                    this.mDividerLeft.setVisibility(0);
                }
            }
            if (this.mBackground == null || this.mBackground.getBackground() == null) {
                return;
            }
            this.mBackground.getBackground().setLevel(getDrawableLevel(this.mParams));
        }
    }

    private int getDrawableLevel(SettingAdapter.ItemLayoutParams itemLayoutParams) {
        if (this.mParams.top) {
            return 1;
        }
        return itemLayoutParams.bottom ? 2 : 0;
    }
}
