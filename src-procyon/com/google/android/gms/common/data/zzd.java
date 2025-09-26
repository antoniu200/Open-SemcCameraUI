// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.data;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class zzd<T extends SafeParcelable> extends AbstractDataBuffer<T>
{
    private static final String[] zzadn;
    private final Parcelable$Creator<T> zzado;
    
    static {
        zzadn = new String[] { "data" };
    }
    
    public zzd(final DataHolder dataHolder, final Parcelable$Creator<T> zzado) {
        super(dataHolder);
        this.zzado = zzado;
    }
    
    public T zzbs(final int n) {
        final byte[] zzg = this.zzabq.zzg("data", n, this.zzabq.zzbt(n));
        final Parcel obtain = Parcel.obtain();
        obtain.unmarshall(zzg, 0, zzg.length);
        obtain.setDataPosition(0);
        final SafeParcelable safeParcelable = (SafeParcelable)this.zzado.createFromParcel(obtain);
        obtain.recycle();
        return (T)safeParcelable;
    }
}
