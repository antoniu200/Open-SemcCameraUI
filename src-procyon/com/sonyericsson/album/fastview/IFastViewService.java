// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.album.fastview;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.os.RemoteException;
import android.net.Uri;
import android.os.IInterface;

public interface IFastViewService extends IInterface
{
    void prepare(final Uri p0) throws RemoteException;
    
    public abstract static class Stub extends Binder implements IFastViewService
    {
        private static final String DESCRIPTOR = "com.sonyericsson.album.fastview.IFastViewService";
        static final int TRANSACTION_prepare = 1;
        
        public Stub() {
            this.attachInterface((IInterface)this, "com.sonyericsson.album.fastview.IFastViewService");
        }
        
        public static IFastViewService asInterface(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.sonyericsson.album.fastview.IFastViewService");
            if (queryLocalInterface != null && queryLocalInterface instanceof IFastViewService) {
                return (IFastViewService)queryLocalInterface;
            }
            return new Proxy(binder);
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n == 1) {
                parcel.enforceInterface("com.sonyericsson.album.fastview.IFastViewService");
                Uri uri;
                if (parcel.readInt() != 0) {
                    uri = (Uri)Uri.CREATOR.createFromParcel(parcel);
                }
                else {
                    uri = null;
                }
                this.prepare(uri);
                parcel2.writeNoException();
                return true;
            }
            if (n != 1598968902) {
                return super.onTransact(n, parcel, parcel2, n2);
            }
            parcel2.writeString("com.sonyericsson.album.fastview.IFastViewService");
            return true;
        }
        
        private static class Proxy implements IFastViewService
        {
            private IBinder mRemote;
            
            Proxy(final IBinder mRemote) {
                this.mRemote = mRemote;
            }
            
            public IBinder asBinder() {
                return this.mRemote;
            }
            
            public String getInterfaceDescriptor() {
                return "com.sonyericsson.album.fastview.IFastViewService";
            }
            
            @Override
            public void prepare(final Uri uri) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.sonyericsson.album.fastview.IFastViewService");
                    if (uri != null) {
                        obtain.writeInt(1);
                        uri.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
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
