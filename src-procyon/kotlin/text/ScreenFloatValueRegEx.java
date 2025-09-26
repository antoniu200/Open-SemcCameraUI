// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.text;

import org.jetbrains.annotations.NotNull;
import kotlin.jvm.JvmField;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u00c2\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u0005" }, d2 = { "Lkotlin/text/ScreenFloatValueRegEx;", "", "()V", "value", "Lkotlin/text/Regex;", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
final class ScreenFloatValueRegEx
{
    public static final ScreenFloatValueRegEx INSTANCE;
    @JvmField
    @NotNull
    public static final Regex value;
    
    static {
        INSTANCE = new ScreenFloatValueRegEx();
        final StringBuilder sb = new StringBuilder();
        sb.append("[eE][+-]?");
        sb.append("(\\p{Digit}+)");
        final String string = sb.toString();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("(0[xX]");
        sb2.append("(\\p{XDigit}+)");
        sb2.append("(\\.)?)|");
        sb2.append("(0[xX]");
        sb2.append("(\\p{XDigit}+)");
        sb2.append("?(\\.)");
        sb2.append("(\\p{XDigit}+)");
        sb2.append(')');
        final String string2 = sb2.toString();
        final StringBuilder sb3 = new StringBuilder();
        sb3.append('(');
        sb3.append("(\\p{Digit}+)");
        sb3.append("(\\.)?(");
        sb3.append("(\\p{Digit}+)");
        sb3.append("?)(");
        sb3.append(string);
        sb3.append(")?)|");
        sb3.append("(\\.(");
        sb3.append("(\\p{Digit}+)");
        sb3.append(")(");
        sb3.append(string);
        sb3.append(")?)|");
        sb3.append("((");
        sb3.append(string2);
        sb3.append(")[pP][+-]?");
        sb3.append("(\\p{Digit}+)");
        sb3.append(')');
        final String string3 = sb3.toString();
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("[\\x00-\\x20]*[+-]?(NaN|Infinity|((");
        sb4.append(string3);
        sb4.append(")[fFdD]?))[\\x00-\\x20]*");
        value = new Regex(sb4.toString());
    }
    
    private ScreenFloatValueRegEx() {
    }
}
