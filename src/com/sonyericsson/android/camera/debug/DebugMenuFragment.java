package com.sonyericsson.android.camera.debug;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.setting.SharedPreferencesAccessor;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.cameracommon.constants.CommonConstants;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: DebugMenuFragment.kt */
@Metadata(bv = {1, 0, 2}, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u00012\u00020\u0002B\u0005¢\u0006\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H\u0002J\b\u0010\u0006\u001a\u00020\u0005H\u0002J\b\u0010\u0007\u001a\u00020\u0005H\u0002J\b\u0010\b\u001a\u00020\u0005H\u0002J\b\u0010\t\u001a\u00020\u0005H\u0002J\b\u0010\n\u001a\u00020\u0005H\u0002J\b\u0010\u000b\u001a\u00020\u0005H\u0002J\u0012\u0010\f\u001a\u00020\u00052\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0016J\u0018\u0010\u000f\u001a\u00020\u00052\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\u001c\u0010\u0014\u001a\u00020\u00052\b\u0010\r\u001a\u0004\u0018\u00010\u000e2\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0016J\u0010\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aH\u0016J\b\u0010\u001b\u001a\u00020\u0005H\u0016J\b\u0010\u001c\u001a\u00020\u0005H\u0016J\u0018\u0010\u001d\u001a\u00020\u00052\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u0016H\u0016J\u0010\u0010!\u001a\u00020\u00052\u0006\u0010\"\u001a\u00020#H\u0002J\b\u0010$\u001a\u00020\u0005H\u0002¨\u0006%"}, d2 = {"Lcom/sonyericsson/android/camera/debug/DebugMenuFragment;", "Landroid/support/v7/preference/PreferenceFragmentCompat;", "Landroid/content/SharedPreferences$OnSharedPreferenceChangeListener;", "()V", "clearFingerPrint", "", "initialize", "initializeAutoPowerOff", "initializeEmulateSideTouch", "initializeForceSettingMigration", "initializeLowPowerMode", "initializePredictiveCaptureOn", "onActivityCreated", "savedInstanceState", "Landroid/os/Bundle;", "onCreateOptionsMenu", "menu", "Landroid/view/Menu;", "inflater", "Landroid/view/MenuInflater;", "onCreatePreferences", "rootKey", "", "onOptionsItemSelected", "", "item", "Landroid/view/MenuItem;", "onPause", "onResume", "onSharedPreferenceChanged", "sharedPreferences", "Landroid/content/SharedPreferences;", "key", "reset", "context", "Landroid/content/Context;", "showKillProcessToast", "SemcCameraUI_release"}, k = 1, mv = {1, 1, 11})
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class DebugMenuFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override // android.support.v7.preference.PreferenceFragmentCompat
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        getPreferenceManager().setSharedPreferencesName(DebugParameterUtils.SHARED_PREFERENCES_CAMERA_DEBUG);
        setPreferencesFromResource(R.xml.debug_preferences, rootKey);
        initialize();
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override // android.support.v4.app.Fragment
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        Intrinsics.checkParameterIsNotNull(menu, "menu");
        Intrinsics.checkParameterIsNotNull(inflater, "inflater");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.debug_fragment_actionbar, menu);
    }

    @Override // android.support.v4.app.Fragment
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        Intrinsics.checkParameterIsNotNull(item, "item");
        Context context = getContext();
        if (!(context instanceof Context) || item.getItemId() != 2131296379) {
            return true;
        }
        reset(context);
        return true;
    }

    @Override // android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        Intrinsics.checkExpressionValueIsNotNull(preferenceScreen, "preferenceScreen");
        SharedPreferences sharedPreferences = preferenceScreen.getSharedPreferences();
        if (sharedPreferences != null) {
            sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        Intrinsics.checkExpressionValueIsNotNull(preferenceScreen, "preferenceScreen");
        SharedPreferences sharedPreferences = preferenceScreen.getSharedPreferences();
        if (sharedPreferences != null) {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        }
    }

    @Override // android.content.SharedPreferences.OnSharedPreferenceChangeListener
    public void onSharedPreferenceChanged(@NotNull SharedPreferences sharedPreferences, @NotNull String key) {
        Intrinsics.checkParameterIsNotNull(sharedPreferences, "sharedPreferences");
        Intrinsics.checkParameterIsNotNull(key, "key");
        int iHashCode = key.hashCode();
        if (iHashCode == -51439766) {
            if (key.equals(DebugParameterUtils.KEY_DEBUG_ALWAYS_PREDICTIVE_CAPTURE)) {
                Preference preferenceFindPreference = findPreference(key);
                if (preferenceFindPreference == null) {
                    throw new TypeCastException("null cannot be cast to non-null type android.support.v14.preference.SwitchPreference");
                }
                ((SwitchPreference) preferenceFindPreference).setChecked(sharedPreferences.getBoolean(key, false));
                return;
            }
            return;
        }
        if (iHashCode == 1006054472) {
            if (key.equals(DebugParameterUtils.KEY_DEBUG_DISABLE_AUTO_POWER_OFF)) {
                Preference preferenceFindPreference2 = findPreference(key);
                if (preferenceFindPreference2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type android.support.v14.preference.SwitchPreference");
                }
                ((SwitchPreference) preferenceFindPreference2).setChecked(sharedPreferences.getBoolean(key, false));
                return;
            }
            return;
        }
        if (iHashCode == 1901671088) {
            if (key.equals(DebugParameterUtils.KEY_DEBUG_EMULATE_SIDETOUCH)) {
                Preference preferenceFindPreference3 = findPreference(key);
                if (preferenceFindPreference3 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type android.support.v14.preference.SwitchPreference");
                }
                ((SwitchPreference) preferenceFindPreference3).setChecked(sharedPreferences.getBoolean(key, false));
                return;
            }
            return;
        }
        if (iHashCode == 2012347019 && key.equals(DebugParameterUtils.KEY_DEBUG_DISABLE_LOW_POWER_MODE)) {
            Preference preferenceFindPreference4 = findPreference(key);
            if (preferenceFindPreference4 == null) {
                throw new TypeCastException("null cannot be cast to non-null type android.support.v14.preference.SwitchPreference");
            }
            ((SwitchPreference) preferenceFindPreference4).setChecked(sharedPreferences.getBoolean(key, false));
            showKillProcessToast();
        }
    }

    private final void initialize() {
        initializeAutoPowerOff();
        initializeLowPowerMode();
        initializeForceSettingMigration();
        initializePredictiveCaptureOn();
        initializeEmulateSideTouch();
    }

    private final void initializeAutoPowerOff() {
        Preference preferenceFindPreference = findPreference(DebugParameterUtils.KEY_DEBUG_DISABLE_AUTO_POWER_OFF);
        if (preferenceFindPreference == null) {
            throw new TypeCastException("null cannot be cast to non-null type android.support.v14.preference.SwitchPreference");
        }
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        Intrinsics.checkExpressionValueIsNotNull(preferenceScreen, "preferenceScreen");
        ((SwitchPreference) preferenceFindPreference).setChecked(preferenceScreen.getSharedPreferences().getBoolean(DebugParameterUtils.KEY_DEBUG_DISABLE_AUTO_POWER_OFF, false));
    }

    private final void initializeLowPowerMode() {
        Preference preferenceFindPreference = findPreference(DebugParameterUtils.KEY_DEBUG_DISABLE_LOW_POWER_MODE);
        if (preferenceFindPreference == null) {
            throw new TypeCastException("null cannot be cast to non-null type android.support.v14.preference.SwitchPreference");
        }
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        Intrinsics.checkExpressionValueIsNotNull(preferenceScreen, "preferenceScreen");
        ((SwitchPreference) preferenceFindPreference).setChecked(preferenceScreen.getSharedPreferences().getBoolean(DebugParameterUtils.KEY_DEBUG_DISABLE_LOW_POWER_MODE, false));
    }

    private final void initializeForceSettingMigration() {
        Preference settingMigration = findPreference(DebugParameterUtils.KEY_DEBUG_FORCE_MIGRATE_SETTINGS);
        Intrinsics.checkExpressionValueIsNotNull(settingMigration, "settingMigration");
        settingMigration.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.sonyericsson.android.camera.debug.DebugMenuFragment.initializeForceSettingMigration.1
            @Override // android.support.v7.preference.Preference.OnPreferenceClickListener
            public final boolean onPreferenceClick(Preference preference) {
                DebugMenuFragment.this.clearFingerPrint();
                return true;
            }
        });
    }

    private final void initializePredictiveCaptureOn() {
        Preference preferenceFindPreference = findPreference(DebugParameterUtils.KEY_DEBUG_ALWAYS_PREDICTIVE_CAPTURE);
        if (preferenceFindPreference == null) {
            throw new TypeCastException("null cannot be cast to non-null type android.support.v14.preference.SwitchPreference");
        }
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        Intrinsics.checkExpressionValueIsNotNull(preferenceScreen, "preferenceScreen");
        ((SwitchPreference) preferenceFindPreference).setChecked(preferenceScreen.getSharedPreferences().getBoolean(DebugParameterUtils.KEY_DEBUG_ALWAYS_PREDICTIVE_CAPTURE, false));
    }

    private final void initializeEmulateSideTouch() {
        Preference preferenceFindPreference = findPreference(DebugParameterUtils.KEY_DEBUG_EMULATE_SIDETOUCH);
        if (preferenceFindPreference == null) {
            throw new TypeCastException("null cannot be cast to non-null type android.support.v14.preference.SwitchPreference");
        }
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        Intrinsics.checkExpressionValueIsNotNull(preferenceScreen, "preferenceScreen");
        ((SwitchPreference) preferenceFindPreference).setChecked(preferenceScreen.getSharedPreferences().getBoolean(DebugParameterUtils.KEY_DEBUG_EMULATE_SIDETOUCH, false));
    }

    private final void reset(Context context) {
        DebugParameterUtils.INSTANCE.reset(context);
        initialize();
    }

    private final void showKillProcessToast() {
        Toast.makeText(getContext(), R.string.debug_preference_toast_kill_process, 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void clearFingerPrint() {
        for (CameraInfo.CameraId cameraId : CameraInfo.CameraId.values()) {
            SharedPreferences sharedPreferences = SharedPreferencesAccessor.getSharedPreferences(getContext(), PlatformCapability.getFileNameForCameraCapability(cameraId), 0);
            if (sharedPreferences == null) {
                return;
            }
            sharedPreferences.edit().remove(CommonConstants.FINGERPRINT).commit();
        }
        Toast.makeText(getContext(), R.string.debug_preference_toast_execute_settings_migration, 0).show();
    }
}
