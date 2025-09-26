// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.app;

import android.os.Parcel;
import android.os.Parcelable$ClassLoaderCreator;
import android.os.Parcelable$Creator;
import android.support.v4.util.DebugUtils;
import android.os.Looper;
import android.content.IntentSender$SendIntentException;
import android.content.IntentSender;
import android.util.AttributeSet;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.ContextMenu$ContextMenuInfo;
import android.view.ContextMenu;
import android.view.animation.Animation;
import android.view.MenuItem;
import android.content.res.Configuration;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.CallSuper;
import android.arch.lifecycle.LiveData;
import android.support.annotation.MainThread;
import android.support.annotation.StringRes;
import android.content.res.Resources;
import android.arch.lifecycle.Lifecycle;
import android.support.annotation.RestrictTo;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.annotation.NonNull;
import android.animation.Animator;
import java.io.PrintWriter;
import java.io.FileDescriptor;
import java.lang.reflect.InvocationTargetException;
import android.content.Context;
import android.arch.lifecycle.ViewModelStore;
import android.arch.lifecycle.MutableLiveData;
import android.os.Parcelable;
import android.util.SparseArray;
import android.support.annotation.Nullable;
import android.arch.lifecycle.LifecycleRegistry;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.v4.util.SimpleArrayMap;
import android.arch.lifecycle.ViewModelStoreOwner;
import android.arch.lifecycle.LifecycleOwner;
import android.view.View$OnCreateContextMenuListener;
import android.content.ComponentCallbacks;

public class Fragment implements ComponentCallbacks, View$OnCreateContextMenuListener, LifecycleOwner, ViewModelStoreOwner
{
    static final int ACTIVITY_CREATED = 2;
    static final int CREATED = 1;
    static final int INITIALIZING = 0;
    static final int RESUMED = 4;
    static final int STARTED = 3;
    static final Object USE_DEFAULT_TRANSITION;
    private static final SimpleArrayMap<String, Class<?>> sClassMap;
    boolean mAdded;
    AnimationInfo mAnimationInfo;
    Bundle mArguments;
    int mBackStackNesting;
    boolean mCalled;
    FragmentManagerImpl mChildFragmentManager;
    FragmentManagerNonConfig mChildNonConfig;
    ViewGroup mContainer;
    int mContainerId;
    boolean mDeferStart;
    boolean mDetached;
    int mFragmentId;
    FragmentManagerImpl mFragmentManager;
    boolean mFromLayout;
    boolean mHasMenu;
    boolean mHidden;
    boolean mHiddenChanged;
    FragmentHostCallback mHost;
    boolean mInLayout;
    int mIndex;
    View mInnerView;
    boolean mIsCreated;
    boolean mIsNewlyAdded;
    LayoutInflater mLayoutInflater;
    LifecycleRegistry mLifecycleRegistry;
    boolean mMenuVisible;
    Fragment mParentFragment;
    boolean mPerformedCreateView;
    float mPostponedAlpha;
    boolean mRemoving;
    boolean mRestored;
    boolean mRetainInstance;
    boolean mRetaining;
    Bundle mSavedFragmentState;
    @Nullable
    Boolean mSavedUserVisibleHint;
    SparseArray<Parcelable> mSavedViewState;
    int mState;
    String mTag;
    Fragment mTarget;
    int mTargetIndex;
    int mTargetRequestCode;
    boolean mUserVisibleHint;
    View mView;
    LifecycleOwner mViewLifecycleOwner;
    MutableLiveData<LifecycleOwner> mViewLifecycleOwnerLiveData;
    LifecycleRegistry mViewLifecycleRegistry;
    ViewModelStore mViewModelStore;
    String mWho;
    
    static {
        sClassMap = new SimpleArrayMap<String, Class<?>>();
        USE_DEFAULT_TRANSITION = new Object();
    }
    
    public Fragment() {
        this.mState = 0;
        this.mIndex = -1;
        this.mTargetIndex = -1;
        this.mMenuVisible = true;
        this.mUserVisibleHint = true;
        this.mLifecycleRegistry = new LifecycleRegistry(this);
        this.mViewLifecycleOwnerLiveData = new MutableLiveData<LifecycleOwner>();
    }
    
    private AnimationInfo ensureAnimationInfo() {
        if (this.mAnimationInfo == null) {
            this.mAnimationInfo = new AnimationInfo();
        }
        return this.mAnimationInfo;
    }
    
    public static Fragment instantiate(final Context context, final String s) {
        return instantiate(context, s, null);
    }
    
