// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.media;

import android.database.Cursor;
import android.provider.MediaStore$Files$FileColumns;
import java.util.List;
import android.net.Uri;
import android.content.Context;

public final class SomcMediaStore
{
    public static final String AUTHORITY_NEW = "somcmedia";
    public static final int AUTHORITY_NEW_VERSION = 400;
    public static final String AUTHORITY_OLD = "media";
    public static final String EXTERNAL_VOLUME = "external";
    private static String versionCache;
    
    private static Context getContext() {
        Context context = null;
        try {
            context = (Context)Class.forName("android.app.ActivityThread").getMethod("currentApplication", (Class<?>[])new Class[0]).invoke(null, (Object[])null);
            return context;
        }
        catch (final Exception ex) {
            return context;
        }
    }
    
    public static String getVersion(final Context context) {
        String versionCache;
        if ((versionCache = SomcMediaStore.versionCache) == null) {
            if ((versionCache = getVersionNew(context)) == null && (versionCache = getVersionOld(context)) == null) {
                versionCache = getVersionTooOld(context);
            }
            SomcMediaStore.versionCache = versionCache;
        }
        return versionCache;
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
    
    public static Uri makeMediaStoreUri(final Context context, final Uri uri) {
        return makeMediaStoreUri(uri, getVersion(context));
    }
    
    public static Uri makeMediaStoreUri(final Uri uri) {
        return makeMediaStoreUri(getContext(), uri);
    }
    
    public static Uri makeMediaStoreUri(final Uri uri, final String s) {
        final String authority = uri.getAuthority();
        final List pathSegments = uri.getPathSegments();
        final int size = pathSegments.size();
        final Uri uri2 = null;
        if (size < 2) {
            return null;
        }
        Uri uri3;
        if (s != null && Integer.parseInt(s) >= 400) {
            uri3 = uri2;
            if (authority.equals("somcmedia")) {
                uri3 = uri2;
                if (((String)pathSegments.get(1)).equals("extended_file")) {
                    uri3 = Uri.parse(Uri.parse(uri.toString().replaceFirst("extended_file", "file")).toString().replaceFirst("somcmedia", "media"));
                }
            }
        }
        else {
            uri3 = uri2;
            if (authority.equals("media")) {
                uri3 = uri2;
                if (((String)pathSegments.get(1)).equals("extended_file")) {
                    uri3 = Uri.parse(uri.toString().replaceFirst("extended_file", "file"));
                }
            }
        }
        return uri3;
    }
    
    public static final class ExtendedFiles
    {
        public static Uri getContentUri(final Context context, final String s) {
            return getContentUri(s, SomcMediaStore.getVersion(context));
        }
        
        public static Uri getContentUri(final Context context, final String s, final long n) {
            return getContentUri(s, n, SomcMediaStore.getVersion(context));
        }
        
        public static Uri getContentUri(final String s) {
            return getContentUri(getContext(), s);
        }
        
        public static Uri getContentUri(final String s, final long n) {
            return getContentUri(getContext(), s, n);
        }
        
        public static Uri getContentUri(final String s, final long l, final String s2) {
            Uri uri2;
            final Uri uri = uri2 = getContentUri(s, s2);
            if (uri != null) {
                uri2 = Uri.withAppendedPath(uri, String.valueOf(l));
            }
            return uri2;
        }
        
        public static Uri getContentUri(final String s, final String s2) {
            Uri uri;
            if (s2 != null && Integer.parseInt(s2) >= 400) {
                final StringBuilder sb = new StringBuilder();
                sb.append("content://somcmedia/");
                sb.append(s);
                sb.append("/extended_file");
                uri = Uri.parse(sb.toString());
            }
            else {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("content://media/");
                sb2.append(s);
                sb2.append("/extended_file");
                uri = Uri.parse(sb2.toString());
            }
            return uri;
        }
        
        public interface ExtendedFileColumns extends MediaStore$Files$FileColumns
        {
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
    }
}
