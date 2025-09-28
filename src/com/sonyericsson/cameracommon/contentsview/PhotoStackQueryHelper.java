package com.sonyericsson.cameracommon.contentsview;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.mediasaving.updator.CrDeleteParameter;
import com.sonyericsson.cameracommon.mediasaving.updator.CrQueryParameter;
import java.util.Locale;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PhotoStackQueryHelper {
    public static final String TAG = "PhotoStackQueryHelper";

    public static boolean deleteImage(ContentResolver contentResolver, Uri uri) {
        int i;
        if (uri == null) {
            return false;
        }
        CrQueryParameter crQueryParameter = new CrQueryParameter();
        crQueryParameter.projection = new String[]{"_id", "_data"};
        crQueryParameter.where = null;
        crQueryParameter.selectionArgs = null;
        crQueryParameter.sortOrder = null;
        crQueryParameter.limit = 0;
        crQueryParameter.offset = 0;
        Cursor cursorCrQuery = crQuery(contentResolver, uri, crQueryParameter);
        if (cursorCrQuery != null) {
            i = 0;
            for (int i2 = 0; i2 < cursorCrQuery.getCount(); i2++) {
                cursorCrQuery.moveToPosition(i2);
                String string = cursorCrQuery.getString(1);
                if (CamLog.VERBOSE) {
                    CamLog.d("Deleting " + string + "...");
                }
                CrDeleteParameter crDeleteParameter = new CrDeleteParameter();
                crDeleteParameter.where = "_id=" + Long.valueOf(ContentUris.parseId(uri)).toString();
                crDeleteParameter.selectionArgs = null;
                if (crDelete(contentResolver, uri, crDeleteParameter) != 1) {
                    i++;
                }
            }
            cursorCrQuery.close();
        } else {
            if (CamLog.VERBOSE) {
                CamLog.d("deleteImage() failed: Cursor is null.");
            }
            i = 0;
        }
        return i == 0;
    }

    public static synchronized Cursor crQuery(ContentResolver contentResolver, Uri uri, CrQueryParameter crQueryParameter) {
        Cursor cursorQuery;
        String str;
        if (CamLog.VERBOSE) {
            CamLog.d("crQuery() is called");
        }
        try {
            if (crQueryParameter.limit > 0) {
                str = String.format(Locale.US, "%s limit %d offset %d", crQueryParameter.sortOrder, Integer.valueOf(crQueryParameter.limit), Integer.valueOf(crQueryParameter.offset));
            } else {
                str = crQueryParameter.sortOrder;
            }
            cursorQuery = contentResolver.query(uri, crQueryParameter.projection, crQueryParameter.where, crQueryParameter.selectionArgs, str);
        } catch (Exception e) {
            if (CamLog.VERBOSE) {
                CamLog.w("crQuery: failed:", e);
            }
            cursorQuery = null;
        }
        return cursorQuery;
    }

    public static synchronized int crDelete(ContentResolver contentResolver, Uri uri, CrDeleteParameter crDeleteParameter) {
        int iDelete;
        if (CamLog.VERBOSE) {
            CamLog.d("crDelete() is called");
        }
        try {
            iDelete = contentResolver.delete(uri, crDeleteParameter.where, crDeleteParameter.selectionArgs);
        } catch (Exception e) {
            if (CamLog.VERBOSE) {
                CamLog.w("crDelete: failed.", e);
            }
            iDelete = 0;
        }
        return iDelete;
    }
}
