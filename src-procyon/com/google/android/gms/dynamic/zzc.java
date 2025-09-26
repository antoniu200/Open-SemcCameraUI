// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.dynamic;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.content.Intent;
import android.os.RemoteException;
import android.os.Bundle;
import android.os.IInterface;

public interface zzc extends IInterface
{
    Bundle getArguments() throws RemoteException;
    
    int getId() throws RemoteException;
    
    boolean getRetainInstance() throws RemoteException;
    
    String getTag() throws RemoteException;
    
    int getTargetRequestCode() throws RemoteException;
    
    boolean getUserVisibleHint() throws RemoteException;
    
    zzd getView() throws RemoteException;
    
    boolean isAdded() throws RemoteException;
    
    boolean isDetached() throws RemoteException;
    
    boolean isHidden() throws RemoteException;
    
    boolean isInLayout() throws RemoteException;
    
    boolean isRemoving() throws RemoteException;
    
    boolean isResumed() throws RemoteException;
    
    boolean isVisible() throws RemoteException;
    
    void setHasOptionsMenu(final boolean p0) throws RemoteException;
    
    void setMenuVisibility(final boolean p0) throws RemoteException;
    
    void setRetainInstance(final boolean p0) throws RemoteException;
    
    void setUserVisibleHint(final boolean p0) throws RemoteException;
    
    void startActivity(final Intent p0) throws RemoteException;
    
    void startActivityForResult(final Intent p0, final int p1) throws RemoteException;
    
    void zzn(final zzd p0) throws RemoteException;
    
    void zzo(final zzd p0) throws RemoteException;
    
    zzd zzsa() throws RemoteException;
    
    zzc zzsb() throws RemoteException;
    
    zzd zzsc() throws RemoteException;
    
    zzc zzsd() throws RemoteException;
    
    public abstract static class zza extends Binder implements zzc
    {
        public zza() {
            this.attachInterface((IInterface)this, "com.google.android.gms.dynamic.IFragmentWrapper");
        }
        
