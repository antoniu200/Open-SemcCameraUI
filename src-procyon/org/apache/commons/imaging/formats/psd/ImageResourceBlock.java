// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.psd;

import java.io.UnsupportedEncodingException;
import org.apache.commons.imaging.util.Debug;

class ImageResourceBlock
{
    final byte[] data;
    final int id;
    final byte[] nameData;
    
    ImageResourceBlock(final int id, final byte[] nameData, final byte[] data) {
        this.id = id;
        this.nameData = nameData;
        this.data = data;
    }
    
    String getName() throws UnsupportedEncodingException {
        final StringBuilder sb = new StringBuilder();
        sb.append("getName: ");
        sb.append(this.nameData.length);
        Debug.debug(sb.toString());
        return new String(this.nameData, "ISO-8859-1");
    }
}
