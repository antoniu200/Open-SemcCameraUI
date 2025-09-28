package com.google.android.gms.internal;

import java.io.IOException;
import java.util.Arrays;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface zzsi {

    public static final class zza extends zzry<zza> {
        public String[] zzbiF;
        public String[] zzbiG;
        public int[] zzbiH;
        public long[] zzbiI;

        public zza() {
            zzFS();
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof zza)) {
                return false;
            }
            zza zzaVar = (zza) obj;
            if (zzsc.equals(this.zzbiF, zzaVar.zzbiF) && zzsc.equals(this.zzbiG, zzaVar.zzbiG) && zzsc.equals(this.zzbiH, zzaVar.zzbiH) && zzsc.equals(this.zzbiI, zzaVar.zzbiI)) {
                return (this.zzbik == null || this.zzbik.isEmpty()) ? zzaVar.zzbik == null || zzaVar.zzbik.isEmpty() : this.zzbik.equals(zzaVar.zzbik);
            }
            return false;
        }

        public int hashCode() {
            return (31 * (((((((((527 + getClass().getName().hashCode()) * 31) + zzsc.hashCode(this.zzbiF)) * 31) + zzsc.hashCode(this.zzbiG)) * 31) + zzsc.hashCode(this.zzbiH)) * 31) + zzsc.hashCode(this.zzbiI))) + ((this.zzbik == null || this.zzbik.isEmpty()) ? 0 : this.zzbik.hashCode());
        }

        @Override // com.google.android.gms.internal.zzry, com.google.android.gms.internal.zzse
        protected int zzB() {
            int iZzB = super.zzB();
            if (this.zzbiF != null && this.zzbiF.length > 0) {
                int iZzfA = 0;
                int i = 0;
                for (int i2 = 0; i2 < this.zzbiF.length; i2++) {
                    String str = this.zzbiF[i2];
                    if (str != null) {
                        i++;
                        iZzfA += zzrx.zzfA(str);
                    }
                }
                iZzB = iZzB + iZzfA + (i * 1);
            }
            if (this.zzbiG != null && this.zzbiG.length > 0) {
                int iZzfA2 = 0;
                int i3 = 0;
                for (int i4 = 0; i4 < this.zzbiG.length; i4++) {
                    String str2 = this.zzbiG[i4];
                    if (str2 != null) {
                        i3++;
                        iZzfA2 += zzrx.zzfA(str2);
                    }
                }
                iZzB = iZzB + iZzfA2 + (i3 * 1);
            }
            if (this.zzbiH != null && this.zzbiH.length > 0) {
                int iZzlJ = 0;
                for (int i5 = 0; i5 < this.zzbiH.length; i5++) {
                    iZzlJ += zzrx.zzlJ(this.zzbiH[i5]);
                }
                iZzB = iZzB + iZzlJ + (this.zzbiH.length * 1);
            }
            if (this.zzbiI == null || this.zzbiI.length <= 0) {
                return iZzB;
            }
            int iZzaa = 0;
            for (int i6 = 0; i6 < this.zzbiI.length; i6++) {
                iZzaa += zzrx.zzaa(this.zzbiI[i6]);
            }
            return iZzB + iZzaa + (1 * this.zzbiI.length);
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

        @Override // com.google.android.gms.internal.zzse
        /* renamed from: zzG, reason: merged with bridge method [inline-methods] */
        public zza zzb(zzrw zzrwVar) throws IOException {
            int iZzlC;
            while (true) {
                int iZzFo = zzrwVar.zzFo();
                if (iZzFo == 0) {
                    return this;
                }
                if (iZzFo == 10) {
                    int iZzc = zzsh.zzc(zzrwVar, 10);
                    int length = this.zzbiF == null ? 0 : this.zzbiF.length;
                    String[] strArr = new String[iZzc + length];
                    if (length != 0) {
                        System.arraycopy(this.zzbiF, 0, strArr, 0, length);
                    }
                    while (length < strArr.length - 1) {
                        strArr[length] = zzrwVar.readString();
                        zzrwVar.zzFo();
                        length++;
                    }
                    strArr[length] = zzrwVar.readString();
                    this.zzbiF = strArr;
                } else if (iZzFo == 18) {
                    int iZzc2 = zzsh.zzc(zzrwVar, 18);
                    int length2 = this.zzbiG == null ? 0 : this.zzbiG.length;
                    String[] strArr2 = new String[iZzc2 + length2];
                    if (length2 != 0) {
                        System.arraycopy(this.zzbiG, 0, strArr2, 0, length2);
                    }
                    while (length2 < strArr2.length - 1) {
                        strArr2[length2] = zzrwVar.readString();
                        zzrwVar.zzFo();
                        length2++;
                    }
                    strArr2[length2] = zzrwVar.readString();
                    this.zzbiG = strArr2;
                } else if (iZzFo != 24) {
                    if (iZzFo == 26) {
                        iZzlC = zzrwVar.zzlC(zzrwVar.zzFv());
                        int position = zzrwVar.getPosition();
                        int i = 0;
                        while (zzrwVar.zzFA() > 0) {
                            zzrwVar.zzFr();
                            i++;
                        }
                        zzrwVar.zzlE(position);
                        int length3 = this.zzbiH == null ? 0 : this.zzbiH.length;
                        int[] iArr = new int[i + length3];
                        if (length3 != 0) {
                            System.arraycopy(this.zzbiH, 0, iArr, 0, length3);
                        }
                        while (length3 < iArr.length) {
                            iArr[length3] = zzrwVar.zzFr();
                            length3++;
                        }
                        this.zzbiH = iArr;
                    } else if (iZzFo == 32) {
                        int iZzc3 = zzsh.zzc(zzrwVar, 32);
                        int length4 = this.zzbiI == null ? 0 : this.zzbiI.length;
                        long[] jArr = new long[iZzc3 + length4];
                        if (length4 != 0) {
                            System.arraycopy(this.zzbiI, 0, jArr, 0, length4);
                        }
                        while (length4 < jArr.length - 1) {
                            jArr[length4] = zzrwVar.zzFq();
                            zzrwVar.zzFo();
                            length4++;
                        }
                        jArr[length4] = zzrwVar.zzFq();
                        this.zzbiI = jArr;
                    } else if (iZzFo == 34) {
                        iZzlC = zzrwVar.zzlC(zzrwVar.zzFv());
                        int position2 = zzrwVar.getPosition();
                        int i2 = 0;
                        while (zzrwVar.zzFA() > 0) {
                            zzrwVar.zzFq();
                            i2++;
                        }
                        zzrwVar.zzlE(position2);
                        int length5 = this.zzbiI == null ? 0 : this.zzbiI.length;
                        long[] jArr2 = new long[i2 + length5];
                        if (length5 != 0) {
                            System.arraycopy(this.zzbiI, 0, jArr2, 0, length5);
                        }
                        while (length5 < jArr2.length) {
                            jArr2[length5] = zzrwVar.zzFq();
                            length5++;
                        }
                        this.zzbiI = jArr2;
                    } else if (!zza(zzrwVar, iZzFo)) {
                        return this;
                    }
                    zzrwVar.zzlD(iZzlC);
                } else {
                    int iZzc4 = zzsh.zzc(zzrwVar, 24);
                    int length6 = this.zzbiH == null ? 0 : this.zzbiH.length;
                    int[] iArr2 = new int[iZzc4 + length6];
                    if (length6 != 0) {
                        System.arraycopy(this.zzbiH, 0, iArr2, 0, length6);
                    }
                    while (length6 < iArr2.length - 1) {
                        iArr2[length6] = zzrwVar.zzFr();
                        zzrwVar.zzFo();
                        length6++;
                    }
                    iArr2[length6] = zzrwVar.zzFr();
                    this.zzbiH = iArr2;
                }
            }
        }

        @Override // com.google.android.gms.internal.zzry, com.google.android.gms.internal.zzse
        public void zza(zzrx zzrxVar) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
            if (this.zzbiF != null && this.zzbiF.length > 0) {
                for (int i = 0; i < this.zzbiF.length; i++) {
                    String str = this.zzbiF[i];
                    if (str != null) {
                        zzrxVar.zzb(1, str);
                    }
                }
            }
            if (this.zzbiG != null && this.zzbiG.length > 0) {
                for (int i2 = 0; i2 < this.zzbiG.length; i2++) {
                    String str2 = this.zzbiG[i2];
                    if (str2 != null) {
                        zzrxVar.zzb(2, str2);
                    }
                }
            }
            if (this.zzbiH != null && this.zzbiH.length > 0) {
                for (int i3 = 0; i3 < this.zzbiH.length; i3++) {
                    zzrxVar.zzy(3, this.zzbiH[i3]);
                }
            }
            if (this.zzbiI != null && this.zzbiI.length > 0) {
                for (int i4 = 0; i4 < this.zzbiI.length; i4++) {
                    zzrxVar.zzb(4, this.zzbiI[i4]);
                }
            }
            super.zza(zzrxVar);
        }
    }

    public static final class zzb extends zzry<zzb> {
        public String version;
        public int zzbiJ;
        public String zzbiK;

        public zzb() {
            zzFT();
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof zzb)) {
                return false;
            }
            zzb zzbVar = (zzb) obj;
            if (this.zzbiJ != zzbVar.zzbiJ) {
                return false;
            }
            if (this.zzbiK == null) {
                if (zzbVar.zzbiK != null) {
                    return false;
                }
            } else if (!this.zzbiK.equals(zzbVar.zzbiK)) {
                return false;
            }
            if (this.version == null) {
                if (zzbVar.version != null) {
                    return false;
                }
            } else if (!this.version.equals(zzbVar.version)) {
                return false;
            }
            return (this.zzbik == null || this.zzbik.isEmpty()) ? zzbVar.zzbik == null || zzbVar.zzbik.isEmpty() : this.zzbik.equals(zzbVar.zzbik);
        }

        public int hashCode() {
            int iHashCode = 0;
            int iHashCode2 = 31 * (((((((527 + getClass().getName().hashCode()) * 31) + this.zzbiJ) * 31) + (this.zzbiK == null ? 0 : this.zzbiK.hashCode())) * 31) + (this.version == null ? 0 : this.version.hashCode()));
            if (this.zzbik != null && !this.zzbik.isEmpty()) {
                iHashCode = this.zzbik.hashCode();
            }
            return iHashCode2 + iHashCode;
        }

        @Override // com.google.android.gms.internal.zzry, com.google.android.gms.internal.zzse
        protected int zzB() {
            int iZzB = super.zzB();
            if (this.zzbiJ != 0) {
                iZzB += zzrx.zzA(1, this.zzbiJ);
            }
            if (!this.zzbiK.equals("")) {
                iZzB += zzrx.zzn(2, this.zzbiK);
            }
            return !this.version.equals("") ? iZzB + zzrx.zzn(3, this.version) : iZzB;
        }

        public zzb zzFT() {
            this.zzbiJ = 0;
            this.zzbiK = "";
            this.version = "";
            this.zzbik = null;
            this.zzbiv = -1;
            return this;
        }

        @Override // com.google.android.gms.internal.zzse
        /* renamed from: zzH, reason: merged with bridge method [inline-methods] */
        public zzb zzb(zzrw zzrwVar) throws IOException {
            while (true) {
                int iZzFo = zzrwVar.zzFo();
                if (iZzFo == 0) {
                    return this;
                }
                if (iZzFo == 8) {
                    int iZzFr = zzrwVar.zzFr();
                    switch (iZzFr) {
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
                        case 26:
                            this.zzbiJ = iZzFr;
                            break;
                    }
                } else if (iZzFo == 18) {
                    this.zzbiK = zzrwVar.readString();
                } else if (iZzFo == 26) {
                    this.version = zzrwVar.readString();
                } else if (!zza(zzrwVar, iZzFo)) {
                    return this;
                }
            }
        }

        @Override // com.google.android.gms.internal.zzry, com.google.android.gms.internal.zzse
        public void zza(zzrx zzrxVar) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
            if (this.zzbiJ != 0) {
                zzrxVar.zzy(1, this.zzbiJ);
            }
            if (!this.zzbiK.equals("")) {
                zzrxVar.zzb(2, this.zzbiK);
            }
            if (!this.version.equals("")) {
                zzrxVar.zzb(3, this.version);
            }
            super.zza(zzrxVar);
        }
    }

    public static final class zzc extends zzry<zzc> {
        public byte[] zzbiL;
        public byte[][] zzbiM;
        public boolean zzbiN;

        public zzc() {
            zzFU();
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof zzc)) {
                return false;
            }
            zzc zzcVar = (zzc) obj;
            if (Arrays.equals(this.zzbiL, zzcVar.zzbiL) && zzsc.zza(this.zzbiM, zzcVar.zzbiM) && this.zzbiN == zzcVar.zzbiN) {
                return (this.zzbik == null || this.zzbik.isEmpty()) ? zzcVar.zzbik == null || zzcVar.zzbik.isEmpty() : this.zzbik.equals(zzcVar.zzbik);
            }
            return false;
        }

        public int hashCode() {
            return (31 * (((((((527 + getClass().getName().hashCode()) * 31) + Arrays.hashCode(this.zzbiL)) * 31) + zzsc.zza(this.zzbiM)) * 31) + (this.zzbiN ? 1231 : 1237))) + ((this.zzbik == null || this.zzbik.isEmpty()) ? 0 : this.zzbik.hashCode());
        }

        @Override // com.google.android.gms.internal.zzry, com.google.android.gms.internal.zzse
        protected int zzB() {
            int iZzB = super.zzB();
            if (!Arrays.equals(this.zzbiL, zzsh.zzbiE)) {
                iZzB += zzrx.zzb(1, this.zzbiL);
            }
            if (this.zzbiM != null && this.zzbiM.length > 0) {
                int iZzE = 0;
                int i = 0;
                for (int i2 = 0; i2 < this.zzbiM.length; i2++) {
                    byte[] bArr = this.zzbiM[i2];
                    if (bArr != null) {
                        i++;
                        iZzE += zzrx.zzE(bArr);
                    }
                }
                iZzB = iZzB + iZzE + (1 * i);
            }
            return this.zzbiN ? iZzB + zzrx.zzc(3, this.zzbiN) : iZzB;
        }

        public zzc zzFU() {
            this.zzbiL = zzsh.zzbiE;
            this.zzbiM = zzsh.zzbiD;
            this.zzbiN = false;
            this.zzbik = null;
            this.zzbiv = -1;
            return this;
        }

        @Override // com.google.android.gms.internal.zzse
        /* renamed from: zzI, reason: merged with bridge method [inline-methods] */
        public zzc zzb(zzrw zzrwVar) throws IOException {
            while (true) {
                int iZzFo = zzrwVar.zzFo();
                if (iZzFo == 0) {
                    return this;
                }
                if (iZzFo == 10) {
                    this.zzbiL = zzrwVar.readBytes();
                } else if (iZzFo == 18) {
                    int iZzc = zzsh.zzc(zzrwVar, 18);
                    int length = this.zzbiM == null ? 0 : this.zzbiM.length;
                    byte[][] bArr = new byte[iZzc + length][];
                    if (length != 0) {
                        System.arraycopy(this.zzbiM, 0, bArr, 0, length);
                    }
                    while (length < bArr.length - 1) {
                        bArr[length] = zzrwVar.readBytes();
                        zzrwVar.zzFo();
                        length++;
                    }
                    bArr[length] = zzrwVar.readBytes();
                    this.zzbiM = bArr;
                } else if (iZzFo == 24) {
                    this.zzbiN = zzrwVar.zzFs();
                } else if (!zza(zzrwVar, iZzFo)) {
                    return this;
                }
            }
        }

        @Override // com.google.android.gms.internal.zzry, com.google.android.gms.internal.zzse
        public void zza(zzrx zzrxVar) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
            if (!Arrays.equals(this.zzbiL, zzsh.zzbiE)) {
                zzrxVar.zza(1, this.zzbiL);
            }
            if (this.zzbiM != null && this.zzbiM.length > 0) {
                for (int i = 0; i < this.zzbiM.length; i++) {
                    byte[] bArr = this.zzbiM[i];
                    if (bArr != null) {
                        zzrxVar.zza(2, bArr);
                    }
                }
            }
            if (this.zzbiN) {
                zzrxVar.zzb(3, this.zzbiN);
            }
            super.zza(zzrxVar);
        }
    }

    public static final class zzd extends zzry<zzd> {
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
            zzFV();
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof zzd)) {
                return false;
            }
            zzd zzdVar = (zzd) obj;
            if (this.zzbiO != zzdVar.zzbiO || this.zzbiP != zzdVar.zzbiP) {
                return false;
            }
            if (this.tag == null) {
                if (zzdVar.tag != null) {
                    return false;
                }
            } else if (!this.tag.equals(zzdVar.tag)) {
                return false;
            }
            if (this.zzbiQ != zzdVar.zzbiQ || this.zzbiR != zzdVar.zzbiR || this.zzbiS != zzdVar.zzbiS || !zzsc.equals(this.zzbiT, zzdVar.zzbiT)) {
                return false;
            }
            if (this.zzbiU == null) {
                if (zzdVar.zzbiU != null) {
                    return false;
                }
            } else if (!this.zzbiU.equals(zzdVar.zzbiU)) {
                return false;
            }
            if (!Arrays.equals(this.zzbiV, zzdVar.zzbiV) || !Arrays.equals(this.zzbiW, zzdVar.zzbiW) || !Arrays.equals(this.zzbiX, zzdVar.zzbiX)) {
                return false;
            }
            if (this.zzbiY == null) {
                if (zzdVar.zzbiY != null) {
                    return false;
                }
            } else if (!this.zzbiY.equals(zzdVar.zzbiY)) {
                return false;
            }
            if (this.zzbiZ == null) {
                if (zzdVar.zzbiZ != null) {
                    return false;
                }
            } else if (!this.zzbiZ.equals(zzdVar.zzbiZ)) {
                return false;
            }
            if (this.zzbja != zzdVar.zzbja) {
                return false;
            }
            if (this.zzbjb == null) {
                if (zzdVar.zzbjb != null) {
                    return false;
                }
            } else if (!this.zzbjb.equals(zzdVar.zzbjb)) {
                return false;
            }
            if (Arrays.equals(this.zzbjc, zzdVar.zzbjc) && this.zzbjd == zzdVar.zzbjd && zzsc.equals(this.zzbje, zzdVar.zzbje)) {
                return (this.zzbik == null || this.zzbik.isEmpty()) ? zzdVar.zzbik == null || zzdVar.zzbik.isEmpty() : this.zzbik.equals(zzdVar.zzbik);
            }
            return false;
        }

        public int hashCode() {
            int iHashCode = 0;
            int iHashCode2 = 31 * (((((((((((((((((((((((((((((((((((((527 + getClass().getName().hashCode()) * 31) + ((int) (this.zzbiO ^ (this.zzbiO >>> 32)))) * 31) + ((int) (this.zzbiP ^ (this.zzbiP >>> 32)))) * 31) + (this.tag == null ? 0 : this.tag.hashCode())) * 31) + this.zzbiQ) * 31) + this.zzbiR) * 31) + (this.zzbiS ? 1231 : 1237)) * 31) + zzsc.hashCode(this.zzbiT)) * 31) + (this.zzbiU == null ? 0 : this.zzbiU.hashCode())) * 31) + Arrays.hashCode(this.zzbiV)) * 31) + Arrays.hashCode(this.zzbiW)) * 31) + Arrays.hashCode(this.zzbiX)) * 31) + (this.zzbiY == null ? 0 : this.zzbiY.hashCode())) * 31) + (this.zzbiZ == null ? 0 : this.zzbiZ.hashCode())) * 31) + ((int) (this.zzbja ^ (this.zzbja >>> 32)))) * 31) + (this.zzbjb == null ? 0 : this.zzbjb.hashCode())) * 31) + Arrays.hashCode(this.zzbjc)) * 31) + this.zzbjd) * 31) + zzsc.hashCode(this.zzbje));
            if (this.zzbik != null && !this.zzbik.isEmpty()) {
                iHashCode = this.zzbik.hashCode();
            }
            return iHashCode2 + iHashCode;
        }

        @Override // com.google.android.gms.internal.zzry, com.google.android.gms.internal.zzse
        protected int zzB() {
            int iZzB = super.zzB();
            if (this.zzbiO != 0) {
                iZzB += zzrx.zzd(1, this.zzbiO);
            }
            if (!this.tag.equals("")) {
                iZzB += zzrx.zzn(2, this.tag);
            }
            if (this.zzbiT != null && this.zzbiT.length > 0) {
                int iZzc = iZzB;
                for (int i = 0; i < this.zzbiT.length; i++) {
                    zze zzeVar = this.zzbiT[i];
                    if (zzeVar != null) {
                        iZzc += zzrx.zzc(3, zzeVar);
                    }
                }
                iZzB = iZzc;
            }
            if (!Arrays.equals(this.zzbiV, zzsh.zzbiE)) {
                iZzB += zzrx.zzb(6, this.zzbiV);
            }
            if (this.zzbiY != null) {
                iZzB += zzrx.zzc(7, this.zzbiY);
            }
            if (!Arrays.equals(this.zzbiW, zzsh.zzbiE)) {
                iZzB += zzrx.zzb(8, this.zzbiW);
            }
            if (this.zzbiU != null) {
                iZzB += zzrx.zzc(9, this.zzbiU);
            }
            if (this.zzbiS) {
                iZzB += zzrx.zzc(10, this.zzbiS);
            }
            if (this.zzbiQ != 0) {
                iZzB += zzrx.zzA(11, this.zzbiQ);
            }
            if (this.zzbiR != 0) {
                iZzB += zzrx.zzA(12, this.zzbiR);
            }
            if (!Arrays.equals(this.zzbiX, zzsh.zzbiE)) {
                iZzB += zzrx.zzb(13, this.zzbiX);
            }
            if (!this.zzbiZ.equals("")) {
                iZzB += zzrx.zzn(14, this.zzbiZ);
            }
            if (this.zzbja != 180000) {
                iZzB += zzrx.zze(15, this.zzbja);
            }
            if (this.zzbjb != null) {
                iZzB += zzrx.zzc(16, this.zzbjb);
            }
            if (this.zzbiP != 0) {
                iZzB += zzrx.zzd(17, this.zzbiP);
            }
            if (!Arrays.equals(this.zzbjc, zzsh.zzbiE)) {
                iZzB += zzrx.zzb(18, this.zzbjc);
            }
            if (this.zzbjd != 0) {
                iZzB += zzrx.zzA(19, this.zzbjd);
            }
            if (this.zzbje == null || this.zzbje.length <= 0) {
                return iZzB;
            }
            int iZzlJ = 0;
            for (int i2 = 0; i2 < this.zzbje.length; i2++) {
                iZzlJ += zzrx.zzlJ(this.zzbje[i2]);
            }
            return iZzB + iZzlJ + (2 * this.zzbje.length);
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

        @Override // com.google.android.gms.internal.zzse
        /* renamed from: zzJ, reason: merged with bridge method [inline-methods] */
        public zzd zzb(zzrw zzrwVar) throws IOException {
            zzse zzseVar;
            while (true) {
                int iZzFo = zzrwVar.zzFo();
                switch (iZzFo) {
                    case 0:
                        return this;
                    case 8:
                        this.zzbiO = zzrwVar.zzFq();
                        continue;
                    case 18:
                        this.tag = zzrwVar.readString();
                        continue;
                    case 26:
                        int iZzc = zzsh.zzc(zzrwVar, 26);
                        int length = this.zzbiT == null ? 0 : this.zzbiT.length;
                        zze[] zzeVarArr = new zze[iZzc + length];
                        if (length != 0) {
                            System.arraycopy(this.zzbiT, 0, zzeVarArr, 0, length);
                        }
                        while (length < zzeVarArr.length - 1) {
                            zzeVarArr[length] = new zze();
                            zzrwVar.zza(zzeVarArr[length]);
                            zzrwVar.zzFo();
                            length++;
                        }
                        zzeVarArr[length] = new zze();
                        zzrwVar.zza(zzeVarArr[length]);
                        this.zzbiT = zzeVarArr;
                        continue;
                    case 50:
                        this.zzbiV = zzrwVar.readBytes();
                        continue;
                    case 58:
                        if (this.zzbiY == null) {
                            this.zzbiY = new zza();
                        }
                        zzseVar = this.zzbiY;
                        break;
                    case 66:
                        this.zzbiW = zzrwVar.readBytes();
                        continue;
                    case 74:
                        if (this.zzbiU == null) {
                            this.zzbiU = new zzb();
                        }
                        zzseVar = this.zzbiU;
                        break;
                    case 80:
                        this.zzbiS = zzrwVar.zzFs();
                        continue;
                    case 88:
                        this.zzbiQ = zzrwVar.zzFr();
                        continue;
                    case 96:
                        this.zzbiR = zzrwVar.zzFr();
                        continue;
                    case 106:
                        this.zzbiX = zzrwVar.readBytes();
                        continue;
                    case 114:
                        this.zzbiZ = zzrwVar.readString();
                        continue;
                    case 120:
                        this.zzbja = zzrwVar.zzFu();
                        continue;
                    case 130:
                        if (this.zzbjb == null) {
                            this.zzbjb = new zzc();
                        }
                        zzseVar = this.zzbjb;
                        break;
                    case 136:
                        this.zzbiP = zzrwVar.zzFq();
                        continue;
                    case 146:
                        this.zzbjc = zzrwVar.readBytes();
                        continue;
                    case 152:
                        int iZzFr = zzrwVar.zzFr();
                        switch (iZzFr) {
                            case 0:
                            case 1:
                            case 2:
                                this.zzbjd = iZzFr;
                                break;
                            default:
                                continue;
                        }
                    case 160:
                        int iZzc2 = zzsh.zzc(zzrwVar, 160);
                        int length2 = this.zzbje == null ? 0 : this.zzbje.length;
                        int[] iArr = new int[iZzc2 + length2];
                        if (length2 != 0) {
                            System.arraycopy(this.zzbje, 0, iArr, 0, length2);
                        }
                        while (length2 < iArr.length - 1) {
                            iArr[length2] = zzrwVar.zzFr();
                            zzrwVar.zzFo();
                            length2++;
                        }
                        iArr[length2] = zzrwVar.zzFr();
                        this.zzbje = iArr;
                        continue;
                    case 162:
                        int iZzlC = zzrwVar.zzlC(zzrwVar.zzFv());
                        int position = zzrwVar.getPosition();
                        int i = 0;
                        while (zzrwVar.zzFA() > 0) {
                            zzrwVar.zzFr();
                            i++;
                        }
                        zzrwVar.zzlE(position);
                        int length3 = this.zzbje == null ? 0 : this.zzbje.length;
                        int[] iArr2 = new int[i + length3];
                        if (length3 != 0) {
                            System.arraycopy(this.zzbje, 0, iArr2, 0, length3);
                        }
                        while (length3 < iArr2.length) {
                            iArr2[length3] = zzrwVar.zzFr();
                            length3++;
                        }
                        this.zzbje = iArr2;
                        zzrwVar.zzlD(iZzlC);
                        continue;
                    default:
                        if (!zza(zzrwVar, iZzFo)) {
                            return this;
                        }
                        continue;
                }
                zzrwVar.zza(zzseVar);
            }
        }

        @Override // com.google.android.gms.internal.zzry, com.google.android.gms.internal.zzse
        public void zza(zzrx zzrxVar) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
            if (this.zzbiO != 0) {
                zzrxVar.zzb(1, this.zzbiO);
            }
            if (!this.tag.equals("")) {
                zzrxVar.zzb(2, this.tag);
            }
            if (this.zzbiT != null && this.zzbiT.length > 0) {
                for (int i = 0; i < this.zzbiT.length; i++) {
                    zze zzeVar = this.zzbiT[i];
                    if (zzeVar != null) {
                        zzrxVar.zza(3, zzeVar);
                    }
                }
            }
            if (!Arrays.equals(this.zzbiV, zzsh.zzbiE)) {
                zzrxVar.zza(6, this.zzbiV);
            }
            if (this.zzbiY != null) {
                zzrxVar.zza(7, this.zzbiY);
            }
            if (!Arrays.equals(this.zzbiW, zzsh.zzbiE)) {
                zzrxVar.zza(8, this.zzbiW);
            }
            if (this.zzbiU != null) {
                zzrxVar.zza(9, this.zzbiU);
            }
            if (this.zzbiS) {
                zzrxVar.zzb(10, this.zzbiS);
            }
            if (this.zzbiQ != 0) {
                zzrxVar.zzy(11, this.zzbiQ);
            }
            if (this.zzbiR != 0) {
                zzrxVar.zzy(12, this.zzbiR);
            }
            if (!Arrays.equals(this.zzbiX, zzsh.zzbiE)) {
                zzrxVar.zza(13, this.zzbiX);
            }
            if (!this.zzbiZ.equals("")) {
                zzrxVar.zzb(14, this.zzbiZ);
            }
            if (this.zzbja != 180000) {
                zzrxVar.zzc(15, this.zzbja);
            }
            if (this.zzbjb != null) {
                zzrxVar.zza(16, this.zzbjb);
            }
            if (this.zzbiP != 0) {
                zzrxVar.zzb(17, this.zzbiP);
            }
            if (!Arrays.equals(this.zzbjc, zzsh.zzbiE)) {
                zzrxVar.zza(18, this.zzbjc);
            }
            if (this.zzbjd != 0) {
                zzrxVar.zzy(19, this.zzbjd);
            }
            if (this.zzbje != null && this.zzbje.length > 0) {
                for (int i2 = 0; i2 < this.zzbje.length; i2++) {
                    zzrxVar.zzy(20, this.zzbje[i2]);
                }
            }
            super.zza(zzrxVar);
        }
    }

    public static final class zze extends zzry<zze> {
        private static volatile zze[] zzbjf;
        public String key;
        public String value;

        public zze() {
            zzFX();
        }

        public static zze[] zzFW() {
            if (zzbjf == null) {
                synchronized (zzsc.zzbiu) {
                    if (zzbjf == null) {
                        zzbjf = new zze[0];
                    }
                }
            }
            return zzbjf;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof zze)) {
                return false;
            }
            zze zzeVar = (zze) obj;
            if (this.key == null) {
                if (zzeVar.key != null) {
                    return false;
                }
            } else if (!this.key.equals(zzeVar.key)) {
                return false;
            }
            if (this.value == null) {
                if (zzeVar.value != null) {
                    return false;
                }
            } else if (!this.value.equals(zzeVar.value)) {
                return false;
            }
            return (this.zzbik == null || this.zzbik.isEmpty()) ? zzeVar.zzbik == null || zzeVar.zzbik.isEmpty() : this.zzbik.equals(zzeVar.zzbik);
        }

        public int hashCode() {
            int iHashCode = 0;
            int iHashCode2 = 31 * (((((527 + getClass().getName().hashCode()) * 31) + (this.key == null ? 0 : this.key.hashCode())) * 31) + (this.value == null ? 0 : this.value.hashCode()));
            if (this.zzbik != null && !this.zzbik.isEmpty()) {
                iHashCode = this.zzbik.hashCode();
            }
            return iHashCode2 + iHashCode;
        }

        @Override // com.google.android.gms.internal.zzry, com.google.android.gms.internal.zzse
        protected int zzB() {
            int iZzB = super.zzB();
            if (!this.key.equals("")) {
                iZzB += zzrx.zzn(1, this.key);
            }
            return !this.value.equals("") ? iZzB + zzrx.zzn(2, this.value) : iZzB;
        }

        public zze zzFX() {
            this.key = "";
            this.value = "";
            this.zzbik = null;
            this.zzbiv = -1;
            return this;
        }

        @Override // com.google.android.gms.internal.zzse
        /* renamed from: zzK, reason: merged with bridge method [inline-methods] */
        public zze zzb(zzrw zzrwVar) throws IOException {
            while (true) {
                int iZzFo = zzrwVar.zzFo();
                if (iZzFo == 0) {
                    return this;
                }
                if (iZzFo == 10) {
                    this.key = zzrwVar.readString();
                } else if (iZzFo == 18) {
                    this.value = zzrwVar.readString();
                } else if (!zza(zzrwVar, iZzFo)) {
                    return this;
                }
            }
        }

        @Override // com.google.android.gms.internal.zzry, com.google.android.gms.internal.zzse
        public void zza(zzrx zzrxVar) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
            if (!this.key.equals("")) {
                zzrxVar.zzb(1, this.key);
            }
            if (!this.value.equals("")) {
                zzrxVar.zzb(2, this.value);
            }
            super.zza(zzrxVar);
        }
    }
}
