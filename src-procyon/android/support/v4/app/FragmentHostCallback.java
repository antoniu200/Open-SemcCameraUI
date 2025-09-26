// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.app;

import android.content.IntentSender$SendIntentException;
import android.content.IntentSender;
import android.os.Bundle;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import java.io.PrintWriter;
import java.io.FileDescriptor;
import android.support.v4.util.Preconditions;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.content.Context;
import android.support.annotation.Nullable;
import android.app.Activity;

public abstract class FragmentHostCallback<E> extends FragmentContainer
{
    @Nullable
    private final Activity mActivity;
    @NonNull
    private final Context mContext;
    final FragmentManagerImpl mFragmentManager;
    @NonNull
    private final Handler mHandler;
    private final int mWindowAnimations;
    
    FragmentHostCallback(@Nullable final Activity mActivity, @NonNull final Context context, @NonNull final Handler handler, final int mWindowAnimations) {
        this.mFragmentManager = new FragmentManagerImpl();
        this.mActivity = mActivity;
        this.mContext = Preconditions.checkNotNull(context, "context == null");
        this.mHandler = Preconditions.checkNotNull(handler, "handler == null");
        this.mWindowAnimations = mWindowAnimations;
    }
    
    public FragmentHostCallback(@NonNull final Context context, @NonNull final Handler handler, final int n) {
        Activity activity;
        if (context instanceof Activity) {
            activity = (Activity)context;
        }
        else {
            activity = null;
        }
        this(activity, context, handler, n);
    }
    
    FragmentHostCallback(@NonNull final FragmentActivity fragmentActivity) {
        this(fragmentActivity, (Context)fragmentActivity, fragmentActivity.mHandler, 0);
    }
    
    @Nullable
    Activity getActivity() {
        return this.mActivity;
    }
    
    @NonNull
    Context getContext() {
        return this.mContext;
    }
    
    FragmentManagerImpl getFragmentManagerImpl() {
        return this.mFragmentManager;
    }
    
    @NonNull
    Handler getHandler() {
        return this.mHandler;
    }
    
    void onAttachFragment(final Fragment fragment) {
    }
    
    public void onDump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
    }
    
    @Nullable
    @Override
    public View onFindViewById(final int n) {
        return null;
    }
    
    @Nullable
    public abstract E onGetHost();
    
    @NonNull
    public LayoutInflater onGetLayoutInflater() {
        return LayoutInflater.from(this.mContext);
    }
    
    public int onGetWindowAnimations() {
        return this.mWindowAnimations;
    }
    
    @Override
    public boolean onHasView() {
        return true;
    }
    
    public boolean onHasWindowAnimations() {
        return true;
    }
    
    public void onRequestPermissionsFromFragment(@NonNull final Fragment fragment, @NonNull final String[] array, final int n) {
    }
    
    public boolean onShouldSaveFragmentState(final Fragment fragment) {
        return true;
    }
    
    public boolean onShouldShowRequestPermissionRationale(@NonNull final String s) {
        return false;
    }
    
    public void onStartActivityFromFragment(final Fragment fragment, final Intent intent, final int n) {
        this.onStartActivityFromFragment(fragment, intent, n, null);
    }
    
    public void onStartActivityFromFragment(final Fragment fragment, final Intent intent, final int n, @Nullable final Bundle bundle) {
        if (n != -1) {
            throw new IllegalStateException("Starting activity with a requestCode requires a FragmentActivity host");
        }
        this.mContext.startActivity(intent);
    }
    
    public void onStartIntentSenderFromFragment(final Fragment fragment, final IntentSender intentSender, final int n, @Nullable final Intent intent, final int n2, final int n3, final int n4, final Bundle bundle) throws IntentSender$SendIntentException {
        if (n != -1) {
            throw new IllegalStateException("Starting intent sender with a requestCode requires a FragmentActivity host");
        }
        ActivityCompat.startIntentSenderForResult(this.mActivity, intentSender, n, intent, n2, n3, n4, bundle);
    }
    
    public void onSupportInvalidateOptionsMenu() {
    }
}
