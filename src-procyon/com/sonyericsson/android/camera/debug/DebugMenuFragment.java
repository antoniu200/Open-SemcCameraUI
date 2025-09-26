// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.debug;

import android.view.MenuItem;
import android.view.MenuInflater;
import org.jetbrains.annotations.NotNull;
import android.view.Menu;
import org.jetbrains.annotations.Nullable;
import android.os.Bundle;
import android.content.Context;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference;
import kotlin.jvm.internal.Intrinsics;
import android.support.v14.preference.SwitchPreference;
import kotlin.TypeCastException;
import android.content.SharedPreferences;
import android.widget.Toast;
import com.sonyericsson.android.camera.setting.SharedPreferencesAccessor;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.device.CameraInfo;
import kotlin.Metadata;
import android.content.SharedPreferences$OnSharedPreferenceChangeListener;
import android.support.v7.preference.PreferenceFragmentCompat;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u00012\u00020\u0002B\u0005¢\u0006\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H\u0002J\b\u0010\u0006\u001a\u00020\u0005H\u0002J\b\u0010\u0007\u001a\u00020\u0005H\u0002J\b\u0010\b\u001a\u00020\u0005H\u0002J\b\u0010\t\u001a\u00020\u0005H\u0002J\b\u0010\n\u001a\u00020\u0005H\u0002J\b\u0010\u000b\u001a\u00020\u0005H\u0002J\u0012\u0010\f\u001a\u00020\u00052\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0016J\u0018\u0010\u000f\u001a\u00020\u00052\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\u001c\u0010\u0014\u001a\u00020\u00052\b\u0010\r\u001a\u0004\u0018\u00010\u000e2\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0016J\u0010\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aH\u0016J\b\u0010\u001b\u001a\u00020\u0005H\u0016J\b\u0010\u001c\u001a\u00020\u0005H\u0016J\u0018\u0010\u001d\u001a\u00020\u00052\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u0016H\u0016J\u0010\u0010!\u001a\u00020\u00052\u0006\u0010\"\u001a\u00020#H\u0002J\b\u0010$\u001a\u00020\u0005H\u0002¨\u0006%" }, d2 = { "Lcom/sonyericsson/android/camera/debug/DebugMenuFragment;", "Landroid/support/v7/preference/PreferenceFragmentCompat;", "Landroid/content/SharedPreferences$OnSharedPreferenceChangeListener;", "()V", "clearFingerPrint", "", "initialize", "initializeAutoPowerOff", "initializeEmulateSideTouch", "initializeForceSettingMigration", "initializeLowPowerMode", "initializePredictiveCaptureOn", "onActivityCreated", "savedInstanceState", "Landroid/os/Bundle;", "onCreateOptionsMenu", "menu", "Landroid/view/Menu;", "inflater", "Landroid/view/MenuInflater;", "onCreatePreferences", "rootKey", "", "onOptionsItemSelected", "", "item", "Landroid/view/MenuItem;", "onPause", "onResume", "onSharedPreferenceChanged", "sharedPreferences", "Landroid/content/SharedPreferences;", "key", "reset", "context", "Landroid/content/Context;", "showKillProcessToast", "SemcCameraUI_release" }, k = 1, mv = { 1, 1, 11 })
public final class DebugMenuFragment extends PreferenceFragmentCompat implements SharedPreferences$OnSharedPreferenceChangeListener
{
    private final void clearFingerPrint() {
        final CameraInfo.CameraId[] values = CameraInfo.CameraId.values();
        for (int length = values.length, i = 0; i < length; ++i) {
            final SharedPreferences sharedPreferences = SharedPreferencesAccessor.getSharedPreferences(this.getContext(), PlatformCapability.getFileNameForCameraCapability(values[i]), 0);
            if (sharedPreferences == null) {
                return;
            }
            sharedPreferences.edit().remove("android.os.Build.FINGERPRINT").commit();
        }
        Toast.makeText(this.getContext(), 2131690313, 0).show();
    }
    
