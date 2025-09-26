// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.internal.contracts;

import org.jetbrains.annotations.NotNull;
import kotlin.internal.ContractsDsl;
import kotlin.SinceKotlin;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\ba\u0018\u00002\u00020\u0001J\u0011\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H§\u0004¨\u0006\u0006" }, d2 = { "Lkotlin/internal/contracts/SimpleEffect;", "", "implies", "Lkotlin/internal/contracts/ConditionalEffect;", "booleanExpression", "", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
@SinceKotlin(version = "1.2")
@ContractsDsl
public interface SimpleEffect
{
    @ContractsDsl
    @NotNull
    ConditionalEffect implies(final boolean p0);
}
