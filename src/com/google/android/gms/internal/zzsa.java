package com.google.android.gms.internal;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class zzsa implements Cloneable {
    private static final zzsb zzbin = new zzsb();
    private int mSize;
    private boolean zzbio;
    private int[] zzbip;
    private zzsb[] zzbiq;

    zzsa() {
        this(10);
    }

    zzsa(int i) {
        this.zzbio = false;
        int iIdealIntArraySize = idealIntArraySize(i);
        this.zzbip = new int[iIdealIntArraySize];
        this.zzbiq = new zzsb[iIdealIntArraySize];
        this.mSize = 0;
    }

    private void gc() {
        int i = this.mSize;
        int[] iArr = this.zzbip;
        zzsb[] zzsbVarArr = this.zzbiq;
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3++) {
            zzsb zzsbVar = zzsbVarArr[i3];
            if (zzsbVar != zzbin) {
                if (i3 != i2) {
                    iArr[i2] = iArr[i3];
                    zzsbVarArr[i2] = zzsbVar;
                    zzsbVarArr[i3] = null;
                }
                i2++;
            }
        }
        this.zzbio = false;
        this.mSize = i2;
    }

    private int idealByteArraySize(int i) {
        for (int i2 = 4; i2 < 32; i2++) {
            int i3 = (1 << i2) - 12;
            if (i <= i3) {
                return i3;
            }
        }
        return i;
    }

    private int idealIntArraySize(int i) {
        return idealByteArraySize(i * 4) / 4;
    }

    private boolean zza(int[] iArr, int[] iArr2, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            if (iArr[i2] != iArr2[i2]) {
                return false;
            }
        }
        return true;
    }

    private boolean zza(zzsb[] zzsbVarArr, zzsb[] zzsbVarArr2, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            if (!zzsbVarArr[i2].equals(zzsbVarArr2[i2])) {
                return false;
            }
        }
        return true;
    }

    private int zzlT(int i) {
        int i2 = this.mSize - 1;
        int i3 = 0;
        while (i3 <= i2) {
            int i4 = (i3 + i2) >>> 1;
            int i5 = this.zzbip[i4];
            if (i5 < i) {
                i3 = i4 + 1;
            } else {
                if (i5 <= i) {
                    return i4;
                }
                i2 = i4 - 1;
            }
        }
        return ~i3;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzsa)) {
            return false;
        }
        zzsa zzsaVar = (zzsa) obj;
        if (size() != zzsaVar.size()) {
            return false;
        }
        return zza(this.zzbip, zzsaVar.zzbip, this.mSize) && zza(this.zzbiq, zzsaVar.zzbiq, this.mSize);
    }

    public int hashCode() {
        if (this.zzbio) {
            gc();
        }
        int iHashCode = 17;
        for (int i = 0; i < this.mSize; i++) {
            iHashCode = this.zzbiq[i].hashCode() + (31 * ((iHashCode * 31) + this.zzbip[i]));
        }
        return iHashCode;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    int size() {
        if (this.zzbio) {
            gc();
        }
        return this.mSize;
    }

    /* renamed from: zzFH, reason: merged with bridge method [inline-methods] */
    public final zzsa clone() {
        int size = size();
        zzsa zzsaVar = new zzsa(size);
        System.arraycopy(this.zzbip, 0, zzsaVar.zzbip, 0, size);
        for (int i = 0; i < size; i++) {
            if (this.zzbiq[i] != null) {
                zzsaVar.zzbiq[i] = this.zzbiq[i].clone();
            }
        }
        zzsaVar.mSize = size;
        return zzsaVar;
    }

    void zza(int i, zzsb zzsbVar) {
        int iZzlT = zzlT(i);
        if (iZzlT >= 0) {
            this.zzbiq[iZzlT] = zzsbVar;
            return;
        }
        int i2 = ~iZzlT;
        if (i2 < this.mSize && this.zzbiq[i2] == zzbin) {
            this.zzbip[i2] = i;
            this.zzbiq[i2] = zzsbVar;
            return;
        }
        if (this.zzbio && this.mSize >= this.zzbip.length) {
            gc();
            i2 = ~zzlT(i);
        }
        if (this.mSize >= this.zzbip.length) {
            int iIdealIntArraySize = idealIntArraySize(this.mSize + 1);
            int[] iArr = new int[iIdealIntArraySize];
            zzsb[] zzsbVarArr = new zzsb[iIdealIntArraySize];
            System.arraycopy(this.zzbip, 0, iArr, 0, this.zzbip.length);
            System.arraycopy(this.zzbiq, 0, zzsbVarArr, 0, this.zzbiq.length);
            this.zzbip = iArr;
            this.zzbiq = zzsbVarArr;
        }
        if (this.mSize - i2 != 0) {
            int i3 = i2 + 1;
            System.arraycopy(this.zzbip, i2, this.zzbip, i3, this.mSize - i2);
            System.arraycopy(this.zzbiq, i2, this.zzbiq, i3, this.mSize - i2);
        }
        this.zzbip[i2] = i;
        this.zzbiq[i2] = zzsbVar;
        this.mSize++;
    }

    zzsb zzlR(int i) {
        int iZzlT = zzlT(i);
        if (iZzlT < 0 || this.zzbiq[iZzlT] == zzbin) {
            return null;
        }
        return this.zzbiq[iZzlT];
    }

    zzsb zzlS(int i) {
        if (this.zzbio) {
            gc();
        }
        return this.zzbiq[i];
    }
}
