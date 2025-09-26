package com.google.lens.sdk;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import com.google.android.apps.lens.library.base.LensSdkParamsReader;
import com.google.android.apps.lens.library.base.LensServiceBridge;
import com.google.android.apps.lens.library.base.proto.nano.LensSdkParamsProto;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class LensApi {

    @VisibleForTesting
    static final String LENS_BITMAP_URI_KEY = "LensBitmapUriKey";

    @VisibleForTesting
    static final String LENS_DEEPLINKING_STRING = "googleapp://lens";

    @VisibleForTesting
    static final Uri LENS_DEEPLINKING_URI = Uri.parse(LENS_DEEPLINKING_STRING);
    private static final String TAG = "LensApi";
    private final Context context;
    private final LensSdkParamsReader paramsReader;
    private final LensServiceBridge serviceBridge;

    public interface LensAvailabilityCallback {
        void onAvailabilityStatusFetched(@LensAvailabilityStatus int status);
    }

    public @interface LensAvailabilityStatus {
        public static final int LENS_READY = 0;
        public static final int LENS_UNAVAILABLE = 1;
        public static final int LENS_UNAVAILABLE_DEVICE_INCOMPATIBLE = 3;
        public static final int LENS_UNAVAILABLE_DEVICE_LOCKED = 5;
        public static final int LENS_UNAVAILABLE_LOCALE_NOT_SUPPORTED = 2;
        public static final int LENS_UNAVAILABLE_UNKNOWN_ERROR_CODE = 6;
    }

    public @interface LensFeature {
        public static final int LENS_AR_STICKERS = 1;
        public static final int LENS_CORE = 0;
    }

    private static final class LensSdkParamsCallback implements LensSdkParamsReader.LensSdkParamsCallback {
        private final LensAvailabilityCallback lensAvailabilityCallback;

        LensSdkParamsCallback(@NonNull LensAvailabilityCallback callback) {
            this.lensAvailabilityCallback = callback;
        }

        @Override // com.google.android.apps.lens.library.base.LensSdkParamsReader.LensSdkParamsCallback
        public void onLensSdkParamsAvailable(LensSdkParamsProto.LensSdkParams params) {
            this.lensAvailabilityCallback.onAvailabilityStatusFetched(LensApi.mapInternalLensAvailabilityToExternal(params.lensAvailabilityStatus));
        }
    }

    public LensApi(Context context) {
        this.context = context;
        this.paramsReader = new LensSdkParamsReader(context);
        this.serviceBridge = new LensServiceBridge(context);
    }

    public void onResume() {
        this.serviceBridge.bindService();
    }

    public void onPause() {
        this.serviceBridge.unbindService();
    }

    public void launchLensActivity(final Activity activity) {
        KeyguardManager keyguardManager = (KeyguardManager) activity.getSystemService("keyguard");
        if (keyguardManager.isKeyguardLocked()) {
            if (Build.VERSION.SDK_INT >= 26) {
                keyguardManager.requestDismissKeyguard(activity, new KeyguardManager.KeyguardDismissCallback() { // from class: com.google.lens.sdk.LensApi.1
                    @Override // android.app.KeyguardManager.KeyguardDismissCallback
                    public void onDismissSucceeded() {
                        Log.d(LensApi.TAG, "Keyguard successfully dismissed");
                        LensApi.this.startLensActivity(activity);
                    }

                    @Override // android.app.KeyguardManager.KeyguardDismissCallback
                    public void onDismissError() {
                        Log.e(LensApi.TAG, "Error dismissing keyguard");
                    }

                    @Override // android.app.KeyguardManager.KeyguardDismissCallback
                    public void onDismissCancelled() {
                        Log.d(LensApi.TAG, "Keyguard dismiss cancelled");
                    }
                });
                return;
            }
            int i = Build.VERSION.SDK_INT;
            StringBuilder sb = new StringBuilder(64);
            sb.append("Cannot start Lens when device is locked with Android ");
            sb.append(i);
            Log.e(TAG, sb.toString());
            return;
        }
        startLensActivity(activity);
    }

    public void launchLensActivity(Activity activity, @LensFeature int feature) {
        switch (feature) {
            case 0:
                launchLensActivity(activity);
                break;
            case 1:
                if (this.paramsReader.getArStickersAvailability() == 0) {
                    Intent intent = new Intent();
                    intent.setClassName(LensSdkParamsReader.LENS_AR_STICKERS_PACKAGE, LensSdkParamsReader.LENS_AR_STICKERS_ACTIVITY);
                    activity.startActivity(intent);
                    break;
                }
                break;
            default:
                StringBuilder sb = new StringBuilder(34);
                sb.append("Invalid lens activity: ");
                sb.append(feature);
                Log.w(TAG, sb.toString());
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startLensActivity(Activity activity) {
        if (this.serviceBridge.prewarmLensActivity()) {
            Log.i(TAG, "Lens is pre-warmed.");
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(Uri.parse(LENS_DEEPLINKING_STRING));
        activity.startActivityForResult(intent, 0);
    }

    public void launchLensActivityWithBitmapUri(Activity activity, Uri bitmapUri) {
        if (bitmapUri == null) {
            Log.e(TAG, "Image URI is null!");
            return;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(appendBitmapUri(LENS_DEEPLINKING_URI, bitmapUri));
        activity.startActivityForResult(intent, 0);
    }

    public void checkLensAvailability(LensAvailabilityCallback callback) {
        if (((KeyguardManager) this.context.getSystemService("keyguard")).isKeyguardLocked() && Build.VERSION.SDK_INT < 26) {
            callback.onAvailabilityStatusFetched(5);
        } else {
            this.paramsReader.getParams(new LensSdkParamsCallback(callback));
        }
    }

    public void checkArStickersAvailability(LensAvailabilityCallback callback) {
        callback.onAvailabilityStatusFetched(mapInternalLensAvailabilityToExternal(this.paramsReader.getArStickersAvailability()));
    }

    private Uri appendBitmapUri(Uri baseUri, Uri bitmapUri) {
        return (baseUri == null || bitmapUri == null) ? baseUri : baseUri.buildUpon().appendQueryParameter(LENS_BITMAP_URI_KEY, bitmapUri.toString()).build();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @LensAvailabilityStatus
    public static int mapInternalLensAvailabilityToExternal(int internalLensAvailabilityStatus) {
        switch (internalLensAvailabilityStatus) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 5:
            case 6:
                return internalLensAvailabilityStatus;
            case 4:
            default:
                StringBuilder sb = new StringBuilder(32);
                sb.append("Internal error code: ");
                sb.append(internalLensAvailabilityStatus);
                Log.d(TAG, sb.toString());
                return 1;
        }
    }
}
