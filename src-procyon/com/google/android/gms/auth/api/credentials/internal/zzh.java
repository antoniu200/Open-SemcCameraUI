// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.api.credentials.internal;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import com.google.android.gms.auth.api.credentials.CredentialRequest;
import android.os.RemoteException;
import android.os.IInterface;

public interface zzh extends IInterface
{
    void zza(final zzg p0) throws RemoteException;
    
    void zza(final zzg p0, final CredentialRequest p1) throws RemoteException;
    
    void zza(final zzg p0, final DeleteRequest p1) throws RemoteException;
    
    void zza(final zzg p0, final SaveRequest p1) throws RemoteException;
    
    public abstract static class zza extends Binder implements zzh
    {
        public static zzh zzat(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
            if (queryLocalInterface != null && queryLocalInterface instanceof zzh) {
                return (zzh)queryLocalInterface;
            }
            return new zzh.zza.zza(binder);
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n == 1598968902) {
                parcel2.writeString("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                return true;
            }
            SaveRequest saveRequest = null;
            final CredentialRequest credentialRequest = null;
            final DeleteRequest deleteRequest = null;
            switch (n) {
                default: {
                    return super.onTransact(n, parcel, parcel2, n2);
                }
                case 4: {
                    parcel.enforceInterface("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                    this.zza(zzg.zza.zzas(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                }
                case 3: {
                    parcel.enforceInterface("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                    final zzg zzas = zzg.zza.zzas(parcel.readStrongBinder());
                    DeleteRequest deleteRequest2 = deleteRequest;
                    if (parcel.readInt() != 0) {
                        deleteRequest2 = (DeleteRequest)DeleteRequest.CREATOR.createFromParcel(parcel);
                    }
                    this.zza(zzas, deleteRequest2);
                    parcel2.writeNoException();
                    return true;
                }
                case 2: {
                    parcel.enforceInterface("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                    final zzg zzas2 = zzg.zza.zzas(parcel.readStrongBinder());
                    if (parcel.readInt() != 0) {
                        saveRequest = (SaveRequest)SaveRequest.CREATOR.createFromParcel(parcel);
                    }
                    this.zza(zzas2, saveRequest);
                    parcel2.writeNoException();
                    return true;
                }
                case 1: {
                    parcel.enforceInterface("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                    final zzg zzas3 = zzg.zza.zzas(parcel.readStrongBinder());
                    CredentialRequest credentialRequest2 = credentialRequest;
                    if (parcel.readInt() != 0) {
                        credentialRequest2 = (CredentialRequest)CredentialRequest.CREATOR.createFromParcel(parcel);
                    }
                    this.zza(zzas3, credentialRequest2);
                    parcel2.writeNoException();
                    return true;
                }
            }
        }
        
        private static class zza implements zzh
        {
            private IBinder zznJ;
            
            zza(final IBinder zznJ) {
                this.zznJ = zznJ;
            }
            
            public IBinder asBinder() {
                return this.zznJ;
            }
            
            @Override
            public void zza(final zzg zzg) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                    IBinder binder;
                    if (zzg != null) {
                        binder = zzg.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    this.zznJ.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void zza(final zzg zzg, final CredentialRequest credentialRequest) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                    IBinder binder;
                    if (zzg != null) {
                        binder = zzg.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    if (credentialRequest != null) {
                        obtain.writeInt(1);
                        credentialRequest.writeToParcel(obtain, 0);
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
            public void zza(final zzg zzg, final DeleteRequest deleteRequest) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                    IBinder binder;
                    if (zzg != null) {
                        binder = zzg.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    if (deleteRequest != null) {
                        obtain.writeInt(1);
                        deleteRequest.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void zza(final zzg zzg, final SaveRequest saveRequest) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
                    IBinder binder;
                    if (zzg != null) {
                        binder = zzg.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    if (saveRequest != null) {
                        obtain.writeInt(1);
                        saveRequest.writeToParcel(obtain, 0);
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
