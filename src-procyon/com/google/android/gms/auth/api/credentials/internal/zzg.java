// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.api.credentials.internal;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.os.RemoteException;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.common.api.Status;
import android.os.IInterface;

public interface zzg extends IInterface
{
    void zza(final Status p0, final Credential p1) throws RemoteException;
    
    void zzg(final Status p0) throws RemoteException;
    
    public abstract static class zza extends Binder implements zzg
    {
        public zza() {
            this.attachInterface((IInterface)this, "com.google.android.gms.auth.api.credentials.internal.ICredentialsCallbacks");
        }
        
        public static zzg zzas(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.auth.api.credentials.internal.ICredentialsCallbacks");
            if (queryLocalInterface != null && queryLocalInterface instanceof zzg) {
                return (zzg)queryLocalInterface;
            }
            return new zzg.zza.zza(binder);
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n == 1598968902) {
                parcel2.writeString("com.google.android.gms.auth.api.credentials.internal.ICredentialsCallbacks");
                return true;
            }
            Credential credential = null;
            Status status = null;
            switch (n) {
                default: {
                    return super.onTransact(n, parcel, parcel2, n2);
                }
                case 2: {
                    parcel.enforceInterface("com.google.android.gms.auth.api.credentials.internal.ICredentialsCallbacks");
                    if (parcel.readInt() != 0) {
                        status = (Status)Status.CREATOR.createFromParcel(parcel);
                    }
                    this.zzg(status);
                    parcel2.writeNoException();
                    return true;
                }
                case 1: {
                    parcel.enforceInterface("com.google.android.gms.auth.api.credentials.internal.ICredentialsCallbacks");
                    Status status2;
                    if (parcel.readInt() != 0) {
                        status2 = (Status)Status.CREATOR.createFromParcel(parcel);
                    }
                    else {
                        status2 = null;
                    }
                    if (parcel.readInt() != 0) {
                        credential = (Credential)Credential.CREATOR.createFromParcel(parcel);
                    }
                    this.zza(status2, credential);
                    parcel2.writeNoException();
                    return true;
                }
            }
        }
        
        private static class zza implements zzg
        {
            private IBinder zznJ;
            
            zza(final IBinder zznJ) {
                this.zznJ = zznJ;
            }
            
            public IBinder asBinder() {
                return this.zznJ;
            }
            
            @Override
            public void zza(final Status status, final Credential credential) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.credentials.internal.ICredentialsCallbacks");
                    if (status != null) {
                        obtain.writeInt(1);
                        status.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    if (credential != null) {
                        obtain.writeInt(1);
                        credential.writeToParcel(obtain, 0);
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
            
            @Override
            public void zzg(final Status status) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.credentials.internal.ICredentialsCallbacks");
                    if (status != null) {
                        obtain.writeInt(1);
                        status.writeToParcel(obtain, 0);
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
        }
    }
}