    public static Fragment instantiate(final Context context, final String s, @Nullable final Bundle arguments) {
        try {
            Class<?> loadClass;
            if ((loadClass = Fragment.sClassMap.get(s)) == null) {
                loadClass = context.getClassLoader().loadClass(s);
                Fragment.sClassMap.put(s, loadClass);
            }
            final Fragment fragment = (Fragment)loadClass.getConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
            if (arguments != null) {
                arguments.setClassLoader(fragment.getClass().getClassLoader());
                fragment.setArguments(arguments);
            }
            return fragment;
        }
        catch (final InvocationTargetException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unable to instantiate fragment ");
            sb.append(s);
            sb.append(": calling Fragment constructor caused an exception");
            throw new InstantiationException(sb.toString(), ex);
        }
        catch (final NoSuchMethodException ex2) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Unable to instantiate fragment ");
            sb2.append(s);
            sb2.append(": could not find Fragment constructor");
            throw new InstantiationException(sb2.toString(), ex2);
        }
        catch (final IllegalAccessException ex3) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Unable to instantiate fragment ");
            sb3.append(s);
            sb3.append(": make sure class name exists, is public, and has an");
            sb3.append(" empty constructor that is public");
            throw new InstantiationException(sb3.toString(), ex3);
        }
        catch (final java.lang.InstantiationException ex4) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Unable to instantiate fragment ");
            sb4.append(s);
            sb4.append(": make sure class name exists, is public, and has an");
            sb4.append(" empty constructor that is public");
            throw new InstantiationException(sb4.toString(), ex4);
        }
        catch (final ClassNotFoundException ex5) {
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("Unable to instantiate fragment ");
            sb5.append(s);
            sb5.append(": make sure class name exists, is public, and has an");
            sb5.append(" empty constructor that is public");
            throw new InstantiationException(sb5.toString(), ex5);
        }
    }
    
    static boolean isSupportFragmentClass(final Context context, final String name) {
        try {
            Class<?> loadClass;
            if ((loadClass = Fragment.sClassMap.get(name)) == null) {
                loadClass = context.getClassLoader().loadClass(name);
                Fragment.sClassMap.put(name, loadClass);
            }
            return Fragment.class.isAssignableFrom(loadClass);
        }
        catch (final ClassNotFoundException ex) {
            return false;
        }
    }
    
    void callStartTransitionListener() {
        Object mStartEnterTransitionListener;
        if (this.mAnimationInfo == null) {
            mStartEnterTransitionListener = null;
        }
        else {
            this.mAnimationInfo.mEnterTransitionPostponed = false;
            mStartEnterTransitionListener = this.mAnimationInfo.mStartEnterTransitionListener;
            this.mAnimationInfo.mStartEnterTransitionListener = null;
        }
        if (mStartEnterTransitionListener != null) {
            ((OnStartEnterTransitionListener)mStartEnterTransitionListener).onStartEnterTransition();
        }
    }
    
    public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
        printWriter.print(s);
        printWriter.print("mFragmentId=#");
        printWriter.print(Integer.toHexString(this.mFragmentId));
        printWriter.print(" mContainerId=#");
        printWriter.print(Integer.toHexString(this.mContainerId));
        printWriter.print(" mTag=");
        printWriter.println(this.mTag);
        printWriter.print(s);
        printWriter.print("mState=");
        printWriter.print(this.mState);
        printWriter.print(" mIndex=");
        printWriter.print(this.mIndex);
        printWriter.print(" mWho=");
        printWriter.print(this.mWho);
        printWriter.print(" mBackStackNesting=");
        printWriter.println(this.mBackStackNesting);
        printWriter.print(s);
        printWriter.print("mAdded=");
        printWriter.print(this.mAdded);
        printWriter.print(" mRemoving=");
        printWriter.print(this.mRemoving);
        printWriter.print(" mFromLayout=");
        printWriter.print(this.mFromLayout);
        printWriter.print(" mInLayout=");
        printWriter.println(this.mInLayout);
        printWriter.print(s);
        printWriter.print("mHidden=");
        printWriter.print(this.mHidden);
        printWriter.print(" mDetached=");
        printWriter.print(this.mDetached);
        printWriter.print(" mMenuVisible=");
        printWriter.print(this.mMenuVisible);
        printWriter.print(" mHasMenu=");
        printWriter.println(this.mHasMenu);
        printWriter.print(s);
        printWriter.print("mRetainInstance=");
        printWriter.print(this.mRetainInstance);
        printWriter.print(" mRetaining=");
        printWriter.print(this.mRetaining);
        printWriter.print(" mUserVisibleHint=");
        printWriter.println(this.mUserVisibleHint);
        if (this.mFragmentManager != null) {
            printWriter.print(s);
            printWriter.print("mFragmentManager=");
            printWriter.println(this.mFragmentManager);
        }
        if (this.mHost != null) {
            printWriter.print(s);
            printWriter.print("mHost=");
            printWriter.println(this.mHost);
        }
        if (this.mParentFragment != null) {
            printWriter.print(s);
            printWriter.print("mParentFragment=");
            printWriter.println(this.mParentFragment);
        }
        if (this.mArguments != null) {
            printWriter.print(s);
            printWriter.print("mArguments=");
            printWriter.println(this.mArguments);
        }
        if (this.mSavedFragmentState != null) {
            printWriter.print(s);
            printWriter.print("mSavedFragmentState=");
            printWriter.println(this.mSavedFragmentState);
        }
        if (this.mSavedViewState != null) {
            printWriter.print(s);
            printWriter.print("mSavedViewState=");
            printWriter.println(this.mSavedViewState);
        }
        if (this.mTarget != null) {
            printWriter.print(s);
            printWriter.print("mTarget=");
            printWriter.print(this.mTarget);
            printWriter.print(" mTargetRequestCode=");
            printWriter.println(this.mTargetRequestCode);
        }
        if (this.getNextAnim() != 0) {
            printWriter.print(s);
            printWriter.print("mNextAnim=");
            printWriter.println(this.getNextAnim());
        }
        if (this.mContainer != null) {
            printWriter.print(s);
            printWriter.print("mContainer=");
            printWriter.println(this.mContainer);
        }
        if (this.mView != null) {
            printWriter.print(s);
            printWriter.print("mView=");
            printWriter.println(this.mView);
        }
        if (this.mInnerView != null) {
            printWriter.print(s);
            printWriter.print("mInnerView=");
            printWriter.println(this.mView);
        }
        if (this.getAnimatingAway() != null) {
            printWriter.print(s);
            printWriter.print("mAnimatingAway=");
            printWriter.println(this.getAnimatingAway());
            printWriter.print(s);
            printWriter.print("mStateAfterAnimating=");
            printWriter.println(this.getStateAfterAnimating());
        }
        if (this.getContext() != null) {
            LoaderManager.getInstance(this).dump(s, fileDescriptor, printWriter, array);
        }
        if (this.mChildFragmentManager != null) {
            printWriter.print(s);
            final StringBuilder sb = new StringBuilder();
            sb.append("Child ");
            sb.append(this.mChildFragmentManager);
            sb.append(":");
            printWriter.println(sb.toString());
            final FragmentManagerImpl mChildFragmentManager = this.mChildFragmentManager;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append("  ");
            mChildFragmentManager.dump(sb2.toString(), fileDescriptor, printWriter, array);
        }
    }
    
    @Override
    public final boolean equals(final Object obj) {
        return super.equals(obj);
    }
    
    Fragment findFragmentByWho(final String s) {
        if (s.equals(this.mWho)) {
            return this;
        }
        if (this.mChildFragmentManager != null) {
            return this.mChildFragmentManager.findFragmentByWho(s);
        }
        return null;
    }
    
    @Nullable
    public final FragmentActivity getActivity() {
        FragmentActivity fragmentActivity;
        if (this.mHost == null) {
            fragmentActivity = null;
        }
        else {
            fragmentActivity = (FragmentActivity)this.mHost.getActivity();
        }
        return fragmentActivity;
    }
    
    public boolean getAllowEnterTransitionOverlap() {
        return this.mAnimationInfo == null || this.mAnimationInfo.mAllowEnterTransitionOverlap == null || this.mAnimationInfo.mAllowEnterTransitionOverlap;
    }
    
    public boolean getAllowReturnTransitionOverlap() {
        return this.mAnimationInfo == null || this.mAnimationInfo.mAllowReturnTransitionOverlap == null || this.mAnimationInfo.mAllowReturnTransitionOverlap;
    }
    
    View getAnimatingAway() {
        if (this.mAnimationInfo == null) {
            return null;
        }
        return this.mAnimationInfo.mAnimatingAway;
    }
    
    Animator getAnimator() {
        if (this.mAnimationInfo == null) {
            return null;
        }
        return this.mAnimationInfo.mAnimator;
    }
    
    @Nullable
    public final Bundle getArguments() {
        return this.mArguments;
    }
    
    @NonNull
    public final FragmentManager getChildFragmentManager() {
        if (this.mChildFragmentManager == null) {
            this.instantiateChildFragmentManager();
            if (this.mState >= 4) {
                this.mChildFragmentManager.dispatchResume();
            }
            else if (this.mState >= 3) {
                this.mChildFragmentManager.dispatchStart();
            }
            else if (this.mState >= 2) {
                this.mChildFragmentManager.dispatchActivityCreated();
            }
            else if (this.mState >= 1) {
                this.mChildFragmentManager.dispatchCreate();
            }
        }
        return this.mChildFragmentManager;
    }
    
    @Nullable
    public Context getContext() {
        Context context;
        if (this.mHost == null) {
            context = null;
        }
        else {
            context = this.mHost.getContext();
        }
        return context;
    }
    
    @Nullable
    public Object getEnterTransition() {
        if (this.mAnimationInfo == null) {
            return null;
        }
        return this.mAnimationInfo.mEnterTransition;
    }
    
    SharedElementCallback getEnterTransitionCallback() {
        if (this.mAnimationInfo == null) {
            return null;
        }
        return this.mAnimationInfo.mEnterTransitionCallback;
    }
    
    @Nullable
    public Object getExitTransition() {
        if (this.mAnimationInfo == null) {
            return null;
        }
        return this.mAnimationInfo.mExitTransition;
    }
    
    SharedElementCallback getExitTransitionCallback() {
        if (this.mAnimationInfo == null) {
            return null;
        }
        return this.mAnimationInfo.mExitTransitionCallback;
    }
    
    @Nullable
    public final FragmentManager getFragmentManager() {
        return this.mFragmentManager;
    }
    
    @Nullable
    public final Object getHost() {
        Object onGetHost;
        if (this.mHost == null) {
            onGetHost = null;
        }
        else {
            onGetHost = this.mHost.onGetHost();
        }
        return onGetHost;
    }
    
    public final int getId() {
        return this.mFragmentId;
    }
    
    public final LayoutInflater getLayoutInflater() {
        if (this.mLayoutInflater == null) {
            return this.performGetLayoutInflater(null);
        }
        return this.mLayoutInflater;
    }
    
    @Deprecated
    @NonNull
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public LayoutInflater getLayoutInflater(@Nullable final Bundle bundle) {
        if (this.mHost == null) {
            throw new IllegalStateException("onGetLayoutInflater() cannot be executed until the Fragment is attached to the FragmentManager.");
        }
        final LayoutInflater onGetLayoutInflater = this.mHost.onGetLayoutInflater();
        this.getChildFragmentManager();
        LayoutInflaterCompat.setFactory2(onGetLayoutInflater, this.mChildFragmentManager.getLayoutInflaterFactory());
        return onGetLayoutInflater;
    }
    
    public Lifecycle getLifecycle() {
        return this.mLifecycleRegistry;
    }
    
    @Deprecated
    public LoaderManager getLoaderManager() {
        return LoaderManager.getInstance(this);
    }
    
    int getNextAnim() {
        if (this.mAnimationInfo == null) {
            return 0;
        }
        return this.mAnimationInfo.mNextAnim;
    }
    
    int getNextTransition() {
        if (this.mAnimationInfo == null) {
            return 0;
        }
        return this.mAnimationInfo.mNextTransition;
    }
    
    int getNextTransitionStyle() {
        if (this.mAnimationInfo == null) {
            return 0;
        }
        return this.mAnimationInfo.mNextTransitionStyle;
    }
    
    @Nullable
    public final Fragment getParentFragment() {
        return this.mParentFragment;
    }
    
    public Object getReenterTransition() {
        if (this.mAnimationInfo == null) {
            return null;
        }
        Object o;
        if (this.mAnimationInfo.mReenterTransition == Fragment.USE_DEFAULT_TRANSITION) {
            o = this.getExitTransition();
        }
        else {
            o = this.mAnimationInfo.mReenterTransition;
        }
        return o;
    }
    
    @NonNull
    public final Resources getResources() {
        return this.requireContext().getResources();
    }
    
    public final boolean getRetainInstance() {
        return this.mRetainInstance;
    }
    
    @Nullable
    public Object getReturnTransition() {
        if (this.mAnimationInfo == null) {
            return null;
        }
        Object o;
        if (this.mAnimationInfo.mReturnTransition == Fragment.USE_DEFAULT_TRANSITION) {
            o = this.getEnterTransition();
        }
        else {
            o = this.mAnimationInfo.mReturnTransition;
        }
        return o;
    }
    
    @Nullable
    public Object getSharedElementEnterTransition() {
        if (this.mAnimationInfo == null) {
            return null;
        }
        return this.mAnimationInfo.mSharedElementEnterTransition;
    }
    
    @Nullable
    public Object getSharedElementReturnTransition() {
        if (this.mAnimationInfo == null) {
            return null;
        }
        Object o;
        if (this.mAnimationInfo.mSharedElementReturnTransition == Fragment.USE_DEFAULT_TRANSITION) {
            o = this.getSharedElementEnterTransition();
        }
        else {
            o = this.mAnimationInfo.mSharedElementReturnTransition;
        }
        return o;
    }
    
    int getStateAfterAnimating() {
        if (this.mAnimationInfo == null) {
            return 0;
        }
        return this.mAnimationInfo.mStateAfterAnimating;
    }
    
    @NonNull
    public final String getString(@StringRes final int n) {
        return this.getResources().getString(n);
    }
    
    @NonNull
    public final String getString(@StringRes final int n, final Object... array) {
        return this.getResources().getString(n, array);
    }
    
    @Nullable
    public final String getTag() {
        return this.mTag;
    }
    
    @Nullable
    public final Fragment getTargetFragment() {
        return this.mTarget;
    }
    
    public final int getTargetRequestCode() {
        return this.mTargetRequestCode;
    }
    
    @NonNull
    public final CharSequence getText(@StringRes final int n) {
        return this.getResources().getText(n);
    }
    
    public boolean getUserVisibleHint() {
        return this.mUserVisibleHint;
    }
    
    @Nullable
    public View getView() {
        return this.mView;
    }
    
    @MainThread
    @NonNull
    public LifecycleOwner getViewLifecycleOwner() {
        if (this.mViewLifecycleOwner == null) {
            throw new IllegalStateException("Can't access the Fragment View's LifecycleOwner when getView() is null i.e., before onCreateView() or after onDestroyView()");
        }
        return this.mViewLifecycleOwner;
    }
    
    @NonNull
    public LiveData<LifecycleOwner> getViewLifecycleOwnerLiveData() {
        return this.mViewLifecycleOwnerLiveData;
    }
    
    @NonNull
    public ViewModelStore getViewModelStore() {
        if (this.getContext() == null) {
            throw new IllegalStateException("Can't access ViewModels from detached fragment");
        }
        if (this.mViewModelStore == null) {
            this.mViewModelStore = new ViewModelStore();
        }
        return this.mViewModelStore;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public final boolean hasOptionsMenu() {
        return this.mHasMenu;
    }
    
    @Override
    public final int hashCode() {
        return super.hashCode();
    }
    
    void initState() {
        this.mIndex = -1;
        this.mWho = null;
        this.mAdded = false;
        this.mRemoving = false;
        this.mFromLayout = false;
        this.mInLayout = false;
        this.mRestored = false;
        this.mBackStackNesting = 0;
        this.mFragmentManager = null;
        this.mChildFragmentManager = null;
        this.mHost = null;
        this.mFragmentId = 0;
        this.mContainerId = 0;
        this.mTag = null;
        this.mHidden = false;
        this.mDetached = false;
        this.mRetaining = false;
    }
    
    void instantiateChildFragmentManager() {
        if (this.mHost == null) {
            throw new IllegalStateException("Fragment has not been attached yet.");
        }
        (this.mChildFragmentManager = new FragmentManagerImpl()).attachController(this.mHost, new FragmentContainer(this) {
            final Fragment this$0;
            
            @Override
            public Fragment instantiate(final Context context, final String s, final Bundle bundle) {
                return this.this$0.mHost.instantiate(context, s, bundle);
            }
            
            @Nullable
            @Override
            public View onFindViewById(final int n) {
                if (this.this$0.mView == null) {
                    throw new IllegalStateException("Fragment does not have a view");
                }
                return this.this$0.mView.findViewById(n);
            }
            
            @Override
            public boolean onHasView() {
                return this.this$0.mView != null;
            }
        }, this);
    }
    
    public final boolean isAdded() {
        return this.mHost != null && this.mAdded;
    }
    
    public final boolean isDetached() {
        return this.mDetached;
    }
    
    public final boolean isHidden() {
        return this.mHidden;
    }
    
    boolean isHideReplaced() {
        return this.mAnimationInfo != null && this.mAnimationInfo.mIsHideReplaced;
    }
    
    final boolean isInBackStack() {
        return this.mBackStackNesting > 0;
    }
    
    public final boolean isInLayout() {
        return this.mInLayout;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public final boolean isMenuVisible() {
        return this.mMenuVisible;
    }
    
    boolean isPostponed() {
        return this.mAnimationInfo != null && this.mAnimationInfo.mEnterTransitionPostponed;
    }
    
    public final boolean isRemoving() {
        return this.mRemoving;
    }
    
    public final boolean isResumed() {
        return this.mState >= 4;
    }
    
    public final boolean isStateSaved() {
        return this.mFragmentManager != null && this.mFragmentManager.isStateSaved();
    }
    
    public final boolean isVisible() {
        return this.isAdded() && !this.isHidden() && this.mView != null && this.mView.getWindowToken() != null && this.mView.getVisibility() == 0;
    }
    
    void noteStateNotSaved() {
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.noteStateNotSaved();
        }
    }
    
    @CallSuper
    public void onActivityCreated(@Nullable final Bundle bundle) {
        this.mCalled = true;
    }
    
    public void onActivityResult(final int n, final int n2, final Intent intent) {
    }
    
    @Deprecated
    @CallSuper
    public void onAttach(final Activity activity) {
        this.mCalled = true;
    }
    
    @CallSuper
    public void onAttach(final Context context) {
        this.mCalled = true;
        Activity activity;
        if (this.mHost == null) {
            activity = null;
        }
        else {
            activity = this.mHost.getActivity();
        }
        if (activity != null) {
            this.mCalled = false;
            this.onAttach(activity);
        }
    }
    
    public void onAttachFragment(final Fragment fragment) {
    }
    
    @CallSuper
    public void onConfigurationChanged(final Configuration configuration) {
        this.mCalled = true;
    }
    
    public boolean onContextItemSelected(final MenuItem menuItem) {
        return false;
    }
    
    @CallSuper
    public void onCreate(@Nullable final Bundle bundle) {
        this.mCalled = true;
        this.restoreChildFragmentState(bundle);
        if (this.mChildFragmentManager != null && !this.mChildFragmentManager.isStateAtLeast(1)) {
            this.mChildFragmentManager.dispatchCreate();
        }
    }
    
    public Animation onCreateAnimation(final int n, final boolean b, final int n2) {
        return null;
    }
    
    public Animator onCreateAnimator(final int n, final boolean b, final int n2) {
        return null;
    }
    
    public void onCreateContextMenu(final ContextMenu contextMenu, final View view, final ContextMenu$ContextMenuInfo contextMenu$ContextMenuInfo) {
        this.getActivity().onCreateContextMenu(contextMenu, view, contextMenu$ContextMenuInfo);
    }
    
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater menuInflater) {
    }
    
    @Nullable
    public View onCreateView(@NonNull final LayoutInflater layoutInflater, @Nullable final ViewGroup viewGroup, @Nullable final Bundle bundle) {
        return null;
    }
    
    @CallSuper
    public void onDestroy() {
        boolean b = true;
        this.mCalled = true;
        final FragmentActivity activity = this.getActivity();
        if (activity == null || !activity.isChangingConfigurations()) {
            b = false;
        }
        if (this.mViewModelStore != null && !b) {
            this.mViewModelStore.clear();
        }
    }
    
    public void onDestroyOptionsMenu() {
    }
    
    @CallSuper
    public void onDestroyView() {
        this.mCalled = true;
    }
    
    @CallSuper
    public void onDetach() {
        this.mCalled = true;
    }
    
    @NonNull
    public LayoutInflater onGetLayoutInflater(@Nullable final Bundle bundle) {
        return this.getLayoutInflater(bundle);
    }
    
    public void onHiddenChanged(final boolean b) {
    }
    
    @Deprecated
    @CallSuper
    public void onInflate(final Activity activity, final AttributeSet set, final Bundle bundle) {
        this.mCalled = true;
    }
    
    @CallSuper
    public void onInflate(final Context context, final AttributeSet set, final Bundle bundle) {
        this.mCalled = true;
        Activity activity;
        if (this.mHost == null) {
            activity = null;
        }
        else {
            activity = this.mHost.getActivity();
        }
        if (activity != null) {
            this.mCalled = false;
            this.onInflate(activity, set, bundle);
        }
    }
    
    @CallSuper
    public void onLowMemory() {
        this.mCalled = true;
    }
    
    public void onMultiWindowModeChanged(final boolean b) {
    }
    
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        return false;
    }
    
    public void onOptionsMenuClosed(final Menu menu) {
    }
    
    @CallSuper
    public void onPause() {
        this.mCalled = true;
    }
    
    public void onPictureInPictureModeChanged(final boolean b) {
    }
    
    public void onPrepareOptionsMenu(final Menu menu) {
    }
    
    public void onRequestPermissionsResult(final int n, @NonNull final String[] array, @NonNull final int[] array2) {
    }
    
    @CallSuper
    public void onResume() {
        this.mCalled = true;
    }
    
    public void onSaveInstanceState(@NonNull final Bundle bundle) {
    }
    
    @CallSuper
    public void onStart() {
        this.mCalled = true;
    }
    
    @CallSuper
    public void onStop() {
        this.mCalled = true;
    }
    
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle bundle) {
    }
    
    @CallSuper
    public void onViewStateRestored(@Nullable final Bundle bundle) {
        this.mCalled = true;
    }
    
    @Nullable
    FragmentManager peekChildFragmentManager() {
        return this.mChildFragmentManager;
    }
    
    void performActivityCreated(final Bundle bundle) {
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.noteStateNotSaved();
        }
        this.mState = 2;
        this.mCalled = false;
        this.onActivityCreated(bundle);
        if (!this.mCalled) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(this);
            sb.append(" did not call through to super.onActivityCreated()");
            throw new SuperNotCalledException(sb.toString());
        }
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchActivityCreated();
        }
    }
    
    void performConfigurationChanged(final Configuration configuration) {
        this.onConfigurationChanged(configuration);
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchConfigurationChanged(configuration);
        }
    }
    
    boolean performContextItemSelected(final MenuItem menuItem) {
        if (!this.mHidden) {
            if (this.onContextItemSelected(menuItem)) {
                return true;
            }
            if (this.mChildFragmentManager != null && this.mChildFragmentManager.dispatchContextItemSelected(menuItem)) {
                return true;
            }
        }
        return false;
    }
    
    void performCreate(final Bundle bundle) {
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.noteStateNotSaved();
        }
        this.mState = 1;
        this.mCalled = false;
        this.onCreate(bundle);
        this.mIsCreated = true;
        if (!this.mCalled) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(this);
            sb.append(" did not call through to super.onCreate()");
            throw new SuperNotCalledException(sb.toString());
        }
        this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
    }
    
    boolean performCreateOptionsMenu(final Menu menu, final MenuInflater menuInflater) {
        final boolean mHidden = this.mHidden;
        int n = 0;
        final boolean b = false;
        if (!mHidden) {
            int n2 = b ? 1 : 0;
            if (this.mHasMenu) {
                n2 = (b ? 1 : 0);
                if (this.mMenuVisible) {
                    n2 = 1;
                    this.onCreateOptionsMenu(menu, menuInflater);
                }
            }
            n = n2;
            if (this.mChildFragmentManager != null) {
                n = (n2 | (this.mChildFragmentManager.dispatchCreateOptionsMenu(menu, menuInflater) ? 1 : 0));
            }
        }
        return n != 0;
    }
    
    void performCreateView(@NonNull final LayoutInflater layoutInflater, @Nullable final ViewGroup viewGroup, @Nullable final Bundle bundle) {
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.noteStateNotSaved();
        }
        this.mPerformedCreateView = true;
        this.mViewLifecycleOwner = new LifecycleOwner(this) {
            final Fragment this$0;
            
            @Override
            public Lifecycle getLifecycle() {
                if (this.this$0.mViewLifecycleRegistry == null) {
                    this.this$0.mViewLifecycleRegistry = new LifecycleRegistry(this.this$0.mViewLifecycleOwner);
                }
                return this.this$0.mViewLifecycleRegistry;
            }
        };
        this.mViewLifecycleRegistry = null;
        this.mView = this.onCreateView(layoutInflater, viewGroup, bundle);
        if (this.mView != null) {
            this.mViewLifecycleOwner.getLifecycle();
            this.mViewLifecycleOwnerLiveData.setValue(this.mViewLifecycleOwner);
        }
        else {
            if (this.mViewLifecycleRegistry != null) {
                throw new IllegalStateException("Called getViewLifecycleOwner() but onCreateView() returned null");
            }
            this.mViewLifecycleOwner = null;
        }
    }
    
    void performDestroy() {
        this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchDestroy();
        }
        this.mState = 0;
        this.mCalled = false;
        this.mIsCreated = false;
        this.onDestroy();
        if (!this.mCalled) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(this);
            sb.append(" did not call through to super.onDestroy()");
            throw new SuperNotCalledException(sb.toString());
        }
        this.mChildFragmentManager = null;
    }
    
    void performDestroyView() {
        if (this.mView != null) {
            this.mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        }
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchDestroyView();
        }
        this.mState = 1;
        this.mCalled = false;
        this.onDestroyView();
        if (!this.mCalled) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(this);
            sb.append(" did not call through to super.onDestroyView()");
            throw new SuperNotCalledException(sb.toString());
        }
        LoaderManager.getInstance(this).markForRedelivery();
        this.mPerformedCreateView = false;
    }
    
    void performDetach() {
        this.mCalled = false;
        this.onDetach();
        this.mLayoutInflater = null;
        if (!this.mCalled) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(this);
            sb.append(" did not call through to super.onDetach()");
            throw new SuperNotCalledException(sb.toString());
        }
        if (this.mChildFragmentManager != null) {
            if (!this.mRetaining) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Child FragmentManager of ");
                sb2.append(this);
                sb2.append(" was not ");
                sb2.append(" destroyed and this fragment is not retaining instance");
                throw new IllegalStateException(sb2.toString());
            }
            this.mChildFragmentManager.dispatchDestroy();
            this.mChildFragmentManager = null;
        }
    }
    
    @NonNull
    LayoutInflater performGetLayoutInflater(@Nullable final Bundle bundle) {
        return this.mLayoutInflater = this.onGetLayoutInflater(bundle);
    }
    
    void performLowMemory() {
        this.onLowMemory();
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchLowMemory();
        }
    }
    
    void performMultiWindowModeChanged(final boolean b) {
        this.onMultiWindowModeChanged(b);
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchMultiWindowModeChanged(b);
        }
    }
    
    boolean performOptionsItemSelected(final MenuItem menuItem) {
        if (!this.mHidden) {
            if (this.mHasMenu && this.mMenuVisible && this.onOptionsItemSelected(menuItem)) {
                return true;
            }
            if (this.mChildFragmentManager != null && this.mChildFragmentManager.dispatchOptionsItemSelected(menuItem)) {
                return true;
            }
        }
        return false;
    }
    
    void performOptionsMenuClosed(final Menu menu) {
        if (!this.mHidden) {
            if (this.mHasMenu && this.mMenuVisible) {
                this.onOptionsMenuClosed(menu);
            }
            if (this.mChildFragmentManager != null) {
                this.mChildFragmentManager.dispatchOptionsMenuClosed(menu);
            }
        }
    }
    
    void performPause() {
        if (this.mView != null) {
            this.mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
        }
        this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchPause();
        }
        this.mState = 3;
        this.mCalled = false;
        this.onPause();
        if (!this.mCalled) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(this);
            sb.append(" did not call through to super.onPause()");
            throw new SuperNotCalledException(sb.toString());
        }
    }
    
    void performPictureInPictureModeChanged(final boolean b) {
        this.onPictureInPictureModeChanged(b);
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchPictureInPictureModeChanged(b);
        }
    }
    
    boolean performPrepareOptionsMenu(final Menu menu) {
        final boolean mHidden = this.mHidden;
        int n = 0;
        final boolean b = false;
        if (!mHidden) {
            int n2 = b ? 1 : 0;
            if (this.mHasMenu) {
                n2 = (b ? 1 : 0);
                if (this.mMenuVisible) {
                    n2 = 1;
                    this.onPrepareOptionsMenu(menu);
                }
            }
            n = n2;
            if (this.mChildFragmentManager != null) {
                n = (n2 | (this.mChildFragmentManager.dispatchPrepareOptionsMenu(menu) ? 1 : 0));
            }
        }
        return n != 0;
    }
    
    void performResume() {
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.noteStateNotSaved();
            this.mChildFragmentManager.execPendingActions();
        }
        this.mState = 4;
        this.mCalled = false;
        this.onResume();
        if (!this.mCalled) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(this);
            sb.append(" did not call through to super.onResume()");
            throw new SuperNotCalledException(sb.toString());
        }
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchResume();
            this.mChildFragmentManager.execPendingActions();
        }
        this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
        if (this.mView != null) {
            this.mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
        }
    }
    
    void performSaveInstanceState(final Bundle bundle) {
        this.onSaveInstanceState(bundle);
        if (this.mChildFragmentManager != null) {
            final Parcelable saveAllState = this.mChildFragmentManager.saveAllState();
            if (saveAllState != null) {
                bundle.putParcelable("android:support:fragments", saveAllState);
            }
        }
    }
    
    void performStart() {
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.noteStateNotSaved();
            this.mChildFragmentManager.execPendingActions();
        }
        this.mState = 3;
        this.mCalled = false;
        this.onStart();
        if (!this.mCalled) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(this);
            sb.append(" did not call through to super.onStart()");
            throw new SuperNotCalledException(sb.toString());
        }
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchStart();
        }
        this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
        if (this.mView != null) {
            this.mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
        }
    }
    
    void performStop() {
        if (this.mView != null) {
            this.mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        }
        this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchStop();
        }
        this.mState = 2;
        this.mCalled = false;
        this.onStop();
        if (!this.mCalled) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(this);
            sb.append(" did not call through to super.onStop()");
            throw new SuperNotCalledException(sb.toString());
        }
    }
    
    public void postponeEnterTransition() {
        this.ensureAnimationInfo().mEnterTransitionPostponed = true;
    }
    
    public void registerForContextMenu(final View view) {
        view.setOnCreateContextMenuListener((View$OnCreateContextMenuListener)this);
    }
    
    public final void requestPermissions(@NonNull final String[] array, final int n) {
        if (this.mHost == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(this);
            sb.append(" not attached to Activity");
            throw new IllegalStateException(sb.toString());
        }
        this.mHost.onRequestPermissionsFromFragment(this, array, n);
    }
    
    @NonNull
    public final FragmentActivity requireActivity() {
        final FragmentActivity activity = this.getActivity();
        if (activity == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(this);
            sb.append(" not attached to an activity.");
            throw new IllegalStateException(sb.toString());
        }
        return activity;
    }
    
    @NonNull
    public final Context requireContext() {
        final Context context = this.getContext();
        if (context == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(this);
            sb.append(" not attached to a context.");
            throw new IllegalStateException(sb.toString());
        }
        return context;
    }
    
    @NonNull
    public final FragmentManager requireFragmentManager() {
        final FragmentManager fragmentManager = this.getFragmentManager();
        if (fragmentManager == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(this);
            sb.append(" not associated with a fragment manager.");
            throw new IllegalStateException(sb.toString());
        }
        return fragmentManager;
    }
    
    @NonNull
    public final Object requireHost() {
        final Object host = this.getHost();
        if (host == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(this);
            sb.append(" not attached to a host.");
            throw new IllegalStateException(sb.toString());
        }
        return host;
    }
    
    void restoreChildFragmentState(@Nullable final Bundle bundle) {
        if (bundle != null) {
            final Parcelable parcelable = bundle.getParcelable("android:support:fragments");
            if (parcelable != null) {
                if (this.mChildFragmentManager == null) {
                    this.instantiateChildFragmentManager();
                }
                this.mChildFragmentManager.restoreAllState(parcelable, this.mChildNonConfig);
                this.mChildNonConfig = null;
                this.mChildFragmentManager.dispatchCreate();
            }
        }
    }
    
    final void restoreViewState(final Bundle bundle) {
        if (this.mSavedViewState != null) {
            this.mInnerView.restoreHierarchyState((SparseArray)this.mSavedViewState);
            this.mSavedViewState = null;
        }
        this.mCalled = false;
        this.onViewStateRestored(bundle);
        if (!this.mCalled) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(this);
            sb.append(" did not call through to super.onViewStateRestored()");
            throw new SuperNotCalledException(sb.toString());
        }
        if (this.mView != null) {
            this.mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        }
    }
    
    public void setAllowEnterTransitionOverlap(final boolean b) {
        this.ensureAnimationInfo().mAllowEnterTransitionOverlap = b;
    }
    
    public void setAllowReturnTransitionOverlap(final boolean b) {
        this.ensureAnimationInfo().mAllowReturnTransitionOverlap = b;
    }
    
    void setAnimatingAway(final View mAnimatingAway) {
        this.ensureAnimationInfo().mAnimatingAway = mAnimatingAway;
    }
    
    void setAnimator(final Animator mAnimator) {
        this.ensureAnimationInfo().mAnimator = mAnimator;
    }
    
    public void setArguments(@Nullable final Bundle mArguments) {
        if (this.mIndex >= 0 && this.isStateSaved()) {
            throw new IllegalStateException("Fragment already active and state has been saved");
        }
        this.mArguments = mArguments;
    }
    
    public void setEnterSharedElementCallback(final SharedElementCallback mEnterTransitionCallback) {
        this.ensureAnimationInfo().mEnterTransitionCallback = mEnterTransitionCallback;
    }
    
    public void setEnterTransition(@Nullable final Object mEnterTransition) {
        this.ensureAnimationInfo().mEnterTransition = mEnterTransition;
    }
    
    public void setExitSharedElementCallback(final SharedElementCallback mExitTransitionCallback) {
        this.ensureAnimationInfo().mExitTransitionCallback = mExitTransitionCallback;
    }
    
    public void setExitTransition(@Nullable final Object mExitTransition) {
        this.ensureAnimationInfo().mExitTransition = mExitTransition;
    }
    
    public void setHasOptionsMenu(final boolean mHasMenu) {
        if (this.mHasMenu != mHasMenu) {
            this.mHasMenu = mHasMenu;
            if (this.isAdded() && !this.isHidden()) {
                this.mHost.onSupportInvalidateOptionsMenu();
            }
        }
    }
    
    void setHideReplaced(final boolean mIsHideReplaced) {
        this.ensureAnimationInfo().mIsHideReplaced = mIsHideReplaced;
    }
    
    final void setIndex(final int mIndex, final Fragment fragment) {
        this.mIndex = mIndex;
        if (fragment != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(fragment.mWho);
            sb.append(":");
            sb.append(this.mIndex);
            this.mWho = sb.toString();
        }
        else {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("android:fragment:");
            sb2.append(this.mIndex);
            this.mWho = sb2.toString();
        }
    }
    
    public void setInitialSavedState(@Nullable final SavedState savedState) {
        if (this.mIndex >= 0) {
            throw new IllegalStateException("Fragment already active");
        }
        Bundle mState;
        if (savedState != null && savedState.mState != null) {
            mState = savedState.mState;
        }
        else {
            mState = null;
        }
        this.mSavedFragmentState = mState;
    }
    
    public void setMenuVisibility(final boolean mMenuVisible) {
        if (this.mMenuVisible != mMenuVisible) {
            this.mMenuVisible = mMenuVisible;
            if (this.mHasMenu && this.isAdded() && !this.isHidden()) {
                this.mHost.onSupportInvalidateOptionsMenu();
            }
        }
    }
    
    void setNextAnim(final int mNextAnim) {
        if (this.mAnimationInfo == null && mNextAnim == 0) {
            return;
        }
        this.ensureAnimationInfo().mNextAnim = mNextAnim;
    }
    
    void setNextTransition(final int mNextTransition, final int mNextTransitionStyle) {
        if (this.mAnimationInfo == null && mNextTransition == 0 && mNextTransitionStyle == 0) {
            return;
        }
        this.ensureAnimationInfo();
        this.mAnimationInfo.mNextTransition = mNextTransition;
        this.mAnimationInfo.mNextTransitionStyle = mNextTransitionStyle;
    }
    
    void setOnStartEnterTransitionListener(final OnStartEnterTransitionListener mStartEnterTransitionListener) {
        this.ensureAnimationInfo();
        if (mStartEnterTransitionListener == this.mAnimationInfo.mStartEnterTransitionListener) {
            return;
        }
        if (mStartEnterTransitionListener != null && this.mAnimationInfo.mStartEnterTransitionListener != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Trying to set a replacement startPostponedEnterTransition on ");
            sb.append(this);
            throw new IllegalStateException(sb.toString());
        }
        if (this.mAnimationInfo.mEnterTransitionPostponed) {
            this.mAnimationInfo.mStartEnterTransitionListener = mStartEnterTransitionListener;
        }
        if (mStartEnterTransitionListener != null) {
            mStartEnterTransitionListener.startListening();
        }
    }
    
    public void setReenterTransition(@Nullable final Object mReenterTransition) {
        this.ensureAnimationInfo().mReenterTransition = mReenterTransition;
    }
    
    public void setRetainInstance(final boolean mRetainInstance) {
        this.mRetainInstance = mRetainInstance;
    }
    
    public void setReturnTransition(@Nullable final Object mReturnTransition) {
        this.ensureAnimationInfo().mReturnTransition = mReturnTransition;
    }
    
    public void setSharedElementEnterTransition(@Nullable final Object mSharedElementEnterTransition) {
        this.ensureAnimationInfo().mSharedElementEnterTransition = mSharedElementEnterTransition;
    }
    
    public void setSharedElementReturnTransition(@Nullable final Object mSharedElementReturnTransition) {
        this.ensureAnimationInfo().mSharedElementReturnTransition = mSharedElementReturnTransition;
    }
    
    void setStateAfterAnimating(final int mStateAfterAnimating) {
        this.ensureAnimationInfo().mStateAfterAnimating = mStateAfterAnimating;
    }
    
    public void setTargetFragment(@Nullable final Fragment mTarget, final int mTargetRequestCode) {
        final FragmentManager fragmentManager = this.getFragmentManager();
        FragmentManager fragmentManager2;
        if (mTarget != null) {
            fragmentManager2 = mTarget.getFragmentManager();
        }
        else {
            fragmentManager2 = null;
        }
        if (fragmentManager != null && fragmentManager2 != null && fragmentManager != fragmentManager2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(mTarget);
            sb.append(" must share the same FragmentManager to be set as a target fragment");
            throw new IllegalArgumentException(sb.toString());
        }
        for (Fragment targetFragment = mTarget; targetFragment != null; targetFragment = targetFragment.getTargetFragment()) {
            if (targetFragment == this) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Setting ");
                sb2.append(mTarget);
                sb2.append(" as the target of ");
                sb2.append(this);
                sb2.append(" would create a target cycle");
                throw new IllegalArgumentException(sb2.toString());
            }
        }
        this.mTarget = mTarget;
        this.mTargetRequestCode = mTargetRequestCode;
    }
    
    public void setUserVisibleHint(final boolean b) {
        if (!this.mUserVisibleHint && b && this.mState < 3 && this.mFragmentManager != null && this.isAdded() && this.mIsCreated) {
            this.mFragmentManager.performPendingDeferredStart(this);
        }
        this.mUserVisibleHint = b;
        this.mDeferStart = (this.mState < 3 && !b);
        if (this.mSavedFragmentState != null) {
            this.mSavedUserVisibleHint = b;
        }
    }
    
    public boolean shouldShowRequestPermissionRationale(@NonNull final String s) {
        return this.mHost != null && this.mHost.onShouldShowRequestPermissionRationale(s);
    }
    
    public void startActivity(final Intent intent) {
        this.startActivity(intent, null);
    }
    
    public void startActivity(final Intent intent, @Nullable final Bundle bundle) {
        if (this.mHost == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(this);
            sb.append(" not attached to Activity");
            throw new IllegalStateException(sb.toString());
        }
        this.mHost.onStartActivityFromFragment(this, intent, -1, bundle);
    }
    
    public void startActivityForResult(final Intent intent, final int n) {
        this.startActivityForResult(intent, n, null);
    }
    
    public void startActivityForResult(final Intent intent, final int n, @Nullable final Bundle bundle) {
        if (this.mHost == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(this);
            sb.append(" not attached to Activity");
            throw new IllegalStateException(sb.toString());
        }
        this.mHost.onStartActivityFromFragment(this, intent, n, bundle);
    }
    
    public void startIntentSenderForResult(final IntentSender intentSender, final int n, @Nullable final Intent intent, final int n2, final int n3, final int n4, final Bundle bundle) throws IntentSender$SendIntentException {
        if (this.mHost == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(this);
            sb.append(" not attached to Activity");
            throw new IllegalStateException(sb.toString());
        }
        this.mHost.onStartIntentSenderFromFragment(this, intentSender, n, intent, n2, n3, n4, bundle);
    }
    
    public void startPostponedEnterTransition() {
        if (this.mFragmentManager != null && this.mFragmentManager.mHost != null) {
            if (Looper.myLooper() != this.mFragmentManager.mHost.getHandler().getLooper()) {
                this.mFragmentManager.mHost.getHandler().postAtFrontOfQueue((Runnable)new Runnable(this) {
                    final Fragment this$0;
                    
                    @Override
                    public void run() {
                        this.this$0.callStartTransitionListener();
                    }
                });
            }
            else {
                this.callStartTransitionListener();
            }
        }
        else {
            this.ensureAnimationInfo().mEnterTransitionPostponed = false;
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(128);
        DebugUtils.buildShortClassTag(this, sb);
        if (this.mIndex >= 0) {
            sb.append(" #");
            sb.append(this.mIndex);
        }
        if (this.mFragmentId != 0) {
            sb.append(" id=0x");
            sb.append(Integer.toHexString(this.mFragmentId));
        }
        if (this.mTag != null) {
            sb.append(" ");
            sb.append(this.mTag);
        }
        sb.append('}');
        return sb.toString();
    }
    
    public void unregisterForContextMenu(final View view) {
        view.setOnCreateContextMenuListener((View$OnCreateContextMenuListener)null);
    }
    
    static class AnimationInfo
    {
        Boolean mAllowEnterTransitionOverlap;
        Boolean mAllowReturnTransitionOverlap;
        View mAnimatingAway;
        Animator mAnimator;
        Object mEnterTransition;
        SharedElementCallback mEnterTransitionCallback;
        boolean mEnterTransitionPostponed;
        Object mExitTransition;
        SharedElementCallback mExitTransitionCallback;
        boolean mIsHideReplaced;
        int mNextAnim;
        int mNextTransition;
        int mNextTransitionStyle;
        Object mReenterTransition;
        Object mReturnTransition;
        Object mSharedElementEnterTransition;
        Object mSharedElementReturnTransition;
        OnStartEnterTransitionListener mStartEnterTransitionListener;
        int mStateAfterAnimating;
        
        AnimationInfo() {
            this.mEnterTransition = null;
            this.mReturnTransition = Fragment.USE_DEFAULT_TRANSITION;
            this.mExitTransition = null;
            this.mReenterTransition = Fragment.USE_DEFAULT_TRANSITION;
            this.mSharedElementEnterTransition = null;
            this.mSharedElementReturnTransition = Fragment.USE_DEFAULT_TRANSITION;
            this.mEnterTransitionCallback = null;
            this.mExitTransitionCallback = null;
        }
    }
    
    public static class InstantiationException extends RuntimeException
    {
        public InstantiationException(final String message, final Exception cause) {
            super(message, cause);
        }
    }
    
    interface OnStartEnterTransitionListener
    {
        void onStartEnterTransition();
        
        void startListening();
    }
    
    public static class SavedState implements Parcelable
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        final Bundle mState;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$ClassLoaderCreator<SavedState>() {
                public SavedState createFromParcel(final Parcel parcel) {
                    return new SavedState(parcel, null);
                }
                
                public SavedState createFromParcel(final Parcel parcel, final ClassLoader classLoader) {
                    return new SavedState(parcel, classLoader);
                }
                
                public SavedState[] newArray(final int n) {
                    return new SavedState[n];
                }
            };
        }
        
        SavedState(final Bundle mState) {
            this.mState = mState;
        }
        
        SavedState(final Parcel parcel, final ClassLoader classLoader) {
            this.mState = parcel.readBundle();
            if (classLoader != null && this.mState != null) {
                this.mState.setClassLoader(classLoader);
            }
        }
        
        public int describeContents() {
            return 0;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            parcel.writeBundle(this.mState);
        }
    }
}
