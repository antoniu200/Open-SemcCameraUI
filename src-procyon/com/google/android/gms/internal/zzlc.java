// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import android.util.Pair;
import android.os.Message;
import android.os.Handler;
import java.util.concurrent.TimeUnit;
import android.util.Log;
import com.google.android.gms.common.api.Releasable;
import java.util.Iterator;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.api.GoogleApiClient;
import android.os.Looper;
import java.util.concurrent.CountDownLatch;
import com.google.android.gms.common.internal.zzq;
import com.google.android.gms.common.api.ResultCallback;
import java.util.ArrayList;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;

public abstract class zzlc<R extends Result> extends PendingResult<R>
{
    private boolean zzL;
    private volatile R zzaaX;
    private final Object zzabh;
    protected final zza<R> zzabi;
    private final ArrayList<PendingResult.zza> zzabj;
    private ResultCallback<? super R> zzabk;
    private volatile boolean zzabl;
    private boolean zzabm;
    private zzq zzabn;
    private Integer zzabo;
    private volatile zzlq<R> zzabp;
    private final CountDownLatch zzoS;
    
    @Deprecated
    protected zzlc(final Looper looper) {
        this.zzabh = new Object();
        this.zzoS = new CountDownLatch(1);
        this.zzabj = new ArrayList<PendingResult.zza>();
        this.zzabi = new zza<R>(looper);
    }
    
    protected zzlc(final GoogleApiClient googleApiClient) {
        this.zzabh = new Object();
        this.zzoS = new CountDownLatch(1);
        this.zzabj = new ArrayList<PendingResult.zza>();
        Looper looper;
        if (googleApiClient != null) {
            looper = googleApiClient.getLooper();
        }
        else {
            looper = Looper.getMainLooper();
        }
        this.zzabi = new zza<R>(looper);
    }
    
    private R get() {
        synchronized (this.zzabh) {
            zzx.zza(this.zzabl ^ true, (Object)"Result has already been consumed.");
            zzx.zza(this.isReady(), (Object)"Result is not ready.");
            final Result zzaaX = this.zzaaX;
            this.zzaaX = null;
            this.zzabk = null;
            this.zzabl = true;
            monitorexit(this.zzabh);
            this.zznL();
            return (R)zzaaX;
        }
    }
    
    private void zzc(final R zzaaX) {
        this.zzaaX = zzaaX;
        this.zzabn = null;
        this.zzoS.countDown();
        final Status status = this.zzaaX.getStatus();
        if (this.zzabk != null) {
            this.zzabi.zznM();
            if (!this.zzL) {
                this.zzabi.zza(this.zzabk, this.get());
            }
        }
        final Iterator<PendingResult.zza> iterator = this.zzabj.iterator();
        while (iterator.hasNext()) {
            iterator.next().zzt(status);
        }
        this.zzabj.clear();
    }
    
    public static void zzd(final Result obj) {
        if (obj instanceof Releasable) {
            try {
                ((Releasable)obj).release();
            }
            catch (final RuntimeException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unable to release ");
                sb.append(obj);
                Log.w("BasePendingResult", sb.toString(), (Throwable)ex);
            }
        }
    }
    
    @Override
    public final R await() {
        final Looper myLooper = Looper.myLooper();
        final Looper mainLooper = Looper.getMainLooper();
        final boolean b = false;
        zzx.zza(myLooper != mainLooper, (Object)"await must not be called on the UI thread");
        zzx.zza(this.zzabl ^ true, (Object)"Result has already been consumed");
        boolean b2 = b;
        if (this.zzabp == null) {
            b2 = true;
        }
        zzx.zza(b2, (Object)"Cannot await if then() has been called.");
        try {
            this.zzoS.await();
        }
        catch (final InterruptedException ex) {
            this.zzw(Status.zzabc);
        }
        zzx.zza(this.isReady(), (Object)"Result is not ready.");
        return this.get();
    }
    
