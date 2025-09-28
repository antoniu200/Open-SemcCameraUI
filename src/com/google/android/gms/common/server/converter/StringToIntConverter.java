package com.google.android.gms.common.server.converter;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.server.response.FastJsonResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class StringToIntConverter implements SafeParcelable, FastJsonResponse.zza<String, Integer> {
    public static final zzb CREATOR = new zzb();
    private final int mVersionCode;
    private final HashMap<String, Integer> zzagP;
    private final HashMap<Integer, String> zzagQ;
    private final ArrayList<Entry> zzagR;

    public static final class Entry implements SafeParcelable {
        public static final zzc CREATOR = new zzc();
        final int versionCode;
        final String zzagS;
        final int zzagT;

        Entry(int i, String str, int i2) {
            this.versionCode = i;
            this.zzagS = str;
            this.zzagT = i2;
        }

        Entry(String str, int i) {
            this.versionCode = 1;
            this.zzagS = str;
            this.zzagT = i;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            zzc zzcVar = CREATOR;
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            zzc zzcVar = CREATOR;
            zzc.zza(this, parcel, i);
        }
    }

    public StringToIntConverter() {
        this.mVersionCode = 1;
        this.zzagP = new HashMap<>();
        this.zzagQ = new HashMap<>();
        this.zzagR = null;
    }

    StringToIntConverter(int i, ArrayList<Entry> arrayList) {
        this.mVersionCode = i;
        this.zzagP = new HashMap<>();
        this.zzagQ = new HashMap<>();
        this.zzagR = null;
        zzb(arrayList);
    }

    private void zzb(ArrayList<Entry> arrayList) {
        Iterator<Entry> it = arrayList.iterator();
        while (it.hasNext()) {
            Entry next = it.next();
            zzi(next.zzagS, next.zzagT);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        zzb zzbVar = CREATOR;
        return 0;
    }

    int getVersionCode() {
        return this.mVersionCode;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        zzb zzbVar = CREATOR;
        zzb.zza(this, parcel, i);
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse.zza
    /* renamed from: zzb, reason: merged with bridge method [inline-methods] */
    public String convertBack(Integer num) {
        String str = this.zzagQ.get(num);
        return (str == null && this.zzagP.containsKey("gms_unknown")) ? "gms_unknown" : str;
    }

    public StringToIntConverter zzi(String str, int i) {
        this.zzagP.put(str, Integer.valueOf(i));
        this.zzagQ.put(Integer.valueOf(i), str);
        return this;
    }

    ArrayList<Entry> zzpA() {
        ArrayList<Entry> arrayList = new ArrayList<>();
        for (String str : this.zzagP.keySet()) {
            arrayList.add(new Entry(str, this.zzagP.get(str).intValue()));
        }
        return arrayList;
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse.zza
    public int zzpB() {
        return 7;
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse.zza
    public int zzpC() {
        return 0;
    }
}
