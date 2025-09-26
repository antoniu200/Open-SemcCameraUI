// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.os.RemoteException;
import android.os.IInterface;

public interface zzmc extends IInterface
{
    void zzbN(final int p0) throws RemoteException;
    
    public abstract static class zza extends Binder implements zzmc
    {
        public zza() {
            this.attachInterface((IInterface)this, "com.google.android.gms.common.internal.service.ICommonCallbacks");
        }
        
        public static zzmc zzaP(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.common.internal.service.ICommonCallbacks");
            if (queryLocalInterface != null && queryLocalInterface instanceof zzmc) {
                return (zzmc)queryLocalInterface;
            }
            return new zzmc.zza.zza(binder);
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n == 1) {
                parcel.enforceInterface("com.google.android.gms.common.internal.service.ICommonCallbacks");
                this.zzbN(parcel.readInt());
                parcel2.writeNoException();
                return true;
            }
            if (n != 1598968902) {
                return super.onTransact(n, parcel, parcel2, n2);
            }
            parcel2.writeString("com.google.android.gms.common.internal.service.ICommonCallbacks");
            return true;
        }
        
        private static class zza implements zzmc
        {
            private IBinder zznJ;
            
            zza(final IBinder zznJ) {
                this.zznJ = zznJ;
            }
            
            public IBinder asBinder() {
                return this.zznJ;
            }
            
            @Override
            public void zzbN(final int n) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.service.ICommonCallbacks");
                    obtain.writeInt(n);
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
