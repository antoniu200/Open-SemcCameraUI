// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import android.app.Dialog;
import com.google.android.gms.common.GooglePlayServicesUtil;
import android.content.IntentSender$SendIntentException;
import android.app.Activity;
import android.os.Parcelable;
import android.os.Bundle;
import android.content.DialogInterface;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import java.io.PrintWriter;
import java.io.FileDescriptor;
import com.google.android.gms.common.api.GoogleApiClient;
import android.util.Log;
import android.support.v4.app.FragmentManager;
import com.google.android.gms.common.internal.zzx;
import android.support.v4.app.FragmentActivity;
import android.os.Looper;
import android.util.SparseArray;
import android.os.Handler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import android.content.DialogInterface$OnCancelListener;
import android.support.v4.app.Fragment;

public class zzlp extends Fragment implements DialogInterface$OnCancelListener
{
    private static final GoogleApiAvailability zzacJ;
    private boolean mStarted;
    private boolean zzacK;
    private int zzacL;
    private ConnectionResult zzacM;
    private final Handler zzacN;
    private zzll zzacO;
    private final SparseArray<zza> zzacP;
    
    static {
        zzacJ = GoogleApiAvailability.getInstance();
    }
    
    public zzlp() {
        this.zzacL = -1;
        this.zzacN = new Handler(Looper.getMainLooper());
        this.zzacP = (SparseArray<zza>)new SparseArray();
    }
    
    public static zzlp zza(final FragmentActivity fragmentActivity) {
        zzx.zzci("Must be called from main thread of process");
        final FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();
        try {
            final zzlp zzlp = (zzlp)supportFragmentManager.findFragmentByTag("GmsSupportLifecycleFragment");
            if (zzlp != null && !zzlp.isRemoving()) {
                return zzlp;
            }
            return null;
        }
        catch (final ClassCastException cause) {
            throw new IllegalStateException("Fragment with tag GmsSupportLifecycleFragment is not a SupportLifecycleFragment", cause);
        }
    }
    
    private void zza(final int n, final ConnectionResult connectionResult) {
        Log.w("GmsSupportLifecycleFragment", "Unresolved error while connecting client. Stopping auto-manage.");
        final zza zza = (zza)this.zzacP.get(n);
        if (zza != null) {
            this.zzbp(n);
            final GoogleApiClient.OnConnectionFailedListener zzacS = zza.zzacS;
            if (zzacS != null) {
                zzacS.onConnectionFailed(connectionResult);
            }
        }
        this.zzok();
    }
    
    public static zzlp zzb(final FragmentActivity fragmentActivity) {
        final zzlp zza = zza(fragmentActivity);
        final FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();
        zzlp zzlp = zza;
        if (zza == null) {
            zzlp = new zzlp();
            supportFragmentManager.beginTransaction().add(zzlp, "GmsSupportLifecycleFragment").commitAllowingStateLoss();
            supportFragmentManager.executePendingTransactions();
        }
        return zzlp;
    }
    
    private void zzok() {
        final int n = 0;
        this.zzacK = false;
        this.zzacL = -1;
        this.zzacM = null;
        int i = n;
        if (this.zzacO != null) {
            this.zzacO.unregister();
            this.zzacO = null;
            i = n;
        }
        while (i < this.zzacP.size()) {
            ((zza)this.zzacP.valueAt(i)).zzacR.connect();
            ++i;
        }
    }
    
