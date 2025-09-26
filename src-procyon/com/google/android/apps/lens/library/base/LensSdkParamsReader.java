// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.apps.lens.library.base;

import java.util.Iterator;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.content.pm.PackageInfo;
import android.content.Intent;
import android.os.Build$VERSION;
import android.content.pm.PackageManager$NameNotFoundException;
import android.util.Log;
import android.support.annotation.VisibleForTesting;
import java.util.ArrayList;
import android.support.annotation.NonNull;
import android.content.pm.PackageManager;
import android.content.Context;
import java.util.List;
import com.google.android.apps.lens.library.base.proto.nano.LensSdkParamsProto;

public class LensSdkParamsReader
{
    public static final String AGSA_AUTHORITY = "com.google.android.googlequicksearchbox.GsaPublicContentProvider";
    private static final LensSdkParamsProto.LensSdkParams DEFAULT_PARAMS;
    public static final String LENS_AR_STICKERS_ACTIVITY = "com.google.vr.apps.ornament.app.MainActivity";
    public static final String LENS_AR_STICKERS_PACKAGE = "com.google.ar.lens";
    public static final String LENS_AVAILABILITY_PROVIDER_URI;
    private static final String LENS_SDK_VERSION = "0.1.0";
    private static final int MIN_AR_CORE_VERSION = 24;
    private static final String TAG = "LensSdkParamsReader";
    private final List<LensSdkParamsCallback> callbacks;
    private final Context context;
    private LensSdkParamsProto.LensSdkParams lensSdkParams;
    private boolean lensSdkParamsReady;
    private final PackageManager packageManager;
    
    static {
        LENS_AVAILABILITY_PROVIDER_URI = String.format("content://%s/publicvalue/lens_oem_availability", "com.google.android.googlequicksearchbox.GsaPublicContentProvider");
        DEFAULT_PARAMS = new LensSdkParamsProto.LensSdkParams();
        LensSdkParamsReader.DEFAULT_PARAMS.lensSdkVersion = "0.1.0";
        LensSdkParamsReader.DEFAULT_PARAMS.agsaVersionName = "";
        LensSdkParamsReader.DEFAULT_PARAMS.lensAvailabilityStatus = -1;
        LensSdkParamsReader.DEFAULT_PARAMS.arStickersAvailabilityStatus = -1;
    }
    
    public LensSdkParamsReader(@NonNull final Context context) {
        this(context, context.getPackageManager());
    }
    
    @VisibleForTesting
    LensSdkParamsReader(@NonNull final Context context, @NonNull final PackageManager packageManager) {
        this.callbacks = new ArrayList<LensSdkParamsCallback>();
        this.context = context;
        this.packageManager = packageManager;
        this.updateParams();
    }
    
    private void updateParams() {
        this.lensSdkParamsReady = false;
        this.lensSdkParams = LensSdkParamsReader.DEFAULT_PARAMS.clone();
        try {
            final PackageInfo packageInfo = this.packageManager.getPackageInfo("com.google.android.googlequicksearchbox", 0);
            if (packageInfo != null) {
                this.lensSdkParams.agsaVersionName = packageInfo.versionName;
            }
        }
        catch (final PackageManager$NameNotFoundException ex) {
            Log.e("LensSdkParamsReader", "Unable to find agsa package: com.google.android.googlequicksearchbox");
        }
        this.lensSdkParams.arStickersAvailabilityStatus = 1;
        if (Build$VERSION.SDK_INT >= 24) {
            final Intent intent = new Intent();
            intent.setClassName("com.google.ar.lens", "com.google.vr.apps.ornament.app.MainActivity");
            if (this.packageManager.resolveActivity(intent, 0) != null) {
                this.lensSdkParams.arStickersAvailabilityStatus = 0;
            }
        }
        new QueryGsaTask().execute((Object[])new Void[0]);
    }
    
    public String getAgsaVersionName() {
        return this.lensSdkParams.agsaVersionName;
    }
    
    public int getArStickersAvailability() {
        return this.lensSdkParams.arStickersAvailabilityStatus;
    }
    
    public String getLensSdkVersion() {
        return this.lensSdkParams.lensSdkVersion;
    }
    
    public void getParams(@NonNull final LensSdkParamsCallback lensSdkParamsCallback) {
        if (this.lensSdkParamsReady) {
            lensSdkParamsCallback.onLensSdkParamsAvailable(this.lensSdkParams);
            return;
        }
        this.callbacks.add(lensSdkParamsCallback);
    }
    
    public interface LensSdkParamsCallback
    {
        void onLensSdkParamsAvailable(final LensSdkParamsProto.LensSdkParams p0);
    }
    
    private class QueryGsaTask extends AsyncTask<Void, Void, Integer>
    {
        final LensSdkParamsReader this$0;
        
        private QueryGsaTask(final LensSdkParamsReader this$0) {
            this.this$0 = this$0;
        }
        
        protected Integer doInBackground(Void... query) {
            final Cursor cursor = null;
            Throwable t = null;
            Label_0114: {
                try {
                    query = (Void[])(Object)this.this$0.context.getContentResolver().query(Uri.parse(LensSdkParamsReader.LENS_AVAILABILITY_PROVIDER_URI), (String[])null, (String)null, (String[])null, (String)null);
                    if (query != null) {
                        try {
                            if (((Cursor)(Object)query).getCount() != 0) {
                                ((Cursor)(Object)query).moveToFirst();
                                int int1;
                                if ((int1 = Integer.parseInt(((Cursor)(Object)query).getString(0))) > 6) {
                                    int1 = 6;
                                }
                                if (query != null) {
                                    ((Cursor)(Object)query).close();
                                }
                                return int1;
                            }
                        }
                        finally {
                            break Label_0114;
                        }
                    }
                    if (query != null) {
                        ((Cursor)(Object)query).close();
                    }
                    return 4;
                }
                finally {
                    final Throwable t2;
                    t = t2;
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            throw t;
        }
        
        protected void onPostExecute(final Integer obj) {
            final String value = String.valueOf(obj);
            final StringBuilder sb = new StringBuilder(25 + String.valueOf(value).length());
            sb.append("Lens availability result:");
            sb.append(value);
            Log.i("LensSdkParamsReader", sb.toString());
            this.this$0.lensSdkParams.lensAvailabilityStatus = obj;
            this.this$0.lensSdkParamsReady = true;
            final Iterator iterator = this.this$0.callbacks.iterator();
            while (iterator.hasNext()) {
                ((LensSdkParamsCallback)iterator.next()).onLensSdkParamsAvailable(this.this$0.lensSdkParams);
            }
            this.this$0.callbacks.clear();
        }
    }
}
