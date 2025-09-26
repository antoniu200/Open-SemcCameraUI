package com.google.android.gms.common.server.response;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.server.converter.ConverterWrapper;
import com.google.android.gms.internal.zzmk;
import com.google.android.gms.internal.zzmu;
import com.google.android.gms.internal.zzmv;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class FastJsonResponse {

    public static class Field<I, O> implements SafeParcelable {
        public static final com.google.android.gms.common.server.response.zza CREATOR = new com.google.android.gms.common.server.response.zza();
        private final int mVersionCode;
        protected final int zzagU;
        protected final boolean zzagV;
        protected final int zzagW;
        protected final boolean zzagX;
        protected final String zzagY;
        protected final int zzagZ;
        protected final Class<? extends FastJsonResponse> zzaha;
        protected final String zzahb;
        private FieldMappingDictionary zzahc;
        private zza<I, O> zzahd;

        /* JADX WARN: Unreachable blocks removed: 1, instructions: 1 */
        Field(int i, int i2, boolean z, int i3, boolean z2, String str, int i4, String str2, ConverterWrapper converterWrapper) {
            this.mVersionCode = i;
            this.zzagU = i2;
            this.zzagV = z;
            this.zzagW = i3;
            this.zzagX = z2;
            this.zzagY = str;
            this.zzagZ = i4;
            zza<I, O> zzaVar = null;
            if (str2 == null) {
                this.zzaha = null;
                this.zzahb = null;
            } else {
                this.zzaha = SafeParcelResponse.class;
                this.zzahb = str2;
            }
            this.zzahd = converterWrapper != null ? (zza<I, O>) converterWrapper.zzpz() : zzaVar;
        }

        protected Field(int i, boolean z, int i2, boolean z2, String str, int i3, Class<? extends FastJsonResponse> cls, zza<I, O> zzaVar) {
            this.mVersionCode = 1;
            this.zzagU = i;
            this.zzagV = z;
            this.zzagW = i2;
            this.zzagX = z2;
            this.zzagY = str;
            this.zzagZ = i3;
            this.zzaha = cls;
            this.zzahb = cls == null ? null : cls.getCanonicalName();
            this.zzahd = zzaVar;
        }

        public static Field zza(String str, int i, zza<?, ?> zzaVar, boolean z) {
            return new Field(zzaVar.zzpB(), z, zzaVar.zzpC(), false, str, i, null, zzaVar);
        }

        public static <T extends FastJsonResponse> Field<T, T> zza(String str, int i, Class<T> cls) {
            return new Field<>(11, false, 11, false, str, i, cls, null);
        }

        public static <T extends FastJsonResponse> Field<ArrayList<T>, ArrayList<T>> zzb(String str, int i, Class<T> cls) {
            return new Field<>(11, true, 11, true, str, i, cls, null);
        }

        public static Field<Integer, Integer> zzj(String str, int i) {
            return new Field<>(0, false, 0, false, str, i, null, null);
        }

        public static Field<Double, Double> zzk(String str, int i) {
            return new Field<>(4, false, 4, false, str, i, null, null);
        }

        public static Field<Boolean, Boolean> zzl(String str, int i) {
            return new Field<>(6, false, 6, false, str, i, null, null);
        }

        public static Field<String, String> zzm(String str, int i) {
            return new Field<>(7, false, 7, false, str, i, null, null);
        }

        public static Field<ArrayList<String>, ArrayList<String>> zzn(String str, int i) {
            return new Field<>(7, true, 7, true, str, i, null, null);
        }

        public I convertBack(O o) {
            return this.zzahd.convertBack(o);
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            com.google.android.gms.common.server.response.zza zzaVar = CREATOR;
            return 0;
        }

        public int getVersionCode() {
            return this.mVersionCode;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Field\n");
            sb.append("            versionCode=");
            sb.append(this.mVersionCode);
            sb.append('\n');
            sb.append("                 typeIn=");
            sb.append(this.zzagU);
            sb.append('\n');
            sb.append("            typeInArray=");
            sb.append(this.zzagV);
            sb.append('\n');
            sb.append("                typeOut=");
            sb.append(this.zzagW);
            sb.append('\n');
            sb.append("           typeOutArray=");
            sb.append(this.zzagX);
            sb.append('\n');
            sb.append("        outputFieldName=");
            sb.append(this.zzagY);
            sb.append('\n');
            sb.append("      safeParcelFieldId=");
            sb.append(this.zzagZ);
            sb.append('\n');
            sb.append("       concreteTypeName=");
            sb.append(zzpM());
            sb.append('\n');
            if (zzpL() != null) {
                sb.append("     concreteType.class=");
                sb.append(zzpL().getCanonicalName());
                sb.append('\n');
            }
            sb.append("          converterName=");
            sb.append(this.zzahd == null ? "null" : this.zzahd.getClass().getCanonicalName());
            sb.append('\n');
            return sb.toString();
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            com.google.android.gms.common.server.response.zza zzaVar = CREATOR;
            com.google.android.gms.common.server.response.zza.zza(this, parcel, i);
        }

        public void zza(FieldMappingDictionary fieldMappingDictionary) {
            this.zzahc = fieldMappingDictionary;
        }

        public int zzpB() {
            return this.zzagU;
        }

        public int zzpC() {
            return this.zzagW;
        }

        public Field<I, O> zzpG() {
            return new Field<>(this.mVersionCode, this.zzagU, this.zzagV, this.zzagW, this.zzagX, this.zzagY, this.zzagZ, this.zzahb, zzpO());
        }

        public boolean zzpH() {
            return this.zzagV;
        }

        public boolean zzpI() {
            return this.zzagX;
        }

        public String zzpJ() {
            return this.zzagY;
        }

        public int zzpK() {
            return this.zzagZ;
        }

        public Class<? extends FastJsonResponse> zzpL() {
            return this.zzaha;
        }

        String zzpM() {
            if (this.zzahb == null) {
                return null;
            }
            return this.zzahb;
        }

        public boolean zzpN() {
            return this.zzahd != null;
        }

        ConverterWrapper zzpO() {
            if (this.zzahd == null) {
                return null;
            }
            return ConverterWrapper.zza(this.zzahd);
        }

        public Map<String, Field<?, ?>> zzpP() {
            zzx.zzw(this.zzahb);
            zzx.zzw(this.zzahc);
            return this.zzahc.zzcw(this.zzahb);
        }
    }

    public interface zza<I, O> {
        I convertBack(O o);

        int zzpB();

        int zzpC();
    }

    private void zza(StringBuilder sb, Field field, Object obj) {
        String string;
        if (field.zzpB() == 11) {
            string = field.zzpL().cast(obj).toString();
        } else if (field.zzpB() != 7) {
            sb.append(obj);
            return;
        } else {
            sb.append("\"");
            sb.append(zzmu.zzcz((String) obj));
            string = "\"";
        }
        sb.append(string);
    }

    private void zza(StringBuilder sb, Field field, ArrayList<Object> arrayList) {
        sb.append("[");
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(",");
            }
            Object obj = arrayList.get(i);
            if (obj != null) {
                zza(sb, field, obj);
            }
        }
        sb.append("]");
    }

    public String toString() {
        String str;
        String strZzi;
        Map<String, Field<?, ?>> mapZzpD = zzpD();
        StringBuilder sb = new StringBuilder(100);
        for (String str2 : mapZzpD.keySet()) {
            Field<?, ?> field = mapZzpD.get(str2);
            if (zza(field)) {
                Object objZza = zza(field, zzb(field));
                sb.append(sb.length() == 0 ? "{" : ",");
                sb.append("\"");
                sb.append(str2);
                sb.append("\":");
                if (objZza == null) {
                    str = "null";
                } else {
                    switch (field.zzpC()) {
                        case 8:
                            sb.append("\"");
                            strZzi = zzmk.zzi((byte[]) objZza);
                            break;
                        case 9:
                            sb.append("\"");
                            strZzi = zzmk.zzj((byte[]) objZza);
                            break;
                        case 10:
                            zzmv.zza(sb, (HashMap) objZza);
                            continue;
                        default:
                            if (field.zzpH()) {
                                zza(sb, (Field) field, (ArrayList<Object>) objZza);
                                break;
                            } else {
                                zza(sb, field, objZza);
                                continue;
                            }
                    }
                    sb.append(strZzi);
                    str = "\"";
                }
                sb.append(str);
            }
        }
        sb.append(sb.length() > 0 ? "}" : "{}");
        return sb.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected <O, I> I zza(Field<I, O> field, Object obj) {
        return ((Field) field).zzahd != null ? field.convertBack(obj) : obj;
    }

    protected boolean zza(Field field) {
        return field.zzpC() == 11 ? field.zzpI() ? zzcv(field.zzpJ()) : zzcu(field.zzpJ()) : zzct(field.zzpJ());
    }

    protected Object zzb(Field field) {
        String strZzpJ = field.zzpJ();
        if (field.zzpL() == null) {
            return zzcs(field.zzpJ());
        }
        zzx.zza(zzcs(field.zzpJ()) == null, "Concrete field shouldn't be value object: %s", field.zzpJ());
        HashMap<String, Object> mapZzpF = field.zzpI() ? zzpF() : zzpE();
        if (mapZzpF != null) {
            return mapZzpF.get(strZzpJ);
        }
        try {
            return getClass().getMethod("get" + Character.toUpperCase(strZzpJ.charAt(0)) + strZzpJ.substring(1), new Class[0]).invoke(this, new Object[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract Object zzcs(String str);

    protected abstract boolean zzct(String str);

    protected boolean zzcu(String str) {
        throw new UnsupportedOperationException("Concrete types not supported");
    }

    protected boolean zzcv(String str) {
        throw new UnsupportedOperationException("Concrete type arrays not supported");
    }

    public abstract Map<String, Field<?, ?>> zzpD();

    public HashMap<String, Object> zzpE() {
        return null;
    }

    public HashMap<String, Object> zzpF() {
        return null;
    }
}
