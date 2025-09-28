package com.google.android.gms.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface zzmc extends IInterface {

    public static abstract class zza extends Binder implements zzmc {

        /* renamed from: com.google.android.gms.internal.zzmc$zza$zza, reason: collision with other inner class name */
        private static class C0029zza implements zzmc {
            private IBinder zznJ;

            C0029zza(IBinder iBinder) {
                this.zznJ = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.zznJ;
            }

            @Override // com.google.android.gms.internal.zzmc
            public void zzbN(int i) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.common.internal.service.ICommonCallbacks");
                    parcelObtain.writeInt(i);
                    this.zznJ.transact(1, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }

        public zza() {
            attachInterface(this, "com.google.android.gms.common.internal.service.ICommonCallbacks");
        }

        public static zzmc zzaP(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.service.ICommonCallbacks");
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof zzmc)) ? new C0029zza(iBinder) : (zzmc) iInterfaceQueryLocalInterface;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i != 1) {
                if (i != 1598968902) {
                    return super.onTransact(i, parcel, parcel2, i2);
                }
                parcel2.writeString("com.google.android.gms.common.internal.service.ICommonCallbacks");
                return true;
            }
            parcel.enforceInterface("com.google.android.gms.common.internal.service.ICommonCallbacks");
            zzbN(parcel.readInt());
            parcel2.writeNoException();
            return true;
        }
    }

    void zzbN(int i) throws RemoteException;
}
