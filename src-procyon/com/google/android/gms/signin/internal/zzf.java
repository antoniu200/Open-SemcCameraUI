// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.signin.internal;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import com.google.android.gms.common.internal.zzp;
import com.google.android.gms.common.internal.zzt;
import com.google.android.gms.common.internal.ResolveAccountRequest;
import com.google.android.gms.common.internal.AuthAccountRequest;
import android.os.RemoteException;
import android.accounts.Account;
import android.os.IInterface;

public interface zzf extends IInterface
{
    void zza(final int p0, final Account p1, final zze p2) throws RemoteException;
    
    void zza(final AuthAccountRequest p0, final zze p1) throws RemoteException;
    
    void zza(final ResolveAccountRequest p0, final zzt p1) throws RemoteException;
    
    void zza(final zzp p0, final int p1, final boolean p2) throws RemoteException;
    
    void zza(final CheckServerAuthResult p0) throws RemoteException;
    
    void zza(final RecordConsentRequest p0, final zze p1) throws RemoteException;
    
    void zza(final zze p0) throws RemoteException;
    
    void zzaq(final boolean p0) throws RemoteException;
    
    void zzjq(final int p0) throws RemoteException;
    
    public abstract static class zza extends Binder implements zzf
    {
        public static zzf zzdN(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.signin.internal.ISignInService");
            if (queryLocalInterface != null && queryLocalInterface instanceof zzf) {
                return (zzf)queryLocalInterface;
            }
            return new zzf.zza.zza(binder);
        }
        
