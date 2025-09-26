// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.rgbe;

import java.io.IOException;
import java.io.InputStream;

class InfoHeaderReader
{
    private final InputStream is;
    
    public InfoHeaderReader(final InputStream is) {
        this.is = is;
    }
    
    private char read() throws IOException {
        final int read = this.is.read();
        if (read < 0) {
            throw new IOException("HDR: Unexpected EOF");
        }
        return (char)read;
    }
    
    public String readNextLine() throws IOException {
        final StringBuilder sb = new StringBuilder();
        while (true) {
            final char read = this.read();
            if (read == '\n') {
                break;
            }
            sb.append(read);
        }
        return sb.toString();
    }
}
