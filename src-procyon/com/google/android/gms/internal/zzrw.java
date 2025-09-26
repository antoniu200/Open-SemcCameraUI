// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import java.io.IOException;

public final class zzrw
{
    private final byte[] buffer;
    private int zzbia;
    private int zzbib;
    private int zzbic;
    private int zzbid;
    private int zzbie;
    private int zzbif;
    private int zzbig;
    private int zzbih;
    private int zzbii;
    
    private zzrw(final byte[] buffer, final int n, final int n2) {
        this.zzbif = Integer.MAX_VALUE;
        this.zzbih = 64;
        this.zzbii = 67108864;
        this.buffer = buffer;
        this.zzbia = n;
        this.zzbib = n2 + n;
        this.zzbid = n;
    }
    
    public static zzrw zzB(final byte[] array) {
        return zza(array, 0, array.length);
    }
    
    private void zzFz() {
        this.zzbib += this.zzbic;
        final int zzbib = this.zzbib;
        if (zzbib > this.zzbif) {
            this.zzbic = zzbib - this.zzbif;
            this.zzbib -= this.zzbic;
            return;
        }
        this.zzbic = 0;
    }
    
    public static long zzX(final long n) {
        return -(n & 0x1L) ^ n >>> 1;
    }
    
    public static zzrw zza(final byte[] array, final int n, final int n2) {
        return new zzrw(array, n, n2);
    }
    
    public static int zzlB(final int n) {
        return -(n & 0x1) ^ n >>> 1;
    }
    
    public int getPosition() {
        return this.zzbid - this.zzbia;
    }
    
