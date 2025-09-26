// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.baselayout.settingshortcut;

import com.sonyericsson.cameracommon.utility.RotationUtil;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.ImageView;

public class ShortcutButton extends ImageView
{
    private static final int DISABLED_FILTER = 2131099706;
    private static final int ICON_FADE_SWITCH_ANIMATION_DURATION = 250;
    private static final int ICON_FADE_SWITCH_ANIMATION_OFFSET = 50;
    private boolean mIsAvailable;
    private int mLastResId;
    private boolean mRequestVisible;
    
    public ShortcutButton(final Context context) {
        super(context);
        this.init();
    }
    
    public ShortcutButton(final Context context, final AttributeSet set) {
        super(context, set);
        this.init();
    }
    
    private Animation createIconAnimation() {
        final ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, 0, this.getLayoutParams().width / 2.0f, 0, this.getLayoutParams().height / 2.0f);
        scaleAnimation.setInterpolator((Interpolator)new OvershootInterpolator());
        scaleAnimation.setDuration(250L);
        scaleAnimation.setStartOffset(50L);
        return (Animation)scaleAnimation;
    }
    
    private void init() {
        this.mIsAvailable = false;
        this.mRequestVisible = false;
        this.update();
    }
    
    private void update() {
        if (this.mIsAvailable && this.mRequestVisible) {
            this.setVisibility(0);
            this.setClickable(true);
        }
        else {
            this.setVisibility(8);
            this.setClickable(false);
            this.setPressed(false);
            this.setSelected(false);
        }
        if (this.isEnabled()) {
            this.clearColorFilter();
        }
        else {
            this.setColorFilter(2131099706);
        }
    }
    
    public void hide() {
        this.mRequestVisible = false;
        this.update();
    }
    
    public void set(final boolean mIsAvailable) {
        this.mIsAvailable = mIsAvailable;
        this.update();
    }
    
    public void setImageResource(final int mLastResId) {
        super.setImageResource(this.mLastResId = mLastResId);
    }
    
    public void setImageResourceWithAnimation(final int mLastResId) {
        if (this.mLastResId != mLastResId) {
            this.startAnimation(this.createIconAnimation());
        }
        super.setImageResource(this.mLastResId = mLastResId);
    }
    
    public void setUiOrientation(final int n) {
        this.setRotation(RotationUtil.getAngle(n));
    }
    
    public void show() {
        this.mRequestVisible = true;
        this.update();
    }
}
