// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.io;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000$\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u001a\u0011\u0010\u000b\u001a\u00020\f*\u00020\bH\u0002¢\u0006\u0002\b\r\u001a\u001c\u0010\u000e\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u000f\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\fH\u0000\u001a\f\u0010\u0011\u001a\u00020\u0012*\u00020\u0002H\u0000\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0000\u0010\u0003\"\u0018\u0010\u0004\u001a\u00020\u0002*\u00020\u00028@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006\"\u0018\u0010\u0007\u001a\u00020\b*\u00020\u00028@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\n¨\u0006\u0013" }, d2 = { "isRooted", "", "Ljava/io/File;", "(Ljava/io/File;)Z", "root", "getRoot", "(Ljava/io/File;)Ljava/io/File;", "rootName", "", "getRootName", "(Ljava/io/File;)Ljava/lang/String;", "getRootLength", "", "getRootLength$FilesKt__FilePathComponentsKt", "subPath", "beginIndex", "endIndex", "toComponents", "Lkotlin/io/FilePathComponents;", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/io/FilesKt")
class FilesKt__FilePathComponentsKt
{
    public FilesKt__FilePathComponentsKt() {
    }
    
    @NotNull
    public static final File getRoot(@NotNull final File file) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        return new File(getRootName(file));
    }
    
    private static final int getRootLength$FilesKt__FilePathComponentsKt(@NotNull final String s) {
        final CharSequence charSequence = s;
        final int indexOf$default = StringsKt__StringsKt.indexOf$default(charSequence, File.separatorChar, 0, false, 4, (Object)null);
        if (indexOf$default == 0) {
            if (s.length() > 1 && s.charAt(1) == File.separatorChar) {
                final int indexOf$default2 = StringsKt__StringsKt.indexOf$default(charSequence, File.separatorChar, 2, false, 4, (Object)null);
                if (indexOf$default2 >= 0) {
                    final int indexOf$default3 = StringsKt__StringsKt.indexOf$default(charSequence, File.separatorChar, indexOf$default2 + 1, false, 4, (Object)null);
                    if (indexOf$default3 >= 0) {
                        return indexOf$default3 + 1;
                    }
                    return s.length();
                }
            }
            return 1;
        }
        if (indexOf$default > 0 && s.charAt(indexOf$default - 1) == ':') {
            return indexOf$default + 1;
        }
        if (indexOf$default == -1 && StringsKt__StringsKt.endsWith$default(charSequence, ':', false, 2, (Object)null)) {
            return s.length();
        }
        return 0;
    }
    
    @NotNull
    public static final String getRootName(@NotNull final File file) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        final String path = file.getPath();
        Intrinsics.checkExpressionValueIsNotNull(path, "path");
        final String path2 = file.getPath();
        Intrinsics.checkExpressionValueIsNotNull(path2, "path");
        final int rootLength$FilesKt__FilePathComponentsKt = getRootLength$FilesKt__FilePathComponentsKt(path2);
        if (path == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        final String substring = path.substring(0, rootLength$FilesKt__FilePathComponentsKt);
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        return substring;
    }
    
    public static final boolean isRooted(@NotNull final File file) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        final String path = file.getPath();
        Intrinsics.checkExpressionValueIsNotNull(path, "path");
        return getRootLength$FilesKt__FilePathComponentsKt(path) > 0;
    }
    
    @NotNull
    public static final File subPath(@NotNull final File file, final int n, final int n2) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        return toComponents(file).subPath(n, n2);
    }
    
    @NotNull
    public static final FilePathComponents toComponents(@NotNull final File file) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        final String path = file.getPath();
        Intrinsics.checkExpressionValueIsNotNull(path, "path");
        final int rootLength$FilesKt__FilePathComponentsKt = getRootLength$FilesKt__FilePathComponentsKt(path);
        final String substring = path.substring(0, rootLength$FilesKt__FilePathComponentsKt);
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        final String substring2 = path.substring(rootLength$FilesKt__FilePathComponentsKt);
        Intrinsics.checkExpressionValueIsNotNull(substring2, "(this as java.lang.String).substring(startIndex)");
        final CharSequence charSequence = substring2;
        Object emptyList;
        if (charSequence.length() == 0) {
            emptyList = CollectionsKt__CollectionsKt.emptyList();
        }
        else {
            final Iterable iterable = StringsKt__StringsKt.split$default(charSequence, new char[] { File.separatorChar }, false, 0, 6, (Object)null);
            final Collection collection = new ArrayList<File>(CollectionsKt__IterablesKt.collectionSizeOrDefault((Iterable<?>)iterable, 10));
            final Iterator iterator = iterable.iterator();
            while (iterator.hasNext()) {
                collection.add(new File((String)iterator.next()));
            }
            emptyList = (List<? extends File>)collection;
        }
        return new FilePathComponents(new File(substring), (List<? extends File>)emptyList);
    }
}
