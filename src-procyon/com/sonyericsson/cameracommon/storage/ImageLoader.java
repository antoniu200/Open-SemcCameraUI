// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.storage;

import android.graphics.Matrix;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import java.io.FileNotFoundException;
import android.graphics.Bitmap;
import java.io.InvalidObjectException;
import android.graphics.Bitmap$Config;
import com.sonyericsson.android.camera.util.CamLog;
import java.io.InputStream;
import android.net.Uri;
import android.graphics.BitmapFactory$Options;
import android.content.Context;

public class ImageLoader
{
    private static final int FIRST_REDUCE_RATIO_FULL_IMG = 2;
    private static final int FULL_SIZE_MAX_LENGTH = 1025;
    public static final String TAG = "ImageLoader";
    private final Context mContext;
    private final byte[] mImageData;
    private final BitmapFactory$Options mOption;
    private final int mOrientation;
    private final Uri mUri;
    
    public ImageLoader(final Context mContext, final Uri mUri, final int mOrientation) {
        this.mContext = mContext;
        this.mUri = mUri;
        this.mImageData = null;
        this.mOrientation = mOrientation;
        this.mOption = new BitmapFactory$Options();
    }
    
    public ImageLoader(final Context mContext, final byte[] mImageData, final int mOrientation) {
        this.mContext = mContext;
        this.mUri = null;
        this.mImageData = mImageData;
        this.mOrientation = mOrientation;
        this.mOption = new BitmapFactory$Options();
    }
    
