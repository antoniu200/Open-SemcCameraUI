// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.dynamic;

import java.lang.reflect.Field;
import android.os.IBinder;

public final class zze<T> extends zzd.zza
{
    private final T mWrappedObject;
    
    private zze(final T mWrappedObject) {
        this.mWrappedObject = mWrappedObject;
    }
    
    public static <T> T zzp(final zzd zzd) {
        if (zzd instanceof zze) {
            return (T)((zze)zzd).mWrappedObject;
        }
        final IBinder binder = zzd.asBinder();
        final Field[] declaredFields = binder.getClass().getDeclaredFields();
        if (declaredFields.length == 1) {
            final Field field = declaredFields[0];
            if (!field.isAccessible()) {
                field.setAccessible(true);
                try {
                    return (T)field.get(binder);
                }
                catch (final IllegalAccessException cause) {
                    throw new IllegalArgumentException("Could not access the field in remoteBinder.", cause);
                }
                catch (final IllegalArgumentException cause2) {
                    throw new IllegalArgumentException("remoteBinder is the wrong class.", cause2);
                }
                catch (final NullPointerException cause3) {
                    throw new IllegalArgumentException("Binder object is null.", cause3);
                }
            }
            throw new IllegalArgumentException("The concrete class implementing IObjectWrapper must have exactly one declared *private* field for the wrapped object. Preferably, this is an instance of the ObjectWrapper<T> class.");
        }
        throw new IllegalArgumentException("The concrete class implementing IObjectWrapper must have exactly *one* declared private field for the wrapped object.  Preferably, this is an instance of the ObjectWrapper<T> class.");
    }
    
    public static <T> zzd zzy(final T t) {
        return new zze<Object>(t);
    }
}
