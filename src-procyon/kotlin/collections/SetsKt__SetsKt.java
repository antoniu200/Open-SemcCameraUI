// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.collections;

import org.jetbrains.annotations.Nullable;
import java.util.LinkedHashSet;
import kotlin.jvm.internal.Intrinsics;
import kotlin.internal.InlineOnly;
import kotlin.SinceKotlin;
import java.util.HashSet;
import org.jetbrains.annotations.NotNull;
import java.util.Set;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u00000\n\u0000\n\u0002\u0010\"\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010#\n\u0002\b\u0005\u001a\u0012\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002\u001a\u001f\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0004j\b\u0012\u0004\u0012\u0002H\u0002`\u0005\"\u0004\b\u0000\u0010\u0002H\u0087\b\u001a5\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0004j\b\u0012\u0004\u0012\u0002H\u0002`\u0005\"\u0004\b\u0000\u0010\u00022\u0012\u0010\u0006\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0007\"\u0002H\u0002¢\u0006\u0002\u0010\b\u001a\u001f\u0010\t\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\nj\b\u0012\u0004\u0012\u0002H\u0002`\u000b\"\u0004\b\u0000\u0010\u0002H\u0087\b\u001a5\u0010\t\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\nj\b\u0012\u0004\u0012\u0002H\u0002`\u000b\"\u0004\b\u0000\u0010\u00022\u0012\u0010\u0006\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0007\"\u0002H\u0002¢\u0006\u0002\u0010\f\u001a\u0015\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\u00020\u000e\"\u0004\b\u0000\u0010\u0002H\u0087\b\u001a+\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\u00020\u000e\"\u0004\b\u0000\u0010\u00022\u0012\u0010\u0006\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0007\"\u0002H\u0002¢\u0006\u0002\u0010\u000f\u001a\u0015\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002H\u0087\b\u001a+\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0012\u0010\u0006\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0007\"\u0002H\u0002¢\u0006\u0002\u0010\u000f\u001a\u001e\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001H\u0000\u001a!\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0004\u0012\u0002H\u0002\u0018\u00010\u0001H\u0087\b¨\u0006\u0013" }, d2 = { "emptySet", "", "T", "hashSetOf", "Ljava/util/HashSet;", "Lkotlin/collections/HashSet;", "elements", "", "([Ljava/lang/Object;)Ljava/util/HashSet;", "linkedSetOf", "Ljava/util/LinkedHashSet;", "Lkotlin/collections/LinkedHashSet;", "([Ljava/lang/Object;)Ljava/util/LinkedHashSet;", "mutableSetOf", "", "([Ljava/lang/Object;)Ljava/util/Set;", "setOf", "optimizeReadOnlySet", "orEmpty", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/collections/SetsKt")
class SetsKt__SetsKt extends SetsKt__SetsJVMKt
{
    public SetsKt__SetsKt() {
    }
    
    @NotNull
    public static final <T> Set<T> emptySet() {
        return EmptySet.INSTANCE;
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> HashSet<T> hashSetOf() {
        return new HashSet<T>();
    }
    
    @NotNull
    public static final <T> HashSet<T> hashSetOf(@NotNull final T... array) {
        Intrinsics.checkParameterIsNotNull(array, "elements");
        return ArraysKt___ArraysKt.toCollection(array, new HashSet(MapsKt__MapsKt.mapCapacity(array.length)));
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> LinkedHashSet<T> linkedSetOf() {
        return new LinkedHashSet<T>();
    }
    
    @NotNull
    public static final <T> LinkedHashSet<T> linkedSetOf(@NotNull final T... array) {
        Intrinsics.checkParameterIsNotNull(array, "elements");
        return ArraysKt___ArraysKt.toCollection(array, new LinkedHashSet(MapsKt__MapsKt.mapCapacity(array.length)));
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> Set<T> mutableSetOf() {
        return new LinkedHashSet<T>();
    }
    
    @NotNull
    public static final <T> Set<T> mutableSetOf(@NotNull final T... array) {
        Intrinsics.checkParameterIsNotNull(array, "elements");
        return ArraysKt___ArraysKt.toCollection(array, (Set<T>)new LinkedHashSet(MapsKt__MapsKt.mapCapacity(array.length)));
    }
    
    @NotNull
    public static final <T> Set<T> optimizeReadOnlySet(@NotNull Set<? extends T> o) {
        Intrinsics.checkParameterIsNotNull(o, "$receiver");
        switch (((Set)o).size()) {
            case 1: {
                o = SetsKt__SetsJVMKt.setOf(((Set<T>)o).iterator().next());
                break;
            }
            case 0: {
                o = emptySet();
                break;
            }
        }
        return (Set<T>)o;
    }
    
    @InlineOnly
    private static final <T> Set<T> orEmpty(@Nullable Set<? extends T> emptySet) {
        if (emptySet == null) {
            emptySet = emptySet();
        }
        return emptySet;
    }
    
    @InlineOnly
    private static final <T> Set<T> setOf() {
        return (Set<T>)emptySet();
    }
    
    @NotNull
    public static final <T> Set<T> setOf(@NotNull final T... array) {
        Intrinsics.checkParameterIsNotNull(array, "elements");
        Object o;
        if (array.length > 0) {
            o = ArraysKt___ArraysKt.toSet(array);
        }
        else {
            o = emptySet();
        }
        return (Set<T>)o;
    }
}
