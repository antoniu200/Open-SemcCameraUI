// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.images;

import android.os.Parcel;
import com.google.android.gms.common.internal.zzw;
import org.json.JSONException;
import org.json.JSONObject;
import android.net.Uri;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class WebImage implements SafeParcelable
{
    public static final Parcelable$Creator<WebImage> CREATOR;
    private final int mVersionCode;
    private final Uri zzaeg;
    private final int zznQ;
    private final int zznR;
    
    static {
        CREATOR = (Parcelable$Creator)new zzb();
    }
    
    WebImage(final int mVersionCode, final Uri zzaeg, final int zznQ, final int zznR) {
        this.mVersionCode = mVersionCode;
        this.zzaeg = zzaeg;
        this.zznQ = zznQ;
        this.zznR = zznR;
    }
    
    public WebImage(final Uri uri) throws IllegalArgumentException {
        this(uri, 0, 0);
    }
    
    public WebImage(final Uri uri, final int n, final int n2) throws IllegalArgumentException {
        this(1, uri, n, n2);
        if (uri == null) {
            throw new IllegalArgumentException("url cannot be null");
        }
        if (n >= 0 && n2 >= 0) {
            return;
        }
        throw new IllegalArgumentException("width and height must not be negative");
    }
    
    public WebImage(final JSONObject jsonObject) throws IllegalArgumentException {
        this(zzi(jsonObject), jsonObject.optInt("width", 0), jsonObject.optInt("height", 0));
    }
    
    private static Uri zzi(final JSONObject jsonObject) {
        Label_0021: {
            if (!jsonObject.has("url")) {
                break Label_0021;
            }
            try {
                return Uri.parse(jsonObject.getString("url"));
                return null;
            }
            catch (final JSONException ex) {
                return null;
            }
        }
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof WebImage)) {
            return false;
        }
        final WebImage webImage = (WebImage)o;
        return zzw.equal(this.zzaeg, webImage.zzaeg) && this.zznQ == webImage.zznQ && this.zznR == webImage.zznR;
    }
    
    public int getHeight() {
        return this.zznR;
    }
    
    public Uri getUrl() {
        return this.zzaeg;
    }
    
    int getVersionCode() {
        return this.mVersionCode;
    }
    
    public int getWidth() {
        return this.zznQ;
    }
    
    @Override
    public int hashCode() {
        return zzw.hashCode(this.zzaeg, this.zznQ, this.zznR);
    }
    
    public JSONObject toJson() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("url", (Object)this.zzaeg.toString());
            jsonObject.put("width", this.zznQ);
            jsonObject.put("height", this.zznR);
            return jsonObject;
        }
        catch (final JSONException ex) {
            return jsonObject;
        }
    }
    
    @Override
    public String toString() {
        return String.format("Image %dx%d %s", this.zznQ, this.zznR, this.zzaeg.toString());
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        zzb.zza(this, parcel, n);
    }
}
