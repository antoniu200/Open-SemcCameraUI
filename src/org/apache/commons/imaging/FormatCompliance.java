package org.apache.commons.imaging;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class FormatCompliance {
    private final List<String> comments;
    private final String description;
    private final boolean failOnError;

    public FormatCompliance(String str) {
        this.comments = new ArrayList();
        this.description = str;
        this.failOnError = false;
    }

    public FormatCompliance(String str, boolean z) {
        this.comments = new ArrayList();
        this.description = str;
        this.failOnError = z;
    }

    public static FormatCompliance getDefault() {
        return new FormatCompliance("ignore", false);
    }

    public void addComment(String str) throws ImageReadException {
        this.comments.add(str);
        if (this.failOnError) {
            throw new ImageReadException(str);
        }
    }

    public void addComment(String str, int i) throws ImageReadException {
        addComment(str + ": " + getValueDescription(i));
    }

    public String toString() {
        StringWriter stringWriter = new StringWriter();
        dump(new PrintWriter(stringWriter));
        return stringWriter.getBuffer().toString();
    }

    public void dump() {
        dump(new PrintWriter(new OutputStreamWriter(System.out, Charset.defaultCharset())));
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("Format Compliance: " + this.description);
        if (this.comments.isEmpty()) {
            printWriter.println("\tNo comments.");
        } else {
            int i = 0;
            while (i < this.comments.size()) {
                StringBuilder sb = new StringBuilder();
                sb.append("\t");
                int i2 = i + 1;
                sb.append(i2);
                sb.append(": ");
                sb.append(this.comments.get(i));
                printWriter.println(sb.toString());
                i = i2;
            }
        }
        printWriter.println("");
        printWriter.flush();
    }

    private String getValueDescription(int i) {
        return i + " (" + Integer.toHexString(i) + ")";
    }

    public boolean compareBytes(String str, byte[] bArr, byte[] bArr2) throws ImageReadException {
        if (bArr.length != bArr2.length) {
            addComment(str + ": Unexpected length: (expected: " + bArr.length + ", actual: " + bArr2.length + ")");
            return false;
        }
        for (int i = 0; i < bArr.length; i++) {
            if (bArr[i] != bArr2[i]) {
                addComment(str + ": Unexpected value: (expected: " + getValueDescription(bArr[i]) + ", actual: " + getValueDescription(bArr2[i]) + ")");
                return false;
            }
        }
        return true;
    }

    public boolean checkBounds(String str, int i, int i2, int i3) throws ImageReadException {
        if (i3 >= i && i3 <= i2) {
            return true;
        }
        addComment(str + ": bounds check: " + i + " <= " + i3 + " <= " + i2 + ": false");
        return false;
    }

    public boolean compare(String str, int i, int i2) throws ImageReadException {
        return compare(str, new int[]{i}, i2);
    }

    public boolean compare(String str, int[] iArr, int i) throws ImageReadException {
        for (int i2 : iArr) {
            if (i == i2) {
                return true;
            }
        }
        StringBuilder sb = new StringBuilder(43);
        sb.append(str);
        sb.append(": Unexpected value: (valid: ");
        if (iArr.length > 1) {
            sb.append('{');
        }
        for (int i3 = 0; i3 < iArr.length; i3++) {
            if (i3 > 0) {
                sb.append(", ");
            }
            sb.append(getValueDescription(iArr[i3]));
        }
        if (iArr.length > 1) {
            sb.append('}');
        }
        sb.append(", actual: " + getValueDescription(i) + ")");
        addComment(sb.toString());
        return false;
    }
}
