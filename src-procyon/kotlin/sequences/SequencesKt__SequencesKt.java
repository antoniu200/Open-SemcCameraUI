// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.sequences;

import kotlin.TuplesKt;
import java.util.ArrayList;
import java.util.List;
import kotlin.Pair;
import kotlin.internal.LowPriorityInOverloadResolution;
import org.jetbrains.annotations.Nullable;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.internal.InlineOnly;
import java.util.Iterator;
import kotlin.jvm.functions.Function0;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000@\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\b\u0006\n\u0002\u0010\u001c\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\u001a+\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0014\b\u0004\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00050\u0004H\u0087\b\u001a\u0012\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002\u001a&\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\b2\u000e\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0004\u001a<\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\b2\u000e\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u00042\u0014\u0010\t\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u000b\u001a=\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\b2\b\u0010\f\u001a\u0004\u0018\u0001H\u00022\u0014\u0010\t\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u000bH\u0007¢\u0006\u0002\u0010\r\u001a+\u0010\u000e\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0012\u0010\u000f\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0010\"\u0002H\u0002¢\u0006\u0002\u0010\u0011\u001a\u001c\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0005\u001a\u001c\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001\u001aC\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00150\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0015*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0018\u0010\u0003\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00150\u00050\u000bH\u0002¢\u0006\u0002\b\u0016\u001a)\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00170\u0001H\u0007¢\u0006\u0002\b\u0018\u001a\"\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00010\u0001\u001a@\u0010\u0019\u001a\u001a\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u001b\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00150\u001b0\u001a\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0015*\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00150\u001a0\u0001¨\u0006\u001c" }, d2 = { "Sequence", "Lkotlin/sequences/Sequence;", "T", "iterator", "Lkotlin/Function0;", "", "emptySequence", "generateSequence", "", "nextFunction", "seedFunction", "Lkotlin/Function1;", "seed", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)Lkotlin/sequences/Sequence;", "sequenceOf", "elements", "", "([Ljava/lang/Object;)Lkotlin/sequences/Sequence;", "asSequence", "constrainOnce", "flatten", "R", "flatten$SequencesKt__SequencesKt", "", "flattenSequenceOfIterable", "unzip", "Lkotlin/Pair;", "", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/sequences/SequencesKt")
class SequencesKt__SequencesKt extends SequencesKt__SequencesJVMKt
{
    public SequencesKt__SequencesKt() {
    }
    
    @InlineOnly
    private static final <T> Sequence<T> Sequence(final Function0<? extends Iterator<? extends T>> function0) {
        return (Sequence<T>)new SequencesKt__SequencesKt$Sequence.SequencesKt__SequencesKt$Sequence$1((Function0)function0);
    }
    
    @NotNull
    public static final <T> Sequence<T> asSequence(@NotNull final Iterator<? extends T> iterator) {
        Intrinsics.checkParameterIsNotNull(iterator, "$receiver");
        return constrainOnce((Sequence<? extends T>)new SequencesKt__SequencesKt$asSequence$$inlined$Sequence.SequencesKt__SequencesKt$asSequence$$inlined$Sequence$1((Iterator)iterator));
    }
    
    @NotNull
    public static final <T> Sequence<T> constrainOnce(@NotNull Sequence<? extends T> sequence) {
        Intrinsics.checkParameterIsNotNull(sequence, "$receiver");
        if (!(sequence instanceof ConstrainedOnceSequence)) {
            sequence = new ConstrainedOnceSequence<T>(sequence);
        }
        return sequence;
    }
    
    @NotNull
    public static final <T> Sequence<T> emptySequence() {
        return EmptySequence.INSTANCE;
    }
    
    @NotNull
    public static final <T> Sequence<T> flatten(@NotNull final Sequence<? extends Sequence<? extends T>> sequence) {
        Intrinsics.checkParameterIsNotNull(sequence, "$receiver");
        return flatten$SequencesKt__SequencesKt((Sequence<?>)sequence, (Function1<? super Object, ? extends Iterator<? extends T>>)SequencesKt__SequencesKt$flatten.SequencesKt__SequencesKt$flatten$1.INSTANCE);
    }
    
    private static final <T, R> Sequence<R> flatten$SequencesKt__SequencesKt(@NotNull final Sequence<? extends T> sequence, final Function1<? super T, ? extends Iterator<? extends R>> function1) {
        if (sequence instanceof TransformingSequence) {
            return ((TransformingSequence<Object, Object>)sequence).flatten$kotlin_stdlib((Function1<? super Object, ? extends Iterator<? extends R>>)function1);
        }
        return new FlatteningSequence<Object, Object, R>(sequence, (Function1<?, ?>)SequencesKt__SequencesKt$flatten.SequencesKt__SequencesKt$flatten$3.INSTANCE, function1);
    }
    
    @JvmName(name = "flattenSequenceOfIterable")
    @NotNull
    public static final <T> Sequence<T> flattenSequenceOfIterable(@NotNull final Sequence<? extends Iterable<? extends T>> sequence) {
        Intrinsics.checkParameterIsNotNull(sequence, "$receiver");
        return flatten$SequencesKt__SequencesKt((Sequence<?>)sequence, (Function1<? super Object, ? extends Iterator<? extends T>>)SequencesKt__SequencesKt$flatten.SequencesKt__SequencesKt$flatten$2.INSTANCE);
    }
    
    @LowPriorityInOverloadResolution
    @NotNull
    public static final <T> Sequence<T> generateSequence(@Nullable final T t, @NotNull final Function1<? super T, ? extends T> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "nextFunction");
        Sequence sequence;
        if (t == null) {
            sequence = EmptySequence.INSTANCE;
        }
        else {
            sequence = new GeneratorSequence((Function0)new SequencesKt__SequencesKt$generateSequence.SequencesKt__SequencesKt$generateSequence$2((Object)t), function1);
        }
        return sequence;
    }
    
    @NotNull
    public static final <T> Sequence<T> generateSequence(@NotNull final Function0<? extends T> function0) {
        Intrinsics.checkParameterIsNotNull(function0, "nextFunction");
        return constrainOnce((Sequence<? extends T>)new GeneratorSequence<T>(function0, (Function1<? super T, ? extends T>)new SequencesKt__SequencesKt$generateSequence.SequencesKt__SequencesKt$generateSequence$1((Function0)function0)));
    }
    
    @NotNull
    public static final <T> Sequence<T> generateSequence(@NotNull final Function0<? extends T> function0, @NotNull final Function1<? super T, ? extends T> function2) {
        Intrinsics.checkParameterIsNotNull(function0, "seedFunction");
        Intrinsics.checkParameterIsNotNull(function2, "nextFunction");
        return new GeneratorSequence<T>(function0, function2);
    }
    
    @NotNull
    public static final <T> Sequence<T> sequenceOf(@NotNull final T... array) {
        Intrinsics.checkParameterIsNotNull(array, "elements");
        Object o;
        if (array.length == 0) {
            o = emptySequence();
        }
        else {
            o = ArraysKt___ArraysKt.asSequence(array);
        }
        return (Sequence<T>)o;
    }
    
    @NotNull
    public static final <T, R> Pair<List<T>, List<R>> unzip(@NotNull final Sequence<? extends Pair<? extends T, ? extends R>> sequence) {
        Intrinsics.checkParameterIsNotNull(sequence, "$receiver");
        final ArrayList list = new ArrayList();
        final ArrayList list2 = new ArrayList();
        for (final Pair pair : sequence) {
            list.add(pair.getFirst());
            list2.add(pair.getSecond());
        }
        return (Pair<List<T>, List<R>>)TuplesKt.to(list, list2);
    }
}
