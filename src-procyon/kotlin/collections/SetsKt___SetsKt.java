// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.collections;

import kotlin.internal.InlineOnly;
import kotlin.sequences.Sequence;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Collection;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import java.util.Set;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u001c\n\u0000\n\u0002\u0010\"\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a,\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\u0086\u0002¢\u0006\u0002\u0010\u0004\u001a4\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u000e\u0010\u0005\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0006H\u0086\u0002¢\u0006\u0002\u0010\u0007\u001a-\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0086\u0002\u001a-\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\tH\u0086\u0002\u001a,\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\u0087\b¢\u0006\u0002\u0010\u0004\u001a,\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\u0086\u0002¢\u0006\u0002\u0010\u0004\u001a4\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u000e\u0010\u0005\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0006H\u0086\u0002¢\u0006\u0002\u0010\u0007\u001a-\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0086\u0002\u001a-\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\tH\u0086\u0002\u001a,\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\u0087\b¢\u0006\u0002\u0010\u0004¨\u0006\r" }, d2 = { "minus", "", "T", "element", "(Ljava/util/Set;Ljava/lang/Object;)Ljava/util/Set;", "elements", "", "(Ljava/util/Set;[Ljava/lang/Object;)Ljava/util/Set;", "", "Lkotlin/sequences/Sequence;", "minusElement", "plus", "plusElement", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/collections/SetsKt")
class SetsKt___SetsKt extends SetsKt__SetsKt
{
    public SetsKt___SetsKt() {
    }
    
    @NotNull
    public static final <T> Set<T> minus(@NotNull final Set<? extends T> set, @NotNull final Iterable<? extends T> iterable) {
        Intrinsics.checkParameterIsNotNull(set, "$receiver");
        Intrinsics.checkParameterIsNotNull(iterable, "elements");
        final Iterable iterable2 = set;
        final Collection<Object> convertToSetForSetOperationWith = CollectionsKt__IterablesKt.convertToSetForSetOperationWith((Iterable<?>)iterable, (Iterable<?>)iterable2);
        if (convertToSetForSetOperationWith.isEmpty()) {
            return (Set<T>)CollectionsKt___CollectionsKt.toSet((Iterable<?>)iterable2);
        }
        if (convertToSetForSetOperationWith instanceof Set) {
            final Collection collection = new LinkedHashSet();
            for (final Object next : iterable2) {
                if (!convertToSetForSetOperationWith.contains(next)) {
                    collection.add(next);
                }
            }
            return (Set<T>)collection;
        }
        final LinkedHashSet set2 = new LinkedHashSet((Collection<? extends E>)set);
        set2.removeAll(convertToSetForSetOperationWith);
        return set2;
    }
    
    @NotNull
    public static final <T> Set<T> minus(@NotNull final Set<? extends T> set, final T t) {
        Intrinsics.checkParameterIsNotNull(set, "$receiver");
        final LinkedHashSet set2 = new LinkedHashSet(MapsKt__MapsKt.mapCapacity(set.size()));
        final Iterator iterator = set.iterator();
        int n = 0;
        while (iterator.hasNext()) {
            final Object next = iterator.next();
            final boolean b = true;
            int n2 = n;
            int n3 = b ? 1 : 0;
            if (n == 0) {
                n2 = n;
                n3 = (b ? 1 : 0);
                if (Intrinsics.areEqual(next, t)) {
                    n2 = 1;
                    n3 = 0;
                }
            }
            n = n2;
            if (n3 != 0) {
                set2.add(next);
                n = n2;
            }
        }
        return set2;
    }
    
    @NotNull
    public static final <T> Set<T> minus(@NotNull final Set<? extends T> set, @NotNull final Sequence<? extends T> sequence) {
        Intrinsics.checkParameterIsNotNull(set, "$receiver");
        Intrinsics.checkParameterIsNotNull(sequence, "elements");
        final LinkedHashSet set2 = new LinkedHashSet((Collection<? extends E>)set);
        CollectionsKt__MutableCollectionsKt.removeAll((Collection<? super Object>)set2, (Sequence<?>)sequence);
        return set2;
    }
    
    @NotNull
    public static final <T> Set<T> minus(@NotNull final Set<? extends T> set, @NotNull final T[] array) {
        Intrinsics.checkParameterIsNotNull(set, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "elements");
        final LinkedHashSet set2 = new LinkedHashSet((Collection<? extends E>)set);
        CollectionsKt__MutableCollectionsKt.removeAll(set2, array);
        return set2;
    }
    
    @InlineOnly
    private static final <T> Set<T> minusElement(@NotNull final Set<? extends T> set, final T t) {
        return (Set<T>)minus(set, (Object)t);
    }
    
    @NotNull
    public static final <T> Set<T> plus(@NotNull final Set<? extends T> set, @NotNull final Iterable<? extends T> iterable) {
        Intrinsics.checkParameterIsNotNull(set, "$receiver");
        Intrinsics.checkParameterIsNotNull(iterable, "elements");
        final Integer collectionSizeOrNull = CollectionsKt__IterablesKt.collectionSizeOrNull((Iterable<?>)iterable);
        int n;
        if (collectionSizeOrNull != null) {
            n = set.size() + collectionSizeOrNull.intValue();
        }
        else {
            n = set.size() * 2;
        }
        final LinkedHashSet set2 = new LinkedHashSet<Object>(MapsKt__MapsKt.mapCapacity(n));
        set2.addAll((Collection<?>)set);
        CollectionsKt__MutableCollectionsKt.addAll((Collection<? super Object>)set2, (Iterable<?>)iterable);
        return (Set<T>)set2;
    }
    
    @NotNull
    public static final <T> Set<T> plus(@NotNull final Set<? extends T> set, final T e) {
        Intrinsics.checkParameterIsNotNull(set, "$receiver");
        final LinkedHashSet set2 = new LinkedHashSet(MapsKt__MapsKt.mapCapacity(set.size() + 1));
        set2.addAll(set);
        set2.add(e);
        return set2;
    }
    
    @NotNull
    public static final <T> Set<T> plus(@NotNull final Set<? extends T> set, @NotNull final Sequence<? extends T> sequence) {
        Intrinsics.checkParameterIsNotNull(set, "$receiver");
        Intrinsics.checkParameterIsNotNull(sequence, "elements");
        final LinkedHashSet set2 = new LinkedHashSet(MapsKt__MapsKt.mapCapacity(set.size() * 2));
        set2.addAll(set);
        CollectionsKt__MutableCollectionsKt.addAll((Collection<? super Object>)set2, (Sequence<?>)sequence);
        return set2;
    }
    
    @NotNull
    public static final <T> Set<T> plus(@NotNull final Set<? extends T> set, @NotNull final T[] array) {
        Intrinsics.checkParameterIsNotNull(set, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "elements");
        final LinkedHashSet set2 = new LinkedHashSet(MapsKt__MapsKt.mapCapacity(set.size() + array.length));
        set2.addAll(set);
        CollectionsKt__MutableCollectionsKt.addAll(set2, array);
        return set2;
    }
    
    @InlineOnly
    private static final <T> Set<T> plusElement(@NotNull final Set<? extends T> set, final T t) {
        return (Set<T>)plus(set, (Object)t);
    }
}
