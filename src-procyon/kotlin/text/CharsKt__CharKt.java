// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.text;

import kotlin.internal.InlineOnly;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u0014\n\u0000\n\u0002\u0010\u000b\n\u0002\u0010\f\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00022\b\b\u0002\u0010\u0004\u001a\u00020\u0001\u001a\n\u0010\u0005\u001a\u00020\u0001*\u00020\u0002\u001a\u0015\u0010\u0006\u001a\u00020\u0007*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0007H\u0087\n¨\u0006\b" }, d2 = { "equals", "", "", "other", "ignoreCase", "isSurrogate", "plus", "", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/text/CharsKt")
class CharsKt__CharKt extends CharsKt__CharJVMKt
{
    public CharsKt__CharKt() {
    }
    
    public static final boolean equals(final char c, final char c2, final boolean b) {
        return c == c2 || (b && (Character.toUpperCase(c) == Character.toUpperCase(c2) || Character.toLowerCase(c) == Character.toLowerCase(c2)));
    }
    
    public static final boolean isSurrogate(final char c) {
        if ('\ud800' <= c) {
            if ('\udfff' >= c) {
                return true;
            }
        }
        return false;
    }
    
    @InlineOnly
    private static final String plus(final char c, final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(c));
        sb.append(str);
        return sb.toString();
    }
}
