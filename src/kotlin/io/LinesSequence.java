package kotlin.io;

import java.io.BufferedReader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.NotNull;

/* compiled from: ReadWrite.kt */
@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010(\n\u0000\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u000f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00020\u0007H\u0096\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\b"}, d2 = {"Lkotlin/io/LinesSequence;", "Lkotlin/sequences/Sequence;", "", "reader", "Ljava/io/BufferedReader;", "(Ljava/io/BufferedReader;)V", "iterator", "", "kotlin-stdlib"}, k = 1, mv = {1, 1, 10})
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
final class LinesSequence implements Sequence<String> {
    private final BufferedReader reader;

    public LinesSequence(@NotNull BufferedReader reader) {
        Intrinsics.checkParameterIsNotNull(reader, "reader");
        this.reader = reader;
    }

    /* compiled from: ReadWrite.kt */
    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0019\n\u0000\n\u0002\u0010(\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0003J\t\u0010\u0007\u001a\u00020\u0005H\u0096\u0002J\t\u0010\b\u001a\u00020\u0002H\u0096\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0002X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"kotlin/io/LinesSequence$iterator$1", "", "", "(Lkotlin/io/LinesSequence;)V", "done", "", "nextValue", "hasNext", "next", "kotlin-stdlib"}, k = 1, mv = {1, 1, 10})
    /* renamed from: kotlin.io.LinesSequence$iterator$1, reason: invalid class name */
    public static final class AnonymousClass1 implements Iterator<String>, KMappedMarker {
        private boolean done;
        private String nextValue;

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        AnonymousClass1() {
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.nextValue == null && !this.done) {
                this.nextValue = LinesSequence.this.reader.readLine();
                if (this.nextValue == null) {
                    this.done = true;
                }
            }
            return this.nextValue != null;
        }

        @Override // java.util.Iterator
        @NotNull
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            String str = this.nextValue;
            this.nextValue = (String) null;
            if (str == null) {
                Intrinsics.throwNpe();
            }
            return str;
        }
    }

    @Override // kotlin.sequences.Sequence
    @NotNull
    public Iterator<String> iterator() {
        return new AnonymousClass1();
    }
}
