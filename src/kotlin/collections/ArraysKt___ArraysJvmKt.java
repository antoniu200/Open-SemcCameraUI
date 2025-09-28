package kotlin.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;
import java.util.SortedSet;
import java.util.TreeSet;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.TypeCastException;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: _ArraysJvm.kt */
@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0096\u0001\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u000b\n\u0002\u0010\u0018\n\u0002\u0010\u0005\n\u0002\u0010\u0012\n\u0002\u0010\f\n\u0002\u0010\u0019\n\u0002\u0010\u0006\n\u0002\u0010\u0013\n\u0002\u0010\u0007\n\u0002\u0010\u0014\n\u0002\u0010\b\n\u0002\u0010\u0015\n\u0002\u0010\t\n\u0002\u0010\u0016\n\u0002\u0010\n\n\u0002\u0010\u0017\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000e\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u001f\n\u0002\b\u0006\n\u0002\u0010\u001e\n\u0002\b\u0004\n\u0002\u0010\u000f\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\f\u001a#\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003¢\u0006\u0002\u0010\u0004\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00050\u0001*\u00020\u0006\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00070\u0001*\u00020\b\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\t0\u0001*\u00020\n\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0001*\u00020\f\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\r0\u0001*\u00020\u000e\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0001*\u00020\u0010\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00110\u0001*\u00020\u0012\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00130\u0001*\u00020\u0014\u001aU\u0010\u0015\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u0006\u0010\u0016\u001a\u0002H\u00022\u001a\u0010\u0017\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0018j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f¢\u0006\u0002\u0010\u001c\u001a9\u0010\u0015\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u0006\u0010\u0016\u001a\u0002H\u00022\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f¢\u0006\u0002\u0010\u001d\u001a&\u0010\u0015\u001a\u00020\u000f*\u00020\b2\u0006\u0010\u0016\u001a\u00020\u00072\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010\u0015\u001a\u00020\u000f*\u00020\n2\u0006\u0010\u0016\u001a\u00020\t2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010\u0015\u001a\u00020\u000f*\u00020\f2\u0006\u0010\u0016\u001a\u00020\u000b2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010\u0015\u001a\u00020\u000f*\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\r2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010\u0015\u001a\u00020\u000f*\u00020\u00102\u0006\u0010\u0016\u001a\u00020\u000f2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010\u0015\u001a\u00020\u000f*\u00020\u00122\u0006\u0010\u0016\u001a\u00020\u00112\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010\u0015\u001a\u00020\u000f*\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u00132\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a0\u0010\u001e\u001a\u00020\u0005\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u000e\u0010\u001f\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\u0087\f¢\u0006\u0002\u0010 \u001a \u0010!\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\u0087\b¢\u0006\u0002\u0010\"\u001a \u0010#\u001a\u00020$\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\u0087\b¢\u0006\u0002\u0010%\u001a0\u0010&\u001a\u00020\u0005\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u000e\u0010\u001f\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\u0087\f¢\u0006\u0002\u0010 \u001a\u0015\u0010&\u001a\u00020\u0005*\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u0006H\u0087\f\u001a\u0015\u0010&\u001a\u00020\u0005*\u00020\b2\u0006\u0010\u001f\u001a\u00020\bH\u0087\f\u001a\u0015\u0010&\u001a\u00020\u0005*\u00020\n2\u0006\u0010\u001f\u001a\u00020\nH\u0087\f\u001a\u0015\u0010&\u001a\u00020\u0005*\u00020\f2\u0006\u0010\u001f\u001a\u00020\fH\u0087\f\u001a\u0015\u0010&\u001a\u00020\u0005*\u00020\u000e2\u0006\u0010\u001f\u001a\u00020\u000eH\u0087\f\u001a\u0015\u0010&\u001a\u00020\u0005*\u00020\u00102\u0006\u0010\u001f\u001a\u00020\u0010H\u0087\f\u001a\u0015\u0010&\u001a\u00020\u0005*\u00020\u00122\u0006\u0010\u001f\u001a\u00020\u0012H\u0087\f\u001a\u0015\u0010&\u001a\u00020\u0005*\u00020\u00142\u0006\u0010\u001f\u001a\u00020\u0014H\u0087\f\u001a \u0010'\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\u0087\b¢\u0006\u0002\u0010\"\u001a\r\u0010'\u001a\u00020\u000f*\u00020\u0006H\u0087\b\u001a\r\u0010'\u001a\u00020\u000f*\u00020\bH\u0087\b\u001a\r\u0010'\u001a\u00020\u000f*\u00020\nH\u0087\b\u001a\r\u0010'\u001a\u00020\u000f*\u00020\fH\u0087\b\u001a\r\u0010'\u001a\u00020\u000f*\u00020\u000eH\u0087\b\u001a\r\u0010'\u001a\u00020\u000f*\u00020\u0010H\u0087\b\u001a\r\u0010'\u001a\u00020\u000f*\u00020\u0012H\u0087\b\u001a\r\u0010'\u001a\u00020\u000f*\u00020\u0014H\u0087\b\u001a \u0010(\u001a\u00020$\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\u0087\b¢\u0006\u0002\u0010%\u001a\r\u0010(\u001a\u00020$*\u00020\u0006H\u0087\b\u001a\r\u0010(\u001a\u00020$*\u00020\bH\u0087\b\u001a\r\u0010(\u001a\u00020$*\u00020\nH\u0087\b\u001a\r\u0010(\u001a\u00020$*\u00020\fH\u0087\b\u001a\r\u0010(\u001a\u00020$*\u00020\u000eH\u0087\b\u001a\r\u0010(\u001a\u00020$*\u00020\u0010H\u0087\b\u001a\r\u0010(\u001a\u00020$*\u00020\u0012H\u0087\b\u001a\r\u0010(\u001a\u00020$*\u00020\u0014H\u0087\b\u001a$\u0010)\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0087\b¢\u0006\u0002\u0010*\u001a.\u0010)\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010+\u001a\u00020\u000fH\u0087\b¢\u0006\u0002\u0010,\u001a\r\u0010)\u001a\u00020\u0006*\u00020\u0006H\u0087\b\u001a\u0015\u0010)\u001a\u00020\u0006*\u00020\u00062\u0006\u0010+\u001a\u00020\u000fH\u0087\b\u001a\r\u0010)\u001a\u00020\b*\u00020\bH\u0087\b\u001a\u0015\u0010)\u001a\u00020\b*\u00020\b2\u0006\u0010+\u001a\u00020\u000fH\u0087\b\u001a\r\u0010)\u001a\u00020\n*\u00020\nH\u0087\b\u001a\u0015\u0010)\u001a\u00020\n*\u00020\n2\u0006\u0010+\u001a\u00020\u000fH\u0087\b\u001a\r\u0010)\u001a\u00020\f*\u00020\fH\u0087\b\u001a\u0015\u0010)\u001a\u00020\f*\u00020\f2\u0006\u0010+\u001a\u00020\u000fH\u0087\b\u001a\r\u0010)\u001a\u00020\u000e*\u00020\u000eH\u0087\b\u001a\u0015\u0010)\u001a\u00020\u000e*\u00020\u000e2\u0006\u0010+\u001a\u00020\u000fH\u0087\b\u001a\r\u0010)\u001a\u00020\u0010*\u00020\u0010H\u0087\b\u001a\u0015\u0010)\u001a\u00020\u0010*\u00020\u00102\u0006\u0010+\u001a\u00020\u000fH\u0087\b\u001a\r\u0010)\u001a\u00020\u0012*\u00020\u0012H\u0087\b\u001a\u0015\u0010)\u001a\u00020\u0012*\u00020\u00122\u0006\u0010+\u001a\u00020\u000fH\u0087\b\u001a\r\u0010)\u001a\u00020\u0014*\u00020\u0014H\u0087\b\u001a\u0015\u0010)\u001a\u00020\u0014*\u00020\u00142\u0006\u0010+\u001a\u00020\u000fH\u0087\b\u001a4\u0010-\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\u0087\b¢\u0006\u0002\u0010.\u001a\u001d\u0010-\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\u0087\b\u001a\u001d\u0010-\u001a\u00020\b*\u00020\b2\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\u0087\b\u001a\u001d\u0010-\u001a\u00020\n*\u00020\n2\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\u0087\b\u001a\u001d\u0010-\u001a\u00020\f*\u00020\f2\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\u0087\b\u001a\u001d\u0010-\u001a\u00020\u000e*\u00020\u000e2\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\u0087\b\u001a\u001d\u0010-\u001a\u00020\u0010*\u00020\u00102\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\u0087\b\u001a\u001d\u0010-\u001a\u00020\u0012*\u00020\u00122\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\u0087\b\u001a\u001d\u0010-\u001a\u00020\u0014*\u00020\u00142\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\u0087\b\u001a7\u0010/\u001a\u000200\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0016\u001a\u0002H\u00022\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f¢\u0006\u0002\u00101\u001a&\u0010/\u001a\u000200*\u00020\u00062\u0006\u0010\u0016\u001a\u00020\u00052\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010/\u001a\u000200*\u00020\b2\u0006\u0010\u0016\u001a\u00020\u00072\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010/\u001a\u000200*\u00020\n2\u0006\u0010\u0016\u001a\u00020\t2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010/\u001a\u000200*\u00020\f2\u0006\u0010\u0016\u001a\u00020\u000b2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010/\u001a\u000200*\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\r2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010/\u001a\u000200*\u00020\u00102\u0006\u0010\u0016\u001a\u00020\u000f2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010/\u001a\u000200*\u00020\u00122\u0006\u0010\u0016\u001a\u00020\u00112\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010/\u001a\u000200*\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u00132\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a-\u00102\u001a\b\u0012\u0004\u0012\u0002H30\u0001\"\u0004\b\u0000\u00103*\u0006\u0012\u0002\b\u00030\u00032\f\u00104\u001a\b\u0012\u0004\u0012\u0002H305¢\u0006\u0002\u00106\u001aA\u00107\u001a\u0002H8\"\u0010\b\u0000\u00108*\n\u0012\u0006\b\u0000\u0012\u0002H309\"\u0004\b\u0001\u00103*\u0006\u0012\u0002\b\u00030\u00032\u0006\u0010:\u001a\u0002H82\f\u00104\u001a\b\u0012\u0004\u0012\u0002H305¢\u0006\u0002\u0010;\u001a,\u0010<\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0016\u001a\u0002H\u0002H\u0086\u0002¢\u0006\u0002\u0010=\u001a4\u0010<\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u000e\u0010>\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\u0086\u0002¢\u0006\u0002\u0010?\u001a2\u0010<\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\f\u0010>\u001a\b\u0012\u0004\u0012\u0002H\u00020@H\u0086\u0002¢\u0006\u0002\u0010A\u001a\u0015\u0010<\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u0016\u001a\u00020\u0005H\u0086\u0002\u001a\u0015\u0010<\u001a\u00020\u0006*\u00020\u00062\u0006\u0010>\u001a\u00020\u0006H\u0086\u0002\u001a\u001b\u0010<\u001a\u00020\u0006*\u00020\u00062\f\u0010>\u001a\b\u0012\u0004\u0012\u00020\u00050@H\u0086\u0002\u001a\u0015\u0010<\u001a\u00020\b*\u00020\b2\u0006\u0010\u0016\u001a\u00020\u0007H\u0086\u0002\u001a\u0015\u0010<\u001a\u00020\b*\u00020\b2\u0006\u0010>\u001a\u00020\bH\u0086\u0002\u001a\u001b\u0010<\u001a\u00020\b*\u00020\b2\f\u0010>\u001a\b\u0012\u0004\u0012\u00020\u00070@H\u0086\u0002\u001a\u0015\u0010<\u001a\u00020\n*\u00020\n2\u0006\u0010\u0016\u001a\u00020\tH\u0086\u0002\u001a\u0015\u0010<\u001a\u00020\n*\u00020\n2\u0006\u0010>\u001a\u00020\nH\u0086\u0002\u001a\u001b\u0010<\u001a\u00020\n*\u00020\n2\f\u0010>\u001a\b\u0012\u0004\u0012\u00020\t0@H\u0086\u0002\u001a\u0015\u0010<\u001a\u00020\f*\u00020\f2\u0006\u0010\u0016\u001a\u00020\u000bH\u0086\u0002\u001a\u0015\u0010<\u001a\u00020\f*\u00020\f2\u0006\u0010>\u001a\u00020\fH\u0086\u0002\u001a\u001b\u0010<\u001a\u00020\f*\u00020\f2\f\u0010>\u001a\b\u0012\u0004\u0012\u00020\u000b0@H\u0086\u0002\u001a\u0015\u0010<\u001a\u00020\u000e*\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\rH\u0086\u0002\u001a\u0015\u0010<\u001a\u00020\u000e*\u00020\u000e2\u0006\u0010>\u001a\u00020\u000eH\u0086\u0002\u001a\u001b\u0010<\u001a\u00020\u000e*\u00020\u000e2\f\u0010>\u001a\b\u0012\u0004\u0012\u00020\r0@H\u0086\u0002\u001a\u0015\u0010<\u001a\u00020\u0010*\u00020\u00102\u0006\u0010\u0016\u001a\u00020\u000fH\u0086\u0002\u001a\u0015\u0010<\u001a\u00020\u0010*\u00020\u00102\u0006\u0010>\u001a\u00020\u0010H\u0086\u0002\u001a\u001b\u0010<\u001a\u00020\u0010*\u00020\u00102\f\u0010>\u001a\b\u0012\u0004\u0012\u00020\u000f0@H\u0086\u0002\u001a\u0015\u0010<\u001a\u00020\u0012*\u00020\u00122\u0006\u0010\u0016\u001a\u00020\u0011H\u0086\u0002\u001a\u0015\u0010<\u001a\u00020\u0012*\u00020\u00122\u0006\u0010>\u001a\u00020\u0012H\u0086\u0002\u001a\u001b\u0010<\u001a\u00020\u0012*\u00020\u00122\f\u0010>\u001a\b\u0012\u0004\u0012\u00020\u00110@H\u0086\u0002\u001a\u0015\u0010<\u001a\u00020\u0014*\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u0013H\u0086\u0002\u001a\u0015\u0010<\u001a\u00020\u0014*\u00020\u00142\u0006\u0010>\u001a\u00020\u0014H\u0086\u0002\u001a\u001b\u0010<\u001a\u00020\u0014*\u00020\u00142\f\u0010>\u001a\b\u0012\u0004\u0012\u00020\u00130@H\u0086\u0002\u001a,\u0010B\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0016\u001a\u0002H\u0002H\u0087\b¢\u0006\u0002\u0010=\u001a\u001d\u0010C\u001a\u000200\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003¢\u0006\u0002\u0010D\u001a*\u0010C\u001a\u000200\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020E*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\u0087\b¢\u0006\u0002\u0010F\u001a1\u0010C\u001a\u000200\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f¢\u0006\u0002\u0010G\u001a\n\u0010C\u001a\u000200*\u00020\b\u001a\u001e\u0010C\u001a\u000200*\u00020\b2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a\n\u0010C\u001a\u000200*\u00020\n\u001a\u001e\u0010C\u001a\u000200*\u00020\n2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a\n\u0010C\u001a\u000200*\u00020\f\u001a\u001e\u0010C\u001a\u000200*\u00020\f2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a\n\u0010C\u001a\u000200*\u00020\u000e\u001a\u001e\u0010C\u001a\u000200*\u00020\u000e2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a\n\u0010C\u001a\u000200*\u00020\u0010\u001a\u001e\u0010C\u001a\u000200*\u00020\u00102\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a\n\u0010C\u001a\u000200*\u00020\u0012\u001a\u001e\u0010C\u001a\u000200*\u00020\u00122\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a\n\u0010C\u001a\u000200*\u00020\u0014\u001a\u001e\u0010C\u001a\u000200*\u00020\u00142\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a9\u0010H\u001a\u000200\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u001a\u0010\u0017\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0018j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0019¢\u0006\u0002\u0010I\u001aM\u0010H\u001a\u000200\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u001a\u0010\u0017\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0018j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f¢\u0006\u0002\u0010J\u001a-\u0010K\u001a\b\u0012\u0004\u0012\u0002H\u00020L\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020E*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003¢\u0006\u0002\u0010M\u001a?\u0010K\u001a\b\u0012\u0004\u0012\u0002H\u00020L\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u001a\u0010\u0017\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0018j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0019¢\u0006\u0002\u0010N\u001a\u0010\u0010K\u001a\b\u0012\u0004\u0012\u00020\u00050L*\u00020\u0006\u001a\u0010\u0010K\u001a\b\u0012\u0004\u0012\u00020\u00070L*\u00020\b\u001a\u0010\u0010K\u001a\b\u0012\u0004\u0012\u00020\t0L*\u00020\n\u001a\u0010\u0010K\u001a\b\u0012\u0004\u0012\u00020\u000b0L*\u00020\f\u001a\u0010\u0010K\u001a\b\u0012\u0004\u0012\u00020\r0L*\u00020\u000e\u001a\u0010\u0010K\u001a\b\u0012\u0004\u0012\u00020\u000f0L*\u00020\u0010\u001a\u0010\u0010K\u001a\b\u0012\u0004\u0012\u00020\u00110L*\u00020\u0012\u001a\u0010\u0010K\u001a\b\u0012\u0004\u0012\u00020\u00130L*\u00020\u0014\u001a\u0015\u0010O\u001a\b\u0012\u0004\u0012\u00020\u00050\u0003*\u00020\u0006¢\u0006\u0002\u0010P\u001a\u0015\u0010O\u001a\b\u0012\u0004\u0012\u00020\u00070\u0003*\u00020\b¢\u0006\u0002\u0010Q\u001a\u0015\u0010O\u001a\b\u0012\u0004\u0012\u00020\t0\u0003*\u00020\n¢\u0006\u0002\u0010R\u001a\u0015\u0010O\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0003*\u00020\f¢\u0006\u0002\u0010S\u001a\u0015\u0010O\u001a\b\u0012\u0004\u0012\u00020\r0\u0003*\u00020\u000e¢\u0006\u0002\u0010T\u001a\u0015\u0010O\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0003*\u00020\u0010¢\u0006\u0002\u0010U\u001a\u0015\u0010O\u001a\b\u0012\u0004\u0012\u00020\u00110\u0003*\u00020\u0012¢\u0006\u0002\u0010V\u001a\u0015\u0010O\u001a\b\u0012\u0004\u0012\u00020\u00130\u0003*\u00020\u0014¢\u0006\u0002\u0010W¨\u0006X"}, d2 = {"asList", "", "T", "", "([Ljava/lang/Object;)Ljava/util/List;", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "binarySearch", "element", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "fromIndex", "toIndex", "([Ljava/lang/Object;Ljava/lang/Object;Ljava/util/Comparator;II)I", "([Ljava/lang/Object;Ljava/lang/Object;II)I", "contentDeepEquals", "other", "([Ljava/lang/Object;[Ljava/lang/Object;)Z", "contentDeepHashCode", "([Ljava/lang/Object;)I", "contentDeepToString", "", "([Ljava/lang/Object;)Ljava/lang/String;", "contentEquals", "contentHashCode", "contentToString", "copyOf", "([Ljava/lang/Object;)[Ljava/lang/Object;", "newSize", "([Ljava/lang/Object;I)[Ljava/lang/Object;", "copyOfRange", "([Ljava/lang/Object;II)[Ljava/lang/Object;", "fill", "", "([Ljava/lang/Object;Ljava/lang/Object;II)V", "filterIsInstance", "R", "klass", "Ljava/lang/Class;", "([Ljava/lang/Object;Ljava/lang/Class;)Ljava/util/List;", "filterIsInstanceTo", "C", "", "destination", "([Ljava/lang/Object;Ljava/util/Collection;Ljava/lang/Class;)Ljava/util/Collection;", "plus", "([Ljava/lang/Object;Ljava/lang/Object;)[Ljava/lang/Object;", "elements", "([Ljava/lang/Object;[Ljava/lang/Object;)[Ljava/lang/Object;", "", "([Ljava/lang/Object;Ljava/util/Collection;)[Ljava/lang/Object;", "plusElement", "sort", "([Ljava/lang/Object;)V", "", "([Ljava/lang/Comparable;)V", "([Ljava/lang/Object;II)V", "sortWith", "([Ljava/lang/Object;Ljava/util/Comparator;)V", "([Ljava/lang/Object;Ljava/util/Comparator;II)V", "toSortedSet", "Ljava/util/SortedSet;", "([Ljava/lang/Comparable;)Ljava/util/SortedSet;", "([Ljava/lang/Object;Ljava/util/Comparator;)Ljava/util/SortedSet;", "toTypedArray", "([Z)[Ljava/lang/Boolean;", "([B)[Ljava/lang/Byte;", "([C)[Ljava/lang/Character;", "([D)[Ljava/lang/Double;", "([F)[Ljava/lang/Float;", "([I)[Ljava/lang/Integer;", "([J)[Ljava/lang/Long;", "([S)[Ljava/lang/Short;", "kotlin-stdlib"}, k = 5, mv = {1, 1, 10}, xi = 1, xs = "kotlin/collections/ArraysKt")
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ArraysKt___ArraysJvmKt extends ArraysKt__ArraysKt {
    @NotNull
    public static final <R> List<R> filterIsInstance(@NotNull Object[] receiver, @NotNull Class<R> klass) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(klass, "klass");
        return (List) ArraysKt.filterIsInstanceTo(receiver, new ArrayList(), klass);
    }

    @NotNull
    public static final <C extends Collection<? super R>, R> C filterIsInstanceTo(@NotNull Object[] receiver, @NotNull C destination, @NotNull Class<R> klass) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(destination, "destination");
        Intrinsics.checkParameterIsNotNull(klass, "klass");
        for (Object obj : receiver) {
            if (klass.isInstance(obj)) {
                destination.add(obj);
            }
        }
        return destination;
    }

    @NotNull
    public static final <T> List<T> asList(@NotNull T[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        List<T> listAsList = ArraysUtilJVM.asList(receiver);
        Intrinsics.checkExpressionValueIsNotNull(listAsList, "ArraysUtilJVM.asList(this)");
        return listAsList;
    }

    /* compiled from: _ArraysJvm.kt */
    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000)\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\b*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00060\u0003j\u0002`\u0004B\u0005¢\u0006\u0002\u0010\u0005J\u0011\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0002H\u0096\u0002J\u0016\u0010\r\u001a\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u0007H\u0096\u0002¢\u0006\u0002\u0010\u000fJ\u0010\u0010\u0010\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u0002H\u0016J\b\u0010\u0011\u001a\u00020\u000bH\u0016J\u0010\u0010\u0012\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u0002H\u0016R\u0014\u0010\u0006\u001a\u00020\u00078VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\t¨\u0006\u0013"}, d2 = {"kotlin/collections/ArraysKt___ArraysJvmKt$asList$1", "Lkotlin/collections/AbstractList;", "", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "([B)V", "size", "", "getSize", "()I", "contains", "", "element", "get", "index", "(I)Ljava/lang/Byte;", "indexOf", "isEmpty", "lastIndexOf", "kotlin-stdlib"}, k = 1, mv = {1, 1, 10})
    /* renamed from: kotlin.collections.ArraysKt___ArraysJvmKt$asList$1, reason: invalid class name */
    public static final class AnonymousClass1 extends AbstractList<Byte> implements RandomAccess {
        final /* synthetic */ byte[] receiver$0;

        AnonymousClass1(byte[] bArr) {
            this.receiver$0 = bArr;
        }

        @Override // kotlin.collections.AbstractCollection, java.util.Collection
        public final /* bridge */ boolean contains(Object obj) {
            if (obj instanceof Byte) {
                return contains(((Number) obj).byteValue());
            }
            return false;
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        public final /* bridge */ int indexOf(Object obj) {
            if (obj instanceof Byte) {
                return indexOf(((Number) obj).byteValue());
            }
            return -1;
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        public final /* bridge */ int lastIndexOf(Object obj) {
            if (obj instanceof Byte) {
                return lastIndexOf(((Number) obj).byteValue());
            }
            return -1;
        }

        @Override // kotlin.collections.AbstractList, kotlin.collections.AbstractCollection
        public int getSize() {
            return this.receiver$0.length;
        }

        @Override // kotlin.collections.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return this.receiver$0.length == 0;
        }

        public boolean contains(byte element) {
            return ArraysKt.contains(this.receiver$0, element);
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        @NotNull
        public Byte get(int index) {
            return Byte.valueOf(this.receiver$0[index]);
        }

        public int indexOf(byte element) {
            return ArraysKt.indexOf(this.receiver$0, element);
        }

        public int lastIndexOf(byte element) {
            return ArraysKt.lastIndexOf(this.receiver$0, element);
        }
    }

    @NotNull
    public static final List<Byte> asList(@NotNull byte[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return new AnonymousClass1(receiver);
    }

    /* compiled from: _ArraysJvm.kt */
    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000)\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\b*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00060\u0003j\u0002`\u0004B\u0005¢\u0006\u0002\u0010\u0005J\u0011\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0002H\u0096\u0002J\u0016\u0010\r\u001a\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u0007H\u0096\u0002¢\u0006\u0002\u0010\u000fJ\u0010\u0010\u0010\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u0002H\u0016J\b\u0010\u0011\u001a\u00020\u000bH\u0016J\u0010\u0010\u0012\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u0002H\u0016R\u0014\u0010\u0006\u001a\u00020\u00078VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\t¨\u0006\u0013"}, d2 = {"kotlin/collections/ArraysKt___ArraysJvmKt$asList$2", "Lkotlin/collections/AbstractList;", "", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "([S)V", "size", "", "getSize", "()I", "contains", "", "element", "get", "index", "(I)Ljava/lang/Short;", "indexOf", "isEmpty", "lastIndexOf", "kotlin-stdlib"}, k = 1, mv = {1, 1, 10})
    /* renamed from: kotlin.collections.ArraysKt___ArraysJvmKt$asList$2, reason: invalid class name */
    public static final class AnonymousClass2 extends AbstractList<Short> implements RandomAccess {
        final /* synthetic */ short[] receiver$0;

        AnonymousClass2(short[] sArr) {
            this.receiver$0 = sArr;
        }

        @Override // kotlin.collections.AbstractCollection, java.util.Collection
        public final /* bridge */ boolean contains(Object obj) {
            if (obj instanceof Short) {
                return contains(((Number) obj).shortValue());
            }
            return false;
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        public final /* bridge */ int indexOf(Object obj) {
            if (obj instanceof Short) {
                return indexOf(((Number) obj).shortValue());
            }
            return -1;
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        public final /* bridge */ int lastIndexOf(Object obj) {
            if (obj instanceof Short) {
                return lastIndexOf(((Number) obj).shortValue());
            }
            return -1;
        }

        @Override // kotlin.collections.AbstractList, kotlin.collections.AbstractCollection
        public int getSize() {
            return this.receiver$0.length;
        }

        @Override // kotlin.collections.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return this.receiver$0.length == 0;
        }

        public boolean contains(short element) {
            return ArraysKt.contains(this.receiver$0, element);
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        @NotNull
        public Short get(int index) {
            return Short.valueOf(this.receiver$0[index]);
        }

        public int indexOf(short element) {
            return ArraysKt.indexOf(this.receiver$0, element);
        }

        public int lastIndexOf(short element) {
            return ArraysKt.lastIndexOf(this.receiver$0, element);
        }
    }

    @NotNull
    public static final List<Short> asList(@NotNull short[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return new AnonymousClass2(receiver);
    }

    /* compiled from: _ArraysJvm.kt */
    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000!\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\b*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00060\u0003j\u0002`\u0004B\u0005¢\u0006\u0002\u0010\u0005J\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0002H\u0096\u0002J\u0016\u0010\f\u001a\u00020\u00022\u0006\u0010\r\u001a\u00020\u0002H\u0096\u0002¢\u0006\u0002\u0010\u000eJ\u0010\u0010\u000f\u001a\u00020\u00022\u0006\u0010\u000b\u001a\u00020\u0002H\u0016J\b\u0010\u0010\u001a\u00020\nH\u0016J\u0010\u0010\u0011\u001a\u00020\u00022\u0006\u0010\u000b\u001a\u00020\u0002H\u0016R\u0014\u0010\u0006\u001a\u00020\u00028VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\b¨\u0006\u0012"}, d2 = {"kotlin/collections/ArraysKt___ArraysJvmKt$asList$3", "Lkotlin/collections/AbstractList;", "", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "([I)V", "size", "getSize", "()I", "contains", "", "element", "get", "index", "(I)Ljava/lang/Integer;", "indexOf", "isEmpty", "lastIndexOf", "kotlin-stdlib"}, k = 1, mv = {1, 1, 10})
    /* renamed from: kotlin.collections.ArraysKt___ArraysJvmKt$asList$3, reason: invalid class name */
    public static final class AnonymousClass3 extends AbstractList<Integer> implements RandomAccess {
        final /* synthetic */ int[] receiver$0;

        AnonymousClass3(int[] iArr) {
            this.receiver$0 = iArr;
        }

        @Override // kotlin.collections.AbstractCollection, java.util.Collection
        public final /* bridge */ boolean contains(Object obj) {
            if (obj instanceof Integer) {
                return contains(((Number) obj).intValue());
            }
            return false;
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        public final /* bridge */ int indexOf(Object obj) {
            if (obj instanceof Integer) {
                return indexOf(((Number) obj).intValue());
            }
            return -1;
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        public final /* bridge */ int lastIndexOf(Object obj) {
            if (obj instanceof Integer) {
                return lastIndexOf(((Number) obj).intValue());
            }
            return -1;
        }

        @Override // kotlin.collections.AbstractList, kotlin.collections.AbstractCollection
        public int getSize() {
            return this.receiver$0.length;
        }

        @Override // kotlin.collections.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return this.receiver$0.length == 0;
        }

        public boolean contains(int element) {
            return ArraysKt.contains(this.receiver$0, element);
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        @NotNull
        public Integer get(int index) {
            return Integer.valueOf(this.receiver$0[index]);
        }

        public int indexOf(int element) {
            return ArraysKt.indexOf(this.receiver$0, element);
        }

        public int lastIndexOf(int element) {
            return ArraysKt.lastIndexOf(this.receiver$0, element);
        }
    }

    @NotNull
    public static final List<Integer> asList(@NotNull int[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return new AnonymousClass3(receiver);
    }

    /* compiled from: _ArraysJvm.kt */
    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000)\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\b*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00060\u0003j\u0002`\u0004B\u0005¢\u0006\u0002\u0010\u0005J\u0011\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0002H\u0096\u0002J\u0016\u0010\r\u001a\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u0007H\u0096\u0002¢\u0006\u0002\u0010\u000fJ\u0010\u0010\u0010\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u0002H\u0016J\b\u0010\u0011\u001a\u00020\u000bH\u0016J\u0010\u0010\u0012\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u0002H\u0016R\u0014\u0010\u0006\u001a\u00020\u00078VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\t¨\u0006\u0013"}, d2 = {"kotlin/collections/ArraysKt___ArraysJvmKt$asList$4", "Lkotlin/collections/AbstractList;", "", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "([J)V", "size", "", "getSize", "()I", "contains", "", "element", "get", "index", "(I)Ljava/lang/Long;", "indexOf", "isEmpty", "lastIndexOf", "kotlin-stdlib"}, k = 1, mv = {1, 1, 10})
    /* renamed from: kotlin.collections.ArraysKt___ArraysJvmKt$asList$4, reason: invalid class name */
    public static final class AnonymousClass4 extends AbstractList<Long> implements RandomAccess {
        final /* synthetic */ long[] receiver$0;

        AnonymousClass4(long[] jArr) {
            this.receiver$0 = jArr;
        }

        @Override // kotlin.collections.AbstractCollection, java.util.Collection
        public final /* bridge */ boolean contains(Object obj) {
            if (obj instanceof Long) {
                return contains(((Number) obj).longValue());
            }
            return false;
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        public final /* bridge */ int indexOf(Object obj) {
            if (obj instanceof Long) {
                return indexOf(((Number) obj).longValue());
            }
            return -1;
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        public final /* bridge */ int lastIndexOf(Object obj) {
            if (obj instanceof Long) {
                return lastIndexOf(((Number) obj).longValue());
            }
            return -1;
        }

        @Override // kotlin.collections.AbstractList, kotlin.collections.AbstractCollection
        public int getSize() {
            return this.receiver$0.length;
        }

        @Override // kotlin.collections.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return this.receiver$0.length == 0;
        }

        public boolean contains(long element) {
            return ArraysKt.contains(this.receiver$0, element);
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        @NotNull
        public Long get(int index) {
            return Long.valueOf(this.receiver$0[index]);
        }

        public int indexOf(long element) {
            return ArraysKt.indexOf(this.receiver$0, element);
        }

        public int lastIndexOf(long element) {
            return ArraysKt.lastIndexOf(this.receiver$0, element);
        }
    }

    @NotNull
    public static final List<Long> asList(@NotNull long[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return new AnonymousClass4(receiver);
    }

    /* compiled from: _ArraysJvm.kt */
    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000)\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\b*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00060\u0003j\u0002`\u0004B\u0005¢\u0006\u0002\u0010\u0005J\u0011\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0002H\u0096\u0002J\u0016\u0010\r\u001a\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u0007H\u0096\u0002¢\u0006\u0002\u0010\u000fJ\u0010\u0010\u0010\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u0002H\u0016J\b\u0010\u0011\u001a\u00020\u000bH\u0016J\u0010\u0010\u0012\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u0002H\u0016R\u0014\u0010\u0006\u001a\u00020\u00078VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\t¨\u0006\u0013"}, d2 = {"kotlin/collections/ArraysKt___ArraysJvmKt$asList$5", "Lkotlin/collections/AbstractList;", "", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "([F)V", "size", "", "getSize", "()I", "contains", "", "element", "get", "index", "(I)Ljava/lang/Float;", "indexOf", "isEmpty", "lastIndexOf", "kotlin-stdlib"}, k = 1, mv = {1, 1, 10})
    /* renamed from: kotlin.collections.ArraysKt___ArraysJvmKt$asList$5, reason: invalid class name */
    public static final class AnonymousClass5 extends AbstractList<Float> implements RandomAccess {
        final /* synthetic */ float[] receiver$0;

        AnonymousClass5(float[] fArr) {
            this.receiver$0 = fArr;
        }

        @Override // kotlin.collections.AbstractCollection, java.util.Collection
        public final /* bridge */ boolean contains(Object obj) {
            if (obj instanceof Float) {
                return contains(((Number) obj).floatValue());
            }
            return false;
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        public final /* bridge */ int indexOf(Object obj) {
            if (obj instanceof Float) {
                return indexOf(((Number) obj).floatValue());
            }
            return -1;
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        public final /* bridge */ int lastIndexOf(Object obj) {
            if (obj instanceof Float) {
                return lastIndexOf(((Number) obj).floatValue());
            }
            return -1;
        }

        @Override // kotlin.collections.AbstractList, kotlin.collections.AbstractCollection
        public int getSize() {
            return this.receiver$0.length;
        }

        @Override // kotlin.collections.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return this.receiver$0.length == 0;
        }

        public boolean contains(float element) {
            return ArraysKt.contains(this.receiver$0, element);
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        @NotNull
        public Float get(int index) {
            return Float.valueOf(this.receiver$0[index]);
        }

        public int indexOf(float element) {
            return ArraysKt.indexOf(this.receiver$0, element);
        }

        public int lastIndexOf(float element) {
            return ArraysKt.lastIndexOf(this.receiver$0, element);
        }
    }

    @NotNull
    public static final List<Float> asList(@NotNull float[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return new AnonymousClass5(receiver);
    }

    /* compiled from: _ArraysJvm.kt */
    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000)\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\b*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00060\u0003j\u0002`\u0004B\u0005¢\u0006\u0002\u0010\u0005J\u0011\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0002H\u0096\u0002J\u0016\u0010\r\u001a\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u0007H\u0096\u0002¢\u0006\u0002\u0010\u000fJ\u0010\u0010\u0010\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u0002H\u0016J\b\u0010\u0011\u001a\u00020\u000bH\u0016J\u0010\u0010\u0012\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u0002H\u0016R\u0014\u0010\u0006\u001a\u00020\u00078VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\t¨\u0006\u0013"}, d2 = {"kotlin/collections/ArraysKt___ArraysJvmKt$asList$6", "Lkotlin/collections/AbstractList;", "", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "([D)V", "size", "", "getSize", "()I", "contains", "", "element", "get", "index", "(I)Ljava/lang/Double;", "indexOf", "isEmpty", "lastIndexOf", "kotlin-stdlib"}, k = 1, mv = {1, 1, 10})
    /* renamed from: kotlin.collections.ArraysKt___ArraysJvmKt$asList$6, reason: invalid class name */
    public static final class AnonymousClass6 extends AbstractList<Double> implements RandomAccess {
        final /* synthetic */ double[] receiver$0;

        AnonymousClass6(double[] dArr) {
            this.receiver$0 = dArr;
        }

        @Override // kotlin.collections.AbstractCollection, java.util.Collection
        public final /* bridge */ boolean contains(Object obj) {
            if (obj instanceof Double) {
                return contains(((Number) obj).doubleValue());
            }
            return false;
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        public final /* bridge */ int indexOf(Object obj) {
            if (obj instanceof Double) {
                return indexOf(((Number) obj).doubleValue());
            }
            return -1;
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        public final /* bridge */ int lastIndexOf(Object obj) {
            if (obj instanceof Double) {
                return lastIndexOf(((Number) obj).doubleValue());
            }
            return -1;
        }

        @Override // kotlin.collections.AbstractList, kotlin.collections.AbstractCollection
        public int getSize() {
            return this.receiver$0.length;
        }

        @Override // kotlin.collections.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return this.receiver$0.length == 0;
        }

        public boolean contains(double element) {
            return ArraysKt.contains(this.receiver$0, element);
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        @NotNull
        public Double get(int index) {
            return Double.valueOf(this.receiver$0[index]);
        }

        public int indexOf(double element) {
            return ArraysKt.indexOf(this.receiver$0, element);
        }

        public int lastIndexOf(double element) {
            return ArraysKt.lastIndexOf(this.receiver$0, element);
        }
    }

    @NotNull
    public static final List<Double> asList(@NotNull double[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return new AnonymousClass6(receiver);
    }

    /* compiled from: _ArraysJvm.kt */
    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000!\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u000b*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00060\u0003j\u0002`\u0004B\u0005¢\u0006\u0002\u0010\u0005J\u0011\u0010\n\u001a\u00020\u00022\u0006\u0010\u000b\u001a\u00020\u0002H\u0096\u0002J\u0016\u0010\f\u001a\u00020\u00022\u0006\u0010\r\u001a\u00020\u0007H\u0096\u0002¢\u0006\u0002\u0010\u000eJ\u0010\u0010\u000f\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u0002H\u0016J\b\u0010\u0010\u001a\u00020\u0002H\u0016J\u0010\u0010\u0011\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u0002H\u0016R\u0014\u0010\u0006\u001a\u00020\u00078VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\t¨\u0006\u0012"}, d2 = {"kotlin/collections/ArraysKt___ArraysJvmKt$asList$7", "Lkotlin/collections/AbstractList;", "", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "([Z)V", "size", "", "getSize", "()I", "contains", "element", "get", "index", "(I)Ljava/lang/Boolean;", "indexOf", "isEmpty", "lastIndexOf", "kotlin-stdlib"}, k = 1, mv = {1, 1, 10})
    /* renamed from: kotlin.collections.ArraysKt___ArraysJvmKt$asList$7, reason: invalid class name */
    public static final class AnonymousClass7 extends AbstractList<Boolean> implements RandomAccess {
        final /* synthetic */ boolean[] receiver$0;

        AnonymousClass7(boolean[] zArr) {
            this.receiver$0 = zArr;
        }

        @Override // kotlin.collections.AbstractCollection, java.util.Collection
        public final /* bridge */ boolean contains(Object obj) {
            if (obj instanceof Boolean) {
                return contains(((Boolean) obj).booleanValue());
            }
            return false;
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        public final /* bridge */ int indexOf(Object obj) {
            if (obj instanceof Boolean) {
                return indexOf(((Boolean) obj).booleanValue());
            }
            return -1;
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        public final /* bridge */ int lastIndexOf(Object obj) {
            if (obj instanceof Boolean) {
                return lastIndexOf(((Boolean) obj).booleanValue());
            }
            return -1;
        }

        @Override // kotlin.collections.AbstractList, kotlin.collections.AbstractCollection
        public int getSize() {
            return this.receiver$0.length;
        }

        @Override // kotlin.collections.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return this.receiver$0.length == 0;
        }

        public boolean contains(boolean element) {
            return ArraysKt.contains(this.receiver$0, element);
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        @NotNull
        public Boolean get(int index) {
            return Boolean.valueOf(this.receiver$0[index]);
        }

        public int indexOf(boolean element) {
            return ArraysKt.indexOf(this.receiver$0, element);
        }

        public int lastIndexOf(boolean element) {
            return ArraysKt.lastIndexOf(this.receiver$0, element);
        }
    }

    @NotNull
    public static final List<Boolean> asList(@NotNull boolean[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return new AnonymousClass7(receiver);
    }

    /* compiled from: _ArraysJvm.kt */
    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000)\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\b*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00060\u0003j\u0002`\u0004B\u0005¢\u0006\u0002\u0010\u0005J\u0011\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0002H\u0096\u0002J\u0016\u0010\r\u001a\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u0007H\u0096\u0002¢\u0006\u0002\u0010\u000fJ\u0010\u0010\u0010\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u0002H\u0016J\b\u0010\u0011\u001a\u00020\u000bH\u0016J\u0010\u0010\u0012\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u0002H\u0016R\u0014\u0010\u0006\u001a\u00020\u00078VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\t¨\u0006\u0013"}, d2 = {"kotlin/collections/ArraysKt___ArraysJvmKt$asList$8", "Lkotlin/collections/AbstractList;", "", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "([C)V", "size", "", "getSize", "()I", "contains", "", "element", "get", "index", "(I)Ljava/lang/Character;", "indexOf", "isEmpty", "lastIndexOf", "kotlin-stdlib"}, k = 1, mv = {1, 1, 10})
    /* renamed from: kotlin.collections.ArraysKt___ArraysJvmKt$asList$8, reason: invalid class name */
    public static final class AnonymousClass8 extends AbstractList<Character> implements RandomAccess {
        final /* synthetic */ char[] receiver$0;

        AnonymousClass8(char[] cArr) {
            this.receiver$0 = cArr;
        }

        @Override // kotlin.collections.AbstractCollection, java.util.Collection
        public final /* bridge */ boolean contains(Object obj) {
            if (obj instanceof Character) {
                return contains(((Character) obj).charValue());
            }
            return false;
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        public final /* bridge */ int indexOf(Object obj) {
            if (obj instanceof Character) {
                return indexOf(((Character) obj).charValue());
            }
            return -1;
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        public final /* bridge */ int lastIndexOf(Object obj) {
            if (obj instanceof Character) {
                return lastIndexOf(((Character) obj).charValue());
            }
            return -1;
        }

        @Override // kotlin.collections.AbstractList, kotlin.collections.AbstractCollection
        public int getSize() {
            return this.receiver$0.length;
        }

        @Override // kotlin.collections.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return this.receiver$0.length == 0;
        }

        public boolean contains(char element) {
            return ArraysKt.contains(this.receiver$0, element);
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        @NotNull
        public Character get(int index) {
            return Character.valueOf(this.receiver$0[index]);
        }

        public int indexOf(char element) {
            return ArraysKt.indexOf(this.receiver$0, element);
        }

        public int lastIndexOf(char element) {
            return ArraysKt.lastIndexOf(this.receiver$0, element);
        }
    }

    @NotNull
    public static final List<Character> asList(@NotNull char[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return new AnonymousClass8(receiver);
    }

    public static /* bridge */ /* synthetic */ int binarySearch$default(Object[] objArr, Object obj, Comparator comparator, int i, int i2, int i3, Object obj2) {
        if ((i3 & 4) != 0) {
            i = 0;
        }
        if ((i3 & 8) != 0) {
            i2 = objArr.length;
        }
        return ArraysKt.binarySearch(objArr, obj, comparator, i, i2);
    }

    public static final <T> int binarySearch(@NotNull T[] receiver, T t, @NotNull Comparator<? super T> comparator, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        return Arrays.binarySearch(receiver, i, i2, t, comparator);
    }

    public static /* bridge */ /* synthetic */ int binarySearch$default(Object[] objArr, Object obj, int i, int i2, int i3, Object obj2) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = objArr.length;
        }
        return ArraysKt.binarySearch(objArr, obj, i, i2);
    }

    public static final <T> int binarySearch(@NotNull T[] receiver, T t, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return Arrays.binarySearch(receiver, i, i2, t);
    }

    public static /* bridge */ /* synthetic */ int binarySearch$default(byte[] bArr, byte b, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = bArr.length;
        }
        return ArraysKt.binarySearch(bArr, b, i, i2);
    }

    public static final int binarySearch(@NotNull byte[] receiver, byte b, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return Arrays.binarySearch(receiver, i, i2, b);
    }

    public static /* bridge */ /* synthetic */ int binarySearch$default(short[] sArr, short s, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = sArr.length;
        }
        return ArraysKt.binarySearch(sArr, s, i, i2);
    }

    public static final int binarySearch(@NotNull short[] receiver, short s, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return Arrays.binarySearch(receiver, i, i2, s);
    }

    public static /* bridge */ /* synthetic */ int binarySearch$default(int[] iArr, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i2 = 0;
        }
        if ((i4 & 4) != 0) {
            i3 = iArr.length;
        }
        return ArraysKt.binarySearch(iArr, i, i2, i3);
    }

    public static final int binarySearch(@NotNull int[] receiver, int i, int i2, int i3) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return Arrays.binarySearch(receiver, i2, i3, i);
    }

    public static /* bridge */ /* synthetic */ int binarySearch$default(long[] jArr, long j, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = jArr.length;
        }
        return ArraysKt.binarySearch(jArr, j, i, i2);
    }

    public static final int binarySearch(@NotNull long[] receiver, long j, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return Arrays.binarySearch(receiver, i, i2, j);
    }

    public static /* bridge */ /* synthetic */ int binarySearch$default(float[] fArr, float f, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = fArr.length;
        }
        return ArraysKt.binarySearch(fArr, f, i, i2);
    }

    public static final int binarySearch(@NotNull float[] receiver, float f, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return Arrays.binarySearch(receiver, i, i2, f);
    }

    public static /* bridge */ /* synthetic */ int binarySearch$default(double[] dArr, double d, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = dArr.length;
        }
        return ArraysKt.binarySearch(dArr, d, i, i2);
    }

    public static final int binarySearch(@NotNull double[] receiver, double d, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return Arrays.binarySearch(receiver, i, i2, d);
    }

    public static /* bridge */ /* synthetic */ int binarySearch$default(char[] cArr, char c, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = cArr.length;
        }
        return ArraysKt.binarySearch(cArr, c, i, i2);
    }

    public static final int binarySearch(@NotNull char[] receiver, char c, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return Arrays.binarySearch(receiver, i, i2, c);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> boolean contentDeepEquals(@NotNull T[] tArr, T[] tArr2) {
        return Arrays.deepEquals(tArr, tArr2);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> int contentDeepHashCode(@NotNull T[] tArr) {
        return Arrays.deepHashCode(tArr);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> String contentDeepToString(@NotNull T[] tArr) {
        String strDeepToString = Arrays.deepToString(tArr);
        Intrinsics.checkExpressionValueIsNotNull(strDeepToString, "java.util.Arrays.deepToString(this)");
        return strDeepToString;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> boolean contentEquals(@NotNull T[] tArr, T[] tArr2) {
        return Arrays.equals(tArr, tArr2);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull byte[] bArr, byte[] bArr2) {
        return Arrays.equals(bArr, bArr2);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull short[] sArr, short[] sArr2) {
        return Arrays.equals(sArr, sArr2);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull int[] iArr, int[] iArr2) {
        return Arrays.equals(iArr, iArr2);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull long[] jArr, long[] jArr2) {
        return Arrays.equals(jArr, jArr2);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull float[] fArr, float[] fArr2) {
        return Arrays.equals(fArr, fArr2);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull double[] dArr, double[] dArr2) {
        return Arrays.equals(dArr, dArr2);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull boolean[] zArr, boolean[] zArr2) {
        return Arrays.equals(zArr, zArr2);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull char[] cArr, char[] cArr2) {
        return Arrays.equals(cArr, cArr2);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> int contentHashCode(@NotNull T[] tArr) {
        return Arrays.hashCode(tArr);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull byte[] bArr) {
        return Arrays.hashCode(bArr);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull short[] sArr) {
        return Arrays.hashCode(sArr);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull int[] iArr) {
        return Arrays.hashCode(iArr);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull long[] jArr) {
        return Arrays.hashCode(jArr);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull float[] fArr) {
        return Arrays.hashCode(fArr);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull double[] dArr) {
        return Arrays.hashCode(dArr);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull boolean[] zArr) {
        return Arrays.hashCode(zArr);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull char[] cArr) {
        return Arrays.hashCode(cArr);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> String contentToString(@NotNull T[] tArr) {
        String string = Arrays.toString(tArr);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.util.Arrays.toString(this)");
        return string;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull byte[] bArr) {
        String string = Arrays.toString(bArr);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.util.Arrays.toString(this)");
        return string;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull short[] sArr) {
        String string = Arrays.toString(sArr);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.util.Arrays.toString(this)");
        return string;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull int[] iArr) {
        String string = Arrays.toString(iArr);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.util.Arrays.toString(this)");
        return string;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull long[] jArr) {
        String string = Arrays.toString(jArr);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.util.Arrays.toString(this)");
        return string;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull float[] fArr) {
        String string = Arrays.toString(fArr);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.util.Arrays.toString(this)");
        return string;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull double[] dArr) {
        String string = Arrays.toString(dArr);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.util.Arrays.toString(this)");
        return string;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull boolean[] zArr) {
        String string = Arrays.toString(zArr);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.util.Arrays.toString(this)");
        return string;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull char[] cArr) {
        String string = Arrays.toString(cArr);
        Intrinsics.checkExpressionValueIsNotNull(string, "java.util.Arrays.toString(this)");
        return string;
    }

    @InlineOnly
    private static final <T> T[] copyOf(@NotNull T[] tArr) {
        T[] tArr2 = (T[]) Arrays.copyOf(tArr, tArr.length);
        Intrinsics.checkExpressionValueIsNotNull(tArr2, "java.util.Arrays.copyOf(this, size)");
        return tArr2;
    }

    @InlineOnly
    private static final byte[] copyOf(@NotNull byte[] bArr) {
        byte[] bArrCopyOf = Arrays.copyOf(bArr, bArr.length);
        Intrinsics.checkExpressionValueIsNotNull(bArrCopyOf, "java.util.Arrays.copyOf(this, size)");
        return bArrCopyOf;
    }

    @InlineOnly
    private static final short[] copyOf(@NotNull short[] sArr) {
        short[] sArrCopyOf = Arrays.copyOf(sArr, sArr.length);
        Intrinsics.checkExpressionValueIsNotNull(sArrCopyOf, "java.util.Arrays.copyOf(this, size)");
        return sArrCopyOf;
    }

    @InlineOnly
    private static final int[] copyOf(@NotNull int[] iArr) {
        int[] iArrCopyOf = Arrays.copyOf(iArr, iArr.length);
        Intrinsics.checkExpressionValueIsNotNull(iArrCopyOf, "java.util.Arrays.copyOf(this, size)");
        return iArrCopyOf;
    }

    @InlineOnly
    private static final long[] copyOf(@NotNull long[] jArr) {
        long[] jArrCopyOf = Arrays.copyOf(jArr, jArr.length);
        Intrinsics.checkExpressionValueIsNotNull(jArrCopyOf, "java.util.Arrays.copyOf(this, size)");
        return jArrCopyOf;
    }

    @InlineOnly
    private static final float[] copyOf(@NotNull float[] fArr) {
        float[] fArrCopyOf = Arrays.copyOf(fArr, fArr.length);
        Intrinsics.checkExpressionValueIsNotNull(fArrCopyOf, "java.util.Arrays.copyOf(this, size)");
        return fArrCopyOf;
    }

    @InlineOnly
    private static final double[] copyOf(@NotNull double[] dArr) {
        double[] dArrCopyOf = Arrays.copyOf(dArr, dArr.length);
        Intrinsics.checkExpressionValueIsNotNull(dArrCopyOf, "java.util.Arrays.copyOf(this, size)");
        return dArrCopyOf;
    }

    @InlineOnly
    private static final boolean[] copyOf(@NotNull boolean[] zArr) {
        boolean[] zArrCopyOf = Arrays.copyOf(zArr, zArr.length);
        Intrinsics.checkExpressionValueIsNotNull(zArrCopyOf, "java.util.Arrays.copyOf(this, size)");
        return zArrCopyOf;
    }

    @InlineOnly
    private static final char[] copyOf(@NotNull char[] cArr) {
        char[] cArrCopyOf = Arrays.copyOf(cArr, cArr.length);
        Intrinsics.checkExpressionValueIsNotNull(cArrCopyOf, "java.util.Arrays.copyOf(this, size)");
        return cArrCopyOf;
    }

    @InlineOnly
    private static final byte[] copyOf(@NotNull byte[] bArr, int i) {
        byte[] bArrCopyOf = Arrays.copyOf(bArr, i);
        Intrinsics.checkExpressionValueIsNotNull(bArrCopyOf, "java.util.Arrays.copyOf(this, newSize)");
        return bArrCopyOf;
    }

    @InlineOnly
    private static final short[] copyOf(@NotNull short[] sArr, int i) {
        short[] sArrCopyOf = Arrays.copyOf(sArr, i);
        Intrinsics.checkExpressionValueIsNotNull(sArrCopyOf, "java.util.Arrays.copyOf(this, newSize)");
        return sArrCopyOf;
    }

    @InlineOnly
    private static final int[] copyOf(@NotNull int[] iArr, int i) {
        int[] iArrCopyOf = Arrays.copyOf(iArr, i);
        Intrinsics.checkExpressionValueIsNotNull(iArrCopyOf, "java.util.Arrays.copyOf(this, newSize)");
        return iArrCopyOf;
    }

    @InlineOnly
    private static final long[] copyOf(@NotNull long[] jArr, int i) {
        long[] jArrCopyOf = Arrays.copyOf(jArr, i);
        Intrinsics.checkExpressionValueIsNotNull(jArrCopyOf, "java.util.Arrays.copyOf(this, newSize)");
        return jArrCopyOf;
    }

    @InlineOnly
    private static final float[] copyOf(@NotNull float[] fArr, int i) {
        float[] fArrCopyOf = Arrays.copyOf(fArr, i);
        Intrinsics.checkExpressionValueIsNotNull(fArrCopyOf, "java.util.Arrays.copyOf(this, newSize)");
        return fArrCopyOf;
    }

    @InlineOnly
    private static final double[] copyOf(@NotNull double[] dArr, int i) {
        double[] dArrCopyOf = Arrays.copyOf(dArr, i);
        Intrinsics.checkExpressionValueIsNotNull(dArrCopyOf, "java.util.Arrays.copyOf(this, newSize)");
        return dArrCopyOf;
    }

    @InlineOnly
    private static final boolean[] copyOf(@NotNull boolean[] zArr, int i) {
        boolean[] zArrCopyOf = Arrays.copyOf(zArr, i);
        Intrinsics.checkExpressionValueIsNotNull(zArrCopyOf, "java.util.Arrays.copyOf(this, newSize)");
        return zArrCopyOf;
    }

    @InlineOnly
    private static final char[] copyOf(@NotNull char[] cArr, int i) {
        char[] cArrCopyOf = Arrays.copyOf(cArr, i);
        Intrinsics.checkExpressionValueIsNotNull(cArrCopyOf, "java.util.Arrays.copyOf(this, newSize)");
        return cArrCopyOf;
    }

    @InlineOnly
    private static final <T> T[] copyOf(@NotNull T[] tArr, int i) {
        T[] tArr2 = (T[]) Arrays.copyOf(tArr, i);
        Intrinsics.checkExpressionValueIsNotNull(tArr2, "java.util.Arrays.copyOf(this, newSize)");
        return tArr2;
    }

    @InlineOnly
    private static final <T> T[] copyOfRange(@NotNull T[] tArr, int i, int i2) {
        T[] tArr2 = (T[]) Arrays.copyOfRange(tArr, i, i2);
        Intrinsics.checkExpressionValueIsNotNull(tArr2, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return tArr2;
    }

    @InlineOnly
    private static final byte[] copyOfRange(@NotNull byte[] bArr, int i, int i2) {
        byte[] bArrCopyOfRange = Arrays.copyOfRange(bArr, i, i2);
        Intrinsics.checkExpressionValueIsNotNull(bArrCopyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return bArrCopyOfRange;
    }

    @InlineOnly
    private static final short[] copyOfRange(@NotNull short[] sArr, int i, int i2) {
        short[] sArrCopyOfRange = Arrays.copyOfRange(sArr, i, i2);
        Intrinsics.checkExpressionValueIsNotNull(sArrCopyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return sArrCopyOfRange;
    }

    @InlineOnly
    private static final int[] copyOfRange(@NotNull int[] iArr, int i, int i2) {
        int[] iArrCopyOfRange = Arrays.copyOfRange(iArr, i, i2);
        Intrinsics.checkExpressionValueIsNotNull(iArrCopyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return iArrCopyOfRange;
    }

    @InlineOnly
    private static final long[] copyOfRange(@NotNull long[] jArr, int i, int i2) {
        long[] jArrCopyOfRange = Arrays.copyOfRange(jArr, i, i2);
        Intrinsics.checkExpressionValueIsNotNull(jArrCopyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return jArrCopyOfRange;
    }

    @InlineOnly
    private static final float[] copyOfRange(@NotNull float[] fArr, int i, int i2) {
        float[] fArrCopyOfRange = Arrays.copyOfRange(fArr, i, i2);
        Intrinsics.checkExpressionValueIsNotNull(fArrCopyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return fArrCopyOfRange;
    }

    @InlineOnly
    private static final double[] copyOfRange(@NotNull double[] dArr, int i, int i2) {
        double[] dArrCopyOfRange = Arrays.copyOfRange(dArr, i, i2);
        Intrinsics.checkExpressionValueIsNotNull(dArrCopyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return dArrCopyOfRange;
    }

    @InlineOnly
    private static final boolean[] copyOfRange(@NotNull boolean[] zArr, int i, int i2) {
        boolean[] zArrCopyOfRange = Arrays.copyOfRange(zArr, i, i2);
        Intrinsics.checkExpressionValueIsNotNull(zArrCopyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return zArrCopyOfRange;
    }

    @InlineOnly
    private static final char[] copyOfRange(@NotNull char[] cArr, int i, int i2) {
        char[] cArrCopyOfRange = Arrays.copyOfRange(cArr, i, i2);
        Intrinsics.checkExpressionValueIsNotNull(cArrCopyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return cArrCopyOfRange;
    }

    public static /* bridge */ /* synthetic */ void fill$default(Object[] objArr, Object obj, int i, int i2, int i3, Object obj2) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = objArr.length;
        }
        ArraysKt.fill(objArr, obj, i, i2);
    }

    public static final <T> void fill(@NotNull T[] receiver, T t, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Arrays.fill(receiver, i, i2, t);
    }

    public static /* bridge */ /* synthetic */ void fill$default(byte[] bArr, byte b, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = bArr.length;
        }
        ArraysKt.fill(bArr, b, i, i2);
    }

    public static final void fill(@NotNull byte[] receiver, byte b, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Arrays.fill(receiver, i, i2, b);
    }

    public static /* bridge */ /* synthetic */ void fill$default(short[] sArr, short s, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = sArr.length;
        }
        ArraysKt.fill(sArr, s, i, i2);
    }

    public static final void fill(@NotNull short[] receiver, short s, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Arrays.fill(receiver, i, i2, s);
    }

    public static /* bridge */ /* synthetic */ void fill$default(int[] iArr, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i2 = 0;
        }
        if ((i4 & 4) != 0) {
            i3 = iArr.length;
        }
        ArraysKt.fill(iArr, i, i2, i3);
    }

    public static final void fill(@NotNull int[] receiver, int i, int i2, int i3) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Arrays.fill(receiver, i2, i3, i);
    }

    public static /* bridge */ /* synthetic */ void fill$default(long[] jArr, long j, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = jArr.length;
        }
        ArraysKt.fill(jArr, j, i, i2);
    }

    public static final void fill(@NotNull long[] receiver, long j, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Arrays.fill(receiver, i, i2, j);
    }

    public static /* bridge */ /* synthetic */ void fill$default(float[] fArr, float f, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = fArr.length;
        }
        ArraysKt.fill(fArr, f, i, i2);
    }

    public static final void fill(@NotNull float[] receiver, float f, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Arrays.fill(receiver, i, i2, f);
    }

    public static /* bridge */ /* synthetic */ void fill$default(double[] dArr, double d, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = dArr.length;
        }
        ArraysKt.fill(dArr, d, i, i2);
    }

    public static final void fill(@NotNull double[] receiver, double d, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Arrays.fill(receiver, i, i2, d);
    }

    public static /* bridge */ /* synthetic */ void fill$default(boolean[] zArr, boolean z, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = zArr.length;
        }
        ArraysKt.fill(zArr, z, i, i2);
    }

    public static final void fill(@NotNull boolean[] receiver, boolean z, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Arrays.fill(receiver, i, i2, z);
    }

    public static /* bridge */ /* synthetic */ void fill$default(char[] cArr, char c, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = cArr.length;
        }
        ArraysKt.fill(cArr, c, i, i2);
    }

    public static final void fill(@NotNull char[] receiver, char c, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Arrays.fill(receiver, i, i2, c);
    }

    @NotNull
    public static final <T> T[] plus(@NotNull T[] receiver, T t) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        int length = receiver.length;
        T[] result = (T[]) Arrays.copyOf(receiver, length + 1);
        result[length] = t;
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final byte[] plus(@NotNull byte[] receiver, byte b) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        int length = receiver.length;
        byte[] result = Arrays.copyOf(receiver, length + 1);
        result[length] = b;
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final short[] plus(@NotNull short[] receiver, short s) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        int length = receiver.length;
        short[] result = Arrays.copyOf(receiver, length + 1);
        result[length] = s;
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final int[] plus(@NotNull int[] receiver, int i) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        int length = receiver.length;
        int[] result = Arrays.copyOf(receiver, length + 1);
        result[length] = i;
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final long[] plus(@NotNull long[] receiver, long j) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        int length = receiver.length;
        long[] result = Arrays.copyOf(receiver, length + 1);
        result[length] = j;
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final float[] plus(@NotNull float[] receiver, float f) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        int length = receiver.length;
        float[] result = Arrays.copyOf(receiver, length + 1);
        result[length] = f;
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final double[] plus(@NotNull double[] receiver, double d) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        int length = receiver.length;
        double[] result = Arrays.copyOf(receiver, length + 1);
        result[length] = d;
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final boolean[] plus(@NotNull boolean[] receiver, boolean z) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        int length = receiver.length;
        boolean[] result = Arrays.copyOf(receiver, length + 1);
        result[length] = z;
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final char[] plus(@NotNull char[] receiver, char c) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        int length = receiver.length;
        char[] result = Arrays.copyOf(receiver, length + 1);
        result[length] = c;
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final <T> T[] plus(@NotNull T[] receiver, @NotNull Collection<? extends T> elements) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int length = receiver.length;
        T[] result = (T[]) Arrays.copyOf(receiver, elements.size() + length);
        Iterator<? extends T> it = elements.iterator();
        while (it.hasNext()) {
            result[length] = it.next();
            length++;
        }
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final byte[] plus(@NotNull byte[] receiver, @NotNull Collection<Byte> elements) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int length = receiver.length;
        byte[] result = Arrays.copyOf(receiver, elements.size() + length);
        Iterator<Byte> it = elements.iterator();
        while (it.hasNext()) {
            result[length] = it.next().byteValue();
            length++;
        }
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final short[] plus(@NotNull short[] receiver, @NotNull Collection<Short> elements) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int length = receiver.length;
        short[] result = Arrays.copyOf(receiver, elements.size() + length);
        Iterator<Short> it = elements.iterator();
        while (it.hasNext()) {
            result[length] = it.next().shortValue();
            length++;
        }
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final int[] plus(@NotNull int[] receiver, @NotNull Collection<Integer> elements) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int length = receiver.length;
        int[] result = Arrays.copyOf(receiver, elements.size() + length);
        Iterator<Integer> it = elements.iterator();
        while (it.hasNext()) {
            result[length] = it.next().intValue();
            length++;
        }
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final long[] plus(@NotNull long[] receiver, @NotNull Collection<Long> elements) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int length = receiver.length;
        long[] result = Arrays.copyOf(receiver, elements.size() + length);
        Iterator<Long> it = elements.iterator();
        while (it.hasNext()) {
            result[length] = it.next().longValue();
            length++;
        }
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final float[] plus(@NotNull float[] receiver, @NotNull Collection<Float> elements) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int length = receiver.length;
        float[] result = Arrays.copyOf(receiver, elements.size() + length);
        Iterator<Float> it = elements.iterator();
        while (it.hasNext()) {
            result[length] = it.next().floatValue();
            length++;
        }
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final double[] plus(@NotNull double[] receiver, @NotNull Collection<Double> elements) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int length = receiver.length;
        double[] result = Arrays.copyOf(receiver, elements.size() + length);
        Iterator<Double> it = elements.iterator();
        while (it.hasNext()) {
            result[length] = it.next().doubleValue();
            length++;
        }
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final boolean[] plus(@NotNull boolean[] receiver, @NotNull Collection<Boolean> elements) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int length = receiver.length;
        boolean[] result = Arrays.copyOf(receiver, elements.size() + length);
        Iterator<Boolean> it = elements.iterator();
        while (it.hasNext()) {
            result[length] = it.next().booleanValue();
            length++;
        }
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final char[] plus(@NotNull char[] receiver, @NotNull Collection<Character> elements) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int length = receiver.length;
        char[] result = Arrays.copyOf(receiver, elements.size() + length);
        Iterator<Character> it = elements.iterator();
        while (it.hasNext()) {
            result[length] = it.next().charValue();
            length++;
        }
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final <T> T[] plus(@NotNull T[] receiver, @NotNull T[] elements) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int length = receiver.length;
        int length2 = elements.length;
        T[] result = (T[]) Arrays.copyOf(receiver, length + length2);
        System.arraycopy(elements, 0, result, length, length2);
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final byte[] plus(@NotNull byte[] receiver, @NotNull byte[] elements) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int length = receiver.length;
        int length2 = elements.length;
        byte[] result = Arrays.copyOf(receiver, length + length2);
        System.arraycopy(elements, 0, result, length, length2);
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final short[] plus(@NotNull short[] receiver, @NotNull short[] elements) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int length = receiver.length;
        int length2 = elements.length;
        short[] result = Arrays.copyOf(receiver, length + length2);
        System.arraycopy(elements, 0, result, length, length2);
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final int[] plus(@NotNull int[] receiver, @NotNull int[] elements) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int length = receiver.length;
        int length2 = elements.length;
        int[] result = Arrays.copyOf(receiver, length + length2);
        System.arraycopy(elements, 0, result, length, length2);
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final long[] plus(@NotNull long[] receiver, @NotNull long[] elements) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int length = receiver.length;
        int length2 = elements.length;
        long[] result = Arrays.copyOf(receiver, length + length2);
        System.arraycopy(elements, 0, result, length, length2);
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final float[] plus(@NotNull float[] receiver, @NotNull float[] elements) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int length = receiver.length;
        int length2 = elements.length;
        float[] result = Arrays.copyOf(receiver, length + length2);
        System.arraycopy(elements, 0, result, length, length2);
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final double[] plus(@NotNull double[] receiver, @NotNull double[] elements) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int length = receiver.length;
        int length2 = elements.length;
        double[] result = Arrays.copyOf(receiver, length + length2);
        System.arraycopy(elements, 0, result, length, length2);
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final boolean[] plus(@NotNull boolean[] receiver, @NotNull boolean[] elements) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int length = receiver.length;
        int length2 = elements.length;
        boolean[] result = Arrays.copyOf(receiver, length + length2);
        System.arraycopy(elements, 0, result, length, length2);
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @NotNull
    public static final char[] plus(@NotNull char[] receiver, @NotNull char[] elements) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        int length = receiver.length;
        int length2 = elements.length;
        char[] result = Arrays.copyOf(receiver, length + length2);
        System.arraycopy(elements, 0, result, length, length2);
        Intrinsics.checkExpressionValueIsNotNull(result, "result");
        return result;
    }

    @InlineOnly
    private static final <T> T[] plusElement(@NotNull T[] tArr, T t) {
        return (T[]) ArraysKt.plus(tArr, t);
    }

    public static final void sort(@NotNull int[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        if (receiver.length > 1) {
            Arrays.sort(receiver);
        }
    }

    public static final void sort(@NotNull long[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        if (receiver.length > 1) {
            Arrays.sort(receiver);
        }
    }

    public static final void sort(@NotNull byte[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        if (receiver.length > 1) {
            Arrays.sort(receiver);
        }
    }

    public static final void sort(@NotNull short[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        if (receiver.length > 1) {
            Arrays.sort(receiver);
        }
    }

    public static final void sort(@NotNull double[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        if (receiver.length > 1) {
            Arrays.sort(receiver);
        }
    }

    public static final void sort(@NotNull float[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        if (receiver.length > 1) {
            Arrays.sort(receiver);
        }
    }

    public static final void sort(@NotNull char[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        if (receiver.length > 1) {
            Arrays.sort(receiver);
        }
    }

    @InlineOnly
    private static final <T extends Comparable<? super T>> void sort(@NotNull T[] tArr) {
        if (tArr == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<kotlin.Any?>");
        }
        ArraysKt.sort((Object[]) tArr);
    }

    public static final <T> void sort(@NotNull T[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        if (receiver.length > 1) {
            Arrays.sort(receiver);
        }
    }

    public static /* bridge */ /* synthetic */ void sort$default(Object[] objArr, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = objArr.length;
        }
        ArraysKt.sort(objArr, i, i2);
    }

    public static final <T> void sort(@NotNull T[] receiver, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Arrays.sort(receiver, i, i2);
    }

    public static /* bridge */ /* synthetic */ void sort$default(byte[] bArr, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = bArr.length;
        }
        ArraysKt.sort(bArr, i, i2);
    }

    public static final void sort(@NotNull byte[] receiver, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Arrays.sort(receiver, i, i2);
    }

    public static /* bridge */ /* synthetic */ void sort$default(short[] sArr, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = sArr.length;
        }
        ArraysKt.sort(sArr, i, i2);
    }

    public static final void sort(@NotNull short[] receiver, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Arrays.sort(receiver, i, i2);
    }

    public static /* bridge */ /* synthetic */ void sort$default(int[] iArr, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = iArr.length;
        }
        ArraysKt.sort(iArr, i, i2);
    }

    public static final void sort(@NotNull int[] receiver, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Arrays.sort(receiver, i, i2);
    }

    public static /* bridge */ /* synthetic */ void sort$default(long[] jArr, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = jArr.length;
        }
        ArraysKt.sort(jArr, i, i2);
    }

    public static final void sort(@NotNull long[] receiver, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Arrays.sort(receiver, i, i2);
    }

    public static /* bridge */ /* synthetic */ void sort$default(float[] fArr, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = fArr.length;
        }
        ArraysKt.sort(fArr, i, i2);
    }

    public static final void sort(@NotNull float[] receiver, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Arrays.sort(receiver, i, i2);
    }

    public static /* bridge */ /* synthetic */ void sort$default(double[] dArr, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = dArr.length;
        }
        ArraysKt.sort(dArr, i, i2);
    }

    public static final void sort(@NotNull double[] receiver, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Arrays.sort(receiver, i, i2);
    }

    public static /* bridge */ /* synthetic */ void sort$default(char[] cArr, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = cArr.length;
        }
        ArraysKt.sort(cArr, i, i2);
    }

    public static final void sort(@NotNull char[] receiver, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Arrays.sort(receiver, i, i2);
    }

    public static final <T> void sortWith(@NotNull T[] receiver, @NotNull Comparator<? super T> comparator) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        if (receiver.length > 1) {
            Arrays.sort(receiver, comparator);
        }
    }

    public static /* bridge */ /* synthetic */ void sortWith$default(Object[] objArr, Comparator comparator, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = objArr.length;
        }
        ArraysKt.sortWith(objArr, comparator, i, i2);
    }

    public static final <T> void sortWith(@NotNull T[] receiver, @NotNull Comparator<? super T> comparator, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        Arrays.sort(receiver, i, i2, comparator);
    }

    @NotNull
    public static final Byte[] toTypedArray(@NotNull byte[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Byte[] bArr = new Byte[receiver.length];
        int length = receiver.length;
        for (int i = 0; i < length; i++) {
            bArr[i] = Byte.valueOf(receiver[i]);
        }
        return bArr;
    }

    @NotNull
    public static final Short[] toTypedArray(@NotNull short[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Short[] shArr = new Short[receiver.length];
        int length = receiver.length;
        for (int i = 0; i < length; i++) {
            shArr[i] = Short.valueOf(receiver[i]);
        }
        return shArr;
    }

    @NotNull
    public static final Integer[] toTypedArray(@NotNull int[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Integer[] numArr = new Integer[receiver.length];
        int length = receiver.length;
        for (int i = 0; i < length; i++) {
            numArr[i] = Integer.valueOf(receiver[i]);
        }
        return numArr;
    }

    @NotNull
    public static final Long[] toTypedArray(@NotNull long[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Long[] lArr = new Long[receiver.length];
        int length = receiver.length;
        for (int i = 0; i < length; i++) {
            lArr[i] = Long.valueOf(receiver[i]);
        }
        return lArr;
    }

    @NotNull
    public static final Float[] toTypedArray(@NotNull float[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Float[] fArr = new Float[receiver.length];
        int length = receiver.length;
        for (int i = 0; i < length; i++) {
            fArr[i] = Float.valueOf(receiver[i]);
        }
        return fArr;
    }

    @NotNull
    public static final Double[] toTypedArray(@NotNull double[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Double[] dArr = new Double[receiver.length];
        int length = receiver.length;
        for (int i = 0; i < length; i++) {
            dArr[i] = Double.valueOf(receiver[i]);
        }
        return dArr;
    }

    @NotNull
    public static final Boolean[] toTypedArray(@NotNull boolean[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Boolean[] boolArr = new Boolean[receiver.length];
        int length = receiver.length;
        for (int i = 0; i < length; i++) {
            boolArr[i] = Boolean.valueOf(receiver[i]);
        }
        return boolArr;
    }

    @NotNull
    public static final Character[] toTypedArray(@NotNull char[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Character[] chArr = new Character[receiver.length];
        int length = receiver.length;
        for (int i = 0; i < length; i++) {
            chArr[i] = Character.valueOf(receiver[i]);
        }
        return chArr;
    }

    @NotNull
    public static final <T extends Comparable<? super T>> SortedSet<T> toSortedSet(@NotNull T[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return (SortedSet) ArraysKt.toCollection(receiver, new TreeSet());
    }

    @NotNull
    public static final SortedSet<Byte> toSortedSet(@NotNull byte[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return (SortedSet) ArraysKt.toCollection(receiver, new TreeSet());
    }

    @NotNull
    public static final SortedSet<Short> toSortedSet(@NotNull short[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return (SortedSet) ArraysKt.toCollection(receiver, new TreeSet());
    }

    @NotNull
    public static final SortedSet<Integer> toSortedSet(@NotNull int[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return (SortedSet) ArraysKt.toCollection(receiver, new TreeSet());
    }

    @NotNull
    public static final SortedSet<Long> toSortedSet(@NotNull long[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return (SortedSet) ArraysKt.toCollection(receiver, new TreeSet());
    }

    @NotNull
    public static final SortedSet<Float> toSortedSet(@NotNull float[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return (SortedSet) ArraysKt.toCollection(receiver, new TreeSet());
    }

    @NotNull
    public static final SortedSet<Double> toSortedSet(@NotNull double[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return (SortedSet) ArraysKt.toCollection(receiver, new TreeSet());
    }

    @NotNull
    public static final SortedSet<Boolean> toSortedSet(@NotNull boolean[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return (SortedSet) ArraysKt.toCollection(receiver, new TreeSet());
    }

    @NotNull
    public static final SortedSet<Character> toSortedSet(@NotNull char[] receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return (SortedSet) ArraysKt.toCollection(receiver, new TreeSet());
    }

    @NotNull
    public static final <T> SortedSet<T> toSortedSet(@NotNull T[] receiver, @NotNull Comparator<? super T> comparator) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        return (SortedSet) ArraysKt.toCollection(receiver, new TreeSet(comparator));
    }
}
