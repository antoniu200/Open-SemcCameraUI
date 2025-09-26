// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.text;

import kotlin.TuplesKt;
import kotlin.collections.IndexingIterable;
import kotlin.jvm.functions.Function0;
import kotlin.collections.IndexedValue;
import kotlin.ranges.IntProgression;
import kotlin.collections.SlidingWindowKt;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.HashSet;
import kotlin.ranges.IntRange;
import java.util.Iterator;
import kotlin.TypeCastException;
import java.util.Comparator;
import kotlin.collections.Grouping;
import kotlin.Unit;
import kotlin.jvm.functions.Function3;
import java.util.ArrayList;
import java.util.Collection;
import org.jetbrains.annotations.Nullable;
import java.util.NoSuchElementException;
import kotlin.jvm.functions.Function2;
import kotlin.internal.InlineOnly;
import kotlin.SinceKotlin;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Pair;
import kotlin.sequences.Sequence;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u00d4\u0001\n\u0000\n\u0002\u0010\u000b\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\f\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010%\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u001f\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0010\u000f\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\"\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\u001a!\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\n\u0010\u0006\u001a\u00020\u0001*\u00020\u0002\u001a!\u0010\u0006\u001a\u00020\u0001*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\u0010\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\b*\u00020\u0002\u001a\u0010\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00050\n*\u00020\u0002\u001aE\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u000e0\f\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e*\u00020\u00022\u001e\u0010\u000f\u001a\u001a\u0012\u0004\u0012\u00020\u0005\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u000e0\u00100\u0004H\u0086\b\u001a3\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u00020\u00050\f\"\u0004\b\u0000\u0010\r*\u00020\u00022\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u0004H\u0086\b\u001aM\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u000e0\f\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e*\u00020\u00022\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u00042\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\u0086\b\u001aN\u0010\u0014\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\r\"\u0018\b\u0001\u0010\u0015*\u0012\u0012\u0006\b\u0000\u0012\u0002H\r\u0012\u0006\b\u0000\u0012\u00020\u00050\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u0004H\u0086\b¢\u0006\u0002\u0010\u0018\u001ah\u0010\u0014\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e\"\u0018\b\u0002\u0010\u0015*\u0012\u0012\u0006\b\u0000\u0012\u0002H\r\u0012\u0006\b\u0000\u0012\u0002H\u000e0\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u00042\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\u0086\b¢\u0006\u0002\u0010\u0019\u001a`\u0010\u001a\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e\"\u0018\b\u0002\u0010\u0015*\u0012\u0012\u0006\b\u0000\u0012\u0002H\r\u0012\u0006\b\u0000\u0012\u0002H\u000e0\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u001e\u0010\u000f\u001a\u001a\u0012\u0004\u0012\u00020\u0005\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u000e0\u00100\u0004H\u0086\b¢\u0006\u0002\u0010\u0018\u001a\u001a\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001c*\u00020\u00022\u0006\u0010\u001e\u001a\u00020\u001fH\u0007\u001a4\u0010\u001b\u001a\b\u0012\u0004\u0012\u0002H 0\u001c\"\u0004\b\u0000\u0010 *\u00020\u00022\u0006\u0010\u001e\u001a\u00020\u001f2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H 0\u0004H\u0007\u001a\u001a\u0010!\u001a\b\u0012\u0004\u0012\u00020\u001d0\n*\u00020\u00022\u0006\u0010\u001e\u001a\u00020\u001fH\u0007\u001a4\u0010!\u001a\b\u0012\u0004\u0012\u0002H 0\n\"\u0004\b\u0000\u0010 *\u00020\u00022\u0006\u0010\u001e\u001a\u00020\u001f2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H 0\u0004H\u0007\u001a\r\u0010\"\u001a\u00020\u001f*\u00020\u0002H\u0087\b\u001a!\u0010\"\u001a\u00020\u001f*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\u0012\u0010#\u001a\u00020\u0002*\u00020\u00022\u0006\u0010$\u001a\u00020\u001f\u001a\u0012\u0010#\u001a\u00020\u001d*\u00020\u001d2\u0006\u0010$\u001a\u00020\u001f\u001a\u0012\u0010%\u001a\u00020\u0002*\u00020\u00022\u0006\u0010$\u001a\u00020\u001f\u001a\u0012\u0010%\u001a\u00020\u001d*\u00020\u001d2\u0006\u0010$\u001a\u00020\u001f\u001a!\u0010&\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u0010&\u001a\u00020\u001d*\u00020\u001d2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u0010'\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u0010'\u001a\u00020\u001d*\u00020\u001d2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\u0015\u0010(\u001a\u00020\u0005*\u00020\u00022\u0006\u0010)\u001a\u00020\u001fH\u0087\b\u001a)\u0010*\u001a\u00020\u0005*\u00020\u00022\u0006\u0010)\u001a\u00020\u001f2\u0012\u0010+\u001a\u000e\u0012\u0004\u0012\u00020\u001f\u0012\u0004\u0012\u00020\u00050\u0004H\u0087\b\u001a\u001c\u0010,\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0006\u0010)\u001a\u00020\u001fH\u0087\b¢\u0006\u0002\u0010-\u001a!\u0010.\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u0010.\u001a\u00020\u001d*\u00020\u001d2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a6\u0010/\u001a\u00020\u0002*\u00020\u00022'\u0010\u0003\u001a#\u0012\u0013\u0012\u00110\u001f¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b()\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000100H\u0086\b\u001a6\u0010/\u001a\u00020\u001d*\u00020\u001d2'\u0010\u0003\u001a#\u0012\u0013\u0012\u00110\u001f¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b()\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000100H\u0086\b\u001aQ\u00103\u001a\u0002H4\"\f\b\u0000\u00104*\u000605j\u0002`6*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H42'\u0010\u0003\u001a#\u0012\u0013\u0012\u00110\u001f¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b()\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000100H\u0086\b¢\u0006\u0002\u00107\u001a!\u00108\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u00108\u001a\u00020\u001d*\u00020\u001d2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a<\u00109\u001a\u0002H4\"\f\b\u0000\u00104*\u000605j\u0002`6*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H42\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b¢\u0006\u0002\u0010:\u001a<\u0010;\u001a\u0002H4\"\f\b\u0000\u00104*\u000605j\u0002`6*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H42\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b¢\u0006\u0002\u0010:\u001a(\u0010<\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0087\b¢\u0006\u0002\u0010=\u001a(\u0010>\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0087\b¢\u0006\u0002\u0010=\u001a\n\u0010?\u001a\u00020\u0005*\u00020\u0002\u001a!\u0010?\u001a\u00020\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\u0011\u0010@\u001a\u0004\u0018\u00010\u0005*\u00020\u0002¢\u0006\u0002\u0010A\u001a(\u0010@\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b¢\u0006\u0002\u0010=\u001a3\u0010B\u001a\b\u0012\u0004\u0012\u0002H 0\u001c\"\u0004\b\u0000\u0010 *\u00020\u00022\u0018\u0010\u000f\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u0002H 0\b0\u0004H\u0086\b\u001aL\u0010C\u001a\u0002H4\"\u0004\b\u0000\u0010 \"\u0010\b\u0001\u00104*\n\u0012\u0006\b\u0000\u0012\u0002H 0D*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H42\u0018\u0010\u000f\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u0002H 0\b0\u0004H\u0086\b¢\u0006\u0002\u0010E\u001aI\u0010F\u001a\u0002H \"\u0004\b\u0000\u0010 *\u00020\u00022\u0006\u0010G\u001a\u0002H 2'\u0010H\u001a#\u0012\u0013\u0012\u0011H ¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b(I\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H 00H\u0086\b¢\u0006\u0002\u0010J\u001a^\u0010K\u001a\u0002H \"\u0004\b\u0000\u0010 *\u00020\u00022\u0006\u0010G\u001a\u0002H 2<\u0010H\u001a8\u0012\u0013\u0012\u00110\u001f¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b()\u0012\u0013\u0012\u0011H ¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b(I\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H 0LH\u0086\b¢\u0006\u0002\u0010M\u001aI\u0010N\u001a\u0002H \"\u0004\b\u0000\u0010 *\u00020\u00022\u0006\u0010G\u001a\u0002H 2'\u0010H\u001a#\u0012\u0004\u0012\u00020\u0005\u0012\u0013\u0012\u0011H ¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b(I\u0012\u0004\u0012\u0002H 00H\u0086\b¢\u0006\u0002\u0010J\u001a^\u0010O\u001a\u0002H \"\u0004\b\u0000\u0010 *\u00020\u00022\u0006\u0010G\u001a\u0002H 2<\u0010H\u001a8\u0012\u0013\u0012\u00110\u001f¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b()\u0012\u0004\u0012\u00020\u0005\u0012\u0013\u0012\u0011H ¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b(I\u0012\u0004\u0012\u0002H 0LH\u0086\b¢\u0006\u0002\u0010M\u001a!\u0010P\u001a\u00020Q*\u00020\u00022\u0012\u0010R\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020Q0\u0004H\u0086\b\u001a6\u0010S\u001a\u00020Q*\u00020\u00022'\u0010R\u001a#\u0012\u0013\u0012\u00110\u001f¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b()\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020Q00H\u0086\b\u001a)\u0010T\u001a\u00020\u0005*\u00020\u00022\u0006\u0010)\u001a\u00020\u001f2\u0012\u0010+\u001a\u000e\u0012\u0004\u0012\u00020\u001f\u0012\u0004\u0012\u00020\u00050\u0004H\u0087\b\u001a\u0019\u0010U\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0006\u0010)\u001a\u00020\u001f¢\u0006\u0002\u0010-\u001a9\u0010V\u001a\u0014\u0012\u0004\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u001c0\f\"\u0004\b\u0000\u0010\r*\u00020\u00022\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u0004H\u0086\b\u001aS\u0010V\u001a\u0014\u0012\u0004\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000e0\u001c0\f\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e*\u00020\u00022\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u00042\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\u0086\b\u001aR\u0010W\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\r\"\u001c\b\u0001\u0010\u0015*\u0016\u0012\u0006\b\u0000\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050X0\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u0004H\u0086\b¢\u0006\u0002\u0010\u0018\u001al\u0010W\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e\"\u001c\b\u0002\u0010\u0015*\u0016\u0012\u0006\b\u0000\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000e0X0\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u00042\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\u0086\b¢\u0006\u0002\u0010\u0019\u001a5\u0010Y\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0Z\"\u0004\b\u0000\u0010\r*\u00020\u00022\u0014\b\u0004\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u0004H\u0087\b\u001a!\u0010[\u001a\u00020\u001f*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u0010\\\u001a\u00020\u001f*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\n\u0010]\u001a\u00020\u0005*\u00020\u0002\u001a!\u0010]\u001a\u00020\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\u0011\u0010^\u001a\u0004\u0018\u00010\u0005*\u00020\u0002¢\u0006\u0002\u0010A\u001a(\u0010^\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b¢\u0006\u0002\u0010=\u001a-\u0010_\u001a\b\u0012\u0004\u0012\u0002H 0\u001c\"\u0004\b\u0000\u0010 *\u00020\u00022\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H 0\u0004H\u0086\b\u001aB\u0010`\u001a\b\u0012\u0004\u0012\u0002H 0\u001c\"\u0004\b\u0000\u0010 *\u00020\u00022'\u0010\u000f\u001a#\u0012\u0013\u0012\u00110\u001f¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b()\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H 00H\u0086\b\u001aH\u0010a\u001a\b\u0012\u0004\u0012\u0002H 0\u001c\"\b\b\u0000\u0010 *\u00020b*\u00020\u00022)\u0010\u000f\u001a%\u0012\u0013\u0012\u00110\u001f¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b()\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u0001H 00H\u0086\b\u001aa\u0010c\u001a\u0002H4\"\b\b\u0000\u0010 *\u00020b\"\u0010\b\u0001\u00104*\n\u0012\u0006\b\u0000\u0012\u0002H 0D*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H42)\u0010\u000f\u001a%\u0012\u0013\u0012\u00110\u001f¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b()\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u0001H 00H\u0086\b¢\u0006\u0002\u0010d\u001a[\u0010e\u001a\u0002H4\"\u0004\b\u0000\u0010 \"\u0010\b\u0001\u00104*\n\u0012\u0006\b\u0000\u0012\u0002H 0D*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H42'\u0010\u000f\u001a#\u0012\u0013\u0012\u00110\u001f¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b()\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H 00H\u0086\b¢\u0006\u0002\u0010d\u001a3\u0010f\u001a\b\u0012\u0004\u0012\u0002H 0\u001c\"\b\b\u0000\u0010 *\u00020b*\u00020\u00022\u0014\u0010\u000f\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u0001H 0\u0004H\u0086\b\u001aL\u0010g\u001a\u0002H4\"\b\b\u0000\u0010 *\u00020b\"\u0010\b\u0001\u00104*\n\u0012\u0006\b\u0000\u0012\u0002H 0D*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H42\u0014\u0010\u000f\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u0001H 0\u0004H\u0086\b¢\u0006\u0002\u0010E\u001aF\u0010h\u001a\u0002H4\"\u0004\b\u0000\u0010 \"\u0010\b\u0001\u00104*\n\u0012\u0006\b\u0000\u0012\u0002H 0D*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H42\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H 0\u0004H\u0086\b¢\u0006\u0002\u0010E\u001a\u0011\u0010i\u001a\u0004\u0018\u00010\u0005*\u00020\u0002¢\u0006\u0002\u0010A\u001a8\u0010j\u001a\u0004\u0018\u00010\u0005\"\u000e\b\u0000\u0010 *\b\u0012\u0004\u0012\u0002H 0k*\u00020\u00022\u0012\u0010l\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H 0\u0004H\u0086\b¢\u0006\u0002\u0010=\u001a-\u0010m\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u001a\u0010n\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00050oj\n\u0012\u0006\b\u0000\u0012\u00020\u0005`p¢\u0006\u0002\u0010q\u001a\u0011\u0010r\u001a\u0004\u0018\u00010\u0005*\u00020\u0002¢\u0006\u0002\u0010A\u001a8\u0010s\u001a\u0004\u0018\u00010\u0005\"\u000e\b\u0000\u0010 *\b\u0012\u0004\u0012\u0002H 0k*\u00020\u00022\u0012\u0010l\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H 0\u0004H\u0086\b¢\u0006\u0002\u0010=\u001a-\u0010t\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u001a\u0010n\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00050oj\n\u0012\u0006\b\u0000\u0012\u00020\u0005`p¢\u0006\u0002\u0010q\u001a\n\u0010u\u001a\u00020\u0001*\u00020\u0002\u001a!\u0010u\u001a\u00020\u0001*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a0\u0010v\u001a\u0002Hw\"\b\b\u0000\u0010w*\u00020\u0002*\u0002Hw2\u0012\u0010R\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020Q0\u0004H\u0087\b¢\u0006\u0002\u0010x\u001a-\u0010y\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u0010*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a-\u0010y\u001a\u000e\u0012\u0004\u0012\u00020\u001d\u0012\u0004\u0012\u00020\u001d0\u0010*\u00020\u001d2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a6\u0010z\u001a\u00020\u0005*\u00020\u00022'\u0010H\u001a#\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b(I\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000500H\u0086\b\u001aK\u0010{\u001a\u00020\u0005*\u00020\u00022<\u0010H\u001a8\u0012\u0013\u0012\u00110\u001f¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b()\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b(I\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050LH\u0086\b\u001a6\u0010|\u001a\u00020\u0005*\u00020\u00022'\u0010H\u001a#\u0012\u0004\u0012\u00020\u0005\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b(I\u0012\u0004\u0012\u00020\u000500H\u0086\b\u001aK\u0010}\u001a\u00020\u0005*\u00020\u00022<\u0010H\u001a8\u0012\u0013\u0012\u00110\u001f¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b()\u0012\u0004\u0012\u00020\u0005\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b(I\u0012\u0004\u0012\u00020\u00050LH\u0086\b\u001a\n\u0010~\u001a\u00020\u0002*\u00020\u0002\u001a\r\u0010~\u001a\u00020\u001d*\u00020\u001dH\u0087\b\u001a\n\u0010\u007f\u001a\u00020\u0005*\u00020\u0002\u001a!\u0010\u007f\u001a\u00020\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\u0012\u0010\u0080\u0001\u001a\u0004\u0018\u00010\u0005*\u00020\u0002¢\u0006\u0002\u0010A\u001a)\u0010\u0080\u0001\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b¢\u0006\u0002\u0010=\u001a\u001a\u0010\u0081\u0001\u001a\u00020\u0002*\u00020\u00022\r\u0010\u0082\u0001\u001a\b\u0012\u0004\u0012\u00020\u001f0\b\u001a\u0015\u0010\u0081\u0001\u001a\u00020\u0002*\u00020\u00022\b\u0010\u0082\u0001\u001a\u00030\u0083\u0001\u001a\u001d\u0010\u0081\u0001\u001a\u00020\u001d*\u00020\u001d2\r\u0010\u0082\u0001\u001a\b\u0012\u0004\u0012\u00020\u001f0\bH\u0087\b\u001a\u0015\u0010\u0081\u0001\u001a\u00020\u001d*\u00020\u001d2\b\u0010\u0082\u0001\u001a\u00030\u0083\u0001\u001a\"\u0010\u0084\u0001\u001a\u00020\u001f*\u00020\u00022\u0012\u0010l\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u001f0\u0004H\u0086\b\u001a$\u0010\u0085\u0001\u001a\u00030\u0086\u0001*\u00020\u00022\u0013\u0010l\u001a\u000f\u0012\u0004\u0012\u00020\u0005\u0012\u0005\u0012\u00030\u0086\u00010\u0004H\u0086\b\u001a\u0013\u0010\u0087\u0001\u001a\u00020\u0002*\u00020\u00022\u0006\u0010$\u001a\u00020\u001f\u001a\u0013\u0010\u0087\u0001\u001a\u00020\u001d*\u00020\u001d2\u0006\u0010$\u001a\u00020\u001f\u001a\u0013\u0010\u0088\u0001\u001a\u00020\u0002*\u00020\u00022\u0006\u0010$\u001a\u00020\u001f\u001a\u0013\u0010\u0088\u0001\u001a\u00020\u001d*\u00020\u001d2\u0006\u0010$\u001a\u00020\u001f\u001a\"\u0010\u0089\u0001\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\"\u0010\u0089\u0001\u001a\u00020\u001d*\u00020\u001d2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\"\u0010\u008a\u0001\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\"\u0010\u008a\u0001\u001a\u00020\u001d*\u00020\u001d2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a+\u0010\u008b\u0001\u001a\u0002H4\"\u0010\b\u0000\u00104*\n\u0012\u0006\b\u0000\u0012\u00020\u00050D*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H4¢\u0006\u0003\u0010\u008c\u0001\u001a\u001d\u0010\u008d\u0001\u001a\u0014\u0012\u0004\u0012\u00020\u00050\u008e\u0001j\t\u0012\u0004\u0012\u00020\u0005`\u008f\u0001*\u00020\u0002\u001a\u0011\u0010\u0090\u0001\u001a\b\u0012\u0004\u0012\u00020\u00050\u001c*\u00020\u0002\u001a\u0011\u0010\u0091\u0001\u001a\b\u0012\u0004\u0012\u00020\u00050X*\u00020\u0002\u001a\u0012\u0010\u0092\u0001\u001a\t\u0012\u0004\u0012\u00020\u00050\u0093\u0001*\u00020\u0002\u001a1\u0010\u0094\u0001\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001c*\u00020\u00022\u0006\u0010\u001e\u001a\u00020\u001f2\t\b\u0002\u0010\u0095\u0001\u001a\u00020\u001f2\t\b\u0002\u0010\u0096\u0001\u001a\u00020\u0001H\u0007\u001aK\u0010\u0094\u0001\u001a\b\u0012\u0004\u0012\u0002H 0\u001c\"\u0004\b\u0000\u0010 *\u00020\u00022\u0006\u0010\u001e\u001a\u00020\u001f2\t\b\u0002\u0010\u0095\u0001\u001a\u00020\u001f2\t\b\u0002\u0010\u0096\u0001\u001a\u00020\u00012\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H 0\u0004H\u0007\u001a1\u0010\u0097\u0001\u001a\b\u0012\u0004\u0012\u00020\u001d0\n*\u00020\u00022\u0006\u0010\u001e\u001a\u00020\u001f2\t\b\u0002\u0010\u0095\u0001\u001a\u00020\u001f2\t\b\u0002\u0010\u0096\u0001\u001a\u00020\u0001H\u0007\u001aK\u0010\u0097\u0001\u001a\b\u0012\u0004\u0012\u0002H 0\n\"\u0004\b\u0000\u0010 *\u00020\u00022\u0006\u0010\u001e\u001a\u00020\u001f2\t\b\u0002\u0010\u0095\u0001\u001a\u00020\u001f2\t\b\u0002\u0010\u0096\u0001\u001a\u00020\u00012\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H 0\u0004H\u0007\u001a\u0018\u0010\u0098\u0001\u001a\u000f\u0012\u000b\u0012\t\u0012\u0004\u0012\u00020\u00050\u0099\u00010\b*\u00020\u0002\u001a)\u0010\u009a\u0001\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u00100\u001c*\u00020\u00022\u0007\u0010\u009b\u0001\u001a\u00020\u0002H\u0086\u0004\u001a]\u0010\u009a\u0001\u001a\b\u0012\u0004\u0012\u0002H\u000e0\u001c\"\u0004\b\u0000\u0010\u000e*\u00020\u00022\u0007\u0010\u009b\u0001\u001a\u00020\u000228\u0010\u000f\u001a4\u0012\u0014\u0012\u00120\u0005¢\u0006\r\b1\u0012\t\b2\u0012\u0005\b\b(\u009c\u0001\u0012\u0014\u0012\u00120\u0005¢\u0006\r\b1\u0012\t\b2\u0012\u0005\b\b(\u009d\u0001\u0012\u0004\u0012\u0002H\u000e00H\u0086\b\u001a\u001f\u0010\u009e\u0001\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u00100\u001c*\u00020\u0002H\u0007\u001aT\u0010\u009e\u0001\u001a\b\u0012\u0004\u0012\u0002H 0\u001c\"\u0004\b\u0000\u0010 *\u00020\u000228\u0010\u000f\u001a4\u0012\u0014\u0012\u00120\u0005¢\u0006\r\b1\u0012\t\b2\u0012\u0005\b\b(\u009c\u0001\u0012\u0014\u0012\u00120\u0005¢\u0006\r\b1\u0012\t\b2\u0012\u0005\b\b(\u009d\u0001\u0012\u0004\u0012\u0002H 00H\u0087\b¨\u0006\u009f\u0001" }, d2 = { "all", "", "", "predicate", "Lkotlin/Function1;", "", "any", "asIterable", "", "asSequence", "Lkotlin/sequences/Sequence;", "associate", "", "K", "V", "transform", "Lkotlin/Pair;", "associateBy", "keySelector", "valueTransform", "associateByTo", "M", "", "destination", "(Ljava/lang/CharSequence;Ljava/util/Map;Lkotlin/jvm/functions/Function1;)Ljava/util/Map;", "(Ljava/lang/CharSequence;Ljava/util/Map;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)Ljava/util/Map;", "associateTo", "chunked", "", "", "size", "", "R", "chunkedSequence", "count", "drop", "n", "dropLast", "dropLastWhile", "dropWhile", "elementAt", "index", "elementAtOrElse", "defaultValue", "elementAtOrNull", "(Ljava/lang/CharSequence;I)Ljava/lang/Character;", "filter", "filterIndexed", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "filterIndexedTo", "C", "Ljava/lang/Appendable;", "Lkotlin/text/Appendable;", "(Ljava/lang/CharSequence;Ljava/lang/Appendable;Lkotlin/jvm/functions/Function2;)Ljava/lang/Appendable;", "filterNot", "filterNotTo", "(Ljava/lang/CharSequence;Ljava/lang/Appendable;Lkotlin/jvm/functions/Function1;)Ljava/lang/Appendable;", "filterTo", "find", "(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1;)Ljava/lang/Character;", "findLast", "first", "firstOrNull", "(Ljava/lang/CharSequence;)Ljava/lang/Character;", "flatMap", "flatMapTo", "", "(Ljava/lang/CharSequence;Ljava/util/Collection;Lkotlin/jvm/functions/Function1;)Ljava/util/Collection;", "fold", "initial", "operation", "acc", "(Ljava/lang/CharSequence;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "foldIndexed", "Lkotlin/Function3;", "(Ljava/lang/CharSequence;Ljava/lang/Object;Lkotlin/jvm/functions/Function3;)Ljava/lang/Object;", "foldRight", "foldRightIndexed", "forEach", "", "action", "forEachIndexed", "getOrElse", "getOrNull", "groupBy", "groupByTo", "", "groupingBy", "Lkotlin/collections/Grouping;", "indexOfFirst", "indexOfLast", "last", "lastOrNull", "map", "mapIndexed", "mapIndexedNotNull", "", "mapIndexedNotNullTo", "(Ljava/lang/CharSequence;Ljava/util/Collection;Lkotlin/jvm/functions/Function2;)Ljava/util/Collection;", "mapIndexedTo", "mapNotNull", "mapNotNullTo", "mapTo", "max", "maxBy", "", "selector", "maxWith", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "(Ljava/lang/CharSequence;Ljava/util/Comparator;)Ljava/lang/Character;", "min", "minBy", "minWith", "none", "onEach", "S", "(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1;)Ljava/lang/CharSequence;", "partition", "reduce", "reduceIndexed", "reduceRight", "reduceRightIndexed", "reversed", "single", "singleOrNull", "slice", "indices", "Lkotlin/ranges/IntRange;", "sumBy", "sumByDouble", "", "take", "takeLast", "takeLastWhile", "takeWhile", "toCollection", "(Ljava/lang/CharSequence;Ljava/util/Collection;)Ljava/util/Collection;", "toHashSet", "Ljava/util/HashSet;", "Lkotlin/collections/HashSet;", "toList", "toMutableList", "toSet", "", "windowed", "step", "partialWindows", "windowedSequence", "withIndex", "Lkotlin/collections/IndexedValue;", "zip", "other", "a", "b", "zipWithNext", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/text/StringsKt")
class StringsKt___StringsKt extends StringsKt___StringsJvmKt
{
    public StringsKt___StringsKt() {
    }
    