        public static zzc zzbj(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.dynamic.IFragmentWrapper");
            if (queryLocalInterface != null && queryLocalInterface instanceof zzc) {
                return (zzc)queryLocalInterface;
            }
            return new zzc.zza.zza(binder);
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public boolean onTransact(int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n == 1598968902) {
                parcel2.writeString("com.google.android.gms.dynamic.IFragmentWrapper");
                return true;
            }
            boolean retainInstance = false;
            final boolean b = false;
            final boolean b2 = false;
            final boolean b3 = false;
            final Intent intent = null;
            final IBinder binder = null;
            final IBinder binder2 = null;
            final IBinder binder3 = null;
            final IBinder binder4 = null;
            final IBinder binder5 = null;
            final Intent intent2 = null;
            switch (n) {
                default: {
                    return super.onTransact(n, parcel, parcel2, n2);
                }
                case 27: {
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    this.zzo(zzd.zza.zzbk(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                }
                case 26: {
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    Intent intent3 = intent2;
                    if (parcel.readInt() != 0) {
                        intent3 = (Intent)Intent.CREATOR.createFromParcel(parcel);
                    }
                    this.startActivityForResult(intent3, parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                }
                case 25: {
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    Intent intent4 = intent;
                    if (parcel.readInt() != 0) {
                        intent4 = (Intent)Intent.CREATOR.createFromParcel(parcel);
                    }
                    this.startActivity(intent4);
                    parcel2.writeNoException();
                    return true;
                }
                case 24: {
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    boolean userVisibleHint = b3;
                    if (parcel.readInt() != 0) {
                        userVisibleHint = true;
                    }
                    this.setUserVisibleHint(userVisibleHint);
                    parcel2.writeNoException();
                    return true;
                }
                case 23: {
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    if (parcel.readInt() != 0) {
                        retainInstance = true;
                    }
                    this.setRetainInstance(retainInstance);
                    parcel2.writeNoException();
                    return true;
                }
                case 22: {
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    boolean menuVisibility = b;
                    if (parcel.readInt() != 0) {
                        menuVisibility = true;
                    }
                    this.setMenuVisibility(menuVisibility);
                    parcel2.writeNoException();
                    return true;
                }
                case 21: {
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    boolean hasOptionsMenu = b2;
                    if (parcel.readInt() != 0) {
                        hasOptionsMenu = true;
                    }
                    this.setHasOptionsMenu(hasOptionsMenu);
                    parcel2.writeNoException();
                    return true;
                }
                case 20: {
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    this.zzn(zzd.zza.zzbk(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                }
                case 19: {
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    n = (this.isVisible() ? 1 : 0);
                    parcel2.writeNoException();
                    parcel2.writeInt(n);
                    return true;
                }
                case 18: {
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    n = (this.isResumed() ? 1 : 0);
                    parcel2.writeNoException();
                    parcel2.writeInt(n);
                    return true;
                }
                case 17: {
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    n = (this.isRemoving() ? 1 : 0);
                    parcel2.writeNoException();
                    parcel2.writeInt(n);
                    return true;
                }
                case 16: {
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    n = (this.isInLayout() ? 1 : 0);
                    parcel2.writeNoException();
                    parcel2.writeInt(n);
                    return true;
                }
                case 15: {
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    n = (this.isHidden() ? 1 : 0);
                    parcel2.writeNoException();
                    parcel2.writeInt(n);
                    return true;
                }
                case 14: {
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    n = (this.isDetached() ? 1 : 0);
                    parcel2.writeNoException();
                    parcel2.writeInt(n);
                    return true;
                }
                case 13: {
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    n = (this.isAdded() ? 1 : 0);
                    parcel2.writeNoException();
                    parcel2.writeInt(n);
                    return true;
                }
                case 12: {
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    final zzd view = this.getView();
                    parcel2.writeNoException();
                    IBinder binder6 = binder;
                    if (view != null) {
                        binder6 = view.asBinder();
                    }
                    parcel2.writeStrongBinder(binder6);
                    return true;
                }
                case 11: {
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    n = (this.getUserVisibleHint() ? 1 : 0);
                    parcel2.writeNoException();
                    parcel2.writeInt(n);
                    return true;
                }
                case 10: {
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    n = this.getTargetRequestCode();
                    parcel2.writeNoException();
                    parcel2.writeInt(n);
                    return true;
                }
                case 9: {
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    final zzc zzsd = this.zzsd();
                    parcel2.writeNoException();
                    IBinder binder7 = binder2;
                    if (zzsd != null) {
                        binder7 = zzsd.asBinder();
                    }
                    parcel2.writeStrongBinder(binder7);
                    return true;
                }
                case 8: {
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    final String tag = this.getTag();
                    parcel2.writeNoException();
                    parcel2.writeString(tag);
                    return true;
                }
                case 7: {
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    n = (this.getRetainInstance() ? 1 : 0);
                    parcel2.writeNoException();
                    parcel2.writeInt(n);
                    return true;
                }
                case 6: {
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    final zzd zzsc = this.zzsc();
                    parcel2.writeNoException();
                    IBinder binder8 = binder3;
                    if (zzsc != null) {
                        binder8 = zzsc.asBinder();
                    }
                    parcel2.writeStrongBinder(binder8);
                    return true;
                }
                case 5: {
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    final zzc zzsb = this.zzsb();
                    parcel2.writeNoException();
                    IBinder binder9 = binder4;
                    if (zzsb != null) {
                        binder9 = zzsb.asBinder();
                    }
                    parcel2.writeStrongBinder(binder9);
                    return true;
                }
                case 4: {
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    n = this.getId();
                    parcel2.writeNoException();
                    parcel2.writeInt(n);
                    return true;
                }
                case 3: {
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    final Bundle arguments = this.getArguments();
                    parcel2.writeNoException();
                    if (arguments != null) {
                        parcel2.writeInt(1);
                        arguments.writeToParcel(parcel2, 1);
                        return true;
                    }
                    parcel2.writeInt(0);
                    return true;
                }
                case 2: {
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    final zzd zzsa = this.zzsa();
                    parcel2.writeNoException();
                    IBinder binder10 = binder5;
                    if (zzsa != null) {
                        binder10 = zzsa.asBinder();
                    }
                    parcel2.writeStrongBinder(binder10);
                    return true;
                }
            }
        }
        
        private static class zza implements zzc
        {
            private IBinder zznJ;
            
            zza(final IBinder zznJ) {
                this.zznJ = zznJ;
            }
            
            public IBinder asBinder() {
                return this.zznJ;
            }
            
            @Override
            public Bundle getArguments() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    this.zznJ.transact(3, obtain, obtain2, 0);
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
            public int getId() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    this.zznJ.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public boolean getRetainInstance() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    final IBinder zznJ = this.zznJ;
                    boolean b = false;
                    zznJ.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        b = true;
                    }
                    return b;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public String getTag() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    this.zznJ.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public int getTargetRequestCode() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    this.zznJ.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public boolean getUserVisibleHint() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    final IBinder zznJ = this.zznJ;
                    boolean b = false;
                    zznJ.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        b = true;
                    }
                    return b;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public zzd getView() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    this.zznJ.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                    return zzd.zza.zzbk(obtain2.readStrongBinder());
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public boolean isAdded() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    final IBinder zznJ = this.zznJ;
                    boolean b = false;
                    zznJ.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        b = true;
                    }
                    return b;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public boolean isDetached() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    final IBinder zznJ = this.zznJ;
                    boolean b = false;
                    zznJ.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        b = true;
                    }
                    return b;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public boolean isHidden() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    final IBinder zznJ = this.zznJ;
                    boolean b = false;
                    zznJ.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        b = true;
                    }
                    return b;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public boolean isInLayout() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    final IBinder zznJ = this.zznJ;
                    boolean b = false;
                    zznJ.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        b = true;
                    }
                    return b;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public boolean isRemoving() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    final IBinder zznJ = this.zznJ;
                    boolean b = false;
                    zznJ.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        b = true;
                    }
                    return b;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public boolean isResumed() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    final IBinder zznJ = this.zznJ;
                    boolean b = false;
                    zznJ.transact(18, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        b = true;
                    }
                    return b;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public boolean isVisible() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    final IBinder zznJ = this.zznJ;
                    boolean b = false;
                    zznJ.transact(19, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        b = true;
                    }
                    return b;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void setHasOptionsMenu(final boolean b) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    obtain.writeInt((int)(b ? 1 : 0));
                    this.zznJ.transact(21, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void setMenuVisibility(final boolean b) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    obtain.writeInt((int)(b ? 1 : 0));
                    this.zznJ.transact(22, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void setRetainInstance(final boolean b) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    obtain.writeInt((int)(b ? 1 : 0));
                    this.zznJ.transact(23, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void setUserVisibleHint(final boolean b) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    obtain.writeInt((int)(b ? 1 : 0));
                    this.zznJ.transact(24, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void startActivity(final Intent intent) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    if (intent != null) {
                        obtain.writeInt(1);
                        intent.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(25, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void startActivityForResult(final Intent intent, final int n) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    if (intent != null) {
                        obtain.writeInt(1);
                        intent.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(n);
                    this.zznJ.transact(26, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void zzn(final zzd zzd) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    IBinder binder;
                    if (zzd != null) {
                        binder = zzd.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    this.zznJ.transact(20, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void zzo(final zzd zzd) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    IBinder binder;
                    if (zzd != null) {
                        binder = zzd.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    this.zznJ.transact(27, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public zzd zzsa() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    this.zznJ.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    return zzd.zza.zzbk(obtain2.readStrongBinder());
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public zzc zzsb() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    this.zznJ.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    return zzc.zza.zzbj(obtain2.readStrongBinder());
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public zzd zzsc() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    this.zznJ.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    return zzd.zza.zzbk(obtain2.readStrongBinder());
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public zzc zzsd() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    this.zznJ.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                    return zzc.zza.zzbj(obtain2.readStrongBinder());
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
