package com.google.android.gms.internal;

import android.accounts.Account;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.zzkc;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface zzkd extends IInterface {

    public static abstract class zza extends Binder implements zzkd {

        /* renamed from: com.google.android.gms.internal.zzkd$zza$zza, reason: collision with other inner class name */
        private static class C0023zza implements zzkd {
            private IBinder zznJ;

            C0023zza(IBinder iBinder) {
                this.zznJ = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.zznJ;
            }

            @Override // com.google.android.gms.internal.zzkd
            public void zza(Account account, int i, zzkc zzkcVar) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.auth.api.accountstatus.internal.IAccountStatusService");
                    if (account != null) {
                        parcelObtain.writeInt(1);
                        account.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    parcelObtain.writeInt(i);
                    parcelObtain.writeStrongBinder(zzkcVar != null ? zzkcVar.asBinder() : null);
                    this.zznJ.transact(1, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }

        public static zzkd zzao(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.auth.api.accountstatus.internal.IAccountStatusService");
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof zzkd)) ? new C0023zza(iBinder) : (zzkd) iInterfaceQueryLocalInterface;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i != 1) {
                if (i != 1598968902) {
                    return super.onTransact(i, parcel, parcel2, i2);
                }
                parcel2.writeString("com.google.android.gms.auth.api.accountstatus.internal.IAccountStatusService");
                return true;
            }
            parcel.enforceInterface("com.google.android.gms.auth.api.accountstatus.internal.IAccountStatusService");
            zza(parcel.readInt() != 0 ? (Account) Account.CREATOR.createFromParcel(parcel) : null, parcel.readInt(), zzkc.zza.zzan(parcel.readStrongBinder()));
            parcel2.writeNoException();
            return true;
        }
    }

    void zza(Account account, int i, zzkc zzkcVar) throws RemoteException;
}
