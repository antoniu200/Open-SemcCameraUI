package com.sonyericsson.android.camera.view.modeselector.internalmode.googlelens;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.google.lens.sdk.LensApi;
import com.sonyericsson.android.camera.configuration.SharedPreferencesConstants;
import com.sonyericsson.android.camera.setting.SharedPreferencesAccessor;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.view.modeselector.AddonMode;
import com.sonyericsson.android.camera.view.modeselector.CapturingModeAttributes;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class GoogleLensMode extends AddonMode {
    public static final String MODE_NAME = "GOOGLE_LENS";
    private boolean[] mAvailable;
    private SharedPreferencesAccessor mGoogleLensPrefsAccessor;

    public GoogleLensMode(@NonNull Context context, @NonNull CapturingModeAttributes capturingModeAttributes) {
        super(context, capturingModeAttributes);
        this.mAvailable = null;
    }

    @Override // com.sonyericsson.android.camera.view.modeselector.Mode
    public boolean isAvailable() {
        return isAvailable(false);
    }

    @Override // com.sonyericsson.android.camera.view.modeselector.AddonMode, com.sonyericsson.android.camera.view.modeselector.Mode
    protected String generateSmallIconMappingName() {
        return super.generateSmallIconMappingName() + "." + MODE_NAME;
    }

    @Override // com.sonyericsson.android.camera.view.modeselector.AddonMode, com.sonyericsson.android.camera.view.modeselector.Mode
    protected String getModeName() {
        return getTag().getSelectorLabel();
    }

    public boolean isAvailable(boolean z) {
        if (!z && this.mAvailable != null) {
            return this.mAvailable[0];
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.sonyericsson.android.camera.view.modeselector.internalmode.googlelens.GoogleLensMode.1
            @Override // java.lang.Runnable
            public void run() {
                new LensApi(GoogleLensMode.this.mContext).checkLensAvailability(new LensApi.LensAvailabilityCallback() { // from class: com.sonyericsson.android.camera.view.modeselector.internalmode.googlelens.GoogleLensMode.1.1
                    @Override // com.google.lens.sdk.LensApi.LensAvailabilityCallback
                    public void onAvailabilityStatusFetched(@LensApi.LensAvailabilityStatus int i) {
                        boolean z2;
                        boolean cachedGoogleLensAvailability = GoogleLensMode.this.getCachedGoogleLensAvailability();
                        if (i == 0) {
                            z2 = true;
                        } else {
                            if (CamLog.VERBOSE) {
                                CamLog.d("lens_availability: false");
                            }
                            z2 = false;
                        }
                        GoogleLensMode.this.mAvailable = new boolean[]{z2};
                        if (cachedGoogleLensAvailability != z2) {
                            GoogleLensMode.this.updateCachedGoogleLensAvailability(z2);
                            if (GoogleLensMode.this.mStateChangeListener != null) {
                                GoogleLensMode.this.mStateChangeListener.onAvailabilityChanged(GoogleLensMode.this, z2);
                            }
                        }
                    }
                });
            }
        });
        return getCachedGoogleLensAvailability();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCachedGoogleLensAvailability(boolean z) {
        if (this.mGoogleLensPrefsAccessor == null) {
            this.mGoogleLensPrefsAccessor = new SharedPreferencesAccessor(this.mContext, SharedPreferencesConstants.GOOGLE_LENS_SHARED_PREFS_NAME);
        }
        this.mGoogleLensPrefsAccessor.writeBoolean(SharedPreferencesConstants.KEY_GOOGLE_LENS_AVAILABLE, z, false);
        this.mGoogleLensPrefsAccessor.apply();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getCachedGoogleLensAvailability() {
        if (this.mGoogleLensPrefsAccessor == null) {
            this.mGoogleLensPrefsAccessor = new SharedPreferencesAccessor(this.mContext, SharedPreferencesConstants.GOOGLE_LENS_SHARED_PREFS_NAME);
        }
        return this.mGoogleLensPrefsAccessor.readBoolean(SharedPreferencesConstants.KEY_GOOGLE_LENS_AVAILABLE, false);
    }

    public static boolean isLensMode(@NonNull Context context, @NonNull CapturingModeAttributes capturingModeAttributes) {
        return generateId(context.getPackageName(), MODE_NAME).equals(generateId(capturingModeAttributes));
    }
}