    private final void initialize() {
        this.initializeAutoPowerOff();
        this.initializeLowPowerMode();
        this.initializeForceSettingMigration();
        this.initializePredictiveCaptureOn();
        this.initializeEmulateSideTouch();
    }
    
    private final void initializeAutoPowerOff() {
        final Preference preference = this.findPreference("KEY_DEBUG_DISABLE_AUTO_POWER_OFF");
        if (preference == null) {
            throw new TypeCastException("null cannot be cast to non-null type android.support.v14.preference.SwitchPreference");
        }
        final SwitchPreference switchPreference = (SwitchPreference)preference;
        final PreferenceScreen preferenceScreen = this.getPreferenceScreen();
        Intrinsics.checkExpressionValueIsNotNull(preferenceScreen, "preferenceScreen");
        switchPreference.setChecked(preferenceScreen.getSharedPreferences().getBoolean("KEY_DEBUG_DISABLE_AUTO_POWER_OFF", false));
    }
    
    private final void initializeEmulateSideTouch() {
        final Preference preference = this.findPreference("KEY_DEBUG_EMULATE_SIDETOUCH");
        if (preference == null) {
            throw new TypeCastException("null cannot be cast to non-null type android.support.v14.preference.SwitchPreference");
        }
        final SwitchPreference switchPreference = (SwitchPreference)preference;
        final PreferenceScreen preferenceScreen = this.getPreferenceScreen();
        Intrinsics.checkExpressionValueIsNotNull(preferenceScreen, "preferenceScreen");
        switchPreference.setChecked(preferenceScreen.getSharedPreferences().getBoolean("KEY_DEBUG_EMULATE_SIDETOUCH", false));
    }
    
    private final void initializeForceSettingMigration() {
        final Preference preference = this.findPreference("KEY_DEBUG_FORCE_MIGRATE_SETTINGS");
        Intrinsics.checkExpressionValueIsNotNull(preference, "settingMigration");
        preference.setOnPreferenceClickListener((OnPreferenceClickListener)new DebugMenuFragment$initializeForceSettingMigration.DebugMenuFragment$initializeForceSettingMigration$1(this));
    }
    
    private final void initializeLowPowerMode() {
        final Preference preference = this.findPreference("KEY_DEBUG_DISABLE_LOW_POWER_MODE");
        if (preference == null) {
            throw new TypeCastException("null cannot be cast to non-null type android.support.v14.preference.SwitchPreference");
        }
        final SwitchPreference switchPreference = (SwitchPreference)preference;
        final PreferenceScreen preferenceScreen = this.getPreferenceScreen();
        Intrinsics.checkExpressionValueIsNotNull(preferenceScreen, "preferenceScreen");
        switchPreference.setChecked(preferenceScreen.getSharedPreferences().getBoolean("KEY_DEBUG_DISABLE_LOW_POWER_MODE", false));
    }
    
    private final void initializePredictiveCaptureOn() {
        final Preference preference = this.findPreference("KEY_DEBUG_ALWAYS_PREDICTIVE_CAPTURE");
        if (preference == null) {
            throw new TypeCastException("null cannot be cast to non-null type android.support.v14.preference.SwitchPreference");
        }
        final SwitchPreference switchPreference = (SwitchPreference)preference;
        final PreferenceScreen preferenceScreen = this.getPreferenceScreen();
        Intrinsics.checkExpressionValueIsNotNull(preferenceScreen, "preferenceScreen");
        switchPreference.setChecked(preferenceScreen.getSharedPreferences().getBoolean("KEY_DEBUG_ALWAYS_PREDICTIVE_CAPTURE", false));
    }
    
    private final void reset(final Context context) {
        DebugParameterUtils.INSTANCE.reset(context);
        this.initialize();
    }
    
    private final void showKillProcessToast() {
        Toast.makeText(this.getContext(), 2131690314, 0).show();
    }
    
    public void onActivityCreated(@Nullable final Bundle bundle) {
        super.onActivityCreated(bundle);
        this.setHasOptionsMenu(true);
    }
    
