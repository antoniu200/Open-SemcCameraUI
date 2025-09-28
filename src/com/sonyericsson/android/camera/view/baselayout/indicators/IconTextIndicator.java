package com.sonyericsson.android.camera.view.baselayout.indicators;

import android.widget.ImageView;
import android.widget.TextView;
import com.sonyericsson.cameracommon.viewfinder.indicators.Indicator;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class IconTextIndicator extends Indicator implements Runnable {
    private static final long TEXT_VISIBLE_DURATION = 2000;
    private int mOrientation;
    private final TextView mText;

    public IconTextIndicator(ImageView imageView, TextView textView) {
        super(imageView);
        this.mText = textView;
        update();
    }

    public void setTextResource(int i) {
        this.mText.setText(i);
    }

    @Override // com.sonyericsson.cameracommon.viewfinder.indicators.Indicator
    public void setSensorOrientation(int i) {
        super.setSensorOrientation(i);
        this.mOrientation = i;
        update();
    }

    @Override // com.sonyericsson.cameracommon.viewfinder.indicators.Indicator
    protected void update() {
        super.update();
        if (this.mText != null) {
            this.mText.removeCallbacks(this);
            if (this.mOn && this.mVisible && isLandscape()) {
                this.mText.setVisibility(0);
                this.mText.postDelayed(this, TEXT_VISIBLE_DURATION);
            } else {
                this.mText.setVisibility(8);
            }
        }
    }

    private boolean isLandscape() {
        return this.mOrientation == 2;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.mText == null || this.mText.getVisibility() != 0) {
            return;
        }
        this.mText.setVisibility(8);
    }
}
