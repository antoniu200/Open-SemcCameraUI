// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.app;

import android.view.View;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.annotation.RestrictTo;
import java.util.List;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.IdRes;
import java.io.PrintWriter;
import java.io.FileDescriptor;
import android.support.annotation.NonNull;

public abstract class FragmentManager
{
    public static final int POP_BACK_STACK_INCLUSIVE = 1;
    
    public static void enableDebugLogging(final boolean debug) {
        FragmentManagerImpl.DEBUG = debug;
    }
    
    public abstract void addOnBackStackChangedListener(@NonNull final OnBackStackChangedListener p0);
    
    @NonNull
    public abstract FragmentTransaction beginTransaction();
    
    public abstract void dump(final String p0, final FileDescriptor p1, final PrintWriter p2, final String[] p3);
    
    public abstract boolean executePendingTransactions();
    
    @Nullable
    public abstract Fragment findFragmentById(@IdRes final int p0);
    
    @Nullable
    public abstract Fragment findFragmentByTag(@Nullable final String p0);
    
    @NonNull
    public abstract BackStackEntry getBackStackEntryAt(final int p0);
    
    public abstract int getBackStackEntryCount();
    
    @Nullable
    public abstract Fragment getFragment(@NonNull final Bundle p0, @NonNull final String p1);
    
    @NonNull
    public abstract List<Fragment> getFragments();
    
    @Nullable
    public abstract Fragment getPrimaryNavigationFragment();
    
    public abstract boolean isDestroyed();
    
    public abstract boolean isStateSaved();
    
    @Deprecated
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public FragmentTransaction openTransaction() {
        return this.beginTransaction();
    }
    
    public abstract void popBackStack();
    
    public abstract void popBackStack(final int p0, final int p1);
    
    public abstract void popBackStack(@Nullable final String p0, final int p1);
    
    public abstract boolean popBackStackImmediate();
    
    public abstract boolean popBackStackImmediate(final int p0, final int p1);
    
    public abstract boolean popBackStackImmediate(@Nullable final String p0, final int p1);
    
    public abstract void putFragment(@NonNull final Bundle p0, @NonNull final String p1, @NonNull final Fragment p2);
    
    public abstract void registerFragmentLifecycleCallbacks(@NonNull final FragmentLifecycleCallbacks p0, final boolean p1);
    
    public abstract void removeOnBackStackChangedListener(@NonNull final OnBackStackChangedListener p0);
    
    @Nullable
    public abstract Fragment.SavedState saveFragmentInstanceState(final Fragment p0);
    
    public abstract void unregisterFragmentLifecycleCallbacks(@NonNull final FragmentLifecycleCallbacks p0);
    
    public interface BackStackEntry
    {
        @Nullable
        CharSequence getBreadCrumbShortTitle();
        
        @StringRes
        int getBreadCrumbShortTitleRes();
        
        @Nullable
        CharSequence getBreadCrumbTitle();
        
        @StringRes
        int getBreadCrumbTitleRes();
        
        int getId();
        
        @Nullable
        String getName();
    }
    
    public abstract static class FragmentLifecycleCallbacks
    {
        public void onFragmentActivityCreated(@NonNull final FragmentManager fragmentManager, @NonNull final Fragment fragment, @Nullable final Bundle bundle) {
        }
        
        public void onFragmentAttached(@NonNull final FragmentManager fragmentManager, @NonNull final Fragment fragment, @NonNull final Context context) {
        }
        
        public void onFragmentCreated(@NonNull final FragmentManager fragmentManager, @NonNull final Fragment fragment, @Nullable final Bundle bundle) {
        }
        
        public void onFragmentDestroyed(@NonNull final FragmentManager fragmentManager, @NonNull final Fragment fragment) {
        }
        
        public void onFragmentDetached(@NonNull final FragmentManager fragmentManager, @NonNull final Fragment fragment) {
        }
        
        public void onFragmentPaused(@NonNull final FragmentManager fragmentManager, @NonNull final Fragment fragment) {
        }
        
        public void onFragmentPreAttached(@NonNull final FragmentManager fragmentManager, @NonNull final Fragment fragment, @NonNull final Context context) {
        }
        
        public void onFragmentPreCreated(@NonNull final FragmentManager fragmentManager, @NonNull final Fragment fragment, @Nullable final Bundle bundle) {
        }
        
        public void onFragmentResumed(@NonNull final FragmentManager fragmentManager, @NonNull final Fragment fragment) {
        }
        
        public void onFragmentSaveInstanceState(@NonNull final FragmentManager fragmentManager, @NonNull final Fragment fragment, @NonNull final Bundle bundle) {
        }
        
        public void onFragmentStarted(@NonNull final FragmentManager fragmentManager, @NonNull final Fragment fragment) {
        }
        
        public void onFragmentStopped(@NonNull final FragmentManager fragmentManager, @NonNull final Fragment fragment) {
        }
        
        public void onFragmentViewCreated(@NonNull final FragmentManager fragmentManager, @NonNull final Fragment fragment, @NonNull final View view, @Nullable final Bundle bundle) {
        }
        
        public void onFragmentViewDestroyed(@NonNull final FragmentManager fragmentManager, @NonNull final Fragment fragment) {
        }
    }
    
    public interface OnBackStackChangedListener
    {
        void onBackStackChanged();
    }
}
