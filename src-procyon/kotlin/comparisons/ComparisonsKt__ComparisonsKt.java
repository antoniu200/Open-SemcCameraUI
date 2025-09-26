// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.comparisons;

import kotlin.jvm.functions.Function2;
import kotlin.TypeCastException;
import org.jetbrains.annotations.Nullable;
import kotlin.jvm.internal.Intrinsics;
import kotlin.internal.InlineOnly;
import java.util.Comparator;
import org.jetbrains.annotations.NotNull;
import kotlin.jvm.functions.Function1;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000<\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a;\u0010\u0000\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u00022\u001a\b\u0004\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005H\u0087\b\u001aY\u0010\u0000\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u000226\u0010\u0007\u001a\u001c\u0012\u0018\b\u0001\u0012\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u00050\b\"\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005¢\u0006\u0002\u0010\t\u001aW\u0010\u0000\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n2\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\n0\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\n`\u00032\u0014\b\u0004\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\n0\u0005H\u0087\b\u001a;\u0010\f\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u00022\u001a\b\u0004\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005H\u0087\b\u001aW\u0010\f\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n2\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\n0\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\n`\u00032\u0014\b\u0004\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\n0\u0005H\u0087\b\u001a-\u0010\r\u001a\u00020\u000e\"\f\b\u0000\u0010\u0002*\u0006\u0012\u0002\b\u00030\u00062\b\u0010\u000f\u001a\u0004\u0018\u0001H\u00022\b\u0010\u0010\u001a\u0004\u0018\u0001H\u0002¢\u0006\u0002\u0010\u0011\u001a>\u0010\u0012\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u000f\u001a\u0002H\u00022\u0006\u0010\u0010\u001a\u0002H\u00022\u0018\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005H\u0087\b¢\u0006\u0002\u0010\u0013\u001aY\u0010\u0012\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u000f\u001a\u0002H\u00022\u0006\u0010\u0010\u001a\u0002H\u000226\u0010\u0007\u001a\u001c\u0012\u0018\b\u0001\u0012\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u00050\b\"\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005¢\u0006\u0002\u0010\u0014\u001aZ\u0010\u0012\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n2\u0006\u0010\u000f\u001a\u0002H\u00022\u0006\u0010\u0010\u001a\u0002H\u00022\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\n0\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\n`\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\n0\u0005H\u0087\b¢\u0006\u0002\u0010\u0015\u001aG\u0010\u0016\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u000f\u001a\u0002H\u00022\u0006\u0010\u0010\u001a\u0002H\u00022 \u0010\u0007\u001a\u001c\u0012\u0018\b\u0001\u0012\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u00050\bH\u0002¢\u0006\u0004\b\u0017\u0010\u0014\u001a&\u0010\u0018\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0006\u001a-\u0010\u0019\u001a\u0016\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0001j\n\u0012\u0006\u0012\u0004\u0018\u0001H\u0002`\u0003\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0006H\u0087\b\u001a@\u0010\u0019\u001a\u0016\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0001j\n\u0012\u0006\u0012\u0004\u0018\u0001H\u0002`\u0003\"\b\b\u0000\u0010\u0002*\u00020\u001a2\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0003\u001a-\u0010\u001b\u001a\u0016\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0001j\n\u0012\u0006\u0012\u0004\u0018\u0001H\u0002`\u0003\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0006H\u0087\b\u001a@\u0010\u001b\u001a\u0016\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0001j\n\u0012\u0006\u0012\u0004\u0018\u0001H\u0002`\u0003\"\b\b\u0000\u0010\u0002*\u00020\u001a2\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0003\u001a&\u0010\u001c\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0006\u001a0\u0010\u001d\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\u001aO\u0010\u001e\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u00032\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0003H\u0086\u0004\u001aO\u0010\u001f\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u00032\u001a\b\u0004\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005H\u0087\b\u001ak\u0010\u001f\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u00032\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\n0\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\n`\u00032\u0014\b\u0004\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\n0\u0005H\u0087\b\u001aO\u0010 \u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u00032\u001a\b\u0004\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005H\u0087\b\u001ak\u0010 \u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u00032\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\n0\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\n`\u00032\u0014\b\u0004\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\n0\u0005H\u0087\b\u001am\u0010!\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u000328\b\u0004\u0010\"\u001a2\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b$\u0012\b\b%\u0012\u0004\b\b(\u000f\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b$\u0012\b\b%\u0012\u0004\b\b(\u0010\u0012\u0004\u0012\u00020\u000e0#H\u0087\b\u001aO\u0010&\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u00032\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0003H\u0086\u0004¨\u0006'" }, d2 = { "compareBy", "Ljava/util/Comparator;", "T", "Lkotlin/Comparator;", "selector", "Lkotlin/Function1;", "", "selectors", "", "([Lkotlin/jvm/functions/Function1;)Ljava/util/Comparator;", "K", "comparator", "compareByDescending", "compareValues", "", "a", "b", "(Ljava/lang/Comparable;Ljava/lang/Comparable;)I", "compareValuesBy", "(Ljava/lang/Object;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)I", "(Ljava/lang/Object;Ljava/lang/Object;[Lkotlin/jvm/functions/Function1;)I", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/util/Comparator;Lkotlin/jvm/functions/Function1;)I", "compareValuesByImpl", "compareValuesByImpl$ComparisonsKt__ComparisonsKt", "naturalOrder", "nullsFirst", "", "nullsLast", "reverseOrder", "reversed", "then", "thenBy", "thenByDescending", "thenComparator", "comparison", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "thenDescending", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/comparisons/ComparisonsKt")
class ComparisonsKt__ComparisonsKt
{
    public ComparisonsKt__ComparisonsKt() {
    }
    
