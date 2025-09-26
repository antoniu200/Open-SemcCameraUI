// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.rotatableview;

import android.util.AttributeSet;
import android.content.Context;
import android.widget.FrameLayout;

public class EdgeRotatableContainerView extends FrameLayout
{
    private int mOrientation;
    
    public EdgeRotatableContainerView(final Context context, final AttributeSet set) {
        super(context, set);
        this.mOrientation = 2;
    }
    
    private void setOrientation(float n, final float n2) {
        if (this.mOrientation == 2) {
            this.setRotation(0.0f);
            this.setTranslationX(0.0f);
            this.setTranslationY(0.0f);
        }
        else {
            this.setRotation(-90.0f);
            n = (n - n2) / 2.0f;
            this.setTranslationX(n);
            this.setTranslationY(n);
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n, n2);
        this.setOrientation((float)this.getMeasuredWidth(), (float)this.getMeasuredHeight());
    }
    
    public void setOrientation(final int mOrientation) {
        this.mOrientation = mOrientation;
        this.setOrientation((float)this.getWidth(), (float)this.getHeight());
    }
}
