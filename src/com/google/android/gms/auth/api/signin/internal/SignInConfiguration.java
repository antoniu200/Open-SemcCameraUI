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

    /* JADX WARN: Removed duplicated region for block: B:16:0x0039 A[Catch: ClassCastException -> 0x0096, TryCatch #0 {ClassCastException -> 0x0096, blocks: (B:5:0x0004, B:7:0x0012, B:9:0x001a, B:14:0x0031, B:16:0x0039, B:21:0x0050, B:23:0x0054, B:28:0x0067, B:30:0x006b, B:35:0x007e, B:37:0x0082, B:40:0x0089, B:33:0x0072, B:26:0x005b, B:19:0x0044, B:12:0x0025), top: B:46:0x0004 }] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0044 A[Catch: ClassCastException -> 0x0096, TryCatch #0 {ClassCastException -> 0x0096, blocks: (B:5:0x0004, B:7:0x0012, B:9:0x001a, B:14:0x0031, B:16:0x0039, B:21:0x0050, B:23:0x0054, B:28:0x0067, B:30:0x006b, B:35:0x007e, B:37:0x0082, B:40:0x0089, B:33:0x0072, B:26:0x005b, B:19:0x0044, B:12:0x0025), top: B:46:0x0004 }] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0054 A[Catch: ClassCastException -> 0x0096, TryCatch #0 {ClassCastException -> 0x0096, blocks: (B:5:0x0004, B:7:0x0012, B:9:0x001a, B:14:0x0031, B:16:0x0039, B:21:0x0050, B:23:0x0054, B:28:0x0067, B:30:0x006b, B:35:0x007e, B:37:0x0082, B:40:0x0089, B:33:0x0072, B:26:0x005b, B:19:0x0044, B:12:0x0025), top: B:46:0x0004 }] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x005b A[Catch: ClassCastException -> 0x0096, TryCatch #0 {ClassCastException -> 0x0096, blocks: (B:5:0x0004, B:7:0x0012, B:9:0x001a, B:14:0x0031, B:16:0x0039, B:21:0x0050, B:23:0x0054, B:28:0x0067, B:30:0x006b, B:35:0x007e, B:37:0x0082, B:40:0x0089, B:33:0x0072, B:26:0x005b, B:19:0x0044, B:12:0x0025), top: B:46:0x0004 }] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x006b A[Catch: ClassCastException -> 0x0096, TryCatch #0 {ClassCastException -> 0x0096, blocks: (B:5:0x0004, B:7:0x0012, B:9:0x001a, B:14:0x0031, B:16:0x0039, B:21:0x0050, B:23:0x0054, B:28:0x0067, B:30:0x006b, B:35:0x007e, B:37:0x0082, B:40:0x0089, B:33:0x0072, B:26:0x005b, B:19:0x0044, B:12:0x0025), top: B:46:0x0004 }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0072 A[Catch: ClassCastException -> 0x0096, TryCatch #0 {ClassCastException -> 0x0096, blocks: (B:5:0x0004, B:7:0x0012, B:9:0x001a, B:14:0x0031, B:16:0x0039, B:21:0x0050, B:23:0x0054, B:28:0x0067, B:30:0x006b, B:35:0x007e, B:37:0x0082, B:40:0x0089, B:33:0x0072, B:26:0x005b, B:19:0x0044, B:12:0x0025), top: B:46:0x0004 }] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0082 A[Catch: ClassCastException -> 0x0096, TryCatch #0 {ClassCastException -> 0x0096, blocks: (B:5:0x0004, B:7:0x0012, B:9:0x001a, B:14:0x0031, B:16:0x0039, B:21:0x0050, B:23:0x0054, B:28:0x0067, B:30:0x006b, B:35:0x007e, B:37:0x0082, B:40:0x0089, B:33:0x0072, B:26:0x005b, B:19:0x0044, B:12:0x0025), top: B:46:0x0004 }] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0089 A[Catch: ClassCastException -> 0x0096, TRY_LEAVE, TryCatch #0 {ClassCastException -> 0x0096, blocks: (B:5:0x0004, B:7:0x0012, B:9:0x001a, B:14:0x0031, B:16:0x0039, B:21:0x0050, B:23:0x0054, B:28:0x0067, B:30:0x006b, B:35:0x007e, B:37:0x0082, B:40:0x0089, B:33:0x0072, B:26:0x005b, B:19:0x0044, B:12:0x0025), top: B:46:0x0004 }] */
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
            com.google.android.gms.auth.api.signin.internal.SignInConfiguration r4 = (com.google.android.gms.auth.api.signin.internal.SignInConfiguration) r4     // Catch: java.lang.ClassCastException -> L96
            java.lang.String r1 = r3.zzTs     // Catch: java.lang.ClassCastException -> L96
            java.lang.String r2 = r4.zzme()     // Catch: java.lang.ClassCastException -> L96
            boolean r1 = r1.equals(r2)     // Catch: java.lang.ClassCastException -> L96
            if (r1 == 0) goto L96
            java.lang.String r1 = r3.zzTl     // Catch: java.lang.ClassCastException -> L96
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch: java.lang.ClassCastException -> L96
            if (r1 == 0) goto L25
            java.lang.String r1 = r4.zzmb()     // Catch: java.lang.ClassCastException -> L96
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch: java.lang.ClassCastException -> L96
            if (r1 == 0) goto L96
            goto L31
        L25:
            java.lang.String r1 = r3.zzTl     // Catch: java.lang.ClassCastException -> L96
            java.lang.String r2 = r4.zzmb()     // Catch: java.lang.ClassCastException -> L96
            boolean r1 = r1.equals(r2)     // Catch: java.lang.ClassCastException -> L96
            if (r1 == 0) goto L96
        L31:
            java.lang.String r1 = r3.zzTw     // Catch: java.lang.ClassCastException -> L96
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch: java.lang.ClassCastException -> L96
            if (r1 == 0) goto L44
            java.lang.String r1 = r4.zzmi()     // Catch: java.lang.ClassCastException -> L96
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch: java.lang.ClassCastException -> L96
            if (r1 == 0) goto L96
            goto L50
        L44:
            java.lang.String r1 = r3.zzTw     // Catch: java.lang.ClassCastException -> L96
            java.lang.String r2 = r4.zzmi()     // Catch: java.lang.ClassCastException -> L96
            boolean r1 = r1.equals(r2)     // Catch: java.lang.ClassCastException -> L96
            if (r1 == 0) goto L96
        L50:
            com.google.android.gms.auth.api.signin.EmailSignInConfig r1 = r3.zzTt     // Catch: java.lang.ClassCastException -> L96
            if (r1 != 0) goto L5b
            com.google.android.gms.auth.api.signin.EmailSignInConfig r1 = r4.zzmf()     // Catch: java.lang.ClassCastException -> L96
            if (r1 != 0) goto L96
            goto L67
        L5b:
            com.google.android.gms.auth.api.signin.EmailSignInConfig r1 = r3.zzTt     // Catch: java.lang.ClassCastException -> L96
            com.google.android.gms.auth.api.signin.EmailSignInConfig r2 = r4.zzmf()     // Catch: java.lang.ClassCastException -> L96
            boolean r1 = r1.equals(r2)     // Catch: java.lang.ClassCastException -> L96
            if (r1 == 0) goto L96
        L67:
            com.google.android.gms.auth.api.signin.FacebookSignInConfig r1 = r3.zzTv     // Catch: java.lang.ClassCastException -> L96
            if (r1 != 0) goto L72
            com.google.android.gms.auth.api.signin.FacebookSignInConfig r1 = r4.zzmh()     // Catch: java.lang.ClassCastException -> L96
            if (r1 != 0) goto L96
            goto L7e
        L72:
            com.google.android.gms.auth.api.signin.FacebookSignInConfig r1 = r3.zzTv     // Catch: java.lang.ClassCastException -> L96
            com.google.android.gms.auth.api.signin.FacebookSignInConfig r2 = r4.zzmh()     // Catch: java.lang.ClassCastException -> L96
            boolean r1 = r1.equals(r2)     // Catch: java.lang.ClassCastException -> L96
            if (r1 == 0) goto L96
        L7e:
            com.google.android.gms.auth.api.signin.GoogleSignInConfig r1 = r3.zzTu     // Catch: java.lang.ClassCastException -> L96
            if (r1 != 0) goto L89
            com.google.android.gms.auth.api.signin.GoogleSignInConfig r3 = r4.zzmg()     // Catch: java.lang.ClassCastException -> L96
            if (r3 != 0) goto L96
            goto L95
        L89:
            com.google.android.gms.auth.api.signin.GoogleSignInConfig r3 = r3.zzTu     // Catch: java.lang.ClassCastException -> L96
            com.google.android.gms.auth.api.signin.GoogleSignInConfig r4 = r4.zzmg()     // Catch: java.lang.ClassCastException -> L96
            boolean r3 = r3.equals(r4)     // Catch: java.lang.ClassCastException -> L96
            if (r3 == 0) goto L96
        L95:
            r0 = 1
        L96:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.auth.api.signin.internal.SignInConfiguration.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        return new zzc().zzl(this.zzTs).zzl(this.zzTl).zzl(this.zzTw).zzl(this.zzTt).zzl(this.zzTu).zzl(this.zzTv).zzmd();
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
