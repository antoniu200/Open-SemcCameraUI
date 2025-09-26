// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.collections;

import java.io.Serializable;
import java.util.List;
import kotlin.jvm.internal.TypeIntrinsics;
import org.jetbrains.annotations.Nullable;
import kotlin.jvm.JvmName;
import kotlin.sequences.Sequence;
import java.util.Collection;
import kotlin.PublishedApi;
import kotlin.Pair;
import java.util.HashMap;
import kotlin.SinceKotlin;
import kotlin.jvm.functions.Function0;
import java.util.Iterator;
import java.util.LinkedHashMap;
import kotlin.jvm.functions.Function1;
import kotlin.TypeCastException;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import java.util.Map;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000~\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010%\n\u0000\n\u0002\u0010&\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010(\n\u0002\u0010)\n\u0002\u0010'\n\u0002\b\u000b\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0016\u001a\u001e\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005\u001a1\u0010\u0006\u001a\u001e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0007j\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u0005`\b\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005H\u0087\b\u001a_\u0010\u0006\u001a\u001e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0007j\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u0005`\b\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u00052*\u0010\t\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u000b0\n\"\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u000b¢\u0006\u0002\u0010\f\u001a1\u0010\r\u001a\u001e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u000ej\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u0005`\u000f\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005H\u0087\b\u001a_\u0010\r\u001a\u001e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u000ej\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u0005`\u000f\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u00052*\u0010\t\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u000b0\n\"\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u000b¢\u0006\u0002\u0010\u0010\u001a\u0010\u0010\u0011\u001a\u00020\u00012\u0006\u0010\u0012\u001a\u00020\u0001H\u0001\u001a!\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005H\u0087\b\u001aO\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u00052*\u0010\t\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u000b0\n\"\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u000b¢\u0006\u0002\u0010\u0014\u001a!\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0016\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005H\u0087\b\u001aO\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0016\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u00052*\u0010\t\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u000b0\n\"\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u000b¢\u0006\u0002\u0010\u0014\u001a*\u0010\u0017\u001a\u0002H\u0004\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0018H\u0087\n¢\u0006\u0002\u0010\u0019\u001a*\u0010\u001a\u001a\u0002H\u0005\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0018H\u0087\n¢\u0006\u0002\u0010\u0019\u001a9\u0010\u001b\u001a\u00020\u001c\"\t\b\u0000\u0010\u0004¢\u0006\u0002\b\u001d\"\u0004\b\u0001\u0010\u0005*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00032\u0006\u0010\u001e\u001a\u0002H\u0004H\u0087\n¢\u0006\u0002\u0010\u001f\u001a1\u0010 \u001a\u00020\u001c\"\t\b\u0000\u0010\u0004¢\u0006\u0002\b\u001d*\u000e\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0002\b\u00030\u00032\u0006\u0010\u001e\u001a\u0002H\u0004H\u0087\b¢\u0006\u0002\u0010\u001f\u001a7\u0010!\u001a\u00020\u001c\"\u0004\b\u0000\u0010\u0004\"\t\b\u0001\u0010\u0005¢\u0006\u0002\b\u001d*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00032\u0006\u0010\"\u001a\u0002H\u0005H\u0087\b¢\u0006\u0002\u0010\u001f\u001aS\u0010#\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00032\u001e\u0010$\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0018\u0012\u0004\u0012\u00020\u001c0%H\u0086\b\u001aG\u0010&\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00032\u0012\u0010$\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u00020\u001c0%H\u0086\b\u001aS\u0010'\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00032\u001e\u0010$\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0018\u0012\u0004\u0012\u00020\u001c0%H\u0086\b\u001an\u0010(\u001a\u0002H)\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005\"\u0018\b\u0002\u0010)*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0004\u0012\u0006\b\u0000\u0012\u0002H\u00050\u0016*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00032\u0006\u0010*\u001a\u0002H)2\u001e\u0010$\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0018\u0012\u0004\u0012\u00020\u001c0%H\u0086\b¢\u0006\u0002\u0010+\u001an\u0010,\u001a\u0002H)\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005\"\u0018\b\u0002\u0010)*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0004\u0012\u0006\b\u0000\u0012\u0002H\u00050\u0016*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00032\u0006\u0010*\u001a\u0002H)2\u001e\u0010$\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0018\u0012\u0004\u0012\u00020\u001c0%H\u0086\b¢\u0006\u0002\u0010+\u001aG\u0010-\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00032\u0012\u0010$\u001a\u000e\u0012\u0004\u0012\u0002H\u0005\u0012\u0004\u0012\u00020\u001c0%H\u0086\b\u001a;\u0010.\u001a\u0004\u0018\u0001H\u0005\"\t\b\u0000\u0010\u0004¢\u0006\u0002\b\u001d\"\u0004\b\u0001\u0010\u0005*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00032\u0006\u0010\u001e\u001a\u0002H\u0004H\u0087\n¢\u0006\u0002\u0010/\u001a@\u00100\u001a\u0002H\u0005\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00032\u0006\u0010\u001e\u001a\u0002H\u00042\f\u00101\u001a\b\u0012\u0004\u0012\u0002H\u000502H\u0087\b¢\u0006\u0002\u00103\u001a@\u00104\u001a\u0002H\u0005\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00032\u0006\u0010\u001e\u001a\u0002H\u00042\f\u00101\u001a\b\u0012\u0004\u0012\u0002H\u000502H\u0080\b¢\u0006\u0002\u00103\u001a@\u00105\u001a\u0002H\u0005\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00162\u0006\u0010\u001e\u001a\u0002H\u00042\f\u00101\u001a\b\u0012\u0004\u0012\u0002H\u000502H\u0086\b¢\u0006\u0002\u00103\u001a1\u00106\u001a\u0002H\u0005\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00032\u0006\u0010\u001e\u001a\u0002H\u0004H\u0007¢\u0006\u0002\u0010/\u001a'\u00107\u001a\u00020\u001c\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003H\u0087\b\u001a9\u00108\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u001809\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003H\u0087\n\u001a<\u00108\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050;0:\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0016H\u0087\n¢\u0006\u0002\b<\u001aY\u0010=\u001a\u000e\u0012\u0004\u0012\u0002H>\u0012\u0004\u0012\u0002H\u00050\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005\"\u0004\b\u0002\u0010>*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00032\u001e\u0010?\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0018\u0012\u0004\u0012\u0002H>0%H\u0086\b\u001at\u0010@\u001a\u0002H)\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005\"\u0004\b\u0002\u0010>\"\u0018\b\u0003\u0010)*\u0012\u0012\u0006\b\u0000\u0012\u0002H>\u0012\u0006\b\u0000\u0012\u0002H\u00050\u0016*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00032\u0006\u0010*\u001a\u0002H)2\u001e\u0010?\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0018\u0012\u0004\u0012\u0002H>0%H\u0086\b¢\u0006\u0002\u0010+\u001aY\u0010A\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H>0\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005\"\u0004\b\u0002\u0010>*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00032\u001e\u0010?\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0018\u0012\u0004\u0012\u0002H>0%H\u0086\b\u001at\u0010B\u001a\u0002H)\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005\"\u0004\b\u0002\u0010>\"\u0018\b\u0003\u0010)*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0004\u0012\u0006\b\u0000\u0012\u0002H>0\u0016*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00032\u0006\u0010*\u001a\u0002H)2\u001e\u0010?\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0018\u0012\u0004\u0012\u0002H>0%H\u0086\b¢\u0006\u0002\u0010+\u001a@\u0010C\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00032\u0006\u0010\u001e\u001a\u0002H\u0004H\u0087\u0002¢\u0006\u0002\u0010D\u001aH\u0010C\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00032\u000e\u0010E\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00040\nH\u0087\u0002¢\u0006\u0002\u0010F\u001aA\u0010C\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00032\f\u0010E\u001a\b\u0012\u0004\u0012\u0002H\u00040GH\u0087\u0002\u001aA\u0010C\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00032\f\u0010E\u001a\b\u0012\u0004\u0012\u0002H\u00040HH\u0087\u0002\u001a2\u0010I\u001a\u00020J\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00162\u0006\u0010\u001e\u001a\u0002H\u0004H\u0087\n¢\u0006\u0002\u0010K\u001a:\u0010I\u001a\u00020J\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00162\u000e\u0010E\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00040\nH\u0087\n¢\u0006\u0002\u0010L\u001a3\u0010I\u001a\u00020J\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00162\f\u0010E\u001a\b\u0012\u0004\u0012\u0002H\u00040GH\u0087\n\u001a3\u0010I\u001a\u00020J\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00162\f\u0010E\u001a\b\u0012\u0004\u0012\u0002H\u00040HH\u0087\n\u001a0\u0010M\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003H\u0000\u001a3\u0010N\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u0010\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u0005\u0018\u00010\u0003H\u0087\b\u001aT\u0010O\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00032\u001a\u0010\t\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u000b0\nH\u0086\u0002¢\u0006\u0002\u0010P\u001aG\u0010O\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00032\u0012\u0010Q\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u000bH\u0086\u0002\u001aM\u0010O\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00032\u0018\u0010\t\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u000b0GH\u0086\u0002\u001aI\u0010O\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00032\u0014\u0010R\u001a\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003H\u0086\u0002\u001aM\u0010O\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00032\u0018\u0010\t\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u000b0HH\u0086\u0002\u001aJ\u0010S\u001a\u00020J\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0004\u0012\u0006\b\u0000\u0012\u0002H\u00050\u00162\u001a\u0010\t\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u000b0\nH\u0087\n¢\u0006\u0002\u0010T\u001a=\u0010S\u001a\u00020J\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0004\u0012\u0006\b\u0000\u0012\u0002H\u00050\u00162\u0012\u0010Q\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u000bH\u0087\n\u001aC\u0010S\u001a\u00020J\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0004\u0012\u0006\b\u0000\u0012\u0002H\u00050\u00162\u0018\u0010\t\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u000b0GH\u0087\n\u001a=\u0010S\u001a\u00020J\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0004\u0012\u0006\b\u0000\u0012\u0002H\u00050\u00162\u0012\u0010R\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003H\u0087\n\u001aC\u0010S\u001a\u00020J\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0004\u0012\u0006\b\u0000\u0012\u0002H\u00050\u00162\u0018\u0010\t\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u000b0HH\u0087\n\u001aG\u0010U\u001a\u00020J\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0004\u0012\u0006\b\u0000\u0012\u0002H\u00050\u00162\u001a\u0010\t\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u000b0\n¢\u0006\u0002\u0010T\u001a@\u0010U\u001a\u00020J\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0004\u0012\u0006\b\u0000\u0012\u0002H\u00050\u00162\u0018\u0010\t\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u000b0G\u001a@\u0010U\u001a\u00020J\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0004\u0012\u0006\b\u0000\u0012\u0002H\u00050\u00162\u0018\u0010\t\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u000b0H\u001a;\u0010V\u001a\u0004\u0018\u0001H\u0005\"\t\b\u0000\u0010\u0004¢\u0006\u0002\b\u001d\"\u0004\b\u0001\u0010\u0005*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00162\u0006\u0010\u001e\u001a\u0002H\u0004H\u0087\b¢\u0006\u0002\u0010/\u001a:\u0010W\u001a\u00020J\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00162\u0006\u0010\u001e\u001a\u0002H\u00042\u0006\u0010\"\u001a\u0002H\u0005H\u0087\n¢\u0006\u0002\u0010X\u001a;\u0010Y\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u000b0\n¢\u0006\u0002\u0010\u0014\u001aQ\u0010Y\u001a\u0002H)\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005\"\u0018\b\u0002\u0010)*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0004\u0012\u0006\b\u0000\u0012\u0002H\u00050\u0016*\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u000b0\n2\u0006\u0010*\u001a\u0002H)¢\u0006\u0002\u0010Z\u001a4\u0010Y\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u000b0G\u001aO\u0010Y\u001a\u0002H)\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005\"\u0018\b\u0002\u0010)*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0004\u0012\u0006\b\u0000\u0012\u0002H\u00050\u0016*\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u000b0G2\u0006\u0010*\u001a\u0002H)¢\u0006\u0002\u0010[\u001a2\u0010Y\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003H\u0007\u001aM\u0010Y\u001a\u0002H)\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005\"\u0018\b\u0002\u0010)*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0004\u0012\u0006\b\u0000\u0012\u0002H\u00050\u0016*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u00032\u0006\u0010*\u001a\u0002H)H\u0007¢\u0006\u0002\u0010\\\u001a4\u0010Y\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u000b0H\u001aO\u0010Y\u001a\u0002H)\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005\"\u0018\b\u0002\u0010)*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0004\u0012\u0006\b\u0000\u0012\u0002H\u00050\u0016*\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u000b0H2\u0006\u0010*\u001a\u0002H)¢\u0006\u0002\u0010]\u001a2\u0010^\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0016\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0003H\u0007\u001a1\u0010_\u001a\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u000b\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0005*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00050\u0018H\u0087\b\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000¨\u0006`" }, d2 = { "INT_MAX_POWER_OF_TWO", "", "emptyMap", "", "K", "V", "hashMapOf", "Ljava/util/HashMap;", "Lkotlin/collections/HashMap;", "pairs", "", "Lkotlin/Pair;", "([Lkotlin/Pair;)Ljava/util/HashMap;", "linkedMapOf", "Ljava/util/LinkedHashMap;", "Lkotlin/collections/LinkedHashMap;", "([Lkotlin/Pair;)Ljava/util/LinkedHashMap;", "mapCapacity", "expectedSize", "mapOf", "([Lkotlin/Pair;)Ljava/util/Map;", "mutableMapOf", "", "component1", "", "(Ljava/util/Map$Entry;)Ljava/lang/Object;", "component2", "contains", "", "Lkotlin/internal/OnlyInputTypes;", "key", "(Ljava/util/Map;Ljava/lang/Object;)Z", "containsKey", "containsValue", "value", "filter", "predicate", "Lkotlin/Function1;", "filterKeys", "filterNot", "filterNotTo", "M", "destination", "(Ljava/util/Map;Ljava/util/Map;Lkotlin/jvm/functions/Function1;)Ljava/util/Map;", "filterTo", "filterValues", "get", "(Ljava/util/Map;Ljava/lang/Object;)Ljava/lang/Object;", "getOrElse", "defaultValue", "Lkotlin/Function0;", "(Ljava/util/Map;Ljava/lang/Object;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "getOrElseNullable", "getOrPut", "getValue", "isNotEmpty", "iterator", "", "", "", "mutableIterator", "mapKeys", "R", "transform", "mapKeysTo", "mapValues", "mapValuesTo", "minus", "(Ljava/util/Map;Ljava/lang/Object;)Ljava/util/Map;", "keys", "(Ljava/util/Map;[Ljava/lang/Object;)Ljava/util/Map;", "", "Lkotlin/sequences/Sequence;", "minusAssign", "", "(Ljava/util/Map;Ljava/lang/Object;)V", "(Ljava/util/Map;[Ljava/lang/Object;)V", "optimizeReadOnlyMap", "orEmpty", "plus", "(Ljava/util/Map;[Lkotlin/Pair;)Ljava/util/Map;", "pair", "map", "plusAssign", "(Ljava/util/Map;[Lkotlin/Pair;)V", "putAll", "remove", "set", "(Ljava/util/Map;Ljava/lang/Object;Ljava/lang/Object;)V", "toMap", "([Lkotlin/Pair;Ljava/util/Map;)Ljava/util/Map;", "(Ljava/lang/Iterable;Ljava/util/Map;)Ljava/util/Map;", "(Ljava/util/Map;Ljava/util/Map;)Ljava/util/Map;", "(Lkotlin/sequences/Sequence;Ljava/util/Map;)Ljava/util/Map;", "toMutableMap", "toPair", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/collections/MapsKt")
class MapsKt__MapsKt extends MapsKt__MapsJVMKt
{
    private static final int INT_MAX_POWER_OF_TWO = 1073741824;
    
    public MapsKt__MapsKt() {
    }
    
    @InlineOnly
    private static final <K, V> K component1(@NotNull final Map.Entry<? extends K, ? extends V> entry) {
        Intrinsics.checkParameterIsNotNull(entry, "$receiver");
        return (K)entry.getKey();
    }
    
    @InlineOnly
    private static final <K, V> V component2(@NotNull final Map.Entry<? extends K, ? extends V> entry) {
        Intrinsics.checkParameterIsNotNull(entry, "$receiver");
        return (V)entry.getValue();
    }
    
    @InlineOnly
    private static final <K, V> boolean contains(@NotNull final Map<? extends K, ? extends V> map, final K k) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        return map.containsKey(k);
    }
    
    @InlineOnly
    private static final <K> boolean containsKey(@NotNull final Map<? extends K, ?> map, final K k) {
        if (map == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.Map<K, *>");
        }
        return map.containsKey(k);
    }
    
    @InlineOnly
    private static final <K, V> boolean containsValue(@NotNull final Map<K, ? extends V> map, final V v) {
        return map.containsValue(v);
    }
    
    @NotNull
    public static final <K, V> Map<K, V> emptyMap() {
        final EmptyMap instance = EmptyMap.INSTANCE;
        if (instance == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.Map<K, V>");
        }
        return instance;
    }
    
    @NotNull
    public static final <K, V> Map<K, V> filter(@NotNull final Map<? extends K, ? extends V> map, @NotNull final Function1<? super Map.Entry<? extends K, ? extends V>, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        final Map map2 = new LinkedHashMap();
        for (final Map.Entry<Object, ?> entry : map.entrySet()) {
            if (function1.invoke((Object)entry)) {
                map2.put(entry.getKey(), entry.getValue());
            }
        }
        return map2;
    }
    
    @NotNull
    public static final <K, V> Map<K, V> filterKeys(@NotNull final Map<? extends K, ? extends V> map, @NotNull final Function1<? super K, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        final LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (final Map.Entry<? super K, V> entry : map.entrySet()) {
            if (function1.invoke(entry.getKey())) {
                linkedHashMap.put(entry.getKey(), entry.getValue());
            }
        }
        return linkedHashMap;
    }
    
    @NotNull
    public static final <K, V> Map<K, V> filterNot(@NotNull final Map<? extends K, ? extends V> map, @NotNull final Function1<? super Map.Entry<? extends K, ? extends V>, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        final Map map2 = new LinkedHashMap();
        for (final Map.Entry<Object, ?> entry : map.entrySet()) {
            if (!function1.invoke((Object)entry)) {
                map2.put(entry.getKey(), entry.getValue());
            }
        }
        return map2;
    }
    
    @NotNull
    public static final <K, V, M extends Map<? super K, ? super V>> M filterNotTo(@NotNull final Map<? extends K, ? extends V> map, @NotNull final M m, @NotNull final Function1<? super Map.Entry<? extends K, ? extends V>, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        Intrinsics.checkParameterIsNotNull(m, "destination");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        for (final Map.Entry<? super K, ?> entry : map.entrySet()) {
            if (!function1.invoke((Object)entry)) {
                m.put(entry.getKey(), (Object)entry.getValue());
            }
        }
        return m;
    }
    
    @NotNull
    public static final <K, V, M extends Map<? super K, ? super V>> M filterTo(@NotNull final Map<? extends K, ? extends V> map, @NotNull final M m, @NotNull final Function1<? super Map.Entry<? extends K, ? extends V>, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        Intrinsics.checkParameterIsNotNull(m, "destination");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        for (final Map.Entry<? super K, ?> entry : map.entrySet()) {
            if (function1.invoke((Object)entry)) {
                m.put(entry.getKey(), (Object)entry.getValue());
            }
        }
        return m;
    }
    
    @NotNull
    public static final <K, V> Map<K, V> filterValues(@NotNull final Map<? extends K, ? extends V> map, @NotNull final Function1<? super V, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        final LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (final Map.Entry<K, ? super V> entry : map.entrySet()) {
            if (function1.invoke(entry.getValue())) {
                linkedHashMap.put(entry.getKey(), entry.getValue());
            }
        }
        return linkedHashMap;
    }
    
    @InlineOnly
    private static final <K, V> V get(@NotNull final Map<? extends K, ? extends V> map, final K k) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        return (V)map.get(k);
    }
    
    @InlineOnly
    private static final <K, V> V getOrElse(@NotNull final Map<K, ? extends V> map, final K k, final Function0<? extends V> function0) {
        V v = (V)map.get(k);
        if (v == null) {
            v = (V)function0.invoke();
        }
        return v;
    }
    
    public static final <K, V> V getOrElseNullable(@NotNull final Map<K, ? extends V> map, final K k, @NotNull final Function0<? extends V> function0) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        Intrinsics.checkParameterIsNotNull(function0, "defaultValue");
        final V value = map.get(k);
        if (value == null && !map.containsKey(k)) {
            return (V)function0.invoke();
        }
        return value;
    }
    
    public static final <K, V> V getOrPut(@NotNull final Map<K, V> map, final K k, @NotNull final Function0<? extends V> function0) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        Intrinsics.checkParameterIsNotNull(function0, "defaultValue");
        V v;
        if ((v = map.get(k)) == null) {
            v = (V)function0.invoke();
            map.put(k, v);
        }
        return v;
    }
    
    @SinceKotlin(version = "1.1")
    public static final <K, V> V getValue(@NotNull final Map<K, ? extends V> map, final K k) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        return MapsKt__MapWithDefaultKt.getOrImplicitDefaultNullable(map, k);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <K, V> HashMap<K, V> hashMapOf() {
        return new HashMap<K, V>();
    }
    
    @NotNull
    public static final <K, V> HashMap<K, V> hashMapOf(@NotNull final Pair<? extends K, ? extends V>... array) {
        Intrinsics.checkParameterIsNotNull(array, "pairs");
        final HashMap hashMap = new HashMap<Object, Object>(mapCapacity(((Pair<? extends K, ? extends V>[])array).length));
        putAll((Map<? super Object, ? super Object>)hashMap, (Pair<?, ?>[])array);
        return (HashMap<K, V>)hashMap;
    }
    
    @InlineOnly
    private static final <K, V> boolean isNotEmpty(@NotNull final Map<? extends K, ? extends V> map) {
        return map.isEmpty() ^ true;
    }
    
    @InlineOnly
    private static final <K, V> Iterator<Map.Entry<K, V>> iterator(@NotNull final Map<? extends K, ? extends V> map) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        return (Iterator<Map.Entry<K, V>>)map.entrySet().iterator();
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <K, V> LinkedHashMap<K, V> linkedMapOf() {
        return new LinkedHashMap<K, V>();
    }
    
    @NotNull
    public static final <K, V> LinkedHashMap<K, V> linkedMapOf(@NotNull final Pair<? extends K, ? extends V>... array) {
        Intrinsics.checkParameterIsNotNull(array, "pairs");
        return toMap((Pair<?, ?>[])array, (LinkedHashMap<K, V>)new LinkedHashMap(mapCapacity(((Pair<? extends K, ? extends V>[])array).length)));
    }
    
    @PublishedApi
    public static final int mapCapacity(final int n) {
        if (n < 3) {
            return n + 1;
        }
        if (n < 1073741824) {
            return n + n / 3;
        }
        return Integer.MAX_VALUE;
    }
    
    @NotNull
    public static final <K, V, R> Map<R, V> mapKeys(@NotNull final Map<? extends K, ? extends V> map, @NotNull final Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        final Map map2 = new LinkedHashMap(mapCapacity(map.size()));
        for (final Object next : map.entrySet()) {
            map2.put(function1.invoke((Object)next), ((Map.Entry<K, Object>)next).getValue());
        }
        return map2;
    }
    
    @NotNull
    public static final <K, V, R, M extends Map<? super R, ? super V>> M mapKeysTo(@NotNull final Map<? extends K, ? extends V> map, @NotNull final M m, @NotNull final Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        Intrinsics.checkParameterIsNotNull(m, "destination");
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        for (final Object next : map.entrySet()) {
            m.put((Object)function1.invoke((Object)next), ((Map.Entry<K, ? super V>)next).getValue());
        }
        return m;
    }
    
    @InlineOnly
    private static final <K, V> Map<K, V> mapOf() {
        return (Map<K, V>)emptyMap();
    }
    
    @NotNull
    public static final <K, V> Map<K, V> mapOf(@NotNull final Pair<? extends K, ? extends V>... array) {
        Intrinsics.checkParameterIsNotNull(array, "pairs");
        Map<Object, Object> map;
        if (((Pair<? extends K, ? extends V>[])array).length > 0) {
            map = toMap((Pair<?, ?>[])array, (Map<Object, Object>)new LinkedHashMap(mapCapacity(((Pair<? extends K, ? extends V>[])array).length)));
        }
        else {
            map = emptyMap();
        }
        return (Map<K, V>)map;
    }
    
    @NotNull
    public static final <K, V, R> Map<K, R> mapValues(@NotNull final Map<? extends K, ? extends V> map, @NotNull final Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        final Map map2 = new LinkedHashMap(mapCapacity(map.size()));
        for (final Object next : map.entrySet()) {
            map2.put(((Map.Entry<Object, V>)next).getKey(), function1.invoke((Object)next));
        }
        return map2;
    }
    
    @NotNull
    public static final <K, V, R, M extends Map<? super K, ? super R>> M mapValuesTo(@NotNull final Map<? extends K, ? extends V> map, @NotNull final M m, @NotNull final Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        Intrinsics.checkParameterIsNotNull(m, "destination");
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        for (final Object next : map.entrySet()) {
            m.put(((Map.Entry<? super K, V>)next).getKey(), (Object)function1.invoke((Object)next));
        }
        return m;
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <K, V> Map<K, V> minus(@NotNull final Map<? extends K, ? extends V> map, @NotNull final Iterable<? extends K> iterable) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        Intrinsics.checkParameterIsNotNull(iterable, "keys");
        final Map<Object, Object> mutableMap = toMutableMap((Map<?, ?>)map);
        CollectionsKt__MutableCollectionsKt.removeAll((Collection<? super Object>)mutableMap.keySet(), (Iterable<?>)iterable);
        return optimizeReadOnlyMap((Map<K, ? extends V>)mutableMap);
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <K, V> Map<K, V> minus(@NotNull final Map<? extends K, ? extends V> map, final K k) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        final Map<Object, Object> mutableMap = toMutableMap((Map<?, ?>)map);
        mutableMap.remove(k);
        return optimizeReadOnlyMap((Map<K, ? extends V>)mutableMap);
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <K, V> Map<K, V> minus(@NotNull final Map<? extends K, ? extends V> map, @NotNull final Sequence<? extends K> sequence) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        Intrinsics.checkParameterIsNotNull(sequence, "keys");
        final Map<Object, Object> mutableMap = toMutableMap((Map<?, ?>)map);
        CollectionsKt__MutableCollectionsKt.removeAll((Collection<? super Object>)mutableMap.keySet(), (Sequence<?>)sequence);
        return optimizeReadOnlyMap((Map<K, ? extends V>)mutableMap);
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <K, V> Map<K, V> minus(@NotNull final Map<? extends K, ? extends V> map, @NotNull final K[] array) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "keys");
        final Map<Object, Object> mutableMap = toMutableMap((Map<?, ?>)map);
        CollectionsKt__MutableCollectionsKt.removeAll(mutableMap.keySet(), array);
        return optimizeReadOnlyMap((Map<K, ? extends V>)mutableMap);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <K, V> void minusAssign(@NotNull final Map<K, V> map, final Iterable<? extends K> iterable) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        CollectionsKt__MutableCollectionsKt.removeAll((Collection<? super Object>)map.keySet(), (Iterable<?>)iterable);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <K, V> void minusAssign(@NotNull final Map<K, V> map, final K k) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        map.remove(k);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <K, V> void minusAssign(@NotNull final Map<K, V> map, final Sequence<? extends K> sequence) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        CollectionsKt__MutableCollectionsKt.removeAll((Collection<? super Object>)map.keySet(), (Sequence<?>)sequence);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <K, V> void minusAssign(@NotNull final Map<K, V> map, final K[] array) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        CollectionsKt__MutableCollectionsKt.removeAll(map.keySet(), array);
    }
    
    @InlineOnly
    @JvmName(name = "mutableIterator")
    private static final <K, V> Iterator<Map.Entry<K, V>> mutableIterator(@NotNull final Map<K, V> map) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        return map.entrySet().iterator();
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <K, V> Map<K, V> mutableMapOf() {
        return new LinkedHashMap<K, V>();
    }
    
    @NotNull
    public static final <K, V> Map<K, V> mutableMapOf(@NotNull final Pair<? extends K, ? extends V>... array) {
        Intrinsics.checkParameterIsNotNull(array, "pairs");
        final Map map = new LinkedHashMap(mapCapacity(((Pair<? extends K, ? extends V>[])array).length));
        putAll((Map<? super Object, ? super Object>)map, (Pair<?, ?>[])array);
        return map;
    }
    
    @NotNull
    public static final <K, V> Map<K, V> optimizeReadOnlyMap(@NotNull Map<K, ? extends V> o) {
        Intrinsics.checkParameterIsNotNull(o, "$receiver");
        switch (((Map)o).size()) {
            case 1: {
                o = MapsKt__MapsJVMKt.toSingletonMap((Map<?, ?>)o);
                break;
            }
            case 0: {
                o = emptyMap();
                break;
            }
        }
        return (Map<K, V>)o;
    }
    
    @InlineOnly
    private static final <K, V> Map<K, V> orEmpty(@Nullable Map<K, ? extends V> emptyMap) {
        if (emptyMap == null) {
            emptyMap = emptyMap();
        }
        return emptyMap;
    }
    
    @NotNull
    public static final <K, V> Map<K, V> plus(@NotNull final Map<? extends K, ? extends V> m, @NotNull final Iterable<? extends Pair<? extends K, ? extends V>> iterable) {
        Intrinsics.checkParameterIsNotNull(m, "$receiver");
        Intrinsics.checkParameterIsNotNull(iterable, "pairs");
        Map<Object, Object> map;
        if (m.isEmpty()) {
            map = toMap((Iterable<? extends Pair<?, ?>>)iterable);
        }
        else {
            map = new LinkedHashMap<Object, Object>(m);
            putAll((Map<? super Object, ? super Object>)map, (Iterable<? extends Pair<?, ?>>)iterable);
        }
        return (Map<K, V>)map;
    }
    
    @NotNull
    public static final <K, V> Map<K, V> plus(@NotNull final Map<? extends K, ? extends V> m, @NotNull final Map<? extends K, ? extends V> i) {
        Intrinsics.checkParameterIsNotNull(m, "$receiver");
        Intrinsics.checkParameterIsNotNull(i, "map");
        final LinkedHashMap linkedHashMap = new LinkedHashMap((Map<? extends K, ? extends V>)m);
        linkedHashMap.putAll(i);
        return linkedHashMap;
    }
    
    @NotNull
    public static final <K, V> Map<K, V> plus(@NotNull final Map<? extends K, ? extends V> m, @NotNull final Pair<? extends K, ? extends V> pair) {
        Intrinsics.checkParameterIsNotNull(m, "$receiver");
        Intrinsics.checkParameterIsNotNull(pair, "pair");
        Map<Object, Object> map;
        if (m.isEmpty()) {
            map = MapsKt__MapsJVMKt.mapOf((Pair<?, ?>)pair);
        }
        else {
            final LinkedHashMap linkedHashMap = new LinkedHashMap((Map<? extends K, ? extends V>)m);
            linkedHashMap.put(pair.getFirst(), pair.getSecond());
            map = linkedHashMap;
        }
        return (Map<K, V>)map;
    }
    
    @NotNull
    public static final <K, V> Map<K, V> plus(@NotNull final Map<? extends K, ? extends V> m, @NotNull final Sequence<? extends Pair<? extends K, ? extends V>> sequence) {
        Intrinsics.checkParameterIsNotNull(m, "$receiver");
        Intrinsics.checkParameterIsNotNull(sequence, "pairs");
        final Map map = new LinkedHashMap(m);
        putAll((Map<? super Object, ? super Object>)map, (Sequence<? extends Pair<?, ?>>)sequence);
        return (Map<K, V>)optimizeReadOnlyMap((Map<K, ?>)map);
    }
    
    @NotNull
    public static final <K, V> Map<K, V> plus(@NotNull final Map<? extends K, ? extends V> m, @NotNull final Pair<? extends K, ? extends V>[] array) {
        Intrinsics.checkParameterIsNotNull(m, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "pairs");
        Map<Object, Object> map;
        if (m.isEmpty()) {
            map = toMap((Pair<?, ?>[])array);
        }
        else {
            map = new LinkedHashMap<Object, Object>(m);
            putAll((Map<? super Object, ? super Object>)map, (Pair<?, ?>[])array);
        }
        return (Map<K, V>)map;
    }
    
    @InlineOnly
    private static final <K, V> void plusAssign(@NotNull final Map<? super K, ? super V> map, final Iterable<? extends Pair<? extends K, ? extends V>> iterable) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        putAll((Map<? super Object, ? super Object>)map, (Iterable<? extends Pair<?, ?>>)iterable);
    }
    
    @InlineOnly
    private static final <K, V> void plusAssign(@NotNull final Map<? super K, ? super V> map, final Map<K, ? extends V> map2) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        map.putAll((Map<? extends K, ? extends V>)map2);
    }
    
    @InlineOnly
    private static final <K, V> void plusAssign(@NotNull final Map<? super K, ? super V> map, final Pair<? extends K, ? extends V> pair) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        map.put((Object)pair.getFirst(), (Object)pair.getSecond());
    }
    
    @InlineOnly
    private static final <K, V> void plusAssign(@NotNull final Map<? super K, ? super V> map, final Sequence<? extends Pair<? extends K, ? extends V>> sequence) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        putAll((Map<? super Object, ? super Object>)map, (Sequence<? extends Pair<?, ?>>)sequence);
    }
    
    @InlineOnly
    private static final <K, V> void plusAssign(@NotNull final Map<? super K, ? super V> map, final Pair<? extends K, ? extends V>[] array) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        putAll((Map<? super Object, ? super Object>)map, (Pair<?, ?>[])array);
    }
    
    public static final <K, V> void putAll(@NotNull final Map<? super K, ? super V> map, @NotNull final Iterable<? extends Pair<? extends K, ? extends V>> iterable) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        Intrinsics.checkParameterIsNotNull(iterable, "pairs");
        for (final Pair pair : iterable) {
            map.put((Object)pair.component1(), (Object)pair.component2());
        }
    }
    
    public static final <K, V> void putAll(@NotNull final Map<? super K, ? super V> map, @NotNull final Sequence<? extends Pair<? extends K, ? extends V>> sequence) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        Intrinsics.checkParameterIsNotNull(sequence, "pairs");
        for (final Pair pair : sequence) {
            map.put((Object)pair.component1(), (Object)pair.component2());
        }
    }
    
    public static final <K, V> void putAll(@NotNull final Map<? super K, ? super V> map, @NotNull final Pair<? extends K, ? extends V>[] array) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "pairs");
        for (final Pair<? extends K, ? extends V> pair : array) {
            map.put((Object)pair.component1(), (Object)pair.component2());
        }
    }
    
    @InlineOnly
    private static final <K, V> V remove(@NotNull final Map<? extends K, V> map, final K k) {
        if (map == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableMap<K, V>");
        }
        return TypeIntrinsics.asMutableMap(map).remove(k);
    }
    
    @InlineOnly
    private static final <K, V> void set(@NotNull final Map<K, V> map, final K k, final V v) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        map.put(k, v);
    }
    
    @NotNull
    public static final <K, V> Map<K, V> toMap(@NotNull final Iterable<? extends Pair<? extends K, ? extends V>> iterable) {
        Intrinsics.checkParameterIsNotNull(iterable, "$receiver");
        if (iterable instanceof Collection) {
            final Collection collection = (Collection)iterable;
            Map<Object, Object> map = null;
            switch (collection.size()) {
                default: {
                    map = toMap((Iterable<? extends Pair<?, ?>>)iterable, (Map<Object, Object>)new LinkedHashMap(mapCapacity(collection.size())));
                    break;
                }
                case 1: {
                    Serializable s;
                    if (iterable instanceof List) {
                        s = ((List<Object>)iterable).get(0);
                    }
                    else {
                        s = (Serializable)iterable.iterator().next();
                    }
                    map = MapsKt__MapsJVMKt.mapOf((Pair<?, ?>)s);
                    break;
                }
                case 0: {
                    map = emptyMap();
                    break;
                }
            }
            return (Map<K, V>)map;
        }
        return optimizeReadOnlyMap((Map<K, ? extends V>)toMap((Iterable<? extends Pair<?, ?>>)iterable, (Map<K, ? extends V>)new LinkedHashMap()));
    }
    
    @NotNull
    public static final <K, V, M extends Map<? super K, ? super V>> M toMap(@NotNull final Iterable<? extends Pair<? extends K, ? extends V>> iterable, @NotNull final M m) {
        Intrinsics.checkParameterIsNotNull(iterable, "$receiver");
        Intrinsics.checkParameterIsNotNull(m, "destination");
        putAll((Map<? super Object, ? super Object>)m, (Iterable<? extends Pair<?, ?>>)iterable);
        return m;
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <K, V> Map<K, V> toMap(@NotNull final Map<? extends K, ? extends V> map) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        Map<Object, Object> map2 = null;
        switch (map.size()) {
            default: {
                map2 = toMutableMap((Map<?, ?>)map);
                break;
            }
            case 1: {
                map2 = MapsKt__MapsJVMKt.toSingletonMap((Map<?, ?>)map);
                break;
            }
            case 0: {
                map2 = emptyMap();
                break;
            }
        }
        return (Map<K, V>)map2;
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <K, V, M extends Map<? super K, ? super V>> M toMap(@NotNull final Map<? extends K, ? extends V> map, @NotNull final M m) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        Intrinsics.checkParameterIsNotNull(m, "destination");
        m.putAll(map);
        return m;
    }
    
    @NotNull
    public static final <K, V> Map<K, V> toMap(@NotNull final Sequence<? extends Pair<? extends K, ? extends V>> sequence) {
        Intrinsics.checkParameterIsNotNull(sequence, "$receiver");
        return optimizeReadOnlyMap((Map<K, ? extends V>)toMap((Sequence<? extends Pair<?, ?>>)sequence, (Map<K, ? extends V>)new LinkedHashMap()));
    }
    
    @NotNull
    public static final <K, V, M extends Map<? super K, ? super V>> M toMap(@NotNull final Sequence<? extends Pair<? extends K, ? extends V>> sequence, @NotNull final M m) {
        Intrinsics.checkParameterIsNotNull(sequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(m, "destination");
        putAll((Map<? super Object, ? super Object>)m, (Sequence<? extends Pair<?, ?>>)sequence);
        return m;
    }
    
    @NotNull
    public static final <K, V> Map<K, V> toMap(@NotNull final Pair<? extends K, ? extends V>[] array) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        Map<Object, Object> map = null;
        switch (((Pair<? extends K, ? extends V>[])array).length) {
            default: {
                map = toMap((Pair<?, ?>[])array, (Map<Object, Object>)new LinkedHashMap(mapCapacity(((Pair<? extends K, ? extends V>[])array).length)));
                break;
            }
            case 1: {
                map = MapsKt__MapsJVMKt.mapOf((Pair<?, ?>)array[0]);
                break;
            }
            case 0: {
                map = emptyMap();
                break;
            }
        }
        return (Map<K, V>)map;
    }
    
    @NotNull
    public static final <K, V, M extends Map<? super K, ? super V>> M toMap(@NotNull final Pair<? extends K, ? extends V>[] array, @NotNull final M m) {
        Intrinsics.checkParameterIsNotNull(array, "$receiver");
        Intrinsics.checkParameterIsNotNull(m, "destination");
        putAll((Map<? super Object, ? super Object>)m, (Pair<?, ?>[])array);
        return m;
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <K, V> Map<K, V> toMutableMap(@NotNull final Map<? extends K, ? extends V> m) {
        Intrinsics.checkParameterIsNotNull(m, "$receiver");
        return new LinkedHashMap<K, V>(m);
    }
    
    @InlineOnly
    private static final <K, V> Pair<K, V> toPair(@NotNull final Map.Entry<? extends K, ? extends V> entry) {
        return new Pair<K, V>((K)entry.getKey(), (V)entry.getValue());
    }
}
