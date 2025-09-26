// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.text;

import kotlin.internal.InlineOnly;
import org.jetbrains.annotations.NotNull;
import kotlin.PublishedApi;
import kotlin.ranges.IntRange;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000&\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\f\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0011\u001a\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0001\u001a\u0018\u0010\f\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u00022\u0006\u0010\u000b\u001a\u00020\nH\u0000\u001a\r\u0010\u000e\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0010\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0011\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0012\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0013\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0014\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0015\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0016\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0017\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0018\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0019\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u001a\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u001b\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\n\u0010\u001c\u001a\u00020\u000f*\u00020\u0002\u001a\r\u0010\u001d\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\r\u0010\u001e\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\r\u0010\u001f\u001a\u00020\u0002*\u00020\u0002H\u0087\b\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0015\u0010\u0005\u001a\u00020\u0006*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0007\u0010\b¨\u0006 " }, d2 = { "category", "Lkotlin/text/CharCategory;", "", "getCategory", "(C)Lkotlin/text/CharCategory;", "directionality", "Lkotlin/text/CharDirectionality;", "getDirectionality", "(C)Lkotlin/text/CharDirectionality;", "checkRadix", "", "radix", "digitOf", "char", "isDefined", "", "isDigit", "isHighSurrogate", "isISOControl", "isIdentifierIgnorable", "isJavaIdentifierPart", "isJavaIdentifierStart", "isLetter", "isLetterOrDigit", "isLowSurrogate", "isLowerCase", "isTitleCase", "isUpperCase", "isWhitespace", "toLowerCase", "toTitleCase", "toUpperCase", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/text/CharsKt")
class CharsKt__CharJVMKt
{
    public CharsKt__CharJVMKt() {
    }
    
    @PublishedApi
    public static final int checkRadix(final int i) {
        if (2 <= i && 36 >= i) {
            return i;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("radix ");
        sb.append(i);
        sb.append(" was not in valid range ");
        sb.append(new IntRange(2, 36));
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static final int digitOf(final char codePoint, final int radix) {
        return Character.digit((int)codePoint, radix);
    }
    
    @NotNull
    public static final CharCategory getCategory(final char ch) {
        return CharCategory.Companion.valueOf(Character.getType(ch));
    }
    
    @NotNull
    public static final CharDirectionality getDirectionality(final char ch) {
        return CharDirectionality.Companion.valueOf(Character.getDirectionality(ch));
    }
    
    @InlineOnly
    private static final boolean isDefined(final char ch) {
        return Character.isDefined(ch);
    }
    
    @InlineOnly
    private static final boolean isDigit(final char ch) {
        return Character.isDigit(ch);
    }
    
    @InlineOnly
    private static final boolean isHighSurrogate(final char ch) {
        return Character.isHighSurrogate(ch);
    }
    
    @InlineOnly
    private static final boolean isISOControl(final char ch) {
        return Character.isISOControl(ch);
    }
    
    @InlineOnly
    private static final boolean isIdentifierIgnorable(final char ch) {
        return Character.isIdentifierIgnorable(ch);
    }
    
    @InlineOnly
    private static final boolean isJavaIdentifierPart(final char ch) {
        return Character.isJavaIdentifierPart(ch);
    }
    
    @InlineOnly
    private static final boolean isJavaIdentifierStart(final char ch) {
        return Character.isJavaIdentifierStart(ch);
    }
    
    @InlineOnly
    private static final boolean isLetter(final char ch) {
        return Character.isLetter(ch);
    }
    
    @InlineOnly
    private static final boolean isLetterOrDigit(final char ch) {
        return Character.isLetterOrDigit(ch);
    }
    
    @InlineOnly
    private static final boolean isLowSurrogate(final char ch) {
        return Character.isLowSurrogate(ch);
    }
    
    @InlineOnly
    private static final boolean isLowerCase(final char ch) {
        return Character.isLowerCase(ch);
    }
    
    @InlineOnly
    private static final boolean isTitleCase(final char ch) {
        return Character.isTitleCase(ch);
    }
    
    @InlineOnly
    private static final boolean isUpperCase(final char ch) {
        return Character.isUpperCase(ch);
    }
    
    public static final boolean isWhitespace(final char c) {
        return Character.isWhitespace(c) || Character.isSpaceChar(c);
    }
    
    @InlineOnly
    private static final char toLowerCase(final char ch) {
        return Character.toLowerCase(ch);
    }
    
    @InlineOnly
    private static final char toTitleCase(final char ch) {
        return Character.toTitleCase(ch);
    }
    
    @InlineOnly
    private static final char toUpperCase(final char ch) {
        return Character.toUpperCase(ch);
    }
}
