// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.dynamic;

import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;

public final class zzb extends zzc.zza
{
    private Fragment zzapz;
    
    private zzb(final Fragment zzapz) {
        this.zzapz = zzapz;
    }
    
    public static zzb zza(final Fragment fragment) {
        if (fragment != null) {
            return new zzb(fragment);
        }
        return null;
    }
    
    public Bundle getArguments() {
        return this.zzapz.getArguments();
    }
    
    public int getId() {
        return this.zzapz.getId();
    }
    
    public boolean getRetainInstance() {
        return this.zzapz.getRetainInstance();
    }
    
    public String getTag() {
        return this.zzapz.getTag();
    }
    
    public int getTargetRequestCode() {
        return this.zzapz.getTargetRequestCode();
    }
    
    public boolean getUserVisibleHint() {
        return this.zzapz.getUserVisibleHint();
    }
    
    public zzd getView() {
        return zze.zzy(this.zzapz.getView());
    }
    
    public boolean isAdded() {
        return this.zzapz.isAdded();
    }
    
    public boolean isDetached() {
        return this.zzapz.isDetached();
    }
    
    public boolean isHidden() {
        return this.zzapz.isHidden();
    }
    
    public boolean isInLayout() {
        return this.zzapz.isInLayout();
    }
    
    public boolean isRemoving() {
        return this.zzapz.isRemoving();
    }
    
    public boolean isResumed() {
        return this.zzapz.isResumed();
    }
    
    public boolean isVisible() {
        return this.zzapz.isVisible();
    }
    
    public void setHasOptionsMenu(final boolean hasOptionsMenu) {
        this.zzapz.setHasOptionsMenu(hasOptionsMenu);
    }
    
    public void setMenuVisibility(final boolean menuVisibility) {
        this.zzapz.setMenuVisibility(menuVisibility);
    }
    
    public void setRetainInstance(final boolean retainInstance) {
        this.zzapz.setRetainInstance(retainInstance);
    }
    
    public void setUserVisibleHint(final boolean userVisibleHint) {
        this.zzapz.setUserVisibleHint(userVisibleHint);
    }
    
    public void startActivity(final Intent intent) {
        this.zzapz.startActivity(intent);
    }
    
    public void startActivityForResult(final Intent intent, final int n) {
        this.zzapz.startActivityForResult(intent, n);
    }
    
    public void zzn(final zzd zzd) {
        this.zzapz.registerForContextMenu((View)zze.zzp(zzd));
    }
    
    public void zzo(final zzd zzd) {
        this.zzapz.unregisterForContextMenu((View)zze.zzp(zzd));
    }
    
    public zzd zzsa() {
        return zze.zzy(this.zzapz.getActivity());
    }
    
    public zzc zzsb() {
        return zza(this.zzapz.getParentFragment());
    }
    
    public zzd zzsc() {
        return zze.zzy(this.zzapz.getResources());
    }
    
    public zzc zzsd() {
        return zza(this.zzapz.getTargetFragment());
    }
}
