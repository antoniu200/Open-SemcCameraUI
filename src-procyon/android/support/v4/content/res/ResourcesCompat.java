// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.content.res;

import android.os.Looper;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import android.support.v4.graphics.TypefaceCompat;
import android.support.v4.util.Preconditions;
import android.support.annotation.RestrictTo;
import android.os.Handler;
import android.util.TypedValue;
import android.graphics.Typeface;
import android.support.annotation.FontRes;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import android.content.res.Resources$NotFoundException;
import android.os.Build$VERSION;
import android.support.annotation.Nullable;
import android.content.res.Resources$Theme;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.content.res.Resources;

public final class ResourcesCompat
{
    private static final String TAG = "ResourcesCompat";
    
    private ResourcesCompat() {
    }
    
    @ColorInt
    public static int getColor(@NonNull final Resources resources, @ColorRes final int n, @Nullable final Resources$Theme resources$Theme) throws Resources$NotFoundException {
        if (Build$VERSION.SDK_INT >= 23) {
            return resources.getColor(n, resources$Theme);
        }
        return resources.getColor(n);
    }
    
    @Nullable
    public static ColorStateList getColorStateList(@NonNull final Resources resources, @ColorRes final int n, @Nullable final Resources$Theme resources$Theme) throws Resources$NotFoundException {
        if (Build$VERSION.SDK_INT >= 23) {
            return resources.getColorStateList(n, resources$Theme);
        }
        return resources.getColorStateList(n);
    }
    
    @Nullable
    public static Drawable getDrawable(@NonNull final Resources resources, @DrawableRes final int n, @Nullable final Resources$Theme resources$Theme) throws Resources$NotFoundException {
        if (Build$VERSION.SDK_INT >= 21) {
            return resources.getDrawable(n, resources$Theme);
        }
        return resources.getDrawable(n);
    }
    
    @Nullable
    public static Drawable getDrawableForDensity(@NonNull final Resources resources, @DrawableRes final int n, final int n2, @Nullable final Resources$Theme resources$Theme) throws Resources$NotFoundException {
        if (Build$VERSION.SDK_INT >= 21) {
            return resources.getDrawableForDensity(n, n2, resources$Theme);
        }
        if (Build$VERSION.SDK_INT >= 15) {
            return resources.getDrawableForDensity(n, n2);
        }
        return resources.getDrawable(n);
    }
    
    @Nullable
    public static Typeface getFont(@NonNull final Context context, @FontRes final int n) throws Resources$NotFoundException {
        if (context.isRestricted()) {
            return null;
        }
        return loadFont(context, n, new TypedValue(), 0, null, null, false);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static Typeface getFont(@NonNull final Context context, @FontRes final int n, final TypedValue typedValue, final int n2, @Nullable final FontCallback fontCallback) throws Resources$NotFoundException {
        if (context.isRestricted()) {
            return null;
        }
        return loadFont(context, n, typedValue, n2, fontCallback, null, true);
    }
    
    public static void getFont(@NonNull final Context context, @FontRes final int n, @NonNull final FontCallback fontCallback, @Nullable final Handler handler) throws Resources$NotFoundException {
        Preconditions.checkNotNull(fontCallback);
        if (context.isRestricted()) {
            fontCallback.callbackFailAsync(-4, handler);
            return;
        }
        loadFont(context, n, new TypedValue(), 0, fontCallback, handler, false);
    }
    
    private static Typeface loadFont(@NonNull final Context context, final int i, final TypedValue typedValue, final int n, @Nullable final FontCallback fontCallback, @Nullable final Handler handler, final boolean b) {
        final Resources resources = context.getResources();
        resources.getValue(i, typedValue, true);
        final Typeface loadFont = loadFont(context, resources, typedValue, i, n, fontCallback, handler, b);
        if (loadFont == null && fontCallback == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Font resource ID #0x");
            sb.append(Integer.toHexString(i));
            sb.append(" could not be retrieved.");
            throw new Resources$NotFoundException(sb.toString());
        }
        return loadFont;
    }
    
    private static Typeface loadFont(@NonNull final Context context, final Resources resources, TypedValue string, final int i, final int n, @Nullable final FontCallback fontCallback, @Nullable final Handler handler, final boolean b) {
        if (string.string == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Resource \"");
            sb.append(resources.getResourceName(i));
            sb.append("\" (");
            sb.append(Integer.toHexString(i));
            sb.append(") is not a Font: ");
            sb.append(string);
            throw new Resources$NotFoundException(sb.toString());
        }
        string = (TypedValue)string.string.toString();
        if (!((String)string).startsWith("res/")) {
            if (fontCallback != null) {
                fontCallback.callbackFailAsync(-3, handler);
            }
            return null;
        }
        final Typeface fromCache = TypefaceCompat.findFromCache(resources, i, n);
        if (fromCache != null) {
            if (fontCallback != null) {
                fontCallback.callbackSuccessAsync(fromCache, handler);
            }
            return fromCache;
        }
        try {
            if (!((String)string).toLowerCase().endsWith(".xml")) {
                final Typeface fromResourcesFontFile = TypefaceCompat.createFromResourcesFontFile(context, resources, i, (String)string, n);
                if (fontCallback != null) {
                    if (fromResourcesFontFile != null) {
                        fontCallback.callbackSuccessAsync(fromResourcesFontFile, handler);
                    }
                    else {
                        fontCallback.callbackFailAsync(-3, handler);
                    }
                }
                return fromResourcesFontFile;
            }
            final FontResourcesParserCompat.FamilyResourceEntry parse = FontResourcesParserCompat.parse((XmlPullParser)resources.getXml(i), resources);
            if (parse == null) {
                Log.e("ResourcesCompat", "Failed to find font-family tag");
                if (fontCallback != null) {
                    fontCallback.callbackFailAsync(-3, handler);
                }
                return null;
            }
            return TypefaceCompat.createFromResourcesFamilyXml(context, parse, resources, i, n, fontCallback, handler, b);
        }
        catch (final IOException ex) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Failed to read xml resource ");
            sb2.append((String)string);
            Log.e("ResourcesCompat", sb2.toString(), (Throwable)ex);
        }
        catch (final XmlPullParserException ex2) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Failed to parse xml resource ");
            sb3.append((String)string);
            Log.e("ResourcesCompat", sb3.toString(), (Throwable)ex2);
        }
        if (fontCallback != null) {
            fontCallback.callbackFailAsync(-3, handler);
        }
        return null;
    }
    
    public abstract static class FontCallback
    {
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public final void callbackFailAsync(final int n, @Nullable final Handler handler) {
            Handler handler2 = handler;
            if (handler == null) {
                handler2 = new Handler(Looper.getMainLooper());
            }
            handler2.post((Runnable)new Runnable(this, n) {
                final FontCallback this$0;
                final int val$reason;
                
                @Override
                public void run() {
                    this.this$0.onFontRetrievalFailed(this.val$reason);
                }
            });
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public final void callbackSuccessAsync(final Typeface typeface, @Nullable final Handler handler) {
            Handler handler2 = handler;
            if (handler == null) {
                handler2 = new Handler(Looper.getMainLooper());
            }
            handler2.post((Runnable)new Runnable(this, typeface) {
                final FontCallback this$0;
                final Typeface val$typeface;
                
                @Override
                public void run() {
                    this.this$0.onFontRetrieved(this.val$typeface);
                }
            });
        }
        
        public abstract void onFontRetrievalFailed(final int p0);
        
        public abstract void onFontRetrieved(@NonNull final Typeface p0);
    }
}
