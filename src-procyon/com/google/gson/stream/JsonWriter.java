// 
// Decompiled by Procyon v0.6.0
// 

package com.google.gson.stream;

import java.io.IOException;
import java.io.Writer;
import java.io.Flushable;
import java.io.Closeable;

public class JsonWriter implements Closeable, Flushable
{
    private static final String[] HTML_SAFE_REPLACEMENT_CHARS;
    private static final String[] REPLACEMENT_CHARS;
    private String deferredName;
    private boolean htmlSafe;
    private String indent;
    private boolean lenient;
    private final Writer out;
    private String separator;
    private boolean serializeNulls;
    private int[] stack;
    private int stackSize;
    
    static {
        REPLACEMENT_CHARS = new String[128];
        for (int i = 0; i <= 31; ++i) {
            JsonWriter.REPLACEMENT_CHARS[i] = String.format("\\u%04x", i);
        }
        JsonWriter.REPLACEMENT_CHARS[34] = "\\\"";
        JsonWriter.REPLACEMENT_CHARS[92] = "\\\\";
        JsonWriter.REPLACEMENT_CHARS[9] = "\\t";
        JsonWriter.REPLACEMENT_CHARS[8] = "\\b";
        JsonWriter.REPLACEMENT_CHARS[10] = "\\n";
        JsonWriter.REPLACEMENT_CHARS[13] = "\\r";
        JsonWriter.REPLACEMENT_CHARS[12] = "\\f";
        (HTML_SAFE_REPLACEMENT_CHARS = JsonWriter.REPLACEMENT_CHARS.clone())[60] = "\\u003c";
        JsonWriter.HTML_SAFE_REPLACEMENT_CHARS[62] = "\\u003e";
        JsonWriter.HTML_SAFE_REPLACEMENT_CHARS[38] = "\\u0026";
        JsonWriter.HTML_SAFE_REPLACEMENT_CHARS[61] = "\\u003d";
        JsonWriter.HTML_SAFE_REPLACEMENT_CHARS[39] = "\\u0027";
    }
    
    public JsonWriter(final Writer out) {
        this.stack = new int[32];
        this.stackSize = 0;
        this.push(6);
        this.separator = ":";
        this.serializeNulls = true;
        if (out == null) {
            throw new NullPointerException("out == null");
        }
        this.out = out;
    }
    
    private void beforeName() throws IOException {
        final int peek = this.peek();
        if (peek == 5) {
            this.out.write(44);
        }
        else if (peek != 3) {
            throw new IllegalStateException("Nesting problem.");
        }
        this.newline();
        this.replaceTop(4);
    }
    
    private void beforeValue(final boolean b) throws IOException {
        switch (this.peek()) {
            default: {
                throw new IllegalStateException("Nesting problem.");
            }
            case 7: {
                if (!this.lenient) {
                    throw new IllegalStateException("JSON must have only one top-level value.");
                }
            }
            case 6: {
                if (!this.lenient && !b) {
                    throw new IllegalStateException("JSON must start with an array or an object.");
                }
                this.replaceTop(7);
                break;
            }
            case 4: {
                this.out.append((CharSequence)this.separator);
                this.replaceTop(5);
                break;
            }
            case 2: {
                this.out.append(',');
                this.newline();
                break;
            }
            case 1: {
                this.replaceTop(2);
                this.newline();
                break;
            }
        }
    }
    
    private JsonWriter close(final int n, final int n2, final String str) throws IOException {
        final int peek = this.peek();
        if (peek != n2 && peek != n) {
            throw new IllegalStateException("Nesting problem.");
        }
        if (this.deferredName != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Dangling name: ");
            sb.append(this.deferredName);
            throw new IllegalStateException(sb.toString());
        }
        --this.stackSize;
        if (peek == n2) {
            this.newline();
        }
        this.out.write(str);
        return this;
    }
    
    private void newline() throws IOException {
        if (this.indent == null) {
            return;
        }
        this.out.write("\n");
        for (int stackSize = this.stackSize, i = 1; i < stackSize; ++i) {
            this.out.write(this.indent);
        }
    }
    
    private JsonWriter open(final int n, final String str) throws IOException {
        this.beforeValue(true);
        this.push(n);
        this.out.write(str);
        return this;
    }
    
    private int peek() {
        if (this.stackSize == 0) {
            throw new IllegalStateException("JsonWriter is closed.");
        }
        return this.stack[this.stackSize - 1];
    }
    
    private void push(final int n) {
        if (this.stackSize == this.stack.length) {
            final int[] stack = new int[this.stackSize * 2];
            System.arraycopy(this.stack, 0, stack, 0, this.stackSize);
            this.stack = stack;
        }
        this.stack[this.stackSize++] = n;
    }
    
    private void replaceTop(final int n) {
        this.stack[this.stackSize - 1] = n;
    }
    
