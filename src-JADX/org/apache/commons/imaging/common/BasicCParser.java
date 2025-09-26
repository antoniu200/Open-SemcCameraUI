package org.apache.commons.imaging.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.Map;
import kotlin.text.Typography;
import org.apache.commons.imaging.ImageReadException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class BasicCParser {
    private final PushbackInputStream is;

    public BasicCParser(ByteArrayInputStream byteArrayInputStream) {
        this.is = new PushbackInputStream(byteArrayInputStream);
    }

    public String nextToken() throws IOException, ImageReadException {
        StringBuilder sb = new StringBuilder();
        int i = this.is.read();
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        while (i != -1) {
            if (z) {
                if (i == 92) {
                    sb.append('\\');
                    z3 = !z3;
                } else {
                    if (i == 34) {
                        sb.append(Typography.quote);
                        if (!z3) {
                            return sb.toString();
                        }
                    } else {
                        if (i == 13 || i == 10) {
                            throw new ImageReadException("Unterminated string in XPM file");
                        }
                        sb.append((char) i);
                    }
                    z3 = false;
                }
            } else if (z2) {
                if (Character.isLetterOrDigit(i) || i == 95) {
                    sb.append((char) i);
                } else {
                    this.is.unread(i);
                    return sb.toString();
                }
            } else if (i == 34) {
                sb.append(Typography.quote);
                z = true;
            } else if (Character.isLetterOrDigit(i) || i == 95) {
                sb.append((char) i);
                z2 = true;
            } else {
                if (i == 123 || i == 125 || i == 91 || i == 93 || i == 42 || i == 59 || i == 61 || i == 44) {
                    sb.append((char) i);
                    return sb.toString();
                }
                if (i != 32 && i != 9 && i != 13 && i != 10) {
                    throw new ImageReadException("Unhandled/invalid character '" + ((char) i) + "' found in XPM file");
                }
            }
            i = this.is.read();
        }
        if (z2) {
            return sb.toString();
        }
        if (z) {
            throw new ImageReadException("Unterminated string ends XMP file");
        }
        return null;
    }

    public static ByteArrayOutputStream preprocess(InputStream inputStream, StringBuilder sb, Map<String, String> map) throws IOException, ImageReadException {
        boolean z;
        boolean z2;
        boolean z3;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        boolean z4 = sb == null;
        StringBuilder sb2 = new StringBuilder();
        int i = inputStream.read();
        boolean z5 = z4;
        boolean z6 = false;
        boolean z7 = false;
        boolean z8 = false;
        boolean z9 = false;
        boolean z10 = false;
        boolean z11 = false;
        boolean z12 = false;
        while (i != -1) {
            if (!z6) {
                if (z8) {
                    if (i == 92) {
                        if (z11) {
                            byteArrayOutputStream.write(92);
                            byteArrayOutputStream.write(92);
                            z11 = false;
                        }
                        z11 = true;
                    } else if (i == 39) {
                        if (z11) {
                            byteArrayOutputStream.write(92);
                            z3 = z8;
                            z11 = false;
                        } else {
                            z3 = false;
                        }
                        byteArrayOutputStream.write(39);
                        z8 = z3;
                    } else {
                        if (i == 13 || i == 10) {
                            throw new ImageReadException("Unterminated single quote in file");
                        }
                        if (z11) {
                            byteArrayOutputStream.write(92);
                            z = false;
                        } else {
                            z = z11;
                        }
                        byteArrayOutputStream.write(i);
                        z11 = z;
                    }
                } else if (z10) {
                    if (i == 92) {
                        if (z11) {
                            byteArrayOutputStream.write(92);
                            byteArrayOutputStream.write(92);
                            z11 = false;
                        }
                        z11 = true;
                    } else if (i == 34) {
                        if (z11) {
                            byteArrayOutputStream.write(92);
                            z2 = z10;
                            z11 = false;
                        } else {
                            z2 = false;
                        }
                        byteArrayOutputStream.write(34);
                        z10 = z2;
                    } else {
                        if (i == 13 || i == 10) {
                            throw new ImageReadException("Unterminated string in file");
                        }
                        if (z11) {
                            byteArrayOutputStream.write(92);
                            z = false;
                        } else {
                            z = z11;
                        }
                        byteArrayOutputStream.write(i);
                        z11 = z;
                    }
                } else if (z12) {
                    if (i == 13 || i == 10) {
                        String[] strArr = tokenizeRow(sb2.toString());
                        if (strArr.length < 2 || strArr.length > 3) {
                            throw new ImageReadException("Bad preprocessor directive");
                        }
                        if (!strArr[0].equals("define")) {
                            throw new ImageReadException("Invalid/unsupported preprocessor directive '" + strArr[0] + "'");
                        }
                        map.put(strArr[1], strArr.length == 3 ? strArr[2] : null);
                        sb2.setLength(0);
                        z12 = false;
                    } else {
                        sb2.append((char) i);
                    }
                } else if (i == 47) {
                    if (z7) {
                        byteArrayOutputStream.write(47);
                    }
                    z7 = true;
                } else if (i != 42) {
                    if (i == 39) {
                        if (z7) {
                            byteArrayOutputStream.write(47);
                        }
                        byteArrayOutputStream.write(i);
                        z8 = true;
                    } else if (i == 34) {
                        if (z7) {
                            byteArrayOutputStream.write(47);
                        }
                        byteArrayOutputStream.write(i);
                        z10 = true;
                    } else if (i != 35) {
                        if (z7) {
                            byteArrayOutputStream.write(47);
                        }
                        byteArrayOutputStream.write(i);
                        if (i != 32 && i != 9 && i != 13 && i != 10) {
                            z5 = true;
                        }
                    } else {
                        if (map == null) {
                            throw new ImageReadException("Unexpected preprocessor directive");
                        }
                        z12 = true;
                    }
                    z7 = false;
                } else if (z7) {
                    z6 = true;
                    z7 = false;
                } else {
                    byteArrayOutputStream.write(i);
                }
                i = inputStream.read();
            } else if (i == 42) {
                if (z9 && !z5) {
                    sb.append('*');
                }
                z9 = true;
            } else if (i != 47) {
                if (z9 && !z5) {
                    sb.append('*');
                }
                if (!z5) {
                    sb.append((char) i);
                }
                z9 = false;
            } else if (z9) {
                z6 = false;
                z9 = false;
                z5 = true;
            } else if (!z5) {
                sb.append((char) i);
            }
            i = inputStream.read();
        }
        if (z7) {
            byteArrayOutputStream.write(47);
        }
        if (z9) {
            byteArrayOutputStream.write(42);
        }
        if (z10) {
            throw new ImageReadException("Unterminated string at the end of file");
        }
        if (z6) {
            throw new ImageReadException("Unterminated comment at the end of file");
        }
        return byteArrayOutputStream;
    }

    public static String[] tokenizeRow(String str) {
        String[] strArrSplit = str.split("[ \t]");
        int i = 0;
        for (String str2 : strArrSplit) {
            if (str2 != null && str2.length() > 0) {
                i++;
            }
        }
        String[] strArr = new String[i];
        int i2 = 0;
        for (String str3 : strArrSplit) {
            if (str3 != null && str3.length() > 0) {
                strArr[i2] = str3;
                i2++;
            }
        }
        return strArr;
    }

    public static void unescapeString(StringBuilder sb, String str) throws ImageReadException {
        if (str.length() < 2) {
            throw new ImageReadException("Parsing XPM file failed, string is too short");
        }
        if (str.charAt(0) != '\"' || str.charAt(str.length() - 1) != '\"') {
            throw new ImageReadException("Parsing XPM file failed, string not surrounded by '\"'");
        }
        boolean z = false;
        int i = 1;
        while (i < str.length() - 1) {
            char cCharAt = str.charAt(i);
            if (z) {
                if (cCharAt == '\\') {
                    sb.append('\\');
                } else if (cCharAt == '\"') {
                    sb.append(Typography.quote);
                } else if (cCharAt == '\'') {
                    sb.append('\'');
                } else if (cCharAt == 'x') {
                    int i2 = i + 2;
                    if (i2 >= str.length()) {
                        throw new ImageReadException("Parsing XPM file failed, hex constant in string too short");
                    }
                    try {
                        sb.append((char) Integer.parseInt(Character.toString(str.charAt(i + 1)) + Character.toString(str.charAt(i2)), 16));
                        i = i2;
                    } catch (NumberFormatException e) {
                        throw new ImageReadException("Parsing XPM file failed, hex constant invalid", e);
                    }
                } else if (cCharAt == '0' || cCharAt == '1' || cCharAt == '2' || cCharAt == '3' || cCharAt == '4' || cCharAt == '5' || cCharAt == '6' || cCharAt == '7') {
                    int i3 = i + 1;
                    int i4 = (i3 >= str.length() || '0' > str.charAt(i3) || str.charAt(i3) > '7') ? 1 : 2;
                    int i5 = i + 2;
                    if (i5 < str.length() && '0' <= str.charAt(i5) && str.charAt(i5) <= '7') {
                        i4++;
                    }
                    int iCharAt = 0;
                    for (int i6 = 0; i6 < i4; i6++) {
                        iCharAt = (iCharAt * 8) + (str.charAt(i + i6) - '0');
                    }
                    i += i4 - 1;
                    sb.append((char) iCharAt);
                } else if (cCharAt == 'a') {
                    sb.append((char) 7);
                } else if (cCharAt == 'b') {
                    sb.append('\b');
                } else if (cCharAt == 'f') {
                    sb.append('\f');
                } else if (cCharAt == 'n') {
                    sb.append('\n');
                } else if (cCharAt == 'r') {
                    sb.append('\r');
                } else if (cCharAt == 't') {
                    sb.append('\t');
                } else if (cCharAt == 'v') {
                    sb.append((char) 11);
                } else {
                    throw new ImageReadException("Parsing XPM file failed, invalid escape sequence");
                }
                z = false;
            } else if (cCharAt == '\\') {
                z = true;
            } else {
                if (cCharAt == '\"') {
                    throw new ImageReadException("Parsing XPM file failed, extra '\"' found in string");
                }
                sb.append(cCharAt);
            }
            i++;
        }
        if (z) {
            throw new ImageReadException("Parsing XPM file failed, unterminated escape sequence found in string");
        }
    }
}
