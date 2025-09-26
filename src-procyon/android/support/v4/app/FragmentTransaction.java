// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.app;

import android.support.annotation.StyleRes;
import android.support.annotation.AnimatorRes;
import android.support.annotation.AnimRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.annotation.IdRes;

public abstract class FragmentTransaction
{
    public static final int TRANSIT_ENTER_MASK = 4096;
    public static final int TRANSIT_EXIT_MASK = 8192;
    public static final int TRANSIT_FRAGMENT_CLOSE = 8194;
    public static final int TRANSIT_FRAGMENT_FADE = 4099;
    public static final int TRANSIT_FRAGMENT_OPEN = 4097;
    public static final int TRANSIT_NONE = 0;
    public static final int TRANSIT_UNSET = -1;
    
    @NonNull
    public abstract FragmentTransaction add(@IdRes final int p0, @NonNull final Fragment p1);
    
    @NonNull
    public abstract FragmentTransaction add(@IdRes final int p0, @NonNull final Fragment p1, @Nullable final String p2);
    
    @NonNull
    public abstract FragmentTransaction add(@NonNull final Fragment p0, @Nullable final String p1);
    
    @NonNull
    public abstract FragmentTransaction addSharedElement(@NonNull final View p0, @NonNull final String p1);
    
    @NonNull
    public abstract FragmentTransaction addToBackStack(@Nullable final String p0);
    
    @NonNull
    public abstract FragmentTransaction attach(@NonNull final Fragment p0);
    
    public abstract int commit();
    
    public abstract int commitAllowingStateLoss();
    
    public abstract void commitNow();
    
    public abstract void commitNowAllowingStateLoss();
    
    @NonNull
    public abstract FragmentTransaction detach(@NonNull final Fragment p0);
    
    @NonNull
    public abstract FragmentTransaction disallowAddToBackStack();
    
    @NonNull
    public abstract FragmentTransaction hide(@NonNull final Fragment p0);
    
    public abstract boolean isAddToBackStackAllowed();
    
    public abstract boolean isEmpty();
    
    @NonNull
    public abstract FragmentTransaction remove(@NonNull final Fragment p0);
    
    @NonNull
    public abstract FragmentTransaction replace(@IdRes final int p0, @NonNull final Fragment p1);
    
    @NonNull
    public abstract FragmentTransaction replace(@IdRes final int p0, @NonNull final Fragment p1, @Nullable final String p2);
    
    @NonNull
    public abstract FragmentTransaction runOnCommit(@NonNull final Runnable p0);
    
    @Deprecated
    public abstract FragmentTransaction setAllowOptimization(final boolean p0);
    
    @NonNull
    public abstract FragmentTransaction setBreadCrumbShortTitle(@StringRes final int p0);
    
    @NonNull
    public abstract FragmentTransaction setBreadCrumbShortTitle(@Nullable final CharSequence p0);
    
    @NonNull
    public abstract FragmentTransaction setBreadCrumbTitle(@StringRes final int p0);
    
    @NonNull
    public abstract FragmentTransaction setBreadCrumbTitle(@Nullable final CharSequence p0);
    
    @NonNull
    public abstract FragmentTransaction setCustomAnimations(@AnimRes @AnimatorRes final int p0, @AnimRes @AnimatorRes final int p1);
    
    @NonNull
    public abstract FragmentTransaction setCustomAnimations(@AnimRes @AnimatorRes final int p0, @AnimRes @AnimatorRes final int p1, @AnimRes @AnimatorRes final int p2, @AnimRes @AnimatorRes final int p3);
    
    @NonNull
    public abstract FragmentTransaction setPrimaryNavigationFragment(@Nullable final Fragment p0);
    
    @NonNull
    public abstract FragmentTransaction setReorderingAllowed(final boolean p0);
    
    @NonNull
    public abstract FragmentTransaction setTransition(final int p0);
    
    @NonNull
    public abstract FragmentTransaction setTransitionStyle(@StyleRes final int p0);
    
    @NonNull
    public abstract FragmentTransaction show(@NonNull final Fragment p0);
}
