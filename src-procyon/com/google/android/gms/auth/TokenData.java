// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth;

import android.os.Parcel;
import com.google.android.gms.common.internal.zzw;
import android.text.TextUtils;
import android.os.Bundle;
import com.google.android.gms.common.internal.zzx;
import java.util.List;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class TokenData implements SafeParcelable
{
    public static final zzd CREATOR;
    final int mVersionCode;
    private final Long zzRA;
    private final boolean zzRB;
    private final boolean zzRC;
    private final List<String> zzRD;
    private final String zzRz;
    
    static {
        CREATOR = new zzd();
    }
    
    TokenData(final int mVersionCode, final String s, final Long zzRA, final boolean zzRB, final boolean zzRC, final List<String> zzRD) {
        this.mVersionCode = mVersionCode;
        this.zzRz = zzx.zzcr(s);
        this.zzRA = zzRA;
        this.zzRB = zzRB;
        this.zzRC = zzRC;
        this.zzRD = zzRD;
    }
    
    public static TokenData zza(Bundle bundle, final String s) {
        bundle.setClassLoader(TokenData.class.getClassLoader());
        bundle = bundle.getBundle(s);
        if (bundle == null) {
            return null;
        }
        bundle.setClassLoader(TokenData.class.getClassLoader());
        return (TokenData)bundle.getParcelable("TokenData");
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof TokenData;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final TokenData tokenData = (TokenData)o;
        boolean b3 = b2;
        if (TextUtils.equals((CharSequence)this.zzRz, (CharSequence)tokenData.zzRz)) {
            b3 = b2;
            if (zzw.equal(this.zzRA, tokenData.zzRA)) {
                b3 = b2;
                if (this.zzRB == tokenData.zzRB) {
                    b3 = b2;
                    if (this.zzRC == tokenData.zzRC) {
                        b3 = b2;
                        if (zzw.equal(this.zzRD, tokenData.zzRD)) {
                            b3 = true;
                        }
                    }
                }
            }
        }
        return b3;
    }
    
    public String getToken() {
        return this.zzRz;
    }
    
    @Override
    public int hashCode() {
        return zzw.hashCode(this.zzRz, this.zzRA, this.zzRB, this.zzRC, this.zzRD);
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zzd.zza(this, parcel, n);
    }
    
    public Long zzlA() {
        return this.zzRA;
    }
    
    public boolean zzlB() {
        return this.zzRB;
    }
    
    public boolean zzlC() {
        return this.zzRC;
    }
    
    public List<String> zzlD() {
        return this.zzRD;
    }
}
