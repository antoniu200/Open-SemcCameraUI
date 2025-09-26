package com.sonyericsson.cameracommon.viewfinder.indicators;

import android.widget.ImageView;
import com.sonyericsson.cameracommon.utility.RotationUtil;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class Indicator {
    protected final ImageView mView;
    protected boolean mOn = false;
    protected boolean mVisible = false;

    public Indicator(ImageView imageView) {
        this.mView = imageView;
        update();
    }

    public void setImageResource(int i) {
        this.mView.setImageResource(i);
    }

    public void show() {
        this.mVisible = true;
        update();
    }

    public void hide() {
        this.mVisible = false;
        update();
    }

    public void set(boolean z) {
        this.mOn = z;
        update();
    }

    public void setSensorOrientation(int i) {
        this.mView.setRotation(RotationUtil.getAngle(i));
    }

    protected void update() {
        if (this.mOn && this.mVisible) {
            this.mView.setVisibility(0);
        } else {
            this.mView.setVisibility(8);
        }
    }
}
