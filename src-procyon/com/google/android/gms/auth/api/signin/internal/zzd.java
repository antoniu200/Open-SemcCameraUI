// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.api.signin.internal;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.content.Intent;
import android.os.RemoteException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import android.os.IInterface;

public interface zzd extends IInterface
{
    void zza(final GoogleSignInAccount p0, final Status p1) throws RemoteException;
    
    void zza(final Status p0, final Intent p1) throws RemoteException;
    
    void zzk(final Status p0) throws RemoteException;
    
    void zzl(final Status p0) throws RemoteException;
    
    void zzm(final Status p0) throws RemoteException;
    
    public abstract static class zza extends Binder implements zzd
    {
        public static zzd zzay(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.auth.api.signin.internal.ISignInCallbacks");
            if (queryLocalInterface != null && queryLocalInterface instanceof zzd) {
                return (zzd)queryLocalInterface;
            }
            return new zzd.zza.zza(binder);
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            final Status status = null;
            Status status2 = null;
            Status status3 = null;
            final Intent intent = null;
            final Status status4 = null;
            switch (n) {
                default: {
                    return super.onTransact(n, parcel, parcel2, n2);
                }
                case 1598968902: {
                    parcel2.writeString("com.google.android.gms.auth.api.signin.internal.ISignInCallbacks");
                    return true;
                }
                case 103: {
                    parcel.enforceInterface("com.google.android.gms.auth.api.signin.internal.ISignInCallbacks");
                    Status status5 = status4;
                    if (parcel.readInt() != 0) {
                        status5 = (Status)Status.CREATOR.createFromParcel(parcel);
                    }
                    this.zzm(status5);
                    parcel2.writeNoException();
                    return true;
                }
                case 102: {
                    parcel.enforceInterface("com.google.android.gms.auth.api.signin.internal.ISignInCallbacks");
                    Status status6 = status;
                    if (parcel.readInt() != 0) {
                        status6 = (Status)Status.CREATOR.createFromParcel(parcel);
                    }
                    this.zzl(status6);
                    parcel2.writeNoException();
                    return true;
                }
                case 101: {
                    parcel.enforceInterface("com.google.android.gms.auth.api.signin.internal.ISignInCallbacks");
                    GoogleSignInAccount googleSignInAccount;
                    if (parcel.readInt() != 0) {
                        googleSignInAccount = (GoogleSignInAccount)GoogleSignInAccount.CREATOR.createFromParcel(parcel);
                    }
                    else {
                        googleSignInAccount = null;
                    }
                    if (parcel.readInt() != 0) {
                        status2 = (Status)Status.CREATOR.createFromParcel(parcel);
                    }
                    this.zza(googleSignInAccount, status2);
                    parcel2.writeNoException();
                    return true;
                }
                case 2: {
                    parcel.enforceInterface("com.google.android.gms.auth.api.signin.internal.ISignInCallbacks");
                    if (parcel.readInt() != 0) {
                        status3 = (Status)Status.CREATOR.createFromParcel(parcel);
                    }
                    this.zzk(status3);
                    parcel2.writeNoException();
                    return true;
                }
                case 1: {
                    parcel.enforceInterface("com.google.android.gms.auth.api.signin.internal.ISignInCallbacks");
                    Status status7;
                    if (parcel.readInt() != 0) {
                        status7 = (Status)Status.CREATOR.createFromParcel(parcel);
                    }
                    else {
                        status7 = null;
                    }
                    Intent intent2 = intent;
                    if (parcel.readInt() != 0) {
                        intent2 = (Intent)Intent.CREATOR.createFromParcel(parcel);
                    }
                    this.zza(status7, intent2);
                    parcel2.writeNoException();
                    return true;
                }
            }
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
            
            @Override
            public void zza(final GoogleSignInAccount googleSignInAccount, final Status status) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.signin.internal.ISignInCallbacks");
                    if (googleSignInAccount != null) {
                        obtain.writeInt(1);
                        googleSignInAccount.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    if (status != null) {
                        obtain.writeInt(1);
                        status.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(101, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void zza(final Status status, final Intent intent) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.signin.internal.ISignInCallbacks");
                    if (status != null) {
                        obtain.writeInt(1);
                        status.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    if (intent != null) {
                        obtain.writeInt(1);
                        intent.writeToParcel(obtain, 0);
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
            public void zzk(final Status status) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.signin.internal.ISignInCallbacks");
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
            
            @Override
            public void zzl(final Status status) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.signin.internal.ISignInCallbacks");
                    if (status != null) {
                        obtain.writeInt(1);
                        status.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(102, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void zzm(final Status status) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.signin.internal.ISignInCallbacks");
                    if (status != null) {
                        obtain.writeInt(1);
                        status.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(103, obtain, obtain2, 0);
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
