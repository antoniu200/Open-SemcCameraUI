package com.google.android.gms.dynamic;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.zzd;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface zzc extends IInterface {

    public static abstract class zza extends Binder implements zzc {

        /* renamed from: com.google.android.gms.dynamic.zzc$zza$zza, reason: collision with other inner class name */
        private static class C0019zza implements zzc {
            private IBinder zznJ;

            C0019zza(IBinder iBinder) {
                this.zznJ = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.zznJ;
            }

            @Override // com.google.android.gms.dynamic.zzc
            public Bundle getArguments() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    this.zznJ.transact(3, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcelObtain2) : null;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.dynamic.zzc
            public int getId() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    this.zznJ.transact(4, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.dynamic.zzc
            public boolean getRetainInstance() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    this.zznJ.transact(7, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.dynamic.zzc
            public String getTag() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    this.zznJ.transact(8, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readString();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.dynamic.zzc
            public int getTargetRequestCode() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    this.zznJ.transact(10, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.dynamic.zzc
            public boolean getUserVisibleHint() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    this.zznJ.transact(11, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.dynamic.zzc
            public zzd getView() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    this.zznJ.transact(12, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return zzd.zza.zzbk(parcelObtain2.readStrongBinder());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.dynamic.zzc
            public boolean isAdded() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    this.zznJ.transact(13, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.dynamic.zzc
            public boolean isDetached() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    this.zznJ.transact(14, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.dynamic.zzc
            public boolean isHidden() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    this.zznJ.transact(15, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.dynamic.zzc
            public boolean isInLayout() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    this.zznJ.transact(16, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.dynamic.zzc
            public boolean isRemoving() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    this.zznJ.transact(17, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.dynamic.zzc
            public boolean isResumed() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    this.zznJ.transact(18, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.dynamic.zzc
            public boolean isVisible() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    this.zznJ.transact(19, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.dynamic.zzc
            public void setHasOptionsMenu(boolean z) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    parcelObtain.writeInt(z ? 1 : 0);
                    this.zznJ.transact(21, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.dynamic.zzc
            public void setMenuVisibility(boolean z) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    parcelObtain.writeInt(z ? 1 : 0);
                    this.zznJ.transact(22, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.dynamic.zzc
            public void setRetainInstance(boolean z) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    parcelObtain.writeInt(z ? 1 : 0);
                    this.zznJ.transact(23, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.dynamic.zzc
            public void setUserVisibleHint(boolean z) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    parcelObtain.writeInt(z ? 1 : 0);
                    this.zznJ.transact(24, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.dynamic.zzc
            public void startActivity(Intent intent) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    if (intent != null) {
                        parcelObtain.writeInt(1);
                        intent.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    this.zznJ.transact(25, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.dynamic.zzc
            public void startActivityForResult(Intent intent, int i) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    if (intent != null) {
                        parcelObtain.writeInt(1);
                        intent.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    parcelObtain.writeInt(i);
                    this.zznJ.transact(26, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.dynamic.zzc
            public void zzn(zzd zzdVar) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    parcelObtain.writeStrongBinder(zzdVar != null ? zzdVar.asBinder() : null);
                    this.zznJ.transact(20, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.dynamic.zzc
            public void zzo(zzd zzdVar) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    parcelObtain.writeStrongBinder(zzdVar != null ? zzdVar.asBinder() : null);
                    this.zznJ.transact(27, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.dynamic.zzc
            public zzd zzsa() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    this.zznJ.transact(2, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return zzd.zza.zzbk(parcelObtain2.readStrongBinder());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.dynamic.zzc
            public zzc zzsb() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    this.zznJ.transact(5, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return zza.zzbj(parcelObtain2.readStrongBinder());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.dynamic.zzc
            public zzd zzsc() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    this.zznJ.transact(6, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return zzd.zza.zzbk(parcelObtain2.readStrongBinder());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.google.android.gms.dynamic.zzc
            public zzc zzsd() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken("com.google.android.gms.dynamic.IFragmentWrapper");
                    this.zznJ.transact(9, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return zza.zzbj(parcelObtain2.readStrongBinder());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }

        public zza() {
            attachInterface(this, "com.google.android.gms.dynamic.IFragmentWrapper");
        }

        public static zzc zzbj(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.dynamic.IFragmentWrapper");
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof zzc)) ? new C0019zza(iBinder) : (zzc) iInterfaceQueryLocalInterface;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1598968902) {
                parcel2.writeString("com.google.android.gms.dynamic.IFragmentWrapper");
                return true;
            }
            switch (i) {
                case 2:
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    zzd zzdVarZzsa = zzsa();
                    parcel2.writeNoException();
                    parcel2.writeStrongBinder(zzdVarZzsa != null ? zzdVarZzsa.asBinder() : null);
                    return true;
                case 3:
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    Bundle arguments = getArguments();
                    parcel2.writeNoException();
                    if (arguments == null) {
                        parcel2.writeInt(0);
                        return true;
                    }
                    parcel2.writeInt(1);
                    arguments.writeToParcel(parcel2, 1);
                    return true;
                case 4:
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    int id = getId();
                    parcel2.writeNoException();
                    parcel2.writeInt(id);
                    return true;
                case 5:
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    zzc zzcVarZzsb = zzsb();
                    parcel2.writeNoException();
                    parcel2.writeStrongBinder(zzcVarZzsb != null ? zzcVarZzsb.asBinder() : null);
                    return true;
                case 6:
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    zzd zzdVarZzsc = zzsc();
                    parcel2.writeNoException();
                    parcel2.writeStrongBinder(zzdVarZzsc != null ? zzdVarZzsc.asBinder() : null);
                    return true;
                case 7:
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    boolean retainInstance = getRetainInstance();
                    parcel2.writeNoException();
                    parcel2.writeInt(retainInstance ? 1 : 0);
                    return true;
                case 8:
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    String tag = getTag();
                    parcel2.writeNoException();
                    parcel2.writeString(tag);
                    return true;
                case 9:
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    zzc zzcVarZzsd = zzsd();
                    parcel2.writeNoException();
                    parcel2.writeStrongBinder(zzcVarZzsd != null ? zzcVarZzsd.asBinder() : null);
                    return true;
                case 10:
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    int targetRequestCode = getTargetRequestCode();
                    parcel2.writeNoException();
                    parcel2.writeInt(targetRequestCode);
                    return true;
                case 11:
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    boolean userVisibleHint = getUserVisibleHint();
                    parcel2.writeNoException();
                    parcel2.writeInt(userVisibleHint ? 1 : 0);
                    return true;
                case 12:
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    zzd view = getView();
                    parcel2.writeNoException();
                    parcel2.writeStrongBinder(view != null ? view.asBinder() : null);
                    return true;
                case 13:
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    boolean zIsAdded = isAdded();
                    parcel2.writeNoException();
                    parcel2.writeInt(zIsAdded ? 1 : 0);
                    return true;
                case 14:
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    boolean zIsDetached = isDetached();
                    parcel2.writeNoException();
                    parcel2.writeInt(zIsDetached ? 1 : 0);
                    return true;
                case 15:
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    boolean zIsHidden = isHidden();
                    parcel2.writeNoException();
                    parcel2.writeInt(zIsHidden ? 1 : 0);
                    return true;
                case 16:
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    boolean zIsInLayout = isInLayout();
                    parcel2.writeNoException();
                    parcel2.writeInt(zIsInLayout ? 1 : 0);
                    return true;
                case 17:
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    boolean zIsRemoving = isRemoving();
                    parcel2.writeNoException();
                    parcel2.writeInt(zIsRemoving ? 1 : 0);
                    return true;
                case 18:
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    boolean zIsResumed = isResumed();
                    parcel2.writeNoException();
                    parcel2.writeInt(zIsResumed ? 1 : 0);
                    return true;
                case 19:
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    boolean zIsVisible = isVisible();
                    parcel2.writeNoException();
                    parcel2.writeInt(zIsVisible ? 1 : 0);
                    return true;
                case 20:
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    zzn(zzd.zza.zzbk(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 21:
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    setHasOptionsMenu(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 22:
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    setMenuVisibility(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 23:
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    setRetainInstance(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 24:
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    setUserVisibleHint(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 25:
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    startActivity(parcel.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 26:
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    startActivityForResult(parcel.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(parcel) : null, parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 27:
                    parcel.enforceInterface("com.google.android.gms.dynamic.IFragmentWrapper");
                    zzo(zzd.zza.zzbk(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    Bundle getArguments() throws RemoteException;

    int getId() throws RemoteException;

    boolean getRetainInstance() throws RemoteException;

    String getTag() throws RemoteException;

    int getTargetRequestCode() throws RemoteException;

    boolean getUserVisibleHint() throws RemoteException;

    zzd getView() throws RemoteException;

    boolean isAdded() throws RemoteException;

    boolean isDetached() throws RemoteException;

    boolean isHidden() throws RemoteException;

    boolean isInLayout() throws RemoteException;

    boolean isRemoving() throws RemoteException;

    boolean isResumed() throws RemoteException;

    boolean isVisible() throws RemoteException;

    void setHasOptionsMenu(boolean z) throws RemoteException;

    void setMenuVisibility(boolean z) throws RemoteException;

    void setRetainInstance(boolean z) throws RemoteException;

    void setUserVisibleHint(boolean z) throws RemoteException;

    void startActivity(Intent intent) throws RemoteException;

    void startActivityForResult(Intent intent, int i) throws RemoteException;

    void zzn(zzd zzdVar) throws RemoteException;

    void zzo(zzd zzdVar) throws RemoteException;

    zzd zzsa() throws RemoteException;

    zzc zzsb() throws RemoteException;

    zzd zzsc() throws RemoteException;

    zzc zzsd() throws RemoteException;
}
