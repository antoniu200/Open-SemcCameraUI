package com.google.android.aidl;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class BaseProxy implements IInterface {
    private final String mDescriptor;
    private final IBinder mRemote;

    protected BaseProxy(IBinder remote, String descriptor) {
        this.mRemote = remote;
        this.mDescriptor = descriptor;
    }

    @Override // android.os.IInterface
    public IBinder asBinder() {
        return this.mRemote;
    }

    protected Parcel obtainAndWriteInterfaceToken() {
        Parcel parcelObtain = Parcel.obtain();
        parcelObtain.writeInterfaceToken(this.mDescriptor);
        return parcelObtain;
    }

    protected Parcel transactAndReadException(int code, Parcel in) throws RemoteException {
        Parcel parcelObtain = Parcel.obtain();
        try {
            try {
                this.mRemote.transact(code, in, parcelObtain, 0);
                parcelObtain.readException();
                return parcelObtain;
            } catch (RuntimeException e) {
                parcelObtain.recycle();
                throw e;
            }
        } finally {
            in.recycle();
        }
    }

    protected void transactAndReadExceptionReturnVoid(int code, Parcel in) throws RemoteException {
        Parcel parcelObtain = Parcel.obtain();
        try {
            this.mRemote.transact(code, in, parcelObtain, 0);
            parcelObtain.readException();
        } finally {
            in.recycle();
            parcelObtain.recycle();
        }
    }

    protected void transactOneway(int code, Parcel in) throws RemoteException {
        try {
            this.mRemote.transact(code, in, null, 1);
        } finally {
            in.recycle();
        }
    }
}
