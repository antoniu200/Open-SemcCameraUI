// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.dynamic;

import android.app.Activity;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.content.Context;
import android.view.View$OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import android.widget.LinearLayout;
import com.google.android.gms.common.internal.zzg;
import com.google.android.gms.common.GooglePlayServicesUtil;
import android.widget.FrameLayout;
import java.util.Iterator;
import java.util.LinkedList;
import android.os.Bundle;

public abstract class zza<T extends LifecycleDelegate>
{
    private T zzapn;
    private Bundle zzapo;
    private LinkedList<zza> zzapp;
    private final zzf<T> zzapq;
    
    public zza() {
        this.zzapq = new zzf<T>() {
            final zza zzapr;
            
            @Override
            public void zza(final T t) {
                this.zzapr.zzapn = t;
                final Iterator iterator = this.zzapr.zzapp.iterator();
                while (iterator.hasNext()) {
                    ((zza)iterator.next()).zzb(this.zzapr.zzapn);
                }
                this.zzapr.zzapp.clear();
                this.zzapr.zzapo = null;
            }
        };
    }
    
    private void zza(final Bundle bundle, final zza e) {
        if (this.zzapn != null) {
            e.zzb(this.zzapn);
            return;
        }
        if (this.zzapp == null) {
            this.zzapp = new LinkedList<zza>();
        }
        this.zzapp.add(e);
        if (bundle != null) {
            if (this.zzapo == null) {
                this.zzapo = (Bundle)bundle.clone();
            }
            else {
                this.zzapo.putAll(bundle);
            }
        }
        this.zza(this.zzapq);
    }
    
    public static void zzb(final FrameLayout frameLayout) {
        final Context context = frameLayout.getContext();
        final int googlePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        final String zzc = zzg.zzc(context, googlePlayServicesAvailable, GooglePlayServicesUtil.zzaf(context));
        final String zzh = zzg.zzh(context, googlePlayServicesAvailable);
        final LinearLayout linearLayout = new LinearLayout(frameLayout.getContext());
        linearLayout.setOrientation(1);
        linearLayout.setLayoutParams((ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-2, -2));
        frameLayout.addView((View)linearLayout);
        final TextView textView = new TextView(frameLayout.getContext());
        textView.setLayoutParams((ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-2, -2));
        textView.setText((CharSequence)zzc);
        linearLayout.addView((View)textView);
        if (zzh != null) {
            final Button button = new Button(context);
            button.setLayoutParams((ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-2, -2));
            button.setText((CharSequence)zzh);
            linearLayout.addView((View)button);
            button.setOnClickListener((View$OnClickListener)new View$OnClickListener(context, googlePlayServicesAvailable) {
                final int zzapy;
                final Context zzry;
                
                public void onClick(final View view) {
                    this.zzry.startActivity(GooglePlayServicesUtil.zzbj(this.zzapy));
                }
            });
        }
    }
    
    private void zzer(final int n) {
        while (!this.zzapp.isEmpty() && this.zzapp.getLast().getState() >= n) {
            this.zzapp.removeLast();
        }
    }
    
    public void onCreate(final Bundle bundle) {
        this.zza(bundle, (zza)new zza(this, bundle) {
            final zza zzapr;
            final Bundle zzapu;
            
            @Override
            public int getState() {
                return 1;
            }
            
            @Override
            public void zzb(final LifecycleDelegate lifecycleDelegate) {
                this.zzapr.zzapn.onCreate(this.zzapu);
            }
        });
    }
    
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final FrameLayout frameLayout = new FrameLayout(layoutInflater.getContext());
        this.zza(bundle, (zza)new zza(this, frameLayout, layoutInflater, viewGroup, bundle) {
            final zza zzapr;
            final Bundle zzapu;
            final FrameLayout zzapv;
            final LayoutInflater zzapw;
            final ViewGroup zzapx;
            
            @Override
            public int getState() {
                return 2;
            }
            
            @Override
            public void zzb(final LifecycleDelegate lifecycleDelegate) {
                this.zzapv.removeAllViews();
                this.zzapv.addView(this.zzapr.zzapn.onCreateView(this.zzapw, this.zzapx, this.zzapu));
            }
        });
        if (this.zzapn == null) {
            this.zza(frameLayout);
        }
        return (View)frameLayout;
    }
    
    public void onDestroy() {
        if (this.zzapn != null) {
            this.zzapn.onDestroy();
            return;
        }
        this.zzer(1);
    }
    
    public void onDestroyView() {
        if (this.zzapn != null) {
            this.zzapn.onDestroyView();
            return;
        }
        this.zzer(2);
    }
    
    public void onInflate(final Activity activity, final Bundle bundle, final Bundle bundle2) {
        this.zza(bundle2, (zza)new zza(this, activity, bundle, bundle2) {
            final zza zzapr;
            final Activity zzaps;
            final Bundle zzapt;
            final Bundle zzapu;
            
            @Override
            public int getState() {
                return 0;
            }
            
            @Override
            public void zzb(final LifecycleDelegate lifecycleDelegate) {
                this.zzapr.zzapn.onInflate(this.zzaps, this.zzapt, this.zzapu);
            }
        });
    }
    
    public void onLowMemory() {
        if (this.zzapn != null) {
            this.zzapn.onLowMemory();
        }
    }
    
    public void onPause() {
        if (this.zzapn != null) {
            this.zzapn.onPause();
            return;
        }
        this.zzer(5);
    }
    
    public void onResume() {
        this.zza(null, (zza)new zza(this) {
            final zza zzapr;
            
            @Override
            public int getState() {
                return 5;
            }
            
            @Override
            public void zzb(final LifecycleDelegate lifecycleDelegate) {
                this.zzapr.zzapn.onResume();
            }
        });
    }
    
    public void onSaveInstanceState(final Bundle bundle) {
        if (this.zzapn != null) {
            this.zzapn.onSaveInstanceState(bundle);
            return;
        }
        if (this.zzapo != null) {
            bundle.putAll(this.zzapo);
        }
    }
    
    public void onStart() {
        this.zza(null, (zza)new zza(this) {
            final zza zzapr;
            
            @Override
            public int getState() {
                return 4;
            }
            
            @Override
            public void zzb(final LifecycleDelegate lifecycleDelegate) {
                this.zzapr.zzapn.onStart();
            }
        });
    }
    
    public void onStop() {
        if (this.zzapn != null) {
            this.zzapn.onStop();
            return;
        }
        this.zzer(4);
    }
    
    protected void zza(final FrameLayout frameLayout) {
        zzb(frameLayout);
    }
    
    protected abstract void zza(final zzf<T> p0);
    
    public T zzrZ() {
        return this.zzapn;
    }
    
    private interface zza
    {
        int getState();
        
        void zzb(final LifecycleDelegate p0);
    }
}
