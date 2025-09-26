package kotlin.ranges;

import com.sonymobile.cameracommon.research.idd.IddUtil;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.LongCompanionObject;
import kotlin.ranges.CharProgression;
import kotlin.ranges.IntProgression;
import kotlin.ranges.LongProgression;
import org.apache.commons.imaging.formats.jpeg.iptc.IptcConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: _Ranges.kt */
@Metadata(bv = {1, 0, 2}, d1 = {"\u0000b\n\u0002\b\u0002\n\u0002\u0010\u000f\n\u0002\b\u0002\n\u0002\u0010\u0005\n\u0002\u0010\u0006\n\u0002\u0010\u0007\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\u0010\n\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\f\n\u0002\b\u0015\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a'\u0010\u0000\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\u0006\u0010\u0003\u001a\u0002H\u0001¢\u0006\u0002\u0010\u0004\u001a\u0012\u0010\u0000\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0005\u001a\u0012\u0010\u0000\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u0006\u001a\u0012\u0010\u0000\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u0007\u001a\u0012\u0010\u0000\u001a\u00020\b*\u00020\b2\u0006\u0010\u0003\u001a\u00020\b\u001a\u0012\u0010\u0000\u001a\u00020\t*\u00020\t2\u0006\u0010\u0003\u001a\u00020\t\u001a\u0012\u0010\u0000\u001a\u00020\n*\u00020\n2\u0006\u0010\u0003\u001a\u00020\n\u001a'\u0010\u000b\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\u0006\u0010\f\u001a\u0002H\u0001¢\u0006\u0002\u0010\u0004\u001a\u0012\u0010\u000b\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\f\u001a\u00020\u0005\u001a\u0012\u0010\u000b\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\f\u001a\u00020\u0006\u001a\u0012\u0010\u000b\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\f\u001a\u00020\u0007\u001a\u0012\u0010\u000b\u001a\u00020\b*\u00020\b2\u0006\u0010\f\u001a\u00020\b\u001a\u0012\u0010\u000b\u001a\u00020\t*\u00020\t2\u0006\u0010\f\u001a\u00020\t\u001a\u0012\u0010\u000b\u001a\u00020\n*\u00020\n2\u0006\u0010\f\u001a\u00020\n\u001a3\u0010\r\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\b\u0010\u0003\u001a\u0004\u0018\u0001H\u00012\b\u0010\f\u001a\u0004\u0018\u0001H\u0001¢\u0006\u0002\u0010\u000e\u001a/\u0010\r\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0010H\u0007¢\u0006\u0002\u0010\u0011\u001a-\u0010\r\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0012¢\u0006\u0002\u0010\u0013\u001a\u001a\u0010\r\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u0005\u001a\u001a\u0010\r\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u0006\u001a\u001a\u0010\r\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u0007\u001a\u001a\u0010\r\u001a\u00020\b*\u00020\b2\u0006\u0010\u0003\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\b\u001a\u0018\u0010\r\u001a\u00020\b*\u00020\b2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\b0\u0012\u001a\u001a\u0010\r\u001a\u00020\t*\u00020\t2\u0006\u0010\u0003\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\t\u001a\u0018\u0010\r\u001a\u00020\t*\u00020\t2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\t0\u0012\u001a\u001a\u0010\r\u001a\u00020\n*\u00020\n2\u0006\u0010\u0003\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\n\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u0016\u001a\u00020\u0006H\u0087\u0002¢\u0006\u0002\b\u0017\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u0016\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b\u0017\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u0016\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u0017\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u0016\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u0017\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u0016\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u0017\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u0016\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u0018\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u0016\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b\u0018\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u0016\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u0018\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u0016\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u0018\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u0016\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u0018\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u0016\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u0019\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u0016\u001a\u00020\u0006H\u0087\u0002¢\u0006\u0002\b\u0019\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u0016\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u0019\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u0016\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u0019\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u0016\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u0019\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u0016\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u001a\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u0016\u001a\u00020\u0006H\u0087\u0002¢\u0006\u0002\b\u001a\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u0016\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b\u001a\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u0016\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u001a\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u0016\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001a\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u0016\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u0016\u001a\u00020\u0006H\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u0016\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u0016\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u0016\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u0016\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u0016\u001a\u00020\u0006H\u0087\u0002¢\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u0016\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u0016\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u0016\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u001c\u001a\u0015\u0010\u001d\u001a\u00020\u001e*\u00020\u00052\u0006\u0010\u001f\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020\u001e*\u00020\u00052\u0006\u0010\u001f\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020 *\u00020\u00052\u0006\u0010\u001f\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020\u001e*\u00020\u00052\u0006\u0010\u001f\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020!*\u00020\"2\u0006\u0010\u001f\u001a\u00020\"H\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020\u001e*\u00020\b2\u0006\u0010\u001f\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020\u001e*\u00020\b2\u0006\u0010\u001f\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020 *\u00020\b2\u0006\u0010\u001f\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020\u001e*\u00020\b2\u0006\u0010\u001f\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020 *\u00020\t2\u0006\u0010\u001f\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020 *\u00020\t2\u0006\u0010\u001f\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020 *\u00020\t2\u0006\u0010\u001f\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020 *\u00020\t2\u0006\u0010\u001f\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020\u001e*\u00020\n2\u0006\u0010\u001f\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020\u001e*\u00020\n2\u0006\u0010\u001f\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020 *\u00020\n2\u0006\u0010\u001f\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010\u001d\u001a\u00020\u001e*\u00020\n2\u0006\u0010\u001f\u001a\u00020\nH\u0086\u0004\u001a\n\u0010#\u001a\u00020!*\u00020!\u001a\n\u0010#\u001a\u00020\u001e*\u00020\u001e\u001a\n\u0010#\u001a\u00020 *\u00020 \u001a\u0015\u0010$\u001a\u00020!*\u00020!2\u0006\u0010$\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010$\u001a\u00020\u001e*\u00020\u001e2\u0006\u0010$\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010$\u001a\u00020 *\u00020 2\u0006\u0010$\u001a\u00020\tH\u0086\u0004\u001a\u0013\u0010%\u001a\u0004\u0018\u00010\u0005*\u00020\u0006H\u0000¢\u0006\u0002\u0010&\u001a\u0013\u0010%\u001a\u0004\u0018\u00010\u0005*\u00020\u0007H\u0000¢\u0006\u0002\u0010'\u001a\u0013\u0010%\u001a\u0004\u0018\u00010\u0005*\u00020\bH\u0000¢\u0006\u0002\u0010(\u001a\u0013\u0010%\u001a\u0004\u0018\u00010\u0005*\u00020\tH\u0000¢\u0006\u0002\u0010)\u001a\u0013\u0010%\u001a\u0004\u0018\u00010\u0005*\u00020\nH\u0000¢\u0006\u0002\u0010*\u001a\u0013\u0010+\u001a\u0004\u0018\u00010\b*\u00020\u0006H\u0000¢\u0006\u0002\u0010,\u001a\u0013\u0010+\u001a\u0004\u0018\u00010\b*\u00020\u0007H\u0000¢\u0006\u0002\u0010-\u001a\u0013\u0010+\u001a\u0004\u0018\u00010\b*\u00020\tH\u0000¢\u0006\u0002\u0010.\u001a\u0013\u0010/\u001a\u0004\u0018\u00010\t*\u00020\u0006H\u0000¢\u0006\u0002\u00100\u001a\u0013\u0010/\u001a\u0004\u0018\u00010\t*\u00020\u0007H\u0000¢\u0006\u0002\u00101\u001a\u0013\u00102\u001a\u0004\u0018\u00010\n*\u00020\u0006H\u0000¢\u0006\u0002\u00103\u001a\u0013\u00102\u001a\u0004\u0018\u00010\n*\u00020\u0007H\u0000¢\u0006\u0002\u00104\u001a\u0013\u00102\u001a\u0004\u0018\u00010\n*\u00020\bH\u0000¢\u0006\u0002\u00105\u001a\u0013\u00102\u001a\u0004\u0018\u00010\n*\u00020\tH\u0000¢\u0006\u0002\u00106\u001a\u0015\u00107\u001a\u000208*\u00020\u00052\u0006\u0010\u001f\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u00107\u001a\u000208*\u00020\u00052\u0006\u0010\u001f\u001a\u00020\bH\u0086\u0004\u001a\u0015\u00107\u001a\u000209*\u00020\u00052\u0006\u0010\u001f\u001a\u00020\tH\u0086\u0004\u001a\u0015\u00107\u001a\u000208*\u00020\u00052\u0006\u0010\u001f\u001a\u00020\nH\u0086\u0004\u001a\u0015\u00107\u001a\u00020:*\u00020\"2\u0006\u0010\u001f\u001a\u00020\"H\u0086\u0004\u001a\u0015\u00107\u001a\u000208*\u00020\b2\u0006\u0010\u001f\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u00107\u001a\u000208*\u00020\b2\u0006\u0010\u001f\u001a\u00020\bH\u0086\u0004\u001a\u0015\u00107\u001a\u000209*\u00020\b2\u0006\u0010\u001f\u001a\u00020\tH\u0086\u0004\u001a\u0015\u00107\u001a\u000208*\u00020\b2\u0006\u0010\u001f\u001a\u00020\nH\u0086\u0004\u001a\u0015\u00107\u001a\u000209*\u00020\t2\u0006\u0010\u001f\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u00107\u001a\u000209*\u00020\t2\u0006\u0010\u001f\u001a\u00020\bH\u0086\u0004\u001a\u0015\u00107\u001a\u000209*\u00020\t2\u0006\u0010\u001f\u001a\u00020\tH\u0086\u0004\u001a\u0015\u00107\u001a\u000209*\u00020\t2\u0006\u0010\u001f\u001a\u00020\nH\u0086\u0004\u001a\u0015\u00107\u001a\u000208*\u00020\n2\u0006\u0010\u001f\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u00107\u001a\u000208*\u00020\n2\u0006\u0010\u001f\u001a\u00020\bH\u0086\u0004\u001a\u0015\u00107\u001a\u000209*\u00020\n2\u0006\u0010\u001f\u001a\u00020\tH\u0086\u0004\u001a\u0015\u00107\u001a\u000208*\u00020\n2\u0006\u0010\u001f\u001a\u00020\nH\u0086\u0004¨\u0006;"}, d2 = {"coerceAtLeast", "T", "", "minimumValue", "(Ljava/lang/Comparable;Ljava/lang/Comparable;)Ljava/lang/Comparable;", "", "", "", "", "", "", "coerceAtMost", "maximumValue", "coerceIn", "(Ljava/lang/Comparable;Ljava/lang/Comparable;Ljava/lang/Comparable;)Ljava/lang/Comparable;", "range", "Lkotlin/ranges/ClosedFloatingPointRange;", "(Ljava/lang/Comparable;Lkotlin/ranges/ClosedFloatingPointRange;)Ljava/lang/Comparable;", "Lkotlin/ranges/ClosedRange;", "(Ljava/lang/Comparable;Lkotlin/ranges/ClosedRange;)Ljava/lang/Comparable;", "contains", "", "value", "byteRangeContains", "doubleRangeContains", "floatRangeContains", "intRangeContains", "longRangeContains", "shortRangeContains", "downTo", "Lkotlin/ranges/IntProgression;", IddUtil.KEY_VALUE_TO, "Lkotlin/ranges/LongProgression;", "Lkotlin/ranges/CharProgression;", "", "reversed", "step", "toByteExactOrNull", "(D)Ljava/lang/Byte;", "(F)Ljava/lang/Byte;", "(I)Ljava/lang/Byte;", "(J)Ljava/lang/Byte;", "(S)Ljava/lang/Byte;", "toIntExactOrNull", "(D)Ljava/lang/Integer;", "(F)Ljava/lang/Integer;", "(J)Ljava/lang/Integer;", "toLongExactOrNull", "(D)Ljava/lang/Long;", "(F)Ljava/lang/Long;", "toShortExactOrNull", "(D)Ljava/lang/Short;", "(F)Ljava/lang/Short;", "(I)Ljava/lang/Short;", "(J)Ljava/lang/Short;", "until", "Lkotlin/ranges/IntRange;", "Lkotlin/ranges/LongRange;", "Lkotlin/ranges/CharRange;", "kotlin-stdlib"}, k = 5, mv = {1, 1, 10}, xi = 1, xs = "kotlin/ranges/RangesKt")
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class RangesKt___RangesKt extends RangesKt__RangesKt {
    public static final byte coerceAtLeast(byte b, byte b2) {
        return b < b2 ? b2 : b;
    }

    public static final double coerceAtLeast(double d, double d2) {
        return d < d2 ? d2 : d;
    }

    public static final float coerceAtLeast(float f, float f2) {
        return f < f2 ? f2 : f;
    }

    public static final int coerceAtLeast(int i, int i2) {
        return i < i2 ? i2 : i;
    }

    public static final long coerceAtLeast(long j, long j2) {
        return j < j2 ? j2 : j;
    }

    public static final short coerceAtLeast(short s, short s2) {
        return s < s2 ? s2 : s;
    }

    public static final byte coerceAtMost(byte b, byte b2) {
        return b > b2 ? b2 : b;
    }

    public static final double coerceAtMost(double d, double d2) {
        return d > d2 ? d2 : d;
    }

    public static final float coerceAtMost(float f, float f2) {
        return f > f2 ? f2 : f;
    }

    public static final int coerceAtMost(int i, int i2) {
        return i > i2 ? i2 : i;
    }

    public static final long coerceAtMost(long j, long j2) {
        return j > j2 ? j2 : j;
    }

    public static final short coerceAtMost(short s, short s2) {
        return s > s2 ? s2 : s;
    }

    @JvmName(name = "intRangeContains")
    public static final boolean intRangeContains(@NotNull ClosedRange<Integer> receiver, byte b) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return receiver.contains(Integer.valueOf(b));
    }

    @JvmName(name = "longRangeContains")
    public static final boolean longRangeContains(@NotNull ClosedRange<Long> receiver, byte b) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return receiver.contains(Long.valueOf(b));
    }

    @JvmName(name = "shortRangeContains")
    public static final boolean shortRangeContains(@NotNull ClosedRange<Short> receiver, byte b) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return receiver.contains(Short.valueOf(b));
    }

    @JvmName(name = "doubleRangeContains")
    public static final boolean doubleRangeContains(@NotNull ClosedRange<Double> receiver, byte b) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return receiver.contains(Double.valueOf(b));
    }

    @JvmName(name = "floatRangeContains")
    public static final boolean floatRangeContains(@NotNull ClosedRange<Float> receiver, byte b) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return receiver.contains(Float.valueOf(b));
    }

    @JvmName(name = "intRangeContains")
    public static final boolean intRangeContains(@NotNull ClosedRange<Integer> receiver, double d) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Integer intExactOrNull = RangesKt.toIntExactOrNull(d);
        if (intExactOrNull != null) {
            return receiver.contains(intExactOrNull);
        }
        return false;
    }

    @JvmName(name = "longRangeContains")
    public static final boolean longRangeContains(@NotNull ClosedRange<Long> receiver, double d) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Long longExactOrNull = RangesKt.toLongExactOrNull(d);
        if (longExactOrNull != null) {
            return receiver.contains(longExactOrNull);
        }
        return false;
    }

    @JvmName(name = "byteRangeContains")
    public static final boolean byteRangeContains(@NotNull ClosedRange<Byte> receiver, double d) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Byte byteExactOrNull = RangesKt.toByteExactOrNull(d);
        if (byteExactOrNull != null) {
            return receiver.contains(byteExactOrNull);
        }
        return false;
    }

    @JvmName(name = "shortRangeContains")
    public static final boolean shortRangeContains(@NotNull ClosedRange<Short> receiver, double d) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Short shortExactOrNull = RangesKt.toShortExactOrNull(d);
        if (shortExactOrNull != null) {
            return receiver.contains(shortExactOrNull);
        }
        return false;
    }

    @JvmName(name = "floatRangeContains")
    public static final boolean floatRangeContains(@NotNull ClosedRange<Float> receiver, double d) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return receiver.contains(Float.valueOf((float) d));
    }

    @JvmName(name = "intRangeContains")
    public static final boolean intRangeContains(@NotNull ClosedRange<Integer> receiver, float f) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Integer intExactOrNull = RangesKt.toIntExactOrNull(f);
        if (intExactOrNull != null) {
            return receiver.contains(intExactOrNull);
        }
        return false;
    }

    @JvmName(name = "longRangeContains")
    public static final boolean longRangeContains(@NotNull ClosedRange<Long> receiver, float f) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Long longExactOrNull = RangesKt.toLongExactOrNull(f);
        if (longExactOrNull != null) {
            return receiver.contains(longExactOrNull);
        }
        return false;
    }

    @JvmName(name = "byteRangeContains")
    public static final boolean byteRangeContains(@NotNull ClosedRange<Byte> receiver, float f) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Byte byteExactOrNull = RangesKt.toByteExactOrNull(f);
        if (byteExactOrNull != null) {
            return receiver.contains(byteExactOrNull);
        }
        return false;
    }

    @JvmName(name = "shortRangeContains")
    public static final boolean shortRangeContains(@NotNull ClosedRange<Short> receiver, float f) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Short shortExactOrNull = RangesKt.toShortExactOrNull(f);
        if (shortExactOrNull != null) {
            return receiver.contains(shortExactOrNull);
        }
        return false;
    }

    @JvmName(name = "doubleRangeContains")
    public static final boolean doubleRangeContains(@NotNull ClosedRange<Double> receiver, float f) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return receiver.contains(Double.valueOf(f));
    }

    @JvmName(name = "longRangeContains")
    public static final boolean longRangeContains(@NotNull ClosedRange<Long> receiver, int i) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return receiver.contains(Long.valueOf(i));
    }

    @JvmName(name = "byteRangeContains")
    public static final boolean byteRangeContains(@NotNull ClosedRange<Byte> receiver, int i) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Byte byteExactOrNull = RangesKt.toByteExactOrNull(i);
        if (byteExactOrNull != null) {
            return receiver.contains(byteExactOrNull);
        }
        return false;
    }

    @JvmName(name = "shortRangeContains")
    public static final boolean shortRangeContains(@NotNull ClosedRange<Short> receiver, int i) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Short shortExactOrNull = RangesKt.toShortExactOrNull(i);
        if (shortExactOrNull != null) {
            return receiver.contains(shortExactOrNull);
        }
        return false;
    }

    @JvmName(name = "doubleRangeContains")
    public static final boolean doubleRangeContains(@NotNull ClosedRange<Double> receiver, int i) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return receiver.contains(Double.valueOf(i));
    }

    @JvmName(name = "floatRangeContains")
    public static final boolean floatRangeContains(@NotNull ClosedRange<Float> receiver, int i) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return receiver.contains(Float.valueOf(i));
    }

    @JvmName(name = "intRangeContains")
    public static final boolean intRangeContains(@NotNull ClosedRange<Integer> receiver, long j) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Integer intExactOrNull = RangesKt.toIntExactOrNull(j);
        if (intExactOrNull != null) {
            return receiver.contains(intExactOrNull);
        }
        return false;
    }

    @JvmName(name = "byteRangeContains")
    public static final boolean byteRangeContains(@NotNull ClosedRange<Byte> receiver, long j) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Byte byteExactOrNull = RangesKt.toByteExactOrNull(j);
        if (byteExactOrNull != null) {
            return receiver.contains(byteExactOrNull);
        }
        return false;
    }

    @JvmName(name = "shortRangeContains")
    public static final boolean shortRangeContains(@NotNull ClosedRange<Short> receiver, long j) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Short shortExactOrNull = RangesKt.toShortExactOrNull(j);
        if (shortExactOrNull != null) {
            return receiver.contains(shortExactOrNull);
        }
        return false;
    }

    @JvmName(name = "doubleRangeContains")
    public static final boolean doubleRangeContains(@NotNull ClosedRange<Double> receiver, long j) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return receiver.contains(Double.valueOf(j));
    }

    @JvmName(name = "floatRangeContains")
    public static final boolean floatRangeContains(@NotNull ClosedRange<Float> receiver, long j) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return receiver.contains(Float.valueOf(j));
    }

    @JvmName(name = "intRangeContains")
    public static final boolean intRangeContains(@NotNull ClosedRange<Integer> receiver, short s) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return receiver.contains(Integer.valueOf(s));
    }

    @JvmName(name = "longRangeContains")
    public static final boolean longRangeContains(@NotNull ClosedRange<Long> receiver, short s) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return receiver.contains(Long.valueOf(s));
    }

    @JvmName(name = "byteRangeContains")
    public static final boolean byteRangeContains(@NotNull ClosedRange<Byte> receiver, short s) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Byte byteExactOrNull = RangesKt.toByteExactOrNull(s);
        if (byteExactOrNull != null) {
            return receiver.contains(byteExactOrNull);
        }
        return false;
    }

    @JvmName(name = "doubleRangeContains")
    public static final boolean doubleRangeContains(@NotNull ClosedRange<Double> receiver, short s) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return receiver.contains(Double.valueOf(s));
    }

    @JvmName(name = "floatRangeContains")
    public static final boolean floatRangeContains(@NotNull ClosedRange<Float> receiver, short s) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return receiver.contains(Float.valueOf(s));
    }

    @NotNull
    public static final IntProgression downTo(int i, byte b) {
        return IntProgression.INSTANCE.fromClosedRange(i, b, -1);
    }

    @NotNull
    public static final LongProgression downTo(long j, byte b) {
        return LongProgression.INSTANCE.fromClosedRange(j, b, -1L);
    }

    @NotNull
    public static final IntProgression downTo(byte b, byte b2) {
        return IntProgression.INSTANCE.fromClosedRange(b, b2, -1);
    }

    @NotNull
    public static final IntProgression downTo(short s, byte b) {
        return IntProgression.INSTANCE.fromClosedRange(s, b, -1);
    }

    @NotNull
    public static final CharProgression downTo(char c, char c2) {
        return CharProgression.INSTANCE.fromClosedRange(c, c2, -1);
    }

    @NotNull
    public static final IntProgression downTo(int i, int i2) {
        return IntProgression.INSTANCE.fromClosedRange(i, i2, -1);
    }

    @NotNull
    public static final LongProgression downTo(long j, int i) {
        return LongProgression.INSTANCE.fromClosedRange(j, i, -1L);
    }

    @NotNull
    public static final IntProgression downTo(byte b, int i) {
        return IntProgression.INSTANCE.fromClosedRange(b, i, -1);
    }

    @NotNull
    public static final IntProgression downTo(short s, int i) {
        return IntProgression.INSTANCE.fromClosedRange(s, i, -1);
    }

    @NotNull
    public static final LongProgression downTo(int i, long j) {
        return LongProgression.INSTANCE.fromClosedRange(i, j, -1L);
    }

    @NotNull
    public static final LongProgression downTo(long j, long j2) {
        return LongProgression.INSTANCE.fromClosedRange(j, j2, -1L);
    }

    @NotNull
    public static final LongProgression downTo(byte b, long j) {
        return LongProgression.INSTANCE.fromClosedRange(b, j, -1L);
    }

    @NotNull
    public static final LongProgression downTo(short s, long j) {
        return LongProgression.INSTANCE.fromClosedRange(s, j, -1L);
    }

    @NotNull
    public static final IntProgression downTo(int i, short s) {
        return IntProgression.INSTANCE.fromClosedRange(i, s, -1);
    }

    @NotNull
    public static final LongProgression downTo(long j, short s) {
        return LongProgression.INSTANCE.fromClosedRange(j, s, -1L);
    }

    @NotNull
    public static final IntProgression downTo(byte b, short s) {
        return IntProgression.INSTANCE.fromClosedRange(b, s, -1);
    }

    @NotNull
    public static final IntProgression downTo(short s, short s2) {
        return IntProgression.INSTANCE.fromClosedRange(s, s2, -1);
    }

    @NotNull
    public static final IntProgression reversed(@NotNull IntProgression receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return IntProgression.INSTANCE.fromClosedRange(receiver.getLast(), receiver.getFirst(), -receiver.getStep());
    }

    @NotNull
    public static final LongProgression reversed(@NotNull LongProgression receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return LongProgression.INSTANCE.fromClosedRange(receiver.getLast(), receiver.getFirst(), -receiver.getStep());
    }

    @NotNull
    public static final CharProgression reversed(@NotNull CharProgression receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return CharProgression.INSTANCE.fromClosedRange(receiver.getLast(), receiver.getFirst(), -receiver.getStep());
    }

    @NotNull
    public static final IntProgression step(@NotNull IntProgression receiver, int i) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        RangesKt.checkStepIsPositive(i > 0, Integer.valueOf(i));
        IntProgression.Companion companion = IntProgression.INSTANCE;
        int first = receiver.getFirst();
        int last = receiver.getLast();
        if (receiver.getStep() <= 0) {
            i = -i;
        }
        return companion.fromClosedRange(first, last, i);
    }

    @NotNull
    public static final LongProgression step(@NotNull LongProgression receiver, long j) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        RangesKt.checkStepIsPositive(j > 0, Long.valueOf(j));
        LongProgression.Companion companion = LongProgression.INSTANCE;
        long first = receiver.getFirst();
        long last = receiver.getLast();
        if (receiver.getStep() <= 0) {
            j = -j;
        }
        return companion.fromClosedRange(first, last, j);
    }

    @NotNull
    public static final CharProgression step(@NotNull CharProgression receiver, int i) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        RangesKt.checkStepIsPositive(i > 0, Integer.valueOf(i));
        CharProgression.Companion companion = CharProgression.INSTANCE;
        char first = receiver.getFirst();
        char last = receiver.getLast();
        if (receiver.getStep() <= 0) {
            i = -i;
        }
        return companion.fromClosedRange(first, last, i);
    }

    @Nullable
    public static final Byte toByteExactOrNull(int i) {
        if (-128 <= i && 127 >= i) {
            return Byte.valueOf((byte) i);
        }
        return null;
    }

    @Nullable
    public static final Byte toByteExactOrNull(long j) {
        long j2 = 127;
        if (-128 <= j && j2 >= j) {
            return Byte.valueOf((byte) j);
        }
        return null;
    }

    @Nullable
    public static final Byte toByteExactOrNull(short s) {
        short s2 = (short) 127;
        if (((short) (-128)) <= s && s2 >= s) {
            return Byte.valueOf((byte) s);
        }
        return null;
    }

    @Nullable
    public static final Byte toByteExactOrNull(double d) {
        double d2 = 127;
        if (d < -128 || d > d2) {
            return null;
        }
        return Byte.valueOf((byte) d);
    }

    @Nullable
    public static final Byte toByteExactOrNull(float f) {
        float f2 = 127;
        if (f < -128 || f > f2) {
            return null;
        }
        return Byte.valueOf((byte) f);
    }

    @Nullable
    public static final Integer toIntExactOrNull(long j) {
        long j2 = Integer.MAX_VALUE;
        if (Integer.MIN_VALUE <= j && j2 >= j) {
            return Integer.valueOf((int) j);
        }
        return null;
    }

    @Nullable
    public static final Integer toIntExactOrNull(double d) {
        double d2 = Integer.MAX_VALUE;
        if (d < Integer.MIN_VALUE || d > d2) {
            return null;
        }
        return Integer.valueOf((int) d);
    }

    @Nullable
    public static final Integer toIntExactOrNull(float f) {
        float f2 = Integer.MAX_VALUE;
        if (f < Integer.MIN_VALUE || f > f2) {
            return null;
        }
        return Integer.valueOf((int) f);
    }

    @Nullable
    public static final Long toLongExactOrNull(double d) {
        double d2 = Long.MIN_VALUE;
        double d3 = LongCompanionObject.MAX_VALUE;
        if (d < d2 || d > d3) {
            return null;
        }
        return Long.valueOf((long) d);
    }

    @Nullable
    public static final Long toLongExactOrNull(float f) {
        float f2 = Long.MIN_VALUE;
        float f3 = LongCompanionObject.MAX_VALUE;
        if (f < f2 || f > f3) {
            return null;
        }
        return Long.valueOf((long) f);
    }

    @Nullable
    public static final Short toShortExactOrNull(int i) {
        if (-32768 <= i && 32767 >= i) {
            return Short.valueOf((short) i);
        }
        return null;
    }

    @Nullable
    public static final Short toShortExactOrNull(long j) {
        long j2 = -32768;
        long j3 = IptcConstants.IPTC_NON_EXTENDED_RECORD_MAXIMUM_SIZE;
        if (j2 <= j && j3 >= j) {
            return Short.valueOf((short) j);
        }
        return null;
    }

    @Nullable
    public static final Short toShortExactOrNull(double d) {
        double d2 = -32768;
        double d3 = IptcConstants.IPTC_NON_EXTENDED_RECORD_MAXIMUM_SIZE;
        if (d < d2 || d > d3) {
            return null;
        }
        return Short.valueOf((short) d);
    }

    @Nullable
    public static final Short toShortExactOrNull(float f) {
        float f2 = -32768;
        float f3 = IptcConstants.IPTC_NON_EXTENDED_RECORD_MAXIMUM_SIZE;
        if (f < f2 || f > f3) {
            return null;
        }
        return Short.valueOf((short) f);
    }

    @NotNull
    public static final IntRange until(int i, byte b) {
        return new IntRange(i, b - 1);
    }

    @NotNull
    public static final LongRange until(long j, byte b) {
        return new LongRange(j, b - 1);
    }

    @NotNull
    public static final IntRange until(byte b, byte b2) {
        return new IntRange(b, b2 - 1);
    }

    @NotNull
    public static final IntRange until(short s, byte b) {
        return new IntRange(s, b - 1);
    }

    @NotNull
    public static final CharRange until(char c, char c2) {
        if (c2 <= 0) {
            return CharRange.INSTANCE.getEMPTY();
        }
        return new CharRange(c, (char) (c2 - 1));
    }

    @NotNull
    public static final IntRange until(int i, int i2) {
        if (i2 <= Integer.MIN_VALUE) {
            return IntRange.INSTANCE.getEMPTY();
        }
        return new IntRange(i, i2 - 1);
    }

    @NotNull
    public static final LongRange until(long j, int i) {
        return new LongRange(j, i - 1);
    }

    @NotNull
    public static final IntRange until(byte b, int i) {
        if (i <= Integer.MIN_VALUE) {
            return IntRange.INSTANCE.getEMPTY();
        }
        return new IntRange(b, i - 1);
    }

    @NotNull
    public static final IntRange until(short s, int i) {
        if (i <= Integer.MIN_VALUE) {
            return IntRange.INSTANCE.getEMPTY();
        }
        return new IntRange(s, i - 1);
    }

    @NotNull
    public static final LongRange until(int i, long j) {
        if (j <= Long.MIN_VALUE) {
            return LongRange.INSTANCE.getEMPTY();
        }
        return new LongRange(i, j - 1);
    }

    @NotNull
    public static final LongRange until(long j, long j2) {
        if (j2 <= Long.MIN_VALUE) {
            return LongRange.INSTANCE.getEMPTY();
        }
        return new LongRange(j, j2 - 1);
    }

    @NotNull
    public static final LongRange until(byte b, long j) {
        if (j <= Long.MIN_VALUE) {
            return LongRange.INSTANCE.getEMPTY();
        }
        return new LongRange(b, j - 1);
    }

    @NotNull
    public static final LongRange until(short s, long j) {
        if (j <= Long.MIN_VALUE) {
            return LongRange.INSTANCE.getEMPTY();
        }
        return new LongRange(s, j - 1);
    }

    @NotNull
    public static final IntRange until(int i, short s) {
        return new IntRange(i, s - 1);
    }

    @NotNull
    public static final LongRange until(long j, short s) {
        return new LongRange(j, s - 1);
    }

    @NotNull
    public static final IntRange until(byte b, short s) {
        return new IntRange(b, s - 1);
    }

    @NotNull
    public static final IntRange until(short s, short s2) {
        return new IntRange(s, s2 - 1);
    }

    @NotNull
    public static final <T extends Comparable<? super T>> T coerceAtLeast(@NotNull T receiver, @NotNull T minimumValue) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(minimumValue, "minimumValue");
        return receiver.compareTo(minimumValue) < 0 ? minimumValue : receiver;
    }

    @NotNull
    public static final <T extends Comparable<? super T>> T coerceAtMost(@NotNull T receiver, @NotNull T maximumValue) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(maximumValue, "maximumValue");
        return receiver.compareTo(maximumValue) > 0 ? maximumValue : receiver;
    }

    @NotNull
    public static final <T extends Comparable<? super T>> T coerceIn(@NotNull T receiver, @Nullable T t, @Nullable T t2) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        if (t != null && t2 != null) {
            if (t.compareTo(t2) > 0) {
                throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + t2 + " is less than minimum " + t + '.');
            }
            if (receiver.compareTo(t) < 0) {
                return t;
            }
            if (receiver.compareTo(t2) > 0) {
                return t2;
            }
        } else {
            if (t != null && receiver.compareTo(t) < 0) {
                return t;
            }
            if (t2 != null && receiver.compareTo(t2) > 0) {
                return t2;
            }
        }
        return receiver;
    }

    public static final byte coerceIn(byte b, byte b2, byte b3) {
        if (b2 <= b3) {
            return b < b2 ? b2 : b > b3 ? b3 : b;
        }
        throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + ((int) b3) + " is less than minimum " + ((int) b2) + '.');
    }

    public static final short coerceIn(short s, short s2, short s3) {
        if (s2 <= s3) {
            return s < s2 ? s2 : s > s3 ? s3 : s;
        }
        throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + ((int) s3) + " is less than minimum " + ((int) s2) + '.');
    }

    public static final int coerceIn(int i, int i2, int i3) {
        if (i2 <= i3) {
            return i < i2 ? i2 : i > i3 ? i3 : i;
        }
        throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + i3 + " is less than minimum " + i2 + '.');
    }

    public static final long coerceIn(long j, long j2, long j3) {
        if (j2 <= j3) {
            return j < j2 ? j2 : j > j3 ? j3 : j;
        }
        throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + j3 + " is less than minimum " + j2 + '.');
    }

    public static final float coerceIn(float f, float f2, float f3) {
        if (f2 <= f3) {
            return f < f2 ? f2 : f > f3 ? f3 : f;
        }
        throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + f3 + " is less than minimum " + f2 + '.');
    }

    public static final double coerceIn(double d, double d2, double d3) {
        if (d2 <= d3) {
            return d < d2 ? d2 : d > d3 ? d3 : d;
        }
        throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + d3 + " is less than minimum " + d2 + '.');
    }

    @SinceKotlin(version = "1.1")
    @NotNull
    public static final <T extends Comparable<? super T>> T coerceIn(@NotNull T receiver, @NotNull ClosedFloatingPointRange<T> range) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(range, "range");
        if (!range.isEmpty()) {
            return (!range.lessThanOrEquals(receiver, range.getStart()) || range.lessThanOrEquals(range.getStart(), receiver)) ? (!range.lessThanOrEquals(range.getEndInclusive(), receiver) || range.lessThanOrEquals(receiver, range.getEndInclusive())) ? receiver : range.getEndInclusive() : range.getStart();
        }
        throw new IllegalArgumentException("Cannot coerce value to an empty range: " + range + '.');
    }

    @NotNull
    public static final <T extends Comparable<? super T>> T coerceIn(@NotNull T receiver, @NotNull ClosedRange<T> range) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(range, "range");
        if (range instanceof ClosedFloatingPointRange) {
            return (T) RangesKt.coerceIn((Comparable) receiver, (ClosedFloatingPointRange) range);
        }
        if (!range.isEmpty()) {
            return receiver.compareTo(range.getStart()) < 0 ? (T) range.getStart() : receiver.compareTo(range.getEndInclusive()) > 0 ? (T) range.getEndInclusive() : receiver;
        }
        throw new IllegalArgumentException("Cannot coerce value to an empty range: " + range + '.');
    }

    public static final int coerceIn(int i, @NotNull ClosedRange<Integer> range) {
        Intrinsics.checkParameterIsNotNull(range, "range");
        if (range instanceof ClosedFloatingPointRange) {
            return ((Number) RangesKt.coerceIn(Integer.valueOf(i), (ClosedFloatingPointRange<Integer>) range)).intValue();
        }
        if (!range.isEmpty()) {
            return i < ((Number) range.getStart()).intValue() ? ((Number) range.getStart()).intValue() : i > ((Number) range.getEndInclusive()).intValue() ? ((Number) range.getEndInclusive()).intValue() : i;
        }
        throw new IllegalArgumentException("Cannot coerce value to an empty range: " + range + '.');
    }

    public static final long coerceIn(long j, @NotNull ClosedRange<Long> range) {
        Intrinsics.checkParameterIsNotNull(range, "range");
        if (range instanceof ClosedFloatingPointRange) {
            return ((Number) RangesKt.coerceIn(Long.valueOf(j), (ClosedFloatingPointRange<Long>) range)).longValue();
        }
        if (!range.isEmpty()) {
            return j < ((Number) range.getStart()).longValue() ? ((Number) range.getStart()).longValue() : j > ((Number) range.getEndInclusive()).longValue() ? ((Number) range.getEndInclusive()).longValue() : j;
        }
        throw new IllegalArgumentException("Cannot coerce value to an empty range: " + range + '.');
    }
}
