// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.io;

import java.nio.charset.CoderResult;
import java.nio.ByteBuffer;
import org.jetbrains.annotations.Nullable;
import java.io.InputStream;
import kotlin.jvm.internal.Intrinsics;
import kotlin.internal.InlineOnly;
import java.nio.charset.CharsetDecoder;
import java.nio.Buffer;
import org.jetbrains.annotations.NotNull;
import java.nio.CharBuffer;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.PropertyReference0;
import kotlin.jvm.internal.PropertyReference0Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.Lazy;
import kotlin.reflect.KProperty;
import kotlin.jvm.JvmName;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000d\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0010\u000b\n\u0002\u0010\u0005\n\u0002\u0010\f\n\u0002\u0010\u0019\n\u0002\u0010\u0006\n\u0002\u0010\u0007\n\u0002\u0010\t\n\u0002\u0010\n\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0087\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\rH\u0087\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u000eH\u0087\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u000fH\u0087\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0010H\u0087\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0011H\u0087\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0012H\u0087\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0013H\u0087\b\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0014H\u0087\b\u001a\t\u0010\u0015\u001a\u00020\nH\u0087\b\u001a\u0013\u0010\u0015\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0087\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\rH\u0087\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u000eH\u0087\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u000fH\u0087\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0010H\u0087\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0011H\u0087\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0012H\u0087\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0013H\u0087\b\u001a\u0011\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0014H\u0087\b\u001a\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017\u001a\u001a\u0010\u0016\u001a\u0004\u0018\u00010\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0003\u001a\u00020\u0004H\u0000\u001a\f\u0010\u001a\u001a\u00020\r*\u00020\u001bH\u0002\u001a\f\u0010\u001c\u001a\u00020\u000f*\u00020\u001bH\u0002\u001a\f\u0010\u001d\u001a\u00020\n*\u00020\u001eH\u0002\u001a$\u0010\u001f\u001a\u00020\r*\u00020\u00042\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\u001b2\u0006\u0010#\u001a\u00020\rH\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u001b\u0010\u0003\u001a\u00020\u00048BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006¨\u0006$" }, d2 = { "BUFFER_SIZE", "", "LINE_SEPARATOR_MAX_LENGTH", "decoder", "Ljava/nio/charset/CharsetDecoder;", "getDecoder", "()Ljava/nio/charset/CharsetDecoder;", "decoder$delegate", "Lkotlin/Lazy;", "print", "", "message", "", "", "", "", "", "", "", "", "", "println", "readLine", "", "inputStream", "Ljava/io/InputStream;", "containsLineSeparator", "Ljava/nio/CharBuffer;", "dequeue", "flipBack", "Ljava/nio/Buffer;", "tryDecode", "byteBuffer", "Ljava/nio/ByteBuffer;", "charBuffer", "isEndOfStream", "kotlin-stdlib" }, k = 2, mv = { 1, 1, 10 })
@JvmName(name = "ConsoleKt")
public final class ConsoleKt
{
    static final KProperty[] $$delegatedProperties;
    private static final int BUFFER_SIZE = 32;
    private static final int LINE_SEPARATOR_MAX_LENGTH = 2;
    private static final Lazy decoder$delegate;
    
    static {
        $$delegatedProperties = new KProperty[] { Reflection.property0(new PropertyReference0Impl(Reflection.getOrCreateKotlinPackage(ConsoleKt.class, "kotlin-stdlib"), "decoder", "getDecoder()Ljava/nio/charset/CharsetDecoder;")) };
        decoder$delegate = LazyKt__LazyJVMKt.lazy((Function0<?>)ConsoleKt$decoder.ConsoleKt$decoder$2.INSTANCE);
    }
    
    private static final boolean containsLineSeparator(@NotNull final CharBuffer charBuffer) {
        boolean b = true;
        if (charBuffer.get(1) != '\n') {
            b = (charBuffer.get(0) == '\n' && b);
        }
        return b;
    }
    
    private static final char dequeue(@NotNull final CharBuffer charBuffer) {
        charBuffer.flip();
        final char value = charBuffer.get();
        charBuffer.compact();
        return value;
    }
    
    private static final void flipBack(@NotNull final Buffer buffer) {
        buffer.position(buffer.limit());
        buffer.limit(buffer.capacity());
    }
    
    private static final CharsetDecoder getDecoder() {
        final Lazy decoder$delegate = ConsoleKt.decoder$delegate;
        final KProperty kProperty = ConsoleKt.$$delegatedProperties[0];
        return (CharsetDecoder)decoder$delegate.getValue();
    }
    
