// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.os.RemoteException;
import android.accounts.Account;
import android.os.IInterface;

public interface zzkd extends IInterface
{
    void zza(final Account p0, final int p1, final zzkc p2) throws RemoteException;
    
    public abstract static class zza extends Binder implements zzkd
    {
        public static zzkd zzao(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.auth.api.accountstatus.internal.IAccountStatusService");
            if (queryLocalInterface != null && queryLocalInterface instanceof zzkd) {
                return (zzkd)queryLocalInterface;
            }
            return new zzkd.zza.zza(binder);
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n == 1) {
                parcel.enforceInterface("com.google.android.gms.auth.api.accountstatus.internal.IAccountStatusService");
                Account account;
                if (parcel.readInt() != 0) {
                    account = (Account)Account.CREATOR.createFromParcel(parcel);
                }
                else {
                    account = null;
                }
                this.zza(account, parcel.readInt(), zzkc.zza.zzan(parcel.readStrongBinder()));
                parcel2.writeNoException();
                return true;
            }
            if (n != 1598968902) {
                return super.onTransact(n, parcel, parcel2, n2);
            }
            parcel2.writeString("com.google.android.gms.auth.api.accountstatus.internal.IAccountStatusService");
            return true;
        }
        
        private static class zza implements zzkd
        {
            private IBinder zznJ;
            
            zza(final IBinder zznJ) {
                this.zznJ = zznJ;
            }
            
            public IBinder asBinder() {
                return this.zznJ;
            }
            
            @Override
            public void zza(final Account account, final int n, final zzkc zzkc) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.accountstatus.internal.IAccountStatusService");
                    if (account != null) {
                        obtain.writeInt(1);
                        account.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(n);
                    IBinder binder;
                    if (zzkc != null) {
                        binder = zzkc.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    this.zznJ.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
