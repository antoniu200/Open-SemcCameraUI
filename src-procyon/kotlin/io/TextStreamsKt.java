// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.io;

import java.io.StringReader;
import kotlin.text.Charsets;
import java.nio.charset.Charset;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import java.io.InputStream;
import java.net.URL;
import kotlin.sequences.Sequence;
import java.util.Iterator;
import java.io.Closeable;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import java.io.BufferedWriter;
import java.io.Writer;
import kotlin.internal.InlineOnly;
import java.io.BufferedReader;
import org.jetbrains.annotations.NotNull;
import java.io.Reader;
import kotlin.jvm.JvmName;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000X\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u001a\u0017\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0017\u0010\u0000\u001a\u00020\u0005*\u00020\u00062\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u001c\u0010\u0007\u001a\u00020\b*\u00020\u00022\u0006\u0010\t\u001a\u00020\u00062\b\b\u0002\u0010\u0003\u001a\u00020\u0004\u001a\u001e\u0010\n\u001a\u00020\u000b*\u00020\u00022\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000b0\r\u001a\u0010\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0010*\u00020\u0001\u001a\n\u0010\u0011\u001a\u00020\u0012*\u00020\u0013\u001a\u0010\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0015*\u00020\u0002\u001a\n\u0010\u0016\u001a\u00020\u000e*\u00020\u0002\u001a\u0017\u0010\u0016\u001a\u00020\u000e*\u00020\u00132\b\b\u0002\u0010\u0017\u001a\u00020\u0018H\u0087\b\u001a\r\u0010\u0019\u001a\u00020\u001a*\u00020\u000eH\u0087\b\u001a5\u0010\u001b\u001a\u0002H\u001c\"\u0004\b\u0000\u0010\u001c*\u00020\u00022\u0018\u0010\u001d\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u0010\u0012\u0004\u0012\u0002H\u001c0\rH\u0086\b\u00f8\u0001\u0000¢\u0006\u0002\u0010\u001f\u0082\u0002\b\n\u0006\b\u0011(\u001e0\u0001¨\u0006 " }, d2 = { "buffered", "Ljava/io/BufferedReader;", "Ljava/io/Reader;", "bufferSize", "", "Ljava/io/BufferedWriter;", "Ljava/io/Writer;", "copyTo", "", "out", "forEachLine", "", "action", "Lkotlin/Function1;", "", "lineSequence", "Lkotlin/sequences/Sequence;", "readBytes", "", "Ljava/net/URL;", "readLines", "", "readText", "charset", "Ljava/nio/charset/Charset;", "reader", "Ljava/io/StringReader;", "useLines", "T", "block", "Requires newer compiler version to be inlined correctly.", "(Ljava/io/Reader;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "kotlin-stdlib" }, k = 2, mv = { 1, 1, 10 })
@JvmName(name = "TextStreamsKt")
public final class TextStreamsKt
{
    @InlineOnly
    private static final BufferedReader buffered(@NotNull final Reader in, final int sz) {
        BufferedReader bufferedReader;
        if (in instanceof BufferedReader) {
            bufferedReader = (BufferedReader)in;
        }
        else {
            bufferedReader = new BufferedReader(in, sz);
        }
        return bufferedReader;
    }
    
    @InlineOnly
    private static final BufferedWriter buffered(@NotNull final Writer out, final int sz) {
        BufferedWriter bufferedWriter;
        if (out instanceof BufferedWriter) {
            bufferedWriter = (BufferedWriter)out;
        }
        else {
            bufferedWriter = new BufferedWriter(out, sz);
        }
        return bufferedWriter;
    }
    
    public static final long copyTo(@NotNull final Reader reader, @NotNull final Writer writer, int i) {
        Intrinsics.checkParameterIsNotNull(reader, "$receiver");
        Intrinsics.checkParameterIsNotNull(writer, "out");
        final char[] array = new char[i];
        i = reader.read(array);
        long n = 0L;
        while (i >= 0) {
            writer.write(array, 0, i);
            n += i;
            i = reader.read(array);
        }
        return n;
    }
    
    public static final void forEachLine(@NotNull Reader in, @NotNull final Function1<? super String, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(in, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "action");
        BufferedReader bufferedReader;
        if (in instanceof BufferedReader) {
            bufferedReader = (BufferedReader)in;
        }
        else {
            bufferedReader = new BufferedReader(in, 8192);
        }
        final Closeable closeable = bufferedReader;
        final Object o = in = null;
        try {
            try {
                final Iterator<String> iterator = lineSequence((BufferedReader)closeable).iterator();
                while (true) {
                    in = (Reader)o;
                    if (!iterator.hasNext()) {
                        break;
                    }
                    in = (Reader)o;
                    function1.invoke(iterator.next());
                }
                in = (Reader)o;
                final Unit instance = Unit.INSTANCE;
                CloseableKt.closeFinally(closeable, (Throwable)o);
                return;
            }
            finally {}
        }
        catch (final Throwable t) {
            throw t;
        }
        CloseableKt.closeFinally(closeable, (Throwable)in);
    }
    
    @NotNull
    public static final Sequence<String> lineSequence(@NotNull final BufferedReader bufferedReader) {
        Intrinsics.checkParameterIsNotNull(bufferedReader, "$receiver");
        return SequencesKt__SequencesKt.constrainOnce((Sequence<? extends String>)new LinesSequence(bufferedReader));
    }
    
    @NotNull
    public static final byte[] readBytes(@NotNull URL url) {
        Intrinsics.checkParameterIsNotNull(url, "$receiver");
        final Closeable closeable = url.openStream();
        final Serializable s = url = null;
        try {
            try {
                final InputStream inputStream = (InputStream)closeable;
                url = (URL)s;
                Intrinsics.checkExpressionValueIsNotNull(inputStream, "it");
                url = (URL)s;
                final byte[] bytes$default = ByteStreamsKt.readBytes$default(inputStream, 0, 1, (Object)null);
                CloseableKt.closeFinally(closeable, (Throwable)s);
                return bytes$default;
            }
            finally {}
        }
        catch (final Throwable t) {
            throw t;
        }
        CloseableKt.closeFinally(closeable, (Throwable)url);
    }
    
    @NotNull
    public static final List<String> readLines(@NotNull final Reader reader) {
        Intrinsics.checkParameterIsNotNull(reader, "$receiver");
        final ArrayList list = new ArrayList();
        forEachLine(reader, (Function1<? super String, Unit>)new TextStreamsKt$readLines.TextStreamsKt$readLines$1(list));
        return list;
    }
    
    @NotNull
    public static final String readText(@NotNull final Reader reader) {
        Intrinsics.checkParameterIsNotNull(reader, "$receiver");
        final StringWriter stringWriter = new StringWriter();
        copyTo$default(reader, (Writer)stringWriter, 0, 2, (Object)null);
        final String string = stringWriter.toString();
        Intrinsics.checkExpressionValueIsNotNull(string, "buffer.toString()");
        return string;
    }
    
    @InlineOnly
    private static final String readText(@NotNull final URL url, final Charset charset) {
        return new String(readBytes(url), charset);
    }
    
    @InlineOnly
    private static final StringReader reader(@NotNull final String s) {
        return new StringReader(s);
    }
    
    public static final <T> T useLines(@NotNull final Reader p0, @NotNull final Function1<? super Sequence<String>, ? extends T> p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "$receiver"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1        
        //     7: ldc             "block"
        //     9: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    12: aload_0        
        //    13: instanceof      Ljava/io/BufferedReader;
        //    16: ifeq            27
        //    19: aload_0        
        //    20: checkcast       Ljava/io/BufferedReader;
        //    23: astore_0       
        //    24: goto            39
        //    27: new             Ljava/io/BufferedReader;
        //    30: dup            
        //    31: aload_0        
        //    32: sipush          8192
        //    35: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;I)V
        //    38: astore_0       
        //    39: aload_0        
        //    40: checkcast       Ljava/io/Closeable;
        //    43: astore_3       
        //    44: aconst_null    
        //    45: checkcast       Ljava/lang/Throwable;
        //    48: astore_2       
        //    49: aload_2        
        //    50: astore_0       
        //    51: aload_1        
        //    52: aload_3        
        //    53: checkcast       Ljava/io/BufferedReader;
        //    56: invokestatic    kotlin/io/TextStreamsKt.lineSequence:(Ljava/io/BufferedReader;)Lkotlin/sequences/Sequence;
        //    59: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    64: astore_1       
        //    65: iconst_1       
        //    66: invokestatic    kotlin/jvm/internal/InlineMarker.finallyStart:(I)V
        //    69: iconst_1       
        //    70: iconst_1       
        //    71: iconst_0       
        //    72: invokestatic    kotlin/internal/PlatformImplementationsKt.apiVersionIsAtLeast:(III)Z
        //    75: ifeq            86
        //    78: aload_3        
        //    79: aload_2        
        //    80: invokestatic    kotlin/io/CloseableKt.closeFinally:(Ljava/io/Closeable;Ljava/lang/Throwable;)V
        //    83: goto            92
        //    86: aload_3        
        //    87: invokeinterface java/io/Closeable.close:()V
        //    92: iconst_1       
        //    93: invokestatic    kotlin/jvm/internal/InlineMarker.finallyEnd:(I)V
        //    96: aload_1        
        //    97: areturn        
        //    98: astore_1       
        //    99: goto            107
        //   102: astore_1       
        //   103: aload_1        
        //   104: astore_0       
        //   105: aload_1        
        //   106: athrow         
        //   107: iconst_1       
        //   108: invokestatic    kotlin/jvm/internal/InlineMarker.finallyStart:(I)V
        //   111: iconst_1       
        //   112: iconst_1       
        //   113: iconst_0       
        //   114: invokestatic    kotlin/internal/PlatformImplementationsKt.apiVersionIsAtLeast:(III)Z
        //   117: ifeq            128
        //   120: aload_3        
        //   121: aload_0        
        //   122: invokestatic    kotlin/io/CloseableKt.closeFinally:(Ljava/io/Closeable;Ljava/lang/Throwable;)V
        //   125: goto            147
        //   128: aload_0        
        //   129: ifnonnull       141
        //   132: aload_3        
        //   133: invokeinterface java/io/Closeable.close:()V
        //   138: goto            147
        //   141: aload_3        
        //   142: invokeinterface java/io/Closeable.close:()V
        //   147: iconst_1       
        //   148: invokestatic    kotlin/jvm/internal/InlineMarker.finallyEnd:(I)V
        //   151: aload_1        
        //   152: athrow         
        //   153: astore_0       
        //   154: goto            147
        //    Signature:
        //  <T:Ljava/lang/Object;>(Ljava/io/Reader;Lkotlin/jvm/functions/Function1<-Lkotlin/sequences/Sequence<Ljava/lang/String;>;+TT;>;)TT;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  51     65     102    107    Ljava/lang/Throwable;
        //  51     65     98     157    Any
        //  105    107    98     157    Any
        //  141    147    153    157    Ljava/lang/Throwable;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0141:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2604)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:206)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:137)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:333)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:254)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:129)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
}
