// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.internal;

import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;
import android.os.IBinder;
import android.content.Intent;
import android.content.ComponentName;
import android.os.Message;
import android.content.ServiceConnection;
import android.content.Context;
import com.google.android.gms.common.stats.zzb;
import java.util.HashMap;
import android.os.Handler;
import android.os.Handler$Callback;

final class zzm extends zzl implements Handler$Callback
{
    private final Handler mHandler;
    private final HashMap<zza, zzb> zzafY;
    private final com.google.android.gms.common.stats.zzb zzafZ;
    private final long zzaga;
    private final Context zzqZ;
    
    zzm(final Context context) {
        this.zzafY = new HashMap<zza, zzb>();
        this.zzqZ = context.getApplicationContext();
        this.mHandler = new Handler(context.getMainLooper(), (Handler$Callback)this);
        this.zzafZ = com.google.android.gms.common.stats.zzb.zzqh();
        this.zzaga = 5000L;
    }
    
    private boolean zza(final zza obj, final ServiceConnection serviceConnection, final String s) {
        zzx.zzb(serviceConnection, "ServiceConnection must not be null");
        synchronized (this.zzafY) {
            final zzb zzb = this.zzafY.get(obj);
            zzb zzb2 = null;
            if (zzb == null) {
                final zzb value = new zzb(obj);
                value.zza(serviceConnection, s);
                value.zzcm(s);
                this.zzafY.put(obj, value);
                zzb2 = value;
            }
            else {
                this.mHandler.removeMessages(0, (Object)zzb);
                if (zzb.zza(serviceConnection)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Trying to bind a GmsServiceConnection that was already connected before.  config=");
                    sb.append(obj);
                    throw new IllegalStateException(sb.toString());
                }
                zzb.zza(serviceConnection, s);
                switch (zzb.getState()) {
                    default: {
                        zzb2 = zzb;
                        break;
                    }
                    case 2: {
                        zzb.zzcm(s);
                        zzb2 = zzb;
                        break;
                    }
                    case 1: {
                        serviceConnection.onServiceConnected(zzb.getComponentName(), zzb.getBinder());
                        zzb2 = zzb;
                        break;
                    }
                }
            }
            return zzb2.isBound();
        }
    }
    
