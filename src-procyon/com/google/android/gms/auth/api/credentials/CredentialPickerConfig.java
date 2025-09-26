// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.api.credentials;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class CredentialPickerConfig implements SafeParcelable
{
    public static final Parcelable$Creator<CredentialPickerConfig> CREATOR;
    private final boolean mShowCancelButton;
    final int mVersionCode;
    private final boolean zzSn;
    
    static {
        CREATOR = (Parcelable$Creator)new zzb();
    }
    
    CredentialPickerConfig(final int mVersionCode, final boolean zzSn, final boolean mShowCancelButton) {
        this.mVersionCode = mVersionCode;
        this.zzSn = zzSn;
        this.mShowCancelButton = mShowCancelButton;
    }
    
    private CredentialPickerConfig(final Builder builder) {
        this(1, builder.zzSn, builder.mShowCancelButton);
    }
    
    public int describeContents() {
        return 0;
    }
    
    public boolean shouldShowAddAccountButton() {
        return this.zzSn;
    }
    
    public boolean shouldShowCancelButton() {
        return this.mShowCancelButton;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zzb.zza(this, parcel, n);
    }
    
    public static class Builder
    {
        private boolean mShowCancelButton;
        private boolean zzSn;
        
        public Builder() {
            this.zzSn = false;
            this.mShowCancelButton = true;
        }
        
        public CredentialPickerConfig build() {
            return new CredentialPickerConfig(this, null);
        }
        
        public Builder setShowAddAccountButton(final boolean zzSn) {
            this.zzSn = zzSn;
            return this;
        }
        
        public Builder setShowCancelButton(final boolean mShowCancelButton) {
            this.mShowCancelButton = mShowCancelButton;
            return this;
        }
    }
}
