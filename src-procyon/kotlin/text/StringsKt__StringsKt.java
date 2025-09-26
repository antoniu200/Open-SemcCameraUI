// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.text;

import kotlin.ReplaceWith;
import kotlin.Deprecated;
import java.util.ArrayList;
import kotlin.jvm.functions.Function1;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function2;
import java.util.List;
import kotlin.sequences.Sequence;
import kotlin.collections.CharIterator;
import java.util.Iterator;
import kotlin.ranges.IntRange;
import kotlin.ranges.IntProgression;
import kotlin.TuplesKt;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;
import kotlin.Pair;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\r\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\f\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001e\n\u0002\b\f\n\u0002\u0010\u0019\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\b\n\u0002\u0010\u0011\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u001b\u001a\u001c\u0010\t\u001a\u00020\n*\u00020\u00022\u0006\u0010\u000b\u001a\u00020\u00022\b\b\u0002\u0010\f\u001a\u00020\r\u001a\u001c\u0010\u000e\u001a\u00020\n*\u00020\u00022\u0006\u0010\u000b\u001a\u00020\u00022\b\b\u0002\u0010\f\u001a\u00020\r\u001a\u001f\u0010\u000f\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\f\u001a\u00020\rH\u0086\u0002\u001a\u001f\u0010\u000f\u001a\u00020\r*\u00020\u00022\u0006\u0010\u000b\u001a\u00020\u00022\b\b\u0002\u0010\f\u001a\u00020\rH\u0086\u0002\u001a\u0015\u0010\u000f\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u0013H\u0087\n\u001a\u001c\u0010\u0014\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\f\u001a\u00020\r\u001a\u001c\u0010\u0014\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00022\b\b\u0002\u0010\f\u001a\u00020\r\u001a:\u0010\u0016\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\n\u0018\u00010\u0017*\u00020\u00022\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\n0\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001aE\u0010\u0016\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\n\u0018\u00010\u0017*\u00020\u00022\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\n0\u00192\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u001b\u001a\u00020\rH\u0002¢\u0006\u0002\b\u001c\u001a:\u0010\u001d\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\n\u0018\u00010\u0017*\u00020\u00022\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\n0\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a\u0012\u0010\u001e\u001a\u00020\r*\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u0006\u001a&\u0010 \u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a;\u0010 \u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u000b\u001a\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u0010!\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u001b\u001a\u00020\rH\u0002¢\u0006\u0002\b\"\u001a&\u0010 \u001a\u00020\u0006*\u00020\u00022\u0006\u0010#\u001a\u00020\n2\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a&\u0010$\u001a\u00020\u0006*\u00020\u00022\u0006\u0010%\u001a\u00020&2\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a,\u0010$\u001a\u00020\u0006*\u00020\u00022\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\n0\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a\r\u0010'\u001a\u00020\r*\u00020\u0002H\u0087\b\u001a\r\u0010(\u001a\u00020\r*\u00020\u0002H\u0087\b\u001a\r\u0010)\u001a\u00020\r*\u00020\u0002H\u0087\b\u001a \u0010*\u001a\u00020\r*\u0004\u0018\u00010\u0002H\u0087\b\u0082\u0002\u000e\n\f\b\u0000\u0012\u0002\u0018\u0001\u001a\u0004\b\u0003\u0010\u0000\u001a \u0010+\u001a\u00020\r*\u0004\u0018\u00010\u0002H\u0087\b\u0082\u0002\u000e\n\f\b\u0000\u0012\u0002\u0018\u0001\u001a\u0004\b\u0003\u0010\u0000\u001a\r\u0010,\u001a\u00020-*\u00020\u0002H\u0086\u0002\u001a&\u0010.\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a&\u0010.\u001a\u00020\u0006*\u00020\u00022\u0006\u0010#\u001a\u00020\n2\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a&\u0010/\u001a\u00020\u0006*\u00020\u00022\u0006\u0010%\u001a\u00020&2\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a,\u0010/\u001a\u00020\u0006*\u00020\u00022\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\n0\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a\u0010\u00100\u001a\b\u0012\u0004\u0012\u00020\n01*\u00020\u0002\u001a\u0010\u00102\u001a\b\u0012\u0004\u0012\u00020\n03*\u00020\u0002\u001a\u0015\u00104\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u0013H\u0087\f\u001a\u000f\u00105\u001a\u00020\n*\u0004\u0018\u00010\nH\u0087\b\u001a\u001c\u00106\u001a\u00020\u0002*\u00020\u00022\u0006\u00107\u001a\u00020\u00062\b\b\u0002\u00108\u001a\u00020\u0011\u001a\u001c\u00106\u001a\u00020\n*\u00020\n2\u0006\u00107\u001a\u00020\u00062\b\b\u0002\u00108\u001a\u00020\u0011\u001a\u001c\u00109\u001a\u00020\u0002*\u00020\u00022\u0006\u00107\u001a\u00020\u00062\b\b\u0002\u00108\u001a\u00020\u0011\u001a\u001c\u00109\u001a\u00020\n*\u00020\n2\u0006\u00107\u001a\u00020\u00062\b\b\u0002\u00108\u001a\u00020\u0011\u001aG\u0010:\u001a\b\u0012\u0004\u0012\u00020\u000101*\u00020\u00022\u000e\u0010;\u001a\n\u0012\u0006\b\u0001\u0012\u00020\n0<2\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010=\u001a\u00020\u0006H\u0002¢\u0006\u0004\b>\u0010?\u001a=\u0010:\u001a\b\u0012\u0004\u0012\u00020\u000101*\u00020\u00022\u0006\u0010;\u001a\u00020&2\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010=\u001a\u00020\u0006H\u0002¢\u0006\u0002\b>\u001a4\u0010@\u001a\u00020\r*\u00020\u00022\u0006\u0010A\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u00022\u0006\u0010B\u001a\u00020\u00062\u0006\u00107\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\rH\u0000\u001a\u0012\u0010C\u001a\u00020\u0002*\u00020\u00022\u0006\u0010D\u001a\u00020\u0002\u001a\u0012\u0010C\u001a\u00020\n*\u00020\n2\u0006\u0010D\u001a\u00020\u0002\u001a\u001a\u0010E\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u0010!\u001a\u00020\u0006\u001a\u0012\u0010E\u001a\u00020\u0002*\u00020\u00022\u0006\u0010F\u001a\u00020\u0001\u001a\u001d\u0010E\u001a\u00020\n*\u00020\n2\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u0010!\u001a\u00020\u0006H\u0087\b\u001a\u0015\u0010E\u001a\u00020\n*\u00020\n2\u0006\u0010F\u001a\u00020\u0001H\u0087\b\u001a\u0012\u0010G\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0002\u001a\u0012\u0010G\u001a\u00020\n*\u00020\n2\u0006\u0010\u0015\u001a\u00020\u0002\u001a\u0012\u0010H\u001a\u00020\u0002*\u00020\u00022\u0006\u0010I\u001a\u00020\u0002\u001a\u001a\u0010H\u001a\u00020\u0002*\u00020\u00022\u0006\u0010D\u001a\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0002\u001a\u0012\u0010H\u001a\u00020\n*\u00020\n2\u0006\u0010I\u001a\u00020\u0002\u001a\u001a\u0010H\u001a\u00020\n*\u00020\n2\u0006\u0010D\u001a\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0002\u001a+\u0010J\u001a\u00020\n*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00132\u0014\b\b\u0010K\u001a\u000e\u0012\u0004\u0012\u00020M\u0012\u0004\u0012\u00020\u00020LH\u0087\b\u001a\u001d\u0010J\u001a\u00020\n*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010N\u001a\u00020\nH\u0087\b\u001a$\u0010O\u001a\u00020\n*\u00020\n2\u0006\u0010I\u001a\u00020\u00112\u0006\u0010N\u001a\u00020\n2\b\b\u0002\u0010P\u001a\u00020\n\u001a$\u0010O\u001a\u00020\n*\u00020\n2\u0006\u0010I\u001a\u00020\n2\u0006\u0010N\u001a\u00020\n2\b\b\u0002\u0010P\u001a\u00020\n\u001a$\u0010Q\u001a\u00020\n*\u00020\n2\u0006\u0010I\u001a\u00020\u00112\u0006\u0010N\u001a\u00020\n2\b\b\u0002\u0010P\u001a\u00020\n\u001a$\u0010Q\u001a\u00020\n*\u00020\n2\u0006\u0010I\u001a\u00020\n2\u0006\u0010N\u001a\u00020\n2\b\b\u0002\u0010P\u001a\u00020\n\u001a$\u0010R\u001a\u00020\n*\u00020\n2\u0006\u0010I\u001a\u00020\u00112\u0006\u0010N\u001a\u00020\n2\b\b\u0002\u0010P\u001a\u00020\n\u001a$\u0010R\u001a\u00020\n*\u00020\n2\u0006\u0010I\u001a\u00020\n2\u0006\u0010N\u001a\u00020\n2\b\b\u0002\u0010P\u001a\u00020\n\u001a$\u0010S\u001a\u00020\n*\u00020\n2\u0006\u0010I\u001a\u00020\u00112\u0006\u0010N\u001a\u00020\n2\b\b\u0002\u0010P\u001a\u00020\n\u001a$\u0010S\u001a\u00020\n*\u00020\n2\u0006\u0010I\u001a\u00020\n2\u0006\u0010N\u001a\u00020\n2\b\b\u0002\u0010P\u001a\u00020\n\u001a\u001d\u0010T\u001a\u00020\n*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010N\u001a\u00020\nH\u0087\b\u001a\"\u0010U\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u0010!\u001a\u00020\u00062\u0006\u0010N\u001a\u00020\u0002\u001a\u001a\u0010U\u001a\u00020\u0002*\u00020\u00022\u0006\u0010F\u001a\u00020\u00012\u0006\u0010N\u001a\u00020\u0002\u001a%\u0010U\u001a\u00020\n*\u00020\n2\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u0010!\u001a\u00020\u00062\u0006\u0010N\u001a\u00020\u0002H\u0087\b\u001a\u001d\u0010U\u001a\u00020\n*\u00020\n2\u0006\u0010F\u001a\u00020\u00012\u0006\u0010N\u001a\u00020\u0002H\u0087\b\u001a=\u0010V\u001a\b\u0012\u0004\u0012\u00020\n03*\u00020\u00022\u0012\u0010;\u001a\n\u0012\u0006\b\u0001\u0012\u00020\n0<\"\u00020\n2\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010=\u001a\u00020\u0006¢\u0006\u0002\u0010W\u001a0\u0010V\u001a\b\u0012\u0004\u0012\u00020\n03*\u00020\u00022\n\u0010;\u001a\u00020&\"\u00020\u00112\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010=\u001a\u00020\u0006\u001a/\u0010V\u001a\b\u0012\u0004\u0012\u00020\n03*\u00020\u00022\u0006\u0010I\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010=\u001a\u00020\u0006H\u0002¢\u0006\u0002\bX\u001a%\u0010V\u001a\b\u0012\u0004\u0012\u00020\n03*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00132\b\b\u0002\u0010=\u001a\u00020\u0006H\u0087\b\u001a=\u0010Y\u001a\b\u0012\u0004\u0012\u00020\n01*\u00020\u00022\u0012\u0010;\u001a\n\u0012\u0006\b\u0001\u0012\u00020\n0<\"\u00020\n2\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010=\u001a\u00020\u0006¢\u0006\u0002\u0010Z\u001a0\u0010Y\u001a\b\u0012\u0004\u0012\u00020\n01*\u00020\u00022\n\u0010;\u001a\u00020&\"\u00020\u00112\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010=\u001a\u00020\u0006\u001a\u001c\u0010[\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\f\u001a\u00020\r\u001a\u001c\u0010[\u001a\u00020\r*\u00020\u00022\u0006\u0010D\u001a\u00020\u00022\b\b\u0002\u0010\f\u001a\u00020\r\u001a$\u0010[\u001a\u00020\r*\u00020\u00022\u0006\u0010D\u001a\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a\u0012\u0010\\\u001a\u00020\u0002*\u00020\u00022\u0006\u0010F\u001a\u00020\u0001\u001a\u001d\u0010\\\u001a\u00020\u0002*\u00020\n2\u0006\u0010]\u001a\u00020\u00062\u0006\u0010^\u001a\u00020\u0006H\u0087\b\u001a\u001f\u0010_\u001a\u00020\n*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010!\u001a\u00020\u0006H\u0087\b\u001a\u0012\u0010_\u001a\u00020\n*\u00020\u00022\u0006\u0010F\u001a\u00020\u0001\u001a\u0012\u0010_\u001a\u00020\n*\u00020\n2\u0006\u0010F\u001a\u00020\u0001\u001a\u001c\u0010`\u001a\u00020\n*\u00020\n2\u0006\u0010I\u001a\u00020\u00112\b\b\u0002\u0010P\u001a\u00020\n\u001a\u001c\u0010`\u001a\u00020\n*\u00020\n2\u0006\u0010I\u001a\u00020\n2\b\b\u0002\u0010P\u001a\u00020\n\u001a\u001c\u0010a\u001a\u00020\n*\u00020\n2\u0006\u0010I\u001a\u00020\u00112\b\b\u0002\u0010P\u001a\u00020\n\u001a\u001c\u0010a\u001a\u00020\n*\u00020\n2\u0006\u0010I\u001a\u00020\n2\b\b\u0002\u0010P\u001a\u00020\n\u001a\u001c\u0010b\u001a\u00020\n*\u00020\n2\u0006\u0010I\u001a\u00020\u00112\b\b\u0002\u0010P\u001a\u00020\n\u001a\u001c\u0010b\u001a\u00020\n*\u00020\n2\u0006\u0010I\u001a\u00020\n2\b\b\u0002\u0010P\u001a\u00020\n\u001a\u001c\u0010c\u001a\u00020\n*\u00020\n2\u0006\u0010I\u001a\u00020\u00112\b\b\u0002\u0010P\u001a\u00020\n\u001a\u001c\u0010c\u001a\u00020\n*\u00020\n2\u0006\u0010I\u001a\u00020\n2\b\b\u0002\u0010P\u001a\u00020\n\u001a\n\u0010d\u001a\u00020\u0002*\u00020\u0002\u001a!\u0010d\u001a\u00020\u0002*\u00020\u00022\u0012\u0010e\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\r0LH\u0086\b\u001a\u0016\u0010d\u001a\u00020\u0002*\u00020\u00022\n\u0010%\u001a\u00020&\"\u00020\u0011\u001a\r\u0010d\u001a\u00020\n*\u00020\nH\u0087\b\u001a!\u0010d\u001a\u00020\n*\u00020\n2\u0012\u0010e\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\r0LH\u0086\b\u001a\u0016\u0010d\u001a\u00020\n*\u00020\n2\n\u0010%\u001a\u00020&\"\u00020\u0011\u001a\n\u0010f\u001a\u00020\u0002*\u00020\u0002\u001a!\u0010f\u001a\u00020\u0002*\u00020\u00022\u0012\u0010e\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\r0LH\u0086\b\u001a\u0016\u0010f\u001a\u00020\u0002*\u00020\u00022\n\u0010%\u001a\u00020&\"\u00020\u0011\u001a\r\u0010f\u001a\u00020\n*\u00020\nH\u0087\b\u001a!\u0010f\u001a\u00020\n*\u00020\n2\u0012\u0010e\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\r0LH\u0086\b\u001a\u0016\u0010f\u001a\u00020\n*\u00020\n2\n\u0010%\u001a\u00020&\"\u00020\u0011\u001a\n\u0010g\u001a\u00020\u0002*\u00020\u0002\u001a!\u0010g\u001a\u00020\u0002*\u00020\u00022\u0012\u0010e\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\r0LH\u0086\b\u001a\u0016\u0010g\u001a\u00020\u0002*\u00020\u00022\n\u0010%\u001a\u00020&\"\u00020\u0011\u001a\r\u0010g\u001a\u00020\n*\u00020\nH\u0087\b\u001a!\u0010g\u001a\u00020\n*\u00020\n2\u0012\u0010e\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\r0LH\u0086\b\u001a\u0016\u0010g\u001a\u00020\n*\u00020\n2\n\u0010%\u001a\u00020&\"\u00020\u0011\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0015\u0010\u0005\u001a\u00020\u0006*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0007\u0010\b¨\u0006h" }, d2 = { "indices", "Lkotlin/ranges/IntRange;", "", "getIndices", "(Ljava/lang/CharSequence;)Lkotlin/ranges/IntRange;", "lastIndex", "", "getLastIndex", "(Ljava/lang/CharSequence;)I", "commonPrefixWith", "", "other", "ignoreCase", "", "commonSuffixWith", "contains", "char", "", "regex", "Lkotlin/text/Regex;", "endsWith", "suffix", "findAnyOf", "Lkotlin/Pair;", "strings", "", "startIndex", "last", "findAnyOf$StringsKt__StringsKt", "findLastAnyOf", "hasSurrogatePairAt", "index", "indexOf", "endIndex", "indexOf$StringsKt__StringsKt", "string", "indexOfAny", "chars", "", "isEmpty", "isNotBlank", "isNotEmpty", "isNullOrBlank", "isNullOrEmpty", "iterator", "Lkotlin/collections/CharIterator;", "lastIndexOf", "lastIndexOfAny", "lineSequence", "Lkotlin/sequences/Sequence;", "lines", "", "matches", "orEmpty", "padEnd", "length", "padChar", "padStart", "rangesDelimitedBy", "delimiters", "", "limit", "rangesDelimitedBy$StringsKt__StringsKt", "(Ljava/lang/CharSequence;[Ljava/lang/String;IZI)Lkotlin/sequences/Sequence;", "regionMatchesImpl", "thisOffset", "otherOffset", "removePrefix", "prefix", "removeRange", "range", "removeSuffix", "removeSurrounding", "delimiter", "replace", "transform", "Lkotlin/Function1;", "Lkotlin/text/MatchResult;", "replacement", "replaceAfter", "missingDelimiterValue", "replaceAfterLast", "replaceBefore", "replaceBeforeLast", "replaceFirst", "replaceRange", "split", "(Ljava/lang/CharSequence;[Ljava/lang/String;ZI)Ljava/util/List;", "split$StringsKt__StringsKt", "splitToSequence", "(Ljava/lang/CharSequence;[Ljava/lang/String;ZI)Lkotlin/sequences/Sequence;", "startsWith", "subSequence", "start", "end", "substring", "substringAfter", "substringAfterLast", "substringBefore", "substringBeforeLast", "trim", "predicate", "trimEnd", "trimStart", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/text/StringsKt")
class StringsKt__StringsKt extends StringsKt__StringsJVMKt
{
    public StringsKt__StringsKt() {
    }
    
    @NotNull
    public static final String commonPrefixWith(@NotNull final CharSequence charSequence, @NotNull final CharSequence charSequence2, final boolean b) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(charSequence2, "other");
        int min;
        int n;
        for (min = Math.min(charSequence.length(), charSequence2.length()), n = 0; n < min && CharsKt__CharKt.equals(charSequence.charAt(n), charSequence2.charAt(n), b); ++n) {}
        final int n2 = n - 1;
        if (!hasSurrogatePairAt(charSequence, n2)) {
            final int n3 = n;
            if (!hasSurrogatePairAt(charSequence2, n2)) {
                return charSequence.subSequence(0, n3).toString();
            }
        }
        final int n3 = n - 1;
        return charSequence.subSequence(0, n3).toString();
    }
    
    @NotNull
    public static final String commonSuffixWith(@NotNull final CharSequence charSequence, @NotNull final CharSequence charSequence2, final boolean b) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(charSequence2, "other");
        int length;
        int length2;
        int min;
        int n;
        for (length = charSequence.length(), length2 = charSequence2.length(), min = Math.min(length, length2), n = 0; n < min && CharsKt__CharKt.equals(charSequence.charAt(length - n - 1), charSequence2.charAt(length2 - n - 1), b); ++n) {}
        if (!hasSurrogatePairAt(charSequence, length - n - 1)) {
            final int n2 = n;
            if (!hasSurrogatePairAt(charSequence2, length2 - n - 1)) {
                return charSequence.subSequence(length - n2, length).toString();
            }
        }
        final int n2 = n - 1;
        return charSequence.subSequence(length - n2, length).toString();
    }
    
    public static final boolean contains(@NotNull final CharSequence charSequence, final char c, final boolean b) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        return indexOf$default(charSequence, c, 0, b, 2, (Object)null) >= 0;
    }
    
    public static final boolean contains(@NotNull final CharSequence charSequence, @NotNull final CharSequence charSequence2, final boolean b) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(charSequence2, "other");
        final boolean b2 = charSequence2 instanceof String;
        boolean b3 = false;
        if (b2) {
            if (indexOf$default(charSequence, (String)charSequence2, 0, b, 2, (Object)null) < 0) {
                return b3;
            }
        }
        else if (indexOf$StringsKt__StringsKt$default(charSequence, charSequence2, 0, charSequence.length(), b, false, 16, (Object)null) < 0) {
            return b3;
        }
        b3 = true;
        return b3;
    }
    
    @InlineOnly
    private static final boolean contains(@NotNull final CharSequence charSequence, final Regex regex) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        return regex.containsMatchIn(charSequence);
    }
    
    public static final boolean endsWith(@NotNull final CharSequence charSequence, final char c, final boolean b) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        return charSequence.length() > 0 && CharsKt__CharKt.equals(charSequence.charAt(getLastIndex(charSequence)), c, b);
    }
    
    public static final boolean endsWith(@NotNull final CharSequence charSequence, @NotNull final CharSequence charSequence2, final boolean b) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(charSequence2, "suffix");
        if (!b && charSequence instanceof String && charSequence2 instanceof String) {
            return StringsKt__StringsJVMKt.endsWith$default((String)charSequence, (String)charSequence2, false, 2, (Object)null);
        }
        return regionMatchesImpl(charSequence, charSequence.length() - charSequence2.length(), charSequence2, 0, charSequence2.length(), b);
    }
    
    @Nullable
    public static final Pair<Integer, String> findAnyOf(@NotNull final CharSequence charSequence, @NotNull final Collection<String> collection, final int n, final boolean b) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(collection, "strings");
        return findAnyOf$StringsKt__StringsKt(charSequence, collection, n, b, false);
    }
    
    private static final Pair<Integer, String> findAnyOf$StringsKt__StringsKt(@NotNull final CharSequence charSequence, final Collection<String> collection, int i, final boolean b, final boolean b2) {
        final Pair<Integer, String> pair = null;
        if (!b && collection.size() == 1) {
            final String s = CollectionsKt___CollectionsKt.single((Iterable<? extends String>)collection);
            if (!b2) {
                i = indexOf$default(charSequence, s, i, false, 4, (Object)null);
            }
            else {
                i = lastIndexOf$default(charSequence, s, i, false, 4, (Object)null);
            }
            Pair<Integer, String> to;
            if (i < 0) {
                to = pair;
            }
            else {
                to = TuplesKt.to(i, s);
            }
            return to;
        }
        IntProgression downTo;
        if (!b2) {
            downTo = new IntRange(RangesKt___RangesKt.coerceAtLeast(i, 0), charSequence.length());
        }
        else {
            downTo = RangesKt___RangesKt.downTo(RangesKt___RangesKt.coerceAtMost(i, getLastIndex(charSequence)), 0);
        }
        Label_0407: {
            if (charSequence instanceof String) {
                i = downTo.getFirst();
                final int last = downTo.getLast();
                final int step = downTo.getStep();
                if (step > 0) {
                    if (i > last) {
                        return null;
                    }
                }
                else if (i < last) {
                    return null;
                }
            Label_0169:
                while (true) {
                    while (true) {
                        for (final Object next : collection) {
                            final String s2 = (String)next;
                            if (StringsKt__StringsJVMKt.regionMatches(s2, 0, (String)charSequence, i, s2.length(), b)) {
                                final String s3 = (String)next;
                                if (s3 != null) {
                                    return TuplesKt.to(i, s3);
                                }
                                if (i != last) {
                                    i += step;
                                    continue Label_0169;
                                }
                                break Label_0407;
                            }
                        }
                        Object next = null;
                        continue;
                    }
                }
            }
            else {
                i = downTo.getFirst();
                final int last2 = downTo.getLast();
                final int step2 = downTo.getStep();
                if (step2 > 0) {
                    if (i > last2) {
                        return null;
                    }
                }
                else if (i < last2) {
                    return null;
                }
            Label_0308:
                while (true) {
                    while (true) {
                        for (final Object next2 : collection) {
                            final String s4 = (String)next2;
                            if (regionMatchesImpl(s4, 0, charSequence, i, s4.length(), b)) {
                                final String s5 = (String)next2;
                                if (s5 != null) {
                                    return TuplesKt.to(i, s5);
                                }
                                if (i != last2) {
                                    i += step2;
                                    continue Label_0308;
                                }
                                break Label_0407;
                            }
                        }
                        Object next2 = null;
                        continue;
                    }
                }
            }
        }
        return null;
    }
    
    @Nullable
    public static final Pair<Integer, String> findLastAnyOf(@NotNull final CharSequence charSequence, @NotNull final Collection<String> collection, final int n, final boolean b) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(collection, "strings");
        return findAnyOf$StringsKt__StringsKt(charSequence, collection, n, b, true);
    }
    
    @NotNull
    public static final IntRange getIndices(@NotNull final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        return new IntRange(0, charSequence.length() - 1);
    }
    
    public static final int getLastIndex(@NotNull final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        return charSequence.length() - 1;
    }
    
    public static final boolean hasSurrogatePairAt(@NotNull final CharSequence charSequence, final int n) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        final int length = charSequence.length();
        boolean b = true;
        if (n >= 0) {
            if (length - 2 >= n && Character.isHighSurrogate(charSequence.charAt(n)) && Character.isLowSurrogate(charSequence.charAt(n + 1))) {
                return b;
            }
        }
        b = false;
        return b;
    }
    
    public static final int indexOf(@NotNull final CharSequence charSequence, final char ch, int fromIndex, final boolean b) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        if (!b && charSequence instanceof String) {
            fromIndex = ((String)charSequence).indexOf(ch, fromIndex);
        }
        else {
            fromIndex = indexOfAny(charSequence, new char[] { ch }, fromIndex, b);
        }
        return fromIndex;
    }
    
    public static final int indexOf(@NotNull final CharSequence charSequence, @NotNull final String str, int fromIndex, final boolean b) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(str, "string");
        if (!b && charSequence instanceof String) {
            fromIndex = ((String)charSequence).indexOf(str, fromIndex);
        }
        else {
            fromIndex = indexOf$StringsKt__StringsKt$default(charSequence, (CharSequence)str, fromIndex, charSequence.length(), b, false, 16, (Object)null);
        }
        return fromIndex;
    }
    
    private static final int indexOf$StringsKt__StringsKt(@NotNull final CharSequence charSequence, final CharSequence charSequence2, int n, int n2, final boolean b, final boolean b2) {
        IntProgression downTo;
        if (!b2) {
            downTo = new IntRange(RangesKt___RangesKt.coerceAtLeast(n, 0), RangesKt___RangesKt.coerceAtMost(n2, charSequence.length()));
        }
        else {
            downTo = RangesKt___RangesKt.downTo(RangesKt___RangesKt.coerceAtMost(n, getLastIndex(charSequence)), RangesKt___RangesKt.coerceAtLeast(n2, 0));
        }
        if (charSequence instanceof String && charSequence2 instanceof String) {
            n = downTo.getFirst();
            n2 = downTo.getLast();
            final int step = downTo.getStep();
            if (step > 0) {
                if (n > n2) {
                    return -1;
                }
            }
            else if (n < n2) {
                return -1;
            }
            while (!StringsKt__StringsJVMKt.regionMatches((String)charSequence2, 0, (String)charSequence, n, charSequence2.length(), b)) {
                if (n == n2) {
                    return -1;
                }
                n += step;
            }
            return n;
        }
        n = downTo.getFirst();
        n2 = downTo.getLast();
        final int step2 = downTo.getStep();
        if (step2 > 0) {
            if (n > n2) {
                return -1;
            }
        }
        else if (n < n2) {
            return -1;
        }
        while (!regionMatchesImpl(charSequence2, 0, charSequence, n, charSequence2.length(), b)) {
            if (n == n2) {
                return -1;
            }
            n += step2;
        }
        return n;
    }
    
    public static final int indexOfAny(@NotNull final CharSequence charSequence, @NotNull final Collection<String> collection, int intValue, final boolean b) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(collection, "strings");
        final Pair<Integer, String> anyOf$StringsKt__StringsKt = findAnyOf$StringsKt__StringsKt(charSequence, collection, intValue, b, false);
        if (anyOf$StringsKt__StringsKt != null) {
            final Integer n = anyOf$StringsKt__StringsKt.getFirst();
            if (n != null) {
                intValue = n;
                return intValue;
            }
        }
        intValue = -1;
        return intValue;
    }
    
    public static final int indexOfAny(@NotNull final CharSequence charSequence, @NotNull final char[] array, int coerceAtLeast, final boolean b) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "chars");
        if (!b && array.length == 1 && charSequence instanceof String) {
            return ((String)charSequence).indexOf(ArraysKt___ArraysKt.single(array), coerceAtLeast);
        }
        coerceAtLeast = RangesKt___RangesKt.coerceAtLeast(coerceAtLeast, 0);
        final int lastIndex = getLastIndex(charSequence);
        Label_0135: {
            if (coerceAtLeast <= lastIndex) {
            Label_0065:
                while (true) {
                    final char char1 = charSequence.charAt(coerceAtLeast);
                    final int length = array.length;
                    int i = 0;
                    while (true) {
                        while (i < length) {
                            if (CharsKt__CharKt.equals(array[i], char1, b)) {
                                final boolean b2 = true;
                                if (b2) {
                                    return coerceAtLeast;
                                }
                                if (coerceAtLeast != lastIndex) {
                                    ++coerceAtLeast;
                                    continue Label_0065;
                                }
                                break Label_0135;
                            }
                            else {
                                ++i;
                            }
                        }
                        final boolean b2 = false;
                        continue;
                    }
                }
            }
        }
        return -1;
    }
    
    @InlineOnly
    private static final boolean isEmpty(@NotNull final CharSequence charSequence) {
        return charSequence.length() == 0;
    }
    
    @InlineOnly
    private static final boolean isNotBlank(@NotNull final CharSequence charSequence) {
        return StringsKt__StringsJVMKt.isBlank(charSequence) ^ true;
    }
    
    @InlineOnly
    private static final boolean isNotEmpty(@NotNull final CharSequence charSequence) {
        return charSequence.length() > 0;
    }
    
    @InlineOnly
    private static final boolean isNullOrBlank(@Nullable final CharSequence charSequence) {
        return charSequence == null || StringsKt__StringsJVMKt.isBlank(charSequence);
    }
    
    @InlineOnly
    private static final boolean isNullOrEmpty(@Nullable final CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }
    
    @NotNull
    public static final CharIterator iterator(@NotNull final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        return (CharIterator)new StringsKt__StringsKt$iterator.StringsKt__StringsKt$iterator$1(charSequence);
    }
    
    public static final int lastIndexOf(@NotNull final CharSequence charSequence, final char ch, int fromIndex, final boolean b) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        if (!b && charSequence instanceof String) {
            fromIndex = ((String)charSequence).lastIndexOf(ch, fromIndex);
        }
        else {
            fromIndex = lastIndexOfAny(charSequence, new char[] { ch }, fromIndex, b);
        }
        return fromIndex;
    }
    
    public static final int lastIndexOf(@NotNull final CharSequence charSequence, @NotNull final String str, int fromIndex, final boolean b) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(str, "string");
        if (!b && charSequence instanceof String) {
            fromIndex = ((String)charSequence).lastIndexOf(str, fromIndex);
        }
        else {
            fromIndex = indexOf$StringsKt__StringsKt(charSequence, str, fromIndex, 0, b, true);
        }
        return fromIndex;
    }
    
    public static final int lastIndexOfAny(@NotNull final CharSequence charSequence, @NotNull final Collection<String> collection, int intValue, final boolean b) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(collection, "strings");
        final Pair<Integer, String> anyOf$StringsKt__StringsKt = findAnyOf$StringsKt__StringsKt(charSequence, collection, intValue, b, true);
        if (anyOf$StringsKt__StringsKt != null) {
            final Integer n = anyOf$StringsKt__StringsKt.getFirst();
            if (n != null) {
                intValue = n;
                return intValue;
            }
        }
        intValue = -1;
        return intValue;
    }
    
    public static final int lastIndexOfAny(@NotNull final CharSequence charSequence, @NotNull final char[] array, int i, final boolean b) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "chars");
        if (!b && array.length == 1 && charSequence instanceof String) {
            return ((String)charSequence).lastIndexOf(ArraysKt___ArraysKt.single(array), i);
        }
        char char1;
        int length;
        int n;
        int n2;
        int n3;
        for (i = RangesKt___RangesKt.coerceAtMost(i, getLastIndex(charSequence)); i >= 0; --i) {
            char1 = charSequence.charAt(i);
            length = array.length;
            n = 0;
            n2 = 0;
            while (true) {
                n3 = n;
                if (n2 >= length) {
                    break;
                }
                if (CharsKt__CharKt.equals(array[n2], char1, b)) {
                    n3 = 1;
                    break;
                }
                ++n2;
            }
            if (n3 != 0) {
                return i;
            }
        }
        return -1;
    }
    
    @NotNull
    public static final Sequence<String> lineSequence(@NotNull final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        return splitToSequence$default(charSequence, new String[] { "\r\n", "\n", "\r" }, false, 0, 6, (Object)null);
    }
    
    @NotNull
    public static final List<String> lines(@NotNull final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        return SequencesKt___SequencesKt.toList((Sequence<? extends String>)lineSequence(charSequence));
    }
    
    @InlineOnly
    private static final boolean matches(@NotNull final CharSequence charSequence, final Regex regex) {
        return regex.matches(charSequence);
    }
    
    @InlineOnly
    private static final String orEmpty(@Nullable String s) {
        if (s == null) {
            s = "";
        }
        return s;
    }
    
    @NotNull
    public static final CharSequence padEnd(@NotNull final CharSequence s, int n, final char c) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        if (n < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Desired length ");
            sb.append(n);
            sb.append(" is less than zero.");
            throw new IllegalArgumentException(sb.toString());
        }
        if (n <= s.length()) {
            return s.subSequence(0, s.length());
        }
        final StringBuilder sb2 = new StringBuilder(n);
        sb2.append(s);
        final int n2 = n - s.length();
        n = 1;
        if (1 <= n2) {
            while (true) {
                sb2.append(c);
                if (n == n2) {
                    break;
                }
                ++n;
            }
        }
        return sb2;
    }
    
    @NotNull
    public static final String padEnd(@NotNull final String s, final int n, final char c) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        return padEnd((CharSequence)s, n, c).toString();
    }
    
    @NotNull
    public static final CharSequence padStart(@NotNull final CharSequence s, int n, final char c) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        if (n < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Desired length ");
            sb.append(n);
            sb.append(" is less than zero.");
            throw new IllegalArgumentException(sb.toString());
        }
        if (n <= s.length()) {
            return s.subSequence(0, s.length());
        }
        final StringBuilder sb2 = new StringBuilder(n);
        final int n2 = n - s.length();
        n = 1;
        if (1 <= n2) {
            while (true) {
                sb2.append(c);
                if (n == n2) {
                    break;
                }
                ++n;
            }
        }
        sb2.append(s);
        return sb2;
    }
    
    @NotNull
    public static final String padStart(@NotNull final String s, final int n, final char c) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        return padStart((CharSequence)s, n, c).toString();
    }
    
    private static final Sequence<IntRange> rangesDelimitedBy$StringsKt__StringsKt(@NotNull final CharSequence charSequence, final char[] array, final int n, final boolean b, final int i) {
        if (i < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Limit must be non-negative, but was ");
            sb.append(i);
            sb.append('.');
            throw new IllegalArgumentException(sb.toString().toString());
        }
        return new DelimitedRangesSequence(charSequence, n, i, (Function2<? super CharSequence, ? super Integer, Pair<Integer, Integer>>)new StringsKt__StringsKt$rangesDelimitedBy.StringsKt__StringsKt$rangesDelimitedBy$2(array, b));
    }
    
    private static final Sequence<IntRange> rangesDelimitedBy$StringsKt__StringsKt(@NotNull final CharSequence charSequence, final String[] array, final int n, final boolean b, final int i) {
        if (i < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Limit must be non-negative, but was ");
            sb.append(i);
            sb.append('.');
            throw new IllegalArgumentException(sb.toString().toString());
        }
        return new DelimitedRangesSequence(charSequence, n, i, (Function2<? super CharSequence, ? super Integer, Pair<Integer, Integer>>)new StringsKt__StringsKt$rangesDelimitedBy.StringsKt__StringsKt$rangesDelimitedBy$4((List)ArraysKt___ArraysJvmKt.asList(array), b));
    }
    
    public static final boolean regionMatchesImpl(@NotNull final CharSequence charSequence, final int n, @NotNull final CharSequence charSequence2, final int n2, final int n3, final boolean b) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(charSequence2, "other");
        if (n2 >= 0 && n >= 0 && n <= charSequence.length() - n3 && n2 <= charSequence2.length() - n3) {
            for (int i = 0; i < n3; ++i) {
                if (!CharsKt__CharKt.equals(charSequence.charAt(n + i), charSequence2.charAt(n2 + i), b)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    @NotNull
    public static final CharSequence removePrefix(@NotNull final CharSequence charSequence, @NotNull final CharSequence charSequence2) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(charSequence2, "prefix");
        if (startsWith$default(charSequence, charSequence2, false, 2, (Object)null)) {
            return charSequence.subSequence(charSequence2.length(), charSequence.length());
        }
        return charSequence.subSequence(0, charSequence.length());
    }
    
    @NotNull
    public static final String removePrefix(@NotNull String substring, @NotNull final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(substring, "$receiver");
        Intrinsics.checkParameterIsNotNull(charSequence, "prefix");
        if (startsWith$default((CharSequence)substring, charSequence, false, 2, (Object)null)) {
            substring = substring.substring(charSequence.length());
            Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.String).substring(startIndex)");
            return substring;
        }
        return substring;
    }
    
    @NotNull
    public static final CharSequence removeRange(@NotNull final CharSequence charSequence, final int n, final int n2) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        if (n2 < n) {
            final StringBuilder sb = new StringBuilder();
            sb.append("End index (");
            sb.append(n2);
            sb.append(") is less than start index (");
            sb.append(n);
            sb.append(").");
            throw new IndexOutOfBoundsException(sb.toString());
        }
        if (n2 == n) {
            return charSequence.subSequence(0, charSequence.length());
        }
        final StringBuilder sb2 = new StringBuilder(charSequence.length() - (n2 - n));
        sb2.append(charSequence, 0, n);
        sb2.append(charSequence, n2, charSequence.length());
        return sb2;
    }
    
    @NotNull
    public static final CharSequence removeRange(@NotNull final CharSequence charSequence, @NotNull final IntRange intRange) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(intRange, "range");
        return removeRange(charSequence, intRange.getStart(), intRange.getEndInclusive() + 1);
    }
    
    @InlineOnly
    private static final String removeRange(@NotNull final String s, final int n, final int n2) {
        if (s == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
        }
        return removeRange((CharSequence)s, n, n2).toString();
    }
    
    @InlineOnly
    private static final String removeRange(@NotNull final String s, final IntRange intRange) {
        if (s == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
        }
        return removeRange((CharSequence)s, intRange).toString();
    }
    
    @NotNull
    public static final CharSequence removeSuffix(@NotNull final CharSequence charSequence, @NotNull final CharSequence charSequence2) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(charSequence2, "suffix");
        if (endsWith$default(charSequence, charSequence2, false, 2, (Object)null)) {
            return charSequence.subSequence(0, charSequence.length() - charSequence2.length());
        }
        return charSequence.subSequence(0, charSequence.length());
    }
    
    @NotNull
    public static final String removeSuffix(@NotNull String substring, @NotNull final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(substring, "$receiver");
        Intrinsics.checkParameterIsNotNull(charSequence, "suffix");
        if (endsWith$default((CharSequence)substring, charSequence, false, 2, (Object)null)) {
            substring = substring.substring(0, substring.length() - charSequence.length());
            Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
            return substring;
        }
        return substring;
    }
    
    @NotNull
    public static final CharSequence removeSurrounding(@NotNull final CharSequence charSequence, @NotNull final CharSequence charSequence2) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(charSequence2, "delimiter");
        return removeSurrounding(charSequence, charSequence2, charSequence2);
    }
    
    @NotNull
    public static final CharSequence removeSurrounding(@NotNull final CharSequence charSequence, @NotNull final CharSequence charSequence2, @NotNull final CharSequence charSequence3) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(charSequence2, "prefix");
        Intrinsics.checkParameterIsNotNull(charSequence3, "suffix");
        if (charSequence.length() >= charSequence2.length() + charSequence3.length() && startsWith$default(charSequence, charSequence2, false, 2, (Object)null) && endsWith$default(charSequence, charSequence3, false, 2, (Object)null)) {
            return charSequence.subSequence(charSequence2.length(), charSequence.length() - charSequence3.length());
        }
        return charSequence.subSequence(0, charSequence.length());
    }
    
    @NotNull
    public static final String removeSurrounding(@NotNull final String s, @NotNull final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(charSequence, "delimiter");
        return removeSurrounding(s, charSequence, charSequence);
    }
    
    @NotNull
    public static final String removeSurrounding(@NotNull String substring, @NotNull final CharSequence charSequence, @NotNull final CharSequence charSequence2) {
        Intrinsics.checkParameterIsNotNull(substring, "$receiver");
        Intrinsics.checkParameterIsNotNull(charSequence, "prefix");
        Intrinsics.checkParameterIsNotNull(charSequence2, "suffix");
        if (substring.length() >= charSequence.length() + charSequence2.length()) {
            final CharSequence charSequence3 = substring;
            if (startsWith$default(charSequence3, charSequence, false, 2, (Object)null) && endsWith$default(charSequence3, charSequence2, false, 2, (Object)null)) {
                substring = substring.substring(charSequence.length(), substring.length() - charSequence2.length());
                Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                return substring;
            }
        }
        return substring;
    }
    
    @InlineOnly
    private static final String replace(@NotNull final CharSequence charSequence, final Regex regex, final String s) {
        return regex.replace(charSequence, s);
    }
    
    @InlineOnly
    private static final String replace(@NotNull final CharSequence charSequence, final Regex regex, final Function1<? super MatchResult, ? extends CharSequence> function1) {
        return regex.replace(charSequence, function1);
    }
    
    @NotNull
    public static final String replaceAfter(@NotNull final String s, final char c, @NotNull final String s2, @NotNull String string) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(s2, "replacement");
        Intrinsics.checkParameterIsNotNull(string, "missingDelimiterValue");
        final CharSequence charSequence = s;
        final int indexOf$default = indexOf$default(charSequence, c, 0, false, 6, (Object)null);
        if (indexOf$default != -1) {
            string = replaceRange(charSequence, indexOf$default + 1, s.length(), s2).toString();
        }
        return string;
    }
    
    @NotNull
    public static final String replaceAfter(@NotNull final String s, @NotNull final String s2, @NotNull final String s3, @NotNull String string) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(s2, "delimiter");
        Intrinsics.checkParameterIsNotNull(s3, "replacement");
        Intrinsics.checkParameterIsNotNull(string, "missingDelimiterValue");
        final CharSequence charSequence = s;
        final int indexOf$default = indexOf$default(charSequence, s2, 0, false, 6, (Object)null);
        if (indexOf$default != -1) {
            string = replaceRange(charSequence, indexOf$default + s2.length(), s.length(), s3).toString();
        }
        return string;
    }
    
    @NotNull
    public static final String replaceAfterLast(@NotNull final String s, final char c, @NotNull final String s2, @NotNull String string) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(s2, "replacement");
        Intrinsics.checkParameterIsNotNull(string, "missingDelimiterValue");
        final CharSequence charSequence = s;
        final int lastIndexOf$default = lastIndexOf$default(charSequence, c, 0, false, 6, (Object)null);
        if (lastIndexOf$default != -1) {
            string = replaceRange(charSequence, lastIndexOf$default + 1, s.length(), s2).toString();
        }
        return string;
    }
    
    @NotNull
    public static final String replaceAfterLast(@NotNull final String s, @NotNull final String s2, @NotNull final String s3, @NotNull String string) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(s2, "delimiter");
        Intrinsics.checkParameterIsNotNull(s3, "replacement");
        Intrinsics.checkParameterIsNotNull(string, "missingDelimiterValue");
        final CharSequence charSequence = s;
        final int lastIndexOf$default = lastIndexOf$default(charSequence, s2, 0, false, 6, (Object)null);
        if (lastIndexOf$default != -1) {
            string = replaceRange(charSequence, lastIndexOf$default + s2.length(), s.length(), s3).toString();
        }
        return string;
    }
    
    @NotNull
    public static final String replaceBefore(@NotNull final String s, final char c, @NotNull final String s2, @NotNull String string) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(s2, "replacement");
        Intrinsics.checkParameterIsNotNull(string, "missingDelimiterValue");
        final CharSequence charSequence = s;
        final int indexOf$default = indexOf$default(charSequence, c, 0, false, 6, (Object)null);
        if (indexOf$default != -1) {
            string = replaceRange(charSequence, 0, indexOf$default, s2).toString();
        }
        return string;
    }
    
    @NotNull
    public static final String replaceBefore(@NotNull final String s, @NotNull final String s2, @NotNull final String s3, @NotNull String string) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(s2, "delimiter");
        Intrinsics.checkParameterIsNotNull(s3, "replacement");
        Intrinsics.checkParameterIsNotNull(string, "missingDelimiterValue");
        final CharSequence charSequence = s;
        final int indexOf$default = indexOf$default(charSequence, s2, 0, false, 6, (Object)null);
        if (indexOf$default != -1) {
            string = replaceRange(charSequence, 0, indexOf$default, s3).toString();
        }
        return string;
    }
    
    @NotNull
    public static final String replaceBeforeLast(@NotNull final String s, final char c, @NotNull final String s2, @NotNull String string) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(s2, "replacement");
        Intrinsics.checkParameterIsNotNull(string, "missingDelimiterValue");
        final CharSequence charSequence = s;
        final int lastIndexOf$default = lastIndexOf$default(charSequence, c, 0, false, 6, (Object)null);
        if (lastIndexOf$default != -1) {
            string = replaceRange(charSequence, 0, lastIndexOf$default, s2).toString();
        }
        return string;
    }
    
    @NotNull
    public static final String replaceBeforeLast(@NotNull final String s, @NotNull final String s2, @NotNull final String s3, @NotNull String string) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(s2, "delimiter");
        Intrinsics.checkParameterIsNotNull(s3, "replacement");
        Intrinsics.checkParameterIsNotNull(string, "missingDelimiterValue");
        final CharSequence charSequence = s;
        final int lastIndexOf$default = lastIndexOf$default(charSequence, s2, 0, false, 6, (Object)null);
        if (lastIndexOf$default != -1) {
            string = replaceRange(charSequence, 0, lastIndexOf$default, s3).toString();
        }
        return string;
    }
    
    @InlineOnly
    private static final String replaceFirst(@NotNull final CharSequence charSequence, final Regex regex, final String s) {
        return regex.replaceFirst(charSequence, s);
    }
    
    @NotNull
    public static final CharSequence replaceRange(@NotNull final CharSequence charSequence, final int n, final int n2, @NotNull final CharSequence s) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(s, "replacement");
        if (n2 < n) {
            final StringBuilder sb = new StringBuilder();
            sb.append("End index (");
            sb.append(n2);
            sb.append(") is less than start index (");
            sb.append(n);
            sb.append(").");
            throw new IndexOutOfBoundsException(sb.toString());
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(charSequence, 0, n);
        sb2.append(s);
        sb2.append(charSequence, n2, charSequence.length());
        return sb2;
    }
    
    @NotNull
    public static final CharSequence replaceRange(@NotNull final CharSequence charSequence, @NotNull final IntRange intRange, @NotNull final CharSequence charSequence2) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(intRange, "range");
        Intrinsics.checkParameterIsNotNull(charSequence2, "replacement");
        return replaceRange(charSequence, intRange.getStart(), intRange.getEndInclusive() + 1, charSequence2);
    }
    
    @InlineOnly
    private static final String replaceRange(@NotNull final String s, final int n, final int n2, final CharSequence charSequence) {
        if (s == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
        }
        return replaceRange((CharSequence)s, n, n2, charSequence).toString();
    }
    
    @InlineOnly
    private static final String replaceRange(@NotNull final String s, final IntRange intRange, final CharSequence charSequence) {
        if (s == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
        }
        return replaceRange((CharSequence)s, intRange, charSequence).toString();
    }
    
    @InlineOnly
    private static final List<String> split(@NotNull final CharSequence charSequence, final Regex regex, final int n) {
        return regex.split(charSequence, n);
    }
    
    @NotNull
    public static final List<String> split(@NotNull final CharSequence charSequence, @NotNull final char[] array, final boolean b, final int n) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "delimiters");
        if (array.length == 1) {
            return split$StringsKt__StringsKt(charSequence, String.valueOf(array[0]), b, n);
        }
        final Iterable<Object> iterable = (Iterable<Object>)SequencesKt___SequencesKt.asIterable((Sequence<? extends IntRange>)rangesDelimitedBy$StringsKt__StringsKt$default(charSequence, array, 0, b, n, 2, (Object)null));
        final Collection collection = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault((Iterable<?>)iterable, 10));
        final Iterator<Object> iterator = (Iterator<Object>)iterable.iterator();
        while (iterator.hasNext()) {
            collection.add(substring(charSequence, iterator.next()));
        }
        return (List<String>)collection;
    }
    
    @NotNull
    public static final List<String> split(@NotNull final CharSequence charSequence, @NotNull final String[] array, final boolean b, final int n) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "delimiters");
        if (array.length == 1) {
            boolean b2 = false;
            final String s = array[0];
            if (s.length() == 0) {
                b2 = true;
            }
            if (!b2) {
                return split$StringsKt__StringsKt(charSequence, s, b, n);
            }
        }
        final Iterable<IntRange> iterable = SequencesKt___SequencesKt.asIterable((Sequence<? extends IntRange>)rangesDelimitedBy$StringsKt__StringsKt$default(charSequence, array, 0, b, n, 2, (Object)null));
        final Collection collection = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault((Iterable<?>)iterable, 10));
        final Iterator<IntRange> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            collection.add(substring(charSequence, iterator.next()));
        }
        return (List<String>)collection;
    }
    
    private static final List<String> split$StringsKt__StringsKt(@NotNull final CharSequence charSequence, final String s, final boolean b, final int i) {
        int n = 0;
        if (i < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Limit must be non-negative, but was ");
            sb.append(i);
            sb.append('.');
            throw new IllegalArgumentException(sb.toString().toString());
        }
        final int index = indexOf(charSequence, s, 0, b);
        if (index != -1 && i != 1) {
            final boolean b2 = i > 0;
            int coerceAtMost = 10;
            if (b2) {
                coerceAtMost = RangesKt___RangesKt.coerceAtMost(i, 10);
            }
            final ArrayList list = new ArrayList(coerceAtMost);
            int n2 = index;
            int index2;
            int n3;
            do {
                list.add((Object)charSequence.subSequence(n, n2).toString());
                n3 = s.length() + n2;
                if (b2 && list.size() == i - 1) {
                    break;
                }
                index2 = indexOf(charSequence, s, n3, b);
                n = n3;
            } while ((n2 = index2) != -1);
            list.add((Object)charSequence.subSequence(n3, charSequence.length()).toString());
            return (List)list;
        }
        return CollectionsKt__CollectionsJVMKt.listOf(charSequence.toString());
    }
    
    @NotNull
    public static final Sequence<String> splitToSequence(@NotNull final CharSequence charSequence, @NotNull final char[] array, final boolean b, final int n) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "delimiters");
        return SequencesKt___SequencesKt.map((Sequence<?>)rangesDelimitedBy$StringsKt__StringsKt$default(charSequence, array, 0, b, n, 2, (Object)null), (Function1<? super Object, ? extends String>)new StringsKt__StringsKt$splitToSequence.StringsKt__StringsKt$splitToSequence$2(charSequence));
    }
    
    @NotNull
    public static final Sequence<String> splitToSequence(@NotNull final CharSequence charSequence, @NotNull final String[] array, final boolean b, final int n) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "delimiters");
        return SequencesKt___SequencesKt.map((Sequence<?>)rangesDelimitedBy$StringsKt__StringsKt$default(charSequence, array, 0, b, n, 2, (Object)null), (Function1<? super Object, ? extends String>)new StringsKt__StringsKt$splitToSequence.StringsKt__StringsKt$splitToSequence$1(charSequence));
    }
    
    public static final boolean startsWith(@NotNull final CharSequence charSequence, final char c, final boolean b) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        final int length = charSequence.length();
        boolean b2 = false;
        if (length > 0) {
            b2 = b2;
            if (CharsKt__CharKt.equals(charSequence.charAt(0), c, b)) {
                b2 = true;
            }
        }
        return b2;
    }
    
    public static final boolean startsWith(@NotNull final CharSequence charSequence, @NotNull final CharSequence charSequence2, final int n, final boolean b) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(charSequence2, "prefix");
        if (!b && charSequence instanceof String && charSequence2 instanceof String) {
            return StringsKt__StringsJVMKt.startsWith$default((String)charSequence, (String)charSequence2, n, false, 4, (Object)null);
        }
        return regionMatchesImpl(charSequence, n, charSequence2, 0, charSequence2.length(), b);
    }
    
    public static final boolean startsWith(@NotNull final CharSequence charSequence, @NotNull final CharSequence charSequence2, final boolean b) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(charSequence2, "prefix");
        if (!b && charSequence instanceof String && charSequence2 instanceof String) {
            return StringsKt__StringsJVMKt.startsWith$default((String)charSequence, (String)charSequence2, false, 2, (Object)null);
        }
        return regionMatchesImpl(charSequence, 0, charSequence2, 0, charSequence2.length(), b);
    }
    
    @NotNull
    public static final CharSequence subSequence(@NotNull final CharSequence charSequence, @NotNull final IntRange intRange) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(intRange, "range");
        return charSequence.subSequence(intRange.getStart(), intRange.getEndInclusive() + 1);
    }
    
    @Deprecated(message = "Use parameters named startIndex and endIndex.", replaceWith = @ReplaceWith(expression = "subSequence(startIndex = start, endIndex = end)", imports = {}))
    @InlineOnly
    private static final CharSequence subSequence(@NotNull final String s, final int beginIndex, final int endIndex) {
        return s.subSequence(beginIndex, endIndex);
    }
    
    @InlineOnly
    private static final String substring(@NotNull final CharSequence charSequence, final int n, final int n2) {
        return charSequence.subSequence(n, n2).toString();
    }
    
    @NotNull
    public static final String substring(@NotNull final CharSequence charSequence, @NotNull final IntRange intRange) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(intRange, "range");
        return charSequence.subSequence(intRange.getStart(), intRange.getEndInclusive() + 1).toString();
    }
    
    @NotNull
    public static final String substring(@NotNull String substring, @NotNull final IntRange intRange) {
        Intrinsics.checkParameterIsNotNull(substring, "$receiver");
        Intrinsics.checkParameterIsNotNull(intRange, "range");
        substring = substring.substring(intRange.getStart(), intRange.getEndInclusive() + 1);
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        return substring;
    }
    
    @NotNull
    public static final String substringAfter(@NotNull final String s, final char c, @NotNull String substring) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(substring, "missingDelimiterValue");
        final int indexOf$default = indexOf$default((CharSequence)s, c, 0, false, 6, (Object)null);
        if (indexOf$default != -1) {
            substring = s.substring(indexOf$default + 1, s.length());
            Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        }
        return substring;
    }
    
    @NotNull
    public static final String substringAfter(@NotNull final String s, @NotNull final String s2, @NotNull String substring) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(s2, "delimiter");
        Intrinsics.checkParameterIsNotNull(substring, "missingDelimiterValue");
        final int indexOf$default = indexOf$default((CharSequence)s, s2, 0, false, 6, (Object)null);
        if (indexOf$default != -1) {
            substring = s.substring(indexOf$default + s2.length(), s.length());
            Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        }
        return substring;
    }
    
    @NotNull
    public static final String substringAfterLast(@NotNull final String s, final char c, @NotNull String substring) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(substring, "missingDelimiterValue");
        final int lastIndexOf$default = lastIndexOf$default((CharSequence)s, c, 0, false, 6, (Object)null);
        if (lastIndexOf$default != -1) {
            substring = s.substring(lastIndexOf$default + 1, s.length());
            Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        }
        return substring;
    }
    
    @NotNull
    public static final String substringAfterLast(@NotNull final String s, @NotNull final String s2, @NotNull String substring) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(s2, "delimiter");
        Intrinsics.checkParameterIsNotNull(substring, "missingDelimiterValue");
        final int lastIndexOf$default = lastIndexOf$default((CharSequence)s, s2, 0, false, 6, (Object)null);
        if (lastIndexOf$default != -1) {
            substring = s.substring(lastIndexOf$default + s2.length(), s.length());
            Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        }
        return substring;
    }
    
    @NotNull
    public static final String substringBefore(@NotNull final String s, final char c, @NotNull String substring) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(substring, "missingDelimiterValue");
        final int indexOf$default = indexOf$default((CharSequence)s, c, 0, false, 6, (Object)null);
        if (indexOf$default != -1) {
            substring = s.substring(0, indexOf$default);
            Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        }
        return substring;
    }
    
    @NotNull
    public static final String substringBefore(@NotNull final String s, @NotNull final String s2, @NotNull String substring) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(s2, "delimiter");
        Intrinsics.checkParameterIsNotNull(substring, "missingDelimiterValue");
        final int indexOf$default = indexOf$default((CharSequence)s, s2, 0, false, 6, (Object)null);
        if (indexOf$default != -1) {
            substring = s.substring(0, indexOf$default);
            Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        }
        return substring;
    }
    
    @NotNull
    public static final String substringBeforeLast(@NotNull final String s, final char c, @NotNull String substring) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(substring, "missingDelimiterValue");
        final int lastIndexOf$default = lastIndexOf$default((CharSequence)s, c, 0, false, 6, (Object)null);
        if (lastIndexOf$default != -1) {
            substring = s.substring(0, lastIndexOf$default);
            Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        }
        return substring;
    }
    
    @NotNull
    public static final String substringBeforeLast(@NotNull final String s, @NotNull final String s2, @NotNull String substring) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(s2, "delimiter");
        Intrinsics.checkParameterIsNotNull(substring, "missingDelimiterValue");
        final int lastIndexOf$default = lastIndexOf$default((CharSequence)s, s2, 0, false, 6, (Object)null);
        if (lastIndexOf$default != -1) {
            substring = s.substring(0, lastIndexOf$default);
            Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        }
        return substring;
    }
    
    @NotNull
    public static final CharSequence trim(@NotNull final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        int n = charSequence.length() - 1;
        int i = 0;
        int n2 = 0;
        while (i <= n) {
            int n3;
            if (n2 == 0) {
                n3 = i;
            }
            else {
                n3 = n;
            }
            final boolean whitespace = CharsKt__CharJVMKt.isWhitespace(charSequence.charAt(n3));
            if (n2 == 0) {
                if (!whitespace) {
                    n2 = 1;
                }
                else {
                    ++i;
                }
            }
            else {
                if (!whitespace) {
                    break;
                }
                --n;
            }
        }
        return charSequence.subSequence(i, n + 1);
    }
    
    @NotNull
    public static final CharSequence trim(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        int n = charSequence.length() - 1;
        int i = 0;
        int n2 = 0;
        while (i <= n) {
            int n3;
            if (n2 == 0) {
                n3 = i;
            }
            else {
                n3 = n;
            }
            final boolean booleanValue = function1.invoke(charSequence.charAt(n3));
            if (n2 == 0) {
                if (!booleanValue) {
                    n2 = 1;
                }
                else {
                    ++i;
                }
            }
            else {
                if (!booleanValue) {
                    break;
                }
                --n;
            }
        }
        return charSequence.subSequence(i, n + 1);
    }
    
    @NotNull
    public static final CharSequence trim(@NotNull final CharSequence charSequence, @NotNull final char... array) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "chars");
        int n = charSequence.length() - 1;
        int i = 0;
        int n2 = 0;
        while (i <= n) {
            int n3;
            if (n2 == 0) {
                n3 = i;
            }
            else {
                n3 = n;
            }
            final boolean contains = ArraysKt___ArraysKt.contains(array, charSequence.charAt(n3));
            if (n2 == 0) {
                if (!contains) {
                    n2 = 1;
                }
                else {
                    ++i;
                }
            }
            else {
                if (!contains) {
                    break;
                }
                --n;
            }
        }
        return charSequence.subSequence(i, n + 1);
    }
    
    @InlineOnly
    private static final String trim(@NotNull final String s) {
        if (s == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
        }
        return trim((CharSequence)s).toString();
    }
    
    @NotNull
    public static final String trim(@NotNull final String s, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        final CharSequence charSequence = s;
        int n = charSequence.length() - 1;
        int i = 0;
        int n2 = 0;
        while (i <= n) {
            int n3;
            if (n2 == 0) {
                n3 = i;
            }
            else {
                n3 = n;
            }
            final boolean booleanValue = function1.invoke(charSequence.charAt(n3));
            if (n2 == 0) {
                if (!booleanValue) {
                    n2 = 1;
                }
                else {
                    ++i;
                }
            }
            else {
                if (!booleanValue) {
                    break;
                }
                --n;
            }
        }
        return charSequence.subSequence(i, n + 1).toString();
    }
    
    @NotNull
    public static final String trim(@NotNull final String s, @NotNull final char... array) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "chars");
        final CharSequence charSequence = s;
        int n = charSequence.length() - 1;
        int i = 0;
        int n2 = 0;
        while (i <= n) {
            int n3;
            if (n2 == 0) {
                n3 = i;
            }
            else {
                n3 = n;
            }
            final boolean contains = ArraysKt___ArraysKt.contains(array, charSequence.charAt(n3));
            if (n2 == 0) {
                if (!contains) {
                    n2 = 1;
                }
                else {
                    ++i;
                }
            }
            else {
                if (!contains) {
                    break;
                }
                --n;
            }
        }
        return charSequence.subSequence(i, n + 1).toString();
    }
    
    @NotNull
    public static final CharSequence trimEnd(@NotNull CharSequence subSequence) {
        Intrinsics.checkParameterIsNotNull(subSequence, "$receiver");
        int length = subSequence.length();
        int n;
        do {
            n = length - 1;
            if (n < 0) {
                subSequence = "";
                return subSequence;
            }
            length = n;
        } while (CharsKt__CharJVMKt.isWhitespace(subSequence.charAt(n)));
        subSequence = subSequence.subSequence(0, n + 1);
        return subSequence;
    }
    
    @NotNull
    public static final CharSequence trimEnd(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        int length = charSequence.length();
        int n;
        do {
            n = length - 1;
            if (n < 0) {
                return "";
            }
            length = n;
        } while (function1.invoke(charSequence.charAt(n)));
        return charSequence.subSequence(0, n + 1);
    }
    
    @NotNull
    public static final CharSequence trimEnd(@NotNull CharSequence subSequence, @NotNull final char... array) {
        Intrinsics.checkParameterIsNotNull(subSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "chars");
        int length = subSequence.length();
        int n;
        do {
            n = length - 1;
            if (n < 0) {
                subSequence = "";
                return subSequence;
            }
            length = n;
        } while (ArraysKt___ArraysKt.contains(array, subSequence.charAt(n)));
        subSequence = subSequence.subSequence(0, n + 1);
        return subSequence;
    }
    
    @InlineOnly
    private static final String trimEnd(@NotNull final String s) {
        if (s == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
        }
        return trimEnd((CharSequence)s).toString();
    }
    
    @NotNull
    public static final String trimEnd(@NotNull final String s, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        final CharSequence charSequence = s;
        int length = charSequence.length();
        int n;
        do {
            n = length - 1;
            if (n < 0) {
                final CharSequence subSequence = "";
                return subSequence.toString();
            }
            length = n;
        } while (function1.invoke(charSequence.charAt(n)));
        final CharSequence subSequence = charSequence.subSequence(0, n + 1);
        return subSequence.toString();
    }
    
    @NotNull
    public static final String trimEnd(@NotNull final String s, @NotNull final char... array) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "chars");
        final CharSequence charSequence = s;
        int length = charSequence.length();
        int n;
        do {
            n = length - 1;
            if (n < 0) {
                final CharSequence subSequence = "";
                return subSequence.toString();
            }
            length = n;
        } while (ArraysKt___ArraysKt.contains(array, charSequence.charAt(n)));
        final CharSequence subSequence = charSequence.subSequence(0, n + 1);
        return subSequence.toString();
    }
    
    @NotNull
    public static final CharSequence trimStart(@NotNull CharSequence subSequence) {
        Intrinsics.checkParameterIsNotNull(subSequence, "$receiver");
        for (int length = subSequence.length(), i = 0; i < length; ++i) {
            if (!CharsKt__CharJVMKt.isWhitespace(subSequence.charAt(i))) {
                subSequence = subSequence.subSequence(i, subSequence.length());
                return subSequence;
            }
        }
        subSequence = "";
        return subSequence;
    }
    
    @NotNull
    public static final CharSequence trimStart(@NotNull final CharSequence charSequence, @NotNull final Function1<? super Character, Boolean> function1) {
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
    public static final CharSequence trimStart(@NotNull CharSequence subSequence, @NotNull final char... array) {
        Intrinsics.checkParameterIsNotNull(subSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "chars");
        for (int length = subSequence.length(), i = 0; i < length; ++i) {
            if (!ArraysKt___ArraysKt.contains(array, subSequence.charAt(i))) {
                subSequence = subSequence.subSequence(i, subSequence.length());
                return subSequence;
            }
        }
        subSequence = "";
        return subSequence;
    }
    
    @InlineOnly
    private static final String trimStart(@NotNull final String s) {
        if (s == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
        }
        return trimStart((CharSequence)s).toString();
    }
    
    @NotNull
    public static final String trimStart(@NotNull final String s, @NotNull final Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        final CharSequence charSequence = s;
        for (int length = charSequence.length(), i = 0; i < length; ++i) {
            if (!function1.invoke(charSequence.charAt(i))) {
                final CharSequence subSequence = charSequence.subSequence(i, charSequence.length());
                return subSequence.toString();
            }
        }
        final CharSequence subSequence = "";
        return subSequence.toString();
    }
    
    @NotNull
    public static final String trimStart(@NotNull final String s, @NotNull final char... array) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "chars");
        final CharSequence charSequence = s;
        for (int length = charSequence.length(), i = 0; i < length; ++i) {
            if (!ArraysKt___ArraysKt.contains(array, charSequence.charAt(i))) {
                final CharSequence subSequence = charSequence.subSequence(i, charSequence.length());
                return subSequence.toString();
            }
        }
        final CharSequence subSequence = "";
        return subSequence.toString();
    }
}
