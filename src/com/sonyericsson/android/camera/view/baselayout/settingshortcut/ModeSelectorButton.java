package com.sonyericsson.android.camera.view.baselayout.settingshortcut;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.TextView;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import com.sonymobile.cameracommon.font.FontUtil;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ModeSelectorButton extends TextView {
    private boolean mIsAvailable;
    private int mOrientation;
    private boolean mRequestVisible;

    private enum Type {
        MODE_SELECTOR(R.drawable.mode_selector_button, R.string.cam_strings_capturing_mode_button_txt, R.string.cam_strings_capturing_mode_button_txt, R.dimen.mode_selector_button_minimum_width),
        RETURN(R.drawable.close_app, -1, R.string.cam_strings_accessibility_back_main_txt, -1);

        private final int mBackgroundId;
        private final int mContentDescriptionId;
        private final int mMinWidthId;
        private final int mStringId;

        Type(int i, int i2, int i3, int i4) {
            this.mBackgroundId = i;
            this.mStringId = i2;
            this.mContentDescriptionId = i3;
            this.mMinWidthId = i4;
        }
    }

    public ModeSelectorButton(Context context) {
        super(context);
        init();
    }

    public ModeSelectorButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public void setUiOrientation(int i) {
        this.mOrientation = i;
        int width = getWidth();
        int height = getHeight();
        if (width == 0 || height == 0) {
            return;
        }
        float f = width / 4.0f;
        setPivotX((height / 4.0f) + f);
        setPivotY(((3 * height) / 4.0f) - f);
        setRotation(RotationUtil.getAngle(i));
    }

    @Override // android.widget.TextView, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        setUiOrientation(this.mOrientation);
    }

    public void update(boolean z) {
        Type type;
        Resources resources = getResources();
        if (z) {
            type = Type.RETURN;
        } else {
            type = Type.MODE_SELECTOR;
        }
        setBackgroundResource(type.mBackgroundId);
        setTextResource(type.mStringId);
        setContentDescription(resources.getString(type.mContentDescriptionId));
        setMinimumWidth(type.mMinWidthId != -1 ? resources.getDimensionPixelSize(type.mMinWidthId) : 0);
    }

    public void show() {
        this.mRequestVisible = true;
        update();
    }

    public void hide() {
        this.mRequestVisible = false;
        update();
    }

    public void set(boolean z) {
        this.mIsAvailable = z;
        update();
    }

    private void init() {
        this.mIsAvailable = false;
        this.mRequestVisible = false;
        FontUtil.setRobotoFont(this, FontUtil.RobotoFontType.MEDIUM);
        update();
    }

    private void update() {
        if (this.mIsAvailable && this.mRequestVisible) {
            setVisibility(0);
            setClickable(true);
        } else {
            setVisibility(8);
            setClickable(false);
            setPressed(false);
            setSelected(false);
        }
    }

    private void setTextResource(int i) {
        if (-1 == i) {
            super.setText("");
        } else {
            super.setText(getResources().getString(i).toUpperCase());
        }
    }
}
