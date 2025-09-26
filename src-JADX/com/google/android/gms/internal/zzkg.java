package com.google.android.gms.internal;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.auth.api.consent.GetConsentIntentRequest;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface zzkg extends IInterface {

    public static abstract class zza extends Binder implements zzkg {

        /* renamed from: com.google.android.gms.internal.zzkg$zza$zza, reason: collision with other inner class name */
        private static class C0024zza implements zzkg {
            private IBinder zznJ;

            C0024zza(IBinder iBinder) {
                this.zznJ = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.zznJ;
            }

            @Override // com.google.android.gms.internal.zzkg
            public Intent zza(GetConsentIntentRequest getConsentIntentRequest) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.auth.api.consent.internal.IConsentService");
                    if (getConsentIntentRequest != null) {
                        parcelObtain.writeInt(1);
                        getConsentIntentRequest.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    this.zznJ.transact(1, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(parcelObtain2) : null;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }

        public static zzkg zzaq(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.auth.api.consent.internal.IConsentService");
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof zzkg)) ? new C0024zza(iBinder) : (zzkg) iInterfaceQueryLocalInterface;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i != 1) {
                if (i != 1598968902) {
                    return super.onTransact(i, parcel, parcel2, i2);
                }
                parcel2.writeString("com.google.android.gms.auth.api.consent.internal.IConsentService");
                return true;
            }
            parcel.enforceInterface("com.google.android.gms.auth.api.consent.internal.IConsentService");
            Intent intentZza = zza(parcel.readInt() != 0 ? GetConsentIntentRequest.CREATOR.createFromParcel(parcel) : null);
            parcel2.writeNoException();
            if (intentZza == null) {
                parcel2.writeInt(0);
                return true;
            }
            parcel2.writeInt(1);
            intentZza.writeToParcel(parcel2, 1);
            return true;
        }
    }

    Intent zza(GetConsentIntentRequest getConsentIntentRequest) throws RemoteException;
}
