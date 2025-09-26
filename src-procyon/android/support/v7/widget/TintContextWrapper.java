// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.widget;

import android.content.res.AssetManager;
import android.os.Build$VERSION;
import android.support.annotation.NonNull;
import android.content.Context;
import android.content.res.Resources$Theme;
import android.content.res.Resources;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import android.support.annotation.RestrictTo;
import android.content.ContextWrapper;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class TintContextWrapper extends ContextWrapper
{
    private static final Object CACHE_LOCK;
    private static ArrayList<WeakReference<TintContextWrapper>> sCache;
    private final Resources mResources;
    private final Resources$Theme mTheme;
    
    static {
        CACHE_LOCK = new Object();
    }
    
    private TintContextWrapper(@NonNull final Context context) {
        super(context);
        if (VectorEnabledTintResources.shouldBeUsed()) {
            this.mResources = new VectorEnabledTintResources((Context)this, context.getResources());
            (this.mTheme = this.mResources.newTheme()).setTo(context.getTheme());
        }
        else {
            this.mResources = new TintResources((Context)this, context.getResources());
            this.mTheme = null;
        }
    }
    
    private static boolean shouldWrap(@NonNull final Context context) {
        final boolean b = context instanceof TintContextWrapper;
        boolean b2 = false;
        if (!b && !(context.getResources() instanceof TintResources) && !(context.getResources() instanceof VectorEnabledTintResources)) {
            if (Build$VERSION.SDK_INT < 21 || VectorEnabledTintResources.shouldBeUsed()) {
                b2 = true;
            }
            return b2;
        }
        return false;
    }
    
    public static Context wrap(@NonNull final Context context) {
        if (shouldWrap(context)) {
            synchronized (TintContextWrapper.CACHE_LOCK) {
                if (TintContextWrapper.sCache == null) {
                    TintContextWrapper.sCache = new ArrayList<WeakReference<TintContextWrapper>>();
                }
                else {
                    for (int i = TintContextWrapper.sCache.size() - 1; i >= 0; --i) {
                        final WeakReference weakReference = TintContextWrapper.sCache.get(i);
                        if (weakReference == null || weakReference.get() == null) {
                            TintContextWrapper.sCache.remove(i);
                        }
                    }
                    for (int j = TintContextWrapper.sCache.size() - 1; j >= 0; --j) {
                        final WeakReference weakReference2 = TintContextWrapper.sCache.get(j);
                        TintContextWrapper tintContextWrapper;
                        if (weakReference2 != null) {
                            tintContextWrapper = (TintContextWrapper)weakReference2.get();
                        }
                        else {
                            tintContextWrapper = null;
                        }
                        if (tintContextWrapper != null && tintContextWrapper.getBaseContext() == context) {
                            return (Context)tintContextWrapper;
                        }
                    }
                }
                final TintContextWrapper referent = new TintContextWrapper(context);
                TintContextWrapper.sCache.add(new WeakReference<TintContextWrapper>(referent));
                return (Context)referent;
            }
        }
        return context;
    }
    
    public AssetManager getAssets() {
        return this.mResources.getAssets();
    }
    
    public Resources getResources() {
        return this.mResources;
    }
    
    public Resources$Theme getTheme() {
        Resources$Theme resources$Theme;
        if (this.mTheme == null) {
            resources$Theme = super.getTheme();
        }
        else {
            resources$Theme = this.mTheme;
        }
        return resources$Theme;
    }
    
    public void setTheme(final int theme) {
        if (this.mTheme == null) {
            super.setTheme(theme);
        }
        else {
            this.mTheme.applyStyle(theme, true);
        }
    }
}
