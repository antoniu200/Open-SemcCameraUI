// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.firstparty.shared;

import android.os.Parcel;
import com.google.android.gms.common.internal.zzw;
import android.text.TextUtils;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class FACLConfig implements SafeParcelable
{
    public static final zza CREATOR;
    final int version;
    boolean zzTA;
    boolean zzTB;
    boolean zzTC;
    boolean zzTx;
    String zzTy;
    boolean zzTz;
    
    static {
        CREATOR = new zza();
    }
    
    FACLConfig(final int version, final boolean zzTx, final String zzTy, final boolean zzTz, final boolean zzTA, final boolean zzTB, final boolean zzTC) {
        this.version = version;
        this.zzTx = zzTx;
        this.zzTy = zzTy;
        this.zzTz = zzTz;
        this.zzTA = zzTA;
        this.zzTB = zzTB;
        this.zzTC = zzTC;
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof FACLConfig;
        boolean b3;
        final boolean b2 = b3 = false;
        if (b) {
            final FACLConfig faclConfig = (FACLConfig)o;
            b3 = b2;
            if (this.zzTx == faclConfig.zzTx) {
                b3 = b2;
                if (TextUtils.equals((CharSequence)this.zzTy, (CharSequence)faclConfig.zzTy)) {
                    b3 = b2;
                    if (this.zzTz == faclConfig.zzTz) {
                        b3 = b2;
                        if (this.zzTA == faclConfig.zzTA) {
                            b3 = b2;
                            if (this.zzTB == faclConfig.zzTB) {
                                b3 = b2;
                                if (this.zzTC == faclConfig.zzTC) {
                                    b3 = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return b3;
    }
    
    @Override
    public int hashCode() {
        return zzw.hashCode(this.zzTx, this.zzTy, this.zzTz, this.zzTA, this.zzTB, this.zzTC);
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zza.zza(this, parcel, n);
    }
}
