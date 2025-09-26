// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.aidl;

import android.os.IBinder;
import android.os.IInterface;
import java.util.Map;
import java.util.List;
import android.os.Parcelable;
import android.os.Parcelable$Creator;
import java.util.HashMap;
import java.util.ArrayList;
import android.text.TextUtils;
import android.os.Parcel;

public class Codecs
{
    private static final ClassLoader CLASS_LOADER;
    private static final int PARCELABLE_NO_FLAGS = 0;
    
    static {
        CLASS_LOADER = Codecs.class.getClassLoader();
    }
    
    private Codecs() {
    }
    
    public static boolean createBoolean(final Parcel parcel) {
        return parcel.readInt() != 0;
    }
    
    public static CharSequence createCharSequence(final Parcel parcel) {
        if (parcel.readInt() == 0) {
            return null;
        }
        return (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
    }
    
    public static ArrayList createList(final Parcel parcel) {
        return parcel.readArrayList(Codecs.CLASS_LOADER);
    }
    
    public static HashMap createMap(final Parcel parcel) {
        return parcel.readHashMap(Codecs.CLASS_LOADER);
    }
    
    public static <T extends Parcelable> T createParcelable(final Parcel parcel, final Parcelable$Creator<T> parcelable$Creator) {
        if (parcel.readInt() == 0) {
            return null;
        }
        return (T)parcelable$Creator.createFromParcel(parcel);
    }
    
    public static void readList(final Parcel parcel, final List<?> list) {
        parcel.readList((List)list, Codecs.CLASS_LOADER);
    }
    
    public static void readMap(final Parcel parcel, final Map<?, ?> map) {
        parcel.readMap((Map)map, Codecs.CLASS_LOADER);
    }
    
    public static void writeBoolean(final Parcel parcel, final boolean b) {
        parcel.writeInt((int)(b ? 1 : 0));
    }
    
    public static void writeCharSequence(final Parcel parcel, final CharSequence charSequence) {
        if (charSequence != null) {
            parcel.writeInt(1);
            TextUtils.writeToParcel(charSequence, parcel, 0);
        }
        else {
            parcel.writeInt(0);
        }
    }
    
    public static void writeCharSequenceAsReturnValue(final Parcel parcel, final CharSequence charSequence) {
        if (charSequence != null) {
            parcel.writeInt(1);
            TextUtils.writeToParcel(charSequence, parcel, 1);
        }
        else {
            parcel.writeInt(0);
        }
    }
    
    public static void writeParcelable(final Parcel parcel, final Parcelable parcelable) {
        if (parcelable == null) {
            parcel.writeInt(0);
        }
        else {
            parcel.writeInt(1);
            parcelable.writeToParcel(parcel, 0);
        }
    }
    
    public static void writeParcelableAsReturnValue(final Parcel parcel, final Parcelable parcelable) {
        if (parcelable == null) {
            parcel.writeInt(0);
        }
        else {
            parcel.writeInt(1);
            parcelable.writeToParcel(parcel, 1);
        }
    }
    
    public static void writeStrongBinder(final Parcel parcel, final IInterface interface1) {
        if (interface1 == null) {
            parcel.writeStrongBinder((IBinder)null);
        }
        else {
            parcel.writeStrongBinder(interface1.asBinder());
        }
    }
}
