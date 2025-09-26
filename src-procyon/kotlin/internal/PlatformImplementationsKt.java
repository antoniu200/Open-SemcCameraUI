// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.internal;

import kotlin.jvm.internal.Intrinsics;
import kotlin.SinceKotlin;
import kotlin.PublishedApi;
import kotlin.KotlinVersion;
import org.jetbrains.annotations.NotNull;
import kotlin.jvm.JvmField;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u001e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0004\u001a \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u0005H\u0001\u001a\"\u0010\b\u001a\u0002H\t\"\n\b\u0000\u0010\t\u0018\u0001*\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0083\b¢\u0006\u0002\u0010\f\u001a\b\u0010\r\u001a\u00020\u0005H\u0002\"\u0010\u0010\u0000\u001a\u00020\u00018\u0000X\u0081\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e" }, d2 = { "IMPLEMENTATIONS", "Lkotlin/internal/PlatformImplementations;", "apiVersionIsAtLeast", "", "major", "", "minor", "patch", "castToBaseType", "T", "", "instance", "(Ljava/lang/Object;)Ljava/lang/Object;", "getJavaVersion", "kotlin-stdlib" }, k = 2, mv = { 1, 1, 10 })
public final class PlatformImplementationsKt
{
    @JvmField
    @NotNull
    public static final PlatformImplementations IMPLEMENTATIONS;
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: istore_0       
        //     4: iload_0        
        //     5: ldc             65544
        //     7: if_icmplt       261
        //    10: ldc             "kotlin.internal.jdk8.JDK8PlatformImplementations"
        //    12: invokestatic    java/lang/Class.forName:(Ljava/lang/String;)Ljava/lang/Class;
        //    15: invokevirtual   java/lang/Class.newInstance:()Ljava/lang/Object;
        //    18: astore_2       
        //    19: aload_2        
        //    20: ldc             "Class.forName(\"kotlin.in\u2026entations\").newInstance()"
        //    22: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    25: aload_2        
        //    26: ifnonnull       45
        //    29: new             Lkotlin/TypeCastException;
        //    32: astore_1       
        //    33: aload_1        
        //    34: ldc             "null cannot be cast to non-null type kotlin.internal.PlatformImplementations"
        //    36: invokespecial   kotlin/TypeCastException.<init>:(Ljava/lang/String;)V
        //    39: aload_1        
        //    40: athrow         
        //    41: astore_1       
        //    42: goto            53
        //    45: aload_2        
        //    46: checkcast       Lkotlin/internal/PlatformImplementations;
        //    49: astore_1       
        //    50: goto            534
        //    53: aload_2        
        //    54: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //    57: invokevirtual   java/lang/Class.getClassLoader:()Ljava/lang/ClassLoader;
        //    60: astore          5
        //    62: ldc             Lkotlin/internal/PlatformImplementations;.class
        //    64: invokevirtual   java/lang/Class.getClassLoader:()Ljava/lang/ClassLoader;
        //    67: astore_2       
        //    68: new             Ljava/lang/ClassCastException;
        //    71: astore          4
        //    73: new             Ljava/lang/StringBuilder;
        //    76: astore_3       
        //    77: aload_3        
        //    78: invokespecial   java/lang/StringBuilder.<init>:()V
        //    81: aload_3        
        //    82: ldc             "Instance classloader: "
        //    84: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    87: pop            
        //    88: aload_3        
        //    89: aload           5
        //    91: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //    94: pop            
        //    95: aload_3        
        //    96: ldc             ", base type classloader: "
        //    98: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   101: pop            
        //   102: aload_3        
        //   103: aload_2        
        //   104: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   107: pop            
        //   108: aload           4
        //   110: aload_3        
        //   111: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   114: invokespecial   java/lang/ClassCastException.<init>:(Ljava/lang/String;)V
        //   117: aload           4
        //   119: aload_1        
        //   120: checkcast       Ljava/lang/Throwable;
        //   123: invokevirtual   java/lang/ClassCastException.initCause:(Ljava/lang/Throwable;)Ljava/lang/Throwable;
        //   126: astore_1       
        //   127: aload_1        
        //   128: ldc             "ClassCastException(\"Inst\u2026baseTypeCL\").initCause(e)"
        //   130: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   133: aload_1        
        //   134: athrow         
        //   135: astore_1       
        //   136: ldc             "kotlin.internal.JRE8PlatformImplementations"
        //   138: invokestatic    java/lang/Class.forName:(Ljava/lang/String;)Ljava/lang/Class;
        //   141: invokevirtual   java/lang/Class.newInstance:()Ljava/lang/Object;
        //   144: astore_2       
        //   145: aload_2        
        //   146: ldc             "Class.forName(\"kotlin.in\u2026entations\").newInstance()"
        //   148: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   151: aload_2        
        //   152: ifnonnull       171
        //   155: new             Lkotlin/TypeCastException;
        //   158: astore_1       
        //   159: aload_1        
        //   160: ldc             "null cannot be cast to non-null type kotlin.internal.PlatformImplementations"
        //   162: invokespecial   kotlin/TypeCastException.<init>:(Ljava/lang/String;)V
        //   165: aload_1        
        //   166: athrow         
        //   167: astore_1       
        //   168: goto            179
        //   171: aload_2        
        //   172: checkcast       Lkotlin/internal/PlatformImplementations;
        //   175: astore_1       
        //   176: goto            534
        //   179: aload_2        
        //   180: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //   183: invokevirtual   java/lang/Class.getClassLoader:()Ljava/lang/ClassLoader;
        //   186: astore          5
        //   188: ldc             Lkotlin/internal/PlatformImplementations;.class
        //   190: invokevirtual   java/lang/Class.getClassLoader:()Ljava/lang/ClassLoader;
        //   193: astore_3       
        //   194: new             Ljava/lang/ClassCastException;
        //   197: astore          4
        //   199: new             Ljava/lang/StringBuilder;
        //   202: astore_2       
        //   203: aload_2        
        //   204: invokespecial   java/lang/StringBuilder.<init>:()V
        //   207: aload_2        
        //   208: ldc             "Instance classloader: "
        //   210: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   213: pop            
        //   214: aload_2        
        //   215: aload           5
        //   217: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   220: pop            
        //   221: aload_2        
        //   222: ldc             ", base type classloader: "
        //   224: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   227: pop            
        //   228: aload_2        
        //   229: aload_3        
        //   230: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   233: pop            
        //   234: aload           4
        //   236: aload_2        
        //   237: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   240: invokespecial   java/lang/ClassCastException.<init>:(Ljava/lang/String;)V
        //   243: aload           4
        //   245: aload_1        
        //   246: checkcast       Ljava/lang/Throwable;
        //   249: invokevirtual   java/lang/ClassCastException.initCause:(Ljava/lang/Throwable;)Ljava/lang/Throwable;
        //   252: astore_1       
        //   253: aload_1        
        //   254: ldc             "ClassCastException(\"Inst\u2026baseTypeCL\").initCause(e)"
        //   256: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   259: aload_1        
        //   260: athrow         
        //   261: iload_0        
        //   262: ldc             65543
        //   264: if_icmplt       526
        //   267: ldc             "kotlin.internal.jdk7.JDK7PlatformImplementations"
        //   269: invokestatic    java/lang/Class.forName:(Ljava/lang/String;)Ljava/lang/Class;
        //   272: invokevirtual   java/lang/Class.newInstance:()Ljava/lang/Object;
        //   275: astore_2       
        //   276: aload_2        
        //   277: ldc             "Class.forName(\"kotlin.in\u2026entations\").newInstance()"
        //   279: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   282: aload_2        
        //   283: ifnonnull       302
        //   286: new             Lkotlin/TypeCastException;
        //   289: astore_1       
        //   290: aload_1        
        //   291: ldc             "null cannot be cast to non-null type kotlin.internal.PlatformImplementations"
        //   293: invokespecial   kotlin/TypeCastException.<init>:(Ljava/lang/String;)V
        //   296: aload_1        
        //   297: athrow         
        //   298: astore_1       
        //   299: goto            310
        //   302: aload_2        
        //   303: checkcast       Lkotlin/internal/PlatformImplementations;
        //   306: astore_1       
        //   307: goto            534
        //   310: aload_2        
        //   311: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //   314: invokevirtual   java/lang/Class.getClassLoader:()Ljava/lang/ClassLoader;
        //   317: astore_3       
        //   318: ldc             Lkotlin/internal/PlatformImplementations;.class
        //   320: invokevirtual   java/lang/Class.getClassLoader:()Ljava/lang/ClassLoader;
        //   323: astore          5
        //   325: new             Ljava/lang/ClassCastException;
        //   328: astore_2       
        //   329: new             Ljava/lang/StringBuilder;
        //   332: astore          4
        //   334: aload           4
        //   336: invokespecial   java/lang/StringBuilder.<init>:()V
        //   339: aload           4
        //   341: ldc             "Instance classloader: "
        //   343: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   346: pop            
        //   347: aload           4
        //   349: aload_3        
        //   350: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   353: pop            
        //   354: aload           4
        //   356: ldc             ", base type classloader: "
        //   358: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   361: pop            
        //   362: aload           4
        //   364: aload           5
        //   366: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   369: pop            
        //   370: aload_2        
        //   371: aload           4
        //   373: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   376: invokespecial   java/lang/ClassCastException.<init>:(Ljava/lang/String;)V
        //   379: aload_2        
        //   380: aload_1        
        //   381: checkcast       Ljava/lang/Throwable;
        //   384: invokevirtual   java/lang/ClassCastException.initCause:(Ljava/lang/Throwable;)Ljava/lang/Throwable;
        //   387: astore_1       
        //   388: aload_1        
        //   389: ldc             "ClassCastException(\"Inst\u2026baseTypeCL\").initCause(e)"
        //   391: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   394: aload_1        
        //   395: athrow         
        //   396: astore_1       
        //   397: ldc             "kotlin.internal.JRE7PlatformImplementations"
        //   399: invokestatic    java/lang/Class.forName:(Ljava/lang/String;)Ljava/lang/Class;
        //   402: invokevirtual   java/lang/Class.newInstance:()Ljava/lang/Object;
        //   405: astore_2       
        //   406: aload_2        
        //   407: ldc             "Class.forName(\"kotlin.in\u2026entations\").newInstance()"
        //   409: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   412: aload_2        
        //   413: ifnonnull       432
        //   416: new             Lkotlin/TypeCastException;
        //   419: astore_1       
        //   420: aload_1        
        //   421: ldc             "null cannot be cast to non-null type kotlin.internal.PlatformImplementations"
        //   423: invokespecial   kotlin/TypeCastException.<init>:(Ljava/lang/String;)V
        //   426: aload_1        
        //   427: athrow         
        //   428: astore_1       
        //   429: goto            440
        //   432: aload_2        
        //   433: checkcast       Lkotlin/internal/PlatformImplementations;
        //   436: astore_1       
        //   437: goto            534
        //   440: aload_2        
        //   441: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //   444: invokevirtual   java/lang/Class.getClassLoader:()Ljava/lang/ClassLoader;
        //   447: astore          4
        //   449: ldc             Lkotlin/internal/PlatformImplementations;.class
        //   451: invokevirtual   java/lang/Class.getClassLoader:()Ljava/lang/ClassLoader;
        //   454: astore_2       
        //   455: new             Ljava/lang/ClassCastException;
        //   458: astore_3       
        //   459: new             Ljava/lang/StringBuilder;
        //   462: astore          5
        //   464: aload           5
        //   466: invokespecial   java/lang/StringBuilder.<init>:()V
        //   469: aload           5
        //   471: ldc             "Instance classloader: "
        //   473: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   476: pop            
        //   477: aload           5
        //   479: aload           4
        //   481: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   484: pop            
        //   485: aload           5
        //   487: ldc             ", base type classloader: "
        //   489: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   492: pop            
        //   493: aload           5
        //   495: aload_2        
        //   496: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   499: pop            
        //   500: aload_3        
        //   501: aload           5
        //   503: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   506: invokespecial   java/lang/ClassCastException.<init>:(Ljava/lang/String;)V
        //   509: aload_3        
        //   510: aload_1        
        //   511: checkcast       Ljava/lang/Throwable;
        //   514: invokevirtual   java/lang/ClassCastException.initCause:(Ljava/lang/Throwable;)Ljava/lang/Throwable;
        //   517: astore_1       
        //   518: aload_1        
        //   519: ldc             "ClassCastException(\"Inst\u2026baseTypeCL\").initCause(e)"
        //   521: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   524: aload_1        
        //   525: athrow         
        //   526: new             Lkotlin/internal/PlatformImplementations;
        //   529: dup            
        //   530: invokespecial   kotlin/internal/PlatformImplementations.<init>:()V
        //   533: astore_1       
        //   534: aload_1        
        //   535: putstatic       kotlin/internal/PlatformImplementationsKt.IMPLEMENTATIONS:Lkotlin/internal/PlatformImplementations;
        //   538: return         
        //   539: astore_1       
        //   540: goto            261
        //   543: astore_1       
        //   544: goto            526
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                              
        //  -----  -----  -----  -----  ----------------------------------
        //  10     25     135    261    Ljava/lang/ClassNotFoundException;
        //  29     41     41     45     Ljava/lang/ClassCastException;
        //  29     41     135    261    Ljava/lang/ClassNotFoundException;
        //  45     50     41     45     Ljava/lang/ClassCastException;
        //  45     50     135    261    Ljava/lang/ClassNotFoundException;
        //  53     135    135    261    Ljava/lang/ClassNotFoundException;
        //  136    151    539    543    Ljava/lang/ClassNotFoundException;
        //  155    167    167    171    Ljava/lang/ClassCastException;
        //  155    167    539    543    Ljava/lang/ClassNotFoundException;
        //  171    176    167    171    Ljava/lang/ClassCastException;
        //  171    176    539    543    Ljava/lang/ClassNotFoundException;
        //  179    261    539    543    Ljava/lang/ClassNotFoundException;
        //  267    282    396    526    Ljava/lang/ClassNotFoundException;
        //  286    298    298    302    Ljava/lang/ClassCastException;
        //  286    298    396    526    Ljava/lang/ClassNotFoundException;
        //  302    307    298    302    Ljava/lang/ClassCastException;
        //  302    307    396    526    Ljava/lang/ClassNotFoundException;
        //  310    396    396    526    Ljava/lang/ClassNotFoundException;
        //  397    412    543    547    Ljava/lang/ClassNotFoundException;
        //  416    428    428    432    Ljava/lang/ClassCastException;
        //  416    428    543    547    Ljava/lang/ClassNotFoundException;
        //  432    437    428    432    Ljava/lang/ClassCastException;
        //  432    437    543    547    Ljava/lang/ClassNotFoundException;
        //  440    526    543    547    Ljava/lang/ClassNotFoundException;
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
    
