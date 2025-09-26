// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.api.signin.internal;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.os.RemoteException;
import com.google.android.gms.auth.api.signin.GoogleSignInConfig;
import android.os.IInterface;

public interface zze extends IInterface
{
    void zza(final zzd p0, final GoogleSignInConfig p1) throws RemoteException;
    
    void zza(final zzd p0, final SignInConfiguration p1) throws RemoteException;
    
    void zzb(final zzd p0, final GoogleSignInConfig p1) throws RemoteException;
    
    void zzb(final zzd p0, final SignInConfiguration p1) throws RemoteException;
    
    void zzc(final zzd p0, final GoogleSignInConfig p1) throws RemoteException;
    
    public abstract static class zza extends Binder implements zze
    {
        public static zze zzaz(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.auth.api.signin.internal.ISignInService");
            if (queryLocalInterface != null && queryLocalInterface instanceof zze) {
                return (zze)queryLocalInterface;
            }
            return new zze.zza.zza(binder);
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            final GoogleSignInConfig googleSignInConfig = null;
            final GoogleSignInConfig googleSignInConfig2 = null;
            SignInConfiguration signInConfiguration = null;
            final SignInConfiguration signInConfiguration2 = null;
            final GoogleSignInConfig googleSignInConfig3 = null;
            switch (n) {
                default: {
                    return super.onTransact(n, parcel, parcel2, n2);
                }
                case 1598968902: {
                    parcel2.writeString("com.google.android.gms.auth.api.signin.internal.ISignInService");
                    return true;
                }
                case 103: {
                    parcel.enforceInterface("com.google.android.gms.auth.api.signin.internal.ISignInService");
                    final zzd zzay = zzd.zza.zzay(parcel.readStrongBinder());
                    GoogleSignInConfig googleSignInConfig4 = googleSignInConfig3;
                    if (parcel.readInt() != 0) {
                        googleSignInConfig4 = (GoogleSignInConfig)GoogleSignInConfig.CREATOR.createFromParcel(parcel);
                    }
                    this.zzc(zzay, googleSignInConfig4);
                    parcel2.writeNoException();
                    return true;
                }
                case 102: {
                    parcel.enforceInterface("com.google.android.gms.auth.api.signin.internal.ISignInService");
                    final zzd zzay2 = zzd.zza.zzay(parcel.readStrongBinder());
                    GoogleSignInConfig googleSignInConfig5 = googleSignInConfig;
                    if (parcel.readInt() != 0) {
                        googleSignInConfig5 = (GoogleSignInConfig)GoogleSignInConfig.CREATOR.createFromParcel(parcel);
                    }
                    this.zzb(zzay2, googleSignInConfig5);
                    parcel2.writeNoException();
                    return true;
                }
                case 101: {
                    parcel.enforceInterface("com.google.android.gms.auth.api.signin.internal.ISignInService");
                    final zzd zzay3 = zzd.zza.zzay(parcel.readStrongBinder());
                    GoogleSignInConfig googleSignInConfig6 = googleSignInConfig2;
                    if (parcel.readInt() != 0) {
                        googleSignInConfig6 = (GoogleSignInConfig)GoogleSignInConfig.CREATOR.createFromParcel(parcel);
                    }
                    this.zza(zzay3, googleSignInConfig6);
                    parcel2.writeNoException();
                    return true;
                }
                case 2: {
                    parcel.enforceInterface("com.google.android.gms.auth.api.signin.internal.ISignInService");
                    final zzd zzay4 = zzd.zza.zzay(parcel.readStrongBinder());
                    if (parcel.readInt() != 0) {
                        signInConfiguration = (SignInConfiguration)SignInConfiguration.CREATOR.createFromParcel(parcel);
                    }
                    this.zzb(zzay4, signInConfiguration);
                    parcel2.writeNoException();
                    return true;
                }
                case 1: {
                    parcel.enforceInterface("com.google.android.gms.auth.api.signin.internal.ISignInService");
                    final zzd zzay5 = zzd.zza.zzay(parcel.readStrongBinder());
                    SignInConfiguration signInConfiguration3 = signInConfiguration2;
                    if (parcel.readInt() != 0) {
                        signInConfiguration3 = (SignInConfiguration)SignInConfiguration.CREATOR.createFromParcel(parcel);
                    }
                    this.zza(zzay5, signInConfiguration3);
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
            public void zza(final zzd zzd, final GoogleSignInConfig googleSignInConfig) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.signin.internal.ISignInService");
                    IBinder binder;
                    if (zzd != null) {
                        binder = zzd.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    if (googleSignInConfig != null) {
                        obtain.writeInt(1);
                        googleSignInConfig.writeToParcel(obtain, 0);
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
            public void zza(final zzd zzd, final SignInConfiguration signInConfiguration) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.signin.internal.ISignInService");
                    IBinder binder;
                    if (zzd != null) {
                        binder = zzd.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    if (signInConfiguration != null) {
                        obtain.writeInt(1);
                        signInConfiguration.writeToParcel(obtain, 0);
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
            public void zzb(final zzd zzd, final GoogleSignInConfig googleSignInConfig) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.signin.internal.ISignInService");
                    IBinder binder;
                    if (zzd != null) {
                        binder = zzd.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    if (googleSignInConfig != null) {
                        obtain.writeInt(1);
                        googleSignInConfig.writeToParcel(obtain, 0);
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
            public void zzb(final zzd zzd, final SignInConfiguration signInConfiguration) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.signin.internal.ISignInService");
                    IBinder binder;
                    if (zzd != null) {
                        binder = zzd.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    if (signInConfiguration != null) {
                        obtain.writeInt(1);
                        signInConfiguration.writeToParcel(obtain, 0);
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
            public void zzc(final zzd zzd, final GoogleSignInConfig googleSignInConfig) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.signin.internal.ISignInService");
                    IBinder binder;
                    if (zzd != null) {
                        binder = zzd.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    if (googleSignInConfig != null) {
                        obtain.writeInt(1);
                        googleSignInConfig.writeToParcel(obtain, 0);
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
