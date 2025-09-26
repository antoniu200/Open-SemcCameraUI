package com.google.android.gms.auth.api.credentials.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.common.api.Status;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface zzg extends IInterface {

    public static abstract class zza extends Binder implements zzg {

        /* renamed from: com.google.android.gms.auth.api.credentials.internal.zzg$zza$zza, reason: collision with other inner class name */
        private static class C0005zza implements zzg {
            private IBinder zznJ;

            C0005zza(IBinder iBinder) {
                this.zznJ = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.zznJ;
            }

            @Override // com.google.android.gms.auth.api.credentials.internal.zzg
            public void zza(Status status, Credential credential) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.auth.api.credentials.internal.ICredentialsCallbacks");
                    if (status != null) {
                        parcelObtain.writeInt(1);
                        status.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    if (credential != null) {
                        parcelObtain.writeInt(1);
                        credential.writeToParcel(parcelObtain, 0);
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

            @Override // com.google.android.gms.auth.api.credentials.internal.zzg
            public void zzg(Status status) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.auth.api.credentials.internal.ICredentialsCallbacks");
                    if (status != null) {
                        parcelObtain.writeInt(1);
                        status.writeToParcel(parcelObtain, 0);
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

        public zza() {
            attachInterface(this, "com.google.android.gms.auth.api.credentials.internal.ICredentialsCallbacks");
        }

        public static zzg zzas(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.auth.api.credentials.internal.ICredentialsCallbacks");
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof zzg)) ? new C0005zza(iBinder) : (zzg) iInterfaceQueryLocalInterface;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1598968902) {
                parcel2.writeString("com.google.android.gms.auth.api.credentials.internal.ICredentialsCallbacks");
                return true;
            }
            switch (i) {
                case 1:
                    parcel.enforceInterface("com.google.android.gms.auth.api.credentials.internal.ICredentialsCallbacks");
                    zza(parcel.readInt() != 0 ? Status.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0 ? Credential.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 2:
                    parcel.enforceInterface("com.google.android.gms.auth.api.credentials.internal.ICredentialsCallbacks");
                    zzg(parcel.readInt() != 0 ? Status.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void zza(Status status, Credential credential) throws RemoteException;

    void zzg(Status status) throws RemoteException;
}
