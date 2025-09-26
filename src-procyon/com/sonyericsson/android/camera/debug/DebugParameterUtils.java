// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.debug;

import android.content.SharedPreferences$Editor;
import kotlin.jvm.internal.Intrinsics;
import android.content.SharedPreferences;
import android.content.Context;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.PropertyReference1;
import kotlin.reflect.KDeclarationContainer;
import kotlin.jvm.internal.PropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.Lazy;
import org.jetbrains.annotations.NotNull;
import kotlin.reflect.KProperty;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0012\u0010\u000f\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0002J\u000e\u0010\u0013\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u0012J\u000e\u0010\u0014\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u0012J\u000e\u0010\u0015\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u0012J\u000e\u0010\u0016\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u0012J\u0018\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0018\u001a\u00020\u0004H\u0002J\u000e\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0011\u001a\u00020\u0012J\u000e\u0010\u001b\u001a\u00020\u001a2\u0006\u0010\u0011\u001a\u00020\u0012R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u001b\u0010\n\u001a\u00020\u000b8FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\n\u0010\f¨\u0006\u001c" }, d2 = { "Lcom/sonyericsson/android/camera/debug/DebugParameterUtils;", "", "()V", "KEY_DEBUG_ALWAYS_PREDICTIVE_CAPTURE", "", "KEY_DEBUG_DISABLE_AUTO_POWER_OFF", "KEY_DEBUG_DISABLE_LOW_POWER_MODE", "KEY_DEBUG_EMULATE_SIDETOUCH", "KEY_DEBUG_FORCE_MIGRATE_SETTINGS", "SHARED_PREFERENCES_CAMERA_DEBUG", "isUserBuild", "", "()Z", "isUserBuild$delegate", "Lkotlin/Lazy;", "getSharedPreferences", "Landroid/content/SharedPreferences;", "context", "Landroid/content/Context;", "isAlwaysPredictiveCaptureEnabled", "isAutoPowerOffDisabled", "isEmulateSideTouchEnabled", "isLowPowerModeDisabled", "isTrue", "key", "preload", "", "reset", "SemcCameraUI_release" }, k = 1, mv = { 1, 1, 11 })
public final class DebugParameterUtils
{
    static final KProperty[] $$delegatedProperties;
    public static final DebugParameterUtils INSTANCE;
    @NotNull
    public static final String KEY_DEBUG_ALWAYS_PREDICTIVE_CAPTURE = "KEY_DEBUG_ALWAYS_PREDICTIVE_CAPTURE";
    @NotNull
    public static final String KEY_DEBUG_DISABLE_AUTO_POWER_OFF = "KEY_DEBUG_DISABLE_AUTO_POWER_OFF";
    @NotNull
    public static final String KEY_DEBUG_DISABLE_LOW_POWER_MODE = "KEY_DEBUG_DISABLE_LOW_POWER_MODE";
    @NotNull
    public static final String KEY_DEBUG_EMULATE_SIDETOUCH = "KEY_DEBUG_EMULATE_SIDETOUCH";
    @NotNull
    public static final String KEY_DEBUG_FORCE_MIGRATE_SETTINGS = "KEY_DEBUG_FORCE_MIGRATE_SETTINGS";
    @NotNull
    public static final String SHARED_PREFERENCES_CAMERA_DEBUG = "com.sonyericsson.android.camera.shared_preferences_debug";
    @NotNull
    private static final Lazy isUserBuild$delegate;
    
    static {
        $$delegatedProperties = new KProperty[] { Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(DebugParameterUtils.class), "isUserBuild", "isUserBuild()Z")) };
        INSTANCE = new DebugParameterUtils();
        isUserBuild$delegate = LazyKt__LazyJVMKt.lazy((Function0<?>)DebugParameterUtils$isUserBuild.DebugParameterUtils$isUserBuild$2.INSTANCE);
    }
    
    private DebugParameterUtils() {
    }
    
    private final SharedPreferences getSharedPreferences(final Context context) {
        SharedPreferences sharedPreferences;
        if (!this.isUserBuild()) {
            sharedPreferences = context.getSharedPreferences("com.sonyericsson.android.camera.shared_preferences_debug", 0);
        }
        else {
            sharedPreferences = null;
        }
        return sharedPreferences;
    }
    
    private final boolean isTrue(final Context context, final String s) {
        if (!this.isUserBuild()) {
            final SharedPreferences sharedPreferences = this.getSharedPreferences(context);
            return sharedPreferences != null && sharedPreferences.getBoolean(s, false);
        }
        return false;
    }
    
    public final boolean isAlwaysPredictiveCaptureEnabled(@NotNull final Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return this.isTrue(context, "KEY_DEBUG_ALWAYS_PREDICTIVE_CAPTURE");
    }
    
    public final boolean isAutoPowerOffDisabled(@NotNull final Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return this.isTrue(context, "KEY_DEBUG_DISABLE_AUTO_POWER_OFF");
    }
    
    public final boolean isEmulateSideTouchEnabled(@NotNull final Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return this.isTrue(context, "KEY_DEBUG_EMULATE_SIDETOUCH");
    }
    
    public final boolean isLowPowerModeDisabled(@NotNull final Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return this.isTrue(context, "KEY_DEBUG_DISABLE_LOW_POWER_MODE");
    }
    
    public final boolean isUserBuild() {
        final Lazy isUserBuild$delegate = DebugParameterUtils.isUserBuild$delegate;
        final KProperty kProperty = DebugParameterUtils.$$delegatedProperties[0];
        return (boolean)isUserBuild$delegate.getValue();
    }
    
    public final void preload(@NotNull final Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        this.getSharedPreferences(context);
    }
    
    public final void reset(@NotNull final Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        final SharedPreferences sharedPreferences = this.getSharedPreferences(context);
        if (sharedPreferences != null) {
            final SharedPreferences$Editor edit = sharedPreferences.edit();
            if (edit != null) {
                edit.remove("KEY_DEBUG_DISABLE_AUTO_POWER_OFF");
                edit.remove("KEY_DEBUG_DISABLE_LOW_POWER_MODE");
                edit.remove("KEY_DEBUG_ALWAYS_PREDICTIVE_CAPTURE");
                edit.remove("KEY_DEBUG_EMULATE_SIDETOUCH");
                edit.apply();
            }
        }
    }
}
