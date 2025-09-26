// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.server.converter;

import android.os.Parcel;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.android.gms.common.server.response.FastJsonResponse;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class StringToIntConverter implements SafeParcelable, zza<String, Integer>
{
    public static final zzb CREATOR;
    private final int mVersionCode;
    private final HashMap<String, Integer> zzagP;
    private final HashMap<Integer, String> zzagQ;
    private final ArrayList<Entry> zzagR;
    
    static {
        CREATOR = new zzb();
    }
    
    public StringToIntConverter() {
        this.mVersionCode = 1;
        this.zzagP = new HashMap<String, Integer>();
        this.zzagQ = new HashMap<Integer, String>();
        this.zzagR = null;
    }
    
    StringToIntConverter(final int mVersionCode, final ArrayList<Entry> list) {
        this.mVersionCode = mVersionCode;
        this.zzagP = new HashMap<String, Integer>();
        this.zzagQ = new HashMap<Integer, String>();
        this.zzagR = null;
        this.zzb(list);
    }
    
    private void zzb(final ArrayList<Entry> list) {
        for (final Entry entry : list) {
            this.zzi(entry.zzagS, entry.zzagT);
        }
    }
    
    public int describeContents() {
        final zzb creator = StringToIntConverter.CREATOR;
        return 0;
    }
    
    int getVersionCode() {
        return this.mVersionCode;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        final zzb creator = StringToIntConverter.CREATOR;
        zzb.zza(this, parcel, n);
    }
    
    public String zzb(final Integer key) {
        final String s = this.zzagQ.get(key);
        if (s == null && this.zzagP.containsKey("gms_unknown")) {
            return "gms_unknown";
        }
        return s;
    }
    
    public StringToIntConverter zzi(final String s, final int n) {
        this.zzagP.put(s, n);
        this.zzagQ.put(n, s);
        return this;
    }
    
    ArrayList<Entry> zzpA() {
        final ArrayList list = new ArrayList();
        for (final String key : this.zzagP.keySet()) {
            list.add(new Entry(key, this.zzagP.get(key)));
        }
        return list;
    }
    
    @Override
    public int zzpB() {
        return 7;
    }
    
    @Override
    public int zzpC() {
        return 0;
    }
    
    public static final class Entry implements SafeParcelable
    {
        public static final zzc CREATOR;
        final int versionCode;
        final String zzagS;
        final int zzagT;
        
        static {
            CREATOR = new zzc();
        }
        
        Entry(final int versionCode, final String zzagS, final int zzagT) {
            this.versionCode = versionCode;
            this.zzagS = zzagS;
            this.zzagT = zzagT;
        }
        
        Entry(final String zzagS, final int zzagT) {
            this.versionCode = 1;
            this.zzagS = zzagS;
            this.zzagT = zzagT;
        }
        
        public int describeContents() {
            final zzc creator = Entry.CREATOR;
            return 0;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            final zzc creator = Entry.CREATOR;
            zzc.zza(this, parcel, n);
        }
    }
}
