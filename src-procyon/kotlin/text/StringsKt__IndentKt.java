// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.text;

import kotlin.TypeCastException;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.sequences.Sequence;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.jvm.functions.Function1;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u001e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u000b\u001a!\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0002¢\u0006\u0002\b\u0004\u001a\u0011\u0010\u0005\u001a\u00020\u0006*\u00020\u0002H\u0002¢\u0006\u0002\b\u0007\u001a\u0014\u0010\b\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0002\u001aJ\u0010\t\u001a\u00020\u0002*\b\u0012\u0004\u0012\u00020\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00062\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u00012\u0014\u0010\r\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001H\u0082\b¢\u0006\u0002\b\u000e\u001a\u0014\u0010\u000f\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0010\u001a\u00020\u0002\u001a\u001e\u0010\u0011\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0010\u001a\u00020\u00022\b\b\u0002\u0010\u0012\u001a\u00020\u0002\u001a\n\u0010\u0013\u001a\u00020\u0002*\u00020\u0002\u001a\u0014\u0010\u0014\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0012\u001a\u00020\u0002¨\u0006\u0015" }, d2 = { "getIndentFunction", "Lkotlin/Function1;", "", "indent", "getIndentFunction$StringsKt__IndentKt", "indentWidth", "", "indentWidth$StringsKt__IndentKt", "prependIndent", "reindent", "", "resultSizeEstimate", "indentAddFunction", "indentCutFunction", "reindent$StringsKt__IndentKt", "replaceIndent", "newIndent", "replaceIndentByMargin", "marginPrefix", "trimIndent", "trimMargin", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/text/StringsKt")
class StringsKt__IndentKt
{
    public StringsKt__IndentKt() {
    }
    
    private static final Function1<String, String> getIndentFunction$StringsKt__IndentKt(final String s) {
        Function1 function1;
        if (s.length() == 0) {
            function1 = (Function1)StringsKt__IndentKt$getIndentFunction.StringsKt__IndentKt$getIndentFunction$1.INSTANCE;
        }
        else {
            function1 = (Function1)new StringsKt__IndentKt$getIndentFunction.StringsKt__IndentKt$getIndentFunction$2(s);
        }
        return function1;
    }
    
    private static final int indentWidth$StringsKt__IndentKt(@NotNull final String s) {
        final CharSequence charSequence = s;
        while (true) {
            for (int length = charSequence.length(), i = 0; i < length; ++i) {
                if (CharsKt__CharJVMKt.isWhitespace(charSequence.charAt(i)) ^ true) {
                    int length2 = i;
                    if (i == -1) {
                        length2 = s.length();
                    }
                    return length2;
                }
            }
            int i = -1;
            continue;
        }
    }
    
