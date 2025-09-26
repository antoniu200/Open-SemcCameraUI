// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import com.google.android.gms.auth.api.proxy.ProxyRequest;
import android.os.RemoteException;
import com.google.android.gms.auth.api.proxy.ProxyGrpcRequest;
import android.os.IInterface;

public interface zzkk extends IInterface
{
    void zza(final zzkj p0, final ProxyGrpcRequest p1) throws RemoteException;
    
    void zza(final zzkj p0, final ProxyRequest p1) throws RemoteException;
    
    public abstract static class zza extends Binder implements zzkk
    {
        public static zzkk zzaw(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.auth.api.internal.IAuthService");
            if (queryLocalInterface != null && queryLocalInterface instanceof zzkk) {
                return (zzkk)queryLocalInterface;
            }
            return new zzkk.zza.zza(binder);
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n == 1598968902) {
                parcel2.writeString("com.google.android.gms.auth.api.internal.IAuthService");
                return true;
            }
            ProxyRequest proxyRequest = null;
            final ProxyGrpcRequest proxyGrpcRequest = null;
            switch (n) {
                default: {
                    return super.onTransact(n, parcel, parcel2, n2);
                }
                case 2: {
                    parcel.enforceInterface("com.google.android.gms.auth.api.internal.IAuthService");
                    final zzkj zzav = zzkj.zza.zzav(parcel.readStrongBinder());
                    ProxyGrpcRequest proxyGrpcRequest2 = proxyGrpcRequest;
                    if (parcel.readInt() != 0) {
                        proxyGrpcRequest2 = (ProxyGrpcRequest)ProxyGrpcRequest.CREATOR.createFromParcel(parcel);
                    }
                    this.zza(zzav, proxyGrpcRequest2);
                    parcel2.writeNoException();
                    return true;
                }
                case 1: {
                    parcel.enforceInterface("com.google.android.gms.auth.api.internal.IAuthService");
                    final zzkj zzav2 = zzkj.zza.zzav(parcel.readStrongBinder());
                    if (parcel.readInt() != 0) {
                        proxyRequest = (ProxyRequest)ProxyRequest.CREATOR.createFromParcel(parcel);
                    }
                    this.zza(zzav2, proxyRequest);
                    parcel2.writeNoException();
                    return true;
                }
            }
        }
        
        private static class zza implements zzkk
        {
            private IBinder zznJ;
            
            zza(final IBinder zznJ) {
                this.zznJ = zznJ;
            }
            
            public IBinder asBinder() {
                return this.zznJ;
            }
            
            @Override
            public void zza(final zzkj zzkj, final ProxyGrpcRequest proxyGrpcRequest) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.internal.IAuthService");
                    IBinder binder;
                    if (zzkj != null) {
                        binder = zzkj.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    if (proxyGrpcRequest != null) {
                        obtain.writeInt(1);
                        proxyGrpcRequest.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void zza(final zzkj zzkj, final ProxyRequest proxyRequest) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.internal.IAuthService");
                    IBinder binder;
                    if (zzkj != null) {
                        binder = zzkj.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    if (proxyRequest != null) {
                        obtain.writeInt(1);
                        proxyRequest.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
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