    public static final boolean all(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        for (int i = 0; i < charSequence.length(); ++i) {
            if (!function1.invoke(charSequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static final boolean any(@NotNull final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        return charSequence.length() == 0 ^ true;
    }
    
    public static final boolean any(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        for (int i = 0; i < charSequence.length(); ++i) {
            if (function1.invoke(charSequence.charAt(i))) {
                return true;
            }
        }
        return false;
    }
    
    @NotNull
    public static final Iterable<Character> asIterable(@NotNull final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        if (charSequence instanceof String && charSequence.length() == 0) {
            return (Iterable)CollectionsKt__CollectionsKt.emptyList();
        }
        return (Iterable<Character>)new StringsKt___StringsKt$asIterable$$inlined$Iterable.StringsKt___StringsKt$asIterable$$inlined$Iterable$1(charSequence);
    }
    
    @NotNull
    public static final Sequence<Character> asSequence(@NotNull final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        if (charSequence instanceof String && charSequence.length() == 0) {
            return SequencesKt__SequencesKt.emptySequence();
        }
        return (Sequence<Character>)new StringsKt___StringsKt$asSequence$$inlined$Sequence.StringsKt___StringsKt$asSequence$$inlined$Sequence$1(charSequence);
    }
    
    @NotNull
    public static final <K, V> Map<K, V> associate(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, ? extends Pair<? extends K, ? extends V>> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        final Map map = new LinkedHashMap(RangesKt___RangesKt.coerceAtLeast(MapsKt__MapsKt.mapCapacity(charSequence.length()), 16));
        for (int i = 0; i < charSequence.length(); ++i) {
            final Pair pair = (Pair)function1.invoke(charSequence.charAt(i));
            map.put(pair.getFirst(), pair.getSecond());
        }
        return map;
    }
    
    @NotNull
    public static final <K> Map<K, Character> associateBy(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, ? extends K> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "keySelector");
        final Map map = new LinkedHashMap(RangesKt___RangesKt.coerceAtLeast(MapsKt__MapsKt.mapCapacity(charSequence.length()), 16));
        for (int i = 0; i < charSequence.length(); ++i) {
            final char char1 = charSequence.charAt(i);
            map.put(function1.invoke(char1), char1);
        }
        return map;
    }
    
    @NotNull
    public static final <K, V> Map<K, V> associateBy(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, ? extends K> function1, @NotNull final Function1<? super Character, ? extends V> function2) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "keySelector");
        Intrinsics.checkParameterIsNotNull(function2, "valueTransform");
        final Map map = new LinkedHashMap(RangesKt___RangesKt.coerceAtLeast(MapsKt__MapsKt.mapCapacity(charSequence.length()), 16));
        for (int i = 0; i < charSequence.length(); ++i) {
            final char char1 = charSequence.charAt(i);
            map.put(function1.invoke(char1), function2.invoke(char1));
        }
        return map;
    }
    
    @NotNull
    public static final <K, M extends Map<? super K, ? super Character>> M associateByTo(@NotNull final CharSequence charSequence, @NotNull final M m, @NotNull final Function1<? super Character, ? extends K> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(m, "destination");
        Intrinsics.checkParameterIsNotNull(function1, "keySelector");
        for (int i = 0; i < charSequence.length(); ++i) {
            final char char1 = charSequence.charAt(i);
            ((Map<? super K, Character>)m).put((Object)function1.invoke(char1), Character.valueOf(char1));
        }
        return m;
    }
    
    @NotNull
    public static final <K, V, M extends Map<? super K, ? super V>> M associateByTo(@NotNull final CharSequence charSequence, @NotNull final M m, @NotNull final Function1<? super Character, ? extends K> function1, @NotNull final Function1<? super Character, ? extends V> function2) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(m, "destination");
        Intrinsics.checkParameterIsNotNull(function1, "keySelector");
        Intrinsics.checkParameterIsNotNull(function2, "valueTransform");
        for (int i = 0; i < charSequence.length(); ++i) {
            final char char1 = charSequence.charAt(i);
            m.put((Object)function1.invoke(char1), (Object)function2.invoke(char1));
        }
        return m;
    }
    
