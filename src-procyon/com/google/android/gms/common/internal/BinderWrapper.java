// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.internal;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public final class BinderWrapper implements Parcelable
{
    public static final Parcelable$Creator<BinderWrapper> CREATOR;
    private IBinder zzaeJ;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<BinderWrapper>() {
            public BinderWrapper zzaj(final Parcel parcel) {
                return new BinderWrapper(parcel, null);
            }
            
            public BinderWrapper[] zzbC(final int n) {
                return new BinderWrapper[n];
            }
        };
    }
    
    public BinderWrapper() {
        this.zzaeJ = null;
    }
    
    public BinderWrapper(final IBinder zzaeJ) {
        this.zzaeJ = null;
        this.zzaeJ = zzaeJ;
    }
    
    private BinderWrapper(final Parcel parcel) {
        this.zzaeJ = null;
        this.zzaeJ = parcel.readStrongBinder();
    }
    
    public int describeContents() {
        return 0;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeStrongBinder(this.zzaeJ);
    }
}
