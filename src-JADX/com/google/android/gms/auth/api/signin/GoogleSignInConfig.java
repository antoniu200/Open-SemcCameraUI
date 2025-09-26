package com.google.android.gms.auth.api.signin;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class GoogleSignInConfig implements Api.ApiOptions.Optional, SafeParcelable {
    final int versionCode;
    private Account zzQd;
    private final ArrayList<Scope> zzSX;
    private boolean zzTi;
    private final boolean zzTj;
    private final boolean zzTk;
    private String zzTl;
    public static final Scope zzTe = new Scope(Scopes.PROFILE);
    public static final Scope zzTf = new Scope("email");
    public static final Scope zzTg = new Scope("openid");
    public static final GoogleSignInConfig zzTh = new zza().zzmc();
    public static final Parcelable.Creator<GoogleSignInConfig> CREATOR = new zze();

    public static final class zza {
        private Account zzQd;
        private boolean zzTi;
        private boolean zzTj;
        private boolean zzTk;
        private String zzTl;
        private Set<Scope> zzTm = new HashSet(Arrays.asList(GoogleSignInConfig.zzTg));

        public GoogleSignInConfig zzmc() {
            return new GoogleSignInConfig(this.zzTm, this.zzQd, this.zzTi, this.zzTj, this.zzTk, this.zzTl);
        }
    }

    GoogleSignInConfig(int i, ArrayList<Scope> arrayList, Account account, boolean z, boolean z2, boolean z3, String str) {
        this.versionCode = i;
        this.zzSX = arrayList;
        this.zzQd = account;
        this.zzTi = z;
        this.zzTj = z2;
        this.zzTk = z3;
        this.zzTl = str;
    }

    private GoogleSignInConfig(Set<Scope> set, Account account, boolean z, boolean z2, boolean z3, String str) {
        this(1, (ArrayList<Scope>) new ArrayList(set), account, z, z2, z3, str);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            GoogleSignInConfig googleSignInConfig = (GoogleSignInConfig) obj;
            if (this.zzSX.size() != googleSignInConfig.zzlS().size() || !this.zzSX.containsAll(googleSignInConfig.zzlS())) {
                return false;
            }
            if (this.zzQd == null) {
                if (googleSignInConfig.getAccount() != null) {
                    return false;
                }
            } else if (!this.zzQd.equals(googleSignInConfig.getAccount())) {
                return false;
            }
            if (TextUtils.isEmpty(this.zzTl)) {
                if (!TextUtils.isEmpty(googleSignInConfig.zzmb())) {
                    return false;
                }
            } else if (!this.zzTl.equals(googleSignInConfig.zzmb())) {
                return false;
            }
            if (this.zzTk == googleSignInConfig.zzma() && this.zzTi == googleSignInConfig.zzlY()) {
                return this.zzTj == googleSignInConfig.zzlZ();
            }
            return false;
        } catch (ClassCastException unused) {
            return false;
        }
    }

    public Account getAccount() {
        return this.zzQd;
    }

    public int hashCode() {
        ArrayList arrayList = new ArrayList();
        Iterator<Scope> it = this.zzSX.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().zznG());
        }
        Collections.sort(arrayList);
        return new com.google.android.gms.auth.api.signin.internal.zzc().zzl(arrayList).zzl(this.zzQd).zzl(this.zzTl).zzP(this.zzTk).zzP(this.zzTi).zzP(this.zzTj).zzmd();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        zze.zza(this, parcel, i);
    }

    public ArrayList<Scope> zzlS() {
        return new ArrayList<>(this.zzSX);
    }

    public boolean zzlY() {
        return this.zzTi;
    }

    public boolean zzlZ() {
        return this.zzTj;
    }

    public boolean zzma() {
        return this.zzTk;
    }

    public String zzmb() {
        return this.zzTl;
    }
}
