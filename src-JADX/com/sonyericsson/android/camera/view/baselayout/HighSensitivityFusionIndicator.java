package com.sonyericsson.android.camera.view.baselayout;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.cameracommon.utility.RotationUtil;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class HighSensitivityFusionIndicator {
    private View.OnClickListener mListener;
    private final ViewGroup mRootView;

    public HighSensitivityFusionIndicator(@NonNull ViewGroup viewGroup) {
        this.mRootView = viewGroup;
        ImageView imageView = (ImageView) this.mRootView.findViewById(R.id.video_high_sensitivity_icn);
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.sonyericsson.android.camera.view.baselayout.HighSensitivityFusionIndicator.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                HighSensitivityFusionIndicator.this.setChecked(!view.isActivated());
                if (HighSensitivityFusionIndicator.this.mListener != null) {
                    HighSensitivityFusionIndicator.this.mListener.onClick(view);
                }
            }
        });
        imageView.setContentDescription(viewGroup.getContext().getString(R.string.cam_strings_accessibility_dual_effect_menu_txt));
    }

    public void setChecked(boolean z) {
        ImageView imageView = (ImageView) this.mRootView.findViewById(R.id.video_high_sensitivity_icn);
        if (imageView.isActivated() != z) {
            imageView.setActivated(z);
        }
    }

    public void setEnabled(boolean z) {
        ImageView imageView = (ImageView) this.mRootView.findViewById(R.id.video_high_sensitivity_icn);
        if (z) {
            imageView.setImageResource(R.drawable.cam_core_main_high_sensitivity);
        } else {
            imageView.setImageResource(R.drawable.cam_core_main_high_sensitivity_disabled);
        }
    }

    public void show() {
        if (this.mRootView.getVisibility() != 0) {
            this.mRootView.setVisibility(0);
        }
    }

    public void hide() {
        if (this.mRootView.getVisibility() != 8) {
            this.mRootView.setVisibility(8);
        }
    }

    public void setListener(View.OnClickListener onClickListener) {
        this.mListener = onClickListener;
    }

    public void setSensorOrientation(int i) {
        this.mRootView.setRotation(RotationUtil.getAngle(i));
        ImageView imageView = (ImageView) this.mRootView.findViewById(R.id.video_high_sensitivity_icn);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.gravity = 48;
        imageView.setLayoutParams(layoutParams);
    }

    public View getAnimationTarget() {
        return this.mRootView.findViewById(R.id.video_high_sensitivity_icn);
    }
}
