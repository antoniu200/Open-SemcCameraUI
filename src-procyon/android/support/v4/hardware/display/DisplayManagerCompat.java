// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.hardware.display;

import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.hardware.display.DisplayManager;
import android.os.Build$VERSION;
import android.view.Display;
import android.support.annotation.NonNull;
import android.content.Context;
import java.util.WeakHashMap;

public final class DisplayManagerCompat
{
    public static final String DISPLAY_CATEGORY_PRESENTATION = "android.hardware.display.category.PRESENTATION";
    private static final WeakHashMap<Context, DisplayManagerCompat> sInstances;
    private final Context mContext;
    
    static {
        sInstances = new WeakHashMap<Context, DisplayManagerCompat>();
    }
    
    private DisplayManagerCompat(final Context mContext) {
        this.mContext = mContext;
    }
    
    @NonNull
    public static DisplayManagerCompat getInstance(@NonNull final Context context) {
        synchronized (DisplayManagerCompat.sInstances) {
            DisplayManagerCompat value;
            if ((value = DisplayManagerCompat.sInstances.get(context)) == null) {
                value = new DisplayManagerCompat(context);
                DisplayManagerCompat.sInstances.put(context, value);
            }
            return value;
        }
    }
    
    @Nullable
    public Display getDisplay(final int n) {
        if (Build$VERSION.SDK_INT >= 17) {
            return ((DisplayManager)this.mContext.getSystemService("display")).getDisplay(n);
        }
        final Display defaultDisplay = ((WindowManager)this.mContext.getSystemService("window")).getDefaultDisplay();
        if (defaultDisplay.getDisplayId() == n) {
            return defaultDisplay;
        }
        return null;
    }
    
    @NonNull
    public Display[] getDisplays() {
        if (Build$VERSION.SDK_INT >= 17) {
            return ((DisplayManager)this.mContext.getSystemService("display")).getDisplays();
        }
        return new Display[] { ((WindowManager)this.mContext.getSystemService("window")).getDefaultDisplay() };
    }
    
    @NonNull
    public Display[] getDisplays(@Nullable final String s) {
        if (Build$VERSION.SDK_INT >= 17) {
            return ((DisplayManager)this.mContext.getSystemService("display")).getDisplays(s);
        }
        if (s == null) {
            return new Display[0];
        }
        return new Display[] { ((WindowManager)this.mContext.getSystemService("window")).getDefaultDisplay() };
    }
}
