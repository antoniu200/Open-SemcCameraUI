// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.parameter;

import android.provider.Settings$Secure;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.configuration.parameters.PredictiveLaunch;
import android.support.annotation.NonNull;
import android.util.ArrayMap;
import android.content.Context;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import java.util.Map;

class SecureSetting
{
    private static final String KEY_LIFT_TRIGGER = "camera_lift_trigger_enabled";
    private static final Map<UserSettingKey, String> KEY_MAP;
    private final Context mContext;
    
    static {
        (KEY_MAP = (Map)new ArrayMap()).put(UserSettingKey.PREDICTIVE_LAUNCH, "camera_lift_trigger_enabled");
    }
    
    public SecureSetting(@NonNull final Context mContext) {
        this.mContext = mContext;
    }
    
    public void clear() {
        this.set(PredictiveLaunch.getDefaultValue());
    }
    
    public void set(@NonNull final UserSettingValue userSettingValue) throws IllegalArgumentException {
        if (!userSettingValue.getKey().isSecureSetting()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Should not be saved to SecureSetting: key = ");
            sb.append(userSettingValue.getKey());
            throw new IllegalArgumentException(sb.toString());
        }
        if (!SecureSetting.KEY_MAP.containsKey(userSettingValue.getKey())) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Cannot handle this key : key = ");
            sb2.append(userSettingValue.getKey());
            throw new IllegalArgumentException(sb2.toString());
        }
        String secureValue = null;
        if (SecureSetting$1.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[userSettingValue.getKey().ordinal()] == 1) {
            secureValue = ((PredictiveLaunch)userSettingValue).getSecureValue();
        }
        if (secureValue != null) {
            Settings$Secure.putString(this.mContext.getContentResolver(), (String)SecureSetting.KEY_MAP.get(userSettingValue.getKey()), secureValue);
        }
    }
}
