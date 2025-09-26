// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.apps.gsa.publicsearch;

import android.os.Parcelable;
import com.google.android.aidl.BaseProxy;
import com.google.android.aidl.Codecs;
import android.os.Parcel;
import android.os.IBinder;
import com.google.android.aidl.BaseStub;
import android.os.RemoteException;
import android.os.IInterface;

public interface IPublicSearchServiceSession extends IInterface
{
    void onGenericClientEvent(final byte[] p0) throws RemoteException;
    
    void onGenericClientEventWithSystemParcelable(final byte[] p0, final SystemParcelableWrapper p1) throws RemoteException;
    
    public abstract static class Stub extends BaseStub implements IPublicSearchServiceSession
    {
        private static final String DESCRIPTOR = "com.google.android.apps.gsa.publicsearch.IPublicSearchServiceSession";
        static final int TRANSACTION_onGenericClientEvent = 1;
        static final int TRANSACTION_onGenericClientEventWithSystemParcelable = 2;
        
        public Stub() {
            super("com.google.android.apps.gsa.publicsearch.IPublicSearchServiceSession");
        }
        
        public static IPublicSearchServiceSession asInterface(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.apps.gsa.publicsearch.IPublicSearchServiceSession");
            if (queryLocalInterface instanceof IPublicSearchServiceSession) {
                return (IPublicSearchServiceSession)queryLocalInterface;
            }
            return new Proxy(binder);
        }
        
        @Override
        protected boolean dispatchTransaction(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            switch (n) {
                default: {
                    return false;
                }
                case 2: {
                    this.onGenericClientEventWithSystemParcelable(parcel.createByteArray(), Codecs.createParcelable(parcel, SystemParcelableWrapper.CREATOR));
                    break;
                }
                case 1: {
                    this.onGenericClientEvent(parcel.createByteArray());
                    break;
                }
            }
            return true;
        }
        
        public static class Proxy extends BaseProxy implements IPublicSearchServiceSession
        {
            Proxy(final IBinder binder) {
                super(binder, "com.google.android.apps.gsa.publicsearch.IPublicSearchServiceSession");
            }
            
            @Override
            public void onGenericClientEvent(final byte[] array) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                obtainAndWriteInterfaceToken.writeByteArray(array);
                this.transactOneway(1, obtainAndWriteInterfaceToken);
            }
            
            @Override
            public void onGenericClientEventWithSystemParcelable(final byte[] array, final SystemParcelableWrapper systemParcelableWrapper) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                obtainAndWriteInterfaceToken.writeByteArray(array);
                Codecs.writeParcelable(obtainAndWriteInterfaceToken, (Parcelable)systemParcelableWrapper);
                this.transactOneway(2, obtainAndWriteInterfaceToken);
            }
        }
    }
}