    public byte[] readBytes() throws IOException {
        final int zzFv = this.zzFv();
        if (zzFv <= this.zzbib - this.zzbid && zzFv > 0) {
            final byte[] array = new byte[zzFv];
            System.arraycopy(this.buffer, this.zzbid, array, 0, zzFv);
            this.zzbid += zzFv;
            return array;
        }
        if (zzFv == 0) {
            return zzsh.zzbiE;
        }
        return this.zzlF(zzFv);
    }
    
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(this.zzFy());
    }
    
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(this.zzFx());
    }
    
    public String readString() throws IOException {
        final int zzFv = this.zzFv();
        if (zzFv <= this.zzbib - this.zzbid && zzFv > 0) {
            final String s = new String(this.buffer, this.zzbid, zzFv, "UTF-8");
            this.zzbid += zzFv;
            return s;
        }
        return new String(this.zzlF(zzFv), "UTF-8");
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
        return this.buffer[this.zzbid++];
    }
    
    public int zzFo() throws IOException {
        if (this.zzFB()) {
            return this.zzbie = 0;
        }
        this.zzbie = this.zzFv();
        if (this.zzbie == 0) {
            throw zzsd.zzFM();
        }
        return this.zzbie;
    }
    
    public void zzFp() throws IOException {
        int zzFo;
        do {
            zzFo = this.zzFo();
        } while (zzFo != 0 && this.zzlA(zzFo));
    }
    
    public long zzFq() throws IOException {
        return this.zzFw();
    }
    
    public int zzFr() throws IOException {
        return this.zzFv();
    }
    
    public boolean zzFs() throws IOException {
        return this.zzFv() != 0;
    }
    
    public int zzFt() throws IOException {
        return zzlB(this.zzFv());
    }
    
    public long zzFu() throws IOException {
        return zzX(this.zzFw());
    }
    
    public int zzFv() throws IOException {
        final byte zzFC = this.zzFC();
        if (zzFC >= 0) {
            return zzFC;
        }
        int n = zzFC & 0x7F;
        final byte zzFC2 = this.zzFC();
        int n2;
        if (zzFC2 >= 0) {
            n2 = zzFC2 << 7;
        }
        else {
            n |= (zzFC2 & 0x7F) << 7;
            final byte zzFC3 = this.zzFC();
            if (zzFC3 >= 0) {
                n2 = zzFC3 << 14;
            }
            else {
                n |= (zzFC3 & 0x7F) << 14;
                final byte zzFC4 = this.zzFC();
                if (zzFC4 >= 0) {
                    n2 = zzFC4 << 21;
                }
                else {
                    final byte zzFC5 = this.zzFC();
                    final int n3 = n | (zzFC4 & 0x7F) << 21 | zzFC5 << 28;
                    if (zzFC5 < 0) {
                        for (int i = 0; i < 5; ++i) {
                            if (this.zzFC() >= 0) {
                                return n3;
                            }
                        }
                        throw zzsd.zzFL();
                    }
                    return n3;
                }
            }
        }
        return n2 | n;
    }
    
    public long zzFw() throws IOException {
        int i = 0;
        long n = 0L;
        while (i < 64) {
            final byte zzFC = this.zzFC();
            n |= (long)(zzFC & 0x7F) << i;
            if ((zzFC & 0x80) == 0x0) {
                return n;
            }
            i += 7;
        }
        throw zzsd.zzFL();
    }
    
    public int zzFx() throws IOException {
        return (this.zzFC() & 0xFF) << 24 | ((this.zzFC() & 0xFF) | (this.zzFC() & 0xFF) << 8 | (this.zzFC() & 0xFF) << 16);
    }
    
    public long zzFy() throws IOException {
        return ((long)this.zzFC() & 0xFFL) << 8 | ((long)this.zzFC() & 0xFFL) | ((long)this.zzFC() & 0xFFL) << 16 | ((long)this.zzFC() & 0xFFL) << 24 | ((long)this.zzFC() & 0xFFL) << 32 | ((long)this.zzFC() & 0xFFL) << 40 | ((long)this.zzFC() & 0xFFL) << 48 | ((long)this.zzFC() & 0xFFL) << 56;
    }
    
    public void zza(final zzse zzse) throws IOException {
        final int zzFv = this.zzFv();
        if (this.zzbig >= this.zzbih) {
            throw zzsd.zzFP();
        }
        final int zzlC = this.zzlC(zzFv);
        ++this.zzbig;
        zzse.zzb(this);
        this.zzlz(0);
        --this.zzbig;
        this.zzlD(zzlC);
    }
    
    public void zza(final zzse zzse, final int n) throws IOException {
        if (this.zzbig >= this.zzbih) {
            throw zzsd.zzFP();
        }
        ++this.zzbig;
        zzse.zzb(this);
        this.zzlz(zzsh.zzD(n, 4));
        --this.zzbig;
    }
    
    public boolean zzlA(final int n) throws IOException {
        switch (zzsh.zzlU(n)) {
            default: {
                throw zzsd.zzFO();
            }
            case 5: {
                this.zzFx();
                return true;
            }
            case 4: {
                return false;
            }
            case 3: {
                this.zzFp();
                this.zzlz(zzsh.zzD(zzsh.zzlV(n), 4));
                return true;
            }
            case 2: {
                this.zzlG(this.zzFv());
                return true;
            }
            case 1: {
                this.zzFy();
                return true;
            }
            case 0: {
                this.zzFr();
                return true;
            }
        }
    }
    
    public int zzlC(int zzbif) throws zzsd {
        if (zzbif < 0) {
            throw zzsd.zzFK();
        }
        zzbif += this.zzbid;
        final int zzbif2 = this.zzbif;
        if (zzbif > zzbif2) {
            throw zzsd.zzFJ();
        }
        this.zzbif = zzbif;
        this.zzFz();
        return zzbif2;
    }
    
    public void zzlD(final int zzbif) {
        this.zzbif = zzbif;
        this.zzFz();
    }
    
    public void zzlE(final int n) {
        if (n > this.zzbid - this.zzbia) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Position ");
            sb.append(n);
            sb.append(" is beyond current ");
            sb.append(this.zzbid - this.zzbia);
            throw new IllegalArgumentException(sb.toString());
        }
        if (n < 0) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Bad position ");
            sb2.append(n);
            throw new IllegalArgumentException(sb2.toString());
        }
        this.zzbid = this.zzbia + n;
    }
    
    public byte[] zzlF(final int n) throws IOException {
        if (n < 0) {
            throw zzsd.zzFK();
        }
        if (this.zzbid + n > this.zzbif) {
            this.zzlG(this.zzbif - this.zzbid);
            throw zzsd.zzFJ();
        }
        if (n <= this.zzbib - this.zzbid) {
            final byte[] array = new byte[n];
            System.arraycopy(this.buffer, this.zzbid, array, 0, n);
            this.zzbid += n;
            return array;
        }
        throw zzsd.zzFJ();
    }
    
    public void zzlG(final int n) throws IOException {
        if (n < 0) {
            throw zzsd.zzFK();
        }
        if (this.zzbid + n > this.zzbif) {
            this.zzlG(this.zzbif - this.zzbid);
            throw zzsd.zzFJ();
        }
        if (n <= this.zzbib - this.zzbid) {
            this.zzbid += n;
            return;
        }
        throw zzsd.zzFJ();
    }
    
    public void zzlz(final int n) throws zzsd {
        if (this.zzbie != n) {
            throw zzsd.zzFN();
        }
    }
    
    public byte[] zzx(final int n, final int n2) {
        if (n2 == 0) {
            return zzsh.zzbiE;
        }
        final byte[] array = new byte[n2];
        System.arraycopy(this.buffer, this.zzbia + n, array, 0, n2);
        return array;
    }
}
