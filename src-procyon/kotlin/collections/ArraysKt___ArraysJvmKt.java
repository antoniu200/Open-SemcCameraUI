// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.collections;

import java.util.TreeSet;
import java.util.SortedSet;
import kotlin.TypeCastException;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import kotlin.internal.InlineOnly;
import kotlin.SinceKotlin;
import java.util.Comparator;
import java.util.Arrays;
import kotlin.jvm.internal.Intrinsics;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u0096\u0001\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u000b\n\u0002\u0010\u0018\n\u0002\u0010\u0005\n\u0002\u0010\u0012\n\u0002\u0010\f\n\u0002\u0010\u0019\n\u0002\u0010\u0006\n\u0002\u0010\u0013\n\u0002\u0010\u0007\n\u0002\u0010\u0014\n\u0002\u0010\b\n\u0002\u0010\u0015\n\u0002\u0010\t\n\u0002\u0010\u0016\n\u0002\u0010\n\n\u0002\u0010\u0017\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000e\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u001f\n\u0002\b\u0006\n\u0002\u0010\u001e\n\u0002\b\u0004\n\u0002\u0010\u000f\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\f\u001a#\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003¢\u0006\u0002\u0010\u0004\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00050\u0001*\u00020\u0006\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00070\u0001*\u00020\b\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\t0\u0001*\u00020\n\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0001*\u00020\f\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\r0\u0001*\u00020\u000e\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0001*\u00020\u0010\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00110\u0001*\u00020\u0012\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00130\u0001*\u00020\u0014\u001aU\u0010\u0015\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u0006\u0010\u0016\u001a\u0002H\u00022\u001a\u0010\u0017\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0018j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f¢\u0006\u0002\u0010\u001c\u001a9\u0010\u0015\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u0006\u0010\u0016\u001a\u0002H\u00022\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f¢\u0006\u0002\u0010\u001d\u001a&\u0010\u0015\u001a\u00020\u000f*\u00020\b2\u0006\u0010\u0016\u001a\u00020\u00072\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010\u0015\u001a\u00020\u000f*\u00020\n2\u0006\u0010\u0016\u001a\u00020\t2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010\u0015\u001a\u00020\u000f*\u00020\f2\u0006\u0010\u0016\u001a\u00020\u000b2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010\u0015\u001a\u00020\u000f*\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\r2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010\u0015\u001a\u00020\u000f*\u00020\u00102\u0006\u0010\u0016\u001a\u00020\u000f2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010\u0015\u001a\u00020\u000f*\u00020\u00122\u0006\u0010\u0016\u001a\u00020\u00112\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010\u0015\u001a\u00020\u000f*\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u00132\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a0\u0010\u001e\u001a\u00020\u0005\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u000e\u0010\u001f\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\u0087\f¢\u0006\u0002\u0010 \u001a \u0010!\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\u0087\b¢\u0006\u0002\u0010\"\u001a \u0010#\u001a\u00020$\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\u0087\b¢\u0006\u0002\u0010%\u001a0\u0010&\u001a\u00020\u0005\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u000e\u0010\u001f\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\u0087\f¢\u0006\u0002\u0010 \u001a\u0015\u0010&\u001a\u00020\u0005*\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u0006H\u0087\f\u001a\u0015\u0010&\u001a\u00020\u0005*\u00020\b2\u0006\u0010\u001f\u001a\u00020\bH\u0087\f\u001a\u0015\u0010&\u001a\u00020\u0005*\u00020\n2\u0006\u0010\u001f\u001a\u00020\nH\u0087\f\u001a\u0015\u0010&\u001a\u00020\u0005*\u00020\f2\u0006\u0010\u001f\u001a\u00020\fH\u0087\f\u001a\u0015\u0010&\u001a\u00020\u0005*\u00020\u000e2\u0006\u0010\u001f\u001a\u00020\u000eH\u0087\f\u001a\u0015\u0010&\u001a\u00020\u0005*\u00020\u00102\u0006\u0010\u001f\u001a\u00020\u0010H\u0087\f\u001a\u0015\u0010&\u001a\u00020\u0005*\u00020\u00122\u0006\u0010\u001f\u001a\u00020\u0012H\u0087\f\u001a\u0015\u0010&\u001a\u00020\u0005*\u00020\u00142\u0006\u0010\u001f\u001a\u00020\u0014H\u0087\f\u001a \u0010'\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\u0087\b¢\u0006\u0002\u0010\"\u001a\r\u0010'\u001a\u00020\u000f*\u00020\u0006H\u0087\b\u001a\r\u0010'\u001a\u00020\u000f*\u00020\bH\u0087\b\u001a\r\u0010'\u001a\u00020\u000f*\u00020\nH\u0087\b\u001a\r\u0010'\u001a\u00020\u000f*\u00020\fH\u0087\b\u001a\r\u0010'\u001a\u00020\u000f*\u00020\u000eH\u0087\b\u001a\r\u0010'\u001a\u00020\u000f*\u00020\u0010H\u0087\b\u001a\r\u0010'\u001a\u00020\u000f*\u00020\u0012H\u0087\b\u001a\r\u0010'\u001a\u00020\u000f*\u00020\u0014H\u0087\b\u001a \u0010(\u001a\u00020$\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\u0087\b¢\u0006\u0002\u0010%\u001a\r\u0010(\u001a\u00020$*\u00020\u0006H\u0087\b\u001a\r\u0010(\u001a\u00020$*\u00020\bH\u0087\b\u001a\r\u0010(\u001a\u00020$*\u00020\nH\u0087\b\u001a\r\u0010(\u001a\u00020$*\u00020\fH\u0087\b\u001a\r\u0010(\u001a\u00020$*\u00020\u000eH\u0087\b\u001a\r\u0010(\u001a\u00020$*\u00020\u0010H\u0087\b\u001a\r\u0010(\u001a\u00020$*\u00020\u0012H\u0087\b\u001a\r\u0010(\u001a\u00020$*\u00020\u0014H\u0087\b\u001a$\u0010)\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0087\b¢\u0006\u0002\u0010*\u001a.\u0010)\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010+\u001a\u00020\u000fH\u0087\b¢\u0006\u0002\u0010,\u001a\r\u0010)\u001a\u00020\u0006*\u00020\u0006H\u0087\b\u001a\u0015\u0010)\u001a\u00020\u0006*\u00020\u00062\u0006\u0010+\u001a\u00020\u000fH\u0087\b\u001a\r\u0010)\u001a\u00020\b*\u00020\bH\u0087\b\u001a\u0015\u0010)\u001a\u00020\b*\u00020\b2\u0006\u0010+\u001a\u00020\u000fH\u0087\b\u001a\r\u0010)\u001a\u00020\n*\u00020\nH\u0087\b\u001a\u0015\u0010)\u001a\u00020\n*\u00020\n2\u0006\u0010+\u001a\u00020\u000fH\u0087\b\u001a\r\u0010)\u001a\u00020\f*\u00020\fH\u0087\b\u001a\u0015\u0010)\u001a\u00020\f*\u00020\f2\u0006\u0010+\u001a\u00020\u000fH\u0087\b\u001a\r\u0010)\u001a\u00020\u000e*\u00020\u000eH\u0087\b\u001a\u0015\u0010)\u001a\u00020\u000e*\u00020\u000e2\u0006\u0010+\u001a\u00020\u000fH\u0087\b\u001a\r\u0010)\u001a\u00020\u0010*\u00020\u0010H\u0087\b\u001a\u0015\u0010)\u001a\u00020\u0010*\u00020\u00102\u0006\u0010+\u001a\u00020\u000fH\u0087\b\u001a\r\u0010)\u001a\u00020\u0012*\u00020\u0012H\u0087\b\u001a\u0015\u0010)\u001a\u00020\u0012*\u00020\u00122\u0006\u0010+\u001a\u00020\u000fH\u0087\b\u001a\r\u0010)\u001a\u00020\u0014*\u00020\u0014H\u0087\b\u001a\u0015\u0010)\u001a\u00020\u0014*\u00020\u00142\u0006\u0010+\u001a\u00020\u000fH\u0087\b\u001a4\u0010-\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\u0087\b¢\u0006\u0002\u0010.\u001a\u001d\u0010-\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\u0087\b\u001a\u001d\u0010-\u001a\u00020\b*\u00020\b2\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\u0087\b\u001a\u001d\u0010-\u001a\u00020\n*\u00020\n2\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\u0087\b\u001a\u001d\u0010-\u001a\u00020\f*\u00020\f2\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\u0087\b\u001a\u001d\u0010-\u001a\u00020\u000e*\u00020\u000e2\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\u0087\b\u001a\u001d\u0010-\u001a\u00020\u0010*\u00020\u00102\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\u0087\b\u001a\u001d\u0010-\u001a\u00020\u0012*\u00020\u00122\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\u0087\b\u001a\u001d\u0010-\u001a\u00020\u0014*\u00020\u00142\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\u0087\b\u001a7\u0010/\u001a\u000200\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0016\u001a\u0002H\u00022\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f¢\u0006\u0002\u00101\u001a&\u0010/\u001a\u000200*\u00020\u00062\u0006\u0010\u0016\u001a\u00020\u00052\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010/\u001a\u000200*\u00020\b2\u0006\u0010\u0016\u001a\u00020\u00072\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010/\u001a\u000200*\u00020\n2\u0006\u0010\u0016\u001a\u00020\t2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010/\u001a\u000200*\u00020\f2\u0006\u0010\u0016\u001a\u00020\u000b2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010/\u001a\u000200*\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\r2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010/\u001a\u000200*\u00020\u00102\u0006\u0010\u0016\u001a\u00020\u000f2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010/\u001a\u000200*\u00020\u00122\u0006\u0010\u0016\u001a\u00020\u00112\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010/\u001a\u000200*\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u00132\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a-\u00102\u001a\b\u0012\u0004\u0012\u0002H30\u0001\"\u0004\b\u0000\u00103*\u0006\u0012\u0002\b\u00030\u00032\f\u00104\u001a\b\u0012\u0004\u0012\u0002H305¢\u0006\u0002\u00106\u001aA\u00107\u001a\u0002H8\"\u0010\b\u0000\u00108*\n\u0012\u0006\b\u0000\u0012\u0002H309\"\u0004\b\u0001\u00103*\u0006\u0012\u0002\b\u00030\u00032\u0006\u0010:\u001a\u0002H82\f\u00104\u001a\b\u0012\u0004\u0012\u0002H305¢\u0006\u0002\u0010;\u001a,\u0010<\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0016\u001a\u0002H\u0002H\u0086\u0002¢\u0006\u0002\u0010=\u001a4\u0010<\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u000e\u0010>\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\u0086\u0002¢\u0006\u0002\u0010?\u001a2\u0010<\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\f\u0010>\u001a\b\u0012\u0004\u0012\u0002H\u00020@H\u0086\u0002¢\u0006\u0002\u0010A\u001a\u0015\u0010<\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u0016\u001a\u00020\u0005H\u0086\u0002\u001a\u0015\u0010<\u001a\u00020\u0006*\u00020\u00062\u0006\u0010>\u001a\u00020\u0006H\u0086\u0002\u001a\u001b\u0010<\u001a\u00020\u0006*\u00020\u00062\f\u0010>\u001a\b\u0012\u0004\u0012\u00020\u00050@H\u0086\u0002\u001a\u0015\u0010<\u001a\u00020\b*\u00020\b2\u0006\u0010\u0016\u001a\u00020\u0007H\u0086\u0002\u001a\u0015\u0010<\u001a\u00020\b*\u00020\b2\u0006\u0010>\u001a\u00020\bH\u0086\u0002\u001a\u001b\u0010<\u001a\u00020\b*\u00020\b2\f\u0010>\u001a\b\u0012\u0004\u0012\u00020\u00070@H\u0086\u0002\u001a\u0015\u0010<\u001a\u00020\n*\u00020\n2\u0006\u0010\u0016\u001a\u00020\tH\u0086\u0002\u001a\u0015\u0010<\u001a\u00020\n*\u00020\n2\u0006\u0010>\u001a\u00020\nH\u0086\u0002\u001a\u001b\u0010<\u001a\u00020\n*\u00020\n2\f\u0010>\u001a\b\u0012\u0004\u0012\u00020\t0@H\u0086\u0002\u001a\u0015\u0010<\u001a\u00020\f*\u00020\f2\u0006\u0010\u0016\u001a\u00020\u000bH\u0086\u0002\u001a\u0015\u0010<\u001a\u00020\f*\u00020\f2\u0006\u0010>\u001a\u00020\fH\u0086\u0002\u001a\u001b\u0010<\u001a\u00020\f*\u00020\f2\f\u0010>\u001a\b\u0012\u0004\u0012\u00020\u000b0@H\u0086\u0002\u001a\u0015\u0010<\u001a\u00020\u000e*\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\rH\u0086\u0002\u001a\u0015\u0010<\u001a\u00020\u000e*\u00020\u000e2\u0006\u0010>\u001a\u00020\u000eH\u0086\u0002\u001a\u001b\u0010<\u001a\u00020\u000e*\u00020\u000e2\f\u0010>\u001a\b\u0012\u0004\u0012\u00020\r0@H\u0086\u0002\u001a\u0015\u0010<\u001a\u00020\u0010*\u00020\u00102\u0006\u0010\u0016\u001a\u00020\u000fH\u0086\u0002\u001a\u0015\u0010<\u001a\u00020\u0010*\u00020\u00102\u0006\u0010>\u001a\u00020\u0010H\u0086\u0002\u001a\u001b\u0010<\u001a\u00020\u0010*\u00020\u00102\f\u0010>\u001a\b\u0012\u0004\u0012\u00020\u000f0@H\u0086\u0002\u001a\u0015\u0010<\u001a\u00020\u0012*\u00020\u00122\u0006\u0010\u0016\u001a\u00020\u0011H\u0086\u0002\u001a\u0015\u0010<\u001a\u00020\u0012*\u00020\u00122\u0006\u0010>\u001a\u00020\u0012H\u0086\u0002\u001a\u001b\u0010<\u001a\u00020\u0012*\u00020\u00122\f\u0010>\u001a\b\u0012\u0004\u0012\u00020\u00110@H\u0086\u0002\u001a\u0015\u0010<\u001a\u00020\u0014*\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u0013H\u0086\u0002\u001a\u0015\u0010<\u001a\u00020\u0014*\u00020\u00142\u0006\u0010>\u001a\u00020\u0014H\u0086\u0002\u001a\u001b\u0010<\u001a\u00020\u0014*\u00020\u00142\f\u0010>\u001a\b\u0012\u0004\u0012\u00020\u00130@H\u0086\u0002\u001a,\u0010B\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0016\u001a\u0002H\u0002H\u0087\b¢\u0006\u0002\u0010=\u001a\u001d\u0010C\u001a\u000200\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003¢\u0006\u0002\u0010D\u001a*\u0010C\u001a\u000200\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020E*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\u0087\b¢\u0006\u0002\u0010F\u001a1\u0010C\u001a\u000200\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f¢\u0006\u0002\u0010G\u001a\n\u0010C\u001a\u000200*\u00020\b\u001a\u001e\u0010C\u001a\u000200*\u00020\b2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a\n\u0010C\u001a\u000200*\u00020\n\u001a\u001e\u0010C\u001a\u000200*\u00020\n2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a\n\u0010C\u001a\u000200*\u00020\f\u001a\u001e\u0010C\u001a\u000200*\u00020\f2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a\n\u0010C\u001a\u000200*\u00020\u000e\u001a\u001e\u0010C\u001a\u000200*\u00020\u000e2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a\n\u0010C\u001a\u000200*\u00020\u0010\u001a\u001e\u0010C\u001a\u000200*\u00020\u00102\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a\n\u0010C\u001a\u000200*\u00020\u0012\u001a\u001e\u0010C\u001a\u000200*\u00020\u00122\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a\n\u0010C\u001a\u000200*\u00020\u0014\u001a\u001e\u0010C\u001a\u000200*\u00020\u00142\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a9\u0010H\u001a\u000200\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u001a\u0010\u0017\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0018j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0019¢\u0006\u0002\u0010I\u001aM\u0010H\u001a\u000200\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u001a\u0010\u0017\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0018j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f¢\u0006\u0002\u0010J\u001a-\u0010K\u001a\b\u0012\u0004\u0012\u0002H\u00020L\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020E*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003¢\u0006\u0002\u0010M\u001a?\u0010K\u001a\b\u0012\u0004\u0012\u0002H\u00020L\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u001a\u0010\u0017\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0018j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0019¢\u0006\u0002\u0010N\u001a\u0010\u0010K\u001a\b\u0012\u0004\u0012\u00020\u00050L*\u00020\u0006\u001a\u0010\u0010K\u001a\b\u0012\u0004\u0012\u00020\u00070L*\u00020\b\u001a\u0010\u0010K\u001a\b\u0012\u0004\u0012\u00020\t0L*\u00020\n\u001a\u0010\u0010K\u001a\b\u0012\u0004\u0012\u00020\u000b0L*\u00020\f\u001a\u0010\u0010K\u001a\b\u0012\u0004\u0012\u00020\r0L*\u00020\u000e\u001a\u0010\u0010K\u001a\b\u0012\u0004\u0012\u00020\u000f0L*\u00020\u0010\u001a\u0010\u0010K\u001a\b\u0012\u0004\u0012\u00020\u00110L*\u00020\u0012\u001a\u0010\u0010K\u001a\b\u0012\u0004\u0012\u00020\u00130L*\u00020\u0014\u001a\u0015\u0010O\u001a\b\u0012\u0004\u0012\u00020\u00050\u0003*\u00020\u0006¢\u0006\u0002\u0010P\u001a\u0015\u0010O\u001a\b\u0012\u0004\u0012\u00020\u00070\u0003*\u00020\b¢\u0006\u0002\u0010Q\u001a\u0015\u0010O\u001a\b\u0012\u0004\u0012\u00020\t0\u0003*\u00020\n¢\u0006\u0002\u0010R\u001a\u0015\u0010O\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0003*\u00020\f¢\u0006\u0002\u0010S\u001a\u0015\u0010O\u001a\b\u0012\u0004\u0012\u00020\r0\u0003*\u00020\u000e¢\u0006\u0002\u0010T\u001a\u0015\u0010O\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0003*\u00020\u0010¢\u0006\u0002\u0010U\u001a\u0015\u0010O\u001a\b\u0012\u0004\u0012\u00020\u00110\u0003*\u00020\u0012¢\u0006\u0002\u0010V\u001a\u0015\u0010O\u001a\b\u0012\u0004\u0012\u00020\u00130\u0003*\u00020\u0014¢\u0006\u0002\u0010W¨\u0006X" }, d2 = { "asList", "", "T", "", "([Ljava/lang/Object;)Ljava/util/List;", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "binarySearch", "element", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "fromIndex", "toIndex", "([Ljava/lang/Object;Ljava/lang/Object;Ljava/util/Comparator;II)I", "([Ljava/lang/Object;Ljava/lang/Object;II)I", "contentDeepEquals", "other", "([Ljava/lang/Object;[Ljava/lang/Object;)Z", "contentDeepHashCode", "([Ljava/lang/Object;)I", "contentDeepToString", "", "([Ljava/lang/Object;)Ljava/lang/String;", "contentEquals", "contentHashCode", "contentToString", "copyOf", "([Ljava/lang/Object;)[Ljava/lang/Object;", "newSize", "([Ljava/lang/Object;I)[Ljava/lang/Object;", "copyOfRange", "([Ljava/lang/Object;II)[Ljava/lang/Object;", "fill", "", "([Ljava/lang/Object;Ljava/lang/Object;II)V", "filterIsInstance", "R", "klass", "Ljava/lang/Class;", "([Ljava/lang/Object;Ljava/lang/Class;)Ljava/util/List;", "filterIsInstanceTo", "C", "", "destination", "([Ljava/lang/Object;Ljava/util/Collection;Ljava/lang/Class;)Ljava/util/Collection;", "plus", "([Ljava/lang/Object;Ljava/lang/Object;)[Ljava/lang/Object;", "elements", "([Ljava/lang/Object;[Ljava/lang/Object;)[Ljava/lang/Object;", "", "([Ljava/lang/Object;Ljava/util/Collection;)[Ljava/lang/Object;", "plusElement", "sort", "([Ljava/lang/Object;)V", "", "([Ljava/lang/Comparable;)V", "([Ljava/lang/Object;II)V", "sortWith", "([Ljava/lang/Object;Ljava/util/Comparator;)V", "([Ljava/lang/Object;Ljava/util/Comparator;II)V", "toSortedSet", "Ljava/util/SortedSet;", "([Ljava/lang/Comparable;)Ljava/util/SortedSet;", "([Ljava/lang/Object;Ljava/util/Comparator;)Ljava/util/SortedSet;", "toTypedArray", "([Z)[Ljava/lang/Boolean;", "([B)[Ljava/lang/Byte;", "([C)[Ljava/lang/Character;", "([D)[Ljava/lang/Double;", "([F)[Ljava/lang/Float;", "([I)[Ljava/lang/Integer;", "([J)[Ljava/lang/Long;", "([S)[Ljava/lang/Short;", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/collections/ArraysKt")
class ArraysKt___ArraysJvmKt extends ArraysKt__ArraysKt
{
    public ArraysKt___ArraysJvmKt() {
    }
    
