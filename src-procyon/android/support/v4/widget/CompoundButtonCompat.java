// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.widget;

import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.support.annotation.Nullable;
import android.util.Log;
import android.os.Build$VERSION;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.widget.CompoundButton;
import java.lang.reflect.Field;

public final class CompoundButtonCompat
{
    private static final String TAG = "CompoundButtonCompat";
    private static Field sButtonDrawableField;
    private static boolean sButtonDrawableFieldFetched;
    
    private CompoundButtonCompat() {
    }
    
    @Nullable
    public static Drawable getButtonDrawable(@NonNull final CompoundButton obj) {
        if (Build$VERSION.SDK_INT >= 23) {
            return obj.getButtonDrawable();
        }
        if (!CompoundButtonCompat.sButtonDrawableFieldFetched) {
            try {
                (CompoundButtonCompat.sButtonDrawableField = CompoundButton.class.getDeclaredField("mButtonDrawable")).setAccessible(true);
            }
            catch (final NoSuchFieldException ex) {
                Log.i("CompoundButtonCompat", "Failed to retrieve mButtonDrawable field", (Throwable)ex);
            }
            CompoundButtonCompat.sButtonDrawableFieldFetched = true;
        }
        if (CompoundButtonCompat.sButtonDrawableField != null) {
            try {
                return (Drawable)CompoundButtonCompat.sButtonDrawableField.get(obj);
            }
            catch (final IllegalAccessException ex2) {
                Log.i("CompoundButtonCompat", "Failed to get button drawable via reflection", (Throwable)ex2);
                CompoundButtonCompat.sButtonDrawableField = null;
            }
        }
        return null;
    }
    
    @Nullable
    public static ColorStateList getButtonTintList(@NonNull final CompoundButton compoundButton) {
        if (Build$VERSION.SDK_INT >= 21) {
            return compoundButton.getButtonTintList();
        }
        if (compoundButton instanceof TintableCompoundButton) {
            return ((TintableCompoundButton)compoundButton).getSupportButtonTintList();
        }
        return null;
    }
    
    @Nullable
    public static PorterDuff$Mode getButtonTintMode(@NonNull final CompoundButton compoundButton) {
        if (Build$VERSION.SDK_INT >= 21) {
            return compoundButton.getButtonTintMode();
        }
        if (compoundButton instanceof TintableCompoundButton) {
            return ((TintableCompoundButton)compoundButton).getSupportButtonTintMode();
        }
        return null;
    }
    
    public static void setButtonTintList(@NonNull final CompoundButton compoundButton, @Nullable final ColorStateList list) {
        if (Build$VERSION.SDK_INT >= 21) {
            compoundButton.setButtonTintList(list);
        }
        else if (compoundButton instanceof TintableCompoundButton) {
            ((TintableCompoundButton)compoundButton).setSupportButtonTintList(list);
        }
    }
    
    public static void setButtonTintMode(@NonNull final CompoundButton compoundButton, @Nullable final PorterDuff$Mode porterDuff$Mode) {
        if (Build$VERSION.SDK_INT >= 21) {
            compoundButton.setButtonTintMode(porterDuff$Mode);
        }
        else if (compoundButton instanceof TintableCompoundButton) {
            ((TintableCompoundButton)compoundButton).setSupportButtonTintMode(porterDuff$Mode);
        }
    }
}