    @Override
    public final R await(final long timeout, final TimeUnit unit) {
        final boolean b = false;
        zzx.zza(timeout <= 0L || Looper.myLooper() != Looper.getMainLooper(), (Object)"await must not be called on the UI thread when time is greater than zero.");
        zzx.zza(this.zzabl ^ true, (Object)"Result has already been consumed.");
        boolean b2 = b;
        if (this.zzabp == null) {
            b2 = true;
        }
        zzx.zza(b2, (Object)"Cannot await if then() has been called.");
        try {
            if (!this.zzoS.await(timeout, unit)) {
                this.zzw(Status.zzabe);
            }
        }
        catch (final InterruptedException ex) {
            this.zzw(Status.zzabc);
        }
        zzx.zza(this.isReady(), (Object)"Result is not ready.");
        return this.get();
    }
    
    @Override
    public void cancel() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        com/google/android/gms/internal/zzlc.zzabh:Ljava/lang/Object;
        //     4: astore_1       
        //     5: aload_1        
        //     6: monitorenter   
        //     7: aload_0        
        //     8: getfield        com/google/android/gms/internal/zzlc.zzL:Z
        //    11: ifne            73
        //    14: aload_0        
        //    15: getfield        com/google/android/gms/internal/zzlc.zzabl:Z
        //    18: ifeq            24
        //    21: goto            73
        //    24: aload_0        
        //    25: getfield        com/google/android/gms/internal/zzlc.zzabn:Lcom/google/android/gms/common/internal/zzq;
        //    28: astore_2       
        //    29: aload_2        
        //    30: ifnull          42
        //    33: aload_0        
        //    34: getfield        com/google/android/gms/internal/zzlc.zzabn:Lcom/google/android/gms/common/internal/zzq;
        //    37: invokeinterface com/google/android/gms/common/internal/zzq.cancel:()V
        //    42: aload_0        
        //    43: getfield        com/google/android/gms/internal/zzlc.zzaaX:Lcom/google/android/gms/common/api/Result;
        //    46: invokestatic    com/google/android/gms/internal/zzlc.zzd:(Lcom/google/android/gms/common/api/Result;)V
        //    49: aload_0        
        //    50: aconst_null    
        //    51: putfield        com/google/android/gms/internal/zzlc.zzabk:Lcom/google/android/gms/common/api/ResultCallback;
        //    54: aload_0        
        //    55: iconst_1       
        //    56: putfield        com/google/android/gms/internal/zzlc.zzL:Z
        //    59: aload_0        
        //    60: aload_0        
        //    61: getstatic       com/google/android/gms/common/api/Status.zzabf:Lcom/google/android/gms/common/api/Status;
        //    64: invokevirtual   com/google/android/gms/internal/zzlc.zzb:(Lcom/google/android/gms/common/api/Status;)Lcom/google/android/gms/common/api/Result;
        //    67: invokespecial   com/google/android/gms/internal/zzlc.zzc:(Lcom/google/android/gms/common/api/Result;)V
        //    70: aload_1        
        //    71: monitorexit    
        //    72: return         
        //    73: aload_1        
        //    74: monitorexit    
        //    75: return         
        //    76: astore_2       
        //    77: aload_1        
        //    78: monitorexit    
        //    79: aload_2        
        //    80: athrow         
        //    81: astore_2       
        //    82: goto            42
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                        
        //  -----  -----  -----  -----  ----------------------------
        //  7      21     76     81     Any
        //  24     29     76     81     Any
        //  33     42     81     85     Landroid/os/RemoteException;
        //  33     42     76     81     Any
        //  42     72     76     81     Any
        //  73     75     76     81     Any
        //  77     79     76     81     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0042:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2604)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:206)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:137)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:333)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:254)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:129)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public boolean isCanceled() {
        synchronized (this.zzabh) {
            return this.zzL;
        }
    }
    
    public final boolean isReady() {
        return this.zzoS.getCount() == 0L;
    }
    
    @Override
    public final void setResultCallback(final ResultCallback<? super R> zzabk) {
        final boolean zzabl = this.zzabl;
        boolean b = true;
        zzx.zza(zzabl ^ true, (Object)"Result has already been consumed.");
        synchronized (this.zzabh) {
            if (this.zzabp != null) {
                b = false;
            }
            zzx.zza(b, (Object)"Cannot set callbacks if then() has been called.");
            if (this.isCanceled()) {
                return;
            }
            if (this.isReady()) {
                this.zzabi.zza(zzabk, this.get());
            }
            else {
                this.zzabk = zzabk;
            }
        }
    }
    
    @Override
    public final void setResultCallback(final ResultCallback<? super R> zzabk, final long duration, final TimeUnit timeUnit) {
        final boolean zzabl = this.zzabl;
        boolean b = true;
        zzx.zza(zzabl ^ true, (Object)"Result has already been consumed.");
        synchronized (this.zzabh) {
            if (this.zzabp != null) {
                b = false;
            }
            zzx.zza(b, (Object)"Cannot set callbacks if then() has been called.");
            if (this.isCanceled()) {
                return;
            }
            if (this.isReady()) {
                this.zzabi.zza(zzabk, this.get());
            }
            else {
                this.zzabk = zzabk;
                this.zzabi.zza(this, timeUnit.toMillis(duration));
            }
        }
    }
    
    @Override
    public final void zza(final PendingResult.zza e) {
        final boolean zzabl = this.zzabl;
        boolean b = true;
        zzx.zza(zzabl ^ true, (Object)"Result has already been consumed.");
        if (e == null) {
            b = false;
        }
        zzx.zzb(b, (Object)"Callback cannot be null.");
        synchronized (this.zzabh) {
            if (this.isReady()) {
                e.zzt(this.zzaaX.getStatus());
            }
            else {
                this.zzabj.add(e);
            }
        }
    }
    
    protected final void zza(final zzq zzabn) {
        synchronized (this.zzabh) {
            this.zzabn = zzabn;
        }
    }
    
    protected abstract R zzb(final Status p0);
    
    public final void zzb(final R r) {
        synchronized (this.zzabh) {
            if (!this.zzabm && !this.zzL) {
                zzx.zza(this.isReady() ^ true, (Object)"Results have already been set");
                zzx.zza(this.zzabl ^ true, (Object)"Result has already been consumed");
                this.zzc(r);
                return;
            }
            zzd(r);
        }
    }
    
    @Override
    public Integer zznF() {
        return this.zzabo;
    }
    
    protected void zznL() {
    }
    
    public final void zzw(final Status status) {
        synchronized (this.zzabh) {
            if (!this.isReady()) {
                this.zzb(this.zzb(status));
                this.zzabm = true;
            }
        }
    }
    
    public static class zza<R extends Result> extends Handler
    {
        public zza() {
            this(Looper.getMainLooper());
        }
        
        public zza(final Looper looper) {
            super(looper);
        }
        
        public void handleMessage(final Message message) {
            switch (message.what) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Don't know how to handle message: ");
                    sb.append(message.what);
                    Log.wtf("BasePendingResult", sb.toString(), (Throwable)new Exception());
                    return;
                }
                case 2: {
                    ((zzlc)message.obj).zzw(Status.zzabe);
                    return;
                }
                case 1: {
                    final Pair pair = (Pair)message.obj;
                    this.zzb((ResultCallback<? super Result>)pair.first, (Result)pair.second);
                }
            }
        }
        
        public void zza(final ResultCallback<? super R> resultCallback, final R r) {
            this.sendMessage(this.obtainMessage(1, (Object)new Pair((Object)resultCallback, (Object)r)));
        }
        
        public void zza(final zzlc<R> zzlc, final long n) {
            this.sendMessageDelayed(this.obtainMessage(2, (Object)zzlc), n);
        }
        
        protected void zzb(final ResultCallback<? super R> resultCallback, final R r) {
            try {
                resultCallback.onResult(r);
            }
            catch (final RuntimeException ex) {
                zzlc.zzd(r);
                throw ex;
            }
        }
        
        public void zznM() {
            this.removeMessages(2);
        }
    }
}
