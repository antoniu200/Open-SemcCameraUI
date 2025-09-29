package com.google.android.gms.auth.api.signin;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Patterns;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class EmailSignInConfig implements SafeParcelable {
    public static final Parcelable.Creator<EmailSignInConfig> CREATOR = new zza();
    final int versionCode;
    private final Uri zzSU;
    private String zzSV;
    private Uri zzSW;

    EmailSignInConfig(int i, Uri uri, String str, Uri uri2) {
        zzx.zzb(uri, "Server widget url cannot be null in order to use email/password sign in.");
        zzx.zzh(uri.toString(), "Server widget url cannot be null in order to use email/password sign in.");
        zzx.zzb(Patterns.WEB_URL.matcher(uri.toString()).matches(), "Invalid server widget url");
        this.versionCode = i;
        this.zzSU = uri;
        this.zzSV = str;
        this.zzSW = uri2;
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
            EmailSignInConfig other = (EmailSignInConfig) obj;

            // Required URI must match
            if (!this.zzSU.equals(other.zzlO())) {
                return false;
            }

            // Optional URI: null-safe equal
            if (this.zzSW == null) {
                if (other.zzlP() != null) {
                    return false;
                }
            } else if (!this.zzSW.equals(other.zzlP())) {
                return false;
            }

            // String field: treat empty == empty
            if (android.text.TextUtils.isEmpty(this.zzSV)) {
                if (!android.text.TextUtils.isEmpty(other.zzlQ())) {
                    return false;
                }
            } else if (!this.zzSV.equals(other.zzlQ())) {
                return false;
            }

            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = (this.zzSU != null ? this.zzSU.hashCode() : 0);
        result = 31 * result + (this.zzSW != null ? this.zzSW.hashCode() : 0);
        final String sv = nz(this.zzSV);
        result = 31 * result + (sv != null ? sv.hashCode() : 0);
        return result;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        zza.zza(this, parcel, i);
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
