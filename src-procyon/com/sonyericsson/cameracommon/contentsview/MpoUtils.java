// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.contentsview;

import com.sonyericsson.android.camera.util.CamLog;
import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class MpoUtils
{
    public static final int MULTIANGLE = 2;
    public static final int STEREO = 1;
    public static final String TAG = "MpoUtils";
    public static final int UNKNOWN = 0;
    
    private MpoUtils() {
    }
    
    static boolean checkFormatIdentifier(final RandomAccessFile randomAccessFile) throws IOException {
        final byte[] b = new byte[4];
        final int length = b.length;
        final int read = randomAccessFile.read(b);
        final boolean b2 = false;
        if (length != read) {
            return false;
        }
        boolean b3 = b2;
        if (b[0] == 77) {
            b3 = b2;
            if (b[1] == 80) {
                b3 = b2;
                if (b[2] == 70) {
                    b3 = b2;
                    if (b[3] == 0) {
                        b3 = true;
                    }
                }
            }
        }
        return b3;
    }
    
    static boolean checkMPEntryTag(final RandomAccessFile randomAccessFile) throws IOException {
        return 0xB002 == (randomAccessFile.readShort() & 0xFFFF);
    }
    
    public static int getType(final String p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore          9
        //     3: aconst_null    
        //     4: astore          10
        //     6: aload           10
        //     8: astore          7
        //    10: new             Ljava/io/RandomAccessFile;
        //    13: astore          8
        //    15: aload           10
        //    17: astore          7
        //    19: aload           8
        //    21: aload_0        
        //    22: ldc             "r"
        //    24: invokespecial   java/io/RandomAccessFile.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //    27: iconst_2       
        //    28: newarray        B
        //    30: astore          7
        //    32: iconst_2       
        //    33: aload           8
        //    35: aload           7
        //    37: invokevirtual   java/io/RandomAccessFile.read:([B)I
        //    40: if_icmpgt       439
        //    43: getstatic       com/sonyericsson/android/camera/util/CamLog.VERBOSE:Z
        //    46: ifeq            117
        //    49: new             Ljava/lang/StringBuilder;
        //    52: astore_0       
        //    53: aload_0        
        //    54: invokespecial   java/lang/StringBuilder.<init>:()V
        //    57: aload_0        
        //    58: ldc             "read:"
        //    60: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    63: pop            
        //    64: aload_0        
        //    65: aload           7
        //    67: iconst_0       
        //    68: baload         
        //    69: sipush          255
        //    72: iand           
        //    73: invokestatic    java/lang/Integer.toHexString:(I)Ljava/lang/String;
        //    76: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    79: pop            
        //    80: aload_0        
        //    81: ldc             " "
        //    83: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    86: pop            
        //    87: aload_0        
        //    88: aload           7
        //    90: iconst_1       
        //    91: baload         
        //    92: sipush          255
        //    95: iand           
        //    96: invokestatic    java/lang/Integer.toHexString:(I)Ljava/lang/String;
        //    99: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   102: pop            
        //   103: iconst_1       
        //   104: anewarray       Ljava/lang/String;
        //   107: dup            
        //   108: iconst_0       
        //   109: aload_0        
        //   110: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   113: aastore        
        //   114: invokestatic    com/sonyericsson/android/camera/util/CamLog.d:([Ljava/lang/String;)V
        //   117: aload           7
        //   119: iconst_0       
        //   120: baload         
        //   121: aload           7
        //   123: iconst_1       
        //   124: baload         
        //   125: invokestatic    com/sonyericsson/cameracommon/contentsview/MpoUtils.isSOI:(BB)Z
        //   128: ifeq            152
        //   131: getstatic       com/sonyericsson/android/camera/util/CamLog.VERBOSE:Z
        //   134: ifeq            32
        //   137: iconst_1       
        //   138: anewarray       Ljava/lang/String;
        //   141: dup            
        //   142: iconst_0       
        //   143: ldc             "This segments is SOI."
        //   145: aastore        
        //   146: invokestatic    com/sonyericsson/android/camera/util/CamLog.d:([Ljava/lang/String;)V
        //   149: goto            32
        //   152: aload           7
        //   154: iconst_0       
        //   155: baload         
        //   156: aload           7
        //   158: iconst_1       
        //   159: baload         
        //   160: invokestatic    com/sonyericsson/cameracommon/contentsview/MpoUtils.isEOI:(BB)Z
        //   163: ifeq            187
        //   166: getstatic       com/sonyericsson/android/camera/util/CamLog.VERBOSE:Z
        //   169: ifeq            32
        //   172: iconst_1       
        //   173: anewarray       Ljava/lang/String;
        //   176: dup            
        //   177: iconst_0       
        //   178: ldc             "This segments is EOI."
        //   180: aastore        
        //   181: invokestatic    com/sonyericsson/android/camera/util/CamLog.d:([Ljava/lang/String;)V
        //   184: goto            32
        //   187: aload           7
        //   189: iconst_0       
        //   190: baload         
        //   191: aload           7
        //   193: iconst_1       
        //   194: baload         
        //   195: invokestatic    com/sonyericsson/cameracommon/contentsview/MpoUtils.isAPP:(BB)Z
        //   198: ifeq            421
        //   201: getstatic       com/sonyericsson/android/camera/util/CamLog.VERBOSE:Z
        //   204: ifeq            242
        //   207: iconst_1       
        //   208: anewarray       Ljava/lang/String;
        //   211: dup            
        //   212: iconst_0       
        //   213: getstatic       java/util/Locale.UK:Ljava/util/Locale;
        //   216: ldc             "This segments is APP%d."
        //   218: iconst_1       
        //   219: anewarray       Ljava/lang/Object;
        //   222: dup            
        //   223: iconst_0       
        //   224: aload           7
        //   226: iconst_1       
        //   227: baload         
        //   228: bipush          32
        //   230: iadd           
        //   231: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   234: aastore        
        //   235: invokestatic    java/lang/String.format:(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //   238: aastore        
        //   239: invokestatic    com/sonyericsson/android/camera/util/CamLog.d:([Ljava/lang/String;)V
        //   242: aload           8
        //   244: invokevirtual   java/io/RandomAccessFile.getFilePointer:()J
        //   247: lstore_3       
        //   248: aload           8
        //   250: invokevirtual   java/io/RandomAccessFile.readShort:()S
        //   253: i2l            
        //   254: lstore          5
        //   256: aload           7
        //   258: iconst_0       
        //   259: baload         
        //   260: aload           7
        //   262: iconst_1       
        //   263: baload         
        //   264: invokestatic    com/sonyericsson/cameracommon/contentsview/MpoUtils.isAPP2:(BB)Z
        //   267: ifeq            409
        //   270: aload           8
        //   272: invokestatic    com/sonyericsson/cameracommon/contentsview/MpoUtils.checkFormatIdentifier:(Ljava/io/RandomAccessFile;)Z
        //   275: ifeq            409
        //   278: getstatic       com/sonyericsson/android/camera/util/CamLog.VERBOSE:Z
        //   281: ifeq            296
        //   284: iconst_1       
        //   285: anewarray       Ljava/lang/String;
        //   288: dup            
        //   289: iconst_0       
        //   290: ldc             "This section has MPF."
        //   292: aastore        
        //   293: invokestatic    com/sonyericsson/android/camera/util/CamLog.d:([Ljava/lang/String;)V
        //   296: aload           8
        //   298: invokevirtual   java/io/RandomAccessFile.readShort:()S
        //   301: pop            
        //   302: aload           8
        //   304: bipush          6
        //   306: invokestatic    com/sonyericsson/cameracommon/contentsview/MpoUtils.skip:(Ljava/io/RandomAccessFile;I)V
        //   309: aload           8
        //   311: invokevirtual   java/io/RandomAccessFile.readShort:()S
        //   314: istore_2       
        //   315: iconst_0       
        //   316: istore_1       
        //   317: iload_1        
        //   318: iload_2        
        //   319: if_icmpge       409
        //   322: aload           8
        //   324: invokestatic    com/sonyericsson/cameracommon/contentsview/MpoUtils.checkMPEntryTag:(Ljava/io/RandomAccessFile;)Z
        //   327: ifeq            378
        //   330: getstatic       com/sonyericsson/android/camera/util/CamLog.VERBOSE:Z
        //   333: ifeq            348
        //   336: iconst_1       
        //   337: anewarray       Ljava/lang/String;
        //   340: dup            
        //   341: iconst_0       
        //   342: ldc             "This tag is MP entry."
        //   344: aastore        
        //   345: invokestatic    com/sonyericsson/android/camera/util/CamLog.d:([Ljava/lang/String;)V
        //   348: aload           8
        //   350: iconst_2       
        //   351: invokestatic    com/sonyericsson/cameracommon/contentsview/MpoUtils.skip:(Ljava/io/RandomAccessFile;I)V
        //   354: aload           8
        //   356: invokevirtual   java/io/RandomAccessFile.readInt:()I
        //   359: bipush          16
        //   361: idiv           
        //   362: invokestatic    com/sonyericsson/cameracommon/contentsview/MpoUtils.typeFromEntries:(I)I
        //   365: istore_1       
        //   366: aload           8
        //   368: ifnull          376
        //   371: aload           8
        //   373: invokevirtual   java/io/RandomAccessFile.close:()V
        //   376: iload_1        
        //   377: ireturn        
        //   378: getstatic       com/sonyericsson/android/camera/util/CamLog.VERBOSE:Z
        //   381: ifeq            396
        //   384: iconst_1       
        //   385: anewarray       Ljava/lang/String;
        //   388: dup            
        //   389: iconst_0       
        //   390: ldc             "This tag is not MP entry."
        //   392: aastore        
        //   393: invokestatic    com/sonyericsson/android/camera/util/CamLog.d:([Ljava/lang/String;)V
        //   396: aload           8
        //   398: bipush          10
        //   400: invokestatic    com/sonyericsson/cameracommon/contentsview/MpoUtils.skip:(Ljava/io/RandomAccessFile;I)V
        //   403: iinc            1, 1
        //   406: goto            317
        //   409: aload           8
        //   411: lload_3        
        //   412: lload           5
        //   414: ladd           
        //   415: invokevirtual   java/io/RandomAccessFile.seek:(J)V
        //   418: goto            32
        //   421: getstatic       com/sonyericsson/android/camera/util/CamLog.VERBOSE:Z
        //   424: ifeq            439
        //   427: iconst_1       
        //   428: anewarray       Ljava/lang/String;
        //   431: dup            
        //   432: iconst_0       
        //   433: ldc             "Found unknown marker."
        //   435: aastore        
        //   436: invokestatic    com/sonyericsson/android/camera/util/CamLog.d:([Ljava/lang/String;)V
        //   439: aload           8
        //   441: ifnull          548
        //   444: aload           8
        //   446: invokevirtual   java/io/RandomAccessFile.close:()V
        //   449: goto            548
        //   452: astore_0       
        //   453: goto            568
        //   456: astore          7
        //   458: aload           8
        //   460: astore_0       
        //   461: aload           7
        //   463: astore          8
        //   465: goto            481
        //   468: astore_0       
        //   469: aload           7
        //   471: astore          8
        //   473: goto            568
        //   476: astore          8
        //   478: aload           9
        //   480: astore_0       
        //   481: aload_0        
        //   482: astore          7
        //   484: new             Ljava/lang/StringBuilder;
        //   487: astore          9
        //   489: aload_0        
        //   490: astore          7
        //   492: aload           9
        //   494: invokespecial   java/lang/StringBuilder.<init>:()V
        //   497: aload_0        
        //   498: astore          7
        //   500: aload           9
        //   502: ldc             "Fail to analize a mpo file by IO Exception. message:"
        //   504: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   507: pop            
        //   508: aload_0        
        //   509: astore          7
        //   511: aload           9
        //   513: aload           8
        //   515: invokevirtual   java/io/IOException.getMessage:()Ljava/lang/String;
        //   518: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   521: pop            
        //   522: aload_0        
        //   523: astore          7
        //   525: iconst_1       
        //   526: anewarray       Ljava/lang/String;
        //   529: dup            
        //   530: iconst_0       
        //   531: aload           9
        //   533: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   536: aastore        
        //   537: invokestatic    com/sonyericsson/android/camera/util/CamLog.e:([Ljava/lang/String;)V
        //   540: aload_0        
        //   541: ifnull          548
        //   544: aload_0        
        //   545: invokevirtual   java/io/RandomAccessFile.close:()V
        //   548: getstatic       com/sonyericsson/android/camera/util/CamLog.VERBOSE:Z
        //   551: ifeq            566
        //   554: iconst_1       
        //   555: anewarray       Ljava/lang/String;
        //   558: dup            
        //   559: iconst_0       
        //   560: ldc             "This mpo is unknown image."
        //   562: aastore        
        //   563: invokestatic    com/sonyericsson/android/camera/util/CamLog.d:([Ljava/lang/String;)V
        //   566: iconst_0       
        //   567: ireturn        
        //   568: aload           8
        //   570: ifnull          578
        //   573: aload           8
        //   575: invokevirtual   java/io/RandomAccessFile.close:()V
        //   578: aload_0        
        //   579: athrow         
        //   580: astore_0       
        //   581: goto            376
        //   584: astore_0       
        //   585: goto            548
        //   588: astore          7
        //   590: goto            578
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  10     15     476    481    Ljava/io/IOException;
        //  10     15     468    476    Any
        //  19     27     476    481    Ljava/io/IOException;
        //  19     27     468    476    Any
        //  27     32     456    468    Ljava/io/IOException;
        //  27     32     452    456    Any
        //  32     117    456    468    Ljava/io/IOException;
        //  32     117    452    456    Any
        //  117    149    456    468    Ljava/io/IOException;
        //  117    149    452    456    Any
        //  152    184    456    468    Ljava/io/IOException;
        //  152    184    452    456    Any
        //  187    242    456    468    Ljava/io/IOException;
        //  187    242    452    456    Any
        //  242    296    456    468    Ljava/io/IOException;
        //  242    296    452    456    Any
        //  296    315    456    468    Ljava/io/IOException;
        //  296    315    452    456    Any
        //  322    348    456    468    Ljava/io/IOException;
        //  322    348    452    456    Any
        //  348    366    456    468    Ljava/io/IOException;
        //  348    366    452    456    Any
        //  371    376    580    584    Ljava/io/IOException;
        //  378    396    456    468    Ljava/io/IOException;
        //  378    396    452    456    Any
        //  396    403    456    468    Ljava/io/IOException;
        //  396    403    452    456    Any
        //  409    418    456    468    Ljava/io/IOException;
        //  409    418    452    456    Any
        //  421    439    456    468    Ljava/io/IOException;
        //  421    439    452    456    Any
        //  444    449    584    588    Ljava/io/IOException;
        //  484    489    468    476    Any
        //  492    497    468    476    Any
        //  500    508    468    476    Any
        //  511    522    468    476    Any
        //  525    540    468    476    Any
        //  544    548    584    588    Ljava/io/IOException;
        //  573    578    588    593    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.util.ConcurrentModificationException
        //     at java.base/java.util.ArrayList$Itr.checkForComodification(ArrayList.java:1095)
        //     at java.base/java.util.ArrayList$Itr.next(ArrayList.java:1049)
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2913)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2501)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:203)
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
    
    static boolean isAPP(final byte b, final byte b2) {
        return b == -1 && b2 >= -32 && b2 <= -17;
    }
    
    static boolean isAPP2(final byte b, final byte b2) {
        return b == -1 && b2 == -30;
    }
    
    static boolean isEOI(final byte b, final byte b2) {
        return b == -1 && b2 == -39;
    }
    
    static boolean isSOI(final byte b, final byte b2) {
        return b == -1 && b2 == -40;
    }
    
    static void skip(final RandomAccessFile randomAccessFile, final int n) throws EOFException, IOException {
        if (n != randomAccessFile.skipBytes(n)) {
            throw new EOFException();
        }
    }
    
    static int typeFromEntries(final int i) {
        if (i == 2) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("This mpo is stereo image. entries:");
                sb.append(i);
                CamLog.d(sb.toString());
            }
            return 1;
        }
        if (i == 15) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("This mpo is multi angle image. entries:");
                sb2.append(i);
                CamLog.d(sb2.toString());
            }
            return 2;
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("This mpo is unknown image. entries:");
            sb3.append(i);
            CamLog.d(sb3.toString());
        }
        return 0;
    }
    
    private static class JpegMaker
    {
        static final byte APP0 = -32;
        static final byte APP15 = -17;
        static final byte APP2 = -30;
        static final byte EOI = -39;
        static final byte MARKER = -1;
        static final byte SOI = -40;
    }
}
