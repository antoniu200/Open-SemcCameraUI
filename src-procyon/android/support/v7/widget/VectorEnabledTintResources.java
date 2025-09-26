// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.widget;

import android.content.res.Resources$NotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Build$VERSION;
import android.support.annotation.NonNull;
import android.content.Context;
import java.lang.ref.WeakReference;
import android.support.annotation.RestrictTo;
import android.content.res.Resources;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class VectorEnabledTintResources extends Resources
{
    public static final int MAX_SDK_WHERE_REQUIRED = 20;
    private static boolean sCompatVectorFromResourcesEnabled = false;
    private final WeakReference<Context> mContextRef;
    
    public VectorEnabledTintResources(@NonNull final Context referent, @NonNull final Resources resources) {
        super(resources.getAssets(), resources.getDisplayMetrics(), resources.getConfiguration());
        this.mContextRef = new WeakReference<Context>(referent);
    }
    
    public static boolean isCompatVectorFromResourcesEnabled() {
        return VectorEnabledTintResources.sCompatVectorFromResourcesEnabled;
    }
    
    public static void setCompatVectorFromResourcesEnabled(final boolean sCompatVectorFromResourcesEnabled) {
        VectorEnabledTintResources.sCompatVectorFromResourcesEnabled = sCompatVectorFromResourcesEnabled;
    }
    
    public static boolean shouldBeUsed() {
        return isCompatVectorFromResourcesEnabled() && Build$VERSION.SDK_INT <= 20;
    }
    
    public Drawable getDrawable(final int n) throws Resources$NotFoundException {
        final Context context = this.mContextRef.get();
        if (context != null) {
            return AppCompatDrawableManager.get().onDrawableLoadedFromResources(context, this, n);
        }
        return super.getDrawable(n);
    }
    
    final Drawable superGetDrawable(final int n) {
        return super.getDrawable(n);
    }
}
