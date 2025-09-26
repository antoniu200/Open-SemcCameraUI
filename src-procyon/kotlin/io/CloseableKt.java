// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.io;

import kotlin.internal.InlineOnly;
import kotlin.jvm.functions.Function1;
import kotlin.SinceKotlin;
import kotlin.PublishedApi;
import org.jetbrains.annotations.Nullable;
import java.io.Closeable;
import kotlin.jvm.JvmName;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u001c\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0018\u0010\u0000\u001a\u00020\u0001*\u0004\u0018\u00010\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004H\u0001\u001a;\u0010\u0005\u001a\u0002H\u0006\"\n\b\u0000\u0010\u0007*\u0004\u0018\u00010\u0002\"\u0004\b\u0001\u0010\u0006*\u0002H\u00072\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u0002H\u0007\u0012\u0004\u0012\u0002H\u00060\tH\u0087\b\u00f8\u0001\u0000¢\u0006\u0002\u0010\u000b\u0082\u0002\b\n\u0006\b\u0011(\n0\u0001¨\u0006\f" }, d2 = { "closeFinally", "", "Ljava/io/Closeable;", "cause", "", "use", "R", "T", "block", "Lkotlin/Function1;", "Requires newer compiler version to be inlined correctly.", "(Ljava/io/Closeable;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "kotlin-stdlib" }, k = 2, mv = { 1, 1, 10 })
@JvmName(name = "CloseableKt")
public final class CloseableKt
{
    @PublishedApi
    @SinceKotlin(version = "1.1")
    public static final void closeFinally(@Nullable final Closeable closeable, @Nullable final Throwable t) {
        if (closeable != null) {
            if (t == null) {
                closeable.close();
            }
            else {
                try {
                    closeable.close();
                }
                catch (final Throwable t2) {
                    ExceptionsKt__ExceptionsKt.addSuppressed(t, t2);
                }
            }
        }
    }
    
    @InlineOnly
    private static final <T extends Closeable, R> R use(final T p0, final Function1<? super T, ? extends R> p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: checkcast       Ljava/lang/Throwable;
        //     4: astore_3       
        //     5: aload_3        
        //     6: astore_2       
        //     7: aload_1        
        //     8: aload_0        
        //     9: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //    14: astore_1       
        //    15: iconst_1       
        //    16: invokestatic    kotlin/jvm/internal/InlineMarker.finallyStart:(I)V
        //    19: iconst_1       
        //    20: iconst_1       
        //    21: iconst_0       
        //    22: invokestatic    kotlin/internal/PlatformImplementationsKt.apiVersionIsAtLeast:(III)Z
        //    25: ifeq            36
        //    28: aload_0        
        //    29: aload_3        
        //    30: invokestatic    kotlin/io/CloseableKt.closeFinally:(Ljava/io/Closeable;Ljava/lang/Throwable;)V
        //    33: goto            49
        //    36: aload_0        
        //    37: ifnonnull       43
        //    40: goto            49
        //    43: aload_0        
        //    44: invokeinterface java/io/Closeable.close:()V
        //    49: iconst_1       
        //    50: invokestatic    kotlin/jvm/internal/InlineMarker.finallyEnd:(I)V
        //    53: aload_1        
        //    54: areturn        
        //    55: astore_1       
        //    56: goto            64
        //    59: astore_1       
        //    60: aload_1        
        //    61: astore_2       
        //    62: aload_1        
        //    63: athrow         
        //    64: iconst_1       
        //    65: invokestatic    kotlin/jvm/internal/InlineMarker.finallyStart:(I)V
        //    68: iconst_1       
        //    69: iconst_1       
        //    70: iconst_0       
        //    71: invokestatic    kotlin/internal/PlatformImplementationsKt.apiVersionIsAtLeast:(III)Z
        //    74: ifeq            85
        //    77: aload_0        
        //    78: aload_2        
        //    79: invokestatic    kotlin/io/CloseableKt.closeFinally:(Ljava/io/Closeable;Ljava/lang/Throwable;)V
        //    82: goto            111
        //    85: aload_0        
        //    86: ifnonnull       92
        //    89: goto            111
        //    92: aload_2        
        //    93: ifnonnull       105
        //    96: aload_0        
        //    97: invokeinterface java/io/Closeable.close:()V
        //   102: goto            111
        //   105: aload_0        
        //   106: invokeinterface java/io/Closeable.close:()V
        //   111: iconst_1       
        //   112: invokestatic    kotlin/jvm/internal/InlineMarker.finallyEnd:(I)V
        //   115: aload_1        
        //   116: athrow         
        //   117: astore_0       
        //   118: goto            111
        //    Signature:
        //  <T::Ljava/io/Closeable;R:Ljava/lang/Object;>(TT;Lkotlin/jvm/functions/Function1<-TT;+TR;>;)TR;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  7      15     59     64     Ljava/lang/Throwable;
        //  7      15     55     121    Any
        //  62     64     55     121    Any
        //  105    111    117    121    Ljava/lang/Throwable;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0105:
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
