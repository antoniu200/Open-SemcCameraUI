// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.parameter;

import java.util.Iterator;
import java.util.Map;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.UserSettingSelectability;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValueHolder;

public class ParameterUtil
{
    public static final String TAG = "ParameterUtil";
    
    public static <T extends UserSettingValue> T applyCurrentValue(final UserSettingValueHolder<T> userSettingValueHolder, final T t) {
        final UserSettingKey key = userSettingValueHolder.get().getKey();
        if (userSettingValueHolder.getRecommendedValue() != null) {
            userSettingValueHolder.applyCurrentValue();
        }
        else {
            userSettingValueHolder.set(t);
        }
        key.setSelectability(UserSettingSelectability.SELECTABLE);
        return userSettingValueHolder.get();
    }
    
    public static <T extends UserSettingValue> T applyRecommendedValue(final UserSettingValueHolder<T> userSettingValueHolder, final T t) {
        final UserSettingKey key = t.getKey();
        userSettingValueHolder.applyRecommendedValue(t);
        key.setSelectability(UserSettingSelectability.SELECTABLE);
        return t;
    }
    
    public static void copy(final Map<UserSettingKey, UserSettingValueHolder<?>> map, final Map<UserSettingKey, UserSettingValueHolder<?>> map2) {
        for (final Map.Entry<Object, UserSettingValueHolder> entry : map2.entrySet()) {
            if (map.containsKey(entry.getKey()) && !entry.getKey().isCommon()) {
                final boolean hasChanged = map.get(entry.getKey()).hasChanged();
                entry.getValue().onApplied();
                entry.getValue().parseValueString(((UserSettingValueHolder)map.get(entry.getKey())).createValueString());
                if (hasChanged) {
                    entry.getValue().canChanged();
                }
                else {
                    entry.getValue().onApplied();
                }
            }
        }
    }
    
    public static <T extends UserSettingValue> T forceChange(final UserSettingValueHolder<T> userSettingValueHolder, final T t) {
        final UserSettingKey key = t.getKey();
        if (!key.isInvalid()) {
            userSettingValueHolder.forceChange(t);
            key.setSelectability(UserSettingSelectability.FORCE_CHANGED);
        }
        return t;
    }
    
    public static <T extends UserSettingValue> T getPrimaryValue(final T t, final T t2, final T[] array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (t == array[i]) {
                return t;
            }
        }
        return t2;
    }
    
    public static <T extends UserSettingValue> T reset(final UserSettingValueHolder<T> userSettingValueHolder) {
        final UserSettingKey key = userSettingValueHolder.get().getKey();
        if (!key.isInvalid()) {
            userSettingValueHolder.reset();
            key.setSelectability(UserSettingSelectability.SELECTABLE);
        }
        return userSettingValueHolder.get();
    }
    
    public static <T extends UserSettingValue> T reset(final UserSettingValueHolder<T> userSettingValueHolder, final T t) {
        final UserSettingKey key = t.getKey();
        if (!key.isInvalid()) {
            userSettingValueHolder.reset();
            userSettingValueHolder.set(t);
            key.setSelectability(UserSettingSelectability.SELECTABLE);
        }
        return userSettingValueHolder.get();
    }
    
    public static <T extends UserSettingValue> T unavailable(final UserSettingValueHolder<T> userSettingValueHolder, final T t) {
        final UserSettingKey key = t.getKey();
        if (!key.isInvalid()) {
            userSettingValueHolder.set(t);
            key.setSelectability(UserSettingSelectability.UNAVAILABLE);
        }
        return t;
    }
    
    public static <T extends UserSettingValue> UserSettingValueHolder<T> updateDefaultValue(final UserSettingValueHolder<T> userSettingValueHolder) {
        final T[] options = userSettingValueHolder.getOptions();
        final UserSettingSelectability selectability = UserSettingSelectability.getSelectability(options.length);
        if (selectability == UserSettingSelectability.INVALID) {
            return userSettingValueHolder;
        }
        final UserSettingValue defaultValue = userSettingValueHolder.getDefaultValue();
        final UserSettingValue userSettingValue = options[0];
        if (selectability == UserSettingSelectability.FIXED) {
            if (defaultValue != userSettingValue) {
                userSettingValueHolder.updateDefaultValue((T)userSettingValue);
            }
        }
        else if (selectability == UserSettingSelectability.SELECTABLE && defaultValue != getPrimaryValue(defaultValue, userSettingValue, options)) {
            userSettingValueHolder.updateDefaultValue((T)userSettingValue);
        }
        return userSettingValueHolder;
    }
}