    @InlineOnly
    private static final <T, K> Comparator<T> compareBy(final Comparator<? super K> comparator, final Function1<? super T, ? extends K> function1) {
        return (Comparator<T>)new ComparisonsKt__ComparisonsKt$compareBy.ComparisonsKt__ComparisonsKt$compareBy$3((Comparator)comparator, (Function1)function1);
    }
    
    @InlineOnly
    private static final <T> Comparator<T> compareBy(final Function1<? super T, ? extends Comparable<?>> function1) {
        return (Comparator<T>)new ComparisonsKt__ComparisonsKt$compareBy.ComparisonsKt__ComparisonsKt$compareBy$2((Function1)function1);
    }
    
    @NotNull
    public static final <T> Comparator<T> compareBy(@NotNull final Function1<? super T, ? extends Comparable<?>>... array) {
        Intrinsics.checkParameterIsNotNull(array, "selectors");
        if (array.length <= 0) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        return (Comparator<T>)new ComparisonsKt__ComparisonsKt$compareBy.ComparisonsKt__ComparisonsKt$compareBy$1((Function1[])array);
    }
    
    @InlineOnly
    private static final <T, K> Comparator<T> compareByDescending(final Comparator<? super K> comparator, final Function1<? super T, ? extends K> function1) {
        return (Comparator<T>)new ComparisonsKt__ComparisonsKt$compareByDescending.ComparisonsKt__ComparisonsKt$compareByDescending$2((Comparator)comparator, (Function1)function1);
    }
    
    @InlineOnly
    private static final <T> Comparator<T> compareByDescending(final Function1<? super T, ? extends Comparable<?>> function1) {
        return (Comparator<T>)new ComparisonsKt__ComparisonsKt$compareByDescending.ComparisonsKt__ComparisonsKt$compareByDescending$1((Function1)function1);
    }
    
    public static final <T extends Comparable<?>> int compareValues(@Nullable final T t, @Nullable final T t2) {
        if (t == t2) {
            return 0;
        }
        if (t == null) {
            return -1;
        }
        if (t2 == null) {
            return 1;
        }
        return t.compareTo(t2);
    }
    
    @InlineOnly
    private static final <T, K> int compareValuesBy(final T t, final T t2, final Comparator<? super K> comparator, final Function1<? super T, ? extends K> function1) {
        return comparator.compare((Object)function1.invoke(t), (Object)function1.invoke(t2));
    }
    
    @InlineOnly
    private static final <T> int compareValuesBy(final T t, final T t2, final Function1<? super T, ? extends Comparable<?>> function1) {
        return compareValues((Comparable)function1.invoke(t), (Comparable)function1.invoke(t2));
    }
    
    public static final <T> int compareValuesBy(final T t, final T t2, @NotNull final Function1<? super T, ? extends Comparable<?>>... array) {
        Intrinsics.checkParameterIsNotNull(array, "selectors");
        if (((Function1<? super Object, ? extends Comparable<?>>[])array).length <= 0) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        return compareValuesByImpl$ComparisonsKt__ComparisonsKt(t, (Object)t2, (Function1<? super Object, ? extends Comparable<?>>[])array);
    }
    
    private static final <T> int compareValuesByImpl$ComparisonsKt__ComparisonsKt(final T t, final T t2, final Function1<? super T, ? extends Comparable<?>>[] array) {
        for (final Function1<? super T, ? extends Comparable<?>> function1 : array) {
            final int compareValues = compareValues((Comparable)function1.invoke(t), (Comparable)function1.invoke(t2));
            if (compareValues != 0) {
                return compareValues;
            }
        }
        return 0;
    }
    
    @NotNull
    public static final <T extends Comparable<? super T>> Comparator<T> naturalOrder() {
        final NaturalOrderComparator instance = NaturalOrderComparator.INSTANCE;
        if (instance == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Comparator<T> /* = java.util.Comparator<T> */");
        }
        return (Comparator<T>)instance;
    }
    
    @InlineOnly
    private static final <T extends Comparable<? super T>> Comparator<T> nullsFirst() {
        return nullsFirst(naturalOrder());
    }
    
    @NotNull
    public static final <T> Comparator<T> nullsFirst(@NotNull final Comparator<? super T> comparator) {
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        return (Comparator<T>)new ComparisonsKt__ComparisonsKt$nullsFirst.ComparisonsKt__ComparisonsKt$nullsFirst$1((Comparator)comparator);
    }
    
