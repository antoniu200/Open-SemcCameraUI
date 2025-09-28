package com.google.android.gms.signin.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.signin.internal.zzf;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface zzd extends IInterface {

    public static abstract class zza extends Binder implements zzd {
        public zza() {
            attachInterface(this, "com.google.android.gms.signin.internal.IOfflineAccessCallbacks");
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1598968902) {
                parcel2.writeString("com.google.android.gms.signin.internal.IOfflineAccessCallbacks");
                return true;
            }
            switch (i) {
                case 2:
                    parcel.enforceInterface("com.google.android.gms.signin.internal.IOfflineAccessCallbacks");
                    zza(parcel.readString(), parcel.createTypedArrayList(Scope.CREATOR), zzf.zza.zzdN(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 3:
                    parcel.enforceInterface("com.google.android.gms.signin.internal.IOfflineAccessCallbacks");
                    zza(parcel.readString(), parcel.readString(), zzf.zza.zzdN(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void zza(String str, String str2, zzf zzfVar) throws RemoteException;

    void zza(String str, List<Scope> list, zzf zzfVar) throws RemoteException;
}
