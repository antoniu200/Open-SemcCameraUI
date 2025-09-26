// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.content.res;

import android.os.Build$VERSION;
import android.support.annotation.NonNull;
import android.content.res.Resources;

public final class ConfigurationHelper
{
    private ConfigurationHelper() {
    }
    
    public static int getDensityDpi(@NonNull final Resources resources) {
        if (Build$VERSION.SDK_INT >= 17) {
            return resources.getConfiguration().densityDpi;
        }
        return resources.getDisplayMetrics().densityDpi;
    }
}