    @NotNull
    public static final List<Byte> asList(@NotNull final byte[] array) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        return (List<Byte>)new ArraysKt___ArraysJvmKt$asList.ArraysKt___ArraysJvmKt$asList$1(array);
    }
    
    @NotNull
    public static final List<Character> asList(@NotNull final char[] array) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        return (List<Character>)new ArraysKt___ArraysJvmKt$asList.ArraysKt___ArraysJvmKt$asList$8(array);
    }
    
    @NotNull
    public static final List<Double> asList(@NotNull final double[] array) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        return (List<Double>)new ArraysKt___ArraysJvmKt$asList.ArraysKt___ArraysJvmKt$asList$6(array);
    }
    
    @NotNull
    public static final List<Float> asList(@NotNull final float[] array) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        return (List<Float>)new ArraysKt___ArraysJvmKt$asList.ArraysKt___ArraysJvmKt$asList$5(array);
    }
    
    @NotNull
    public static final List<Integer> asList(@NotNull final int[] array) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        return (List<Integer>)new ArraysKt___ArraysJvmKt$asList.ArraysKt___ArraysJvmKt$asList$3(array);
    }
    
    @NotNull
    public static final List<Long> asList(@NotNull final long[] array) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        return (List<Long>)new ArraysKt___ArraysJvmKt$asList.ArraysKt___ArraysJvmKt$asList$4(array);
    }
    
    @NotNull
    public static final <T> List<T> asList(@NotNull final T[] array) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        final List<T> list = ArraysUtilJVM.asList(array);
        Intrinsics.checkExpressionValueIsNotNull(list, "ArraysUtilJVM.asList(this)");
        return list;
    }
    
    @NotNull
    public static final List<Short> asList(@NotNull final short[] array) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        return (List<Short>)new ArraysKt___ArraysJvmKt$asList.ArraysKt___ArraysJvmKt$asList$2(array);
    }
    
    @NotNull
    public static final List<Boolean> asList(@NotNull final boolean[] array) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        return (List<Boolean>)new ArraysKt___ArraysJvmKt$asList.ArraysKt___ArraysJvmKt$asList$7(array);
    }
    
    public static final int binarySearch(@NotNull final byte[] a, final byte key, final int fromIndex, final int toIndex) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        return Arrays.binarySearch(a, fromIndex, toIndex, key);
    }
    
    public static final int binarySearch(@NotNull final char[] a, final char key, final int fromIndex, final int toIndex) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        return Arrays.binarySearch(a, fromIndex, toIndex, key);
    }
    
    public static final int binarySearch(@NotNull final double[] a, final double key, final int fromIndex, final int toIndex) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        return Arrays.binarySearch(a, fromIndex, toIndex, key);
    }
    
    public static final int binarySearch(@NotNull final float[] a, final float key, final int fromIndex, final int toIndex) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        return Arrays.binarySearch(a, fromIndex, toIndex, key);
    }
    
    public static final int binarySearch(@NotNull final int[] a, final int key, final int fromIndex, final int toIndex) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        return Arrays.binarySearch(a, fromIndex, toIndex, key);
    }
    
    public static final int binarySearch(@NotNull final long[] a, final long key, final int fromIndex, final int toIndex) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        return Arrays.binarySearch(a, fromIndex, toIndex, key);
    }
    
    public static final <T> int binarySearch(@NotNull final T[] a, final T key, final int fromIndex, final int toIndex) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        return Arrays.binarySearch(a, fromIndex, toIndex, key);
    }
    
    public static final <T> int binarySearch(@NotNull final T[] a, final T key, @NotNull final Comparator<? super T> c, final int fromIndex, final int toIndex) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        Intrinsics.checkParameterIsNotNull(c, "comparator");
        return Arrays.binarySearch(a, fromIndex, toIndex, key, c);
    }
    
    public static final int binarySearch(@NotNull final short[] a, final short key, final int fromIndex, final int toIndex) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        return Arrays.binarySearch(a, fromIndex, toIndex, key);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> boolean contentDeepEquals(@NotNull final T[] a1, final T[] a2) {
        return Arrays.deepEquals(a1, a2);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> int contentDeepHashCode(@NotNull final T[] a) {
        return Arrays.deepHashCode(a);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> String contentDeepToString(@NotNull final T[] a) {
        final String deepToString = Arrays.deepToString(a);
        Intrinsics.checkExpressionValueIsNotNull(deepToString, "java.util.Arrays.deepToString(this)");
        return deepToString;
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull final byte[] a, final byte[] a2) {
        return Arrays.equals(a, a2);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull final char[] a, final char[] a2) {
        return Arrays.equals(a, a2);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull final double[] a, final double[] a2) {
        return Arrays.equals(a, a2);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull final float[] a, final float[] a2) {
        return Arrays.equals(a, a2);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull final int[] a, final int[] a2) {
        return Arrays.equals(a, a2);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull final long[] a, final long[] a2) {
        return Arrays.equals(a, a2);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> boolean contentEquals(@NotNull final T[] a, final T[] a2) {
        return Arrays.equals(a, a2);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull final short[] a, final short[] a2) {
        return Arrays.equals(a, a2);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull final boolean[] a, final boolean[] a2) {
        return Arrays.equals(a, a2);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull final byte[] a) {
        return Arrays.hashCode(a);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull final char[] a) {
        return Arrays.hashCode(a);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull final double[] a) {
        return Arrays.hashCode(a);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull final float[] a) {
        return Arrays.hashCode(a);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull final int[] a) {
        return Arrays.hashCode(a);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull final long[] a) {
        return Arrays.hashCode(a);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> int contentHashCode(@NotNull final T[] a) {
        return Arrays.hashCode(a);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull final short[] a) {
        return Arrays.hashCode(a);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull final boolean[] a) {
        return Arrays.hashCode(a);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull final byte[] a) {
        final String string = Arrays.toString(a);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.util.Arrays.toString(this)");
        return string;
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull final char[] a) {
        final String string = Arrays.toString(a);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.util.Arrays.toString(this)");
        return string;
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull final double[] a) {
        final String string = Arrays.toString(a);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.util.Arrays.toString(this)");
        return string;
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull final float[] a) {
        final String string = Arrays.toString(a);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.util.Arrays.toString(this)");
        return string;
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull final int[] a) {
        final String string = Arrays.toString(a);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.util.Arrays.toString(this)");
        return string;
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull final long[] a) {
        final String string = Arrays.toString(a);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.util.Arrays.toString(this)");
        return string;
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> String contentToString(@NotNull final T[] a) {
        final String string = Arrays.toString(a);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.util.Arrays.toString(this)");
        return string;
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull final short[] a) {
        final String string = Arrays.toString(a);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.util.Arrays.toString(this)");
        return string;
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull final boolean[] a) {
        final String string = Arrays.toString(a);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.util.Arrays.toString(this)");
        return string;
    }
    
    @InlineOnly
    private static final byte[] copyOf(@NotNull byte[] copy) {
        copy = Arrays.copyOf(copy, copy.length);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, size)");
        return copy;
    }
    
    @InlineOnly
    private static final byte[] copyOf(@NotNull byte[] copy, final int newLength) {
        copy = Arrays.copyOf(copy, newLength);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, newSize)");
        return copy;
    }
    
    @InlineOnly
    private static final char[] copyOf(@NotNull char[] copy) {
        copy = Arrays.copyOf(copy, copy.length);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, size)");
        return copy;
    }
    
    @InlineOnly
    private static final char[] copyOf(@NotNull char[] copy, final int newLength) {
        copy = Arrays.copyOf(copy, newLength);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, newSize)");
        return copy;
    }
    
    @InlineOnly
    private static final double[] copyOf(@NotNull double[] copy) {
        copy = Arrays.copyOf(copy, copy.length);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, size)");
        return copy;
    }
    
    @InlineOnly
    private static final double[] copyOf(@NotNull double[] copy, final int newLength) {
        copy = Arrays.copyOf(copy, newLength);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, newSize)");
        return copy;
    }
    
    @InlineOnly
    private static final float[] copyOf(@NotNull float[] copy) {
        copy = Arrays.copyOf(copy, copy.length);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, size)");
        return copy;
    }
    
    @InlineOnly
    private static final float[] copyOf(@NotNull float[] copy, final int newLength) {
        copy = Arrays.copyOf(copy, newLength);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, newSize)");
        return copy;
    }
    
    @InlineOnly
    private static final int[] copyOf(@NotNull int[] copy) {
        copy = Arrays.copyOf(copy, copy.length);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, size)");
        return copy;
    }
    
    @InlineOnly
    private static final int[] copyOf(@NotNull int[] copy, final int newLength) {
        copy = Arrays.copyOf(copy, newLength);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, newSize)");
        return copy;
    }
    
    @InlineOnly
    private static final long[] copyOf(@NotNull long[] copy) {
        copy = Arrays.copyOf(copy, copy.length);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, size)");
        return copy;
    }
    
    @InlineOnly
    private static final long[] copyOf(@NotNull long[] copy, final int newLength) {
        copy = Arrays.copyOf(copy, newLength);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, newSize)");
        return copy;
    }
    
    @InlineOnly
    private static final <T> T[] copyOf(@NotNull final T[] original) {
        final T[] copy = Arrays.copyOf(original, original.length);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, size)");
        return copy;
    }
    
    @InlineOnly
    private static final <T> T[] copyOf(@NotNull final T[] original, final int newLength) {
        final T[] copy = Arrays.copyOf(original, newLength);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, newSize)");
        return copy;
    }
    
    @InlineOnly
    private static final short[] copyOf(@NotNull short[] copy) {
        copy = Arrays.copyOf(copy, copy.length);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, size)");
        return copy;
    }
    
    @InlineOnly
    private static final short[] copyOf(@NotNull short[] copy, final int newLength) {
        copy = Arrays.copyOf(copy, newLength);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, newSize)");
        return copy;
    }
    
    @InlineOnly
    private static final boolean[] copyOf(@NotNull boolean[] copy) {
        copy = Arrays.copyOf(copy, copy.length);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, size)");
        return copy;
    }
    
    @InlineOnly
    private static final boolean[] copyOf(@NotNull boolean[] copy, final int newLength) {
        copy = Arrays.copyOf(copy, newLength);
        Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, newSize)");
        return copy;
    }
    
    @InlineOnly
    private static final byte[] copyOfRange(@NotNull byte[] copyOfRange, final int from, final int to) {
        copyOfRange = Arrays.copyOfRange(copyOfRange, from, to);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR\u2026this, fromIndex, toIndex)");
        return copyOfRange;
    }
    
    @InlineOnly
    private static final char[] copyOfRange(@NotNull char[] copyOfRange, final int from, final int to) {
        copyOfRange = Arrays.copyOfRange(copyOfRange, from, to);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR\u2026this, fromIndex, toIndex)");
        return copyOfRange;
    }
    
    @InlineOnly
    private static final double[] copyOfRange(@NotNull double[] copyOfRange, final int from, final int to) {
        copyOfRange = Arrays.copyOfRange(copyOfRange, from, to);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR\u2026this, fromIndex, toIndex)");
        return copyOfRange;
    }
    
    @InlineOnly
    private static final float[] copyOfRange(@NotNull float[] copyOfRange, final int from, final int to) {
        copyOfRange = Arrays.copyOfRange(copyOfRange, from, to);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR\u2026this, fromIndex, toIndex)");
        return copyOfRange;
    }
    
    @InlineOnly
    private static final int[] copyOfRange(@NotNull int[] copyOfRange, final int from, final int to) {
        copyOfRange = Arrays.copyOfRange(copyOfRange, from, to);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR\u2026this, fromIndex, toIndex)");
        return copyOfRange;
    }
    
    @InlineOnly
    private static final long[] copyOfRange(@NotNull long[] copyOfRange, final int from, final int to) {
        copyOfRange = Arrays.copyOfRange(copyOfRange, from, to);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR\u2026this, fromIndex, toIndex)");
        return copyOfRange;
    }
    
    @InlineOnly
    private static final <T> T[] copyOfRange(@NotNull final T[] original, final int from, final int to) {
        final T[] copyOfRange = Arrays.copyOfRange(original, from, to);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR\u2026this, fromIndex, toIndex)");
        return copyOfRange;
    }
    
    @InlineOnly
    private static final short[] copyOfRange(@NotNull short[] copyOfRange, final int from, final int to) {
        copyOfRange = Arrays.copyOfRange(copyOfRange, from, to);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR\u2026this, fromIndex, toIndex)");
        return copyOfRange;
    }
    
    @InlineOnly
    private static final boolean[] copyOfRange(@NotNull boolean[] copyOfRange, final int from, final int to) {
        copyOfRange = Arrays.copyOfRange(copyOfRange, from, to);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR\u2026this, fromIndex, toIndex)");
        return copyOfRange;
    }
    
    public static final void fill(@NotNull final byte[] a, final byte val, final int fromIndex, final int toIndex) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        Arrays.fill(a, fromIndex, toIndex, val);
    }
    
    public static final void fill(@NotNull final char[] a, final char val, final int fromIndex, final int toIndex) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        Arrays.fill(a, fromIndex, toIndex, val);
    }
    
    public static final void fill(@NotNull final double[] a, final double val, final int fromIndex, final int toIndex) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        Arrays.fill(a, fromIndex, toIndex, val);
    }
    
    public static final void fill(@NotNull final float[] a, final float val, final int fromIndex, final int toIndex) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        Arrays.fill(a, fromIndex, toIndex, val);
    }
    
    public static final void fill(@NotNull final int[] a, final int val, final int fromIndex, final int toIndex) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        Arrays.fill(a, fromIndex, toIndex, val);
    }
    
    public static final void fill(@NotNull final long[] a, final long val, final int fromIndex, final int toIndex) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        Arrays.fill(a, fromIndex, toIndex, val);
    }
    
    public static final <T> void fill(@NotNull final T[] a, final T val, final int fromIndex, final int toIndex) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        Arrays.fill(a, fromIndex, toIndex, val);
    }
    
    public static final void fill(@NotNull final short[] a, final short val, final int fromIndex, final int toIndex) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        Arrays.fill(a, fromIndex, toIndex, val);
    }
    
    public static final void fill(@NotNull final boolean[] a, final boolean val, final int fromIndex, final int toIndex) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        Arrays.fill(a, fromIndex, toIndex, val);
    }
    
    @NotNull
    public static final <R> List<R> filterIsInstance(@NotNull final Object[] array, @NotNull final Class<R> clazz) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        Intrinsics.checkParameterIsNotNull(clazz, "klass");
        return filterIsInstanceTo(array, (List<R>)new ArrayList(), clazz);
    }
    
    @NotNull
    public static final <C extends Collection<? super R>, R> C filterIsInstanceTo(@NotNull final Object[] array, @NotNull final C c, @NotNull final Class<R> clazz) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        Intrinsics.checkParameterIsNotNull(c, "destination");
        Intrinsics.checkParameterIsNotNull(clazz, "klass");
        for (final Object o : array) {
            if (clazz.isInstance(o)) {
                c.add((Object)o);
            }
        }
        return c;
    }
    
    @NotNull
    public static final byte[] plus(@NotNull byte[] copy, final byte b) {
        Intrinsics.checkParameterIsNotNull(copy, "$receiver");
        final int length = copy.length;
        copy = Arrays.copyOf(copy, length + 1);
        copy[length] = b;
        Intrinsics.checkExpressionValueIsNotNull(copy, "result");
        return copy;
    }
    
    @NotNull
    public static final byte[] plus(@NotNull byte[] copy, @NotNull final Collection<Byte> collection) {
        Intrinsics.checkParameterIsNotNull(copy, "$receiver");
        Intrinsics.checkParameterIsNotNull(collection, "elements");
        int length = copy.length;
        copy = Arrays.copyOf(copy, collection.size() + length);
        final Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            copy[length] = ((Number)iterator.next()).byteValue();
            ++length;
        }
        Intrinsics.checkExpressionValueIsNotNull(copy, "result");
        return copy;
    }
    
    @NotNull
    public static final byte[] plus(@NotNull byte[] copy, @NotNull final byte[] array) {
        Intrinsics.checkParameterIsNotNull(copy, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "elements");
        final int length = copy.length;
        final int length2 = array.length;
        copy = Arrays.copyOf(copy, length + length2);
        System.arraycopy(array, 0, copy, length, length2);
        Intrinsics.checkExpressionValueIsNotNull(copy, "result");
        return copy;
    }
    
    @NotNull
    public static final char[] plus(@NotNull char[] copy, final char c) {
        Intrinsics.checkParameterIsNotNull(copy, "$receiver");
        final int length = copy.length;
        copy = Arrays.copyOf(copy, length + 1);
        copy[length] = c;
        Intrinsics.checkExpressionValueIsNotNull(copy, "result");
        return copy;
    }
    
    @NotNull
    public static final char[] plus(@NotNull char[] copy, @NotNull final Collection<Character> collection) {
        Intrinsics.checkParameterIsNotNull(copy, "$receiver");
        Intrinsics.checkParameterIsNotNull(collection, "elements");
        int length = copy.length;
        copy = Arrays.copyOf(copy, collection.size() + length);
        final Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            copy[length] = (char)iterator.next();
            ++length;
        }
        Intrinsics.checkExpressionValueIsNotNull(copy, "result");
        return copy;
    }
    
    @NotNull
    public static final char[] plus(@NotNull char[] copy, @NotNull final char[] array) {
        Intrinsics.checkParameterIsNotNull(copy, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "elements");
        final int length = copy.length;
        final int length2 = array.length;
        copy = Arrays.copyOf(copy, length + length2);
        System.arraycopy(array, 0, copy, length, length2);
        Intrinsics.checkExpressionValueIsNotNull(copy, "result");
        return copy;
    }
    
    @NotNull
    public static final double[] plus(@NotNull double[] copy, final double n) {
        Intrinsics.checkParameterIsNotNull(copy, "$receiver");
        final int length = copy.length;
        copy = Arrays.copyOf(copy, length + 1);
        copy[length] = n;
        Intrinsics.checkExpressionValueIsNotNull(copy, "result");
        return copy;
    }
    
    @NotNull
    public static final double[] plus(@NotNull double[] copy, @NotNull final Collection<Double> collection) {
        Intrinsics.checkParameterIsNotNull(copy, "$receiver");
        Intrinsics.checkParameterIsNotNull(collection, "elements");
        int length = copy.length;
        copy = Arrays.copyOf(copy, collection.size() + length);
        final Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            copy[length] = ((Number)iterator.next()).doubleValue();
            ++length;
        }
        Intrinsics.checkExpressionValueIsNotNull(copy, "result");
        return copy;
    }
    
    @NotNull
    public static final double[] plus(@NotNull double[] copy, @NotNull final double[] array) {
        Intrinsics.checkParameterIsNotNull(copy, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "elements");
        final int length = copy.length;
        final int length2 = array.length;
        copy = Arrays.copyOf(copy, length + length2);
        System.arraycopy(array, 0, copy, length, length2);
        Intrinsics.checkExpressionValueIsNotNull(copy, "result");
        return copy;
    }
    
    @NotNull
    public static final float[] plus(@NotNull float[] copy, final float n) {
        Intrinsics.checkParameterIsNotNull(copy, "$receiver");
        final int length = copy.length;
        copy = Arrays.copyOf(copy, length + 1);
        copy[length] = n;
        Intrinsics.checkExpressionValueIsNotNull(copy, "result");
        return copy;
    }
    
    @NotNull
    public static final float[] plus(@NotNull float[] copy, @NotNull final Collection<Float> collection) {
        Intrinsics.checkParameterIsNotNull(copy, "$receiver");
        Intrinsics.checkParameterIsNotNull(collection, "elements");
        int length = copy.length;
        copy = Arrays.copyOf(copy, collection.size() + length);
        final Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            copy[length] = ((Number)iterator.next()).floatValue();
            ++length;
        }
        Intrinsics.checkExpressionValueIsNotNull(copy, "result");
        return copy;
    }
    
    @NotNull
    public static final float[] plus(@NotNull float[] copy, @NotNull final float[] array) {
        Intrinsics.checkParameterIsNotNull(copy, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "elements");
        final int length = copy.length;
        final int length2 = array.length;
        copy = Arrays.copyOf(copy, length + length2);
        System.arraycopy(array, 0, copy, length, length2);
        Intrinsics.checkExpressionValueIsNotNull(copy, "result");
        return copy;
    }
    
    @NotNull
    public static final int[] plus(@NotNull int[] copy, final int n) {
        Intrinsics.checkParameterIsNotNull(copy, "$receiver");
        final int length = copy.length;
        copy = Arrays.copyOf(copy, length + 1);
        copy[length] = n;
        Intrinsics.checkExpressionValueIsNotNull(copy, "result");
        return copy;
    }
    
    @NotNull
    public static final int[] plus(@NotNull int[] copy, @NotNull final Collection<Integer> collection) {
        Intrinsics.checkParameterIsNotNull(copy, "$receiver");
        Intrinsics.checkParameterIsNotNull(collection, "elements");
        int length = copy.length;
        copy = Arrays.copyOf(copy, collection.size() + length);
        final Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            copy[length] = ((Number)iterator.next()).intValue();
            ++length;
        }
        Intrinsics.checkExpressionValueIsNotNull(copy, "result");
        return copy;
    }
    
    @NotNull
    public static final int[] plus(@NotNull int[] copy, @NotNull final int[] array) {
        Intrinsics.checkParameterIsNotNull(copy, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "elements");
        final int length = copy.length;
        final int length2 = array.length;
        copy = Arrays.copyOf(copy, length + length2);
        System.arraycopy(array, 0, copy, length, length2);
        Intrinsics.checkExpressionValueIsNotNull(copy, "result");
        return copy;
    }
    
    @NotNull
    public static final long[] plus(@NotNull long[] copy, final long n) {
        Intrinsics.checkParameterIsNotNull(copy, "$receiver");
        final int length = copy.length;
        copy = Arrays.copyOf(copy, length + 1);
        copy[length] = n;
        Intrinsics.checkExpressionValueIsNotNull(copy, "result");
        return copy;
    }
    
    @NotNull
    public static final long[] plus(@NotNull long[] copy, @NotNull final Collection<Long> collection) {
        Intrinsics.checkParameterIsNotNull(copy, "$receiver");
        Intrinsics.checkParameterIsNotNull(collection, "elements");
        int length = copy.length;
        copy = Arrays.copyOf(copy, collection.size() + length);
        final Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            copy[length] = ((Number)iterator.next()).longValue();
            ++length;
        }
        Intrinsics.checkExpressionValueIsNotNull(copy, "result");
        return copy;
    }
    
    @NotNull
    public static final long[] plus(@NotNull long[] copy, @NotNull final long[] array) {
        Intrinsics.checkParameterIsNotNull(copy, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "elements");
        final int length = copy.length;
        final int length2 = array.length;
        copy = Arrays.copyOf(copy, length + length2);
        System.arraycopy(array, 0, copy, length, length2);
        Intrinsics.checkExpressionValueIsNotNull(copy, "result");
        return copy;
    }
    
    @NotNull
    public static final <T> T[] plus(@NotNull final T[] original, final T t) {
        Intrinsics.checkParameterIsNotNull(original, "$receiver");
        final int length = original.length;
        final T[] copy = Arrays.copyOf(original, length + 1);
        copy[length] = t;
        Intrinsics.checkExpressionValueIsNotNull(copy, "result");
        return copy;
    }
    
    @NotNull
    public static final <T> T[] plus(@NotNull final T[] original, @NotNull final Collection<? extends T> collection) {
        Intrinsics.checkParameterIsNotNull(original, "$receiver");
        Intrinsics.checkParameterIsNotNull(collection, "elements");
        int length = original.length;
        final T[] copy = Arrays.copyOf(original, collection.size() + length);
        final Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            copy[length] = (T)iterator.next();
            ++length;
        }
        Intrinsics.checkExpressionValueIsNotNull(copy, "result");
        return copy;
    }
    
    @NotNull
    public static final <T> T[] plus(@NotNull final T[] original, @NotNull final T[] array) {
        Intrinsics.checkParameterIsNotNull(original, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "elements");
        final int length = original.length;
        final int length2 = array.length;
        final T[] copy = Arrays.copyOf(original, length + length2);
        System.arraycopy(array, 0, copy, length, length2);
        Intrinsics.checkExpressionValueIsNotNull(copy, "result");
        return copy;
    }
    
    @NotNull
    public static final short[] plus(@NotNull short[] copy, @NotNull final Collection<Short> collection) {
        Intrinsics.checkParameterIsNotNull(copy, "$receiver");
        Intrinsics.checkParameterIsNotNull(collection, "elements");
        int length = copy.length;
        copy = Arrays.copyOf(copy, collection.size() + length);
        final Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            copy[length] = ((Number)iterator.next()).shortValue();
            ++length;
        }
        Intrinsics.checkExpressionValueIsNotNull(copy, "result");
        return copy;
    }
    
    @NotNull
    public static final short[] plus(@NotNull short[] copy, final short n) {
        Intrinsics.checkParameterIsNotNull(copy, "$receiver");
        final int length = copy.length;
        copy = Arrays.copyOf(copy, length + 1);
        copy[length] = n;
        Intrinsics.checkExpressionValueIsNotNull(copy, "result");
        return copy;
    }
    
    @NotNull
    public static final short[] plus(@NotNull short[] copy, @NotNull final short[] array) {
        Intrinsics.checkParameterIsNotNull(copy, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "elements");
        final int length = copy.length;
        final int length2 = array.length;
        copy = Arrays.copyOf(copy, length + length2);
        System.arraycopy(array, 0, copy, length, length2);
        Intrinsics.checkExpressionValueIsNotNull(copy, "result");
        return copy;
    }
    
    @NotNull
    public static final boolean[] plus(@NotNull boolean[] copy, @NotNull final Collection<Boolean> collection) {
        Intrinsics.checkParameterIsNotNull(copy, "$receiver");
        Intrinsics.checkParameterIsNotNull(collection, "elements");
        int length = copy.length;
        copy = Arrays.copyOf(copy, collection.size() + length);
        final Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            copy[length] = (boolean)iterator.next();
            ++length;
        }
        Intrinsics.checkExpressionValueIsNotNull(copy, "result");
        return copy;
    }
    
    @NotNull
    public static final boolean[] plus(@NotNull boolean[] copy, final boolean b) {
        Intrinsics.checkParameterIsNotNull(copy, "$receiver");
        final int length = copy.length;
        copy = Arrays.copyOf(copy, length + 1);
        copy[length] = b;
        Intrinsics.checkExpressionValueIsNotNull(copy, "result");
        return copy;
    }
    
    @NotNull
    public static final boolean[] plus(@NotNull boolean[] copy, @NotNull final boolean[] array) {
        Intrinsics.checkParameterIsNotNull(copy, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "elements");
        final int length = copy.length;
        final int length2 = array.length;
        copy = Arrays.copyOf(copy, length + length2);
        System.arraycopy(array, 0, copy, length, length2);
        Intrinsics.checkExpressionValueIsNotNull(copy, "result");
        return copy;
    }
    
    @InlineOnly
    private static final <T> T[] plusElement(@NotNull final T[] array, final T t) {
        return (T[])plus(array, (Object)t);
    }
    
    public static final void sort(@NotNull final byte[] a) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        if (a.length > 1) {
            Arrays.sort(a);
        }
    }
    
    public static final void sort(@NotNull final byte[] a, final int fromIndex, final int toIndex) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        Arrays.sort(a, fromIndex, toIndex);
    }
    
    public static final void sort(@NotNull final char[] a) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        if (a.length > 1) {
            Arrays.sort(a);
        }
    }
    
    public static final void sort(@NotNull final char[] a, final int fromIndex, final int toIndex) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        Arrays.sort(a, fromIndex, toIndex);
    }
    
    public static final void sort(@NotNull final double[] a) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        if (a.length > 1) {
            Arrays.sort(a);
        }
    }
    
    public static final void sort(@NotNull final double[] a, final int fromIndex, final int toIndex) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        Arrays.sort(a, fromIndex, toIndex);
    }
    
    public static final void sort(@NotNull final float[] a) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        if (a.length > 1) {
            Arrays.sort(a);
        }
    }
    
    public static final void sort(@NotNull final float[] a, final int fromIndex, final int toIndex) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        Arrays.sort(a, fromIndex, toIndex);
    }
    
    public static final void sort(@NotNull final int[] a) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        if (a.length > 1) {
            Arrays.sort(a);
        }
    }
    
    public static final void sort(@NotNull final int[] a, final int fromIndex, final int toIndex) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        Arrays.sort(a, fromIndex, toIndex);
    }
    
    public static final void sort(@NotNull final long[] a) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        if (a.length > 1) {
            Arrays.sort(a);
        }
    }
    
    public static final void sort(@NotNull final long[] a, final int fromIndex, final int toIndex) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        Arrays.sort(a, fromIndex, toIndex);
    }
    
    @InlineOnly
    private static final <T extends Comparable<? super T>> void sort(@NotNull final T[] array) {
        if (array == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<kotlin.Any?>");
        }
        sort((Object[])array);
    }
    
    public static final <T> void sort(@NotNull final T[] a) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        if (a.length > 1) {
            Arrays.sort(a);
        }
    }
    
    public static final <T> void sort(@NotNull final T[] a, final int fromIndex, final int toIndex) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        Arrays.sort(a, fromIndex, toIndex);
    }
    
    public static final void sort(@NotNull final short[] a) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        if (a.length > 1) {
            Arrays.sort(a);
        }
    }
    
    public static final void sort(@NotNull final short[] a, final int fromIndex, final int toIndex) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        Arrays.sort(a, fromIndex, toIndex);
    }
    
    public static final <T> void sortWith(@NotNull final T[] a, @NotNull final Comparator<? super T> c) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        Intrinsics.checkParameterIsNotNull(c, "comparator");
        if (a.length > 1) {
            Arrays.sort(a, c);
        }
    }
    
    public static final <T> void sortWith(@NotNull final T[] a, @NotNull final Comparator<? super T> c, final int fromIndex, final int toIndex) {
        Intrinsics.checkParameterIsNotNull(a, "$receiver");
        Intrinsics.checkParameterIsNotNull(c, "comparator");
        Arrays.sort(a, fromIndex, toIndex, c);
    }
    
    @NotNull
    public static final SortedSet<Byte> toSortedSet(@NotNull final byte[] array) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        return ArraysKt___ArraysKt.toCollection(array, (SortedSet<Byte>)new TreeSet());
    }
    
    @NotNull
    public static final SortedSet<Character> toSortedSet(@NotNull final char[] array) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        return ArraysKt___ArraysKt.toCollection(array, (SortedSet<Character>)new TreeSet());
    }
    
    @NotNull
    public static final SortedSet<Double> toSortedSet(@NotNull final double[] array) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        return ArraysKt___ArraysKt.toCollection(array, (SortedSet<Double>)new TreeSet());
    }
    
    @NotNull
    public static final SortedSet<Float> toSortedSet(@NotNull final float[] array) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        return ArraysKt___ArraysKt.toCollection(array, (SortedSet<Float>)new TreeSet());
    }
    
    @NotNull
    public static final SortedSet<Integer> toSortedSet(@NotNull final int[] array) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        return ArraysKt___ArraysKt.toCollection(array, (SortedSet<Integer>)new TreeSet());
    }
    
    @NotNull
    public static final SortedSet<Long> toSortedSet(@NotNull final long[] array) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        return ArraysKt___ArraysKt.toCollection(array, (SortedSet<Long>)new TreeSet());
    }
    
    @NotNull
    public static final <T extends Comparable<? super T>> SortedSet<T> toSortedSet(@NotNull final T[] array) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        return ArraysKt___ArraysKt.toCollection(array, (SortedSet<T>)new TreeSet());
    }
    
    @NotNull
    public static final <T> SortedSet<T> toSortedSet(@NotNull final T[] array, @NotNull final Comparator<? super T> comparator) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        return ArraysKt___ArraysKt.toCollection(array, new Collection((Comparator<? super T>)comparator));
    }
    
    @NotNull
    public static final SortedSet<Short> toSortedSet(@NotNull final short[] array) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        return ArraysKt___ArraysKt.toCollection(array, (SortedSet<Short>)new TreeSet());
    }
    
    @NotNull
    public static final SortedSet<Boolean> toSortedSet(@NotNull final boolean[] array) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        return ArraysKt___ArraysKt.toCollection(array, (SortedSet<Boolean>)new TreeSet());
    }
    
    @NotNull
    public static final Boolean[] toTypedArray(@NotNull final boolean[] array) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        final Boolean[] array2 = new Boolean[array.length];
        for (int length = array.length, i = 0; i < length; ++i) {
            array2[i] = array[i];
        }
        return array2;
    }
    
    @NotNull
    public static final Byte[] toTypedArray(@NotNull final byte[] array) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        final Byte[] array2 = new Byte[array.length];
        for (int length = array.length, i = 0; i < length; ++i) {
            array2[i] = array[i];
        }
        return array2;
    }
    
    @NotNull
    public static final Character[] toTypedArray(@NotNull final char[] array) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        final Character[] array2 = new Character[array.length];
        for (int length = array.length, i = 0; i < length; ++i) {
            array2[i] = array[i];
        }
        return array2;
    }
    
    @NotNull
    public static final Double[] toTypedArray(@NotNull final double[] array) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        final Double[] array2 = new Double[array.length];
        for (int length = array.length, i = 0; i < length; ++i) {
            array2[i] = array[i];
        }
        return array2;
    }
    
    @NotNull
    public static final Float[] toTypedArray(@NotNull final float[] array) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        final Float[] array2 = new Float[array.length];
        for (int length = array.length, i = 0; i < length; ++i) {
            array2[i] = array[i];
        }
        return array2;
    }
    
    @NotNull
    public static final Integer[] toTypedArray(@NotNull final int[] array) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        final Integer[] array2 = new Integer[array.length];
        for (int length = array.length, i = 0; i < length; ++i) {
            array2[i] = array[i];
        }
        return array2;
    }
    
    @NotNull
    public static final Long[] toTypedArray(@NotNull final long[] array) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        final Long[] array2 = new Long[array.length];
        for (int length = array.length, i = 0; i < length; ++i) {
            array2[i] = array[i];
        }
        return array2;
    }
    
    @NotNull
    public static final Short[] toTypedArray(@NotNull final short[] array) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        final Short[] array2 = new Short[array.length];
        for (int length = array.length, i = 0; i < length; ++i) {
            array2[i] = array[i];
        }
        return array2;
    }
}
