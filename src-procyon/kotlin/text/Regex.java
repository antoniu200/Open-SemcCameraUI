// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.text;

import java.util.Collections;
import kotlin.jvm.internal.Lambda;
import java.util.EnumSet;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function0;
import java.util.regex.Matcher;
import java.util.List;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.Nullable;
import kotlin.PublishedApi;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import java.util.regex.Pattern;
import java.util.Set;
import kotlin.Metadata;
import java.io.Serializable;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 ,2\u00060\u0001j\u0002`\u0002:\u0002,-B\u000f\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005B\u0017\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bB\u001d\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00070\n¢\u0006\u0002\u0010\u000bB\u000f\b\u0001\u0012\u0006\u0010\f\u001a\u00020\r¢\u0006\u0002\u0010\u000eJ\u000e\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017J\u001a\u0010\u0018\u001a\u0004\u0018\u00010\u00192\u0006\u0010\u0016\u001a\u00020\u00172\b\b\u0002\u0010\u001a\u001a\u00020\u001bJ\u001e\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00190\u001d2\u0006\u0010\u0016\u001a\u00020\u00172\b\b\u0002\u0010\u001a\u001a\u00020\u001bJ\u0010\u0010\u001e\u001a\u0004\u0018\u00010\u00192\u0006\u0010\u0016\u001a\u00020\u0017J\u0011\u0010\u001f\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0086\u0004J\"\u0010 \u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u00172\u0012\u0010!\u001a\u000e\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u00170\"J\u0016\u0010 \u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010#\u001a\u00020\u0004J\u0016\u0010$\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010#\u001a\u00020\u0004J\u001e\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00040&2\u0006\u0010\u0016\u001a\u00020\u00172\b\b\u0002\u0010'\u001a\u00020\u001bJ\u0006\u0010(\u001a\u00020\rJ\b\u0010)\u001a\u00020\u0004H\u0016J\b\u0010*\u001a\u00020+H\u0002R\u0016\u0010\u000f\u001a\n\u0012\u0004\u0012\u00020\u0007\u0018\u00010\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00070\n8F¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0003\u001a\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013¨\u0006." }, d2 = { "Lkotlin/text/Regex;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "pattern", "", "(Ljava/lang/String;)V", "option", "Lkotlin/text/RegexOption;", "(Ljava/lang/String;Lkotlin/text/RegexOption;)V", "options", "", "(Ljava/lang/String;Ljava/util/Set;)V", "nativePattern", "Ljava/util/regex/Pattern;", "(Ljava/util/regex/Pattern;)V", "_options", "getOptions", "()Ljava/util/Set;", "getPattern", "()Ljava/lang/String;", "containsMatchIn", "", "input", "", "find", "Lkotlin/text/MatchResult;", "startIndex", "", "findAll", "Lkotlin/sequences/Sequence;", "matchEntire", "matches", "replace", "transform", "Lkotlin/Function1;", "replacement", "replaceFirst", "split", "", "limit", "toPattern", "toString", "writeReplace", "", "Companion", "Serialized", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
public final class Regex implements Serializable
{
    public static final Companion Companion;
    private Set<? extends RegexOption> _options;
    private final Pattern nativePattern;
    
    static {
        Companion = new Companion(null);
    }
    
    public Regex(@NotNull final String regex) {
        Intrinsics.checkParameterIsNotNull(regex, "pattern");
        final Pattern compile = Pattern.compile(regex);
        Intrinsics.checkExpressionValueIsNotNull(compile, "Pattern.compile(pattern)");
        this(compile);
    }
    
    public Regex(@NotNull final String regex, @NotNull final Set<? extends RegexOption> set) {
        Intrinsics.checkParameterIsNotNull(regex, "pattern");
        Intrinsics.checkParameterIsNotNull(set, "options");
        final Pattern compile = Pattern.compile(regex, Regex.Companion.ensureUnicodeCase(RegexKt.access$toInt(set)));
        Intrinsics.checkExpressionValueIsNotNull(compile, "Pattern.compile(pattern,\u2026odeCase(options.toInt()))");
        this(compile);
    }
    
    public Regex(@NotNull final String regex, @NotNull final RegexOption regexOption) {
        Intrinsics.checkParameterIsNotNull(regex, "pattern");
        Intrinsics.checkParameterIsNotNull(regexOption, "option");
        final Pattern compile = Pattern.compile(regex, Regex.Companion.ensureUnicodeCase(regexOption.getValue()));
        Intrinsics.checkExpressionValueIsNotNull(compile, "Pattern.compile(pattern,\u2026nicodeCase(option.value))");
        this(compile);
    }
    
    @PublishedApi
    public Regex(@NotNull final Pattern nativePattern) {
        Intrinsics.checkParameterIsNotNull(nativePattern, "nativePattern");
        this.nativePattern = nativePattern;
    }
    
    private final Object writeReplace() {
        final String pattern = this.nativePattern.pattern();
        Intrinsics.checkExpressionValueIsNotNull(pattern, "nativePattern.pattern()");
        return new Serialized(pattern, this.nativePattern.flags());
    }
    
    public final boolean containsMatchIn(@NotNull final CharSequence input) {
        Intrinsics.checkParameterIsNotNull(input, "input");
        return this.nativePattern.matcher(input).find();
    }
    
    @Nullable
    public final MatchResult find(@NotNull final CharSequence input, final int n) {
        Intrinsics.checkParameterIsNotNull(input, "input");
        final Matcher matcher = this.nativePattern.matcher(input);
        Intrinsics.checkExpressionValueIsNotNull(matcher, "nativePattern.matcher(input)");
        return RegexKt.access$findNext(matcher, n, input);
    }
    
    @NotNull
    public final Sequence<MatchResult> findAll(@NotNull final CharSequence charSequence, final int n) {
        Intrinsics.checkParameterIsNotNull(charSequence, "input");
        return SequencesKt__SequencesKt.generateSequence((Function0<? extends MatchResult>)new Regex$findAll.Regex$findAll$1(this, charSequence, n), (Function1<? super MatchResult, ? extends MatchResult>)Regex$findAll.Regex$findAll$2.INSTANCE);
    }
    
    @NotNull
    public final Set<RegexOption> getOptions() {
        Object options = this._options;
        if (options == null) {
            final int flags = this.nativePattern.flags();
            final EnumSet<RegexOption> all = EnumSet.allOf(RegexOption.class);
            CollectionsKt__MutableCollectionsKt.retainAll((Iterable<?>)all, (Function1<? super Object, Boolean>)new Function1<T, Boolean>(flags) {
                final int $value$inlined;
                
                @Override
                public final boolean invoke(final T t) {
                    final int $value$inlined = this.$value$inlined;
                    final Enum<T> & FlagEnum enum1 = t;
                    return ($value$inlined & enum1.getMask()) == enum1.getValue();
                }
            });
            options = Collections.unmodifiableSet((Set<?>)all);
            Intrinsics.checkExpressionValueIsNotNull(options, "Collections.unmodifiable\u2026mask == it.value }\n    })");
            this._options = (Set<? extends RegexOption>)options;
        }
        return (Set<RegexOption>)options;
    }
    
    @NotNull
    public final String getPattern() {
        final String pattern = this.nativePattern.pattern();
        Intrinsics.checkExpressionValueIsNotNull(pattern, "nativePattern.pattern()");
        return pattern;
    }
    
    @Nullable
    public final MatchResult matchEntire(@NotNull final CharSequence input) {
        Intrinsics.checkParameterIsNotNull(input, "input");
        final Matcher matcher = this.nativePattern.matcher(input);
        Intrinsics.checkExpressionValueIsNotNull(matcher, "nativePattern.matcher(input)");
        return RegexKt.access$matchEntire(matcher, input);
    }
    
    public final boolean matches(@NotNull final CharSequence input) {
        Intrinsics.checkParameterIsNotNull(input, "input");
        return this.nativePattern.matcher(input).matches();
    }
    
    @NotNull
    public final String replace(@NotNull final CharSequence input, @NotNull final String replacement) {
        Intrinsics.checkParameterIsNotNull(input, "input");
        Intrinsics.checkParameterIsNotNull(replacement, "replacement");
        final String replaceAll = this.nativePattern.matcher(input).replaceAll(replacement);
        Intrinsics.checkExpressionValueIsNotNull(replaceAll, "nativePattern.matcher(in\u2026).replaceAll(replacement)");
        return replaceAll;
    }
    
    @NotNull
    public final String replace(@NotNull final CharSequence charSequence, @NotNull final Function1<? super MatchResult, ? extends CharSequence> function1) {
        Intrinsics.checkParameterIsNotNull(charSequence, "input");
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        int start = 0;
        MatchResult find$default = find$default(this, charSequence, 0, 2, (Object)null);
        if (find$default != null) {
            final int length = charSequence.length();
            final StringBuilder sb = new StringBuilder(length);
            MatchResult next;
            int start2;
            do {
                if (find$default == null) {
                    Intrinsics.throwNpe();
                }
                sb.append(charSequence, start, find$default.getRange().getStart());
                sb.append((CharSequence)function1.invoke(find$default));
                start2 = find$default.getRange().getEndInclusive() + 1;
                next = find$default.next();
                if (start2 >= length) {
                    break;
                }
                start = start2;
            } while ((find$default = next) != null);
            if (start2 < length) {
                sb.append(charSequence, start2, length);
            }
            final String string = sb.toString();
            Intrinsics.checkExpressionValueIsNotNull(string, "sb.toString()");
            return string;
        }
        return charSequence.toString();
    }
    
    @NotNull
    public final String replaceFirst(@NotNull final CharSequence input, @NotNull final String replacement) {
        Intrinsics.checkParameterIsNotNull(input, "input");
        Intrinsics.checkParameterIsNotNull(replacement, "replacement");
        final String replaceFirst = this.nativePattern.matcher(input).replaceFirst(replacement);
        Intrinsics.checkExpressionValueIsNotNull(replaceFirst, "nativePattern.matcher(in\u2026replaceFirst(replacement)");
        return replaceFirst;
    }
    
    @NotNull
    public final List<String> split(@NotNull final CharSequence input, final int i) {
        Intrinsics.checkParameterIsNotNull(input, "input");
        if (i < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Limit must be non-negative, but was ");
            sb.append(i);
            sb.append('.');
            throw new IllegalArgumentException(sb.toString().toString());
        }
        final Pattern nativePattern = this.nativePattern;
        int limit;
        if ((limit = i) == 0) {
            limit = -1;
        }
        final String[] split = nativePattern.split(input, limit);
        Intrinsics.checkExpressionValueIsNotNull(split, "nativePattern.split(inpu\u2026imit == 0) -1 else limit)");
        return ArraysKt___ArraysJvmKt.asList(split);
    }
    
    @NotNull
    public final Pattern toPattern() {
        return this.nativePattern;
    }
    
    @NotNull
    @Override
    public String toString() {
        final String string = this.nativePattern.toString();
        Intrinsics.checkExpressionValueIsNotNull(string, "nativePattern.toString()");
        return string;
    }
    
    @Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0002J\u000e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007J\u000e\u0010\t\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007J\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\b\u001a\u00020\u0007¨\u0006\f" }, d2 = { "Lkotlin/text/Regex$Companion;", "", "()V", "ensureUnicodeCase", "", "flags", "escape", "", "literal", "escapeReplacement", "fromLiteral", "Lkotlin/text/Regex;", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
    public static final class Companion
    {
        private Companion() {
        }
        
        private final int ensureUnicodeCase(final int n) {
            int n2 = n;
            if ((n & 0x2) != 0x0) {
                n2 = (n | 0x40);
            }
            return n2;
        }
        
        @NotNull
        public final String escape(@NotNull String quote) {
            Intrinsics.checkParameterIsNotNull(quote, "literal");
            quote = Pattern.quote(quote);
            Intrinsics.checkExpressionValueIsNotNull(quote, "Pattern.quote(literal)");
            return quote;
        }
        
        @NotNull
        public final String escapeReplacement(@NotNull String quoteReplacement) {
            Intrinsics.checkParameterIsNotNull(quoteReplacement, "literal");
            quoteReplacement = Matcher.quoteReplacement(quoteReplacement);
            Intrinsics.checkExpressionValueIsNotNull(quoteReplacement, "Matcher.quoteReplacement(literal)");
            return quoteReplacement;
        }
        
        @NotNull
        public final Regex fromLiteral(@NotNull final String s) {
            Intrinsics.checkParameterIsNotNull(s, "literal");
            return new Regex(s, RegexOption.LITERAL);
        }
    }
    
    @Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0002\u0018\u0000 \u000e2\u00060\u0001j\u0002`\u0002:\u0001\u000eB\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\b\u0010\f\u001a\u00020\rH\u0002R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u000f" }, d2 = { "Lkotlin/text/Regex$Serialized;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "pattern", "", "flags", "", "(Ljava/lang/String;I)V", "getFlags", "()I", "getPattern", "()Ljava/lang/String;", "readResolve", "", "Companion", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
    private static final class Serialized implements Serializable
    {
        public static final Companion Companion;
        private static final long serialVersionUID = 0L;
        private final int flags;
        @NotNull
        private final String pattern;
        
        static {
            Companion = new Companion(null);
        }
        
        public Serialized(@NotNull final String pattern, final int flags) {
            Intrinsics.checkParameterIsNotNull(pattern, "pattern");
            this.pattern = pattern;
            this.flags = flags;
        }
        
        private final Object readResolve() {
            final Pattern compile = Pattern.compile(this.pattern, this.flags);
            Intrinsics.checkExpressionValueIsNotNull(compile, "Pattern.compile(pattern, flags)");
            return new Regex(compile);
        }
        
        public final int getFlags() {
            return this.flags;
        }
        
        @NotNull
        public final String getPattern() {
            return this.pattern;
        }
        
        @Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0005" }, d2 = { "Lkotlin/text/Regex$Serialized$Companion;", "", "()V", "serialVersionUID", "", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
        public static final class Companion
        {
            private Companion() {
            }
        }
    }
}
