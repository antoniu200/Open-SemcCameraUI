package com.sonymobile.media;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class SomcMediaStore {
    public static final String AUTHORITY_NEW = "somcmedia";
    public static final int AUTHORITY_NEW_VERSION = 400;
    public static final String AUTHORITY_OLD = "media";
    public static final String EXTERNAL_VOLUME = "external";
    private static String versionCache;

    public static final class ExtendedFiles {

        public interface ExtendedFileColumns extends MediaStore.Files.FileColumns {
            public static final int SOMC_CATEGORY_NONE = 0;
            public static final int SOMC_CATEGORY_SEQUENCE = 2;
            public static final int SOMC_CATEGORY_SEQUENCE_COVER = 3;
            public static final int SOMC_CATEGORY_SINGLES = 1;
            public static final String SOMC_FILE_TYPE = "somctype";
            public static final int SOMC_FILE_TYPE_AR_EFFECT = 7;
            public static final int SOMC_FILE_TYPE_BACKGROUND_DEFOCUS = 9;
            public static final int SOMC_FILE_TYPE_BURST_COVER = 2;
            public static final int SOMC_FILE_TYPE_BURST_IMAGE = 129;
            public static final int SOMC_FILE_TYPE_CINEMAGRAPH = 8;
            public static final int SOMC_FILE_TYPE_HIGHLIGHT_VIDEO = 14;
            public static final int SOMC_FILE_TYPE_HI_RES_AUDIO = 13;
            public static final int SOMC_FILE_TYPE_INFO_EYE = 5;
            public static final int SOMC_FILE_TYPE_NONE = 0;
            public static final int SOMC_FILE_TYPE_SOCIAL_CAST = 6;
            public static final int SOMC_FILE_TYPE_SOUNDPHOTO = 42;
            public static final int SOMC_FILE_TYPE_TIMESHIFT_COVER = 4;
            public static final int SOMC_FILE_TYPE_TIMESHIFT_IMAGE = 130;
            public static final int SOMC_FILE_TYPE_TIMESHIFT_VIDEO = 12;
            public static final int SOMC_FILE_TYPE_TIMESHIFT_VIDEO_120F = 11;
            public static final int SOMC_FILE_TYPE_WIKITUDE = 10;
            public static final String SOMC_FOLDER_CATEGORY = "somccategory";
        }

        public static Uri getContentUri(String str) {
            return getContentUri(SomcMediaStore.getContext(), str);
        }

        public static Uri getContentUri(String str, long j) {
            return getContentUri(SomcMediaStore.getContext(), str, j);
        }

        public static Uri getContentUri(Context context, String str) {
            return getContentUri(str, SomcMediaStore.getVersion(context));
        }

        public static Uri getContentUri(Context context, String str, long j) {
            return getContentUri(str, j, SomcMediaStore.getVersion(context));
        }

        public static Uri getContentUri(String str, String str2) {
            if (str2 != null && Integer.parseInt(str2) >= 400) {
                return Uri.parse("content://somcmedia/" + str + "/extended_file");
            }
            return Uri.parse("content://media/" + str + "/extended_file");
        }

        public static Uri getContentUri(String str, long j, String str2) {
            Uri contentUri = getContentUri(str, str2);
            return contentUri != null ? Uri.withAppendedPath(contentUri, String.valueOf(j)) : contentUri;
        }
    }

    public static Uri makeMediaStoreUri(Uri uri) {
        return makeMediaStoreUri(getContext(), uri);
    }

    public static Uri makeMediaStoreUri(Context context, Uri uri) {
        return makeMediaStoreUri(uri, getVersion(context));
    }

    public static Uri makeMediaStoreUri(Uri uri, String str) {
        String authority = uri.getAuthority();
        List<String> pathSegments = uri.getPathSegments();
        if (pathSegments.size() < 2) {
            return null;
        }
        if (str != null && Integer.parseInt(str) >= 400) {
            if (authority.equals(AUTHORITY_NEW) && pathSegments.get(1).equals("extended_file")) {
                return Uri.parse(Uri.parse(uri.toString().replaceFirst("extended_file", "file")).toString().replaceFirst(AUTHORITY_NEW, AUTHORITY_OLD));
            }
            return null;
        }
        if (authority.equals(AUTHORITY_OLD) && pathSegments.get(1).equals("extended_file")) {
            return Uri.parse(uri.toString().replaceFirst("extended_file", "file"));
        }
        return null;
    }

    public static String getVersion(Context context) throws Throwable {
        String versionNew = versionCache;
        if (versionNew == null) {
            versionNew = getVersionNew(context);
            if (versionNew == null && (versionNew = getVersionOld(context)) == null) {
                versionNew = getVersionTooOld(context);
            }
            versionCache = versionNew;
        }
        return versionNew;
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x003d A[PHI: r8
  0x003d: PHI (r8v5 android.database.Cursor) = (r8v4 android.database.Cursor), (r8v8 android.database.Cursor) binds: [B:20:0x003b, B:13:0x0030] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.lang.String getVersionTooOld(android.content.Context r8) throws java.lang.Throwable {
        /*
            r0 = 0
            android.content.ContentResolver r1 = r8.getContentResolver()     // Catch: java.lang.Throwable -> L33 java.lang.Exception -> L3a
            java.lang.String r8 = "content://media/internal/extended_version"
            android.net.Uri r2 = android.net.Uri.parse(r8)     // Catch: java.lang.Throwable -> L33 java.lang.Exception -> L3a
            java.lang.String r8 = "version"
            java.lang.String[] r3 = new java.lang.String[]{r8}     // Catch: java.lang.Throwable -> L33 java.lang.Exception -> L3a
            r4 = 0
            r5 = 0
            r6 = 0
            android.database.Cursor r8 = r1.query(r2, r3, r4, r5, r6)     // Catch: java.lang.Throwable -> L33 java.lang.Exception -> L3a
            if (r8 == 0) goto L30
            boolean r1 = r8.moveToFirst()     // Catch: java.lang.Throwable -> L2b java.lang.Exception -> L3b
            if (r1 == 0) goto L30
            r1 = 0
            java.lang.String r1 = r8.getString(r1)     // Catch: java.lang.Throwable -> L2b java.lang.Exception -> L3b
            if (r8 == 0) goto L2a
            r8.close()
        L2a:
            return r1
        L2b:
            r0 = move-exception
            r7 = r0
            r0 = r8
            r8 = r7
            goto L34
        L30:
            if (r8 == 0) goto L40
            goto L3d
        L33:
            r8 = move-exception
        L34:
            if (r0 == 0) goto L39
            r0.close()
        L39:
            throw r8
        L3a:
            r8 = r0
        L3b:
            if (r8 == 0) goto L40
        L3d:
            r8.close()
        L40:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sonymobile.media.SomcMediaStore.getVersionTooOld(android.content.Context):java.lang.String");
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x003e A[PHI: r8
  0x003e: PHI (r8v5 android.database.Cursor) = (r8v4 android.database.Cursor), (r8v8 android.database.Cursor) binds: [B:20:0x003c, B:13:0x0031] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.lang.String getVersionOld(android.content.Context r8) throws java.lang.Throwable {
        /*
            r0 = 0
            android.content.ContentResolver r1 = r8.getContentResolver()     // Catch: java.lang.Throwable -> L34 java.lang.Exception -> L3b
            java.lang.String r8 = "content://media/external/extended_version"
            android.net.Uri r2 = android.net.Uri.parse(r8)     // Catch: java.lang.Throwable -> L34 java.lang.Exception -> L3b
            java.lang.String r8 = "value"
            java.lang.String[] r3 = new java.lang.String[]{r8}     // Catch: java.lang.Throwable -> L34 java.lang.Exception -> L3b
            java.lang.String r4 = "key='version'"
            r5 = 0
            r6 = 0
            android.database.Cursor r8 = r1.query(r2, r3, r4, r5, r6)     // Catch: java.lang.Throwable -> L34 java.lang.Exception -> L3b
            if (r8 == 0) goto L31
            boolean r1 = r8.moveToFirst()     // Catch: java.lang.Throwable -> L2c java.lang.Exception -> L3c
            if (r1 == 0) goto L31
            r1 = 0
            java.lang.String r1 = r8.getString(r1)     // Catch: java.lang.Throwable -> L2c java.lang.Exception -> L3c
            if (r8 == 0) goto L2b
            r8.close()
        L2b:
            return r1
        L2c:
            r0 = move-exception
            r7 = r0
            r0 = r8
            r8 = r7
            goto L35
        L31:
            if (r8 == 0) goto L41
            goto L3e
        L34:
            r8 = move-exception
        L35:
            if (r0 == 0) goto L3a
            r0.close()
        L3a:
            throw r8
        L3b:
            r8 = r0
        L3c:
            if (r8 == 0) goto L41
        L3e:
            r8.close()
        L41:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sonymobile.media.SomcMediaStore.getVersionOld(android.content.Context):java.lang.String");
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0038 A[PHI: r8
  0x0038: PHI (r8v5 android.database.Cursor) = (r8v4 android.database.Cursor), (r8v7 android.database.Cursor) binds: [B:20:0x0036, B:13:0x002b] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.lang.String getVersionNew(android.content.Context r8) throws java.lang.Throwable {
        /*
            r0 = 0
            android.content.ContentResolver r1 = r8.getContentResolver()     // Catch: java.lang.Throwable -> L2e java.lang.Exception -> L35
            java.lang.String r8 = "content://somcmedia/none/version"
            android.net.Uri r2 = android.net.Uri.parse(r8)     // Catch: java.lang.Throwable -> L2e java.lang.Exception -> L35
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            android.database.Cursor r8 = r1.query(r2, r3, r4, r5, r6)     // Catch: java.lang.Throwable -> L2e java.lang.Exception -> L35
            if (r8 == 0) goto L2b
            boolean r1 = r8.moveToFirst()     // Catch: java.lang.Throwable -> L26 java.lang.Exception -> L36
            if (r1 == 0) goto L2b
            r1 = 0
            java.lang.String r1 = r8.getString(r1)     // Catch: java.lang.Throwable -> L26 java.lang.Exception -> L36
            if (r8 == 0) goto L25
            r8.close()
        L25:
            return r1
        L26:
            r0 = move-exception
            r7 = r0
            r0 = r8
            r8 = r7
            goto L2f
        L2b:
            if (r8 == 0) goto L3b
            goto L38
        L2e:
            r8 = move-exception
        L2f:
            if (r0 == 0) goto L34
            r0.close()
        L34:
            throw r8
        L35:
            r8 = r0
        L36:
            if (r8 == 0) goto L3b
        L38:
            r8.close()
        L3b:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sonymobile.media.SomcMediaStore.getVersionNew(android.content.Context):java.lang.String");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Context getContext() {
        try {
            return (Context) Class.forName("android.app.ActivityThread").getMethod("currentApplication", new Class[0]).invoke(null, (Object[]) null);
        } catch (Exception unused) {
            return null;
        }
    }
}
