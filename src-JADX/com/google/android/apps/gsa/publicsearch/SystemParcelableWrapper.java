package com.google.android.apps.gsa.publicsearch;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class SystemParcelableWrapper implements Parcelable {
    public static final Parcelable.Creator<SystemParcelableWrapper> CREATOR = new Parcelable.Creator<SystemParcelableWrapper>() { // from class: com.google.android.apps.gsa.publicsearch.SystemParcelableWrapper.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SystemParcelableWrapper createFromParcel(Parcel source) {
            return new SystemParcelableWrapper(source.readParcelable(null));
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SystemParcelableWrapper[] newArray(int size) {
            return new SystemParcelableWrapper[size];
        }
    };
    private final Parcelable parcelable;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public SystemParcelableWrapper(Parcelable parcelable) {
        if (!isSystemParcelable(parcelable)) {
            throw new IllegalArgumentException("Only Android system classes can be passed in SystemParcelableWrapper.");
        }
        this.parcelable = parcelable;
    }

    public Parcelable getParcelable() {
        return this.parcelable;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.parcelable, flags);
    }

    static boolean isSystemParcelable(Parcelable parcelable) {
        return parcelable.getClass().getName().startsWith("android.os.");
    }
}
