// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.collections;

import java.util.Collection;
import java.util.TreeSet;
import java.util.Comparator;
import org.jetbrains.annotations.NotNull;
import kotlin.jvm.internal.Intrinsics;
import java.util.Collections;
import java.util.Set;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000$\n\u0000\n\u0002\u0010\"\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u001f\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u0002H\u0002¢\u0006\u0002\u0010\u0004\u001a+\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0006\"\u0004\b\u0000\u0010\u00022\u0012\u0010\u0007\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\b\"\u0002H\u0002¢\u0006\u0002\u0010\t\u001aG\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0006\"\u0004\b\u0000\u0010\u00022\u001a\u0010\n\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u000bj\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\f2\u0012\u0010\u0007\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\b\"\u0002H\u0002¢\u0006\u0002\u0010\r¨\u0006\u000e" }, d2 = { "setOf", "", "T", "element", "(Ljava/lang/Object;)Ljava/util/Set;", "sortedSetOf", "Ljava/util/TreeSet;", "elements", "", "([Ljava/lang/Object;)Ljava/util/TreeSet;", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "(Ljava/util/Comparator;[Ljava/lang/Object;)Ljava/util/TreeSet;", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/collections/SetsKt")
class SetsKt__SetsJVMKt
{
    public SetsKt__SetsJVMKt() {
    }
    
    @NotNull
    public static final <T> Set<T> setOf(final T o) {
        final Set<T> singleton = Collections.singleton(o);
        Intrinsics.checkExpressionValueIsNotNull(singleton, "java.util.Collections.singleton(element)");
        return singleton;
    }
    
    @NotNull
    public static final <T> TreeSet<T> sortedSetOf(@NotNull final Comparator<? super T> comparator, @NotNull final T... array) {
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        Intrinsics.checkParameterIsNotNull(array, "elements");
        return ArraysKt___ArraysKt.toCollection(array, new Collection((Comparator<? super T>)comparator));
    }
    
    @NotNull
    public static final <T> TreeSet<T> sortedSetOf(@NotNull final T... array) {
        Intrinsics.checkParameterIsNotNull(array, "elements");
        return ArraysKt___ArraysKt.toCollection(array, new TreeSet());
    }
}
