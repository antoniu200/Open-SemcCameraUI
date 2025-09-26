package com.google.android.gms.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.auth.api.proxy.ProxyGrpcRequest;
import com.google.android.gms.auth.api.proxy.ProxyRequest;
import com.google.android.gms.internal.zzkj;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface zzkk extends IInterface {

    public static abstract class zza extends Binder implements zzkk {

        /* renamed from: com.google.android.gms.internal.zzkk$zza$zza, reason: collision with other inner class name */
        private static class C0026zza implements zzkk {
            private IBinder zznJ;

            C0026zza(IBinder iBinder) {
                this.zznJ = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.zznJ;
            }

            @Override // com.google.android.gms.internal.zzkk
            public void zza(zzkj zzkjVar, ProxyGrpcRequest proxyGrpcRequest) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.auth.api.internal.IAuthService");
                    parcelObtain.writeStrongBinder(zzkjVar != null ? zzkjVar.asBinder() : null);
                    if (proxyGrpcRequest != null) {
                        parcelObtain.writeInt(1);
                        proxyGrpcRequest.writeToParcel(parcelObtain, 0);
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

            @Override // com.google.android.gms.internal.zzkk
            public void zza(zzkj zzkjVar, ProxyRequest proxyRequest) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.auth.api.internal.IAuthService");
                    parcelObtain.writeStrongBinder(zzkjVar != null ? zzkjVar.asBinder() : null);
                    if (proxyRequest != null) {
                        parcelObtain.writeInt(1);
                        proxyRequest.writeToParcel(parcelObtain, 0);
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
        }

        public static zzkk zzaw(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.auth.api.internal.IAuthService");
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof zzkk)) ? new C0026zza(iBinder) : (zzkk) iInterfaceQueryLocalInterface;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1598968902) {
                parcel2.writeString("com.google.android.gms.auth.api.internal.IAuthService");
                return true;
            }
            switch (i) {
                case 1:
                    parcel.enforceInterface("com.google.android.gms.auth.api.internal.IAuthService");
                    zza(zzkj.zza.zzav(parcel.readStrongBinder()), parcel.readInt() != 0 ? ProxyRequest.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 2:
                    parcel.enforceInterface("com.google.android.gms.auth.api.internal.IAuthService");
                    zza(zzkj.zza.zzav(parcel.readStrongBinder()), parcel.readInt() != 0 ? ProxyGrpcRequest.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void zza(zzkj zzkjVar, ProxyGrpcRequest proxyGrpcRequest) throws RemoteException;

    void zza(zzkj zzkjVar, ProxyRequest proxyRequest) throws RemoteException;
}
