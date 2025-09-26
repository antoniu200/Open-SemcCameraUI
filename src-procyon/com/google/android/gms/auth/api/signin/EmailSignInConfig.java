// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.api.signin;

import android.os.Parcel;
import com.google.android.gms.auth.api.signin.internal.zzc;
import android.text.TextUtils;
import android.util.Patterns;
import com.google.android.gms.common.internal.zzx;
import android.net.Uri;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class EmailSignInConfig implements SafeParcelable
{
    public static final Parcelable$Creator<EmailSignInConfig> CREATOR;
    final int versionCode;
    private final Uri zzSU;
    private String zzSV;
    private Uri zzSW;
    
    static {
        CREATOR = (Parcelable$Creator)new zza();
    }
    
    EmailSignInConfig(final int versionCode, final Uri zzSU, final String zzSV, final Uri zzSW) {
        zzx.zzb(zzSU, "Server widget url cannot be null in order to use email/password sign in.");
        zzx.zzh(zzSU.toString(), "Server widget url cannot be null in order to use email/password sign in.");
        zzx.zzb(Patterns.WEB_URL.matcher(zzSU.toString()).matches(), (Object)"Invalid server widget url");
        this.versionCode = versionCode;
        this.zzSU = zzSU;
        this.zzSV = zzSV;
        this.zzSW = zzSW;
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = false;
        if (o == null) {
            return false;
        }
        try {
            final EmailSignInConfig emailSignInConfig = (EmailSignInConfig)o;
            boolean b2 = b;
            if (this.zzSU.equals((Object)emailSignInConfig.zzlO())) {
                if (this.zzSW == null) {
                    b2 = b;
                    if (emailSignInConfig.zzlP() != null) {
                        return b2;
                    }
                }
                else {
                    b2 = b;
                    if (!this.zzSW.equals((Object)emailSignInConfig.zzlP())) {
                        return b2;
                    }
                }
                if (TextUtils.isEmpty((CharSequence)this.zzSV)) {
                    b2 = b;
                    if (!TextUtils.isEmpty((CharSequence)emailSignInConfig.zzlQ())) {
                        return b2;
                    }
                }
                else {
                    final boolean equals = this.zzSV.equals(emailSignInConfig.zzlQ());
                    b2 = b;
                    if (!equals) {
                        return b2;
                    }
                }
                b2 = true;
            }
            return b2;
        }
        catch (final ClassCastException ex) {
            return b;
        }
    }
    
    @Override
    public int hashCode() {
        return new zzc().zzl(this.zzSU).zzl(this.zzSW).zzl(this.zzSV).zzmd();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zza.zza(this, parcel, n);
    }
    
    public Uri zzlO() {
        return this.zzSU;
    }
    
    public Uri zzlP() {
        return this.zzSW;
    }
    
    public String zzlQ() {
        return this.zzSV;
    }
}
