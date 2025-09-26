// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ReadOnlyBufferException;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;

public final class zzrx
{
    private final ByteBuffer zzbij;
    
    private zzrx(final ByteBuffer zzbij) {
        (this.zzbij = zzbij).order(ByteOrder.LITTLE_ENDIAN);
    }
    
    private zzrx(final byte[] array, final int offset, final int length) {
        this(ByteBuffer.wrap(array, offset, length));
    }
    
    public static int zzA(final int n, final int n2) {
        return zzlM(n) + zzlJ(n2);
    }
    
    public static int zzB(final int n, final int n2) {
        return zzlM(n) + zzlK(n2);
    }
    
    public static zzrx zzC(final byte[] array) {
        return zzb(array, 0, array.length);
    }
    
    public static int zzE(final byte[] array) {
        return zzlO(array.length) + array.length;
    }
    
    private static int zza(final CharSequence seq, int i) {
        final int length = seq.length();
        int n = 0;
        while (i < length) {
            final char char1 = seq.charAt(i);
            int n2;
            if (char1 < '\u0800') {
                n += '\u007f' - char1 >>> 31;
                n2 = i;
            }
            else {
                final int n3 = n += 2;
                n2 = i;
                if ('\ud800' <= char1) {
                    n = n3;
                    n2 = i;
                    if (char1 <= '\udfff') {
                        if (Character.codePointAt(seq, i) < 65536) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Unpaired surrogate at index ");
                            sb.append(i);
                            throw new IllegalArgumentException(sb.toString());
                        }
                        n2 = i + 1;
                        n = n3;
                    }
                }
            }
            i = n2 + 1;
        }
        return n;
    }
    
    private static int zza(final CharSequence charSequence, final byte[] array, int i, int j) {
        final int length = charSequence.length();
        final int n = j + i;
        int n2;
        char char1;
        for (j = 0; j < length; ++j) {
            n2 = j + i;
            if (n2 >= n) {
                break;
            }
            char1 = charSequence.charAt(j);
            if (char1 >= '\u0080') {
                break;
            }
            array[n2] = (byte)char1;
        }
        if (j == length) {
            return i + length;
        }
        int k = i + j;
        char char2;
        int n3;
        int n4;
        char char3;
        int n5;
        int n6;
        int n7;
        int n8;
        StringBuilder sb;
        StringBuilder sb2;
        StringBuilder sb3;
        for (i = j; i < length; ++i, k = j) {
            char2 = charSequence.charAt(i);
            if (char2 < '\u0080' && k < n) {
                j = k + 1;
                array[k] = (byte)char2;
            }
            else if (char2 < '\u0800' && k <= n - 2) {
                n3 = k + 1;
                array[k] = (byte)(0x3C0 | char2 >>> 6);
                j = n3 + 1;
                array[n3] = (byte)((char2 & '?') | 0x80);
            }
            else if ((char2 < '\ud800' || '\udfff' < char2) && k <= n - 3) {
                j = k + 1;
                array[k] = (byte)(0x1E0 | char2 >>> 12);
                n4 = j + 1;
                array[j] = (byte)((char2 >>> 6 & 0x3F) | 0x80);
                j = n4 + 1;
                array[n4] = (byte)((char2 & '?') | 0x80);
            }
            else {
                if (k <= n - 4) {
                    j = i + 1;
                    if (j != charSequence.length()) {
                        char3 = charSequence.charAt(j);
                        if (Character.isSurrogatePair(char2, char3)) {
                            i = Character.toCodePoint(char2, char3);
                            n5 = k + 1;
                            array[k] = (byte)(0xF0 | i >>> 18);
                            n6 = n5 + 1;
                            array[n5] = (byte)((i >>> 12 & 0x3F) | 0x80);
                            n7 = n6 + 1;
                            array[n6] = (byte)((i >>> 6 & 0x3F) | 0x80);
                            n8 = n7 + 1;
                            array[n7] = (byte)((i & 0x3F) | 0x80);
                            i = j;
                            j = n8;
                            continue;
                        }
                        i = j;
                    }
                    sb = new StringBuilder();
                    sb.append("Unpaired surrogate at index ");
                    sb.append(i - 1);
                    throw new IllegalArgumentException(sb.toString());
                }
                if ('\ud800' <= char2 && char2 <= '\udfff') {
                    j = i + 1;
                    if (j == charSequence.length() || !Character.isSurrogatePair(char2, charSequence.charAt(j))) {
                        sb2 = new StringBuilder();
                        sb2.append("Unpaired surrogate at index ");
                        sb2.append(i);
                        throw new IllegalArgumentException(sb2.toString());
                    }
                }
                sb3 = new StringBuilder();
                sb3.append("Failed writing ");
                sb3.append(char2);
                sb3.append(" at index ");
                sb3.append(k);
                throw new ArrayIndexOutOfBoundsException(sb3.toString());
            }
        }
        return k;
    }
    
    private static void zza(final CharSequence charSequence, final ByteBuffer byteBuffer) {
        if (byteBuffer.isReadOnly()) {
            throw new ReadOnlyBufferException();
        }
        if (byteBuffer.hasArray()) {
            try {
                byteBuffer.position();
                return;
            }
            catch (final ArrayIndexOutOfBoundsException cause) {
                final BufferOverflowException ex = new BufferOverflowException();
                ex.initCause(cause);
                throw ex;
            }
        }
        zzb(charSequence, byteBuffer);
    }
    
    public static int zzaa(final long n) {
        return zzad(n);
    }
    
    public static int zzab(final long n) {
        return zzad(zzaf(n));
    }
    
    public static int zzad(final long n) {
        if ((0xFFFFFFFFFFFFFF80L & n) == 0x0L) {
            return 1;
        }
        if ((0xFFFFFFFFFFFFC000L & n) == 0x0L) {
            return 2;
        }
        if ((0xFFFFFFFFFFE00000L & n) == 0x0L) {
            return 3;
        }
        if ((0xFFFFFFFFF0000000L & n) == 0x0L) {
            return 4;
        }
        if ((0xFFFFFFF800000000L & n) == 0x0L) {
            return 5;
        }
        if ((0xFFFFFC0000000000L & n) == 0x0L) {
            return 6;
        }
        if ((0xFFFE000000000000L & n) == 0x0L) {
            return 7;
        }
        if ((0xFF00000000000000L & n) == 0x0L) {
            return 8;
        }
        if ((n & Long.MIN_VALUE) == 0x0L) {
            return 9;
        }
        return 10;
    }
    
    public static long zzaf(final long n) {
        return n >> 63 ^ n << 1;
    }
    
    public static int zzav(final boolean b) {
        return 1;
    }
    
    public static int zzb(final int n, final double n2) {
        return zzlM(n) + zzk(n2);
    }
    
    public static int zzb(final int n, final zzse zzse) {
        return zzlM(n) * 2 + zzd(zzse);
    }
    
    public static int zzb(final int n, final byte[] array) {
        return zzlM(n) + zzE(array);
    }
    
    public static zzrx zzb(final byte[] array, final int n, final int n2) {
        return new zzrx(array, n, n2);
    }
    
    private static void zzb(final CharSequence charSequence, final ByteBuffer byteBuffer) {
        for (int length = charSequence.length(), i = 0; i < length; ++i) {
            final char char1 = charSequence.charAt(i);
            int n;
            if (char1 < '\u0080') {
                n = char1;
            }
            else {
                int n2;
                if (char1 < '\u0800') {
                    n2 = (0x3C0 | char1 >>> 6);
                }
                else {
                    if (char1 >= '\ud800' && '\udfff' >= char1) {
                        final int n3 = i + 1;
                        if (n3 != charSequence.length()) {
                            final char char2 = charSequence.charAt(n3);
                            if (Character.isSurrogatePair(char1, char2)) {
                                final int codePoint = Character.toCodePoint(char1, char2);
                                byteBuffer.put((byte)(0xF0 | codePoint >>> 18));
                                byteBuffer.put((byte)((codePoint >>> 12 & 0x3F) | 0x80));
                                byteBuffer.put((byte)((codePoint >>> 6 & 0x3F) | 0x80));
                                byteBuffer.put((byte)((codePoint & 0x3F) | 0x80));
                                i = n3;
                                continue;
                            }
                            i = n3;
                        }
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Unpaired surrogate at index ");
                        sb.append(i - 1);
                        throw new IllegalArgumentException(sb.toString());
                    }
                    byteBuffer.put((byte)(0x1E0 | char1 >>> 12));
                    n2 = ((char1 >>> 6 & 0x3F) | 0x80);
                }
                byteBuffer.put((byte)n2);
                n = ((char1 & '?') | 0x80);
            }
            byteBuffer.put((byte)n);
        }
    }
    
    public static int zzc(final int n, final float n2) {
        return zzlM(n) + zzj(n2);
    }
    
    public static int zzc(final int n, final zzse zzse) {
        return zzlM(n) + zze(zzse);
    }
    
    public static int zzc(final int n, final boolean b) {
        return zzlM(n) + zzav(b);
    }
    
    private static int zzc(final CharSequence charSequence) {
        int length;
        int n;
        for (length = charSequence.length(), n = 0; n < length && charSequence.charAt(n) < '\u0080'; ++n) {}
        int n2 = length;
        int n3;
        while (true) {
            n3 = n2;
            if (n >= length) {
                break;
            }
            final char char1 = charSequence.charAt(n);
            if (char1 >= '\u0800') {
                n3 = n2 + zza(charSequence, n);
                break;
            }
            n2 += '\u007f' - char1 >>> 31;
            ++n;
        }
        if (n3 < length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("UTF-8 length does not fit in int: ");
            sb.append(n3 + 4294967296L);
            throw new IllegalArgumentException(sb.toString());
        }
        return n3;
    }
    
    public static int zzd(final int n, final long n2) {
        return zzlM(n) + zzaa(n2);
    }
    
    public static int zzd(final zzse zzse) {
        return zzse.zzFR();
    }
    
    public static int zze(final int n, final long n2) {
        return zzlM(n) + zzab(n2);
    }
    
    public static int zze(final zzse zzse) {
        final int zzFR = zzse.zzFR();
        return zzlO(zzFR) + zzFR;
    }
    
    public static int zzfA(final String s) {
        final int zzc = zzc(s);
        return zzlO(zzc) + zzc;
    }
    
    public static int zzj(final float n) {
        return 4;
    }
    
    public static int zzk(final double n) {
        return 8;
    }
    
    public static int zzlJ(final int n) {
        if (n >= 0) {
            return zzlO(n);
        }
        return 10;
    }
    
    public static int zzlK(final int n) {
        return zzlO(zzlQ(n));
    }
    
    public static int zzlM(final int n) {
        return zzlO(zzsh.zzD(n, 0));
    }
    
    public static int zzlO(final int n) {
        if ((n & 0xFFFFFF80) == 0x0) {
            return 1;
        }
        if ((n & 0xFFFFC000) == 0x0) {
            return 2;
        }
        if ((0xFFE00000 & n) == 0x0) {
            return 3;
        }
        if ((n & 0xF0000000) == 0x0) {
            return 4;
        }
        return 5;
    }
    
    public static int zzlQ(final int n) {
        return n >> 31 ^ n << 1;
    }
    
    public static int zzn(final int n, final String s) {
        return zzlM(n) + zzfA(s);
    }
    
    public void zzC(final int n, final int n2) throws IOException {
        this.zzlN(zzsh.zzD(n, n2));
    }
    
    public void zzD(final byte[] array) throws IOException {
        this.zzlN(array.length);
        this.zzF(array);
    }
    
    public void zzF(final byte[] array) throws IOException {
        this.zzc(array, 0, array.length);
    }
    
    public int zzFD() {
        return this.zzbij.remaining();
    }
    
    public void zzFE() {
        if (this.zzFD() != 0) {
            throw new IllegalStateException("Did not write as much data as expected.");
        }
    }
    
    public void zzY(final long n) throws IOException {
        this.zzac(n);
    }
    
    public void zzZ(final long n) throws IOException {
        this.zzac(zzaf(n));
    }
    
    public void zza(final int n, final double n2) throws IOException {
        this.zzC(n, 1);
        this.zzj(n2);
    }
    
    public void zza(final int n, final zzse zzse) throws IOException {
        this.zzC(n, 2);
        this.zzc(zzse);
    }
    
    public void zza(final int n, final byte[] array) throws IOException {
        this.zzC(n, 2);
        this.zzD(array);
    }
    
    public void zzac(long n) throws IOException {
        while ((0xFFFFFFFFFFFFFF80L & n) != 0x0L) {
            this.zzlL(((int)n & 0x7F) | 0x80);
            n >>>= 7;
        }
        this.zzlL((int)n);
    }
    
    public void zzae(final long n) throws IOException {
        if (this.zzbij.remaining() < 8) {
            throw new zza(this.zzbij.position(), this.zzbij.limit());
        }
        this.zzbij.putLong(n);
    }
    
    public void zzau(final boolean b) throws IOException {
        this.zzlL(b ? 1 : 0);
    }
    
    public void zzb(final byte b) throws IOException {
        if (!this.zzbij.hasRemaining()) {
            throw new zza(this.zzbij.position(), this.zzbij.limit());
        }
        this.zzbij.put(b);
    }
    
    public void zzb(final int n, final float n2) throws IOException {
        this.zzC(n, 5);
        this.zzi(n2);
    }
    
    public void zzb(final int n, final long n2) throws IOException {
        this.zzC(n, 0);
        this.zzY(n2);
    }
    
    public void zzb(final int n, final String s) throws IOException {
        this.zzC(n, 2);
        this.zzfz(s);
    }
    
    public void zzb(final int n, final boolean b) throws IOException {
        this.zzC(n, 0);
        this.zzau(b);
    }
    
    public void zzb(final zzse zzse) throws IOException {
        zzse.zza(this);
    }
    
    public void zzc(final int n, final long n2) throws IOException {
        this.zzC(n, 0);
        this.zzZ(n2);
    }
    
    public void zzc(final zzse zzse) throws IOException {
        this.zzlN(zzse.zzFQ());
        zzse.zza(this);
    }
    
    public void zzc(final byte[] src, final int offset, final int length) throws IOException {
        if (this.zzbij.remaining() >= length) {
            this.zzbij.put(src, offset, length);
            return;
        }
        throw new zza(this.zzbij.position(), this.zzbij.limit());
    }
    
    public void zzfz(final String s) throws IOException {
        try {
            final int zzlO = zzlO(s.length());
            if (zzlO != zzlO(s.length() * 3)) {
                this.zzlN(zzc(s));
                zza(s, this.zzbij);
                return;
            }
            final int position = this.zzbij.position();
            if (this.zzbij.remaining() < zzlO) {
                throw new zza(position + zzlO, this.zzbij.limit());
            }
            this.zzbij.position();
            zza(s, this.zzbij);
            final int position2 = this.zzbij.position();
            this.zzbij.position();
            this.zzlN(position2 - position - zzlO);
            this.zzbij.position();
        }
        catch (final BufferOverflowException cause) {
            final zza zza = new zza(this.zzbij.position(), this.zzbij.limit());
            zza.initCause(cause);
            throw zza;
        }
    }
    
    public void zzi(final float value) throws IOException {
        this.zzlP(Float.floatToIntBits(value));
    }
    
    public void zzj(final double value) throws IOException {
        this.zzae(Double.doubleToLongBits(value));
    }
    
    public void zzlH(final int n) throws IOException {
        if (n >= 0) {
            this.zzlN(n);
            return;
        }
        this.zzac(n);
    }
    
    public void zzlI(final int n) throws IOException {
        this.zzlN(zzlQ(n));
    }
    
    public void zzlL(final int n) throws IOException {
        this.zzb((byte)n);
    }
    
    public void zzlN(int n) throws IOException {
        while ((n & 0xFFFFFF80) != 0x0) {
            this.zzlL((n & 0x7F) | 0x80);
            n >>>= 7;
        }
        this.zzlL(n);
    }
    
    public void zzlP(final int n) throws IOException {
        if (this.zzbij.remaining() < 4) {
            throw new zza(this.zzbij.position(), this.zzbij.limit());
        }
        this.zzbij.putInt(n);
    }
    
    public void zzy(final int n, final int n2) throws IOException {
        this.zzC(n, 0);
        this.zzlH(n2);
    }
    
    public void zzz(final int n, final int n2) throws IOException {
        this.zzC(n, 0);
        this.zzlI(n2);
    }
    
    public static class zza extends IOException
    {
        zza(final int i, final int j) {
            final StringBuilder sb = new StringBuilder();
            sb.append("CodedOutputStream was writing to a flat byte array and ran out of space (pos ");
            sb.append(i);
            sb.append(" limit ");
            sb.append(j);
            sb.append(").");
            super(sb.toString());
        }
    }
}
