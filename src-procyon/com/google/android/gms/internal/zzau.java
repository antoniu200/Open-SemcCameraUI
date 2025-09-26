// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import com.google.android.gms.auth.AccountChangeEventsResponse;
import com.google.android.gms.auth.AccountChangeEventsRequest;
import android.os.RemoteException;
import android.os.Bundle;
import android.accounts.Account;
import android.os.IInterface;

public interface zzau extends IInterface
{
    Bundle zza(final Account p0) throws RemoteException;
    
    Bundle zza(final Account p0, final String p1, final Bundle p2) throws RemoteException;
    
    Bundle zza(final Bundle p0) throws RemoteException;
    
    Bundle zza(final String p0, final Bundle p1) throws RemoteException;
    
    Bundle zza(final String p0, final String p1, final Bundle p2) throws RemoteException;
    
    AccountChangeEventsResponse zza(final AccountChangeEventsRequest p0) throws RemoteException;
    
    public abstract static class zza extends Binder implements zzau
    {
        public static zzau zza(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.auth.IAuthManagerService");
            if (queryLocalInterface != null && queryLocalInterface instanceof zzau) {
                return (zzau)queryLocalInterface;
            }
            return new zzau.zza.zza(binder);
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n == 1598968902) {
                parcel2.writeString("com.google.android.auth.IAuthManagerService");
                return true;
            }
            final Bundle bundle = null;
            Bundle bundle2 = null;
            final AccountChangeEventsRequest accountChangeEventsRequest = null;
            final Bundle bundle3 = null;
            Bundle bundle4 = null;
            final Account account = null;
            switch (n) {
                default: {
                    switch (n) {
                        default: {
                            return super.onTransact(n, parcel, parcel2, n2);
                        }
                        case 7: {
                            parcel.enforceInterface("com.google.android.auth.IAuthManagerService");
                            Account account2 = account;
                            if (parcel.readInt() != 0) {
                                account2 = (Account)Account.CREATOR.createFromParcel(parcel);
                            }
                            final Bundle zza = this.zza(account2);
                            parcel2.writeNoException();
                            if (zza != null) {
                                parcel2.writeInt(1);
                                zza.writeToParcel(parcel2, 1);
                                return true;
                            }
                            parcel2.writeInt(0);
                            return true;
                        }
                        case 6: {
                            parcel.enforceInterface("com.google.android.auth.IAuthManagerService");
                            Bundle bundle5 = bundle;
                            if (parcel.readInt() != 0) {
                                bundle5 = (Bundle)Bundle.CREATOR.createFromParcel(parcel);
                            }
                            final Bundle zza2 = this.zza(bundle5);
                            parcel2.writeNoException();
                            if (zza2 != null) {
                                parcel2.writeInt(1);
                                zza2.writeToParcel(parcel2, 1);
                                return true;
                            }
                            parcel2.writeInt(0);
                            return true;
                        }
                        case 5: {
                            parcel.enforceInterface("com.google.android.auth.IAuthManagerService");
                            Account account3;
                            if (parcel.readInt() != 0) {
                                account3 = (Account)Account.CREATOR.createFromParcel(parcel);
                            }
                            else {
                                account3 = null;
                            }
                            final String string = parcel.readString();
                            if (parcel.readInt() != 0) {
                                bundle2 = (Bundle)Bundle.CREATOR.createFromParcel(parcel);
                            }
                            final Bundle zza3 = this.zza(account3, string, bundle2);
                            parcel2.writeNoException();
                            if (zza3 != null) {
                                parcel2.writeInt(1);
                                zza3.writeToParcel(parcel2, 1);
                                return true;
                            }
                            parcel2.writeInt(0);
                            return true;
                        }
                    }
                    break;
                }
                case 3: {
                    parcel.enforceInterface("com.google.android.auth.IAuthManagerService");
                    AccountChangeEventsRequest accountChangeEventsRequest2 = accountChangeEventsRequest;
                    if (parcel.readInt() != 0) {
                        accountChangeEventsRequest2 = (AccountChangeEventsRequest)AccountChangeEventsRequest.CREATOR.createFromParcel(parcel);
                    }
                    final AccountChangeEventsResponse zza4 = this.zza(accountChangeEventsRequest2);
                    parcel2.writeNoException();
                    if (zza4 != null) {
                        parcel2.writeInt(1);
                        zza4.writeToParcel(parcel2, 1);
                        return true;
                    }
                    parcel2.writeInt(0);
                    return true;
                }
                case 2: {
                    parcel.enforceInterface("com.google.android.auth.IAuthManagerService");
                    final String string2 = parcel.readString();
                    Bundle bundle6 = bundle3;
                    if (parcel.readInt() != 0) {
                        bundle6 = (Bundle)Bundle.CREATOR.createFromParcel(parcel);
                    }
                    final Bundle zza5 = this.zza(string2, bundle6);
                    parcel2.writeNoException();
                    if (zza5 != null) {
                        parcel2.writeInt(1);
                        zza5.writeToParcel(parcel2, 1);
                        return true;
                    }
                    parcel2.writeInt(0);
                    return true;
                }
                case 1: {
                    parcel.enforceInterface("com.google.android.auth.IAuthManagerService");
                    final String string3 = parcel.readString();
                    final String string4 = parcel.readString();
                    if (parcel.readInt() != 0) {
                        bundle4 = (Bundle)Bundle.CREATOR.createFromParcel(parcel);
                    }
                    final Bundle zza6 = this.zza(string3, string4, bundle4);
                    parcel2.writeNoException();
                    if (zza6 != null) {
                        parcel2.writeInt(1);
                        zza6.writeToParcel(parcel2, 1);
                        return true;
                    }
                    parcel2.writeInt(0);
                    return true;
                }
            }
        }
        
