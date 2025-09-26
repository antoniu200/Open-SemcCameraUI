package com.google.android.gms.internal;

import android.accounts.Account;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.auth.AccountChangeEventsRequest;
import com.google.android.gms.auth.AccountChangeEventsResponse;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface zzau extends IInterface {

    public static abstract class zza extends Binder implements zzau {

        /* renamed from: com.google.android.gms.internal.zzau$zza$zza, reason: collision with other inner class name */
        private static class C0021zza implements zzau {
            private IBinder zznJ;

            C0021zza(IBinder iBinder) {
                this.zznJ = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.zznJ;
            }

            @Override // com.google.android.gms.internal.zzau
            public Bundle zza(Account account) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.auth.IAuthManagerService");
                    if (account != null) {
                        parcelObtain.writeInt(1);
                        account.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    this.zznJ.transact(7, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcelObtain2) : null;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.zzau
            public Bundle zza(Account account, String str, Bundle bundle) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.auth.IAuthManagerService");
                    if (account != null) {
                        parcelObtain.writeInt(1);
                        account.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    parcelObtain.writeString(str);
                    if (bundle != null) {
                        parcelObtain.writeInt(1);
                        bundle.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    this.zznJ.transact(5, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcelObtain2) : null;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.zzau
            public Bundle zza(Bundle bundle) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.auth.IAuthManagerService");
                    if (bundle != null) {
                        parcelObtain.writeInt(1);
                        bundle.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    this.zznJ.transact(6, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcelObtain2) : null;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.zzau
            public Bundle zza(String str, Bundle bundle) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.auth.IAuthManagerService");
                    parcelObtain.writeString(str);
                    if (bundle != null) {
                        parcelObtain.writeInt(1);
                        bundle.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    this.zznJ.transact(2, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcelObtain2) : null;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.zzau
            public Bundle zza(String str, String str2, Bundle bundle) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.auth.IAuthManagerService");
                    parcelObtain.writeString(str);
                    parcelObtain.writeString(str2);
                    if (bundle != null) {
                        parcelObtain.writeInt(1);
                        bundle.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    this.zznJ.transact(1, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcelObtain2) : null;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.zzau
            public AccountChangeEventsResponse zza(AccountChangeEventsRequest accountChangeEventsRequest) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.auth.IAuthManagerService");
                    if (accountChangeEventsRequest != null) {
                        parcelObtain.writeInt(1);
                        accountChangeEventsRequest.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    this.zznJ.transact(3, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0 ? AccountChangeEventsResponse.CREATOR.createFromParcel(parcelObtain2) : null;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }

        public static zzau zza(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.auth.IAuthManagerService");
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof zzau)) ? new C0021zza(iBinder) : (zzau) iInterfaceQueryLocalInterface;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1598968902) {
                parcel2.writeString("com.google.android.auth.IAuthManagerService");
                return true;
            }
            switch (i) {
                case 1:
                    parcel.enforceInterface("com.google.android.auth.IAuthManagerService");
                    Bundle bundleZza = zza(parcel.readString(), parcel.readString(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    if (bundleZza == null) {
                        parcel2.writeInt(0);
                        return true;
                    }
                    parcel2.writeInt(1);
                    bundleZza.writeToParcel(parcel2, 1);
                    return true;
                case 2:
                    parcel.enforceInterface("com.google.android.auth.IAuthManagerService");
                    Bundle bundleZza2 = zza(parcel.readString(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    if (bundleZza2 == null) {
                        parcel2.writeInt(0);
                        return true;
                    }
                    parcel2.writeInt(1);
                    bundleZza2.writeToParcel(parcel2, 1);
                    return true;
                case 3:
                    parcel.enforceInterface("com.google.android.auth.IAuthManagerService");
                    AccountChangeEventsResponse accountChangeEventsResponseZza = zza(parcel.readInt() != 0 ? AccountChangeEventsRequest.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    if (accountChangeEventsResponseZza == null) {
                        parcel2.writeInt(0);
                        return true;
                    }
                    parcel2.writeInt(1);
                    accountChangeEventsResponseZza.writeToParcel(parcel2, 1);
                    return true;
                default:
                    switch (i) {
                        case 5:
                            parcel.enforceInterface("com.google.android.auth.IAuthManagerService");
                            Bundle bundleZza3 = zza(parcel.readInt() != 0 ? (Account) Account.CREATOR.createFromParcel(parcel) : null, parcel.readString(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                            parcel2.writeNoException();
                            if (bundleZza3 == null) {
                                parcel2.writeInt(0);
                                return true;
                            }
                            parcel2.writeInt(1);
                            bundleZza3.writeToParcel(parcel2, 1);
                            return true;
                        case 6:
                            parcel.enforceInterface("com.google.android.auth.IAuthManagerService");
                            Bundle bundleZza4 = zza(parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                            parcel2.writeNoException();
                            if (bundleZza4 == null) {
                                parcel2.writeInt(0);
                                return true;
                            }
                            parcel2.writeInt(1);
                            bundleZza4.writeToParcel(parcel2, 1);
                            return true;
                        case 7:
                            parcel.enforceInterface("com.google.android.auth.IAuthManagerService");
                            Bundle bundleZza5 = zza(parcel.readInt() != 0 ? (Account) Account.CREATOR.createFromParcel(parcel) : null);
                            parcel2.writeNoException();
                            if (bundleZza5 == null) {
                                parcel2.writeInt(0);
                                return true;
                            }
                            parcel2.writeInt(1);
                            bundleZza5.writeToParcel(parcel2, 1);
                            return true;
                        default:
                            return super.onTransact(i, parcel, parcel2, i2);
                    }
            }
        }
    }

    Bundle zza(Account account) throws RemoteException;

    Bundle zza(Account account, String str, Bundle bundle) throws RemoteException;

    Bundle zza(Bundle bundle) throws RemoteException;

    Bundle zza(String str, Bundle bundle) throws RemoteException;

    Bundle zza(String str, String str2, Bundle bundle) throws RemoteException;

    AccountChangeEventsResponse zza(AccountChangeEventsRequest accountChangeEventsRequest) throws RemoteException;
}
