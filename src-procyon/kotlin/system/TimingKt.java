// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.system;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.JvmName;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u0014\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\u001a\u0017\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u0086\b\u001a\u0017\u0010\u0005\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u0086\b¨\u0006\u0006" }, d2 = { "measureNanoTime", "", "block", "Lkotlin/Function0;", "", "measureTimeMillis", "kotlin-stdlib" }, k = 2, mv = { 1, 1, 10 })
@JvmName(name = "TimingKt")
public final class TimingKt
{
    public static final long measureNanoTime(@NotNull final Function0<Unit> function0) {
        Intrinsics.checkParameterIsNotNull(function0, "block");
        final long nanoTime = System.nanoTime();
        function0.invoke();
        return System.nanoTime() - nanoTime;
    }
    
    public static final long measureTimeMillis(@NotNull final Function0<Unit> function0) {
        Intrinsics.checkParameterIsNotNull(function0, "block");
        final long currentTimeMillis = System.currentTimeMillis();
        function0.invoke();
        return System.currentTimeMillis() - currentTimeMillis;
    }
}
