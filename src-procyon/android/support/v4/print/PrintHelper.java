// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.print;

import android.os.CancellationSignal$OnCancelListener;
import android.print.PrintDocumentInfo$Builder;
import android.os.Bundle;
import android.print.PrintDocumentAdapter$LayoutResultCallback;
import android.print.PageRange;
import android.os.AsyncTask;
import android.print.PrintAttributes$Margins;
import android.print.PrintDocumentAdapter$WriteResultCallback;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PrintDocumentAdapter;
import android.print.PrintAttributes$MediaSize;
import android.print.PrintManager;
import android.support.annotation.Nullable;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import android.util.Log;
import android.graphics.Rect;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.support.annotation.RequiresApi;
import android.print.PrintAttributes$Builder;
import android.print.PrintAttributes;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Bitmap$Config;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.os.Build$VERSION;
import android.graphics.BitmapFactory$Options;
import android.content.Context;
import android.annotation.SuppressLint;

public final class PrintHelper
{
    @SuppressLint({ "InlinedApi" })
    public static final int COLOR_MODE_COLOR = 2;
    @SuppressLint({ "InlinedApi" })
    public static final int COLOR_MODE_MONOCHROME = 1;
    static final boolean IS_MIN_MARGINS_HANDLING_CORRECT;
    private static final String LOG_TAG = "PrintHelper";
    private static final int MAX_PRINT_SIZE = 3500;
    public static final int ORIENTATION_LANDSCAPE = 1;
    public static final int ORIENTATION_PORTRAIT = 2;
    static final boolean PRINT_ACTIVITY_RESPECTS_ORIENTATION;
    public static final int SCALE_MODE_FILL = 2;
    public static final int SCALE_MODE_FIT = 1;
    int mColorMode;
    final Context mContext;
    BitmapFactory$Options mDecodeOptions;
    final Object mLock;
    int mOrientation;
    int mScaleMode;
    
    static {
        final int sdk_INT = Build$VERSION.SDK_INT;
        final boolean b = true;
        PRINT_ACTIVITY_RESPECTS_ORIENTATION = (sdk_INT < 20 || Build$VERSION.SDK_INT > 23);
        IS_MIN_MARGINS_HANDLING_CORRECT = (Build$VERSION.SDK_INT != 23 && b);
    }
    
    public PrintHelper(@NonNull final Context mContext) {
        this.mDecodeOptions = null;
        this.mLock = new Object();
        this.mScaleMode = 2;
        this.mColorMode = 2;
        this.mOrientation = 1;
        this.mContext = mContext;
    }
    
