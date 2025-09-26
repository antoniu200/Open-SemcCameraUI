// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.server.response;

import android.os.Parcel;
import java.util.Iterator;
import com.google.android.gms.common.internal.zzx;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class FieldMappingDictionary implements SafeParcelable
{
    public static final zzc CREATOR;
    private final int mVersionCode;
    private final HashMap<String, Map<String, FastJsonResponse.Field<?, ?>>> zzahe;
    private final ArrayList<Entry> zzahf;
    private final String zzahg;
    
    static {
        CREATOR = new zzc();
    }
    
    FieldMappingDictionary(final int mVersionCode, final ArrayList<Entry> list, final String s) {
        this.mVersionCode = mVersionCode;
        this.zzahf = null;
        this.zzahe = zzc(list);
        this.zzahg = zzx.zzw(s);
        this.zzpQ();
    }
    
    public FieldMappingDictionary(final Class<? extends FastJsonResponse> clazz) {
        this.mVersionCode = 1;
        this.zzahf = null;
        this.zzahe = new HashMap<String, Map<String, FastJsonResponse.Field<?, ?>>>();
        this.zzahg = clazz.getCanonicalName();
    }
    
    private static HashMap<String, Map<String, FastJsonResponse.Field<?, ?>>> zzc(final ArrayList<Entry> list) {
        final HashMap hashMap = new HashMap();
        for (int size = list.size(), i = 0; i < size; ++i) {
            final Entry entry = list.get(i);
            hashMap.put(entry.className, entry.zzpU());
        }
        return hashMap;
    }
    
    public int describeContents() {
        final zzc creator = FieldMappingDictionary.CREATOR;
        return 0;
    }
    
    int getVersionCode() {
        return this.mVersionCode;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (final String s : this.zzahe.keySet()) {
            sb.append(s);
            sb.append(":\n");
            final Map map = this.zzahe.get(s);
            for (final String str : map.keySet()) {
                sb.append("  ");
                sb.append(str);
                sb.append(": ");
                sb.append(map.get(str));
            }
        }
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        final zzc creator = FieldMappingDictionary.CREATOR;
        zzc.zza(this, parcel, n);
    }
    
    public void zza(final Class<? extends FastJsonResponse> clazz, final Map<String, FastJsonResponse.Field<?, ?>> value) {
        this.zzahe.put(clazz.getCanonicalName(), value);
    }
    
    public boolean zzb(final Class<? extends FastJsonResponse> clazz) {
        return this.zzahe.containsKey(clazz.getCanonicalName());
    }
    
    public Map<String, FastJsonResponse.Field<?, ?>> zzcw(final String key) {
        return this.zzahe.get(key);
    }
    
    public void zzpQ() {
        final Iterator<String> iterator = this.zzahe.keySet().iterator();
        while (iterator.hasNext()) {
            final Map map = this.zzahe.get(iterator.next());
            final Iterator iterator2 = map.keySet().iterator();
            while (iterator2.hasNext()) {
                ((FastJsonResponse.Field)map.get(iterator2.next())).zza(this);
            }
        }
    }
    
    public void zzpR() {
        for (final String s : this.zzahe.keySet()) {
            final Map map = this.zzahe.get(s);
            final HashMap<String, FastJsonResponse.Field<?, ?>> value = new HashMap<String, FastJsonResponse.Field<?, ?>>();
            for (final String key : map.keySet()) {
                value.put(key, ((FastJsonResponse.Field)map.get(key)).zzpG());
            }
            this.zzahe.put(s, value);
        }
    }
    
    ArrayList<Entry> zzpS() {
        final ArrayList list = new ArrayList();
        for (final String key : this.zzahe.keySet()) {
            list.add(new Entry(key, this.zzahe.get(key)));
        }
        return list;
    }
    
    public String zzpT() {
        return this.zzahg;
    }
    
    public static class Entry implements SafeParcelable
    {
        public static final zzd CREATOR;
        final String className;
        final int versionCode;
        final ArrayList<FieldMapPair> zzahh;
        
        static {
            CREATOR = new zzd();
        }
        
        Entry(final int versionCode, final String className, final ArrayList<FieldMapPair> zzahh) {
            this.versionCode = versionCode;
            this.className = className;
            this.zzahh = zzahh;
        }
        
        Entry(final String className, final Map<String, FastJsonResponse.Field<?, ?>> map) {
            this.versionCode = 1;
            this.className = className;
            this.zzahh = zzF(map);
        }
        
        private static ArrayList<FieldMapPair> zzF(final Map<String, FastJsonResponse.Field<?, ?>> map) {
            if (map == null) {
                return null;
            }
            final ArrayList list = new ArrayList();
            for (final String s : map.keySet()) {
                list.add(new FieldMapPair(s, map.get(s)));
            }
            return list;
        }
        
        public int describeContents() {
            final zzd creator = Entry.CREATOR;
            return 0;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            final zzd creator = Entry.CREATOR;
            zzd.zza(this, parcel, n);
        }
        
        HashMap<String, FastJsonResponse.Field<?, ?>> zzpU() {
            final HashMap hashMap = new HashMap();
            for (int size = this.zzahh.size(), i = 0; i < size; ++i) {
                final FieldMapPair fieldMapPair = this.zzahh.get(i);
                hashMap.put(fieldMapPair.key, fieldMapPair.zzahi);
            }
            return hashMap;
        }
    }
    
    public static class FieldMapPair implements SafeParcelable
    {
        public static final zzb CREATOR;
        final String key;
        final int versionCode;
        final FastJsonResponse.Field<?, ?> zzahi;
        
        static {
            CREATOR = new zzb();
        }
        
        FieldMapPair(final int versionCode, final String key, final FastJsonResponse.Field<?, ?> zzahi) {
            this.versionCode = versionCode;
            this.key = key;
            this.zzahi = zzahi;
        }
        
        FieldMapPair(final String key, final FastJsonResponse.Field<?, ?> zzahi) {
            this.versionCode = 1;
            this.key = key;
            this.zzahi = zzahi;
        }
        
        public int describeContents() {
            final zzb creator = FieldMapPair.CREATOR;
            return 0;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            final zzb creator = FieldMapPair.CREATOR;
            zzb.zza(this, parcel, n);
        }
    }
}
