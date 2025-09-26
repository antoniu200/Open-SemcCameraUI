// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.baselayout.settingshortcut;

import android.content.res.Resources;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import com.sonymobile.cameracommon.font.FontUtil;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.TextView;

public class ModeSelectorButton extends TextView
{
    private boolean mIsAvailable;
    private int mOrientation;
    private boolean mRequestVisible;
    
    public ModeSelectorButton(final Context context) {
        super(context);
        this.init();
    }
    
    public ModeSelectorButton(final Context context, final AttributeSet set) {
        super(context, set);
        this.init();
    }
    
    private void init() {
        this.mIsAvailable = false;
        this.mRequestVisible = false;
        FontUtil.setRobotoFont(this, FontUtil.RobotoFontType.MEDIUM);
        this.update();
    }
    
    private void setTextResource(final int n) {
        if (-1 == n) {
            super.setText((CharSequence)"");
        }
        else {
            super.setText((CharSequence)this.getResources().getString(n).toUpperCase());
        }
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
    }
    
    public void hide() {
        this.mRequestVisible = false;
        this.update();
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        this.setUiOrientation(this.mOrientation);
    }
    
    public void set(final boolean mIsAvailable) {
        this.mIsAvailable = mIsAvailable;
        this.update();
    }
    
    public void setUiOrientation(final int mOrientation) {
        this.mOrientation = mOrientation;
        final int width = this.getWidth();
        final int height = this.getHeight();
        if (width != 0 && height != 0) {
            final float n = width / 4.0f;
            this.setPivotX(height / 4.0f + n);
            this.setPivotY(3 * height / 4.0f - n);
            this.setRotation(RotationUtil.getAngle(mOrientation));
        }
    }
    
    public void show() {
        this.mRequestVisible = true;
        this.update();
    }
    
    public void update(final boolean b) {
        final Resources resources = this.getResources();
        Type type;
        if (b) {
            type = Type.RETURN;
        }
        else {
            type = Type.MODE_SELECTOR;
        }
        this.setBackgroundResource(type.mBackgroundId);
        this.setTextResource(type.mStringId);
        this.setContentDescription((CharSequence)resources.getString(type.mContentDescriptionId));
        int dimensionPixelSize;
        if (type.mMinWidthId != -1) {
            dimensionPixelSize = resources.getDimensionPixelSize(type.mMinWidthId);
        }
        else {
            dimensionPixelSize = 0;
        }
        this.setMinimumWidth(dimensionPixelSize);
    }
    
    private enum Type
    {
        private static final Type[] $VALUES;
        
        MODE_SELECTOR(2131231505, 2131689669, 2131689669, 2131165438), 
        RETURN(2131231465, -1, 2131689563, -1);
        
        private final int mBackgroundId;
        private final int mContentDescriptionId;
        private final int mMinWidthId;
        private final int mStringId;
        
        static {
            $VALUES = new Type[] { Type.MODE_SELECTOR, Type.RETURN };
        }
        
        private Type(final int mBackgroundId, final int mStringId, final int mContentDescriptionId, final int mMinWidthId) {
            this.mBackgroundId = mBackgroundId;
            this.mStringId = mStringId;
            this.mContentDescriptionId = mContentDescriptionId;
            this.mMinWidthId = mMinWidthId;
        }
    }
}
