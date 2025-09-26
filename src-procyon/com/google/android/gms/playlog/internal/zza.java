// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.playlog.internal;

import android.os.Parcelable$Creator;
import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import java.util.List;
import android.os.RemoteException;
import android.os.IInterface;

public interface zza extends IInterface
{
    void zza(final String p0, final PlayLoggerContext p1, final LogEvent p2) throws RemoteException;
    
    void zza(final String p0, final PlayLoggerContext p1, final List<LogEvent> p2) throws RemoteException;
    
    void zza(final String p0, final PlayLoggerContext p1, final byte[] p2) throws RemoteException;
    
    public abstract static class zza extends Binder implements zza
    {
        public static zza zzdz(final IBinder binder) {
            if (binder == null) {
                return null;
            }
            final IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.playlog.internal.IPlayLogService");
            if (queryLocalInterface != null && queryLocalInterface instanceof zza) {
                return (zza)queryLocalInterface;
            }
            return new zza(binder);
        }
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n == 1598968902) {
                parcel2.writeString("com.google.android.gms.playlog.internal.IPlayLogService");
                return true;
            }
            final PlayLoggerContext playLoggerContext = null;
            LogEvent zzgi = null;
            final PlayLoggerContext playLoggerContext2 = null;
            switch (n) {
                default: {
                    return super.onTransact(n, parcel, parcel2, n2);
                }
                case 4: {
                    parcel.enforceInterface("com.google.android.gms.playlog.internal.IPlayLogService");
                    final String string = parcel.readString();
                    PlayLoggerContext zzgj = playLoggerContext2;
                    if (parcel.readInt() != 0) {
                        zzgj = PlayLoggerContext.CREATOR.zzgj(parcel);
                    }
                    this.zza(string, zzgj, parcel.createByteArray());
                    return true;
                }
                case 3: {
                    parcel.enforceInterface("com.google.android.gms.playlog.internal.IPlayLogService");
                    final String string2 = parcel.readString();
                    PlayLoggerContext zzgj2 = playLoggerContext;
                    if (parcel.readInt() != 0) {
                        zzgj2 = PlayLoggerContext.CREATOR.zzgj(parcel);
                    }
                    this.zza(string2, zzgj2, parcel.createTypedArrayList((Parcelable$Creator)LogEvent.CREATOR));
                    return true;
                }
                case 2: {
                    parcel.enforceInterface("com.google.android.gms.playlog.internal.IPlayLogService");
                    final String string3 = parcel.readString();
                    PlayLoggerContext zzgj3;
                    if (parcel.readInt() != 0) {
                        zzgj3 = PlayLoggerContext.CREATOR.zzgj(parcel);
                    }
                    else {
                        zzgj3 = null;
                    }
                    if (parcel.readInt() != 0) {
                        zzgi = LogEvent.CREATOR.zzgi(parcel);
                    }
                    this.zza(string3, zzgj3, zzgi);
                    return true;
                }
            }
        }
        
        private static class zza implements com.google.android.gms.playlog.internal.zza
        {
            private IBinder zznJ;
            
            zza(final IBinder zznJ) {
                this.zznJ = zznJ;
            }
            
            public IBinder asBinder() {
                return this.zznJ;
            }
            
            @Override
            public void zza(final String s, final PlayLoggerContext playLoggerContext, final LogEvent logEvent) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.playlog.internal.IPlayLogService");
                    obtain.writeString(s);
                    if (playLoggerContext != null) {
                        obtain.writeInt(1);
                        playLoggerContext.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    if (logEvent != null) {
                        obtain.writeInt(1);
                        logEvent.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    this.zznJ.transact(2, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
            
            @Override
            public void zza(final String s, final PlayLoggerContext playLoggerContext, final List<LogEvent> list) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.playlog.internal.IPlayLogService");
                    obtain.writeString(s);
                    if (playLoggerContext != null) {
                        obtain.writeInt(1);
                        playLoggerContext.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    obtain.writeTypedList((List)list);
                    this.zznJ.transact(3, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
            
            @Override
            public void zza(final String s, final PlayLoggerContext playLoggerContext, final byte[] array) throws RemoteException {
                final Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.playlog.internal.IPlayLogService");
                    obtain.writeString(s);
                    if (playLoggerContext != null) {
                        obtain.writeInt(1);
                        playLoggerContext.writeToParcel(obtain, 0);
                    }
                    else {
                        obtain.writeInt(0);
                    }
                    obtain.writeByteArray(array);
                    this.zznJ.transact(4, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
        }
    }
}
