package com.google.android.gms.common.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface zzt extends IInterface {

    public static abstract class zza extends Binder implements zzt {

        /* renamed from: com.google.android.gms.common.internal.zzt$zza$zza, reason: collision with other inner class name */
        private static class C0015zza implements zzt {
            private IBinder zznJ;

            C0015zza(IBinder iBinder) {
                this.zznJ = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.zznJ;
            }

            @Override // com.google.android.gms.common.internal.zzt
            public void zzb(ResolveAccountResponse resolveAccountResponse) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.common.internal.IResolveAccountCallbacks");
                    if (resolveAccountResponse != null) {
                        parcelObtain.writeInt(1);
                        resolveAccountResponse.writeToParcel(parcelObtain, 0);
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
            attachInterface(this, "com.google.android.gms.common.internal.IResolveAccountCallbacks");
        }

        public static zzt zzaL(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.IResolveAccountCallbacks");
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof zzt)) ? new C0015zza(iBinder) : (zzt) iInterfaceQueryLocalInterface;
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
                parcel2.writeString("com.google.android.gms.common.internal.IResolveAccountCallbacks");
                return true;
            }
            parcel.enforceInterface("com.google.android.gms.common.internal.IResolveAccountCallbacks");
            zzb(parcel.readInt() != 0 ? ResolveAccountResponse.CREATOR.createFromParcel(parcel) : null);
            parcel2.writeNoException();
            return true;
        }
    }

    void zzb(ResolveAccountResponse resolveAccountResponse) throws RemoteException;
}
