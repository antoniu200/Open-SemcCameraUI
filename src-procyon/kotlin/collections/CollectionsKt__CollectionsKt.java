// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.collections;

import kotlin.ranges.IntRange;
import java.util.Comparator;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.internal.InlineOnly;
import kotlin.SinceKotlin;
import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.functions.Function1;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000p\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000f\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a@\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\u0004\b\u0000\u0010\u00072\u0006\u0010\f\u001a\u00020\u00062!\u0010\r\u001a\u001d\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u0011\u0012\u0004\u0012\u0002H\u00070\u000eH\u0087\b\u001a@\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0013\"\u0004\b\u0000\u0010\u00072\u0006\u0010\f\u001a\u00020\u00062!\u0010\r\u001a\u001d\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u0011\u0012\u0004\u0012\u0002H\u00070\u000eH\u0087\b\u001a\u001f\u0010\u0014\u001a\u0012\u0012\u0004\u0012\u0002H\u00070\u0015j\b\u0012\u0004\u0012\u0002H\u0007`\u0016\"\u0004\b\u0000\u0010\u0007H\u0087\b\u001a5\u0010\u0014\u001a\u0012\u0012\u0004\u0012\u0002H\u00070\u0015j\b\u0012\u0004\u0012\u0002H\u0007`\u0016\"\u0004\b\u0000\u0010\u00072\u0012\u0010\u0017\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00070\u0018\"\u0002H\u0007¢\u0006\u0002\u0010\u0019\u001a\u0012\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\u0004\b\u0000\u0010\u0007\u001a\u0015\u0010\u001b\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\u0004\b\u0000\u0010\u0007H\u0087\b\u001a+\u0010\u001b\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\u0004\b\u0000\u0010\u00072\u0012\u0010\u0017\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00070\u0018\"\u0002H\u0007¢\u0006\u0002\u0010\u001c\u001a%\u0010\u001d\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\b\b\u0000\u0010\u0007*\u00020\u001e2\b\u0010\u001f\u001a\u0004\u0018\u0001H\u0007¢\u0006\u0002\u0010 \u001a3\u0010\u001d\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\b\b\u0000\u0010\u0007*\u00020\u001e2\u0016\u0010\u0017\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u0001H\u00070\u0018\"\u0004\u0018\u0001H\u0007¢\u0006\u0002\u0010\u001c\u001a\u0015\u0010!\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0013\"\u0004\b\u0000\u0010\u0007H\u0087\b\u001a+\u0010!\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0013\"\u0004\b\u0000\u0010\u00072\u0012\u0010\u0017\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00070\u0018\"\u0002H\u0007¢\u0006\u0002\u0010\u001c\u001a%\u0010\"\u001a\u00020#2\u0006\u0010\f\u001a\u00020\u00062\u0006\u0010$\u001a\u00020\u00062\u0006\u0010%\u001a\u00020\u0006H\u0002¢\u0006\u0002\b&\u001a%\u0010'\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0002\"\u0004\b\u0000\u0010\u0007*\n\u0012\u0006\b\u0001\u0012\u0002H\u00070\u0018H\u0000¢\u0006\u0002\u0010(\u001aS\u0010)\u001a\u00020\u0006\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\b2\u0006\u0010\u001f\u001a\u0002H\u00072\u001a\u0010*\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00070+j\n\u0012\u0006\b\u0000\u0012\u0002H\u0007`,2\b\b\u0002\u0010$\u001a\u00020\u00062\b\b\u0002\u0010%\u001a\u00020\u0006¢\u0006\u0002\u0010-\u001a>\u0010)\u001a\u00020\u0006\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\b2\b\b\u0002\u0010$\u001a\u00020\u00062\b\b\u0002\u0010%\u001a\u00020\u00062\u0012\u0010.\u001a\u000e\u0012\u0004\u0012\u0002H\u0007\u0012\u0004\u0012\u00020\u00060\u000e\u001aE\u0010)\u001a\u00020\u0006\"\u000e\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070/*\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00070\b2\b\u0010\u001f\u001a\u0004\u0018\u0001H\u00072\b\b\u0002\u0010$\u001a\u00020\u00062\b\b\u0002\u0010%\u001a\u00020\u0006¢\u0006\u0002\u00100\u001ad\u00101\u001a\u00020\u0006\"\u0004\b\u0000\u0010\u0007\"\u000e\b\u0001\u00102*\b\u0012\u0004\u0012\u0002H20/*\b\u0012\u0004\u0012\u0002H\u00070\b2\b\u00103\u001a\u0004\u0018\u0001H22\b\b\u0002\u0010$\u001a\u00020\u00062\b\b\u0002\u0010%\u001a\u00020\u00062\u0016\b\u0004\u00104\u001a\u0010\u0012\u0004\u0012\u0002H\u0007\u0012\u0006\u0012\u0004\u0018\u0001H20\u000eH\u0086\b¢\u0006\u0002\u00105\u001a,\u00106\u001a\u000207\"\t\b\u0000\u0010\u0007¢\u0006\u0002\b8*\b\u0012\u0004\u0012\u0002H\u00070\u00022\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0002H\u0087\b\u001a\u0019\u00109\u001a\u000207\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u0002H\u0087\b\u001a\u001e\u0010:\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\bH\u0000\u001a!\u0010;\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0002\"\u0004\b\u0000\u0010\u0007*\n\u0012\u0004\u0012\u0002H\u0007\u0018\u00010\u0002H\u0087\b\u001a!\u0010;\u001a\b\u0012\u0004\u0012\u0002H\u00070\b\"\u0004\b\u0000\u0010\u0007*\n\u0012\u0004\u0012\u0002H\u0007\u0018\u00010\bH\u0087\b\"\u0019\u0010\u0000\u001a\u00020\u0001*\u0006\u0012\u0002\b\u00030\u00028F¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"!\u0010\u0005\u001a\u00020\u0006\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\b8F¢\u0006\u0006\u001a\u0004\b\t\u0010\n¨\u0006<" }, d2 = { "indices", "Lkotlin/ranges/IntRange;", "", "getIndices", "(Ljava/util/Collection;)Lkotlin/ranges/IntRange;", "lastIndex", "", "T", "", "getLastIndex", "(Ljava/util/List;)I", "List", "size", "init", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "index", "MutableList", "", "arrayListOf", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "elements", "", "([Ljava/lang/Object;)Ljava/util/ArrayList;", "emptyList", "listOf", "([Ljava/lang/Object;)Ljava/util/List;", "listOfNotNull", "", "element", "(Ljava/lang/Object;)Ljava/util/List;", "mutableListOf", "rangeCheck", "", "fromIndex", "toIndex", "rangeCheck$CollectionsKt__CollectionsKt", "asCollection", "([Ljava/lang/Object;)Ljava/util/Collection;", "binarySearch", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "(Ljava/util/List;Ljava/lang/Object;Ljava/util/Comparator;II)I", "comparison", "", "(Ljava/util/List;Ljava/lang/Comparable;II)I", "binarySearchBy", "K", "key", "selector", "(Ljava/util/List;Ljava/lang/Comparable;IILkotlin/jvm/functions/Function1;)I", "containsAll", "", "Lkotlin/internal/OnlyInputTypes;", "isNotEmpty", "optimizeReadOnlyList", "orEmpty", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/collections/CollectionsKt")
class CollectionsKt__CollectionsKt extends CollectionsKt__CollectionsJVMKt
{
    public CollectionsKt__CollectionsKt() {
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> List<T> List(final int initialCapacity, final Function1<? super Integer, ? extends T> function1) {
        final ArrayList list = new ArrayList(initialCapacity);
        for (int i = 0; i < initialCapacity; ++i) {
            list.add(function1.invoke(i));
        }
        return list;
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> List<T> MutableList(final int initialCapacity, final Function1<? super Integer, ? extends T> function1) {
        final ArrayList list = new ArrayList(initialCapacity);
        for (int i = 0; i < initialCapacity; ++i) {
            list.add(function1.invoke(i));
        }
        return list;
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> ArrayList<T> arrayListOf() {
        return new ArrayList<T>();
    }
    
    @NotNull
    public static final <T> ArrayList<T> arrayListOf(@NotNull final T... array) {
        Intrinsics.checkParameterIsNotNull(array, "elements");
        ArrayList list;
        if (array.length == 0) {
            list = new ArrayList();
        }
        else {
            list = new ArrayList((Collection<? extends E>)new ArrayAsCollection<Object>((E[])array, true));
        }
        return list;
    }
    
    @NotNull
    public static final <T> Collection<T> asCollection(@NotNull final T[] array) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        return new ArrayAsCollection<T>(array, false);
    }
    
    public static final <T> int binarySearch(@NotNull final List<? extends T> list, int i, int n, @NotNull final Function1<? super T, Integer> function1) {
        Intrinsics.checkParameterIsNotNull(list, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "comparison");
        rangeCheck$CollectionsKt__CollectionsKt(list.size(), i, n);
        --n;
        while (i <= n) {
            final int n2 = i + n >>> 1;
            final int intValue = function1.invoke(list.get(n2)).intValue();
            if (intValue < 0) {
                i = n2 + 1;
            }
            else {
                if (intValue <= 0) {
                    return n2;
                }
                n = n2 - 1;
            }
        }
        return -(i + 1);
    }
    
    public static final <T extends Comparable<? super T>> int binarySearch(@NotNull final List<? extends T> list, @Nullable final T t, int i, int n) {
        Intrinsics.checkParameterIsNotNull(list, "$receiver");
        rangeCheck$CollectionsKt__CollectionsKt(list.size(), i, n);
        --n;
        while (i <= n) {
            final int n2 = i + n >>> 1;
            final int compareValues = ComparisonsKt__ComparisonsKt.compareValues(list.get(n2), t);
            if (compareValues < 0) {
                i = n2 + 1;
            }
            else {
                if (compareValues <= 0) {
                    return n2;
                }
                n = n2 - 1;
            }
        }
        return -(i + 1);
    }
    
    public static final <T> int binarySearch(@NotNull final List<? extends T> list, final T t, @NotNull final Comparator<? super T> comparator, int i, int n) {
        Intrinsics.checkParameterIsNotNull(list, "$receiver");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        rangeCheck$CollectionsKt__CollectionsKt(list.size(), i, n);
        --n;
        while (i <= n) {
            final int n2 = i + n >>> 1;
            final int compare = comparator.compare(list.get(n2), t);
            if (compare < 0) {
                i = n2 + 1;
            }
            else {
                if (compare <= 0) {
                    return n2;
                }
                n = n2 - 1;
            }
        }
        return -(i + 1);
    }
    
    public static final <T, K extends Comparable<? super K>> int binarySearchBy(@NotNull final List<? extends T> list, @Nullable final K k, final int n, final int n2, @NotNull final Function1<? super T, ? extends K> function1) {
        Intrinsics.checkParameterIsNotNull(list, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "selector");
        return binarySearch((List<?>)list, n, n2, (Function1<? super Object, Integer>)new CollectionsKt__CollectionsKt$binarySearchBy.CollectionsKt__CollectionsKt$binarySearchBy$1((Function1)function1, (Comparable)k));
    }
    
    @InlineOnly
    private static final <T> boolean containsAll(@NotNull final Collection<? extends T> collection, final Collection<? extends T> collection2) {
        return collection.containsAll(collection2);
    }
    
    @NotNull
    public static final <T> List<T> emptyList() {
        return EmptyList.INSTANCE;
    }
    
    @NotNull
    public static final IntRange getIndices(@NotNull final Collection<?> collection) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        return new IntRange(0, collection.size() - 1);
    }
    
    public static final <T> int getLastIndex(@NotNull final List<? extends T> list) {
        Intrinsics.checkParameterIsNotNull(list, "$receiver");
        return list.size() - 1;
    }
    
    @InlineOnly
    private static final <T> boolean isNotEmpty(@NotNull final Collection<? extends T> collection) {
        return collection.isEmpty() ^ true;
    }
    
    @InlineOnly
    private static final <T> List<T> listOf() {
        return (List<T>)emptyList();
    }
    
    @NotNull
    public static final <T> List<T> listOf(@NotNull final T... array) {
        Intrinsics.checkParameterIsNotNull(array, "elements");
        Object o;
        if (array.length > 0) {
            o = ArraysKt___ArraysJvmKt.asList(array);
        }
        else {
            o = emptyList();
        }
        return (List<T>)o;
    }
    
    @NotNull
    public static final <T> List<T> listOfNotNull(@Nullable final T t) {
        Object o;
        if (t != null) {
            o = CollectionsKt__CollectionsJVMKt.listOf(t);
        }
        else {
            o = emptyList();
        }
        return (List<T>)o;
    }
    
    @NotNull
    public static final <T> List<T> listOfNotNull(@NotNull final T... array) {
        Intrinsics.checkParameterIsNotNull(array, "elements");
        return ArraysKt___ArraysKt.filterNotNull(array);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> List<T> mutableListOf() {
        return new ArrayList<T>();
    }
    
    @NotNull
    public static final <T> List<T> mutableListOf(@NotNull final T... array) {
        Intrinsics.checkParameterIsNotNull(array, "elements");
        List list;
        if (array.length == 0) {
            list = new ArrayList();
        }
        else {
            list = new ArrayList(new ArrayAsCollection<Object>(array, true));
        }
        return list;
    }
    
    @NotNull
    public static final <T> List<T> optimizeReadOnlyList(@NotNull List<? extends T> o) {
        Intrinsics.checkParameterIsNotNull(o, "$receiver");
        switch (((List)o).size()) {
            case 1: {
                o = CollectionsKt__CollectionsJVMKt.listOf(((List<T>)o).get(0));
                break;
            }
            case 0: {
                o = emptyList();
                break;
            }
        }
        return (List<T>)o;
    }
    
    @InlineOnly
    private static final <T> Collection<T> orEmpty(@Nullable Collection<? extends T> collection) {
        if (collection == null) {
            collection = emptyList();
        }
        return collection;
    }
    
    @InlineOnly
    private static final <T> List<T> orEmpty(@Nullable List<? extends T> emptyList) {
        if (emptyList == null) {
            emptyList = emptyList();
        }
        return emptyList;
    }
    
    private static final void rangeCheck$CollectionsKt__CollectionsKt(final int i, final int n, final int n2) {
        if (n > n2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("fromIndex (");
            sb.append(n);
            sb.append(") is greater than toIndex (");
            sb.append(n2);
            sb.append(").");
            throw new IllegalArgumentException(sb.toString());
        }
        if (n < 0) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("fromIndex (");
            sb2.append(n);
            sb2.append(") is less than zero.");
            throw new IndexOutOfBoundsException(sb2.toString());
        }
        if (n2 > i) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("toIndex (");
            sb3.append(n2);
            sb3.append(") is greater than size (");
            sb3.append(i);
            sb3.append(").");
            throw new IndexOutOfBoundsException(sb3.toString());
        }
    }
}
