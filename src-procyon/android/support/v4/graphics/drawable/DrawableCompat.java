// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.graphics.drawable;

import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import org.xmlpull.v1.XmlPullParser;
import android.content.res.Resources;
import android.util.Log;
import android.graphics.ColorFilter;
import android.graphics.drawable.DrawableContainer$DrawableContainerState;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.InsetDrawable;
import android.os.Build$VERSION;
import android.content.res.Resources$Theme;
import android.support.annotation.NonNull;
import android.graphics.drawable.Drawable;
import java.lang.reflect.Method;

public final class DrawableCompat
{
    private static final String TAG = "DrawableCompat";
    private static Method sGetLayoutDirectionMethod;
    private static boolean sGetLayoutDirectionMethodFetched;
    private static Method sSetLayoutDirectionMethod;
    private static boolean sSetLayoutDirectionMethodFetched;
    
    private DrawableCompat() {
    }
    
    public static void applyTheme(@NonNull final Drawable drawable, @NonNull final Resources$Theme resources$Theme) {
        if (Build$VERSION.SDK_INT >= 21) {
            drawable.applyTheme(resources$Theme);
        }
    }
    
    public static boolean canApplyTheme(@NonNull final Drawable drawable) {
        return Build$VERSION.SDK_INT >= 21 && drawable.canApplyTheme();
    }
    
    public static void clearColorFilter(@NonNull final Drawable drawable) {
        if (Build$VERSION.SDK_INT >= 23) {
            drawable.clearColorFilter();
        }
        else if (Build$VERSION.SDK_INT >= 21) {
            drawable.clearColorFilter();
            if (drawable instanceof InsetDrawable) {
                clearColorFilter(((InsetDrawable)drawable).getDrawable());
            }
            else if (drawable instanceof WrappedDrawable) {
                clearColorFilter(((WrappedDrawable)drawable).getWrappedDrawable());
            }
            else if (drawable instanceof DrawableContainer) {
                final DrawableContainer$DrawableContainerState drawableContainer$DrawableContainerState = (DrawableContainer$DrawableContainerState)((DrawableContainer)drawable).getConstantState();
                if (drawableContainer$DrawableContainerState != null) {
                    for (int i = 0; i < drawableContainer$DrawableContainerState.getChildCount(); ++i) {
                        final Drawable child = drawableContainer$DrawableContainerState.getChild(i);
                        if (child != null) {
                            clearColorFilter(child);
                        }
                    }
                }
            }
        }
        else {
            drawable.clearColorFilter();
        }
    }
    
    public static int getAlpha(@NonNull final Drawable drawable) {
        if (Build$VERSION.SDK_INT >= 19) {
            return drawable.getAlpha();
        }
        return 0;
    }
    
    public static ColorFilter getColorFilter(@NonNull final Drawable drawable) {
        if (Build$VERSION.SDK_INT >= 21) {
            return drawable.getColorFilter();
        }
        return null;
    }
    
    public static int getLayoutDirection(@NonNull final Drawable obj) {
        if (Build$VERSION.SDK_INT >= 23) {
            return obj.getLayoutDirection();
        }
        if (Build$VERSION.SDK_INT >= 17) {
            if (!DrawableCompat.sGetLayoutDirectionMethodFetched) {
                try {
                    (DrawableCompat.sGetLayoutDirectionMethod = Drawable.class.getDeclaredMethod("getLayoutDirection", (Class<?>[])new Class[0])).setAccessible(true);
                }
                catch (final NoSuchMethodException ex) {
                    Log.i("DrawableCompat", "Failed to retrieve getLayoutDirection() method", (Throwable)ex);
                }
                DrawableCompat.sGetLayoutDirectionMethodFetched = true;
            }
            if (DrawableCompat.sGetLayoutDirectionMethod != null) {
                try {
                    return (int)DrawableCompat.sGetLayoutDirectionMethod.invoke(obj, new Object[0]);
                }
                catch (final Exception ex2) {
                    Log.i("DrawableCompat", "Failed to invoke getLayoutDirection() via reflection", (Throwable)ex2);
                    DrawableCompat.sGetLayoutDirectionMethod = null;
                }
            }
            return 0;
        }
        return 0;
    }
    
