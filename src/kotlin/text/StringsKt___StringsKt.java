package kotlin.text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.SinceKotlin;
import kotlin.TuplesKt;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.CharIterator;
import kotlin.collections.CollectionsKt;
import kotlin.collections.Grouping;
import kotlin.collections.IndexedValue;
import kotlin.collections.IndexingIterable;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.collections.SlidingWindowKt;
import kotlin.internal.InlineOnly;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import org.apache.commons.imaging.formats.tiff.constants.GpsTagConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: _Strings.kt */
@Metadata(bv = {1, 0, 2}, d1 = {"\u0000Ô\u0001\n\u0000\n\u0002\u0010\u000b\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\f\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010%\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u001f\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0010\u000f\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\"\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\u001a!\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\n\u0010\u0006\u001a\u00020\u0001*\u00020\u0002\u001a!\u0010\u0006\u001a\u00020\u0001*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\u0010\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\b*\u00020\u0002\u001a\u0010\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00050\n*\u00020\u0002\u001aE\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u000e0\f\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e*\u00020\u00022\u001e\u0010\u000f\u001a\u001a\u0012\u0004\u0012\u00020\u0005\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u000e0\u00100\u0004H\u0086\b\u001a3\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u00020\u00050\f\"\u0004\b\u0000\u0010\r*\u00020\u00022\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u0004H\u0086\b\u001aM\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u000e0\f\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e*\u00020\u00022\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u00042\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\u0086\b\u001aN\u0010\u0014\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\r\"\u0018\b\u0001\u0010\u0015*\u0012\u0012\u0006\b\u0000\u0012\u0002H\r\u0012\u0006\b\u0000\u0012\u00020\u00050\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u0004H\u0086\b¢\u0006\u0002\u0010\u0018\u001ah\u0010\u0014\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e\"\u0018\b\u0002\u0010\u0015*\u0012\u0012\u0006\b\u0000\u0012\u0002H\r\u0012\u0006\b\u0000\u0012\u0002H\u000e0\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u00042\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\u0086\b¢\u0006\u0002\u0010\u0019\u001a`\u0010\u001a\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e\"\u0018\b\u0002\u0010\u0015*\u0012\u0012\u0006\b\u0000\u0012\u0002H\r\u0012\u0006\b\u0000\u0012\u0002H\u000e0\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u001e\u0010\u000f\u001a\u001a\u0012\u0004\u0012\u00020\u0005\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u000e0\u00100\u0004H\u0086\b¢\u0006\u0002\u0010\u0018\u001a\u001a\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001c*\u00020\u00022\u0006\u0010\u001e\u001a\u00020\u001fH\u0007\u001a4\u0010\u001b\u001a\b\u0012\u0004\u0012\u0002H 0\u001c\"\u0004\b\u0000\u0010 *\u00020\u00022\u0006\u0010\u001e\u001a\u00020\u001f2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H 0\u0004H\u0007\u001a\u001a\u0010!\u001a\b\u0012\u0004\u0012\u00020\u001d0\n*\u00020\u00022\u0006\u0010\u001e\u001a\u00020\u001fH\u0007\u001a4\u0010!\u001a\b\u0012\u0004\u0012\u0002H 0\n\"\u0004\b\u0000\u0010 *\u00020\u00022\u0006\u0010\u001e\u001a\u00020\u001f2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H 0\u0004H\u0007\u001a\r\u0010\"\u001a\u00020\u001f*\u00020\u0002H\u0087\b\u001a!\u0010\"\u001a\u00020\u001f*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\u0012\u0010#\u001a\u00020\u0002*\u00020\u00022\u0006\u0010$\u001a\u00020\u001f\u001a\u0012\u0010#\u001a\u00020\u001d*\u00020\u001d2\u0006\u0010$\u001a\u00020\u001f\u001a\u0012\u0010%\u001a\u00020\u0002*\u00020\u00022\u0006\u0010$\u001a\u00020\u001f\u001a\u0012\u0010%\u001a\u00020\u001d*\u00020\u001d2\u0006\u0010$\u001a\u00020\u001f\u001a!\u0010&\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u0010&\u001a\u00020\u001d*\u00020\u001d2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u0010'\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u0010'\u001a\u00020\u001d*\u00020\u001d2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\u0015\u0010(\u001a\u00020\u0005*\u00020\u00022\u0006\u0010)\u001a\u00020\u001fH\u0087\b\u001a)\u0010*\u001a\u00020\u0005*\u00020\u00022\u0006\u0010)\u001a\u00020\u001f2\u0012\u0010+\u001a\u000e\u0012\u0004\u0012\u00020\u001f\u0012\u0004\u0012\u00020\u00050\u0004H\u0087\b\u001a\u001c\u0010,\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0006\u0010)\u001a\u00020\u001fH\u0087\b¢\u0006\u0002\u0010-\u001a!\u0010.\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u0010.\u001a\u00020\u001d*\u00020\u001d2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a6\u0010/\u001a\u00020\u0002*\u00020\u00022'\u0010\u0003\u001a#\u0012\u0013\u0012\u00110\u001f¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b()\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000100H\u0086\b\u001a6\u0010/\u001a\u00020\u001d*\u00020\u001d2'\u0010\u0003\u001a#\u0012\u0013\u0012\u00110\u001f¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b()\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000100H\u0086\b\u001aQ\u00103\u001a\u0002H4\"\f\b\u0000\u00104*\u000605j\u0002`6*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H42'\u0010\u0003\u001a#\u0012\u0013\u0012\u00110\u001f¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b()\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000100H\u0086\b¢\u0006\u0002\u00107\u001a!\u00108\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u00108\u001a\u00020\u001d*\u00020\u001d2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a<\u00109\u001a\u0002H4\"\f\b\u0000\u00104*\u000605j\u0002`6*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H42\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b¢\u0006\u0002\u0010:\u001a<\u0010;\u001a\u0002H4\"\f\b\u0000\u00104*\u000605j\u0002`6*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H42\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b¢\u0006\u0002\u0010:\u001a(\u0010<\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0087\b¢\u0006\u0002\u0010=\u001a(\u0010>\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0087\b¢\u0006\u0002\u0010=\u001a\n\u0010?\u001a\u00020\u0005*\u00020\u0002\u001a!\u0010?\u001a\u00020\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\u0011\u0010@\u001a\u0004\u0018\u00010\u0005*\u00020\u0002¢\u0006\u0002\u0010A\u001a(\u0010@\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b¢\u0006\u0002\u0010=\u001a3\u0010B\u001a\b\u0012\u0004\u0012\u0002H 0\u001c\"\u0004\b\u0000\u0010 *\u00020\u00022\u0018\u0010\u000f\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u0002H 0\b0\u0004H\u0086\b\u001aL\u0010C\u001a\u0002H4\"\u0004\b\u0000\u0010 \"\u0010\b\u0001\u00104*\n\u0012\u0006\b\u0000\u0012\u0002H 0D*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H42\u0018\u0010\u000f\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u0002H 0\b0\u0004H\u0086\b¢\u0006\u0002\u0010E\u001aI\u0010F\u001a\u0002H \"\u0004\b\u0000\u0010 *\u00020\u00022\u0006\u0010G\u001a\u0002H 2'\u0010H\u001a#\u0012\u0013\u0012\u0011H ¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b(I\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H 00H\u0086\b¢\u0006\u0002\u0010J\u001a^\u0010K\u001a\u0002H \"\u0004\b\u0000\u0010 *\u00020\u00022\u0006\u0010G\u001a\u0002H 2<\u0010H\u001a8\u0012\u0013\u0012\u00110\u001f¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b()\u0012\u0013\u0012\u0011H ¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b(I\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H 0LH\u0086\b¢\u0006\u0002\u0010M\u001aI\u0010N\u001a\u0002H \"\u0004\b\u0000\u0010 *\u00020\u00022\u0006\u0010G\u001a\u0002H 2'\u0010H\u001a#\u0012\u0004\u0012\u00020\u0005\u0012\u0013\u0012\u0011H ¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b(I\u0012\u0004\u0012\u0002H 00H\u0086\b¢\u0006\u0002\u0010J\u001a^\u0010O\u001a\u0002H \"\u0004\b\u0000\u0010 *\u00020\u00022\u0006\u0010G\u001a\u0002H 2<\u0010H\u001a8\u0012\u0013\u0012\u00110\u001f¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b()\u0012\u0004\u0012\u00020\u0005\u0012\u0013\u0012\u0011H ¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b(I\u0012\u0004\u0012\u0002H 0LH\u0086\b¢\u0006\u0002\u0010M\u001a!\u0010P\u001a\u00020Q*\u00020\u00022\u0012\u0010R\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020Q0\u0004H\u0086\b\u001a6\u0010S\u001a\u00020Q*\u00020\u00022'\u0010R\u001a#\u0012\u0013\u0012\u00110\u001f¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b()\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020Q00H\u0086\b\u001a)\u0010T\u001a\u00020\u0005*\u00020\u00022\u0006\u0010)\u001a\u00020\u001f2\u0012\u0010+\u001a\u000e\u0012\u0004\u0012\u00020\u001f\u0012\u0004\u0012\u00020\u00050\u0004H\u0087\b\u001a\u0019\u0010U\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0006\u0010)\u001a\u00020\u001f¢\u0006\u0002\u0010-\u001a9\u0010V\u001a\u0014\u0012\u0004\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u001c0\f\"\u0004\b\u0000\u0010\r*\u00020\u00022\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u0004H\u0086\b\u001aS\u0010V\u001a\u0014\u0012\u0004\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000e0\u001c0\f\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e*\u00020\u00022\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u00042\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\u0086\b\u001aR\u0010W\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\r\"\u001c\b\u0001\u0010\u0015*\u0016\u0012\u0006\b\u0000\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050X0\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u0004H\u0086\b¢\u0006\u0002\u0010\u0018\u001al\u0010W\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e\"\u001c\b\u0002\u0010\u0015*\u0016\u0012\u0006\b\u0000\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000e0X0\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u00042\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\u0086\b¢\u0006\u0002\u0010\u0019\u001a5\u0010Y\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0Z\"\u0004\b\u0000\u0010\r*\u00020\u00022\u0014\b\u0004\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u0004H\u0087\b\u001a!\u0010[\u001a\u00020\u001f*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u0010\\\u001a\u00020\u001f*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\n\u0010]\u001a\u00020\u0005*\u00020\u0002\u001a!\u0010]\u001a\u00020\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\u0011\u0010^\u001a\u0004\u0018\u00010\u0005*\u00020\u0002¢\u0006\u0002\u0010A\u001a(\u0010^\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b¢\u0006\u0002\u0010=\u001a-\u0010_\u001a\b\u0012\u0004\u0012\u0002H 0\u001c\"\u0004\b\u0000\u0010 *\u00020\u00022\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H 0\u0004H\u0086\b\u001aB\u0010`\u001a\b\u0012\u0004\u0012\u0002H 0\u001c\"\u0004\b\u0000\u0010 *\u00020\u00022'\u0010\u000f\u001a#\u0012\u0013\u0012\u00110\u001f¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b()\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H 00H\u0086\b\u001aH\u0010a\u001a\b\u0012\u0004\u0012\u0002H 0\u001c\"\b\b\u0000\u0010 *\u00020b*\u00020\u00022)\u0010\u000f\u001a%\u0012\u0013\u0012\u00110\u001f¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b()\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u0001H 00H\u0086\b\u001aa\u0010c\u001a\u0002H4\"\b\b\u0000\u0010 *\u00020b\"\u0010\b\u0001\u00104*\n\u0012\u0006\b\u0000\u0012\u0002H 0D*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H42)\u0010\u000f\u001a%\u0012\u0013\u0012\u00110\u001f¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b()\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u0001H 00H\u0086\b¢\u0006\u0002\u0010d\u001a[\u0010e\u001a\u0002H4\"\u0004\b\u0000\u0010 \"\u0010\b\u0001\u00104*\n\u0012\u0006\b\u0000\u0012\u0002H 0D*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H42'\u0010\u000f\u001a#\u0012\u0013\u0012\u00110\u001f¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b()\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H 00H\u0086\b¢\u0006\u0002\u0010d\u001a3\u0010f\u001a\b\u0012\u0004\u0012\u0002H 0\u001c\"\b\b\u0000\u0010 *\u00020b*\u00020\u00022\u0014\u0010\u000f\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u0001H 0\u0004H\u0086\b\u001aL\u0010g\u001a\u0002H4\"\b\b\u0000\u0010 *\u00020b\"\u0010\b\u0001\u00104*\n\u0012\u0006\b\u0000\u0012\u0002H 0D*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H42\u0014\u0010\u000f\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u0001H 0\u0004H\u0086\b¢\u0006\u0002\u0010E\u001aF\u0010h\u001a\u0002H4\"\u0004\b\u0000\u0010 \"\u0010\b\u0001\u00104*\n\u0012\u0006\b\u0000\u0012\u0002H 0D*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H42\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H 0\u0004H\u0086\b¢\u0006\u0002\u0010E\u001a\u0011\u0010i\u001a\u0004\u0018\u00010\u0005*\u00020\u0002¢\u0006\u0002\u0010A\u001a8\u0010j\u001a\u0004\u0018\u00010\u0005\"\u000e\b\u0000\u0010 *\b\u0012\u0004\u0012\u0002H 0k*\u00020\u00022\u0012\u0010l\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H 0\u0004H\u0086\b¢\u0006\u0002\u0010=\u001a-\u0010m\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u001a\u0010n\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00050oj\n\u0012\u0006\b\u0000\u0012\u00020\u0005`p¢\u0006\u0002\u0010q\u001a\u0011\u0010r\u001a\u0004\u0018\u00010\u0005*\u00020\u0002¢\u0006\u0002\u0010A\u001a8\u0010s\u001a\u0004\u0018\u00010\u0005\"\u000e\b\u0000\u0010 *\b\u0012\u0004\u0012\u0002H 0k*\u00020\u00022\u0012\u0010l\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H 0\u0004H\u0086\b¢\u0006\u0002\u0010=\u001a-\u0010t\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u001a\u0010n\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00050oj\n\u0012\u0006\b\u0000\u0012\u00020\u0005`p¢\u0006\u0002\u0010q\u001a\n\u0010u\u001a\u00020\u0001*\u00020\u0002\u001a!\u0010u\u001a\u00020\u0001*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a0\u0010v\u001a\u0002Hw\"\b\b\u0000\u0010w*\u00020\u0002*\u0002Hw2\u0012\u0010R\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020Q0\u0004H\u0087\b¢\u0006\u0002\u0010x\u001a-\u0010y\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u0010*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a-\u0010y\u001a\u000e\u0012\u0004\u0012\u00020\u001d\u0012\u0004\u0012\u00020\u001d0\u0010*\u00020\u001d2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a6\u0010z\u001a\u00020\u0005*\u00020\u00022'\u0010H\u001a#\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b(I\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000500H\u0086\b\u001aK\u0010{\u001a\u00020\u0005*\u00020\u00022<\u0010H\u001a8\u0012\u0013\u0012\u00110\u001f¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b()\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b(I\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050LH\u0086\b\u001a6\u0010|\u001a\u00020\u0005*\u00020\u00022'\u0010H\u001a#\u0012\u0004\u0012\u00020\u0005\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b(I\u0012\u0004\u0012\u00020\u000500H\u0086\b\u001aK\u0010}\u001a\u00020\u0005*\u00020\u00022<\u0010H\u001a8\u0012\u0013\u0012\u00110\u001f¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b()\u0012\u0004\u0012\u00020\u0005\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b1\u0012\b\b2\u0012\u0004\b\b(I\u0012\u0004\u0012\u00020\u00050LH\u0086\b\u001a\n\u0010~\u001a\u00020\u0002*\u00020\u0002\u001a\r\u0010~\u001a\u00020\u001d*\u00020\u001dH\u0087\b\u001a\n\u0010\u007f\u001a\u00020\u0005*\u00020\u0002\u001a!\u0010\u007f\u001a\u00020\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\u0012\u0010\u0080\u0001\u001a\u0004\u0018\u00010\u0005*\u00020\u0002¢\u0006\u0002\u0010A\u001a)\u0010\u0080\u0001\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b¢\u0006\u0002\u0010=\u001a\u001a\u0010\u0081\u0001\u001a\u00020\u0002*\u00020\u00022\r\u0010\u0082\u0001\u001a\b\u0012\u0004\u0012\u00020\u001f0\b\u001a\u0015\u0010\u0081\u0001\u001a\u00020\u0002*\u00020\u00022\b\u0010\u0082\u0001\u001a\u00030\u0083\u0001\u001a\u001d\u0010\u0081\u0001\u001a\u00020\u001d*\u00020\u001d2\r\u0010\u0082\u0001\u001a\b\u0012\u0004\u0012\u00020\u001f0\bH\u0087\b\u001a\u0015\u0010\u0081\u0001\u001a\u00020\u001d*\u00020\u001d2\b\u0010\u0082\u0001\u001a\u00030\u0083\u0001\u001a\"\u0010\u0084\u0001\u001a\u00020\u001f*\u00020\u00022\u0012\u0010l\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u001f0\u0004H\u0086\b\u001a$\u0010\u0085\u0001\u001a\u00030\u0086\u0001*\u00020\u00022\u0013\u0010l\u001a\u000f\u0012\u0004\u0012\u00020\u0005\u0012\u0005\u0012\u00030\u0086\u00010\u0004H\u0086\b\u001a\u0013\u0010\u0087\u0001\u001a\u00020\u0002*\u00020\u00022\u0006\u0010$\u001a\u00020\u001f\u001a\u0013\u0010\u0087\u0001\u001a\u00020\u001d*\u00020\u001d2\u0006\u0010$\u001a\u00020\u001f\u001a\u0013\u0010\u0088\u0001\u001a\u00020\u0002*\u00020\u00022\u0006\u0010$\u001a\u00020\u001f\u001a\u0013\u0010\u0088\u0001\u001a\u00020\u001d*\u00020\u001d2\u0006\u0010$\u001a\u00020\u001f\u001a\"\u0010\u0089\u0001\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\"\u0010\u0089\u0001\u001a\u00020\u001d*\u00020\u001d2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\"\u0010\u008a\u0001\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\"\u0010\u008a\u0001\u001a\u00020\u001d*\u00020\u001d2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a+\u0010\u008b\u0001\u001a\u0002H4\"\u0010\b\u0000\u00104*\n\u0012\u0006\b\u0000\u0012\u00020\u00050D*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H4¢\u0006\u0003\u0010\u008c\u0001\u001a\u001d\u0010\u008d\u0001\u001a\u0014\u0012\u0004\u0012\u00020\u00050\u008e\u0001j\t\u0012\u0004\u0012\u00020\u0005`\u008f\u0001*\u00020\u0002\u001a\u0011\u0010\u0090\u0001\u001a\b\u0012\u0004\u0012\u00020\u00050\u001c*\u00020\u0002\u001a\u0011\u0010\u0091\u0001\u001a\b\u0012\u0004\u0012\u00020\u00050X*\u00020\u0002\u001a\u0012\u0010\u0092\u0001\u001a\t\u0012\u0004\u0012\u00020\u00050\u0093\u0001*\u00020\u0002\u001a1\u0010\u0094\u0001\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001c*\u00020\u00022\u0006\u0010\u001e\u001a\u00020\u001f2\t\b\u0002\u0010\u0095\u0001\u001a\u00020\u001f2\t\b\u0002\u0010\u0096\u0001\u001a\u00020\u0001H\u0007\u001aK\u0010\u0094\u0001\u001a\b\u0012\u0004\u0012\u0002H 0\u001c\"\u0004\b\u0000\u0010 *\u00020\u00022\u0006\u0010\u001e\u001a\u00020\u001f2\t\b\u0002\u0010\u0095\u0001\u001a\u00020\u001f2\t\b\u0002\u0010\u0096\u0001\u001a\u00020\u00012\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H 0\u0004H\u0007\u001a1\u0010\u0097\u0001\u001a\b\u0012\u0004\u0012\u00020\u001d0\n*\u00020\u00022\u0006\u0010\u001e\u001a\u00020\u001f2\t\b\u0002\u0010\u0095\u0001\u001a\u00020\u001f2\t\b\u0002\u0010\u0096\u0001\u001a\u00020\u0001H\u0007\u001aK\u0010\u0097\u0001\u001a\b\u0012\u0004\u0012\u0002H 0\n\"\u0004\b\u0000\u0010 *\u00020\u00022\u0006\u0010\u001e\u001a\u00020\u001f2\t\b\u0002\u0010\u0095\u0001\u001a\u00020\u001f2\t\b\u0002\u0010\u0096\u0001\u001a\u00020\u00012\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H 0\u0004H\u0007\u001a\u0018\u0010\u0098\u0001\u001a\u000f\u0012\u000b\u0012\t\u0012\u0004\u0012\u00020\u00050\u0099\u00010\b*\u00020\u0002\u001a)\u0010\u009a\u0001\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u00100\u001c*\u00020\u00022\u0007\u0010\u009b\u0001\u001a\u00020\u0002H\u0086\u0004\u001a]\u0010\u009a\u0001\u001a\b\u0012\u0004\u0012\u0002H\u000e0\u001c\"\u0004\b\u0000\u0010\u000e*\u00020\u00022\u0007\u0010\u009b\u0001\u001a\u00020\u000228\u0010\u000f\u001a4\u0012\u0014\u0012\u00120\u0005¢\u0006\r\b1\u0012\t\b2\u0012\u0005\b\b(\u009c\u0001\u0012\u0014\u0012\u00120\u0005¢\u0006\r\b1\u0012\t\b2\u0012\u0005\b\b(\u009d\u0001\u0012\u0004\u0012\u0002H\u000e00H\u0086\b\u001a\u001f\u0010\u009e\u0001\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u00100\u001c*\u00020\u0002H\u0007\u001aT\u0010\u009e\u0001\u001a\b\u0012\u0004\u0012\u0002H 0\u001c\"\u0004\b\u0000\u0010 *\u00020\u000228\u0010\u000f\u001a4\u0012\u0014\u0012\u00120\u0005¢\u0006\r\b1\u0012\t\b2\u0012\u0005\b\b(\u009c\u0001\u0012\u0014\u0012\u00120\u0005¢\u0006\r\b1\u0012\t\b2\u0012\u0005\b\b(\u009d\u0001\u0012\u0004\u0012\u0002H 00H\u0087\b¨\u0006\u009f\u0001"}, d2 = {"all", "", "", "predicate", "Lkotlin/Function1;", "", "any", "asIterable", "", "asSequence", "Lkotlin/sequences/Sequence;", "associate", "", "K", GpsTagConstants.GPS_TAG_GPS_STATUS_VALUE_MEASUREMENT_INTEROPERABILITY, "transform", "Lkotlin/Pair;", "associateBy", "keySelector", "valueTransform", "associateByTo", "M", "", "destination", "(Ljava/lang/CharSequence;Ljava/util/Map;Lkotlin/jvm/functions/Function1;)Ljava/util/Map;", "(Ljava/lang/CharSequence;Ljava/util/Map;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)Ljava/util/Map;", "associateTo", "chunked", "", "", "size", "", "R", "chunkedSequence", "count", "drop", "n", "dropLast", "dropLastWhile", "dropWhile", "elementAt", "index", "elementAtOrElse", "defaultValue", "elementAtOrNull", "(Ljava/lang/CharSequence;I)Ljava/lang/Character;", "filter", "filterIndexed", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "filterIndexedTo", "C", "Ljava/lang/Appendable;", "Lkotlin/text/Appendable;", "(Ljava/lang/CharSequence;Ljava/lang/Appendable;Lkotlin/jvm/functions/Function2;)Ljava/lang/Appendable;", "filterNot", "filterNotTo", "(Ljava/lang/CharSequence;Ljava/lang/Appendable;Lkotlin/jvm/functions/Function1;)Ljava/lang/Appendable;", "filterTo", "find", "(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1;)Ljava/lang/Character;", "findLast", "first", "firstOrNull", "(Ljava/lang/CharSequence;)Ljava/lang/Character;", "flatMap", "flatMapTo", "", "(Ljava/lang/CharSequence;Ljava/util/Collection;Lkotlin/jvm/functions/Function1;)Ljava/util/Collection;", "fold", "initial", "operation", "acc", "(Ljava/lang/CharSequence;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "foldIndexed", "Lkotlin/Function3;", "(Ljava/lang/CharSequence;Ljava/lang/Object;Lkotlin/jvm/functions/Function3;)Ljava/lang/Object;", "foldRight", "foldRightIndexed", "forEach", "", "action", "forEachIndexed", "getOrElse", "getOrNull", "groupBy", "groupByTo", "", "groupingBy", "Lkotlin/collections/Grouping;", "indexOfFirst", "indexOfLast", "last", "lastOrNull", "map", "mapIndexed", "mapIndexedNotNull", "", "mapIndexedNotNullTo", "(Ljava/lang/CharSequence;Ljava/util/Collection;Lkotlin/jvm/functions/Function2;)Ljava/util/Collection;", "mapIndexedTo", "mapNotNull", "mapNotNullTo", "mapTo", "max", "maxBy", "", "selector", "maxWith", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "(Ljava/lang/CharSequence;Ljava/util/Comparator;)Ljava/lang/Character;", "min", "minBy", "minWith", "none", "onEach", "S", "(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1;)Ljava/lang/CharSequence;", "partition", "reduce", "reduceIndexed", "reduceRight", "reduceRightIndexed", "reversed", "single", "singleOrNull", "slice", "indices", "Lkotlin/ranges/IntRange;", "sumBy", "sumByDouble", "", "take", "takeLast", "takeLastWhile", "takeWhile", "toCollection", "(Ljava/lang/CharSequence;Ljava/util/Collection;)Ljava/util/Collection;", "toHashSet", "Ljava/util/HashSet;", "Lkotlin/collections/HashSet;", "toList", "toMutableList", "toSet", "", "windowed", "step", "partialWindows", "windowedSequence", "withIndex", "Lkotlin/collections/IndexedValue;", "zip", "other", "a", "b", "zipWithNext", "kotlin-stdlib"}, k = 5, mv = {1, 1, 10}, xi = 1, xs = "kotlin/text/StringsKt")
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class StringsKt___StringsKt extends StringsKt___StringsJvmKt {
    @InlineOnly
    private static final char elementAt(@NotNull CharSequence charSequence, int i) {
        return charSequence.charAt(i);
    }

    @InlineOnly
    private static final char elementAtOrElse(@NotNull CharSequence charSequence, int i, Function1<? super Integer, Character> function1) {
        return (i < 0 || i > StringsKt.getLastIndex(charSequence)) ? function1.invoke(Integer.valueOf(i)).charValue() : charSequence.charAt(i);
    }

    @InlineOnly
    private static final Character elementAtOrNull(@NotNull CharSequence charSequence, int i) {
        return StringsKt.getOrNull(charSequence, i);
    }

    public static final char first(@NotNull CharSequence receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        if (receiver.length() == 0) {
            throw new NoSuchElementException("Char sequence is empty.");
        }
        return receiver.charAt(0);
    }

    public static final char first(@NotNull CharSequence receiver, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        for (int i = 0; i < receiver.length(); i++) {
            char cCharAt = receiver.charAt(i);
            if (predicate.invoke(Character.valueOf(cCharAt)).booleanValue()) {
                return cCharAt;
            }
        }
        throw new NoSuchElementException("Char sequence contains no character matching the predicate.");
    }

    @Nullable
    public static final Character firstOrNull(@NotNull CharSequence receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        if (receiver.length() == 0) {
            return null;
        }
        return Character.valueOf(receiver.charAt(0));
    }

    @Nullable
    public static final Character firstOrNull(@NotNull CharSequence receiver, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        for (int i = 0; i < receiver.length(); i++) {
            char cCharAt = receiver.charAt(i);
            if (predicate.invoke(Character.valueOf(cCharAt)).booleanValue()) {
                return Character.valueOf(cCharAt);
            }
        }
        return null;
    }

    @InlineOnly
    private static final char getOrElse(@NotNull CharSequence charSequence, int i, Function1<? super Integer, Character> function1) {
        return (i < 0 || i > StringsKt.getLastIndex(charSequence)) ? function1.invoke(Integer.valueOf(i)).charValue() : charSequence.charAt(i);
    }

    @Nullable
    public static final Character getOrNull(@NotNull CharSequence receiver, int i) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        if (i < 0 || i > StringsKt.getLastIndex(receiver)) {
            return null;
        }
        return Character.valueOf(receiver.charAt(i));
    }

    public static final int indexOfFirst(@NotNull CharSequence receiver, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int length = receiver.length();
        for (int i = 0; i < length; i++) {
            if (predicate.invoke(Character.valueOf(receiver.charAt(i))).booleanValue()) {
                return i;
            }
        }
        return -1;
    }

    public static final int indexOfLast(@NotNull CharSequence receiver, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        for (int length = receiver.length() - 1; length >= 0; length--) {
            if (predicate.invoke(Character.valueOf(receiver.charAt(length))).booleanValue()) {
                return length;
            }
        }
        return -1;
    }

    public static final char last(@NotNull CharSequence receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        if (receiver.length() == 0) {
            throw new NoSuchElementException("Char sequence is empty.");
        }
        return receiver.charAt(StringsKt.getLastIndex(receiver));
    }

    public static final char last(@NotNull CharSequence receiver, @NotNull Function1<? super Character, Boolean> predicate) {
        char cCharAt;
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int length = receiver.length();
        do {
            length--;
            if (length >= 0) {
                cCharAt = receiver.charAt(length);
            } else {
                throw new NoSuchElementException("Char sequence contains no character matching the predicate.");
            }
        } while (!predicate.invoke(Character.valueOf(cCharAt)).booleanValue());
        return cCharAt;
    }

    @Nullable
    public static final Character lastOrNull(@NotNull CharSequence receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        if (receiver.length() == 0) {
            return null;
        }
        return Character.valueOf(receiver.charAt(receiver.length() - 1));
    }

    @Nullable
    public static final Character lastOrNull(@NotNull CharSequence receiver, @NotNull Function1<? super Character, Boolean> predicate) {
        char cCharAt;
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int length = receiver.length();
        do {
            length--;
            if (length < 0) {
                return null;
            }
            cCharAt = receiver.charAt(length);
        } while (!predicate.invoke(Character.valueOf(cCharAt)).booleanValue());
        return Character.valueOf(cCharAt);
    }

    public static final char single(@NotNull CharSequence receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        switch (receiver.length()) {
            case 0:
                throw new NoSuchElementException("Char sequence is empty.");
            case 1:
                return receiver.charAt(0);
            default:
                throw new IllegalArgumentException("Char sequence has more than one element.");
        }
    }

    public static final char single(@NotNull CharSequence receiver, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        Character chValueOf = (Character) null;
        boolean z = false;
        for (int i = 0; i < receiver.length(); i++) {
            char cCharAt = receiver.charAt(i);
            if (predicate.invoke(Character.valueOf(cCharAt)).booleanValue()) {
                if (z) {
                    throw new IllegalArgumentException("Char sequence contains more than one matching element.");
                }
                chValueOf = Character.valueOf(cCharAt);
                z = true;
            }
        }
        if (!z) {
            throw new NoSuchElementException("Char sequence contains no character matching the predicate.");
        }
        if (chValueOf == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Char");
        }
        return chValueOf.charValue();
    }

    @Nullable
    public static final Character singleOrNull(@NotNull CharSequence receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        if (receiver.length() == 1) {
            return Character.valueOf(receiver.charAt(0));
        }
        return null;
    }

    @Nullable
    public static final Character singleOrNull(@NotNull CharSequence receiver, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        Character chValueOf = (Character) null;
        boolean z = false;
        for (int i = 0; i < receiver.length(); i++) {
            char cCharAt = receiver.charAt(i);
            if (predicate.invoke(Character.valueOf(cCharAt)).booleanValue()) {
                if (z) {
                    return null;
                }
                chValueOf = Character.valueOf(cCharAt);
                z = true;
            }
        }
        if (z) {
            return chValueOf;
        }
        return null;
    }

    @NotNull
    public static final CharSequence drop(@NotNull CharSequence receiver, int i) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        if (!(i >= 0)) {
            throw new IllegalArgumentException(("Requested character count " + i + " is less than zero.").toString());
        }
        return receiver.subSequence(RangesKt.coerceAtMost(i, receiver.length()), receiver.length());
    }

    @NotNull
    public static final String drop(@NotNull String receiver, int i) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        if (!(i >= 0)) {
            throw new IllegalArgumentException(("Requested character count " + i + " is less than zero.").toString());
        }
        String strSubstring = receiver.substring(RangesKt.coerceAtMost(i, receiver.length()));
        Intrinsics.checkExpressionValueIsNotNull(strSubstring, "(this as java.lang.String).substring(startIndex)");
        return strSubstring;
    }

    @NotNull
    public static final CharSequence dropLast(@NotNull CharSequence receiver, int i) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        if (!(i >= 0)) {
            throw new IllegalArgumentException(("Requested character count " + i + " is less than zero.").toString());
        }
        return StringsKt.take(receiver, RangesKt.coerceAtLeast(receiver.length() - i, 0));
    }

    @NotNull
    public static final String dropLast(@NotNull String receiver, int i) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        if (!(i >= 0)) {
            throw new IllegalArgumentException(("Requested character count " + i + " is less than zero.").toString());
        }
        return StringsKt.take(receiver, RangesKt.coerceAtLeast(receiver.length() - i, 0));
    }

    @NotNull
    public static final CharSequence dropLastWhile(@NotNull CharSequence receiver, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        for (int lastIndex = StringsKt.getLastIndex(receiver); lastIndex >= 0; lastIndex--) {
            if (!predicate.invoke(Character.valueOf(receiver.charAt(lastIndex))).booleanValue()) {
                return receiver.subSequence(0, lastIndex + 1);
            }
        }
        return "";
    }

    @NotNull
    public static final String dropLastWhile(@NotNull String receiver, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        for (int lastIndex = StringsKt.getLastIndex(receiver); lastIndex >= 0; lastIndex--) {
            if (!predicate.invoke(Character.valueOf(receiver.charAt(lastIndex))).booleanValue()) {
                String strSubstring = receiver.substring(0, lastIndex + 1);
                Intrinsics.checkExpressionValueIsNotNull(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                return strSubstring;
            }
        }
        return "";
    }

    @NotNull
    public static final CharSequence dropWhile(@NotNull CharSequence receiver, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int length = receiver.length();
        for (int i = 0; i < length; i++) {
            if (!predicate.invoke(Character.valueOf(receiver.charAt(i))).booleanValue()) {
                return receiver.subSequence(i, receiver.length());
            }
        }
        return "";
    }

    @NotNull
    public static final String dropWhile(@NotNull String receiver, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int length = receiver.length();
        for (int i = 0; i < length; i++) {
            if (!predicate.invoke(Character.valueOf(receiver.charAt(i))).booleanValue()) {
                String strSubstring = receiver.substring(i);
                Intrinsics.checkExpressionValueIsNotNull(strSubstring, "(this as java.lang.String).substring(startIndex)");
                return strSubstring;
            }
        }
        return "";
    }

    @NotNull
    public static final CharSequence filter(@NotNull CharSequence receiver, @NotNull Function1<? super Character, Boolean> predicate) throws IOException {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        StringBuilder sb = new StringBuilder();
        int length = receiver.length();
        for (int i = 0; i < length; i++) {
            char cCharAt = receiver.charAt(i);
            if (predicate.invoke(Character.valueOf(cCharAt)).booleanValue()) {
                sb.append(cCharAt);
            }
        }
        return sb;
    }

    @NotNull
    public static final String filter(@NotNull String receiver, @NotNull Function1<? super Character, Boolean> predicate) throws IOException {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        String str = receiver;
        StringBuilder sb = new StringBuilder();
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char cCharAt = str.charAt(i);
            if (predicate.invoke(Character.valueOf(cCharAt)).booleanValue()) {
                sb.append(cCharAt);
            }
        }
        String string = sb.toString();
        Intrinsics.checkExpressionValueIsNotNull(string, "filterTo(StringBuilder(), predicate).toString()");
        return string;
    }

    @NotNull
    public static final CharSequence filterIndexed(@NotNull CharSequence receiver, @NotNull Function2<? super Integer, ? super Character, Boolean> predicate) throws IOException {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        StringBuilder sb = new StringBuilder();
        int i = 0;
        int i2 = 0;
        while (i < receiver.length()) {
            char cCharAt = receiver.charAt(i);
            int i3 = i2 + 1;
            if (predicate.invoke(Integer.valueOf(i2), Character.valueOf(cCharAt)).booleanValue()) {
                sb.append(cCharAt);
            }
            i++;
            i2 = i3;
        }
        return sb;
    }

    @NotNull
    public static final String filterIndexed(@NotNull String receiver, @NotNull Function2<? super Integer, ? super Character, Boolean> predicate) throws IOException {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        String str = receiver;
        StringBuilder sb = new StringBuilder();
        int i = 0;
        int i2 = 0;
        while (i < str.length()) {
            char cCharAt = str.charAt(i);
            int i3 = i2 + 1;
            if (predicate.invoke(Integer.valueOf(i2), Character.valueOf(cCharAt)).booleanValue()) {
                sb.append(cCharAt);
            }
            i++;
            i2 = i3;
        }
        String string = sb.toString();
        Intrinsics.checkExpressionValueIsNotNull(string, "filterIndexedTo(StringBu…(), predicate).toString()");
        return string;
    }

    @NotNull
    public static final CharSequence filterNot(@NotNull CharSequence receiver, @NotNull Function1<? super Character, Boolean> predicate) throws IOException {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < receiver.length(); i++) {
            char cCharAt = receiver.charAt(i);
            if (!predicate.invoke(Character.valueOf(cCharAt)).booleanValue()) {
                sb.append(cCharAt);
            }
        }
        return sb;
    }

    @NotNull
    public static final String filterNot(@NotNull String receiver, @NotNull Function1<? super Character, Boolean> predicate) throws IOException {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        String str = receiver;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char cCharAt = str.charAt(i);
            if (!predicate.invoke(Character.valueOf(cCharAt)).booleanValue()) {
                sb.append(cCharAt);
            }
        }
        String string = sb.toString();
        Intrinsics.checkExpressionValueIsNotNull(string, "filterNotTo(StringBuilder(), predicate).toString()");
        return string;
    }

    @NotNull
    public static final <C extends Appendable> C filterNotTo(@NotNull CharSequence receiver, @NotNull C destination, @NotNull Function1<? super Character, Boolean> predicate) throws IOException {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        for (int i = 0; i < receiver.length(); i++) {
            char cCharAt = receiver.charAt(i);
            if (!predicate.invoke(Character.valueOf(cCharAt)).booleanValue()) {
                destination.append(cCharAt);
            }
        }
        return destination;
    }

    @NotNull
    public static final <C extends Appendable> C filterTo(@NotNull CharSequence receiver, @NotNull C destination, @NotNull Function1<? super Character, Boolean> predicate) throws IOException {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int length = receiver.length();
        for (int i = 0; i < length; i++) {
            char cCharAt = receiver.charAt(i);
            if (predicate.invoke(Character.valueOf(cCharAt)).booleanValue()) {
                destination.append(cCharAt);
            }
        }
        return destination;
    }

    @NotNull
    public static final CharSequence slice(@NotNull CharSequence receiver, @NotNull IntRange indices) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(indices, "indices");
        return indices.isEmpty() ? "" : StringsKt.subSequence(receiver, indices);
    }

    @NotNull
    public static final String slice(@NotNull String receiver, @NotNull IntRange indices) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(indices, "indices");
        return indices.isEmpty() ? "" : StringsKt.substring(receiver, indices);
    }

    @NotNull
    public static final CharSequence slice(@NotNull CharSequence receiver, @NotNull Iterable<Integer> indices) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(indices, "indices");
        int iCollectionSizeOrDefault = CollectionsKt.collectionSizeOrDefault(indices, 10);
        if (iCollectionSizeOrDefault == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(iCollectionSizeOrDefault);
        Iterator<Integer> it = indices.iterator();
        while (it.hasNext()) {
            sb.append(receiver.charAt(it.next().intValue()));
        }
        return sb;
    }

    @InlineOnly
    private static final String slice(@NotNull String str, Iterable<Integer> iterable) {
        if (str == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
        }
        return StringsKt.slice((CharSequence) str, iterable).toString();
    }

    @NotNull
    public static final CharSequence take(@NotNull CharSequence receiver, int i) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        if (!(i >= 0)) {
            throw new IllegalArgumentException(("Requested character count " + i + " is less than zero.").toString());
        }
        return receiver.subSequence(0, RangesKt.coerceAtMost(i, receiver.length()));
    }

    @NotNull
    public static final String take(@NotNull String receiver, int i) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        if (!(i >= 0)) {
            throw new IllegalArgumentException(("Requested character count " + i + " is less than zero.").toString());
        }
        String strSubstring = receiver.substring(0, RangesKt.coerceAtMost(i, receiver.length()));
        Intrinsics.checkExpressionValueIsNotNull(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        return strSubstring;
    }

    @NotNull
    public static final CharSequence takeLast(@NotNull CharSequence receiver, int i) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        if (!(i >= 0)) {
            throw new IllegalArgumentException(("Requested character count " + i + " is less than zero.").toString());
        }
        int length = receiver.length();
        return receiver.subSequence(length - RangesKt.coerceAtMost(i, length), length);
    }

    @NotNull
    public static final String takeLast(@NotNull String receiver, int i) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        if (!(i >= 0)) {
            throw new IllegalArgumentException(("Requested character count " + i + " is less than zero.").toString());
        }
        int length = receiver.length();
        String strSubstring = receiver.substring(length - RangesKt.coerceAtMost(i, length));
        Intrinsics.checkExpressionValueIsNotNull(strSubstring, "(this as java.lang.String).substring(startIndex)");
        return strSubstring;
    }

    @NotNull
    public static final CharSequence takeLastWhile(@NotNull CharSequence receiver, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        for (int lastIndex = StringsKt.getLastIndex(receiver); lastIndex >= 0; lastIndex--) {
            if (!predicate.invoke(Character.valueOf(receiver.charAt(lastIndex))).booleanValue()) {
                return receiver.subSequence(lastIndex + 1, receiver.length());
            }
        }
        return receiver.subSequence(0, receiver.length());
    }

    @NotNull
    public static final String takeLastWhile(@NotNull String receiver, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        for (int lastIndex = StringsKt.getLastIndex(receiver); lastIndex >= 0; lastIndex--) {
            if (!predicate.invoke(Character.valueOf(receiver.charAt(lastIndex))).booleanValue()) {
                String strSubstring = receiver.substring(lastIndex + 1);
                Intrinsics.checkExpressionValueIsNotNull(strSubstring, "(this as java.lang.String).substring(startIndex)");
                return strSubstring;
            }
        }
        return receiver;
    }

    @NotNull
    public static final CharSequence takeWhile(@NotNull CharSequence receiver, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int length = receiver.length();
        for (int i = 0; i < length; i++) {
            if (!predicate.invoke(Character.valueOf(receiver.charAt(i))).booleanValue()) {
                return receiver.subSequence(0, i);
            }
        }
        return receiver.subSequence(0, receiver.length());
    }

    @NotNull
    public static final String takeWhile(@NotNull String receiver, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int length = receiver.length();
        for (int i = 0; i < length; i++) {
            if (!predicate.invoke(Character.valueOf(receiver.charAt(i))).booleanValue()) {
                String strSubstring = receiver.substring(0, i);
                Intrinsics.checkExpressionValueIsNotNull(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                return strSubstring;
            }
        }
        return receiver;
    }

    @NotNull
    public static final CharSequence reversed(@NotNull CharSequence receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        StringBuilder sbReverse = new StringBuilder(receiver).reverse();
        Intrinsics.checkExpressionValueIsNotNull(sbReverse, "StringBuilder(this).reverse()");
        return sbReverse;
    }

    @InlineOnly
    private static final String reversed(@NotNull String str) {
        if (str == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
        }
        return StringsKt.reversed((CharSequence) str).toString();
    }

    @NotNull
    public static final <K, V> Map<K, V> associate(@NotNull CharSequence receiver, @NotNull Function1<? super Character, ? extends Pair<? extends K, ? extends V>> transform) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        LinkedHashMap linkedHashMap = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity(receiver.length()), 16));
        for (int i = 0; i < receiver.length(); i++) {
            Pair<? extends K, ? extends V> pairInvoke = transform.invoke(Character.valueOf(receiver.charAt(i)));
            linkedHashMap.put(pairInvoke.getFirst(), pairInvoke.getSecond());
        }
        return linkedHashMap;
    }

    @NotNull
    public static final <K> Map<K, Character> associateBy(@NotNull CharSequence receiver, @NotNull Function1<? super Character, ? extends K> keySelector) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        LinkedHashMap linkedHashMap = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity(receiver.length()), 16));
        for (int i = 0; i < receiver.length(); i++) {
            char cCharAt = receiver.charAt(i);
            linkedHashMap.put(keySelector.invoke(Character.valueOf(cCharAt)), Character.valueOf(cCharAt));
        }
        return linkedHashMap;
    }

    @NotNull
    public static final <K, V> Map<K, V> associateBy(@NotNull CharSequence receiver, @NotNull Function1<? super Character, ? extends K> keySelector, @NotNull Function1<? super Character, ? extends V> valueTransform) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        Intrinsics.checkParameterIsNotNull(valueTransform, "valueTransform");
        LinkedHashMap linkedHashMap = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity(receiver.length()), 16));
        for (int i = 0; i < receiver.length(); i++) {
            char cCharAt = receiver.charAt(i);
            linkedHashMap.put(keySelector.invoke(Character.valueOf(cCharAt)), valueTransform.invoke(Character.valueOf(cCharAt)));
        }
        return linkedHashMap;
    }

    @NotNull
    public static final <K, M extends Map<? super K, ? super Character>> M associateByTo(@NotNull CharSequence receiver, @NotNull M destination, @NotNull Function1<? super Character, ? extends K> keySelector) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        for (int i = 0; i < receiver.length(); i++) {
            char cCharAt = receiver.charAt(i);
            destination.put(keySelector.invoke(Character.valueOf(cCharAt)), Character.valueOf(cCharAt));
        }
        return destination;
    }

    @NotNull
    public static final <K, V, M extends Map<? super K, ? super V>> M associateByTo(@NotNull CharSequence receiver, @NotNull M destination, @NotNull Function1<? super Character, ? extends K> keySelector, @NotNull Function1<? super Character, ? extends V> valueTransform) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        Intrinsics.checkParameterIsNotNull(valueTransform, "valueTransform");
        for (int i = 0; i < receiver.length(); i++) {
            char cCharAt = receiver.charAt(i);
            destination.put(keySelector.invoke(Character.valueOf(cCharAt)), valueTransform.invoke(Character.valueOf(cCharAt)));
        }
        return destination;
    }

    @NotNull
    public static final <K, V, M extends Map<? super K, ? super V>> M associateTo(@NotNull CharSequence receiver, @NotNull M destination, @NotNull Function1<? super Character, ? extends Pair<? extends K, ? extends V>> transform) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        for (int i = 0; i < receiver.length(); i++) {
            Pair<? extends K, ? extends V> pairInvoke = transform.invoke(Character.valueOf(receiver.charAt(i)));
            destination.put(pairInvoke.getFirst(), pairInvoke.getSecond());
        }
        return destination;
    }

    @NotNull
    public static final <C extends Collection<? super Character>> C toCollection(@NotNull CharSequence receiver, @NotNull C destination) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        for (int i = 0; i < receiver.length(); i++) {
            destination.add(Character.valueOf(receiver.charAt(i)));
        }
        return destination;
    }

    @NotNull
    public static final HashSet<Character> toHashSet(@NotNull CharSequence receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return (HashSet) StringsKt.toCollection(receiver, new HashSet(MapsKt.mapCapacity(receiver.length())));
    }

    @NotNull
    public static final List<Character> toList(@NotNull CharSequence receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        switch (receiver.length()) {
            case 0:
                return CollectionsKt.emptyList();
            case 1:
                return CollectionsKt.listOf(Character.valueOf(receiver.charAt(0)));
            default:
                return StringsKt.toMutableList(receiver);
        }
    }

    @NotNull
    public static final List<Character> toMutableList(@NotNull CharSequence receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return (List) StringsKt.toCollection(receiver, new ArrayList(receiver.length()));
    }

    @NotNull
    public static final Set<Character> toSet(@NotNull CharSequence receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        switch (receiver.length()) {
            case 0:
                return SetsKt.emptySet();
            case 1:
                return SetsKt.setOf(Character.valueOf(receiver.charAt(0)));
            default:
                return (Set) StringsKt.toCollection(receiver, new LinkedHashSet(MapsKt.mapCapacity(receiver.length())));
        }
    }

    @NotNull
    public static final <R> List<R> flatMap(@NotNull CharSequence receiver, @NotNull Function1<? super Character, ? extends Iterable<? extends R>> transform) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < receiver.length(); i++) {
            CollectionsKt.addAll(arrayList, transform.invoke(Character.valueOf(receiver.charAt(i))));
        }
        return arrayList;
    }

    @NotNull
    public static final <R, C extends Collection<? super R>> C flatMapTo(@NotNull CharSequence receiver, @NotNull C destination, @NotNull Function1<? super Character, ? extends Iterable<? extends R>> transform) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        for (int i = 0; i < receiver.length(); i++) {
            CollectionsKt.addAll(destination, transform.invoke(Character.valueOf(receiver.charAt(i))));
        }
        return destination;
    }

    @NotNull
    public static final <K> Map<K, List<Character>> groupBy(@NotNull CharSequence receiver, @NotNull Function1<? super Character, ? extends K> keySelector) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (int i = 0; i < receiver.length(); i++) {
            char cCharAt = receiver.charAt(i);
            K kInvoke = keySelector.invoke(Character.valueOf(cCharAt));
            Object arrayList = linkedHashMap.get(kInvoke);
            if (arrayList == null) {
                arrayList = new ArrayList();
                linkedHashMap.put(kInvoke, arrayList);
            }
            ((List) arrayList).add(Character.valueOf(cCharAt));
        }
        return linkedHashMap;
    }

    @NotNull
    public static final <K, V> Map<K, List<V>> groupBy(@NotNull CharSequence receiver, @NotNull Function1<? super Character, ? extends K> keySelector, @NotNull Function1<? super Character, ? extends V> valueTransform) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        Intrinsics.checkParameterIsNotNull(valueTransform, "valueTransform");
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (int i = 0; i < receiver.length(); i++) {
            char cCharAt = receiver.charAt(i);
            K kInvoke = keySelector.invoke(Character.valueOf(cCharAt));
            List<V> arrayList = linkedHashMap.get(kInvoke);
            if (arrayList == null) {
                arrayList = new ArrayList<>();
                linkedHashMap.put(kInvoke, arrayList);
            }
            arrayList.add(valueTransform.invoke(Character.valueOf(cCharAt)));
        }
        return linkedHashMap;
    }

    @NotNull
    public static final <K, M extends Map<? super K, List<Character>>> M groupByTo(@NotNull CharSequence receiver, @NotNull M destination, @NotNull Function1<? super Character, ? extends K> keySelector) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        for (int i = 0; i < receiver.length(); i++) {
            char cCharAt = receiver.charAt(i);
            K kInvoke = keySelector.invoke(Character.valueOf(cCharAt));
            Object arrayList = destination.get(kInvoke);
            if (arrayList == null) {
                arrayList = new ArrayList();
                destination.put(kInvoke, arrayList);
            }
            ((List) arrayList).add(Character.valueOf(cCharAt));
        }
        return destination;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @NotNull
    public static final <K, V, M extends Map<? super K, List<V>>> M groupByTo(@NotNull CharSequence receiver, @NotNull M destination, @NotNull Function1<? super Character, ? extends K> keySelector, @NotNull Function1<? super Character, ? extends V> valueTransform) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        Intrinsics.checkParameterIsNotNull(valueTransform, "valueTransform");
        for (int i = 0; i < receiver.length(); i++) {
            char cCharAt = receiver.charAt(i);
            K kInvoke = keySelector.invoke(Character.valueOf(cCharAt));
            Object arrayList = destination.get(kInvoke);
            if (arrayList == null) {
                arrayList = new ArrayList();
                destination.put(kInvoke, arrayList);
            }
            ((List) arrayList).add(valueTransform.invoke(Character.valueOf(cCharAt)));
        }
        return destination;
    }

    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <K> Grouping<Character, K> groupingBy(@NotNull final CharSequence receiver, @NotNull final Function1<? super Character, ? extends K> keySelector) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(keySelector, "keySelector");
        return new Grouping<Character, K>() { // from class: kotlin.text.StringsKt___StringsKt.groupingBy.1
            @Override // kotlin.collections.Grouping
            public /* bridge */ /* synthetic */ Object keyOf(Character ch) {
                return keyOf(ch.charValue());
            }

            @Override // kotlin.collections.Grouping
            @NotNull
            public Iterator<Character> sourceIterator() {
                return StringsKt.iterator(receiver);
            }

            public K keyOf(char element) {
                return (K) keySelector.invoke(Character.valueOf(element));
            }
        };
    }

    @NotNull
    public static final <R> List<R> map(@NotNull CharSequence receiver, @NotNull Function1<? super Character, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        ArrayList arrayList = new ArrayList(receiver.length());
        for (int i = 0; i < receiver.length(); i++) {
            arrayList.add(transform.invoke(Character.valueOf(receiver.charAt(i))));
        }
        return arrayList;
    }

    @NotNull
    public static final <R> List<R> mapIndexed(@NotNull CharSequence receiver, @NotNull Function2<? super Integer, ? super Character, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        ArrayList arrayList = new ArrayList(receiver.length());
        int i = 0;
        for (int i2 = 0; i2 < receiver.length(); i2++) {
            char cCharAt = receiver.charAt(i2);
            Integer numValueOf = Integer.valueOf(i);
            i++;
            arrayList.add(transform.invoke(numValueOf, Character.valueOf(cCharAt)));
        }
        return arrayList;
    }

    @NotNull
    public static final <R> List<R> mapIndexedNotNull(@NotNull CharSequence receiver, @NotNull Function2<? super Integer, ? super Character, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        ArrayList arrayList = new ArrayList();
        int i = 0;
        int i2 = 0;
        while (i < receiver.length()) {
            int i3 = i2 + 1;
            R rInvoke = transform.invoke(Integer.valueOf(i2), Character.valueOf(receiver.charAt(i)));
            if (rInvoke != null) {
                arrayList.add(rInvoke);
            }
            i++;
            i2 = i3;
        }
        return arrayList;
    }

    @NotNull
    public static final <R, C extends Collection<? super R>> C mapIndexedTo(@NotNull CharSequence receiver, @NotNull C destination, @NotNull Function2<? super Integer, ? super Character, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        int i = 0;
        for (int i2 = 0; i2 < receiver.length(); i2++) {
            char cCharAt = receiver.charAt(i2);
            Integer numValueOf = Integer.valueOf(i);
            i++;
            destination.add(transform.invoke(numValueOf, Character.valueOf(cCharAt)));
        }
        return destination;
    }

    @NotNull
    public static final <R> List<R> mapNotNull(@NotNull CharSequence receiver, @NotNull Function1<? super Character, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < receiver.length(); i++) {
            R rInvoke = transform.invoke(Character.valueOf(receiver.charAt(i)));
            if (rInvoke != null) {
                arrayList.add(rInvoke);
            }
        }
        return arrayList;
    }

    @NotNull
    public static final <R, C extends Collection<? super R>> C mapTo(@NotNull CharSequence receiver, @NotNull C destination, @NotNull Function1<? super Character, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        for (int i = 0; i < receiver.length(); i++) {
            destination.add(transform.invoke(Character.valueOf(receiver.charAt(i))));
        }
        return destination;
    }

    @NotNull
    public static final Iterable<IndexedValue<Character>> withIndex(@NotNull final CharSequence receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return new IndexingIterable(new Function0<CharIterator>() { // from class: kotlin.text.StringsKt___StringsKt.withIndex.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(0);
            }

            @Override // kotlin.jvm.functions.Function0
            @NotNull
            public final CharIterator invoke() {
                return StringsKt.iterator(receiver);
            }
        });
    }

    public static final boolean all(@NotNull CharSequence receiver, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        for (int i = 0; i < receiver.length(); i++) {
            if (!predicate.invoke(Character.valueOf(receiver.charAt(i))).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    public static final boolean any(@NotNull CharSequence receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return !(receiver.length() == 0);
    }

    public static final boolean any(@NotNull CharSequence receiver, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        for (int i = 0; i < receiver.length(); i++) {
            if (predicate.invoke(Character.valueOf(receiver.charAt(i))).booleanValue()) {
                return true;
            }
        }
        return false;
    }

    @InlineOnly
    private static final int count(@NotNull CharSequence charSequence) {
        return charSequence.length();
    }

    public static final int count(@NotNull CharSequence receiver, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int i = 0;
        for (int i2 = 0; i2 < receiver.length(); i2++) {
            if (predicate.invoke(Character.valueOf(receiver.charAt(i2))).booleanValue()) {
                i++;
            }
        }
        return i;
    }

    public static final <R> R fold(@NotNull CharSequence receiver, R r, @NotNull Function2<? super R, ? super Character, ? extends R> operation) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        for (int i = 0; i < receiver.length(); i++) {
            r = operation.invoke(r, Character.valueOf(receiver.charAt(i)));
        }
        return r;
    }

    public static final <R> R foldIndexed(@NotNull CharSequence receiver, R r, @NotNull Function3<? super Integer, ? super R, ? super Character, ? extends R> operation) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        R rInvoke = r;
        int i = 0;
        for (int i2 = 0; i2 < receiver.length(); i2++) {
            char cCharAt = receiver.charAt(i2);
            Integer numValueOf = Integer.valueOf(i);
            i++;
            rInvoke = operation.invoke(numValueOf, rInvoke, Character.valueOf(cCharAt));
        }
        return rInvoke;
    }

    public static final <R> R foldRight(@NotNull CharSequence receiver, R r, @NotNull Function2<? super Character, ? super R, ? extends R> operation) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        for (int lastIndex = StringsKt.getLastIndex(receiver); lastIndex >= 0; lastIndex--) {
            r = operation.invoke(Character.valueOf(receiver.charAt(lastIndex)), r);
        }
        return r;
    }

    public static final <R> R foldRightIndexed(@NotNull CharSequence receiver, R r, @NotNull Function3<? super Integer, ? super Character, ? super R, ? extends R> operation) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        for (int lastIndex = StringsKt.getLastIndex(receiver); lastIndex >= 0; lastIndex--) {
            r = operation.invoke(Integer.valueOf(lastIndex), Character.valueOf(receiver.charAt(lastIndex)), r);
        }
        return r;
    }

    public static final void forEach(@NotNull CharSequence receiver, @NotNull Function1<? super Character, Unit> action) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(action, "action");
        for (int i = 0; i < receiver.length(); i++) {
            action.invoke(Character.valueOf(receiver.charAt(i)));
        }
    }

    public static final void forEachIndexed(@NotNull CharSequence receiver, @NotNull Function2<? super Integer, ? super Character, Unit> action) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(action, "action");
        int i = 0;
        for (int i2 = 0; i2 < receiver.length(); i2++) {
            char cCharAt = receiver.charAt(i2);
            Integer numValueOf = Integer.valueOf(i);
            i++;
            action.invoke(numValueOf, Character.valueOf(cCharAt));
        }
    }

    @Nullable
    public static final Character max(@NotNull CharSequence receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        int i = 1;
        if (receiver.length() == 0) {
            return null;
        }
        char cCharAt = receiver.charAt(0);
        int lastIndex = StringsKt.getLastIndex(receiver);
        if (1 <= lastIndex) {
            while (true) {
                char cCharAt2 = receiver.charAt(i);
                if (cCharAt < cCharAt2) {
                    cCharAt = cCharAt2;
                }
                if (i == lastIndex) {
                    break;
                }
                i++;
            }
        }
        return Character.valueOf(cCharAt);
    }

    @Nullable
    public static final <R extends Comparable<? super R>> Character maxBy(@NotNull CharSequence receiver, @NotNull Function1<? super Character, ? extends R> selector) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(selector, "selector");
        int i = 1;
        if (receiver.length() == 0) {
            return null;
        }
        char cCharAt = receiver.charAt(0);
        R rInvoke = selector.invoke(Character.valueOf(cCharAt));
        int lastIndex = StringsKt.getLastIndex(receiver);
        if (1 <= lastIndex) {
            while (true) {
                char cCharAt2 = receiver.charAt(i);
                R rInvoke2 = selector.invoke(Character.valueOf(cCharAt2));
                if (rInvoke.compareTo(rInvoke2) < 0) {
                    cCharAt = cCharAt2;
                    rInvoke = rInvoke2;
                }
                if (i == lastIndex) {
                    break;
                }
                i++;
            }
        }
        return Character.valueOf(cCharAt);
    }

    @Nullable
    public static final Character maxWith(@NotNull CharSequence receiver, @NotNull Comparator<? super Character> comparator) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        int i = 1;
        if (receiver.length() == 0) {
            return null;
        }
        char cCharAt = receiver.charAt(0);
        int lastIndex = StringsKt.getLastIndex(receiver);
        if (1 <= lastIndex) {
            while (true) {
                char cCharAt2 = receiver.charAt(i);
                if (comparator.compare(Character.valueOf(cCharAt), Character.valueOf(cCharAt2)) < 0) {
                    cCharAt = cCharAt2;
                }
                if (i == lastIndex) {
                    break;
                }
                i++;
            }
        }
        return Character.valueOf(cCharAt);
    }

    @Nullable
    public static final Character min(@NotNull CharSequence receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        int i = 1;
        if (receiver.length() == 0) {
            return null;
        }
        char cCharAt = receiver.charAt(0);
        int lastIndex = StringsKt.getLastIndex(receiver);
        if (1 <= lastIndex) {
            while (true) {
                char cCharAt2 = receiver.charAt(i);
                if (cCharAt > cCharAt2) {
                    cCharAt = cCharAt2;
                }
                if (i == lastIndex) {
                    break;
                }
                i++;
            }
        }
        return Character.valueOf(cCharAt);
    }

    @Nullable
    public static final <R extends Comparable<? super R>> Character minBy(@NotNull CharSequence receiver, @NotNull Function1<? super Character, ? extends R> selector) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(selector, "selector");
        int i = 1;
        if (receiver.length() == 0) {
            return null;
        }
        char cCharAt = receiver.charAt(0);
        R rInvoke = selector.invoke(Character.valueOf(cCharAt));
        int lastIndex = StringsKt.getLastIndex(receiver);
        if (1 <= lastIndex) {
            while (true) {
                char cCharAt2 = receiver.charAt(i);
                R rInvoke2 = selector.invoke(Character.valueOf(cCharAt2));
                if (rInvoke.compareTo(rInvoke2) > 0) {
                    cCharAt = cCharAt2;
                    rInvoke = rInvoke2;
                }
                if (i == lastIndex) {
                    break;
                }
                i++;
            }
        }
        return Character.valueOf(cCharAt);
    }

    @Nullable
    public static final Character minWith(@NotNull CharSequence receiver, @NotNull Comparator<? super Character> comparator) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        int i = 1;
        if (receiver.length() == 0) {
            return null;
        }
        char cCharAt = receiver.charAt(0);
        int lastIndex = StringsKt.getLastIndex(receiver);
        if (1 <= lastIndex) {
            while (true) {
                char cCharAt2 = receiver.charAt(i);
                if (comparator.compare(Character.valueOf(cCharAt), Character.valueOf(cCharAt2)) > 0) {
                    cCharAt = cCharAt2;
                }
                if (i == lastIndex) {
                    break;
                }
                i++;
            }
        }
        return Character.valueOf(cCharAt);
    }

    public static final boolean none(@NotNull CharSequence receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return receiver.length() == 0;
    }

    public static final boolean none(@NotNull CharSequence receiver, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        for (int i = 0; i < receiver.length(); i++) {
            if (predicate.invoke(Character.valueOf(receiver.charAt(i))).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <S extends CharSequence> S onEach(@NotNull S receiver, @NotNull Function1<? super Character, Unit> action) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(action, "action");
        for (int i = 0; i < receiver.length(); i++) {
            action.invoke(Character.valueOf(receiver.charAt(i)));
        }
        return receiver;
    }

    public static final char reduce(@NotNull CharSequence receiver, @NotNull Function2<? super Character, ? super Character, Character> operation) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        int i = 1;
        if (receiver.length() == 0) {
            throw new UnsupportedOperationException("Empty char sequence can't be reduced.");
        }
        char cCharAt = receiver.charAt(0);
        int lastIndex = StringsKt.getLastIndex(receiver);
        if (1 <= lastIndex) {
            while (true) {
                cCharAt = operation.invoke(Character.valueOf(cCharAt), Character.valueOf(receiver.charAt(i))).charValue();
                if (i == lastIndex) {
                    break;
                }
                i++;
            }
        }
        return cCharAt;
    }

    public static final char reduceIndexed(@NotNull CharSequence receiver, @NotNull Function3<? super Integer, ? super Character, ? super Character, Character> operation) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        int i = 1;
        if (receiver.length() == 0) {
            throw new UnsupportedOperationException("Empty char sequence can't be reduced.");
        }
        char cCharAt = receiver.charAt(0);
        int lastIndex = StringsKt.getLastIndex(receiver);
        if (1 <= lastIndex) {
            while (true) {
                cCharAt = operation.invoke(Integer.valueOf(i), Character.valueOf(cCharAt), Character.valueOf(receiver.charAt(i))).charValue();
                if (i == lastIndex) {
                    break;
                }
                i++;
            }
        }
        return cCharAt;
    }

    public static final char reduceRight(@NotNull CharSequence receiver, @NotNull Function2<? super Character, ? super Character, Character> operation) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        int lastIndex = StringsKt.getLastIndex(receiver);
        if (lastIndex < 0) {
            throw new UnsupportedOperationException("Empty char sequence can't be reduced.");
        }
        char cCharAt = receiver.charAt(lastIndex);
        for (int i = lastIndex - 1; i >= 0; i--) {
            cCharAt = operation.invoke(Character.valueOf(receiver.charAt(i)), Character.valueOf(cCharAt)).charValue();
        }
        return cCharAt;
    }

    public static final char reduceRightIndexed(@NotNull CharSequence receiver, @NotNull Function3<? super Integer, ? super Character, ? super Character, Character> operation) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        int lastIndex = StringsKt.getLastIndex(receiver);
        if (lastIndex < 0) {
            throw new UnsupportedOperationException("Empty char sequence can't be reduced.");
        }
        char cCharAt = receiver.charAt(lastIndex);
        for (int i = lastIndex - 1; i >= 0; i--) {
            cCharAt = operation.invoke(Integer.valueOf(i), Character.valueOf(receiver.charAt(i)), Character.valueOf(cCharAt)).charValue();
        }
        return cCharAt;
    }

    public static final int sumBy(@NotNull CharSequence receiver, @NotNull Function1<? super Character, Integer> selector) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(selector, "selector");
        int iIntValue = 0;
        for (int i = 0; i < receiver.length(); i++) {
            iIntValue += selector.invoke(Character.valueOf(receiver.charAt(i))).intValue();
        }
        return iIntValue;
    }

    public static final double sumByDouble(@NotNull CharSequence receiver, @NotNull Function1<? super Character, Double> selector) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(selector, "selector");
        double dDoubleValue = 0.0d;
        for (int i = 0; i < receiver.length(); i++) {
            dDoubleValue += selector.invoke(Character.valueOf(receiver.charAt(i))).doubleValue();
        }
        return dDoubleValue;
    }

    @SinceKotlin(version = "1.2")
    @NotNull
    public static final List<String> chunked(@NotNull CharSequence receiver, int i) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return StringsKt.windowed(receiver, i, i, true);
    }

    @SinceKotlin(version = "1.2")
    @NotNull
    public static final <R> List<R> chunked(@NotNull CharSequence receiver, int i, @NotNull Function1<? super CharSequence, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        return StringsKt.windowed(receiver, i, i, true, transform);
    }

    @SinceKotlin(version = "1.2")
    @NotNull
    public static final Sequence<String> chunkedSequence(@NotNull CharSequence receiver, int i) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return StringsKt.chunkedSequence(receiver, i, new Function1<CharSequence, String>() { // from class: kotlin.text.StringsKt___StringsKt.chunkedSequence.1
            @Override // kotlin.jvm.functions.Function1
            @NotNull
            public final String invoke(@NotNull CharSequence it) {
                Intrinsics.checkParameterIsNotNull(it, "it");
                return it.toString();
            }
        });
    }

    @SinceKotlin(version = "1.2")
    @NotNull
    public static final <R> Sequence<R> chunkedSequence(@NotNull CharSequence receiver, int i, @NotNull Function1<? super CharSequence, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        return StringsKt.windowedSequence(receiver, i, i, true, transform);
    }

    @NotNull
    public static final Pair<CharSequence, CharSequence> partition(@NotNull CharSequence receiver, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < receiver.length(); i++) {
            char cCharAt = receiver.charAt(i);
            if (predicate.invoke(Character.valueOf(cCharAt)).booleanValue()) {
                sb.append(cCharAt);
            } else {
                sb2.append(cCharAt);
            }
        }
        return new Pair<>(sb, sb2);
    }

    @NotNull
    public static final Pair<String, String> partition(@NotNull String receiver, @NotNull Function1<? super Character, Boolean> predicate) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        int length = receiver.length();
        for (int i = 0; i < length; i++) {
            char cCharAt = receiver.charAt(i);
            if (predicate.invoke(Character.valueOf(cCharAt)).booleanValue()) {
                sb.append(cCharAt);
            } else {
                sb2.append(cCharAt);
            }
        }
        return new Pair<>(sb.toString(), sb2.toString());
    }

    @SinceKotlin(version = "1.2")
    @NotNull
    public static /* bridge */ /* synthetic */ List windowed$default(CharSequence charSequence, int i, int i2, boolean z, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i2 = 1;
        }
        if ((i3 & 4) != 0) {
            z = false;
        }
        return StringsKt.windowed(charSequence, i, i2, z);
    }

    @SinceKotlin(version = "1.2")
    @NotNull
    public static final List<String> windowed(@NotNull CharSequence receiver, int i, int i2, boolean z) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return StringsKt.windowed(receiver, i, i2, z, new Function1<CharSequence, String>() { // from class: kotlin.text.StringsKt___StringsKt.windowed.1
            @Override // kotlin.jvm.functions.Function1
            @NotNull
            public final String invoke(@NotNull CharSequence it) {
                Intrinsics.checkParameterIsNotNull(it, "it");
                return it.toString();
            }
        });
    }

    @SinceKotlin(version = "1.2")
    @NotNull
    public static /* bridge */ /* synthetic */ List windowed$default(CharSequence charSequence, int i, int i2, boolean z, Function1 function1, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i2 = 1;
        }
        if ((i3 & 4) != 0) {
            z = false;
        }
        return StringsKt.windowed(charSequence, i, i2, z, function1);
    }

    @SinceKotlin(version = "1.2")
    @NotNull
    public static final <R> List<R> windowed(@NotNull CharSequence receiver, int i, int i2, boolean z, @NotNull Function1<? super CharSequence, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        SlidingWindowKt.checkWindowSizeStep(i, i2);
        int length = receiver.length();
        ArrayList arrayList = new ArrayList(((length + i2) - 1) / i2);
        int i3 = 0;
        while (i3 < length) {
            int i4 = i3 + i;
            if (i4 > length) {
                if (!z) {
                    break;
                }
                i4 = length;
            }
            arrayList.add(transform.invoke(receiver.subSequence(i3, i4)));
            i3 += i2;
        }
        return arrayList;
    }

    @SinceKotlin(version = "1.2")
    @NotNull
    public static /* bridge */ /* synthetic */ Sequence windowedSequence$default(CharSequence charSequence, int i, int i2, boolean z, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i2 = 1;
        }
        if ((i3 & 4) != 0) {
            z = false;
        }
        return StringsKt.windowedSequence(charSequence, i, i2, z);
    }

    @SinceKotlin(version = "1.2")
    @NotNull
    public static final Sequence<String> windowedSequence(@NotNull CharSequence receiver, int i, int i2, boolean z) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return StringsKt.windowedSequence(receiver, i, i2, z, new Function1<CharSequence, String>() { // from class: kotlin.text.StringsKt___StringsKt.windowedSequence.1
            @Override // kotlin.jvm.functions.Function1
            @NotNull
            public final String invoke(@NotNull CharSequence it) {
                Intrinsics.checkParameterIsNotNull(it, "it");
                return it.toString();
            }
        });
    }

    @SinceKotlin(version = "1.2")
    @NotNull
    public static /* bridge */ /* synthetic */ Sequence windowedSequence$default(CharSequence charSequence, int i, int i2, boolean z, Function1 function1, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i2 = 1;
        }
        if ((i3 & 4) != 0) {
            z = false;
        }
        return StringsKt.windowedSequence(charSequence, i, i2, z, function1);
    }

    @SinceKotlin(version = "1.2")
    @NotNull
    public static final <R> Sequence<R> windowedSequence(@NotNull final CharSequence receiver, final int i, int i2, boolean z, @NotNull final Function1<? super CharSequence, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        SlidingWindowKt.checkWindowSizeStep(i, i2);
        return SequencesKt.map(CollectionsKt.asSequence(RangesKt.step(z ? StringsKt.getIndices(receiver) : RangesKt.until(0, (receiver.length() - i) + 1), i2)), new Function1<Integer, R>() { // from class: kotlin.text.StringsKt___StringsKt.windowedSequence.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Object invoke(Integer num) {
                return invoke(num.intValue());
            }

            public final R invoke(int i3) {
                return (R) transform.invoke(receiver.subSequence(i3, RangesKt.coerceAtMost(i + i3, receiver.length())));
            }
        });
    }

    @NotNull
    public static final <V> List<V> zip(@NotNull CharSequence receiver, @NotNull CharSequence other, @NotNull Function2<? super Character, ? super Character, ? extends V> transform) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(other, "other");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        int iMin = Math.min(receiver.length(), other.length());
        ArrayList arrayList = new ArrayList(iMin);
        for (int i = 0; i < iMin; i++) {
            arrayList.add(transform.invoke(Character.valueOf(receiver.charAt(i)), Character.valueOf(other.charAt(i))));
        }
        return arrayList;
    }

    @SinceKotlin(version = "1.2")
    @NotNull
    public static final <R> List<R> zipWithNext(@NotNull CharSequence receiver, @NotNull Function2<? super Character, ? super Character, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        int length = receiver.length() - 1;
        if (length < 1) {
            return CollectionsKt.emptyList();
        }
        ArrayList arrayList = new ArrayList(length);
        int i = 0;
        while (i < length) {
            Character chValueOf = Character.valueOf(receiver.charAt(i));
            i++;
            arrayList.add(transform.invoke(chValueOf, Character.valueOf(receiver.charAt(i))));
        }
        return arrayList;
    }

    @NotNull
    public static final Iterable<Character> asIterable(@NotNull CharSequence receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        if (receiver instanceof String) {
            if (receiver.length() == 0) {
                return CollectionsKt.emptyList();
            }
        }
        return new StringsKt___StringsKt$asIterable$$inlined$Iterable$1(receiver);
    }

    @NotNull
    public static final Sequence<Character> asSequence(@NotNull final CharSequence receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        if (receiver instanceof String) {
            if (receiver.length() == 0) {
                return SequencesKt.emptySequence();
            }
        }
        return new Sequence<Character>() { // from class: kotlin.text.StringsKt___StringsKt$asSequence$$inlined$Sequence$1
            @Override // kotlin.sequences.Sequence
            @NotNull
            public Iterator<Character> iterator() {
                return StringsKt.iterator(receiver);
            }
        };
    }

    @InlineOnly
    private static final Character find(@NotNull CharSequence charSequence, Function1<? super Character, Boolean> function1) {
        for (int i = 0; i < charSequence.length(); i++) {
            char cCharAt = charSequence.charAt(i);
            if (function1.invoke(Character.valueOf(cCharAt)).booleanValue()) {
                return Character.valueOf(cCharAt);
            }
        }
        return null;
    }

    @InlineOnly
    private static final Character findLast(@NotNull CharSequence charSequence, Function1<? super Character, Boolean> function1) {
        char cCharAt;
        int length = charSequence.length();
        do {
            length--;
            if (length < 0) {
                return null;
            }
            cCharAt = charSequence.charAt(length);
        } while (!function1.invoke(Character.valueOf(cCharAt)).booleanValue());
        return Character.valueOf(cCharAt);
    }

    @NotNull
    public static final <C extends Appendable> C filterIndexedTo(@NotNull CharSequence receiver, @NotNull C destination, @NotNull Function2<? super Integer, ? super Character, Boolean> predicate) throws IOException {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(predicate, "predicate");
        int i = 0;
        int i2 = 0;
        while (i < receiver.length()) {
            char cCharAt = receiver.charAt(i);
            int i3 = i2 + 1;
            if (predicate.invoke(Integer.valueOf(i2), Character.valueOf(cCharAt)).booleanValue()) {
                destination.append(cCharAt);
            }
            i++;
            i2 = i3;
        }
        return destination;
    }

    @NotNull
    public static final <R, C extends Collection<? super R>> C mapIndexedNotNullTo(@NotNull CharSequence receiver, @NotNull C destination, @NotNull Function2<? super Integer, ? super Character, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        int i = 0;
        int i2 = 0;
        while (i < receiver.length()) {
            int i3 = i2 + 1;
            R rInvoke = transform.invoke(Integer.valueOf(i2), Character.valueOf(receiver.charAt(i)));
            if (rInvoke != null) {
                destination.add(rInvoke);
            }
            i++;
            i2 = i3;
        }
        return destination;
    }

    @NotNull
    public static final <R, C extends Collection<? super R>> C mapNotNullTo(@NotNull CharSequence receiver, @NotNull C destination, @NotNull Function1<? super Character, ? extends R> transform) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(transform, "transform");
        for (int i = 0; i < receiver.length(); i++) {
            R rInvoke = transform.invoke(Character.valueOf(receiver.charAt(i)));
            if (rInvoke != null) {
                destination.add(rInvoke);
            }
        }
        return destination;
    }

    @NotNull
    public static final List<Pair<Character, Character>> zip(@NotNull CharSequence receiver, @NotNull CharSequence other) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(other, "other");
        int iMin = Math.min(receiver.length(), other.length());
        ArrayList arrayList = new ArrayList(iMin);
        for (int i = 0; i < iMin; i++) {
            arrayList.add(TuplesKt.to(Character.valueOf(receiver.charAt(i)), Character.valueOf(other.charAt(i))));
        }
        return arrayList;
    }

    @SinceKotlin(version = "1.2")
    @NotNull
    public static final List<Pair<Character, Character>> zipWithNext(@NotNull CharSequence receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        int length = receiver.length() - 1;
        if (length < 1) {
            return CollectionsKt.emptyList();
        }
        ArrayList arrayList = new ArrayList(length);
        int i = 0;
        while (i < length) {
            char cCharAt = receiver.charAt(i);
            i++;
            arrayList.add(TuplesKt.to(Character.valueOf(cCharAt), Character.valueOf(receiver.charAt(i))));
        }
        return arrayList;
    }
}
