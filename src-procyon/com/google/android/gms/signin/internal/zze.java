// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.signin.internal;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.Status;
import android.os.RemoteException;
import com.google.android.gms.common.ConnectionResult;
import android.os.IInterface;

public interface zze extends IInterface
{
    void zza(final ConnectionResult p0, final AuthAccountResult p1) throws RemoteException;
    
    void zza(final Status p0, final GoogleSignInAccount p1) throws RemoteException;
    
    void zzbd(final Status p0) throws RemoteException;
    
    void zzbe(final Status p0) throws RemoteException;
    
    public abstract static class zza extends Binder implements zze
    {
        public zza() {
            this.attachInterface((IInterface)this, "com.google.android.gms.signin.internal.ISignInCallbacks");
        }
        
        public static zze zzdM(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.signin.internal.ISignInCallbacks");
            if (queryLocalInterface != null && queryLocalInterface instanceof zze) {
                return (zze)queryLocalInterface;
            }
            return new zze.zza.zza(binder);
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            final Status status = null;
            Status status2 = null;
            final AuthAccountResult authAccountResult = null;
            GoogleSignInAccount googleSignInAccount = null;
            switch (n) {
                default: {
                    return super.onTransact(n, parcel, parcel2, n2);
                }
                case 1598968902: {
                    parcel2.writeString("com.google.android.gms.signin.internal.ISignInCallbacks");
                    return true;
                }
                case 7: {
                    parcel.enforceInterface("com.google.android.gms.signin.internal.ISignInCallbacks");
                    Status status3;
                    if (parcel.readInt() != 0) {
                        status3 = (Status)Status.CREATOR.createFromParcel(parcel);
                    }
                    else {
                        status3 = null;
                    }
                    if (parcel.readInt() != 0) {
                        googleSignInAccount = (GoogleSignInAccount)GoogleSignInAccount.CREATOR.createFromParcel(parcel);
                    }
                    this.zza(status3, googleSignInAccount);
                    parcel2.writeNoException();
                    return true;
                }
                case 6: {
                    parcel.enforceInterface("com.google.android.gms.signin.internal.ISignInCallbacks");
                    Status status4 = status;
                    if (parcel.readInt() != 0) {
                        status4 = (Status)Status.CREATOR.createFromParcel(parcel);
                    }
                    this.zzbe(status4);
                    parcel2.writeNoException();
                    return true;
                }
                case 4: {
                    parcel.enforceInterface("com.google.android.gms.signin.internal.ISignInCallbacks");
                    if (parcel.readInt() != 0) {
                        status2 = (Status)Status.CREATOR.createFromParcel(parcel);
                    }
                    this.zzbd(status2);
                    parcel2.writeNoException();
                    return true;
                }
                case 3: {
                    parcel.enforceInterface("com.google.android.gms.signin.internal.ISignInCallbacks");
                    ConnectionResult connectionResult;
                    if (parcel.readInt() != 0) {
                        connectionResult = (ConnectionResult)ConnectionResult.CREATOR.createFromParcel(parcel);
                    }
                    else {
                        connectionResult = null;
                    }
                    AuthAccountResult authAccountResult2 = authAccountResult;
                    if (parcel.readInt() != 0) {
                        authAccountResult2 = (AuthAccountResult)AuthAccountResult.CREATOR.createFromParcel(parcel);
                    }
                    this.zza(connectionResult, authAccountResult2);
                    parcel2.writeNoException();
                    return true;
                }
            }
        }
        
        private static class zza implements zze
        {
            private IBinder zznJ;
            
            zza(final IBinder zznJ) {
                this.zznJ = zznJ;
            }
            
            public IBinder asBinder() {
                return this.zznJ;
            }
            
            @Override
            public void zza(final ConnectionResult connectionResult, final AuthAccountResult authAccountResult) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInCallbacks");
                    if (connectionResult != null) {
                        obtain.writeInt(1);
                        connectionResult.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    if (authAccountResult != null) {
                        obtain.writeInt(1);
                        authAccountResult.writeToParcel(obtain, 0);
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
            public void zza(final Status status, final GoogleSignInAccount googleSignInAccount) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInCallbacks");
                    if (status != null) {
                        obtain.writeInt(1);
                        status.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    if (googleSignInAccount != null) {
                        obtain.writeInt(1);
                        googleSignInAccount.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void zzbd(final Status status) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInCallbacks");
                    if (status != null) {
                        obtain.writeInt(1);
                        status.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void zzbe(final Status status) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInCallbacks");
                    if (status != null) {
                        obtain.writeInt(1);
                        status.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(6, obtain, obtain2, 0);
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
