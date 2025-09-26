// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png;

import java.io.UnsupportedEncodingException;
import org.apache.commons.imaging.common.BinaryFunctions;

public enum ChunkType
{
    private static final ChunkType[] $VALUES;
    
    IDAT, 
    IEND, 
    IHDR, 
    PLTE, 
    bKGD, 
    cHRM, 
    gAMA, 
    hIST, 
    iCCP, 
    iTXt, 
    pHYs, 
    sBIT, 
    sPLT, 
    sRGB, 
    tEXt, 
    tIME, 
    tRNS, 
    zTXt;
    
    final byte[] array;
    final int value;
    
    static {
        $VALUES = new ChunkType[] { ChunkType.IHDR, ChunkType.PLTE, ChunkType.IDAT, ChunkType.IEND, ChunkType.tRNS, ChunkType.cHRM, ChunkType.gAMA, ChunkType.iCCP, ChunkType.sBIT, ChunkType.sRGB, ChunkType.tEXt, ChunkType.zTXt, ChunkType.iTXt, ChunkType.bKGD, ChunkType.hIST, ChunkType.pHYs, ChunkType.sPLT, ChunkType.tIME };
    }
    
    private ChunkType() {
        final char[] charArray = this.name().toCharArray();
        try {
            this.array = this.name().getBytes("UTF-8");
            this.value = BinaryFunctions.charsToQuad(charArray[0], charArray[1], charArray[2], charArray[3]);
        }
        catch (final UnsupportedEncodingException cause) {
            throw new RuntimeException(cause);
        }
    }
}
