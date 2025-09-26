// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.xperiaxloops;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.IInterface;

public interface IXperiaXLoopsService extends IInterface
{
    void notifyShowLoopsByApps(final boolean p0, final int p1) throws RemoteException;
    
    void registerCallback(final int p0, final IXperiaXLoopsServiceCallback p1) throws RemoteException;
    
    boolean requestAssistEmphasis(final Bundle p0) throws RemoteException;
    
    void sendFPAResult(final int p0, final Bundle p1) throws RemoteException;
    
    void sendKeyguardStatus(final int p0) throws RemoteException;
    
    void sendScreenStatus(final int p0) throws RemoteException;
    
    void setLoopsColorOnLockscreen(final int p0) throws RemoteException;
    
    void unregisterCallback(final int p0, final IXperiaXLoopsServiceCallback p1) throws RemoteException;
    
    public abstract static class Stub extends Binder implements IXperiaXLoopsService
    {
        private static final String DESCRIPTOR = "com.sonymobile.xperiaxloops.IXperiaXLoopsService";
        static final int TRANSACTION_notifyShowLoopsByApps = 8;
        static final int TRANSACTION_registerCallback = 1;
        static final int TRANSACTION_requestAssistEmphasis = 4;
        static final int TRANSACTION_sendFPAResult = 6;
        static final int TRANSACTION_sendKeyguardStatus = 5;
        static final int TRANSACTION_sendScreenStatus = 7;
        static final int TRANSACTION_setLoopsColorOnLockscreen = 3;
        static final int TRANSACTION_unregisterCallback = 2;
        
        public Stub() {
            this.attachInterface((IInterface)this, "com.sonymobile.xperiaxloops.IXperiaXLoopsService");
        }
        
        public static IXperiaXLoopsService asInterface(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.sonymobile.xperiaxloops.IXperiaXLoopsService");
            if (queryLocalInterface != null && queryLocalInterface instanceof IXperiaXLoopsService) {
                return (IXperiaXLoopsService)queryLocalInterface;
            }
            return new Proxy(binder);
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public boolean onTransact(int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n == 1598968902) {
                parcel2.writeString("com.sonymobile.xperiaxloops.IXperiaXLoopsService");
                return true;
            }
            final Bundle bundle = null;
            Bundle bundle2 = null;
            switch (n) {
                default: {
                    return super.onTransact(n, parcel, parcel2, n2);
                }
                case 8: {
                    parcel.enforceInterface("com.sonymobile.xperiaxloops.IXperiaXLoopsService");
                    this.notifyShowLoopsByApps(parcel.readInt() != 0, parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                }
                case 7: {
                    parcel.enforceInterface("com.sonymobile.xperiaxloops.IXperiaXLoopsService");
                    this.sendScreenStatus(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                }
                case 6: {
                    parcel.enforceInterface("com.sonymobile.xperiaxloops.IXperiaXLoopsService");
                    n = parcel.readInt();
                    if (parcel.readInt() != 0) {
                        bundle2 = (Bundle)Bundle.CREATOR.createFromParcel(parcel);
                    }
                    this.sendFPAResult(n, bundle2);
                    parcel2.writeNoException();
                    return true;
                }
                case 5: {
                    parcel.enforceInterface("com.sonymobile.xperiaxloops.IXperiaXLoopsService");
                    this.sendKeyguardStatus(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                }
                case 4: {
                    parcel.enforceInterface("com.sonymobile.xperiaxloops.IXperiaXLoopsService");
                    Bundle bundle3 = bundle;
                    if (parcel.readInt() != 0) {
                        bundle3 = (Bundle)Bundle.CREATOR.createFromParcel(parcel);
                    }
                    n = (this.requestAssistEmphasis(bundle3) ? 1 : 0);
                    parcel2.writeNoException();
                    parcel2.writeInt(n);
                    return true;
                }
                case 3: {
                    parcel.enforceInterface("com.sonymobile.xperiaxloops.IXperiaXLoopsService");
                    this.setLoopsColorOnLockscreen(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                }
                case 2: {
                    parcel.enforceInterface("com.sonymobile.xperiaxloops.IXperiaXLoopsService");
                    this.unregisterCallback(parcel.readInt(), IXperiaXLoopsServiceCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                }
                case 1: {
                    parcel.enforceInterface("com.sonymobile.xperiaxloops.IXperiaXLoopsService");
                    this.registerCallback(parcel.readInt(), IXperiaXLoopsServiceCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                }
            }
        }
        
        private static class Proxy implements IXperiaXLoopsService
        {
            private IBinder mRemote;
            
            Proxy(final IBinder mRemote) {
                this.mRemote = mRemote;
            }
            
            public IBinder asBinder() {
                return this.mRemote;
            }
            
            public String getInterfaceDescriptor() {
                return "com.sonymobile.xperiaxloops.IXperiaXLoopsService";
            }
            
            @Override
            public void notifyShowLoopsByApps(final boolean b, final int n) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.sonymobile.xperiaxloops.IXperiaXLoopsService");
                    obtain.writeInt((int)(b ? 1 : 0));
                    obtain.writeInt(n);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void registerCallback(final int n, final IXperiaXLoopsServiceCallback xperiaXLoopsServiceCallback) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.sonymobile.xperiaxloops.IXperiaXLoopsService");
                    obtain.writeInt(n);
                    IBinder binder;
                    if (xperiaXLoopsServiceCallback != null) {
                        binder = xperiaXLoopsServiceCallback.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public boolean requestAssistEmphasis(final Bundle bundle) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.sonymobile.xperiaxloops.IXperiaXLoopsService");
                    boolean b = true;
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() == 0) {
                        b = false;
                    }
                    return b;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void sendFPAResult(final int n, final Bundle bundle) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.sonymobile.xperiaxloops.IXperiaXLoopsService");
                    obtain.writeInt(n);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void sendKeyguardStatus(final int n) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.sonymobile.xperiaxloops.IXperiaXLoopsService");
                    obtain.writeInt(n);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void sendScreenStatus(final int n) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.sonymobile.xperiaxloops.IXperiaXLoopsService");
                    obtain.writeInt(n);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void setLoopsColorOnLockscreen(final int n) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.sonymobile.xperiaxloops.IXperiaXLoopsService");
                    obtain.writeInt(n);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void unregisterCallback(final int n, final IXperiaXLoopsServiceCallback xperiaXLoopsServiceCallback) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.sonymobile.xperiaxloops.IXperiaXLoopsService");
                    obtain.writeInt(n);
                    IBinder binder;
                    if (xperiaXLoopsServiceCallback != null) {
                        binder = xperiaXLoopsServiceCallback.asBinder();
                    }
                    else {
                        binder = null;
                    }
                    obtain.writeStrongBinder(binder);
                    this.mRemote.transact(2, obtain, obtain2, 0);
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
