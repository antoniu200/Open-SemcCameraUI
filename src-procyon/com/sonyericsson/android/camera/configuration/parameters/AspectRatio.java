// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import java.util.ArrayList;

public enum AspectRatio implements UserSettingValue
{
    private static final AspectRatio[] $VALUES;
    
    FOUR_TO_THREE(2131230947, 2131689640), 
    ONE_TO_ONE(2131230946, 2131689639), 
    SIXTEEN_TO_NINE(2131230945, 2131689638);
    
    public static final String TAG = "AspectRatio";
    private static final int sParameterTextId = 2131689641;
    private final int mIconId;
    private final int mTextId;
    
    static {
        $VALUES = new AspectRatio[] { AspectRatio.SIXTEEN_TO_NINE, AspectRatio.FOUR_TO_THREE, AspectRatio.ONE_TO_ONE };
    }
    
    private AspectRatio(final int mIconId, final int mTextId) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
    }
    
    public static AspectRatio getAspectRatio(final int n, final int n2) {
        if (n == 0 || n2 == 0) {
            return null;
        }
        if (n * 9 == n2 * 16) {
            return AspectRatio.SIXTEEN_TO_NINE;
        }
        if (n * 3 == n2 * 4) {
            return AspectRatio.FOUR_TO_THREE;
        }
        if (n == n2) {
            return AspectRatio.ONE_TO_ONE;
        }
        return null;
    }
    
    public static AspectRatio getDefaultValue(final CapturingMode capturingMode) {
        final Resolution defaultValue = Resolution.getDefaultValue(capturingMode);
        return getAspectRatio(defaultValue.getPictureRect().width(), defaultValue.getPictureRect().height());
    }
    
    public static AspectRatio[] getOptions(final CapturingMode capturingMode) {
        final ArrayList list = new ArrayList();
        if (!capturingMode.isVideo()) {
            final Resolution[] options = Resolution.getOptions(capturingMode);
            final int length = options.length;
            int i = 0;
            int n = 0;
            int n3;
            int n2 = n3 = n;
            while (i < length) {
                final Resolution resolution = options[i];
                final int width = resolution.getPictureRect().width();
                final int height = resolution.getPictureRect().height();
                int n4;
                int n5;
                if (getAspectRatio(width, height) == AspectRatio.SIXTEEN_TO_NINE) {
                    n4 = 1;
                    n5 = n2;
                }
                else if (getAspectRatio(width, height) == AspectRatio.FOUR_TO_THREE) {
                    n5 = 1;
                    n4 = n;
                }
                else {
                    n4 = n;
                    n5 = n2;
                    if (getAspectRatio(width, height) == AspectRatio.ONE_TO_ONE) {
                        n3 = 1;
                        n5 = n2;
                        n4 = n;
                    }
                }
                ++i;
                n = n4;
                n2 = n5;
            }
            if (n != 0) {
                list.add(AspectRatio.SIXTEEN_TO_NINE);
            }
            if (n2 != 0) {
                list.add(AspectRatio.FOUR_TO_THREE);
            }
            if (n3 != 0) {
                list.add(AspectRatio.ONE_TO_ONE);
            }
        }
        return list.toArray(new AspectRatio[0]);
    }
    
    @Override
    public void apply(final UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }
    
    @Override
    public int getIconId() {
        return this.mIconId;
    }
    
    @Override
    public UserSettingKey getKey() {
        return UserSettingKey.ASPECT_RATIO;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131689641;
    }
    
    @Override
    public String getName() {
        return this.getClass().getName();
    }
    
    @Override
    public int getTextId() {
        return this.mTextId;
    }
    
    @Override
    public String getValue() {
        return this.toString();
    }
}