    @InlineOnly
    private static final <T extends Comparable<? super T>> Comparator<T> nullsLast() {
        return nullsLast(naturalOrder());
    }
    
    @NotNull
    public static final <T> Comparator<T> nullsLast(@NotNull final Comparator<? super T> comparator) {
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        return (Comparator<T>)new ComparisonsKt__ComparisonsKt$nullsLast.ComparisonsKt__ComparisonsKt$nullsLast$1((Comparator)comparator);
    }
    
    @NotNull
    public static final <T extends Comparable<? super T>> Comparator<T> reverseOrder() {
        final ReverseOrderComparator instance = ReverseOrderComparator.INSTANCE;
        if (instance == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Comparator<T> /* = java.util.Comparator<T> */");
        }
        return (Comparator<T>)instance;
    }
    
    @NotNull
    public static final <T> Comparator<T> reversed(@NotNull final Comparator<T> comparator) {
        Intrinsics.checkParameterIsNotNull(comparator, "$receiver");
        Comparator comparator2;
        if (comparator instanceof ReversedComparator) {
            comparator2 = ((ReversedComparator)comparator).getComparator();
        }
        else if (Intrinsics.areEqual(comparator, NaturalOrderComparator.INSTANCE)) {
            final ReverseOrderComparator instance = ReverseOrderComparator.INSTANCE;
            if (instance == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.Comparator<T> /* = java.util.Comparator<T> */");
            }
            comparator2 = instance;
        }
        else if (Intrinsics.areEqual(comparator, ReverseOrderComparator.INSTANCE)) {
            final NaturalOrderComparator instance2 = NaturalOrderComparator.INSTANCE;
            if (instance2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.Comparator<T> /* = java.util.Comparator<T> */");
            }
            comparator2 = instance2;
        }
        else {
            comparator2 = new ReversedComparator(comparator);
        }
        return comparator2;
    }
    
    @NotNull
    public static final <T> Comparator<T> then(@NotNull final Comparator<T> comparator, @NotNull final Comparator<? super T> comparator2) {
        Intrinsics.checkParameterIsNotNull(comparator, "$receiver");
        Intrinsics.checkParameterIsNotNull(comparator2, "comparator");
        return (Comparator<T>)new ComparisonsKt__ComparisonsKt$then.ComparisonsKt__ComparisonsKt$then$1((Comparator)comparator, (Comparator)comparator2);
    }
    
    @InlineOnly
    private static final <T, K> Comparator<T> thenBy(@NotNull final Comparator<T> comparator, final Comparator<? super K> comparator2, final Function1<? super T, ? extends K> function1) {
        return (Comparator<T>)new ComparisonsKt__ComparisonsKt$thenBy.ComparisonsKt__ComparisonsKt$thenBy$2((Comparator)comparator, (Comparator)comparator2, (Function1)function1);
    }
    
    @InlineOnly
    private static final <T> Comparator<T> thenBy(@NotNull final Comparator<T> comparator, final Function1<? super T, ? extends Comparable<?>> function1) {
        return (Comparator<T>)new ComparisonsKt__ComparisonsKt$thenBy.ComparisonsKt__ComparisonsKt$thenBy$1((Comparator)comparator, (Function1)function1);
    }
    
    @InlineOnly
    private static final <T, K> Comparator<T> thenByDescending(@NotNull final Comparator<T> comparator, final Comparator<? super K> comparator2, final Function1<? super T, ? extends K> function1) {
        return (Comparator<T>)new ComparisonsKt__ComparisonsKt$thenByDescending.ComparisonsKt__ComparisonsKt$thenByDescending$2((Comparator)comparator, (Comparator)comparator2, (Function1)function1);
    }
    
    @InlineOnly
    private static final <T> Comparator<T> thenByDescending(@NotNull final Comparator<T> comparator, final Function1<? super T, ? extends Comparable<?>> function1) {
        return (Comparator<T>)new ComparisonsKt__ComparisonsKt$thenByDescending.ComparisonsKt__ComparisonsKt$thenByDescending$1((Comparator)comparator, (Function1)function1);
    }
    
    @InlineOnly
    private static final <T> Comparator<T> thenComparator(@NotNull final Comparator<T> comparator, final Function2<? super T, ? super T, Integer> function2) {
        return (Comparator<T>)new ComparisonsKt__ComparisonsKt$thenComparator.ComparisonsKt__ComparisonsKt$thenComparator$1((Comparator)comparator, (Function2)function2);
    }
    
    @NotNull
    public static final <T> Comparator<T> thenDescending(@NotNull final Comparator<T> comparator, @NotNull final Comparator<? super T> comparator2) {
        Intrinsics.checkParameterIsNotNull(comparator, "$receiver");
        Intrinsics.checkParameterIsNotNull(comparator2, "comparator");
        return (Comparator<T>)new ComparisonsKt__ComparisonsKt$thenDescending.ComparisonsKt__ComparisonsKt$thenDescending$1((Comparator)comparator, (Comparator)comparator2);
    }
}
