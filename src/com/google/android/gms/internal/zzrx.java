package com.google.android.gms.internal;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ReadOnlyBufferException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class zzrx {
    private final ByteBuffer zzbij;

    public static class zza extends IOException {
        zza(int i, int i2) {
            super("CodedOutputStream was writing to a flat byte array and ran out of space (pos " + i + " limit " + i2 + ").");
        }
    }

    private zzrx(ByteBuffer byteBuffer) {
        this.zzbij = byteBuffer;
        this.zzbij.order(ByteOrder.LITTLE_ENDIAN);
    }

    private zzrx(byte[] bArr, int i, int i2) {
        this(ByteBuffer.wrap(bArr, i, i2));
    }

    public static int zzA(int i, int i2) {
        return zzlM(i) + zzlJ(i2);
    }

    public static int zzB(int i, int i2) {
        return zzlM(i) + zzlK(i2);
    }

    public static zzrx zzC(byte[] bArr) {
        return zzb(bArr, 0, bArr.length);
    }

    public static int zzE(byte[] bArr) {
        return zzlO(bArr.length) + bArr.length;
    }

    private static int zza(CharSequence charSequence, int i) {
        int length = charSequence.length();
        int i2 = 0;
        while (i < length) {
            char cCharAt = charSequence.charAt(i);
            if (cCharAt < 2048) {
                i2 += (127 - cCharAt) >>> 31;
            } else {
                i2 += 2;
                if (55296 <= cCharAt && cCharAt <= 57343) {
                    if (Character.codePointAt(charSequence, i) < 65536) {
                        throw new IllegalArgumentException("Unpaired surrogate at index " + i);
                    }
                    i++;
                }
            }
            i++;
        }
        return i2;
    }

    private static int zza(CharSequence charSequence, byte[] bArr, int i, int i2) {
        int i3;
        int i4;
        int i5;
        char cCharAt;
        int length = charSequence.length();
        int i6 = i2 + i;
        int i7 = 0;
        while (i7 < length && (i5 = i7 + i) < i6 && (cCharAt = charSequence.charAt(i7)) < 128) {
            bArr[i5] = (byte) cCharAt;
            i7++;
        }
        if (i7 == length) {
            return i + length;
        }
        int i8 = i + i7;
        while (i7 < length) {
            char cCharAt2 = charSequence.charAt(i7);
            if (cCharAt2 >= 128 || i8 >= i6) {
                if (cCharAt2 < 2048 && i8 <= i6 - 2) {
                    int i9 = i8 + 1;
                    bArr[i8] = (byte) (960 | (cCharAt2 >>> 6));
                    i8 = i9 + 1;
                    bArr[i9] = (byte) ((cCharAt2 & '?') | 128);
                } else {
                    if ((cCharAt2 >= 55296 && 57343 >= cCharAt2) || i8 > i6 - 3) {
                        if (i8 > i6 - 4) {
                            if (55296 <= cCharAt2 && cCharAt2 <= 57343 && ((i4 = i7 + 1) == charSequence.length() || !Character.isSurrogatePair(cCharAt2, charSequence.charAt(i4)))) {
                                throw new IllegalArgumentException("Unpaired surrogate at index " + i7);
                            }
                            throw new ArrayIndexOutOfBoundsException("Failed writing " + cCharAt2 + " at index " + i8);
                        }
                        int i10 = i7 + 1;
                        if (i10 != charSequence.length()) {
                            char cCharAt3 = charSequence.charAt(i10);
                            if (Character.isSurrogatePair(cCharAt2, cCharAt3)) {
                                int codePoint = Character.toCodePoint(cCharAt2, cCharAt3);
                                int i11 = i8 + 1;
                                bArr[i8] = (byte) (240 | (codePoint >>> 18));
                                int i12 = i11 + 1;
                                bArr[i11] = (byte) (((codePoint >>> 12) & 63) | 128);
                                int i13 = i12 + 1;
                                bArr[i12] = (byte) (((codePoint >>> 6) & 63) | 128);
                                i8 = i13 + 1;
                                bArr[i13] = (byte) ((codePoint & 63) | 128);
                                i7 = i10;
                            } else {
                                i7 = i10;
                            }
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append("Unpaired surrogate at index ");
                        sb.append(i7 - 1);
                        throw new IllegalArgumentException(sb.toString());
                    }
                    int i14 = i8 + 1;
                    bArr[i8] = (byte) (480 | (cCharAt2 >>> '\f'));
                    int i15 = i14 + 1;
                    bArr[i14] = (byte) (((cCharAt2 >>> 6) & 63) | 128);
                    i3 = i15 + 1;
                    bArr[i15] = (byte) ((cCharAt2 & '?') | 128);
                }
                i7++;
            } else {
                i3 = i8 + 1;
                bArr[i8] = (byte) cCharAt2;
            }
            i8 = i3;
            i7++;
        }
        return i8;
    }

    private static void zza(CharSequence charSequence, ByteBuffer byteBuffer) {
        if (byteBuffer.isReadOnly()) {
            throw new ReadOnlyBufferException();
        }
        if (!byteBuffer.hasArray()) {
            zzb(charSequence, byteBuffer);
            return;
        }
        try {
            byteBuffer.position(zza(charSequence, byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), byteBuffer.remaining()) - byteBuffer.arrayOffset());
        } catch (ArrayIndexOutOfBoundsException e) {
            BufferOverflowException bufferOverflowException = new BufferOverflowException();
            bufferOverflowException.initCause(e);
            throw bufferOverflowException;
        }
    }

    public static int zzaa(long j) {
        return zzad(j);
    }

    public static int zzab(long j) {
        return zzad(zzaf(j));
    }

    public static int zzad(long j) {
        if (((-128) & j) == 0) {
            return 1;
        }
        if (((-16384) & j) == 0) {
            return 2;
        }
        if (((-2097152) & j) == 0) {
            return 3;
        }
        if (((-268435456) & j) == 0) {
            return 4;
        }
        if (((-34359738368L) & j) == 0) {
            return 5;
        }
        if (((-4398046511104L) & j) == 0) {
            return 6;
        }
        if (((-562949953421312L) & j) == 0) {
            return 7;
        }
        if (((-72057594037927936L) & j) == 0) {
            return 8;
        }
        return (j & Long.MIN_VALUE) == 0 ? 9 : 10;
    }

    public static long zzaf(long j) {
        return (j >> 63) ^ (j << 1);
    }

    public static int zzav(boolean z) {
        return 1;
    }

    public static int zzb(int i, double d) {
        return zzlM(i) + zzk(d);
    }

    public static int zzb(int i, zzse zzseVar) {
        return (zzlM(i) * 2) + zzd(zzseVar);
    }

    public static int zzb(int i, byte[] bArr) {
        return zzlM(i) + zzE(bArr);
    }

    public static zzrx zzb(byte[] bArr, int i, int i2) {
        return new zzrx(bArr, i, i2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v13 */
    private static void zzb(CharSequence charSequence, ByteBuffer byteBuffer) {
        int i;
        int length = charSequence.length();
        int i2 = 0;
        while (i2 < length) {
            char cCharAt = charSequence.charAt(i2);
            char c = cCharAt;
            if (cCharAt >= 128) {
                if (cCharAt < 2048) {
                    i = 960 | (cCharAt >>> 6);
                } else {
                    if (cCharAt >= 55296 && 57343 >= cCharAt) {
                        int i3 = i2 + 1;
                        if (i3 != charSequence.length()) {
                            char cCharAt2 = charSequence.charAt(i3);
                            if (Character.isSurrogatePair(cCharAt, cCharAt2)) {
                                int codePoint = Character.toCodePoint(cCharAt, cCharAt2);
                                byteBuffer.put((byte) (240 | (codePoint >>> 18)));
                                byteBuffer.put((byte) (((codePoint >>> 12) & 63) | 128));
                                byteBuffer.put((byte) (((codePoint >>> 6) & 63) | 128));
                                byteBuffer.put((byte) ((codePoint & 63) | 128));
                                i2 = i3;
                            } else {
                                i2 = i3;
                            }
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append("Unpaired surrogate at index ");
                        sb.append(i2 - 1);
                        throw new IllegalArgumentException(sb.toString());
                    }
                    byteBuffer.put((byte) (480 | (cCharAt >>> '\f')));
                    i = ((cCharAt >>> 6) & 63) | 128;
                }
                byteBuffer.put((byte) i);
                c = (cCharAt & '?') | 128;
                byteBuffer.put((byte) c);
            } else {
                byteBuffer.put((byte) c);
            }
            i2++;
        }
    }

    public static int zzc(int i, float f) {
        return zzlM(i) + zzj(f);
    }

    public static int zzc(int i, zzse zzseVar) {
        return zzlM(i) + zze(zzseVar);
    }

    public static int zzc(int i, boolean z) {
        return zzlM(i) + zzav(z);
    }

    private static int zzc(CharSequence charSequence) {
        int length = charSequence.length();
        int i = 0;
        while (i < length && charSequence.charAt(i) < 128) {
            i++;
        }
        int iZza = length;
        while (true) {
            if (i < length) {
                char cCharAt = charSequence.charAt(i);
                if (cCharAt >= 2048) {
                    iZza += zza(charSequence, i);
                    break;
                }
                iZza += (127 - cCharAt) >>> 31;
                i++;
            } else {
                break;
            }
        }
        if (iZza >= length) {
            return iZza;
        }
        throw new IllegalArgumentException("UTF-8 length does not fit in int: " + (iZza + 4294967296L));
    }

    public static int zzd(int i, long j) {
        return zzlM(i) + zzaa(j);
    }

    public static int zzd(zzse zzseVar) {
        return zzseVar.zzFR();
    }

    public static int zze(int i, long j) {
        return zzlM(i) + zzab(j);
    }

    public static int zze(zzse zzseVar) {
        int iZzFR = zzseVar.zzFR();
        return zzlO(iZzFR) + iZzFR;
    }

    public static int zzfA(String str) {
        int iZzc = zzc(str);
        return zzlO(iZzc) + iZzc;
    }

    public static int zzj(float f) {
        return 4;
    }

    public static int zzk(double d) {
        return 8;
    }

    public static int zzlJ(int i) {
        if (i >= 0) {
            return zzlO(i);
        }
        return 10;
    }

    public static int zzlK(int i) {
        return zzlO(zzlQ(i));
    }

    public static int zzlM(int i) {
        return zzlO(zzsh.zzD(i, 0));
    }

    public static int zzlO(int i) {
        if ((i & (-128)) == 0) {
            return 1;
        }
        if ((i & (-16384)) == 0) {
            return 2;
        }
        if (((-2097152) & i) == 0) {
            return 3;
        }
        return (i & (-268435456)) == 0 ? 4 : 5;
    }

    public static int zzlQ(int i) {
        return (i >> 31) ^ (i << 1);
    }

    public static int zzn(int i, String str) {
        return zzlM(i) + zzfA(str);
    }

    public void zzC(int i, int i2) throws IOException {
        zzlN(zzsh.zzD(i, i2));
    }

    public void zzD(byte[] bArr) throws IOException {
        zzlN(bArr.length);
        zzF(bArr);
    }

    public void zzF(byte[] bArr) throws IOException {
        zzc(bArr, 0, bArr.length);
    }

    public int zzFD() {
        return this.zzbij.remaining();
    }

    public void zzFE() {
        if (zzFD() != 0) {
            throw new IllegalStateException("Did not write as much data as expected.");
        }
    }

    public void zzY(long j) throws IOException {
        zzac(j);
    }

    public void zzZ(long j) throws IOException {
        zzac(zzaf(j));
    }

    public void zza(int i, double d) throws IOException {
        zzC(i, 1);
        zzj(d);
    }

    public void zza(int i, zzse zzseVar) throws IOException {
        zzC(i, 2);
        zzc(zzseVar);
    }

    public void zza(int i, byte[] bArr) throws IOException {
        zzC(i, 2);
        zzD(bArr);
    }

    public void zzac(long j) throws IOException {
        while (((-128) & j) != 0) {
            zzlL((((int) j) & 127) | 128);
            j >>>= 7;
        }
        zzlL((int) j);
    }

    public void zzae(long j) throws IOException {
        if (this.zzbij.remaining() < 8) {
            throw new zza(this.zzbij.position(), this.zzbij.limit());
        }
        this.zzbij.putLong(j);
    }

    public void zzau(boolean z) throws IOException {
        zzlL(z ? 1 : 0);
    }

    public void zzb(byte b) throws IOException {
        if (!this.zzbij.hasRemaining()) {
            throw new zza(this.zzbij.position(), this.zzbij.limit());
        }
        this.zzbij.put(b);
    }

    public void zzb(int i, float f) throws IOException {
        zzC(i, 5);
        zzi(f);
    }

    public void zzb(int i, long j) throws IOException {
        zzC(i, 0);
        zzY(j);
    }

    public void zzb(int i, String str) throws IOException {
        zzC(i, 2);
        zzfz(str);
    }

    public void zzb(int i, boolean z) throws IOException {
        zzC(i, 0);
        zzau(z);
    }

    public void zzb(zzse zzseVar) throws IOException {
        zzseVar.zza(this);
    }

    public void zzc(int i, long j) throws IOException {
        zzC(i, 0);
        zzZ(j);
    }

    public void zzc(zzse zzseVar) throws IOException {
        zzlN(zzseVar.zzFQ());
        zzseVar.zza(this);
    }

    public void zzc(byte[] bArr, int i, int i2) throws IOException {
        if (this.zzbij.remaining() < i2) {
            throw new zza(this.zzbij.position(), this.zzbij.limit());
        }
        this.zzbij.put(bArr, i, i2);
    }

    public void zzfz(String str) throws IOException {
        try {
            int iZzlO = zzlO(str.length());
            if (iZzlO != zzlO(str.length() * 3)) {
                zzlN(zzc(str));
                zza(str, this.zzbij);
                return;
            }
            int iPosition = this.zzbij.position();
            if (this.zzbij.remaining() < iZzlO) {
                throw new zza(iPosition + iZzlO, this.zzbij.limit());
            }
            this.zzbij.position(iPosition + iZzlO);
            zza(str, this.zzbij);
            int iPosition2 = this.zzbij.position();
            this.zzbij.position(iPosition);
            zzlN((iPosition2 - iPosition) - iZzlO);
            this.zzbij.position(iPosition2);
        } catch (BufferOverflowException e) {
            zza zzaVar = new zza(this.zzbij.position(), this.zzbij.limit());
            zzaVar.initCause(e);
            throw zzaVar;
        }
    }

    public void zzi(float f) throws IOException {
        zzlP(Float.floatToIntBits(f));
    }

    public void zzj(double d) throws IOException {
        zzae(Double.doubleToLongBits(d));
    }

    public void zzlH(int i) throws IOException {
        if (i >= 0) {
            zzlN(i);
        } else {
            zzac(i);
        }
    }

    public void zzlI(int i) throws IOException {
        zzlN(zzlQ(i));
    }

    public void zzlL(int i) throws IOException {
        zzb((byte) i);
    }

    public void zzlN(int i) throws IOException {
        while ((i & (-128)) != 0) {
            zzlL((i & 127) | 128);
            i >>>= 7;
        }
        zzlL(i);
    }

    public void zzlP(int i) throws IOException {
        if (this.zzbij.remaining() < 4) {
            throw new zza(this.zzbij.position(), this.zzbij.limit());
        }
        this.zzbij.putInt(i);
    }

    public void zzy(int i, int i2) throws IOException {
        zzC(i, 0);
        zzlH(i2);
    }

    public void zzz(int i, int i2) throws IOException {
        zzC(i, 0);
        zzlI(i2);
    }
}
