// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging;

import java.io.StringWriter;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class FormatCompliance
{
    private final List<String> comments;
    private final String description;
    private final boolean failOnError;
    
    public FormatCompliance(final String description) {
        this.comments = new ArrayList<String>();
        this.description = description;
        this.failOnError = false;
    }
    
    public FormatCompliance(final String description, final boolean failOnError) {
        this.comments = new ArrayList<String>();
        this.description = description;
        this.failOnError = failOnError;
    }
    
    public static FormatCompliance getDefault() {
        return new FormatCompliance("ignore", false);
    }
    
    private String getValueDescription(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(n);
        sb.append(" (");
        sb.append(Integer.toHexString(n));
        sb.append(")");
        return sb.toString();
    }
    
    public void addComment(final String s) throws ImageReadException {
        this.comments.add(s);
        if (this.failOnError) {
            throw new ImageReadException(s);
        }
    }
    
    public void addComment(final String str, final int n) throws ImageReadException {
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(": ");
        sb.append(this.getValueDescription(n));
        this.addComment(sb.toString());
    }
    
    public boolean checkBounds(final String str, final int i, final int j, final int k) throws ImageReadException {
        if (k >= i && k <= j) {
            return true;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(": ");
        sb.append("bounds check: ");
        sb.append(i);
        sb.append(" <= ");
        sb.append(k);
        sb.append(" <= ");
        sb.append(j);
        sb.append(": false");
        this.addComment(sb.toString());
        return false;
    }
    
    public boolean compare(final String s, final int n, final int n2) throws ImageReadException {
        return this.compare(s, new int[] { n }, n2);
    }
    
    public boolean compare(final String str, final int[] array, final int n) throws ImageReadException {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (n == array[i]) {
                return true;
            }
        }
        final StringBuilder sb = new StringBuilder(43);
        sb.append(str);
        sb.append(": Unexpected value: (valid: ");
        if (array.length > 1) {
            sb.append('{');
        }
        for (int j = 0; j < array.length; ++j) {
            if (j > 0) {
                sb.append(", ");
            }
            sb.append(this.getValueDescription(array[j]));
        }
        if (array.length > 1) {
            sb.append('}');
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(", actual: ");
        sb2.append(this.getValueDescription(n));
        sb2.append(")");
        sb.append(sb2.toString());
        this.addComment(sb.toString());
        return false;
    }
    
    public boolean compareBytes(final String s, final byte[] array, final byte[] array2) throws ImageReadException {
        if (array.length != array2.length) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(": ");
            sb.append("Unexpected length: (expected: ");
            sb.append(array.length);
            sb.append(", actual: ");
            sb.append(array2.length);
            sb.append(")");
            this.addComment(sb.toString());
            return false;
        }
        for (int i = 0; i < array.length; ++i) {
            if (array[i] != array2[i]) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(s);
                sb2.append(": ");
                sb2.append("Unexpected value: (expected: ");
                sb2.append(this.getValueDescription(array[i]));
                sb2.append(", actual: ");
                sb2.append(this.getValueDescription(array2[i]));
                sb2.append(")");
                this.addComment(sb2.toString());
                return false;
            }
        }
        return true;
    }
    
    public void dump() {
        this.dump(new PrintWriter(new OutputStreamWriter(System.out, Charset.defaultCharset())));
    }
    
    public void dump(final PrintWriter printWriter) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Format Compliance: ");
        sb.append(this.description);
        printWriter.println(sb.toString());
        if (this.comments.isEmpty()) {
            printWriter.println("\tNo comments.");
        }
        else {
            int j;
            for (int i = 0; i < this.comments.size(); i = j) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("\t");
                j = i + 1;
                sb2.append(j);
                sb2.append(": ");
                sb2.append(this.comments.get(i));
                printWriter.println(sb2.toString());
            }
        }
        printWriter.println("");
        printWriter.flush();
    }
    
    @Override
    public String toString() {
        final StringWriter out = new StringWriter();
        this.dump(new PrintWriter(out));
        return out.getBuffer().toString();
    }
}
