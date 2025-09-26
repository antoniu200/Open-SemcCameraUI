package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class BinderWrapper implements Parcelable {
    public static final Parcelable.Creator<BinderWrapper> CREATOR = new Parcelable.Creator<BinderWrapper>() { // from class: com.google.android.gms.common.internal.BinderWrapper.1
        @Override // android.os.Parcelable.Creator
        /* renamed from: zzaj, reason: merged with bridge method [inline-methods] */
        public BinderWrapper createFromParcel(Parcel parcel) {
            return new BinderWrapper(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: zzbC, reason: merged with bridge method [inline-methods] */
        public BinderWrapper[] newArray(int i) {
            return new BinderWrapper[i];
        }
    };
    private IBinder zzaeJ;

    public BinderWrapper() {
        this.zzaeJ = null;
    }

    public BinderWrapper(IBinder iBinder) {
        this.zzaeJ = null;
        this.zzaeJ = iBinder;
    }

    private BinderWrapper(Parcel parcel) {
        this.zzaeJ = null;
        this.zzaeJ = parcel.readStrongBinder();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStrongBinder(this.zzaeJ);
    }
}
