// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.contentsview;

import android.content.ContentUris;
import java.util.Locale;
import android.database.Cursor;
import com.sonyericsson.cameracommon.mediasaving.updator.CrQueryParameter;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.mediasaving.updator.CrDeleteParameter;
import android.net.Uri;
import android.content.ContentResolver;

public class PhotoStackQueryHelper
{
    public static final String TAG = "PhotoStackQueryHelper";
    
    public static int crDelete(final ContentResolver contentResolver, final Uri uri, final CrDeleteParameter crDeleteParameter) {
        synchronized (PhotoStackQueryHelper.class) {
            if (CamLog.VERBOSE) {
                CamLog.d("crDelete() is called");
            }
            int delete;
            try {
                delete = contentResolver.delete(uri, crDeleteParameter.where, crDeleteParameter.selectionArgs);
            }
            catch (final Exception ex) {
                if (CamLog.VERBOSE) {
                    CamLog.w("crDelete: failed.", ex);
                }
                delete = 0;
            }
            return delete;
        }
    }
    
    public static Cursor crQuery(final ContentResolver contentResolver, final Uri uri, final CrQueryParameter crQueryParameter) {
        synchronized (PhotoStackQueryHelper.class) {
            if (CamLog.VERBOSE) {
                CamLog.d("crQuery() is called");
            }
            Cursor query;
            try {
                String s;
                if (crQueryParameter.limit > 0) {
                    s = String.format(Locale.US, "%s limit %d offset %d", crQueryParameter.sortOrder, crQueryParameter.limit, crQueryParameter.offset);
                }
                else {
                    s = crQueryParameter.sortOrder;
                }
                query = contentResolver.query(uri, crQueryParameter.projection, crQueryParameter.where, crQueryParameter.selectionArgs, s);
            }
            catch (final Exception ex) {
                if (CamLog.VERBOSE) {
                    CamLog.w("crQuery: failed:", ex);
                }
                query = null;
            }
            return query;
        }
    }
    
    public static boolean deleteImage(final ContentResolver contentResolver, final Uri uri) {
        if (uri == null) {
            return false;
        }
        final CrQueryParameter crQueryParameter = new CrQueryParameter();
        crQueryParameter.projection = new String[] { "_id", "_data" };
        crQueryParameter.where = null;
        crQueryParameter.selectionArgs = null;
        crQueryParameter.sortOrder = null;
        crQueryParameter.limit = 0;
        crQueryParameter.offset = 0;
        final Cursor crQuery = crQuery(contentResolver, uri, crQueryParameter);
        int n;
        if (crQuery != null) {
            int i = 0;
            n = 0;
            while (i < crQuery.getCount()) {
                crQuery.moveToPosition(i);
                final String string = crQuery.getString(1);
                if (CamLog.VERBOSE) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Deleting ");
                    sb.append(string);
                    sb.append("...");
                    CamLog.d(sb.toString());
                }
                final CrDeleteParameter crDeleteParameter = new CrDeleteParameter();
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("_id=");
                sb2.append(Long.valueOf(ContentUris.parseId(uri)).toString());
                crDeleteParameter.where = sb2.toString();
                crDeleteParameter.selectionArgs = null;
                int n2 = n;
                if (crDelete(contentResolver, uri, crDeleteParameter) != 1) {
                    n2 = n + 1;
                }
                ++i;
                n = n2;
            }
            crQuery.close();
        }
        else {
            if (CamLog.VERBOSE) {
                CamLog.d("deleteImage() failed: Cursor is null.");
            }
            n = 0;
        }
        return n == 0;
    }
}
