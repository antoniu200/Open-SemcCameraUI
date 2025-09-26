// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.dynamic;

import android.os.RemoteException;
import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.os.IInterface;

public interface zzd extends IInterface
{
    public abstract static class zza extends Binder implements zzd
    {
        public zza() {
            this.attachInterface((IInterface)this, "com.google.android.gms.dynamic.IObjectWrapper");
        }
        
        public static zzd zzbk(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.dynamic.IObjectWrapper");
            if (queryLocalInterface != null && queryLocalInterface instanceof zzd) {
                return (zzd)queryLocalInterface;
            }
            return new zzd.zza.zza(binder);
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n != 1598968902) {
                return super.onTransact(n, parcel, parcel2, n2);
            }
            parcel2.writeString("com.google.android.gms.dynamic.IObjectWrapper");
            return true;
        }
        
        private static class zza implements zzd
        {
            private IBinder zznJ;
            
            zza(final IBinder zznJ) {
                this.zznJ = zznJ;
            }
            
            public IBinder asBinder() {
                return this.zznJ;
            }
        }
    }
}