    @InlineOnly
    private static final void print(final byte b) {
        System.out.print((Object)b);
    }
    
    @InlineOnly
    private static final void print(final char c) {
        System.out.print(c);
    }
    
    @InlineOnly
    private static final void print(final double d) {
        System.out.print(d);
    }
    
    @InlineOnly
    private static final void print(final float f) {
        System.out.print(f);
    }
    
    @InlineOnly
    private static final void print(final int i) {
        System.out.print(i);
    }
    
    @InlineOnly
    private static final void print(final long l) {
        System.out.print(l);
    }
    
    @InlineOnly
    private static final void print(final Object obj) {
        System.out.print(obj);
    }
    
    @InlineOnly
    private static final void print(final short s) {
        System.out.print((Object)s);
    }
    
    @InlineOnly
    private static final void print(final boolean b) {
        System.out.print(b);
    }
    
    @InlineOnly
    private static final void print(final char[] s) {
        System.out.print(s);
    }
    
    @InlineOnly
    private static final void println() {
        System.out.println();
    }
    
    @InlineOnly
    private static final void println(final byte b) {
        System.out.println((Object)b);
    }
    
    @InlineOnly
    private static final void println(final char x) {
        System.out.println(x);
    }
    
    @InlineOnly
    private static final void println(final double x) {
        System.out.println(x);
    }
    
    @InlineOnly
    private static final void println(final float x) {
        System.out.println(x);
    }
    
    @InlineOnly
    private static final void println(final int x) {
        System.out.println(x);
    }
    
    @InlineOnly
    private static final void println(final long x) {
        System.out.println(x);
    }
    
    @InlineOnly
    private static final void println(final Object x) {
        System.out.println(x);
    }
    
    @InlineOnly
    private static final void println(final short s) {
        System.out.println((Object)s);
    }
    
    @InlineOnly
    private static final void println(final boolean x) {
        System.out.println(x);
    }
    
    @InlineOnly
    private static final void println(final char[] x) {
        System.out.println(x);
    }
    
    @Nullable
    public static final String readLine() {
        final InputStream in = System.in;
        Intrinsics.checkExpressionValueIsNotNull(in, "System.`in`");
        return readLine(in, getDecoder());
    }
    
    @Nullable
    public static final String readLine(@NotNull final InputStream inputStream, @NotNull final CharsetDecoder charsetDecoder) {
        Intrinsics.checkParameterIsNotNull(inputStream, "inputStream");
        Intrinsics.checkParameterIsNotNull(charsetDecoder, "decoder");
        if (charsetDecoder.maxCharsPerByte() > 1) {
            throw new IllegalArgumentException("Encodings with multiple chars per byte are not supported".toString());
        }
        final ByteBuffer allocate = ByteBuffer.allocate(32);
        final CharBuffer allocate2 = CharBuffer.allocate(2);
        final StringBuilder sb = new StringBuilder();
        int n;
        if ((n = inputStream.read()) == -1) {
            return null;
        }
        do {
            allocate.put((byte)n);
            Intrinsics.checkExpressionValueIsNotNull(allocate, "byteBuffer");
            Intrinsics.checkExpressionValueIsNotNull(allocate2, "charBuffer");
            if (tryDecode(charsetDecoder, allocate, allocate2, false)) {
                if (containsLineSeparator(allocate2)) {
                    break;
                }
                if (allocate2.hasRemaining()) {
                    continue;
                }
                sb.append(dequeue(allocate2));
            }
        } while ((n = inputStream.read()) != -1);
        tryDecode(charsetDecoder, allocate, allocate2, true);
        charsetDecoder.reset();
        final int position = allocate2.position();
        final char value = allocate2.get(0);
        final char value2 = allocate2.get(1);
        switch (position) {
            case 2: {
                if (value != '\r' || value2 != '\n') {
                    sb.append(value);
                }
                if (value2 != '\n') {
                    sb.append(value2);
                    break;
                }
                break;
            }
            case 1: {
                if (value != '\n') {
                    sb.append(value);
                    break;
                }
                break;
            }
        }
        return sb.toString();
    }
    
    private static final boolean tryDecode(@NotNull final CharsetDecoder charsetDecoder, final ByteBuffer in, final CharBuffer out, final boolean endOfInput) {
        final int position = out.position();
        in.flip();
        final CoderResult decode = charsetDecoder.decode(in, out, endOfInput);
        if (decode.isError()) {
            decode.throwException();
        }
        final boolean b = out.position() > position;
        if (b) {
            in.clear();
        }
        else {
            flipBack(in);
        }
        return b;
    }
}
