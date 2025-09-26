// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.os.RemoteException;
import android.os.IInterface;

public interface zzmd extends IInterface
{
    void zza(final zzmc p0) throws RemoteException;
    
    public abstract static class zza extends Binder implements zzmd
    {
        public static zzmd zzaQ(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.common.internal.service.ICommonService");
            if (queryLocalInterface != null && queryLocalInterface instanceof zzmd) {
                return (zzmd)queryLocalInterface;
            }
            return new zzmd.zza.zza(binder);
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n == 1) {
                parcel.enforceInterface("com.google.android.gms.common.internal.service.ICommonService");
                this.zza(zzmc.zza.zzaP(parcel.readStrongBinder()));
                return true;
            }
            if (n != 1598968902) {
                return super.onTransact(n, parcel, parcel2, n2);
            }
            parcel2.writeString("com.google.android.gms.common.internal.service.ICommonService");
            return true;
        }
        
        private static class zza implements zzmd
        {
            private IBinder zznJ;
            
            zza(final IBinder zznJ) {
                this.zznJ = zznJ;
            }
            
            public IBinder asBinder() {
                return this.zznJ;
            }
            
            @Override
            public void zza(final zzmc zzmc) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.service.ICommonService");
                    IBinder binder;
                    if (zzmc != null) {
                        binder = zzmc.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    this.zznJ.transact(1, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
        }
    }
}
