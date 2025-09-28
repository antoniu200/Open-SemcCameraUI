package com.google.android.gms.dynamic;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.dynamic.LifecycleDelegate;
import java.util.Iterator;
import java.util.LinkedList;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class zza<T extends LifecycleDelegate> {
    private T zzapn;
    private Bundle zzapo;
    private LinkedList<InterfaceC0018zza> zzapp;
    private final zzf<T> zzapq = (zzf<T>) new zzf<T>() { // from class: com.google.android.gms.dynamic.zza.1
        @Override // com.google.android.gms.dynamic.zzf
        public void zza(T t) {
            zza.this.zzapn = t;
            Iterator it = zza.this.zzapp.iterator();
            while (it.hasNext()) {
                ((InterfaceC0018zza) it.next()).zzb(zza.this.zzapn);
            }
            zza.this.zzapp.clear();
            zza.this.zzapo = null;
        }
    };

    /* renamed from: com.google.android.gms.dynamic.zza$zza, reason: collision with other inner class name */
    private interface InterfaceC0018zza {
        int getState();

        void zzb(LifecycleDelegate lifecycleDelegate);
    }

    private void zza(Bundle bundle, InterfaceC0018zza interfaceC0018zza) {
        if (this.zzapn != null) {
            interfaceC0018zza.zzb(this.zzapn);
            return;
        }
        if (this.zzapp == null) {
            this.zzapp = new LinkedList<>();
        }
        this.zzapp.add(interfaceC0018zza);
        if (bundle != null) {
            if (this.zzapo == null) {
                this.zzapo = (Bundle) bundle.clone();
            } else {
                this.zzapo.putAll(bundle);
            }
        }
        zza(this.zzapq);
    }

    public static void zzb(FrameLayout frameLayout) throws PackageManager.NameNotFoundException {
        final Context context = frameLayout.getContext();
        final int iIsGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        String strZzc = com.google.android.gms.common.internal.zzg.zzc(context, iIsGooglePlayServicesAvailable, GooglePlayServicesUtil.zzaf(context));
        String strZzh = com.google.android.gms.common.internal.zzg.zzh(context, iIsGooglePlayServicesAvailable);
        LinearLayout linearLayout = new LinearLayout(frameLayout.getContext());
        linearLayout.setOrientation(1);
        linearLayout.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
        frameLayout.addView(linearLayout);
        TextView textView = new TextView(frameLayout.getContext());
        textView.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
        textView.setText(strZzc);
        linearLayout.addView(textView);
        if (strZzh != null) {
            Button button = new Button(context);
            button.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
            button.setText(strZzh);
            linearLayout.addView(button);
            button.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.gms.dynamic.zza.5
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    context.startActivity(GooglePlayServicesUtil.zzbj(iIsGooglePlayServicesAvailable));
                }
            });
        }
    }

    private void zzer(int i) {
        while (!this.zzapp.isEmpty() && this.zzapp.getLast().getState() >= i) {
            this.zzapp.removeLast();
        }
    }

    public void onCreate(final Bundle bundle) {
        zza(bundle, new InterfaceC0018zza() { // from class: com.google.android.gms.dynamic.zza.3
            @Override // com.google.android.gms.dynamic.zza.InterfaceC0018zza
            public int getState() {
                return 1;
            }

            @Override // com.google.android.gms.dynamic.zza.InterfaceC0018zza
            public void zzb(LifecycleDelegate lifecycleDelegate) {
                zza.this.zzapn.onCreate(bundle);
            }
        });
    }

    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) throws PackageManager.NameNotFoundException {
        final FrameLayout frameLayout = new FrameLayout(layoutInflater.getContext());
        zza(bundle, new InterfaceC0018zza() { // from class: com.google.android.gms.dynamic.zza.4
            @Override // com.google.android.gms.dynamic.zza.InterfaceC0018zza
            public int getState() {
                return 2;
            }

            @Override // com.google.android.gms.dynamic.zza.InterfaceC0018zza
            public void zzb(LifecycleDelegate lifecycleDelegate) {
                frameLayout.removeAllViews();
                frameLayout.addView(zza.this.zzapn.onCreateView(layoutInflater, viewGroup, bundle));
            }
        });
        if (this.zzapn == null) {
            zza(frameLayout);
        }
        return frameLayout;
    }

    public void onDestroy() {
        if (this.zzapn != null) {
            this.zzapn.onDestroy();
        } else {
            zzer(1);
        }
    }

    public void onDestroyView() {
        if (this.zzapn != null) {
            this.zzapn.onDestroyView();
        } else {
            zzer(2);
        }
    }

    public void onInflate(final Activity activity, final Bundle bundle, final Bundle bundle2) {
        zza(bundle2, new InterfaceC0018zza() { // from class: com.google.android.gms.dynamic.zza.2
            @Override // com.google.android.gms.dynamic.zza.InterfaceC0018zza
            public int getState() {
                return 0;
            }

            @Override // com.google.android.gms.dynamic.zza.InterfaceC0018zza
            public void zzb(LifecycleDelegate lifecycleDelegate) {
                zza.this.zzapn.onInflate(activity, bundle, bundle2);
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
        } else {
            zzer(5);
        }
    }

    public void onResume() {
        zza((Bundle) null, new InterfaceC0018zza() { // from class: com.google.android.gms.dynamic.zza.7
            @Override // com.google.android.gms.dynamic.zza.InterfaceC0018zza
            public int getState() {
                return 5;
            }

            @Override // com.google.android.gms.dynamic.zza.InterfaceC0018zza
            public void zzb(LifecycleDelegate lifecycleDelegate) {
                zza.this.zzapn.onResume();
            }
        });
    }

    public void onSaveInstanceState(Bundle bundle) {
        if (this.zzapn != null) {
            this.zzapn.onSaveInstanceState(bundle);
        } else if (this.zzapo != null) {
            bundle.putAll(this.zzapo);
        }
    }

    public void onStart() {
        zza((Bundle) null, new InterfaceC0018zza() { // from class: com.google.android.gms.dynamic.zza.6
            @Override // com.google.android.gms.dynamic.zza.InterfaceC0018zza
            public int getState() {
                return 4;
            }

            @Override // com.google.android.gms.dynamic.zza.InterfaceC0018zza
            public void zzb(LifecycleDelegate lifecycleDelegate) {
                zza.this.zzapn.onStart();
            }
        });
    }

    public void onStop() {
        if (this.zzapn != null) {
            this.zzapn.onStop();
        } else {
            zzer(4);
        }
    }

    protected void zza(FrameLayout frameLayout) throws PackageManager.NameNotFoundException {
        zzb(frameLayout);
    }

    protected abstract void zza(zzf<T> zzfVar);

    public T zzrZ() {
        return this.zzapn;
    }
}