    private void zzb(final zza obj, final ServiceConnection serviceConnection, final String s) {
        zzx.zzb(serviceConnection, "ServiceConnection must not be null");
        synchronized (this.zzafY) {
            final zzb zzb = this.zzafY.get(obj);
            if (zzb == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Nonexistent connection status for service config: ");
                sb.append(obj);
                throw new IllegalStateException(sb.toString());
            }
            if (!zzb.zza(serviceConnection)) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Trying to unbind a GmsServiceConnection  that was not bound before.  config=");
                sb2.append(obj);
                throw new IllegalStateException(sb2.toString());
            }
            zzb.zzb(serviceConnection, s);
            if (zzb.zzpn()) {
                this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(0, (Object)zzb), this.zzaga);
            }
        }
    }
    
    public boolean handleMessage(final Message message) {
        if (message.what != 0) {
            return false;
        }
        final zzb zzb = (zzb)message.obj;
        synchronized (this.zzafY) {
            if (zzb.zzpn()) {
                if (zzb.isBound()) {
                    zzb.zzcn("GmsClientSupervisor");
                }
                this.zzafY.remove(zzb.zzagf);
            }
            return true;
        }
    }
    
    @Override
    public boolean zza(final ComponentName componentName, final ServiceConnection serviceConnection, final String s) {
        return this.zza(new zza(componentName), serviceConnection, s);
    }
    
    @Override
    public boolean zza(final String s, final ServiceConnection serviceConnection, final String s2) {
        return this.zza(new zza(s), serviceConnection, s2);
    }
    
    @Override
    public void zzb(final ComponentName componentName, final ServiceConnection serviceConnection, final String s) {
        this.zzb(new zza(componentName), serviceConnection, s);
    }
    
    @Override
    public void zzb(final String s, final ServiceConnection serviceConnection, final String s2) {
        this.zzb(new zza(s), serviceConnection, s2);
    }
    
    private static final class zza
    {
        private final String zzPp;
        private final ComponentName zzagb;
        
        public zza(final ComponentName componentName) {
            this.zzPp = null;
            this.zzagb = zzx.zzw(componentName);
        }
        
        public zza(final String s) {
            this.zzPp = zzx.zzcr(s);
            this.zzagb = null;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof zza)) {
                return false;
            }
            final zza zza = (zza)o;
            return zzw.equal(this.zzPp, zza.zzPp) && zzw.equal(this.zzagb, zza.zzagb);
        }
        
        @Override
        public int hashCode() {
            return zzw.hashCode(this.zzPp, this.zzagb);
        }
        
        @Override
        public String toString() {
            if (this.zzPp == null) {
                return this.zzagb.flattenToString();
            }
            return this.zzPp;
        }
        
        public Intent zzpm() {
            if (this.zzPp != null) {
                return new Intent(this.zzPp).setPackage("com.google.android.gms");
            }
            return new Intent().setComponent(this.zzagb);
        }
    }
    
    private final class zzb
    {
        private int mState;
        private IBinder zzaeJ;
        private ComponentName zzagb;
        private final zza zzagc;
        private final Set<ServiceConnection> zzagd;
        private boolean zzage;
        private final zzm.zza zzagf;
        final zzm zzagg;
        
        public zzb(final zzm zzagg, final zzm.zza zzagf) {
            this.zzagg = zzagg;
            this.zzagf = zzagf;
            this.zzagc = new zza();
            this.zzagd = new HashSet<ServiceConnection>();
            this.mState = 2;
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
        
        public void zza(final ServiceConnection serviceConnection, final String s) {
            this.zzagg.zzafZ.zza(this.zzagg.zzqZ, serviceConnection, s, this.zzagf.zzpm());
            this.zzagd.add(serviceConnection);
        }
        
        public boolean zza(final ServiceConnection serviceConnection) {
            return this.zzagd.contains(serviceConnection);
        }
        
        public void zzb(final ServiceConnection serviceConnection, final String s) {
            this.zzagg.zzafZ.zzb(this.zzagg.zzqZ, serviceConnection);
            this.zzagd.remove(serviceConnection);
        }
        
        public void zzcm(final String s) {
            this.mState = 3;
            if (this.zzage = this.zzagg.zzafZ.zza(this.zzagg.zzqZ, s, this.zzagf.zzpm(), (ServiceConnection)this.zzagc, 129)) {
                return;
            }
            this.mState = 2;
            try {
                this.zzagg.zzafZ.zza(this.zzagg.zzqZ, (ServiceConnection)this.zzagc);
            }
            catch (final IllegalArgumentException ex) {}
        }
        
        public void zzcn(final String s) {
            this.zzagg.zzafZ.zza(this.zzagg.zzqZ, (ServiceConnection)this.zzagc);
            this.zzage = false;
            this.mState = 2;
        }
        
        public boolean zzpn() {
            return this.zzagd.isEmpty();
        }
        
        public class zza implements ServiceConnection
        {
            final zzb zzagh;
            
            public zza(final zzb zzagh) {
                this.zzagh = zzagh;
            }
            
            public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
                synchronized (this.zzagh.zzagg.zzafY) {
                    this.zzagh.zzaeJ = binder;
                    this.zzagh.zzagb = componentName;
                    final Iterator iterator = this.zzagh.zzagd.iterator();
                    while (iterator.hasNext()) {
                        ((ServiceConnection)iterator.next()).onServiceConnected(componentName, binder);
                    }
                    this.zzagh.mState = 1;
                }
            }
            
            public void onServiceDisconnected(final ComponentName componentName) {
                synchronized (this.zzagh.zzagg.zzafY) {
                    this.zzagh.zzaeJ = null;
                    this.zzagh.zzagb = componentName;
                    final Iterator iterator = this.zzagh.zzagd.iterator();
                    while (iterator.hasNext()) {
                        ((ServiceConnection)iterator.next()).onServiceDisconnected(componentName);
                    }
                    this.zzagh.mState = 2;
                }
            }
        }
    }
}
