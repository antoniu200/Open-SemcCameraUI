// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.app;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.app.Application;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.RestrictTo;
import android.support.annotation.RequiresApi;
import android.app.AppComponentFactory;

@RequiresApi(api = 28)
@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class CoreComponentFactory extends AppComponentFactory
{
    private static final String TAG = "CoreComponentFactory";
    
    static <T> T checkCompatWrapper(final T t) {
        if (t instanceof CompatWrapped) {
            final Object wrapper = ((CompatWrapped)t).getWrapper();
            if (wrapper != null) {
                return (T)wrapper;
            }
        }
        return t;
    }
    
    public Activity instantiateActivity(final ClassLoader classLoader, final String s, final Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return checkCompatWrapper(super.instantiateActivity(classLoader, s, intent));
    }
    
    public Application instantiateApplication(final ClassLoader classLoader, final String s) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return checkCompatWrapper(super.instantiateApplication(classLoader, s));
    }
    
    public ContentProvider instantiateProvider(final ClassLoader classLoader, final String s) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return checkCompatWrapper(super.instantiateProvider(classLoader, s));
    }
    
    public BroadcastReceiver instantiateReceiver(final ClassLoader classLoader, final String s, final Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return checkCompatWrapper(super.instantiateReceiver(classLoader, s, intent));
    }
    
    public Service instantiateService(final ClassLoader classLoader, final String s, final Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return checkCompatWrapper(super.instantiateService(classLoader, s, intent));
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public interface CompatWrapped
    {
        Object getWrapper();
    }
}
