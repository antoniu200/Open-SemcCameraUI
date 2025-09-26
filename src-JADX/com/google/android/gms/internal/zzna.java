package com.google.android.gms.internal;

import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.zzx;
import java.util.Set;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class zzna {
    public static String[] zza(Scope[] scopeArr) {
        zzx.zzb(scopeArr, "scopes can't be null.");
        String[] strArr = new String[scopeArr.length];
        for (int i = 0; i < scopeArr.length; i++) {
            strArr[i] = scopeArr[i].zznG();
        }
        return strArr;
    }

    public static String[] zzc(Set<Scope> set) {
        zzx.zzb(set, "scopes can't be null.");
        return zza((Scope[]) set.toArray(new Scope[set.size()]));
    }
}
