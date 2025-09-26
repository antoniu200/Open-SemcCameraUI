// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.collections;

import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function2;
import kotlin.SinceKotlin;
import java.util.Iterator;
import java.util.LinkedHashMap;
import kotlin.jvm.internal.Intrinsics;
import java.util.Map;
import kotlin.jvm.functions.Function4;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000@\n\u0000\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010%\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\n\u001a\u009b\u0001\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0004\b\u0002\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052b\u0010\u0006\u001a^\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0015\u0012\u0013\u0018\u0001H\u0003¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0013\u0012\u00110\r¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000e\u0012\u0004\u0012\u0002H\u00030\u0007H\u0087\b\u001a´\u0001\u0010\u000f\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0004\b\u0002\u0010\u0003\"\u0016\b\u0003\u0010\u0010*\u0010\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0011*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\u0012\u001a\u0002H\u00102b\u0010\u0006\u001a^\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0015\u0012\u0013\u0018\u0001H\u0003¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0013\u0012\u00110\r¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000e\u0012\u0004\u0012\u0002H\u00030\u0007H\u0087\b¢\u0006\u0002\u0010\u0013\u001aI\u0010\u0014\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0016\b\u0002\u0010\u0010*\u0010\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00150\u0011*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\u0012\u001a\u0002H\u0010H\u0007¢\u0006\u0002\u0010\u0016\u001a¼\u0001\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0004\b\u0002\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u000526\u0010\u0018\u001a2\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H\u00030\u00192K\u0010\u0006\u001aG\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u0011H\u0003¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H\u00030\u001aH\u0087\b\u001a|\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0004\b\u0002\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\u001b\u001a\u0002H\u000326\u0010\u0006\u001a2\u0012\u0013\u0012\u0011H\u0003¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H\u00030\u0019H\u0087\b¢\u0006\u0002\u0010\u001c\u001a\u00d5\u0001\u0010\u001d\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0004\b\u0002\u0010\u0003\"\u0016\b\u0003\u0010\u0010*\u0010\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0011*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\u0012\u001a\u0002H\u001026\u0010\u0018\u001a2\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H\u00030\u00192K\u0010\u0006\u001aG\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u0011H\u0003¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H\u00030\u001aH\u0087\b¢\u0006\u0002\u0010\u001e\u001a\u0090\u0001\u0010\u001d\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0004\b\u0002\u0010\u0003\"\u0016\b\u0003\u0010\u0010*\u0010\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0011*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\u0012\u001a\u0002H\u00102\u0006\u0010\u001b\u001a\u0002H\u000326\u0010\u0006\u001a2\u0012\u0013\u0012\u0011H\u0003¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H\u00030\u0019H\u0087\b¢\u0006\u0002\u0010\u001f\u001a\u0088\u0001\u0010 \u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H!0\u0001\"\u0004\b\u0000\u0010!\"\b\b\u0001\u0010\u0004*\u0002H!\"\u0004\b\u0002\u0010\u0002*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052K\u0010\u0006\u001aG\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u0011H!¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H!0\u001aH\u0087\b\u001a¡\u0001\u0010\"\u001a\u0002H\u0010\"\u0004\b\u0000\u0010!\"\b\b\u0001\u0010\u0004*\u0002H!\"\u0004\b\u0002\u0010\u0002\"\u0016\b\u0003\u0010\u0010*\u0010\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H!0\u0011*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\u0012\u001a\u0002H\u00102K\u0010\u0006\u001aG\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u0011H!¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H!0\u001aH\u0087\b¢\u0006\u0002\u0010#¨\u0006$" }, d2 = { "aggregate", "", "K", "R", "T", "Lkotlin/collections/Grouping;", "operation", "Lkotlin/Function4;", "Lkotlin/ParameterName;", "name", "key", "accumulator", "element", "", "first", "aggregateTo", "M", "", "destination", "(Lkotlin/collections/Grouping;Ljava/util/Map;Lkotlin/jvm/functions/Function4;)Ljava/util/Map;", "eachCountTo", "", "(Lkotlin/collections/Grouping;Ljava/util/Map;)Ljava/util/Map;", "fold", "initialValueSelector", "Lkotlin/Function2;", "Lkotlin/Function3;", "initialValue", "(Lkotlin/collections/Grouping;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/util/Map;", "foldTo", "(Lkotlin/collections/Grouping;Ljava/util/Map;Lkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function3;)Ljava/util/Map;", "(Lkotlin/collections/Grouping;Ljava/util/Map;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/util/Map;", "reduce", "S", "reduceTo", "(Lkotlin/collections/Grouping;Ljava/util/Map;Lkotlin/jvm/functions/Function3;)Ljava/util/Map;", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/collections/GroupingKt")
class GroupingKt__GroupingKt extends GroupingKt__GroupingJVMKt
{
    public GroupingKt__GroupingKt() {
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <T, K, R> Map<K, R> aggregate(@NotNull final Grouping<T, ? extends K> grouping, @NotNull final Function4<? super K, ? super R, ? super T, ? super Boolean, ? extends R> function4) {
        Intrinsics.checkParameterIsNotNull(grouping, "$receiver");
        Intrinsics.checkParameterIsNotNull(function4, "operation");
        final Map map = new LinkedHashMap();
        final Iterator<T> sourceIterator = grouping.sourceIterator();
        while (sourceIterator.hasNext()) {
            final T next = sourceIterator.next();
            final K key = (K)grouping.keyOf(next);
            final Object value = map.get(key);
            map.put(key, function4.invoke(key, (Object)value, next, Boolean.valueOf(value == null && !map.containsKey(key))));
        }
        return map;
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <T, K, R, M extends Map<? super K, R>> M aggregateTo(@NotNull final Grouping<T, ? extends K> grouping, @NotNull final M m, @NotNull final Function4<? super K, ? super R, ? super T, ? super Boolean, ? extends R> function4) {
        Intrinsics.checkParameterIsNotNull(grouping, "$receiver");
        Intrinsics.checkParameterIsNotNull(m, "destination");
        Intrinsics.checkParameterIsNotNull(function4, "operation");
        final Iterator<T> sourceIterator = grouping.sourceIterator();
        while (sourceIterator.hasNext()) {
            final T next = sourceIterator.next();
            final K key = (K)grouping.keyOf(next);
            final R value = ((Map<K, R>)m).get(key);
            m.put(key, (R)function4.invoke(key, value, next, value == null && !m.containsKey(key)));
        }
        return m;
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <T, K, M extends Map<? super K, Integer>> M eachCountTo(@NotNull final Grouping<T, ? extends K> grouping, @NotNull final M m) {
        Intrinsics.checkParameterIsNotNull(grouping, "$receiver");
        Intrinsics.checkParameterIsNotNull(m, "destination");
        final Iterator<T> sourceIterator = grouping.sourceIterator();
        while (sourceIterator.hasNext()) {
            final K key = (K)grouping.keyOf(sourceIterator.next());
            Integer n = ((Map<K, Integer>)m).get(key);
            if (n == null && !m.containsKey(key)) {
                n = 0;
            }
            m.put(key, n.intValue() + 1);
        }
        return m;
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <T, K, R> Map<K, R> fold(@NotNull final Grouping<T, ? extends K> grouping, final R r, @NotNull final Function2<? super R, ? super T, ? extends R> function2) {
        Intrinsics.checkParameterIsNotNull(grouping, "$receiver");
        Intrinsics.checkParameterIsNotNull(function2, "operation");
        final Map map = new LinkedHashMap();
        final Iterator<T> sourceIterator = grouping.sourceIterator();
        while (sourceIterator.hasNext()) {
            final T next = sourceIterator.next();
            final K key = (K)grouping.keyOf(next);
            Object value = map.get(key);
            if (value == null && !map.containsKey(key)) {
                value = r;
            }
            map.put(key, function2.invoke((Object)value, next));
        }
        return map;
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <T, K, R> Map<K, R> fold(@NotNull final Grouping<T, ? extends K> grouping, @NotNull final Function2<? super K, ? super T, ? extends R> function2, @NotNull final Function3<? super K, ? super R, ? super T, ? extends R> function3) {
        Intrinsics.checkParameterIsNotNull(grouping, "$receiver");
        Intrinsics.checkParameterIsNotNull(function2, "initialValueSelector");
        Intrinsics.checkParameterIsNotNull(function3, "operation");
        final Map map = new LinkedHashMap();
        final Iterator<T> sourceIterator = grouping.sourceIterator();
        while (sourceIterator.hasNext()) {
            final T next = sourceIterator.next();
            final K key = (K)grouping.keyOf(next);
            Object o = map.get(key);
            if (o == null && !map.containsKey(key)) {
                o = function2.invoke(key, next);
            }
            map.put(key, function3.invoke(key, (Object)o, next));
        }
        return map;
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <T, K, R, M extends Map<? super K, R>> M foldTo(@NotNull final Grouping<T, ? extends K> grouping, @NotNull final M m, final R r, @NotNull final Function2<? super R, ? super T, ? extends R> function2) {
        Intrinsics.checkParameterIsNotNull(grouping, "$receiver");
        Intrinsics.checkParameterIsNotNull(m, "destination");
        Intrinsics.checkParameterIsNotNull(function2, "operation");
        final Iterator<T> sourceIterator = grouping.sourceIterator();
        while (sourceIterator.hasNext()) {
            final T next = sourceIterator.next();
            final K key = (K)grouping.keyOf(next);
            R value = ((Map<K, R>)m).get(key);
            if (value == null && !m.containsKey(key)) {
                value = r;
            }
            m.put(key, (R)function2.invoke(value, next));
        }
        return m;
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <T, K, R, M extends Map<? super K, R>> M foldTo(@NotNull final Grouping<T, ? extends K> grouping, @NotNull final M m, @NotNull final Function2<? super K, ? super T, ? extends R> function2, @NotNull final Function3<? super K, ? super R, ? super T, ? extends R> function3) {
        Intrinsics.checkParameterIsNotNull(grouping, "$receiver");
        Intrinsics.checkParameterIsNotNull(m, "destination");
        Intrinsics.checkParameterIsNotNull(function2, "initialValueSelector");
        Intrinsics.checkParameterIsNotNull(function3, "operation");
        final Iterator<T> sourceIterator = grouping.sourceIterator();
        while (sourceIterator.hasNext()) {
            final T next = sourceIterator.next();
            final K key = (K)grouping.keyOf(next);
            R r = ((Map<K, R>)m).get(key);
            if (r == null && !m.containsKey(key)) {
                r = (R)function2.invoke(key, next);
            }
            m.put(key, (R)function3.invoke(key, r, next));
        }
        return m;
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <S, T extends S, K> Map<K, S> reduce(@NotNull final Grouping<T, ? extends K> grouping, @NotNull final Function3<? super K, ? super S, ? super T, ? extends S> function3) {
        Intrinsics.checkParameterIsNotNull(grouping, "$receiver");
        Intrinsics.checkParameterIsNotNull(function3, "operation");
        final Map map = new LinkedHashMap();
        final Iterator<T> sourceIterator = grouping.sourceIterator();
        while (sourceIterator.hasNext()) {
            S n = sourceIterator.next();
            final K key = (K)grouping.keyOf((T)n);
            final Object value = map.get(key);
            if (value != null || map.containsKey(key)) {
                n = (S)function3.invoke(key, (Object)value, (T)n);
            }
            map.put(key, n);
        }
        return map;
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <S, T extends S, K, M extends Map<? super K, S>> M reduceTo(@NotNull final Grouping<T, ? extends K> grouping, @NotNull final M m, @NotNull final Function3<? super K, ? super S, ? super T, ? extends S> function3) {
        Intrinsics.checkParameterIsNotNull(grouping, "$receiver");
        Intrinsics.checkParameterIsNotNull(m, "destination");
        Intrinsics.checkParameterIsNotNull(function3, "operation");
        final Iterator<T> sourceIterator = grouping.sourceIterator();
        while (sourceIterator.hasNext()) {
            S n = sourceIterator.next();
            final K key = (K)grouping.keyOf((T)n);
            final S value = ((Map<K, S>)m).get(key);
            if (value != null || m.containsKey(key)) {
                n = (S)function3.invoke(key, value, (T)n);
            }
            m.put(key, n);
        }
        return m;
    }
}
