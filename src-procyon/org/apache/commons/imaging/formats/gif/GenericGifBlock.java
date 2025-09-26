// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.gif;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

class GenericGifBlock extends GifBlock
{
    final List<byte[]> subblocks;
    
    public GenericGifBlock(final int n, final List<byte[]> subblocks) {
        super(n);
        this.subblocks = subblocks;
    }
    
    public byte[] appendSubBlocks() throws IOException {
        return this.appendSubBlocks(false);
    }
    
    public byte[] appendSubBlocks(final boolean b) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i = 0; i < this.subblocks.size(); ++i) {
            final byte[] b2 = this.subblocks.get(i);
            if (b && i > 0) {
                byteArrayOutputStream.write(b2.length);
            }
            byteArrayOutputStream.write(b2);
        }
        return byteArrayOutputStream.toByteArray();
    }
}
