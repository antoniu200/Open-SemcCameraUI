// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.overlaycontrol.imagequality;

import com.sonyericsson.cameracommon.utility.RotationUtil;
import com.sonymobile.cameracommon.font.FontUtil;
import android.view.ViewGroup$LayoutParams;
import com.sonyericsson.android.camera.util.CoordinateUtil;
import android.widget.RelativeLayout$LayoutParams;
import android.util.DisplayMetrics;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.ImageView;
import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;

class ImageQualityControlTab extends LinearLayout
{
    private RelativeLayout mContainer;
    private final Context mContext;
    private ImageView mIcon;
    private ImageView mValueIcon;
    private TextView mValueText;
    
    public ImageQualityControlTab(final Context mContext, final AttributeSet set) {
        super(mContext, set);
        this.mContext = mContext;
    }
    
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.setWillNotDraw(false);
        this.mContainer = (RelativeLayout)this.findViewById(2131296363);
        this.mIcon = (ImageView)this.findViewById(2131296423);
        this.mValueText = (TextView)this.findViewById(2131296691);
        this.mValueIcon = (ImageView)this.findViewById(2131296690);
        if (this.getResources().getDisplayMetrics().densityDpi > DisplayMetrics.DENSITY_DEVICE_STABLE) {
            final float n = DisplayMetrics.DENSITY_DEVICE_STABLE * 1.0f / 160.0f;
            final RelativeLayout$LayoutParams layoutParams = (RelativeLayout$LayoutParams)this.mIcon.getLayoutParams();
            layoutParams.height = (int)(CoordinateUtil.convertPx2Dip(this.mContext, layoutParams.height) * n);
            this.mIcon.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            final RelativeLayout$LayoutParams layoutParams2 = (RelativeLayout$LayoutParams)this.mValueText.getLayoutParams();
            layoutParams2.height = (int)(CoordinateUtil.convertPx2Dip(this.mContext, layoutParams2.height) * n);
            this.mValueText.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
            final RelativeLayout$LayoutParams layoutParams3 = (RelativeLayout$LayoutParams)this.mValueIcon.getLayoutParams();
            layoutParams3.height = (int)(CoordinateUtil.convertPx2Dip(this.mContext, layoutParams3.height) * n);
            this.mValueIcon.setLayoutParams((ViewGroup$LayoutParams)layoutParams3);
        }
        FontUtil.setRobotoFont(this.mValueText, FontUtil.RobotoFontType.MEDIUM);
    }
    
    public void setIcon(final int imageResource) {
        this.mIcon.setImageResource(imageResource);
        this.mIcon.setVisibility(0);
    }
    
    public void setUiOrientation(final int n) {
        this.mContainer.setRotation(RotationUtil.getAngle(n));
    }
    
    public void setValueIcon(final int imageResource) {
        this.mValueIcon.setImageResource(imageResource);
        this.mValueIcon.setVisibility(0);
    }
    
    public void setValueText(final String text, final boolean b) {
        this.mValueText.setText((CharSequence)text);
        if (!b) {
            this.mValueText.setTextColor(-1);
        }
        else {
            this.mValueText.setTextColor(this.getResources().getColor(2131099718));
        }
        this.mValueText.setVisibility(0);
    }
}
