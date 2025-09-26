package com.sonyericsson.psm.sysmonservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface ISysmonService extends IInterface {
    int getCameraLowTempBurnTimeoutSec() throws RemoteException;

    int getThermalLevelForCamera() throws RemoteException;

    int getThermalLevelForFs1seg() throws RemoteException;

    public static abstract class Stub extends Binder implements ISysmonService {
        private static final String DESCRIPTOR = "com.sonyericsson.psm.sysmonservice.ISysmonService";
        static final int TRANSACTION_getCameraLowTempBurnTimeoutSec = 3;
        static final int TRANSACTION_getThermalLevelForCamera = 1;
        static final int TRANSACTION_getThermalLevelForFs1seg = 2;

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISysmonService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (iInterfaceQueryLocalInterface != null && (iInterfaceQueryLocalInterface instanceof ISysmonService)) {
                return (ISysmonService) iInterfaceQueryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1598968902) {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    int thermalLevelForCamera = getThermalLevelForCamera();
                    parcel2.writeNoException();
                    parcel2.writeInt(thermalLevelForCamera);
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    int thermalLevelForFs1seg = getThermalLevelForFs1seg();
                    parcel2.writeNoException();
                    parcel2.writeInt(thermalLevelForFs1seg);
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    int cameraLowTempBurnTimeoutSec = getCameraLowTempBurnTimeoutSec();
                    parcel2.writeNoException();
                    parcel2.writeInt(cameraLowTempBurnTimeoutSec);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }

        private static class Proxy implements ISysmonService {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.sonyericsson.psm.sysmonservice.ISysmonService
            public int getThermalLevelForCamera() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sonyericsson.psm.sysmonservice.ISysmonService
            public int getThermalLevelForFs1seg() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sonyericsson.psm.sysmonservice.ISysmonService
            public int getCameraLowTempBurnTimeoutSec() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }
    }
}
