// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v14.preference;

import android.widget.CompoundButton;
import android.support.annotation.RestrictTo;
import android.support.v7.preference.PreferenceViewHolder;
import android.view.accessibility.AccessibilityManager;
import android.widget.Checkable;
import android.widget.CompoundButton$OnCheckedChangeListener;
import android.widget.Switch;
import android.view.View;
import android.content.res.TypedArray;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.preference.R;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v7.preference.TwoStatePreference;

public class SwitchPreference extends TwoStatePreference
{
    private final Listener mListener;
    private CharSequence mSwitchOff;
    private CharSequence mSwitchOn;
    
    public SwitchPreference(final Context context) {
        this(context, null);
    }
    
    public SwitchPreference(final Context context, final AttributeSet set) {
        this(context, set, TypedArrayUtils.getAttr(context, R.attr.switchPreferenceStyle, 16843629));
    }
    
    public SwitchPreference(final Context context, final AttributeSet set, final int n) {
        this(context, set, n, 0);
    }
    
    public SwitchPreference(final Context context, final AttributeSet set, final int n, final int n2) {
        super(context, set, n, n2);
        this.mListener = new Listener();
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.SwitchPreference, n, n2);
        this.setSummaryOn(TypedArrayUtils.getString(obtainStyledAttributes, R.styleable.SwitchPreference_summaryOn, R.styleable.SwitchPreference_android_summaryOn));
        this.setSummaryOff(TypedArrayUtils.getString(obtainStyledAttributes, R.styleable.SwitchPreference_summaryOff, R.styleable.SwitchPreference_android_summaryOff));
        this.setSwitchTextOn(TypedArrayUtils.getString(obtainStyledAttributes, R.styleable.SwitchPreference_switchTextOn, R.styleable.SwitchPreference_android_switchTextOn));
        this.setSwitchTextOff(TypedArrayUtils.getString(obtainStyledAttributes, R.styleable.SwitchPreference_switchTextOff, R.styleable.SwitchPreference_android_switchTextOff));
        this.setDisableDependentsState(TypedArrayUtils.getBoolean(obtainStyledAttributes, R.styleable.SwitchPreference_disableDependentsState, R.styleable.SwitchPreference_android_disableDependentsState, false));
        obtainStyledAttributes.recycle();
    }
    
    private void syncSwitchView(final View view) {
        final boolean b = view instanceof Switch;
        if (b) {
            ((Switch)view).setOnCheckedChangeListener((CompoundButton$OnCheckedChangeListener)null);
        }
        if (view instanceof Checkable) {
            ((Checkable)view).setChecked(this.mChecked);
        }
        if (b) {
            final Switch switch1 = (Switch)view;
            switch1.setTextOn(this.mSwitchOn);
            switch1.setTextOff(this.mSwitchOff);
            switch1.setOnCheckedChangeListener((CompoundButton$OnCheckedChangeListener)this.mListener);
        }
    }
    
    private void syncViewIfAccessibilityEnabled(final View view) {
        if (!((AccessibilityManager)this.getContext().getSystemService("accessibility")).isEnabled()) {
            return;
        }
        this.syncSwitchView(view.findViewById(16908352));
        this.syncSummaryView(view.findViewById(16908304));
    }
    
    public CharSequence getSwitchTextOff() {
        return this.mSwitchOff;
    }
    
    public CharSequence getSwitchTextOn() {
        return this.mSwitchOn;
    }
    
    @Override
    public void onBindViewHolder(final PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.syncSwitchView(preferenceViewHolder.findViewById(16908352));
        this.syncSummaryView(preferenceViewHolder);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    @Override
    protected void performClick(final View view) {
        super.performClick(view);
        this.syncViewIfAccessibilityEnabled(view);
    }
    
    public void setSwitchTextOff(final int n) {
        this.setSwitchTextOff(this.getContext().getString(n));
    }
    
    public void setSwitchTextOff(final CharSequence mSwitchOff) {
        this.mSwitchOff = mSwitchOff;
        this.notifyChanged();
    }
    
    public void setSwitchTextOn(final int n) {
        this.setSwitchTextOn(this.getContext().getString(n));
    }
    
    public void setSwitchTextOn(final CharSequence mSwitchOn) {
        this.mSwitchOn = mSwitchOn;
        this.notifyChanged();
    }
    
    private class Listener implements CompoundButton$OnCheckedChangeListener
    {
        final SwitchPreference this$0;
        
        Listener(final SwitchPreference this$0) {
            this.this$0 = this$0;
        }
        
        public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
            if (!this.this$0.callChangeListener(b)) {
                compoundButton.setChecked(b ^ true);
                return;
            }
            this.this$0.setChecked(b);
        }
    }
}
