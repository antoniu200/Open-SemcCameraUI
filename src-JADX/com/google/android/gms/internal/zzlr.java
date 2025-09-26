package com.google.android.gms.internal;

import android.os.Binder;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class zzlr<T> {
    private static zza zzadc = null;
    private static int zzadd = 0;
    private static String zzade = "com.google.android.providers.gsf.permission.READ_GSERVICES";
    private static final Object zzpy = new Object();
    private T zzOX = null;
    protected final String zzue;
    protected final T zzuf;

    private interface zza {
        Long getLong(String str, Long l);

        String getString(String str, String str2);

        Boolean zzb(String str, Boolean bool);

        Float zzb(String str, Float f);

        Integer zzb(String str, Integer num);
    }

    protected zzlr(String str, T t) {
        this.zzue = str;
        this.zzuf = t;
    }

    public static boolean isInitialized() {
        return zzadc != null;
    }

    public static zzlr<Float> zza(String str, Float f) {
        return new zzlr<Float>(str, f) { // from class: com.google.android.gms.internal.zzlr.4
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.android.gms.internal.zzlr
            /* renamed from: zzcc, reason: merged with bridge method [inline-methods] */
            public Float zzbY(String str2) {
                return zzlr.zzadc.zzb(this.zzue, (Float) this.zzuf);
            }
        };
    }

    public static zzlr<Integer> zza(String str, Integer num) {
        return new zzlr<Integer>(str, num) { // from class: com.google.android.gms.internal.zzlr.3
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.android.gms.internal.zzlr
            /* renamed from: zzcb, reason: merged with bridge method [inline-methods] */
            public Integer zzbY(String str2) {
                return zzlr.zzadc.zzb(this.zzue, (Integer) this.zzuf);
            }
        };
    }

    public static zzlr<Long> zza(String str, Long l) {
        return new zzlr<Long>(str, l) { // from class: com.google.android.gms.internal.zzlr.2
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.android.gms.internal.zzlr
            /* renamed from: zzca, reason: merged with bridge method [inline-methods] */
            public Long zzbY(String str2) {
                return zzlr.zzadc.getLong(this.zzue, (Long) this.zzuf);
            }
        };
    }

    public static zzlr<Boolean> zzg(String str, boolean z) {
        return new zzlr<Boolean>(str, Boolean.valueOf(z)) { // from class: com.google.android.gms.internal.zzlr.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.android.gms.internal.zzlr
            /* renamed from: zzbZ, reason: merged with bridge method [inline-methods] */
            public Boolean zzbY(String str2) {
                return zzlr.zzadc.zzb(this.zzue, (Boolean) this.zzuf);
            }
        };
    }

    public static int zzoo() {
        return zzadd;
    }

    public static zzlr<String> zzu(String str, String str2) {
        return new zzlr<String>(str, str2) { // from class: com.google.android.gms.internal.zzlr.5
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.android.gms.internal.zzlr
            /* renamed from: zzcd, reason: merged with bridge method [inline-methods] */
            public String zzbY(String str3) {
                return zzlr.zzadc.getString(this.zzue, (String) this.zzuf);
            }
        };
    }

    public final T get() {
        return this.zzOX != null ? this.zzOX : zzbY(this.zzue);
    }

    protected abstract T zzbY(String str);

    public final T zzop() {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return get();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }
}
