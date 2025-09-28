package org.apache.commons.imaging.formats.rgbe;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class InfoHeaderReader {
    private final InputStream is;

    public InfoHeaderReader(InputStream inputStream) {
        this.is = inputStream;
    }

    private char read() throws IOException {
        int i = this.is.read();
        if (i < 0) {
            throw new IOException("HDR: Unexpected EOF");
        }
        return (char) i;
    }

    public String readNextLine() throws IOException {
        StringBuilder sb = new StringBuilder();
        while (true) {
            char c = read();
            if (c != '\n') {
                sb.append(c);
            } else {
                return sb.toString();
            }
        }
    }
}
