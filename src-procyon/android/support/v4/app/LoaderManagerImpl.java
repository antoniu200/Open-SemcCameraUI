// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.app;

import android.support.v4.util.SparseArrayCompat;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.MutableLiveData;
import android.support.v4.util.DebugUtils;
import java.io.PrintWriter;
import java.io.FileDescriptor;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.util.Log;
import java.lang.reflect.Modifier;
import android.support.v4.content.Loader;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.arch.lifecycle.ViewModelStore;
import android.support.annotation.NonNull;
import android.arch.lifecycle.LifecycleOwner;

class LoaderManagerImpl extends LoaderManager
{
    static boolean DEBUG = false;
    static final String TAG = "LoaderManager";
    @NonNull
    private final LifecycleOwner mLifecycleOwner;
    @NonNull
    private final LoaderViewModel mLoaderViewModel;
    
    LoaderManagerImpl(@NonNull final LifecycleOwner mLifecycleOwner, @NonNull final ViewModelStore viewModelStore) {
        this.mLifecycleOwner = mLifecycleOwner;
        this.mLoaderViewModel = LoaderViewModel.getInstance(viewModelStore);
    }
    
    @MainThread
    @NonNull
    private <D> Loader<D> createAndInstallLoader(final int n, @Nullable final Bundle bundle, @NonNull final LoaderCallbacks<D> loaderCallbacks, @Nullable final Loader<D> loader) {
        try {
            this.mLoaderViewModel.startCreatingLoader();
            final Loader<D> onCreateLoader = loaderCallbacks.onCreateLoader(n, bundle);
            if (onCreateLoader == null) {
                throw new IllegalArgumentException("Object returned from onCreateLoader must not be null");
            }
            if (onCreateLoader.getClass().isMemberClass() && !Modifier.isStatic(onCreateLoader.getClass().getModifiers())) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Object returned from onCreateLoader must not be a non-static inner member class: ");
                sb.append(onCreateLoader);
                throw new IllegalArgumentException(sb.toString());
            }
            final LoaderInfo obj = new LoaderInfo<D>(n, bundle, (Loader<Object>)onCreateLoader, (Loader<Object>)loader);
            if (LoaderManagerImpl.DEBUG) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("  Created new loader ");
                sb2.append(obj);
                Log.v("LoaderManager", sb2.toString());
            }
            this.mLoaderViewModel.putLoader(n, obj);
            this.mLoaderViewModel.finishCreatingLoader();
            return obj.setCallback(this.mLifecycleOwner, loaderCallbacks);
        }
        finally {
            this.mLoaderViewModel.finishCreatingLoader();
        }
    }
    
    @MainThread
    @Override
    public void destroyLoader(final int i) {
        if (this.mLoaderViewModel.isCreatingLoader()) {
            throw new IllegalStateException("Called while creating a loader");
        }
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new IllegalStateException("destroyLoader must be called on the main thread");
        }
        if (LoaderManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("destroyLoader in ");
            sb.append(this);
            sb.append(" of ");
            sb.append(i);
            Log.v("LoaderManager", sb.toString());
        }
        final LoaderInfo<Object> loader = this.mLoaderViewModel.getLoader(i);
        if (loader != null) {
            loader.destroy(true);
            this.mLoaderViewModel.removeLoader(i);
        }
    }
    
    @Deprecated
    @Override
    public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
        this.mLoaderViewModel.dump(s, fileDescriptor, printWriter, array);
    }
    
    @Nullable
    @Override
    public <D> Loader<D> getLoader(final int n) {
        if (this.mLoaderViewModel.isCreatingLoader()) {
            throw new IllegalStateException("Called while creating a loader");
        }
        final LoaderInfo<Object> loader = this.mLoaderViewModel.getLoader(n);
        Loader<Object> loader2;
        if (loader != null) {
            loader2 = loader.getLoader();
        }
        else {
            loader2 = null;
        }
        return (Loader<D>)loader2;
    }
    
    @Override
    public boolean hasRunningLoaders() {
        return this.mLoaderViewModel.hasRunningLoaders();
    }
    
    @MainThread
    @NonNull
    @Override
    public <D> Loader<D> initLoader(final int n, @Nullable final Bundle obj, @NonNull final LoaderCallbacks<D> loaderCallbacks) {
        if (this.mLoaderViewModel.isCreatingLoader()) {
            throw new IllegalStateException("Called while creating a loader");
        }
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new IllegalStateException("initLoader must be called on the main thread");
        }
        final LoaderInfo<Object> loader = this.mLoaderViewModel.getLoader(n);
        if (LoaderManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("initLoader in ");
            sb.append(this);
            sb.append(": args=");
            sb.append(obj);
            Log.v("LoaderManager", sb.toString());
        }
        if (loader == null) {
            return this.createAndInstallLoader(n, obj, loaderCallbacks, null);
        }
        if (LoaderManagerImpl.DEBUG) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("  Re-using existing loader ");
            sb2.append(loader);
            Log.v("LoaderManager", sb2.toString());
        }
        return (Loader<D>)loader.setCallback(this.mLifecycleOwner, (LoaderCallbacks<Object>)loaderCallbacks);
    }
    
    @Override
    public void markForRedelivery() {
        this.mLoaderViewModel.markForRedelivery();
    }
    
    @MainThread
    @NonNull
    @Override
    public <D> Loader<D> restartLoader(final int n, @Nullable final Bundle obj, @NonNull final LoaderCallbacks<D> loaderCallbacks) {
        if (this.mLoaderViewModel.isCreatingLoader()) {
            throw new IllegalStateException("Called while creating a loader");
        }
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new IllegalStateException("restartLoader must be called on the main thread");
        }
        if (LoaderManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("restartLoader in ");
            sb.append(this);
            sb.append(": args=");
            sb.append(obj);
            Log.v("LoaderManager", sb.toString());
        }
        final LoaderInfo<Object> loader = this.mLoaderViewModel.getLoader(n);
        Object destroy = null;
        if (loader != null) {
            destroy = loader.destroy(false);
        }
        return this.createAndInstallLoader(n, obj, loaderCallbacks, (Loader<D>)destroy);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(128);
        sb.append("LoaderManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        DebugUtils.buildShortClassTag(this.mLifecycleOwner, sb);
        sb.append("}}");
        return sb.toString();
    }
    
    public static class LoaderInfo<D> extends MutableLiveData<D> implements OnLoadCompleteListener<D>
    {
        @Nullable
        private final Bundle mArgs;
        private final int mId;
        private LifecycleOwner mLifecycleOwner;
        @NonNull
        private final Loader<D> mLoader;
        private LoaderObserver<D> mObserver;
        private Loader<D> mPriorLoader;
        
        LoaderInfo(final int mId, @Nullable final Bundle mArgs, @NonNull final Loader<D> mLoader, @Nullable final Loader<D> mPriorLoader) {
            this.mId = mId;
            this.mArgs = mArgs;
            this.mLoader = mLoader;
            this.mPriorLoader = mPriorLoader;
            this.mLoader.registerListener(mId, (Loader.OnLoadCompleteListener<D>)this);
        }
        
        @MainThread
        Loader<D> destroy(final boolean b) {
            if (LoaderManagerImpl.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("  Destroying: ");
                sb.append(this);
                Log.v("LoaderManager", sb.toString());
            }
            this.mLoader.cancelLoad();
            this.mLoader.abandon();
            final LoaderObserver<D> mObserver = this.mObserver;
            if (mObserver != null) {
                this.removeObserver(mObserver);
                if (b) {
                    mObserver.reset();
                }
            }
            this.mLoader.unregisterListener((Loader.OnLoadCompleteListener<D>)this);
            if ((mObserver != null && !mObserver.hasDeliveredData()) || b) {
                this.mLoader.reset();
                return this.mPriorLoader;
            }
            return this.mLoader;
        }
        
        public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
            printWriter.print(s);
            printWriter.print("mId=");
            printWriter.print(this.mId);
            printWriter.print(" mArgs=");
            printWriter.println(this.mArgs);
            printWriter.print(s);
            printWriter.print("mLoader=");
            printWriter.println(this.mLoader);
            final Loader<D> mLoader = this.mLoader;
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append("  ");
            mLoader.dump(sb.toString(), fileDescriptor, printWriter, array);
            if (this.mObserver != null) {
                printWriter.print(s);
                printWriter.print("mCallbacks=");
                printWriter.println(this.mObserver);
                final LoaderObserver<D> mObserver = this.mObserver;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(s);
                sb2.append("  ");
                mObserver.dump(sb2.toString(), printWriter);
            }
            printWriter.print(s);
            printWriter.print("mData=");
            printWriter.println(this.getLoader().dataToString(this.getValue()));
            printWriter.print(s);
            printWriter.print("mStarted=");
            printWriter.println(this.hasActiveObservers());
        }
        
        @NonNull
        Loader<D> getLoader() {
            return this.mLoader;
        }
        
        boolean isCallbackWaitingForData() {
            final boolean hasActiveObservers = this.hasActiveObservers();
            final boolean b = false;
            if (!hasActiveObservers) {
                return false;
            }
            boolean b2 = b;
            if (this.mObserver != null) {
                b2 = b;
                if (!this.mObserver.hasDeliveredData()) {
                    b2 = true;
                }
            }
            return b2;
        }
        
        void markForRedelivery() {
            final LifecycleOwner mLifecycleOwner = this.mLifecycleOwner;
            final LoaderObserver<D> mObserver = this.mObserver;
            if (mLifecycleOwner != null && mObserver != null) {
                super.removeObserver(mObserver);
                this.observe(mLifecycleOwner, mObserver);
            }
        }
        
        @Override
        protected void onActive() {
            if (LoaderManagerImpl.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("  Starting: ");
                sb.append(this);
                Log.v("LoaderManager", sb.toString());
            }
            this.mLoader.startLoading();
        }
        
        @Override
        protected void onInactive() {
            if (LoaderManagerImpl.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("  Stopping: ");
                sb.append(this);
                Log.v("LoaderManager", sb.toString());
            }
            this.mLoader.stopLoading();
        }
        
        @Override
        public void onLoadComplete(@NonNull final Loader<D> loader, @Nullable final D value) {
            if (LoaderManagerImpl.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("onLoadComplete: ");
                sb.append(this);
                Log.v("LoaderManager", sb.toString());
            }
            if (Looper.myLooper() == Looper.getMainLooper()) {
                this.setValue(value);
            }
            else {
                if (LoaderManagerImpl.DEBUG) {
                    Log.w("LoaderManager", "onLoadComplete was incorrectly called on a background thread");
                }
                this.postValue(value);
            }
        }
        
        @Override
        public void removeObserver(@NonNull final Observer<? super D> observer) {
            super.removeObserver((Observer<D>)observer);
            this.mLifecycleOwner = null;
            this.mObserver = null;
        }
        
        @MainThread
        @NonNull
        Loader<D> setCallback(@NonNull final LifecycleOwner mLifecycleOwner, @NonNull final LoaderCallbacks<D> loaderCallbacks) {
            final LoaderObserver mObserver = new LoaderObserver((Loader<D>)this.mLoader, (LoaderCallbacks<D>)loaderCallbacks);
            this.observe(mLifecycleOwner, mObserver);
            if (this.mObserver != null) {
                this.removeObserver(this.mObserver);
            }
            this.mLifecycleOwner = mLifecycleOwner;
            this.mObserver = mObserver;
            return this.mLoader;
        }
        
        @Override
        public void setValue(final D value) {
            super.setValue(value);
            if (this.mPriorLoader != null) {
                this.mPriorLoader.reset();
                this.mPriorLoader = null;
            }
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder(64);
            sb.append("LoaderInfo{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(" #");
            sb.append(this.mId);
            sb.append(" : ");
            DebugUtils.buildShortClassTag(this.mLoader, sb);
            sb.append("}}");
            return sb.toString();
        }
    }
    
    static class LoaderObserver<D> implements Observer<D>
    {
        @NonNull
        private final LoaderCallbacks<D> mCallback;
        private boolean mDeliveredData;
        @NonNull
        private final Loader<D> mLoader;
        
        LoaderObserver(@NonNull final Loader<D> mLoader, @NonNull final LoaderCallbacks<D> mCallback) {
            this.mDeliveredData = false;
            this.mLoader = mLoader;
            this.mCallback = mCallback;
        }
        
        public void dump(final String s, final PrintWriter printWriter) {
            printWriter.print(s);
            printWriter.print("mDeliveredData=");
            printWriter.println(this.mDeliveredData);
        }
        
        boolean hasDeliveredData() {
            return this.mDeliveredData;
        }
        
        @Override
        public void onChanged(@Nullable final D n) {
            if (LoaderManagerImpl.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("  onLoadFinished in ");
                sb.append(this.mLoader);
                sb.append(": ");
                sb.append(this.mLoader.dataToString(n));
                Log.v("LoaderManager", sb.toString());
            }
            this.mCallback.onLoadFinished(this.mLoader, n);
            this.mDeliveredData = true;
        }
        
        @MainThread
        void reset() {
            if (this.mDeliveredData) {
                if (LoaderManagerImpl.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("  Resetting: ");
                    sb.append(this.mLoader);
                    Log.v("LoaderManager", sb.toString());
                }
                this.mCallback.onLoaderReset(this.mLoader);
            }
        }
        
        @Override
        public String toString() {
            return this.mCallback.toString();
        }
    }
    
    static class LoaderViewModel extends ViewModel
    {
        private static final ViewModelProvider.Factory FACTORY;
        private boolean mCreatingLoader;
        private SparseArrayCompat<LoaderInfo> mLoaders;
        
        static {
            FACTORY = new ViewModelProvider.Factory() {
                @NonNull
                @Override
                public <T extends ViewModel> T create(@NonNull final Class<T> clazz) {
                    return (T)new LoaderViewModel();
                }
            };
        }
        
        LoaderViewModel() {
            this.mLoaders = new SparseArrayCompat<LoaderInfo>();
            this.mCreatingLoader = false;
        }
        
        @NonNull
        static LoaderViewModel getInstance(final ViewModelStore viewModelStore) {
            return new ViewModelProvider(viewModelStore, LoaderViewModel.FACTORY).get(LoaderViewModel.class);
        }
        
        public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
            if (this.mLoaders.size() > 0) {
                printWriter.print(s);
                printWriter.println("Loaders:");
                final StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append("    ");
                final String string = sb.toString();
                for (int i = 0; i < this.mLoaders.size(); ++i) {
                    final LoaderInfo loaderInfo = this.mLoaders.valueAt(i);
                    printWriter.print(s);
                    printWriter.print("  #");
                    printWriter.print(this.mLoaders.keyAt(i));
                    printWriter.print(": ");
                    printWriter.println(loaderInfo.toString());
                    loaderInfo.dump(string, fileDescriptor, printWriter, array);
                }
            }
        }
        
        void finishCreatingLoader() {
            this.mCreatingLoader = false;
        }
        
         <D> LoaderInfo<D> getLoader(final int n) {
            return this.mLoaders.get(n);
        }
        
        boolean hasRunningLoaders() {
            for (int size = this.mLoaders.size(), i = 0; i < size; ++i) {
                if (this.mLoaders.valueAt(i).isCallbackWaitingForData()) {
                    return true;
                }
            }
            return false;
        }
        
        boolean isCreatingLoader() {
            return this.mCreatingLoader;
        }
        
        void markForRedelivery() {
            for (int size = this.mLoaders.size(), i = 0; i < size; ++i) {
                this.mLoaders.valueAt(i).markForRedelivery();
            }
        }
        
        @Override
        protected void onCleared() {
            super.onCleared();
            for (int size = this.mLoaders.size(), i = 0; i < size; ++i) {
                this.mLoaders.valueAt(i).destroy(true);
            }
            this.mLoaders.clear();
        }
        
        void putLoader(final int n, @NonNull final LoaderInfo loaderInfo) {
            this.mLoaders.put(n, loaderInfo);
        }
        
        void removeLoader(final int n) {
            this.mLoaders.remove(n);
        }
        
        void startCreatingLoader() {
            this.mCreatingLoader = true;
        }
    }
}
