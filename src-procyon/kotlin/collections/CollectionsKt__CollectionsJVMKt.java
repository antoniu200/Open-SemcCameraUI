// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.collections;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.TypeCastException;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.CollectionToArray;
import java.util.Collection;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000*\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u001e\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a\"\u0010\u0000\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00012\n\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u0004H\u0081\b¢\u0006\u0002\u0010\u0005\u001a4\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00060\u0001\"\u0004\b\u0000\u0010\u00062\n\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u00042\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\u00060\u0001H\u0081\b¢\u0006\u0002\u0010\b\u001a\u001f\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\u00060\n\"\u0004\b\u0000\u0010\u00062\u0006\u0010\u000b\u001a\u0002H\u0006¢\u0006\u0002\u0010\f\u001a1\u0010\r\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\u00020\u0001\"\u0004\b\u0000\u0010\u0006*\n\u0012\u0006\b\u0001\u0012\u0002H\u00060\u00012\u0006\u0010\u000e\u001a\u00020\u000fH\u0000¢\u0006\u0002\u0010\u0010\u001a\u001f\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\u00060\n\"\u0004\b\u0000\u0010\u0006*\b\u0012\u0004\u0012\u0002H\u00060\u0012H\u0087\b¨\u0006\u0013" }, d2 = { "copyToArrayImpl", "", "", "collection", "", "(Ljava/util/Collection;)[Ljava/lang/Object;", "T", "array", "(Ljava/util/Collection;[Ljava/lang/Object;)[Ljava/lang/Object;", "listOf", "", "element", "(Ljava/lang/Object;)Ljava/util/List;", "copyToArrayOfAny", "isVarargs", "", "([Ljava/lang/Object;Z)[Ljava/lang/Object;", "toList", "Ljava/util/Enumeration;", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/collections/CollectionsKt")
class CollectionsKt__CollectionsJVMKt
{
    public CollectionsKt__CollectionsJVMKt() {
    }
    
    @InlineOnly
    private static final Object[] copyToArrayImpl(final Collection<?> collection) {
        return CollectionToArray.toArray(collection);
    }
    
    @InlineOnly
    private static final <T> T[] copyToArrayImpl(final Collection<?> collection, final T[] array) {
        if (array == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<kotlin.Any?>");
        }
        final Object[] array2 = CollectionToArray.toArray(collection, array);
        if (array2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        return (T[])array2;
    }
    
    @NotNull
    public static final <T> Object[] copyToArrayOfAny(@NotNull T[] copy, final boolean b) {
        Intrinsics.checkParameterIsNotNull(copy, "$receiver");
        if (!b || !Intrinsics.areEqual(copy.getClass(), Object[].class)) {
            copy = Arrays.copyOf(copy, copy.length, (Class<? extends T[]>)Object[].class);
            Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(\u2026 Array<Any?>::class.java)");
        }
        return copy;
    }
    
    @NotNull
    public static final <T> List<T> listOf(final T o) {
        final List<T> singletonList = Collections.singletonList(o);
        Intrinsics.checkExpressionValueIsNotNull(singletonList, "java.util.Collections.singletonList(element)");
        return singletonList;
    }
    
    @InlineOnly
    private static final <T> List<T> toList(@NotNull final Enumeration<T> e) {
        final ArrayList<T> list = Collections.list(e);
        Intrinsics.checkExpressionValueIsNotNull(list, "java.util.Collections.list(this)");
        return list;
    }
}
