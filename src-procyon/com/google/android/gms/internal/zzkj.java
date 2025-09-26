// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.os.RemoteException;
import com.google.android.gms.auth.api.proxy.ProxyResponse;
import android.os.IInterface;

public interface zzkj extends IInterface
{
    void zza(final ProxyResponse p0) throws RemoteException;
    
    public abstract static class zza extends Binder implements zzkj
    {
        public zza() {
            this.attachInterface((IInterface)this, "com.google.android.gms.auth.api.internal.IAuthCallbacks");
        }
        
        public static zzkj zzav(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.auth.api.internal.IAuthCallbacks");
            if (queryLocalInterface != null && queryLocalInterface instanceof zzkj) {
                return (zzkj)queryLocalInterface;
            }
            return new zzkj.zza.zza(binder);
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n == 1) {
                parcel.enforceInterface("com.google.android.gms.auth.api.internal.IAuthCallbacks");
                ProxyResponse proxyResponse;
                if (parcel.readInt() != 0) {
                    proxyResponse = (ProxyResponse)ProxyResponse.CREATOR.createFromParcel(parcel);
                }
                else {
                    proxyResponse = null;
                }
                this.zza(proxyResponse);
                parcel2.writeNoException();
                return true;
            }
            if (n != 1598968902) {
                return super.onTransact(n, parcel, parcel2, n2);
            }
            parcel2.writeString("com.google.android.gms.auth.api.internal.IAuthCallbacks");
            return true;
        }
        
        private static class zza implements zzkj
        {
            private IBinder zznJ;
            
            zza(final IBinder zznJ) {
                this.zznJ = zznJ;
            }
            
            public IBinder asBinder() {
                return this.zznJ;
            }
            
            @Override
            public void zza(final ProxyResponse proxyResponse) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.internal.IAuthCallbacks");
                    if (proxyResponse != null) {
                        obtain.writeInt(1);
                        proxyResponse.writeToParcel(obtain, 0);
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
