// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import java.io.PrintWriter;
import java.io.FileDescriptor;
import android.support.annotation.MainThread;
import android.arch.lifecycle.ViewModelStoreOwner;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;

public abstract class LoaderManager
{
    public static void enableDebugLogging(final boolean debug) {
        LoaderManagerImpl.DEBUG = debug;
    }
    
    @NonNull
    public static <T extends LifecycleOwner & ViewModelStoreOwner> LoaderManager getInstance(@NonNull final T t) {
        return new LoaderManagerImpl(t, t.getViewModelStore());
    }
    
    @MainThread
    public abstract void destroyLoader(final int p0);
    
    @Deprecated
    public abstract void dump(final String p0, final FileDescriptor p1, final PrintWriter p2, final String[] p3);
    
    @Nullable
    public abstract <D> Loader<D> getLoader(final int p0);
    
    public boolean hasRunningLoaders() {
        return false;
    }
    
    @MainThread
    @NonNull
    public abstract <D> Loader<D> initLoader(final int p0, @Nullable final Bundle p1, @NonNull final LoaderCallbacks<D> p2);
    
    public abstract void markForRedelivery();
    
    @MainThread
    @NonNull
    public abstract <D> Loader<D> restartLoader(final int p0, @Nullable final Bundle p1, @NonNull final LoaderCallbacks<D> p2);
    
    public interface LoaderCallbacks<D>
    {
        @MainThread
        @NonNull
        Loader<D> onCreateLoader(final int p0, @Nullable final Bundle p1);
        
        @MainThread
        void onLoadFinished(@NonNull final Loader<D> p0, final D p1);
        
        @MainThread
        void onLoaderReset(@NonNull final Loader<D> p0);
    }
}
