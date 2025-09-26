// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.modeselector;

import android.content.res.Resources;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager$NameNotFoundException;
import android.content.res.Resources$NotFoundException;
import com.sonyericsson.android.camera.util.CamLog;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory$Options;
import android.net.Uri;
import android.graphics.Bitmap;
import android.content.Context;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;

public class ResourceUtil
{
    public static final String HTTPS_SCHEME = "https";
    public static final String HTTP_SCHEME = "http";
    private static final int ICON_SIZE_LIMIT_COEFFICIENT = 2;
    private static final int IO_BUFFER_SIZE = 4096;
    private static final int NETWORK_TIMEOUT = 60000;
    public static final String RESOURCE_SCHEME = "resource";
    public static final String RES_TYPE_NAME_DRAWABLE = "drawable";
    public static final String RES_TYPE_NAME_STRING = "string";
    private static final String TAG = "ResourceUtil";
    
    private static void copy(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        final byte[] array = new byte[4096];
        while (true) {
            final int read = inputStream.read(array);
            if (read == -1) {
                break;
            }
            outputStream.write(array, 0, read);
        }
    }
    
    public static Bitmap getBitmap(final Context context, final String s, final int n, final int n2) {
        final Bitmap bitmap = null;
        if (s == null) {
            return null;
        }
        final Uri parse = Uri.parse(s);
        Bitmap bitmap2;
        if ("resource".equals(parse.getScheme())) {
            bitmap2 = getBitmap(context, parse.getHost(), Integer.parseInt(parse.getLastPathSegment()), n, n2);
        }
        else if ("https".equals(parse.getScheme())) {
            bitmap2 = getRemoteBitmap(s, n, n2);
        }
        else {
            bitmap2 = bitmap;
            if ("http".equals(parse.getScheme())) {
                bitmap2 = getRemoteBitmap(s, n, n2);
            }
        }
        return bitmap2;
    }
    
    public static Bitmap getBitmap(final Context context, final String s, final int n, final int n2, final int n3) {
        final PackageManager packageManager = context.getPackageManager();
        final OutOfMemoryError outOfMemoryError = null;
        OutOfMemoryError outOfMemoryError2 = null;
        Bitmap scaledBitmap;
        try {
            final Resources resourcesForApplication = packageManager.getResourcesForApplication(s);
            final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
            bitmapFactory$Options.inJustDecodeBounds = true;
            final int i = n2 * 2;
            final int j = n3 * 2;
            BitmapFactory.decodeResource(resourcesForApplication, n, bitmapFactory$Options);
            if (i < bitmapFactory$Options.outWidth || j < bitmapFactory$Options.outHeight) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Stop loading drawable. The drawable size is too large. Limit size is [w=");
                sb.append(i);
                sb.append(", h=");
                sb.append(j);
                sb.append("]. Drawable size is [w=");
                sb.append(bitmapFactory$Options.outWidth);
                sb.append(", h=");
                sb.append(bitmapFactory$Options.outHeight);
                sb.append("]");
                CamLog.e(sb.toString());
                return null;
            }
            final Bitmap decodeResource = BitmapFactory.decodeResource(resourcesForApplication, n);
            outOfMemoryError2 = outOfMemoryError;
            if ((scaledBitmap = decodeResource) != null) {
                try {
                    scaledBitmap = Bitmap.createScaledBitmap(decodeResource, n2, n3, true);
                    outOfMemoryError2 = outOfMemoryError;
                }
                catch (final OutOfMemoryError outOfMemoryError2) {
                    scaledBitmap = decodeResource;
                }
                catch (final Resources$NotFoundException outOfMemoryError2) {
                    scaledBitmap = decodeResource;
                }
                catch (final PackageManager$NameNotFoundException outOfMemoryError2) {
                    scaledBitmap = decodeResource;
                }
            }
        }
        catch (final OutOfMemoryError outOfMemoryError2) {
            scaledBitmap = null;
        }
        catch (final Resources$NotFoundException outOfMemoryError2) {
            scaledBitmap = null;
        }
        catch (final PackageManager$NameNotFoundException outOfMemoryError2) {
            scaledBitmap = null;
        }
        if (outOfMemoryError2 != null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Could not get drawable. Message : ");
            sb2.append(outOfMemoryError2.getMessage());
            CamLog.e(sb2.toString());
        }
        return scaledBitmap;
    }
    
