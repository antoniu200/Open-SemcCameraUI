// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.viewfinder.indicators;

import android.graphics.drawable.AnimationDrawable;
import com.sonyericsson.cameracommon.utility.BrandConfig;
import android.widget.ImageView;

public class GeotagIndicator extends Indicator
{
    public GeotagIndicator(final ImageView imageView) {
        super(imageView);
    }
    
    private int getAcquiredGpsIcon() {
        if (BrandConfig.isVerizonBrand()) {
            return 2131230842;
        }
        return 2131230841;
    }
    
    private int getAcquiringGpsResource() {
        if (BrandConfig.isVerizonBrand()) {
            return 2131230849;
        }
        return 2131230848;
    }
    
    public void isAcquired(final boolean b) {
        if (b) {
            this.mView.setImageResource(this.getAcquiredGpsIcon());
        }
        else {
            this.mView.setImageResource(this.getAcquiringGpsResource());
            ((AnimationDrawable)this.mView.getDrawable()).start();
        }
    }
    
    public void release() {
        if (this.mView.getDrawable() instanceof AnimationDrawable) {
            ((AnimationDrawable)this.mView.getDrawable()).stop();
        }
    }
}
