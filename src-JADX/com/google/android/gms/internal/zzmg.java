package com.google.android.gms.internal;

import java.util.LinkedHashMap;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzmg<K, V> {
    private int size;
    private final LinkedHashMap<K, V> zzagB;
    private int zzagC;
    private int zzagD;
    private int zzagE;
    private int zzagF;
    private int zzagG;
    private int zzagH;

    public zzmg(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.zzagC = i;
        this.zzagB = new LinkedHashMap<>(0, 0.75f, true);
    }

    private int zzc(K k, V v) {
        int iSizeOf = sizeOf(k, v);
        if (iSizeOf >= 0) {
            return iSizeOf;
        }
        throw new IllegalStateException("Negative size: " + k + "=" + v);
    }

    protected V create(K k) {
        return null;
    }

    protected void entryRemoved(boolean z, K k, V v, V v2) {
    }

    public final void evictAll() {
        trimToSize(-1);
    }

    public final V get(K k) {
        V v;
        if (k == null) {
            throw new NullPointerException("key == null");
        }
        synchronized (this) {
            V v2 = this.zzagB.get(k);
            if (v2 != null) {
                this.zzagG++;
                return v2;
            }
            this.zzagH++;
            V vCreate = create(k);
            if (vCreate == null) {
                return null;
            }
            synchronized (this) {
                this.zzagE++;
                v = (V) this.zzagB.put(k, vCreate);
                if (v != null) {
                    this.zzagB.put(k, v);
                } else {
                    this.size += zzc(k, vCreate);
                }
            }
            if (v != null) {
                entryRemoved(false, k, vCreate, v);
                return v;
            }
            trimToSize(this.zzagC);
            return vCreate;
        }
    }

    public final V put(K k, V v) {
        V vPut;
        if (k == null || v == null) {
            throw new NullPointerException("key == null || value == null");
        }
        synchronized (this) {
            this.zzagD++;
            this.size += zzc(k, v);
            vPut = this.zzagB.put(k, v);
            if (vPut != null) {
                this.size -= zzc(k, vPut);
            }
        }
        if (vPut != null) {
            entryRemoved(false, k, vPut, v);
        }
        trimToSize(this.zzagC);
        return vPut;
    }

    public final synchronized int size() {
        return this.size;
    }

    protected int sizeOf(K k, V v) {
        return 1;
    }

    public final synchronized String toString() {
        int i;
        i = this.zzagG + this.zzagH;
        return String.format("LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", Integer.valueOf(this.zzagC), Integer.valueOf(this.zzagG), Integer.valueOf(this.zzagH), Integer.valueOf(i != 0 ? (100 * this.zzagG) / i : 0));
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x0071, code lost:
    
        throw new java.lang.IllegalStateException(getClass().getName() + ".sizeOf() is reporting inconsistent results!");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void trimToSize(int r5) {
        /*
            r4 = this;
        L0:
            monitor-enter(r4)
            int r0 = r4.size     // Catch: java.lang.Throwable -> L72
            if (r0 < 0) goto L53
            java.util.LinkedHashMap<K, V> r0 = r4.zzagB     // Catch: java.lang.Throwable -> L72
            boolean r0 = r0.isEmpty()     // Catch: java.lang.Throwable -> L72
            if (r0 == 0) goto L12
            int r0 = r4.size     // Catch: java.lang.Throwable -> L72
            if (r0 == 0) goto L12
            goto L53
        L12:
            int r0 = r4.size     // Catch: java.lang.Throwable -> L72
            if (r0 <= r5) goto L51
            java.util.LinkedHashMap<K, V> r0 = r4.zzagB     // Catch: java.lang.Throwable -> L72
            boolean r0 = r0.isEmpty()     // Catch: java.lang.Throwable -> L72
            if (r0 == 0) goto L1f
            goto L51
        L1f:
            java.util.LinkedHashMap<K, V> r0 = r4.zzagB     // Catch: java.lang.Throwable -> L72
            java.util.Set r0 = r0.entrySet()     // Catch: java.lang.Throwable -> L72
            java.util.Iterator r0 = r0.iterator()     // Catch: java.lang.Throwable -> L72
            java.lang.Object r0 = r0.next()     // Catch: java.lang.Throwable -> L72
            java.util.Map$Entry r0 = (java.util.Map.Entry) r0     // Catch: java.lang.Throwable -> L72
            java.lang.Object r1 = r0.getKey()     // Catch: java.lang.Throwable -> L72
            java.lang.Object r0 = r0.getValue()     // Catch: java.lang.Throwable -> L72
            java.util.LinkedHashMap<K, V> r2 = r4.zzagB     // Catch: java.lang.Throwable -> L72
            r2.remove(r1)     // Catch: java.lang.Throwable -> L72
            int r2 = r4.size     // Catch: java.lang.Throwable -> L72
            int r3 = r4.zzc(r1, r0)     // Catch: java.lang.Throwable -> L72
            int r2 = r2 - r3
            r4.size = r2     // Catch: java.lang.Throwable -> L72
            int r2 = r4.zzagF     // Catch: java.lang.Throwable -> L72
            r3 = 1
            int r2 = r2 + r3
            r4.zzagF = r2     // Catch: java.lang.Throwable -> L72
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L72
            r2 = 0
            r4.entryRemoved(r3, r1, r0, r2)
            goto L0
        L51:
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L72
            return
        L53:
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException     // Catch: java.lang.Throwable -> L72
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L72
            r0.<init>()     // Catch: java.lang.Throwable -> L72
            java.lang.Class r1 = r4.getClass()     // Catch: java.lang.Throwable -> L72
            java.lang.String r1 = r1.getName()     // Catch: java.lang.Throwable -> L72
            r0.append(r1)     // Catch: java.lang.Throwable -> L72
            java.lang.String r1 = ".sizeOf() is reporting inconsistent results!"
            r0.append(r1)     // Catch: java.lang.Throwable -> L72
            java.lang.String r0 = r0.toString()     // Catch: java.lang.Throwable -> L72
            r5.<init>(r0)     // Catch: java.lang.Throwable -> L72
            throw r5     // Catch: java.lang.Throwable -> L72
        L72:
            r5 = move-exception
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L72
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzmg.trimToSize(int):void");
    }
}
