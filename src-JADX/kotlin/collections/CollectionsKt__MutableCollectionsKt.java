package kotlin.collections;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.TypeCastException;
import kotlin.internal.InlineOnly;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: MutableCollections.kt */
@Metadata(bv = {1, 0, 2}, d1 = {"\u0000R\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u001f\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001d\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0002\b\u0004\u001a-\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0005¢\u0006\u0002\u0010\u0006\u001a&\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\u001a&\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\b\u001a9\u0010\t\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\n2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\f2\u0006\u0010\r\u001a\u00020\u0001H\u0002¢\u0006\u0002\b\u000e\u001a9\u0010\t\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000f2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\f2\u0006\u0010\r\u001a\u00020\u0001H\u0002¢\u0006\u0002\b\u000e\u001a(\u0010\u0010\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u0006\u0010\u0012\u001a\u0002H\u0002H\u0087\n¢\u0006\u0002\u0010\u0013\u001a.\u0010\u0010\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005H\u0087\n¢\u0006\u0002\u0010\u0014\u001a)\u0010\u0010\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007H\u0087\n\u001a)\u0010\u0010\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0087\n\u001a(\u0010\u0015\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u0006\u0010\u0012\u001a\u0002H\u0002H\u0087\n¢\u0006\u0002\u0010\u0013\u001a.\u0010\u0015\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005H\u0087\n¢\u0006\u0002\u0010\u0014\u001a)\u0010\u0015\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007H\u0087\n\u001a)\u0010\u0015\u001a\u00020\u0011\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0087\n\u001a-\u0010\u0016\u001a\u00020\u0001\"\t\b\u0000\u0010\u0002¢\u0006\u0002\b\u0017*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u0006\u0010\u0012\u001a\u0002H\u0002H\u0087\b¢\u0006\u0002\u0010\u0018\u001a&\u0010\u0016\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000f2\u0006\u0010\u0019\u001a\u00020\u001aH\u0087\b¢\u0006\u0002\u0010\u001b\u001a-\u0010\u001c\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0005¢\u0006\u0002\u0010\u0006\u001a&\u0010\u001c\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\u001a&\u0010\u001c\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\b\u001a.\u0010\u001c\u001a\u00020\u0001\"\t\b\u0000\u0010\u0002¢\u0006\u0002\b\u0017*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u001dH\u0087\b\u001a*\u0010\u001c\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\n2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\f\u001a*\u0010\u001c\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000f2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\f\u001a-\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0005¢\u0006\u0002\u0010\u0006\u001a&\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\u001a&\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\b\u001a.\u0010\u001e\u001a\u00020\u0001\"\t\b\u0000\u0010\u0002¢\u0006\u0002\b\u0017*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u001dH\u0087\b\u001a*\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\n2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\f\u001a*\u0010\u001e\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000f2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\f\u001a\u0015\u0010\u001f\u001a\u00020\u0001*\u0006\u0012\u0002\b\u00030\u0003H\u0002¢\u0006\u0002\b ¨\u0006!"}, d2 = {"addAll", "", "T", "", "elements", "", "(Ljava/util/Collection;[Ljava/lang/Object;)Z", "", "Lkotlin/sequences/Sequence;", "filterInPlace", "", "predicate", "Lkotlin/Function1;", "predicateResultToRemove", "filterInPlace$CollectionsKt__MutableCollectionsKt", "", "minusAssign", "", "element", "(Ljava/util/Collection;Ljava/lang/Object;)V", "(Ljava/util/Collection;[Ljava/lang/Object;)V", "plusAssign", "remove", "Lkotlin/internal/OnlyInputTypes;", "(Ljava/util/Collection;Ljava/lang/Object;)Z", "index", "", "(Ljava/util/List;I)Ljava/lang/Object;", "removeAll", "", "retainAll", "retainNothing", "retainNothing$CollectionsKt__MutableCollectionsKt", "kotlin-stdlib"}, k = 5, mv = {1, 1, 10}, xi = 1, xs = "kotlin/collections/CollectionsKt")
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class CollectionsKt__MutableCollectionsKt extends CollectionsKt__MutableCollectionsJVMKt {
    @InlineOnly
    private static final <T> boolean remove(@NotNull Collection<? extends T> collection, T t) {
        if (collection == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableCollection<T>");
        }
        return TypeIntrinsics.asMutableCollection(collection).remove(t);
    }

    @InlineOnly
    private static final <T> boolean removeAll(@NotNull Collection<? extends T> collection, Collection<? extends T> collection2) {
        if (collection == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableCollection<T>");
        }
        return TypeIntrinsics.asMutableCollection(collection).removeAll(collection2);
    }

    @InlineOnly
    private static final <T> boolean retainAll(@NotNull Collection<? extends T> collection, Collection<? extends T> collection2) {
        if (collection == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableCollection<T>");
        }
        return TypeIntrinsics.asMutableCollection(collection).retainAll(collection2);
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Use removeAt(index) instead.", replaceWith = @ReplaceWith(expression = "removeAt(index)", imports = {}))
    @InlineOnly
    private static final <T> T remove(@NotNull List<T> list, int i) {
        return list.remove(i);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @InlineOnly
    private static final <T> void plusAssign(@NotNull Collection<? super T> receiver, T t) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        receiver.add(t);
    }

    @InlineOnly
    private static final <T> void plusAssign(@NotNull Collection<? super T> receiver, Iterable<? extends T> iterable) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        CollectionsKt.addAll(receiver, iterable);
    }

    @InlineOnly
    private static final <T> void plusAssign(@NotNull Collection<? super T> receiver, T[] tArr) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        CollectionsKt.addAll(receiver, tArr);
    }

    @InlineOnly
    private static final <T> void plusAssign(@NotNull Collection<? super T> receiver, Sequence<? extends T> sequence) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        CollectionsKt.addAll(receiver, sequence);
    }

    @InlineOnly
    private static final <T> void minusAssign(@NotNull Collection<? super T> receiver, T t) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        receiver.remove(t);
    }

    @InlineOnly
    private static final <T> void minusAssign(@NotNull Collection<? super T> receiver, Iterable<? extends T> iterable) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        CollectionsKt.removeAll(receiver, iterable);
    }

    @InlineOnly
    private static final <T> void minusAssign(@NotNull Collection<? super T> receiver, T[] tArr) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        CollectionsKt.removeAll(receiver, tArr);
    }

    @InlineOnly
    private static final <T> void minusAssign(@NotNull Collection<? super T> receiver, Sequence<? extends T> sequence) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        CollectionsKt.removeAll(receiver, sequence);
    }

    public static final <T> boolean addAll(@NotNull Collection<? super T> receiver, @NotNull Iterable<? extends T> elements) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        if (elements instanceof Collection) {
            return receiver.addAll((Collection) elements);
        }
        boolean z = false;
        Iterator<? extends T> it = elements.iterator();
        while (it.hasNext()) {
            if (receiver.add(it.next())) {
                z = true;
            }
        }
        return z;
    }

    public static final <T> boolean addAll(@NotNull Collection<? super T> receiver, @NotNull Sequence<? extends T> elements) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        Iterator<? extends T> it = elements.iterator();
        boolean z = false;
        while (it.hasNext()) {
            if (receiver.add(it.next())) {
                z = true;
            }
        }
        return z;
    }

    public static final <T> boolean addAll(@NotNull Collection<? super T> receiver, @NotNull T[] elements) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        return receiver.addAll(ArraysKt.asList(elements));
    }

    public static final <T> boolean removeAll(@NotNull Iterable<? extends T> receiver, @NotNull Function1<? super T, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        return filterInPlace$CollectionsKt__MutableCollectionsKt((Iterable) receiver, (Function1) predicate, true);
    }

    public static final <T> boolean retainAll(@NotNull Iterable<? extends T> receiver, @NotNull Function1<? super T, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        return filterInPlace$CollectionsKt__MutableCollectionsKt((Iterable) receiver, (Function1) predicate, false);
    }

    private static final <T> boolean filterInPlace$CollectionsKt__MutableCollectionsKt(@NotNull Iterable<? extends T> iterable, Function1<? super T, Boolean> function1, boolean z) {
        Iterator<? extends T> it = iterable.iterator();
        boolean z2 = false;
        while (it.hasNext()) {
            if (function1.invoke(it.next()).booleanValue() == z) {
                it.remove();
                z2 = true;
            }
        }
        return z2;
    }

    public static final <T> boolean removeAll(@NotNull List<T> receiver, @NotNull Function1<? super T, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        return filterInPlace$CollectionsKt__MutableCollectionsKt((List) receiver, (Function1) predicate, true);
    }

    public static final <T> boolean retainAll(@NotNull List<T> receiver, @NotNull Function1<? super T, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        return filterInPlace$CollectionsKt__MutableCollectionsKt((List) receiver, (Function1) predicate, false);
    }

    private static final <T> boolean filterInPlace$CollectionsKt__MutableCollectionsKt(@NotNull List<T> list, Function1<? super T, Boolean> function1, boolean z) {
        int i;
        if (!(list instanceof RandomAccess)) {
            if (list == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableIterable<T>");
            }
            return filterInPlace$CollectionsKt__MutableCollectionsKt(TypeIntrinsics.asMutableIterable(list), function1, z);
        }
        int lastIndex = CollectionsKt.getLastIndex(list);
        if (lastIndex >= 0) {
            int i2 = 0;
            i = 0;
            while (true) {
                T t = list.get(i2);
                if (function1.invoke(t).booleanValue() != z) {
                    if (i != i2) {
                        list.set(i, t);
                    }
                    i++;
                }
                if (i2 == lastIndex) {
                    break;
                }
                i2++;
            }
        } else {
            i = 0;
        }
        if (i >= list.size()) {
            return false;
        }
        int lastIndex2 = CollectionsKt.getLastIndex(list);
        if (lastIndex2 < i) {
            return true;
        }
        while (true) {
            list.remove(lastIndex2);
            if (lastIndex2 == i) {
                return true;
            }
            lastIndex2--;
        }
    }

    public static final <T> boolean removeAll(@NotNull Collection<? super T> receiver, @NotNull Iterable<? extends T> elements) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        return TypeIntrinsics.asMutableCollection(receiver).removeAll(CollectionsKt.convertToSetForSetOperationWith(elements, receiver));
    }

    public static final <T> boolean removeAll(@NotNull Collection<? super T> receiver, @NotNull Sequence<? extends T> elements) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        HashSet hashSet = SequencesKt.toHashSet(elements);
        return (hashSet.isEmpty() ^ true) && receiver.removeAll(hashSet);
    }

    public static final <T> boolean removeAll(@NotNull Collection<? super T> receiver, @NotNull T[] elements) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        return ((elements.length == 0) ^ true) && receiver.removeAll(ArraysKt.toHashSet(elements));
    }

    public static final <T> boolean retainAll(@NotNull Collection<? super T> receiver, @NotNull Iterable<? extends T> elements) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        return TypeIntrinsics.asMutableCollection(receiver).retainAll(CollectionsKt.convertToSetForSetOperationWith(elements, receiver));
    }

    public static final <T> boolean retainAll(@NotNull Collection<? super T> receiver, @NotNull T[] elements) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        if (!(elements.length == 0)) {
            return receiver.retainAll(ArraysKt.toHashSet(elements));
        }
        return retainNothing$CollectionsKt__MutableCollectionsKt(receiver);
    }

    public static final <T> boolean retainAll(@NotNull Collection<? super T> receiver, @NotNull Sequence<? extends T> elements) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        HashSet hashSet = SequencesKt.toHashSet(elements);
        if (!hashSet.isEmpty()) {
            return receiver.retainAll(hashSet);
        }
        return retainNothing$CollectionsKt__MutableCollectionsKt(receiver);
    }

    private static final boolean retainNothing$CollectionsKt__MutableCollectionsKt(@NotNull Collection<?> collection) {
        boolean z = !collection.isEmpty();
        collection.clear();
        return z;
    }
}
