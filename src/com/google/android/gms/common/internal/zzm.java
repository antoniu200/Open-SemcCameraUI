package com.google.android.gms.common.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
final class zzm extends zzl implements Handler.Callback {
    private final Handler mHandler;
    private final HashMap<zza, zzb> zzafY = new HashMap<>();
    private final com.google.android.gms.common.stats.zzb zzafZ = com.google.android.gms.common.stats.zzb.zzqh();
    private final long zzaga = 5000;
    private final Context zzqZ;

    private static final class zza {
        private final String zzPp;
        private final ComponentName zzagb;

        public zza(ComponentName componentName) {
            this.zzPp = null;
            this.zzagb = (ComponentName) zzx.zzw(componentName);
        }

        public zza(String str) {
            this.zzPp = zzx.zzcr(str);
            this.zzagb = null;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof zza)) {
                return false;
            }
            zza zzaVar = (zza) obj;
            return zzw.equal(this.zzPp, zzaVar.zzPp) && zzw.equal(this.zzagb, zzaVar.zzagb);
        }

        public int hashCode() {
            return zzw.hashCode(this.zzPp, this.zzagb);
        }

        public String toString() {
            return this.zzPp == null ? this.zzagb.flattenToString() : this.zzPp;
        }

        public Intent zzpm() {
            return this.zzPp != null ? new Intent(this.zzPp).setPackage("com.google.android.gms") : new Intent().setComponent(this.zzagb);
        }
    }

    private final class zzb {
        private IBinder zzaeJ;
        private ComponentName zzagb;
        private boolean zzage;
        private final zza zzagf;
        private final zza zzagc = new zza();
        private final Set<ServiceConnection> zzagd = new HashSet();
        private int mState = 2;

        public class zza implements ServiceConnection {
            public zza() {
            }

            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                synchronized (zzm.this.zzafY) {
                    zzb.this.zzaeJ = iBinder;
                    zzb.this.zzagb = componentName;
                    Iterator it = zzb.this.zzagd.iterator();
                    while (it.hasNext()) {
                        ((ServiceConnection) it.next()).onServiceConnected(componentName, iBinder);
                    }
                    zzb.this.mState = 1;
                }
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName componentName) {
                synchronized (zzm.this.zzafY) {
                    zzb.this.zzaeJ = null;
                    zzb.this.zzagb = componentName;
                    Iterator it = zzb.this.zzagd.iterator();
                    while (it.hasNext()) {
                        ((ServiceConnection) it.next()).onServiceDisconnected(componentName);
                    }
                    zzb.this.mState = 2;
                }
            }
        }

        public zzb(zza zzaVar) {
            this.zzagf = zzaVar;
        }

        public IBinder getBinder() {
            return this.zzaeJ;
        }

        public ComponentName getComponentName() {
            return this.zzagb;
        }

        public int getState() {
            return this.mState;
        }

        public boolean isBound() {
            return this.zzage;
        }

        public void zza(ServiceConnection serviceConnection, String str) {
            zzm.this.zzafZ.zza(zzm.this.zzqZ, serviceConnection, str, this.zzagf.zzpm());
            this.zzagd.add(serviceConnection);
        }

        public boolean zza(ServiceConnection serviceConnection) {
            return this.zzagd.contains(serviceConnection);
        }

        public void zzb(ServiceConnection serviceConnection, String str) {
            zzm.this.zzafZ.zzb(zzm.this.zzqZ, serviceConnection);
            this.zzagd.remove(serviceConnection);
        }

        public void zzcm(String str) {
            this.mState = 3;
            this.zzage = zzm.this.zzafZ.zza(zzm.this.zzqZ, str, this.zzagf.zzpm(), this.zzagc, 129);
            if (this.zzage) {
                return;
            }
            this.mState = 2;
            try {
                zzm.this.zzafZ.zza(zzm.this.zzqZ, this.zzagc);
            } catch (IllegalArgumentException unused) {
            }
        }

        public void zzcn(String str) {
            zzm.this.zzafZ.zza(zzm.this.zzqZ, this.zzagc);
            this.zzage = false;
            this.mState = 2;
        }

        public boolean zzpn() {
            return this.zzagd.isEmpty();
        }
    }

    zzm(Context context) {
        this.zzqZ = context.getApplicationContext();
        this.mHandler = new Handler(context.getMainLooper(), this);
    }

    private boolean zza(zza zzaVar, ServiceConnection serviceConnection, String str) {
        boolean zIsBound;
        zzx.zzb(serviceConnection, "ServiceConnection must not be null");
        synchronized (this.zzafY) {
            zzb zzbVar = this.zzafY.get(zzaVar);
            if (zzbVar != null) {
                this.mHandler.removeMessages(0, zzbVar);
                if (!zzbVar.zza(serviceConnection)) {
                    zzbVar.zza(serviceConnection, str);
                    switch (zzbVar.getState()) {
                        case 1:
                            serviceConnection.onServiceConnected(zzbVar.getComponentName(), zzbVar.getBinder());
                            break;
                        case 2:
                            zzbVar.zzcm(str);
                            break;
                    }
                } else {
                    throw new IllegalStateException("Trying to bind a GmsServiceConnection that was already connected before.  config=" + zzaVar);
                }
            } else {
                zzbVar = new zzb(zzaVar);
                zzbVar.zza(serviceConnection, str);
                zzbVar.zzcm(str);
                this.zzafY.put(zzaVar, zzbVar);
            }
            zIsBound = zzbVar.isBound();
        }
        return zIsBound;
    }

    private void zzb(zza zzaVar, ServiceConnection serviceConnection, String str) {
        zzx.zzb(serviceConnection, "ServiceConnection must not be null");
        synchronized (this.zzafY) {
            zzb zzbVar = this.zzafY.get(zzaVar);
            if (zzbVar == null) {
                throw new IllegalStateException("Nonexistent connection status for service config: " + zzaVar);
            }
            if (!zzbVar.zza(serviceConnection)) {
                throw new IllegalStateException("Trying to unbind a GmsServiceConnection  that was not bound before.  config=" + zzaVar);
            }
            zzbVar.zzb(serviceConnection, str);
            if (zzbVar.zzpn()) {
                this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(0, zzbVar), this.zzaga);
            }
        }
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        if (message.what != 0) {
            return false;
        }
        zzb zzbVar = (zzb) message.obj;
        synchronized (this.zzafY) {
            if (zzbVar.zzpn()) {
                if (zzbVar.isBound()) {
                    zzbVar.zzcn("GmsClientSupervisor");
                }
                this.zzafY.remove(zzbVar.zzagf);
            }
        }
        return true;
    }

    @Override // com.google.android.gms.common.internal.zzl
    public boolean zza(ComponentName componentName, ServiceConnection serviceConnection, String str) {
        return zza(new zza(componentName), serviceConnection, str);
    }

    @Override // com.google.android.gms.common.internal.zzl
    public boolean zza(String str, ServiceConnection serviceConnection, String str2) {
        return zza(new zza(str), serviceConnection, str2);
    }

    @Override // com.google.android.gms.common.internal.zzl
    public void zzb(ComponentName componentName, ServiceConnection serviceConnection, String str) {
        zzb(new zza(componentName), serviceConnection, str);
    }

    @Override // com.google.android.gms.common.internal.zzl
    public void zzb(String str, ServiceConnection serviceConnection, String str2) {
        zzb(new zza(str), serviceConnection, str2);
    }
}
