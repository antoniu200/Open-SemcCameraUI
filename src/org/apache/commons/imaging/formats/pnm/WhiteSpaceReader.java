package org.apache.commons.imaging.formats.pnm;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class WhiteSpaceReader {
    private final InputStream is;

    public WhiteSpaceReader(InputStream inputStream) {
        this.is = inputStream;
    }

    private char read() throws IOException {
        int i = this.is.read();
        if (i < 0) {
            throw new IOException("PNM: Unexpected EOF");
        }
        return (char) i;
    }

    public char nextChar() throws IOException {
        char c = read();
        if (c == '#') {
            while (c != '\n' && c != '\r') {
                c = read();
            }
        }
        return c;
    }

    public String readtoWhiteSpace() throws IOException {
        char cNextChar = nextChar();
        while (Character.isWhitespace(cNextChar)) {
            cNextChar = nextChar();
        }
        StringBuilder sb = new StringBuilder();
        while (!Character.isWhitespace(cNextChar)) {
            sb.append(cNextChar);
            cNextChar = nextChar();
        }
        return sb.toString();
    }

    public String readLine() throws IOException {
        StringBuilder sb = new StringBuilder();
        while (true) {
            char c = read();
            if (c == '\n' || c == '\r') {
                break;
            }
            sb.append(c);
        }
        if (sb.length() > 0) {
            return sb.toString();
        }
        return null;
    }
}
