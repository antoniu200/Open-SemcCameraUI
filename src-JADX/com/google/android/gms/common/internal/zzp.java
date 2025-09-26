package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface zzp extends IInterface {

    public static abstract class zza extends Binder implements zzp {

        /* renamed from: com.google.android.gms.common.internal.zzp$zza$zza, reason: collision with other inner class name */
        private static class C0011zza implements zzp {
            private IBinder zznJ;

            C0011zza(IBinder iBinder) {
                this.zznJ = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.zznJ;
            }

            @Override // com.google.android.gms.common.internal.zzp
            public Account getAccount() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.common.internal.IAccountAccessor");
                    this.zznJ.transact(2, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0 ? (Account) Account.CREATOR.createFromParcel(parcelObtain2) : null;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }

        public static zzp zzaH(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.IAccountAccessor");
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof zzp)) ? new C0011zza(iBinder) : (zzp) iInterfaceQueryLocalInterface;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i != 2) {
                if (i != 1598968902) {
                    return super.onTransact(i, parcel, parcel2, i2);
                }
                parcel2.writeString("com.google.android.gms.common.internal.IAccountAccessor");
                return true;
            }
            parcel.enforceInterface("com.google.android.gms.common.internal.IAccountAccessor");
            Account account = getAccount();
            parcel2.writeNoException();
            if (account == null) {
                parcel2.writeInt(0);
                return true;
            }
            parcel2.writeInt(1);
            account.writeToParcel(parcel2, 1);
            return true;
        }
    }

    Account getAccount() throws RemoteException;
}