    private void string(final String s) throws IOException {
        String[] array;
        if (this.htmlSafe) {
            array = JsonWriter.HTML_SAFE_REPLACEMENT_CHARS;
        }
        else {
            array = JsonWriter.REPLACEMENT_CHARS;
        }
        this.out.write("\"");
        final int length = s.length();
        int i = 0;
        int n = 0;
        while (i < length) {
            final char char1 = s.charAt(i);
            int n2 = 0;
            Label_0146: {
                String str;
                if (char1 < '\u0080') {
                    if ((str = array[char1]) == null) {
                        n2 = n;
                        break Label_0146;
                    }
                }
                else if (char1 == '\u2028') {
                    str = "\\u2028";
                }
                else {
                    n2 = n;
                    if (char1 != '\u2029') {
                        break Label_0146;
                    }
                    str = "\\u2029";
                }
                if (n < i) {
                    this.out.write(s, n, i - n);
                }
                this.out.write(str);
                n2 = i + 1;
            }
            ++i;
            n = n2;
        }
        if (n < length) {
            this.out.write(s, n, length - n);
        }
        this.out.write("\"");
    }
    
    private void writeDeferredName() throws IOException {
        if (this.deferredName != null) {
            this.beforeName();
            this.string(this.deferredName);
            this.deferredName = null;
        }
    }
    
    public JsonWriter beginArray() throws IOException {
        this.writeDeferredName();
        return this.open(1, "[");
    }
    
    public JsonWriter beginObject() throws IOException {
        this.writeDeferredName();
        return this.open(3, "{");
    }
    
    @Override
    public void close() throws IOException {
        this.out.close();
        final int stackSize = this.stackSize;
        if (stackSize <= 1 && (stackSize != 1 || this.stack[stackSize - 1] == 7)) {
            this.stackSize = 0;
            return;
        }
        throw new IOException("Incomplete document");
    }
    
    public JsonWriter endArray() throws IOException {
        return this.close(1, 2, "]");
    }
    
    public JsonWriter endObject() throws IOException {
        return this.close(3, 5, "}");
    }
    
    @Override
    public void flush() throws IOException {
        if (this.stackSize == 0) {
            throw new IllegalStateException("JsonWriter is closed.");
        }
        this.out.flush();
    }
    
    public final boolean getSerializeNulls() {
        return this.serializeNulls;
    }
    
    public final boolean isHtmlSafe() {
        return this.htmlSafe;
    }
    
    public boolean isLenient() {
        return this.lenient;
    }
    
    public JsonWriter name(final String deferredName) throws IOException {
        if (deferredName == null) {
            throw new NullPointerException("name == null");
        }
        if (this.deferredName != null) {
            throw new IllegalStateException();
        }
        if (this.stackSize == 0) {
            throw new IllegalStateException("JsonWriter is closed.");
        }
        this.deferredName = deferredName;
        return this;
    }
    
    public JsonWriter nullValue() throws IOException {
        if (this.deferredName != null) {
            if (!this.serializeNulls) {
                this.deferredName = null;
                return this;
            }
            this.writeDeferredName();
        }
        this.beforeValue(false);
        this.out.write("null");
        return this;
    }
    
    public final void setHtmlSafe(final boolean htmlSafe) {
        this.htmlSafe = htmlSafe;
    }
    
    public final void setIndent(final String indent) {
        if (indent.length() == 0) {
            this.indent = null;
            this.separator = ":";
        }
        else {
            this.indent = indent;
            this.separator = ": ";
        }
    }
    
    public final void setLenient(final boolean lenient) {
        this.lenient = lenient;
    }
    
    public final void setSerializeNulls(final boolean serializeNulls) {
        this.serializeNulls = serializeNulls;
    }
    
    public JsonWriter value(final double n) throws IOException {
        if (!Double.isNaN(n) && !Double.isInfinite(n)) {
            this.writeDeferredName();
            this.beforeValue(false);
            this.out.append((CharSequence)Double.toString(n));
            return this;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Numeric values must be finite, but was ");
        sb.append(n);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public JsonWriter value(final long i) throws IOException {
        this.writeDeferredName();
        this.beforeValue(false);
        this.out.write(Long.toString(i));
        return this;
    }
    
    public JsonWriter value(final Number obj) throws IOException {
        if (obj == null) {
            return this.nullValue();
        }
        this.writeDeferredName();
        final String string = obj.toString();
        if (!this.lenient && (string.equals("-Infinity") || string.equals("Infinity") || string.equals("NaN"))) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Numeric values must be finite, but was ");
            sb.append(obj);
            throw new IllegalArgumentException(sb.toString());
        }
        this.beforeValue(false);
        this.out.append((CharSequence)string);
        return this;
    }
    
    public JsonWriter value(final String s) throws IOException {
        if (s == null) {
            return this.nullValue();
        }
        this.writeDeferredName();
        this.beforeValue(false);
        this.string(s);
        return this;
    }
    
    public JsonWriter value(final boolean b) throws IOException {
        this.writeDeferredName();
        this.beforeValue(false);
        final Writer out = this.out;
        String str;
        if (b) {
            str = "true";
        }
        else {
            str = "false";
        }
        out.write(str);
        return this;
    }
}
