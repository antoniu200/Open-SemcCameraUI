// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.common;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.PushbackInputStream;

public class BasicCParser
{
    private final PushbackInputStream is;
    
    public BasicCParser(final ByteArrayInputStream in) {
        this.is = new PushbackInputStream(in);
    }
    
    public static ByteArrayOutputStream preprocess(final InputStream inputStream, final StringBuilder sb, final Map<String, String> map) throws IOException, ImageReadException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int n;
        if (sb == null) {
            n = 1;
        }
        else {
            n = 0;
        }
        final StringBuilder sb2 = new StringBuilder();
        int i = inputStream.read();
        int n2 = n;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        int n9 = 0;
        while (i != -1) {
            int n18 = 0;
            int n19 = 0;
            Label_1224: {
                int n10 = 0;
                int n11 = 0;
                int n12 = 0;
                int n13 = 0;
                int n14 = 0;
                int n15 = 0;
                Label_0115: {
                    if (n3 != 0) {
                        if (i == 42) {
                            if (n6 != 0 && n2 == 0) {
                                sb.append('*');
                            }
                            n10 = 1;
                            n11 = n8;
                            n12 = n2;
                            n13 = n7;
                            n14 = n5;
                            n15 = n3;
                        }
                        else if (i == 47) {
                            if (n6 != 0) {
                                n15 = 0;
                                n10 = 0;
                                n12 = 1;
                                n14 = n5;
                                n13 = n7;
                                n11 = n8;
                            }
                            else {
                                n15 = n3;
                                n14 = n5;
                                n10 = n6;
                                n13 = n7;
                                n12 = n2;
                                n11 = n8;
                                if (n2 == 0) {
                                    sb.append((char)i);
                                    n15 = n3;
                                    n14 = n5;
                                    n10 = n6;
                                    n13 = n7;
                                    n12 = n2;
                                    n11 = n8;
                                }
                            }
                        }
                        else {
                            if (n6 != 0 && n2 == 0) {
                                sb.append('*');
                            }
                            if (n2 == 0) {
                                sb.append((char)i);
                            }
                            n10 = 0;
                            n15 = n3;
                            n14 = n5;
                            n13 = n7;
                            n12 = n2;
                            n11 = n8;
                        }
                    }
                    else {
                        Label_0486: {
                            Label_0357: {
                                if (n5 != 0) {
                                    if (i == 92) {
                                        if (n8 == 0) {
                                            break Label_0357;
                                        }
                                        byteArrayOutputStream.write(92);
                                        byteArrayOutputStream.write(92);
                                    }
                                    else {
                                        if (i == 39) {
                                            int n16;
                                            if (n8 != 0) {
                                                byteArrayOutputStream.write(92);
                                                n16 = n5;
                                                n8 = 0;
                                            }
                                            else {
                                                n16 = 0;
                                            }
                                            byteArrayOutputStream.write(39);
                                            n15 = n3;
                                            n14 = n16;
                                            n10 = n6;
                                            n13 = n7;
                                            n12 = n2;
                                            n11 = n8;
                                            break Label_0115;
                                        }
                                        if (i != 13 && i != 10) {
                                            if (n8 != 0) {
                                                byteArrayOutputStream.write(92);
                                                n8 = 0;
                                            }
                                            byteArrayOutputStream.write(i);
                                            break Label_0486;
                                        }
                                        throw new ImageReadException("Unterminated single quote in file");
                                    }
                                }
                                else if (n7 != 0) {
                                    if (i == 92) {
                                        if (n8 == 0) {
                                            break Label_0357;
                                        }
                                        byteArrayOutputStream.write(92);
                                        byteArrayOutputStream.write(92);
                                    }
                                    else {
                                        if (i == 34) {
                                            int n17;
                                            if (n8 != 0) {
                                                byteArrayOutputStream.write(92);
                                                n17 = n7;
                                                n8 = 0;
                                            }
                                            else {
                                                n17 = 0;
                                            }
                                            byteArrayOutputStream.write(34);
                                            n15 = n3;
                                            n14 = n5;
                                            n10 = n6;
                                            n13 = n17;
                                            n12 = n2;
                                            n11 = n8;
                                            break Label_0115;
                                        }
                                        if (i != 13 && i != 10) {
                                            if (n8 != 0) {
                                                byteArrayOutputStream.write(92);
                                                n8 = 0;
                                            }
                                            byteArrayOutputStream.write(i);
                                            break Label_0486;
                                        }
                                        throw new ImageReadException("Unterminated string in file");
                                    }
                                }
                                else if (n9 != 0) {
                                    if (i != 13 && i != 10) {
                                        sb2.append((char)i);
                                        n15 = n3;
                                        n14 = n5;
                                        n10 = n6;
                                        n13 = n7;
                                        n12 = n2;
                                        n11 = n8;
                                        break Label_0115;
                                    }
                                    final String[] tokenizeRow = tokenizeRow(sb2.toString());
                                    if (tokenizeRow.length < 2 || tokenizeRow.length > 3) {
                                        throw new ImageReadException("Bad preprocessor directive");
                                    }
                                    if (!tokenizeRow[0].equals("define")) {
                                        final StringBuilder sb3 = new StringBuilder();
                                        sb3.append("Invalid/unsupported preprocessor directive '");
                                        sb3.append(tokenizeRow[0]);
                                        sb3.append("'");
                                        throw new ImageReadException(sb3.toString());
                                    }
                                    final String s = tokenizeRow[1];
                                    String s2;
                                    if (tokenizeRow.length == 3) {
                                        s2 = tokenizeRow[2];
                                    }
                                    else {
                                        s2 = null;
                                    }
                                    map.put(s, s2);
                                    sb2.setLength();
                                    n18 = 0;
                                    n19 = n4;
                                    break Label_1224;
                                }
                                else {
                                    if (i == 47) {
                                        if (n4 != 0) {
                                            byteArrayOutputStream.write(47);
                                        }
                                        n19 = 1;
                                        n18 = n9;
                                        break Label_1224;
                                    }
                                    int n20;
                                    int n21;
                                    int n22;
                                    int n23;
                                    if (i == 42) {
                                        if (n4 == 0) {
                                            byteArrayOutputStream.write(i);
                                            n19 = n4;
                                            n18 = n9;
                                            break Label_1224;
                                        }
                                        n20 = 1;
                                        n21 = n2;
                                        n22 = n7;
                                        n23 = n5;
                                    }
                                    else if (i == 39) {
                                        if (n4 != 0) {
                                            byteArrayOutputStream.write(47);
                                        }
                                        byteArrayOutputStream.write(i);
                                        n23 = 1;
                                        n20 = n3;
                                        n22 = n7;
                                        n21 = n2;
                                    }
                                    else if (i == 34) {
                                        if (n4 != 0) {
                                            byteArrayOutputStream.write(47);
                                        }
                                        byteArrayOutputStream.write(i);
                                        n22 = 1;
                                        n20 = n3;
                                        n23 = n5;
                                        n21 = n2;
                                    }
                                    else if (i == 35) {
                                        if (map == null) {
                                            throw new ImageReadException("Unexpected preprocessor directive");
                                        }
                                        n18 = 1;
                                        n19 = n4;
                                        break Label_1224;
                                    }
                                    else {
                                        if (n4 != 0) {
                                            byteArrayOutputStream.write(47);
                                        }
                                        byteArrayOutputStream.write(i);
                                        n20 = n3;
                                        n23 = n5;
                                        n22 = n7;
                                        n21 = n2;
                                        if (i != 32) {
                                            n20 = n3;
                                            n23 = n5;
                                            n22 = n7;
                                            n21 = n2;
                                            if (i != 9) {
                                                n20 = n3;
                                                n23 = n5;
                                                n22 = n7;
                                                n21 = n2;
                                                if (i != 13) {
                                                    n20 = n3;
                                                    n23 = n5;
                                                    n22 = n7;
                                                    n21 = n2;
                                                    if (i != 10) {
                                                        n21 = 1;
                                                        n20 = n3;
                                                        n23 = n5;
                                                        n22 = n7;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    final int n24 = 0;
                                    n3 = n20;
                                    n19 = n24;
                                    n5 = n23;
                                    n7 = n22;
                                    n2 = n21;
                                    n18 = n9;
                                    break Label_1224;
                                }
                                n11 = 0;
                                n15 = n3;
                                n14 = n5;
                                n10 = n6;
                                n13 = n7;
                                n12 = n2;
                                break Label_0115;
                            }
                            n11 = 1;
                            n15 = n3;
                            n14 = n5;
                            n10 = n6;
                            n13 = n7;
                            n12 = n2;
                            break Label_0115;
                        }
                        n15 = n3;
                        n14 = n5;
                        n10 = n6;
                        n13 = n7;
                        n12 = n2;
                        n11 = n8;
                    }
                }
                n3 = n15;
                n19 = n4;
                n5 = n14;
                n6 = n10;
                n7 = n13;
                n2 = n12;
                n8 = n11;
                n18 = n9;
            }
            i = inputStream.read();
            n4 = n19;
            n9 = n18;
        }
        if (n4 != 0) {
            byteArrayOutputStream.write(47);
        }
        if (n6 != 0) {
            byteArrayOutputStream.write(42);
        }
        if (n7 != 0) {
            throw new ImageReadException("Unterminated string at the end of file");
        }
        if (n3 != 0) {
            throw new ImageReadException("Unterminated comment at the end of file");
        }
        return byteArrayOutputStream;
    }
    
    public static String[] tokenizeRow(final String s) {
        final String[] split = s.split("[ \t]");
        final int length = split.length;
        final int n = 0;
        int i = 0;
        int n2 = 0;
        while (i < length) {
            final String s2 = split[i];
            int n3 = n2;
            if (s2 != null) {
                n3 = n2;
                if (s2.length() > 0) {
                    n3 = n2 + 1;
                }
            }
            ++i;
            n2 = n3;
        }
        final String[] array = new String[n2];
        final int length2 = split.length;
        int n4 = 0;
        int n5;
        for (int j = n; j < length2; ++j, n4 = n5) {
            final String s3 = split[j];
            n5 = n4;
            if (s3 != null) {
                n5 = n4;
                if (s3.length() > 0) {
                    array[n4] = s3;
                    n5 = n4 + 1;
                }
            }
        }
        return array;
    }
    
    public static void unescapeString(final StringBuilder sb, final String s) throws ImageReadException {
        if (s.length() < 2) {
            throw new ImageReadException("Parsing XPM file failed, string is too short");
        }
        if (s.charAt(0) != '\"' || s.charAt(s.length() - 1) != '\"') {
            throw new ImageReadException("Parsing XPM file failed, string not surrounded by '\"'");
        }
        int n = 0;
        for (int i = 1; i < s.length() - 1; ++i) {
            final char char1 = s.charAt(i);
            if (n != 0) {
                Label_0569: {
                    if (char1 == '\\') {
                        sb.append('\\');
                    }
                    else if (char1 == '\"') {
                        sb.append('\"');
                    }
                    else if (char1 == '\'') {
                        sb.append('\'');
                    }
                    else {
                        if (char1 == 'x') {
                            final int index = i + 2;
                            if (index >= s.length()) {
                                throw new ImageReadException("Parsing XPM file failed, hex constant in string too short");
                            }
                            final char char2 = s.charAt(i + 1);
                            final char char3 = s.charAt(index);
                            try {
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append(Character.toString(char2));
                                sb2.append(Character.toString(char3));
                                sb.append((char)Integer.parseInt(sb2.toString(), 16));
                                i = index;
                                break Label_0569;
                            }
                            catch (final NumberFormatException ex) {
                                throw new ImageReadException("Parsing XPM file failed, hex constant invalid", ex);
                            }
                        }
                        if (char1 != '0' && char1 != '1' && char1 != '2' && char1 != '3' && char1 != '4' && char1 != '5' && char1 != '6' && char1 != '7') {
                            if (char1 == 'a') {
                                sb.append('\u0007');
                            }
                            else if (char1 == 'b') {
                                sb.append('\b');
                            }
                            else if (char1 == 'f') {
                                sb.append('\f');
                            }
                            else if (char1 == 'n') {
                                sb.append('\n');
                            }
                            else if (char1 == 'r') {
                                sb.append('\r');
                            }
                            else if (char1 == 't') {
                                sb.append('\t');
                            }
                            else {
                                if (char1 != 'v') {
                                    throw new ImageReadException("Parsing XPM file failed, invalid escape sequence");
                                }
                                sb.append('\u000b');
                            }
                        }
                        else {
                            final int n2 = i + 1;
                            int n3;
                            if (n2 < s.length() && '0' <= s.charAt(n2) && s.charAt(n2) <= '7') {
                                n3 = 2;
                            }
                            else {
                                n3 = 1;
                            }
                            final int n4 = i + 2;
                            int n5 = n3;
                            if (n4 < s.length()) {
                                n5 = n3;
                                if ('0' <= s.charAt(n4)) {
                                    n5 = n3;
                                    if (s.charAt(n4) <= '7') {
                                        n5 = n3 + 1;
                                    }
                                }
                            }
                            int j = 0;
                            int n6 = 0;
                            while (j < n5) {
                                n6 = n6 * 8 + (s.charAt(i + j) - '0');
                                ++j;
                            }
                            i += n5 - 1;
                            sb.append((char)n6);
                        }
                    }
                }
                n = 0;
            }
            else if (char1 == '\\') {
                n = 1;
            }
            else {
                if (char1 == '\"') {
                    throw new ImageReadException("Parsing XPM file failed, extra '\"' found in string");
                }
                sb.append(char1);
            }
        }
        if (n != 0) {
            throw new ImageReadException("Parsing XPM file failed, unterminated escape sequence found in string");
        }
    }
    
    public String nextToken() throws IOException, ImageReadException {
        final StringBuilder sb = new StringBuilder();
        int i = this.is.read();
        int n = 0;
        int n3;
        int n2 = n3 = 0;
        while (i != -1) {
            int n4;
            int n5;
            int n6;
            if (n != 0) {
                if (i == 92) {
                    sb.append('\\');
                    n4 = (n3 ^ 0x1);
                    n5 = n;
                    n6 = n2;
                }
                else {
                    if (i == 34) {
                        sb.append('\"');
                        if (n3 == 0) {
                            return sb.toString();
                        }
                    }
                    else {
                        if (i == 13 || i == 10) {
                            throw new ImageReadException("Unterminated string in XPM file");
                        }
                        sb.append((char)i);
                    }
                    n4 = 0;
                    n5 = n;
                    n6 = n2;
                }
            }
            else if (n2 != 0) {
                if (!Character.isLetterOrDigit(i) && i != 95) {
                    this.is.unread(i);
                    return sb.toString();
                }
                sb.append((char)i);
                n5 = n;
                n6 = n2;
                n4 = n3;
            }
            else if (i == 34) {
                sb.append('\"');
                n5 = 1;
                n6 = n2;
                n4 = n3;
            }
            else if (!Character.isLetterOrDigit(i) && i != 95) {
                if (i == 123 || i == 125 || i == 91 || i == 93 || i == 42 || i == 59 || i == 61 || i == 44) {
                    sb.append((char)i);
                    return sb.toString();
                }
                n5 = n;
                n6 = n2;
                n4 = n3;
                if (i != 32) {
                    n5 = n;
                    n6 = n2;
                    n4 = n3;
                    if (i != 9) {
                        n5 = n;
                        n6 = n2;
                        n4 = n3;
                        if (i != 13) {
                            if (i != 10) {
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("Unhandled/invalid character '");
                                sb2.append((char)i);
                                sb2.append("' found in XPM file");
                                throw new ImageReadException(sb2.toString());
                            }
                            n5 = n;
                            n6 = n2;
                            n4 = n3;
                        }
                    }
                }
            }
            else {
                sb.append((char)i);
                n6 = 1;
                n4 = n3;
                n5 = n;
            }
            i = this.is.read();
            n = n5;
            n2 = n6;
            n3 = n4;
        }
        if (n2 != 0) {
            return sb.toString();
        }
        if (n != 0) {
            throw new ImageReadException("Unterminated string ends XMP file");
        }
        return null;
    }
}
