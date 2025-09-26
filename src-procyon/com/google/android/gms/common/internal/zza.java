// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.internal;

import com.google.android.gms.common.GooglePlayServicesUtil;
import android.os.RemoteException;
import android.util.Log;
import android.os.Binder;
import android.accounts.Account;
import android.content.Context;

public class zza extends zzp.zza
{
    private Context mContext;
    private Account zzQd;
    int zzaeG;
    
    public static Account zzb(final zzp zzp) {
        if (zzp != null) {
            final long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                try {
                    final Account account = zzp.getAccount();
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                    return account;
                }
                finally {}
            }
            catch (final RemoteException ex) {
                Log.w("AccountAccessor", "Remote account accessor probably died");
                Binder.restoreCallingIdentity(clearCallingIdentity);
                return null;
            }
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
        return null;
    }
    
    public boolean equals(final Object o) {
        return this == o || (o instanceof zza && this.zzQd.equals((Object)((zza)o).zzQd));
    }
    
    public Account getAccount() {
        final int callingUid = Binder.getCallingUid();
        if (callingUid == this.zzaeG) {
            return this.zzQd;
        }
        if (GooglePlayServicesUtil.zze(this.mContext, callingUid)) {
            this.zzaeG = callingUid;
            return this.zzQd;
        }
        throw new SecurityException("Caller is not GooglePlayServices");
    }
}
