// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.collections;

import kotlin.sequences.Sequence;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.SequenceBuilder;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import java.util.Iterator;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000*\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010(\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0000\u001aH\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u00070\u0006\"\u0004\b\u0000\u0010\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\b0\u00062\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0000\u001aD\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u00070\u000e\"\u0004\b\u0000\u0010\b*\b\u0012\u0004\u0012\u0002H\b0\u000e2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0000¨\u0006\u000f" }, d2 = { "checkWindowSizeStep", "", "size", "", "step", "windowedIterator", "", "", "T", "iterator", "partialWindows", "", "reuseBuffer", "windowedSequence", "Lkotlin/sequences/Sequence;", "kotlin-stdlib" }, k = 2, mv = { 1, 1, 10 })
public final class SlidingWindowKt
{
    public static final void checkWindowSizeStep(final int n, final int i) {
        if (n <= 0 || i <= 0) {
            String s;
            if (n != i) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Both size ");
                sb.append(n);
                sb.append(" and step ");
                sb.append(i);
                sb.append(" must be greater than zero.");
                s = sb.toString();
            }
            else {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("size ");
                sb2.append(n);
                sb2.append(" must be greater than zero.");
                s = sb2.toString();
            }
            throw new IllegalArgumentException(s.toString());
        }
    }
    
    @NotNull
    public static final <T> Iterator<List<T>> windowedIterator(@NotNull final Iterator<? extends T> iterator, final int n, final int n2, final boolean b, final boolean b2) {
        Intrinsics.checkParameterIsNotNull(iterator, "iterator");
        if (!iterator.hasNext()) {
            return EmptyIterator.INSTANCE;
        }
        return SequenceBuilderKt__SequenceBuilderKt.buildIterator((Function2<? super SequenceBuilder<? super List<T>>, ? super Continuation<? super Unit>, ?>)new SlidingWindowKt$windowedIterator.SlidingWindowKt$windowedIterator$1(n2, n, (Iterator)iterator, b2, b, (Continuation)null));
    }
    
    @NotNull
    public static final <T> Sequence<List<T>> windowedSequence(@NotNull final Sequence<? extends T> sequence, final int n, final int n2, final boolean b, final boolean b2) {
        Intrinsics.checkParameterIsNotNull(sequence, "$receiver");
        checkWindowSizeStep(n, n2);
        return (Sequence<List<T>>)new SlidingWindowKt$windowedSequence$$inlined$Sequence.SlidingWindowKt$windowedSequence$$inlined$Sequence$1((Sequence)sequence, n, n2, b, b2);
    }
}
