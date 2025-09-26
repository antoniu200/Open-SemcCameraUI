// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.dynamic;

import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public final class zzh extends zzc.zza
{
    private Fragment zzafl;
    
    private zzh(final Fragment zzafl) {
        this.zzafl = zzafl;
    }
    
    public static zzh zza(final Fragment fragment) {
        if (fragment != null) {
            return new zzh(fragment);
        }
        return null;
    }
    
    public Bundle getArguments() {
        return this.zzafl.getArguments();
    }
    
    public int getId() {
        return this.zzafl.getId();
    }
    
    public boolean getRetainInstance() {
        return this.zzafl.getRetainInstance();
    }
    
    public String getTag() {
        return this.zzafl.getTag();
    }
    
    public int getTargetRequestCode() {
        return this.zzafl.getTargetRequestCode();
    }
    
    public boolean getUserVisibleHint() {
        return this.zzafl.getUserVisibleHint();
    }
    
    public zzd getView() {
        return zze.zzy(this.zzafl.getView());
    }
    
    public boolean isAdded() {
        return this.zzafl.isAdded();
    }
    
    public boolean isDetached() {
        return this.zzafl.isDetached();
    }
    
    public boolean isHidden() {
        return this.zzafl.isHidden();
    }
    
    public boolean isInLayout() {
        return this.zzafl.isInLayout();
    }
    
    public boolean isRemoving() {
        return this.zzafl.isRemoving();
    }
    
    public boolean isResumed() {
        return this.zzafl.isResumed();
    }
    
    public boolean isVisible() {
        return this.zzafl.isVisible();
    }
    
    public void setHasOptionsMenu(final boolean hasOptionsMenu) {
        this.zzafl.setHasOptionsMenu(hasOptionsMenu);
    }
    
    public void setMenuVisibility(final boolean menuVisibility) {
        this.zzafl.setMenuVisibility(menuVisibility);
    }
    
    public void setRetainInstance(final boolean retainInstance) {
        this.zzafl.setRetainInstance(retainInstance);
    }
    
    public void setUserVisibleHint(final boolean userVisibleHint) {
        this.zzafl.setUserVisibleHint(userVisibleHint);
    }
    
    public void startActivity(final Intent intent) {
        this.zzafl.startActivity(intent);
    }
    
    public void startActivityForResult(final Intent intent, final int n) {
        this.zzafl.startActivityForResult(intent, n);
    }
    
    public void zzn(final zzd zzd) {
        this.zzafl.registerForContextMenu(zze.zzp(zzd));
    }
    
    public void zzo(final zzd zzd) {
        this.zzafl.unregisterForContextMenu(zze.zzp(zzd));
    }
    
    public zzd zzsa() {
        return zze.zzy(this.zzafl.getActivity());
    }
    
    public zzc zzsb() {
        return zza(this.zzafl.getParentFragment());
    }
    
    public zzd zzsc() {
        return zze.zzy(this.zzafl.getResources());
    }
    
    public zzc zzsd() {
        return zza(this.zzafl.getTargetFragment());
    }
}
