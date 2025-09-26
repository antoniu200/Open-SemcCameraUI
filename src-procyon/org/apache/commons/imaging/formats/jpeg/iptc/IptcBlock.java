// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg.iptc;

public class IptcBlock
{
    final byte[] blockData;
    final byte[] blockNameBytes;
    public final int blockType;
    
    public IptcBlock(final int blockType, final byte[] blockNameBytes, final byte[] blockData) {
        this.blockData = blockData;
        this.blockNameBytes = blockNameBytes;
        this.blockType = blockType;
    }
    
    public boolean isIPTCBlock() {
        return this.blockType == 1028;
    }
}
