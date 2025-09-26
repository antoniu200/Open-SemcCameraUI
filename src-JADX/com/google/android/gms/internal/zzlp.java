package com.google.android.gms.internal;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.SparseArray;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.zzx;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzlp extends Fragment implements DialogInterface.OnCancelListener {
    private static final GoogleApiAvailability zzacJ = GoogleApiAvailability.getInstance();
    private boolean mStarted;
    private boolean zzacK;
    private ConnectionResult zzacM;
    private zzll zzacO;
    private int zzacL = -1;
    private final Handler zzacN = new Handler(Looper.getMainLooper());
    private final SparseArray<zza> zzacP = new SparseArray<>();

    private class zza implements GoogleApiClient.OnConnectionFailedListener {
        public final int zzacQ;
        public final GoogleApiClient zzacR;
        public final GoogleApiClient.OnConnectionFailedListener zzacS;

        public zza(int i, GoogleApiClient googleApiClient, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
            this.zzacQ = i;
            this.zzacR = googleApiClient;
            this.zzacS = onConnectionFailedListener;
            googleApiClient.registerConnectionFailedListener(this);
        }

        public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            printWriter.append((CharSequence) str).append("GoogleApiClient #").print(this.zzacQ);
            printWriter.println(":");
            this.zzacR.dump(str + "  ", fileDescriptor, printWriter, strArr);
        }

        @Override // com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
        public void onConnectionFailed(ConnectionResult connectionResult) {
            zzlp.this.zzacN.post(zzlp.this.new zzb(this.zzacQ, connectionResult));
        }

        public void zzom() {
            this.zzacR.unregisterConnectionFailedListener(this);
            this.zzacR.disconnect();
        }
    }

    private class zzb implements Runnable {
        private final int zzacU;
        private final ConnectionResult zzacV;

        public zzb(int i, ConnectionResult connectionResult) {
            this.zzacU = i;
            this.zzacV = connectionResult;
        }

        @Override // java.lang.Runnable
        public void run() throws PackageManager.NameNotFoundException {
            if (!zzlp.this.mStarted || zzlp.this.zzacK) {
                return;
            }
            zzlp.this.zzacK = true;
            zzlp.this.zzacL = this.zzacU;
            zzlp.this.zzacM = this.zzacV;
            if (this.zzacV.hasResolution()) {
                try {
                    this.zzacV.startResolutionForResult(zzlp.this.getActivity(), 1 + ((zzlp.this.getActivity().getSupportFragmentManager().getFragments().indexOf(zzlp.this) + 1) << 16));
                    return;
                } catch (IntentSender.SendIntentException unused) {
                    zzlp.this.zzok();
                    return;
                }
            }
            if (zzlp.zzacJ.isUserResolvableError(this.zzacV.getErrorCode())) {
                GooglePlayServicesUtil.showErrorDialogFragment(this.zzacV.getErrorCode(), zzlp.this.getActivity(), zzlp.this, 2, zzlp.this);
            } else {
                if (this.zzacV.getErrorCode() != 18) {
                    zzlp.this.zza(this.zzacU, this.zzacV);
                    return;
                }
                final Dialog dialogZza = zzlp.zzacJ.zza(zzlp.this.getActivity(), zzlp.this);
                zzlp.this.zzacO = zzll.zza(zzlp.this.getActivity().getApplicationContext(), new zzll() { // from class: com.google.android.gms.internal.zzlp.zzb.1
                    @Override // com.google.android.gms.internal.zzll
                    protected void zzoi() {
                        zzlp.this.zzok();
                        dialogZza.dismiss();
                    }
                });
            }
        }
    }

    public static zzlp zza(FragmentActivity fragmentActivity) {
        zzx.zzci("Must be called from main thread of process");
        try {
            zzlp zzlpVar = (zzlp) fragmentActivity.getSupportFragmentManager().findFragmentByTag("GmsSupportLifecycleFragment");
            if (zzlpVar == null || zzlpVar.isRemoving()) {
                return null;
            }
            return zzlpVar;
        } catch (ClassCastException e) {
            throw new IllegalStateException("Fragment with tag GmsSupportLifecycleFragment is not a SupportLifecycleFragment", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zza(int i, ConnectionResult connectionResult) {
        Log.w("GmsSupportLifecycleFragment", "Unresolved error while connecting client. Stopping auto-manage.");
        zza zzaVar = this.zzacP.get(i);
        if (zzaVar != null) {
            zzbp(i);
            GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener = zzaVar.zzacS;
            if (onConnectionFailedListener != null) {
                onConnectionFailedListener.onConnectionFailed(connectionResult);
            }
        }
        zzok();
    }

    public static zzlp zzb(FragmentActivity fragmentActivity) {
        zzlp zzlpVarZza = zza(fragmentActivity);
        FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();
        if (zzlpVarZza != null) {
            return zzlpVarZza;
        }
        zzlp zzlpVar = new zzlp();
        supportFragmentManager.beginTransaction().add(zzlpVar, "GmsSupportLifecycleFragment").commitAllowingStateLoss();
        supportFragmentManager.executePendingTransactions();
        return zzlpVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zzok() {
        this.zzacK = false;
        this.zzacL = -1;
        this.zzacM = null;
        if (this.zzacO != null) {
            this.zzacO.unregister();
            this.zzacO = null;
        }
        for (int i = 0; i < this.zzacP.size(); i++) {
            this.zzacP.valueAt(i).zzacR.connect();
        }
    }

    @Override // android.support.v4.app.Fragment
    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        super.dump(str, fileDescriptor, printWriter, strArr);
        for (int i = 0; i < this.zzacP.size(); i++) {
            this.zzacP.valueAt(i).dump(str, fileDescriptor, printWriter, strArr);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0022  */
    @Override // android.support.v4.app.Fragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void onActivityResult(int r1, int r2, android.content.Intent r3) {
        /*
            r0 = this;
            r3 = 1
            switch(r1) {
                case 1: goto L12;
                case 2: goto L5;
                default: goto L4;
            }
        L4:
            goto L22
        L5:
            com.google.android.gms.common.GoogleApiAvailability r1 = com.google.android.gms.internal.zzlp.zzacJ
            android.support.v4.app.FragmentActivity r2 = r0.getActivity()
            int r1 = r1.isGooglePlayServicesAvailable(r2)
            if (r1 != 0) goto L22
            goto L23
        L12:
            r1 = -1
            if (r2 != r1) goto L16
            goto L23
        L16:
            if (r2 != 0) goto L22
            com.google.android.gms.common.ConnectionResult r1 = new com.google.android.gms.common.ConnectionResult
            r2 = 13
            r3 = 0
            r1.<init>(r2, r3)
            r0.zzacM = r1
        L22:
            r3 = 0
        L23:
            if (r3 == 0) goto L29
            r0.zzok()
            return
        L29:
            int r1 = r0.zzacL
            com.google.android.gms.common.ConnectionResult r2 = r0.zzacM
            r0.zza(r1, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzlp.onActivityResult(int, int, android.content.Intent):void");
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        zza(this.zzacL, new ConnectionResult(13, null));
    }

    @Override // android.support.v4.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.zzacK = bundle.getBoolean("resolving_error", false);
            this.zzacL = bundle.getInt("failed_client_id", -1);
            if (this.zzacL >= 0) {
                this.zzacM = new ConnectionResult(bundle.getInt("failed_status"), (PendingIntent) bundle.getParcelable("failed_resolution"));
            }
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("resolving_error", this.zzacK);
        if (this.zzacL >= 0) {
            bundle.putInt("failed_client_id", this.zzacL);
            bundle.putInt("failed_status", this.zzacM.getErrorCode());
            bundle.putParcelable("failed_resolution", this.zzacM.getResolution());
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onStart() {
        super.onStart();
        this.mStarted = true;
        if (this.zzacK) {
            return;
        }
        for (int i = 0; i < this.zzacP.size(); i++) {
            this.zzacP.valueAt(i).zzacR.connect();
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onStop() {
        super.onStop();
        this.mStarted = false;
        for (int i = 0; i < this.zzacP.size(); i++) {
            this.zzacP.valueAt(i).zzacR.disconnect();
        }
    }

    public void zza(int i, GoogleApiClient googleApiClient, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        zzx.zzb(googleApiClient, "GoogleApiClient instance cannot be null");
        zzx.zza(this.zzacP.indexOfKey(i) < 0, "Already managing a GoogleApiClient with id " + i);
        this.zzacP.put(i, new zza(i, googleApiClient, onConnectionFailedListener));
        if (!this.mStarted || this.zzacK) {
            return;
        }
        googleApiClient.connect();
    }

    public void zzbp(int i) {
        zza zzaVar = this.zzacP.get(i);
        this.zzacP.remove(i);
        if (zzaVar != null) {
            zzaVar.zzom();
        }
    }
}
