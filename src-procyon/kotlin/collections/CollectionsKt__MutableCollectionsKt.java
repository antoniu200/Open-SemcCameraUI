// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.collections;

import kotlin.ReplaceWith;
import kotlin.DeprecationLevel;
import kotlin.Deprecated;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.TypeCastException;
import java.util.RandomAccess;
import java.util.List;
import kotlin.jvm.functions.Function1;
import kotlin.sequences.Sequence;
import java.util.Iterator;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000R\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u001f\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001d\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0002\b\u0004\u001a-\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0005¢\u0006\u0002\u0010\u0006\u001a&\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\u001a&\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\b\u001a9\u0010\t\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\n2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\f2\u0006\u0010\r\u001a\u00020\u0001H\u0002¢\u0006\u0002\b\u000e\u001a9\u0010\t\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000f2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\f2\u0006\u0010\r\u001a\u00020\u0001H\u0002¢\u0006\u0002\b\u000e\u001a(\u0010\u0010\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u0006\u0010\u0012\u001a\u0002H\u0002H\u0087\n¢\u0006\u0002\u0010\u0013\u001a.\u0010\u0010\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005H\u0087\n¢\u0006\u0002\u0010\u0014\u001a)\u0010\u0010\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007H\u0087\n\u001a)\u0010\u0010\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0087\n\u001a(\u0010\u0015\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u0006\u0010\u0012\u001a\u0002H\u0002H\u0087\n¢\u0006\u0002\u0010\u0013\u001a.\u0010\u0015\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005H\u0087\n¢\u0006\u0002\u0010\u0014\u001a)\u0010\u0015\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007H\u0087\n\u001a)\u0010\u0015\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0087\n\u001a-\u0010\u0016\u001a\u00020\u0001\"\t\b\u0000\u0010\u0002¢\u0006\u0002\b\u0017*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u0006\u0010\u0012\u001a\u0002H\u0002H\u0087\b¢\u0006\u0002\u0010\u0018\u001a&\u0010\u0016\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000f2\u0006\u0010\u0019\u001a\u00020\u001aH\u0087\b¢\u0006\u0002\u0010\u001b\u001a-\u0010\u001c\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0005¢\u0006\u0002\u0010\u0006\u001a&\u0010\u001c\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\u001a&\u0010\u001c\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\b\u001a.\u0010\u001c\u001a\u00020\u0001\"\t\b\u0000\u0010\u0002¢\u0006\u0002\b\u0017*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u001dH\u0087\b\u001a*\u0010\u001c\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\n2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\f\u001a*\u0010\u001c\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000f2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\f\u001a-\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0005¢\u0006\u0002\u0010\u0006\u001a&\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\u001a&\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\b\u001a.\u0010\u001e\u001a\u00020\u0001\"\t\b\u0000\u0010\u0002¢\u0006\u0002\b\u0017*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u001dH\u0087\b\u001a*\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\n2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\f\u001a*\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000f2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\f\u001a\u0015\u0010\u001f\u001a\u00020\u0001*\u0006\u0012\u0002\b\u00030\u0003H\u0002¢\u0006\u0002\b ¨\u0006!" }, d2 = { "addAll", "", "T", "", "elements", "", "(Ljava/util/Collection;[Ljava/lang/Object;)Z", "", "Lkotlin/sequences/Sequence;", "filterInPlace", "", "predicate", "Lkotlin/Function1;", "predicateResultToRemove", "filterInPlace$CollectionsKt__MutableCollectionsKt", "", "minusAssign", "", "element", "(Ljava/util/Collection;Ljava/lang/Object;)V", "(Ljava/util/Collection;[Ljava/lang/Object;)V", "plusAssign", "remove", "Lkotlin/internal/OnlyInputTypes;", "(Ljava/util/Collection;Ljava/lang/Object;)Z", "index", "", "(Ljava/util/List;I)Ljava/lang/Object;", "removeAll", "", "retainAll", "retainNothing", "retainNothing$CollectionsKt__MutableCollectionsKt", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/collections/CollectionsKt")
class CollectionsKt__MutableCollectionsKt extends CollectionsKt__MutableCollectionsJVMKt
{
    public CollectionsKt__MutableCollectionsKt() {
    }
    
    public static final <T> boolean addAll(@NotNull final Collection<? super T> collection, @NotNull final Iterable<? extends T> iterable) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        Intrinsics.checkParameterIsNotNull(iterable, "elements");
        if (iterable instanceof Collection) {
            return collection.addAll((Collection<? extends T>)iterable);
        }
        boolean b = false;
        final Iterator<Object> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            if (collection.add((Object)iterator.next())) {
                b = true;
            }
        }
        return b;
    }
    
    public static final <T> boolean addAll(@NotNull final Collection<? super T> collection, @NotNull final Sequence<? extends T> sequence) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        Intrinsics.checkParameterIsNotNull(sequence, "elements");
        final Iterator<? extends T> iterator = sequence.iterator();
        boolean b = false;
        while (iterator.hasNext()) {
            if (collection.add((Object)iterator.next())) {
                b = true;
            }
        }
        return b;
    }
    
    public static final <T> boolean addAll(@NotNull final Collection<? super T> collection, @NotNull final T[] array) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "elements");
        return collection.addAll((Collection<? extends T>)ArraysKt___ArraysJvmKt.asList(array));
    }
    
    private static final <T> boolean filterInPlace$CollectionsKt__MutableCollectionsKt(@NotNull final Iterable<? extends T> iterable, final Function1<? super T, Boolean> function1, final boolean b) {
        final Iterator<? extends T> iterator = iterable.iterator();
        boolean b2 = false;
        while (iterator.hasNext()) {
            if (function1.invoke((Object)iterator.next()) == b) {
                iterator.remove();
                b2 = true;
            }
        }
        return b2;
    }
    
    private static final <T> boolean filterInPlace$CollectionsKt__MutableCollectionsKt(@NotNull final List<T> list, final Function1<? super T, Boolean> function1, final boolean b) {
        if (!(list instanceof RandomAccess)) {
            if (list == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableIterable<T>");
            }
            return filterInPlace$CollectionsKt__MutableCollectionsKt((Iterable<?>)TypeIntrinsics.asMutableIterable(list), (Function1<? super Object, Boolean>)function1, b);
        }
        else {
            final int lastIndex = CollectionsKt__CollectionsKt.getLastIndex((List<?>)list);
            int n3;
            if (lastIndex >= 0) {
                int n = 0;
                int n2 = 0;
                while (true) {
                    final T value = (T)list.get(n);
                    if (function1.invoke(value) != b) {
                        if (n2 != n) {
                            list.set(n2, (T)value);
                        }
                        ++n2;
                    }
                    n3 = n2;
                    if (n == lastIndex) {
                        break;
                    }
                    ++n;
                }
            }
            else {
                n3 = 0;
            }
            if (n3 < list.size()) {
                int lastIndex2 = CollectionsKt__CollectionsKt.getLastIndex((List<?>)list);
                if (lastIndex2 >= n3) {
                    while (true) {
                        list.remove(lastIndex2);
                        if (lastIndex2 == n3) {
                            break;
                        }
                        --lastIndex2;
                    }
                }
                return true;
            }
            return false;
        }
    }
    
    @InlineOnly
    private static final <T> void minusAssign(@NotNull final Collection<? super T> collection, final Iterable<? extends T> iterable) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        removeAll((Collection<? super Object>)collection, (Iterable<?>)iterable);
    }
    
    @InlineOnly
    private static final <T> void minusAssign(@NotNull final Collection<? super T> collection, final T t) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        collection.remove(t);
    }
    
    @InlineOnly
    private static final <T> void minusAssign(@NotNull final Collection<? super T> collection, final Sequence<? extends T> sequence) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        removeAll((Collection<? super Object>)collection, (Sequence<?>)sequence);
    }
    
    @InlineOnly
    private static final <T> void minusAssign(@NotNull final Collection<? super T> collection, final T[] array) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        removeAll(collection, array);
    }
    
    @InlineOnly
    private static final <T> void plusAssign(@NotNull final Collection<? super T> collection, final Iterable<? extends T> iterable) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        addAll((Collection<? super Object>)collection, (Iterable<?>)iterable);
    }
    
    @InlineOnly
    private static final <T> void plusAssign(@NotNull final Collection<? super T> collection, final T t) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        collection.add(t);
    }
    
    @InlineOnly
    private static final <T> void plusAssign(@NotNull final Collection<? super T> collection, final Sequence<? extends T> sequence) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        addAll((Collection<? super Object>)collection, (Sequence<?>)sequence);
    }
    
    @InlineOnly
    private static final <T> void plusAssign(@NotNull final Collection<? super T> collection, final T[] array) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        addAll(collection, array);
    }
    
    @Deprecated(level = DeprecationLevel.ERROR, message = "Use removeAt(index) instead.", replaceWith = @ReplaceWith(expression = "removeAt(index)", imports = {}))
    @InlineOnly
    private static final <T> T remove(@NotNull final List<T> list, final int n) {
        return list.remove(n);
    }
    
    @InlineOnly
    private static final <T> boolean remove(@NotNull final Collection<? extends T> collection, final T t) {
        if (collection == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableCollection<T>");
        }
        return TypeIntrinsics.asMutableCollection(collection).remove(t);
    }
    
    public static final <T> boolean removeAll(@NotNull final Iterable<? extends T> iterable, @NotNull final Function1<? super T, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(iterable, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        return filterInPlace$CollectionsKt__MutableCollectionsKt((Iterable<?>)iterable, (Function1<? super Object, Boolean>)function1, true);
    }
    
    public static final <T> boolean removeAll(@NotNull final Collection<? super T> collection, @NotNull final Iterable<? extends T> iterable) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        Intrinsics.checkParameterIsNotNull(iterable, "elements");
        return TypeIntrinsics.asMutableCollection(collection).removeAll(CollectionsKt__IterablesKt.convertToSetForSetOperationWith((Iterable<?>)iterable, (Iterable<?>)collection));
    }
    
    @InlineOnly
    private static final <T> boolean removeAll(@NotNull final Collection<? extends T> collection, final Collection<? extends T> collection2) {
        if (collection == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableCollection<T>");
        }
        return TypeIntrinsics.asMutableCollection(collection).removeAll(collection2);
    }
    
    public static final <T> boolean removeAll(@NotNull final Collection<? super T> collection, @NotNull final Sequence<? extends T> sequence) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        Intrinsics.checkParameterIsNotNull(sequence, "elements");
        final Collection collection2 = SequencesKt___SequencesKt.toHashSet((Sequence<?>)sequence);
        final boolean empty = collection2.isEmpty();
        boolean b = true;
        if (!(empty ^ true) || !collection.removeAll(collection2)) {
            b = false;
        }
        return b;
    }
    
    public static final <T> boolean removeAll(@NotNull final Collection<? super T> collection, @NotNull final T[] array) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "elements");
        final int length = array.length;
        final boolean b = false;
        final boolean b2 = length == 0;
        boolean b3 = b;
        if (b2 ^ true) {
            b3 = b;
            if (collection.removeAll(ArraysKt___ArraysKt.toHashSet(array))) {
                b3 = true;
            }
        }
        return b3;
    }
    
    public static final <T> boolean removeAll(@NotNull final List<T> list, @NotNull final Function1<? super T, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(list, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        return filterInPlace$CollectionsKt__MutableCollectionsKt(list, function1, true);
    }
    
    public static final <T> boolean retainAll(@NotNull final Iterable<? extends T> iterable, @NotNull final Function1<? super T, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(iterable, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        return filterInPlace$CollectionsKt__MutableCollectionsKt((Iterable<?>)iterable, (Function1<? super Object, Boolean>)function1, false);
    }
    
    public static final <T> boolean retainAll(@NotNull final Collection<? super T> collection, @NotNull final Iterable<? extends T> iterable) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        Intrinsics.checkParameterIsNotNull(iterable, "elements");
        return TypeIntrinsics.asMutableCollection(collection).retainAll(CollectionsKt__IterablesKt.convertToSetForSetOperationWith((Iterable<?>)iterable, (Iterable<?>)collection));
    }
    
    @InlineOnly
    private static final <T> boolean retainAll(@NotNull final Collection<? extends T> collection, final Collection<? extends T> collection2) {
        if (collection == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableCollection<T>");
        }
        return TypeIntrinsics.asMutableCollection(collection).retainAll(collection2);
    }
    
    public static final <T> boolean retainAll(@NotNull final Collection<? super T> collection, @NotNull final Sequence<? extends T> sequence) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        Intrinsics.checkParameterIsNotNull(sequence, "elements");
        final Collection collection2 = SequencesKt___SequencesKt.toHashSet((Sequence<?>)sequence);
        if (collection2.isEmpty() ^ true) {
            return collection.retainAll(collection2);
        }
        return retainNothing$CollectionsKt__MutableCollectionsKt(collection);
    }
    
    public static final <T> boolean retainAll(@NotNull final Collection<? super T> collection, @NotNull final T[] array) {
        Intrinsics.checkParameterIsNotNull(collection, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "elements");
        if (array.length == 0 ^ true) {
            return collection.retainAll(ArraysKt___ArraysKt.toHashSet(array));
        }
        return retainNothing$CollectionsKt__MutableCollectionsKt(collection);
    }
    
    public static final <T> boolean retainAll(@NotNull final List<T> list, @NotNull final Function1<? super T, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(list, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        return filterInPlace$CollectionsKt__MutableCollectionsKt(list, function1, false);
    }
    
    private static final boolean retainNothing$CollectionsKt__MutableCollectionsKt(@NotNull final Collection<?> collection) {
        final boolean empty = collection.isEmpty();
        collection.clear();
        return empty ^ true;
    }
}
