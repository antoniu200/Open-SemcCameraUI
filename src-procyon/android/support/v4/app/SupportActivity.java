// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.app;

import android.support.annotation.CallSuper;
import android.arch.lifecycle.ReportFragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.arch.lifecycle.Lifecycle;
import android.view.View;
import android.view.Window$Callback;
import android.view.KeyEvent;
import android.arch.lifecycle.LifecycleRegistry;
import android.support.v4.util.SimpleArrayMap;
import android.support.annotation.RestrictTo;
import android.support.v4.view.KeyEventDispatcher;
import android.arch.lifecycle.LifecycleOwner;
import android.app.Activity;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class SupportActivity extends Activity implements LifecycleOwner, Component
{
    private SimpleArrayMap<Class<? extends ExtraData>, ExtraData> mExtraDataMap;
    private LifecycleRegistry mLifecycleRegistry;
    
    public SupportActivity() {
        this.mExtraDataMap = new SimpleArrayMap<Class<? extends ExtraData>, ExtraData>();
        this.mLifecycleRegistry = new LifecycleRegistry(this);
    }
    
    public boolean dispatchKeyEvent(final KeyEvent keyEvent) {
        final View decorView = this.getWindow().getDecorView();
        return (decorView != null && KeyEventDispatcher.dispatchBeforeHierarchy(decorView, keyEvent)) || KeyEventDispatcher.dispatchKeyEvent((KeyEventDispatcher.Component)this, decorView, (Window$Callback)this, keyEvent);
    }
    
    public boolean dispatchKeyShortcutEvent(final KeyEvent keyEvent) {
        final View decorView = this.getWindow().getDecorView();
        return (decorView != null && KeyEventDispatcher.dispatchBeforeHierarchy(decorView, keyEvent)) || super.dispatchKeyShortcutEvent(keyEvent);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public <T extends ExtraData> T getExtraData(final Class<T> clazz) {
        return (T)this.mExtraDataMap.get(clazz);
    }
    
    public Lifecycle getLifecycle() {
        return this.mLifecycleRegistry;
    }
    
    protected void onCreate(@Nullable final Bundle bundle) {
        super.onCreate(bundle);
        ReportFragment.injectIfNeededIn(this);
    }
    
    @CallSuper
    protected void onSaveInstanceState(final Bundle bundle) {
        this.mLifecycleRegistry.markState(Lifecycle.State.CREATED);
        super.onSaveInstanceState(bundle);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void putExtraData(final ExtraData extraData) {
        this.mExtraDataMap.put(extraData.getClass(), extraData);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public boolean superDispatchKeyEvent(final KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static class ExtraData
    {
    }
}
