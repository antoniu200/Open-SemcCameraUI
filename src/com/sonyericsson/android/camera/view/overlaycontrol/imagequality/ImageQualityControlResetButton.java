package com.sonyericsson.android.camera.view.overlaycontrol.imagequality;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import com.sonymobile.cameracommon.font.FontUtil;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class ImageQualityControlResetButton extends RelativeLayout {
    private RelativeLayout mContainer;
    private TextView mTextView;

    public ImageQualityControlResetButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mContainer = (RelativeLayout) findViewById(R.id.container);
        this.mTextView = (TextView) findViewById(R.id.reset_button_text);
        if ("Reset".equalsIgnoreCase(this.mTextView.getText().toString())) {
            this.mTextView.setText("RESET");
        }
        FontUtil.setRobotoFont(this.mTextView, FontUtil.RobotoFontType.CONDENSED_BOLD);
    }

    public void setUiOrientation(int i) {
        this.mContainer.setRotation(RotationUtil.getAngle(i));
    }

    @Override // android.view.View
    public void setEnabled(boolean z) {
        super.setEnabled(z);
        if (z) {
            this.mTextView.setTextColor(Color.parseColor("#FFFFFFFF"));
        } else {
            this.mTextView.setTextColor(Color.parseColor("#4CFFFFFF"));
        }
    }
}
