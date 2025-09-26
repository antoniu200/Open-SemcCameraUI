// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.xperiaxloops;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.os.RemoteException;
import android.os.IInterface;

public interface IXperiaXLoopsServiceCallback extends IInterface
{
    void hide(final boolean p0) throws RemoteException;
    
    void show() throws RemoteException;
    
    public abstract static class Stub extends Binder implements IXperiaXLoopsServiceCallback
    {
        private static final String DESCRIPTOR = "com.sonymobile.xperiaxloops.IXperiaXLoopsServiceCallback";
        static final int TRANSACTION_hide = 2;
        static final int TRANSACTION_show = 1;
        
        public Stub() {
            this.attachInterface((IInterface)this, "com.sonymobile.xperiaxloops.IXperiaXLoopsServiceCallback");
        }
        
        public static IXperiaXLoopsServiceCallback asInterface(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.sonymobile.xperiaxloops.IXperiaXLoopsServiceCallback");
            if (queryLocalInterface != null && queryLocalInterface instanceof IXperiaXLoopsServiceCallback) {
                return (IXperiaXLoopsServiceCallback)queryLocalInterface;
            }
            return new Proxy(binder);
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n == 1598968902) {
                parcel2.writeString("com.sonymobile.xperiaxloops.IXperiaXLoopsServiceCallback");
                return true;
            }
            switch (n) {
                default: {
                    return super.onTransact(n, parcel, parcel2, n2);
                }
                case 2: {
                    parcel.enforceInterface("com.sonymobile.xperiaxloops.IXperiaXLoopsServiceCallback");
                    this.hide(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                }
                case 1: {
                    parcel.enforceInterface("com.sonymobile.xperiaxloops.IXperiaXLoopsServiceCallback");
                    this.show();
                    parcel2.writeNoException();
                    return true;
                }
            }
        }
        
        private static class Proxy implements IXperiaXLoopsServiceCallback
        {
            private IBinder mRemote;
            
            Proxy(final IBinder mRemote) {
                this.mRemote = mRemote;
            }
            
            public IBinder asBinder() {
                return this.mRemote;
            }
            
            public String getInterfaceDescriptor() {
                return "com.sonymobile.xperiaxloops.IXperiaXLoopsServiceCallback";
            }
            
            @Override
            public void hide(final boolean b) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.sonymobile.xperiaxloops.IXperiaXLoopsServiceCallback");
                    obtain.writeInt((int)(b ? 1 : 0));
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public void show() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.sonymobile.xperiaxloops.IXperiaXLoopsServiceCallback");
                    this.mRemote.transact(1, obtain, obtain2, 0);
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
