// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.server.converter;

import android.os.Parcel;
import com.google.android.gms.common.server.response.FastJsonResponse;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class ConverterWrapper implements SafeParcelable
{
    public static final zza CREATOR;
    private final int mVersionCode;
    private final StringToIntConverter zzagO;
    
    static {
        CREATOR = new zza();
    }
    
    ConverterWrapper(final int mVersionCode, final StringToIntConverter zzagO) {
        this.mVersionCode = mVersionCode;
        this.zzagO = zzagO;
    }
    
    private ConverterWrapper(final StringToIntConverter zzagO) {
        this.mVersionCode = 1;
        this.zzagO = zzagO;
    }
    
    public static ConverterWrapper zza(final FastJsonResponse.zza<?, ?> zza) {
        if (zza instanceof StringToIntConverter) {
            return new ConverterWrapper((StringToIntConverter)zza);
        }
        throw new IllegalArgumentException("Unsupported safe parcelable field converter class.");
    }
    
    public int describeContents() {
        final zza creator = ConverterWrapper.CREATOR;
        return 0;
    }
    
    int getVersionCode() {
        return this.mVersionCode;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        final zza creator = ConverterWrapper.CREATOR;
        zza.zza(this, parcel, n);
    }
    
    StringToIntConverter zzpy() {
        return this.zzagO;
    }
    
    public FastJsonResponse.zza<?, ?> zzpz() {
        if (this.zzagO != null) {
            return this.zzagO;
        }
        throw new IllegalStateException("There was no converter wrapped in this ConverterWrapper.");
    }
}