    @PublishedApi
    @SinceKotlin(version = "1.2")
    public static final boolean apiVersionIsAtLeast(final int n, final int n2, final int n3) {
        return KotlinVersion.CURRENT.isAtLeast(n, n2, n3);
    }
    
    @InlineOnly
    private static final <T> T castToBaseType(final Object o) {
        try {
            Intrinsics.reifiedOperationMarker(1, "T");
            return (T)o;
        }
        catch (final ClassCastException ex) {
            final ClassLoader classLoader = o.getClass().getClassLoader();
            Intrinsics.reifiedOperationMarker(4, "T");
            final ClassLoader classLoader2 = Object.class.getClassLoader();
            final StringBuilder sb = new StringBuilder();
            sb.append("Instance classloader: ");
            sb.append(classLoader);
            sb.append(", base type classloader: ");
            sb.append(classLoader2);
            final Throwable initCause = new ClassCastException(sb.toString()).initCause(ex);
            Intrinsics.checkExpressionValueIsNotNull(initCause, "ClassCastException(\"Inst\u2026baseTypeCL\").initCause(e)");
            throw initCause;
        }
    }
    
    private static final int getJavaVersion() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: invokestatic    java/lang/System.getProperty:(Ljava/lang/String;)Ljava/lang/String;
        //     5: astore          5
        //     7: ldc             65542
        //     9: istore_0       
        //    10: aload           5
        //    12: ifnull          167
        //    15: aload           5
        //    17: checkcast       Ljava/lang/CharSequence;
        //    20: astore          6
        //    22: aload           6
        //    24: bipush          46
        //    26: iconst_0       
        //    27: iconst_0       
        //    28: bipush          6
        //    30: aconst_null    
        //    31: invokestatic    kotlin/text/StringsKt.indexOf$default:(Ljava/lang/CharSequence;CIZILjava/lang/Object;)I
        //    34: istore          4
        //    36: iload           4
        //    38: ifge            54
        //    41: aload           5
        //    43: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //    46: istore_1       
        //    47: iload_1        
        //    48: ldc             65536
        //    50: imul           
        //    51: istore_0       
        //    52: iload_0        
        //    53: ireturn        
        //    54: iload           4
        //    56: iconst_1       
        //    57: iadd           
        //    58: istore_3       
        //    59: aload           6
        //    61: bipush          46
        //    63: iload_3        
        //    64: iconst_0       
        //    65: iconst_4       
        //    66: aconst_null    
        //    67: invokestatic    kotlin/text/StringsKt.indexOf$default:(Ljava/lang/CharSequence;CIZILjava/lang/Object;)I
        //    70: istore_2       
        //    71: iload_2        
        //    72: istore_1       
        //    73: iload_2        
        //    74: ifge            83
        //    77: aload           5
        //    79: invokevirtual   java/lang/String.length:()I
        //    82: istore_1       
        //    83: aload           5
        //    85: ifnonnull       98
        //    88: new             Lkotlin/TypeCastException;
        //    91: dup            
        //    92: ldc             "null cannot be cast to non-null type java.lang.String"
        //    94: invokespecial   kotlin/TypeCastException.<init>:(Ljava/lang/String;)V
        //    97: athrow         
        //    98: aload           5
        //   100: iconst_0       
        //   101: iload           4
        //   103: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   106: astore          6
        //   108: aload           6
        //   110: ldc             "(this as java.lang.Strin\u2026ing(startIndex, endIndex)"
        //   112: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   115: aload           5
        //   117: ifnonnull       130
        //   120: new             Lkotlin/TypeCastException;
        //   123: dup            
        //   124: ldc             "null cannot be cast to non-null type java.lang.String"
        //   126: invokespecial   kotlin/TypeCastException.<init>:(Ljava/lang/String;)V
        //   129: athrow         
        //   130: aload           5
        //   132: iload_3        
        //   133: iload_1        
        //   134: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   137: astore          5
        //   139: aload           5
        //   141: ldc             "(this as java.lang.Strin\u2026ing(startIndex, endIndex)"
        //   143: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   146: aload           6
        //   148: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //   151: istore_1       
        //   152: aload           5
        //   154: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //   157: istore_2       
        //   158: iload_1        
        //   159: ldc             65536
        //   161: imul           
        //   162: iload_2        
        //   163: iadd           
        //   164: istore_0       
        //   165: iload_0        
        //   166: ireturn        
        //   167: ldc             65542
        //   169: ireturn        
        //   170: astore          5
        //   172: goto            52
        //   175: astore          5
        //   177: goto            165
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                             
        //  -----  -----  -----  -----  ---------------------------------
        //  41     47     170    175    Ljava/lang/NumberFormatException;
        //  146    158    175    180    Ljava/lang/NumberFormatException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0165:
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