    @NotNull
    public static final String prependIndent(@NotNull final String s, @NotNull final String s2) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(s2, "indent");
        return SequencesKt___SequencesKt.joinToString$default((Sequence)SequencesKt___SequencesKt.map((Sequence<?>)StringsKt__StringsKt.lineSequence(s), (Function1<? super Object, ?>)new StringsKt__IndentKt$prependIndent.StringsKt__IndentKt$prependIndent$1(s2)), (CharSequence)"\n", (CharSequence)null, (CharSequence)null, 0, (CharSequence)null, (Function1)null, 62, (Object)null);
    }
    
    private static final String reindent$StringsKt__IndentKt(@NotNull final List<String> list, final int capacity, final Function1<? super String, String> function1, final Function1<? super String, String> function2) {
        final int lastIndex = CollectionsKt__CollectionsKt.getLastIndex((List<?>)list);
        final Iterable iterable = list;
        final Collection collection = new ArrayList();
        final Iterator iterator = iterable.iterator();
        int n = 0;
        while (iterator.hasNext()) {
            final String s = (String)iterator.next();
            Object o;
            if ((n == 0 || n == lastIndex) && StringsKt__StringsJVMKt.isBlank(s)) {
                o = null;
            }
            else {
                final String s2 = function2.invoke(s);
                o = s;
                if (s2 != null) {
                    final String s3 = function1.invoke(s2);
                    o = s;
                    if (s3 != null) {
                        o = s3;
                    }
                }
            }
            if (o != null) {
                collection.add(o);
            }
            ++n;
        }
        final String string = ((StringBuilder)CollectionsKt___CollectionsKt.joinTo$default((Iterable)collection, (Appendable)new StringBuilder(capacity), (CharSequence)"\n", (CharSequence)null, (CharSequence)null, 0, (CharSequence)null, (Function1)null, 124, (Object)null)).toString();
        Intrinsics.checkExpressionValueIsNotNull(string, "mapIndexedNotNull { inde\u2026\"\\n\")\n        .toString()");
        return string;
    }
    
    @NotNull
    public static final String replaceIndent(@NotNull String string, @NotNull String s) {
        Intrinsics.checkParameterIsNotNull(string, "$receiver");
        Intrinsics.checkParameterIsNotNull(s, "newIndent");
        final List<String> lines = StringsKt__StringsKt.lines(string);
        final Iterable iterable = lines;
        final Collection collection = new ArrayList();
        for (final Object next : iterable) {
            if (StringsKt__StringsJVMKt.isBlank((CharSequence)next) ^ true) {
                collection.add(next);
            }
        }
        final Iterable iterable2 = collection;
        final Collection collection2 = new ArrayList<Integer>(CollectionsKt__IterablesKt.collectionSizeOrDefault((Iterable<?>)iterable2, 10));
        final Iterator iterator2 = iterable2.iterator();
        while (iterator2.hasNext()) {
            collection2.add(indentWidth$StringsKt__IndentKt((String)iterator2.next()));
        }
        final Integer n = CollectionsKt___CollectionsKt.min((Iterable<? extends Integer>)(List<? extends T>)collection2);
        int n2 = 0;
        int intValue;
        if (n != null) {
            intValue = n;
        }
        else {
            intValue = 0;
        }
        final int length = string.length();
        final int length2 = s.length();
        final int size = lines.size();
        final Function1<String, String> indentFunction$StringsKt__IndentKt = getIndentFunction$StringsKt__IndentKt(s);
        final int lastIndex = CollectionsKt__CollectionsKt.getLastIndex((List<?>)lines);
        final Collection collection3 = new ArrayList();
        final Iterator iterator3 = iterable.iterator();
        while (iterator3.hasNext()) {
            s = (String)iterator3.next();
            if ((n2 == 0 || n2 == lastIndex) && StringsKt__StringsJVMKt.isBlank(s)) {
                string = null;
            }
            else {
                final String drop = StringsKt___StringsKt.drop(s, intValue);
                string = s;
                if (drop != null) {
                    final String s2 = indentFunction$StringsKt__IndentKt.invoke(drop);
                    string = s;
                    if (s2 != null) {
                        string = s2;
                    }
                }
            }
            if (string != null) {
                collection3.add(string);
            }
            ++n2;
        }
        string = ((StringBuilder)CollectionsKt___CollectionsKt.joinTo$default((Iterable)collection3, (Appendable)new StringBuilder(length + length2 * size), (CharSequence)"\n", (CharSequence)null, (CharSequence)null, 0, (CharSequence)null, (Function1)null, 124, (Object)null)).toString();
        Intrinsics.checkExpressionValueIsNotNull(string, "mapIndexedNotNull { inde\u2026\"\\n\")\n        .toString()");
        return string;
    }
    
    @NotNull
    public static final String replaceIndentByMargin(@NotNull String string, @NotNull String substring, @NotNull final String s) {
        Intrinsics.checkParameterIsNotNull(string, "$receiver");
        Intrinsics.checkParameterIsNotNull(substring, "newIndent");
        Intrinsics.checkParameterIsNotNull(s, "marginPrefix");
        if (!(StringsKt__StringsJVMKt.isBlank(s) ^ true)) {
            throw new IllegalArgumentException("marginPrefix must be non-blank string.".toString());
        }
        final List<String> lines = StringsKt__StringsKt.lines(string);
        final int length = string.length();
        final int length2 = substring.length();
        final int size = lines.size();
        final Function1<String, String> indentFunction$StringsKt__IndentKt = getIndentFunction$StringsKt__IndentKt(substring);
        final int lastIndex = CollectionsKt__CollectionsKt.getLastIndex((List<?>)lines);
        final Iterable iterable = lines;
        final Collection collection = new ArrayList();
        final Iterator iterator = iterable.iterator();
        int n = 0;
        while (iterator.hasNext()) {
            final String s2 = (String)iterator.next();
            substring = null;
            Label_0304: {
                if ((n != 0 && n != lastIndex) || !StringsKt__StringsJVMKt.isBlank(s2)) {
                    final CharSequence charSequence = s2;
                    final int length3 = charSequence.length();
                    int i = 0;
                    while (true) {
                        while (i < length3) {
                            if (CharsKt__CharJVMKt.isWhitespace(charSequence.charAt(i)) ^ true) {
                                if (i != -1) {
                                    if (StringsKt__StringsJVMKt.startsWith$default(s2, s, i, false, 4, (Object)null)) {
                                        final int length4 = s.length();
                                        if (s2 == null) {
                                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                                        }
                                        substring = s2.substring(i + length4);
                                        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.String).substring(startIndex)");
                                    }
                                }
                                string = s2;
                                if (substring == null) {
                                    break Label_0304;
                                }
                                substring = indentFunction$StringsKt__IndentKt.invoke(substring);
                                string = s2;
                                if (substring != null) {
                                    string = substring;
                                }
                                break Label_0304;
                            }
                            else {
                                ++i;
                            }
                        }
                        i = -1;
                        continue;
                    }
                }
                string = null;
            }
            if (string != null) {
                collection.add(string);
            }
            ++n;
        }
        string = ((StringBuilder)CollectionsKt___CollectionsKt.joinTo$default((Iterable)collection, (Appendable)new StringBuilder(length + length2 * size), (CharSequence)"\n", (CharSequence)null, (CharSequence)null, 0, (CharSequence)null, (Function1)null, 124, (Object)null)).toString();
        Intrinsics.checkExpressionValueIsNotNull(string, "mapIndexedNotNull { inde\u2026\"\\n\")\n        .toString()");
        return string;
    }
    
    @NotNull
    public static final String trimIndent(@NotNull final String s) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        return replaceIndent(s, "");
    }
    
    @NotNull
    public static final String trimMargin(@NotNull final String s, @NotNull final String s2) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(s2, "marginPrefix");
        return replaceIndentByMargin(s, "", s2);
    }
}
