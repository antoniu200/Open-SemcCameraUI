package kotlin.text;

import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Strings.kt */
@Metadata(bv = {1, 0, 2}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010(\n\u0000\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001BJ\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012+\u0010\b\u001a'\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0006\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0006\u0018\u00010\n0\t¢\u0006\u0002\b\u000b¢\u0006\u0002\u0010\fJ\u000f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00020\u000eH\u0096\u0002R3\u0010\b\u001a'\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0006\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0006\u0018\u00010\n0\t¢\u0006\u0002\b\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lkotlin/text/DelimitedRangesSequence;", "Lkotlin/sequences/Sequence;", "Lkotlin/ranges/IntRange;", "input", "", "startIndex", "", "limit", "getNextMatch", "Lkotlin/Function2;", "Lkotlin/Pair;", "Lkotlin/ExtensionFunctionType;", "(Ljava/lang/CharSequence;IILkotlin/jvm/functions/Function2;)V", "iterator", "", "kotlin-stdlib"}, k = 1, mv = {1, 1, 10})
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
final class DelimitedRangesSequence implements Sequence<IntRange> {
    private final Function2<CharSequence, Integer, Pair<Integer, Integer>> getNextMatch;
    private final CharSequence input;
    private final int limit;
    private final int startIndex;

    /* JADX WARN: Multi-variable type inference failed */
    public DelimitedRangesSequence(@NotNull CharSequence input, int i, int i2, @NotNull Function2<? super CharSequence, ? super Integer, Pair<Integer, Integer>> getNextMatch) {
        Intrinsics.checkParameterIsNotNull(input, "input");
        Intrinsics.checkParameterIsNotNull(getNextMatch, "getNextMatch");
        this.input = input;
        this.startIndex = i;
        this.limit = i2;
        this.getNextMatch = getNextMatch;
    }

    /* compiled from: Strings.kt */
    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000'\n\u0000\n\u0002\u0010(\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0013\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0003J\b\u0010\u0018\u001a\u00020\u0019H\u0002J\t\u0010\u001a\u001a\u00020\u001bH\u0096\u0002J\t\u0010\u001c\u001a\u00020\u0002H\u0096\u0002R\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001a\u0010\n\u001a\u00020\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\u0007\"\u0004\b\f\u0010\tR\u001c\u0010\r\u001a\u0004\u0018\u00010\u0002X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0012\u001a\u00020\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0007\"\u0004\b\u0014\u0010\tR\u001a\u0010\u0015\u001a\u00020\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0007\"\u0004\b\u0017\u0010\t¨\u0006\u001d"}, d2 = {"kotlin/text/DelimitedRangesSequence$iterator$1", "", "Lkotlin/ranges/IntRange;", "(Lkotlin/text/DelimitedRangesSequence;)V", "counter", "", "getCounter", "()I", "setCounter", "(I)V", "currentStartIndex", "getCurrentStartIndex", "setCurrentStartIndex", "nextItem", "getNextItem", "()Lkotlin/ranges/IntRange;", "setNextItem", "(Lkotlin/ranges/IntRange;)V", "nextSearchIndex", "getNextSearchIndex", "setNextSearchIndex", "nextState", "getNextState", "setNextState", "calcNext", "", "hasNext", "", "next", "kotlin-stdlib"}, k = 1, mv = {1, 1, 10})
    /* renamed from: kotlin.text.DelimitedRangesSequence$iterator$1, reason: invalid class name */
    public static final class AnonymousClass1 implements Iterator<IntRange>, KMappedMarker {
        private int counter;
        private int currentStartIndex;

        @Nullable
        private IntRange nextItem;
        private int nextSearchIndex;
        private int nextState = -1;

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        AnonymousClass1() {
            this.currentStartIndex = RangesKt.coerceIn(DelimitedRangesSequence.this.startIndex, 0, DelimitedRangesSequence.this.input.length());
            this.nextSearchIndex = this.currentStartIndex;
        }

        public final int getNextState() {
            return this.nextState;
        }

        public final void setNextState(int i) {
            this.nextState = i;
        }

        public final int getCurrentStartIndex() {
            return this.currentStartIndex;
        }

        public final void setCurrentStartIndex(int i) {
            this.currentStartIndex = i;
        }

        public final int getNextSearchIndex() {
            return this.nextSearchIndex;
        }

        public final void setNextSearchIndex(int i) {
            this.nextSearchIndex = i;
        }

        @Nullable
        public final IntRange getNextItem() {
            return this.nextItem;
        }

        public final void setNextItem(@Nullable IntRange intRange) {
            this.nextItem = intRange;
        }

        public final int getCounter() {
            return this.counter;
        }

        public final void setCounter(int i) {
            this.counter = i;
        }

        /* JADX WARN: Removed duplicated region for block: B:9:0x0027  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private final void calcNext() {
            /*
                r6 = this;
                int r0 = r6.nextSearchIndex
                r1 = 0
                if (r0 >= 0) goto Le
                r6.nextState = r1
                r0 = 0
                kotlin.ranges.IntRange r0 = (kotlin.ranges.IntRange) r0
                r6.nextItem = r0
                goto La4
            Le:
                kotlin.text.DelimitedRangesSequence r0 = kotlin.text.DelimitedRangesSequence.this
                int r0 = kotlin.text.DelimitedRangesSequence.access$getLimit$p(r0)
                r2 = -1
                r3 = 1
                if (r0 <= 0) goto L27
                int r0 = r6.counter
                int r0 = r0 + r3
                r6.counter = r0
                int r0 = r6.counter
                kotlin.text.DelimitedRangesSequence r4 = kotlin.text.DelimitedRangesSequence.this
                int r4 = kotlin.text.DelimitedRangesSequence.access$getLimit$p(r4)
                if (r0 >= r4) goto L35
            L27:
                int r0 = r6.nextSearchIndex
                kotlin.text.DelimitedRangesSequence r4 = kotlin.text.DelimitedRangesSequence.this
                java.lang.CharSequence r4 = kotlin.text.DelimitedRangesSequence.access$getInput$p(r4)
                int r4 = r4.length()
                if (r0 <= r4) goto L4b
            L35:
                int r0 = r6.currentStartIndex
                kotlin.ranges.IntRange r1 = new kotlin.ranges.IntRange
                kotlin.text.DelimitedRangesSequence r4 = kotlin.text.DelimitedRangesSequence.this
                java.lang.CharSequence r4 = kotlin.text.DelimitedRangesSequence.access$getInput$p(r4)
                int r4 = kotlin.text.StringsKt.getLastIndex(r4)
                r1.<init>(r0, r4)
                r6.nextItem = r1
                r6.nextSearchIndex = r2
                goto La2
            L4b:
                kotlin.text.DelimitedRangesSequence r0 = kotlin.text.DelimitedRangesSequence.this
                kotlin.jvm.functions.Function2 r0 = kotlin.text.DelimitedRangesSequence.access$getGetNextMatch$p(r0)
                kotlin.text.DelimitedRangesSequence r4 = kotlin.text.DelimitedRangesSequence.this
                java.lang.CharSequence r4 = kotlin.text.DelimitedRangesSequence.access$getInput$p(r4)
                int r5 = r6.nextSearchIndex
                java.lang.Integer r5 = java.lang.Integer.valueOf(r5)
                java.lang.Object r0 = r0.invoke(r4, r5)
                kotlin.Pair r0 = (kotlin.Pair) r0
                if (r0 != 0) goto L7b
                int r0 = r6.currentStartIndex
                kotlin.ranges.IntRange r1 = new kotlin.ranges.IntRange
                kotlin.text.DelimitedRangesSequence r4 = kotlin.text.DelimitedRangesSequence.this
                java.lang.CharSequence r4 = kotlin.text.DelimitedRangesSequence.access$getInput$p(r4)
                int r4 = kotlin.text.StringsKt.getLastIndex(r4)
                r1.<init>(r0, r4)
                r6.nextItem = r1
                r6.nextSearchIndex = r2
                goto La2
            L7b:
                java.lang.Object r2 = r0.component1()
                java.lang.Number r2 = (java.lang.Number) r2
                int r2 = r2.intValue()
                java.lang.Object r0 = r0.component2()
                java.lang.Number r0 = (java.lang.Number) r0
                int r0 = r0.intValue()
                int r4 = r6.currentStartIndex
                kotlin.ranges.IntRange r4 = kotlin.ranges.RangesKt.until(r4, r2)
                r6.nextItem = r4
                int r2 = r2 + r0
                r6.currentStartIndex = r2
                int r2 = r6.currentStartIndex
                if (r0 != 0) goto L9f
                r1 = r3
            L9f:
                int r2 = r2 + r1
                r6.nextSearchIndex = r2
            La2:
                r6.nextState = r3
            La4:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: kotlin.text.DelimitedRangesSequence.AnonymousClass1.calcNext():void");
        }

        @Override // java.util.Iterator
        @NotNull
        public IntRange next() {
            if (this.nextState == -1) {
                calcNext();
            }
            if (this.nextState == 0) {
                throw new NoSuchElementException();
            }
            IntRange intRange = this.nextItem;
            if (intRange == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.ranges.IntRange");
            }
            this.nextItem = (IntRange) null;
            this.nextState = -1;
            return intRange;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.nextState == -1) {
                calcNext();
            }
            return this.nextState == 1;
        }
    }

    @Override // kotlin.sequences.Sequence
    @NotNull
    public Iterator<IntRange> iterator() {
        return new AnonymousClass1();
    }
}
