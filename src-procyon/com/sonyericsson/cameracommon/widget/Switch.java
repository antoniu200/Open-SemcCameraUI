// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.widget;

import android.content.res.Resources$Theme;
import android.view.View$OnClickListener;
import android.widget.CompoundButton;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;
import android.widget.CompoundButton$OnCheckedChangeListener;
import android.widget.LinearLayout;

public class Switch extends LinearLayout
{
    private static final int DISABLED_FILTER = 2131099706;
    private static final int OFF_POSITION = 0;
    public static final String TAG = "Switch";
    private boolean mIsChecked;
    private CompoundButton$OnCheckedChangeListener mOnCheckedChangeListener;
    private int mOnPosition;
    protected View mSwitchBundle;
    private ImageView mSwitchKnob;
    private ImageView mSwitchTrack;
    protected TextView mText;
    
    public Switch(final Context context, final AttributeSet set) {
        super(context, set);
        this.mOnPosition = 0;
    }
    
    private void changeState() {
        this.setChecked(this.mIsChecked ^= true);
        if (this.mOnCheckedChangeListener != null) {
            this.mOnCheckedChangeListener.onCheckedChanged((CompoundButton)null, this.mIsChecked);
        }
    }
    
    private void updateIcon() {
        if (this.mIsChecked) {
            this.mSwitchTrack.setImageResource(2131231310);
            this.mSwitchKnob.setImageResource(2131231311);
        }
        else {
            this.mSwitchTrack.setImageResource(2131231308);
            this.mSwitchKnob.setImageResource(2131231309);
        }
        this.mOnPosition = this.mSwitchTrack.getDrawable().getIntrinsicWidth() - this.mSwitchKnob.getDrawable().getIntrinsicWidth();
    }
    
    private void updatePosition() {
        int mOnPosition;
        if (this.mIsChecked) {
            mOnPosition = this.mOnPosition;
        }
        else {
            mOnPosition = 0;
        }
        this.mSwitchKnob.setTranslationX((float)mOnPosition);
    }
    
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mSwitchTrack = (ImageView)this.findViewById(2131296642);
        this.mSwitchKnob = (ImageView)this.findViewById(2131296640);
        this.mText = (TextView)this.findViewById(2131296639);
        this.mSwitchBundle = this.findViewById(2131296638);
        this.setOnClickListener((View$OnClickListener)new SwitchOnClickListener());
    }
    
    public void setChecked(final boolean mIsChecked) {
        this.mIsChecked = mIsChecked;
        this.updateIcon();
        this.updatePosition();
    }
    
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        if (this.isEnabled()) {
            this.mText.setTextColor(this.getResources().getColor(2131099700, (Resources$Theme)null));
            this.mSwitchTrack.clearColorFilter();
        }
        else {
            this.mText.setTextColor(this.getResources().getColor(2131099711, (Resources$Theme)null));
            this.mSwitchTrack.setColorFilter(2131099706);
        }
    }
    
    public void setOnCheckedChangeListener(final CompoundButton$OnCheckedChangeListener mOnCheckedChangeListener) {
        this.mOnCheckedChangeListener = mOnCheckedChangeListener;
    }
    
    public void setText(final CharSequence text) {
        this.mText.setText(text);
    }
    
    private final class SwitchOnClickListener implements View$OnClickListener
    {
        final Switch this$0;
        
        private SwitchOnClickListener(final Switch this$0) {
            this.this$0 = this$0;
        }
        
        public void onClick(final View view) {
            this.this$0.changeState();
        }
    }
}
