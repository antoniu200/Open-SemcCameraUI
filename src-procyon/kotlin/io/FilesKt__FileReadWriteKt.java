// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.io;

import kotlin.sequences.Sequence;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.io.PrintWriter;
import kotlin.jvm.functions.Function1;
import java.io.Serializable;
import kotlin.jvm.functions.Function2;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.Writer;
import java.io.BufferedWriter;
import kotlin.internal.InlineOnly;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.BufferedReader;
import kotlin.text.Charsets;
import java.nio.charset.Charset;
import kotlin.Unit;
import java.io.FileOutputStream;
import java.io.Closeable;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000z\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u001a\u0012\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004\u001a\u001c\u0010\u0005\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t\u001a!\u0010\n\u001a\u00020\u000b*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\f\u001a\u00020\rH\u0087\b\u001a!\u0010\u000e\u001a\u00020\u000f*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\f\u001a\u00020\rH\u0087\b\u001aB\u0010\u0010\u001a\u00020\u0001*\u00020\u000226\u0010\u0011\u001a2\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\r¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0016\u0012\u0004\u0012\u00020\u00010\u0012\u001aJ\u0010\u0010\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0017\u001a\u00020\r26\u0010\u0011\u001a2\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\r¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0016\u0012\u0004\u0012\u00020\u00010\u0012\u001a7\u0010\u0018\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2!\u0010\u0011\u001a\u001d\u0012\u0013\u0012\u00110\u0007¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u001a\u0012\u0004\u0012\u00020\u00010\u0019\u001a\r\u0010\u001b\u001a\u00020\u001c*\u00020\u0002H\u0087\b\u001a\r\u0010\u001d\u001a\u00020\u001e*\u00020\u0002H\u0087\b\u001a\u0017\u0010\u001f\u001a\u00020 *\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\tH\u0087\b\u001a\n\u0010!\u001a\u00020\u0004*\u00020\u0002\u001a\u001a\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00070#*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t\u001a\u0014\u0010$\u001a\u00020\u0007*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t\u001a\u0017\u0010%\u001a\u00020&*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\tH\u0087\b\u001a?\u0010'\u001a\u0002H(\"\u0004\b\u0000\u0010(*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2\u0018\u0010)\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070*\u0012\u0004\u0012\u0002H(0\u0019H\u0086\b\u00f8\u0001\u0000¢\u0006\u0002\u0010,\u001a\u0012\u0010-\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004\u001a\u001c\u0010.\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t\u001a\u0017\u0010/\u001a\u000200*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\tH\u0087\b\u0082\u0002\b\n\u0006\b\u0011(+0\u0001¨\u00061" }, d2 = { "appendBytes", "", "Ljava/io/File;", "array", "", "appendText", "text", "", "charset", "Ljava/nio/charset/Charset;", "bufferedReader", "Ljava/io/BufferedReader;", "bufferSize", "", "bufferedWriter", "Ljava/io/BufferedWriter;", "forEachBlock", "action", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "buffer", "bytesRead", "blockSize", "forEachLine", "Lkotlin/Function1;", "line", "inputStream", "Ljava/io/FileInputStream;", "outputStream", "Ljava/io/FileOutputStream;", "printWriter", "Ljava/io/PrintWriter;", "readBytes", "readLines", "", "readText", "reader", "Ljava/io/InputStreamReader;", "useLines", "T", "block", "Lkotlin/sequences/Sequence;", "Requires newer compiler version to be inlined correctly.", "(Ljava/io/File;Ljava/nio/charset/Charset;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "writeBytes", "writeText", "writer", "Ljava/io/OutputStreamWriter;", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/io/FilesKt")
class FilesKt__FileReadWriteKt extends FilesKt__FilePathComponentsKt
{
    public FilesKt__FileReadWriteKt() {
    }
    
