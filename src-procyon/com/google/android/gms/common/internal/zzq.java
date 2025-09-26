// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.internal;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.os.RemoteException;
import android.os.IInterface;

public interface zzq extends IInterface
{
    void cancel() throws RemoteException;
    
    public abstract static class zza extends Binder implements zzq
    {
        public static zzq zzaI(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.common.internal.ICancelToken");
            if (queryLocalInterface != null && queryLocalInterface instanceof zzq) {
                return (zzq)queryLocalInterface;
            }
            return new zzq.zza.zza(binder);
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n == 2) {
                parcel.enforceInterface("com.google.android.gms.common.internal.ICancelToken");
                this.cancel();
                return true;
            }
            if (n != 1598968902) {
                return super.onTransact(n, parcel, parcel2, n2);
            }
            parcel2.writeString("com.google.android.gms.common.internal.ICancelToken");
            return true;
        }
        
        private static class zza implements zzq
        {
            private IBinder zznJ;
            
            zza(final IBinder zznJ) {
                this.zznJ = zznJ;
            }
            
            public IBinder asBinder() {
                return this.zznJ;
            }
            
            @Override
            public void cancel() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.ICancelToken");
                    this.zznJ.transact(2, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
        }
    }
}
