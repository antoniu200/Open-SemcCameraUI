// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.text;

import java.util.TreeSet;
import kotlin.jvm.internal.Intrinsics;
import java.util.SortedSet;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u0010\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\f\n\u0002\u0010\r\n\u0000\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u0003¨\u0006\u0004" }, d2 = { "toSortedSet", "Ljava/util/SortedSet;", "", "", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/text/StringsKt")
class StringsKt___StringsJvmKt extends StringsKt__StringsKt
{
    public StringsKt___StringsJvmKt() {
    }
    
    @NotNull
    public static final SortedSet<Character> toSortedSet(@NotNull final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        return StringsKt___StringsKt.toCollection(charSequence, (SortedSet<Character>)new TreeSet());
    }
}
