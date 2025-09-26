package kotlin.sequences;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.ArraysKt;
import kotlin.internal.InlineOnly;
import kotlin.internal.LowPriorityInOverloadResolution;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Sequences.kt */
@Metadata(bv = {1, 0, 2}, d1 = {"\u0000@\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\b\u0006\n\u0002\u0010\u001c\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\u001a+\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0014\b\u0004\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00050\u0004H\u0087\b\u001a\u0012\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002\u001a&\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\b2\u000e\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0004\u001a<\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\b2\u000e\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u00042\u0014\u0010\t\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u000b\u001a=\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\b2\b\u0010\f\u001a\u0004\u0018\u0001H\u00022\u0014\u0010\t\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u000bH\u0007¢\u0006\u0002\u0010\r\u001a+\u0010\u000e\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0012\u0010\u000f\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0010\"\u0002H\u0002¢\u0006\u0002\u0010\u0011\u001a\u001c\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0005\u001a\u001c\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001\u001aC\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00150\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0015*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0018\u0010\u0003\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00150\u00050\u000bH\u0002¢\u0006\u0002\b\u0016\u001a)\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00170\u0001H\u0007¢\u0006\u0002\b\u0018\u001a\"\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00010\u0001\u001a@\u0010\u0019\u001a\u001a\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u001b\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00150\u001b0\u001a\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0015*\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00150\u001a0\u0001¨\u0006\u001c"}, d2 = {"Sequence", "Lkotlin/sequences/Sequence;", "T", "iterator", "Lkotlin/Function0;", "", "emptySequence", "generateSequence", "", "nextFunction", "seedFunction", "Lkotlin/Function1;", "seed", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)Lkotlin/sequences/Sequence;", "sequenceOf", "elements", "", "([Ljava/lang/Object;)Lkotlin/sequences/Sequence;", "asSequence", "constrainOnce", "flatten", "R", "flatten$SequencesKt__SequencesKt", "", "flattenSequenceOfIterable", "unzip", "Lkotlin/Pair;", "", "kotlin-stdlib"}, k = 5, mv = {1, 1, 10}, xi = 1, xs = "kotlin/sequences/SequencesKt")
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SequencesKt__SequencesKt extends SequencesKt__SequencesJVMKt {
    @InlineOnly
    private static final <T> Sequence<T> Sequence(final Function0<? extends Iterator<? extends T>> function0) {
        return new Sequence<T>() { // from class: kotlin.sequences.SequencesKt__SequencesKt.Sequence.1
            @Override // kotlin.sequences.Sequence
            @NotNull
            public Iterator<T> iterator() {
                return (Iterator) function0.invoke();
            }
        };
    }

    @NotNull
    public static final <T> Sequence<T> asSequence(@NotNull final Iterator<? extends T> receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return SequencesKt.constrainOnce(new Sequence<T>() { // from class: kotlin.sequences.SequencesKt__SequencesKt$asSequence$$inlined$Sequence$1
            @Override // kotlin.sequences.Sequence
            @NotNull
            public Iterator<T> iterator() {
                return receiver;
            }
        });
    }

    @NotNull
    public static final <T> Sequence<T> sequenceOf(@NotNull T... elements) {
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        return elements.length == 0 ? SequencesKt.emptySequence() : ArraysKt.asSequence(elements);
    }

    @NotNull
    public static final <T> Sequence<T> emptySequence() {
        return EmptySequence.INSTANCE;
    }

    @NotNull
    public static final <T> Sequence<T> flatten(@NotNull Sequence<? extends Sequence<? extends T>> receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return flatten$SequencesKt__SequencesKt(receiver, new Function1<Sequence<? extends T>, Iterator<? extends T>>() { // from class: kotlin.sequences.SequencesKt__SequencesKt.flatten.1
            @Override // kotlin.jvm.functions.Function1
            @NotNull
            public final Iterator<T> invoke(@NotNull Sequence<? extends T> it) {
                Intrinsics.checkParameterIsNotNull(it, "it");
                return it.iterator();
            }
        });
    }

    @JvmName(name = "flattenSequenceOfIterable")
    @NotNull
    public static final <T> Sequence<T> flattenSequenceOfIterable(@NotNull Sequence<? extends Iterable<? extends T>> receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return flatten$SequencesKt__SequencesKt(receiver, new Function1<Iterable<? extends T>, Iterator<? extends T>>() { // from class: kotlin.sequences.SequencesKt__SequencesKt.flatten.2
            @Override // kotlin.jvm.functions.Function1
            @NotNull
            public final Iterator<T> invoke(@NotNull Iterable<? extends T> it) {
                Intrinsics.checkParameterIsNotNull(it, "it");
                return it.iterator();
            }
        });
    }

    private static final <T, R> Sequence<R> flatten$SequencesKt__SequencesKt(@NotNull Sequence<? extends T> sequence, Function1<? super T, ? extends Iterator<? extends R>> function1) {
        if (sequence instanceof TransformingSequence) {
            return ((TransformingSequence) sequence).flatten$kotlin_stdlib(function1);
        }
        return new FlatteningSequence(sequence, new Function1<T, T>() { // from class: kotlin.sequences.SequencesKt__SequencesKt.flatten.3
            @Override // kotlin.jvm.functions.Function1
            public final T invoke(T t) {
                return t;
            }
        }, function1);
    }

    @NotNull
    public static final <T, R> Pair<List<T>, List<R>> unzip(@NotNull Sequence<? extends Pair<? extends T, ? extends R>> receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (Pair<? extends T, ? extends R> pair : receiver) {
            arrayList.add(pair.getFirst());
            arrayList2.add(pair.getSecond());
        }
        return TuplesKt.to(arrayList, arrayList2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @NotNull
    public static final <T> Sequence<T> constrainOnce(@NotNull Sequence<? extends T> receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return receiver instanceof ConstrainedOnceSequence ? receiver : new ConstrainedOnceSequence(receiver);
    }

    @NotNull
    public static final <T> Sequence<T> generateSequence(@NotNull final Function0<? extends T> nextFunction) {
        Intrinsics.checkParameterIsNotNull(nextFunction, "nextFunction");
        return SequencesKt.constrainOnce(new GeneratorSequence(nextFunction, new Function1<T, T>() { // from class: kotlin.sequences.SequencesKt__SequencesKt.generateSequence.1
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            @Nullable
            public final T invoke(@NotNull T it) {
                Intrinsics.checkParameterIsNotNull(it, "it");
                return (T) nextFunction.invoke();
            }
        }));
    }

    @LowPriorityInOverloadResolution
    @NotNull
    public static final <T> Sequence<T> generateSequence(@Nullable final T t, @NotNull Function1<? super T, ? extends T> nextFunction) {
        Intrinsics.checkParameterIsNotNull(nextFunction, "nextFunction");
        if (t == null) {
            return EmptySequence.INSTANCE;
        }
        return new GeneratorSequence(new Function0<T>() { // from class: kotlin.sequences.SequencesKt__SequencesKt.generateSequence.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(0);
            }

            @Override // kotlin.jvm.functions.Function0
            @Nullable
            public final T invoke() {
                return (T) t;
            }
        }, nextFunction);
    }

    @NotNull
    public static final <T> Sequence<T> generateSequence(@NotNull Function0<? extends T> seedFunction, @NotNull Function1<? super T, ? extends T> nextFunction) {
        Intrinsics.checkParameterIsNotNull(seedFunction, "seedFunction");
        Intrinsics.checkParameterIsNotNull(nextFunction, "nextFunction");
        return new GeneratorSequence(seedFunction, nextFunction);
    }
}