    @NotNull
    public static final <K, V, M extends Map<? super K, ? super V>> M associateTo(@NotNull final CharSequence charSequence, @NotNull final M m, @NotNull final Function1<? super Character, ? extends Pair<? extends K, ? extends V>> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(m, "destination");
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        for (int i = 0; i < charSequence.length(); ++i) {
            final Pair pair = (Pair)function1.invoke(charSequence.charAt(i));
            m.put((Object)pair.getFirst(), (Object)pair.getSecond());
        }
        return m;
    }
    
    @SinceKotlin(version = "1.2")
    @NotNull
    public static final List<String> chunked(@NotNull final CharSequence charSequence, final int n) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        return windowed(charSequence, n, n, true);
    }
    
    @SinceKotlin(version = "1.2")
    @NotNull
    public static final <R> List<R> chunked(@NotNull final CharSequence charSequence, final int n, @NotNull final Function1<? super CharSequence, ? extends R> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        return windowed(charSequence, n, n, true, function1);
    }
    
    @SinceKotlin(version = "1.2")
    @NotNull
    public static final Sequence<String> chunkedSequence(@NotNull final CharSequence charSequence, final int n) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        return chunkedSequence(charSequence, n, (Function1<? super CharSequence, ? extends String>)StringsKt___StringsKt$chunkedSequence.StringsKt___StringsKt$chunkedSequence$1.INSTANCE);
    }
    
    @SinceKotlin(version = "1.2")
    @NotNull
    public static final <R> Sequence<R> chunkedSequence(@NotNull final CharSequence charSequence, final int n, @NotNull final Function1<? super CharSequence, ? extends R> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        return windowedSequence(charSequence, n, n, true, function1);
    }
    
    @InlineOnly
    private static final int count(@NotNull final CharSequence charSequence) {
        return charSequence.length();
    }
    
    public static final int count(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        int i = 0;
        int n = 0;
        while (i < charSequence.length()) {
            int n2 = n;
            if (function1.invoke(charSequence.charAt(i))) {
                n2 = n + 1;
            }
            ++i;
            n = n2;
        }
        return n;
    }
    
    @NotNull
    public static final CharSequence drop(@NotNull final CharSequence charSequence, final int i) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        if (i < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Requested character count ");
            sb.append(i);
            sb.append(" is less than zero.");
            throw new IllegalArgumentException(sb.toString().toString());
        }
        return charSequence.subSequence(RangesKt___RangesKt.coerceAtMost(i, charSequence.length()), charSequence.length());
    }
    
    @NotNull
    public static final String drop(@NotNull String substring, final int i) {
        Intrinsics.checkParameterIsNotNull(substring, "$receiver");
        if (i < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Requested character count ");
            sb.append(i);
            sb.append(" is less than zero.");
            throw new IllegalArgumentException(sb.toString().toString());
        }
        substring = substring.substring(RangesKt___RangesKt.coerceAtMost(i, substring.length()));
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.String).substring(startIndex)");
        return substring;
    }
    
    @NotNull
    public static final CharSequence dropLast(@NotNull final CharSequence charSequence, final int i) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        if (i < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Requested character count ");
            sb.append(i);
            sb.append(" is less than zero.");
            throw new IllegalArgumentException(sb.toString().toString());
        }
        return take(charSequence, RangesKt___RangesKt.coerceAtLeast(charSequence.length() - i, 0));
    }
    
    @NotNull
    public static final String dropLast(@NotNull final String s, final int i) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        if (i < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Requested character count ");
            sb.append(i);
            sb.append(" is less than zero.");
            throw new IllegalArgumentException(sb.toString().toString());
        }
        return take(s, RangesKt___RangesKt.coerceAtLeast(s.length() - i, 0));
    }
    
    @NotNull
    public static final CharSequence dropLastWhile(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        for (int i = StringsKt__StringsKt.getLastIndex(charSequence); i >= 0; --i) {
            if (!function1.invoke(charSequence.charAt(i))) {
                return charSequence.subSequence(0, i + 1);
            }
        }
        return "";
    }
    
    @NotNull
    public static final String dropLastWhile(@NotNull String substring, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(substring, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        for (int i = StringsKt__StringsKt.getLastIndex(substring); i >= 0; --i) {
            if (!function1.invoke(substring.charAt(i))) {
                substring = substring.substring(0, i + 1);
                Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                return substring;
            }
        }
        return "";
    }
    
    @NotNull
    public static final CharSequence dropWhile(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        for (int length = charSequence.length(), i = 0; i < length; ++i) {
            if (!function1.invoke(charSequence.charAt(i))) {
                return charSequence.subSequence(i, charSequence.length());
            }
        }
        return "";
    }
    
    @NotNull
    public static final String dropWhile(@NotNull String substring, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(substring, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        for (int length = substring.length(), i = 0; i < length; ++i) {
            if (!function1.invoke(substring.charAt(i))) {
                substring = substring.substring(i);
                Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.String).substring(startIndex)");
                return substring;
            }
        }
        return "";
    }
    
    @InlineOnly
    private static final char elementAt(@NotNull final CharSequence charSequence, final int n) {
        return charSequence.charAt(n);
    }
    
    @InlineOnly
    private static final char elementAtOrElse(@NotNull final CharSequence charSequence, final int i, final Function1<? super Integer, Character> function1) {
        char c;
        if (i >= 0 && i <= StringsKt__StringsKt.getLastIndex(charSequence)) {
            c = charSequence.charAt(i);
        }
        else {
            c = function1.invoke(i);
        }
        return c;
    }
    
    @InlineOnly
    private static final Character elementAtOrNull(@NotNull final CharSequence charSequence, final int n) {
        return getOrNull(charSequence, n);
    }
    
    @NotNull
    public static final CharSequence filter(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        final Appendable appendable = new StringBuilder();
        for (int length = charSequence.length(), i = 0; i < length; ++i) {
            final char char1 = charSequence.charAt(i);
            if (function1.invoke(char1)) {
                appendable.append(char1);
            }
        }
        return (CharSequence)appendable;
    }
    
    @NotNull
    public static final String filter(@NotNull String string, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(string, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        final CharSequence charSequence = string;
        final Appendable appendable = new StringBuilder();
        for (int length = charSequence.length(), i = 0; i < length; ++i) {
            final char char1 = charSequence.charAt(i);
            if (function1.invoke(char1)) {
                appendable.append(char1);
            }
        }
        string = ((StringBuilder)appendable).toString();
        Intrinsics.checkExpressionValueIsNotNull(string, "filterTo(StringBuilder(), predicate).toString()");
        return string;
    }
    
    @NotNull
    public static final CharSequence filterIndexed(@NotNull final CharSequence charSequence, @NotNull final Function2<? super Integer, ? super Character, Boolean> function2) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function2, "predicate");
        final Appendable appendable = new StringBuilder();
        for (int i = 0, j = 0; i < charSequence.length(); ++i, ++j) {
            final char char1 = charSequence.charAt(i);
            if (function2.invoke(j, char1)) {
                appendable.append(char1);
            }
        }
        return (CharSequence)appendable;
    }
    
    @NotNull
    public static final String filterIndexed(@NotNull String string, @NotNull final Function2<? super Integer, ? super Character, Boolean> function2) {
        Intrinsics.checkParameterIsNotNull(string, "$receiver");
        Intrinsics.checkParameterIsNotNull(function2, "predicate");
        final CharSequence charSequence = string;
        final Appendable appendable = new StringBuilder();
        for (int i = 0, j = 0; i < charSequence.length(); ++i, ++j) {
            final char char1 = charSequence.charAt(i);
            if (function2.invoke(j, char1)) {
                appendable.append(char1);
            }
        }
        string = ((StringBuilder)appendable).toString();
        Intrinsics.checkExpressionValueIsNotNull(string, "filterIndexedTo(StringBu\u2026(), predicate).toString()");
        return string;
    }
    
    @NotNull
    public static final <C extends Appendable> C filterIndexedTo(@NotNull final CharSequence charSequence, @NotNull final C c, @NotNull final Function2<? super Integer, ? super Character, Boolean> function2) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(c, "destination");
        Intrinsics.checkParameterIsNotNull(function2, "predicate");
        for (int i = 0, j = 0; i < charSequence.length(); ++i, ++j) {
            final char char1 = charSequence.charAt(i);
            if (function2.invoke(j, char1)) {
                c.append(char1);
            }
        }
        return c;
    }
    
    @NotNull
    public static final CharSequence filterNot(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        final Appendable appendable = new StringBuilder();
        for (int i = 0; i < charSequence.length(); ++i) {
            final char char1 = charSequence.charAt(i);
            if (!function1.invoke(char1)) {
                appendable.append(char1);
            }
        }
        return (CharSequence)appendable;
    }
    
    @NotNull
    public static final String filterNot(@NotNull String string, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(string, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        final CharSequence charSequence = string;
        final Appendable appendable = new StringBuilder();
        for (int i = 0; i < charSequence.length(); ++i) {
            final char char1 = charSequence.charAt(i);
            if (!function1.invoke(char1)) {
                appendable.append(char1);
            }
        }
        string = ((StringBuilder)appendable).toString();
        Intrinsics.checkExpressionValueIsNotNull(string, "filterNotTo(StringBuilder(), predicate).toString()");
        return string;
    }
    
    @NotNull
    public static final <C extends Appendable> C filterNotTo(@NotNull final CharSequence charSequence, @NotNull final C c, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(c, "destination");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        for (int i = 0; i < charSequence.length(); ++i) {
            final char char1 = charSequence.charAt(i);
            if (!function1.invoke(char1)) {
                c.append(char1);
            }
        }
        return c;
    }
    
    @NotNull
    public static final <C extends Appendable> C filterTo(@NotNull final CharSequence charSequence, @NotNull final C c, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(c, "destination");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        for (int length = charSequence.length(), i = 0; i < length; ++i) {
            final char char1 = charSequence.charAt(i);
            if (function1.invoke(char1)) {
                c.append(char1);
            }
        }
        return c;
    }
    
    @InlineOnly
    private static final Character find(@NotNull final CharSequence charSequence, final Function1<? super Character, Boolean> function1) {
        for (int i = 0; i < charSequence.length(); ++i) {
            final char char1 = charSequence.charAt(i);
            if (function1.invoke(char1)) {
                return char1;
            }
        }
        return null;
    }
    
    @InlineOnly
    private static final Character findLast(@NotNull final CharSequence charSequence, final Function1<? super Character, Boolean> function1) {
        int length = charSequence.length();
        while (--length >= 0) {
            final char char1 = charSequence.charAt(length);
            if (function1.invoke(char1)) {
                return char1;
            }
        }
        return null;
    }
    
    public static final char first(@NotNull final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        if (charSequence.length() == 0) {
            throw new NoSuchElementException("Char sequence is empty.");
        }
        return charSequence.charAt(0);
    }
    
    public static final char first(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        for (int i = 0; i < charSequence.length(); ++i) {
            final char char1 = charSequence.charAt(i);
            if (function1.invoke(char1)) {
                return char1;
            }
        }
        throw new NoSuchElementException("Char sequence contains no character matching the predicate.");
    }
    
    @Nullable
    public static final Character firstOrNull(@NotNull final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Character value;
        if (charSequence.length() == 0) {
            value = null;
        }
        else {
            value = charSequence.charAt(0);
        }
        return value;
    }
    
    @Nullable
    public static final Character firstOrNull(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        for (int i = 0; i < charSequence.length(); ++i) {
            final char char1 = charSequence.charAt(i);
            if (function1.invoke(char1)) {
                return char1;
            }
        }
        return null;
    }
    
    @NotNull
    public static final <R> List<R> flatMap(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, ? extends Iterable<? extends R>> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        final Collection collection = new ArrayList();
        for (int i = 0; i < charSequence.length(); ++i) {
            CollectionsKt__MutableCollectionsKt.addAll((Collection<? super Object>)collection, (Iterable<?>)function1.invoke(charSequence.charAt(i)));
        }
        return (List<R>)collection;
    }
    
    @NotNull
    public static final <R, C extends Collection<? super R>> C flatMapTo(@NotNull final CharSequence charSequence, @NotNull final C c, @NotNull final Function1<? super Character, ? extends Iterable<? extends R>> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(c, "destination");
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        for (int i = 0; i < charSequence.length(); ++i) {
            CollectionsKt__MutableCollectionsKt.addAll((Collection<? super Object>)c, (Iterable<?>)function1.invoke(charSequence.charAt(i)));
        }
        return c;
    }
    
    public static final <R> R fold(@NotNull final CharSequence charSequence, R invoke, @NotNull final Function2<? super R, ? super Character, ? extends R> function2) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function2, "operation");
        for (int i = 0; i < charSequence.length(); ++i) {
            invoke = (R)function2.invoke(invoke, charSequence.charAt(i));
        }
        return invoke;
    }
    
    public static final <R> R foldIndexed(@NotNull final CharSequence charSequence, R invoke, @NotNull final Function3<? super Integer, ? super R, ? super Character, ? extends R> function3) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function3, "operation");
        int n = 0;
        int n2 = 0;
        while (true) {
            final int i = n2;
            if (n >= charSequence.length()) {
                break;
            }
            final char char1 = charSequence.charAt(n);
            n2 = i + 1;
            invoke = (R)function3.invoke(i, invoke, char1);
            ++n;
        }
        return invoke;
    }
    
    public static final <R> R foldRight(@NotNull final CharSequence charSequence, R invoke, @NotNull final Function2<? super Character, ? super R, ? extends R> function2) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function2, "operation");
        for (int i = StringsKt__StringsKt.getLastIndex(charSequence); i >= 0; --i) {
            invoke = (R)function2.invoke(charSequence.charAt(i), invoke);
        }
        return invoke;
    }
    
    public static final <R> R foldRightIndexed(@NotNull final CharSequence charSequence, R invoke, @NotNull final Function3<? super Integer, ? super Character, ? super R, ? extends R> function3) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function3, "operation");
        for (int i = StringsKt__StringsKt.getLastIndex(charSequence); i >= 0; --i) {
            invoke = (R)function3.invoke(i, charSequence.charAt(i), invoke);
        }
        return invoke;
    }
    
    public static final void forEach(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "action");
        for (int i = 0; i < charSequence.length(); ++i) {
            function1.invoke(charSequence.charAt(i));
        }
    }
    
    public static final void forEachIndexed(@NotNull final CharSequence charSequence, @NotNull final Function2<? super Integer, ? super Character, Unit> function2) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function2, "action");
        int n = 0;
        int n2 = 0;
        while (true) {
            final int i = n2;
            if (n >= charSequence.length()) {
                break;
            }
            final char char1 = charSequence.charAt(n);
            n2 = i + 1;
            function2.invoke(i, char1);
            ++n;
        }
    }
    
    @InlineOnly
    private static final char getOrElse(@NotNull final CharSequence charSequence, final int i, final Function1<? super Integer, Character> function1) {
        char c;
        if (i >= 0 && i <= StringsKt__StringsKt.getLastIndex(charSequence)) {
            c = charSequence.charAt(i);
        }
        else {
            c = function1.invoke(i);
        }
        return c;
    }
    
    @Nullable
    public static final Character getOrNull(@NotNull final CharSequence charSequence, final int n) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Character value;
        if (n >= 0 && n <= StringsKt__StringsKt.getLastIndex(charSequence)) {
            value = charSequence.charAt(n);
        }
        else {
            value = null;
        }
        return value;
    }
    
    @NotNull
    public static final <K> Map<K, List<Character>> groupBy(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, ? extends K> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "keySelector");
        final Map map = new LinkedHashMap();
        for (int i = 0; i < charSequence.length(); ++i) {
            final char char1 = charSequence.charAt(i);
            final K invoke = (K)function1.invoke(char1);
            Object value;
            if ((value = map.get(invoke)) == null) {
                value = new ArrayList();
                map.put(invoke, value);
            }
            ((List)value).add(char1);
        }
        return map;
    }
    
    @NotNull
    public static final <K, V> Map<K, List<V>> groupBy(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, ? extends K> function1, @NotNull final Function1<? super Character, ? extends V> function2) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "keySelector");
        Intrinsics.checkParameterIsNotNull(function2, "valueTransform");
        final Map map = new LinkedHashMap();
        for (int i = 0; i < charSequence.length(); ++i) {
            final char char1 = charSequence.charAt(i);
            final K invoke = (K)function1.invoke(char1);
            Object value;
            if ((value = map.get(invoke)) == null) {
                value = new ArrayList();
                map.put(invoke, value);
            }
            ((List)value).add(function2.invoke(char1));
        }
        return map;
    }
    
    @NotNull
    public static final <K, M extends Map<? super K, List<Character>>> M groupByTo(@NotNull final CharSequence charSequence, @NotNull final M m, @NotNull final Function1<? super Character, ? extends K> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(m, "destination");
        Intrinsics.checkParameterIsNotNull(function1, "keySelector");
        for (int i = 0; i < charSequence.length(); ++i) {
            final char char1 = charSequence.charAt(i);
            final K invoke = (K)function1.invoke(char1);
            List<Character> value;
            if ((value = ((Map<K, List<Character>>)m).get(invoke)) == null) {
                value = new ArrayList<Character>();
                m.put(invoke, value);
            }
            value.add(char1);
        }
        return m;
    }
    
    @NotNull
    public static final <K, V, M extends Map<? super K, List<V>>> M groupByTo(@NotNull final CharSequence charSequence, @NotNull final M m, @NotNull final Function1<? super Character, ? extends K> function1, @NotNull final Function1<? super Character, ? extends V> function2) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(m, "destination");
        Intrinsics.checkParameterIsNotNull(function1, "keySelector");
        Intrinsics.checkParameterIsNotNull(function2, "valueTransform");
        for (int i = 0; i < charSequence.length(); ++i) {
            final char char1 = charSequence.charAt(i);
            final K invoke = (K)function1.invoke(char1);
            List<V> value;
            if ((value = ((Map<K, List<V>>)m).get(invoke)) == null) {
                value = new ArrayList<V>();
                m.put(invoke, value);
            }
            ((List<Object>)value).add(function2.invoke(char1));
        }
        return m;
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <K> Grouping<Character, K> groupingBy(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, ? extends K> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "keySelector");
        return (Grouping<Character, K>)new StringsKt___StringsKt$groupingBy.StringsKt___StringsKt$groupingBy$1(charSequence, (Function1)function1);
    }
    
    public static final int indexOfFirst(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        for (int length = charSequence.length(), i = 0; i < length; ++i) {
            if (function1.invoke(charSequence.charAt(i))) {
                return i;
            }
        }
        return -1;
    }
    
    public static final int indexOfLast(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        for (int i = charSequence.length() - 1; i >= 0; --i) {
            if (function1.invoke(charSequence.charAt(i))) {
                return i;
            }
        }
        return -1;
    }
    
    public static final char last(@NotNull final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        if (charSequence.length() == 0) {
            throw new NoSuchElementException("Char sequence is empty.");
        }
        return charSequence.charAt(StringsKt__StringsKt.getLastIndex(charSequence));
    }
    
    public static final char last(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        int length = charSequence.length();
        while (--length >= 0) {
            final char char1 = charSequence.charAt(length);
            if (function1.invoke(char1)) {
                return char1;
            }
        }
        throw new NoSuchElementException("Char sequence contains no character matching the predicate.");
    }
    
    @Nullable
    public static final Character lastOrNull(@NotNull final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Character value;
        if (charSequence.length() == 0) {
            value = null;
        }
        else {
            value = charSequence.charAt(charSequence.length() - 1);
        }
        return value;
    }
    
    @Nullable
    public static final Character lastOrNull(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        int length = charSequence.length();
        while (--length >= 0) {
            final char char1 = charSequence.charAt(length);
            if (function1.invoke(char1)) {
                return char1;
            }
        }
        return null;
    }
    
    @NotNull
    public static final <R> List<R> map(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, ? extends R> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        final Collection collection = new ArrayList(charSequence.length());
        for (int i = 0; i < charSequence.length(); ++i) {
            collection.add(function1.invoke(charSequence.charAt(i)));
        }
        return (List<R>)collection;
    }
    
    @NotNull
    public static final <R> List<R> mapIndexed(@NotNull final CharSequence charSequence, @NotNull final Function2<? super Integer, ? super Character, ? extends R> function2) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function2, "transform");
        final Collection collection = new ArrayList(charSequence.length());
        int n = 0;
        int n2 = 0;
        while (true) {
            final int i = n2;
            if (n >= charSequence.length()) {
                break;
            }
            final char char1 = charSequence.charAt(n);
            n2 = i + 1;
            collection.add(function2.invoke(i, char1));
            ++n;
        }
        return (List<R>)collection;
    }
    
    @NotNull
    public static final <R> List<R> mapIndexedNotNull(@NotNull final CharSequence charSequence, @NotNull final Function2<? super Integer, ? super Character, ? extends R> function2) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function2, "transform");
        final Collection collection = new ArrayList();
        for (int i = 0, j = 0; i < charSequence.length(); ++i, ++j) {
            final R invoke = (R)function2.invoke(j, charSequence.charAt(i));
            if (invoke != null) {
                collection.add(invoke);
            }
        }
        return (List<R>)collection;
    }
    
    @NotNull
    public static final <R, C extends Collection<? super R>> C mapIndexedNotNullTo(@NotNull final CharSequence charSequence, @NotNull final C c, @NotNull final Function2<? super Integer, ? super Character, ? extends R> function2) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(c, "destination");
        Intrinsics.checkParameterIsNotNull(function2, "transform");
        for (int i = 0, j = 0; i < charSequence.length(); ++i, ++j) {
            final R invoke = (R)function2.invoke(j, charSequence.charAt(i));
            if (invoke != null) {
                c.add(invoke);
            }
        }
        return c;
    }
    
    @NotNull
    public static final <R, C extends Collection<? super R>> C mapIndexedTo(@NotNull final CharSequence charSequence, @NotNull final C c, @NotNull final Function2<? super Integer, ? super Character, ? extends R> function2) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(c, "destination");
        Intrinsics.checkParameterIsNotNull(function2, "transform");
        int n = 0;
        int n2 = 0;
        while (true) {
            final int i = n2;
            if (n >= charSequence.length()) {
                break;
            }
            final char char1 = charSequence.charAt(n);
            n2 = i + 1;
            c.add((Object)function2.invoke(i, char1));
            ++n;
        }
        return c;
    }
    
    @NotNull
    public static final <R> List<R> mapNotNull(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, ? extends R> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        final Collection collection = new ArrayList();
        for (int i = 0; i < charSequence.length(); ++i) {
            final R invoke = (R)function1.invoke(charSequence.charAt(i));
            if (invoke != null) {
                collection.add(invoke);
            }
        }
        return (List<R>)collection;
    }
    
    @NotNull
    public static final <R, C extends Collection<? super R>> C mapNotNullTo(@NotNull final CharSequence charSequence, @NotNull final C c, @NotNull final Function1<? super Character, ? extends R> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(c, "destination");
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        for (int i = 0; i < charSequence.length(); ++i) {
            final R invoke = (R)function1.invoke(charSequence.charAt(i));
            if (invoke != null) {
                c.add(invoke);
            }
        }
        return c;
    }
    
    @NotNull
    public static final <R, C extends Collection<? super R>> C mapTo(@NotNull final CharSequence charSequence, @NotNull final C c, @NotNull final Function1<? super Character, ? extends R> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(c, "destination");
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        for (int i = 0; i < charSequence.length(); ++i) {
            c.add((Object)function1.invoke(charSequence.charAt(i)));
        }
        return c;
    }
    
    @Nullable
    public static final Character max(@NotNull final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        final int length = charSequence.length();
        final int n = 1;
        if (length == 0) {
            return null;
        }
        final char char1 = charSequence.charAt(0);
        final int lastIndex = StringsKt__StringsKt.getLastIndex(charSequence);
        char c = char1;
        if (1 <= lastIndex) {
            int n2 = n;
            char c2 = char1;
            while (true) {
                final char char2 = charSequence.charAt(n2);
                char c3 = c2;
                if (c2 < char2) {
                    c3 = char2;
                }
                c = c3;
                if (n2 == lastIndex) {
                    break;
                }
                ++n2;
                c2 = c3;
            }
        }
        return c;
    }
    
    @Nullable
    public static final <R extends Comparable<? super R>> Character maxBy(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, ? extends R> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "selector");
        final int length = charSequence.length();
        final int n = 1;
        if (length == 0) {
            return null;
        }
        char char1 = charSequence.charAt(0);
        Comparable comparable = (Comparable)function1.invoke(char1);
        final int lastIndex = StringsKt__StringsKt.getLastIndex(charSequence);
        char c = char1;
        if (1 <= lastIndex) {
            int n2 = n;
            while (true) {
                final char char2 = charSequence.charAt(n2);
                final Comparable comparable2 = (Comparable)function1.invoke(char2);
                Comparable comparable3 = comparable;
                if (comparable.compareTo(comparable2) < 0) {
                    char1 = char2;
                    comparable3 = comparable2;
                }
                c = char1;
                if (n2 == lastIndex) {
                    break;
                }
                ++n2;
                comparable = comparable3;
            }
        }
        return c;
    }
    
    @Nullable
    public static final Character maxWith(@NotNull final CharSequence charSequence, @NotNull final Comparator<? super Character> comparator) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        final int length = charSequence.length();
        final int n = 1;
        if (length == 0) {
            return null;
        }
        final char char1 = charSequence.charAt(0);
        final int lastIndex = StringsKt__StringsKt.getLastIndex(charSequence);
        char c = char1;
        if (1 <= lastIndex) {
            int n2 = n;
            char c2 = char1;
            while (true) {
                final char char2 = charSequence.charAt(n2);
                char c3 = c2;
                if (comparator.compare(c2, char2) < 0) {
                    c3 = char2;
                }
                c = c3;
                if (n2 == lastIndex) {
                    break;
                }
                ++n2;
                c2 = c3;
            }
        }
        return c;
    }
    
    @Nullable
    public static final Character min(@NotNull final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        final int length = charSequence.length();
        final int n = 1;
        if (length == 0) {
            return null;
        }
        final char char1 = charSequence.charAt(0);
        final int lastIndex = StringsKt__StringsKt.getLastIndex(charSequence);
        char c = char1;
        if (1 <= lastIndex) {
            int n2 = n;
            char c2 = char1;
            while (true) {
                final char char2 = charSequence.charAt(n2);
                char c3 = c2;
                if (c2 > char2) {
                    c3 = char2;
                }
                c = c3;
                if (n2 == lastIndex) {
                    break;
                }
                ++n2;
                c2 = c3;
            }
        }
        return c;
    }
    
    @Nullable
    public static final <R extends Comparable<? super R>> Character minBy(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, ? extends R> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "selector");
        final int length = charSequence.length();
        final int n = 1;
        if (length == 0) {
            return null;
        }
        char char1 = charSequence.charAt(0);
        Comparable comparable = (Comparable)function1.invoke(char1);
        final int lastIndex = StringsKt__StringsKt.getLastIndex(charSequence);
        char c = char1;
        if (1 <= lastIndex) {
            int n2 = n;
            while (true) {
                final char char2 = charSequence.charAt(n2);
                final Comparable comparable2 = (Comparable)function1.invoke(char2);
                Comparable comparable3 = comparable;
                if (comparable.compareTo(comparable2) > 0) {
                    char1 = char2;
                    comparable3 = comparable2;
                }
                c = char1;
                if (n2 == lastIndex) {
                    break;
                }
                ++n2;
                comparable = comparable3;
            }
        }
        return c;
    }
    
    @Nullable
    public static final Character minWith(@NotNull final CharSequence charSequence, @NotNull final Comparator<? super Character> comparator) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        final int length = charSequence.length();
        final int n = 1;
        if (length == 0) {
            return null;
        }
        final char char1 = charSequence.charAt(0);
        final int lastIndex = StringsKt__StringsKt.getLastIndex(charSequence);
        char c = char1;
        if (1 <= lastIndex) {
            int n2 = n;
            char c2 = char1;
            while (true) {
                final char char2 = charSequence.charAt(n2);
                char c3 = c2;
                if (comparator.compare(c2, char2) > 0) {
                    c3 = char2;
                }
                c = c3;
                if (n2 == lastIndex) {
                    break;
                }
                ++n2;
                c2 = c3;
            }
        }
        return c;
    }
    
    public static final boolean none(@NotNull final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        return charSequence.length() == 0;
    }
    
    public static final boolean none(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        for (int i = 0; i < charSequence.length(); ++i) {
            if (function1.invoke(charSequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <S extends CharSequence> S onEach(@NotNull final S n, @NotNull final Function1<? super Character, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(n, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "action");
        for (int i = 0; i < n.length(); ++i) {
            function1.invoke(n.charAt(i));
        }
        return n;
    }
    
    @NotNull
    public static final Pair<CharSequence, CharSequence> partition(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        final StringBuilder sb = new StringBuilder();
        final StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < charSequence.length(); ++i) {
            final char char1 = charSequence.charAt(i);
            if (function1.invoke(char1)) {
                sb.append(char1);
            }
            else {
                sb2.append(char1);
            }
        }
        return new Pair<CharSequence, CharSequence>(sb, sb2);
    }
    
    @NotNull
    public static final Pair<String, String> partition(@NotNull final String s, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        final StringBuilder sb = new StringBuilder();
        final StringBuilder sb2 = new StringBuilder();
        for (int length = s.length(), i = 0; i < length; ++i) {
            final char char1 = s.charAt(i);
            if (function1.invoke(char1)) {
                sb.append(char1);
            }
            else {
                sb2.append(char1);
            }
        }
        return new Pair<String, String>(sb.toString(), sb2.toString());
    }
    
    public static final char reduce(@NotNull final CharSequence charSequence, @NotNull final Function2<? super Character, ? super Character, Character> function2) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function2, "operation");
        final int length = charSequence.length();
        final int n = 1;
        if (length == 0) {
            throw new UnsupportedOperationException("Empty char sequence can't be reduced.");
        }
        char char1 = charSequence.charAt(0);
        final int lastIndex = StringsKt__StringsKt.getLastIndex(charSequence);
        char charValue = char1;
        if (1 <= lastIndex) {
            int n2 = n;
            while (true) {
                char1 = (charValue = function2.invoke(char1, charSequence.charAt(n2)));
                if (n2 == lastIndex) {
                    break;
                }
                ++n2;
            }
        }
        return charValue;
    }
    
    public static final char reduceIndexed(@NotNull final CharSequence charSequence, @NotNull final Function3<? super Integer, ? super Character, ? super Character, Character> function3) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function3, "operation");
        final int length = charSequence.length();
        final int n = 1;
        if (length == 0) {
            throw new UnsupportedOperationException("Empty char sequence can't be reduced.");
        }
        char char1 = charSequence.charAt(0);
        final int lastIndex = StringsKt__StringsKt.getLastIndex(charSequence);
        char charValue = char1;
        if (1 <= lastIndex) {
            int i = n;
            while (true) {
                char1 = (charValue = function3.invoke(i, char1, charSequence.charAt(i)));
                if (i == lastIndex) {
                    break;
                }
                ++i;
            }
        }
        return charValue;
    }
    
    public static final char reduceRight(@NotNull final CharSequence charSequence, @NotNull final Function2<? super Character, ? super Character, Character> function2) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function2, "operation");
        final int lastIndex = StringsKt__StringsKt.getLastIndex(charSequence);
        if (lastIndex < 0) {
            throw new UnsupportedOperationException("Empty char sequence can't be reduced.");
        }
        int i = lastIndex - 1;
        char c = charSequence.charAt(lastIndex);
        while (i >= 0) {
            c = function2.invoke(charSequence.charAt(i), c);
            --i;
        }
        return c;
    }
    
    public static final char reduceRightIndexed(@NotNull final CharSequence charSequence, @NotNull final Function3<? super Integer, ? super Character, ? super Character, Character> function3) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function3, "operation");
        final int lastIndex = StringsKt__StringsKt.getLastIndex(charSequence);
        if (lastIndex < 0) {
            throw new UnsupportedOperationException("Empty char sequence can't be reduced.");
        }
        int i = lastIndex - 1;
        char c = charSequence.charAt(lastIndex);
        while (i >= 0) {
            c = function3.invoke(i, charSequence.charAt(i), c);
            --i;
        }
        return c;
    }
    
    @NotNull
    public static final CharSequence reversed(@NotNull final CharSequence seq) {
        Intrinsics.checkParameterIsNotNull(seq, "$receiver");
        final StringBuilder reverse = new StringBuilder(seq).reverse();
        Intrinsics.checkExpressionValueIsNotNull(reverse, "StringBuilder(this).reverse()");
        return reverse;
    }
    
    @InlineOnly
    private static final String reversed(@NotNull final String s) {
        if (s == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
        }
        return reversed((CharSequence)s).toString();
    }
    
    public static final char single(@NotNull final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        switch (charSequence.length()) {
            default: {
                throw new IllegalArgumentException("Char sequence has more than one element.");
            }
            case 1: {
                return charSequence.charAt(0);
            }
            case 0: {
                throw new NoSuchElementException("Char sequence is empty.");
            }
        }
    }
    
    public static final char single(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        Character value = null;
        int i = 0;
        int n = 0;
        while (i < charSequence.length()) {
            final char char1 = charSequence.charAt(i);
            int n2 = n;
            if (function1.invoke(char1)) {
                if (n != 0) {
                    throw new IllegalArgumentException("Char sequence contains more than one matching element.");
                }
                value = char1;
                n2 = 1;
            }
            ++i;
            n = n2;
        }
        if (n == 0) {
            throw new NoSuchElementException("Char sequence contains no character matching the predicate.");
        }
        if (value == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Char");
        }
        return value;
    }
    
    @Nullable
    public static final Character singleOrNull(@NotNull final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Character value;
        if (charSequence.length() == 1) {
            value = charSequence.charAt(0);
        }
        else {
            value = null;
        }
        return value;
    }
    
    @Nullable
    public static final Character singleOrNull(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        Character value = null;
        int i = 0;
        int n = 0;
        while (i < charSequence.length()) {
            final char char1 = charSequence.charAt(i);
            int n2 = n;
            if (function1.invoke(char1)) {
                if (n != 0) {
                    return null;
                }
                value = char1;
                n2 = 1;
            }
            ++i;
            n = n2;
        }
        if (n == 0) {
            return null;
        }
        return value;
    }
    
    @NotNull
    public static final CharSequence slice(@NotNull final CharSequence charSequence, @NotNull final Iterable<Integer> iterable) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(iterable, "indices");
        final int collectionSizeOrDefault = CollectionsKt__IterablesKt.collectionSizeOrDefault((Iterable<?>)iterable, 10);
        if (collectionSizeOrDefault == 0) {
            return "";
        }
        final StringBuilder sb = new StringBuilder(collectionSizeOrDefault);
        final Iterator<? extends T> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            sb.append(charSequence.charAt(((Number)iterator.next()).intValue()));
        }
        return sb;
    }
    
    @NotNull
    public static final CharSequence slice(@NotNull final CharSequence charSequence, @NotNull final IntRange intRange) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(intRange, "indices");
        if (intRange.isEmpty()) {
            return "";
        }
        return StringsKt__StringsKt.subSequence(charSequence, intRange);
    }
    
    @InlineOnly
    private static final String slice(@NotNull final String s, final Iterable<Integer> iterable) {
        if (s == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
        }
        return slice((CharSequence)s, iterable).toString();
    }
    
    @NotNull
    public static final String slice(@NotNull final String s, @NotNull final IntRange intRange) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(intRange, "indices");
        if (intRange.isEmpty()) {
            return "";
        }
        return StringsKt__StringsKt.substring(s, intRange);
    }
    
    public static final int sumBy(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, Integer> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "selector");
        int i = 0;
        int n = 0;
        while (i < charSequence.length()) {
            n += function1.invoke(charSequence.charAt(i)).intValue();
            ++i;
        }
        return n;
    }
    
    public static final double sumByDouble(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, Double> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "selector");
        double n = 0.0;
        for (int i = 0; i < charSequence.length(); ++i) {
            n += function1.invoke(charSequence.charAt(i)).doubleValue();
        }
        return n;
    }
    
    @NotNull
    public static final CharSequence take(@NotNull final CharSequence charSequence, final int i) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        if (i < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Requested character count ");
            sb.append(i);
            sb.append(" is less than zero.");
            throw new IllegalArgumentException(sb.toString().toString());
        }
        return charSequence.subSequence(0, RangesKt___RangesKt.coerceAtMost(i, charSequence.length()));
    }
    
    @NotNull
    public static final String take(@NotNull String substring, final int i) {
        Intrinsics.checkParameterIsNotNull(substring, "$receiver");
        if (i < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Requested character count ");
            sb.append(i);
            sb.append(" is less than zero.");
            throw new IllegalArgumentException(sb.toString().toString());
        }
        substring = substring.substring(0, RangesKt___RangesKt.coerceAtMost(i, substring.length()));
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        return substring;
    }
    
    @NotNull
    public static final CharSequence takeLast(@NotNull final CharSequence charSequence, final int i) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        if (i < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Requested character count ");
            sb.append(i);
            sb.append(" is less than zero.");
            throw new IllegalArgumentException(sb.toString().toString());
        }
        final int length = charSequence.length();
        return charSequence.subSequence(length - RangesKt___RangesKt.coerceAtMost(i, length), length);
    }
    
    @NotNull
    public static final String takeLast(@NotNull String substring, final int i) {
        Intrinsics.checkParameterIsNotNull(substring, "$receiver");
        if (i < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Requested character count ");
            sb.append(i);
            sb.append(" is less than zero.");
            throw new IllegalArgumentException(sb.toString().toString());
        }
        final int length = substring.length();
        substring = substring.substring(length - RangesKt___RangesKt.coerceAtMost(i, length));
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.String).substring(startIndex)");
        return substring;
    }
    
    @NotNull
    public static final CharSequence takeLastWhile(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        for (int i = StringsKt__StringsKt.getLastIndex(charSequence); i >= 0; --i) {
            if (!function1.invoke(charSequence.charAt(i))) {
                return charSequence.subSequence(i + 1, charSequence.length());
            }
        }
        return charSequence.subSequence(0, charSequence.length());
    }
    
    @NotNull
    public static final String takeLastWhile(@NotNull String substring, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(substring, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        for (int i = StringsKt__StringsKt.getLastIndex(substring); i >= 0; --i) {
            if (!function1.invoke(substring.charAt(i))) {
                substring = substring.substring(i + 1);
                Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.String).substring(startIndex)");
                return substring;
            }
        }
        return substring;
    }
    
    @NotNull
    public static final CharSequence takeWhile(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        for (int length = charSequence.length(), i = 0; i < length; ++i) {
            if (!function1.invoke(charSequence.charAt(i))) {
                return charSequence.subSequence(0, i);
            }
        }
        return charSequence.subSequence(0, charSequence.length());
    }
    
    @NotNull
    public static final String takeWhile(@NotNull String substring, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(substring, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        for (int length = substring.length(), i = 0; i < length; ++i) {
            if (!function1.invoke(substring.charAt(i))) {
                substring = substring.substring(0, i);
                Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                return substring;
            }
        }
        return substring;
    }
    
    @NotNull
    public static final <C extends Collection<? super Character>> C toCollection(@NotNull final CharSequence charSequence, @NotNull final C c) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(c, "destination");
        for (int i = 0; i < charSequence.length(); ++i) {
            ((Collection<Character>)c).add(charSequence.charAt(i));
        }
        return c;
    }
    
    @NotNull
    public static final HashSet<Character> toHashSet(@NotNull final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        return toCollection(charSequence, new HashSet(MapsKt__MapsKt.mapCapacity(charSequence.length())));
    }
    
    @NotNull
    public static final List<Character> toList(@NotNull final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Object o = null;
        switch (charSequence.length()) {
            default: {
                o = toMutableList(charSequence);
                break;
            }
            case 1: {
                o = CollectionsKt__CollectionsJVMKt.listOf(charSequence.charAt(0));
                break;
            }
            case 0: {
                o = CollectionsKt__CollectionsKt.emptyList();
                break;
            }
        }
        return (List<Character>)o;
    }
    
    @NotNull
    public static final List<Character> toMutableList(@NotNull final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        return toCollection(charSequence, (List<Character>)new ArrayList(charSequence.length()));
    }
    
    @NotNull
    public static final Set<Character> toSet(@NotNull final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Set<?> set = null;
        switch (charSequence.length()) {
            default: {
                set = toCollection(charSequence, (Set<?>)new LinkedHashSet(MapsKt__MapsKt.mapCapacity(charSequence.length())));
                break;
            }
            case 1: {
                set = SetsKt__SetsJVMKt.setOf(charSequence.charAt(0));
                break;
            }
            case 0: {
                set = SetsKt__SetsKt.emptySet();
                break;
            }
        }
        return (Set<Character>)set;
    }
    
    @SinceKotlin(version = "1.2")
    @NotNull
    public static final List<String> windowed(@NotNull final CharSequence charSequence, final int n, final int n2, final boolean b) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        return windowed(charSequence, n, n2, b, (Function1<? super CharSequence, ? extends String>)StringsKt___StringsKt$windowed.StringsKt___StringsKt$windowed$1.INSTANCE);
    }
    
    @SinceKotlin(version = "1.2")
    @NotNull
    public static final <R> List<R> windowed(@NotNull final CharSequence charSequence, final int n, final int n2, final boolean b, @NotNull final Function1<? super CharSequence, ? extends R> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        SlidingWindowKt.checkWindowSizeStep(n, n2);
        final int length = charSequence.length();
        final ArrayList list = new ArrayList<R>((length + n2 - 1) / n2);
        for (int i = 0; i < length; i += n2) {
            int n3;
            if ((n3 = i + n) > length) {
                if (!b) {
                    break;
                }
                n3 = length;
            }
            list.add(function1.invoke(charSequence.subSequence(i, n3)));
        }
        return (List)list;
    }
    
    @SinceKotlin(version = "1.2")
    @NotNull
    public static final Sequence<String> windowedSequence(@NotNull final CharSequence charSequence, final int n, final int n2, final boolean b) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        return windowedSequence(charSequence, n, n2, b, (Function1<? super CharSequence, ? extends String>)StringsKt___StringsKt$windowedSequence.StringsKt___StringsKt$windowedSequence$1.INSTANCE);
    }
    
    @SinceKotlin(version = "1.2")
    @NotNull
    public static final <R> Sequence<R> windowedSequence(@NotNull final CharSequence charSequence, final int n, final int n2, final boolean b, @NotNull final Function1<? super CharSequence, ? extends R> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        SlidingWindowKt.checkWindowSizeStep(n, n2);
        IntRange intRange;
        if (b) {
            intRange = StringsKt__StringsKt.getIndices(charSequence);
        }
        else {
            intRange = RangesKt___RangesKt.until(0, charSequence.length() - n + 1);
        }
        return (Sequence<R>)SequencesKt___SequencesKt.map((Sequence<?>)CollectionsKt___CollectionsKt.asSequence((Iterable<?>)RangesKt___RangesKt.step(intRange, n2)), (Function1<? super Object, ?>)new StringsKt___StringsKt$windowedSequence.StringsKt___StringsKt$windowedSequence$2(charSequence, (Function1)function1, n));
    }
    
    @NotNull
    public static final Iterable<IndexedValue<Character>> withIndex(@NotNull final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        return (Iterable)new IndexingIterable((Function0<? extends Iterator<?>>)new StringsKt___StringsKt$withIndex.StringsKt___StringsKt$withIndex$1(charSequence));
    }
    
    @NotNull
    public static final List<Pair<Character, Character>> zip(@NotNull final CharSequence charSequence, @NotNull final CharSequence charSequence2) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(charSequence2, "other");
        final int min = Math.min(charSequence.length(), charSequence2.length());
        final ArrayList list = new ArrayList<Pair<Character, Character>>(min);
        for (int i = 0; i < min; ++i) {
            list.add(TuplesKt.to(charSequence.charAt(i), charSequence2.charAt(i)));
        }
        return (List)list;
    }
    
    @NotNull
    public static final <V> List<V> zip(@NotNull final CharSequence charSequence, @NotNull final CharSequence charSequence2, @NotNull final Function2<? super Character, ? super Character, ? extends V> function2) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(charSequence2, "other");
        Intrinsics.checkParameterIsNotNull(function2, "transform");
        final int min = Math.min(charSequence.length(), charSequence2.length());
        final ArrayList list = new ArrayList<V>(min);
        for (int i = 0; i < min; ++i) {
            list.add(function2.invoke(charSequence.charAt(i), charSequence2.charAt(i)));
        }
        return (List)list;
    }
    
    @SinceKotlin(version = "1.2")
    @NotNull
    public static final List<Pair<Character, Character>> zipWithNext(@NotNull final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        final int initialCapacity = charSequence.length() - 1;
        List<Object> emptyList;
        if (initialCapacity < 1) {
            emptyList = CollectionsKt__CollectionsKt.emptyList();
        }
        else {
            final ArrayList<Object> list = new ArrayList<Object>(initialCapacity);
            int i = 0;
            while (i < initialCapacity) {
                final char char1 = charSequence.charAt(i);
                ++i;
                list.add(TuplesKt.to(char1, charSequence.charAt(i)));
            }
            emptyList = list;
        }
        return (List<Pair<Character, Character>>)emptyList;
    }
    
    @SinceKotlin(version = "1.2")
    @NotNull
    public static final <R> List<R> zipWithNext(@NotNull final CharSequence charSequence, @NotNull final Function2<? super Character, ? super Character, ? extends R> function2) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function2, "transform");
        final int initialCapacity = charSequence.length() - 1;
        if (initialCapacity < 1) {
            return CollectionsKt__CollectionsKt.emptyList();
        }
        final ArrayList list = new ArrayList<R>(initialCapacity);
        int i = 0;
        while (i < initialCapacity) {
            final char char1 = charSequence.charAt(i);
            ++i;
            list.add(function2.invoke(char1, charSequence.charAt(i)));
        }
        return (List)list;
    }
}
