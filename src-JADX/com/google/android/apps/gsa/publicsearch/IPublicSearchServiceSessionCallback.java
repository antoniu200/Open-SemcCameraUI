package com.google.android.apps.gsa.publicsearch;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.aidl.BaseProxy;
import com.google.android.aidl.BaseStub;
import com.google.android.aidl.Codecs;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface IPublicSearchServiceSessionCallback extends IInterface {
    void onServiceEvent(byte[] serializedServiceEvent, SystemParcelableWrapper systemParcelableWrapper) throws RemoteException;

    public static abstract class Stub extends BaseStub implements IPublicSearchServiceSessionCallback {
        private static final String DESCRIPTOR = "com.google.android.apps.gsa.publicsearch.IPublicSearchServiceSessionCallback";
        static final int TRANSACTION_onServiceEvent = 1;

        public Stub() {
            super(DESCRIPTOR);
        }

        public static IPublicSearchServiceSessionCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = obj.queryLocalInterface(DESCRIPTOR);
            if (iInterfaceQueryLocalInterface instanceof IPublicSearchServiceSessionCallback) {
                return (IPublicSearchServiceSessionCallback) iInterfaceQueryLocalInterface;
            }
            return new Proxy(obj);
        }

        @Override // com.google.android.aidl.BaseStub
        protected boolean dispatchTransaction(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code != 1) {
                return false;
            }
            onServiceEvent(data.createByteArray(), (SystemParcelableWrapper) Codecs.createParcelable(data, SystemParcelableWrapper.CREATOR));
            return true;
        }

        public static class Proxy extends BaseProxy implements IPublicSearchServiceSessionCallback {
            Proxy(IBinder remote) {
                super(remote, Stub.DESCRIPTOR);
            }

            @Override // com.google.android.apps.gsa.publicsearch.IPublicSearchServiceSessionCallback
            public void onServiceEvent(byte[] serializedServiceEvent, SystemParcelableWrapper systemParcelableWrapper) throws RemoteException {
                Parcel parcelObtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                parcelObtainAndWriteInterfaceToken.writeByteArray(serializedServiceEvent);
                Codecs.writeParcelable(parcelObtainAndWriteInterfaceToken, systemParcelableWrapper);
                transactOneway(1, parcelObtainAndWriteInterfaceToken);
            }
        }
    }
}
