// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.collections;

import kotlin.internal.InlineOnly;
import kotlin.PublishedApi;
import kotlin.jvm.functions.Function1;
import kotlin.SinceKotlin;
import java.util.Iterator;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Ref;
import java.util.LinkedHashMap;
import kotlin.jvm.internal.Intrinsics;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000&\n\u0000\n\u0002\u0010$\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010&\n\u0000\u001a0\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00030\u0001\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u0005H\u0007\u001aW\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\b0\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\t\"\u0004\b\u0002\u0010\b*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\t0\u00072\u001e\u0010\n\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\t0\f\u0012\u0004\u0012\u0002H\b0\u000bH\u0081\b¨\u0006\r" }, d2 = { "eachCount", "", "K", "", "T", "Lkotlin/collections/Grouping;", "mapValuesInPlace", "", "R", "V", "f", "Lkotlin/Function1;", "", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/collections/GroupingKt")
class GroupingKt__GroupingJVMKt
{
    public GroupingKt__GroupingJVMKt() {
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <T, K> Map<K, Integer> eachCount(@NotNull final Grouping<T, ? extends K> grouping) {
        Intrinsics.checkParameterIsNotNull(grouping, "$receiver");
        final Map map = new LinkedHashMap();
        final Iterator<T> sourceIterator = grouping.sourceIterator();
        while (sourceIterator.hasNext()) {
            final K key = (K)grouping.keyOf(sourceIterator.next());
            Object value = map.get(key);
            if (value == null && !map.containsKey(key)) {
                value = new Ref.IntRef();
            }
            final Ref.IntRef intRef = (Ref.IntRef)value;
            ++intRef.element;
            map.put(key, intRef);
        }
        for (final Map.Entry<K, Ref.IntRef> entry : map.entrySet()) {
            if (entry == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableMap.MutableEntry<K, R>");
            }
            TypeIntrinsics.asMutableMapEntry(entry).setValue(entry.getValue().element);
        }
        return TypeIntrinsics.asMutableMap(map);
    }
    
    @PublishedApi
    @InlineOnly
    private static final <K, V, R> Map<K, R> mapValuesInPlace(@NotNull final Map<K, V> map, final Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        for (final Map.Entry entry : map.entrySet()) {
            if (entry == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableMap.MutableEntry<K, R>");
            }
            TypeIntrinsics.asMutableMapEntry(entry).setValue(function1.invoke(entry));
        }
        if (map == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableMap<K, R>");
        }
        return TypeIntrinsics.asMutableMap(map);
    }
}
