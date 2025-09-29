package android.support.v4.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.os.CancellationSignal;
import android.os.Process;
import android.os.ParcelFileDescriptor;
import android.os.StrictMode;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.util.Log;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValueHolder;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

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

    @RequiresApi(19)
    @Nullable
    private static ByteBuffer mmap(File file) {
        try (FileInputStream in = new FileInputStream(file)) {
            FileChannel channel = in.getChannel();
            long size = channel.size();
            MappedByteBuffer mapped = channel.map(FileChannel.MapMode.READ_ONLY, 0, size);
            return mapped;
        } catch (IOException e) {
            return null;
        }
    }

    @RequiresApi(19)
    @Nullable
    public static ByteBuffer mmap(Context context,
                                  android.os.CancellationSignal cancellationSignal,
                                  android.net.Uri uri) {
        try (android.os.ParcelFileDescriptor pfd =
                 context.getContentResolver().openFileDescriptor(uri, "r", cancellationSignal)) {
            if (pfd == null) return null;
            try (FileInputStream in = new FileInputStream(pfd.getFileDescriptor())) {
                FileChannel channel = in.getChannel();
                long size = channel.size();
                MappedByteBuffer mapped = channel.map(FileChannel.MapMode.READ_ONLY, 0, size);
                return mapped;
            }
        } catch (IOException e) {
            return null;
        }
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
