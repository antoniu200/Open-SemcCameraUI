package com.google.android.gms.internal;

import java.io.IOException;
import kotlin.jvm.internal.ByteCompanionObject;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class zzrw {
    private final byte[] buffer;
    private int zzbia;
    private int zzbib;
    private int zzbic;
    private int zzbid;
    private int zzbie;
    private int zzbig;
    private int zzbif = Integer.MAX_VALUE;
    private int zzbih = 64;
    private int zzbii = 67108864;

    private zzrw(byte[] bArr, int i, int i2) {
        this.buffer = bArr;
        this.zzbia = i;
        this.zzbib = i2 + i;
        this.zzbid = i;
    }

    public static zzrw zzB(byte[] bArr) {
        return zza(bArr, 0, bArr.length);
    }

    private void zzFz() {
        this.zzbib += this.zzbic;
        int i = this.zzbib;
        if (i <= this.zzbif) {
            this.zzbic = 0;
        } else {
            this.zzbic = i - this.zzbif;
            this.zzbib -= this.zzbic;
        }
    }

    public static long zzX(long j) {
        return (-(j & 1)) ^ (j >>> 1);
    }

    public static zzrw zza(byte[] bArr, int i, int i2) {
        return new zzrw(bArr, i, i2);
    }

    public static int zzlB(int i) {
        return (-(i & 1)) ^ (i >>> 1);
    }

    public int getPosition() {
        return this.zzbid - this.zzbia;
    }

    public byte[] readBytes() throws IOException {
        int iZzFv = zzFv();
        if (iZzFv > this.zzbib - this.zzbid || iZzFv <= 0) {
            return iZzFv == 0 ? zzsh.zzbiE : zzlF(iZzFv);
        }
        byte[] bArr = new byte[iZzFv];
        System.arraycopy(this.buffer, this.zzbid, bArr, 0, iZzFv);
        this.zzbid += iZzFv;
        return bArr;
    }

    public double readDouble() throws IOException {
        return Double.longBitsToDouble(zzFy());
    }

    public float readFloat() throws IOException {
        return Float.intBitsToFloat(zzFx());
    }

    public String readString() throws IOException {
        int iZzFv = zzFv();
        if (iZzFv > this.zzbib - this.zzbid || iZzFv <= 0) {
            return new String(zzlF(iZzFv), "UTF-8");
        }
        String str = new String(this.buffer, this.zzbid, iZzFv, "UTF-8");
        this.zzbid += iZzFv;
        return str;
    }

    public int zzFA() {
        if (this.zzbif == Integer.MAX_VALUE) {
            return -1;
        }
        return this.zzbif - this.zzbid;
    }

    public boolean zzFB() {
        return this.zzbid == this.zzbib;
    }

    public byte zzFC() throws IOException {
        if (this.zzbid == this.zzbib) {
            throw zzsd.zzFJ();
        }
        byte[] bArr = this.buffer;
        int i = this.zzbid;
        this.zzbid = i + 1;
        return bArr[i];
    }

    public int zzFo() throws IOException {
        if (zzFB()) {
            this.zzbie = 0;
            return 0;
        }
        this.zzbie = zzFv();
        if (this.zzbie == 0) {
            throw zzsd.zzFM();
        }
        return this.zzbie;
    }

    public void zzFp() throws IOException {
        int iZzFo;
        do {
            iZzFo = zzFo();
            if (iZzFo == 0) {
                return;
            }
        } while (zzlA(iZzFo));
    }

    public long zzFq() throws IOException {
        return zzFw();
    }

    public int zzFr() throws IOException {
        return zzFv();
    }

    public boolean zzFs() throws IOException {
        return zzFv() != 0;
    }

    public int zzFt() throws IOException {
        return zzlB(zzFv());
    }

    public long zzFu() throws IOException {
        return zzX(zzFw());
    }

    public int zzFv() throws IOException {
        int i;
        byte bZzFC = zzFC();
        if (bZzFC >= 0) {
            return bZzFC;
        }
        int i2 = bZzFC & ByteCompanionObject.MAX_VALUE;
        byte bZzFC2 = zzFC();
        if (bZzFC2 >= 0) {
            i = bZzFC2 << 7;
        } else {
            i2 |= (bZzFC2 & ByteCompanionObject.MAX_VALUE) << 7;
            byte bZzFC3 = zzFC();
            if (bZzFC3 >= 0) {
                i = bZzFC3 << 14;
            } else {
                i2 |= (bZzFC3 & ByteCompanionObject.MAX_VALUE) << 14;
                byte bZzFC4 = zzFC();
                if (bZzFC4 < 0) {
                    int i3 = i2 | ((bZzFC4 & ByteCompanionObject.MAX_VALUE) << 21);
                    byte bZzFC5 = zzFC();
                    int i4 = i3 | (bZzFC5 << 28);
                    if (bZzFC5 >= 0) {
                        return i4;
                    }
                    for (int i5 = 0; i5 < 5; i5++) {
                        if (zzFC() >= 0) {
                            return i4;
                        }
                    }
                    throw zzsd.zzFL();
                }
                i = bZzFC4 << 21;
            }
        }
        return i | i2;
    }

    public long zzFw() throws IOException {
        long j = 0;
        for (int i = 0; i < 64; i += 7) {
            j |= (r3 & ByteCompanionObject.MAX_VALUE) << i;
            if ((zzFC() & ByteCompanionObject.MIN_VALUE) == 0) {
                return j;
            }
        }
        throw zzsd.zzFL();
    }

    public int zzFx() throws IOException {
        byte bZzFC = zzFC();
        byte bZzFC2 = zzFC();
        byte bZzFC3 = zzFC();
        return ((zzFC() & 255) << 24) | (bZzFC & 255) | ((bZzFC2 & 255) << 8) | ((bZzFC3 & 255) << 16);
    }

    public long zzFy() throws IOException {
        return ((zzFC() & 255) << 8) | (zzFC() & 255) | ((zzFC() & 255) << 16) | ((zzFC() & 255) << 24) | ((zzFC() & 255) << 32) | ((zzFC() & 255) << 40) | ((zzFC() & 255) << 48) | ((zzFC() & 255) << 56);
    }

    public void zza(zzse zzseVar) throws IOException {
        int iZzFv = zzFv();
        if (this.zzbig >= this.zzbih) {
            throw zzsd.zzFP();
        }
        int iZzlC = zzlC(iZzFv);
        this.zzbig++;
        zzseVar.zzb(this);
        zzlz(0);
        this.zzbig--;
        zzlD(iZzlC);
    }

    public void zza(zzse zzseVar, int i) throws IOException {
        if (this.zzbig >= this.zzbih) {
            throw zzsd.zzFP();
        }
        this.zzbig++;
        zzseVar.zzb(this);
        zzlz(zzsh.zzD(i, 4));
        this.zzbig--;
    }

    public boolean zzlA(int i) throws IOException {
        switch (zzsh.zzlU(i)) {
            case 0:
                zzFr();
                return true;
            case 1:
                zzFy();
                return true;
            case 2:
                zzlG(zzFv());
                return true;
            case 3:
                zzFp();
                zzlz(zzsh.zzD(zzsh.zzlV(i), 4));
                return true;
            case 4:
                return false;
            case 5:
                zzFx();
                return true;
            default:
                throw zzsd.zzFO();
        }
    }

    public int zzlC(int i) throws zzsd {
        if (i < 0) {
            throw zzsd.zzFK();
        }
        int i2 = i + this.zzbid;
        int i3 = this.zzbif;
        if (i2 > i3) {
            throw zzsd.zzFJ();
        }
        this.zzbif = i2;
        zzFz();
        return i3;
    }

    public void zzlD(int i) {
        this.zzbif = i;
        zzFz();
    }

    public void zzlE(int i) {
        if (i > this.zzbid - this.zzbia) {
            throw new IllegalArgumentException("Position " + i + " is beyond current " + (this.zzbid - this.zzbia));
        }
        if (i >= 0) {
            this.zzbid = this.zzbia + i;
            return;
        }
        throw new IllegalArgumentException("Bad position " + i);
    }

    public byte[] zzlF(int i) throws IOException {
        if (i < 0) {
            throw zzsd.zzFK();
        }
        if (this.zzbid + i > this.zzbif) {
            zzlG(this.zzbif - this.zzbid);
            throw zzsd.zzFJ();
        }
        if (i > this.zzbib - this.zzbid) {
            throw zzsd.zzFJ();
        }
        byte[] bArr = new byte[i];
        System.arraycopy(this.buffer, this.zzbid, bArr, 0, i);
        this.zzbid += i;
        return bArr;
    }

    public void zzlG(int i) throws IOException {
        if (i < 0) {
            throw zzsd.zzFK();
        }
        if (this.zzbid + i > this.zzbif) {
            zzlG(this.zzbif - this.zzbid);
            throw zzsd.zzFJ();
        }
        if (i > this.zzbib - this.zzbid) {
            throw zzsd.zzFJ();
        }
        this.zzbid += i;
    }

    public void zzlz(int i) throws zzsd {
        if (this.zzbie != i) {
            throw zzsd.zzFN();
        }
    }

    public byte[] zzx(int i, int i2) {
        if (i2 == 0) {
            return zzsh.zzbiE;
        }
        byte[] bArr = new byte[i2];
        System.arraycopy(this.buffer, this.zzbia + i, bArr, 0, i2);
        return bArr;
    }
}
