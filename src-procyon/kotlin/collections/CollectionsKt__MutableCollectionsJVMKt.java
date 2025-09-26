// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.collections;

import kotlin.jvm.functions.Function2;
import kotlin.ReplaceWith;
import kotlin.DeprecationLevel;
import kotlin.Deprecated;
import kotlin.NotImplementedError;
import java.util.Comparator;
import kotlin.jvm.internal.Intrinsics;
import java.util.Random;
import kotlin.internal.InlineOnly;
import kotlin.SinceKotlin;
import java.util.Collections;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000<\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010!\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u001c\n\u0000\n\u0002\u0010\u000f\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a&\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0004\u001a\u0002H\u0002H\u0087\b¢\u0006\u0002\u0010\u0005\u001a\u0019\u0010\u0006\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0087\b\u001a!\u0010\u0006\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0087\b\u001a\u001e\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\u00020\n\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000bH\u0007\u001a&\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\u00020\n\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\bH\u0007\u001a \u0010\f\u001a\u00020\u0001\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\r*\b\u0012\u0004\u0012\u0002H\u00020\u0003\u001a3\u0010\f\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0018\u0010\u000e\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00100\u000fH\u0087\b\u001a5\u0010\f\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u001a\u0010\u0011\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0012j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0013H\u0087\b\u001a2\u0010\u0014\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u001a\u0010\u0011\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0012j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0013¨\u0006\u0015" }, d2 = { "fill", "", "T", "", "value", "(Ljava/util/List;Ljava/lang/Object;)V", "shuffle", "random", "Ljava/util/Random;", "shuffled", "", "", "sort", "", "comparison", "Lkotlin/Function2;", "", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "sortWith", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/collections/CollectionsKt")
class CollectionsKt__MutableCollectionsJVMKt extends CollectionsKt__IteratorsKt
{
    public CollectionsKt__MutableCollectionsJVMKt() {
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final <T> void fill(@NotNull final List<T> list, final T obj) {
        Collections.fill(list, obj);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final <T> void shuffle(@NotNull final List<T> list) {
        Collections.shuffle(list);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final <T> void shuffle(@NotNull final List<T> list, final Random rnd) {
        Collections.shuffle(list, rnd);
    }
    
    @SinceKotlin(version = "1.2")
    @NotNull
    public static final <T> List<T> shuffled(@NotNull final Iterable<? extends T> iterable) {
        Intrinsics.checkParameterIsNotNull(iterable, "$receiver");
        final List<Object> mutableList = CollectionsKt___CollectionsKt.toMutableList((Iterable<?>)iterable);
        Collections.shuffle(mutableList);
        return (List<T>)mutableList;
    }
    
    @SinceKotlin(version = "1.2")
    @NotNull
    public static final <T> List<T> shuffled(@NotNull final Iterable<? extends T> iterable, @NotNull final Random rnd) {
        Intrinsics.checkParameterIsNotNull(iterable, "$receiver");
        Intrinsics.checkParameterIsNotNull(rnd, "random");
        final List<Object> mutableList = CollectionsKt___CollectionsKt.toMutableList((Iterable<?>)iterable);
        Collections.shuffle(mutableList, rnd);
        return (List<T>)mutableList;
    }
    
    public static final <T extends Comparable<? super T>> void sort(@NotNull final List<T> list) {
        Intrinsics.checkParameterIsNotNull(list, "$receiver");
        if (list.size() > 1) {
            Collections.sort(list);
        }
    }
    
    @Deprecated(level = DeprecationLevel.ERROR, message = "Use sortWith(comparator) instead.", replaceWith = @ReplaceWith(expression = "this.sortWith(comparator)", imports = {}))
    @InlineOnly
    private static final <T> void sort(@NotNull final List<T> list, final Comparator<? super T> comparator) {
        throw new NotImplementedError(null, 1, null);
    }
    
    @Deprecated(level = DeprecationLevel.ERROR, message = "Use sortWith(Comparator(comparison)) instead.", replaceWith = @ReplaceWith(expression = "this.sortWith(Comparator(comparison))", imports = {}))
    @InlineOnly
    private static final <T> void sort(@NotNull final List<T> list, final Function2<? super T, ? super T, Integer> function2) {
        throw new NotImplementedError(null, 1, null);
    }
    
    public static final <T> void sortWith(@NotNull final List<T> list, @NotNull final Comparator<? super T> c) {
        Intrinsics.checkParameterIsNotNull(list, "$receiver");
        Intrinsics.checkParameterIsNotNull(c, "comparator");
        if (list.size() > 1) {
            Collections.sort(list, c);
        }
    }
}
