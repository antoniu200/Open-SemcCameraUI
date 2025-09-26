package com.google.android.gms.auth.api.credentials.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.auth.api.credentials.CredentialRequest;
import com.google.android.gms.auth.api.credentials.internal.zzg;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface zzh extends IInterface {

    public static abstract class zza extends Binder implements zzh {

        /* renamed from: com.google.android.gms.auth.api.credentials.internal.zzh$zza$zza, reason: collision with other inner class name */
        private static class C0006zza implements zzh {
            private IBinder zznJ;

            C0006zza(IBinder iBinder) {
                this.zznJ = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.zznJ;
            }

            @Override // com.google.android.gms.auth.api.credentials.internal.zzh
            public void zza(zzg zzgVar) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                    parcelObtain.writeStrongBinder(zzgVar != null ? zzgVar.asBinder() : null);
                    this.zznJ.transact(4, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.auth.api.credentials.internal.zzh
            public void zza(zzg zzgVar, CredentialRequest credentialRequest) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                    parcelObtain.writeStrongBinder(zzgVar != null ? zzgVar.asBinder() : null);
                    if (credentialRequest != null) {
                        parcelObtain.writeInt(1);
                        credentialRequest.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    this.zznJ.transact(1, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.auth.api.credentials.internal.zzh
            public void zza(zzg zzgVar, DeleteRequest deleteRequest) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                    parcelObtain.writeStrongBinder(zzgVar != null ? zzgVar.asBinder() : null);
                    if (deleteRequest != null) {
                        parcelObtain.writeInt(1);
                        deleteRequest.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    this.zznJ.transact(3, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.auth.api.credentials.internal.zzh
            public void zza(zzg zzgVar, SaveRequest saveRequest) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                    parcelObtain.writeStrongBinder(zzgVar != null ? zzgVar.asBinder() : null);
                    if (saveRequest != null) {
                        parcelObtain.writeInt(1);
                        saveRequest.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    this.zznJ.transact(2, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }

        public static zzh zzat(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof zzh)) ? new C0006zza(iBinder) : (zzh) iInterfaceQueryLocalInterface;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1598968902) {
                parcel2.writeString("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                return true;
            }
            switch (i) {
                case 1:
                    parcel.enforceInterface("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                    zza(zzg.zza.zzas(parcel.readStrongBinder()), parcel.readInt() != 0 ? CredentialRequest.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 2:
                    parcel.enforceInterface("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                    zza(zzg.zza.zzas(parcel.readStrongBinder()), parcel.readInt() != 0 ? SaveRequest.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 3:
                    parcel.enforceInterface("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                    zza(zzg.zza.zzas(parcel.readStrongBinder()), parcel.readInt() != 0 ? DeleteRequest.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 4:
                    parcel.enforceInterface("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                    zza(zzg.zza.zzas(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void zza(zzg zzgVar) throws RemoteException;

    void zza(zzg zzgVar, CredentialRequest credentialRequest) throws RemoteException;

    void zza(zzg zzgVar, DeleteRequest deleteRequest) throws RemoteException;

    void zza(zzg zzgVar, SaveRequest saveRequest) throws RemoteException;
}
