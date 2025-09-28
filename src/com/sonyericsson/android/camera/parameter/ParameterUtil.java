package com.sonyericsson.android.camera.parameter;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.UserSettingSelectability;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValueHolder;
import java.util.Map;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ParameterUtil {
    public static final String TAG = "ParameterUtil";

    public static <T extends UserSettingValue> UserSettingValueHolder<T> updateDefaultValue(UserSettingValueHolder<T> userSettingValueHolder) {
        UserSettingValue[] options = userSettingValueHolder.getOptions();
        UserSettingSelectability selectability = UserSettingSelectability.getSelectability(options.length);
        if (selectability == UserSettingSelectability.INVALID) {
            return userSettingValueHolder;
        }
        UserSettingValue defaultValue = userSettingValueHolder.getDefaultValue();
        UserSettingValue userSettingValue = options[0];
        if (selectability == UserSettingSelectability.FIXED) {
            if (defaultValue != userSettingValue) {
                userSettingValueHolder.updateDefaultValue(userSettingValue);
            }
        } else if (selectability == UserSettingSelectability.SELECTABLE && defaultValue != getPrimaryValue(defaultValue, userSettingValue, options)) {
            userSettingValueHolder.updateDefaultValue(userSettingValue);
        }
        return userSettingValueHolder;
    }

    public static <T extends UserSettingValue> T getPrimaryValue(T t, T t2, T[] tArr) {
        for (T t3 : tArr) {
            if (t == t3) {
                return t;
            }
        }
        return t2;
    }

    public static <T extends UserSettingValue> T forceChange(UserSettingValueHolder<T> userSettingValueHolder, T t) {
        UserSettingKey key = t.getKey();
        if (!key.isInvalid()) {
            userSettingValueHolder.forceChange(t);
            key.setSelectability(UserSettingSelectability.FORCE_CHANGED);
        }
        return t;
    }

    public static <T extends UserSettingValue> T unavailable(UserSettingValueHolder<T> userSettingValueHolder, T t) {
        UserSettingKey key = t.getKey();
        if (!key.isInvalid()) {
            userSettingValueHolder.set(t);
            key.setSelectability(UserSettingSelectability.UNAVAILABLE);
        }
        return t;
    }

    public static <T extends UserSettingValue> T reset(UserSettingValueHolder<T> userSettingValueHolder) {
        UserSettingKey key = userSettingValueHolder.get().getKey();
        if (!key.isInvalid()) {
            userSettingValueHolder.reset();
            key.setSelectability(UserSettingSelectability.SELECTABLE);
        }
        return (T) userSettingValueHolder.get();
    }

    public static <T extends UserSettingValue> T applyRecommendedValue(UserSettingValueHolder<T> userSettingValueHolder, T t) {
        UserSettingKey key = t.getKey();
        userSettingValueHolder.applyRecommendedValue(t);
        key.setSelectability(UserSettingSelectability.SELECTABLE);
        return t;
    }

    public static <T extends UserSettingValue> T reset(UserSettingValueHolder<T> userSettingValueHolder, T t) {
        UserSettingKey key = t.getKey();
        if (!key.isInvalid()) {
            userSettingValueHolder.reset();
            userSettingValueHolder.set(t);
            key.setSelectability(UserSettingSelectability.SELECTABLE);
        }
        return (T) userSettingValueHolder.get();
    }

    public static <T extends UserSettingValue> T applyCurrentValue(UserSettingValueHolder<T> userSettingValueHolder, T t) {
        UserSettingKey key = userSettingValueHolder.get().getKey();
        if (userSettingValueHolder.getRecommendedValue() != null) {
            userSettingValueHolder.applyCurrentValue();
        } else {
            userSettingValueHolder.set(t);
        }
        key.setSelectability(UserSettingSelectability.SELECTABLE);
        return (T) userSettingValueHolder.get();
    }

    public static void copy(Map<UserSettingKey, UserSettingValueHolder<?>> map, Map<UserSettingKey, UserSettingValueHolder<?>> map2) {
        for (Map.Entry<UserSettingKey, UserSettingValueHolder<?>> entry : map2.entrySet()) {
            if (map.containsKey(entry.getKey()) && !entry.getKey().isCommon()) {
                boolean zHasChanged = map.get(entry.getKey()).hasChanged();
                entry.getValue().onApplied();
                entry.getValue().parseValueString(map.get(entry.getKey()).createValueString());
                if (zHasChanged) {
                    entry.getValue().canChanged();
                } else {
                    entry.getValue().onApplied();
                }
            }
        }
    }
}
