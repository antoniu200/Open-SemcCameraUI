// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.viewfinder.indicators;

import com.sonyericsson.cameracommon.utility.RotationUtil;
import android.widget.ImageView;

public class Indicator
{
    protected boolean mOn;
    protected final ImageView mView;
    protected boolean mVisible;
    
    public Indicator(final ImageView mView) {
        this.mView = mView;
        this.mOn = false;
        this.mVisible = false;
        this.update();
    }
    
    public void hide() {
        this.mVisible = false;
        this.update();
    }
    
    public void set(final boolean mOn) {
        this.mOn = mOn;
        this.update();
    }
    
    public void setImageResource(final int imageResource) {
        this.mView.setImageResource(imageResource);
    }
    
    public void setSensorOrientation(final int n) {
        this.mView.setRotation(RotationUtil.getAngle(n));
    }
    
    public void show() {
        this.mVisible = true;
        this.update();
    }
    
    protected void update() {
        if (this.mOn && this.mVisible) {
            this.mView.setVisibility(0);
        }
        else {
            this.mView.setVisibility(8);
        }
    }
}
