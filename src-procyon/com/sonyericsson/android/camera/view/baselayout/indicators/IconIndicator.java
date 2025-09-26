// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.baselayout.indicators;

import android.view.View;

public class IconIndicator extends BaseIndicator
{
    private int mImageResourceId;
    
    public IconIndicator(final String s) {
        super(s);
        this.mImageResourceId = -1;
    }
    
    @Override
    protected void onUpdated(final View view, final boolean b, final int n) {
        super.onUpdated(view, b, n);
        if (this.mImageResourceId != -1) {
            view.setBackgroundResource(this.mImageResourceId);
        }
    }
    
    public void setBackgroundResource(final int mImageResourceId) {
        this.mImageResourceId = mImageResourceId;
    }
}
