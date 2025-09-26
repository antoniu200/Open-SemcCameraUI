package com.google.android.gms.common.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class zzw {

    public static final class zza {
        private final Object zzJm;
        private final List<String> zzago;

        private zza(Object obj) {
            this.zzJm = zzx.zzw(obj);
            this.zzago = new ArrayList();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(100);
            sb.append(this.zzJm.getClass().getSimpleName());
            sb.append('{');
            int size = this.zzago.size();
            for (int i = 0; i < size; i++) {
                sb.append(this.zzago.get(i));
                if (i < size - 1) {
                    sb.append(", ");
                }
            }
            sb.append('}');
            return sb.toString();
        }

        public zza zzg(String str, Object obj) {
            this.zzago.add(((String) zzx.zzw(str)) + "=" + String.valueOf(obj));
            return this;
        }
    }

    public static boolean equal(Object obj, Object obj2) {
        if (obj != obj2) {
            return obj != null && obj.equals(obj2);
        }
        return true;
    }

    public static int hashCode(Object... objArr) {
        return Arrays.hashCode(objArr);
    }

    public static zza zzv(Object obj) {
        return new zza(obj);
    }
}
