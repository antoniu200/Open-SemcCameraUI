package com.google.android.gms.auth.api.signin;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzmn;
import com.google.android.gms.internal.zzmp;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class GoogleSignInAccount implements SafeParcelable {
    public static final Parcelable.Creator<GoogleSignInAccount> CREATOR = new zzc();
    public static zzmn zzSY = zzmp.zzqt();
    final int versionCode;
    private String zzSZ;
    private String zzSs;
    private String zzTa;
    private Uri zzTb;
    private String zzTc;
    private long zzTd;
    private String zzwN;

    GoogleSignInAccount(int i, String str, String str2, String str3, String str4, Uri uri, String str5, long j) {
        this.versionCode = i;
        this.zzwN = zzx.zzcr(str);
        this.zzSs = str2;
        this.zzSZ = str3;
        this.zzTa = str4;
        this.zzTb = uri;
        this.zzTc = str5;
        this.zzTd = j;
    }

    private JSONObject zzlX() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("id", getId());
            if (getIdToken() != null) {
                jSONObject.put("tokenId", getIdToken());
            }
            if (getEmail() != null) {
                jSONObject.put("email", getEmail());
            }
            if (getDisplayName() != null) {
                jSONObject.put("displayName", getDisplayName());
            }
            if (zzlT() != null) {
                jSONObject.put("photoUrl", zzlT().toString());
            }
            if (zzlU() != null) {
                jSONObject.put("serverAuthCode", zzlU());
            }
            jSONObject.put("expirationTime", this.zzTd);
            return jSONObject;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj instanceof GoogleSignInAccount) {
            return ((GoogleSignInAccount) obj).zzlW().equals(zzlW());
        }
        return false;
    }

    public String getDisplayName() {
        return this.zzTa;
    }

    public String getEmail() {
        return this.zzSZ;
    }

    public String getId() {
        return this.zzwN;
    }

    public String getIdToken() {
        return this.zzSs;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        zzc.zza(this, parcel, i);
    }

    public Uri zzlT() {
        return this.zzTb;
    }

    public String zzlU() {
        return this.zzTc;
    }

    public long zzlV() {
        return this.zzTd;
    }

    public String zzlW() {
        return zzlX().toString();
    }
}
