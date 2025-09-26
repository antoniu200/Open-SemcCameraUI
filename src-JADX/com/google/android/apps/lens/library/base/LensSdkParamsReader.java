package com.google.android.apps.lens.library.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import com.google.android.apps.lens.library.base.proto.nano.LensSdkParamsProto;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class LensSdkParamsReader {
    public static final String LENS_AR_STICKERS_ACTIVITY = "com.google.vr.apps.ornament.app.MainActivity";
    public static final String LENS_AR_STICKERS_PACKAGE = "com.google.ar.lens";
    private static final String LENS_SDK_VERSION = "0.1.0";
    private static final int MIN_AR_CORE_VERSION = 24;
    private static final String TAG = "LensSdkParamsReader";
    private final List<LensSdkParamsCallback> callbacks;
    private final Context context;
    private LensSdkParamsProto.LensSdkParams lensSdkParams;
    private boolean lensSdkParamsReady;
    private final PackageManager packageManager;
    public static final String AGSA_AUTHORITY = "com.google.android.googlequicksearchbox.GsaPublicContentProvider";
    public static final String LENS_AVAILABILITY_PROVIDER_URI = String.format("content://%s/publicvalue/lens_oem_availability", AGSA_AUTHORITY);
    private static final LensSdkParamsProto.LensSdkParams DEFAULT_PARAMS = new LensSdkParamsProto.LensSdkParams();

    public interface LensSdkParamsCallback {
        void onLensSdkParamsAvailable(LensSdkParamsProto.LensSdkParams params);
    }

    static {
        DEFAULT_PARAMS.lensSdkVersion = LENS_SDK_VERSION;
        DEFAULT_PARAMS.agsaVersionName = "";
        DEFAULT_PARAMS.lensAvailabilityStatus = -1;
        DEFAULT_PARAMS.arStickersAvailabilityStatus = -1;
    }

    public LensSdkParamsReader(@NonNull Context context) {
        this(context, context.getPackageManager());
    }

    @VisibleForTesting
    LensSdkParamsReader(@NonNull Context context, @NonNull PackageManager packageManager) throws PackageManager.NameNotFoundException {
        this.callbacks = new ArrayList();
        this.context = context;
        this.packageManager = packageManager;
        updateParams();
    }

    public String getLensSdkVersion() {
        return this.lensSdkParams.lensSdkVersion;
    }

    public String getAgsaVersionName() {
        return this.lensSdkParams.agsaVersionName;
    }

    public int getArStickersAvailability() {
        return this.lensSdkParams.arStickersAvailabilityStatus;
    }

    public void getParams(@NonNull LensSdkParamsCallback callback) {
        if (this.lensSdkParamsReady) {
            callback.onLensSdkParamsAvailable(this.lensSdkParams);
        } else {
            this.callbacks.add(callback);
        }
    }

    private void updateParams() throws PackageManager.NameNotFoundException {
        this.lensSdkParamsReady = false;
        this.lensSdkParams = DEFAULT_PARAMS.mo6clone();
        try {
            PackageInfo packageInfo = this.packageManager.getPackageInfo("com.google.android.googlequicksearchbox", 0);
            if (packageInfo != null) {
                this.lensSdkParams.agsaVersionName = packageInfo.versionName;
            }
        } catch (PackageManager.NameNotFoundException unused) {
            Log.e(TAG, "Unable to find agsa package: com.google.android.googlequicksearchbox");
        }
        this.lensSdkParams.arStickersAvailabilityStatus = 1;
        if (Build.VERSION.SDK_INT >= 24) {
            Intent intent = new Intent();
            intent.setClassName(LENS_AR_STICKERS_PACKAGE, LENS_AR_STICKERS_ACTIVITY);
            if (this.packageManager.resolveActivity(intent, 0) != null) {
                this.lensSdkParams.arStickersAvailabilityStatus = 0;
            }
        }
        new QueryGsaTask().execute(new Void[0]);
    }

    private class QueryGsaTask extends AsyncTask<Void, Void, Integer> {
        private QueryGsaTask() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Integer doInBackground(Void... params) throws Throwable {
            Cursor cursor = null;
            try {
                Cursor cursorQuery = LensSdkParamsReader.this.context.getContentResolver().query(Uri.parse(LensSdkParamsReader.LENS_AVAILABILITY_PROVIDER_URI), null, null, null, null);
                if (cursorQuery != null) {
                    try {
                        if (cursorQuery.getCount() != 0) {
                            cursorQuery.moveToFirst();
                            int i = Integer.parseInt(cursorQuery.getString(0));
                            if (i > 6) {
                                i = 6;
                            }
                            Integer numValueOf = Integer.valueOf(i);
                            if (cursorQuery != null) {
                                cursorQuery.close();
                            }
                            return numValueOf;
                        }
                    } catch (Throwable th) {
                        cursor = cursorQuery;
                        th = th;
                        if (cursor != null) {
                            cursor.close();
                        }
                        throw th;
                    }
                }
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
                return 4;
            } catch (Throwable th2) {
                th = th2;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Integer status) {
            String strValueOf = String.valueOf(status);
            StringBuilder sb = new StringBuilder(25 + String.valueOf(strValueOf).length());
            sb.append("Lens availability result:");
            sb.append(strValueOf);
            Log.i(LensSdkParamsReader.TAG, sb.toString());
            LensSdkParamsReader.this.lensSdkParams.lensAvailabilityStatus = status.intValue();
            LensSdkParamsReader.this.lensSdkParamsReady = true;
            Iterator it = LensSdkParamsReader.this.callbacks.iterator();
            while (it.hasNext()) {
                ((LensSdkParamsCallback) it.next()).onLensSdkParamsAvailable(LensSdkParamsReader.this.lensSdkParams);
            }
            LensSdkParamsReader.this.callbacks.clear();
        }
    }
}
