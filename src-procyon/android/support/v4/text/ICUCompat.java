// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.text;

import android.support.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import android.util.Log;
import java.util.Locale;
import android.os.Build$VERSION;
import java.lang.reflect.Method;

public final class ICUCompat
{
    private static final String TAG = "ICUCompat";
    private static Method sAddLikelySubtagsMethod;
    private static Method sGetScriptMethod;
    
    static {
        if (Build$VERSION.SDK_INT >= 21) {
            try {
                ICUCompat.sAddLikelySubtagsMethod = Class.forName("libcore.icu.ICU").getMethod("addLikelySubtags", Locale.class);
                return;
            }
            catch (final Exception cause) {
                throw new IllegalStateException(cause);
            }
        }
        try {
            final Class<?> forName = Class.forName("libcore.icu.ICU");
            if (forName != null) {
                ICUCompat.sGetScriptMethod = forName.getMethod("getScript", String.class);
                ICUCompat.sAddLikelySubtagsMethod = forName.getMethod("addLikelySubtags", String.class);
            }
        }
        catch (final Exception ex) {
            ICUCompat.sGetScriptMethod = null;
            ICUCompat.sAddLikelySubtagsMethod = null;
            Log.w("ICUCompat", (Throwable)ex);
        }
    }
    
    private ICUCompat() {
    }
    
    private static String addLikelySubtags(Locale string) {
        string = (Locale)string.toString();
        try {
            if (ICUCompat.sAddLikelySubtagsMethod != null) {
                return (String)ICUCompat.sAddLikelySubtagsMethod.invoke(null, string);
            }
        }
        catch (final InvocationTargetException ex) {
            Log.w("ICUCompat", (Throwable)ex);
        }
        catch (final IllegalAccessException ex2) {
            Log.w("ICUCompat", (Throwable)ex2);
        }
        return (String)string;
    }
    
    private static String getScript(String s) {
        try {
            if (ICUCompat.sGetScriptMethod != null) {
                s = (String)ICUCompat.sGetScriptMethod.invoke(null, s);
                return s;
            }
        }
        catch (final InvocationTargetException ex) {
            Log.w("ICUCompat", (Throwable)ex);
        }
        catch (final IllegalAccessException ex2) {
            Log.w("ICUCompat", (Throwable)ex2);
        }
        return null;
    }
    
    @Nullable
    public static String maximizeAndGetScript(final Locale locale) {
        if (Build$VERSION.SDK_INT >= 21) {
            try {
                return ((Locale)ICUCompat.sAddLikelySubtagsMethod.invoke(null, locale)).getScript();
            }
            catch (final IllegalAccessException ex) {
                Log.w("ICUCompat", (Throwable)ex);
            }
            catch (final InvocationTargetException ex2) {
                Log.w("ICUCompat", (Throwable)ex2);
            }
            return locale.getScript();
        }
        final String addLikelySubtags = addLikelySubtags(locale);
        if (addLikelySubtags != null) {
            return getScript(addLikelySubtags);
        }
        return null;
    }
}
