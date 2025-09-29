package android.support.v4.content.res;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.FontRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.util.Preconditions;
import android.util.Log;
import android.util.TypedValue;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class ResourcesCompat {
    private static final String TAG = "ResourcesCompat";

    @Nullable
    public static Drawable getDrawable(@NonNull Resources resources, @DrawableRes int i, @Nullable Resources.Theme theme) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= 21) {
            return resources.getDrawable(i, theme);
        }
        return resources.getDrawable(i);
    }

    @Nullable
    public static Drawable getDrawableForDensity(@NonNull Resources resources, @DrawableRes int i, int i2, @Nullable Resources.Theme theme) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= 21) {
            return resources.getDrawableForDensity(i, i2, theme);
        }
        if (Build.VERSION.SDK_INT >= 15) {
            return resources.getDrawableForDensity(i, i2);
        }
        return resources.getDrawable(i);
    }

    @ColorInt
    public static int getColor(@NonNull Resources resources, @ColorRes int i, @Nullable Resources.Theme theme) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= 23) {
            return resources.getColor(i, theme);
        }
        return resources.getColor(i);
    }

    @Nullable
    public static ColorStateList getColorStateList(@NonNull Resources resources, @ColorRes int i, @Nullable Resources.Theme theme) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= 23) {
            return resources.getColorStateList(i, theme);
        }
        return resources.getColorStateList(i);
    }

    @Nullable
    public static Typeface getFont(@NonNull Context context, @FontRes int i) throws Resources.NotFoundException {
        if (context.isRestricted()) {
            return null;
        }
        return loadFont(context, i, new TypedValue(), 0, null, null, false);
    }

    public static abstract class FontCallback {
        public abstract void onFontRetrievalFailed(int i);

        public abstract void onFontRetrieved(@NonNull Typeface typeface);

        @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
        public final void callbackSuccessAsync(final Typeface typeface, @Nullable Handler handler) {
            if (handler == null) {
                handler = new Handler(Looper.getMainLooper());
            }
            handler.post(new Runnable() { // from class: android.support.v4.content.res.ResourcesCompat.FontCallback.1
                @Override // java.lang.Runnable
                public void run() {
                    FontCallback.this.onFontRetrieved(typeface);
                }
            });
        }

        @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
        public final void callbackFailAsync(final int i, @Nullable Handler handler) {
            if (handler == null) {
                handler = new Handler(Looper.getMainLooper());
            }
            handler.post(new Runnable() { // from class: android.support.v4.content.res.ResourcesCompat.FontCallback.2
                @Override // java.lang.Runnable
                public void run() {
                    FontCallback.this.onFontRetrievalFailed(i);
                }
            });
        }
    }

    public static void getFont(@NonNull Context context, @FontRes int i, @NonNull FontCallback fontCallback, @Nullable Handler handler) throws Resources.NotFoundException {
        Preconditions.checkNotNull(fontCallback);
        if (context.isRestricted()) {
            fontCallback.callbackFailAsync(-4, handler);
        } else {
            loadFont(context, i, new TypedValue(), 0, fontCallback, handler, false);
        }
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public static Typeface getFont(@NonNull Context context, @FontRes int i, TypedValue typedValue, int i2, @Nullable FontCallback fontCallback) throws Resources.NotFoundException {
        if (context.isRestricted()) {
            return null;
        }
        return loadFont(context, i, typedValue, i2, fontCallback, null, true);
    }

    private static Typeface loadFont(@NonNull Context context, int i, TypedValue typedValue, int i2, @Nullable FontCallback fontCallback, @Nullable Handler handler, boolean z) throws Resources.NotFoundException {
        Resources resources = context.getResources();
        resources.getValue(i, typedValue, true);
        Typeface typefaceLoadFont = loadFont(context, resources, typedValue, i, i2, fontCallback, handler, z);
        if (typefaceLoadFont != null || fontCallback != null) {
            return typefaceLoadFont;
        }
        throw new Resources.NotFoundException("Font resource ID #0x" + Integer.toHexString(i) + " could not be retrieved.");
    }

    private static Typeface loadFont(@NonNull Context context, Resources res, TypedValue value,
            int id, int style, @Nullable FontCallback fontCallback, @Nullable Handler handler,
            boolean isAsync) {
        final CharSequence cs = value.string;
        if (cs == null) {
            final android.content.res.Resources.NotFoundException nf = new android.content.res.Resources.NotFoundException(
                    "Resource \"" + res.getResourceName(id) + "\" (" + java.lang.Integer.toHexString(id) + ") is not a Font: " + value);
            throw nf;
        }

        final String file = cs.toString();

        // Only resources under res/ are valid for fonts
        if (!file.startsWith("res/")) {
            if (fontCallback != null) {
                fontCallback.callbackFailAsync(-3, handler);
            }
            return null;
        }

        // Cache check
        final android.graphics.Typeface cached = android.support.v4.graphics.TypefaceCompat.findFromCache(res, id, style);
        if (cached != null) {
            if (fontCallback != null) {
                fontCallback.callbackSuccessAsync(cached, handler);
            }
            return cached;
        }

        try {
            if (file.toLowerCase().endsWith(".xml")) {
                final android.content.res.XmlResourceParser parser = res.getXml(id);
                final android.support.v4.content.res.FontResourcesParserCompat.FamilyResourceEntry entry =
                        android.support.v4.content.res.FontResourcesParserCompat.parse(parser, res);
                if (entry == null) {
                    android.util.Log.e("ResourcesCompat", "Failed to find font-family tag");
                    if (fontCallback != null) {
                        fontCallback.callbackFailAsync(-3, handler);
                    }
                    return null;
                }
                return android.support.v4.graphics.TypefaceCompat.createFromResourcesFamilyXml(
                        context, entry, res, id, style, fontCallback, handler, isAsync);
            } else {
                final android.graphics.Typeface typeface =
                        android.support.v4.graphics.TypefaceCompat.createFromResourcesFontFile(context, res, id, file, style);
                if (fontCallback != null) {
                    if (typeface != null) {
                        fontCallback.callbackSuccessAsync(typeface, handler);
                    } else {
                        fontCallback.callbackFailAsync(-3, handler);
                    }
                }
                return typeface;
            }
        } catch (java.io.IOException e) {
            android.util.Log.e("ResourcesCompat", "Failed to read xml resource " + file, e);
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            android.util.Log.e("ResourcesCompat", "Failed to parse xml resource " + file, e);
        }

        if (fontCallback != null) {
            fontCallback.callbackFailAsync(-3, handler);
        }
        return null;
    }

    private ResourcesCompat() {
    }
}
