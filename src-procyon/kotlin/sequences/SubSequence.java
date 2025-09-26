// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.sequences;

import java.util.Iterator;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010(\n\u0002\b\u0002\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003B#\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006¢\u0006\u0002\u0010\bJ\u0016\u0010\f\u001a\b\u0012\u0004\u0012\u00028\u00000\u00022\u0006\u0010\r\u001a\u00020\u0006H\u0016J\u000f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00028\u00000\u000fH\u0096\u0002J\u0016\u0010\u0010\u001a\b\u0012\u0004\u0012\u00028\u00000\u00022\u0006\u0010\r\u001a\u00020\u0006H\u0016R\u0014\u0010\t\u001a\u00020\u00068BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0011" }, d2 = { "Lkotlin/sequences/SubSequence;", "T", "Lkotlin/sequences/Sequence;", "Lkotlin/sequences/DropTakeSequence;", "sequence", "startIndex", "", "endIndex", "(Lkotlin/sequences/Sequence;II)V", "count", "getCount", "()I", "drop", "n", "iterator", "", "take", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
public final class SubSequence<T> implements Sequence<T>, DropTakeSequence<T>
{
    private final int endIndex;
    private final Sequence<T> sequence;
    private final int startIndex;
    
    public SubSequence(@NotNull final Sequence<? extends T> sequence, int startIndex, int endIndex) {
        Intrinsics.checkParameterIsNotNull(sequence, "sequence");
        this.sequence = (Sequence<T>)sequence;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        startIndex = this.startIndex;
        endIndex = 0;
        if (startIndex >= 0) {
            startIndex = 1;
        }
        else {
            startIndex = 0;
        }
        if (startIndex == 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("startIndex should be non-negative, but is ");
            sb.append(this.startIndex);
            throw new IllegalArgumentException(sb.toString().toString());
        }
        if (this.endIndex >= 0) {
            startIndex = 1;
        }
        else {
            startIndex = 0;
        }
        if (startIndex == 0) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("endIndex should be non-negative, but is ");
            sb2.append(this.endIndex);
            throw new IllegalArgumentException(sb2.toString().toString());
        }
        startIndex = endIndex;
        if (this.endIndex >= this.startIndex) {
            startIndex = 1;
        }
        if (startIndex == 0) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("endIndex should be not less than startIndex, but was ");
            sb3.append(this.endIndex);
            sb3.append(" < ");
            sb3.append(this.startIndex);
            throw new IllegalArgumentException(sb3.toString().toString());
        }
    }
    
    private final int getCount() {
        return this.endIndex - this.startIndex;
    }
    
    @NotNull
    @Override
    public Sequence<T> drop(final int n) {
        Sequence<Object> emptySequence;
        if (n >= this.getCount()) {
            emptySequence = SequencesKt__SequencesKt.emptySequence();
        }
        else {
            emptySequence = new SubSequence<Object>(this.sequence, this.startIndex + n, this.endIndex);
        }
        return (Sequence<T>)emptySequence;
    }
    
    @NotNull
    @Override
    public Iterator<T> iterator() {
        return (Iterator<T>)new SubSequence$iterator.SubSequence$iterator$1(this);
    }
    
    @NotNull
    @Override
    public Sequence<T> take(final int n) {
        Sequence sequence;
        if (n >= this.getCount()) {
            sequence = this;
        }
        else {
            sequence = new SubSequence(this.sequence, this.startIndex, this.startIndex + n);
        }
        return sequence;
    }
}
