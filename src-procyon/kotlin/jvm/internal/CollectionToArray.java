// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.jvm.internal;

import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function0;
import kotlin.TypeCastException;
import java.lang.reflect.Array;
import org.jetbrains.annotations.Nullable;
import java.util.Iterator;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import kotlin.jvm.JvmName;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u00002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a#\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00012\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\bH\u0007¢\u0006\u0004\b\t\u0010\n\u001a5\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00012\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\b2\u0010\u0010\u000b\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0002\u0018\u00010\u0001H\u0007¢\u0006\u0004\b\t\u0010\f\u001a~\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00012\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\b2\u0014\u0010\u000e\u001a\u0010\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00010\u000f2\u001a\u0010\u0010\u001a\u0016\u0012\u0004\u0012\u00020\u0005\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00010\u00112(\u0010\u0012\u001a$\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001\u0012\u0004\u0012\u00020\u0005\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00010\u0013H\u0082\b¢\u0006\u0002\u0010\u0014\"\u0018\u0010\u0000\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0003\"\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0015" }, d2 = { "EMPTY", "", "", "[Ljava/lang/Object;", "MAX_SIZE", "", "collectionToArray", "collection", "", "toArray", "(Ljava/util/Collection;)[Ljava/lang/Object;", "a", "(Ljava/util/Collection;[Ljava/lang/Object;)[Ljava/lang/Object;", "toArrayImpl", "empty", "Lkotlin/Function0;", "alloc", "Lkotlin/Function1;", "trim", "Lkotlin/Function2;", "(Ljava/util/Collection;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function2;)[Ljava/lang/Object;", "kotlin-stdlib" }, k = 2, mv = { 1, 1, 10 })
@JvmName(name = "CollectionToArray")
public final class CollectionToArray
{
    private static final Object[] EMPTY;
    private static final int MAX_SIZE = 2147483645;
    
    static {
        EMPTY = new Object[0];
    }
    
    @JvmName(name = "toArray")
    @NotNull
    public static final Object[] toArray(@NotNull final Collection<?> collection) {
        Intrinsics.checkParameterIsNotNull(collection, "collection");
        final int size = collection.size();
        if (size != 0) {
            final Iterator iterator = collection.iterator();
            if (iterator.hasNext()) {
                Object[] array = new Object[size];
                int n = 0;
                while (true) {
                    final int newLength = n + 1;
                    array[n] = iterator.next();
                    Object[] copy;
                    if (newLength >= array.length) {
                        if (!iterator.hasNext()) {
                            return array;
                        }
                        int newLength2;
                        if ((newLength2 = newLength * 3 + 1 >>> 1) <= newLength) {
                            if (newLength >= 2147483645) {
                                throw new OutOfMemoryError();
                            }
                            newLength2 = 2147483645;
                        }
                        copy = Arrays.copyOf(array, newLength2);
                        Intrinsics.checkExpressionValueIsNotNull(copy, "Arrays.copyOf(result, newSize)");
                    }
                    else {
                        copy = array;
                        if (!iterator.hasNext()) {
                            array = Arrays.copyOf(array, newLength);
                            Intrinsics.checkExpressionValueIsNotNull(array, "Arrays.copyOf(result, size)");
                            return array;
                        }
                    }
                    n = newLength;
                    array = copy;
                }
            }
        }
        return CollectionToArray.EMPTY;
    }
    
    @JvmName(name = "toArray")
    @NotNull
    public static final Object[] toArray(@NotNull final Collection<?> collection, @Nullable final Object[] array) {
        Intrinsics.checkParameterIsNotNull(collection, "collection");
        if (array == null) {
            throw new NullPointerException();
        }
        final int size = collection.size();
        int n = 0;
        Object[] copy;
        if (size == 0) {
            copy = array;
            if (array.length > 0) {
                array[0] = null;
                copy = array;
            }
        }
        else {
            final Iterator iterator = collection.iterator();
            if (!iterator.hasNext()) {
                copy = array;
                if (array.length > 0) {
                    array[0] = null;
                    copy = array;
                }
            }
            else {
                if (size <= array.length) {
                    copy = array;
                }
                else {
                    final Object instance = Array.newInstance(array.getClass().getComponentType(), size);
                    if (instance == null) {
                        throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<kotlin.Any?>");
                    }
                    copy = (Object[])instance;
                }
                while (true) {
                    final int newLength = n + 1;
                    copy[n] = iterator.next();
                    Object[] copy2;
                    if (newLength >= copy.length) {
                        if (!iterator.hasNext()) {
                            break;
                        }
                        int newLength2;
                        if ((newLength2 = newLength * 3 + 1 >>> 1) <= newLength) {
                            if (newLength >= 2147483645) {
                                throw new OutOfMemoryError();
                            }
                            newLength2 = 2147483645;
                        }
                        copy2 = Arrays.copyOf(copy, newLength2);
                        Intrinsics.checkExpressionValueIsNotNull(copy2, "Arrays.copyOf(result, newSize)");
                    }
                    else {
                        copy2 = copy;
                        if (!iterator.hasNext()) {
                            if (copy == array) {
                                array[newLength] = null;
                                copy = array;
                                break;
                            }
                            copy = Arrays.copyOf(copy, newLength);
                            Intrinsics.checkExpressionValueIsNotNull(copy, "Arrays.copyOf(result, size)");
                            break;
                        }
                    }
                    n = newLength;
                    copy = copy2;
                }
            }
        }
        return copy;
    }
    
    private static final Object[] toArrayImpl(final Collection<?> collection, final Function0<Object[]> function0, final Function1<? super Integer, Object[]> function2, final Function2<? super Object[], ? super Integer, Object[]> function3) {
        final int size = collection.size();
        if (size == 0) {
            return function0.invoke();
        }
        final Iterator iterator = collection.iterator();
        if (!iterator.hasNext()) {
            return function0.invoke();
        }
        Object[] original = function2.invoke(size);
        int n = 0;
        while (true) {
            final int i = n + 1;
            original[n] = iterator.next();
            Object[] copy;
            if (i >= original.length) {
                if (!iterator.hasNext()) {
                    return original;
                }
                int newLength;
                if ((newLength = i * 3 + 1 >>> 1) <= i) {
                    if (i >= 2147483645) {
                        throw new OutOfMemoryError();
                    }
                    newLength = 2147483645;
                }
                copy = Arrays.copyOf(original, newLength);
                Intrinsics.checkExpressionValueIsNotNull(copy, "Arrays.copyOf(result, newSize)");
            }
            else {
                copy = original;
                if (!iterator.hasNext()) {
                    return function3.invoke(original, i);
                }
            }
            n = i;
            original = copy;
        }
    }
}