        private static class zza implements zzau
        {
            private IBinder zznJ;
            
            zza(final IBinder zznJ) {
                this.zznJ = zznJ;
            }
            
            public IBinder asBinder() {
                return this.zznJ;
            }
            
            @Override
            public Bundle zza(final Account account) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.auth.IAuthManagerService");
                    if (account != null) {
                        obtain.writeInt(1);
                        account.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    Bundle bundle;
                    if (obtain2.readInt() != 0) {
                        bundle = (Bundle)Bundle.CREATOR.createFromParcel(obtain2);
                    }
                    else {
                        bundle = null;
                    }
                    return bundle;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public Bundle zza(final Account account, final String s, final Bundle bundle) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.auth.IAuthManagerService");
                    if (account != null) {
                        obtain.writeInt(1);
                        account.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(s);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    Bundle bundle2;
                    if (obtain2.readInt() != 0) {
                        bundle2 = (Bundle)Bundle.CREATOR.createFromParcel(obtain2);
                    }
                    else {
                        bundle2 = null;
                    }
                    return bundle2;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public Bundle zza(Bundle bundle) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.auth.IAuthManagerService");
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        bundle = (Bundle)Bundle.CREATOR.createFromParcel(obtain2);
                    }
                    else {
                        bundle = null;
                    }
                    return bundle;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public Bundle zza(final String s, final Bundle bundle) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.auth.IAuthManagerService");
                    obtain.writeString(s);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    Bundle bundle2;
                    if (obtain2.readInt() != 0) {
                        bundle2 = (Bundle)Bundle.CREATOR.createFromParcel(obtain2);
                    }
                    else {
                        bundle2 = null;
                    }
                    return bundle2;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public Bundle zza(final String s, final String s2, final Bundle bundle) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.auth.IAuthManagerService");
                    obtain.writeString(s);
                    obtain.writeString(s2);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    Bundle bundle2;
                    if (obtain2.readInt() != 0) {
                        bundle2 = (Bundle)Bundle.CREATOR.createFromParcel(obtain2);
                    }
                    else {
                        bundle2 = null;
                    }
                    return bundle2;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public AccountChangeEventsResponse zza(final AccountChangeEventsRequest accountChangeEventsRequest) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.auth.IAuthManagerService");
                    if (accountChangeEventsRequest != null) {
                        obtain.writeInt(1);
                        accountChangeEventsRequest.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    AccountChangeEventsResponse accountChangeEventsResponse;
                    if (obtain2.readInt() != 0) {
                        accountChangeEventsResponse = (AccountChangeEventsResponse)AccountChangeEventsResponse.CREATOR.createFromParcel(obtain2);
                    }
                    else {
                        accountChangeEventsResponse = null;
                    }
                    return accountChangeEventsResponse;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
