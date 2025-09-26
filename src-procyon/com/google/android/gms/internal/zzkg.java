// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.os.RemoteException;
import android.content.Intent;
import com.google.android.gms.auth.api.consent.GetConsentIntentRequest;
import android.os.IInterface;

public interface zzkg extends IInterface
{
    Intent zza(final GetConsentIntentRequest p0) throws RemoteException;
    
    public abstract static class zza extends Binder implements zzkg
    {
        public static zzkg zzaq(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.auth.api.consent.internal.IConsentService");
            if (queryLocalInterface != null && queryLocalInterface instanceof zzkg) {
                return (zzkg)queryLocalInterface;
            }
            return new zzkg.zza.zza(binder);
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n != 1) {
                if (n != 1598968902) {
                    return super.onTransact(n, parcel, parcel2, n2);
                }
                parcel2.writeString("com.google.android.gms.auth.api.consent.internal.IConsentService");
                return true;
            }
            else {
                parcel.enforceInterface("com.google.android.gms.auth.api.consent.internal.IConsentService");
                GetConsentIntentRequest getConsentIntentRequest;
                if (parcel.readInt() != 0) {
                    getConsentIntentRequest = (GetConsentIntentRequest)GetConsentIntentRequest.CREATOR.createFromParcel(parcel);
                }
                else {
                    getConsentIntentRequest = null;
                }
                final Intent zza = this.zza(getConsentIntentRequest);
                parcel2.writeNoException();
                if (zza != null) {
                    parcel2.writeInt(1);
                    zza.writeToParcel(parcel2, 1);
                    return true;
                }
                parcel2.writeInt(0);
                return true;
            }
        }
        
        private static class zza implements zzkg
        {
            private IBinder zznJ;
            
            zza(final IBinder zznJ) {
                this.zznJ = zznJ;
            }
            
            public IBinder asBinder() {
                return this.zznJ;
            }
            
            @Override
            public Intent zza(final GetConsentIntentRequest getConsentIntentRequest) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                final Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.auth.api.consent.internal.IConsentService");
                    if (getConsentIntentRequest != null) {
                        obtain.writeInt(1);
                        getConsentIntentRequest.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    Intent intent;
                    if (obtain2.readInt() != 0) {
                        intent = (Intent)Intent.CREATOR.createFromParcel(obtain2);
                    }
                    else {
                        intent = null;
                    }
                    return intent;
                }
                finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
