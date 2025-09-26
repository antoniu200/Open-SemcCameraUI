// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.server.response;

import android.os.Parcel;
import com.google.android.gms.common.server.converter.ConverterWrapper;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;
import java.util.Iterator;
import java.util.Map;
import com.google.android.gms.internal.zzmk;
import com.google.android.gms.internal.zzmv;
import java.util.HashMap;
import java.util.ArrayList;
import com.google.android.gms.internal.zzmu;

public abstract class FastJsonResponse
{
    private void zza(final StringBuilder sb, final Field field, final Object o) {
        String string;
        if (field.zzpB() == 11) {
            string = ((FastJsonResponse)field.zzpL().cast(o)).toString();
        }
        else {
            if (field.zzpB() != 7) {
                sb.append(o);
                return;
            }
            sb.append("\"");
            sb.append(zzmu.zzcz((String)o));
            string = "\"";
        }
        sb.append(string);
    }
    
    private void zza(final StringBuilder sb, final Field field, final ArrayList<Object> list) {
        sb.append("[");
        for (int size = list.size(), i = 0; i < size; ++i) {
            if (i > 0) {
                sb.append(",");
            }
            final Object value = list.get(i);
            if (value != null) {
                this.zza(sb, field, value);
            }
        }
        sb.append("]");
    }
    
    @Override
    public String toString() {
        final Map<String, Field<?, ?>> zzpD = this.zzpD();
        final StringBuilder sb = new StringBuilder(100);
        for (final String str : zzpD.keySet()) {
            final Field field = zzpD.get(str);
            if (!this.zza(field)) {
                continue;
            }
            final ArrayList<Object> zza = this.zza((Field<ArrayList<Object>, Object>)field, this.zzb(field));
            String str2;
            if (sb.length() == 0) {
                str2 = "{";
            }
            else {
                str2 = ",";
            }
            sb.append(str2);
            sb.append("\"");
            sb.append(str);
            sb.append("\":");
            String str3;
            if (zza == null) {
                str3 = "null";
            }
            else {
                String str4 = null;
                switch (field.zzpC()) {
                    default: {
                        if (field.zzpH()) {
                            this.zza(sb, field, zza);
                            continue;
                        }
                        this.zza(sb, field, (Object)zza);
                        continue;
                    }
                    case 10: {
                        zzmv.zza(sb, (HashMap<String, String>)zza);
                        continue;
                    }
                    case 9: {
                        sb.append("\"");
                        str4 = zzmk.zzj((byte[])(Object)zza);
                        break;
                    }
                    case 8: {
                        sb.append("\"");
                        str4 = zzmk.zzi((byte[])(Object)zza);
                        break;
                    }
                }
                sb.append(str4);
                str3 = "\"";
            }
            sb.append(str3);
        }
        String str5;
        if (sb.length() > 0) {
            str5 = "}";
        }
        else {
            str5 = "{}";
        }
        sb.append(str5);
        return sb.toString();
    }
    
    protected <O, I> I zza(final Field<I, O> field, final Object o) {
        if (((Field<Object, Object>)field).zzahd != null) {
            return field.convertBack(o);
        }
        return (I)o;
    }
    
    protected boolean zza(final Field field) {
        if (field.zzpC() != 11) {
            return this.zzct(field.zzpJ());
        }
        if (field.zzpI()) {
            return this.zzcv(field.zzpJ());
        }
        return this.zzcu(field.zzpJ());
    }
    
    protected Object zzb(final Field field) {
        final String zzpJ = field.zzpJ();
        if (field.zzpL() != null) {
            zzx.zza(this.zzcs(field.zzpJ()) == null, "Concrete field shouldn't be value object: %s", field.zzpJ());
            HashMap<String, Object> hashMap;
            if (field.zzpI()) {
                hashMap = this.zzpF();
            }
            else {
                hashMap = this.zzpE();
            }
            if (hashMap != null) {
                return hashMap.get(zzpJ);
            }
            try {
                final StringBuilder sb = new StringBuilder();
                sb.append("get");
                sb.append(Character.toUpperCase(zzpJ.charAt(0)));
                sb.append(zzpJ.substring(1));
                return this.getClass().getMethod(sb.toString(), (Class<?>[])new Class[0]).invoke(this, new Object[0]);
            }
            catch (final Exception cause) {
                throw new RuntimeException(cause);
            }
        }
        return this.zzcs(field.zzpJ());
    }
    
    protected abstract Object zzcs(final String p0);
    
    protected abstract boolean zzct(final String p0);
    
    protected boolean zzcu(final String s) {
        throw new UnsupportedOperationException("Concrete types not supported");
    }
    
    protected boolean zzcv(final String s) {
        throw new UnsupportedOperationException("Concrete type arrays not supported");
    }
    
    public abstract Map<String, Field<?, ?>> zzpD();
    
    public HashMap<String, Object> zzpE() {
        return null;
    }
    
    public HashMap<String, Object> zzpF() {
        return null;
    }
    
