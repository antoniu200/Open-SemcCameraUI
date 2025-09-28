package android.support.v4.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.os.Process;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.util.Log;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValueHolder;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TypefaceCompatUtil {
    private static final String CACHE_FILE_PREFIX = ".font";
    private static final String TAG = "TypefaceCompatUtil";

    private TypefaceCompatUtil() {
    }

    @Nullable
    public static File getTempFile(Context context) {
        String str = CACHE_FILE_PREFIX + Process.myPid() + UserSettingValueHolder.DELIMITER + Process.myTid() + UserSettingValueHolder.DELIMITER;
        for (int i = 0; i < 100; i++) {
            File file = new File(context.getCacheDir(), str + i);
            if (file.createNewFile()) {
                return file;
            }
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0027  */
    /* JADX WARN: Removed duplicated region for block: B:30:? A[Catch: IOException -> 0x0036, SYNTHETIC, TRY_LEAVE, TryCatch #3 {IOException -> 0x0036, blocks: (B:3:0x0001, B:6:0x0018, B:16:0x0029, B:20:0x0032, B:19:0x002e, B:21:0x0035), top: B:27:0x0001, inners: #1 }] */
    @android.support.annotation.RequiresApi(19)
    @android.support.annotation.Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.nio.ByteBuffer mmap(java.io.File r9) throws java.lang.Throwable {
        /*
            r0 = 0
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch: java.io.IOException -> L36
            r1.<init>(r9)     // Catch: java.io.IOException -> L36
            java.nio.channels.FileChannel r2 = r1.getChannel()     // Catch: java.lang.Throwable -> L1c java.lang.Throwable -> L1f
            long r6 = r2.size()     // Catch: java.lang.Throwable -> L1c java.lang.Throwable -> L1f
            java.nio.channels.FileChannel$MapMode r3 = java.nio.channels.FileChannel.MapMode.READ_ONLY     // Catch: java.lang.Throwable -> L1c java.lang.Throwable -> L1f
            r4 = 0
            java.nio.MappedByteBuffer r9 = r2.map(r3, r4, r6)     // Catch: java.lang.Throwable -> L1c java.lang.Throwable -> L1f
            if (r1 == 0) goto L1b
            r1.close()     // Catch: java.io.IOException -> L36
        L1b:
            return r9
        L1c:
            r9 = move-exception
            r2 = r0
            goto L25
        L1f:
            r9 = move-exception
            throw r9     // Catch: java.lang.Throwable -> L21
        L21:
            r2 = move-exception
            r8 = r2
            r2 = r9
            r9 = r8
        L25:
            if (r1 == 0) goto L35
            if (r2 == 0) goto L32
            r1.close()     // Catch: java.lang.Throwable -> L2d java.io.IOException -> L36
            goto L35
        L2d:
            r1 = move-exception
            r2.addSuppressed(r1)     // Catch: java.io.IOException -> L36
            goto L35
        L32:
            r1.close()     // Catch: java.io.IOException -> L36
        L35:
            throw r9     // Catch: java.io.IOException -> L36
        L36:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.graphics.TypefaceCompatUtil.mmap(java.io.File):java.nio.ByteBuffer");
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0042  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x005c  */
    /* JADX WARN: Removed duplicated region for block: B:56:? A[Catch: all -> 0x0051, Throwable -> 0x0054, SYNTHETIC, TRY_LEAVE, TryCatch #1 {all -> 0x0051, blocks: (B:8:0x0013, B:11:0x002e, B:23:0x0044, B:27:0x004d, B:26:0x0049, B:28:0x0050), top: B:44:0x0013 }] */
    /* JADX WARN: Removed duplicated region for block: B:59:? A[Catch: IOException -> 0x006b, SYNTHETIC, TRY_LEAVE, TryCatch #7 {IOException -> 0x006b, blocks: (B:3:0x0005, B:6:0x000f, B:13:0x0033, B:37:0x005e, B:41:0x0067, B:40:0x0063, B:42:0x006a), top: B:51:0x0005, inners: #4 }] */
    @android.support.annotation.RequiresApi(19)
    @android.support.annotation.Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.nio.ByteBuffer mmap(android.content.Context r8, android.os.CancellationSignal r9, android.net.Uri r10) throws java.lang.Throwable {
        /*
            android.content.ContentResolver r8 = r8.getContentResolver()
            r0 = 0
            java.lang.String r1 = "r"
            android.os.ParcelFileDescriptor r8 = r8.openFileDescriptor(r10, r1, r9)     // Catch: java.io.IOException -> L6b
            if (r8 != 0) goto L13
            if (r8 == 0) goto L12
            r8.close()     // Catch: java.io.IOException -> L6b
        L12:
            return r0
        L13:
            java.io.FileInputStream r9 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L51 java.lang.Throwable -> L54
            java.io.FileDescriptor r10 = r8.getFileDescriptor()     // Catch: java.lang.Throwable -> L51 java.lang.Throwable -> L54
            r9.<init>(r10)     // Catch: java.lang.Throwable -> L51 java.lang.Throwable -> L54
            java.nio.channels.FileChannel r1 = r9.getChannel()     // Catch: java.lang.Throwable -> L37 java.lang.Throwable -> L3a
            long r5 = r1.size()     // Catch: java.lang.Throwable -> L37 java.lang.Throwable -> L3a
            java.nio.channels.FileChannel$MapMode r2 = java.nio.channels.FileChannel.MapMode.READ_ONLY     // Catch: java.lang.Throwable -> L37 java.lang.Throwable -> L3a
            r3 = 0
            java.nio.MappedByteBuffer r10 = r1.map(r2, r3, r5)     // Catch: java.lang.Throwable -> L37 java.lang.Throwable -> L3a
            if (r9 == 0) goto L31
            r9.close()     // Catch: java.lang.Throwable -> L51 java.lang.Throwable -> L54
        L31:
            if (r8 == 0) goto L36
            r8.close()     // Catch: java.io.IOException -> L6b
        L36:
            return r10
        L37:
            r10 = move-exception
            r1 = r0
            goto L40
        L3a:
            r10 = move-exception
            throw r10     // Catch: java.lang.Throwable -> L3c
        L3c:
            r1 = move-exception
            r7 = r1
            r1 = r10
            r10 = r7
        L40:
            if (r9 == 0) goto L50
            if (r1 == 0) goto L4d
            r9.close()     // Catch: java.lang.Throwable -> L48 java.lang.Throwable -> L51
            goto L50
        L48:
            r9 = move-exception
            r1.addSuppressed(r9)     // Catch: java.lang.Throwable -> L51 java.lang.Throwable -> L54
            goto L50
        L4d:
            r9.close()     // Catch: java.lang.Throwable -> L51 java.lang.Throwable -> L54
        L50:
            throw r10     // Catch: java.lang.Throwable -> L51 java.lang.Throwable -> L54
        L51:
            r9 = move-exception
            r10 = r0
            goto L5a
        L54:
            r9 = move-exception
            throw r9     // Catch: java.lang.Throwable -> L56
        L56:
            r10 = move-exception
            r7 = r10
            r10 = r9
            r9 = r7
        L5a:
            if (r8 == 0) goto L6a
            if (r10 == 0) goto L67
            r8.close()     // Catch: java.lang.Throwable -> L62 java.io.IOException -> L6b
            goto L6a
        L62:
            r8 = move-exception
            r10.addSuppressed(r8)     // Catch: java.io.IOException -> L6b
            goto L6a
        L67:
            r8.close()     // Catch: java.io.IOException -> L6b
        L6a:
            throw r9     // Catch: java.io.IOException -> L6b
        L6b:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.graphics.TypefaceCompatUtil.mmap(android.content.Context, android.os.CancellationSignal, android.net.Uri):java.nio.ByteBuffer");
    }

    @RequiresApi(19)
    @Nullable
    public static ByteBuffer copyToDirectBuffer(Context context, Resources resources, int i) {
        File tempFile = getTempFile(context);
        if (tempFile == null) {
            return null;
        }
        try {
            if (copyToFile(tempFile, resources, i)) {
                return mmap(tempFile);
            }
            return null;
        } finally {
            tempFile.delete();
        }
    }

    public static boolean copyToFile(File file, InputStream inputStream) throws Throwable {
        StrictMode.ThreadPolicy threadPolicyAllowThreadDiskWrites = StrictMode.allowThreadDiskWrites();
        FileOutputStream fileOutputStream = null;
        try {
            try {
                FileOutputStream fileOutputStream2 = new FileOutputStream(file, false);
                try {
                    byte[] bArr = new byte[1024];
                    while (true) {
                        int i = inputStream.read(bArr);
                        if (i != -1) {
                            fileOutputStream2.write(bArr, 0, i);
                        } else {
                            closeQuietly(fileOutputStream2);
                            StrictMode.setThreadPolicy(threadPolicyAllowThreadDiskWrites);
                            return true;
                        }
                    }
                } catch (IOException e) {
                    e = e;
                    fileOutputStream = fileOutputStream2;
                    Log.e(TAG, "Error copying resource contents to temp file: " + e.getMessage());
                    closeQuietly(fileOutputStream);
                    StrictMode.setThreadPolicy(threadPolicyAllowThreadDiskWrites);
                    return false;
                } catch (Throwable th) {
                    th = th;
                    fileOutputStream = fileOutputStream2;
                    closeQuietly(fileOutputStream);
                    StrictMode.setThreadPolicy(threadPolicyAllowThreadDiskWrites);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (IOException e2) {
            e = e2;
        }
    }

    public static boolean copyToFile(File file, Resources resources, int i) throws Throwable {
        InputStream inputStreamOpenRawResource;
        try {
            inputStreamOpenRawResource = resources.openRawResource(i);
        } catch (Throwable th) {
            th = th;
            inputStreamOpenRawResource = null;
        }
        try {
            boolean zCopyToFile = copyToFile(file, inputStreamOpenRawResource);
            closeQuietly(inputStreamOpenRawResource);
            return zCopyToFile;
        } catch (Throwable th2) {
            th = th2;
            closeQuietly(inputStreamOpenRawResource);
            throw th;
        }
    }

    public static void closeQuietly(Closeable closeable) throws IOException {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException unused) {
            }
        }
    }
}
