package com.sonyericsson.android.camera.view.baselayout.settingshortcut;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import com.sonyericsson.cameracommon.utility.RotationUtil;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ShortcutButton extends ImageView {
    private static final int DISABLED_FILTER = 2131099706;
    private static final int ICON_FADE_SWITCH_ANIMATION_DURATION = 250;
    private static final int ICON_FADE_SWITCH_ANIMATION_OFFSET = 50;
    private boolean mIsAvailable;
    private int mLastResId;
    private boolean mRequestVisible;

    public ShortcutButton(Context context) {
        super(context);
        init();
    }

    public ShortcutButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public void setUiOrientation(int i) {
        setRotation(RotationUtil.getAngle(i));
    }

    @Override // android.widget.ImageView
    public void setImageResource(int i) {
        this.mLastResId = i;
        super.setImageResource(i);
    }

    public void setImageResourceWithAnimation(int i) {
        if (this.mLastResId != i) {
            startAnimation(createIconAnimation());
        }
        this.mLastResId = i;
        super.setImageResource(i);
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
        if (isEnabled()) {
            clearColorFilter();
        } else {
            setColorFilter(2131099706);
        }
    }

    private Animation createIconAnimation() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, 0, getLayoutParams().width / 2.0f, 0, getLayoutParams().height / 2.0f);
        scaleAnimation.setInterpolator(new OvershootInterpolator());
        scaleAnimation.setDuration(250L);
        scaleAnimation.setStartOffset(50L);
        return scaleAnimation;
    }
}
