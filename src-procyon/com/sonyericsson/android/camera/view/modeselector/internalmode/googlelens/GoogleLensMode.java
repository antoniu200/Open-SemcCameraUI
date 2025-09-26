// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.modeselector.internalmode.googlelens;

import com.sonyericsson.android.camera.util.CamLog;
import com.google.lens.sdk.LensApi;
import android.os.Handler;
import android.os.Looper;
import com.sonyericsson.android.camera.view.modeselector.Mode;
import com.sonyericsson.android.camera.view.modeselector.CapturingModeAttributes;
import android.support.annotation.NonNull;
import android.content.Context;
import com.sonyericsson.android.camera.setting.SharedPreferencesAccessor;
import com.sonyericsson.android.camera.view.modeselector.AddonMode;

public class GoogleLensMode extends AddonMode
{
    public static final String MODE_NAME = "GOOGLE_LENS";
    private boolean[] mAvailable;
    private SharedPreferencesAccessor mGoogleLensPrefsAccessor;
    
    public GoogleLensMode(@NonNull final Context context, @NonNull final CapturingModeAttributes capturingModeAttributes) {
        super(context, capturingModeAttributes);
        this.mAvailable = null;
    }
    
    private boolean getCachedGoogleLensAvailability() {
        if (this.mGoogleLensPrefsAccessor == null) {
            this.mGoogleLensPrefsAccessor = new SharedPreferencesAccessor(this.mContext, "google-lens");
        }
        return this.mGoogleLensPrefsAccessor.readBoolean("GOOGLE_LENS_AVAILABLE", false);
    }
    
    public static boolean isLensMode(@NonNull final Context context, @NonNull final CapturingModeAttributes capturingModeAttributes) {
        return AddonMode.generateId(context.getPackageName(), "GOOGLE_LENS").equals(AddonMode.generateId(capturingModeAttributes));
    }
    
    private void updateCachedGoogleLensAvailability(final boolean b) {
        if (this.mGoogleLensPrefsAccessor == null) {
            this.mGoogleLensPrefsAccessor = new SharedPreferencesAccessor(this.mContext, "google-lens");
        }
        this.mGoogleLensPrefsAccessor.writeBoolean("GOOGLE_LENS_AVAILABLE", b, false);
        this.mGoogleLensPrefsAccessor.apply();
    }
    
    @Override
    protected String generateSmallIconMappingName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(super.generateSmallIconMappingName());
        sb.append(".");
        sb.append("GOOGLE_LENS");
        return sb.toString();
    }
    
    @Override
    protected String getModeName() {
        return this.getTag().getSelectorLabel();
    }
    
    @Override
    public boolean isAvailable() {
        return this.isAvailable(false);
    }
    
    public boolean isAvailable(final boolean b) {
        if (!b && this.mAvailable != null) {
            return this.mAvailable[0];
        }
        new Handler(Looper.getMainLooper()).post((Runnable)new Runnable(this) {
            final GoogleLensMode this$0;
            
            @Override
            public void run() {
                new LensApi(this.this$0.mContext).checkLensAvailability((LensApi.LensAvailabilityCallback)new LensApi.LensAvailabilityCallback(this) {
                    final GoogleLensMode$1 this$1;
                    
                    @Override
                    public void onAvailabilityStatusFetched(@LensAvailabilityStatus final int n) {
                        final boolean access$100 = this.this$1.this$0.getCachedGoogleLensAvailability();
                        int n2;
                        if (n == 0) {
                            n2 = 1;
                        }
                        else {
                            if (CamLog.VERBOSE) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("lens_availability: ");
                                sb.append(false);
                                CamLog.d(sb.toString());
                            }
                            n2 = 0;
                        }
                        this.this$1.this$0.mAvailable = new boolean[] { n2 != 0 };
                        if ((access$100 ? 1 : 0) != n2) {
                            this.this$1.this$0.updateCachedGoogleLensAvailability((boolean)(n2 != 0));
                            if (this.this$1.this$0.mStateChangeListener != null) {
                                this.this$1.this$0.mStateChangeListener.onAvailabilityChanged(this.this$1.this$0, (boolean)(n2 != 0));
                            }
                        }
                    }
                });
            }
        });
        return this.getCachedGoogleLensAvailability();
    }
}
