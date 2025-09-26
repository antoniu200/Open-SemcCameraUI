// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view;

import android.util.AttributeSet;
import android.content.Context;
import com.sonyericsson.android.camera.configuration.parameters.FrontAngle;
import android.widget.ImageView;

public class FrontAngleSwitchButton extends ImageView
{
    private FrontAngle mAngle;
    private int mCurrentOrientation;
    
    public FrontAngleSwitchButton(final Context context) {
        super(context);
        this.mCurrentOrientation = 0;
        this.mAngle = FrontAngle.DEFAULT;
    }
    
    public FrontAngleSwitchButton(final Context context, final AttributeSet set) {
        super(context, set);
        this.mCurrentOrientation = 0;
        this.mAngle = FrontAngle.DEFAULT;
    }
    
    private void update() {
        switch (this.mCurrentOrientation) {
            case 2: {
                if (this.mAngle == FrontAngle.DEFAULT) {
                    this.setBackgroundResource(2131231575);
                    break;
                }
                this.setBackgroundResource(2131231576);
                break;
            }
            case 1: {
                if (this.mAngle == FrontAngle.DEFAULT) {
                    this.setBackgroundResource(2131231577);
                    break;
                }
                this.setBackgroundResource(2131231578);
                break;
            }
        }
        this.invalidate();
    }
    
    public void hide() {
        this.setVisibility(4);
    }
    
    public void setUiOrientation(final int mCurrentOrientation) {
        if (this.mCurrentOrientation == mCurrentOrientation && this.getVisibility() == 0) {
            return;
        }
        this.mCurrentOrientation = mCurrentOrientation;
        this.update();
    }
    
    public void show() {
        this.setVisibility(0);
    }
    
    public void switchFrontAngle(final FrontAngle mAngle) {
        this.mAngle = mAngle;
        if (this.mAngle == FrontAngle.DEFAULT) {
            this.setContentDescription((CharSequence)this.getResources().getString(2131689607));
        }
        else {
            this.setContentDescription((CharSequence)this.getResources().getString(2131689608));
        }
        this.update();
    }
}
