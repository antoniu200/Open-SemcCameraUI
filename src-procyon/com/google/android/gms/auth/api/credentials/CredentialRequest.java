// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.api.credentials;

import android.os.Parcel;
import com.google.android.gms.common.internal.zzx;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class CredentialRequest implements SafeParcelable
{
    public static final Parcelable$Creator<CredentialRequest> CREATOR;
    final int mVersionCode;
    private final boolean zzSo;
    private final String[] zzSp;
    private final CredentialPickerConfig zzSq;
    private final CredentialPickerConfig zzSr;
    
    static {
        CREATOR = (Parcelable$Creator)new zzc();
    }
    
    CredentialRequest(final int mVersionCode, final boolean zzSo, final String[] array, final CredentialPickerConfig credentialPickerConfig, final CredentialPickerConfig credentialPickerConfig2) {
        this.mVersionCode = mVersionCode;
        this.zzSo = zzSo;
        this.zzSp = zzx.zzw(array);
        CredentialPickerConfig build = credentialPickerConfig;
        if (credentialPickerConfig == null) {
            build = new CredentialPickerConfig.Builder().build();
        }
        this.zzSq = build;
        CredentialPickerConfig build2;
        if ((build2 = credentialPickerConfig2) == null) {
            build2 = new CredentialPickerConfig.Builder().build();
        }
        this.zzSr = build2;
    }
    
    private CredentialRequest(final Builder builder) {
        this(2, builder.zzSo, builder.zzSp, builder.zzSq, builder.zzSr);
    }
    
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
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zzc.zza(this, parcel, n);
    }
    
    public static final class Builder
    {
        private boolean zzSo;
        private String[] zzSp;
        private CredentialPickerConfig zzSq;
        private CredentialPickerConfig zzSr;
        
        public CredentialRequest build() {
            if (this.zzSp == null) {
                this.zzSp = new String[0];
            }
            if (!this.zzSo && this.zzSp.length == 0) {
                throw new IllegalStateException("At least one authentication method must be specified");
            }
            return new CredentialRequest(this, null);
        }
        
        public Builder setAccountTypes(final String... zzSp) {
            this.zzSp = zzSp;
            return this;
        }
        
        public Builder setCredentialHintPickerConfig(final CredentialPickerConfig zzSr) {
            this.zzSr = zzSr;
            return this;
        }
        
        public Builder setCredentialPickerConfig(final CredentialPickerConfig zzSq) {
            this.zzSq = zzSq;
            return this;
        }
        
        public Builder setSupportsPasswordLogin(final boolean zzSo) {
            this.zzSo = zzSo;
            return this;
        }
    }
}
