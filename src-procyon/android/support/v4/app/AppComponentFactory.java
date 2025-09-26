// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.app;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.app.Application;
import java.lang.reflect.InvocationTargetException;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.RequiresApi;

@RequiresApi(28)
public class AppComponentFactory extends android.app.AppComponentFactory
{
    public final Activity instantiateActivity(final ClassLoader classLoader, final String s, final Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(this.instantiateActivityCompat(classLoader, s, intent));
    }
    
    @NonNull
    public Activity instantiateActivityCompat(@NonNull final ClassLoader classLoader, @NonNull final String name, @Nullable final Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            return (Activity)classLoader.loadClass(name).getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
        }
        catch (final InvocationTargetException | NoSuchMethodException cause) {
            throw new RuntimeException("Couldn't call constructor", (Throwable)cause);
        }
    }
    
    public final Application instantiateApplication(final ClassLoader classLoader, final String s) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(this.instantiateApplicationCompat(classLoader, s));
    }
    
    @NonNull
    public Application instantiateApplicationCompat(@NonNull final ClassLoader classLoader, @NonNull final String name) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            return (Application)classLoader.loadClass(name).getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
        }
        catch (final InvocationTargetException | NoSuchMethodException cause) {
            throw new RuntimeException("Couldn't call constructor", (Throwable)cause);
        }
    }
    
    public final ContentProvider instantiateProvider(final ClassLoader classLoader, final String s) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(this.instantiateProviderCompat(classLoader, s));
    }
    
    @NonNull
    public ContentProvider instantiateProviderCompat(@NonNull final ClassLoader classLoader, @NonNull final String name) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            return (ContentProvider)classLoader.loadClass(name).getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
        }
        catch (final InvocationTargetException | NoSuchMethodException cause) {
            throw new RuntimeException("Couldn't call constructor", (Throwable)cause);
        }
    }
    
    public final BroadcastReceiver instantiateReceiver(final ClassLoader classLoader, final String s, final Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(this.instantiateReceiverCompat(classLoader, s, intent));
    }
    
    @NonNull
    public BroadcastReceiver instantiateReceiverCompat(@NonNull final ClassLoader classLoader, @NonNull final String name, @Nullable final Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            return (BroadcastReceiver)classLoader.loadClass(name).getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
        }
        catch (final InvocationTargetException | NoSuchMethodException cause) {
            throw new RuntimeException("Couldn't call constructor", (Throwable)cause);
        }
    }
    
    public final Service instantiateService(final ClassLoader classLoader, final String s, final Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(this.instantiateServiceCompat(classLoader, s, intent));
    }
    
    @NonNull
    public Service instantiateServiceCompat(@NonNull final ClassLoader classLoader, @NonNull final String name, @Nullable final Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            return (Service)classLoader.loadClass(name).getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
        }
        catch (final InvocationTargetException | NoSuchMethodException cause) {
            throw new RuntimeException("Couldn't call constructor", (Throwable)cause);
        }
    }
}
