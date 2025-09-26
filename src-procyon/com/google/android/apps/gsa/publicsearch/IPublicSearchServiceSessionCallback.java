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

public interface IPublicSearchServiceSessionCallback extends IInterface
{
    void onServiceEvent(final byte[] p0, final SystemParcelableWrapper p1) throws RemoteException;
    
    public abstract static class Stub extends BaseStub implements IPublicSearchServiceSessionCallback
    {
        private static final String DESCRIPTOR = "com.google.android.apps.gsa.publicsearch.IPublicSearchServiceSessionCallback";
        static final int TRANSACTION_onServiceEvent = 1;
        
        public Stub() {
            super("com.google.android.apps.gsa.publicsearch.IPublicSearchServiceSessionCallback");
        }
        
        public static IPublicSearchServiceSessionCallback asInterface(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.apps.gsa.publicsearch.IPublicSearchServiceSessionCallback");
            if (queryLocalInterface instanceof IPublicSearchServiceSessionCallback) {
                return (IPublicSearchServiceSessionCallback)queryLocalInterface;
            }
            return new Proxy(binder);
        }
        
        @Override
        protected boolean dispatchTransaction(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n == 1) {
                this.onServiceEvent(parcel.createByteArray(), Codecs.createParcelable(parcel, SystemParcelableWrapper.CREATOR));
                return true;
            }
            return false;
        }
        
        public static class Proxy extends BaseProxy implements IPublicSearchServiceSessionCallback
        {
            Proxy(final IBinder binder) {
                super(binder, "com.google.android.apps.gsa.publicsearch.IPublicSearchServiceSessionCallback");
            }
            
            @Override
            public void onServiceEvent(final byte[] array, final SystemParcelableWrapper systemParcelableWrapper) throws RemoteException {
                final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
                obtainAndWriteInterfaceToken.writeByteArray(array);
                Codecs.writeParcelable(obtainAndWriteInterfaceToken, (Parcelable)systemParcelableWrapper);
                this.transactOneway(1, obtainAndWriteInterfaceToken);
            }
        }
    }
}
