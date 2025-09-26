// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.overlaycontrol.imagequality;

import com.sonyericsson.cameracommon.utility.RotationUtil;
import android.graphics.Color;
import com.sonymobile.cameracommon.font.FontUtil;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.TextView;
import android.widget.RelativeLayout;

class ImageQualityControlResetButton extends RelativeLayout
{
    private RelativeLayout mContainer;
    private TextView mTextView;
    
    public ImageQualityControlResetButton(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mContainer = (RelativeLayout)this.findViewById(2131296363);
        this.mTextView = (TextView)this.findViewById(2131296532);
        if ("Reset".equalsIgnoreCase(this.mTextView.getText().toString())) {
            this.mTextView.setText((CharSequence)"RESET");
        }
        FontUtil.setRobotoFont(this.mTextView, FontUtil.RobotoFontType.CONDENSED_BOLD);
    }
    
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            this.mTextView.setTextColor(Color.parseColor("#FFFFFFFF"));
        }
        else {
            this.mTextView.setTextColor(Color.parseColor("#4CFFFFFF"));
        }
    }
    
    public void setUiOrientation(final int n) {
        this.mContainer.setRotation(RotationUtil.getAngle(n));
    }
}
