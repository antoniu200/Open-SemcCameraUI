// 
// Decompiled by Procyon v0.6.0
// 

package android.arch.lifecycle;

import java.lang.reflect.InvocationTargetException;
import android.app.Application;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

public class ViewModelProvider
{
    private static final String DEFAULT_KEY = "android.arch.lifecycle.ViewModelProvider.DefaultKey";
    private final Factory mFactory;
    private final ViewModelStore mViewModelStore;
    
    public ViewModelProvider(@NonNull final ViewModelStore mViewModelStore, @NonNull final Factory mFactory) {
        this.mFactory = mFactory;
        this.mViewModelStore = mViewModelStore;
    }
    
    public ViewModelProvider(@NonNull final ViewModelStoreOwner viewModelStoreOwner, @NonNull final Factory factory) {
        this(viewModelStoreOwner.getViewModelStore(), factory);
    }
    
    @MainThread
    @NonNull
    public <T extends ViewModel> T get(@NonNull final Class<T> clazz) {
        final String canonicalName = clazz.getCanonicalName();
        if (canonicalName == null) {
            throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("android.arch.lifecycle.ViewModelProvider.DefaultKey:");
        sb.append(canonicalName);
        return this.get(sb.toString(), clazz);
    }
    
    @MainThread
    @NonNull
    public <T extends ViewModel> T get(@NonNull final String s, @NonNull final Class<T> clazz) {
        final ViewModel value = this.mViewModelStore.get(s);
        if (clazz.isInstance(value)) {
            return (T)value;
        }
        final ViewModel create = this.mFactory.create(clazz);
        this.mViewModelStore.put(s, create);
        return (T)create;
    }
    
    public static class AndroidViewModelFactory extends NewInstanceFactory
    {
        private static AndroidViewModelFactory sInstance;
        private Application mApplication;
        
        public AndroidViewModelFactory(@NonNull final Application mApplication) {
            this.mApplication = mApplication;
        }
        
        @NonNull
        public static AndroidViewModelFactory getInstance(@NonNull final Application application) {
            if (AndroidViewModelFactory.sInstance == null) {
                AndroidViewModelFactory.sInstance = new AndroidViewModelFactory(application);
            }
            return AndroidViewModelFactory.sInstance;
        }
        
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull final Class<T> clazz) {
            if (AndroidViewModel.class.isAssignableFrom(clazz)) {
                try {
                    return clazz.getConstructor(Application.class).newInstance(this.mApplication);
                }
                catch (final InvocationTargetException cause) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Cannot create an instance of ");
                    sb.append(clazz);
                    throw new RuntimeException(sb.toString(), cause);
                }
                catch (final InstantiationException cause2) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Cannot create an instance of ");
                    sb2.append(clazz);
                    throw new RuntimeException(sb2.toString(), cause2);
                }
                catch (final IllegalAccessException cause3) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("Cannot create an instance of ");
                    sb3.append(clazz);
                    throw new RuntimeException(sb3.toString(), cause3);
                }
                catch (final NoSuchMethodException cause4) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("Cannot create an instance of ");
                    sb4.append(clazz);
                    throw new RuntimeException(sb4.toString(), cause4);
                }
            }
            return super.create(clazz);
        }
    }
    
    public static class NewInstanceFactory implements Factory
    {
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull final Class<T> clazz) {
            try {
                return clazz.newInstance();
            }
            catch (final IllegalAccessException cause) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Cannot create an instance of ");
                sb.append(clazz);
                throw new RuntimeException(sb.toString(), cause);
            }
            catch (final InstantiationException cause2) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Cannot create an instance of ");
                sb2.append(clazz);
                throw new RuntimeException(sb2.toString(), cause2);
            }
        }
    }
    
    public interface Factory
    {
        @NonNull
         <T extends ViewModel> T create(@NonNull final Class<T> p0);
    }
}