    @Override
    public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
        super.dump(s, fileDescriptor, printWriter, array);
        for (int i = 0; i < this.zzacP.size(); ++i) {
            ((zza)this.zzacP.valueAt(i)).dump(s, fileDescriptor, printWriter, array);
        }
    }
    
    @Override
    public void onActivityResult(int n, final int n2, final Intent intent) {
        final int n3 = 1;
        Label_0081: {
            switch (n) {
                case 2: {
                    if (zzlp.zzacJ.isGooglePlayServicesAvailable((Context)this.getActivity()) == 0) {
                        n = n3;
                        break Label_0081;
                    }
                    break;
                }
                case 1: {
                    if (n2 == -1) {
                        n = n3;
                        break Label_0081;
                    }
                    if (n2 == 0) {
                        this.zzacM = new ConnectionResult(13, null);
                        break;
                    }
                    break;
                }
            }
            n = 0;
        }
        if (n != 0) {
            this.zzok();
            return;
        }
        this.zza(this.zzacL, this.zzacM);
    }
    
    public void onCancel(final DialogInterface dialogInterface) {
        this.zza(this.zzacL, new ConnectionResult(13, null));
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.zzacK = bundle.getBoolean("resolving_error", false);
            this.zzacL = bundle.getInt("failed_client_id", -1);
            if (this.zzacL >= 0) {
                this.zzacM = new ConnectionResult(bundle.getInt("failed_status"), (PendingIntent)bundle.getParcelable("failed_resolution"));
            }
        }
    }
    
    @Override
    public void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("resolving_error", this.zzacK);
        if (this.zzacL >= 0) {
            bundle.putInt("failed_client_id", this.zzacL);
            bundle.putInt("failed_status", this.zzacM.getErrorCode());
            bundle.putParcelable("failed_resolution", (Parcelable)this.zzacM.getResolution());
        }
    }
    
    @Override
    public void onStart() {
        super.onStart();
        this.mStarted = true;
        if (!this.zzacK) {
            for (int i = 0; i < this.zzacP.size(); ++i) {
                ((zza)this.zzacP.valueAt(i)).zzacR.connect();
            }
        }
    }
    
    @Override
    public void onStop() {
        super.onStop();
        int i = 0;
        this.mStarted = false;
        while (i < this.zzacP.size()) {
            ((zza)this.zzacP.valueAt(i)).zzacR.disconnect();
            ++i;
        }
    }
    
    public void zza(final int i, final GoogleApiClient googleApiClient, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        zzx.zzb(googleApiClient, "GoogleApiClient instance cannot be null");
        final boolean b = this.zzacP.indexOfKey(i) < 0;
        final StringBuilder sb = new StringBuilder();
        sb.append("Already managing a GoogleApiClient with id ");
        sb.append(i);
        zzx.zza(b, (Object)sb.toString());
        this.zzacP.put(i, (Object)new zza(i, googleApiClient, onConnectionFailedListener));
        if (this.mStarted && !this.zzacK) {
            googleApiClient.connect();
        }
    }
    
    public void zzbp(final int n) {
        final zza zza = (zza)this.zzacP.get(n);
        this.zzacP.remove(n);
        if (zza != null) {
            zza.zzom();
        }
    }
    
    private class zza implements OnConnectionFailedListener
    {
        public final int zzacQ;
        public final GoogleApiClient zzacR;
        public final OnConnectionFailedListener zzacS;
        final zzlp zzacT;
        
        public zza(final zzlp zzacT, final int zzacQ, final GoogleApiClient zzacR, final OnConnectionFailedListener zzacS) {
            this.zzacT = zzacT;
            this.zzacQ = zzacQ;
            this.zzacR = zzacR;
            this.zzacS = zzacS;
            zzacR.registerConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener)this);
        }
        
        public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
            printWriter.append(s).append("GoogleApiClient #").print(this.zzacQ);
            printWriter.println(":");
            final GoogleApiClient zzacR = this.zzacR;
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append("  ");
            zzacR.dump(sb.toString(), fileDescriptor, printWriter, array);
        }
        
        @Override
        public void onConnectionFailed(final ConnectionResult connectionResult) {
            this.zzacT.zzacN.post((Runnable)this.zzacT.new zzb(this.zzacQ, connectionResult));
        }
        
        public void zzom() {
            this.zzacR.unregisterConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener)this);
            this.zzacR.disconnect();
        }
    }
    
    private class zzb implements Runnable
    {
        final zzlp zzacT;
        private final int zzacU;
        private final ConnectionResult zzacV;
        
        public zzb(final zzlp zzacT, final int zzacU, final ConnectionResult zzacV) {
            this.zzacT = zzacT;
            this.zzacU = zzacU;
            this.zzacV = zzacV;
        }
        
        @Override
        public void run() {
            if (this.zzacT.mStarted) {
                if (this.zzacT.zzacK) {
                    return;
                }
                this.zzacT.zzacK = true;
                this.zzacT.zzacL = this.zzacU;
                this.zzacT.zzacM = this.zzacV;
                if (this.zzacV.hasResolution()) {
                    try {
                        this.zzacV.startResolutionForResult(this.zzacT.getActivity(), 1 + (this.zzacT.getActivity().getSupportFragmentManager().getFragments().indexOf(this.zzacT) + 1 << 16));
                        return;
                    }
                    catch (final IntentSender$SendIntentException ex) {
                        this.zzacT.zzok();
                        return;
                    }
                }
                if (zzlp.zzacJ.isUserResolvableError(this.zzacV.getErrorCode())) {
                    GooglePlayServicesUtil.showErrorDialogFragment(this.zzacV.getErrorCode(), this.zzacT.getActivity(), this.zzacT, 2, (DialogInterface$OnCancelListener)this.zzacT);
                    return;
                }
                if (this.zzacV.getErrorCode() == 18) {
                    this.zzacT.zzacO = zzll.zza(this.zzacT.getActivity().getApplicationContext(), new zzll(this, zzlp.zzacJ.zza(this.zzacT.getActivity(), (DialogInterface$OnCancelListener)this.zzacT)) {
                        final Dialog zzacW;
                        final zzb zzacX;
                        
                        @Override
                        protected void zzoi() {
                            this.zzacX.zzacT.zzok();
                            this.zzacW.dismiss();
                        }
                    });
                    return;
                }
                this.zzacT.zza(this.zzacU, this.zzacV);
            }
        }
    }
}
