// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.auth.api.proxy;

import android.util.Patterns;
import com.google.android.gms.common.internal.zzx;
import android.os.Parcel;
import java.util.Iterator;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import android.os.Bundle;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class ProxyRequest implements SafeParcelable
{
    public static final Parcelable$Creator<ProxyRequest> CREATOR;
    public static final int HTTP_METHOD_DELETE = 3;
    public static final int HTTP_METHOD_GET = 0;
    public static final int HTTP_METHOD_HEAD = 4;
    public static final int HTTP_METHOD_OPTIONS = 5;
    public static final int HTTP_METHOD_PATCH = 7;
    public static final int HTTP_METHOD_POST = 1;
    public static final int HTTP_METHOD_PUT = 2;
    public static final int HTTP_METHOD_TRACE = 6;
    public static final int LAST_CODE = 7;
    public static final int VERSION_CODE = 2;
    public final byte[] body;
    public final int httpMethod;
    public final long timeoutMillis;
    public final String url;
    final int versionCode;
    Bundle zzSK;
    
    static {
        CREATOR = (Parcelable$Creator)new zzb();
    }
    
    ProxyRequest(final int versionCode, final String url, final int httpMethod, final long timeoutMillis, final byte[] body, final Bundle zzSK) {
        this.versionCode = versionCode;
        this.url = url;
        this.httpMethod = httpMethod;
        this.timeoutMillis = timeoutMillis;
        this.body = body;
        this.zzSK = zzSK;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public Map<String, String> getHeaderMap() {
        final LinkedHashMap m = new LinkedHashMap(this.zzSK.size());
        for (final String s : this.zzSK.keySet()) {
            m.put(s, this.zzSK.getString(s));
        }
        return (Map<String, String>)Collections.unmodifiableMap((Map<?, ?>)m);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ProxyRequest[ url: ");
        sb.append(this.url);
        sb.append(", method: ");
        sb.append(this.httpMethod);
        sb.append(" ]");
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zzb.zza(this, parcel, n);
    }
    
    public static class Builder
    {
        private String zzSL;
        private int zzSM;
        private long zzSN;
        private byte[] zzSO;
        private Bundle zzSP;
        
        public Builder(final String str) {
            this.zzSM = ProxyRequest.HTTP_METHOD_GET;
            this.zzSN = 3000L;
            this.zzSO = null;
            this.zzSP = new Bundle();
            zzx.zzcr(str);
            if (Patterns.WEB_URL.matcher(str).matches()) {
                this.zzSL = str;
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("The supplied url [ ");
            sb.append(str);
            sb.append("] is not match Patterns.WEB_URL!");
            throw new IllegalArgumentException(sb.toString());
        }
        
        public ProxyRequest build() {
            if (this.zzSO == null) {
                this.zzSO = new byte[0];
            }
            return new ProxyRequest(2, this.zzSL, this.zzSM, this.zzSN, this.zzSO, this.zzSP);
        }
        
        public Builder putHeader(final String s, final String s2) {
            zzx.zzh(s, "Header name cannot be null or empty!");
            final Bundle zzSP = this.zzSP;
            String s3 = s2;
            if (s2 == null) {
                s3 = "";
            }
            zzSP.putString(s, s3);
            return this;
        }
        
        public Builder setBody(final byte[] zzSO) {
            this.zzSO = zzSO;
            return this;
        }
        
        public Builder setHttpMethod(final int zzSM) {
            zzx.zzb(zzSM >= 0 && zzSM <= ProxyRequest.LAST_CODE, (Object)"Unrecognized http method code.");
            this.zzSM = zzSM;
            return this;
        }
        
        public Builder setTimeoutMillis(final long zzSN) {
            zzx.zzb(zzSN >= 0L, (Object)"The specified timeout must be non-negative.");
            this.zzSN = zzSN;
            return this;
        }
    }
}
