// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.graphics;

import android.os.ParcelFileDescriptor;
import java.nio.MappedByteBuffer;
import android.content.ContentResolver;
import java.nio.channels.FileChannel;
import java.io.FileInputStream;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Process;
import android.os.StrictMode$ThreadPolicy;
import android.util.Log;
import java.io.FileOutputStream;
import android.os.StrictMode;
import java.io.InputStream;
import android.support.annotation.RequiresApi;
import android.support.annotation.Nullable;
import java.io.File;
import java.nio.ByteBuffer;
import android.content.res.Resources;
import android.content.Context;
import java.io.IOException;
import java.io.Closeable;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class TypefaceCompatUtil
{
    private static final String CACHE_FILE_PREFIX = ".font";
    private static final String TAG = "TypefaceCompatUtil";
    
    private TypefaceCompatUtil() {
    }
    
    public static void closeQuietly(final Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        }
        catch (final IOException ex) {}
    }
    
    @Nullable
    @RequiresApi(19)
    public static ByteBuffer copyToDirectBuffer(Context tempFile, final Resources resources, final int n) {
        tempFile = (Context)getTempFile(tempFile);
        if (tempFile == null) {
            return null;
        }
        try {
            if (!copyToFile((File)tempFile, resources, n)) {
                return null;
            }
            return mmap((File)tempFile);
        }
        finally {
            ((File)tempFile).delete();
        }
    }
    
    public static boolean copyToFile(final File file, final Resources resources, final int n) {
        Closeable closeable;
        try {
            final InputStream openRawResource = resources.openRawResource(n);
            try {
                final boolean copyToFile = copyToFile(file, openRawResource);
                closeQuietly(openRawResource);
                return copyToFile;
            }
            finally {}
        }
        finally {
            closeable = null;
        }
        closeQuietly(closeable);
    }
    
    public static boolean copyToFile(final File file, final InputStream ex) {
        final StrictMode$ThreadPolicy allowThreadDiskWrites = StrictMode.allowThreadDiskWrites();
        final Closeable closeable = null;
        Closeable closeable2 = null;
        Closeable closeable3;
        try {
            try {
                closeable2 = closeable2;
                final FileOutputStream fileOutputStream = new FileOutputStream(file, false);
                try {
                    final byte[] array = new byte[1024];
                    while (true) {
                        final int read = ((InputStream)ex).read(array);
                        if (read == -1) {
                            break;
                        }
                        fileOutputStream.write(array, 0, read);
                    }
                    closeQuietly(fileOutputStream);
                    StrictMode.setThreadPolicy(allowThreadDiskWrites);
                    return true;
                }
                catch (final IOException ex) {}
                finally {
                    closeable2 = fileOutputStream;
                }
            }
            finally {}
        }
        catch (final IOException ex) {
            closeable3 = closeable;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Error copying resource contents to temp file: ");
        sb.append(ex.getMessage());
        Log.e("TypefaceCompatUtil", sb.toString());
        closeQuietly(closeable3);
        StrictMode.setThreadPolicy(allowThreadDiskWrites);
        return false;
        closeQuietly(closeable2);
        StrictMode.setThreadPolicy(allowThreadDiskWrites);
    }
    
    @Nullable
    public static File getTempFile(final Context context) {
        final StringBuilder sb = new StringBuilder();
        sb.append(".font");
        sb.append(Process.myPid());
        sb.append("-");
        sb.append(Process.myTid());
        sb.append("-");
        final String string = sb.toString();
        int i = 0;
    Label_0116_Outer:
        while (true) {
            Label_0122: {
                if (i >= 100) {
                    break Label_0122;
                }
                final File cacheDir = context.getCacheDir();
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(string);
                sb2.append(i);
                final File file = new File(cacheDir, sb2.toString());
                while (true) {
                    try {
                        if (file.createNewFile()) {
                            return file;
                        }
                        ++i;
                        continue Label_0116_Outer;
                        return null;
                    }
                    catch (final IOException ex) {
                        continue;
                    }
                    break;
                }
            }
        }
    }
    
    @Nullable
    @RequiresApi(19)
    public static ByteBuffer mmap(Context t, final CancellationSignal cancellationSignal, Uri openFileDescriptor) {
        final ContentResolver contentResolver = ((Context)t).getContentResolver();
        try {
            openFileDescriptor = (Uri)contentResolver.openFileDescriptor(openFileDescriptor, "r", cancellationSignal);
            if (openFileDescriptor == null) {
                if (openFileDescriptor != null) {
                    ((ParcelFileDescriptor)openFileDescriptor).close();
                }
                return null;
            }
            try {
                final FileInputStream fileInputStream = new FileInputStream(((ParcelFileDescriptor)openFileDescriptor).getFileDescriptor());
                try {
                    final FileChannel channel = fileInputStream.getChannel();
                    final MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_ONLY, 0L, channel.size());
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                    if (openFileDescriptor != null) {
                        ((ParcelFileDescriptor)openFileDescriptor).close();
                    }
                    return map;
                }
                catch (final Throwable t) {
                    try {
                        throw t;
                    }
                    finally {}
                }
                finally {
                    t = null;
                }
                if (fileInputStream != null) {
                    if (t != null) {
                        try {
                            fileInputStream.close();
                        }
                        catch (final Throwable exception) {
                            t.addSuppressed(exception);
                        }
                    }
                    else {
                        fileInputStream.close();
                    }
                }
                throw cancellationSignal;
            }
            catch (final Throwable t) {
                try {
                    throw t;
                }
                finally {}
            }
            finally {
                t = null;
            }
            if (openFileDescriptor != null) {
                if (t != null) {
                    try {
                        ((ParcelFileDescriptor)openFileDescriptor).close();
                    }
                    catch (final Throwable exception2) {
                        t.addSuppressed(exception2);
                    }
                }
                else {
                    ((ParcelFileDescriptor)openFileDescriptor).close();
                }
            }
            throw cancellationSignal;
        }
        catch (final IOException ex) {
            return null;
        }
    }
    
    @Nullable
    @RequiresApi(19)
    private static ByteBuffer mmap(final File file) {
        try {
            final FileInputStream fileInputStream = new FileInputStream(file);
            Throwable t = null;
            try {
                final FileChannel channel = fileInputStream.getChannel();
                final MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_ONLY, 0L, channel.size());
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                return map;
            }
            catch (final Throwable t) {
                try {
                    throw t;
                }
                finally {}
            }
            finally {
                t = null;
            }
            if (fileInputStream != null) {
                if (t != null) {
                    try {
                        fileInputStream.close();
                    }
                    catch (final Throwable exception) {
                        t.addSuppressed(exception);
                    }
                }
                else {
                    fileInputStream.close();
                }
            }
            throw file;
        }
        catch (final IOException ex) {
            return null;
        }
    }
}
