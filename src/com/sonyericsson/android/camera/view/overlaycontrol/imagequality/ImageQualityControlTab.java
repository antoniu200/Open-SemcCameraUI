package com.sonyericsson.android.camera.view.overlaycontrol.imagequality;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.util.CoordinateUtil;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import com.sonymobile.cameracommon.font.FontUtil;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class ImageQualityControlTab extends LinearLayout {
    private RelativeLayout mContainer;
    private final Context mContext;
    private ImageView mIcon;
    private ImageView mValueIcon;
    private TextView mValueText;

    public ImageQualityControlTab(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        setWillNotDraw(false);
        this.mContainer = (RelativeLayout) findViewById(R.id.container);
        this.mIcon = (ImageView) findViewById(2131296423);
        this.mValueText = (TextView) findViewById(R.id.valueText);
        this.mValueIcon = (ImageView) findViewById(R.id.valueIcon);
        if (getResources().getDisplayMetrics().densityDpi > DisplayMetrics.DENSITY_DEVICE_STABLE) {
            float f = (DisplayMetrics.DENSITY_DEVICE_STABLE * 1.0f) / 160.0f;
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mIcon.getLayoutParams();
            layoutParams.height = (int) (CoordinateUtil.convertPx2Dip(this.mContext, layoutParams.height) * f);
            this.mIcon.setLayoutParams(layoutParams);
            RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.mValueText.getLayoutParams();
            layoutParams2.height = (int) (CoordinateUtil.convertPx2Dip(this.mContext, layoutParams2.height) * f);
            this.mValueText.setLayoutParams(layoutParams2);
            RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams) this.mValueIcon.getLayoutParams();
            layoutParams3.height = (int) (CoordinateUtil.convertPx2Dip(this.mContext, layoutParams3.height) * f);
            this.mValueIcon.setLayoutParams(layoutParams3);
        }
        FontUtil.setRobotoFont(this.mValueText, FontUtil.RobotoFontType.MEDIUM);
    }

    public void setIcon(int i) {
        this.mIcon.setImageResource(i);
        this.mIcon.setVisibility(0);
    }

    public void setValueText(String str, boolean z) {
        this.mValueText.setText(str);
        if (!z) {
            this.mValueText.setTextColor(-1);
        } else {
            this.mValueText.setTextColor(getResources().getColor(R.color.image_quality_control_tab_text_color_selected));
        }
        this.mValueText.setVisibility(0);
    }

    public void setValueIcon(int i) {
        this.mValueIcon.setImageResource(i);
        this.mValueIcon.setVisibility(0);
    }

    public void setUiOrientation(int i) {
        this.mContainer.setRotation(RotationUtil.getAngle(i));
    }
}
