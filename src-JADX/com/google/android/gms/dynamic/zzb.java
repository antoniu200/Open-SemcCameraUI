package com.google.android.gms.dynamic;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.dynamic.zzc;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class zzb extends zzc.zza {
    private Fragment zzapz;

    private zzb(Fragment fragment) {
        this.zzapz = fragment;
    }

    public static zzb zza(Fragment fragment) {
        if (fragment != null) {
            return new zzb(fragment);
        }
        return null;
    }

    @Override // com.google.android.gms.dynamic.zzc
    public Bundle getArguments() {
        return this.zzapz.getArguments();
    }

    @Override // com.google.android.gms.dynamic.zzc
    public int getId() {
        return this.zzapz.getId();
    }

    @Override // com.google.android.gms.dynamic.zzc
    public boolean getRetainInstance() {
        return this.zzapz.getRetainInstance();
    }

    @Override // com.google.android.gms.dynamic.zzc
    public String getTag() {
        return this.zzapz.getTag();
    }

    @Override // com.google.android.gms.dynamic.zzc
    public int getTargetRequestCode() {
        return this.zzapz.getTargetRequestCode();
    }

    @Override // com.google.android.gms.dynamic.zzc
    public boolean getUserVisibleHint() {
        return this.zzapz.getUserVisibleHint();
    }

    @Override // com.google.android.gms.dynamic.zzc
    public zzd getView() {
        return zze.zzy(this.zzapz.getView());
    }

    @Override // com.google.android.gms.dynamic.zzc
    public boolean isAdded() {
        return this.zzapz.isAdded();
    }

    @Override // com.google.android.gms.dynamic.zzc
    public boolean isDetached() {
        return this.zzapz.isDetached();
    }

    @Override // com.google.android.gms.dynamic.zzc
    public boolean isHidden() {
        return this.zzapz.isHidden();
    }

    @Override // com.google.android.gms.dynamic.zzc
    public boolean isInLayout() {
        return this.zzapz.isInLayout();
    }

    @Override // com.google.android.gms.dynamic.zzc
    public boolean isRemoving() {
        return this.zzapz.isRemoving();
    }

    @Override // com.google.android.gms.dynamic.zzc
    public boolean isResumed() {
        return this.zzapz.isResumed();
    }

    @Override // com.google.android.gms.dynamic.zzc
    public boolean isVisible() {
        return this.zzapz.isVisible();
    }

    @Override // com.google.android.gms.dynamic.zzc
    public void setHasOptionsMenu(boolean z) {
        this.zzapz.setHasOptionsMenu(z);
    }

    @Override // com.google.android.gms.dynamic.zzc
    public void setMenuVisibility(boolean z) {
        this.zzapz.setMenuVisibility(z);
    }

    @Override // com.google.android.gms.dynamic.zzc
    public void setRetainInstance(boolean z) {
        this.zzapz.setRetainInstance(z);
    }

    @Override // com.google.android.gms.dynamic.zzc
    public void setUserVisibleHint(boolean z) {
        this.zzapz.setUserVisibleHint(z);
    }

    @Override // com.google.android.gms.dynamic.zzc
    public void startActivity(Intent intent) {
        this.zzapz.startActivity(intent);
    }

    @Override // com.google.android.gms.dynamic.zzc
    public void startActivityForResult(Intent intent, int i) {
        this.zzapz.startActivityForResult(intent, i);
    }

    @Override // com.google.android.gms.dynamic.zzc
    public void zzn(zzd zzdVar) {
        this.zzapz.registerForContextMenu((View) zze.zzp(zzdVar));
    }

    @Override // com.google.android.gms.dynamic.zzc
    public void zzo(zzd zzdVar) {
        this.zzapz.unregisterForContextMenu((View) zze.zzp(zzdVar));
    }

    @Override // com.google.android.gms.dynamic.zzc
    public zzd zzsa() {
        return zze.zzy(this.zzapz.getActivity());
    }

    @Override // com.google.android.gms.dynamic.zzc
    public zzc zzsb() {
        return zza(this.zzapz.getParentFragment());
    }

    @Override // com.google.android.gms.dynamic.zzc
    public zzd zzsc() {
        return zze.zzy(this.zzapz.getResources());
    }

    @Override // com.google.android.gms.dynamic.zzc
    public zzc zzsd() {
        return zza(this.zzapz.getTargetFragment());
    }
}
