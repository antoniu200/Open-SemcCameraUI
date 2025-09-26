// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.comparisons;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.internal.InlineOnly;
import kotlin.SinceKotlin;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000(\n\u0002\b\u0002\n\u0002\u0010\u000f\n\u0002\b\u0005\n\u0002\u0010\u0005\n\u0002\u0010\u0006\n\u0002\u0010\u0007\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\u0010\n\n\u0002\b\u0002\u001a-\u0010\u0000\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u0006\u0010\u0003\u001a\u0002H\u00012\u0006\u0010\u0004\u001a\u0002H\u0001H\u0007¢\u0006\u0002\u0010\u0005\u001a5\u0010\u0000\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u0006\u0010\u0003\u001a\u0002H\u00012\u0006\u0010\u0004\u001a\u0002H\u00012\u0006\u0010\u0006\u001a\u0002H\u0001H\u0007¢\u0006\u0002\u0010\u0007\u001a\u0019\u0010\u0000\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\bH\u0087\b\u001a!\u0010\u0000\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\b2\u0006\u0010\u0006\u001a\u00020\bH\u0087\b\u001a\u0019\u0010\u0000\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\tH\u0087\b\u001a!\u0010\u0000\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\t2\u0006\u0010\u0006\u001a\u00020\tH\u0087\b\u001a\u0019\u0010\u0000\u001a\u00020\n2\u0006\u0010\u0003\u001a\u00020\n2\u0006\u0010\u0004\u001a\u00020\nH\u0087\b\u001a!\u0010\u0000\u001a\u00020\n2\u0006\u0010\u0003\u001a\u00020\n2\u0006\u0010\u0004\u001a\u00020\n2\u0006\u0010\u0006\u001a\u00020\nH\u0087\b\u001a\u0019\u0010\u0000\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u000bH\u0087\b\u001a!\u0010\u0000\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u000b2\u0006\u0010\u0006\u001a\u00020\u000bH\u0087\b\u001a\u0019\u0010\u0000\u001a\u00020\f2\u0006\u0010\u0003\u001a\u00020\f2\u0006\u0010\u0004\u001a\u00020\fH\u0087\b\u001a!\u0010\u0000\u001a\u00020\f2\u0006\u0010\u0003\u001a\u00020\f2\u0006\u0010\u0004\u001a\u00020\f2\u0006\u0010\u0006\u001a\u00020\fH\u0087\b\u001a\u0019\u0010\u0000\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\u0006\u0010\u0004\u001a\u00020\rH\u0087\b\u001a!\u0010\u0000\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\u0006\u0010\u0004\u001a\u00020\r2\u0006\u0010\u0006\u001a\u00020\rH\u0087\b\u001a-\u0010\u000e\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u0006\u0010\u0003\u001a\u0002H\u00012\u0006\u0010\u0004\u001a\u0002H\u0001H\u0007¢\u0006\u0002\u0010\u0005\u001a5\u0010\u000e\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u0006\u0010\u0003\u001a\u0002H\u00012\u0006\u0010\u0004\u001a\u0002H\u00012\u0006\u0010\u0006\u001a\u0002H\u0001H\u0007¢\u0006\u0002\u0010\u0007\u001a\u0019\u0010\u000e\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\bH\u0087\b\u001a!\u0010\u000e\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\b2\u0006\u0010\u0006\u001a\u00020\bH\u0087\b\u001a\u0019\u0010\u000e\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\tH\u0087\b\u001a!\u0010\u000e\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\t2\u0006\u0010\u0006\u001a\u00020\tH\u0087\b\u001a\u0019\u0010\u000e\u001a\u00020\n2\u0006\u0010\u0003\u001a\u00020\n2\u0006\u0010\u0004\u001a\u00020\nH\u0087\b\u001a!\u0010\u000e\u001a\u00020\n2\u0006\u0010\u0003\u001a\u00020\n2\u0006\u0010\u0004\u001a\u00020\n2\u0006\u0010\u0006\u001a\u00020\nH\u0087\b\u001a\u0019\u0010\u000e\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u000bH\u0087\b\u001a!\u0010\u000e\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u000b2\u0006\u0010\u0006\u001a\u00020\u000bH\u0087\b\u001a\u0019\u0010\u000e\u001a\u00020\f2\u0006\u0010\u0003\u001a\u00020\f2\u0006\u0010\u0004\u001a\u00020\fH\u0087\b\u001a!\u0010\u000e\u001a\u00020\f2\u0006\u0010\u0003\u001a\u00020\f2\u0006\u0010\u0004\u001a\u00020\f2\u0006\u0010\u0006\u001a\u00020\fH\u0087\b\u001a\u0019\u0010\u000e\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\u0006\u0010\u0004\u001a\u00020\rH\u0087\b\u001a!\u0010\u000e\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\u0006\u0010\u0004\u001a\u00020\r2\u0006\u0010\u0006\u001a\u00020\rH\u0087\b¨\u0006\u000f" }, d2 = { "maxOf", "T", "", "a", "b", "(Ljava/lang/Comparable;Ljava/lang/Comparable;)Ljava/lang/Comparable;", "c", "(Ljava/lang/Comparable;Ljava/lang/Comparable;Ljava/lang/Comparable;)Ljava/lang/Comparable;", "", "", "", "", "", "", "minOf", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/comparisons/ComparisonsKt")
class ComparisonsKt___ComparisonsJvmKt extends ComparisonsKt__ComparisonsKt
{
    public ComparisonsKt___ComparisonsJvmKt() {
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final byte maxOf(final byte a, final byte b) {
        return (byte)Math.max(a, b);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final byte maxOf(final byte a, final byte a2, final byte b) {
        return (byte)Math.max(a, Math.max(a2, b));
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final double maxOf(final double a, final double b) {
        return Math.max(a, b);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final double maxOf(final double a, final double a2, final double b) {
        return Math.max(a, Math.max(a2, b));
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final float maxOf(final float a, final float b) {
        return Math.max(a, b);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final float maxOf(final float a, final float a2, final float b) {
        return Math.max(a, Math.max(a2, b));
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int maxOf(final int a, final int b) {
        return Math.max(a, b);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int maxOf(final int a, final int a2, final int b) {
        return Math.max(a, Math.max(a2, b));
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final long maxOf(final long a, final long b) {
        return Math.max(a, b);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final long maxOf(final long a, final long a2, final long b) {
        return Math.max(a, Math.max(a2, b));
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <T extends Comparable<? super T>> T maxOf(@NotNull T t, @NotNull final T t2) {
        Intrinsics.checkParameterIsNotNull(t, "a");
        Intrinsics.checkParameterIsNotNull(t2, "b");
        if (t.compareTo(t2) < 0) {
            t = t2;
        }
        return t;
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <T extends Comparable<? super T>> T maxOf(@NotNull final T t, @NotNull final T t2, @NotNull final T t3) {
        Intrinsics.checkParameterIsNotNull(t, "a");
        Intrinsics.checkParameterIsNotNull(t2, "b");
        Intrinsics.checkParameterIsNotNull(t3, "c");
        return maxOf(t, maxOf(t2, t3));
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final short maxOf(final short a, final short b) {
        return (short)Math.max(a, b);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final short maxOf(final short a, final short a2, final short b) {
        return (short)Math.max(a, Math.max(a2, b));
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final byte minOf(final byte a, final byte b) {
        return (byte)Math.min(a, b);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final byte minOf(final byte a, final byte a2, final byte b) {
        return (byte)Math.min(a, Math.min(a2, b));
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final double minOf(final double a, final double b) {
        return Math.min(a, b);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final double minOf(final double a, final double a2, final double b) {
        return Math.min(a, Math.min(a2, b));
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final float minOf(final float a, final float b) {
        return Math.min(a, b);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final float minOf(final float a, final float a2, final float b) {
        return Math.min(a, Math.min(a2, b));
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int minOf(final int a, final int b) {
        return Math.min(a, b);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int minOf(final int a, final int a2, final int b) {
        return Math.min(a, Math.min(a2, b));
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final long minOf(final long a, final long b) {
        return Math.min(a, b);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final long minOf(final long a, final long a2, final long b) {
        return Math.min(a, Math.min(a2, b));
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <T extends Comparable<? super T>> T minOf(@NotNull T t, @NotNull final T t2) {
        Intrinsics.checkParameterIsNotNull(t, "a");
        Intrinsics.checkParameterIsNotNull(t2, "b");
        if (t.compareTo(t2) > 0) {
            t = t2;
        }
        return t;
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <T extends Comparable<? super T>> T minOf(@NotNull final T t, @NotNull final T t2, @NotNull final T t3) {
        Intrinsics.checkParameterIsNotNull(t, "a");
        Intrinsics.checkParameterIsNotNull(t2, "b");
        Intrinsics.checkParameterIsNotNull(t3, "c");
        return minOf(t, minOf(t2, t3));
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final short minOf(final short a, final short b) {
        return (short)Math.min(a, b);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final short minOf(final short a, final short a2, final short b) {
        return (short)Math.min(a, Math.min(a2, b));
    }
}
