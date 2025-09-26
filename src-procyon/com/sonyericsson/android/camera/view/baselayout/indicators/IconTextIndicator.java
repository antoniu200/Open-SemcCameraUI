// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.baselayout.indicators;

import android.widget.ImageView;
import android.widget.TextView;
import com.sonyericsson.cameracommon.viewfinder.indicators.Indicator;

public class IconTextIndicator extends Indicator implements Runnable
{
    private static final long TEXT_VISIBLE_DURATION = 2000L;
    private int mOrientation;
    private final TextView mText;
    
    public IconTextIndicator(final ImageView imageView, final TextView mText) {
        super(imageView);
        this.mText = mText;
        this.update();
    }
    
    private boolean isLandscape() {
        return this.mOrientation == 2;
    }
    
    @Override
    public void run() {
        if (this.mText != null && this.mText.getVisibility() == 0) {
            this.mText.setVisibility(8);
        }
    }
    
    @Override
    public void setSensorOrientation(final int n) {
        super.setSensorOrientation(n);
        this.mOrientation = n;
        this.update();
    }
    
    public void setTextResource(final int text) {
        this.mText.setText(text);
    }
    
    @Override
    protected void update() {
        super.update();
        if (this.mText != null) {
            this.mText.removeCallbacks((Runnable)this);
            if (this.mOn && this.mVisible && this.isLandscape()) {
                this.mText.setVisibility(0);
                this.mText.postDelayed((Runnable)this, 2000L);
            }
            else {
                this.mText.setVisibility(8);
            }
        }
    }
}
