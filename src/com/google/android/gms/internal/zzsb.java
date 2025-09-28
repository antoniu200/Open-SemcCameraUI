package com.google.android.gms.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class zzsb implements Cloneable {
    private zzrz<?, ?> zzbir;
    private Object zzbis;
    private List<zzsg> zzbit = new ArrayList();

    zzsb() {
    }

    private byte[] toByteArray() throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        byte[] bArr = new byte[zzB()];
        zza(zzrx.zzC(bArr));
        return bArr;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzsb)) {
            return false;
        }
        zzsb zzsbVar = (zzsb) obj;
        if (this.zzbis != null && zzsbVar.zzbis != null) {
            if (this.zzbir != zzsbVar.zzbir) {
                return false;
            }
            return !this.zzbir.zzbil.isArray() ? this.zzbis.equals(zzsbVar.zzbis) : this.zzbis instanceof byte[] ? Arrays.equals((byte[]) this.zzbis, (byte[]) zzsbVar.zzbis) : this.zzbis instanceof int[] ? Arrays.equals((int[]) this.zzbis, (int[]) zzsbVar.zzbis) : this.zzbis instanceof long[] ? Arrays.equals((long[]) this.zzbis, (long[]) zzsbVar.zzbis) : this.zzbis instanceof float[] ? Arrays.equals((float[]) this.zzbis, (float[]) zzsbVar.zzbis) : this.zzbis instanceof double[] ? Arrays.equals((double[]) this.zzbis, (double[]) zzsbVar.zzbis) : this.zzbis instanceof boolean[] ? Arrays.equals((boolean[]) this.zzbis, (boolean[]) zzsbVar.zzbis) : Arrays.deepEquals((Object[]) this.zzbis, (Object[]) zzsbVar.zzbis);
        }
        if (this.zzbit != null && zzsbVar.zzbit != null) {
            return this.zzbit.equals(zzsbVar.zzbit);
        }
        try {
            return Arrays.equals(toByteArray(), zzsbVar.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public int hashCode() {
        try {
            return 527 + Arrays.hashCode(toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    int zzB() {
        if (this.zzbis != null) {
            return this.zzbir.zzX(this.zzbis);
        }
        Iterator<zzsg> it = this.zzbit.iterator();
        int iZzB = 0;
        while (it.hasNext()) {
            iZzB += it.next().zzB();
        }
        return iZzB;
    }

    /* renamed from: zzFI, reason: merged with bridge method [inline-methods] */
    public final zzsb clone() throws CloneNotSupportedException {
        Object objClone;
        zzsb zzsbVar = new zzsb();
        try {
            zzsbVar.zzbir = this.zzbir;
            if (this.zzbit == null) {
                zzsbVar.zzbit = null;
            } else {
                zzsbVar.zzbit.addAll(this.zzbit);
            }
            if (this.zzbis == null) {
                return zzsbVar;
            }
            if (this.zzbis instanceof zzse) {
                objClone = ((zzse) this.zzbis).clone();
            } else {
                if (!(this.zzbis instanceof byte[])) {
                    int i = 0;
                    if (this.zzbis instanceof byte[][]) {
                        byte[][] bArr = (byte[][]) this.zzbis;
                        byte[][] bArr2 = new byte[bArr.length][];
                        zzsbVar.zzbis = bArr2;
                        while (i < bArr.length) {
                            bArr2[i] = (byte[]) bArr[i].clone();
                            i++;
                        }
                    } else if (this.zzbis instanceof boolean[]) {
                        objClone = ((boolean[]) this.zzbis).clone();
                    } else if (this.zzbis instanceof int[]) {
                        objClone = ((int[]) this.zzbis).clone();
                    } else if (this.zzbis instanceof long[]) {
                        objClone = ((long[]) this.zzbis).clone();
                    } else if (this.zzbis instanceof float[]) {
                        objClone = ((float[]) this.zzbis).clone();
                    } else if (this.zzbis instanceof double[]) {
                        objClone = ((double[]) this.zzbis).clone();
                    } else if (this.zzbis instanceof zzse[]) {
                        zzse[] zzseVarArr = (zzse[]) this.zzbis;
                        zzse[] zzseVarArr2 = new zzse[zzseVarArr.length];
                        zzsbVar.zzbis = zzseVarArr2;
                        while (i < zzseVarArr.length) {
                            zzseVarArr2[i] = zzseVarArr[i].clone();
                            i++;
                        }
                    }
                    return zzsbVar;
                }
                objClone = ((byte[]) this.zzbis).clone();
            }
            zzsbVar.zzbis = objClone;
            return zzsbVar;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    void zza(zzrx zzrxVar) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        if (this.zzbis != null) {
            this.zzbir.zza(this.zzbis, zzrxVar);
            return;
        }
        Iterator<zzsg> it = this.zzbit.iterator();
        while (it.hasNext()) {
            it.next().zza(zzrxVar);
        }
    }

    void zza(zzsg zzsgVar) {
        this.zzbit.add(zzsgVar);
    }

    /* JADX WARN: Multi-variable type inference failed */
    <T> T zzb(zzrz<?, T> zzrzVar) {
        if (this.zzbis == null) {
            this.zzbir = zzrzVar;
            this.zzbis = zzrzVar.zzE(this.zzbit);
            this.zzbit = null;
        } else if (this.zzbir != zzrzVar) {
            throw new IllegalStateException("Tried to getExtension with a differernt Extension.");
        }
        return (T) this.zzbis;
    }
}
