// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.collections;

import kotlin.internal.LowPriorityInOverloadResolution;
import kotlin.Deprecated;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KProperty;
import org.jetbrains.annotations.NotNull;
import java.util.Map;
import kotlin.jvm.JvmName;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000.\n\u0002\b\u0003\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\u001aK\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0002\"\b\b\u0001\u0010\u0001*\u0002H\u0002*\u0015\u0012\u0006\b\u0000\u0012\u00020\u0004\u0012\t\u0012\u0007H\u0002¢\u0006\u0002\b\u00050\u00032\b\u0010\u0006\u001a\u0004\u0018\u00010\u00072\n\u0010\b\u001a\u0006\u0012\u0002\b\u00030\tH\u0087\n¢\u0006\u0002\u0010\n\u001a@\u0010\u0000\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0006\b\u0000\u0012\u00020\u0004\u0012\u0006\b\u0000\u0012\u0002H\u00020\u000b2\b\u0010\u0006\u001a\u0004\u0018\u00010\u00072\n\u0010\b\u001a\u0006\u0012\u0002\b\u00030\tH\u0087\b¢\u0006\u0004\b\f\u0010\n\u001aO\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0002\"\b\b\u0001\u0010\u0001*\u0002H\u0002*\u0017\u0012\u0006\b\u0000\u0012\u00020\u0004\u0012\u000b\b\u0001\u0012\u0007H\u0002¢\u0006\u0002\b\u00050\u000b2\b\u0010\u0006\u001a\u0004\u0018\u00010\u00072\n\u0010\b\u001a\u0006\u0012\u0002\b\u00030\tH\u0087\n¢\u0006\u0004\b\r\u0010\n\u001aF\u0010\u000e\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0006\b\u0000\u0012\u00020\u0004\u0012\u0006\b\u0000\u0012\u0002H\u00020\u000b2\b\u0010\u0006\u001a\u0004\u0018\u00010\u00072\n\u0010\b\u001a\u0006\u0012\u0002\b\u00030\t2\u0006\u0010\u0010\u001a\u0002H\u0002H\u0087\n¢\u0006\u0002\u0010\u0011¨\u0006\u0012" }, d2 = { "getValue", "V1", "V", "", "", "Lkotlin/internal/Exact;", "thisRef", "", "property", "Lkotlin/reflect/KProperty;", "(Ljava/util/Map;Ljava/lang/Object;Lkotlin/reflect/KProperty;)Ljava/lang/Object;", "", "getVarContravariant", "getVar", "setValue", "", "value", "(Ljava/util/Map;Ljava/lang/Object;Lkotlin/reflect/KProperty;Ljava/lang/Object;)V", "kotlin-stdlib" }, k = 2, mv = { 1, 1, 10 })
@JvmName(name = "MapAccessorsKt")
public final class MapAccessorsKt
{
    @InlineOnly
    private static final <V, V1 extends V> V1 getValue(@NotNull final Map<? super String, ? extends V> map, final Object o, final KProperty<?> kProperty) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        return MapsKt__MapWithDefaultKt.getOrImplicitDefaultNullable((Map<String, ? extends V1>)map, kProperty.getName());
    }
    
    @InlineOnly
    @JvmName(name = "getVar")
    private static final <V, V1 extends V> V1 getVar(@NotNull final Map<? super String, ? extends V> map, final Object o, final KProperty<?> kProperty) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        return MapsKt__MapWithDefaultKt.getOrImplicitDefaultNullable((Map<String, ? extends V1>)map, kProperty.getName());
    }
    
    @Deprecated(message = "Use getValue() with two type parameters instead")
    @InlineOnly
    @LowPriorityInOverloadResolution
    @JvmName(name = "getVarContravariant")
    private static final <V> V getVarContravariant(@NotNull final Map<? super String, ? super V> map, final Object o, final KProperty<?> kProperty) {
        return MapsKt__MapWithDefaultKt.getOrImplicitDefaultNullable((Map<String, ? extends V>)map, kProperty.getName());
    }
    
    @InlineOnly
    private static final <V> void setValue(@NotNull final Map<? super String, ? super V> map, final Object o, final KProperty<?> kProperty, final V v) {
        Intrinsics.checkParameterIsNotNull(map, "$receiver");
        map.put(kProperty.getName(), v);
    }
}
