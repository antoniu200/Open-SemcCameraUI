// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.contentsview;

import android.graphics.Matrix;
import android.net.Uri;
import android.content.Context;
import android.media.ThumbnailUtils;
import android.graphics.Bitmap$Config;
import android.graphics.BitmapFactory;
import com.sonyericsson.android.camera.util.CamLog;
import android.graphics.Bitmap;
import com.sonyericsson.cameracommon.contentsview.contents.Content;
import android.graphics.BitmapFactory$Options;

public class ThumbnailFactory
{
    private static final int MAX_NUM_PIXELS_MICRO_THUMBNAIL = 19200;
    public static final String TAG = "ThumbnailFactory";
    public static final int TARGET_SIZE_MICRO_THUMBNAIL = 96;
    private static final int UNCONSTRAINED = -1;
    
    private static int computeInitialSampleSize(final BitmapFactory$Options bitmapFactory$Options, final int n, final int n2) {
        final double n3 = bitmapFactory$Options.outWidth;
        final double n4 = bitmapFactory$Options.outHeight;
        int n5;
        if (n2 == -1) {
            n5 = 1;
        }
        else {
            n5 = (int)Math.ceil(Math.sqrt(n3 * n4 / n2));
        }
        int n6;
        if (n == -1) {
            n6 = 128;
        }
        else {
            final double n7 = n;
            n6 = (int)Math.min(Math.floor(n3 / n7), Math.floor(n4 / n7));
        }
        if (n6 < n5) {
            return n5;
        }
        if (n2 == -1 && n == -1) {
            return 1;
        }
        if (n == -1) {
            return n5;
        }
        return n6;
    }
    
    private static int computeSampleSize(final BitmapFactory$Options bitmapFactory$Options, int n, int n2) {
        final int computeInitialSampleSize = computeInitialSampleSize(bitmapFactory$Options, n, n2);
        if (computeInitialSampleSize <= 8) {
            n = 1;
            while (true) {
                n2 = n;
                if (n >= computeInitialSampleSize) {
                    break;
                }
                n <<= 1;
            }
        }
        else {
            n2 = 8 * ((computeInitialSampleSize + 7) / 8);
        }
        return n2;
    }
    
