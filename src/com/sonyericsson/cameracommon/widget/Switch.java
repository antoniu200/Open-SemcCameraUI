package com.sonyericsson.cameracommon.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sonyericsson.android.camera.R;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class Switch extends LinearLayout {
    private static final int DISABLED_FILTER = 2131099706;
    private static final int OFF_POSITION = 0;
    public static final String TAG = "Switch";
    private boolean mIsChecked;
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;
    private int mOnPosition;
    protected View mSwitchBundle;
    private ImageView mSwitchKnob;
    private ImageView mSwitchTrack;
    protected TextView mText;

    public Switch(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mOnPosition = 0;
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mSwitchTrack = (ImageView) findViewById(R.id.switch_track);
        this.mSwitchKnob = (ImageView) findViewById(R.id.switch_knob);
        this.mText = (TextView) findViewById(R.id.switch_category);
        this.mSwitchBundle = findViewById(R.id.switch_bundle);
        setOnClickListener(new SwitchOnClickListener());
    }

    public void setText(CharSequence charSequence) {
        this.mText.setText(charSequence);
    }

    public void setChecked(boolean z) {
        this.mIsChecked = z;
        updateIcon();
        updatePosition();
    }

    @Override // android.view.View
    public void setEnabled(boolean z) {
        super.setEnabled(z);
        if (isEnabled()) {
            this.mText.setTextColor(getResources().getColor(R.color.default_text_col, null));
            this.mSwitchTrack.clearColorFilter();
        } else {
            this.mText.setTextColor(getResources().getColor(R.color.grayout_text_col, null));
            this.mSwitchTrack.setColorFilter(2131099706);
        }
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        this.mOnCheckedChangeListener = onCheckedChangeListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeState() {
        this.mIsChecked = !this.mIsChecked;
        setChecked(this.mIsChecked);
        if (this.mOnCheckedChangeListener != null) {
            this.mOnCheckedChangeListener.onCheckedChanged(null, this.mIsChecked);
        }
    }

    private void updatePosition() {
        this.mSwitchKnob.setTranslationX(this.mIsChecked ? this.mOnPosition : 0);
    }

    private void updateIcon() {
        if (this.mIsChecked) {
            this.mSwitchTrack.setImageResource(R.drawable.cam_setting_switch_on_bg_icn);
            this.mSwitchKnob.setImageResource(R.drawable.cam_setting_switch_on_icn);
        } else {
            this.mSwitchTrack.setImageResource(R.drawable.cam_setting_switch_off_bg_icn);
            this.mSwitchKnob.setImageResource(R.drawable.cam_setting_switch_off_icn);
        }
        this.mOnPosition = this.mSwitchTrack.getDrawable().getIntrinsicWidth() - this.mSwitchKnob.getDrawable().getIntrinsicWidth();
    }

    private final class SwitchOnClickListener implements View.OnClickListener {
        private SwitchOnClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Switch.this.changeState();
        }
    }
}
