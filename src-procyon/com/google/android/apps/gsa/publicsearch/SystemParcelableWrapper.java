// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.apps.gsa.publicsearch;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public final class SystemParcelableWrapper implements Parcelable
{
    public static final Parcelable$Creator<SystemParcelableWrapper> CREATOR;
    private final Parcelable parcelable;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<SystemParcelableWrapper>() {
            public SystemParcelableWrapper createFromParcel(final Parcel parcel) {
                return new SystemParcelableWrapper(parcel.readParcelable((ClassLoader)null));
            }
            
            public SystemParcelableWrapper[] newArray(final int n) {
                return new SystemParcelableWrapper[n];
            }
        };
    }
    
    public SystemParcelableWrapper(final Parcelable parcelable) {
        if (!isSystemParcelable(parcelable)) {
            throw new IllegalArgumentException("Only Android system classes can be passed in SystemParcelableWrapper.");
        }
        this.parcelable = parcelable;
    }
    
    static boolean isSystemParcelable(final Parcelable parcelable) {
        return parcelable.getClass().getName().startsWith("android.os.");
    }
    
    public int describeContents() {
        return 0;
    }
    
    public Parcelable getParcelable() {
        return this.parcelable;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeParcelable(this.parcelable, n);
    }
}
