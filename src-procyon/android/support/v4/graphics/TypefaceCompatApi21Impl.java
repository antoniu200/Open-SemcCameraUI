// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.graphics;

import android.content.ContentResolver;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.provider.FontsContractCompat;
import android.os.CancellationSignal;
import android.content.Context;
import android.system.ErrnoException;
import android.system.OsConstants;
import android.system.Os;
import java.io.File;
import android.os.ParcelFileDescriptor;
import android.support.annotation.RestrictTo;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
class TypefaceCompatApi21Impl extends TypefaceCompatBaseImpl
{
    private static final String TAG = "TypefaceCompatApi21Impl";
    
    private File getFile(final ParcelFileDescriptor parcelFileDescriptor) {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("/proc/self/fd/");
            sb.append(parcelFileDescriptor.getFd());
            final String readlink = Os.readlink(sb.toString());
            if (OsConstants.S_ISREG(Os.stat(readlink).st_mode)) {
                return new File(readlink);
            }
            return null;
        }
        catch (final ErrnoException ex) {
            return null;
        }
    }
    
    @Override
    public Typeface createFromFontInfo(final Context context, CancellationSignal t, @NonNull FontsContractCompat.FontInfo[] openFileDescriptor, final int n) {
        if (openFileDescriptor.length < 1) {
            return null;
        }
        final FontsContractCompat.FontInfo bestInfo = this.findBestInfo(openFileDescriptor, n);
        final ContentResolver contentResolver = context.getContentResolver();
        try {
            openFileDescriptor = (FontsContractCompat.FontInfo[])(Object)contentResolver.openFileDescriptor(bestInfo.getUri(), "r", (CancellationSignal)t);
            try {
                final File file = this.getFile((ParcelFileDescriptor)(Object)openFileDescriptor);
                if (file != null && file.canRead()) {
                    final Typeface fromFile = Typeface.createFromFile(file);
                    if (openFileDescriptor != null) {
                        ((ParcelFileDescriptor)(Object)openFileDescriptor).close();
                    }
                    return fromFile;
                }
                final FileInputStream fileInputStream = new FileInputStream(((ParcelFileDescriptor)(Object)openFileDescriptor).getFileDescriptor());
                try {
                    final Typeface fromInputStream = super.createFromInputStream(context, fileInputStream);
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                    if (openFileDescriptor != null) {
                        ((ParcelFileDescriptor)(Object)openFileDescriptor).close();
                    }
                    return fromInputStream;
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
                throw context;
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
                        ((ParcelFileDescriptor)(Object)openFileDescriptor).close();
                    }
                    catch (final Throwable exception2) {
                        t.addSuppressed(exception2);
                    }
                }
                else {
                    ((ParcelFileDescriptor)(Object)openFileDescriptor).close();
                }
            }
            throw context;
        }
        catch (final IOException ex) {
            return null;
        }
    }
}
