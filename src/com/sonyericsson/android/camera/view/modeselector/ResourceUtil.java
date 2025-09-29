package com.sonyericsson.android.camera.view.modeselector;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.capability.SharedPrefsTranslator;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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

    private static android.graphics.Bitmap getRemoteBitmap(String url, int reqW, int reqH) throws Throwable {
        HttpURLConnection conn = null;
        BufferedInputStream in = null;
        BufferedOutputStream bout = null;
        ByteArrayOutputStream baos = null;

        try {
            URL u = new URL(url);
            conn = (HttpURLConnection) u.openConnection();
            final int TIMEOUT = 60000;
            conn.setConnectTimeout(TIMEOUT);
            conn.setReadTimeout(TIMEOUT);

            in = new BufferedInputStream(conn.getInputStream());
            baos = new ByteArrayOutputStream();
            bout = new BufferedOutputStream(baos, 4096);

            // matches fallback: copy(in, out) then flush
            copy(in, bout);
            bout.flush();

            // decode full image from memory, as in fallback
            byte[] data = baos.toByteArray();
            Bitmap decoded = BitmapFactory.decodeByteArray(data, 0, data.length);

            // close streams (then disconnect), like the fallback does
            try { in.close(); } catch (Exception ignore) {}
            in = null;
            try { bout.close(); } catch (Exception ignore) {}
            bout = null;
            try { conn.disconnect(); } catch (Exception ignore) {}
            conn = null;

            if (decoded != null) {
                Bitmap scaled = Bitmap.createScaledBitmap(decoded, reqW, reqH, true);
                if (decoded != scaled) {
                    try { decoded.recycle(); } catch (Throwable ignore) {}
                }
                return scaled;
            } else {
                CamLog.e(new String[] { "Bitmap is null." });
                return null;
            }

        } catch (IOException e) {
            CamLog.e(new String[] { "Could not open connection. " + e.getMessage() });
            if (conn != null) {
                try { conn.disconnect(); } catch (Exception ignore) {}
            }
            return null;

        } catch (MalformedURLException e) {
            CamLog.e(new String[] { "Malformed URL. " + e.getMessage() });
            if (conn != null) {
                try { conn.disconnect(); } catch (Exception ignore) {}
            }
            return null;

        } catch (Throwable t) {
            // ensure disconnect on fatal path, as in fallback
            if (conn != null) {
                try { conn.disconnect(); } catch (Exception ignore) {}
            }
            throw t;

        } finally {
            // mirror the fallback’s explicit closes for safety
            if (in != null) {
                try { in.close(); } catch (Exception ignore) {}
            }
            if (bout != null) {
                try { bout.close(); } catch (Exception ignore) {}
            }
            if (baos != null) {
                try { baos.close(); } catch (Exception ignore) {}
            }
        }
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