    public static class Field<I, O> implements SafeParcelable
    {
        public static final com.google.android.gms.common.server.response.zza CREATOR;
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
        
        static {
            CREATOR = new com.google.android.gms.common.server.response.zza();
        }
        
        Field(final int mVersionCode, final int zzagU, final boolean zzagV, final int zzagW, final boolean zzagX, final String zzagY, final int zzagZ, final String zzahb, final ConverterWrapper converterWrapper) {
            this.mVersionCode = mVersionCode;
            this.zzagU = zzagU;
            this.zzagV = zzagV;
            this.zzagW = zzagW;
            this.zzagX = zzagX;
            this.zzagY = zzagY;
            this.zzagZ = zzagZ;
            zza<?, ?> zzpz = null;
            if (zzahb == null) {
                this.zzaha = null;
                this.zzahb = null;
            }
            else {
                this.zzaha = SafeParcelResponse.class;
                this.zzahb = zzahb;
            }
            if (converterWrapper != null) {
                zzpz = converterWrapper.zzpz();
            }
            this.zzahd = (zza<I, O>)zzpz;
        }
        
        protected Field(final int zzagU, final boolean zzagV, final int zzagW, final boolean zzagX, String canonicalName, final int zzagZ, final Class<? extends FastJsonResponse> zzaha, final zza<I, O> zzahd) {
            this.mVersionCode = 1;
            this.zzagU = zzagU;
            this.zzagV = zzagV;
            this.zzagW = zzagW;
            this.zzagX = zzagX;
            this.zzagY = canonicalName;
            this.zzagZ = zzagZ;
            this.zzaha = zzaha;
            if (zzaha == null) {
                canonicalName = null;
            }
            else {
                canonicalName = zzaha.getCanonicalName();
            }
            this.zzahb = canonicalName;
            this.zzahd = zzahd;
        }
        
        public static Field zza(final String s, final int n, final zza<?, ?> zza, final boolean b) {
            return new Field(zza.zzpB(), b, zza.zzpC(), false, s, n, null, (zza<I, O>)zza);
        }
        
        public static <T extends FastJsonResponse> Field<T, T> zza(final String s, final int n, final Class<T> clazz) {
            return new Field<T, T>(11, false, 11, false, s, n, clazz, null);
        }
        
        public static <T extends FastJsonResponse> Field<ArrayList<T>, ArrayList<T>> zzb(final String s, final int n, final Class<T> clazz) {
            return new Field<ArrayList<T>, ArrayList<T>>(11, true, 11, true, s, n, clazz, null);
        }
        
        public static Field<Integer, Integer> zzj(final String s, final int n) {
            return new Field<Integer, Integer>(0, false, 0, false, s, n, null, null);
        }
        
        public static Field<Double, Double> zzk(final String s, final int n) {
            return new Field<Double, Double>(4, false, 4, false, s, n, null, null);
        }
        
        public static Field<Boolean, Boolean> zzl(final String s, final int n) {
            return new Field<Boolean, Boolean>(6, false, 6, false, s, n, null, null);
        }
        
        public static Field<String, String> zzm(final String s, final int n) {
            return new Field<String, String>(7, false, 7, false, s, n, null, null);
        }
        
        public static Field<ArrayList<String>, ArrayList<String>> zzn(final String s, final int n) {
            return new Field<ArrayList<String>, ArrayList<String>>(7, true, 7, true, s, n, null, null);
        }
        
        public I convertBack(final O o) {
            return this.zzahd.convertBack(o);
        }
        
        public int describeContents() {
            final com.google.android.gms.common.server.response.zza creator = Field.CREATOR;
            return 0;
        }
        
        public int getVersionCode() {
            return this.mVersionCode;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
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
            sb.append(this.zzpM());
            sb.append('\n');
            if (this.zzpL() != null) {
                sb.append("     concreteType.class=");
                sb.append(this.zzpL().getCanonicalName());
                sb.append('\n');
            }
            sb.append("          converterName=");
            String canonicalName;
            if (this.zzahd == null) {
                canonicalName = "null";
            }
            else {
                canonicalName = this.zzahd.getClass().getCanonicalName();
            }
            sb.append(canonicalName);
            sb.append('\n');
            return sb.toString();
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            final com.google.android.gms.common.server.response.zza creator = Field.CREATOR;
            com.google.android.gms.common.server.response.zza.zza(this, parcel, n);
        }
        
        public void zza(final FieldMappingDictionary zzahc) {
            this.zzahc = zzahc;
        }
        
        public int zzpB() {
            return this.zzagU;
        }
        
        public int zzpC() {
            return this.zzagW;
        }
        
        public Field<I, O> zzpG() {
            return new Field<I, O>(this.mVersionCode, this.zzagU, this.zzagV, this.zzagW, this.zzagX, this.zzagY, this.zzagZ, this.zzahb, this.zzpO());
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
    
    public interface zza<I, O>
    {
        I convertBack(final O p0);
        
        int zzpB();
        
        int zzpC();
    }
}
