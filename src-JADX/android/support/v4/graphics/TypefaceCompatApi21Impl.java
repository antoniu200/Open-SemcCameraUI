package android.support.v4.graphics;

import android.os.ParcelFileDescriptor;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import java.io.File;

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

    /* JADX WARN: Removed duplicated region for block: B:30:0x0052  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x006c  */
    /* JADX WARN: Removed duplicated region for block: B:64:? A[Catch: all -> 0x0061, Throwable -> 0x0064, SYNTHETIC, TRY_LEAVE, TryCatch #1 {all -> 0x0061, blocks: (B:7:0x0018, B:9:0x001e, B:12:0x0025, B:16:0x002f, B:19:0x003e, B:31:0x0054, B:35:0x005d, B:34:0x0059, B:36:0x0060), top: B:52:0x0018 }] */
    /* JADX WARN: Removed duplicated region for block: B:67:? A[Catch: IOException -> 0x007b, SYNTHETIC, TRY_LEAVE, TryCatch #7 {IOException -> 0x007b, blocks: (B:6:0x000e, B:14:0x002b, B:21:0x0043, B:45:0x006e, B:49:0x0077, B:48:0x0073, B:50:0x007a), top: B:59:0x000e, inners: #4 }] */
    @Override // android.support.v4.graphics.TypefaceCompatBaseImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public android.graphics.Typeface createFromFontInfo(android.content.Context r5, android.os.CancellationSignal r6, @android.support.annotation.NonNull android.support.v4.provider.FontsContractCompat.FontInfo[] r7, int r8) throws java.lang.Throwable {
        /*
            r4 = this;
            int r0 = r7.length
            r1 = 0
            r2 = 1
            if (r0 >= r2) goto L6
            return r1
        L6:
            android.support.v4.provider.FontsContractCompat$FontInfo r7 = r4.findBestInfo(r7, r8)
            android.content.ContentResolver r8 = r5.getContentResolver()
            android.net.Uri r7 = r7.getUri()     // Catch: java.io.IOException -> L7b
            java.lang.String r0 = "r"
            android.os.ParcelFileDescriptor r6 = r8.openFileDescriptor(r7, r0, r6)     // Catch: java.io.IOException -> L7b
            java.io.File r7 = r4.getFile(r6)     // Catch: java.lang.Throwable -> L61 java.lang.Throwable -> L64
            if (r7 == 0) goto L2f
            boolean r8 = r7.canRead()     // Catch: java.lang.Throwable -> L61 java.lang.Throwable -> L64
            if (r8 != 0) goto L25
            goto L2f
        L25:
            android.graphics.Typeface r4 = android.graphics.Typeface.createFromFile(r7)     // Catch: java.lang.Throwable -> L61 java.lang.Throwable -> L64
            if (r6 == 0) goto L2e
            r6.close()     // Catch: java.io.IOException -> L7b
        L2e:
            return r4
        L2f:
            java.io.FileInputStream r7 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L61 java.lang.Throwable -> L64
            java.io.FileDescriptor r8 = r6.getFileDescriptor()     // Catch: java.lang.Throwable -> L61 java.lang.Throwable -> L64
            r7.<init>(r8)     // Catch: java.lang.Throwable -> L61 java.lang.Throwable -> L64
            android.graphics.Typeface r4 = super.createFromInputStream(r5, r7)     // Catch: java.lang.Throwable -> L47 java.lang.Throwable -> L4a
            if (r7 == 0) goto L41
            r7.close()     // Catch: java.lang.Throwable -> L61 java.lang.Throwable -> L64
        L41:
            if (r6 == 0) goto L46
            r6.close()     // Catch: java.io.IOException -> L7b
        L46:
            return r4
        L47:
            r4 = move-exception
            r5 = r1
            goto L50
        L4a:
            r4 = move-exception
            throw r4     // Catch: java.lang.Throwable -> L4c
        L4c:
            r5 = move-exception
            r3 = r5
            r5 = r4
            r4 = r3
        L50:
            if (r7 == 0) goto L60
            if (r5 == 0) goto L5d
            r7.close()     // Catch: java.lang.Throwable -> L58 java.lang.Throwable -> L61
            goto L60
        L58:
            r7 = move-exception
            r5.addSuppressed(r7)     // Catch: java.lang.Throwable -> L61 java.lang.Throwable -> L64
            goto L60
        L5d:
            r7.close()     // Catch: java.lang.Throwable -> L61 java.lang.Throwable -> L64
        L60:
            throw r4     // Catch: java.lang.Throwable -> L61 java.lang.Throwable -> L64
        L61:
            r4 = move-exception
            r5 = r1
            goto L6a
        L64:
            r4 = move-exception
            throw r4     // Catch: java.lang.Throwable -> L66
        L66:
            r5 = move-exception
            r3 = r5
            r5 = r4
            r4 = r3
        L6a:
            if (r6 == 0) goto L7a
            if (r5 == 0) goto L77
            r6.close()     // Catch: java.lang.Throwable -> L72 java.io.IOException -> L7b
            goto L7a
        L72:
            r6 = move-exception
            r5.addSuppressed(r6)     // Catch: java.io.IOException -> L7b
            goto L7a
        L77:
            r6.close()     // Catch: java.io.IOException -> L7b
        L7a:
            throw r4     // Catch: java.io.IOException -> L7b
        L7b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.graphics.TypefaceCompatApi21Impl.createFromFontInfo(android.content.Context, android.os.CancellationSignal, android.support.v4.provider.FontsContractCompat$FontInfo[], int):android.graphics.Typeface");
    }
}
