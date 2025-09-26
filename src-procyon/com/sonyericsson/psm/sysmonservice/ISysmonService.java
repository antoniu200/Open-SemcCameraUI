// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.psm.sysmonservice;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.os.RemoteException;
import android.os.IInterface;

public interface ISysmonService extends IInterface
{
    int getCameraLowTempBurnTimeoutSec() throws RemoteException;
    
    int getThermalLevelForCamera() throws RemoteException;
    
    int getThermalLevelForFs1seg() throws RemoteException;
    
    public abstract static class Stub extends Binder implements ISysmonService
    {
        private static final String DESCRIPTOR = "com.sonyericsson.psm.sysmonservice.ISysmonService";
        static final int TRANSACTION_getCameraLowTempBurnTimeoutSec = 3;
        static final int TRANSACTION_getThermalLevelForCamera = 1;
        static final int TRANSACTION_getThermalLevelForFs1seg = 2;
        
        public Stub() {
            this.attachInterface((IInterface)this, "com.sonyericsson.psm.sysmonservice.ISysmonService");
        }
        
        public static ISysmonService asInterface(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.sonyericsson.psm.sysmonservice.ISysmonService");
            if (queryLocalInterface != null && queryLocalInterface instanceof ISysmonService) {
                return (ISysmonService)queryLocalInterface;
            }
            return new Proxy(binder);
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public boolean onTransact(int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n == 1598968902) {
                parcel2.writeString("com.sonyericsson.psm.sysmonservice.ISysmonService");
                return true;
            }
            switch (n) {
                default: {
                    return super.onTransact(n, parcel, parcel2, n2);
                }
                case 3: {
                    parcel.enforceInterface("com.sonyericsson.psm.sysmonservice.ISysmonService");
                    n = this.getCameraLowTempBurnTimeoutSec();
                    parcel2.writeNoException();
                    parcel2.writeInt(n);
                    return true;
                }
                case 2: {
                    parcel.enforceInterface("com.sonyericsson.psm.sysmonservice.ISysmonService");
                    n = this.getThermalLevelForFs1seg();
                    parcel2.writeNoException();
                    parcel2.writeInt(n);
                    return true;
                }
                case 1: {
                    parcel.enforceInterface("com.sonyericsson.psm.sysmonservice.ISysmonService");
                    n = this.getThermalLevelForCamera();
                    parcel2.writeNoException();
                    parcel2.writeInt(n);
                    return true;
                }
            }
        }
        
        private static class Proxy implements ISysmonService
        {
            private IBinder mRemote;
            
            Proxy(final IBinder mRemote) {
                this.mRemote = mRemote;
            }
            
            public IBinder asBinder() {
                return this.mRemote;
            }
            
            @Override
            public int getCameraLowTempBurnTimeoutSec() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.sonyericsson.psm.sysmonservice.ISysmonService");
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            public String getInterfaceDescriptor() {
                return "com.sonyericsson.psm.sysmonservice.ISysmonService";
            }
            
            @Override
            public int getThermalLevelForCamera() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.sonyericsson.psm.sysmonservice.ISysmonService");
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
            
            @Override
            public int getThermalLevelForFs1seg() throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.sonyericsson.psm.sysmonservice.ISysmonService");
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
