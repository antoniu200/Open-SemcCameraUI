// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.collections;

import kotlin.ranges.IntRange;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u0018\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001\u001a#\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0007¢\u0006\u0002\b\u0004\u001a\u001d\u0010\u0005\u001a\u00020\u0006*\u0006\u0012\u0002\b\u00030\u00012\u0006\u0010\u0007\u001a\u00020\u0006H\u0002¢\u0006\u0002\b\b\u001a\u001d\u0010\t\u001a\u00020\u0006*\u0006\u0012\u0002\b\u00030\u00012\u0006\u0010\u0007\u001a\u00020\u0006H\u0002¢\u0006\u0002\b\n¨\u0006\u000b" }, d2 = { "asReversed", "", "T", "", "asReversedMutable", "reverseElementIndex", "", "index", "reverseElementIndex$CollectionsKt__ReversedViewsKt", "reversePositionIndex", "reversePositionIndex$CollectionsKt__ReversedViewsKt", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/collections/CollectionsKt")
class CollectionsKt__ReversedViewsKt extends CollectionsKt__MutableCollectionsKt
{
    public CollectionsKt__ReversedViewsKt() {
    }
    
    @NotNull
    public static final <T> List<T> asReversed(@NotNull final List<? extends T> list) {
        Intrinsics.checkParameterIsNotNull(list, "$receiver");
        return new ReversedListReadOnly<T>(list);
    }
    
    @JvmName(name = "asReversedMutable")
    @NotNull
    public static final <T> List<T> asReversedMutable(@NotNull final List<T> list) {
        Intrinsics.checkParameterIsNotNull(list, "$receiver");
        return new ReversedList<T>(list);
    }
    
    private static final int reverseElementIndex$CollectionsKt__ReversedViewsKt(@NotNull final List<?> list, final int i) {
        final int lastIndex = CollectionsKt__CollectionsKt.getLastIndex(list);
        if (i >= 0) {
            if (lastIndex >= i) {
                return CollectionsKt__CollectionsKt.getLastIndex(list) - i;
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Element index ");
        sb.append(i);
        sb.append(" must be in range [");
        sb.append(new IntRange(0, CollectionsKt__CollectionsKt.getLastIndex(list)));
        sb.append("].");
        throw new IndexOutOfBoundsException(sb.toString());
    }
    
    private static final int reversePositionIndex$CollectionsKt__ReversedViewsKt(@NotNull final List<?> list, final int i) {
        final int size = list.size();
        if (i >= 0) {
            if (size >= i) {
                return list.size() - i;
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Position index ");
        sb.append(i);
        sb.append(" must be in range [");
        sb.append(new IntRange(0, list.size()));
        sb.append("].");
        throw new IndexOutOfBoundsException(sb.toString());
    }
}
