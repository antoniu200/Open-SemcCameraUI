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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean proceed = false;

        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    proceed = true;
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // 13 = ConnectionResult.CANCELED
                    this.zzacM = new ConnectionResult(13, null);
                }
                break;

            case 2: {
                int status = zzacJ.isGooglePlayServicesAvailable(getActivity());
                if (status == ConnectionResult.SUCCESS) {
                    proceed = true;
                }
                break;
            }

            default:
                // no-op
                break;
        }

        if (proceed) {
            zzok();
        } else {
            zza(zzacL, zzacM);
        }
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