        public boolean onTransact(int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n == 1598968902) {
                parcel2.writeString("com.google.android.gms.signin.internal.ISignInService");
                return true;
            }
            final boolean b = false;
            boolean b2 = false;
            final Account account = null;
            final ResolveAccountRequest resolveAccountRequest = null;
            CheckServerAuthResult checkServerAuthResult = null;
            final AuthAccountRequest authAccountRequest = null;
            final RecordConsentRequest recordConsentRequest = null;
            switch (n) {
                default: {
                    switch (n) {
                        default: {
                            return super.onTransact(n, parcel, parcel2, n2);
                        }
                        case 11: {
                            parcel.enforceInterface("com.google.android.gms.signin.internal.ISignInService");
                            this.zza(zze.zza.zzdM(parcel.readStrongBinder()));
                            parcel2.writeNoException();
                            return true;
                        }
                        case 10: {
                            parcel.enforceInterface("com.google.android.gms.signin.internal.ISignInService");
                            RecordConsentRequest recordConsentRequest2 = recordConsentRequest;
                            if (parcel.readInt() != 0) {
                                recordConsentRequest2 = (RecordConsentRequest)RecordConsentRequest.CREATOR.createFromParcel(parcel);
                            }
                            this.zza(recordConsentRequest2, zze.zza.zzdM(parcel.readStrongBinder()));
                            parcel2.writeNoException();
                            return true;
                        }
                        case 9: {
                            parcel.enforceInterface("com.google.android.gms.signin.internal.ISignInService");
                            final zzp zzaH = zzp.zza.zzaH(parcel.readStrongBinder());
                            n = parcel.readInt();
                            if (parcel.readInt() != 0) {
                                b2 = true;
                            }
                            this.zza(zzaH, n, b2);
                            parcel2.writeNoException();
                            return true;
                        }
                        case 8: {
                            parcel.enforceInterface("com.google.android.gms.signin.internal.ISignInService");
                            n = parcel.readInt();
                            Account account2 = account;
                            if (parcel.readInt() != 0) {
                                account2 = (Account)Account.CREATOR.createFromParcel(parcel);
                            }
                            this.zza(n, account2, zze.zza.zzdM(parcel.readStrongBinder()));
                            parcel2.writeNoException();
                            return true;
                        }
                        case 7: {
                            parcel.enforceInterface("com.google.android.gms.signin.internal.ISignInService");
                            this.zzjq(parcel.readInt());
                            parcel2.writeNoException();
                            return true;
                        }
                    }
                    break;
                }
                case 5: {
                    parcel.enforceInterface("com.google.android.gms.signin.internal.ISignInService");
                    ResolveAccountRequest resolveAccountRequest2 = resolveAccountRequest;
                    if (parcel.readInt() != 0) {
                        resolveAccountRequest2 = (ResolveAccountRequest)ResolveAccountRequest.CREATOR.createFromParcel(parcel);
                    }
                    this.zza(resolveAccountRequest2, zzt.zza.zzaL(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                }
                case 4: {
                    parcel.enforceInterface("com.google.android.gms.signin.internal.ISignInService");
                    boolean b3 = b;
                    if (parcel.readInt() != 0) {
                        b3 = true;
                    }
                    this.zzaq(b3);
                    parcel2.writeNoException();
                    return true;
                }
                case 3: {
                    parcel.enforceInterface("com.google.android.gms.signin.internal.ISignInService");
                    if (parcel.readInt() != 0) {
                        checkServerAuthResult = (CheckServerAuthResult)CheckServerAuthResult.CREATOR.createFromParcel(parcel);
                    }
                    this.zza(checkServerAuthResult);
                    parcel2.writeNoException();
                    return true;
                }
                case 2: {
                    parcel.enforceInterface("com.google.android.gms.signin.internal.ISignInService");
                    AuthAccountRequest authAccountRequest2 = authAccountRequest;
                    if (parcel.readInt() != 0) {
                        authAccountRequest2 = (AuthAccountRequest)AuthAccountRequest.CREATOR.createFromParcel(parcel);
                    }
                    this.zza(authAccountRequest2, zze.zza.zzdM(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                }
            }
        }
        
        private static class zza implements zzf
        {
            private IBinder zznJ;
            
            zza(final IBinder zznJ) {
                this.zznJ = zznJ;
            }
            
            public IBinder asBinder() {
                return this.zznJ;
            }
            
            @Override
            public void zza(final int n, final Account account, final zze zze) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                    obtain.writeInt(n);
                    if (account != null) {
                        obtain.writeInt(1);
                        account.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    IBinder binder;
                    if (zze != null) {
                        binder = zze.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    this.zznJ.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void zza(final AuthAccountRequest authAccountRequest, final zze zze) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                    if (authAccountRequest != null) {
                        obtain.writeInt(1);
                        authAccountRequest.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    IBinder binder;
                    if (zze != null) {
                        binder = zze.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    this.zznJ.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void zza(final ResolveAccountRequest resolveAccountRequest, final zzt zzt) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                    if (resolveAccountRequest != null) {
                        obtain.writeInt(1);
                        resolveAccountRequest.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    IBinder binder;
                    if (zzt != null) {
                        binder = zzt.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    this.zznJ.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void zza(final zzp zzp, final int n, final boolean b) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                    IBinder binder;
                    if (zzp != null) {
                        binder = zzp.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    obtain.writeInt(n);
                    obtain.writeInt((int)(b ? 1 : 0));
                    this.zznJ.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void zza(final CheckServerAuthResult checkServerAuthResult) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                    if (checkServerAuthResult != null) {
                        obtain.writeInt(1);
                        checkServerAuthResult.writeToParcel(obtain, 0);
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
            public void zza(final RecordConsentRequest recordConsentRequest, final zze zze) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                    if (recordConsentRequest != null) {
                        obtain.writeInt(1);
                        recordConsentRequest.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    IBinder binder;
                    if (zze != null) {
                        binder = zze.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    this.zznJ.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void zza(final zze zze) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                    IBinder binder;
                    if (zze != null) {
                        binder = zze.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    this.zznJ.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void zzaq(final boolean b) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                    obtain.writeInt((int)(b ? 1 : 0));
                    this.zznJ.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void zzjq(final int n) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                    obtain.writeInt(n);
                    this.zznJ.transact(7, obtain, obtain2, 0);
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
