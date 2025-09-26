// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.ranges;

import kotlin.SinceKotlin;
import org.jetbrains.annotations.Nullable;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000b\n\u0002\b\u0002\n\u0002\u0010\u000f\n\u0002\b\u0002\n\u0002\u0010\u0005\n\u0002\u0010\u0006\n\u0002\u0010\u0007\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\u0010\n\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\f\n\u0002\b\u0015\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a'\u0010\u0000\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\u0006\u0010\u0003\u001a\u0002H\u0001¢\u0006\u0002\u0010\u0004\u001a\u0012\u0010\u0000\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0005\u001a\u0012\u0010\u0000\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u0006\u001a\u0012\u0010\u0000\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u0007\u001a\u0012\u0010\u0000\u001a\u00020\b*\u00020\b2\u0006\u0010\u0003\u001a\u00020\b\u001a\u0012\u0010\u0000\u001a\u00020\t*\u00020\t2\u0006\u0010\u0003\u001a\u00020\t\u001a\u0012\u0010\u0000\u001a\u00020\n*\u00020\n2\u0006\u0010\u0003\u001a\u00020\n\u001a'\u0010\u000b\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\u0006\u0010\f\u001a\u0002H\u0001¢\u0006\u0002\u0010\u0004\u001a\u0012\u0010\u000b\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\f\u001a\u00020\u0005\u001a\u0012\u0010\u000b\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\f\u001a\u00020\u0006\u001a\u0012\u0010\u000b\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\f\u001a\u00020\u0007\u001a\u0012\u0010\u000b\u001a\u00020\b*\u00020\b2\u0006\u0010\f\u001a\u00020\b\u001a\u0012\u0010\u000b\u001a\u00020\t*\u00020\t2\u0006\u0010\f\u001a\u00020\t\u001a\u0012\u0010\u000b\u001a\u00020\n*\u00020\n2\u0006\u0010\f\u001a\u00020\n\u001a3\u0010\r\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\b\u0010\u0003\u001a\u0004\u0018\u0001H\u00012\b\u0010\f\u001a\u0004\u0018\u0001H\u0001¢\u0006\u0002\u0010\u000e\u001a/\u0010\r\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0010H\u0007¢\u0006\u0002\u0010\u0011\u001a-\u0010\r\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0012¢\u0006\u0002\u0010\u0013\u001a\u001a\u0010\r\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u0005\u001a\u001a\u0010\r\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u0006\u001a\u001a\u0010\r\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u0007\u001a\u001a\u0010\r\u001a\u00020\b*\u00020\b2\u0006\u0010\u0003\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\b\u001a\u0018\u0010\r\u001a\u00020\b*\u00020\b2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\b0\u0012\u001a\u001a\u0010\r\u001a\u00020\t*\u00020\t2\u0006\u0010\u0003\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\t\u001a\u0018\u0010\r\u001a\u00020\t*\u00020\t2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\t0\u0012\u001a\u001a\u0010\r\u001a\u00020\n*\u00020\n2\u0006\u0010\u0003\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\n\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u0016\u001a\u00020\u0006H\u0087\u0002¢\u0006\u0002\b\u0017\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u0016\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b\u0017\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u0016\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u0017\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u0016\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u0017\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u0016\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u0017\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u0016\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u0018\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u0016\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b\u0018\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u0016\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u0018\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u0016\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u0018\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u0016\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u0018\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u0016\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u0019\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u0016\u001a\u00020\u0006H\u0087\u0002¢\u0006\u0002\b\u0019\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u0016\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u0019\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u0016\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u0019\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u0016\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u0019\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u0016\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u001a\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u0016\u001a\u00020\u0006H\u0087\u0002¢\u0006\u0002\b\u001a\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u0016\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b\u001a\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u0016\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u001a\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u0016\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001a\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u0016\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u0016\u001a\u00020\u0006H\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u0016\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u0016\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u0016\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u0016\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u0016\u001a\u00020\u0006H\u0087\u0002¢\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u0016\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u0016\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u0016\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u001c\u001a\u0015\u0010\u001d\u001a\u00020\u001e*\u00020\u00052\u0006\u0010\u001f\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020\u001e*\u00020\u00052\u0006\u0010\u001f\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020 *\u00020\u00052\u0006\u0010\u001f\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020\u001e*\u00020\u00052\u0006\u0010\u001f\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020!*\u00020\"2\u0006\u0010\u001f\u001a\u00020\"H\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020\u001e*\u00020\b2\u0006\u0010\u001f\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020\u001e*\u00020\b2\u0006\u0010\u001f\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020 *\u00020\b2\u0006\u0010\u001f\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020\u001e*\u00020\b2\u0006\u0010\u001f\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020 *\u00020\t2\u0006\u0010\u001f\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020 *\u00020\t2\u0006\u0010\u001f\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020 *\u00020\t2\u0006\u0010\u001f\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020 *\u00020\t2\u0006\u0010\u001f\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020\u001e*\u00020\n2\u0006\u0010\u001f\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020\u001e*\u00020\n2\u0006\u0010\u001f\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020 *\u00020\n2\u0006\u0010\u001f\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020\u001e*\u00020\n2\u0006\u0010\u001f\u001a\u00020\nH\u0086\u0004\u001a\n\u0010#\u001a\u00020!*\u00020!\u001a\n\u0010#\u001a\u00020\u001e*\u00020\u001e\u001a\n\u0010#\u001a\u00020 *\u00020 \u001a\u0015\u0010$\u001a\u00020!*\u00020!2\u0006\u0010$\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010$\u001a\u00020\u001e*\u00020\u001e2\u0006\u0010$\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010$\u001a\u00020 *\u00020 2\u0006\u0010$\u001a\u00020\tH\u0086\u0004\u001a\u0013\u0010%\u001a\u0004\u0018\u00010\u0005*\u00020\u0006H\u0000¢\u0006\u0002\u0010&\u001a\u0013\u0010%\u001a\u0004\u0018\u00010\u0005*\u00020\u0007H\u0000¢\u0006\u0002\u0010'\u001a\u0013\u0010%\u001a\u0004\u0018\u00010\u0005*\u00020\bH\u0000¢\u0006\u0002\u0010(\u001a\u0013\u0010%\u001a\u0004\u0018\u00010\u0005*\u00020\tH\u0000¢\u0006\u0002\u0010)\u001a\u0013\u0010%\u001a\u0004\u0018\u00010\u0005*\u00020\nH\u0000¢\u0006\u0002\u0010*\u001a\u0013\u0010+\u001a\u0004\u0018\u00010\b*\u00020\u0006H\u0000¢\u0006\u0002\u0010,\u001a\u0013\u0010+\u001a\u0004\u0018\u00010\b*\u00020\u0007H\u0000¢\u0006\u0002\u0010-\u001a\u0013\u0010+\u001a\u0004\u0018\u00010\b*\u00020\tH\u0000¢\u0006\u0002\u0010.\u001a\u0013\u0010/\u001a\u0004\u0018\u00010\t*\u00020\u0006H\u0000¢\u0006\u0002\u00100\u001a\u0013\u0010/\u001a\u0004\u0018\u00010\t*\u00020\u0007H\u0000¢\u0006\u0002\u00101\u001a\u0013\u00102\u001a\u0004\u0018\u00010\n*\u00020\u0006H\u0000¢\u0006\u0002\u00103\u001a\u0013\u00102\u001a\u0004\u0018\u00010\n*\u00020\u0007H\u0000¢\u0006\u0002\u00104\u001a\u0013\u00102\u001a\u0004\u0018\u00010\n*\u00020\bH\u0000¢\u0006\u0002\u00105\u001a\u0013\u00102\u001a\u0004\u0018\u00010\n*\u00020\tH\u0000¢\u0006\u0002\u00106\u001a\u0015\u00107\u001a\u000208*\u00020\u00052\u0006\u0010\u001f\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u00107\u001a\u000208*\u00020\u00052\u0006\u0010\u001f\u001a\u00020\bH\u0086\u0004\u001a\u0015\u00107\u001a\u000209*\u00020\u00052\u0006\u0010\u001f\u001a\u00020\tH\u0086\u0004\u001a\u0015\u00107\u001a\u000208*\u00020\u00052\u0006\u0010\u001f\u001a\u00020\nH\u0086\u0004\u001a\u0015\u00107\u001a\u00020:*\u00020\"2\u0006\u0010\u001f\u001a\u00020\"H\u0086\u0004\u001a\u0015\u00107\u001a\u000208*\u00020\b2\u0006\u0010\u001f\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u00107\u001a\u000208*\u00020\b2\u0006\u0010\u001f\u001a\u00020\bH\u0086\u0004\u001a\u0015\u00107\u001a\u000209*\u00020\b2\u0006\u0010\u001f\u001a\u00020\tH\u0086\u0004\u001a\u0015\u00107\u001a\u000208*\u00020\b2\u0006\u0010\u001f\u001a\u00020\nH\u0086\u0004\u001a\u0015\u00107\u001a\u000209*\u00020\t2\u0006\u0010\u001f\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u00107\u001a\u000209*\u00020\t2\u0006\u0010\u001f\u001a\u00020\bH\u0086\u0004\u001a\u0015\u00107\u001a\u000209*\u00020\t2\u0006\u0010\u001f\u001a\u00020\tH\u0086\u0004\u001a\u0015\u00107\u001a\u000209*\u00020\t2\u0006\u0010\u001f\u001a\u00020\nH\u0086\u0004\u001a\u0015\u00107\u001a\u000208*\u00020\n2\u0006\u0010\u001f\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u00107\u001a\u000208*\u00020\n2\u0006\u0010\u001f\u001a\u00020\bH\u0086\u0004\u001a\u0015\u00107\u001a\u000209*\u00020\n2\u0006\u0010\u001f\u001a\u00020\tH\u0086\u0004\u001a\u0015\u00107\u001a\u000208*\u00020\n2\u0006\u0010\u001f\u001a\u00020\nH\u0086\u0004¨\u0006;" }, d2 = { "coerceAtLeast", "T", "", "minimumValue", "(Ljava/lang/Comparable;Ljava/lang/Comparable;)Ljava/lang/Comparable;", "", "", "", "", "", "", "coerceAtMost", "maximumValue", "coerceIn", "(Ljava/lang/Comparable;Ljava/lang/Comparable;Ljava/lang/Comparable;)Ljava/lang/Comparable;", "range", "Lkotlin/ranges/ClosedFloatingPointRange;", "(Ljava/lang/Comparable;Lkotlin/ranges/ClosedFloatingPointRange;)Ljava/lang/Comparable;", "Lkotlin/ranges/ClosedRange;", "(Ljava/lang/Comparable;Lkotlin/ranges/ClosedRange;)Ljava/lang/Comparable;", "contains", "", "value", "byteRangeContains", "doubleRangeContains", "floatRangeContains", "intRangeContains", "longRangeContains", "shortRangeContains", "downTo", "Lkotlin/ranges/IntProgression;", "to", "Lkotlin/ranges/LongProgression;", "Lkotlin/ranges/CharProgression;", "", "reversed", "step", "toByteExactOrNull", "(D)Ljava/lang/Byte;", "(F)Ljava/lang/Byte;", "(I)Ljava/lang/Byte;", "(J)Ljava/lang/Byte;", "(S)Ljava/lang/Byte;", "toIntExactOrNull", "(D)Ljava/lang/Integer;", "(F)Ljava/lang/Integer;", "(J)Ljava/lang/Integer;", "toLongExactOrNull", "(D)Ljava/lang/Long;", "(F)Ljava/lang/Long;", "toShortExactOrNull", "(D)Ljava/lang/Short;", "(F)Ljava/lang/Short;", "(I)Ljava/lang/Short;", "(J)Ljava/lang/Short;", "until", "Lkotlin/ranges/IntRange;", "Lkotlin/ranges/LongRange;", "Lkotlin/ranges/CharRange;", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/ranges/RangesKt")
class RangesKt___RangesKt extends RangesKt__RangesKt
{
    public RangesKt___RangesKt() {
    }
    
    @JvmName(name = "byteRangeContains")
    public static final boolean byteRangeContains(@NotNull final ClosedRange<Byte> closedRange, final double n) {
        Intrinsics.checkParameterIsNotNull(closedRange, "$receiver");
        final Byte byteExactOrNull = toByteExactOrNull(n);
        return byteExactOrNull != null && closedRange.contains(byteExactOrNull);
    }
    
    @JvmName(name = "byteRangeContains")
    public static final boolean byteRangeContains(@NotNull final ClosedRange<Byte> closedRange, final float n) {
        Intrinsics.checkParameterIsNotNull(closedRange, "$receiver");
        final Byte byteExactOrNull = toByteExactOrNull(n);
        return byteExactOrNull != null && closedRange.contains(byteExactOrNull);
    }
    
    @JvmName(name = "byteRangeContains")
    public static final boolean byteRangeContains(@NotNull final ClosedRange<Byte> closedRange, final int n) {
        Intrinsics.checkParameterIsNotNull(closedRange, "$receiver");
        final Byte byteExactOrNull = toByteExactOrNull(n);
        return byteExactOrNull != null && closedRange.contains(byteExactOrNull);
    }
    
    @JvmName(name = "byteRangeContains")
    public static final boolean byteRangeContains(@NotNull final ClosedRange<Byte> closedRange, final long n) {
        Intrinsics.checkParameterIsNotNull(closedRange, "$receiver");
        final Byte byteExactOrNull = toByteExactOrNull(n);
        return byteExactOrNull != null && closedRange.contains(byteExactOrNull);
    }
    
    @JvmName(name = "byteRangeContains")
    public static final boolean byteRangeContains(@NotNull final ClosedRange<Byte> closedRange, final short n) {
        Intrinsics.checkParameterIsNotNull(closedRange, "$receiver");
        final Byte byteExactOrNull = toByteExactOrNull(n);
        return byteExactOrNull != null && closedRange.contains(byteExactOrNull);
    }
    
    public static final byte coerceAtLeast(final byte b, final byte b2) {
        byte b3 = b;
        if (b < b2) {
            b3 = b2;
        }
        return b3;
    }
    
    public static final double coerceAtLeast(final double n, final double n2) {
        double n3 = n;
        if (n < n2) {
            n3 = n2;
        }
        return n3;
    }
    
    public static final float coerceAtLeast(final float n, final float n2) {
        float n3 = n;
        if (n < n2) {
            n3 = n2;
        }
        return n3;
    }
    
    public static final int coerceAtLeast(final int n, final int n2) {
        int n3 = n;
        if (n < n2) {
            n3 = n2;
        }
        return n3;
    }
    
    public static final long coerceAtLeast(final long n, final long n2) {
        long n3 = n;
        if (n < n2) {
            n3 = n2;
        }
        return n3;
    }
    
    @NotNull
    public static final <T extends Comparable<? super T>> T coerceAtLeast(@NotNull final T t, @NotNull final T t2) {
        Intrinsics.checkParameterIsNotNull(t, "$receiver");
        Intrinsics.checkParameterIsNotNull(t2, "minimumValue");
        Comparable<? super T> comparable = t;
        if (t.compareTo(t2) < 0) {
            comparable = t2;
        }
        return (T)comparable;
    }
    
    public static final short coerceAtLeast(final short n, final short n2) {
        short n3 = n;
        if (n < n2) {
            n3 = n2;
        }
        return n3;
    }
    
    public static final byte coerceAtMost(final byte b, final byte b2) {
        byte b3 = b;
        if (b > b2) {
            b3 = b2;
        }
        return b3;
    }
    
    public static final double coerceAtMost(final double n, final double n2) {
        double n3 = n;
        if (n > n2) {
            n3 = n2;
        }
        return n3;
    }
    
    public static final float coerceAtMost(final float n, final float n2) {
        float n3 = n;
        if (n > n2) {
            n3 = n2;
        }
        return n3;
    }
    
    public static final int coerceAtMost(final int n, final int n2) {
        int n3 = n;
        if (n > n2) {
            n3 = n2;
        }
        return n3;
    }
    
    public static final long coerceAtMost(final long n, final long n2) {
        long n3 = n;
        if (n > n2) {
            n3 = n2;
        }
        return n3;
    }
    
    @NotNull
    public static final <T extends Comparable<? super T>> T coerceAtMost(@NotNull final T t, @NotNull final T t2) {
        Intrinsics.checkParameterIsNotNull(t, "$receiver");
        Intrinsics.checkParameterIsNotNull(t2, "maximumValue");
        Comparable<? super T> comparable = t;
        if (t.compareTo(t2) > 0) {
            comparable = t2;
        }
        return (T)comparable;
    }
    
    public static final short coerceAtMost(final short n, final short n2) {
        short n3 = n;
        if (n > n2) {
            n3 = n2;
        }
        return n3;
    }
    
    public static final byte coerceIn(final byte b, final byte i, final byte j) {
        if (i > j) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot coerce value to an empty range: maximum ");
            sb.append(j);
            sb.append(" is less than minimum ");
            sb.append(i);
            sb.append('.');
            throw new IllegalArgumentException(sb.toString());
        }
        if (b < i) {
            return i;
        }
        if (b > j) {
            return j;
        }
        return b;
    }
    
    public static final double coerceIn(final double n, final double d, final double d2) {
        if (d > d2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot coerce value to an empty range: maximum ");
            sb.append(d2);
            sb.append(" is less than minimum ");
            sb.append(d);
            sb.append('.');
            throw new IllegalArgumentException(sb.toString());
        }
        if (n < d) {
            return d;
        }
        if (n > d2) {
            return d2;
        }
        return n;
    }
    
    public static final float coerceIn(final float n, final float f, final float f2) {
        if (f > f2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot coerce value to an empty range: maximum ");
            sb.append(f2);
            sb.append(" is less than minimum ");
            sb.append(f);
            sb.append('.');
            throw new IllegalArgumentException(sb.toString());
        }
        if (n < f) {
            return f;
        }
        if (n > f2) {
            return f2;
        }
        return n;
    }
    
    public static final int coerceIn(final int n, final int i, final int j) {
        if (i > j) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot coerce value to an empty range: maximum ");
            sb.append(j);
            sb.append(" is less than minimum ");
            sb.append(i);
            sb.append('.');
            throw new IllegalArgumentException(sb.toString());
        }
        if (n < i) {
            return i;
        }
        if (n > j) {
            return j;
        }
        return n;
    }
    
    public static final int coerceIn(final int i, @NotNull final ClosedRange<Integer> obj) {
        Intrinsics.checkParameterIsNotNull(obj, "range");
        if (obj instanceof ClosedFloatingPointRange) {
            return coerceIn(i, (ClosedFloatingPointRange<Number>)obj).intValue();
        }
        if (obj.isEmpty()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot coerce value to an empty range: ");
            sb.append(obj);
            sb.append('.');
            throw new IllegalArgumentException(sb.toString());
        }
        int n;
        if (i < obj.getStart().intValue()) {
            n = obj.getStart().intValue();
        }
        else if ((n = i) > obj.getEndInclusive().intValue()) {
            n = obj.getEndInclusive().intValue();
        }
        return n;
    }
    
    public static final long coerceIn(final long n, final long lng, final long lng2) {
        if (lng > lng2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot coerce value to an empty range: maximum ");
            sb.append(lng2);
            sb.append(" is less than minimum ");
            sb.append(lng);
            sb.append('.');
            throw new IllegalArgumentException(sb.toString());
        }
        if (n < lng) {
            return lng;
        }
        if (n > lng2) {
            return lng2;
        }
        return n;
    }
    
    public static final long coerceIn(final long l, @NotNull final ClosedRange<Long> obj) {
        Intrinsics.checkParameterIsNotNull(obj, "range");
        if (obj instanceof ClosedFloatingPointRange) {
            return coerceIn(l, (ClosedFloatingPointRange<Number>)obj).longValue();
        }
        if (obj.isEmpty()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot coerce value to an empty range: ");
            sb.append(obj);
            sb.append('.');
            throw new IllegalArgumentException(sb.toString());
        }
        long n;
        if (l < obj.getStart().longValue()) {
            n = obj.getStart().longValue();
        }
        else {
            n = l;
            if (l > obj.getEndInclusive().longValue()) {
                n = obj.getEndInclusive().longValue();
            }
        }
        return n;
    }
    
    @NotNull
    public static final <T extends Comparable<? super T>> T coerceIn(@NotNull final T t, @Nullable final T obj, @Nullable final T obj2) {
        Intrinsics.checkParameterIsNotNull(t, "$receiver");
        if (obj != null && obj2 != null) {
            if (obj.compareTo(obj2) > 0) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Cannot coerce value to an empty range: maximum ");
                sb.append(obj2);
                sb.append(" is less than minimum ");
                sb.append(obj);
                sb.append('.');
                throw new IllegalArgumentException(sb.toString());
            }
            if (t.compareTo(obj) < 0) {
                return obj;
            }
            if (t.compareTo(obj2) > 0) {
                return obj2;
            }
        }
        else {
            if (obj != null && t.compareTo(obj) < 0) {
                return obj;
            }
            if (obj2 != null && t.compareTo(obj2) > 0) {
                return obj2;
            }
        }
        return t;
    }
    
    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <T extends Comparable<? super T>> T coerceIn(@NotNull final T t, @NotNull final ClosedFloatingPointRange<T> obj) {
        Intrinsics.checkParameterIsNotNull(t, "$receiver");
        Intrinsics.checkParameterIsNotNull(obj, "range");
        if (obj.isEmpty()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot coerce value to an empty range: ");
            sb.append(obj);
            sb.append('.');
            throw new IllegalArgumentException(sb.toString());
        }
        Comparable<? super T> comparable;
        if (obj.lessThanOrEquals(t, (T)obj.getStart()) && !obj.lessThanOrEquals((T)obj.getStart(), t)) {
            comparable = obj.getStart();
        }
        else {
            comparable = t;
            if (obj.lessThanOrEquals((T)obj.getEndInclusive(), t)) {
                comparable = t;
                if (!obj.lessThanOrEquals(t, (T)obj.getEndInclusive())) {
                    comparable = obj.getEndInclusive();
                }
            }
        }
        return (T)comparable;
    }
    
    @NotNull
    public static final <T extends Comparable<? super T>> T coerceIn(@NotNull final T t, @NotNull final ClosedRange<T> obj) {
        Intrinsics.checkParameterIsNotNull(t, "$receiver");
        Intrinsics.checkParameterIsNotNull(obj, "range");
        if (obj instanceof ClosedFloatingPointRange) {
            return coerceIn(t, (ClosedFloatingPointRange<T>)obj);
        }
        if (obj.isEmpty()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot coerce value to an empty range: ");
            sb.append(obj);
            sb.append('.');
            throw new IllegalArgumentException(sb.toString());
        }
        Comparable<? super T> comparable;
        if (t.compareTo((Object)obj.getStart()) < 0) {
            comparable = obj.getStart();
        }
        else {
            comparable = t;
            if (t.compareTo((Object)obj.getEndInclusive()) > 0) {
                comparable = obj.getEndInclusive();
            }
        }
        return (T)comparable;
    }
    
    public static final short coerceIn(final short n, final short i, final short j) {
        if (i > j) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot coerce value to an empty range: maximum ");
            sb.append(j);
            sb.append(" is less than minimum ");
            sb.append(i);
            sb.append('.');
            throw new IllegalArgumentException(sb.toString());
        }
        if (n < i) {
            return i;
        }
        if (n > j) {
            return j;
        }
        return n;
    }
    
    @JvmName(name = "doubleRangeContains")
    public static final boolean doubleRangeContains(@NotNull final ClosedRange<Double> closedRange, final byte b) {
        Intrinsics.checkParameterIsNotNull(closedRange, "$receiver");
        return closedRange.contains((double)b);
    }
    
    @JvmName(name = "doubleRangeContains")
    public static final boolean doubleRangeContains(@NotNull final ClosedRange<Double> closedRange, final float n) {
        Intrinsics.checkParameterIsNotNull(closedRange, "$receiver");
        return closedRange.contains((double)n);
    }
    
    @JvmName(name = "doubleRangeContains")
    public static final boolean doubleRangeContains(@NotNull final ClosedRange<Double> closedRange, final int n) {
        Intrinsics.checkParameterIsNotNull(closedRange, "$receiver");
        return closedRange.contains((double)n);
    }
    
    @JvmName(name = "doubleRangeContains")
    public static final boolean doubleRangeContains(@NotNull final ClosedRange<Double> closedRange, final long n) {
        Intrinsics.checkParameterIsNotNull(closedRange, "$receiver");
        return closedRange.contains(Double.valueOf(n));
    }
    
    @JvmName(name = "doubleRangeContains")
    public static final boolean doubleRangeContains(@NotNull final ClosedRange<Double> closedRange, final short n) {
        Intrinsics.checkParameterIsNotNull(closedRange, "$receiver");
        return closedRange.contains((double)n);
    }
    
    @NotNull
    public static final CharProgression downTo(final char c, final char c2) {
        return CharProgression.Companion.fromClosedRange(c, c2, -1);
    }
    
    @NotNull
    public static final IntProgression downTo(final byte b, final byte b2) {
        return IntProgression.Companion.fromClosedRange(b, b2, -1);
    }
    
    @NotNull
    public static final IntProgression downTo(final byte b, final int n) {
        return IntProgression.Companion.fromClosedRange(b, n, -1);
    }
    
    @NotNull
    public static final IntProgression downTo(final byte b, final short n) {
        return IntProgression.Companion.fromClosedRange(b, n, -1);
    }
    
    @NotNull
    public static final IntProgression downTo(final int n, final byte b) {
        return IntProgression.Companion.fromClosedRange(n, b, -1);
    }
    
    @NotNull
    public static final IntProgression downTo(final int n, final int n2) {
        return IntProgression.Companion.fromClosedRange(n, n2, -1);
    }
    
    @NotNull
    public static final IntProgression downTo(final int n, final short n2) {
        return IntProgression.Companion.fromClosedRange(n, n2, -1);
    }
    
    @NotNull
    public static final IntProgression downTo(final short n, final byte b) {
        return IntProgression.Companion.fromClosedRange(n, b, -1);
    }
    
    @NotNull
    public static final IntProgression downTo(final short n, final int n2) {
        return IntProgression.Companion.fromClosedRange(n, n2, -1);
    }
    
    @NotNull
    public static final IntProgression downTo(final short n, final short n2) {
        return IntProgression.Companion.fromClosedRange(n, n2, -1);
    }
    
    @NotNull
    public static final LongProgression downTo(final byte b, final long n) {
        return LongProgression.Companion.fromClosedRange(b, n, -1L);
    }
    
    @NotNull
    public static final LongProgression downTo(final int n, final long n2) {
        return LongProgression.Companion.fromClosedRange(n, n2, -1L);
    }
    
    @NotNull
    public static final LongProgression downTo(final long n, final byte b) {
        return LongProgression.Companion.fromClosedRange(n, b, -1L);
    }
    
    @NotNull
    public static final LongProgression downTo(final long n, final int n2) {
        return LongProgression.Companion.fromClosedRange(n, n2, -1L);
    }
    
    @NotNull
    public static final LongProgression downTo(final long n, final long n2) {
        return LongProgression.Companion.fromClosedRange(n, n2, -1L);
    }
    
    @NotNull
    public static final LongProgression downTo(final long n, final short n2) {
        return LongProgression.Companion.fromClosedRange(n, n2, -1L);
    }
    
    @NotNull
    public static final LongProgression downTo(final short n, final long n2) {
        return LongProgression.Companion.fromClosedRange(n, n2, -1L);
    }
    
    @JvmName(name = "floatRangeContains")
    public static final boolean floatRangeContains(@NotNull final ClosedRange<Float> closedRange, final byte b) {
        Intrinsics.checkParameterIsNotNull(closedRange, "$receiver");
        return closedRange.contains((float)b);
    }
    
    @JvmName(name = "floatRangeContains")
    public static final boolean floatRangeContains(@NotNull final ClosedRange<Float> closedRange, final double n) {
        Intrinsics.checkParameterIsNotNull(closedRange, "$receiver");
        return closedRange.contains((float)n);
    }
    
    @JvmName(name = "floatRangeContains")
    public static final boolean floatRangeContains(@NotNull final ClosedRange<Float> closedRange, final int n) {
        Intrinsics.checkParameterIsNotNull(closedRange, "$receiver");
        return closedRange.contains(Float.valueOf(n));
    }
    
    @JvmName(name = "floatRangeContains")
    public static final boolean floatRangeContains(@NotNull final ClosedRange<Float> closedRange, final long n) {
        Intrinsics.checkParameterIsNotNull(closedRange, "$receiver");
        return closedRange.contains(Float.valueOf(n));
    }
    
    @JvmName(name = "floatRangeContains")
    public static final boolean floatRangeContains(@NotNull final ClosedRange<Float> closedRange, final short n) {
        Intrinsics.checkParameterIsNotNull(closedRange, "$receiver");
        return closedRange.contains((float)n);
    }
    
    @JvmName(name = "intRangeContains")
    public static final boolean intRangeContains(@NotNull final ClosedRange<Integer> closedRange, final byte i) {
        Intrinsics.checkParameterIsNotNull(closedRange, "$receiver");
        return closedRange.contains((int)i);
    }
    
    @JvmName(name = "intRangeContains")
    public static final boolean intRangeContains(@NotNull final ClosedRange<Integer> closedRange, final double n) {
        Intrinsics.checkParameterIsNotNull(closedRange, "$receiver");
        final Integer intExactOrNull = toIntExactOrNull(n);
        return intExactOrNull != null && closedRange.contains(intExactOrNull);
    }
    
    @JvmName(name = "intRangeContains")
    public static final boolean intRangeContains(@NotNull final ClosedRange<Integer> closedRange, final float n) {
        Intrinsics.checkParameterIsNotNull(closedRange, "$receiver");
        final Integer intExactOrNull = toIntExactOrNull(n);
        return intExactOrNull != null && closedRange.contains(intExactOrNull);
    }
    
    @JvmName(name = "intRangeContains")
    public static final boolean intRangeContains(@NotNull final ClosedRange<Integer> closedRange, final long n) {
        Intrinsics.checkParameterIsNotNull(closedRange, "$receiver");
        final Integer intExactOrNull = toIntExactOrNull(n);
        return intExactOrNull != null && closedRange.contains(intExactOrNull);
    }
    
    @JvmName(name = "intRangeContains")
    public static final boolean intRangeContains(@NotNull final ClosedRange<Integer> closedRange, final short i) {
        Intrinsics.checkParameterIsNotNull(closedRange, "$receiver");
        return closedRange.contains((int)i);
    }
    
    @JvmName(name = "longRangeContains")
    public static final boolean longRangeContains(@NotNull final ClosedRange<Long> closedRange, final byte b) {
        Intrinsics.checkParameterIsNotNull(closedRange, "$receiver");
        return closedRange.contains((long)b);
    }
    
    @JvmName(name = "longRangeContains")
    public static final boolean longRangeContains(@NotNull final ClosedRange<Long> closedRange, final double n) {
        Intrinsics.checkParameterIsNotNull(closedRange, "$receiver");
        final Long longExactOrNull = toLongExactOrNull(n);
        return longExactOrNull != null && closedRange.contains(longExactOrNull);
    }
    
    @JvmName(name = "longRangeContains")
    public static final boolean longRangeContains(@NotNull final ClosedRange<Long> closedRange, final float n) {
        Intrinsics.checkParameterIsNotNull(closedRange, "$receiver");
        final Long longExactOrNull = toLongExactOrNull(n);
        return longExactOrNull != null && closedRange.contains(longExactOrNull);
    }
    
    @JvmName(name = "longRangeContains")
    public static final boolean longRangeContains(@NotNull final ClosedRange<Long> closedRange, final int n) {
        Intrinsics.checkParameterIsNotNull(closedRange, "$receiver");
        return closedRange.contains((long)n);
    }
    
    @JvmName(name = "longRangeContains")
    public static final boolean longRangeContains(@NotNull final ClosedRange<Long> closedRange, final short n) {
        Intrinsics.checkParameterIsNotNull(closedRange, "$receiver");
        return closedRange.contains((long)n);
    }
    
    @NotNull
    public static final CharProgression reversed(@NotNull final CharProgression charProgression) {
        Intrinsics.checkParameterIsNotNull(charProgression, "$receiver");
        return CharProgression.Companion.fromClosedRange(charProgression.getLast(), charProgression.getFirst(), -charProgression.getStep());
    }
    
    @NotNull
    public static final IntProgression reversed(@NotNull final IntProgression intProgression) {
        Intrinsics.checkParameterIsNotNull(intProgression, "$receiver");
        return IntProgression.Companion.fromClosedRange(intProgression.getLast(), intProgression.getFirst(), -intProgression.getStep());
    }
    
    @NotNull
    public static final LongProgression reversed(@NotNull final LongProgression longProgression) {
        Intrinsics.checkParameterIsNotNull(longProgression, "$receiver");
        return LongProgression.Companion.fromClosedRange(longProgression.getLast(), longProgression.getFirst(), -longProgression.getStep());
    }
    
    @JvmName(name = "shortRangeContains")
    public static final boolean shortRangeContains(@NotNull final ClosedRange<Short> closedRange, final byte b) {
        Intrinsics.checkParameterIsNotNull(closedRange, "$receiver");
        return closedRange.contains((short)b);
    }
    
    @JvmName(name = "shortRangeContains")
    public static final boolean shortRangeContains(@NotNull final ClosedRange<Short> closedRange, final double n) {
        Intrinsics.checkParameterIsNotNull(closedRange, "$receiver");
        final Short shortExactOrNull = toShortExactOrNull(n);
        return shortExactOrNull != null && closedRange.contains(shortExactOrNull);
    }
    
    @JvmName(name = "shortRangeContains")
    public static final boolean shortRangeContains(@NotNull final ClosedRange<Short> closedRange, final float n) {
        Intrinsics.checkParameterIsNotNull(closedRange, "$receiver");
        final Short shortExactOrNull = toShortExactOrNull(n);
        return shortExactOrNull != null && closedRange.contains(shortExactOrNull);
    }
    
    @JvmName(name = "shortRangeContains")
    public static final boolean shortRangeContains(@NotNull final ClosedRange<Short> closedRange, final int n) {
        Intrinsics.checkParameterIsNotNull(closedRange, "$receiver");
        final Short shortExactOrNull = toShortExactOrNull(n);
        return shortExactOrNull != null && closedRange.contains(shortExactOrNull);
    }
    
    @JvmName(name = "shortRangeContains")
    public static final boolean shortRangeContains(@NotNull final ClosedRange<Short> closedRange, final long n) {
        Intrinsics.checkParameterIsNotNull(closedRange, "$receiver");
        final Short shortExactOrNull = toShortExactOrNull(n);
        return shortExactOrNull != null && closedRange.contains(shortExactOrNull);
    }
    
    @NotNull
    public static final CharProgression step(@NotNull final CharProgression charProgression, int i) {
        Intrinsics.checkParameterIsNotNull(charProgression, "$receiver");
        RangesKt__RangesKt.checkStepIsPositive(i > 0, i);
        final CharProgression.Companion companion = CharProgression.Companion;
        final char first = charProgression.getFirst();
        final char last = charProgression.getLast();
        if (charProgression.getStep() <= 0) {
            i = -i;
        }
        return companion.fromClosedRange(first, last, i);
    }
    
    @NotNull
    public static final IntProgression step(@NotNull final IntProgression intProgression, int i) {
        Intrinsics.checkParameterIsNotNull(intProgression, "$receiver");
        RangesKt__RangesKt.checkStepIsPositive(i > 0, i);
        final IntProgression.Companion companion = IntProgression.Companion;
        final int first = intProgression.getFirst();
        final int last = intProgression.getLast();
        if (intProgression.getStep() <= 0) {
            i = -i;
        }
        return companion.fromClosedRange(first, last, i);
    }
    
    @NotNull
    public static final LongProgression step(@NotNull final LongProgression longProgression, long l) {
        Intrinsics.checkParameterIsNotNull(longProgression, "$receiver");
        RangesKt__RangesKt.checkStepIsPositive(l > 0L, l);
        final LongProgression.Companion companion = LongProgression.Companion;
        final long first = longProgression.getFirst();
        final long last = longProgression.getLast();
        if (longProgression.getStep() <= 0L) {
            l = -l;
        }
        return companion.fromClosedRange(first, last, l);
    }
    
    @Nullable
    public static final Byte toByteExactOrNull(final double n) {
        final double n2 = -128;
        final double n3 = 127;
        Byte value;
        if (n >= n2 && n <= n3) {
            value = (byte)n;
        }
        else {
            value = null;
        }
        return value;
    }
    
    @Nullable
    public static final Byte toByteExactOrNull(final float n) {
        final float n2 = -128;
        final float n3 = 127;
        Byte value;
        if (n >= n2 && n <= n3) {
            value = (byte)n;
        }
        else {
            value = null;
        }
        return value;
    }
    
    @Nullable
    public static final Byte toByteExactOrNull(final int n) {
        if (-128 <= n) {
            if (127 >= n) {
                return (byte)n;
            }
        }
        return null;
    }
    
    @Nullable
    public static final Byte toByteExactOrNull(final long n) {
        final long n2 = -128;
        final long n3 = 127;
        if (n2 <= n) {
            if (n3 >= n) {
                return (byte)n;
            }
        }
        return null;
    }
    
    @Nullable
    public static final Byte toByteExactOrNull(final short n) {
        final short n2 = -128;
        final short n3 = 127;
        if (n2 <= n) {
            if (n3 >= n) {
                return (byte)n;
            }
        }
        return null;
    }
    
    @Nullable
    public static final Integer toIntExactOrNull(final double n) {
        final double n2 = Integer.MIN_VALUE;
        final double n3 = Integer.MAX_VALUE;
        Integer value;
        if (n >= n2 && n <= n3) {
            value = (int)n;
        }
        else {
            value = null;
        }
        return value;
    }
    
    @Nullable
    public static final Integer toIntExactOrNull(final float n) {
        final float n2 = Integer.MIN_VALUE;
        final float n3 = Integer.MAX_VALUE;
        Integer value;
        if (n >= n2 && n <= n3) {
            value = (int)n;
        }
        else {
            value = null;
        }
        return value;
    }
    
    @Nullable
    public static final Integer toIntExactOrNull(final long n) {
        final long n2 = Integer.MIN_VALUE;
        final long n3 = Integer.MAX_VALUE;
        if (n2 <= n) {
            if (n3 >= n) {
                return (int)n;
            }
        }
        return null;
    }
    
    @Nullable
    public static final Long toLongExactOrNull(final double n) {
        final double n2 = Long.MIN_VALUE;
        final double n3 = Long.MAX_VALUE;
        Long value;
        if (n >= n2 && n <= n3) {
            value = (long)n;
        }
        else {
            value = null;
        }
        return value;
    }
    
    @Nullable
    public static final Long toLongExactOrNull(final float n) {
        final float n2 = Long.MIN_VALUE;
        final float n3 = Long.MAX_VALUE;
        Long value;
        if (n >= n2 && n <= n3) {
            value = (long)n;
        }
        else {
            value = null;
        }
        return value;
    }
    
    @Nullable
    public static final Short toShortExactOrNull(final double n) {
        final double n2 = -32768;
        final double n3 = 32767;
        Short value;
        if (n >= n2 && n <= n3) {
            value = (short)n;
        }
        else {
            value = null;
        }
        return value;
    }
    
    @Nullable
    public static final Short toShortExactOrNull(final float n) {
        final float n2 = -32768;
        final float n3 = 32767;
        Short value;
        if (n >= n2 && n <= n3) {
            value = (short)n;
        }
        else {
            value = null;
        }
        return value;
    }
    
    @Nullable
    public static final Short toShortExactOrNull(final int n) {
        if (-32768 <= n) {
            if (32767 >= n) {
                return (short)n;
            }
        }
        return null;
    }
    
    @Nullable
    public static final Short toShortExactOrNull(final long n) {
        final long n2 = -32768;
        final long n3 = 32767;
        if (n2 <= n) {
            if (n3 >= n) {
                return (short)n;
            }
        }
        return null;
    }
    
    @NotNull
    public static final CharRange until(final char c, final char c2) {
        if (c2 <= '\0') {
            return CharRange.Companion.getEMPTY();
        }
        return new CharRange(c, (char)(c2 - '\u0001'));
    }
    
    @NotNull
    public static final IntRange until(final byte b, final byte b2) {
        return new IntRange(b, b2 - 1);
    }
    
    @NotNull
    public static final IntRange until(final byte b, final int n) {
        if (n <= Integer.MIN_VALUE) {
            return IntRange.Companion.getEMPTY();
        }
        return new IntRange(b, n - 1);
    }
    
    @NotNull
    public static final IntRange until(final byte b, final short n) {
        return new IntRange(b, n - 1);
    }
    
    @NotNull
    public static final IntRange until(final int n, final byte b) {
        return new IntRange(n, b - 1);
    }
    
    @NotNull
    public static final IntRange until(final int n, final int n2) {
        if (n2 <= Integer.MIN_VALUE) {
            return IntRange.Companion.getEMPTY();
        }
        return new IntRange(n, n2 - 1);
    }
    
    @NotNull
    public static final IntRange until(final int n, final short n2) {
        return new IntRange(n, n2 - 1);
    }
    
    @NotNull
    public static final IntRange until(final short n, final byte b) {
        return new IntRange(n, b - 1);
    }
    
    @NotNull
    public static final IntRange until(final short n, final int n2) {
        if (n2 <= Integer.MIN_VALUE) {
            return IntRange.Companion.getEMPTY();
        }
        return new IntRange(n, n2 - 1);
    }
    
    @NotNull
    public static final IntRange until(final short n, final short n2) {
        return new IntRange(n, n2 - 1);
    }
    
    @NotNull
    public static final LongRange until(final byte b, final long n) {
        if (n <= Long.MIN_VALUE) {
            return LongRange.Companion.getEMPTY();
        }
        return new LongRange(b, n - 1L);
    }
    
    @NotNull
    public static final LongRange until(final int n, final long n2) {
        if (n2 <= Long.MIN_VALUE) {
            return LongRange.Companion.getEMPTY();
        }
        return new LongRange(n, n2 - 1L);
    }
    
    @NotNull
    public static final LongRange until(final long n, final byte b) {
        return new LongRange(n, b - 1L);
    }
    
    @NotNull
    public static final LongRange until(final long n, final int n2) {
        return new LongRange(n, n2 - 1L);
    }
    
    @NotNull
    public static final LongRange until(final long n, final long n2) {
        if (n2 <= Long.MIN_VALUE) {
            return LongRange.Companion.getEMPTY();
        }
        return new LongRange(n, n2 - 1L);
    }
    
    @NotNull
    public static final LongRange until(final long n, final short n2) {
        return new LongRange(n, n2 - 1L);
    }
    
    @NotNull
    public static final LongRange until(final short n, final long n2) {
        if (n2 <= Long.MIN_VALUE) {
            return LongRange.Companion.getEMPTY();
        }
        return new LongRange(n, n2 - 1L);
    }
}
