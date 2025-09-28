package com.sonyericsson.android.camera.view.modeselector;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.capability.SharedPrefsTranslator;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ResourceUtil {
    public static final String HTTPS_SCHEME = "https";
    public static final String HTTP_SCHEME = "http";
    private static final int ICON_SIZE_LIMIT_COEFFICIENT = 2;
    private static final int IO_BUFFER_SIZE = 4096;
    private static final int NETWORK_TIMEOUT = 60000;
    public static final String RESOURCE_SCHEME = "resource";
    public static final String RES_TYPE_NAME_DRAWABLE = "drawable";
    public static final String RES_TYPE_NAME_STRING = "string";
    private static final String TAG = "ResourceUtil";

    public static boolean isDrawableResource(Resources resources, int i) {
        return isCorrectResourceType(resources, i, RES_TYPE_NAME_DRAWABLE);
    }

    public static boolean isStringResource(Resources resources, int i) {
        return isCorrectResourceType(resources, i, RES_TYPE_NAME_STRING);
    }

    private static boolean isCorrectResourceType(Resources resources, int i, String str) throws Resources.NotFoundException {
        String resourceTypeName;
        try {
            resourceTypeName = resources.getResourceTypeName(i);
        } catch (Resources.NotFoundException e) {
            CamLog.w("Resource type is not appropriate. Message : " + e.getMessage());
            resourceTypeName = null;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("checkResourceType() : expected=" + str + ", actual=" + resourceTypeName);
        }
        return str.equals(resourceTypeName);
    }

    public static String getString(Context context, String str, int i) {
        try {
            return context.getPackageManager().getResourcesForApplication(str).getString(i);
        } catch (PackageManager.NameNotFoundException e) {
            CamLog.e("Could not get string. Message : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static String getString(Context context, String str, int i, String str2, int i2) {
        String string;
        try {
            string = getString(context, str, i);
            try {
                if (i2 < string.length()) {
                    CamLog.w("Loaded string is over limit length.");
                    string = string.substring(0, i2);
                }
                e = null;
            } catch (Resources.NotFoundException e) {
                e = e;
            } catch (OutOfMemoryError e2) {
                e = e2;
            } catch (RuntimeException e3) {
                e = e3;
            }
        } catch (Resources.NotFoundException e4) {
            e = e4;
            string = str2;
        } catch (OutOfMemoryError e5) {
            e = e5;
            string = str2;
        } catch (RuntimeException e6) {
            e = e6;
            string = str2;
        }
        if (e != null) {
            CamLog.e("Could not get string. Message : " + e.getMessage());
        }
        return string;
    }

    public static String getResourceUri(Context context, String str, int i) {
        return "resource://" + str + SharedPrefsTranslator.CONNECTOR_SLASH + i;
    }

    public static Bitmap getBitmap(Context context, String str, int i, int i2) {
        if (str == null) {
            return null;
        }
        Uri uri = Uri.parse(str);
        if (RESOURCE_SCHEME.equals(uri.getScheme())) {
            return getBitmap(context, uri.getHost(), Integer.parseInt(uri.getLastPathSegment()), i, i2);
        }
        if (HTTPS_SCHEME.equals(uri.getScheme())) {
            return getRemoteBitmap(str, i, i2);
        }
        if (HTTP_SCHEME.equals(uri.getScheme())) {
            return getRemoteBitmap(str, i, i2);
        }
        return null;
    }

    public static Bitmap getBitmap(Context context, String str, int i, int i2, int i3) throws PackageManager.NameNotFoundException {
        Bitmap bitmapDecodeResource;
        Resources resourcesForApplication;
        BitmapFactory.Options options;
        int i4;
        int i5;
        Throwable th = null;
        try {
            resourcesForApplication = context.getPackageManager().getResourcesForApplication(str);
            options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            i4 = i2 * 2;
            i5 = i3 * 2;
            BitmapFactory.decodeResource(resourcesForApplication, i, options);
        } catch (PackageManager.NameNotFoundException e) {
            th = e;
            bitmapDecodeResource = null;
        } catch (Resources.NotFoundException e2) {
            th = e2;
            bitmapDecodeResource = null;
        } catch (OutOfMemoryError e3) {
            th = e3;
            bitmapDecodeResource = null;
        }
        if (i4 >= options.outWidth && i5 >= options.outHeight) {
            bitmapDecodeResource = BitmapFactory.decodeResource(resourcesForApplication, i);
            if (bitmapDecodeResource != null) {
                try {
                    bitmapDecodeResource = Bitmap.createScaledBitmap(bitmapDecodeResource, i2, i3, true);
                } catch (PackageManager.NameNotFoundException e4) {
                    th = e4;
                } catch (Resources.NotFoundException e5) {
                    th = e5;
                } catch (OutOfMemoryError e6) {
                    th = e6;
                }
            }
            if (th != null) {
                CamLog.e("Could not get drawable. Message : " + th.getMessage());
            }
            return bitmapDecodeResource;
        }
        CamLog.e("Stop loading drawable. The drawable size is too large. Limit size is [w=" + i4 + ", h=" + i5 + "]. Drawable size is [w=" + options.outWidth + ", h=" + options.outHeight + "]");
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00c5  */
    /* JADX WARN: Type inference failed for: r7v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r7v1 */
    /* JADX WARN: Type inference failed for: r7v4, types: [java.net.HttpURLConnection] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static android.graphics.Bitmap getRemoteBitmap(java.lang.String r7, int r8, int r9) throws java.lang.Throwable {
        /*
            r0 = 1
            r1 = 0
            r2 = 0
            java.net.URL r3 = new java.net.URL     // Catch: java.lang.Throwable -> L77 java.io.IOException -> L7a java.net.MalformedURLException -> L9e
            r3.<init>(r7)     // Catch: java.lang.Throwable -> L77 java.io.IOException -> L7a java.net.MalformedURLException -> L9e
            java.net.URLConnection r7 = r3.openConnection()     // Catch: java.lang.Throwable -> L77 java.io.IOException -> L7a java.net.MalformedURLException -> L9e
            java.net.HttpURLConnection r7 = (java.net.HttpURLConnection) r7     // Catch: java.lang.Throwable -> L77 java.io.IOException -> L7a java.net.MalformedURLException -> L9e
            r3 = 60000(0xea60, float:8.4078E-41)
            r7.setConnectTimeout(r3)     // Catch: java.io.IOException -> L73 java.net.MalformedURLException -> L75 java.lang.Throwable -> Lc2
            r7.setReadTimeout(r3)     // Catch: java.io.IOException -> L73 java.net.MalformedURLException -> L75 java.lang.Throwable -> Lc2
            java.io.BufferedInputStream r3 = new java.io.BufferedInputStream     // Catch: java.lang.Throwable -> L65
            java.io.InputStream r4 = r7.getInputStream()     // Catch: java.lang.Throwable -> L65
            r3.<init>(r4)     // Catch: java.lang.Throwable -> L65
            java.io.ByteArrayOutputStream r4 = new java.io.ByteArrayOutputStream     // Catch: java.lang.Throwable -> L62
            r4.<init>()     // Catch: java.lang.Throwable -> L62
            java.io.BufferedOutputStream r5 = new java.io.BufferedOutputStream     // Catch: java.lang.Throwable -> L62
            r6 = 4096(0x1000, float:5.74E-42)
            r5.<init>(r4, r6)     // Catch: java.lang.Throwable -> L62
            copy(r3, r5)     // Catch: java.lang.Throwable -> L60
            r5.flush()     // Catch: java.lang.Throwable -> L60
            byte[] r4 = r4.toByteArray()     // Catch: java.lang.Throwable -> L60
            int r6 = r4.length     // Catch: java.lang.Throwable -> L60
            android.graphics.Bitmap r4 = android.graphics.BitmapFactory.decodeByteArray(r4, r1, r6)     // Catch: java.lang.Throwable -> L60
            if (r3 == 0) goto L40
            r3.close()     // Catch: java.io.IOException -> L73 java.net.MalformedURLException -> L75 java.lang.Throwable -> Lc2
        L40:
            if (r5 == 0) goto L45
            r5.close()     // Catch: java.io.IOException -> L73 java.net.MalformedURLException -> L75 java.lang.Throwable -> Lc2
        L45:
            if (r7 == 0) goto L4a
            r7.disconnect()
        L4a:
            if (r4 == 0) goto L56
            android.graphics.Bitmap r2 = android.graphics.Bitmap.createScaledBitmap(r4, r8, r9, r0)
            if (r4 == r2) goto L5f
            r4.recycle()
            goto L5f
        L56:
            java.lang.String r7 = "Bitmap is null."
            java.lang.String[] r7 = new java.lang.String[]{r7}
            com.sonyericsson.android.camera.util.CamLog.e(r7)
        L5f:
            return r2
        L60:
            r8 = move-exception
            goto L68
        L62:
            r8 = move-exception
            r5 = r2
            goto L68
        L65:
            r8 = move-exception
            r3 = r2
            r5 = r3
        L68:
            if (r3 == 0) goto L6d
            r3.close()     // Catch: java.io.IOException -> L73 java.net.MalformedURLException -> L75 java.lang.Throwable -> Lc2
        L6d:
            if (r5 == 0) goto L72
            r5.close()     // Catch: java.io.IOException -> L73 java.net.MalformedURLException -> L75 java.lang.Throwable -> Lc2
        L72:
            throw r8     // Catch: java.io.IOException -> L73 java.net.MalformedURLException -> L75 java.lang.Throwable -> Lc2
        L73:
            r8 = move-exception
            goto L7c
        L75:
            r8 = move-exception
            goto La0
        L77:
            r8 = move-exception
            r7 = r2
            goto Lc3
        L7a:
            r8 = move-exception
            r7 = r2
        L7c:
            java.lang.String[] r9 = new java.lang.String[r0]     // Catch: java.lang.Throwable -> Lc2
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lc2
            r0.<init>()     // Catch: java.lang.Throwable -> Lc2
            java.lang.String r3 = "Could not open connection. "
            r0.append(r3)     // Catch: java.lang.Throwable -> Lc2
            java.lang.String r8 = r8.getMessage()     // Catch: java.lang.Throwable -> Lc2
            r0.append(r8)     // Catch: java.lang.Throwable -> Lc2
            java.lang.String r8 = r0.toString()     // Catch: java.lang.Throwable -> Lc2
            r9[r1] = r8     // Catch: java.lang.Throwable -> Lc2
            com.sonyericsson.android.camera.util.CamLog.e(r9)     // Catch: java.lang.Throwable -> Lc2
            if (r7 == 0) goto L9d
            r7.disconnect()
        L9d:
            return r2
        L9e:
            r8 = move-exception
            r7 = r2
        La0:
            java.lang.String[] r9 = new java.lang.String[r0]     // Catch: java.lang.Throwable -> Lc2
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lc2
            r0.<init>()     // Catch: java.lang.Throwable -> Lc2
            java.lang.String r3 = "Malformed URL. "
            r0.append(r3)     // Catch: java.lang.Throwable -> Lc2
            java.lang.String r8 = r8.getMessage()     // Catch: java.lang.Throwable -> Lc2
            r0.append(r8)     // Catch: java.lang.Throwable -> Lc2
            java.lang.String r8 = r0.toString()     // Catch: java.lang.Throwable -> Lc2
            r9[r1] = r8     // Catch: java.lang.Throwable -> Lc2
            com.sonyericsson.android.camera.util.CamLog.e(r9)     // Catch: java.lang.Throwable -> Lc2
            if (r7 == 0) goto Lc1
            r7.disconnect()
        Lc1:
            return r2
        Lc2:
            r8 = move-exception
        Lc3:
            if (r7 == 0) goto Lc8
            r7.disconnect()
        Lc8:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sonyericsson.android.camera.view.modeselector.ResourceUtil.getRemoteBitmap(java.lang.String, int, int):android.graphics.Bitmap");
    }

    private static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[4096];
        while (true) {
            int i = inputStream.read(bArr);
            if (i == -1) {
                return;
            } else {
                outputStream.write(bArr, 0, i);
            }
        }
    }
}
