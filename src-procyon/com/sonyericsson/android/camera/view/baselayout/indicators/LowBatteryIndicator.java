// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.baselayout.indicators;

import com.sonyericsson.cameracommon.systemmonitor.BatteryChangedReceiver;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import android.widget.LinearLayout$LayoutParams;
import com.sonymobile.cameracommon.font.FontUtil;
import android.view.View;
import android.content.Context;
import android.widget.TextView;

public class LowBatteryIndicator extends BaseIndicator
{
    private final int mIconWidth;
    private int mLevel;
    private final int mRightMargin;
    private TextView mTextView;
    private final int mTextWidth;
    
    public LowBatteryIndicator(final Context context, final String s) {
        super(s);
        this.mIconWidth = context.getResources().getDimensionPixelSize(2131165675);
        this.mTextWidth = context.getResources().getDimensionPixelSize(2131165282);
        this.mRightMargin = context.getResources().getDimensionPixelSize(2131165676);
        this.mLevel = 0;
    }
    
    private void updateTextView(final View view) {
        if (this.mTextView == null) {
            FontUtil.setRobotoFont(this.mTextView = (TextView)view.findViewById(2131296318), FontUtil.RobotoFontType.MEDIUM);
        }
        final TextView mTextView = this.mTextView;
        final StringBuilder sb = new StringBuilder();
        sb.append(this.mLevel);
        sb.append("%");
        mTextView.setText((CharSequence)sb.toString());
    }
    
    @Override
    protected void onUpdated(final View view, final boolean b, final int n) {
        if (b) {
            view.setVisibility(0);
            ((LinearLayout$LayoutParams)view.getLayoutParams()).width = this.mIconWidth + this.mTextWidth;
            ((LinearLayout$LayoutParams)view.getLayoutParams()).rightMargin = this.mRightMargin;
            view.setRotation(RotationUtil.getAngle(n));
            view.setPivotX((float)(this.mTextWidth + this.mIconWidth / 2));
            view.setPivotY(this.mIconWidth / 2.0f);
            view.requestLayout();
            this.updateTextView(view);
        }
        else {
            view.setVisibility(4);
            ((LinearLayout$LayoutParams)view.getLayoutParams()).width = this.mTextWidth;
            ((LinearLayout$LayoutParams)view.getLayoutParams()).rightMargin = 0;
            view.setRotation(0.0f);
            view.requestLayout();
        }
    }
    
    public void setBatteryLevel(final int mLevel) {
        this.mLevel = mLevel;
        if (BatteryChangedReceiver.isCheckEnabled() && this.mLevel <= BatteryChangedReceiver.THRESHOLD_LOW_BATTERY_LEVEL) {
            this.set(true);
        }
        else {
            this.set(false);
        }
    }
}
