// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.collections;

import kotlin.TuplesKt;
import kotlin.Pair;
import java.util.Collection;
import java.util.ArrayList;
import kotlin.jvm.internal.Intrinsics;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u0018\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a+\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u000e\b\u0001\u0012\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00030\u0003¢\u0006\u0002\u0010\u0004\u001aG\u0010\u0005\u001a\u001a\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00070\u00010\u0006\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0007*\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00070\u00060\u0003¢\u0006\u0002\u0010\b¨\u0006\t" }, d2 = { "flatten", "", "T", "", "([[Ljava/lang/Object;)Ljava/util/List;", "unzip", "Lkotlin/Pair;", "R", "([Lkotlin/Pair;)Lkotlin/Pair;", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/collections/ArraysKt")
class ArraysKt__ArraysKt extends ArraysKt__ArraysJVMKt
{
    public ArraysKt__ArraysKt() {
    }
    
    @NotNull
    public static final <T> List<T> flatten(@NotNull final T[][] array) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        final Object[] array2 = array;
        final int length = array2.length;
        final int n = 0;
        int i = 0;
        int initialCapacity = 0;
        while (i < length) {
            initialCapacity += ((Object[])array2[i]).length;
            ++i;
        }
        final ArrayList list = new ArrayList<Object>(initialCapacity);
        for (int length2 = array.length, j = n; j < length2; ++j) {
            CollectionsKt__MutableCollectionsKt.addAll((Collection<? super T>)list, array[j]);
        }
        return (List<T>)list;
    }
    
    @NotNull
    public static final <T, R> Pair<List<T>, List<R>> unzip(@NotNull final Pair<? extends T, ? extends R>[] array) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        final ArrayList list = new ArrayList(array.length);
        final ArrayList list2 = new ArrayList(array.length);
        for (final Pair<? extends T, ? extends R> pair : array) {
            list.add(pair.getFirst());
            list2.add(pair.getSecond());
        }
        return (Pair<List<T>, List<R>>)TuplesKt.to(list, list2);
    }
}
