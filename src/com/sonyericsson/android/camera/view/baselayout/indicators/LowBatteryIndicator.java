package com.sonyericsson.android.camera.view.baselayout.indicators;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.cameracommon.systemmonitor.BatteryChangedReceiver;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import com.sonymobile.cameracommon.font.FontUtil;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class LowBatteryIndicator extends BaseIndicator {
    private final int mIconWidth;
    private int mLevel;
    private final int mRightMargin;
    private TextView mTextView;
    private final int mTextWidth;

    public LowBatteryIndicator(Context context, String str) {
        super(str);
        this.mIconWidth = context.getResources().getDimensionPixelSize(R.dimen.top_indicator_image_size);
        this.mTextWidth = context.getResources().getDimensionPixelSize(R.dimen.battery_indicator_text_max_width);
        this.mRightMargin = context.getResources().getDimensionPixelSize(R.dimen.top_indicators_interval);
        this.mLevel = 0;
    }

    public void setBatteryLevel(int i) {
        this.mLevel = i;
        if (BatteryChangedReceiver.isCheckEnabled() && this.mLevel <= BatteryChangedReceiver.THRESHOLD_LOW_BATTERY_LEVEL) {
            set(true);
        } else {
            set(false);
        }
    }

    @Override // com.sonyericsson.android.camera.view.baselayout.indicators.BaseIndicator
    protected void onUpdated(View view, boolean z, int i) {
        if (z) {
            view.setVisibility(0);
            ((LinearLayout.LayoutParams) view.getLayoutParams()).width = this.mIconWidth + this.mTextWidth;
            ((LinearLayout.LayoutParams) view.getLayoutParams()).rightMargin = this.mRightMargin;
            view.setRotation(RotationUtil.getAngle(i));
            view.setPivotX(this.mTextWidth + (this.mIconWidth / 2));
            view.setPivotY(this.mIconWidth / 2.0f);
            view.requestLayout();
            updateTextView(view);
            return;
        }
        view.setVisibility(4);
        ((LinearLayout.LayoutParams) view.getLayoutParams()).width = this.mTextWidth;
        ((LinearLayout.LayoutParams) view.getLayoutParams()).rightMargin = 0;
        view.setRotation(0.0f);
        view.requestLayout();
    }

    private void updateTextView(View view) {
        if (this.mTextView == null) {
            this.mTextView = (TextView) view.findViewById(R.id.battery_indicator_text);
            FontUtil.setRobotoFont(this.mTextView, FontUtil.RobotoFontType.MEDIUM);
        }
        this.mTextView.setText(this.mLevel + "%");
    }
}
