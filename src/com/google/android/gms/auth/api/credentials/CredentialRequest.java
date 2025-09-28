package com.google.android.gms.auth.api.credentials;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.auth.api.credentials.CredentialPickerConfig;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class CredentialRequest implements SafeParcelable {
    public static final Parcelable.Creator<CredentialRequest> CREATOR = new zzc();
    final int mVersionCode;
    private final boolean zzSo;
    private final String[] zzSp;
    private final CredentialPickerConfig zzSq;
    private final CredentialPickerConfig zzSr;

    public static final class Builder {
        private boolean zzSo;
        private String[] zzSp;
        private CredentialPickerConfig zzSq;
        private CredentialPickerConfig zzSr;

        public CredentialRequest build() {
            if (this.zzSp == null) {
                this.zzSp = new String[0];
            }
            if (this.zzSo || this.zzSp.length != 0) {
                return new CredentialRequest(this);
            }
            throw new IllegalStateException("At least one authentication method must be specified");
        }

        public Builder setAccountTypes(String... strArr) {
            this.zzSp = strArr;
            return this;
        }

        public Builder setCredentialHintPickerConfig(CredentialPickerConfig credentialPickerConfig) {
            this.zzSr = credentialPickerConfig;
            return this;
        }

        public Builder setCredentialPickerConfig(CredentialPickerConfig credentialPickerConfig) {
            this.zzSq = credentialPickerConfig;
            return this;
        }

        public Builder setSupportsPasswordLogin(boolean z) {
            this.zzSo = z;
            return this;
        }
    }

    CredentialRequest(int i, boolean z, String[] strArr, CredentialPickerConfig credentialPickerConfig, CredentialPickerConfig credentialPickerConfig2) {
        this.mVersionCode = i;
        this.zzSo = z;
        this.zzSp = (String[]) zzx.zzw(strArr);
        this.zzSq = credentialPickerConfig == null ? new CredentialPickerConfig.Builder().build() : credentialPickerConfig;
        this.zzSr = credentialPickerConfig2 == null ? new CredentialPickerConfig.Builder().build() : credentialPickerConfig2;
    }

    private CredentialRequest(Builder builder) {
        this(2, builder.zzSo, builder.zzSp, builder.zzSq, builder.zzSr);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String[] getAccountTypes() {
        return this.zzSp;
    }

    public CredentialPickerConfig getCredentialHintPickerConfig() {
        return this.zzSr;
    }

    public CredentialPickerConfig getCredentialPickerConfig() {
        return this.zzSq;
    }

    public boolean getSupportsPasswordLogin() {
        return this.zzSo;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        zzc.zza(this, parcel, i);
    }
}