    public static Bitmap createMicroThumbnail(final Content.ContentInfo contentInfo) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("createMicroThumbnail(type:");
            sb.append(contentInfo.mType);
            sb.append(",id;");
            sb.append(contentInfo.mId);
            sb.append(",data:");
            sb.append(contentInfo.mOriginalPath);
            sb.append(")");
            CamLog.d(sb.toString());
        }
        Bitmap thumbnail = null;
        Label_0354: {
            Label_0319: {
                try {
                    Bitmap bitmap = null;
                    switch (contentInfo.mType) {
                        default: {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("createMicroThumbnail() wrong type:");
                            sb2.append(contentInfo.mType);
                            CamLog.e(sb2.toString());
                            bitmap = null;
                            break;
                        }
                        case 2: {
                            bitmap = createVideoThumbnail(contentInfo.mOriginalPath);
                            break;
                        }
                        case 1:
                        case 3: {
                            final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
                            bitmapFactory$Options.inSampleSize = 1;
                            bitmapFactory$Options.inJustDecodeBounds = true;
                            BitmapFactory.decodeFile(contentInfo.mOriginalPath, bitmapFactory$Options);
                            if (!bitmapFactory$Options.mCancel && bitmapFactory$Options.outWidth != -1 && bitmapFactory$Options.outHeight != -1) {
                                bitmapFactory$Options.inSampleSize = computeSampleSize(bitmapFactory$Options, 96, 19200);
                                bitmapFactory$Options.inJustDecodeBounds = false;
                                bitmapFactory$Options.inDither = false;
                                bitmapFactory$Options.inPreferredConfig = Bitmap$Config.ARGB_8888;
                                bitmap = BitmapFactory.decodeFile(contentInfo.mOriginalPath, bitmapFactory$Options);
                                break;
                            }
                            return null;
                        }
                    }
                    if (bitmap != null) {
                        thumbnail = ThumbnailUtils.extractThumbnail(bitmap, 96, 96);
                        try {
                            bitmap.recycle();
                            break Label_0354;
                        }
                        catch (final OutOfMemoryError outOfMemoryError) {
                            goto Label_0299;
                        }
                        catch (final Exception obj) {
                            break Label_0319;
                        }
                    }
                    thumbnail = null;
                    break Label_0354;
                }
                catch (final OutOfMemoryError outOfMemoryError) {
                    thumbnail = null;
                }
                catch (final Exception obj) {
                    thumbnail = null;
                }
            }
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("createMicroThumbnail() got exception ex :");
            final Exception obj;
            sb3.append(obj);
            CamLog.e(sb3.toString());
        }
        if (thumbnail == null) {
            CamLog.e("createMicroThumbnail() can't create a Micro thumbnail.");
            return null;
        }
        return rotateThumbnail(thumbnail, contentInfo.mOrientation);
    }
    
    public static Bitmap createVideoThumbnail(final Context p0, final Uri p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: invokespecial   android/media/MediaMetadataRetriever.<init>:()V
        //     7: astore_2       
        //     8: aload_2        
        //     9: aload_0        
        //    10: aload_1        
        //    11: invokevirtual   android/media/MediaMetadataRetriever.setDataSource:(Landroid/content/Context;Landroid/net/Uri;)V
        //    14: aload_2        
        //    15: ldc2_w          -1
        //    18: invokevirtual   android/media/MediaMetadataRetriever.getFrameAtTime:(J)Landroid/graphics/Bitmap;
        //    21: astore_0       
        //    22: aload_2        
        //    23: invokevirtual   android/media/MediaMetadataRetriever.release:()V
        //    26: goto            100
        //    29: astore_1       
        //    30: iconst_1       
        //    31: anewarray       Ljava/lang/String;
        //    34: dup            
        //    35: iconst_0       
        //    36: ldc             "Ignore failures while cleaning up."
        //    38: aastore        
        //    39: invokestatic    com/sonyericsson/android/camera/util/CamLog.e:([Ljava/lang/String;)V
        //    42: goto            100
        //    45: astore_0       
        //    46: goto            102
        //    49: astore_0       
        //    50: iconst_1       
        //    51: anewarray       Ljava/lang/String;
        //    54: dup            
        //    55: iconst_0       
        //    56: ldc             "Assume this is a corrupt video file."
        //    58: aastore        
        //    59: invokestatic    com/sonyericsson/android/camera/util/CamLog.e:([Ljava/lang/String;)V
        //    62: goto            78
        //    65: astore_0       
        //    66: iconst_1       
        //    67: anewarray       Ljava/lang/String;
        //    70: dup            
        //    71: iconst_0       
        //    72: ldc             "Assume this is a corrupt video file."
        //    74: aastore        
        //    75: invokestatic    com/sonyericsson/android/camera/util/CamLog.e:([Ljava/lang/String;)V
        //    78: aload_2        
        //    79: invokevirtual   android/media/MediaMetadataRetriever.release:()V
        //    82: goto            98
        //    85: astore_0       
        //    86: iconst_1       
        //    87: anewarray       Ljava/lang/String;
        //    90: dup            
        //    91: iconst_0       
        //    92: ldc             "Ignore failures while cleaning up."
        //    94: aastore        
        //    95: invokestatic    com/sonyericsson/android/camera/util/CamLog.e:([Ljava/lang/String;)V
        //    98: aconst_null    
        //    99: astore_0       
        //   100: aload_0        
        //   101: areturn        
        //   102: aload_2        
        //   103: invokevirtual   android/media/MediaMetadataRetriever.release:()V
        //   106: goto            122
        //   109: astore_1       
        //   110: iconst_1       
        //   111: anewarray       Ljava/lang/String;
        //   114: dup            
        //   115: iconst_0       
        //   116: ldc             "Ignore failures while cleaning up."
        //   118: aastore        
        //   119: invokestatic    com/sonyericsson/android/camera/util/CamLog.e:([Ljava/lang/String;)V
        //   122: aload_0        
        //   123: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                
        //  -----  -----  -----  -----  ------------------------------------
        //  8      22     65     78     Ljava/lang/IllegalArgumentException;
        //  8      22     49     65     Ljava/lang/RuntimeException;
        //  8      22     45     124    Any
        //  22     26     29     45     Ljava/lang/RuntimeException;
        //  50     62     45     124    Any
        //  66     78     45     124    Any
        //  78     82     85     98     Ljava/lang/RuntimeException;
        //  102    106    109    122    Ljava/lang/RuntimeException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 71 out of bounds for length 71
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:100)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:106)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:302)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:385)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:427)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3362)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:112)
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
    
    public static Bitmap createVideoThumbnail(final Context context, final Uri uri, final int n) {
        return rotateThumbnail(createVideoThumbnail(context, uri), n);
    }
    
    public static Bitmap createVideoThumbnail(final String p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: invokespecial   android/media/MediaMetadataRetriever.<init>:()V
        //     7: astore_1       
        //     8: aload_1        
        //     9: aload_0        
        //    10: invokevirtual   android/media/MediaMetadataRetriever.setDataSource:(Ljava/lang/String;)V
        //    13: aload_1        
        //    14: ldc2_w          -1
        //    17: invokevirtual   android/media/MediaMetadataRetriever.getFrameAtTime:(J)Landroid/graphics/Bitmap;
        //    20: astore_0       
        //    21: aload_1        
        //    22: invokevirtual   android/media/MediaMetadataRetriever.release:()V
        //    25: goto            99
        //    28: astore_1       
        //    29: iconst_1       
        //    30: anewarray       Ljava/lang/String;
        //    33: dup            
        //    34: iconst_0       
        //    35: ldc             "Ignore failures while cleaning up."
        //    37: aastore        
        //    38: invokestatic    com/sonyericsson/android/camera/util/CamLog.e:([Ljava/lang/String;)V
        //    41: goto            99
        //    44: astore_0       
        //    45: goto            101
        //    48: astore_0       
        //    49: iconst_1       
        //    50: anewarray       Ljava/lang/String;
        //    53: dup            
        //    54: iconst_0       
        //    55: ldc             "Assume this is a corrupt video file."
        //    57: aastore        
        //    58: invokestatic    com/sonyericsson/android/camera/util/CamLog.e:([Ljava/lang/String;)V
        //    61: goto            77
        //    64: astore_0       
        //    65: iconst_1       
        //    66: anewarray       Ljava/lang/String;
        //    69: dup            
        //    70: iconst_0       
        //    71: ldc             "Assume this is a corrupt video file."
        //    73: aastore        
        //    74: invokestatic    com/sonyericsson/android/camera/util/CamLog.e:([Ljava/lang/String;)V
        //    77: aload_1        
        //    78: invokevirtual   android/media/MediaMetadataRetriever.release:()V
        //    81: goto            97
        //    84: astore_0       
        //    85: iconst_1       
        //    86: anewarray       Ljava/lang/String;
        //    89: dup            
        //    90: iconst_0       
        //    91: ldc             "Ignore failures while cleaning up."
        //    93: aastore        
        //    94: invokestatic    com/sonyericsson/android/camera/util/CamLog.e:([Ljava/lang/String;)V
        //    97: aconst_null    
        //    98: astore_0       
        //    99: aload_0        
        //   100: areturn        
        //   101: aload_1        
        //   102: invokevirtual   android/media/MediaMetadataRetriever.release:()V
        //   105: goto            121
        //   108: astore_1       
        //   109: iconst_1       
        //   110: anewarray       Ljava/lang/String;
        //   113: dup            
        //   114: iconst_0       
        //   115: ldc             "Ignore failures while cleaning up."
        //   117: aastore        
        //   118: invokestatic    com/sonyericsson/android/camera/util/CamLog.e:([Ljava/lang/String;)V
        //   121: aload_0        
        //   122: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                
        //  -----  -----  -----  -----  ------------------------------------
        //  8      21     64     77     Ljava/lang/IllegalArgumentException;
        //  8      21     48     64     Ljava/lang/RuntimeException;
        //  8      21     44     123    Any
        //  21     25     28     44     Ljava/lang/RuntimeException;
        //  49     61     44     123    Any
        //  65     77     44     123    Any
        //  77     81     84     97     Ljava/lang/RuntimeException;
        //  101    105    108    121    Ljava/lang/RuntimeException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 70 out of bounds for length 70
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:100)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:106)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:302)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:385)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:427)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3362)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:112)
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
    
    private static Bitmap rotateThumbnail(final Bitmap bitmap, final int n) {
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        Bitmap bitmap2 = bitmap;
        if (n != 0) {
            try {
                final Matrix matrix = new Matrix();
                matrix.setRotate((float)n, width / 2.0f, height / 2.0f);
                bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
                bitmap.recycle();
            }
            catch (final Exception ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Exception : width = ");
                sb.append(width);
                sb.append(", height = ");
                sb.append(height);
                CamLog.e(sb.toString());
                bitmap2 = bitmap;
            }
            catch (final IllegalArgumentException ex2) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("IllegalArgumentException : width = ");
                sb2.append(width);
                sb2.append(", height = ");
                sb2.append(height);
                CamLog.e(sb2.toString());
                bitmap2 = bitmap;
            }
        }
        return bitmap2;
    }
    
    public static boolean tryCreateThumbnail(final String str) {
        final boolean verbose = CamLog.VERBOSE;
        boolean b = false;
        if (verbose) {
            final StringBuilder sb = new StringBuilder();
            sb.append("tryCreateThumbnail(");
            sb.append(str);
            sb.append(")");
            CamLog.d(sb.toString());
        }
        try {
            final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
            bitmapFactory$Options.inSampleSize = 1;
            bitmapFactory$Options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(str, bitmapFactory$Options);
            if (bitmapFactory$Options.mCancel || bitmapFactory$Options.outWidth == -1 || bitmapFactory$Options.outHeight == -1) {
                return false;
            }
            b = true;
        }
        catch (final Exception ex) {
            CamLog.e("createMicroThumbnail() : ", ex);
        }
        return b;
    }
}
