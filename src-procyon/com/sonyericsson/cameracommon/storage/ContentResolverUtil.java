// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.storage;

import android.database.sqlite.SQLiteFullException;
import com.sonyericsson.cameracommon.mediasaving.updator.CrUpdateParameter;
import com.sonyericsson.android.camera.util.CamLog;
import java.io.InputStream;
import android.net.Uri;
import android.content.Context;

public class ContentResolverUtil
{
    public static final String TAG = "ContentResolverUtil";
    
    public static InputStream crOpenInputStream(final Context context, final Uri uri) {
        InputStream openInputStream;
        try {
            openInputStream = context.getContentResolver().openInputStream(uri);
        }
        catch (final Exception ex) {
            if (CamLog.VERBOSE) {
                CamLog.w("crOpenInputStream failed.", ex);
            }
            openInputStream = null;
        }
        return openInputStream;
    }
    
    public static int crUpdate(final Context context, final Uri obj, final CrUpdateParameter crUpdateParameter) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("crUpdate: ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        try {
            context.getContentResolver().update(obj, crUpdateParameter.values, crUpdateParameter.where, crUpdateParameter.selectionArgs);
            goto Label_0084;
        }
        catch (final Exception ex) {
            if (CamLog.VERBOSE) {
                CamLog.w("crUpdate failed.", ex);
            }
        }
        catch (final SQLiteFullException ex2) {
            throw ex2;
        }
    }
}
