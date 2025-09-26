// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.server.response;

import com.google.android.gms.common.internal.safeparcel.zzb;
import java.util.ArrayList;
import java.util.Set;
import android.os.Bundle;
import java.math.BigDecimal;
import com.google.android.gms.internal.zzmj;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.internal.zzmu;
import com.google.android.gms.internal.zzmk;
import com.google.android.gms.internal.zzmv;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import com.google.android.gms.common.internal.zzx;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class SafeParcelResponse extends FastJsonResponse implements SafeParcelable
{
    public static final zze CREATOR;
    private final String mClassName;
    private final int mVersionCode;
    private final FieldMappingDictionary zzahc;
    private final Parcel zzahj;
    private final int zzahk;
    private int zzahl;
    private int zzahm;
    
    static {
        CREATOR = new zze();
    }
    
    SafeParcelResponse(final int mVersionCode, final Parcel parcel, final FieldMappingDictionary zzahc) {
        this.mVersionCode = mVersionCode;
        this.zzahj = zzx.zzw(parcel);
        this.zzahk = 2;
        this.zzahc = zzahc;
        String zzpT;
        if (this.zzahc == null) {
            zzpT = null;
        }
        else {
            zzpT = this.zzahc.zzpT();
        }
        this.mClassName = zzpT;
        this.zzahl = 2;
    }
    
    private SafeParcelResponse(final SafeParcelable safeParcelable, final FieldMappingDictionary fieldMappingDictionary, final String s) {
        this.mVersionCode = 1;
        safeParcelable.writeToParcel(this.zzahj = Parcel.obtain(), 0);
        this.zzahk = 1;
        this.zzahc = zzx.zzw(fieldMappingDictionary);
        this.mClassName = zzx.zzw(s);
        this.zzahl = 2;
    }
    
    private static HashMap<Integer, Map.Entry<String, Field<?, ?>>> zzG(final Map<String, Field<?, ?>> map) {
        final HashMap hashMap = new HashMap();
        for (final Map.Entry<K, Field> value : map.entrySet()) {
            hashMap.put(value.getValue().zzpK(), value);
        }
        return hashMap;
    }
    
    public static <T extends FastJsonResponse & SafeParcelable> SafeParcelResponse zza(final T t) {
        return new SafeParcelResponse(t, zzb(t), t.getClass().getCanonicalName());
    }
    
    private static void zza(final FieldMappingDictionary fieldMappingDictionary, FastJsonResponse fastJsonResponse) {
        final Class<? extends FastJsonResponse> class1 = fastJsonResponse.getClass();
        if (!fieldMappingDictionary.zzb(class1)) {
            final Map<String, Field<?, ?>> zzpD = fastJsonResponse.zzpD();
            fieldMappingDictionary.zza(class1, zzpD);
            final Iterator<String> iterator = zzpD.keySet().iterator();
            while (iterator.hasNext()) {
                fastJsonResponse = (FastJsonResponse)zzpD.get(iterator.next());
                final Class<? extends FastJsonResponse> zzpL = ((Field)fastJsonResponse).zzpL();
                if (zzpL != null) {
                    try {
                        zza(fieldMappingDictionary, (FastJsonResponse)zzpL.newInstance());
                        continue;
                    }
                    catch (final IllegalAccessException cause) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Could not access object of type ");
                        sb.append(((Field)fastJsonResponse).zzpL().getCanonicalName());
                        throw new IllegalStateException(sb.toString(), cause);
                    }
                    catch (final InstantiationException cause2) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Could not instantiate an object of type ");
                        sb2.append(((Field)fastJsonResponse).zzpL().getCanonicalName());
                        throw new IllegalStateException(sb2.toString(), cause2);
                    }
                    break;
                }
            }
        }
    }
    
    private void zza(StringBuilder sb, final int i, final Object obj) {
        String str = null;
        switch (i) {
            default: {
                sb = new StringBuilder();
                sb.append("Unknown type = ");
                sb.append(i);
                throw new IllegalArgumentException(sb.toString());
            }
            case 11: {
                throw new IllegalArgumentException("Method does not accept concrete type.");
            }
            case 10: {
                zzmv.zza(sb, (HashMap<String, String>)obj);
                return;
            }
            case 9: {
                sb.append("\"");
                str = zzmk.zzj((byte[])obj);
                break;
            }
            case 8: {
                sb.append("\"");
                str = zzmk.zzi((byte[])obj);
                break;
            }
            case 7: {
                sb.append("\"");
                str = zzmu.zzcz(obj.toString());
                break;
            }
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6: {
                sb.append(obj);
                return;
            }
        }
        sb.append(str);
        sb.append("\"");
    }
    
    private void zza(StringBuilder sb, final Field<?, ?> field, final Parcel parcel, final int n) {
        Object o = null;
        switch (field.zzpC()) {
            default: {
                sb = new StringBuilder();
                sb.append("Unknown field out type = ");
                sb.append(field.zzpC());
                throw new IllegalArgumentException(sb.toString());
            }
            case 11: {
                throw new IllegalArgumentException("Method does not accept concrete type.");
            }
            case 10: {
                o = zzi(com.google.android.gms.common.internal.safeparcel.zza.zzr(parcel, n));
                break;
            }
            case 8:
            case 9: {
                o = com.google.android.gms.common.internal.safeparcel.zza.zzs(parcel, n);
                break;
            }
            case 7: {
                o = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, n);
                break;
            }
            case 6: {
                o = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, n);
                break;
            }
            case 5: {
                o = com.google.android.gms.common.internal.safeparcel.zza.zzo(parcel, n);
                break;
            }
            case 4: {
                o = com.google.android.gms.common.internal.safeparcel.zza.zzn(parcel, n);
                break;
            }
            case 3: {
                o = com.google.android.gms.common.internal.safeparcel.zza.zzl(parcel, n);
                break;
            }
            case 2: {
                o = com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel, n);
                break;
            }
            case 1: {
                o = com.google.android.gms.common.internal.safeparcel.zza.zzk(parcel, n);
                break;
            }
            case 0: {
                o = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, n);
                break;
            }
        }
        this.zzb(sb, field, this.zza(field, o));
    }
    
    private void zza(final StringBuilder sb, final String str, final Field<?, ?> field, final Parcel parcel, final int n) {
        sb.append("\"");
        sb.append(str);
        sb.append("\":");
        if (field.zzpN()) {
            this.zza(sb, field, parcel, n);
            return;
        }
        this.zzb(sb, field, parcel, n);
    }
    
    private void zza(final StringBuilder sb, final Map<String, Field<?, ?>> map, final Parcel parcel) {
        final HashMap<Integer, Map.Entry<String, Field<?, ?>>> zzG = zzG(map);
        sb.append('{');
        final int zzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        int n = 0;
        while (parcel.dataPosition() < zzap) {
            final int zzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            final Map.Entry<String, V> entry = (Map.Entry<String, V>)zzG.get(com.google.android.gms.common.internal.safeparcel.zza.zzbM(zzao));
            if (entry == null) {
                continue;
            }
            if (n != 0) {
                sb.append(",");
            }
            this.zza(sb, entry.getKey(), (Field<?, ?>)entry.getValue(), parcel, zzao);
            n = 1;
        }
        if (parcel.dataPosition() != zzap) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Overread allowed size end=");
            sb2.append(zzap);
            throw new com.google.android.gms.common.internal.safeparcel.zza.zza(sb2.toString(), parcel);
        }
        sb.append('}');
    }
    
    private static FieldMappingDictionary zzb(final FastJsonResponse fastJsonResponse) {
        final FieldMappingDictionary fieldMappingDictionary = new FieldMappingDictionary(fastJsonResponse.getClass());
        zza(fieldMappingDictionary, fastJsonResponse);
        fieldMappingDictionary.zzpR();
        fieldMappingDictionary.zzpQ();
        return fieldMappingDictionary;
    }
    
    private void zzb(final StringBuilder sb, final Field<?, ?> field, Parcel zzE, int i) {
        String str2 = null;
        Label_0582: {
            if (!field.zzpI()) {
                Number obj = null;
                Label_0654: {
                    String str3 = null;
                    switch (field.zzpC()) {
                        default: {
                            throw new IllegalStateException("Unknown field type out");
                        }
                        case 11: {
                            zzE = com.google.android.gms.common.internal.safeparcel.zza.zzE(zzE, i);
                            zzE.setDataPosition(0);
                            this.zza(sb, field.zzpP(), zzE);
                            return;
                        }
                        case 10: {
                            final Bundle zzr = com.google.android.gms.common.internal.safeparcel.zza.zzr(zzE, i);
                            final Set keySet = zzr.keySet();
                            keySet.size();
                            sb.append("{");
                            final Iterator iterator = keySet.iterator();
                            i = 1;
                            while (iterator.hasNext()) {
                                final String str = (String)iterator.next();
                                if (i == 0) {
                                    sb.append(",");
                                }
                                sb.append("\"");
                                sb.append(str);
                                sb.append("\"");
                                sb.append(":");
                                sb.append("\"");
                                sb.append(zzmu.zzcz(zzr.getString(str)));
                                sb.append("\"");
                                i = 0;
                            }
                            str2 = "}";
                            break Label_0582;
                        }
                        case 9: {
                            final byte[] zzs = com.google.android.gms.common.internal.safeparcel.zza.zzs(zzE, i);
                            sb.append("\"");
                            str3 = zzmk.zzj(zzs);
                            break;
                        }
                        case 8: {
                            final byte[] zzs2 = com.google.android.gms.common.internal.safeparcel.zza.zzs(zzE, i);
                            sb.append("\"");
                            str3 = zzmk.zzi(zzs2);
                            break;
                        }
                        case 7: {
                            final String zzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(zzE, i);
                            sb.append("\"");
                            str3 = zzmu.zzcz(zzp);
                            break;
                        }
                        case 6: {
                            sb.append(com.google.android.gms.common.internal.safeparcel.zza.zzc(zzE, i));
                            return;
                        }
                        case 5: {
                            obj = com.google.android.gms.common.internal.safeparcel.zza.zzo(zzE, i);
                            break Label_0654;
                        }
                        case 4: {
                            sb.append(com.google.android.gms.common.internal.safeparcel.zza.zzn(zzE, i));
                            return;
                        }
                        case 3: {
                            sb.append(com.google.android.gms.common.internal.safeparcel.zza.zzl(zzE, i));
                            return;
                        }
                        case 2: {
                            sb.append(com.google.android.gms.common.internal.safeparcel.zza.zzi(zzE, i));
                            return;
                        }
                        case 1: {
                            obj = com.google.android.gms.common.internal.safeparcel.zza.zzk(zzE, i);
                            break Label_0654;
                        }
                        case 0: {
                            sb.append(com.google.android.gms.common.internal.safeparcel.zza.zzg(zzE, i));
                            return;
                        }
                    }
                    sb.append(str3);
                    str2 = "\"";
                    break Label_0582;
                }
                sb.append(obj);
                return;
            }
            sb.append("[");
            Label_0263: {
                Number[] array = null;
                switch (field.zzpC()) {
                    default: {
                        throw new IllegalStateException("Unknown field type out.");
                    }
                    case 11: {
                        final Parcel[] zzF = com.google.android.gms.common.internal.safeparcel.zza.zzF(zzE, i);
                        int length;
                        for (length = zzF.length, i = 0; i < length; ++i) {
                            if (i > 0) {
                                sb.append(",");
                            }
                            zzF[i].setDataPosition(0);
                            this.zza(sb, field.zzpP(), zzF[i]);
                        }
                        break Label_0263;
                    }
                    case 8:
                    case 9:
                    case 10: {
                        throw new UnsupportedOperationException("List of type BASE64, BASE64_URL_SAFE, or STRING_MAP is not supported");
                    }
                    case 7: {
                        zzmj.zza(sb, com.google.android.gms.common.internal.safeparcel.zza.zzB(zzE, i));
                        break Label_0263;
                    }
                    case 6: {
                        zzmj.zza(sb, com.google.android.gms.common.internal.safeparcel.zza.zzu(zzE, i));
                        break Label_0263;
                    }
                    case 5: {
                        array = com.google.android.gms.common.internal.safeparcel.zza.zzA(zzE, i);
                        break;
                    }
                    case 4: {
                        zzmj.zza(sb, com.google.android.gms.common.internal.safeparcel.zza.zzz(zzE, i));
                        break Label_0263;
                    }
                    case 3: {
                        zzmj.zza(sb, com.google.android.gms.common.internal.safeparcel.zza.zzy(zzE, i));
                        break Label_0263;
                    }
                    case 2: {
                        zzmj.zza(sb, com.google.android.gms.common.internal.safeparcel.zza.zzw(zzE, i));
                        break Label_0263;
                    }
                    case 1: {
                        array = com.google.android.gms.common.internal.safeparcel.zza.zzx(zzE, i);
                        break;
                    }
                    case 0: {
                        zzmj.zza(sb, com.google.android.gms.common.internal.safeparcel.zza.zzv(zzE, i));
                        break Label_0263;
                    }
                }
                zzmj.zza(sb, array);
            }
            str2 = "]";
        }
        sb.append(str2);
    }
    
    private void zzb(final StringBuilder sb, final Field<?, ?> field, final Object o) {
        if (field.zzpH()) {
            this.zzb(sb, field, (ArrayList<?>)o);
            return;
        }
        this.zza(sb, field.zzpB(), o);
    }
    
    private void zzb(final StringBuilder sb, final Field<?, ?> field, final ArrayList<?> list) {
        sb.append("[");
        for (int size = list.size(), i = 0; i < size; ++i) {
            if (i != 0) {
                sb.append(",");
            }
            this.zza(sb, field.zzpB(), list.get(i));
        }
        sb.append("]");
    }
    
    public static HashMap<String, String> zzi(final Bundle bundle) {
        final HashMap hashMap = new HashMap();
        for (final String key : bundle.keySet()) {
            hashMap.put(key, bundle.getString(key));
        }
        return hashMap;
    }
    
    public int describeContents() {
        final zze creator = SafeParcelResponse.CREATOR;
        return 0;
    }
    
    public int getVersionCode() {
        return this.mVersionCode;
    }
    
    @Override
    public String toString() {
        zzx.zzb(this.zzahc, "Cannot convert to JSON on client side.");
        final Parcel zzpV = this.zzpV();
        zzpV.setDataPosition(0);
        final StringBuilder sb = new StringBuilder(100);
        this.zza(sb, this.zzahc.zzcw(this.mClassName), zzpV);
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        final zze creator = SafeParcelResponse.CREATOR;
        zze.zza(this, parcel, n);
    }
    
    @Override
    protected Object zzcs(final String s) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }
    
    @Override
    protected boolean zzct(final String s) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }
    
    @Override
    public Map<String, Field<?, ?>> zzpD() {
        if (this.zzahc == null) {
            return null;
        }
        return this.zzahc.zzcw(this.mClassName);
    }
    
    public Parcel zzpV() {
        switch (this.zzahl) {
            case 0: {
                this.zzahm = zzb.zzaq(this.zzahj);
            }
            case 1: {
                zzb.zzI(this.zzahj, this.zzahm);
                this.zzahl = 2;
                break;
            }
        }
        return this.zzahj;
    }
    
    FieldMappingDictionary zzpW() {
        switch (this.zzahk) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Invalid creation type: ");
                sb.append(this.zzahk);
                throw new IllegalStateException(sb.toString());
            }
            case 2: {
                return this.zzahc;
            }
            case 1: {
                return this.zzahc;
            }
            case 0: {
                return null;
            }
        }
    }
}
