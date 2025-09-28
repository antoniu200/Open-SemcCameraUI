package com.google.android.gms.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.zzmc;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface zzmd extends IInterface {

    public static abstract class zza extends Binder implements zzmd {

        /* renamed from: com.google.android.gms.internal.zzmd$zza$zza, reason: collision with other inner class name */
        private static class C0030zza implements zzmd {
            private IBinder zznJ;

            C0030zza(IBinder iBinder) {
                this.zznJ = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.zznJ;
            }

            @Override // com.google.android.gms.internal.zzmd
            public void zza(zzmc zzmcVar) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.common.internal.service.ICommonService");
                    parcelObtain.writeStrongBinder(zzmcVar != null ? zzmcVar.asBinder() : null);
                    this.zznJ.transact(1, parcelObtain, null, 1);
                } finally {
                    parcelObtain.recycle();
                }
            }
        }

        public static zzmd zzaQ(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.service.ICommonService");
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof zzmd)) ? new C0030zza(iBinder) : (zzmd) iInterfaceQueryLocalInterface;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                parcel.enforceInterface("com.google.android.gms.common.internal.service.ICommonService");
                zza(zzmc.zza.zzaP(parcel.readStrongBinder()));
                return true;
            }
            if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            }
            parcel2.writeString("com.google.android.gms.common.internal.service.ICommonService");
            return true;
        }
    }

    void zza(zzmc zzmcVar) throws RemoteException;
}
