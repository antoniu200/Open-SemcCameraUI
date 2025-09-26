// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

public final class zzsa implements Cloneable
{
    private static final zzsb zzbin;
    private int mSize;
    private boolean zzbio;
    private int[] zzbip;
    private zzsb[] zzbiq;
    
    static {
        zzbin = new zzsb();
    }
    
    zzsa() {
        this(10);
    }
    
    zzsa(int idealIntArraySize) {
        this.zzbio = false;
        idealIntArraySize = this.idealIntArraySize(idealIntArraySize);
        this.zzbip = new int[idealIntArraySize];
        this.zzbiq = new zzsb[idealIntArraySize];
        this.mSize = 0;
    }
    
    private void gc() {
        final int mSize = this.mSize;
        final int[] zzbip = this.zzbip;
        final zzsb[] zzbiq = this.zzbiq;
        int i = 0;
        int mSize2 = 0;
        while (i < mSize) {
            final zzsb zzsb = zzbiq[i];
            int n = mSize2;
            if (zzsb != zzsa.zzbin) {
                if (i != mSize2) {
                    zzbip[mSize2] = zzbip[i];
                    zzbiq[mSize2] = zzsb;
                    zzbiq[i] = null;
                }
                n = mSize2 + 1;
            }
            ++i;
            mSize2 = n;
        }
        this.zzbio = false;
        this.mSize = mSize2;
    }
    
    private int idealByteArraySize(final int n) {
        for (int i = 4; i < 32; ++i) {
            final int n2 = (1 << i) - 12;
            if (n <= n2) {
                return n2;
            }
        }
        return n;
    }
    
    private int idealIntArraySize(final int n) {
        return this.idealByteArraySize(n * 4) / 4;
    }
    
    private boolean zza(final int[] array, final int[] array2, final int n) {
        for (int i = 0; i < n; ++i) {
            if (array[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }
    
    private boolean zza(final zzsb[] array, final zzsb[] array2, final int n) {
        for (int i = 0; i < n; ++i) {
            if (!array[i].equals(array2[i])) {
                return false;
            }
        }
        return true;
    }
    
    private int zzlT(final int n) {
        int n2 = this.mSize - 1;
        int i = 0;
        while (i <= n2) {
            final int n3 = i + n2 >>> 1;
            final int n4 = this.zzbip[n3];
            if (n4 < n) {
                i = n3 + 1;
            }
            else {
                if (n4 <= n) {
                    return n3;
                }
                n2 = n3 - 1;
            }
        }
        return ~i;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zzsa)) {
            return false;
        }
        final zzsa zzsa = (zzsa)o;
        return this.size() == zzsa.size() && (this.zza(this.zzbip, zzsa.zzbip, this.mSize) && this.zza(this.zzbiq, zzsa.zzbiq, this.mSize));
    }
    
    @Override
    public int hashCode() {
        if (this.zzbio) {
            this.gc();
        }
        int n = 17;
        for (int i = 0; i < this.mSize; ++i) {
            n = this.zzbiq[i].hashCode() + 31 * (n * 31 + this.zzbip[i]);
        }
        return n;
    }
    
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    int size() {
        if (this.zzbio) {
            this.gc();
        }
        return this.mSize;
    }
    
    public final zzsa zzFH() {
        final int size = this.size();
        final zzsa zzsa = new zzsa(size);
        final int[] zzbip = this.zzbip;
        final int[] zzbip2 = zzsa.zzbip;
        int i = 0;
        System.arraycopy(zzbip, 0, zzbip2, 0, size);
        while (i < size) {
            if (this.zzbiq[i] != null) {
                zzsa.zzbiq[i] = this.zzbiq[i].zzFI();
            }
            ++i;
        }
        zzsa.mSize = size;
        return zzsa;
    }
    
    void zza(final int n, final zzsb zzsb) {
        final int zzlT = this.zzlT(n);
        if (zzlT >= 0) {
            this.zzbiq[zzlT] = zzsb;
            return;
        }
        final int n2 = ~zzlT;
        if (n2 < this.mSize && this.zzbiq[n2] == zzsa.zzbin) {
            this.zzbip[n2] = n;
            this.zzbiq[n2] = zzsb;
            return;
        }
        int n3 = n2;
        if (this.zzbio) {
            n3 = n2;
            if (this.mSize >= this.zzbip.length) {
                this.gc();
                n3 = ~this.zzlT(n);
            }
        }
        if (this.mSize >= this.zzbip.length) {
            final int idealIntArraySize = this.idealIntArraySize(this.mSize + 1);
            final int[] zzbip = new int[idealIntArraySize];
            final zzsb[] zzbiq = new zzsb[idealIntArraySize];
            System.arraycopy(this.zzbip, 0, zzbip, 0, this.zzbip.length);
            System.arraycopy(this.zzbiq, 0, zzbiq, 0, this.zzbiq.length);
            this.zzbip = zzbip;
            this.zzbiq = zzbiq;
        }
        if (this.mSize - n3 != 0) {
            final int[] zzbip2 = this.zzbip;
            final int[] zzbip3 = this.zzbip;
            final int n4 = n3 + 1;
            System.arraycopy(zzbip2, n3, zzbip3, n4, this.mSize - n3);
            System.arraycopy(this.zzbiq, n3, this.zzbiq, n4, this.mSize - n3);
        }
        this.zzbip[n3] = n;
        this.zzbiq[n3] = zzsb;
        ++this.mSize;
    }
    
    zzsb zzlR(int zzlT) {
        zzlT = this.zzlT(zzlT);
        if (zzlT >= 0 && this.zzbiq[zzlT] != zzsa.zzbin) {
            return this.zzbiq[zzlT];
        }
        return null;
    }
    
    zzsb zzlS(final int n) {
        if (this.zzbio) {
            this.gc();
        }
        return this.zzbiq[n];
    }
}
