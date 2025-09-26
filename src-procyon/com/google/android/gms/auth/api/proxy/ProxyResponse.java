// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.api.proxy;

import android.os.Parcel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import android.os.Bundle;
import android.app.PendingIntent;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class ProxyResponse implements SafeParcelable
{
    public static final Parcelable$Creator<ProxyResponse> CREATOR;
    public static final int STATUS_CODE_NO_CONNECTION = -1;
    public final byte[] body;
    public final int googlePlayServicesStatusCode;
    public final PendingIntent recoveryAction;
    public final int statusCode;
    final int versionCode;
    final Bundle zzSK;
    
    static {
        CREATOR = (Parcelable$Creator)new zzc();
    }
    
    ProxyResponse(final int versionCode, final int googlePlayServicesStatusCode, final PendingIntent recoveryAction, final int statusCode, final Bundle zzSK, final byte[] body) {
        this.versionCode = versionCode;
        this.googlePlayServicesStatusCode = googlePlayServicesStatusCode;
        this.statusCode = statusCode;
        this.zzSK = zzSK;
        this.body = body;
        this.recoveryAction = recoveryAction;
    }
    
    public ProxyResponse(final int n, final PendingIntent pendingIntent, final int n2, final Bundle bundle, final byte[] array) {
        this(1, n, pendingIntent, n2, bundle, array);
    }
    
    private ProxyResponse(final int n, final Bundle bundle, final byte[] array) {
        this(1, 0, null, n, bundle, array);
    }
    
    public ProxyResponse(final int n, final Map<String, String> map, final byte[] array) {
        this(n, zzE(map), array);
    }
    
    public static ProxyResponse createErrorProxyResponse(final int n, final PendingIntent pendingIntent, final int n2, final Map<String, String> map, final byte[] array) {
        return new ProxyResponse(1, n, pendingIntent, n2, zzE(map), array);
    }
    
    private static Bundle zzE(final Map<String, String> map) {
        final Bundle bundle = new Bundle();
        if (map == null) {
            return bundle;
        }
        for (final Map.Entry entry : map.entrySet()) {
            bundle.putString((String)entry.getKey(), (String)entry.getValue());
        }
        return bundle;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public Map<String, String> getHeaders() {
        final HashMap hashMap = new HashMap();
        for (final String s : this.zzSK.keySet()) {
            hashMap.put(s, this.zzSK.getString(s));
        }
        return hashMap;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zzc.zza(this, parcel, n);
    }
}
