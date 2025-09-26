// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.album.fastview;

import java.lang.reflect.Method;
import java.util.HashMap;
import dalvik.system.PathClassLoader;
import java.util.Map;

public class ReflectionUtil
{
    private static final Map<String, PathClassLoader> sClassLoaderMap;
    
    static {
        sClassLoaderMap = new HashMap<String, PathClassLoader>();
    }
    
    private ReflectionUtil() {
    }
    
    public static Class<?> getClass(final String s, final String name) throws Exception {
        synchronized (ReflectionUtil.class) {
            PathClassLoader loader;
            if ((loader = ReflectionUtil.sClassLoaderMap.get(s)) == null) {
                loader = new PathClassLoader(s, ClassLoader.getSystemClassLoader());
                ReflectionUtil.sClassLoaderMap.put(s, loader);
            }
            return Class.forName(name, true, (ClassLoader)loader);
        }
    }
    
    public static Method getMethod(final Class<?> clazz, final String name, final Class<?>... parameterTypes) throws Exception {
        final Method declaredMethod = clazz.getDeclaredMethod(name, (Class[])parameterTypes);
        declaredMethod.setAccessible(true);
        return declaredMethod;
    }
}
