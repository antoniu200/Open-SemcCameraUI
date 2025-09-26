// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.app;

import android.util.LongSparseArray;
import android.support.annotation.RequiresApi;
import java.util.Map;
import android.util.Log;
import android.os.Build$VERSION;
import android.support.annotation.NonNull;
import android.content.res.Resources;
import java.lang.reflect.Field;

class ResourcesFlusher
{
    private static final String TAG = "ResourcesFlusher";
    private static Field sDrawableCacheField;
    private static boolean sDrawableCacheFieldFetched;
    private static Field sResourcesImplField;
    private static boolean sResourcesImplFieldFetched;
    private static Class sThemedResourceCacheClazz;
    private static boolean sThemedResourceCacheClazzFetched;
    private static Field sThemedResourceCache_mUnthemedEntriesField;
    private static boolean sThemedResourceCache_mUnthemedEntriesFieldFetched;
    
    private ResourcesFlusher() {
    }
    
    static void flush(@NonNull final Resources resources) {
        if (Build$VERSION.SDK_INT >= 28) {
            return;
        }
        if (Build$VERSION.SDK_INT >= 24) {
            flushNougats(resources);
        }
        else if (Build$VERSION.SDK_INT >= 23) {
            flushMarshmallows(resources);
        }
        else if (Build$VERSION.SDK_INT >= 21) {
            flushLollipops(resources);
        }
    }
    
    @RequiresApi(21)
    private static void flushLollipops(@NonNull final Resources obj) {
        if (!ResourcesFlusher.sDrawableCacheFieldFetched) {
            try {
                (ResourcesFlusher.sDrawableCacheField = Resources.class.getDeclaredField("mDrawableCache")).setAccessible(true);
            }
            catch (final NoSuchFieldException ex) {
                Log.e("ResourcesFlusher", "Could not retrieve Resources#mDrawableCache field", (Throwable)ex);
            }
            ResourcesFlusher.sDrawableCacheFieldFetched = true;
        }
        if (ResourcesFlusher.sDrawableCacheField != null) {
            Map map;
            try {
                map = (Map)ResourcesFlusher.sDrawableCacheField.get(obj);
            }
            catch (final IllegalAccessException ex2) {
                Log.e("ResourcesFlusher", "Could not retrieve value from Resources#mDrawableCache", (Throwable)ex2);
                map = null;
            }
            if (map != null) {
                map.clear();
            }
        }
    }
    
    @RequiresApi(23)
    private static void flushMarshmallows(@NonNull final Resources obj) {
        if (!ResourcesFlusher.sDrawableCacheFieldFetched) {
            try {
                (ResourcesFlusher.sDrawableCacheField = Resources.class.getDeclaredField("mDrawableCache")).setAccessible(true);
            }
            catch (final NoSuchFieldException ex) {
                Log.e("ResourcesFlusher", "Could not retrieve Resources#mDrawableCache field", (Throwable)ex);
            }
            ResourcesFlusher.sDrawableCacheFieldFetched = true;
        }
        Object value = null;
        Label_0069: {
            if (ResourcesFlusher.sDrawableCacheField != null) {
                try {
                    value = ResourcesFlusher.sDrawableCacheField.get(obj);
                    break Label_0069;
                }
                catch (final IllegalAccessException ex2) {
                    Log.e("ResourcesFlusher", "Could not retrieve value from Resources#mDrawableCache", (Throwable)ex2);
                }
            }
            value = null;
        }
        if (value == null) {
            return;
        }
        flushThemedResourcesCache(value);
    }
    
    @RequiresApi(24)
    private static void flushNougats(@NonNull Resources value) {
        if (!ResourcesFlusher.sResourcesImplFieldFetched) {
            try {
                (ResourcesFlusher.sResourcesImplField = Resources.class.getDeclaredField("mResourcesImpl")).setAccessible(true);
            }
            catch (final NoSuchFieldException ex) {
                Log.e("ResourcesFlusher", "Could not retrieve Resources#mResourcesImpl field", (Throwable)ex);
            }
            ResourcesFlusher.sResourcesImplFieldFetched = true;
        }
        if (ResourcesFlusher.sResourcesImplField == null) {
            return;
        }
        try {
            value = ResourcesFlusher.sResourcesImplField.get(value);
        }
        catch (final IllegalAccessException ex2) {
            Log.e("ResourcesFlusher", "Could not retrieve value from Resources#mResourcesImpl", (Throwable)ex2);
            value = null;
        }
        if (value == null) {
            return;
        }
        if (!ResourcesFlusher.sDrawableCacheFieldFetched) {
            try {
                (ResourcesFlusher.sDrawableCacheField = value.getClass().getDeclaredField("mDrawableCache")).setAccessible(true);
            }
            catch (final NoSuchFieldException ex3) {
                Log.e("ResourcesFlusher", "Could not retrieve ResourcesImpl#mDrawableCache field", (Throwable)ex3);
            }
            ResourcesFlusher.sDrawableCacheFieldFetched = true;
        }
        Object value2 = null;
        Label_0146: {
            if (ResourcesFlusher.sDrawableCacheField != null) {
                try {
                    value2 = ResourcesFlusher.sDrawableCacheField.get(value);
                    break Label_0146;
                }
                catch (final IllegalAccessException ex4) {
                    Log.e("ResourcesFlusher", "Could not retrieve value from ResourcesImpl#mDrawableCache", (Throwable)ex4);
                }
            }
            value2 = null;
        }
        if (value2 != null) {
            flushThemedResourcesCache(value2);
        }
    }
    
    @RequiresApi(16)
    private static void flushThemedResourcesCache(@NonNull final Object obj) {
        if (!ResourcesFlusher.sThemedResourceCacheClazzFetched) {
            try {
                ResourcesFlusher.sThemedResourceCacheClazz = Class.forName("android.content.res.ThemedResourceCache");
            }
            catch (final ClassNotFoundException ex) {
                Log.e("ResourcesFlusher", "Could not find ThemedResourceCache class", (Throwable)ex);
            }
            ResourcesFlusher.sThemedResourceCacheClazzFetched = true;
        }
        if (ResourcesFlusher.sThemedResourceCacheClazz == null) {
            return;
        }
        if (!ResourcesFlusher.sThemedResourceCache_mUnthemedEntriesFieldFetched) {
            try {
                (ResourcesFlusher.sThemedResourceCache_mUnthemedEntriesField = ResourcesFlusher.sThemedResourceCacheClazz.getDeclaredField("mUnthemedEntries")).setAccessible(true);
            }
            catch (final NoSuchFieldException ex2) {
                Log.e("ResourcesFlusher", "Could not retrieve ThemedResourceCache#mUnthemedEntries field", (Throwable)ex2);
            }
            ResourcesFlusher.sThemedResourceCache_mUnthemedEntriesFieldFetched = true;
        }
        if (ResourcesFlusher.sThemedResourceCache_mUnthemedEntriesField == null) {
            return;
        }
        LongSparseArray longSparseArray;
        try {
            longSparseArray = (LongSparseArray)ResourcesFlusher.sThemedResourceCache_mUnthemedEntriesField.get(obj);
        }
        catch (final IllegalAccessException ex3) {
            Log.e("ResourcesFlusher", "Could not retrieve value from ThemedResourceCache#mUnthemedEntries", (Throwable)ex3);
            longSparseArray = null;
        }
        if (longSparseArray != null) {
            longSparseArray.clear();
        }
    }
}