    public static final void appendBytes(@NotNull File file, @NotNull final byte[] b) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(b, "array");
        final Closeable closeable = new FileOutputStream(file, true);
        final File file2 = file = null;
        try {
            try {
                ((FileOutputStream)closeable).write(b);
                file = file2;
                final Unit instance = Unit.INSTANCE;
                CloseableKt.closeFinally(closeable, (Throwable)file2);
                return;
            }
            finally {}
        }
        catch (final Throwable t) {
            throw t;
        }
        CloseableKt.closeFinally(closeable, (Throwable)file);
    }
    
    public static final void appendText(@NotNull final File file, @NotNull final String s, @NotNull final Charset charset) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(s, "text");
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        final byte[] bytes = s.getBytes(charset);
        Intrinsics.checkExpressionValueIsNotNull(bytes, "(this as java.lang.String).getBytes(charset)");
        appendBytes(file, bytes);
    }
    
    @InlineOnly
    private static final BufferedReader bufferedReader(@NotNull final File file, final Charset cs, final int sz) {
        final Reader in = new InputStreamReader(new FileInputStream(file), cs);
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
    private static final BufferedWriter bufferedWriter(@NotNull final File file, final Charset cs, final int sz) {
        final Writer out = new OutputStreamWriter(new FileOutputStream(file), cs);
        BufferedWriter bufferedWriter;
        if (out instanceof BufferedWriter) {
            bufferedWriter = (BufferedWriter)out;
        }
        else {
            bufferedWriter = new BufferedWriter(out, sz);
        }
        return bufferedWriter;
    }
    
    public static final void forEachBlock(@NotNull File file, int read, @NotNull final Function2<? super byte[], ? super Integer, Unit> function2) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(function2, "action");
        final byte[] b = new byte[RangesKt___RangesKt.coerceAtLeast(read, 512)];
        final Closeable closeable = new FileInputStream(file);
        final Serializable s = file = null;
        try {
            try {
                final FileInputStream fileInputStream = (FileInputStream)closeable;
                while (true) {
                    file = (File)s;
                    read = fileInputStream.read(b);
                    if (read <= 0) {
                        break;
                    }
                    file = (File)s;
                    function2.invoke(b, read);
                }
                file = (File)s;
                final Unit instance = Unit.INSTANCE;
                CloseableKt.closeFinally(closeable, (Throwable)s);
                return;
            }
            finally {}
        }
        catch (final Throwable t) {
            throw t;
        }
        CloseableKt.closeFinally(closeable, (Throwable)file);
    }
    
    public static final void forEachBlock(@NotNull final File file, @NotNull final Function2<? super byte[], ? super Integer, Unit> function2) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(function2, "action");
        forEachBlock(file, 4096, function2);
    }
    
    public static final void forEachLine(@NotNull final File file, @NotNull final Charset cs, @NotNull final Function1<? super String, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(cs, "charset");
        Intrinsics.checkParameterIsNotNull(function1, "action");
        TextStreamsKt.forEachLine(new BufferedReader(new InputStreamReader(new FileInputStream(file), cs)), function1);
    }
    
    @InlineOnly
    private static final FileInputStream inputStream(@NotNull final File file) {
        return new FileInputStream(file);
    }
    
    @InlineOnly
    private static final FileOutputStream outputStream(@NotNull final File file) {
        return new FileOutputStream(file);
    }
    
    @InlineOnly
    private static final PrintWriter printWriter(@NotNull final File file, final Charset cs) {
        final Writer out = new OutputStreamWriter(new FileOutputStream(file), cs);
        BufferedWriter bufferedWriter;
        if (out instanceof BufferedWriter) {
            bufferedWriter = (BufferedWriter)out;
        }
        else {
            bufferedWriter = new BufferedWriter(out, 8192);
        }
        return new PrintWriter(bufferedWriter);
    }
    
    @NotNull
    public static final byte[] readBytes(@NotNull final File file) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        final Closeable closeable = new FileInputStream(file);
        Throwable t2;
        final Throwable t = t2 = null;
        try {
            try {
                final FileInputStream fileInputStream = (FileInputStream)closeable;
                int n = 0;
                t2 = t;
                final long length = file.length();
                if (length > Integer.MAX_VALUE) {
                    t2 = t;
                    t2 = t;
                    t2 = t;
                    final StringBuilder sb = new StringBuilder();
                    t2 = t;
                    sb.append("File ");
                    t2 = t;
                    sb.append(file);
                    t2 = t;
                    sb.append(" is too big (");
                    t2 = t;
                    sb.append(length);
                    t2 = t;
                    sb.append(" bytes) to fit in memory.");
                    t2 = t;
                    final OutOfMemoryError outOfMemoryError = new OutOfMemoryError(sb.toString());
                    t2 = t;
                    throw outOfMemoryError;
                }
                int i = (int)length;
                t2 = t;
                byte[] copy = new byte[i];
                while (i > 0) {
                    t2 = t;
                    final int read = fileInputStream.read(copy, n, i);
                    if (read < 0) {
                        break;
                    }
                    i -= read;
                    n += read;
                }
                if (i != 0) {
                    t2 = t;
                    copy = Arrays.copyOf(copy, n);
                    t2 = t;
                    Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, newSize)");
                }
                CloseableKt.closeFinally(closeable, t);
                return copy;
            }
            finally {}
        }
        catch (final Throwable t3) {
            throw t3;
        }
        CloseableKt.closeFinally(closeable, t2);
    }
    
    @NotNull
    public static final List<String> readLines(@NotNull final File file, @NotNull final Charset charset) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        final ArrayList list = new ArrayList();
        forEachLine(file, charset, (Function1<? super String, Unit>)new FilesKt__FileReadWriteKt$readLines.FilesKt__FileReadWriteKt$readLines$1(list));
        return list;
    }
    
    @NotNull
    public static final String readText(@NotNull final File file, @NotNull final Charset charset) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        return new String(readBytes(file), charset);
    }
    
    @InlineOnly
    private static final InputStreamReader reader(@NotNull final File file, final Charset cs) {
        return new InputStreamReader(new FileInputStream(file), cs);
    }
    
    public static final <T> T useLines(@NotNull final File p0, @NotNull final Charset p1, @NotNull final Function1<? super Sequence<String>, ? extends T> p2) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "$receiver"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1        
        //     7: ldc             "charset"
        //     9: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    12: aload_2        
        //    13: ldc_w           "block"
        //    16: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    19: new             Ljava/io/InputStreamReader;
        //    22: dup            
        //    23: new             Ljava/io/FileInputStream;
        //    26: dup            
        //    27: aload_0        
        //    28: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //    31: checkcast       Ljava/io/InputStream;
        //    34: aload_1        
        //    35: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;Ljava/nio/charset/Charset;)V
        //    38: checkcast       Ljava/io/Reader;
        //    41: astore_0       
        //    42: aload_0        
        //    43: instanceof      Ljava/io/BufferedReader;
        //    46: ifeq            57
        //    49: aload_0        
        //    50: checkcast       Ljava/io/BufferedReader;
        //    53: astore_0       
        //    54: goto            69
        //    57: new             Ljava/io/BufferedReader;
        //    60: dup            
        //    61: aload_0        
        //    62: sipush          8192
        //    65: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;I)V
        //    68: astore_0       
        //    69: aload_0        
        //    70: checkcast       Ljava/io/Closeable;
        //    73: astore_3       
        //    74: aconst_null    
        //    75: checkcast       Ljava/lang/Throwable;
        //    78: astore_1       
        //    79: aload_1        
        //    80: astore_0       
        //    81: aload_2        
        //    82: aload_3        
        //    83: checkcast       Ljava/io/BufferedReader;
        //    86: invokestatic    kotlin/io/TextStreamsKt.lineSequence:(Ljava/io/BufferedReader;)Lkotlin/sequences/Sequence;
        //    89: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    94: astore_2       
        //    95: iconst_1       
        //    96: invokestatic    kotlin/jvm/internal/InlineMarker.finallyStart:(I)V
        //    99: iconst_1       
        //   100: iconst_1       
        //   101: iconst_0       
        //   102: invokestatic    kotlin/internal/PlatformImplementationsKt.apiVersionIsAtLeast:(III)Z
        //   105: ifeq            116
        //   108: aload_3        
        //   109: aload_1        
        //   110: invokestatic    kotlin/io/CloseableKt.closeFinally:(Ljava/io/Closeable;Ljava/lang/Throwable;)V
        //   113: goto            122
        //   116: aload_3        
        //   117: invokeinterface java/io/Closeable.close:()V
        //   122: iconst_1       
        //   123: invokestatic    kotlin/jvm/internal/InlineMarker.finallyEnd:(I)V
        //   126: aload_2        
        //   127: areturn        
        //   128: astore_1       
        //   129: goto            137
        //   132: astore_1       
        //   133: aload_1        
        //   134: astore_0       
        //   135: aload_1        
        //   136: athrow         
        //   137: iconst_1       
        //   138: invokestatic    kotlin/jvm/internal/InlineMarker.finallyStart:(I)V
        //   141: iconst_1       
        //   142: iconst_1       
        //   143: iconst_0       
        //   144: invokestatic    kotlin/internal/PlatformImplementationsKt.apiVersionIsAtLeast:(III)Z
        //   147: ifeq            158
        //   150: aload_3        
        //   151: aload_0        
        //   152: invokestatic    kotlin/io/CloseableKt.closeFinally:(Ljava/io/Closeable;Ljava/lang/Throwable;)V
        //   155: goto            177
        //   158: aload_0        
        //   159: ifnonnull       171
        //   162: aload_3        
        //   163: invokeinterface java/io/Closeable.close:()V
        //   168: goto            177
        //   171: aload_3        
        //   172: invokeinterface java/io/Closeable.close:()V
        //   177: iconst_1       
        //   178: invokestatic    kotlin/jvm/internal/InlineMarker.finallyEnd:(I)V
        //   181: aload_1        
        //   182: athrow         
        //   183: astore_0       
        //   184: goto            177
        //    Signature:
        //  <T:Ljava/lang/Object;>(Ljava/io/File;Ljava/nio/charset/Charset;Lkotlin/jvm/functions/Function1<-Lkotlin/sequences/Sequence<Ljava/lang/String;>;+TT;>;)TT;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  81     95     132    137    Ljava/lang/Throwable;
        //  81     95     128    187    Any
        //  135    137    128    187    Any
        //  171    177    183    187    Ljava/lang/Throwable;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0171:
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
    
    public static final void writeBytes(@NotNull File file, @NotNull final byte[] b) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(b, "array");
        final Closeable closeable = new FileOutputStream(file);
        final File file2 = file = null;
        try {
            try {
                ((FileOutputStream)closeable).write(b);
                file = file2;
                final Unit instance = Unit.INSTANCE;
                CloseableKt.closeFinally(closeable, (Throwable)file2);
                return;
            }
            finally {}
        }
        catch (final Throwable t) {
            throw t;
        }
        CloseableKt.closeFinally(closeable, (Throwable)file);
    }
    
    public static final void writeText(@NotNull final File file, @NotNull final String s, @NotNull final Charset charset) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(s, "text");
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        final byte[] bytes = s.getBytes(charset);
        Intrinsics.checkExpressionValueIsNotNull(bytes, "(this as java.lang.String).getBytes(charset)");
        writeBytes(file, bytes);
    }
    
    @InlineOnly
    private static final OutputStreamWriter writer(@NotNull final File file, final Charset cs) {
        return new OutputStreamWriter(new FileOutputStream(file), cs);
    }
}
