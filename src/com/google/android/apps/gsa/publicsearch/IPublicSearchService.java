package com.google.android.apps.gsa.publicsearch;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.aidl.BaseProxy;
import com.google.android.aidl.BaseStub;
import com.google.android.aidl.Codecs;
import com.google.android.apps.gsa.publicsearch.IPublicSearchServiceSession;
import com.google.android.apps.gsa.publicsearch.IPublicSearchServiceSessionCallback;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface IPublicSearchService extends IInterface {
    IPublicSearchServiceSession beginSession(String sessionType, IPublicSearchServiceSessionCallback callback, byte[] serializedSessionContext) throws RemoteException;

    public static abstract class Stub extends BaseStub implements IPublicSearchService {
        private static final String DESCRIPTOR = "com.google.android.apps.gsa.publicsearch.IPublicSearchService";
        static final int TRANSACTION_beginSession = 1;

        public Stub() {
            super(DESCRIPTOR);
        }

        public static IPublicSearchService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = obj.queryLocalInterface(DESCRIPTOR);
            if (iInterfaceQueryLocalInterface instanceof IPublicSearchService) {
                return (IPublicSearchService) iInterfaceQueryLocalInterface;
            }
            return new Proxy(obj);
        }

        @Override // com.google.android.aidl.BaseStub
        protected boolean dispatchTransaction(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code != 1) {
                return false;
            }
            IPublicSearchServiceSession iPublicSearchServiceSessionBeginSession = beginSession(data.readString(), IPublicSearchServiceSessionCallback.Stub.asInterface(data.readStrongBinder()), data.createByteArray());
            reply.writeNoException();
            Codecs.writeStrongBinder(reply, iPublicSearchServiceSessionBeginSession);
            return true;
        }

        public static class Proxy extends BaseProxy implements IPublicSearchService {
            Proxy(IBinder remote) {
                super(remote, Stub.DESCRIPTOR);
            }

            @Override // com.google.android.apps.gsa.publicsearch.IPublicSearchService
            public IPublicSearchServiceSession beginSession(String sessionType, IPublicSearchServiceSessionCallback callback, byte[] serializedSessionContext) throws RemoteException {
                Parcel parcelObtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                parcelObtainAndWriteInterfaceToken.writeString(sessionType);
                Codecs.writeStrongBinder(parcelObtainAndWriteInterfaceToken, callback);
                parcelObtainAndWriteInterfaceToken.writeByteArray(serializedSessionContext);
                Parcel parcelTransactAndReadException = transactAndReadException(1, parcelObtainAndWriteInterfaceToken);
                IPublicSearchServiceSession iPublicSearchServiceSessionAsInterface = IPublicSearchServiceSession.Stub.asInterface(parcelTransactAndReadException.readStrongBinder());
                parcelTransactAndReadException.recycle();
                return iPublicSearchServiceSessionAsInterface;
            }
        }
    }
}
