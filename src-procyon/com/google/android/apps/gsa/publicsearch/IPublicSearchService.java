// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.apps.gsa.publicsearch;

import com.google.android.aidl.BaseProxy;
import com.google.android.aidl.Codecs;
import android.os.Parcel;
import android.os.IBinder;
import com.google.android.aidl.BaseStub;
import android.os.RemoteException;
import android.os.IInterface;

public interface IPublicSearchService extends IInterface
{
    IPublicSearchServiceSession beginSession(final String p0, final IPublicSearchServiceSessionCallback p1, final byte[] p2) throws RemoteException;
    
    public abstract static class Stub extends BaseStub implements IPublicSearchService
    {
        private static final String DESCRIPTOR = "com.google.android.apps.gsa.publicsearch.IPublicSearchService";
        static final int TRANSACTION_beginSession = 1;
        
        public Stub() {
            super("com.google.android.apps.gsa.publicsearch.IPublicSearchService");
        }
        
        public static IPublicSearchService asInterface(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.apps.gsa.publicsearch.IPublicSearchService");
            if (queryLocalInterface instanceof IPublicSearchService) {
                return (IPublicSearchService)queryLocalInterface;
            }
            return new Proxy(binder);
        }
        
        @Override
        protected boolean dispatchTransaction(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n == 1) {
                final IPublicSearchServiceSession beginSession = this.beginSession(parcel.readString(), IPublicSearchServiceSessionCallback.Stub.asInterface(parcel.readStrongBinder()), parcel.createByteArray());
                parcel2.writeNoException();
                Codecs.writeStrongBinder(parcel2, (IInterface)beginSession);
                return true;
            }
            return false;
        }
        
        public static class Proxy extends BaseProxy implements IPublicSearchService
        {
            Proxy(final IBinder binder) {
                super(binder, "com.google.android.apps.gsa.publicsearch.IPublicSearchService");
            }
            
            @Override
            public IPublicSearchServiceSession beginSession(final String s, final IPublicSearchServiceSessionCallback publicSearchServiceSessionCallback, final byte[] array) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                obtainAndWriteInterfaceToken.writeString(s);
                Codecs.writeStrongBinder(obtainAndWriteInterfaceToken, (IInterface)publicSearchServiceSessionCallback);
                obtainAndWriteInterfaceToken.writeByteArray(array);
                final Parcel transactAndReadException = this.transactAndReadException(1, obtainAndWriteInterfaceToken);
                final IPublicSearchServiceSession interface1 = IPublicSearchServiceSession.Stub.asInterface(transactAndReadException.readStrongBinder());
                transactAndReadException.recycle();
                return interface1;
            }
        }
    }
}
