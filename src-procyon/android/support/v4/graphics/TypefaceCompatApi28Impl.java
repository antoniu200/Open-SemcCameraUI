// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.graphics;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Array;
import android.graphics.Typeface;
import android.support.annotation.RestrictTo;
import android.support.annotation.RequiresApi;

@RequiresApi(28)
@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class TypefaceCompatApi28Impl extends TypefaceCompatApi26Impl
{
    private static final String CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD = "createFromFamiliesWithDefault";
    private static final String DEFAULT_FAMILY = "sans-serif";
    private static final int RESOLVE_BY_FONT_TABLE = -1;
    private static final String TAG = "TypefaceCompatApi28Impl";
    
    @Override
    protected Typeface createFromFamiliesWithDefault(final Object o) {
        try {
            final Object instance = Array.newInstance(this.mFontFamily, 1);
            Array.set(instance, 0, o);
            return (Typeface)this.mCreateFromFamiliesWithDefault.invoke(null, instance, "sans-serif", -1, -1);
        }
        catch (final IllegalAccessException | InvocationTargetException cause) {
            throw new RuntimeException((Throwable)cause);
        }
    }
    
    @Override
    protected Method obtainCreateFromFamiliesWithDefaultMethod(final Class componentType) throws NoSuchMethodException {
        final Method declaredMethod = Typeface.class.getDeclaredMethod("createFromFamiliesWithDefault", Array.newInstance(componentType, 1).getClass(), String.class, Integer.TYPE, Integer.TYPE);
        declaredMethod.setAccessible(true);
        return declaredMethod;
    }
}
