// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.internal;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.os.RemoteException;
import com.google.android.gms.dynamic.zzd;
import android.os.IInterface;

public interface zzu extends IInterface
{
    zzd zza(final zzd p0, final int p1, final int p2) throws RemoteException;
    
    public abstract static class zza extends Binder implements zzu
    {
        public static zzu zzaM(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.common.internal.ISignInButtonCreator");
            if (queryLocalInterface != null && queryLocalInterface instanceof zzu) {
                return (zzu)queryLocalInterface;
            }
            return new zzu.zza.zza(binder);
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n == 1) {
                parcel.enforceInterface("com.google.android.gms.common.internal.ISignInButtonCreator");
                final zzd zza = this.zza(zzd.zza.zzbk(parcel.readStrongBinder()), parcel.readInt(), parcel.readInt());
                parcel2.writeNoException();
                IBinder binder;
                if (zza != null) {
                    binder = zza.asBinder();
                }
                else {
                    binder = null;
                }
                parcel2.writeStrongBinder(binder);
                return true;
            }
            if (n != 1598968902) {
                return super.onTransact(n, parcel, parcel2, n2);
            }
            parcel2.writeString("com.google.android.gms.common.internal.ISignInButtonCreator");
            return true;
        }
        
        private static class zza implements zzu
        {
            private IBinder zznJ;
            
            zza(final IBinder zznJ) {
                this.zznJ = zznJ;
            }
            
            public IBinder asBinder() {
                return this.zznJ;
            }
            
            @Override
            public zzd zza(zzd zzbk, final int n, final int n2) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.ISignInButtonCreator");
                    IBinder binder;
                    if (zzbk != null) {
                        binder = zzbk.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    obtain.writeInt(n);
                    obtain.writeInt(n2);
                    this.zznJ.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    zzbk = zzd.zza.zzbk(obtain2.readStrongBinder());
                    return zzbk;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
