// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.data;

import java.util.Iterator;
import java.util.ArrayList;

public final class FreezableUtils
{
    public static <T, E extends Freezable<T>> ArrayList<T> freeze(final ArrayList<E> list) {
        final ArrayList list2 = new ArrayList(list.size());
        for (int size = list.size(), i = 0; i < size; ++i) {
            list2.add(((E)list.get(i)).freeze());
        }
        return list2;
    }
    
    public static <T, E extends Freezable<T>> ArrayList<T> freeze(final E[] array) {
        final ArrayList list = new ArrayList(array.length);
        for (int i = 0; i < array.length; ++i) {
            list.add(array[i].freeze());
        }
        return list;
    }
    
    public static <T, E extends Freezable<T>> ArrayList<T> freezeIterable(final Iterable<E> iterable) {
        final ArrayList list = new ArrayList();
        final Iterator<E> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            list.add(((Freezable<Object>)iterator.next()).freeze());
        }
        return list;
    }
}
