// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.internal;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.os.RemoteException;
import android.accounts.Account;
import android.os.IInterface;

public interface zzp extends IInterface
{
    Account getAccount() throws RemoteException;
    
    public abstract static class zza extends Binder implements zzp
    {
        public static zzp zzaH(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.common.internal.IAccountAccessor");
            if (queryLocalInterface != null && queryLocalInterface instanceof zzp) {
                return (zzp)queryLocalInterface;
            }
            return new zzp.zza.zza(binder);
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n != 2) {
                if (n != 1598968902) {
                    return super.onTransact(n, parcel, parcel2, n2);
                }
                parcel2.writeString("com.google.android.gms.common.internal.IAccountAccessor");
                return true;
            }
            else {
                parcel.enforceInterface("com.google.android.gms.common.internal.IAccountAccessor");
                final Account account = this.getAccount();
                parcel2.writeNoException();
                if (account != null) {
                    parcel2.writeInt(1);
                    account.writeToParcel(parcel2, 1);
                    return true;
                }
                parcel2.writeInt(0);
                return true;
            }
        }
        
        private static class zza implements zzp
        {
            private IBinder zznJ;
            
            zza(final IBinder zznJ) {
                this.zznJ = zznJ;
            }
            
            public IBinder asBinder() {
                return this.zznJ;
            }
            
            @Override
            public Account getAccount() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IAccountAccessor");
                    this.zznJ.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    Account account;
                    if (obtain2.readInt() != 0) {
                        account = (Account)Account.CREATOR.createFromParcel(obtain2);
                    }
                    else {
                        account = null;
                    }
                    return account;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
