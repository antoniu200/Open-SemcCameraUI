package com.sonyericsson.cameracommon.viewfinder.indicators;

import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.cameracommon.utility.BrandConfig;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class GeotagIndicator extends Indicator {
    public GeotagIndicator(ImageView imageView) {
        super(imageView);
    }

    public void isAcquired(boolean z) {
        if (z) {
            this.mView.setImageResource(getAcquiredGpsIcon());
        } else {
            this.mView.setImageResource(getAcquiringGpsResource());
            ((AnimationDrawable) this.mView.getDrawable()).start();
        }
    }

    public void release() {
        if (this.mView.getDrawable() instanceof AnimationDrawable) {
            ((AnimationDrawable) this.mView.getDrawable()).stop();
        }
    }

    private int getAcquiringGpsResource() {
        return BrandConfig.isVerizonBrand() ? R.drawable.cam_acquiring_gps_vzw_anim : R.drawable.cam_acquiring_gps_anim;
    }

    private int getAcquiredGpsIcon() {
        return BrandConfig.isVerizonBrand() ? R.drawable.cam_acquired_gps_vzw_icn : R.drawable.cam_acquired_gps_icn;
    }
}
