package com.google.android.gms.common.internal;

import java.util.Iterator;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzv {
    private final String separator;

    private zzv(String str) {
        this.separator = str;
    }

    public static zzv zzcq(String str) {
        return new zzv(str);
    }

    public final String zza(Iterable<?> iterable) {
        return zza(new StringBuilder(), iterable).toString();
    }

    public final StringBuilder zza(StringBuilder sb, Iterable<?> iterable) {
        Iterator<?> it = iterable.iterator();
        if (it.hasNext()) {
            while (true) {
                sb.append(zzu(it.next()));
                if (!it.hasNext()) {
                    break;
                }
                sb.append(this.separator);
            }
        }
        return sb;
    }

    CharSequence zzu(Object obj) {
        return obj instanceof CharSequence ? (CharSequence) obj : obj.toString();
    }
}
