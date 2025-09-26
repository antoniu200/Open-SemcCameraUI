// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.text;

import kotlin.internal.InlineOnly;
import org.jetbrains.annotations.NotNull;
import java.util.regex.Pattern;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\f\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0087\b¨\u0006\u0003" }, d2 = { "toRegex", "Lkotlin/text/Regex;", "Ljava/util/regex/Pattern;", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/text/StringsKt")
class StringsKt__RegexExtensionsJVMKt extends StringsKt__IndentKt
{
    public StringsKt__RegexExtensionsJVMKt() {
    }
    
    @InlineOnly
    private static final Regex toRegex(@NotNull final Pattern pattern) {
        return new Regex(pattern);
    }
}
