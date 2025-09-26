package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.content.Context;
import android.os.Binder;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.internal.zzp;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zza extends zzp.zza {
    private Context mContext;
    private Account zzQd;
    int zzaeG;

    public static Account zzb(zzp zzpVar) {
        if (zzpVar == null) {
            return null;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return zzpVar.getAccount();
        } catch (RemoteException unused) {
            Log.w("AccountAccessor", "Remote account accessor probably died");
            return null;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof zza) {
            return this.zzQd.equals(((zza) obj).zzQd);
        }
        return false;
    }

    @Override // com.google.android.gms.common.internal.zzp
    public Account getAccount() {
        int callingUid = Binder.getCallingUid();
        if (callingUid == this.zzaeG) {
            return this.zzQd;
        }
        if (!GooglePlayServicesUtil.zze(this.mContext, callingUid)) {
            throw new SecurityException("Caller is not GooglePlayServices");
        }
        this.zzaeG = callingUid;
        return this.zzQd;
    }
}
