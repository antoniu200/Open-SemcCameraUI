package com.google.android.gms.common.server.response;

import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.server.response.FastJsonResponse;
import com.google.android.gms.internal.zzmj;
import com.google.android.gms.internal.zzmk;
import com.google.android.gms.internal.zzmu;
import com.google.android.gms.internal.zzmv;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SafeParcelResponse extends FastJsonResponse implements SafeParcelable {
    public static final zze CREATOR = new zze();
    private final String mClassName;
    private final int mVersionCode;
    private final FieldMappingDictionary zzahc;
    private final Parcel zzahj;
    private final int zzahk;
    private int zzahl;
    private int zzahm;

    SafeParcelResponse(int i, Parcel parcel, FieldMappingDictionary fieldMappingDictionary) {
        this.mVersionCode = i;
        this.zzahj = (Parcel) zzx.zzw(parcel);
        this.zzahk = 2;
        this.zzahc = fieldMappingDictionary;
        this.mClassName = this.zzahc == null ? null : this.zzahc.zzpT();
        this.zzahl = 2;
    }

    private SafeParcelResponse(SafeParcelable safeParcelable, FieldMappingDictionary fieldMappingDictionary, String str) {
        this.mVersionCode = 1;
        this.zzahj = Parcel.obtain();
        safeParcelable.writeToParcel(this.zzahj, 0);
        this.zzahk = 1;
        this.zzahc = (FieldMappingDictionary) zzx.zzw(fieldMappingDictionary);
        this.mClassName = (String) zzx.zzw(str);
        this.zzahl = 2;
    }

    private static HashMap<Integer, Map.Entry<String, FastJsonResponse.Field<?, ?>>> zzG(Map<String, FastJsonResponse.Field<?, ?>> map) {
        HashMap<Integer, Map.Entry<String, FastJsonResponse.Field<?, ?>>> map2 = new HashMap<>();
        for (Map.Entry<String, FastJsonResponse.Field<?, ?>> entry : map.entrySet()) {
            map2.put(Integer.valueOf(entry.getValue().zzpK()), entry);
        }
        return map2;
    }

    public static <T extends FastJsonResponse & SafeParcelable> SafeParcelResponse zza(T t) {
        String canonicalName = t.getClass().getCanonicalName();
        return new SafeParcelResponse(t, zzb(t), canonicalName);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static void zza(FieldMappingDictionary fieldMappingDictionary, FastJsonResponse fastJsonResponse) {
        Class<?> cls = fastJsonResponse.getClass();
        if (fieldMappingDictionary.zzb(cls)) {
            return;
        }
        Map<String, FastJsonResponse.Field<?, ?>> mapZzpD = fastJsonResponse.zzpD();
        fieldMappingDictionary.zza(cls, mapZzpD);
        Iterator<String> it = mapZzpD.keySet().iterator();
        while (it.hasNext()) {
            FastJsonResponse.Field<?, ?> field = mapZzpD.get(it.next());
            Class<? extends FastJsonResponse> clsZzpL = field.zzpL();
            if (clsZzpL != null) {
                try {
                    zza(fieldMappingDictionary, clsZzpL.newInstance());
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException("Could not access object of type " + field.zzpL().getCanonicalName(), e);
                } catch (InstantiationException e2) {
                    throw new IllegalStateException("Could not instantiate an object of type " + field.zzpL().getCanonicalName(), e2);
                }
            }
        }
    }

    private void zza(StringBuilder sb, int i, Object obj) {
        String strZzcz;
        switch (i) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                sb.append(obj);
                return;
            case 7:
                sb.append("\"");
                strZzcz = zzmu.zzcz(obj.toString());
                break;
            case 8:
                sb.append("\"");
                strZzcz = zzmk.zzi((byte[]) obj);
                break;
            case 9:
                sb.append("\"");
                strZzcz = zzmk.zzj((byte[]) obj);
                break;
            case 10:
                zzmv.zza(sb, (HashMap) obj);
                return;
            case 11:
                throw new IllegalArgumentException("Method does not accept concrete type.");
            default:
                throw new IllegalArgumentException("Unknown type = " + i);
        }
        sb.append(strZzcz);
        sb.append("\"");
    }

    private void zza(StringBuilder sb, FastJsonResponse.Field<?, ?> field, Parcel parcel, int i) {
        Object objValueOf;
        switch (field.zzpC()) {
            case 0:
                objValueOf = Integer.valueOf(com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, i));
                break;
            case 1:
                objValueOf = com.google.android.gms.common.internal.safeparcel.zza.zzk(parcel, i);
                break;
            case 2:
                objValueOf = Long.valueOf(com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel, i));
                break;
            case 3:
                objValueOf = Float.valueOf(com.google.android.gms.common.internal.safeparcel.zza.zzl(parcel, i));
                break;
            case 4:
                objValueOf = Double.valueOf(com.google.android.gms.common.internal.safeparcel.zza.zzn(parcel, i));
                break;
            case 5:
                objValueOf = com.google.android.gms.common.internal.safeparcel.zza.zzo(parcel, i);
                break;
            case 6:
                objValueOf = Boolean.valueOf(com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, i));
                break;
            case 7:
                objValueOf = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, i);
                break;
            case 8:
            case 9:
                objValueOf = com.google.android.gms.common.internal.safeparcel.zza.zzs(parcel, i);
                break;
            case 10:
                objValueOf = zzi(com.google.android.gms.common.internal.safeparcel.zza.zzr(parcel, i));
                break;
            case 11:
                throw new IllegalArgumentException("Method does not accept concrete type.");
            default:
                throw new IllegalArgumentException("Unknown field out type = " + field.zzpC());
        }
        zzb(sb, field, zza(field, objValueOf));
    }

    private void zza(StringBuilder sb, String str, FastJsonResponse.Field<?, ?> field, Parcel parcel, int i) {
        sb.append("\"");
        sb.append(str);
        sb.append("\":");
        if (field.zzpN()) {
            zza(sb, field, parcel, i);
        } else {
            zzb(sb, field, parcel, i);
        }
    }

    private void zza(StringBuilder sb, Map<String, FastJsonResponse.Field<?, ?>> map, Parcel parcel) {
        HashMap<Integer, Map.Entry<String, FastJsonResponse.Field<?, ?>>> mapZzG = zzG(map);
        sb.append('{');
        int iZzap = com.google.android.gms.common.internal.safeparcel.zza.zzap(parcel);
        boolean z = false;
        while (parcel.dataPosition() < iZzap) {
            int iZzao = com.google.android.gms.common.internal.safeparcel.zza.zzao(parcel);
            Map.Entry<String, FastJsonResponse.Field<?, ?>> entry = mapZzG.get(Integer.valueOf(com.google.android.gms.common.internal.safeparcel.zza.zzbM(iZzao)));
            if (entry != null) {
                if (z) {
                    sb.append(",");
                }
                zza(sb, entry.getKey(), entry.getValue(), parcel, iZzao);
                z = true;
            }
        }
        if (parcel.dataPosition() == iZzap) {
            sb.append('}');
            return;
        }
        throw new zza.C0010zza("Overread allowed size end=" + iZzap, parcel);
    }

    private static FieldMappingDictionary zzb(FastJsonResponse fastJsonResponse) {
        FieldMappingDictionary fieldMappingDictionary = new FieldMappingDictionary(fastJsonResponse.getClass());
        zza(fieldMappingDictionary, fastJsonResponse);
        fieldMappingDictionary.zzpR();
        fieldMappingDictionary.zzpQ();
        return fieldMappingDictionary;
    }

    private void zzb(StringBuilder sb, FastJsonResponse.Field<?, ?> field, Parcel parcel, int i) {
        Object objZzk;
        String strZzcz;
        String str;
        Object[] objArrZzx;
        if (field.zzpI()) {
            sb.append("[");
            switch (field.zzpC()) {
                case 0:
                    zzmj.zza(sb, com.google.android.gms.common.internal.safeparcel.zza.zzv(parcel, i));
                    str = "]";
                    break;
                case 1:
                    objArrZzx = com.google.android.gms.common.internal.safeparcel.zza.zzx(parcel, i);
                    zzmj.zza(sb, objArrZzx);
                    str = "]";
                    break;
                case 2:
                    zzmj.zza(sb, com.google.android.gms.common.internal.safeparcel.zza.zzw(parcel, i));
                    str = "]";
                    break;
                case 3:
                    zzmj.zza(sb, com.google.android.gms.common.internal.safeparcel.zza.zzy(parcel, i));
                    str = "]";
                    break;
                case 4:
                    zzmj.zza(sb, com.google.android.gms.common.internal.safeparcel.zza.zzz(parcel, i));
                    str = "]";
                    break;
                case 5:
                    objArrZzx = com.google.android.gms.common.internal.safeparcel.zza.zzA(parcel, i);
                    zzmj.zza(sb, objArrZzx);
                    str = "]";
                    break;
                case 6:
                    zzmj.zza(sb, com.google.android.gms.common.internal.safeparcel.zza.zzu(parcel, i));
                    str = "]";
                    break;
                case 7:
                    zzmj.zza(sb, com.google.android.gms.common.internal.safeparcel.zza.zzB(parcel, i));
                    str = "]";
                    break;
                case 8:
                case 9:
                case 10:
                    throw new UnsupportedOperationException("List of type BASE64, BASE64_URL_SAFE, or STRING_MAP is not supported");
                case 11:
                    Parcel[] parcelArrZzF = com.google.android.gms.common.internal.safeparcel.zza.zzF(parcel, i);
                    int length = parcelArrZzF.length;
                    for (int i2 = 0; i2 < length; i2++) {
                        if (i2 > 0) {
                            sb.append(",");
                        }
                        parcelArrZzF[i2].setDataPosition(0);
                        zza(sb, field.zzpP(), parcelArrZzF[i2]);
                    }
                    str = "]";
                    break;
                default:
                    throw new IllegalStateException("Unknown field type out.");
            }
        } else {
            switch (field.zzpC()) {
                case 0:
                    sb.append(com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, i));
                    return;
                case 1:
                    objZzk = com.google.android.gms.common.internal.safeparcel.zza.zzk(parcel, i);
                    sb.append(objZzk);
                    return;
                case 2:
                    sb.append(com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel, i));
                    return;
                case 3:
                    sb.append(com.google.android.gms.common.internal.safeparcel.zza.zzl(parcel, i));
                    return;
                case 4:
                    sb.append(com.google.android.gms.common.internal.safeparcel.zza.zzn(parcel, i));
                    return;
                case 5:
                    objZzk = com.google.android.gms.common.internal.safeparcel.zza.zzo(parcel, i);
                    sb.append(objZzk);
                    return;
                case 6:
                    sb.append(com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, i));
                    return;
                case 7:
                    String strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, i);
                    sb.append("\"");
                    strZzcz = zzmu.zzcz(strZzp);
                    sb.append(strZzcz);
                    str = "\"";
                    break;
                case 8:
                    byte[] bArrZzs = com.google.android.gms.common.internal.safeparcel.zza.zzs(parcel, i);
                    sb.append("\"");
                    strZzcz = zzmk.zzi(bArrZzs);
                    sb.append(strZzcz);
                    str = "\"";
                    break;
                case 9:
                    byte[] bArrZzs2 = com.google.android.gms.common.internal.safeparcel.zza.zzs(parcel, i);
                    sb.append("\"");
                    strZzcz = zzmk.zzj(bArrZzs2);
                    sb.append(strZzcz);
                    str = "\"";
                    break;
                case 10:
                    Bundle bundleZzr = com.google.android.gms.common.internal.safeparcel.zza.zzr(parcel, i);
                    Set<String> setKeySet = bundleZzr.keySet();
                    setKeySet.size();
                    sb.append("{");
                    boolean z = true;
                    for (String str2 : setKeySet) {
                        if (!z) {
                            sb.append(",");
                        }
                        sb.append("\"");
                        sb.append(str2);
                        sb.append("\"");
                        sb.append(":");
                        sb.append("\"");
                        sb.append(zzmu.zzcz(bundleZzr.getString(str2)));
                        sb.append("\"");
                        z = false;
                    }
                    str = "}";
                    break;
                case 11:
                    Parcel parcelZzE = com.google.android.gms.common.internal.safeparcel.zza.zzE(parcel, i);
                    parcelZzE.setDataPosition(0);
                    zza(sb, field.zzpP(), parcelZzE);
                    return;
                default:
                    throw new IllegalStateException("Unknown field type out");
            }
        }
        sb.append(str);
    }

    private void zzb(StringBuilder sb, FastJsonResponse.Field<?, ?> field, Object obj) {
        if (field.zzpH()) {
            zzb(sb, field, (ArrayList<?>) obj);
        } else {
            zza(sb, field.zzpB(), obj);
        }
    }

    private void zzb(StringBuilder sb, FastJsonResponse.Field<?, ?> field, ArrayList<?> arrayList) {
        sb.append("[");
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                sb.append(",");
            }
            zza(sb, field.zzpB(), arrayList.get(i));
        }
        sb.append("]");
    }

    public static HashMap<String, String> zzi(Bundle bundle) {
        HashMap<String, String> map = new HashMap<>();
        for (String str : bundle.keySet()) {
            map.put(str, bundle.getString(str));
        }
        return map;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        zze zzeVar = CREATOR;
        return 0;
    }

    public int getVersionCode() {
        return this.mVersionCode;
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    public String toString() {
        zzx.zzb(this.zzahc, "Cannot convert to JSON on client side.");
        Parcel parcelZzpV = zzpV();
        parcelZzpV.setDataPosition(0);
        StringBuilder sb = new StringBuilder(100);
        zza(sb, this.zzahc.zzcw(this.mClassName), parcelZzpV);
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        zze zzeVar = CREATOR;
        zze.zza(this, parcel, i);
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    protected Object zzcs(String str) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    protected boolean zzct(String str) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    public Map<String, FastJsonResponse.Field<?, ?>> zzpD() {
        if (this.zzahc == null) {
            return null;
        }
        return this.zzahc.zzcw(this.mClassName);
    }

    public Parcel zzpV() {
        switch (this.zzahl) {
            case 0:
                this.zzahm = com.google.android.gms.common.internal.safeparcel.zzb.zzaq(this.zzahj);
            case 1:
                com.google.android.gms.common.internal.safeparcel.zzb.zzI(this.zzahj, this.zzahm);
                this.zzahl = 2;
                break;
        }
        return this.zzahj;
    }

    FieldMappingDictionary zzpW() {
        switch (this.zzahk) {
            case 0:
                return null;
            case 1:
                return this.zzahc;
            case 2:
                return this.zzahc;
            default:
                throw new IllegalStateException("Invalid creation type: " + this.zzahk);
        }
    }
}
