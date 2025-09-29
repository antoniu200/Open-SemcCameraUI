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

    private static String getVersionTooOld(Context query) {
        Object string = null;
        try {
            query = (Context)query.getContentResolver().query(Uri.parse("content://media/internal/extended_version"), new String[] { "version" }, (String)null, (String[])null, (String)null);
            if (query == null) {
                goto Label_0068;
            }
            try {
                if (((Cursor)query).moveToFirst()) {
                    string = ((Cursor)query).getString(0);
                    if (query != null) {
                        ((Cursor)query).close();
                    }
                    return (String)string;
                }
                goto Label_0068;
            }
            catch (final Exception string) {}
            finally {
                string = query;
                final Context context;
                query = context;
            }
        }
        catch (final Exception ex) {}
    }

    private static String getVersionOld(Context query) {
        try {
            query = (Context)query.getContentResolver().query(Uri.parse("content://media/external/extended_version"), new String[] { "value" }, "key='version'", (String[])null, (String)null);
            if (query == null) {
                goto Label_0065;
            }
            try {
                if (((Cursor)query).moveToFirst()) {
                    final String string = ((Cursor)query).getString(0);
                    if (query != null) {
                        ((Cursor)query).close();
                    }
                    return string;
                }
                goto Label_0065;
            }
            catch (final Exception ex) {}
        }
        catch (final Exception ex2) {}
    }

    private static String getVersionNew(Context query) {
        try {
            query = (Context)query.getContentResolver().query(Uri.parse("content://somcmedia/none/version"), (String[])null, (String)null, (String[])null, (String)null);
            if (query == null) {
                goto Label_0056;
            }
            try {
                if (((Cursor)query).moveToFirst()) {
                    final String string = ((Cursor)query).getString(0);
                    if (query != null) {
                        ((Cursor)query).close();
                    }
                    return string;
                }
                goto Label_0056;
            }
            catch (final Exception ex) {}
        }
        catch (final Exception ex2) {}
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