    static Bitmap convertBitmapForColorMode(final Bitmap bitmap, final int n) {
        if (n != 1) {
            return bitmap;
        }
        final Bitmap bitmap2 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap$Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap2);
        final Paint paint = new Paint();
        final ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.0f);
        paint.setColorFilter((ColorFilter)new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        canvas.setBitmap((Bitmap)null);
        return bitmap2;
    }
    
    @RequiresApi(19)
    private static PrintAttributes$Builder copyAttributes(final PrintAttributes printAttributes) {
        final PrintAttributes$Builder setMinMargins = new PrintAttributes$Builder().setMediaSize(printAttributes.getMediaSize()).setResolution(printAttributes.getResolution()).setMinMargins(printAttributes.getMinMargins());
        if (printAttributes.getColorMode() != 0) {
            setMinMargins.setColorMode(printAttributes.getColorMode());
        }
        if (Build$VERSION.SDK_INT >= 23 && printAttributes.getDuplexMode() != 0) {
            setMinMargins.setDuplexMode(printAttributes.getDuplexMode());
        }
        return setMinMargins;
    }
    
    static Matrix getMatrix(final int n, final int n2, final RectF rectF, final int n3) {
        final Matrix matrix = new Matrix();
        final float width = rectF.width();
        final float n4 = (float)n;
        final float n5 = width / n4;
        float n6;
        if (n3 == 2) {
            n6 = Math.max(n5, rectF.height() / n2);
        }
        else {
            n6 = Math.min(n5, rectF.height() / n2);
        }
        matrix.postScale(n6, n6);
        matrix.postTranslate((rectF.width() - n4 * n6) / 2.0f, (rectF.height() - n2 * n6) / 2.0f);
        return matrix;
    }
    
    static boolean isPortrait(final Bitmap bitmap) {
        return bitmap.getWidth() <= bitmap.getHeight();
    }
    
    private Bitmap loadBitmap(Uri decodeStream, final BitmapFactory$Options bitmapFactory$Options) throws FileNotFoundException {
        if (decodeStream == null || this.mContext == null) {
            throw new IllegalArgumentException("bad argument to loadBitmap");
        }
        final InputStream inputStream = null;
        InputStream inputStream2;
        try {
            final InputStream openInputStream = this.mContext.getContentResolver().openInputStream(decodeStream);
            try {
                decodeStream = (Uri)BitmapFactory.decodeStream(openInputStream, (Rect)null, bitmapFactory$Options);
                if (openInputStream != null) {
                    try {
                        openInputStream.close();
                    }
                    catch (final IOException ex) {
                        Log.w("PrintHelper", "close fail ", (Throwable)ex);
                    }
                }
                return (Bitmap)decodeStream;
            }
            finally {}
        }
        finally {
            inputStream2 = inputStream;
        }
        if (inputStream2 != null) {
            try {
                inputStream2.close();
            }
            catch (final IOException ex2) {
                Log.w("PrintHelper", "close fail ", (Throwable)ex2);
            }
        }
    }
    
    public static boolean systemSupportsPrint() {
        return Build$VERSION.SDK_INT >= 19;
    }
    
    public int getColorMode() {
        return this.mColorMode;
    }
    
    public int getOrientation() {
        if (Build$VERSION.SDK_INT >= 19 && this.mOrientation == 0) {
            return 1;
        }
        return this.mOrientation;
    }
    
    public int getScaleMode() {
        return this.mScaleMode;
    }
    
    Bitmap loadConstrainedBitmap(final Uri uri) throws FileNotFoundException {
        if (uri == null || this.mContext == null) {
            throw new IllegalArgumentException("bad argument to getScaledBitmap");
        }
        final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
        bitmapFactory$Options.inJustDecodeBounds = true;
        this.loadBitmap(uri, bitmapFactory$Options);
        final int outWidth = bitmapFactory$Options.outWidth;
        final int outHeight = bitmapFactory$Options.outHeight;
        if (outWidth > 0 && outHeight > 0) {
            int i;
            int inSampleSize;
            for (i = Math.max(outWidth, outHeight), inSampleSize = 1; i > 3500; i >>>= 1, inSampleSize <<= 1) {}
            if (inSampleSize > 0) {
                if (Math.min(outWidth, outHeight) / inSampleSize > 0) {
                    final Object mLock = this.mLock;
                    synchronized (mLock) {
                        this.mDecodeOptions = new BitmapFactory$Options();
                        this.mDecodeOptions.inMutable = true;
                        this.mDecodeOptions.inSampleSize = inSampleSize;
                        final BitmapFactory$Options mDecodeOptions = this.mDecodeOptions;
                        monitorexit(mLock);
                        try {
                            final Bitmap loadBitmap = this.loadBitmap(uri, mDecodeOptions);
                            synchronized (this.mLock) {
                                this.mDecodeOptions = null;
                                return loadBitmap;
                            }
                        }
                        finally {
                            synchronized (this.mLock) {
                                this.mDecodeOptions = null;
                                monitorexit(this.mLock);
                            }
                        }
                    }
                }
            }
            return null;
        }
        return null;
    }
    
    public void printBitmap(@NonNull final String s, @NonNull final Bitmap bitmap) {
        this.printBitmap(s, bitmap, null);
    }
    
    public void printBitmap(@NonNull final String s, @NonNull final Bitmap bitmap, @Nullable final OnPrintFinishCallback onPrintFinishCallback) {
        if (Build$VERSION.SDK_INT >= 19 && bitmap != null) {
            final PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
            PrintAttributes$MediaSize mediaSize;
            if (isPortrait(bitmap)) {
                mediaSize = PrintAttributes$MediaSize.UNKNOWN_PORTRAIT;
            }
            else {
                mediaSize = PrintAttributes$MediaSize.UNKNOWN_LANDSCAPE;
            }
            printManager.print(s, (PrintDocumentAdapter)new PrintBitmapAdapter(s, this.mScaleMode, bitmap, onPrintFinishCallback), new PrintAttributes$Builder().setMediaSize(mediaSize).setColorMode(this.mColorMode).build());
        }
    }
    
    public void printBitmap(@NonNull final String s, @NonNull final Uri uri) throws FileNotFoundException {
        this.printBitmap(s, uri, null);
    }
    
    public void printBitmap(@NonNull final String s, @NonNull final Uri uri, @Nullable final OnPrintFinishCallback onPrintFinishCallback) throws FileNotFoundException {
        if (Build$VERSION.SDK_INT < 19) {
            return;
        }
        final PrintUriAdapter printUriAdapter = new PrintUriAdapter(s, uri, onPrintFinishCallback, this.mScaleMode);
        final PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
        final PrintAttributes$Builder printAttributes$Builder = new PrintAttributes$Builder();
        printAttributes$Builder.setColorMode(this.mColorMode);
        if (this.mOrientation != 1 && this.mOrientation != 0) {
            if (this.mOrientation == 2) {
                printAttributes$Builder.setMediaSize(PrintAttributes$MediaSize.UNKNOWN_PORTRAIT);
            }
        }
        else {
            printAttributes$Builder.setMediaSize(PrintAttributes$MediaSize.UNKNOWN_LANDSCAPE);
        }
        printManager.print(s, (PrintDocumentAdapter)printUriAdapter, printAttributes$Builder.build());
    }
    
    public void setColorMode(final int mColorMode) {
        this.mColorMode = mColorMode;
    }
    
    public void setOrientation(final int mOrientation) {
        this.mOrientation = mOrientation;
    }
    
    public void setScaleMode(final int mScaleMode) {
        this.mScaleMode = mScaleMode;
    }
    
    @RequiresApi(19)
    void writeBitmap(final PrintAttributes printAttributes, final int n, final Bitmap bitmap, final ParcelFileDescriptor parcelFileDescriptor, final CancellationSignal cancellationSignal, final PrintDocumentAdapter$WriteResultCallback printDocumentAdapter$WriteResultCallback) {
        PrintAttributes build;
        if (PrintHelper.IS_MIN_MARGINS_HANDLING_CORRECT) {
            build = printAttributes;
        }
        else {
            build = copyAttributes(printAttributes).setMinMargins(new PrintAttributes$Margins(0, 0, 0, 0)).build();
        }
        new AsyncTask<Void, Void, Throwable>(this, cancellationSignal, build, bitmap, printAttributes, n, parcelFileDescriptor, printDocumentAdapter$WriteResultCallback) {
            final PrintHelper this$0;
            final PrintAttributes val$attributes;
            final Bitmap val$bitmap;
            final CancellationSignal val$cancellationSignal;
            final ParcelFileDescriptor val$fileDescriptor;
            final int val$fittingMode;
            final PrintAttributes val$pdfAttributes;
            final PrintDocumentAdapter$WriteResultCallback val$writeResultCallback;
            
            protected Throwable doInBackground(final Void... p0) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     1: getfield        android/support/v4/print/PrintHelper$1.val$cancellationSignal:Landroid/os/CancellationSignal;
                //     4: invokevirtual   android/os/CancellationSignal.isCanceled:()Z
                //     7: ifeq            12
                //    10: aconst_null    
                //    11: areturn        
                //    12: new             Landroid/print/pdf/PrintedPdfDocument;
                //    15: astore          4
                //    17: aload           4
                //    19: aload_0        
                //    20: getfield        android/support/v4/print/PrintHelper$1.this$0:Landroid/support/v4/print/PrintHelper;
                //    23: getfield        android/support/v4/print/PrintHelper.mContext:Landroid/content/Context;
                //    26: aload_0        
                //    27: getfield        android/support/v4/print/PrintHelper$1.val$pdfAttributes:Landroid/print/PrintAttributes;
                //    30: invokespecial   android/print/pdf/PrintedPdfDocument.<init>:(Landroid/content/Context;Landroid/print/PrintAttributes;)V
                //    33: aload_0        
                //    34: getfield        android/support/v4/print/PrintHelper$1.val$bitmap:Landroid/graphics/Bitmap;
                //    37: aload_0        
                //    38: getfield        android/support/v4/print/PrintHelper$1.val$pdfAttributes:Landroid/print/PrintAttributes;
                //    41: invokevirtual   android/print/PrintAttributes.getColorMode:()I
                //    44: invokestatic    android/support/v4/print/PrintHelper.convertBitmapForColorMode:(Landroid/graphics/Bitmap;I)Landroid/graphics/Bitmap;
                //    47: astore_3       
                //    48: aload_0        
                //    49: getfield        android/support/v4/print/PrintHelper$1.val$cancellationSignal:Landroid/os/CancellationSignal;
                //    52: invokevirtual   android/os/CancellationSignal.isCanceled:()Z
                //    55: istore_2       
                //    56: iload_2        
                //    57: ifeq            62
                //    60: aconst_null    
                //    61: areturn        
                //    62: aload           4
                //    64: iconst_1       
                //    65: invokevirtual   android/print/pdf/PrintedPdfDocument.startPage:(I)Landroid/graphics/pdf/PdfDocument$Page;
                //    68: astore          5
                //    70: getstatic       android/support/v4/print/PrintHelper.IS_MIN_MARGINS_HANDLING_CORRECT:Z
                //    73: ifeq            95
                //    76: new             Landroid/graphics/RectF;
                //    79: astore_1       
                //    80: aload_1        
                //    81: aload           5
                //    83: invokevirtual   android/graphics/pdf/PdfDocument$Page.getInfo:()Landroid/graphics/pdf/PdfDocument$PageInfo;
                //    86: invokevirtual   android/graphics/pdf/PdfDocument$PageInfo.getContentRect:()Landroid/graphics/Rect;
                //    89: invokespecial   android/graphics/RectF.<init>:(Landroid/graphics/Rect;)V
                //    92: goto            152
                //    95: new             Landroid/print/pdf/PrintedPdfDocument;
                //    98: astore          7
                //   100: aload           7
                //   102: aload_0        
                //   103: getfield        android/support/v4/print/PrintHelper$1.this$0:Landroid/support/v4/print/PrintHelper;
                //   106: getfield        android/support/v4/print/PrintHelper.mContext:Landroid/content/Context;
                //   109: aload_0        
                //   110: getfield        android/support/v4/print/PrintHelper$1.val$attributes:Landroid/print/PrintAttributes;
                //   113: invokespecial   android/print/pdf/PrintedPdfDocument.<init>:(Landroid/content/Context;Landroid/print/PrintAttributes;)V
                //   116: aload           7
                //   118: iconst_1       
                //   119: invokevirtual   android/print/pdf/PrintedPdfDocument.startPage:(I)Landroid/graphics/pdf/PdfDocument$Page;
                //   122: astore          6
                //   124: new             Landroid/graphics/RectF;
                //   127: astore_1       
                //   128: aload_1        
                //   129: aload           6
                //   131: invokevirtual   android/graphics/pdf/PdfDocument$Page.getInfo:()Landroid/graphics/pdf/PdfDocument$PageInfo;
                //   134: invokevirtual   android/graphics/pdf/PdfDocument$PageInfo.getContentRect:()Landroid/graphics/Rect;
                //   137: invokespecial   android/graphics/RectF.<init>:(Landroid/graphics/Rect;)V
                //   140: aload           7
                //   142: aload           6
                //   144: invokevirtual   android/print/pdf/PrintedPdfDocument.finishPage:(Landroid/graphics/pdf/PdfDocument$Page;)V
                //   147: aload           7
                //   149: invokevirtual   android/print/pdf/PrintedPdfDocument.close:()V
                //   152: aload_3        
                //   153: invokevirtual   android/graphics/Bitmap.getWidth:()I
                //   156: aload_3        
                //   157: invokevirtual   android/graphics/Bitmap.getHeight:()I
                //   160: aload_1        
                //   161: aload_0        
                //   162: getfield        android/support/v4/print/PrintHelper$1.val$fittingMode:I
                //   165: invokestatic    android/support/v4/print/PrintHelper.getMatrix:(IILandroid/graphics/RectF;I)Landroid/graphics/Matrix;
                //   168: astore          6
                //   170: getstatic       android/support/v4/print/PrintHelper.IS_MIN_MARGINS_HANDLING_CORRECT:Z
                //   173: ifeq            179
                //   176: goto            203
                //   179: aload           6
                //   181: aload_1        
                //   182: getfield        android/graphics/RectF.left:F
                //   185: aload_1        
                //   186: getfield        android/graphics/RectF.top:F
                //   189: invokevirtual   android/graphics/Matrix.postTranslate:(FF)Z
                //   192: pop            
                //   193: aload           5
                //   195: invokevirtual   android/graphics/pdf/PdfDocument$Page.getCanvas:()Landroid/graphics/Canvas;
                //   198: aload_1        
                //   199: invokevirtual   android/graphics/Canvas.clipRect:(Landroid/graphics/RectF;)Z
                //   202: pop            
                //   203: aload           5
                //   205: invokevirtual   android/graphics/pdf/PdfDocument$Page.getCanvas:()Landroid/graphics/Canvas;
                //   208: aload_3        
                //   209: aload           6
                //   211: aconst_null    
                //   212: invokevirtual   android/graphics/Canvas.drawBitmap:(Landroid/graphics/Bitmap;Landroid/graphics/Matrix;Landroid/graphics/Paint;)V
                //   215: aload           4
                //   217: aload           5
                //   219: invokevirtual   android/print/pdf/PrintedPdfDocument.finishPage:(Landroid/graphics/pdf/PdfDocument$Page;)V
                //   222: aload_0        
                //   223: getfield        android/support/v4/print/PrintHelper$1.val$cancellationSignal:Landroid/os/CancellationSignal;
                //   226: invokevirtual   android/os/CancellationSignal.isCanceled:()Z
                //   229: istore_2       
                //   230: iload_2        
                //   231: ifeq            269
                //   234: aload           4
                //   236: invokevirtual   android/print/pdf/PrintedPdfDocument.close:()V
                //   239: aload_0        
                //   240: getfield        android/support/v4/print/PrintHelper$1.val$fileDescriptor:Landroid/os/ParcelFileDescriptor;
                //   243: astore_1       
                //   244: aload_1        
                //   245: ifnull          255
                //   248: aload_0        
                //   249: getfield        android/support/v4/print/PrintHelper$1.val$fileDescriptor:Landroid/os/ParcelFileDescriptor;
                //   252: invokevirtual   android/os/ParcelFileDescriptor.close:()V
                //   255: aload_3        
                //   256: aload_0        
                //   257: getfield        android/support/v4/print/PrintHelper$1.val$bitmap:Landroid/graphics/Bitmap;
                //   260: if_acmpeq       267
                //   263: aload_3        
                //   264: invokevirtual   android/graphics/Bitmap.recycle:()V
                //   267: aconst_null    
                //   268: areturn        
                //   269: new             Ljava/io/FileOutputStream;
                //   272: astore_1       
                //   273: aload_1        
                //   274: aload_0        
                //   275: getfield        android/support/v4/print/PrintHelper$1.val$fileDescriptor:Landroid/os/ParcelFileDescriptor;
                //   278: invokevirtual   android/os/ParcelFileDescriptor.getFileDescriptor:()Ljava/io/FileDescriptor;
                //   281: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/FileDescriptor;)V
                //   284: aload           4
                //   286: aload_1        
                //   287: invokevirtual   android/print/pdf/PrintedPdfDocument.writeTo:(Ljava/io/OutputStream;)V
                //   290: aload           4
                //   292: invokevirtual   android/print/pdf/PrintedPdfDocument.close:()V
                //   295: aload_0        
                //   296: getfield        android/support/v4/print/PrintHelper$1.val$fileDescriptor:Landroid/os/ParcelFileDescriptor;
                //   299: astore_1       
                //   300: aload_1        
                //   301: ifnull          311
                //   304: aload_0        
                //   305: getfield        android/support/v4/print/PrintHelper$1.val$fileDescriptor:Landroid/os/ParcelFileDescriptor;
                //   308: invokevirtual   android/os/ParcelFileDescriptor.close:()V
                //   311: aload_3        
                //   312: aload_0        
                //   313: getfield        android/support/v4/print/PrintHelper$1.val$bitmap:Landroid/graphics/Bitmap;
                //   316: if_acmpeq       323
                //   319: aload_3        
                //   320: invokevirtual   android/graphics/Bitmap.recycle:()V
                //   323: aconst_null    
                //   324: areturn        
                //   325: astore_1       
                //   326: aload           4
                //   328: invokevirtual   android/print/pdf/PrintedPdfDocument.close:()V
                //   331: aload_0        
                //   332: getfield        android/support/v4/print/PrintHelper$1.val$fileDescriptor:Landroid/os/ParcelFileDescriptor;
                //   335: astore          4
                //   337: aload           4
                //   339: ifnull          349
                //   342: aload_0        
                //   343: getfield        android/support/v4/print/PrintHelper$1.val$fileDescriptor:Landroid/os/ParcelFileDescriptor;
                //   346: invokevirtual   android/os/ParcelFileDescriptor.close:()V
                //   349: aload_3        
                //   350: aload_0        
                //   351: getfield        android/support/v4/print/PrintHelper$1.val$bitmap:Landroid/graphics/Bitmap;
                //   354: if_acmpeq       361
                //   357: aload_3        
                //   358: invokevirtual   android/graphics/Bitmap.recycle:()V
                //   361: aload_1        
                //   362: athrow         
                //   363: astore_1       
                //   364: aload_1        
                //   365: areturn        
                //   366: astore_1       
                //   367: goto            255
                //   370: astore_1       
                //   371: goto            311
                //   374: astore          4
                //   376: goto            349
                //    Exceptions:
                //  Try           Handler
                //  Start  End    Start  End    Type                 
                //  -----  -----  -----  -----  ---------------------
                //  0      10     363    366    Ljava/lang/Throwable;
                //  12     56     363    366    Ljava/lang/Throwable;
                //  62     92     325    363    Any
                //  95     152    325    363    Any
                //  152    176    325    363    Any
                //  179    203    325    363    Any
                //  203    230    325    363    Any
                //  234    244    363    366    Ljava/lang/Throwable;
                //  248    255    366    370    Ljava/io/IOException;
                //  248    255    363    366    Ljava/lang/Throwable;
                //  255    267    363    366    Ljava/lang/Throwable;
                //  269    290    325    363    Any
                //  290    300    363    366    Ljava/lang/Throwable;
                //  304    311    370    374    Ljava/io/IOException;
                //  304    311    363    366    Ljava/lang/Throwable;
                //  311    323    363    366    Ljava/lang/Throwable;
                //  326    337    363    366    Ljava/lang/Throwable;
                //  342    349    374    379    Ljava/io/IOException;
                //  342    349    363    366    Ljava/lang/Throwable;
                //  349    361    363    366    Ljava/lang/Throwable;
                //  361    363    363    366    Ljava/lang/Throwable;
                // 
                // The error that occurred was:
                // 
                // java.lang.IndexOutOfBoundsException: Index 182 out of bounds for length 182
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
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1151)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:993)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:534)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:548)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:534)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:377)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:318)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:213)
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
            
            protected void onPostExecute(final Throwable t) {
                if (this.val$cancellationSignal.isCanceled()) {
                    this.val$writeResultCallback.onWriteCancelled();
                }
                else if (t == null) {
                    this.val$writeResultCallback.onWriteFinished(new PageRange[] { PageRange.ALL_PAGES });
                }
                else {
                    Log.e("PrintHelper", "Error writing printed content", t);
                    this.val$writeResultCallback.onWriteFailed((CharSequence)null);
                }
            }
        }.execute((Object[])new Void[0]);
    }
    
    public interface OnPrintFinishCallback
    {
        void onFinish();
    }
    
    @RequiresApi(19)
    private class PrintBitmapAdapter extends PrintDocumentAdapter
    {
        private PrintAttributes mAttributes;
        private final Bitmap mBitmap;
        private final OnPrintFinishCallback mCallback;
        private final int mFittingMode;
        private final String mJobName;
        final PrintHelper this$0;
        
        PrintBitmapAdapter(final PrintHelper this$0, final String mJobName, final int mFittingMode, final Bitmap mBitmap, final OnPrintFinishCallback mCallback) {
            this.this$0 = this$0;
            this.mJobName = mJobName;
            this.mFittingMode = mFittingMode;
            this.mBitmap = mBitmap;
            this.mCallback = mCallback;
        }
        
        public void onFinish() {
            if (this.mCallback != null) {
                this.mCallback.onFinish();
            }
        }
        
        public void onLayout(final PrintAttributes printAttributes, final PrintAttributes mAttributes, final CancellationSignal cancellationSignal, final PrintDocumentAdapter$LayoutResultCallback printDocumentAdapter$LayoutResultCallback, final Bundle bundle) {
            this.mAttributes = mAttributes;
            printDocumentAdapter$LayoutResultCallback.onLayoutFinished(new PrintDocumentInfo$Builder(this.mJobName).setContentType(1).setPageCount(1).build(), true ^ mAttributes.equals((Object)printAttributes));
        }
        
        public void onWrite(final PageRange[] array, final ParcelFileDescriptor parcelFileDescriptor, final CancellationSignal cancellationSignal, final PrintDocumentAdapter$WriteResultCallback printDocumentAdapter$WriteResultCallback) {
            this.this$0.writeBitmap(this.mAttributes, this.mFittingMode, this.mBitmap, parcelFileDescriptor, cancellationSignal, printDocumentAdapter$WriteResultCallback);
        }
    }
    
    @RequiresApi(19)
    private class PrintUriAdapter extends PrintDocumentAdapter
    {
        PrintAttributes mAttributes;
        Bitmap mBitmap;
        final OnPrintFinishCallback mCallback;
        final int mFittingMode;
        final Uri mImageFile;
        final String mJobName;
        AsyncTask<Uri, Boolean, Bitmap> mLoadBitmap;
        final PrintHelper this$0;
        
        PrintUriAdapter(final PrintHelper this$0, final String mJobName, final Uri mImageFile, final OnPrintFinishCallback mCallback, final int mFittingMode) {
            this.this$0 = this$0;
            this.mJobName = mJobName;
            this.mImageFile = mImageFile;
            this.mCallback = mCallback;
            this.mFittingMode = mFittingMode;
            this.mBitmap = null;
        }
        
        void cancelLoad() {
            synchronized (this.this$0.mLock) {
                if (this.this$0.mDecodeOptions != null) {
                    if (Build$VERSION.SDK_INT < 24) {
                        this.this$0.mDecodeOptions.requestCancelDecode();
                    }
                    this.this$0.mDecodeOptions = null;
                }
            }
        }
        
        public void onFinish() {
            super.onFinish();
            this.cancelLoad();
            if (this.mLoadBitmap != null) {
                this.mLoadBitmap.cancel(true);
            }
            if (this.mCallback != null) {
                this.mCallback.onFinish();
            }
            if (this.mBitmap != null) {
                this.mBitmap.recycle();
                this.mBitmap = null;
            }
        }
        
        public void onLayout(final PrintAttributes printAttributes, final PrintAttributes mAttributes, final CancellationSignal cancellationSignal, final PrintDocumentAdapter$LayoutResultCallback printDocumentAdapter$LayoutResultCallback, final Bundle bundle) {
            synchronized (this) {
                this.mAttributes = mAttributes;
                monitorexit(this);
                if (cancellationSignal.isCanceled()) {
                    printDocumentAdapter$LayoutResultCallback.onLayoutCancelled();
                    return;
                }
                if (this.mBitmap != null) {
                    printDocumentAdapter$LayoutResultCallback.onLayoutFinished(new PrintDocumentInfo$Builder(this.mJobName).setContentType(1).setPageCount(1).build(), true ^ mAttributes.equals((Object)printAttributes));
                    return;
                }
                this.mLoadBitmap = (AsyncTask<Uri, Boolean, Bitmap>)new AsyncTask<Uri, Boolean, Bitmap>(this, cancellationSignal, mAttributes, printAttributes, printDocumentAdapter$LayoutResultCallback) {
                    final PrintUriAdapter this$1;
                    final CancellationSignal val$cancellationSignal;
                    final PrintDocumentAdapter$LayoutResultCallback val$layoutResultCallback;
                    final PrintAttributes val$newPrintAttributes;
                    final PrintAttributes val$oldPrintAttributes;
                    
                    protected Bitmap doInBackground(final Uri... array) {
                        try {
                            return this.this$1.this$0.loadConstrainedBitmap(this.this$1.mImageFile);
                        }
                        catch (final FileNotFoundException ex) {
                            return null;
                        }
                    }
                    
                    protected void onCancelled(final Bitmap bitmap) {
                        this.val$layoutResultCallback.onLayoutCancelled();
                        this.this$1.mLoadBitmap = null;
                    }
                    
                    protected void onPostExecute(final Bitmap bitmap) {
                        super.onPostExecute((Object)bitmap);
                        Object bitmap2 = bitmap;
                        Label_0109: {
                            if (bitmap != null) {
                                if (PrintHelper.PRINT_ACTIVITY_RESPECTS_ORIENTATION) {
                                    bitmap2 = bitmap;
                                    if (this.this$1.this$0.mOrientation != 0) {
                                        break Label_0109;
                                    }
                                }
                                synchronized (this) {
                                    final PrintAttributes$MediaSize mediaSize = this.this$1.mAttributes.getMediaSize();
                                    monitorexit(this);
                                    bitmap2 = bitmap;
                                    if (mediaSize != null) {
                                        bitmap2 = bitmap;
                                        if (mediaSize.isPortrait() != PrintHelper.isPortrait(bitmap)) {
                                            bitmap2 = new Matrix();
                                            ((Matrix)bitmap2).postRotate(90.0f);
                                            bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), (Matrix)bitmap2, true);
                                        }
                                    }
                                }
                            }
                        }
                        if ((this.this$1.mBitmap = (Bitmap)bitmap2) != null) {
                            this.val$layoutResultCallback.onLayoutFinished(new PrintDocumentInfo$Builder(this.this$1.mJobName).setContentType(1).setPageCount(1).build(), true ^ this.val$newPrintAttributes.equals((Object)this.val$oldPrintAttributes));
                        }
                        else {
                            this.val$layoutResultCallback.onLayoutFailed((CharSequence)null);
                        }
                        this.this$1.mLoadBitmap = null;
                    }
                    
                    protected void onPreExecute() {
                        this.val$cancellationSignal.setOnCancelListener((CancellationSignal$OnCancelListener)new CancellationSignal$OnCancelListener(this) {
                            final PrintHelper$PrintUriAdapter$1 this$2;
                            
                            public void onCancel() {
                                this.this$2.this$1.cancelLoad();
                                this.this$2.cancel(false);
                            }
                        });
                    }
                }.execute((Object[])new Uri[0]);
            }
        }
        
        public void onWrite(final PageRange[] array, final ParcelFileDescriptor parcelFileDescriptor, final CancellationSignal cancellationSignal, final PrintDocumentAdapter$WriteResultCallback printDocumentAdapter$WriteResultCallback) {
            this.this$0.writeBitmap(this.mAttributes, this.mFittingMode, this.mBitmap, parcelFileDescriptor, cancellationSignal, printDocumentAdapter$WriteResultCallback);
        }
    }
}
