package android.support.v4.graphics;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Typeface;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.provider.FontsContractCompat.FontInfo;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RequiresApi(21)
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class TypefaceCompatApi21Impl extends TypefaceCompatBaseImpl {
    private static final String TAG = "TypefaceCompatApi21Impl";

    TypefaceCompatApi21Impl() {
    }

    private File getFile(ParcelFileDescriptor parcelFileDescriptor) throws ErrnoException {
        try {
            String str = Os.readlink("/proc/self/fd/" + parcelFileDescriptor.getFd());
            if (OsConstants.S_ISREG(Os.stat(str).st_mode)) {
                return new File(str);
            }
            return null;
        } catch (ErrnoException unused) {
            return null;
        }
    }

    @Override // android.support.v4.graphics.TypefaceCompatBaseImpl
    public Typeface createFromFontInfo(Context context, CancellationSignal cancellationSignal,
            @NonNull FontInfo[] fonts, int style) {
        if (fonts.length < 1) {
            return null;
        }
        final FontInfo bestFont = findBestInfo(fonts, style);
        final ContentResolver resolver = context.getContentResolver();
        try (ParcelFileDescriptor pfd =
                     resolver.openFileDescriptor(bestFont.getUri(), "r", cancellationSignal)) {
            final File file = getFile(pfd);
            if (file == null || !file.canRead()) {
                // Unable to use the real file for creating Typeface. Fallback to copying
                // implementation.
                try (FileInputStream fis = new FileInputStream(pfd.getFileDescriptor())) {
                    return super.createFromInputStream(context, fis);
                }
            }
            return Typeface.createFromFile(file);
        } catch (IOException e) {
            return null;
        }
    }
}
