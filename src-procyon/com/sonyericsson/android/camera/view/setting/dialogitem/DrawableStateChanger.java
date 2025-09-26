// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.setting.dialogitem;

import com.sonyericsson.android.camera.view.setting.dialog.SettingAdapter;
import android.view.View;

class DrawableStateChanger
{
    private static final int DRAWABLE_LEVEL_BOTTOM = 2;
    private static final int DRAWABLE_LEVEL_NORMAL = 0;
    private static final int DRAWABLE_LEVEL_TOP = 1;
    private View mBackground;
    private View mDividerBottom;
    private View mDividerLeft;
    private View mDividerRight;
    private final SettingAdapter.ItemLayoutParams mParams;
    
    public DrawableStateChanger(final SettingAdapter.ItemLayoutParams mParams) {
        this.mParams = mParams;
        this.mDividerBottom = null;
        this.mDividerLeft = null;
        this.mDividerRight = null;
        this.mBackground = null;
    }
    
    private int getDrawableLevel(final SettingAdapter.ItemLayoutParams itemLayoutParams) {
        if (this.mParams.top) {
            return 1;
        }
        if (itemLayoutParams.bottom) {
            return 2;
        }
        return 0;
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
                }
                else if (this.mParams.left) {
                    this.mDividerRight.setVisibility(0);
                    this.mDividerLeft.setVisibility(8);
                }
                else if (this.mParams.right) {
                    this.mDividerRight.setVisibility(8);
                    this.mDividerLeft.setVisibility(0);
                }
                else {
                    this.mDividerRight.setVisibility(0);
                    this.mDividerLeft.setVisibility(0);
                }
            }
            if (this.mBackground != null && this.mBackground.getBackground() != null) {
                this.mBackground.getBackground().setLevel(this.getDrawableLevel(this.mParams));
            }
        }
    }
    
    public DrawableStateChanger background(final View mBackground) {
        this.mBackground = mBackground;
        return this;
    }
    
    public DrawableStateChanger dividerHorizontal(final View mDividerBottom) {
        this.mDividerBottom = mDividerBottom;
        return this;
    }
    
    public DrawableStateChanger dividerVertical(final View mDividerLeft, final View mDividerRight) {
        this.mDividerLeft = mDividerLeft;
        this.mDividerRight = mDividerRight;
        return this;
    }
}
