// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png.chunks;

import java.io.ByteArrayInputStream;
import org.apache.commons.imaging.common.BinaryFileParser;

public class PngChunk extends BinaryFileParser
{
    public final boolean ancillary;
    private final byte[] bytes;
    public final int chunkType;
    public final int crc;
    public final boolean isPrivate;
    public final int length;
    private final boolean[] propertyBits;
    public final boolean reserved;
    public final boolean safeToCopy;
    
    public PngChunk(int length, final int chunkType, int crc, final byte[] bytes) {
        this.length = length;
        this.chunkType = chunkType;
        this.crc = crc;
        this.bytes = bytes;
        this.propertyBits = new boolean[4];
        length = 24;
        crc = 0;
        while (true) {
            final int n = length;
            boolean b = true;
            if (crc >= 4) {
                break;
            }
            length = n - 8;
            final boolean[] propertyBits = this.propertyBits;
            if ((0xFF & chunkType >> n & 0x20) <= 0) {
                b = false;
            }
            propertyBits[crc] = b;
            ++crc;
        }
        this.ancillary = this.propertyBits[0];
        this.isPrivate = this.propertyBits[1];
        this.reserved = this.propertyBits[2];
        this.safeToCopy = this.propertyBits[3];
    }
    
    public byte[] getBytes() {
        return this.bytes;
    }
    
    protected ByteArrayInputStream getDataStream() {
        return new ByteArrayInputStream(this.getBytes());
    }
    
    public boolean[] getPropertyBits() {
        return this.propertyBits.clone();
    }
}
