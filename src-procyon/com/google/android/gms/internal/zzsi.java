// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import java.util.Arrays;
import java.io.IOException;

public interface zzsi
{
    public static final class zza extends zzry<zza>
    {
        public String[] zzbiF;
        public String[] zzbiG;
        public int[] zzbiH;
        public long[] zzbiI;
        
        public zza() {
            this.zzFS();
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b = true;
            if (o == this) {
                return true;
            }
            if (!(o instanceof zza)) {
                return false;
            }
            final zza zza = (zza)o;
            if (!zzsc.equals(this.zzbiF, zza.zzbiF)) {
                return false;
            }
            if (!zzsc.equals(this.zzbiG, zza.zzbiG)) {
                return false;
            }
            if (!zzsc.equals(this.zzbiH, zza.zzbiH)) {
                return false;
            }
            if (!zzsc.equals(this.zzbiI, zza.zzbiI)) {
                return false;
            }
            if (this.zzbik != null && !this.zzbik.isEmpty()) {
                return this.zzbik.equals(zza.zzbik);
            }
            if (zza.zzbik != null) {
                if (zza.zzbik.isEmpty()) {
                    return true;
                }
                b = false;
            }
            return b;
        }
        
        @Override
        public int hashCode() {
            final int hashCode = this.getClass().getName().hashCode();
            final int hashCode2 = zzsc.hashCode(this.zzbiF);
            final int hashCode3 = zzsc.hashCode(this.zzbiG);
            final int hashCode4 = zzsc.hashCode(this.zzbiH);
            final int hashCode5 = zzsc.hashCode(this.zzbiI);
            int hashCode6;
            if (this.zzbik != null && !this.zzbik.isEmpty()) {
                hashCode6 = this.zzbik.hashCode();
            }
            else {
                hashCode6 = 0;
            }
            return 31 * (((((527 + hashCode) * 31 + hashCode2) * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode5) + hashCode6;
        }
        
        @Override
        protected int zzB() {
            final int zzB = super.zzB();
            final String[] zzbiF = this.zzbiF;
            final int n = 0;
            int n2 = zzB;
            if (zzbiF != null) {
                n2 = zzB;
                if (this.zzbiF.length > 0) {
                    int i = 0;
                    int n4;
                    int n3 = n4 = 0;
                    while (i < this.zzbiF.length) {
                        final String s = this.zzbiF[i];
                        int n5 = n3;
                        int n6 = n4;
                        if (s != null) {
                            n6 = n4 + 1;
                            n5 = n3 + zzrx.zzfA(s);
                        }
                        ++i;
                        n3 = n5;
                        n4 = n6;
                    }
                    n2 = zzB + n3 + n4 * 1;
                }
            }
            int n7 = n2;
            if (this.zzbiG != null) {
                n7 = n2;
                if (this.zzbiG.length > 0) {
                    int j = 0;
                    int n9;
                    int n8 = n9 = 0;
                    while (j < this.zzbiG.length) {
                        final String s2 = this.zzbiG[j];
                        int n10 = n8;
                        int n11 = n9;
                        if (s2 != null) {
                            n11 = n9 + 1;
                            n10 = n8 + zzrx.zzfA(s2);
                        }
                        ++j;
                        n8 = n10;
                        n9 = n11;
                    }
                    n7 = n2 + n8 + n9 * 1;
                }
            }
            int n12 = n7;
            if (this.zzbiH != null) {
                n12 = n7;
                if (this.zzbiH.length > 0) {
                    int k = 0;
                    int n13 = 0;
                    while (k < this.zzbiH.length) {
                        n13 += zzrx.zzlJ(this.zzbiH[k]);
                        ++k;
                    }
                    n12 = n7 + n13 + this.zzbiH.length * 1;
                }
            }
            int n14 = n12;
            if (this.zzbiI != null) {
                n14 = n12;
                if (this.zzbiI.length > 0) {
                    int n15 = 0;
                    for (int l = n; l < this.zzbiI.length; ++l) {
                        n15 += zzrx.zzaa(this.zzbiI[l]);
                    }
                    n14 = n12 + n15 + 1 * this.zzbiI.length;
                }
            }
            return n14;
        }
        
        public zza zzFS() {
            this.zzbiF = zzsh.zzbiC;
            this.zzbiG = zzsh.zzbiC;
            this.zzbiH = zzsh.zzbix;
            this.zzbiI = zzsh.zzbiy;
            this.zzbik = null;
            this.zzbiv = -1;
            return this;
        }
        
        public zza zzG(final zzrw zzrw) throws IOException {
            while (true) {
                final int zzFo = zzrw.zzFo();
                if (zzFo == 0) {
                    return this;
                }
                if (zzFo != 10) {
                    if (zzFo != 18) {
                        if (zzFo != 24) {
                            int n2;
                            if (zzFo != 26) {
                                if (zzFo == 32) {
                                    final int zzc = zzsh.zzc(zzrw, 32);
                                    int length;
                                    if (this.zzbiI == null) {
                                        length = 0;
                                    }
                                    else {
                                        length = this.zzbiI.length;
                                    }
                                    final long[] zzbiI = new long[zzc + length];
                                    int i = length;
                                    if (length != 0) {
                                        System.arraycopy(this.zzbiI, 0, zzbiI, 0, length);
                                        i = length;
                                    }
                                    while (i < zzbiI.length - 1) {
                                        zzbiI[i] = zzrw.zzFq();
                                        zzrw.zzFo();
                                        ++i;
                                    }
                                    zzbiI[i] = zzrw.zzFq();
                                    this.zzbiI = zzbiI;
                                    continue;
                                }
                                if (zzFo != 34) {
                                    if (!this.zza(zzrw, zzFo)) {
                                        return this;
                                    }
                                    continue;
                                }
                                else {
                                    final int zzlC = zzrw.zzlC(zzrw.zzFv());
                                    final int position = zzrw.getPosition();
                                    int n = 0;
                                    while (zzrw.zzFA() > 0) {
                                        zzrw.zzFq();
                                        ++n;
                                    }
                                    zzrw.zzlE(position);
                                    int length2;
                                    if (this.zzbiI == null) {
                                        length2 = 0;
                                    }
                                    else {
                                        length2 = this.zzbiI.length;
                                    }
                                    final long[] zzbiI2 = new long[n + length2];
                                    int j = length2;
                                    if (length2 != 0) {
                                        System.arraycopy(this.zzbiI, 0, zzbiI2, 0, length2);
                                        j = length2;
                                    }
                                    while (j < zzbiI2.length) {
                                        zzbiI2[j] = zzrw.zzFq();
                                        ++j;
                                    }
                                    this.zzbiI = zzbiI2;
                                    n2 = zzlC;
                                }
                            }
                            else {
                                final int zzlC2 = zzrw.zzlC(zzrw.zzFv());
                                final int position2 = zzrw.getPosition();
                                int n3 = 0;
                                while (zzrw.zzFA() > 0) {
                                    zzrw.zzFr();
                                    ++n3;
                                }
                                zzrw.zzlE(position2);
                                int length3;
                                if (this.zzbiH == null) {
                                    length3 = 0;
                                }
                                else {
                                    length3 = this.zzbiH.length;
                                }
                                final int[] zzbiH = new int[n3 + length3];
                                int k = length3;
                                if (length3 != 0) {
                                    System.arraycopy(this.zzbiH, 0, zzbiH, 0, length3);
                                    k = length3;
                                }
                                while (k < zzbiH.length) {
                                    zzbiH[k] = zzrw.zzFr();
                                    ++k;
                                }
                                this.zzbiH = zzbiH;
                                n2 = zzlC2;
                            }
                            zzrw.zzlD(n2);
                        }
                        else {
                            final int zzc2 = zzsh.zzc(zzrw, 24);
                            int length4;
                            if (this.zzbiH == null) {
                                length4 = 0;
                            }
                            else {
                                length4 = this.zzbiH.length;
                            }
                            final int[] zzbiH2 = new int[zzc2 + length4];
                            int l = length4;
                            if (length4 != 0) {
                                System.arraycopy(this.zzbiH, 0, zzbiH2, 0, length4);
                                l = length4;
                            }
                            while (l < zzbiH2.length - 1) {
                                zzbiH2[l] = zzrw.zzFr();
                                zzrw.zzFo();
                                ++l;
                            }
                            zzbiH2[l] = zzrw.zzFr();
                            this.zzbiH = zzbiH2;
                        }
                    }
                    else {
                        final int zzc3 = zzsh.zzc(zzrw, 18);
                        int length5;
                        if (this.zzbiG == null) {
                            length5 = 0;
                        }
                        else {
                            length5 = this.zzbiG.length;
                        }
                        final String[] zzbiG = new String[zzc3 + length5];
                        int n4 = length5;
                        if (length5 != 0) {
                            System.arraycopy(this.zzbiG, 0, zzbiG, 0, length5);
                            n4 = length5;
                        }
                        while (n4 < zzbiG.length - 1) {
                            zzbiG[n4] = zzrw.readString();
                            zzrw.zzFo();
                            ++n4;
                        }
                        zzbiG[n4] = zzrw.readString();
                        this.zzbiG = zzbiG;
                    }
                }
                else {
                    final int zzc4 = zzsh.zzc(zzrw, 10);
                    int length6;
                    if (this.zzbiF == null) {
                        length6 = 0;
                    }
                    else {
                        length6 = this.zzbiF.length;
                    }
                    final String[] zzbiF = new String[zzc4 + length6];
                    int n5 = length6;
                    if (length6 != 0) {
                        System.arraycopy(this.zzbiF, 0, zzbiF, 0, length6);
                        n5 = length6;
                    }
                    while (n5 < zzbiF.length - 1) {
                        zzbiF[n5] = zzrw.readString();
                        zzrw.zzFo();
                        ++n5;
                    }
                    zzbiF[n5] = zzrw.readString();
                    this.zzbiF = zzbiF;
                }
            }
        }
        
        @Override
        public void zza(final zzrx zzrx) throws IOException {
            final String[] zzbiF = this.zzbiF;
            final int n = 0;
            if (zzbiF != null && this.zzbiF.length > 0) {
                for (int i = 0; i < this.zzbiF.length; ++i) {
                    final String s = this.zzbiF[i];
                    if (s != null) {
                        zzrx.zzb(1, s);
                    }
                }
            }
            if (this.zzbiG != null && this.zzbiG.length > 0) {
                for (int j = 0; j < this.zzbiG.length; ++j) {
                    final String s2 = this.zzbiG[j];
                    if (s2 != null) {
                        zzrx.zzb(2, s2);
                    }
                }
            }
            if (this.zzbiH != null && this.zzbiH.length > 0) {
                for (int k = 0; k < this.zzbiH.length; ++k) {
                    zzrx.zzy(3, this.zzbiH[k]);
                }
            }
            if (this.zzbiI != null && this.zzbiI.length > 0) {
                for (int l = n; l < this.zzbiI.length; ++l) {
                    zzrx.zzb(4, this.zzbiI[l]);
                }
            }
            super.zza(zzrx);
        }
    }
    
    public static final class zzb extends zzry<zzb>
    {
        public String version;
        public int zzbiJ;
        public String zzbiK;
        
        public zzb() {
            this.zzFT();
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b = true;
            if (o == this) {
                return true;
            }
            if (!(o instanceof zzb)) {
                return false;
            }
            final zzb zzb = (zzb)o;
            if (this.zzbiJ != zzb.zzbiJ) {
                return false;
            }
            if (this.zzbiK == null) {
                if (zzb.zzbiK != null) {
                    return false;
                }
            }
            else if (!this.zzbiK.equals(zzb.zzbiK)) {
                return false;
            }
            if (this.version == null) {
                if (zzb.version != null) {
                    return false;
                }
            }
            else if (!this.version.equals(zzb.version)) {
                return false;
            }
            if (this.zzbik != null && !this.zzbik.isEmpty()) {
                return this.zzbik.equals(zzb.zzbik);
            }
            if (zzb.zzbik != null) {
                if (zzb.zzbik.isEmpty()) {
                    return true;
                }
                b = false;
            }
            return b;
        }
        
        @Override
        public int hashCode() {
            final int hashCode = this.getClass().getName().hashCode();
            final int zzbiJ = this.zzbiJ;
            final String zzbiK = this.zzbiK;
            final int n = 0;
            int hashCode2;
            if (zzbiK == null) {
                hashCode2 = 0;
            }
            else {
                hashCode2 = this.zzbiK.hashCode();
            }
            int hashCode3;
            if (this.version == null) {
                hashCode3 = 0;
            }
            else {
                hashCode3 = this.version.hashCode();
            }
            int hashCode4 = n;
            if (this.zzbik != null) {
                if (this.zzbik.isEmpty()) {
                    hashCode4 = n;
                }
                else {
                    hashCode4 = this.zzbik.hashCode();
                }
            }
            return 31 * ((((527 + hashCode) * 31 + zzbiJ) * 31 + hashCode2) * 31 + hashCode3) + hashCode4;
        }
        
        @Override
        protected int zzB() {
            int zzB;
            final int n = zzB = super.zzB();
            if (this.zzbiJ != 0) {
                zzB = n + zzrx.zzA(1, this.zzbiJ);
            }
            int n2 = zzB;
            if (!this.zzbiK.equals("")) {
                n2 = zzB + zzrx.zzn(2, this.zzbiK);
            }
            int n3 = n2;
            if (!this.version.equals("")) {
                n3 = n2 + zzrx.zzn(3, this.version);
            }
            return n3;
        }
        
        public zzb zzFT() {
            this.zzbiJ = 0;
            this.zzbiK = "";
            this.version = "";
            this.zzbik = null;
            this.zzbiv = -1;
            return this;
        }
        
        public zzb zzH(final zzrw zzrw) throws IOException {
            while (true) {
                final int zzFo = zzrw.zzFo();
                if (zzFo == 0) {
                    return this;
                }
                if (zzFo != 8) {
                    if (zzFo != 18) {
                        if (zzFo != 26) {
                            if (!this.zza(zzrw, zzFo)) {
                                return this;
                            }
                            continue;
                        }
                        else {
                            this.version = zzrw.readString();
                        }
                    }
                    else {
                        this.zzbiK = zzrw.readString();
                    }
                }
                else {
                    final int zzFr = zzrw.zzFr();
                    switch (zzFr) {
                        default: {
                            continue;
                        }
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                        case 11:
                        case 12:
                        case 13:
                        case 14:
                        case 15:
                        case 16:
                        case 17:
                        case 18:
                        case 19:
                        case 20:
                        case 21:
                        case 22:
                        case 23:
                        case 24:
                        case 25:
                        case 26: {
                            this.zzbiJ = zzFr;
                            continue;
                        }
                    }
                }
            }
        }
        
        @Override
        public void zza(final zzrx zzrx) throws IOException {
            if (this.zzbiJ != 0) {
                zzrx.zzy(1, this.zzbiJ);
            }
            if (!this.zzbiK.equals("")) {
                zzrx.zzb(2, this.zzbiK);
            }
            if (!this.version.equals("")) {
                zzrx.zzb(3, this.version);
            }
            super.zza(zzrx);
        }
    }
    
    public static final class zzc extends zzry<zzc>
    {
        public byte[] zzbiL;
        public byte[][] zzbiM;
        public boolean zzbiN;
        
        public zzc() {
            this.zzFU();
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b = true;
            if (o == this) {
                return true;
            }
            if (!(o instanceof zzc)) {
                return false;
            }
            final zzc zzc = (zzc)o;
            if (!Arrays.equals(this.zzbiL, zzc.zzbiL)) {
                return false;
            }
            if (!zzsc.zza(this.zzbiM, zzc.zzbiM)) {
                return false;
            }
            if (this.zzbiN != zzc.zzbiN) {
                return false;
            }
            if (this.zzbik != null && !this.zzbik.isEmpty()) {
                return this.zzbik.equals(zzc.zzbik);
            }
            if (zzc.zzbik != null) {
                if (zzc.zzbik.isEmpty()) {
                    return true;
                }
                b = false;
            }
            return b;
        }
        
        @Override
        public int hashCode() {
            final int hashCode = this.getClass().getName().hashCode();
            final int hashCode2 = Arrays.hashCode(this.zzbiL);
            final int zza = zzsc.zza(this.zzbiM);
            int n;
            if (this.zzbiN) {
                n = 1231;
            }
            else {
                n = 1237;
            }
            int hashCode3;
            if (this.zzbik != null && !this.zzbik.isEmpty()) {
                hashCode3 = this.zzbik.hashCode();
            }
            else {
                hashCode3 = 0;
            }
            return 31 * ((((527 + hashCode) * 31 + hashCode2) * 31 + zza) * 31 + n) + hashCode3;
        }
        
        @Override
        protected int zzB() {
            int zzB;
            final int n = zzB = super.zzB();
            if (!Arrays.equals(this.zzbiL, zzsh.zzbiE)) {
                zzB = n + zzrx.zzb(1, this.zzbiL);
            }
            int n2 = zzB;
            if (this.zzbiM != null) {
                n2 = zzB;
                if (this.zzbiM.length > 0) {
                    int i = 0;
                    int n3 = 0;
                    int n4 = 0;
                    while (i < this.zzbiM.length) {
                        final byte[] array = this.zzbiM[i];
                        int n5 = n3;
                        int n6 = n4;
                        if (array != null) {
                            n6 = n4 + 1;
                            n5 = n3 + zzrx.zzE(array);
                        }
                        ++i;
                        n3 = n5;
                        n4 = n6;
                    }
                    n2 = zzB + n3 + 1 * n4;
                }
            }
            int n7 = n2;
            if (this.zzbiN) {
                n7 = n2 + zzrx.zzc(3, this.zzbiN);
            }
            return n7;
        }
        
        public zzc zzFU() {
            this.zzbiL = zzsh.zzbiE;
            this.zzbiM = zzsh.zzbiD;
            this.zzbiN = false;
            this.zzbik = null;
            this.zzbiv = -1;
            return this;
        }
        
        public zzc zzI(final zzrw zzrw) throws IOException {
            while (true) {
                final int zzFo = zzrw.zzFo();
                if (zzFo == 0) {
                    return this;
                }
                if (zzFo != 10) {
                    if (zzFo != 18) {
                        if (zzFo != 24) {
                            if (!this.zza(zzrw, zzFo)) {
                                return this;
                            }
                            continue;
                        }
                        else {
                            this.zzbiN = zzrw.zzFs();
                        }
                    }
                    else {
                        final int zzc = zzsh.zzc(zzrw, 18);
                        int length;
                        if (this.zzbiM == null) {
                            length = 0;
                        }
                        else {
                            length = this.zzbiM.length;
                        }
                        final byte[][] zzbiM = new byte[zzc + length][];
                        int i = length;
                        if (length != 0) {
                            System.arraycopy(this.zzbiM, 0, zzbiM, 0, length);
                            i = length;
                        }
                        while (i < zzbiM.length - 1) {
                            zzbiM[i] = zzrw.readBytes();
                            zzrw.zzFo();
                            ++i;
                        }
                        zzbiM[i] = zzrw.readBytes();
                        this.zzbiM = zzbiM;
                    }
                }
                else {
                    this.zzbiL = zzrw.readBytes();
                }
            }
        }
        
        @Override
        public void zza(final zzrx zzrx) throws IOException {
            if (!Arrays.equals(this.zzbiL, zzsh.zzbiE)) {
                zzrx.zza(1, this.zzbiL);
            }
            if (this.zzbiM != null && this.zzbiM.length > 0) {
                for (int i = 0; i < this.zzbiM.length; ++i) {
                    final byte[] array = this.zzbiM[i];
                    if (array != null) {
                        zzrx.zza(2, array);
                    }
                }
            }
            if (this.zzbiN) {
                zzrx.zzb(3, this.zzbiN);
            }
            super.zza(zzrx);
        }
    }
    
    public static final class zzd extends zzry<zzd>
    {
        public String tag;
        public long zzbiO;
        public long zzbiP;
        public int zzbiQ;
        public int zzbiR;
        public boolean zzbiS;
        public zze[] zzbiT;
        public zzb zzbiU;
        public byte[] zzbiV;
        public byte[] zzbiW;
        public byte[] zzbiX;
        public zza zzbiY;
        public String zzbiZ;
        public long zzbja;
        public zzc zzbjb;
        public byte[] zzbjc;
        public int zzbjd;
        public int[] zzbje;
        
        public zzd() {
            this.zzFV();
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b = true;
            if (o == this) {
                return true;
            }
            if (!(o instanceof zzd)) {
                return false;
            }
            final zzd zzd = (zzd)o;
            if (this.zzbiO != zzd.zzbiO) {
                return false;
            }
            if (this.zzbiP != zzd.zzbiP) {
                return false;
            }
            if (this.tag == null) {
                if (zzd.tag != null) {
                    return false;
                }
            }
            else if (!this.tag.equals(zzd.tag)) {
                return false;
            }
            if (this.zzbiQ != zzd.zzbiQ) {
                return false;
            }
            if (this.zzbiR != zzd.zzbiR) {
                return false;
            }
            if (this.zzbiS != zzd.zzbiS) {
                return false;
            }
            if (!zzsc.equals(this.zzbiT, zzd.zzbiT)) {
                return false;
            }
            if (this.zzbiU == null) {
                if (zzd.zzbiU != null) {
                    return false;
                }
            }
            else if (!this.zzbiU.equals(zzd.zzbiU)) {
                return false;
            }
            if (!Arrays.equals(this.zzbiV, zzd.zzbiV)) {
                return false;
            }
            if (!Arrays.equals(this.zzbiW, zzd.zzbiW)) {
                return false;
            }
            if (!Arrays.equals(this.zzbiX, zzd.zzbiX)) {
                return false;
            }
            if (this.zzbiY == null) {
                if (zzd.zzbiY != null) {
                    return false;
                }
            }
            else if (!this.zzbiY.equals(zzd.zzbiY)) {
                return false;
            }
            if (this.zzbiZ == null) {
                if (zzd.zzbiZ != null) {
                    return false;
                }
            }
            else if (!this.zzbiZ.equals(zzd.zzbiZ)) {
                return false;
            }
            if (this.zzbja != zzd.zzbja) {
                return false;
            }
            if (this.zzbjb == null) {
                if (zzd.zzbjb != null) {
                    return false;
                }
            }
            else if (!this.zzbjb.equals(zzd.zzbjb)) {
                return false;
            }
            if (!Arrays.equals(this.zzbjc, zzd.zzbjc)) {
                return false;
            }
            if (this.zzbjd != zzd.zzbjd) {
                return false;
            }
            if (!zzsc.equals(this.zzbje, zzd.zzbje)) {
                return false;
            }
            if (this.zzbik != null && !this.zzbik.isEmpty()) {
                return this.zzbik.equals(zzd.zzbik);
            }
            if (zzd.zzbik != null) {
                if (zzd.zzbik.isEmpty()) {
                    return true;
                }
                b = false;
            }
            return b;
        }
        
        @Override
        public int hashCode() {
            final int hashCode = this.getClass().getName().hashCode();
            final int n = (int)(this.zzbiO ^ this.zzbiO >>> 32);
            final int n2 = (int)(this.zzbiP ^ this.zzbiP >>> 32);
            final String tag = this.tag;
            final int n3 = 0;
            int hashCode2;
            if (tag == null) {
                hashCode2 = 0;
            }
            else {
                hashCode2 = this.tag.hashCode();
            }
            final int zzbiQ = this.zzbiQ;
            final int zzbiR = this.zzbiR;
            int n4;
            if (this.zzbiS) {
                n4 = 1231;
            }
            else {
                n4 = 1237;
            }
            final int hashCode3 = zzsc.hashCode(this.zzbiT);
            int hashCode4;
            if (this.zzbiU == null) {
                hashCode4 = 0;
            }
            else {
                hashCode4 = this.zzbiU.hashCode();
            }
            final int hashCode5 = Arrays.hashCode(this.zzbiV);
            final int hashCode6 = Arrays.hashCode(this.zzbiW);
            final int hashCode7 = Arrays.hashCode(this.zzbiX);
            int hashCode8;
            if (this.zzbiY == null) {
                hashCode8 = 0;
            }
            else {
                hashCode8 = this.zzbiY.hashCode();
            }
            int hashCode9;
            if (this.zzbiZ == null) {
                hashCode9 = 0;
            }
            else {
                hashCode9 = this.zzbiZ.hashCode();
            }
            final int n5 = (int)(this.zzbja ^ this.zzbja >>> 32);
            int hashCode10;
            if (this.zzbjb == null) {
                hashCode10 = 0;
            }
            else {
                hashCode10 = this.zzbjb.hashCode();
            }
            final int hashCode11 = Arrays.hashCode(this.zzbjc);
            final int zzbjd = this.zzbjd;
            final int hashCode12 = zzsc.hashCode(this.zzbje);
            int hashCode13 = n3;
            if (this.zzbik != null) {
                if (this.zzbik.isEmpty()) {
                    hashCode13 = n3;
                }
                else {
                    hashCode13 = this.zzbik.hashCode();
                }
            }
            return 31 * (((((((((((((((((((527 + hashCode) * 31 + n) * 31 + n2) * 31 + hashCode2) * 31 + zzbiQ) * 31 + zzbiR) * 31 + n4) * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode5) * 31 + hashCode6) * 31 + hashCode7) * 31 + hashCode8) * 31 + hashCode9) * 31 + n5) * 31 + hashCode10) * 31 + hashCode11) * 31 + zzbjd) * 31 + hashCode12) + hashCode13;
        }
        
        @Override
        protected int zzB() {
            int zzB;
            final int n = zzB = super.zzB();
            if (this.zzbiO != 0L) {
                zzB = n + zzrx.zzd(1, this.zzbiO);
            }
            int n2 = zzB;
            if (!this.tag.equals("")) {
                n2 = zzB + zzrx.zzn(2, this.tag);
            }
            final zze[] zzbiT = this.zzbiT;
            final int n3 = 0;
            int n4 = n2;
            if (zzbiT != null) {
                n4 = n2;
                if (this.zzbiT.length > 0) {
                    int n5;
                    for (int i = 0; i < this.zzbiT.length; ++i, n2 = n5) {
                        final zze zze = this.zzbiT[i];
                        n5 = n2;
                        if (zze != null) {
                            n5 = n2 + zzrx.zzc(3, zze);
                        }
                    }
                    n4 = n2;
                }
            }
            int n6 = n4;
            if (!Arrays.equals(this.zzbiV, zzsh.zzbiE)) {
                n6 = n4 + zzrx.zzb(6, this.zzbiV);
            }
            int n7 = n6;
            if (this.zzbiY != null) {
                n7 = n6 + zzrx.zzc(7, this.zzbiY);
            }
            int n8 = n7;
            if (!Arrays.equals(this.zzbiW, zzsh.zzbiE)) {
                n8 = n7 + zzrx.zzb(8, this.zzbiW);
            }
            int n9 = n8;
            if (this.zzbiU != null) {
                n9 = n8 + zzrx.zzc(9, this.zzbiU);
            }
            int n10 = n9;
            if (this.zzbiS) {
                n10 = n9 + zzrx.zzc(10, this.zzbiS);
            }
            int n11 = n10;
            if (this.zzbiQ != 0) {
                n11 = n10 + zzrx.zzA(11, this.zzbiQ);
            }
            int n12 = n11;
            if (this.zzbiR != 0) {
                n12 = n11 + zzrx.zzA(12, this.zzbiR);
            }
            int n13 = n12;
            if (!Arrays.equals(this.zzbiX, zzsh.zzbiE)) {
                n13 = n12 + zzrx.zzb(13, this.zzbiX);
            }
            int n14 = n13;
            if (!this.zzbiZ.equals("")) {
                n14 = n13 + zzrx.zzn(14, this.zzbiZ);
            }
            int n15 = n14;
            if (this.zzbja != 180000L) {
                n15 = n14 + zzrx.zze(15, this.zzbja);
            }
            int n16 = n15;
            if (this.zzbjb != null) {
                n16 = n15 + zzrx.zzc(16, this.zzbjb);
            }
            int n17 = n16;
            if (this.zzbiP != 0L) {
                n17 = n16 + zzrx.zzd(17, this.zzbiP);
            }
            int n18 = n17;
            if (!Arrays.equals(this.zzbjc, zzsh.zzbiE)) {
                n18 = n17 + zzrx.zzb(18, this.zzbjc);
            }
            int n19 = n18;
            if (this.zzbjd != 0) {
                n19 = n18 + zzrx.zzA(19, this.zzbjd);
            }
            int n20 = n19;
            if (this.zzbje != null) {
                n20 = n19;
                if (this.zzbje.length > 0) {
                    int n21 = 0;
                    for (int j = n3; j < this.zzbje.length; ++j) {
                        n21 += zzrx.zzlJ(this.zzbje[j]);
                    }
                    n20 = n19 + n21 + 2 * this.zzbje.length;
                }
            }
            return n20;
        }
        
        public zzd zzFV() {
            this.zzbiO = 0L;
            this.zzbiP = 0L;
            this.tag = "";
            this.zzbiQ = 0;
            this.zzbiR = 0;
            this.zzbiS = false;
            this.zzbiT = zze.zzFW();
            this.zzbiU = null;
            this.zzbiV = zzsh.zzbiE;
            this.zzbiW = zzsh.zzbiE;
            this.zzbiX = zzsh.zzbiE;
            this.zzbiY = null;
            this.zzbiZ = "";
            this.zzbja = 180000L;
            this.zzbjb = null;
            this.zzbjc = zzsh.zzbiE;
            this.zzbjd = 0;
            this.zzbje = zzsh.zzbix;
            this.zzbik = null;
            this.zzbiv = -1;
            return this;
        }
        
        public zzd zzJ(final zzrw zzrw) throws IOException {
            while (true) {
                final int zzFo = zzrw.zzFo();
                zzse zzse = null;
                switch (zzFo) {
                    default: {
                        if (!this.zza(zzrw, zzFo)) {
                            return this;
                        }
                        continue;
                    }
                    case 162: {
                        final int zzlC = zzrw.zzlC(zzrw.zzFv());
                        final int position = zzrw.getPosition();
                        int n = 0;
                        while (zzrw.zzFA() > 0) {
                            zzrw.zzFr();
                            ++n;
                        }
                        zzrw.zzlE(position);
                        int length;
                        if (this.zzbje == null) {
                            length = 0;
                        }
                        else {
                            length = this.zzbje.length;
                        }
                        final int[] zzbje = new int[n + length];
                        int i = length;
                        if (length != 0) {
                            System.arraycopy(this.zzbje, 0, zzbje, 0, length);
                            i = length;
                        }
                        while (i < zzbje.length) {
                            zzbje[i] = zzrw.zzFr();
                            ++i;
                        }
                        this.zzbje = zzbje;
                        zzrw.zzlD(zzlC);
                        continue;
                    }
                    case 160: {
                        final int zzc = zzsh.zzc(zzrw, 160);
                        int length2;
                        if (this.zzbje == null) {
                            length2 = 0;
                        }
                        else {
                            length2 = this.zzbje.length;
                        }
                        final int[] zzbje2 = new int[zzc + length2];
                        int j = length2;
                        if (length2 != 0) {
                            System.arraycopy(this.zzbje, 0, zzbje2, 0, length2);
                            j = length2;
                        }
                        while (j < zzbje2.length - 1) {
                            zzbje2[j] = zzrw.zzFr();
                            zzrw.zzFo();
                            ++j;
                        }
                        zzbje2[j] = zzrw.zzFr();
                        this.zzbje = zzbje2;
                        continue;
                    }
                    case 152: {
                        final int zzFr = zzrw.zzFr();
                        switch (zzFr) {
                            default: {
                                continue;
                            }
                            case 0:
                            case 1:
                            case 2: {
                                this.zzbjd = zzFr;
                                continue;
                            }
                        }
                        break;
                    }
                    case 146: {
                        this.zzbjc = zzrw.readBytes();
                        continue;
                    }
                    case 136: {
                        this.zzbiP = zzrw.zzFq();
                        continue;
                    }
                    case 130: {
                        if (this.zzbjb == null) {
                            this.zzbjb = new zzc();
                        }
                        zzse = this.zzbjb;
                        break;
                    }
                    case 120: {
                        this.zzbja = zzrw.zzFu();
                        continue;
                    }
                    case 114: {
                        this.zzbiZ = zzrw.readString();
                        continue;
                    }
                    case 106: {
                        this.zzbiX = zzrw.readBytes();
                        continue;
                    }
                    case 96: {
                        this.zzbiR = zzrw.zzFr();
                        continue;
                    }
                    case 88: {
                        this.zzbiQ = zzrw.zzFr();
                        continue;
                    }
                    case 80: {
                        this.zzbiS = zzrw.zzFs();
                        continue;
                    }
                    case 74: {
                        if (this.zzbiU == null) {
                            this.zzbiU = new zzb();
                        }
                        zzse = this.zzbiU;
                        break;
                    }
                    case 66: {
                        this.zzbiW = zzrw.readBytes();
                        continue;
                    }
                    case 58: {
                        if (this.zzbiY == null) {
                            this.zzbiY = new zza();
                        }
                        zzse = this.zzbiY;
                        break;
                    }
                    case 50: {
                        this.zzbiV = zzrw.readBytes();
                        continue;
                    }
                    case 26: {
                        final int zzc2 = zzsh.zzc(zzrw, 26);
                        int length3;
                        if (this.zzbiT == null) {
                            length3 = 0;
                        }
                        else {
                            length3 = this.zzbiT.length;
                        }
                        final zze[] zzbiT = new zze[zzc2 + length3];
                        int k = length3;
                        if (length3 != 0) {
                            System.arraycopy(this.zzbiT, 0, zzbiT, 0, length3);
                            k = length3;
                        }
                        while (k < zzbiT.length - 1) {
                            zzrw.zza(zzbiT[k] = new zze());
                            zzrw.zzFo();
                            ++k;
                        }
                        zzrw.zza(zzbiT[k] = new zze());
                        this.zzbiT = zzbiT;
                        continue;
                    }
                    case 18: {
                        this.tag = zzrw.readString();
                        continue;
                    }
                    case 8: {
                        this.zzbiO = zzrw.zzFq();
                        continue;
                    }
                    case 0: {
                        return this;
                    }
                }
                zzrw.zza(zzse);
            }
        }
        
        @Override
        public void zza(final zzrx zzrx) throws IOException {
            if (this.zzbiO != 0L) {
                zzrx.zzb(1, this.zzbiO);
            }
            if (!this.tag.equals("")) {
                zzrx.zzb(2, this.tag);
            }
            final zze[] zzbiT = this.zzbiT;
            final int n = 0;
            if (zzbiT != null && this.zzbiT.length > 0) {
                for (int i = 0; i < this.zzbiT.length; ++i) {
                    final zze zze = this.zzbiT[i];
                    if (zze != null) {
                        zzrx.zza(3, zze);
                    }
                }
            }
            if (!Arrays.equals(this.zzbiV, zzsh.zzbiE)) {
                zzrx.zza(6, this.zzbiV);
            }
            if (this.zzbiY != null) {
                zzrx.zza(7, this.zzbiY);
            }
            if (!Arrays.equals(this.zzbiW, zzsh.zzbiE)) {
                zzrx.zza(8, this.zzbiW);
            }
            if (this.zzbiU != null) {
                zzrx.zza(9, this.zzbiU);
            }
            if (this.zzbiS) {
                zzrx.zzb(10, this.zzbiS);
            }
            if (this.zzbiQ != 0) {
                zzrx.zzy(11, this.zzbiQ);
            }
            if (this.zzbiR != 0) {
                zzrx.zzy(12, this.zzbiR);
            }
            if (!Arrays.equals(this.zzbiX, zzsh.zzbiE)) {
                zzrx.zza(13, this.zzbiX);
            }
            if (!this.zzbiZ.equals("")) {
                zzrx.zzb(14, this.zzbiZ);
            }
            if (this.zzbja != 180000L) {
                zzrx.zzc(15, this.zzbja);
            }
            if (this.zzbjb != null) {
                zzrx.zza(16, this.zzbjb);
            }
            if (this.zzbiP != 0L) {
                zzrx.zzb(17, this.zzbiP);
            }
            if (!Arrays.equals(this.zzbjc, zzsh.zzbiE)) {
                zzrx.zza(18, this.zzbjc);
            }
            if (this.zzbjd != 0) {
                zzrx.zzy(19, this.zzbjd);
            }
            if (this.zzbje != null && this.zzbje.length > 0) {
                for (int j = n; j < this.zzbje.length; ++j) {
                    zzrx.zzy(20, this.zzbje[j]);
                }
            }
            super.zza(zzrx);
        }
    }
    
    public static final class zze extends zzry<zze>
    {
        private static volatile zze[] zzbjf;
        public String key;
        public String value;
        
        public zze() {
            this.zzFX();
        }
        
        public static zze[] zzFW() {
            if (zze.zzbjf == null) {
                synchronized (zzsc.zzbiu) {
                    if (zze.zzbjf == null) {
                        zze.zzbjf = new zze[0];
                    }
                }
            }
            return zze.zzbjf;
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b = true;
            if (o == this) {
                return true;
            }
            if (!(o instanceof zze)) {
                return false;
            }
            final zze zze = (zze)o;
            if (this.key == null) {
                if (zze.key != null) {
                    return false;
                }
            }
            else if (!this.key.equals(zze.key)) {
                return false;
            }
            if (this.value == null) {
                if (zze.value != null) {
                    return false;
                }
            }
            else if (!this.value.equals(zze.value)) {
                return false;
            }
            if (this.zzbik != null && !this.zzbik.isEmpty()) {
                return this.zzbik.equals(zze.zzbik);
            }
            if (zze.zzbik != null) {
                if (zze.zzbik.isEmpty()) {
                    return true;
                }
                b = false;
            }
            return b;
        }
        
        @Override
        public int hashCode() {
            final int hashCode = this.getClass().getName().hashCode();
            final String key = this.key;
            final int n = 0;
            int hashCode2;
            if (key == null) {
                hashCode2 = 0;
            }
            else {
                hashCode2 = this.key.hashCode();
            }
            int hashCode3;
            if (this.value == null) {
                hashCode3 = 0;
            }
            else {
                hashCode3 = this.value.hashCode();
            }
            int hashCode4 = n;
            if (this.zzbik != null) {
                if (this.zzbik.isEmpty()) {
                    hashCode4 = n;
                }
                else {
                    hashCode4 = this.zzbik.hashCode();
                }
            }
            return 31 * (((527 + hashCode) * 31 + hashCode2) * 31 + hashCode3) + hashCode4;
        }
        
        @Override
        protected int zzB() {
            int zzB;
            final int n = zzB = super.zzB();
            if (!this.key.equals("")) {
                zzB = n + zzrx.zzn(1, this.key);
            }
            int n2 = zzB;
            if (!this.value.equals("")) {
                n2 = zzB + zzrx.zzn(2, this.value);
            }
            return n2;
        }
        
        public zze zzFX() {
            this.key = "";
            this.value = "";
            this.zzbik = null;
            this.zzbiv = -1;
            return this;
        }
        
        public zze zzK(final zzrw zzrw) throws IOException {
            while (true) {
                final int zzFo = zzrw.zzFo();
                if (zzFo == 0) {
                    return this;
                }
                if (zzFo != 10) {
                    if (zzFo != 18) {
                        if (!this.zza(zzrw, zzFo)) {
                            return this;
                        }
                        continue;
                    }
                    else {
                        this.value = zzrw.readString();
                    }
                }
                else {
                    this.key = zzrw.readString();
                }
            }
        }
        
        @Override
        public void zza(final zzrx zzrx) throws IOException {
            if (!this.key.equals("")) {
                zzrx.zzb(1, this.key);
            }
            if (!this.value.equals("")) {
                zzrx.zzb(2, this.value);
            }
            super.zza(zzrx);
        }
    }
}
