package com.sonyericsson.album.fastview;

import dalvik.system.PathClassLoader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ReflectionUtil {
    private static final Map<String, PathClassLoader> sClassLoaderMap = new HashMap();

    private ReflectionUtil() {
    }

    public static synchronized Class<?> getClass(String str, String str2) throws Exception {
        PathClassLoader pathClassLoader;
        pathClassLoader = sClassLoaderMap.get(str);
        if (pathClassLoader == null) {
            pathClassLoader = new PathClassLoader(str, ClassLoader.getSystemClassLoader());
            sClassLoaderMap.put(str, pathClassLoader);
        }
        return Class.forName(str2, true, pathClassLoader);
    }

    public static Method getMethod(Class<?> cls, String str, Class<?>... clsArr) throws Exception {
        Method declaredMethod = cls.getDeclaredMethod(str, clsArr);
        declaredMethod.setAccessible(true);
        return declaredMethod;
    }
}
