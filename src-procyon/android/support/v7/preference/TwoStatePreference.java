// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.preference;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.support.annotation.RestrictTo;
import android.text.TextUtils;
import android.widget.TextView;
import android.view.View;
import android.os.Parcelable;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.content.Context;

public abstract class TwoStatePreference extends Preference
{
    protected boolean mChecked;
    private boolean mCheckedSet;
    private boolean mDisableDependentsState;
    private CharSequence mSummaryOff;
    private CharSequence mSummaryOn;
    
    public TwoStatePreference(final Context context) {
        this(context, null);
    }
    
    public TwoStatePreference(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public TwoStatePreference(final Context context, final AttributeSet set, final int n) {
        this(context, set, n, 0);
    }
    
    public TwoStatePreference(final Context context, final AttributeSet set, final int n, final int n2) {
        super(context, set, n, n2);
    }
    
    public boolean getDisableDependentsState() {
        return this.mDisableDependentsState;
    }
    
    public CharSequence getSummaryOff() {
        return this.mSummaryOff;
    }
    
    public CharSequence getSummaryOn() {
        return this.mSummaryOn;
    }
    
    public boolean isChecked() {
        return this.mChecked;
    }
    
    @Override
    protected void onClick() {
        super.onClick();
        final boolean b = this.isChecked() ^ true;
        if (this.callChangeListener(b)) {
            this.setChecked(b);
        }
    }
    
    @Override
    protected Object onGetDefaultValue(final TypedArray typedArray, final int n) {
        return typedArray.getBoolean(n, false);
    }
    
    @Override
    protected void onRestoreInstanceState(final Parcelable parcelable) {
        if (parcelable != null && parcelable.getClass().equals(SavedState.class)) {
            final SavedState savedState = (SavedState)parcelable;
            super.onRestoreInstanceState(savedState.getSuperState());
            this.setChecked(savedState.checked);
            return;
        }
        super.onRestoreInstanceState(parcelable);
    }
    
    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable onSaveInstanceState = super.onSaveInstanceState();
        if (this.isPersistent()) {
            return onSaveInstanceState;
        }
        final SavedState savedState = new SavedState(onSaveInstanceState);
        savedState.checked = this.isChecked();
        return (Parcelable)savedState;
    }
    
    @Override
    protected void onSetInitialValue(final Object o) {
        Object value = o;
        if (o == null) {
            value = false;
        }
        this.setChecked(this.getPersistedBoolean((boolean)value));
    }
    
    public void setChecked(final boolean mChecked) {
        final boolean b = this.mChecked != mChecked;
        if (b || !this.mCheckedSet) {
            this.mChecked = mChecked;
            this.mCheckedSet = true;
            this.persistBoolean(mChecked);
            if (b) {
                this.notifyDependencyChange(this.shouldDisableDependents());
                this.notifyChanged();
            }
        }
    }
    
    public void setDisableDependentsState(final boolean mDisableDependentsState) {
        this.mDisableDependentsState = mDisableDependentsState;
    }
    
    public void setSummaryOff(final int n) {
        this.setSummaryOff(this.getContext().getString(n));
    }
    
    public void setSummaryOff(final CharSequence mSummaryOff) {
        this.mSummaryOff = mSummaryOff;
        if (!this.isChecked()) {
            this.notifyChanged();
        }
    }
    
    public void setSummaryOn(final int n) {
        this.setSummaryOn(this.getContext().getString(n));
    }
    
    public void setSummaryOn(final CharSequence mSummaryOn) {
        this.mSummaryOn = mSummaryOn;
        if (this.isChecked()) {
            this.notifyChanged();
        }
    }
    
    @Override
    public boolean shouldDisableDependents() {
        final boolean mDisableDependentsState = this.mDisableDependentsState;
        final boolean b = false;
        boolean mChecked;
        if (mDisableDependentsState) {
            mChecked = this.mChecked;
        }
        else {
            mChecked = !this.mChecked;
        }
        if (!mChecked) {
            final boolean b2 = b;
            if (!super.shouldDisableDependents()) {
                return b2;
            }
        }
        return true;
    }
    
    protected void syncSummaryView(final PreferenceViewHolder preferenceViewHolder) {
        this.syncSummaryView(preferenceViewHolder.findViewById(16908304));
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    protected void syncSummaryView(final View view) {
        if (!(view instanceof TextView)) {
            return;
        }
        final TextView textView = (TextView)view;
        final int n = 1;
        int n2 = 0;
        Label_0077: {
            if (this.mChecked && !TextUtils.isEmpty(this.mSummaryOn)) {
                textView.setText(this.mSummaryOn);
            }
            else {
                n2 = n;
                if (this.mChecked) {
                    break Label_0077;
                }
                n2 = n;
                if (TextUtils.isEmpty(this.mSummaryOff)) {
                    break Label_0077;
                }
                textView.setText(this.mSummaryOff);
            }
            n2 = 0;
        }
        int n3;
        if ((n3 = n2) != 0) {
            final CharSequence summary = this.getSummary();
            n3 = n2;
            if (!TextUtils.isEmpty(summary)) {
                textView.setText(summary);
                n3 = 0;
            }
        }
        int visibility = 8;
        if (n3 == 0) {
            visibility = 0;
        }
        if (visibility != textView.getVisibility()) {
            textView.setVisibility(visibility);
        }
    }
    
    static class SavedState extends BaseSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        boolean checked;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<SavedState>() {
                public SavedState createFromParcel(final Parcel parcel) {
                    return new SavedState(parcel);
                }
                
                public SavedState[] newArray(final int n) {
                    return new SavedState[n];
                }
            };
        }
        
        public SavedState(final Parcel parcel) {
            super(parcel);
            final int int1 = parcel.readInt();
            boolean checked = true;
            if (int1 != 1) {
                checked = false;
            }
            this.checked = checked;
        }
        
        public SavedState(final Parcelable parcelable) {
            super(parcelable);
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt((int)(this.checked ? 1 : 0));
        }
    }
}
