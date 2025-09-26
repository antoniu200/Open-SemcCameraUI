// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.aidl;

import android.os.RemoteException;
import android.os.Parcel;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Binder;

public abstract class BaseStub extends Binder implements IInterface
{
    private static TransactionInterceptor globalInterceptor;
    
    protected BaseStub(final String s) {
        this.attachInterface((IInterface)this, s);
    }
    
    static void installTransactionInterceptorPackagePrivate(final TransactionInterceptor globalInterceptor) {
        monitorenter(BaseStub.class);
        Label_0023: {
            if (globalInterceptor == null) {
                Label_0049: {
                    try {
                        throw new IllegalArgumentException("null interceptor");
                    }
                    finally {
                        break Label_0049;
                    }
                    break Label_0023;
                }
                monitorexit(BaseStub.class);
            }
        }
        if (BaseStub.globalInterceptor != null) {
            throw new IllegalStateException("Duplicate TransactionInterceptor installation.");
        }
        BaseStub.globalInterceptor = globalInterceptor;
        monitorexit(BaseStub.class);
    }
    
    public IBinder asBinder() {
        return (IBinder)this;
    }
    
    protected boolean dispatchTransaction(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
        return false;
    }
    
    public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
        if (this.routeToSuperOrEnforceInterface(n, parcel, parcel2, n2)) {
            return true;
        }
        if (BaseStub.globalInterceptor == null) {
            return this.dispatchTransaction(n, parcel, parcel2, n2);
        }
        return BaseStub.globalInterceptor.interceptTransaction(this, n, parcel, parcel2, n2);
    }
    
    protected boolean routeToSuperOrEnforceInterface(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
        if (n > 16777215) {
            return super.onTransact(n, parcel, parcel2, n2);
        }
        parcel.enforceInterface(this.getInterfaceDescriptor());
        return false;
    }
}