    public static void inflate(@NonNull final Drawable drawable, @NonNull final Resources resources, @NonNull final XmlPullParser xmlPullParser, @NonNull final AttributeSet set, @Nullable final Resources$Theme resources$Theme) throws XmlPullParserException, IOException {
        if (Build$VERSION.SDK_INT >= 21) {
            drawable.inflate(resources, xmlPullParser, set, resources$Theme);
        }
        else {
            drawable.inflate(resources, xmlPullParser, set);
        }
    }
    
    public static boolean isAutoMirrored(@NonNull final Drawable drawable) {
        return Build$VERSION.SDK_INT >= 19 && drawable.isAutoMirrored();
    }
    
    @Deprecated
    public static void jumpToCurrentState(@NonNull final Drawable drawable) {
        drawable.jumpToCurrentState();
    }
    
    public static void setAutoMirrored(@NonNull final Drawable drawable, final boolean autoMirrored) {
        if (Build$VERSION.SDK_INT >= 19) {
            drawable.setAutoMirrored(autoMirrored);
        }
    }
    
    public static void setHotspot(@NonNull final Drawable drawable, final float n, final float n2) {
        if (Build$VERSION.SDK_INT >= 21) {
            drawable.setHotspot(n, n2);
        }
    }
    
    public static void setHotspotBounds(@NonNull final Drawable drawable, final int n, final int n2, final int n3, final int n4) {
        if (Build$VERSION.SDK_INT >= 21) {
            drawable.setHotspotBounds(n, n2, n3, n4);
        }
    }
    
    public static boolean setLayoutDirection(@NonNull final Drawable obj, final int n) {
        if (Build$VERSION.SDK_INT >= 23) {
            return obj.setLayoutDirection(n);
        }
        if (Build$VERSION.SDK_INT >= 17) {
            if (!DrawableCompat.sSetLayoutDirectionMethodFetched) {
                try {
                    (DrawableCompat.sSetLayoutDirectionMethod = Drawable.class.getDeclaredMethod("setLayoutDirection", Integer.TYPE)).setAccessible(true);
                }
                catch (final NoSuchMethodException ex) {
                    Log.i("DrawableCompat", "Failed to retrieve setLayoutDirection(int) method", (Throwable)ex);
                }
                DrawableCompat.sSetLayoutDirectionMethodFetched = true;
            }
            if (DrawableCompat.sSetLayoutDirectionMethod != null) {
                try {
                    DrawableCompat.sSetLayoutDirectionMethod.invoke(obj, n);
                    return true;
                }
                catch (final Exception ex2) {
                    Log.i("DrawableCompat", "Failed to invoke setLayoutDirection(int) via reflection", (Throwable)ex2);
                    DrawableCompat.sSetLayoutDirectionMethod = null;
                }
            }
            return false;
        }
        return false;
    }
    
    public static void setTint(@NonNull final Drawable drawable, @ColorInt final int n) {
        if (Build$VERSION.SDK_INT >= 21) {
            drawable.setTint(n);
        }
        else if (drawable instanceof TintAwareDrawable) {
            ((TintAwareDrawable)drawable).setTint(n);
        }
    }
    
    public static void setTintList(@NonNull final Drawable drawable, @Nullable final ColorStateList list) {
        if (Build$VERSION.SDK_INT >= 21) {
            drawable.setTintList(list);
        }
        else if (drawable instanceof TintAwareDrawable) {
            ((TintAwareDrawable)drawable).setTintList(list);
        }
    }
    
    public static void setTintMode(@NonNull final Drawable drawable, @NonNull final PorterDuff$Mode porterDuff$Mode) {
        if (Build$VERSION.SDK_INT >= 21) {
            drawable.setTintMode(porterDuff$Mode);
        }
        else if (drawable instanceof TintAwareDrawable) {
            ((TintAwareDrawable)drawable).setTintMode(porterDuff$Mode);
        }
    }
    
    public static <T extends Drawable> T unwrap(@NonNull final Drawable drawable) {
        if (drawable instanceof WrappedDrawable) {
            return (T)((WrappedDrawable)drawable).getWrappedDrawable();
        }
        return (T)drawable;
    }
    
    public static Drawable wrap(@NonNull final Drawable drawable) {
        if (Build$VERSION.SDK_INT >= 23) {
            return drawable;
        }
        if (Build$VERSION.SDK_INT >= 21) {
            if (!(drawable instanceof TintAwareDrawable)) {
                return new WrappedDrawableApi21(drawable);
            }
            return drawable;
        }
        else {
            if (!(drawable instanceof TintAwareDrawable)) {
                return new WrappedDrawableApi14(drawable);
            }
            return drawable;
        }
    }
}
