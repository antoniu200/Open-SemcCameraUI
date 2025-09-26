// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.io;

import java.io.ByteArrayOutputStream;
import kotlin.collections.ByteIterator;
import kotlin.jvm.internal.Intrinsics;
import kotlin.TypeCastException;
import java.io.ByteArrayInputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.BufferedWriter;
import kotlin.text.Charsets;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import kotlin.internal.InlineOnly;
import java.io.BufferedInputStream;
import org.jetbrains.annotations.NotNull;
import java.io.InputStream;
import kotlin.jvm.JvmName;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000Z\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0017\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0017\u0010\u0000\u001a\u00020\u0005*\u00020\u00062\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0017\u0010\u0007\u001a\u00020\b*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u0017\u0010\u000b\u001a\u00020\f*\u00020\u00062\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u0017\u0010\r\u001a\u00020\u000e*\u00020\u000f2\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u001c\u0010\u0010\u001a\u00020\u0011*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00062\b\b\u0002\u0010\u0003\u001a\u00020\u0004\u001a\r\u0010\u0013\u001a\u00020\u000e*\u00020\u0014H\u0087\b\u001a\u001d\u0010\u0013\u001a\u00020\u000e*\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u0004H\u0087\b\u001a\r\u0010\u0017\u001a\u00020\u0018*\u00020\u0001H\u0086\u0002\u001a\u0014\u0010\u0019\u001a\u00020\u0014*\u00020\u00022\b\b\u0002\u0010\u001a\u001a\u00020\u0004\u001a\u0017\u0010\u001b\u001a\u00020\u001c*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u0017\u0010\u001d\u001a\u00020\u001e*\u00020\u00062\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b¨\u0006\u001f" }, d2 = { "buffered", "Ljava/io/BufferedInputStream;", "Ljava/io/InputStream;", "bufferSize", "", "Ljava/io/BufferedOutputStream;", "Ljava/io/OutputStream;", "bufferedReader", "Ljava/io/BufferedReader;", "charset", "Ljava/nio/charset/Charset;", "bufferedWriter", "Ljava/io/BufferedWriter;", "byteInputStream", "Ljava/io/ByteArrayInputStream;", "", "copyTo", "", "out", "inputStream", "", "offset", "length", "iterator", "Lkotlin/collections/ByteIterator;", "readBytes", "estimatedSize", "reader", "Ljava/io/InputStreamReader;", "writer", "Ljava/io/OutputStreamWriter;", "kotlin-stdlib" }, k = 2, mv = { 1, 1, 10 })
@JvmName(name = "ByteStreamsKt")
public final class ByteStreamsKt
{
    @InlineOnly
    private static final BufferedInputStream buffered(@NotNull final InputStream in, final int size) {
        BufferedInputStream bufferedInputStream;
        if (in instanceof BufferedInputStream) {
            bufferedInputStream = (BufferedInputStream)in;
        }
        else {
            bufferedInputStream = new BufferedInputStream(in, size);
        }
        return bufferedInputStream;
    }
    
    @InlineOnly
    private static final BufferedOutputStream buffered(@NotNull final OutputStream out, final int size) {
        BufferedOutputStream bufferedOutputStream;
        if (out instanceof BufferedOutputStream) {
            bufferedOutputStream = (BufferedOutputStream)out;
        }
        else {
            bufferedOutputStream = new BufferedOutputStream(out, size);
        }
        return bufferedOutputStream;
    }
    
    @InlineOnly
    private static final BufferedReader bufferedReader(@NotNull final InputStream in, final Charset cs) {
        final Reader in2 = new InputStreamReader(in, cs);
        BufferedReader bufferedReader;
        if (in2 instanceof BufferedReader) {
            bufferedReader = (BufferedReader)in2;
        }
        else {
            bufferedReader = new BufferedReader(in2, 8192);
        }
        return bufferedReader;
    }
    
    @InlineOnly
    private static final BufferedWriter bufferedWriter(@NotNull final OutputStream out, final Charset cs) {
        final Writer out2 = new OutputStreamWriter(out, cs);
        BufferedWriter bufferedWriter;
        if (out2 instanceof BufferedWriter) {
            bufferedWriter = (BufferedWriter)out2;
        }
        else {
            bufferedWriter = new BufferedWriter(out2, 8192);
        }
        return bufferedWriter;
    }
    
    @InlineOnly
    private static final ByteArrayInputStream byteInputStream(@NotNull final String s, final Charset charset) {
        if (s == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        final byte[] bytes = s.getBytes(charset);
        Intrinsics.checkExpressionValueIsNotNull(bytes, "(this as java.lang.String).getBytes(charset)");
        return new ByteArrayInputStream(bytes);
    }
    
    public static final long copyTo(@NotNull final InputStream inputStream, @NotNull final OutputStream outputStream, int i) {
        Intrinsics.checkParameterIsNotNull(inputStream, "$receiver");
        Intrinsics.checkParameterIsNotNull(outputStream, "out");
        final byte[] b = new byte[i];
        i = inputStream.read(b);
        long n = 0L;
        while (i >= 0) {
            outputStream.write(b, 0, i);
            n += i;
            i = inputStream.read(b);
        }
        return n;
    }
    
    @InlineOnly
    private static final ByteArrayInputStream inputStream(@NotNull final byte[] buf) {
        return new ByteArrayInputStream(buf);
    }
    
    @InlineOnly
    private static final ByteArrayInputStream inputStream(@NotNull final byte[] buf, final int offset, final int length) {
        return new ByteArrayInputStream(buf, offset, length);
    }
    
    @NotNull
    public static final ByteIterator iterator(@NotNull final BufferedInputStream bufferedInputStream) {
        Intrinsics.checkParameterIsNotNull(bufferedInputStream, "$receiver");
        return (ByteIterator)new ByteStreamsKt$iterator.ByteStreamsKt$iterator$1(bufferedInputStream);
    }
    
    @NotNull
    public static final byte[] readBytes(@NotNull final InputStream inputStream, final int a) {
        Intrinsics.checkParameterIsNotNull(inputStream, "$receiver");
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(Math.max(a, inputStream.available()));
        copyTo$default(inputStream, (OutputStream)byteArrayOutputStream, 0, 2, (Object)null);
        final byte[] byteArray = byteArrayOutputStream.toByteArray();
        Intrinsics.checkExpressionValueIsNotNull(byteArray, "buffer.toByteArray()");
        return byteArray;
    }
    
    @InlineOnly
    private static final InputStreamReader reader(@NotNull final InputStream in, final Charset cs) {
        return new InputStreamReader(in, cs);
    }
    
    @InlineOnly
    private static final OutputStreamWriter writer(@NotNull final OutputStream out, final Charset cs) {
        return new OutputStreamWriter(out, cs);
    }
}
