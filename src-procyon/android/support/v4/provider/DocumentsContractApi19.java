// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.provider;

import android.database.Cursor;
import android.provider.DocumentsContract;
import android.content.ContentResolver;
import android.util.Log;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.net.Uri;
import android.content.Context;
import android.support.annotation.RequiresApi;

@RequiresApi(19)
class DocumentsContractApi19
{
    private static final int FLAG_VIRTUAL_DOCUMENT = 512;
    private static final String TAG = "DocumentFile";
    
    private DocumentsContractApi19() {
    }
    
    public static boolean canRead(final Context context, final Uri uri) {
        return context.checkCallingOrSelfUriPermission(uri, 1) == 0 && !TextUtils.isEmpty((CharSequence)getRawType(context, uri));
    }
    
    public static boolean canWrite(final Context context, final Uri uri) {
        if (context.checkCallingOrSelfUriPermission(uri, 2) != 0) {
            return false;
        }
        final String rawType = getRawType(context, uri);
        final int queryForInt = queryForInt(context, uri, "flags", 0);
        return !TextUtils.isEmpty((CharSequence)rawType) && ((queryForInt & 0x4) != 0x0 || ("vnd.android.document/directory".equals(rawType) && (queryForInt & 0x8) != 0x0) || (!TextUtils.isEmpty((CharSequence)rawType) && (queryForInt & 0x2) != 0x0));
    }
    
    private static void closeQuietly(@Nullable final AutoCloseable autoCloseable) {
        if (autoCloseable == null) {
            goto Label_0016;
        }
        try {
            autoCloseable.close();
            goto Label_0016;
        }
        catch (final RuntimeException ex) {
            throw ex;
        }
        catch (final Exception ex2) {
            goto Label_0016;
        }
    }
    
    public static boolean exists(Context context, Uri query) {
        final ContentResolver contentResolver = context.getContentResolver();
        boolean b = false;
        final AutoCloseable autoCloseable = null;
        context = null;
        AutoCloseable autoCloseable2;
        try {
            try {
                query = (Uri)contentResolver.query(query, new String[] { "document_id" }, (String)null, (String[])null, (String)null);
                try {
                    if (((Cursor)query).getCount() > 0) {
                        b = true;
                    }
                    closeQuietly((AutoCloseable)query);
                    return b;
                }
                catch (final Exception contentResolver) {}
                finally {
                    context = (Context)query;
                }
            }
            finally {}
        }
        catch (final Exception contentResolver) {
            autoCloseable2 = autoCloseable;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Failed query: ");
        sb.append(contentResolver);
        Log.w("DocumentFile", sb.toString());
        closeQuietly(autoCloseable2);
        return false;
        closeQuietly((AutoCloseable)context);
    }
    
    public static long getFlags(final Context context, final Uri uri) {
        return queryForLong(context, uri, "flags", 0L);
    }
    
    @Nullable
    public static String getName(final Context context, final Uri uri) {
        return queryForString(context, uri, "_display_name", null);
    }
    
    @Nullable
    private static String getRawType(final Context context, final Uri uri) {
        return queryForString(context, uri, "mime_type", null);
    }
    
    @Nullable
    public static String getType(final Context context, final Uri uri) {
        final String rawType = getRawType(context, uri);
        if ("vnd.android.document/directory".equals(rawType)) {
            return null;
        }
        return rawType;
    }
    
    public static boolean isDirectory(final Context context, final Uri uri) {
        return "vnd.android.document/directory".equals(getRawType(context, uri));
    }
    
    public static boolean isFile(final Context context, final Uri uri) {
        final String rawType = getRawType(context, uri);
        return !"vnd.android.document/directory".equals(rawType) && !TextUtils.isEmpty((CharSequence)rawType);
    }
    
    public static boolean isVirtual(final Context context, final Uri uri) {
        final boolean documentUri = DocumentsContract.isDocumentUri(context, uri);
        boolean b = false;
        if (!documentUri) {
            return false;
        }
        if ((getFlags(context, uri) & 0x200L) != 0x0L) {
            b = true;
        }
        return b;
    }
    
    public static long lastModified(final Context context, final Uri uri) {
        return queryForLong(context, uri, "last_modified", 0L);
    }
    
    public static long length(final Context context, final Uri uri) {
        return queryForLong(context, uri, "_size", 0L);
    }
    
    private static int queryForInt(final Context context, final Uri uri, final String s, final int n) {
        return (int)queryForLong(context, uri, s, n);
    }
    
    private static long queryForLong(Context context, Uri query, final String obj, final long n) {
        final ContentResolver contentResolver = context.getContentResolver();
        final AutoCloseable autoCloseable = null;
        context = null;
        AutoCloseable autoCloseable2;
        try {
            try {
                query = (Uri)contentResolver.query(query, new String[] { (String)obj }, (String)null, (String[])null, (String)null);
                try {
                    if (((Cursor)query).moveToFirst() && !((Cursor)query).isNull(0)) {
                        final long long1 = ((Cursor)query).getLong(0);
                        closeQuietly((AutoCloseable)query);
                        return long1;
                    }
                    closeQuietly((AutoCloseable)query);
                    return n;
                }
                catch (final Exception obj) {}
                finally {
                    context = (Context)query;
                }
            }
            finally {}
        }
        catch (final Exception obj) {
            autoCloseable2 = autoCloseable;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Failed query: ");
        sb.append(obj);
        Log.w("DocumentFile", sb.toString());
        closeQuietly(autoCloseable2);
        return n;
        closeQuietly((AutoCloseable)context);
    }
    
    @Nullable
    private static String queryForString(Context string, Uri query, final String obj, @Nullable final String s) {
        final ContentResolver contentResolver = string.getContentResolver();
        final AutoCloseable autoCloseable = null;
        string = null;
        AutoCloseable autoCloseable2;
        try {
            try {
                query = (Uri)contentResolver.query(query, new String[] { (String)obj }, (String)null, (String[])null, (String)null);
                try {
                    if (((Cursor)query).moveToFirst() && !((Cursor)query).isNull(0)) {
                        string = (Context)((Cursor)query).getString(0);
                        closeQuietly((AutoCloseable)query);
                        return (String)string;
                    }
                    closeQuietly((AutoCloseable)query);
                    return s;
                }
                catch (final Exception obj) {}
                finally {
                    string = (Context)query;
                }
            }
            finally {}
        }
        catch (final Exception obj) {
            autoCloseable2 = autoCloseable;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Failed query: ");
        sb.append(obj);
        Log.w("DocumentFile", sb.toString());
        closeQuietly(autoCloseable2);
        return s;
        closeQuietly((AutoCloseable)string);
    }
}
