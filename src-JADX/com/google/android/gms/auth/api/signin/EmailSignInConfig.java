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

    /* JADX WARN: Removed duplicated region for block: B:16:0x0031 A[Catch: ClassCastException -> 0x0049, TryCatch #0 {ClassCastException -> 0x0049, blocks: (B:5:0x0004, B:7:0x0012, B:9:0x0016, B:14:0x0029, B:16:0x0031, B:19:0x003c, B:12:0x001d), top: B:25:0x0004 }] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x003c A[Catch: ClassCastException -> 0x0049, TRY_LEAVE, TryCatch #0 {ClassCastException -> 0x0049, blocks: (B:5:0x0004, B:7:0x0012, B:9:0x0016, B:14:0x0029, B:16:0x0031, B:19:0x003c, B:12:0x001d), top: B:25:0x0004 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean equals(java.lang.Object r4) {
        /*
            r3 = this;
            r0 = 0
            if (r4 != 0) goto L4
            return r0
        L4:
            com.google.android.gms.auth.api.signin.EmailSignInConfig r4 = (com.google.android.gms.auth.api.signin.EmailSignInConfig) r4     // Catch: java.lang.ClassCastException -> L49
            android.net.Uri r1 = r3.zzSU     // Catch: java.lang.ClassCastException -> L49
            android.net.Uri r2 = r4.zzlO()     // Catch: java.lang.ClassCastException -> L49
            boolean r1 = r1.equals(r2)     // Catch: java.lang.ClassCastException -> L49
            if (r1 == 0) goto L49
            android.net.Uri r1 = r3.zzSW     // Catch: java.lang.ClassCastException -> L49
            if (r1 != 0) goto L1d
            android.net.Uri r1 = r4.zzlP()     // Catch: java.lang.ClassCastException -> L49
            if (r1 != 0) goto L49
            goto L29
        L1d:
            android.net.Uri r1 = r3.zzSW     // Catch: java.lang.ClassCastException -> L49
            android.net.Uri r2 = r4.zzlP()     // Catch: java.lang.ClassCastException -> L49
            boolean r1 = r1.equals(r2)     // Catch: java.lang.ClassCastException -> L49
            if (r1 == 0) goto L49
        L29:
            java.lang.String r1 = r3.zzSV     // Catch: java.lang.ClassCastException -> L49
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch: java.lang.ClassCastException -> L49
            if (r1 == 0) goto L3c
            java.lang.String r3 = r4.zzlQ()     // Catch: java.lang.ClassCastException -> L49
            boolean r3 = android.text.TextUtils.isEmpty(r3)     // Catch: java.lang.ClassCastException -> L49
            if (r3 == 0) goto L49
            goto L48
        L3c:
            java.lang.String r3 = r3.zzSV     // Catch: java.lang.ClassCastException -> L49
            java.lang.String r4 = r4.zzlQ()     // Catch: java.lang.ClassCastException -> L49
            boolean r3 = r3.equals(r4)     // Catch: java.lang.ClassCastException -> L49
            if (r3 == 0) goto L49
        L48:
            r0 = 1
        L49:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.auth.api.signin.EmailSignInConfig.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        return new com.google.android.gms.auth.api.signin.internal.zzc().zzl(this.zzSU).zzl(this.zzSW).zzl(this.zzSV).zzmd();
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
