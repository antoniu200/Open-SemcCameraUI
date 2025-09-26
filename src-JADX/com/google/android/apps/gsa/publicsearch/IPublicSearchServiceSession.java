package com.google.android.apps.gsa.publicsearch;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.aidl.BaseProxy;
import com.google.android.aidl.BaseStub;
import com.google.android.aidl.Codecs;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface IPublicSearchServiceSession extends IInterface {
    void onGenericClientEvent(byte[] serializedClientEvent) throws RemoteException;

    void onGenericClientEventWithSystemParcelable(byte[] serializedClientEvent, SystemParcelableWrapper systemParcelableWrapper) throws RemoteException;

    public static abstract class Stub extends BaseStub implements IPublicSearchServiceSession {
        private static final String DESCRIPTOR = "com.google.android.apps.gsa.publicsearch.IPublicSearchServiceSession";
        static final int TRANSACTION_onGenericClientEvent = 1;
        static final int TRANSACTION_onGenericClientEventWithSystemParcelable = 2;

        public Stub() {
            super(DESCRIPTOR);
        }

        public static IPublicSearchServiceSession asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = obj.queryLocalInterface(DESCRIPTOR);
            if (iInterfaceQueryLocalInterface instanceof IPublicSearchServiceSession) {
                return (IPublicSearchServiceSession) iInterfaceQueryLocalInterface;
            }
            return new Proxy(obj);
        }

        @Override // com.google.android.aidl.BaseStub
        protected boolean dispatchTransaction(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    onGenericClientEvent(data.createByteArray());
                    return true;
                case 2:
                    onGenericClientEventWithSystemParcelable(data.createByteArray(), (SystemParcelableWrapper) Codecs.createParcelable(data, SystemParcelableWrapper.CREATOR));
                    return true;
                default:
                    return false;
            }
        }

        public static class Proxy extends BaseProxy implements IPublicSearchServiceSession {
            Proxy(IBinder remote) {
                super(remote, Stub.DESCRIPTOR);
            }

            @Override // com.google.android.apps.gsa.publicsearch.IPublicSearchServiceSession
            public void onGenericClientEvent(byte[] serializedClientEvent) throws RemoteException {
                Parcel parcelObtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                parcelObtainAndWriteInterfaceToken.writeByteArray(serializedClientEvent);
                transactOneway(1, parcelObtainAndWriteInterfaceToken);
            }

            @Override // com.google.android.apps.gsa.publicsearch.IPublicSearchServiceSession
            public void onGenericClientEventWithSystemParcelable(byte[] serializedClientEvent, SystemParcelableWrapper systemParcelableWrapper) throws RemoteException {
                Parcel parcelObtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                parcelObtainAndWriteInterfaceToken.writeByteArray(serializedClientEvent);
                Codecs.writeParcelable(parcelObtainAndWriteInterfaceToken, systemParcelableWrapper);
                transactOneway(2, parcelObtainAndWriteInterfaceToken);
            }
        }
    }
}
