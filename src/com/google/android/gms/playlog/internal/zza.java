package com.google.android.gms.playlog.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface zza extends IInterface {

    /* renamed from: com.google.android.gms.playlog.internal.zza$zza, reason: collision with other inner class name */
    public static abstract class AbstractBinderC0031zza extends Binder implements zza {

        /* renamed from: com.google.android.gms.playlog.internal.zza$zza$zza, reason: collision with other inner class name */
        private static class C0032zza implements zza {
            private IBinder zznJ;

            C0032zza(IBinder iBinder) {
                this.zznJ = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.zznJ;
            }

            @Override // com.google.android.gms.playlog.internal.zza
            public void zza(String str, PlayLoggerContext playLoggerContext, LogEvent logEvent) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.playlog.internal.IPlayLogService");
                    parcelObtain.writeString(str);
                    if (playLoggerContext != null) {
                        parcelObtain.writeInt(1);
                        playLoggerContext.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    if (logEvent != null) {
                        parcelObtain.writeInt(1);
                        logEvent.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    this.zznJ.transact(2, parcelObtain, null, 1);
                } finally {
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.playlog.internal.zza
            public void zza(String str, PlayLoggerContext playLoggerContext, List<LogEvent> list) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.playlog.internal.IPlayLogService");
                    parcelObtain.writeString(str);
                    if (playLoggerContext != null) {
                        parcelObtain.writeInt(1);
                        playLoggerContext.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    parcelObtain.writeTypedList(list);
                    this.zznJ.transact(3, parcelObtain, null, 1);
                } finally {
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.playlog.internal.zza
            public void zza(String str, PlayLoggerContext playLoggerContext, byte[] bArr) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.playlog.internal.IPlayLogService");
                    parcelObtain.writeString(str);
                    if (playLoggerContext != null) {
                        parcelObtain.writeInt(1);
                        playLoggerContext.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    parcelObtain.writeByteArray(bArr);
                    this.zznJ.transact(4, parcelObtain, null, 1);
                } finally {
                    parcelObtain.recycle();
                }
            }
        }

        public static zza zzdz(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.playlog.internal.IPlayLogService");
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof zza)) ? new C0032zza(iBinder) : (zza) iInterfaceQueryLocalInterface;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1598968902) {
                parcel2.writeString("com.google.android.gms.playlog.internal.IPlayLogService");
                return true;
            }
            switch (i) {
                case 2:
                    parcel.enforceInterface("com.google.android.gms.playlog.internal.IPlayLogService");
                    zza(parcel.readString(), parcel.readInt() != 0 ? PlayLoggerContext.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0 ? LogEvent.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case 3:
                    parcel.enforceInterface("com.google.android.gms.playlog.internal.IPlayLogService");
                    zza(parcel.readString(), parcel.readInt() != 0 ? PlayLoggerContext.CREATOR.createFromParcel(parcel) : null, parcel.createTypedArrayList(LogEvent.CREATOR));
                    return true;
                case 4:
                    parcel.enforceInterface("com.google.android.gms.playlog.internal.IPlayLogService");
                    zza(parcel.readString(), parcel.readInt() != 0 ? PlayLoggerContext.CREATOR.createFromParcel(parcel) : null, parcel.createByteArray());
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void zza(String str, PlayLoggerContext playLoggerContext, LogEvent logEvent) throws RemoteException;

    void zza(String str, PlayLoggerContext playLoggerContext, List<LogEvent> list) throws RemoteException;

    void zza(String str, PlayLoggerContext playLoggerContext, byte[] bArr) throws RemoteException;
}
