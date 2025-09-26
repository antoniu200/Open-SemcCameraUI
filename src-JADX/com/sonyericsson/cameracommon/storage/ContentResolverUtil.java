package com.sonyericsson.cameracommon.storage;

import android.content.Context;
import android.database.sqlite.SQLiteFullException;
import android.net.Uri;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.mediasaving.updator.CrUpdateParameter;
import java.io.InputStream;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ContentResolverUtil {
    public static final String TAG = "ContentResolverUtil";

    public static int crUpdate(Context context, Uri uri, CrUpdateParameter crUpdateParameter) {
        int iUpdate;
        if (CamLog.VERBOSE) {
            CamLog.d("crUpdate: " + uri);
        }
        try {
            iUpdate = context.getContentResolver().update(uri, crUpdateParameter.values, crUpdateParameter.where, crUpdateParameter.selectionArgs);
        } catch (SQLiteFullException e) {
            throw e;
        } catch (Exception e2) {
            if (CamLog.VERBOSE) {
                CamLog.w("crUpdate failed.", e2);
            }
            iUpdate = 0;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("crUpdate: result: " + iUpdate);
        }
        return iUpdate;
    }

    public static InputStream crOpenInputStream(Context context, Uri uri) {
        try {
            return context.getContentResolver().openInputStream(uri);
        } catch (Exception e) {
            if (CamLog.VERBOSE) {
                CamLog.w("crOpenInputStream failed.", e);
            }
            return null;
        }
    }
}
