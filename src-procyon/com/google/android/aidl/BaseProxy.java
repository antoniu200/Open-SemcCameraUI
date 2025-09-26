// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.aidl;

import android.os.RemoteException;
import android.os.Parcel;
import android.os.IBinder;
import android.os.IInterface;

public abstract class BaseProxy implements IInterface
{
    private final String mDescriptor;
    private final IBinder mRemote;
    
    protected BaseProxy(final IBinder mRemote, final String mDescriptor) {
        this.mRemote = mRemote;
        this.mDescriptor = mDescriptor;
    }
    
    public IBinder asBinder() {
        return this.mRemote;
    }
    
    protected Parcel obtainAndWriteInterfaceToken() {
        final Parcel obtain = Parcel.obtain();
        obtain.writeInterfaceToken(this.mDescriptor);
        return obtain;
    }
    
    protected Parcel transactAndReadException(final int n, final Parcel parcel) throws RemoteException {
        final Parcel obtain = Parcel.obtain();
        try {
            try {
                this.mRemote.transact(n, parcel, obtain, 0);
                obtain.readException();
                parcel.recycle();
                return obtain;
            }
            finally {}
        }
        catch (final RuntimeException ex) {
            final Parcel parcel2;
            parcel2.recycle();
            throw ex;
        }
        parcel.recycle();
    }
    
    protected void transactAndReadExceptionReturnVoid(final int n, final Parcel parcel) throws RemoteException {
        final Parcel obtain = Parcel.obtain();
        try {
            this.mRemote.transact(n, parcel, obtain, 0);
            obtain.readException();
        }
        finally {
            parcel.recycle();
            obtain.recycle();
        }
    }
    
    protected void transactOneway(final int n, final Parcel parcel) throws RemoteException {
        try {
            this.mRemote.transact(n, parcel, (Parcel)null, 1);
        }
        finally {
            parcel.recycle();
        }
    }
}