    private static Bitmap getRemoteBitmap(final String p0, final int p1, final int p2) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore          5
        //     3: new             Ljava/net/URL;
        //     6: astore_3       
        //     7: aload_3        
        //     8: aload_0        
        //     9: invokespecial   java/net/URL.<init>:(Ljava/lang/String;)V
        //    12: aload_3        
        //    13: invokevirtual   java/net/URL.openConnection:()Ljava/net/URLConnection;
        //    16: checkcast       Ljava/net/HttpURLConnection;
        //    19: astore_3       
        //    20: aload_3        
        //    21: astore_0       
        //    22: aload_3        
        //    23: ldc             60000
        //    25: invokevirtual   java/net/HttpURLConnection.setConnectTimeout:(I)V
        //    28: aload_3        
        //    29: astore_0       
        //    30: aload_3        
        //    31: ldc             60000
        //    33: invokevirtual   java/net/HttpURLConnection.setReadTimeout:(I)V
        //    36: new             Ljava/io/BufferedInputStream;
        //    39: astore          6
        //    41: aload           6
        //    43: aload_3        
        //    44: invokevirtual   java/net/HttpURLConnection.getInputStream:()Ljava/io/InputStream;
        //    47: invokespecial   java/io/BufferedInputStream.<init>:(Ljava/io/InputStream;)V
        //    50: new             Ljava/io/ByteArrayOutputStream;
        //    53: astore_0       
        //    54: aload_0        
        //    55: invokespecial   java/io/ByteArrayOutputStream.<init>:()V
        //    58: new             Ljava/io/BufferedOutputStream;
        //    61: astore          4
        //    63: aload           4
        //    65: aload_0        
        //    66: sipush          4096
        //    69: invokespecial   java/io/BufferedOutputStream.<init>:(Ljava/io/OutputStream;I)V
        //    72: aload           6
        //    74: aload           4
        //    76: invokestatic    com/sonyericsson/android/camera/view/modeselector/ResourceUtil.copy:(Ljava/io/InputStream;Ljava/io/OutputStream;)V
        //    79: aload           4
        //    81: invokevirtual   java/io/OutputStream.flush:()V
        //    84: aload_0        
        //    85: invokevirtual   java/io/ByteArrayOutputStream.toByteArray:()[B
        //    88: astore_0       
        //    89: aload_0        
        //    90: iconst_0       
        //    91: aload_0        
        //    92: arraylength    
        //    93: invokestatic    android/graphics/BitmapFactory.decodeByteArray:([BII)Landroid/graphics/Bitmap;
        //    96: astore          7
        //    98: aload           6
        //   100: ifnull          110
        //   103: aload_3        
        //   104: astore_0       
        //   105: aload           6
        //   107: invokevirtual   java/io/InputStream.close:()V
        //   110: aload           4
        //   112: ifnull          122
        //   115: aload_3        
        //   116: astore_0       
        //   117: aload           4
        //   119: invokevirtual   java/io/OutputStream.close:()V
        //   122: aload_3        
        //   123: ifnull          130
        //   126: aload_3        
        //   127: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   130: aload           7
        //   132: ifnull          162
        //   135: aload           7
        //   137: iload_1        
        //   138: iload_2        
        //   139: iconst_1       
        //   140: invokestatic    android/graphics/Bitmap.createScaledBitmap:(Landroid/graphics/Bitmap;IIZ)Landroid/graphics/Bitmap;
        //   143: astore_3       
        //   144: aload_3        
        //   145: astore_0       
        //   146: aload           7
        //   148: aload_3        
        //   149: if_acmpeq       177
        //   152: aload           7
        //   154: invokevirtual   android/graphics/Bitmap.recycle:()V
        //   157: aload_3        
        //   158: astore_0       
        //   159: goto            177
        //   162: iconst_1       
        //   163: anewarray       Ljava/lang/String;
        //   166: dup            
        //   167: iconst_0       
        //   168: ldc             "Bitmap is null."
        //   170: aastore        
        //   171: invokestatic    com/sonyericsson/android/camera/util/CamLog.e:([Ljava/lang/String;)V
        //   174: aload           5
        //   176: astore_0       
        //   177: aload_0        
        //   178: areturn        
        //   179: astore          5
        //   181: goto            200
        //   184: astore          5
        //   186: aconst_null    
        //   187: astore          4
        //   189: goto            200
        //   192: astore          5
        //   194: aconst_null    
        //   195: astore          6
        //   197: aconst_null    
        //   198: astore          4
        //   200: aload           6
        //   202: ifnull          212
        //   205: aload_3        
        //   206: astore_0       
        //   207: aload           6
        //   209: invokevirtual   java/io/InputStream.close:()V
        //   212: aload           4
        //   214: ifnull          224
        //   217: aload_3        
        //   218: astore_0       
        //   219: aload           4
        //   221: invokevirtual   java/io/OutputStream.close:()V
        //   224: aload_3        
        //   225: astore_0       
        //   226: aload           5
        //   228: athrow         
        //   229: astore          4
        //   231: goto            249
        //   234: astore          4
        //   236: goto            317
        //   239: astore_3       
        //   240: aconst_null    
        //   241: astore_0       
        //   242: goto            382
        //   245: astore          4
        //   247: aconst_null    
        //   248: astore_3       
        //   249: aload_3        
        //   250: astore_0       
        //   251: new             Ljava/lang/StringBuilder;
        //   254: astore          5
        //   256: aload_3        
        //   257: astore_0       
        //   258: aload           5
        //   260: invokespecial   java/lang/StringBuilder.<init>:()V
        //   263: aload_3        
        //   264: astore_0       
        //   265: aload           5
        //   267: ldc             "Could not open connection. "
        //   269: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   272: pop            
        //   273: aload_3        
        //   274: astore_0       
        //   275: aload           5
        //   277: aload           4
        //   279: invokevirtual   java/io/IOException.getMessage:()Ljava/lang/String;
        //   282: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   285: pop            
        //   286: aload_3        
        //   287: astore_0       
        //   288: iconst_1       
        //   289: anewarray       Ljava/lang/String;
        //   292: dup            
        //   293: iconst_0       
        //   294: aload           5
        //   296: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   299: aastore        
        //   300: invokestatic    com/sonyericsson/android/camera/util/CamLog.e:([Ljava/lang/String;)V
        //   303: aload_3        
        //   304: ifnull          311
        //   307: aload_3        
        //   308: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   311: aconst_null    
        //   312: areturn        
        //   313: astore          4
        //   315: aconst_null    
        //   316: astore_3       
        //   317: aload_3        
        //   318: astore_0       
        //   319: new             Ljava/lang/StringBuilder;
        //   322: astore          5
        //   324: aload_3        
        //   325: astore_0       
        //   326: aload           5
        //   328: invokespecial   java/lang/StringBuilder.<init>:()V
        //   331: aload_3        
        //   332: astore_0       
        //   333: aload           5
        //   335: ldc             "Malformed URL. "
        //   337: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   340: pop            
        //   341: aload_3        
        //   342: astore_0       
        //   343: aload           5
        //   345: aload           4
        //   347: invokevirtual   java/net/MalformedURLException.getMessage:()Ljava/lang/String;
        //   350: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   353: pop            
        //   354: aload_3        
        //   355: astore_0       
        //   356: iconst_1       
        //   357: anewarray       Ljava/lang/String;
        //   360: dup            
        //   361: iconst_0       
        //   362: aload           5
        //   364: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   367: aastore        
        //   368: invokestatic    com/sonyericsson/android/camera/util/CamLog.e:([Ljava/lang/String;)V
        //   371: aload_3        
        //   372: ifnull          379
        //   375: aload_3        
        //   376: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   379: aconst_null    
        //   380: areturn        
        //   381: astore_3       
        //   382: aload_0        
        //   383: ifnull          390
        //   386: aload_0        
        //   387: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   390: aload_3        
        //   391: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                            
        //  -----  -----  -----  -----  --------------------------------
        //  3      20     313    317    Ljava/net/MalformedURLException;
        //  3      20     245    249    Ljava/io/IOException;
        //  3      20     239    245    Any
        //  22     28     234    239    Ljava/net/MalformedURLException;
        //  22     28     229    234    Ljava/io/IOException;
        //  22     28     381    382    Any
        //  30     36     234    239    Ljava/net/MalformedURLException;
        //  30     36     229    234    Ljava/io/IOException;
        //  30     36     381    382    Any
        //  36     50     192    200    Any
        //  50     72     184    192    Any
        //  72     98     179    184    Any
        //  105    110    234    239    Ljava/net/MalformedURLException;
        //  105    110    229    234    Ljava/io/IOException;
        //  105    110    381    382    Any
        //  117    122    234    239    Ljava/net/MalformedURLException;
        //  117    122    229    234    Ljava/io/IOException;
        //  117    122    381    382    Any
        //  207    212    234    239    Ljava/net/MalformedURLException;
        //  207    212    229    234    Ljava/io/IOException;
        //  207    212    381    382    Any
        //  219    224    234    239    Ljava/net/MalformedURLException;
        //  219    224    229    234    Ljava/io/IOException;
        //  219    224    381    382    Any
        //  226    229    234    239    Ljava/net/MalformedURLException;
        //  226    229    229    234    Ljava/io/IOException;
        //  226    229    381    382    Any
        //  251    256    381    382    Any
        //  258    263    381    382    Any
        //  265    273    381    382    Any
        //  275    286    381    382    Any
        //  288    303    381    382    Any
        //  319    324    381    382    Any
        //  326    331    381    382    Any
        //  333    341    381    382    Any
        //  343    354    381    382    Any
        //  356    371    381    382    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0110:
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
    
