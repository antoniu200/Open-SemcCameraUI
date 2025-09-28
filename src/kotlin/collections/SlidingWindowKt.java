package kotlin.collections;

import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.SequenceBuilder;
import kotlin.coroutines.experimental.SequenceBuilderKt;
import kotlin.coroutines.experimental.jvm.internal.CoroutineImpl;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: SlidingWindow.kt */
@Metadata(bv = {1, 0, 2}, d1 = {"\u0000*\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010(\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0000\u001aH\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u00070\u0006\"\u0004\b\u0000\u0010\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\b0\u00062\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0000\u001aD\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u00070\u000e\"\u0004\b\u0000\u0010\b*\b\u0012\u0004\u0012\u0002H\b0\u000e2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0000¨\u0006\u000f"}, d2 = {"checkWindowSizeStep", "", "size", "", "step", "windowedIterator", "", "", "T", "iterator", "partialWindows", "", "reuseBuffer", "windowedSequence", "Lkotlin/sequences/Sequence;", "kotlin-stdlib"}, k = 2, mv = {1, 1, 10})
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class SlidingWindowKt {
    public static final void checkWindowSizeStep(int i, int i2) {
        String str;
        if (i > 0 && i2 > 0) {
            return;
        }
        if (i != i2) {
            str = "Both size " + i + " and step " + i2 + " must be greater than zero.";
        } else {
            str = "size " + i + " must be greater than zero.";
        }
        throw new IllegalArgumentException(str.toString());
    }

    @NotNull
    public static final <T> Sequence<List<T>> windowedSequence(@NotNull final Sequence<? extends T> receiver, final int i, final int i2, final boolean z, final boolean z2) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        checkWindowSizeStep(i, i2);
        return new Sequence<List<? extends T>>() { // from class: kotlin.collections.SlidingWindowKt$windowedSequence$$inlined$Sequence$1
            @Override // kotlin.sequences.Sequence
            @NotNull
            public Iterator<List<? extends T>> iterator() {
                return SlidingWindowKt.windowedIterator(receiver.iterator(), i, i2, z, z2);
            }
        };
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* compiled from: SlidingWindow.kt */
    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00040\u0003H\n¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", "T", "Lkotlin/coroutines/experimental/SequenceBuilder;", "", "invoke", "(Lkotlin/coroutines/experimental/SequenceBuilder;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 10})
    /* renamed from: kotlin.collections.SlidingWindowKt$windowedIterator$1, reason: invalid class name */
    static final class AnonymousClass1<T> extends CoroutineImpl implements Function2<SequenceBuilder<? super List<? extends T>>, Continuation<? super Unit>, Object> {
        final /* synthetic */ Iterator $iterator;
        final /* synthetic */ boolean $partialWindows;
        final /* synthetic */ boolean $reuseBuffer;
        final /* synthetic */ int $size;
        final /* synthetic */ int $step;
        int I$0;
        int I$1;
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        private SequenceBuilder p$;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(int i, int i2, Iterator it, boolean z, boolean z2, Continuation continuation) {
            super(2, continuation);
            this.$step = i;
            this.$size = i2;
            this.$iterator = it;
            this.$reuseBuffer = z;
            this.$partialWindows = z2;
        }

        @Override // kotlin.coroutines.experimental.jvm.internal.CoroutineImpl
        public /* bridge */ /* synthetic */ Continuation create(Object obj, Continuation continuation) {
            return create((SequenceBuilder) obj, (Continuation<? super Unit>) continuation);
        }

        @NotNull
        public final Continuation<Unit> create(@NotNull SequenceBuilder<? super List<? extends T>> receiver, @NotNull Continuation<? super Unit> continuation) {
            Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
            Intrinsics.checkParameterIsNotNull(continuation, "continuation");
            AnonymousClass1 anonymousClass1 = new AnonymousClass1(this.$step, this.$size, this.$iterator, this.$reuseBuffer, this.$partialWindows, continuation);
            anonymousClass1.p$ = receiver;
            return anonymousClass1;
        }

        @Override // kotlin.jvm.functions.Function2
        @Nullable
        public final Object invoke(@NotNull SequenceBuilder<? super List<? extends T>> receiver, @NotNull Continuation<? super Unit> continuation) {
            Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
            Intrinsics.checkParameterIsNotNull(continuation, "continuation");
            return ((AnonymousClass1) create((SequenceBuilder) receiver, continuation)).doResume(Unit.INSTANCE, null);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:29:0x007f  */
        /* JADX WARN: Removed duplicated region for block: B:39:0x00ac  */
        /* JADX WARN: Removed duplicated region for block: B:40:0x00b0  */
        /* JADX WARN: Removed duplicated region for block: B:50:0x00de A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:54:0x00f2  */
        /* JADX WARN: Removed duplicated region for block: B:66:0x012f  */
        /* JADX WARN: Removed duplicated region for block: B:69:0x013a  */
        /* JADX WARN: Removed duplicated region for block: B:77:0x0162  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:35:0x00a5 -> B:37:0x00a8). Please report as a decompilation issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:61:0x0122 -> B:63:0x0125). Please report as a decompilation issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:74:0x0159 -> B:76:0x015c). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.experimental.jvm.internal.CoroutineImpl
        @org.jetbrains.annotations.Nullable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final java.lang.Object doResume(@org.jetbrains.annotations.Nullable java.lang.Object r10, @org.jetbrains.annotations.Nullable java.lang.Throwable r11) throws java.lang.Throwable {
            /*
                Method dump skipped, instructions count: 398
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: kotlin.collections.SlidingWindowKt.AnonymousClass1.doResume(java.lang.Object, java.lang.Throwable):java.lang.Object");
        }
    }

    @NotNull
    public static final <T> Iterator<List<T>> windowedIterator(@NotNull Iterator<? extends T> iterator, int i, int i2, boolean z, boolean z2) {
        Intrinsics.checkParameterIsNotNull(iterator, "iterator");
        return !iterator.hasNext() ? EmptyIterator.INSTANCE : SequenceBuilderKt.buildIterator(new AnonymousClass1(i2, i, iterator, z2, z, null));
    }
}
