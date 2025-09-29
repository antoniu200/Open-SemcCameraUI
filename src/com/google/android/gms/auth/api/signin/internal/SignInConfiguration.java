package com.google.android.gms.auth.api.signin.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.auth.api.signin.EmailSignInConfig;
import com.google.android.gms.auth.api.signin.FacebookSignInConfig;
import com.google.android.gms.auth.api.signin.GoogleSignInConfig;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class SignInConfiguration implements SafeParcelable {
    public static final Parcelable.Creator<SignInConfiguration> CREATOR = new zzh();
    private static int zzTr = 31;
    final int versionCode;
    private String zzTl;
    private final String zzTs;
    private EmailSignInConfig zzTt;
    private GoogleSignInConfig zzTu;
    private FacebookSignInConfig zzTv;
    private String zzTw;

    SignInConfiguration(int i, String str, String str2, EmailSignInConfig emailSignInConfig, GoogleSignInConfig googleSignInConfig, FacebookSignInConfig facebookSignInConfig, String str3) {
        this.versionCode = i;
        this.zzTs = zzx.zzcr(str);
        this.zzTl = str2;
        this.zzTt = emailSignInConfig;
        this.zzTu = googleSignInConfig;
        this.zzTv = facebookSignInConfig;
        this.zzTw = str3;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            SignInConfiguration other = (SignInConfiguration) obj;

            // zzTs vs other.zzme()
            if (!this.zzTs.equals(other.zzme())) {
                return false;
            }

            // zzTl vs other.zzmb() (treat empty == empty)
            if (android.text.TextUtils.isEmpty(this.zzTl)) {
                if (!android.text.TextUtils.isEmpty(other.zzmb())) {
                    return false;
                }
            } else if (!this.zzTl.equals(other.zzmb())) {
                return false;
            }

            // zzTw vs other.zzmi() (treat empty == empty)
            if (android.text.TextUtils.isEmpty(this.zzTw)) {
                if (!android.text.TextUtils.isEmpty(other.zzmi())) {
                    return false;
                }
            } else if (!this.zzTw.equals(other.zzmi())) {
                return false;
            }

            // zzTt (EmailSignInConfig) vs other.zzmf()
            if (this.zzTt == null) {
                if (other.zzmf() != null) {
                    return false;
                }
            } else if (!this.zzTt.equals(other.zzmf())) {
                return false;
            }

            // zzTv (FacebookSignInConfig) vs other.zzmh()
            if (this.zzTv == null) {
                if (other.zzmh() != null) {
                    return false;
                }
            } else if (!this.zzTv.equals(other.zzmh())) {
                return false;
            }

            // zzTu (GoogleSignInConfig) vs other.zzmg()
            if (this.zzTu == null) {
                if (other.zzmg() != null) {
                    return false;
                }
            } else if (!this.zzTu.equals(other.zzmg())) {
                return false;
            }

            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }

    private static String normalizeEmpty(String s) {
        return android.text.TextUtils.isEmpty(s) ? null : s;
    }

    @Override
    public int hashCode() {
        final String tl = normalizeEmpty(this.zzTl);
        final String tw = normalizeEmpty(this.zzTw);

        int result = (this.zzTs != null ? this.zzTs.hashCode() : 0);
        result = 31 * result + (tl != null ? tl.hashCode() : 0);
        result = 31 * result + (tw != null ? tw.hashCode() : 0);
        result = 31 * result + (this.zzTt != null ? this.zzTt.hashCode() : 0);
        result = 31 * result + (this.zzTu != null ? this.zzTu.hashCode() : 0);
        result = 31 * result + (this.zzTv != null ? this.zzTv.hashCode() : 0);
        return result;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        zzh.zza(this, parcel, i);
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