    public void onCreateOptionsMenu(@NotNull final Menu menu, @NotNull final MenuInflater menuInflater) {
        Intrinsics.checkParameterIsNotNull(menu, "menu");
        Intrinsics.checkParameterIsNotNull(menuInflater, "inflater");
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(2131558400, menu);
    }
    
    @Override
    public void onCreatePreferences(@Nullable final Bundle bundle, @Nullable final String s) {
        this.getPreferenceManager().setSharedPreferencesName("com.sonyericsson.android.camera.shared_preferences_debug");
        this.setPreferencesFromResource(2131886082, s);
        this.initialize();
    }
    
    public boolean onOptionsItemSelected(@NotNull final MenuItem menuItem) {
        Intrinsics.checkParameterIsNotNull(menuItem, "item");
        final Context context = this.getContext();
        if (context instanceof Context) {
            if (menuItem.getItemId() == 2131296379) {
                this.reset(context);
            }
        }
        return true;
    }
    
    public void onPause() {
        super.onPause();
        final PreferenceScreen preferenceScreen = this.getPreferenceScreen();
        Intrinsics.checkExpressionValueIsNotNull(preferenceScreen, "preferenceScreen");
        final SharedPreferences sharedPreferences = preferenceScreen.getSharedPreferences();
        if (sharedPreferences != null) {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener((SharedPreferences$OnSharedPreferenceChangeListener)this);
        }
    }
    
    public void onResume() {
        super.onResume();
        final PreferenceScreen preferenceScreen = this.getPreferenceScreen();
        Intrinsics.checkExpressionValueIsNotNull(preferenceScreen, "preferenceScreen");
        final SharedPreferences sharedPreferences = preferenceScreen.getSharedPreferences();
        if (sharedPreferences != null) {
            sharedPreferences.registerOnSharedPreferenceChangeListener((SharedPreferences$OnSharedPreferenceChangeListener)this);
        }
    }
    
    public void onSharedPreferenceChanged(@NotNull final SharedPreferences sharedPreferences, @NotNull final String s) {
        Intrinsics.checkParameterIsNotNull(sharedPreferences, "sharedPreferences");
        Intrinsics.checkParameterIsNotNull(s, "key");
        final int hashCode = s.hashCode();
        if (hashCode != -51439766) {
            if (hashCode != 1006054472) {
                if (hashCode != 1901671088) {
                    if (hashCode == 2012347019) {
                        if (s.equals("KEY_DEBUG_DISABLE_LOW_POWER_MODE")) {
                            final Preference preference = this.findPreference(s);
                            if (preference == null) {
                                throw new TypeCastException("null cannot be cast to non-null type android.support.v14.preference.SwitchPreference");
                            }
                            ((SwitchPreference)preference).setChecked(sharedPreferences.getBoolean(s, false));
                            this.showKillProcessToast();
                        }
                    }
                }
                else if (s.equals("KEY_DEBUG_EMULATE_SIDETOUCH")) {
                    final Preference preference2 = this.findPreference(s);
                    if (preference2 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type android.support.v14.preference.SwitchPreference");
                    }
                    ((SwitchPreference)preference2).setChecked(sharedPreferences.getBoolean(s, false));
                }
            }
            else if (s.equals("KEY_DEBUG_DISABLE_AUTO_POWER_OFF")) {
                final Preference preference3 = this.findPreference(s);
                if (preference3 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type android.support.v14.preference.SwitchPreference");
                }
                ((SwitchPreference)preference3).setChecked(sharedPreferences.getBoolean(s, false));
            }
        }
        else if (s.equals("KEY_DEBUG_ALWAYS_PREDICTIVE_CAPTURE")) {
            final Preference preference4 = this.findPreference(s);
            if (preference4 == null) {
                throw new TypeCastException("null cannot be cast to non-null type android.support.v14.preference.SwitchPreference");
            }
            ((SwitchPreference)preference4).setChecked(sharedPreferences.getBoolean(s, false));
        }
    }
}
