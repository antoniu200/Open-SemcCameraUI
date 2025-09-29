package android.support.v7.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.preference.Preference;
import android.util.AttributeSet;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class TwoStatePreference extends Preference {
    protected boolean mChecked;
    private boolean mCheckedSet;
    private boolean mDisableDependentsState;
    private CharSequence mSummaryOff;
    private CharSequence mSummaryOn;

    public TwoStatePreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public TwoStatePreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public TwoStatePreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TwoStatePreference(Context context) {
        this(context, null);
    }

    @Override // android.support.v7.preference.Preference
    protected void onClick() {
        super.onClick();
        boolean z = !isChecked();
        if (callChangeListener(Boolean.valueOf(z))) {
            setChecked(z);
        }
    }

    public void setChecked(boolean z) {
        boolean z2 = this.mChecked != z;
        if (z2 || !this.mCheckedSet) {
            this.mChecked = z;
            this.mCheckedSet = true;
            persistBoolean(z);
            if (z2) {
                notifyDependencyChange(shouldDisableDependents());
                notifyChanged();
            }
        }
    }

    public boolean isChecked() {
        return this.mChecked;
    }

    @Override // android.support.v7.preference.Preference
    public boolean shouldDisableDependents() {
        return (this.mDisableDependentsState ? this.mChecked : !this.mChecked) || super.shouldDisableDependents();
    }

    public void setSummaryOn(CharSequence charSequence) {
        this.mSummaryOn = charSequence;
        if (isChecked()) {
            notifyChanged();
        }
    }

    public void setSummaryOn(int i) {
        setSummaryOn(getContext().getString(i));
    }

    public CharSequence getSummaryOn() {
        return this.mSummaryOn;
    }

    public void setSummaryOff(CharSequence charSequence) {
        this.mSummaryOff = charSequence;
        if (isChecked()) {
            return;
        }
        notifyChanged();
    }

    public void setSummaryOff(int i) {
        setSummaryOff(getContext().getString(i));
    }

    public CharSequence getSummaryOff() {
        return this.mSummaryOff;
    }

    public boolean getDisableDependentsState() {
        return this.mDisableDependentsState;
    }

    public void setDisableDependentsState(boolean z) {
        this.mDisableDependentsState = z;
    }

    @Override // android.support.v7.preference.Preference
    protected Object onGetDefaultValue(TypedArray typedArray, int i) {
        return Boolean.valueOf(typedArray.getBoolean(i, false));
    }

    @Override // android.support.v7.preference.Preference
    protected void onSetInitialValue(Object obj) {
        if (obj == null) {
            obj = false;
        }
        setChecked(getPersistedBoolean(((Boolean) obj).booleanValue()));
    }

    protected void syncSummaryView(PreferenceViewHolder preferenceViewHolder) {
        syncSummaryView(preferenceViewHolder.findViewById(android.R.id.summary));
    }

    @android.support.annotation.RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
    protected void syncSummaryView(View view) {
        // Sync the summary view
        TextView summaryView = (TextView) view.findViewById(com.android.internal.R.id.summary);
        if (summaryView != null) {
            boolean useDefaultSummary = true;
            if (mChecked && !TextUtils.isEmpty(mSummaryOn)) {
                summaryView.setText(mSummaryOn);
                useDefaultSummary = false;
            } else if (!mChecked && !TextUtils.isEmpty(mSummaryOff)) {
                summaryView.setText(mSummaryOff);
                useDefaultSummary = false;
            }
            if (useDefaultSummary) {
                final CharSequence summary = getSummary();
                if (!TextUtils.isEmpty(summary)) {
                    summaryView.setText(summary);
                    useDefaultSummary = false;
                }
            }
            int newVisibility = View.GONE;
            if (!useDefaultSummary) {
                // Someone has written to it
                newVisibility = View.VISIBLE;
            }
            if (newVisibility != summaryView.getVisibility()) {
                summaryView.setVisibility(newVisibility);
            }
        }
    }

    @Override // android.support.v7.preference.Preference
    protected Parcelable onSaveInstanceState() {
        Parcelable parcelableOnSaveInstanceState = super.onSaveInstanceState();
        if (isPersistent()) {
            return parcelableOnSaveInstanceState;
        }
        SavedState savedState = new SavedState(parcelableOnSaveInstanceState);
        savedState.checked = isChecked();
        return savedState;
    }

    @Override // android.support.v7.preference.Preference
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable == null || !parcelable.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        setChecked(savedState.checked);
    }

    static class SavedState extends Preference.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: android.support.v7.preference.TwoStatePreference.SavedState.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        boolean checked;

        public SavedState(Parcel parcel) {
            super(parcel);
            this.checked = parcel.readInt() == 1;
        }

        @Override // android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.checked ? 1 : 0);
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }
    }
}
