package com.google.android.gms.signin.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class CheckServerAuthResult implements SafeParcelable {
    public static final Parcelable.Creator<CheckServerAuthResult> CREATOR = new zzc();
    final int mVersionCode;
    final boolean zzaVi;
    final List<Scope> zzaVj;

    CheckServerAuthResult(int i, boolean z, List<Scope> list) {
        this.mVersionCode = i;
        this.zzaVi = z;
        this.zzaVj = list;
    }

    public CheckServerAuthResult(boolean z, Set<Scope> set) {
        this(1, z, zze(set));
    }

    private static List<Scope> zze(Set<Scope> set) {
        return set == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList(set));
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        zzc.zza(this, parcel, i);
    }
}
