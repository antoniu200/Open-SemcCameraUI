// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.aidl;

import android.os.RemoteException;
import android.os.Parcel;

interface TransactionInterceptor
{
    boolean interceptTransaction(final BaseStub p0, final int p1, final Parcel p2, final Parcel p3, final int p4) throws RemoteException;
}
