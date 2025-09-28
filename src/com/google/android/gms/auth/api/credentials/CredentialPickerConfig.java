package com.google.android.gms.auth.api.credentials;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class CredentialPickerConfig implements SafeParcelable {
    public static final Parcelable.Creator<CredentialPickerConfig> CREATOR = new zzb();
    private final boolean mShowCancelButton;
    final int mVersionCode;
    private final boolean zzSn;

    public static class Builder {
        private boolean zzSn = false;
        private boolean mShowCancelButton = true;

        public CredentialPickerConfig build() {
            return new CredentialPickerConfig(this);
        }

        public Builder setShowAddAccountButton(boolean z) {
            this.zzSn = z;
            return this;
        }

        public Builder setShowCancelButton(boolean z) {
            this.mShowCancelButton = z;
            return this;
        }
    }

    CredentialPickerConfig(int i, boolean z, boolean z2) {
        this.mVersionCode = i;
        this.zzSn = z;
        this.mShowCancelButton = z2;
    }

    private CredentialPickerConfig(Builder builder) {
        this(1, builder.zzSn, builder.mShowCancelButton);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean shouldShowAddAccountButton() {
        return this.zzSn;
    }

    public boolean shouldShowCancelButton() {
        return this.mShowCancelButton;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        zzb.zza(this, parcel, i);
    }
}
