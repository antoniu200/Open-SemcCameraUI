// 
// Decompiled by Procyon v0.6.0
// 

package com.google.lens.sdk;

import com.google.android.apps.lens.library.base.proto.nano.LensSdkParamsProto;
import android.support.annotation.NonNull;
import android.app.KeyguardManager$KeyguardDismissCallback;
import android.os.Build$VERSION;
import android.app.KeyguardManager;
import android.content.Intent;
import android.util.Log;
import android.app.Activity;
import com.google.android.apps.lens.library.base.LensServiceBridge;
import com.google.android.apps.lens.library.base.LensSdkParamsReader;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.VisibleForTesting;

public class LensApi
{
    @VisibleForTesting
    static final String LENS_BITMAP_URI_KEY = "LensBitmapUriKey";
    @VisibleForTesting
    static final String LENS_DEEPLINKING_STRING = "googleapp://lens";
    @VisibleForTesting
    static final Uri LENS_DEEPLINKING_URI;
    private static final String TAG = "LensApi";
    private final Context context;
    private final LensSdkParamsReader paramsReader;
    private final LensServiceBridge serviceBridge;
    
    static {
        LENS_DEEPLINKING_URI = Uri.parse("googleapp://lens");
    }
    
    public LensApi(final Context context) {
        this.context = context;
        this.paramsReader = new LensSdkParamsReader(context);
        this.serviceBridge = new LensServiceBridge(context);
    }
    
    private Uri appendBitmapUri(final Uri uri, final Uri uri2) {
        if (uri != null && uri2 != null) {
            return uri.buildUpon().appendQueryParameter("LensBitmapUriKey", uri2.toString()).build();
        }
        return uri;
    }
    
    @LensAvailabilityStatus
    private static int mapInternalLensAvailabilityToExternal(final int i) {
        switch (i) {
            default: {
                final StringBuilder sb = new StringBuilder(32);
                sb.append("Internal error code: ");
                sb.append(i);
                Log.d("LensApi", sb.toString());
                return 1;
            }
            case 0:
            case 1:
            case 2:
            case 3:
            case 5:
            case 6: {
                return i;
            }
        }
    }
    
    private void startLensActivity(final Activity activity) {
        if (this.serviceBridge.prewarmLensActivity()) {
            Log.i("LensApi", "Lens is pre-warmed.");
        }
        final Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(Uri.parse("googleapp://lens"));
        activity.startActivityForResult(intent, 0);
    }
    
    public void checkArStickersAvailability(final LensAvailabilityCallback lensAvailabilityCallback) {
        lensAvailabilityCallback.onAvailabilityStatusFetched(mapInternalLensAvailabilityToExternal(this.paramsReader.getArStickersAvailability()));
    }
    
    public void checkLensAvailability(final LensAvailabilityCallback lensAvailabilityCallback) {
        if (((KeyguardManager)this.context.getSystemService("keyguard")).isKeyguardLocked() && Build$VERSION.SDK_INT < 26) {
            lensAvailabilityCallback.onAvailabilityStatusFetched(5);
            return;
        }
        this.paramsReader.getParams((LensSdkParamsReader.LensSdkParamsCallback)new LensSdkParamsCallback(lensAvailabilityCallback));
    }
    
    public void launchLensActivity(final Activity activity) {
        final KeyguardManager keyguardManager = (KeyguardManager)activity.getSystemService("keyguard");
        if (!keyguardManager.isKeyguardLocked()) {
            this.startLensActivity(activity);
            return;
        }
        if (Build$VERSION.SDK_INT >= 26) {
            keyguardManager.requestDismissKeyguard(activity, (KeyguardManager$KeyguardDismissCallback)new KeyguardManager$KeyguardDismissCallback(this, activity) {
                final LensApi this$0;
                final Activity val$activity;
                
                public void onDismissCancelled() {
                    Log.d("LensApi", "Keyguard dismiss cancelled");
                }
                
                public void onDismissError() {
                    Log.e("LensApi", "Error dismissing keyguard");
                }
                
                public void onDismissSucceeded() {
                    Log.d("LensApi", "Keyguard successfully dismissed");
                    this.this$0.startLensActivity(this.val$activity);
                }
            });
            return;
        }
        final int sdk_INT = Build$VERSION.SDK_INT;
        final StringBuilder sb = new StringBuilder(64);
        sb.append("Cannot start Lens when device is locked with Android ");
        sb.append(sdk_INT);
        Log.e("LensApi", sb.toString());
    }
    
    public void launchLensActivity(final Activity activity, @LensFeature final int i) {
        switch (i) {
            default: {
                final StringBuilder sb = new StringBuilder(34);
                sb.append("Invalid lens activity: ");
                sb.append(i);
                Log.w("LensApi", sb.toString());
                break;
            }
            case 1: {
                if (this.paramsReader.getArStickersAvailability() == 0) {
                    final Intent intent = new Intent();
                    intent.setClassName("com.google.ar.lens", "com.google.vr.apps.ornament.app.MainActivity");
                    activity.startActivity(intent);
                    break;
                }
                break;
            }
            case 0: {
                this.launchLensActivity(activity);
                break;
            }
        }
    }
    
    public void launchLensActivityWithBitmapUri(final Activity activity, final Uri uri) {
        if (uri == null) {
            Log.e("LensApi", "Image URI is null!");
            return;
        }
        final Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(this.appendBitmapUri(LensApi.LENS_DEEPLINKING_URI, uri));
        activity.startActivityForResult(intent, 0);
    }
    
    public void onPause() {
        this.serviceBridge.unbindService();
    }
    
    public void onResume() {
        this.serviceBridge.bindService();
    }
    
    public interface LensAvailabilityCallback
    {
        void onAvailabilityStatusFetched(@LensAvailabilityStatus final int p0);
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
    
    private static final class LensSdkParamsCallback implements LensSdkParamsReader.LensSdkParamsCallback
    {
        private final LensAvailabilityCallback lensAvailabilityCallback;
        
        LensSdkParamsCallback(@NonNull final LensAvailabilityCallback lensAvailabilityCallback) {
            this.lensAvailabilityCallback = lensAvailabilityCallback;
        }
        
        @Override
        public void onLensSdkParamsAvailable(final LensSdkParamsProto.LensSdkParams lensSdkParams) {
            this.lensAvailabilityCallback.onAvailabilityStatusFetched(mapInternalLensAvailabilityToExternal(lensSdkParams.lensAvailabilityStatus));
        }
    }
}
