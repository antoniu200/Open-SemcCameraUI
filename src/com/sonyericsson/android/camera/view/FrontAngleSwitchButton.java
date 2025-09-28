package com.sonyericsson.android.camera.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.parameters.FrontAngle;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class FrontAngleSwitchButton extends ImageView {
    private FrontAngle mAngle;
    private int mCurrentOrientation;

    public FrontAngleSwitchButton(Context context) {
        super(context);
        this.mCurrentOrientation = 0;
        this.mAngle = FrontAngle.DEFAULT;
    }

    public FrontAngleSwitchButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mCurrentOrientation = 0;
        this.mAngle = FrontAngle.DEFAULT;
    }

    public void switchFrontAngle(FrontAngle frontAngle) {
        this.mAngle = frontAngle;
        if (this.mAngle == FrontAngle.DEFAULT) {
            setContentDescription(getResources().getString(R.string.cam_strings_accessibility_swf_normal_txt));
        } else {
            setContentDescription(getResources().getString(R.string.cam_strings_accessibility_swf_wide_txt));
        }
        update();
    }

    public void setUiOrientation(int i) {
        if (this.mCurrentOrientation == i && getVisibility() == 0) {
            return;
        }
        this.mCurrentOrientation = i;
        update();
    }

    private void update() {
        switch (this.mCurrentOrientation) {
            case 1:
                if (this.mAngle == FrontAngle.DEFAULT) {
                    setBackgroundResource(R.drawable.wide_switch_background_port_superwide);
                    break;
                } else {
                    setBackgroundResource(R.drawable.wide_switch_background_port_wide);
                    break;
                }
            case 2:
                if (this.mAngle == FrontAngle.DEFAULT) {
                    setBackgroundResource(R.drawable.wide_switch_background_land_superwide);
                    break;
                } else {
                    setBackgroundResource(R.drawable.wide_switch_background_land_wide);
                    break;
                }
        }
        invalidate();
    }

    public void show() {
        setVisibility(0);
    }

    public void hide() {
        setVisibility(4);
    }
}
