// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.pnm;

import java.io.IOException;
import java.io.InputStream;

class WhiteSpaceReader
{
    private final InputStream is;
    
    public WhiteSpaceReader(final InputStream is) {
        this.is = is;
    }
    
    private char read() throws IOException {
        final int read = this.is.read();
        if (read < 0) {
            throw new IOException("PNM: Unexpected EOF");
        }
        return (char)read;
    }
    
    public char nextChar() throws IOException {
        char read2;
        char read = read2 = this.read();
        if (read == '#') {
            while (true) {
                read2 = read;
                if (read == '\n' || (read2 = read) == '\r') {
                    break;
                }
                read = this.read();
            }
        }
        return read2;
    }
    
    public String readLine() throws IOException {
        final StringBuilder sb = new StringBuilder();
        while (true) {
            final char read = this.read();
            if (read == '\n' || read == '\r') {
                break;
            }
            sb.append(read);
        }
        String string;
        if (sb.length() > 0) {
            string = sb.toString();
        }
        else {
            string = null;
        }
        return string;
    }
    
    public String readtoWhiteSpace() throws IOException {
        char c;
        for (c = this.nextChar(); Character.isWhitespace(c); c = this.nextChar()) {}
        final StringBuilder sb = new StringBuilder();
        while (!Character.isWhitespace(c)) {
            sb.append(c);
            c = this.nextChar();
        }
        return sb.toString();
    }
}
