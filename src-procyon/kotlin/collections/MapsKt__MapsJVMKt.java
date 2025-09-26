// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.collections;

import java.util.Comparator;
import kotlin.internal.InlineOnly;
import java.util.Properties;
import java.util.TreeMap;
import java.util.SortedMap;
import java.util.Collections;
import java.util.Map;
import kotlin.Pair;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.functions.Function0;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.ConcurrentMap;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000D\n\u0000\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\u001a2\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0005\u001aY\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\b\"\u0004\b\u0001\u0010\u00032*\u0010\t\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00050\n\"\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0005¢\u0006\u0002\u0010\u000b\u001a@\u0010\f\u001a\u0002H\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\r2\u0006\u0010\u000e\u001a\u0002H\u00022\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0010H\u0086\b¢\u0006\u0002\u0010\u0011\u001a\u0019\u0010\u0012\u001a\u00020\u0013*\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00140\u0001H\u0087\b\u001a2\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001H\u0000\u001a1\u0010\u0016\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001H\u0081\b\u001a:\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\b\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\u001a@\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u000e\u0010\u0018\u001a\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0019¨\u0006\u001a" }, d2 = { "mapOf", "", "K", "V", "pair", "Lkotlin/Pair;", "sortedMapOf", "Ljava/util/SortedMap;", "", "pairs", "", "([Lkotlin/Pair;)Ljava/util/SortedMap;", "getOrPut", "Ljava/util/concurrent/ConcurrentMap;", "key", "defaultValue", "Lkotlin/Function0;", "(Ljava/util/concurrent/ConcurrentMap;Ljava/lang/Object;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "toProperties", "Ljava/util/Properties;", "", "toSingletonMap", "toSingletonMapOrSelf", "toSortedMap", "comparator", "Ljava/util/Comparator;", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/collections/MapsKt")
class MapsKt__MapsJVMKt extends MapsKt__MapWithDefaultKt
{
    public MapsKt__MapsJVMKt() {
    }
    
    public static final <K, V> V getOrPut(@NotNull final ConcurrentMap<K, V> concurrentMap, final K k, @NotNull final Function0<? extends V> function0) {
        Intrinsics.checkParameterIsNotNull(concurrentMap, "$receiver");
        Intrinsics.checkParameterIsNotNull(function0, "defaultValue");
        final V value = concurrentMap.get(k);
        V v;
        if (value != null) {
            v = value;
        }
        else {
            final V invoke = (V)function0.invoke();
            final V putIfAbsent = concurrentMap.putIfAbsent((K)k, invoke);
            v = invoke;
            if (putIfAbsent != null) {
                v = putIfAbsent;
            }
        }
        return v;
    }
    
    @NotNull
    public static final <K, V> Map<K, V> mapOf(@NotNull final Pair<? extends K, ? extends V> pair) {
        Intrinsics.checkParameterIsNotNull(pair, "pair");
        final Map<? extends K, ? extends V> singletonMap = Collections.singletonMap(pair.getFirst(), pair.getSecond());
        Intrinsics.checkExpressionValueIsNotNull(singletonMap, "java.util.Collections.si\u2026(pair.first, pair.second)");
        return (Map<K, V>)singletonMap;
    }
    
    @NotNull
    public static final <K extends Comparable<? super K>, V> SortedMap<K, V> sortedMapOf(@NotNull final Pair<? extends K, ? extends V>... array) {
        Intrinsics.checkParameterIsNotNull(array, "pairs");
        final TreeMap treeMap = new TreeMap();
        MapsKt__MapsKt.putAll((Map<? super Object, ? super Object>)treeMap, (Pair<?, ?>[])array);
        return treeMap;
    }
    
    @InlineOnly
    private static final Properties toProperties(@NotNull final Map<String, String> t) {
        final Properties properties = new Properties();
        properties.putAll(t);
        return properties;
    }
    
    @NotNull
    public static final <K, V> Map<K, V> toSingletonMap(@NotNull final Map<? extends K, ? extends V> map) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        final Map.Entry entry = (Map.Entry)map.entrySet().iterator().next();
        final Map<Object, V> singletonMap = Collections.singletonMap(entry.getKey(), entry.getValue());
        Intrinsics.checkExpressionValueIsNotNull(singletonMap, "java.util.Collections.singletonMap(key, value)");
        Intrinsics.checkExpressionValueIsNotNull(singletonMap, "with(entries.iterator().\u2026ingletonMap(key, value) }");
        return (Map<K, V>)singletonMap;
    }
    
    @InlineOnly
    private static final <K, V> Map<K, V> toSingletonMapOrSelf(@NotNull final Map<K, ? extends V> map) {
        return (Map<K, V>)toSingletonMap((Map<?, ?>)map);
    }
    
    @NotNull
    public static final <K extends Comparable<? super K>, V> SortedMap<K, V> toSortedMap(@NotNull final Map<? extends K, ? extends V> m) {
        Intrinsics.checkParameterIsNotNull(m, "$receiver");
        return new TreeMap<K, V>(m);
    }
    
    @NotNull
    public static final <K, V> SortedMap<K, V> toSortedMap(@NotNull final Map<? extends K, ? extends V> map, @NotNull final Comparator<? super K> comparator) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        final TreeMap treeMap = new TreeMap((Comparator<? super K>)comparator);
        treeMap.putAll(map);
        return treeMap;
    }
}