    private void calcBounds(final InputStream inputStream, final BitmapFactory$Options bitmapFactory$Options) throws InvalidObjectException, FileNotFoundException {
        if (CamLog.VERBOSE) {
            CamLog.d("calcBounds()");
        }
        bitmapFactory$Options.inSampleSize = 2;
        bitmapFactory$Options.inJustDecodeBounds = true;
        bitmapFactory$Options.inPreferredConfig = Bitmap$Config.RGB_565;
        final Bitmap decodeStream = this.decodeStream(inputStream, bitmapFactory$Options);
        if (decodeStream != null && !decodeStream.isRecycled()) {
            decodeStream.recycle();
        }
        if (bitmapFactory$Options.outWidth != -1 && bitmapFactory$Options.outHeight != -1) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("BMP out height:");
                sb.append(bitmapFactory$Options.outHeight);
                CamLog.d(sb.toString());
            }
            if (CamLog.VERBOSE) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("BMP out width:");
                sb2.append(bitmapFactory$Options.outWidth);
                CamLog.d(sb2.toString());
            }
            if (CamLog.VERBOSE) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Scale ratio:");
                sb3.append(bitmapFactory$Options.inSampleSize);
                CamLog.d(sb3.toString());
            }
            return;
        }
        CamLog.e("Bitmap read error");
        throw new InvalidObjectException("Failed to calculate bounds of bitmap");
    }
    
    private int calcRatio(final BitmapFactory$Options bitmapFactory$Options, int n, final int n2) {
        final int n3 = bitmapFactory$Options.outWidth * n;
        final int n4 = bitmapFactory$Options.outHeight * n;
        n = (n3 + n2 - 1) / n2;
        final int max = Math.max((n4 + n2 - 1) / n2, n);
        if (max == 0) {
            if (CamLog.VERBOSE) {
                CamLog.d("Full size image loading ratio: error");
            }
            return 1;
        }
        Label_0099: {
            if ((n = max) > 1) {
                if (n3 / max <= n2) {
                    n = max;
                    if (n4 / max <= n2) {
                        break Label_0099;
                    }
                }
                n = max - 1;
            }
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Full size image loading ratio:");
            sb.append(n);
            CamLog.d(sb.toString());
        }
        return n;
    }
    
    private Bitmap decodeStream(final InputStream inputStream, final BitmapFactory$Options bitmapFactory$Options) throws FileNotFoundException {
        final Rect rect = new Rect(0, 0, 0, 0);
        if (CamLog.VERBOSE) {
            CamLog.d("Loading full size image started");
        }
        final Bitmap decodeStream = BitmapFactory.decodeStream(inputStream, rect, bitmapFactory$Options);
        if (CamLog.VERBOSE) {
            CamLog.d("Loading full size image finished");
        }
        return decodeStream;
    }
    
    private Bitmap loadFullSize(final InputStream inputStream, final BitmapFactory$Options bitmapFactory$Options) throws FileNotFoundException, InvalidObjectException {
        if (CamLog.VERBOSE) {
            CamLog.d("loadFullSize()");
        }
        bitmapFactory$Options.inJustDecodeBounds = false;
        bitmapFactory$Options.inDither = false;
        bitmapFactory$Options.inPreferredConfig = Bitmap$Config.ARGB_8888;
        final Bitmap decodeStream = this.decodeStream(inputStream, bitmapFactory$Options);
        if (decodeStream == null) {
            CamLog.e("loadFullSize: Decode read error");
            throw new InvalidObjectException("Failed to decode full size image");
        }
        CamLog.d("loadFullSize: mOrientation", RotationUtil.orientationToString(this.mOrientation));
        Bitmap copy = decodeStream;
        if (this.mOrientation != 0) {
            final Matrix matrix = new Matrix();
            matrix.setRotate((float)this.mOrientation, decodeStream.getWidth() / 2.0f, decodeStream.getHeight() / 2.0f);
            final Bitmap bitmap = Bitmap.createBitmap(decodeStream, 0, 0, decodeStream.getWidth(), decodeStream.getHeight(), matrix, false);
            decodeStream.recycle();
            copy = bitmap.copy(Bitmap$Config.ARGB_8888, false);
            bitmap.recycle();
        }
        return copy;
    }
    
    public Bitmap load() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: ifeq            18
        //     6: iconst_1       
        //     7: anewarray       Ljava/lang/String;
        //    10: dup            
        //    11: iconst_0       
        //    12: ldc             "Loading full size image started"
        //    14: aastore        
        //    15: invokestatic    com/sonyericsson/android/camera/util/CamLog.d:([Ljava/lang/String;)V
        //    18: aconst_null    
        //    19: astore          4
        //    21: aconst_null    
        //    22: astore          6
        //    24: aconst_null    
        //    25: astore          5
        //    27: aconst_null    
        //    28: astore_2       
        //    29: aconst_null    
        //    30: astore          11
        //    32: aconst_null    
        //    33: astore          9
        //    35: aconst_null    
        //    36: astore          10
        //    38: aconst_null    
        //    39: astore          8
        //    41: aconst_null    
        //    42: astore          7
        //    44: aconst_null    
        //    45: astore_3       
        //    46: aload           11
        //    48: astore_1       
        //    49: getstatic       com/sonyericsson/android/camera/util/CamLog.VERBOSE:Z
        //    52: ifeq            113
        //    55: aload           11
        //    57: astore_1       
        //    58: new             Ljava/lang/StringBuilder;
        //    61: astore          12
        //    63: aload           11
        //    65: astore_1       
        //    66: aload           12
        //    68: invokespecial   java/lang/StringBuilder.<init>:()V
        //    71: aload           11
        //    73: astore_1       
        //    74: aload           12
        //    76: ldc             "Start loading original image:"
        //    78: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    81: pop            
        //    82: aload           11
        //    84: astore_1       
        //    85: aload           12
        //    87: aload_0        
        //    88: getfield        com/sonyericsson/cameracommon/storage/ImageLoader.mUri:Landroid/net/Uri;
        //    91: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //    94: pop            
        //    95: aload           11
        //    97: astore_1       
        //    98: iconst_1       
        //    99: anewarray       Ljava/lang/String;
        //   102: dup            
        //   103: iconst_0       
        //   104: aload           12
        //   106: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   109: aastore        
        //   110: invokestatic    com/sonyericsson/android/camera/util/CamLog.d:([Ljava/lang/String;)V
        //   113: aload           11
        //   115: astore_1       
        //   116: aload_0        
        //   117: getfield        com/sonyericsson/cameracommon/storage/ImageLoader.mImageData:[B
        //   120: ifnull          149
        //   123: aload           11
        //   125: astore_1       
        //   126: new             Ljava/io/ByteArrayInputStream;
        //   129: astore          12
        //   131: aload           11
        //   133: astore_1       
        //   134: aload           12
        //   136: aload_0        
        //   137: getfield        com/sonyericsson/cameracommon/storage/ImageLoader.mImageData:[B
        //   140: invokespecial   java/io/ByteArrayInputStream.<init>:([B)V
        //   143: aload           12
        //   145: astore_1       
        //   146: goto            168
        //   149: aload           11
        //   151: astore_1       
        //   152: aload_0        
        //   153: getfield        com/sonyericsson/cameracommon/storage/ImageLoader.mContext:Landroid/content/Context;
        //   156: aload_0        
        //   157: getfield        com/sonyericsson/cameracommon/storage/ImageLoader.mUri:Landroid/net/Uri;
        //   160: invokestatic    com/sonyericsson/cameracommon/storage/ContentResolverUtil.crOpenInputStream:(Landroid/content/Context;Landroid/net/Uri;)Ljava/io/InputStream;
        //   163: astore          11
        //   165: aload           11
        //   167: astore_1       
        //   168: aload_1        
        //   169: ifnull          250
        //   172: aload_0        
        //   173: aload_1        
        //   174: aload_0        
        //   175: getfield        com/sonyericsson/cameracommon/storage/ImageLoader.mOption:Landroid/graphics/BitmapFactory$Options;
        //   178: invokespecial   com/sonyericsson/cameracommon/storage/ImageLoader.calcBounds:(Ljava/io/InputStream;Landroid/graphics/BitmapFactory$Options;)V
        //   181: aload_0        
        //   182: getfield        com/sonyericsson/cameracommon/storage/ImageLoader.mOption:Landroid/graphics/BitmapFactory$Options;
        //   185: aload_0        
        //   186: aload_0        
        //   187: getfield        com/sonyericsson/cameracommon/storage/ImageLoader.mOption:Landroid/graphics/BitmapFactory$Options;
        //   190: aload_0        
        //   191: getfield        com/sonyericsson/cameracommon/storage/ImageLoader.mOption:Landroid/graphics/BitmapFactory$Options;
        //   194: getfield        android/graphics/BitmapFactory$Options.inSampleSize:I
        //   197: sipush          1025
        //   200: invokespecial   com/sonyericsson/cameracommon/storage/ImageLoader.calcRatio:(Landroid/graphics/BitmapFactory$Options;II)I
        //   203: putfield        android/graphics/BitmapFactory$Options.inSampleSize:I
        //   206: aload_1        
        //   207: invokevirtual   java/io/InputStream.close:()V
        //   210: goto            250
        //   213: astore_2       
        //   214: goto            796
        //   217: astore_2       
        //   218: aconst_null    
        //   219: astore_2       
        //   220: aload_1        
        //   221: astore_3       
        //   222: goto            458
        //   225: astore_2       
        //   226: aconst_null    
        //   227: astore_2       
        //   228: aload_1        
        //   229: astore_3       
        //   230: goto            534
        //   233: astore_2       
        //   234: aconst_null    
        //   235: astore_2       
        //   236: aload_1        
        //   237: astore_3       
        //   238: goto            623
        //   241: astore          4
        //   243: aconst_null    
        //   244: astore_2       
        //   245: aload_1        
        //   246: astore_3       
        //   247: goto            713
        //   250: aload_0        
        //   251: getfield        com/sonyericsson/cameracommon/storage/ImageLoader.mImageData:[B
        //   254: ifnull          277
        //   257: new             Ljava/io/ByteArrayInputStream;
        //   260: astore          7
        //   262: aload           7
        //   264: aload_0        
        //   265: getfield        com/sonyericsson/cameracommon/storage/ImageLoader.mImageData:[B
        //   268: invokespecial   java/io/ByteArrayInputStream.<init>:([B)V
        //   271: aload           7
        //   273: astore_1       
        //   274: goto            293
        //   277: aload_0        
        //   278: getfield        com/sonyericsson/cameracommon/storage/ImageLoader.mContext:Landroid/content/Context;
        //   281: aload_0        
        //   282: getfield        com/sonyericsson/cameracommon/storage/ImageLoader.mUri:Landroid/net/Uri;
        //   285: invokestatic    com/sonyericsson/cameracommon/storage/ContentResolverUtil.crOpenInputStream:(Landroid/content/Context;Landroid/net/Uri;)Ljava/io/InputStream;
        //   288: astore          7
        //   290: aload           7
        //   292: astore_1       
        //   293: aload_1        
        //   294: ifnull          359
        //   297: aload_0        
        //   298: aload_1        
        //   299: aload_0        
        //   300: getfield        com/sonyericsson/cameracommon/storage/ImageLoader.mOption:Landroid/graphics/BitmapFactory$Options;
        //   303: invokespecial   com/sonyericsson/cameracommon/storage/ImageLoader.loadFullSize:(Ljava/io/InputStream;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
        //   306: astore_2       
        //   307: aload_1        
        //   308: invokevirtual   java/io/InputStream.close:()V
        //   311: goto            359
        //   314: astore          4
        //   316: goto            354
        //   319: astore_2       
        //   320: goto            796
        //   323: astore_2       
        //   324: aload_3        
        //   325: astore_2       
        //   326: aload_1        
        //   327: astore_3       
        //   328: goto            458
        //   331: astore_2       
        //   332: aload           4
        //   334: astore_2       
        //   335: aload_1        
        //   336: astore_3       
        //   337: goto            534
        //   340: astore_2       
        //   341: aload           6
        //   343: astore_2       
        //   344: aload_1        
        //   345: astore_3       
        //   346: goto            623
        //   349: astore          4
        //   351: aload           5
        //   353: astore_2       
        //   354: aload_1        
        //   355: astore_3       
        //   356: goto            713
        //   359: aload_2        
        //   360: astore_3       
        //   361: aload_2        
        //   362: astore          4
        //   364: aload_2        
        //   365: astore          6
        //   367: aload_2        
        //   368: astore          5
        //   370: getstatic       com/sonyericsson/android/camera/util/CamLog.VERBOSE:Z
        //   373: ifeq            399
        //   376: aload_2        
        //   377: astore_3       
        //   378: aload_2        
        //   379: astore          4
        //   381: aload_2        
        //   382: astore          6
        //   384: aload_2        
        //   385: astore          5
        //   387: iconst_1       
        //   388: anewarray       Ljava/lang/String;
        //   391: dup            
        //   392: iconst_0       
        //   393: ldc             "Loading full size image finished"
        //   395: aastore        
        //   396: invokestatic    com/sonyericsson/android/camera/util/CamLog.d:([Ljava/lang/String;)V
        //   399: aload_1        
        //   400: ifnull          443
        //   403: aload_1        
        //   404: invokevirtual   java/io/InputStream.close:()V
        //   407: goto            443
        //   410: astore_3       
        //   411: new             Ljava/lang/StringBuilder;
        //   414: dup            
        //   415: invokespecial   java/lang/StringBuilder.<init>:()V
        //   418: astore_1       
        //   419: aload_1        
        //   420: ldc             "Close stream failed:"
        //   422: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   425: pop            
        //   426: aload_1        
        //   427: aload_3        
        //   428: invokevirtual   java/lang/Exception.toString:()Ljava/lang/String;
        //   431: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   434: pop            
        //   435: aload_1        
        //   436: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   439: aload_3        
        //   440: invokestatic    com/sonyericsson/android/camera/util/CamLog.e:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   443: aload_2        
        //   444: astore_1       
        //   445: goto            794
        //   448: astore_2       
        //   449: goto            796
        //   452: astore_1       
        //   453: aconst_null    
        //   454: astore_2       
        //   455: aload           9
        //   457: astore_3       
        //   458: aload_3        
        //   459: astore_1       
        //   460: iconst_1       
        //   461: anewarray       Ljava/lang/String;
        //   464: dup            
        //   465: iconst_0       
        //   466: ldc             "Maybe File access error."
        //   468: aastore        
        //   469: invokestatic    com/sonyericsson/android/camera/util/CamLog.e:([Ljava/lang/String;)V
        //   472: aload_2        
        //   473: astore_1       
        //   474: aload_3        
        //   475: ifnull          794
        //   478: aload_3        
        //   479: invokevirtual   java/io/InputStream.close:()V
        //   482: aload_2        
        //   483: astore_1       
        //   484: goto            794
        //   487: astore          4
        //   489: new             Ljava/lang/StringBuilder;
        //   492: dup            
        //   493: invokespecial   java/lang/StringBuilder.<init>:()V
        //   496: astore_3       
        //   497: aload_2        
        //   498: astore_1       
        //   499: aload_3        
        //   500: ldc             "Close stream failed:"
        //   502: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   505: pop            
        //   506: aload_3        
        //   507: aload           4
        //   509: invokevirtual   java/lang/Exception.toString:()Ljava/lang/String;
        //   512: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   515: pop            
        //   516: aload_3        
        //   517: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   520: aload           4
        //   522: invokestatic    com/sonyericsson/android/camera/util/CamLog.e:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   525: goto            794
        //   528: astore_1       
        //   529: aconst_null    
        //   530: astore_2       
        //   531: aload           10
        //   533: astore_3       
        //   534: aload_3        
        //   535: astore_1       
        //   536: new             Ljava/lang/StringBuilder;
        //   539: astore          4
        //   541: aload_3        
        //   542: astore_1       
        //   543: aload           4
        //   545: invokespecial   java/lang/StringBuilder.<init>:()V
        //   548: aload_3        
        //   549: astore_1       
        //   550: aload           4
        //   552: ldc             "Close failed:"
        //   554: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   557: pop            
        //   558: aload_3        
        //   559: astore_1       
        //   560: aload           4
        //   562: aload_0        
        //   563: getfield        com/sonyericsson/cameracommon/storage/ImageLoader.mUri:Landroid/net/Uri;
        //   566: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   569: pop            
        //   570: aload_3        
        //   571: astore_1       
        //   572: iconst_1       
        //   573: anewarray       Ljava/lang/String;
        //   576: dup            
        //   577: iconst_0       
        //   578: aload           4
        //   580: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   583: aastore        
        //   584: invokestatic    com/sonyericsson/android/camera/util/CamLog.e:([Ljava/lang/String;)V
        //   587: aload_2        
        //   588: astore_1       
        //   589: aload_3        
        //   590: ifnull          794
        //   593: aload_3        
        //   594: invokevirtual   java/io/InputStream.close:()V
        //   597: aload_2        
        //   598: astore_1       
        //   599: goto            794
        //   602: astore          4
        //   604: new             Ljava/lang/StringBuilder;
        //   607: dup            
        //   608: invokespecial   java/lang/StringBuilder.<init>:()V
        //   611: astore_3       
        //   612: aload_2        
        //   613: astore_1       
        //   614: goto            499
        //   617: astore_1       
        //   618: aconst_null    
        //   619: astore_2       
        //   620: aload           8
        //   622: astore_3       
        //   623: aload_3        
        //   624: astore_1       
        //   625: new             Ljava/lang/StringBuilder;
        //   628: astore          4
        //   630: aload_3        
        //   631: astore_1       
        //   632: aload           4
        //   634: invokespecial   java/lang/StringBuilder.<init>:()V
        //   637: aload_3        
        //   638: astore_1       
        //   639: aload           4
        //   641: ldc             "File not found:"
        //   643: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   646: pop            
        //   647: aload_3        
        //   648: astore_1       
        //   649: aload           4
        //   651: aload_0        
        //   652: getfield        com/sonyericsson/cameracommon/storage/ImageLoader.mUri:Landroid/net/Uri;
        //   655: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   658: pop            
        //   659: aload_3        
        //   660: astore_1       
        //   661: iconst_1       
        //   662: anewarray       Ljava/lang/String;
        //   665: dup            
        //   666: iconst_0       
        //   667: aload           4
        //   669: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   672: aastore        
        //   673: invokestatic    com/sonyericsson/android/camera/util/CamLog.e:([Ljava/lang/String;)V
        //   676: aload_2        
        //   677: astore_1       
        //   678: aload_3        
        //   679: ifnull          794
        //   682: aload_3        
        //   683: invokevirtual   java/io/InputStream.close:()V
        //   686: aload_2        
        //   687: astore_1       
        //   688: goto            794
        //   691: astore          4
        //   693: new             Ljava/lang/StringBuilder;
        //   696: dup            
        //   697: invokespecial   java/lang/StringBuilder.<init>:()V
        //   700: astore_3       
        //   701: aload_2        
        //   702: astore_1       
        //   703: goto            499
        //   706: astore          4
        //   708: aconst_null    
        //   709: astore_2       
        //   710: aload           7
        //   712: astore_3       
        //   713: aload_3        
        //   714: astore_1       
        //   715: new             Ljava/lang/StringBuilder;
        //   718: astore          5
        //   720: aload_3        
        //   721: astore_1       
        //   722: aload           5
        //   724: invokespecial   java/lang/StringBuilder.<init>:()V
        //   727: aload_3        
        //   728: astore_1       
        //   729: aload           5
        //   731: ldc             "Load full size error:"
        //   733: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   736: pop            
        //   737: aload_3        
        //   738: astore_1       
        //   739: aload           5
        //   741: aload           4
        //   743: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   746: pop            
        //   747: aload_3        
        //   748: astore_1       
        //   749: iconst_1       
        //   750: anewarray       Ljava/lang/String;
        //   753: dup            
        //   754: iconst_0       
        //   755: aload           5
        //   757: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   760: aastore        
        //   761: invokestatic    com/sonyericsson/android/camera/util/CamLog.e:([Ljava/lang/String;)V
        //   764: aload_2        
        //   765: astore_1       
        //   766: aload_3        
        //   767: ifnull          794
        //   770: aload_3        
        //   771: invokevirtual   java/io/InputStream.close:()V
        //   774: aload_2        
        //   775: astore_1       
        //   776: goto            794
        //   779: astore          4
        //   781: new             Ljava/lang/StringBuilder;
        //   784: dup            
        //   785: invokespecial   java/lang/StringBuilder.<init>:()V
        //   788: astore_3       
        //   789: aload_2        
        //   790: astore_1       
        //   791: goto            499
        //   794: aload_1        
        //   795: areturn        
        //   796: aload_1        
        //   797: ifnull          840
        //   800: aload_1        
        //   801: invokevirtual   java/io/InputStream.close:()V
        //   804: goto            840
        //   807: astore_1       
        //   808: new             Ljava/lang/StringBuilder;
        //   811: dup            
        //   812: invokespecial   java/lang/StringBuilder.<init>:()V
        //   815: astore_3       
        //   816: aload_3        
        //   817: ldc             "Close stream failed:"
        //   819: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   822: pop            
        //   823: aload_3        
        //   824: aload_1        
        //   825: invokevirtual   java/lang/Exception.toString:()Ljava/lang/String;
        //   828: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   831: pop            
        //   832: aload_3        
        //   833: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   836: aload_1        
        //   837: invokestatic    com/sonyericsson/android/camera/util/CamLog.e:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   840: aload_2        
        //   841: athrow         
        //   842: astore_3       
        //   843: goto            344
        //   846: astore_3       
        //   847: goto            335
        //   850: astore_3       
        //   851: goto            326
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                
        //  -----  -----  -----  -----  ------------------------------------
        //  49     55     706    713    Ljava/io/InvalidObjectException;
        //  49     55     617    623    Ljava/io/FileNotFoundException;
        //  49     55     528    534    Ljava/io/IOException;
        //  49     55     452    458    Ljava/lang/IllegalArgumentException;
        //  49     55     448    452    Any
        //  58     63     706    713    Ljava/io/InvalidObjectException;
        //  58     63     617    623    Ljava/io/FileNotFoundException;
        //  58     63     528    534    Ljava/io/IOException;
        //  58     63     452    458    Ljava/lang/IllegalArgumentException;
        //  58     63     448    452    Any
        //  66     71     706    713    Ljava/io/InvalidObjectException;
        //  66     71     617    623    Ljava/io/FileNotFoundException;
        //  66     71     528    534    Ljava/io/IOException;
        //  66     71     452    458    Ljava/lang/IllegalArgumentException;
        //  66     71     448    452    Any
        //  74     82     706    713    Ljava/io/InvalidObjectException;
        //  74     82     617    623    Ljava/io/FileNotFoundException;
        //  74     82     528    534    Ljava/io/IOException;
        //  74     82     452    458    Ljava/lang/IllegalArgumentException;
        //  74     82     448    452    Any
        //  85     95     706    713    Ljava/io/InvalidObjectException;
        //  85     95     617    623    Ljava/io/FileNotFoundException;
        //  85     95     528    534    Ljava/io/IOException;
        //  85     95     452    458    Ljava/lang/IllegalArgumentException;
        //  85     95     448    452    Any
        //  98     113    706    713    Ljava/io/InvalidObjectException;
        //  98     113    617    623    Ljava/io/FileNotFoundException;
        //  98     113    528    534    Ljava/io/IOException;
        //  98     113    452    458    Ljava/lang/IllegalArgumentException;
        //  98     113    448    452    Any
        //  116    123    706    713    Ljava/io/InvalidObjectException;
        //  116    123    617    623    Ljava/io/FileNotFoundException;
        //  116    123    528    534    Ljava/io/IOException;
        //  116    123    452    458    Ljava/lang/IllegalArgumentException;
        //  116    123    448    452    Any
        //  126    131    706    713    Ljava/io/InvalidObjectException;
        //  126    131    617    623    Ljava/io/FileNotFoundException;
        //  126    131    528    534    Ljava/io/IOException;
        //  126    131    452    458    Ljava/lang/IllegalArgumentException;
        //  126    131    448    452    Any
        //  134    143    706    713    Ljava/io/InvalidObjectException;
        //  134    143    617    623    Ljava/io/FileNotFoundException;
        //  134    143    528    534    Ljava/io/IOException;
        //  134    143    452    458    Ljava/lang/IllegalArgumentException;
        //  134    143    448    452    Any
        //  152    165    706    713    Ljava/io/InvalidObjectException;
        //  152    165    617    623    Ljava/io/FileNotFoundException;
        //  152    165    528    534    Ljava/io/IOException;
        //  152    165    452    458    Ljava/lang/IllegalArgumentException;
        //  152    165    448    452    Any
        //  172    210    241    250    Ljava/io/InvalidObjectException;
        //  172    210    233    241    Ljava/io/FileNotFoundException;
        //  172    210    225    233    Ljava/io/IOException;
        //  172    210    217    225    Ljava/lang/IllegalArgumentException;
        //  172    210    213    217    Any
        //  250    271    241    250    Ljava/io/InvalidObjectException;
        //  250    271    233    241    Ljava/io/FileNotFoundException;
        //  250    271    225    233    Ljava/io/IOException;
        //  250    271    217    225    Ljava/lang/IllegalArgumentException;
        //  250    271    213    217    Any
        //  277    290    241    250    Ljava/io/InvalidObjectException;
        //  277    290    233    241    Ljava/io/FileNotFoundException;
        //  277    290    225    233    Ljava/io/IOException;
        //  277    290    217    225    Ljava/lang/IllegalArgumentException;
        //  277    290    213    217    Any
        //  297    307    349    354    Ljava/io/InvalidObjectException;
        //  297    307    340    344    Ljava/io/FileNotFoundException;
        //  297    307    331    335    Ljava/io/IOException;
        //  297    307    323    326    Ljava/lang/IllegalArgumentException;
        //  297    307    319    323    Any
        //  307    311    314    319    Ljava/io/InvalidObjectException;
        //  307    311    842    846    Ljava/io/FileNotFoundException;
        //  307    311    846    850    Ljava/io/IOException;
        //  307    311    850    854    Ljava/lang/IllegalArgumentException;
        //  307    311    319    323    Any
        //  370    376    349    354    Ljava/io/InvalidObjectException;
        //  370    376    340    344    Ljava/io/FileNotFoundException;
        //  370    376    331    335    Ljava/io/IOException;
        //  370    376    323    326    Ljava/lang/IllegalArgumentException;
        //  370    376    319    323    Any
        //  387    399    349    354    Ljava/io/InvalidObjectException;
        //  387    399    340    344    Ljava/io/FileNotFoundException;
        //  387    399    331    335    Ljava/io/IOException;
        //  387    399    323    326    Ljava/lang/IllegalArgumentException;
        //  387    399    319    323    Any
        //  403    407    410    443    Ljava/lang/Exception;
        //  460    472    448    452    Any
        //  478    482    487    499    Ljava/lang/Exception;
        //  536    541    448    452    Any
        //  543    548    448    452    Any
        //  550    558    448    452    Any
        //  560    570    448    452    Any
        //  572    587    448    452    Any
        //  593    597    602    617    Ljava/lang/Exception;
        //  625    630    448    452    Any
        //  632    637    448    452    Any
        //  639    647    448    452    Any
        //  649    659    448    452    Any
        //  661    676    448    452    Any
        //  682    686    691    706    Ljava/lang/Exception;
        //  715    720    448    452    Any
        //  722    727    448    452    Any
        //  729    737    448    452    Any
        //  739    747    448    452    Any
        //  749    764    448    452    Any
        //  770    774    779    794    Ljava/lang/Exception;
        //  800    804    807    840    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 486 out of bounds for length 486
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
}
