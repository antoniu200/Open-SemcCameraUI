// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.api.signin.internal;

import android.os.Parcel;
import android.text.TextUtils;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.auth.api.signin.FacebookSignInConfig;
import com.google.android.gms.auth.api.signin.GoogleSignInConfig;
import com.google.android.gms.auth.api.signin.EmailSignInConfig;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class SignInConfiguration implements SafeParcelable
{
    public static final Parcelable$Creator<SignInConfiguration> CREATOR;
    private static int zzTr = 31;
    final int versionCode;
    private String zzTl;
    private final String zzTs;
    private EmailSignInConfig zzTt;
    private GoogleSignInConfig zzTu;
    private FacebookSignInConfig zzTv;
    private String zzTw;
    
    static {
        CREATOR = (Parcelable$Creator)new zzh();
    }
    
    SignInConfiguration(final int versionCode, final String s, final String zzTl, final EmailSignInConfig zzTt, final GoogleSignInConfig zzTu, final FacebookSignInConfig zzTv, final String zzTw) {
        this.versionCode = versionCode;
        this.zzTs = zzx.zzcr(s);
        this.zzTl = zzTl;
        this.zzTt = zzTt;
        this.zzTu = zzTu;
        this.zzTv = zzTv;
        this.zzTw = zzTw;
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
            final SignInConfiguration signInConfiguration = (SignInConfiguration)o;
            boolean b2 = b;
            if (this.zzTs.equals(signInConfiguration.zzme())) {
                if (TextUtils.isEmpty((CharSequence)this.zzTl)) {
                    b2 = b;
                    if (!TextUtils.isEmpty((CharSequence)signInConfiguration.zzmb())) {
                        return b2;
                    }
                }
                else {
                    b2 = b;
                    if (!this.zzTl.equals(signInConfiguration.zzmb())) {
                        return b2;
                    }
                }
                if (TextUtils.isEmpty((CharSequence)this.zzTw)) {
                    b2 = b;
                    if (!TextUtils.isEmpty((CharSequence)signInConfiguration.zzmi())) {
                        return b2;
                    }
                }
                else {
                    b2 = b;
                    if (!this.zzTw.equals(signInConfiguration.zzmi())) {
                        return b2;
                    }
                }
                if (this.zzTt == null) {
                    b2 = b;
                    if (signInConfiguration.zzmf() != null) {
                        return b2;
                    }
                }
                else {
                    b2 = b;
                    if (!this.zzTt.equals(signInConfiguration.zzmf())) {
                        return b2;
                    }
                }
                if (this.zzTv == null) {
                    b2 = b;
                    if (signInConfiguration.zzmh() != null) {
                        return b2;
                    }
                }
                else {
                    b2 = b;
                    if (!this.zzTv.equals(signInConfiguration.zzmh())) {
                        return b2;
                    }
                }
                if (this.zzTu == null) {
                    b2 = b;
                    if (signInConfiguration.zzmg() != null) {
                        return b2;
                    }
                }
                else {
                    final boolean equals = this.zzTu.equals(signInConfiguration.zzmg());
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
        return new zzc().zzl(this.zzTs).zzl(this.zzTl).zzl(this.zzTw).zzl(this.zzTt).zzl(this.zzTu).zzl(this.zzTv).zzmd();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zzh.zza(this, parcel, n);
    }
    
    public String zzmb() {
        return this.zzTl;
    }
    
    public String zzme() {
        return this.zzTs;
    }
    
    public EmailSignInConfig zzmf() {
        return this.zzTt;
    }
    
    public GoogleSignInConfig zzmg() {
        return this.zzTu;
    }
    
    public FacebookSignInConfig zzmh() {
        return this.zzTv;
    }
    
    public String zzmi() {
        return this.zzTw;
    }
}