    public static String getResourceUri(final Context context, final String str, final int i) {
        final StringBuilder sb = new StringBuilder();
        sb.append("resource://");
        sb.append(str);
        sb.append("/");
        sb.append(i);
        return sb.toString();
    }
    
    public static String getString(final Context context, final String s, final int n) {
        final PackageManager packageManager = context.getPackageManager();
        try {
            return packageManager.getResourcesForApplication(s).getString(n);
        }
        catch (final PackageManager$NameNotFoundException cause) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Could not get string. Message : ");
            sb.append(cause.getMessage());
            CamLog.e(sb.toString());
            throw new RuntimeException((Throwable)cause);
        }
    }
    
    public static String getString(Context ex, String o, final int n, final String ex2, final int endIndex) {
        try {
            ex = (RuntimeException)(o = getString((Context)ex, (String)o, n));
            try {
                if (endIndex < ((String)ex).length()) {
                    CamLog.w("Loaded string is over limit length.");
                    o = ((String)ex).substring(0, endIndex);
                }
                ex = null;
            }
            catch (final RuntimeException ex2) {
                o = ex;
                ex = ex2;
            }
            catch (final OutOfMemoryError ex2) {
                o = ex;
                ex = ex2;
            }
            catch (final Resources$NotFoundException ex2) {
                o = ex;
                ex = ex2;
            }
        }
        catch (final RuntimeException ex) {
            o = ex2;
        }
        catch (final OutOfMemoryError ex) {
            o = ex2;
        }
        catch (final Resources$NotFoundException ex) {
            o = ex2;
        }
        if (ex != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Could not get string. Message : ");
            sb.append(ex.getMessage());
            CamLog.e(sb.toString());
        }
        return (String)o;
    }
    
    private static boolean isCorrectResourceType(final Resources resources, final int n, final String str) {
        String resourceTypeName;
        try {
            resourceTypeName = resources.getResourceTypeName(n);
        }
        catch (final Resources$NotFoundException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Resource type is not appropriate. Message : ");
            sb.append(ex.getMessage());
            CamLog.w(sb.toString());
            resourceTypeName = null;
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("checkResourceType() : expected=");
            sb2.append(str);
            sb2.append(", actual=");
            sb2.append(resourceTypeName);
            CamLog.d(sb2.toString());
        }
        return str.equals(resourceTypeName);
    }
    
    public static boolean isDrawableResource(final Resources resources, final int n) {
        return isCorrectResourceType(resources, n, "drawable");
    }
    
    public static boolean isStringResource(final Resources resources, final int n) {
        return isCorrectResourceType(resources, n, "string");
    }
}
