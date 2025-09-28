package com.sonyericsson.android.camera.debug;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import java.util.Locale;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.PropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KProperty;
import org.jetbrains.annotations.NotNull;

/* compiled from: DebugParameterUtils.kt */
@Metadata(bv = {1, 0, 2}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0012\u0010\u000f\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0002J\u000e\u0010\u0013\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u0012J\u000e\u0010\u0014\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u0012J\u000e\u0010\u0015\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u0012J\u000e\u0010\u0016\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u0012J\u0018\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0018\u001a\u00020\u0004H\u0002J\u000e\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0011\u001a\u00020\u0012J\u000e\u0010\u001b\u001a\u00020\u001a2\u0006\u0010\u0011\u001a\u00020\u0012R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u001b\u0010\n\u001a\u00020\u000b8FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\n\u0010\f¨\u0006\u001c"}, d2 = {"Lcom/sonyericsson/android/camera/debug/DebugParameterUtils;", "", "()V", DebugParameterUtils.KEY_DEBUG_ALWAYS_PREDICTIVE_CAPTURE, "", DebugParameterUtils.KEY_DEBUG_DISABLE_AUTO_POWER_OFF, DebugParameterUtils.KEY_DEBUG_DISABLE_LOW_POWER_MODE, DebugParameterUtils.KEY_DEBUG_EMULATE_SIDETOUCH, DebugParameterUtils.KEY_DEBUG_FORCE_MIGRATE_SETTINGS, "SHARED_PREFERENCES_CAMERA_DEBUG", "isUserBuild", "", "()Z", "isUserBuild$delegate", "Lkotlin/Lazy;", "getSharedPreferences", "Landroid/content/SharedPreferences;", "context", "Landroid/content/Context;", "isAlwaysPredictiveCaptureEnabled", "isAutoPowerOffDisabled", "isEmulateSideTouchEnabled", "isLowPowerModeDisabled", "isTrue", "key", "preload", "", "reset", "SemcCameraUI_release"}, k = 1, mv = {1, 1, 11})
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class DebugParameterUtils {

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
    static final /* synthetic */ KProperty[] $$delegatedProperties = {Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(DebugParameterUtils.class), "isUserBuild", "isUserBuild()Z"))};
    public static final DebugParameterUtils INSTANCE = new DebugParameterUtils();

    /* renamed from: isUserBuild$delegate, reason: from kotlin metadata */
    @NotNull
    private static final Lazy isUserBuild = LazyKt.lazy(new Function0<Boolean>() { // from class: com.sonyericsson.android.camera.debug.DebugParameterUtils.isUserBuild.2
        @Override // kotlin.jvm.functions.Function0
        public /* bridge */ /* synthetic */ Boolean invoke() {
            return Boolean.valueOf(invoke2());
        }

        /* renamed from: invoke, reason: avoid collision after fix types in other method */
        public final boolean invoke2() {
            String str = Build.TYPE;
            Intrinsics.checkExpressionValueIsNotNull(str, "Build.TYPE");
            Locale locale = Locale.ENGLISH;
            Intrinsics.checkExpressionValueIsNotNull(locale, "Locale.ENGLISH");
            if (str == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String lowerCase = str.toLowerCase(locale);
            Intrinsics.checkExpressionValueIsNotNull(lowerCase, "(this as java.lang.String).toLowerCase(locale)");
            return Intrinsics.areEqual(lowerCase, "user");
        }
    });

    public final boolean isUserBuild() {
        Lazy lazy = isUserBuild;
        KProperty kProperty = $$delegatedProperties[0];
        return ((Boolean) lazy.getValue()).booleanValue();
    }

    private DebugParameterUtils() {
    }

    public final void preload(@NotNull Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        getSharedPreferences(context);
    }

    public final void reset(@NotNull Context context) {
        SharedPreferences.Editor editorEdit;
        Intrinsics.checkParameterIsNotNull(context, "context");
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        if (sharedPreferences == null || (editorEdit = sharedPreferences.edit()) == null) {
            return;
        }
        editorEdit.remove(KEY_DEBUG_DISABLE_AUTO_POWER_OFF);
        editorEdit.remove(KEY_DEBUG_DISABLE_LOW_POWER_MODE);
        editorEdit.remove(KEY_DEBUG_ALWAYS_PREDICTIVE_CAPTURE);
        editorEdit.remove(KEY_DEBUG_EMULATE_SIDETOUCH);
        editorEdit.apply();
    }

    public final boolean isAutoPowerOffDisabled(@NotNull Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return isTrue(context, KEY_DEBUG_DISABLE_AUTO_POWER_OFF);
    }

    public final boolean isLowPowerModeDisabled(@NotNull Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return isTrue(context, KEY_DEBUG_DISABLE_LOW_POWER_MODE);
    }

    public final boolean isAlwaysPredictiveCaptureEnabled(@NotNull Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return isTrue(context, KEY_DEBUG_ALWAYS_PREDICTIVE_CAPTURE);
    }

    public final boolean isEmulateSideTouchEnabled(@NotNull Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return isTrue(context, KEY_DEBUG_EMULATE_SIDETOUCH);
    }

    private final boolean isTrue(Context context, String key) {
        SharedPreferences sharedPreferences;
        if (isUserBuild() || (sharedPreferences = getSharedPreferences(context)) == null) {
            return false;
        }
        return sharedPreferences.getBoolean(key, false);
    }

    private final SharedPreferences getSharedPreferences(Context context) {
        if (isUserBuild()) {
            return null;
        }
        return context.getSharedPreferences(SHARED_PREFERENCES_CAMERA_DEBUG, 0);
    }
}
