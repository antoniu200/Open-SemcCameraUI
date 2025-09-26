// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.widget;

import android.os.Build$VERSION;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.view.View;

public class TooltipCompat
{
    private TooltipCompat() {
    }
    
    public static void setTooltipText(@NonNull final View view, @Nullable final CharSequence tooltipText) {
        if (Build$VERSION.SDK_INT >= 26) {
            view.setTooltipText(tooltipText);
        }
        else {
            TooltipCompatHandler.setTooltipText(view, tooltipText);
        }
    }
}
