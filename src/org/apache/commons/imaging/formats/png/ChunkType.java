package org.apache.commons.imaging.formats.png;

import java.io.UnsupportedEncodingException;
import org.apache.commons.imaging.common.BinaryFunctions;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum ChunkType {
    IHDR,
    PLTE,
    IDAT,
    IEND,
    tRNS,
    cHRM,
    gAMA,
    iCCP,
    sBIT,
    sRGB,
    tEXt,
    zTXt,
    iTXt,
    bKGD,
    hIST,
    pHYs,
    sPLT,
    tIME;

    final byte[] array;
    final int value;

    ChunkType() {
        char[] charArray = name().toCharArray();
        try {
            this.array = name().getBytes("UTF-8");
            this.value = BinaryFunctions.charsToQuad(charArray[0], charArray[1], charArray[2], charArray[3]);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
