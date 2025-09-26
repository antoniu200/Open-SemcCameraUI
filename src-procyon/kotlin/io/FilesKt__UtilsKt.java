// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.io;

import java.util.ArrayList;
import kotlin.jvm.functions.Function1;
import java.util.List;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.Nullable;
import java.io.Serializable;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.Closeable;
import java.util.Iterator;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import java.io.IOException;
import kotlin.jvm.functions.Function2;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000<\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\u001a(\u0010\t\u001a\u00020\u00022\b\b\u0002\u0010\n\u001a\u00020\u00012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0002\u001a(\u0010\r\u001a\u00020\u00022\b\b\u0002\u0010\n\u001a\u00020\u00012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0002\u001a8\u0010\u000e\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00022\b\b\u0002\u0010\u0011\u001a\u00020\u000f2\u001a\b\u0002\u0010\u0012\u001a\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00150\u0013\u001a&\u0010\u0016\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00022\b\b\u0002\u0010\u0011\u001a\u00020\u000f2\b\b\u0002\u0010\u0017\u001a\u00020\u0018\u001a\n\u0010\u0019\u001a\u00020\u000f*\u00020\u0002\u001a\u0012\u0010\u001a\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0002\u001a\u0012\u0010\u001a\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0001\u001a\n\u0010\u001c\u001a\u00020\u0002*\u00020\u0002\u001a\u001d\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00020\u001d*\b\u0012\u0004\u0012\u00020\u00020\u001dH\u0002¢\u0006\u0002\b\u001e\u001a\u0011\u0010\u001c\u001a\u00020\u001f*\u00020\u001fH\u0002¢\u0006\u0002\b\u001e\u001a\u0012\u0010 \u001a\u00020\u0002*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u0014\u0010\"\u001a\u0004\u0018\u00010\u0002*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u0012\u0010#\u001a\u00020\u0002*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u0012\u0010$\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0002\u001a\u0012\u0010$\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0001\u001a\u0012\u0010&\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0002\u001a\u0012\u0010&\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0001\u001a\u0012\u0010'\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0002\u001a\u0012\u0010'\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0001\u001a\u0012\u0010(\u001a\u00020\u0001*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u001b\u0010)\u001a\u0004\u0018\u00010\u0001*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002H\u0002¢\u0006\u0002\b*\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0015\u0010\u0005\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0004\"\u0015\u0010\u0007\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\b\u0010\u0004¨\u0006+" }, d2 = { "extension", "", "Ljava/io/File;", "getExtension", "(Ljava/io/File;)Ljava/lang/String;", "invariantSeparatorsPath", "getInvariantSeparatorsPath", "nameWithoutExtension", "getNameWithoutExtension", "createTempDir", "prefix", "suffix", "directory", "createTempFile", "copyRecursively", "", "target", "overwrite", "onError", "Lkotlin/Function2;", "Ljava/io/IOException;", "Lkotlin/io/OnErrorAction;", "copyTo", "bufferSize", "", "deleteRecursively", "endsWith", "other", "normalize", "", "normalize$FilesKt__UtilsKt", "Lkotlin/io/FilePathComponents;", "relativeTo", "base", "relativeToOrNull", "relativeToOrSelf", "resolve", "relative", "resolveSibling", "startsWith", "toRelativeString", "toRelativeStringOrNull", "toRelativeStringOrNull$FilesKt__UtilsKt", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/io/FilesKt")
class FilesKt__UtilsKt extends FilesKt__FileTreeWalkKt
{
    public FilesKt__UtilsKt() {
    }
    
    public static final boolean copyRecursively(@NotNull final File file, @NotNull final File parent, final boolean b, @NotNull final Function2<? super File, ? super IOException, ? extends OnErrorAction> function2) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(parent, "target");
        Intrinsics.checkParameterIsNotNull(function2, "onError");
        final boolean exists = file.exists();
        final boolean b2 = true;
        if (!exists) {
            return (OnErrorAction)function2.invoke(file, new NoSuchFileException(file, null, "The source file doesn't exist.", 2, null)) != OnErrorAction.TERMINATE && b2;
        }
        try {
            for (final File file2 : FilesKt__FileTreeWalkKt.walkTopDown(file).onFail((Function2<? super File, ? super IOException, Unit>)new FilesKt__UtilsKt$copyRecursively.FilesKt__UtilsKt$copyRecursively$2((Function2)function2))) {
                if (!file2.exists()) {
                    if ((OnErrorAction)function2.invoke(file2, new NoSuchFileException(file2, null, "The source file doesn't exist.", 2, null)) == OnErrorAction.TERMINATE) {
                        return false;
                    }
                    continue;
                }
                else {
                    final File file3 = new File(parent, toRelativeString(file2, file));
                    if (file3.exists() && (!file2.isDirectory() || !file3.isDirectory()) && (!b || !(file3.isDirectory() ? deleteRecursively(file3) : file3.delete()))) {
                        if ((OnErrorAction)function2.invoke(file3, new FileAlreadyExistsException(file2, file3, "The destination file already exists.")) == OnErrorAction.TERMINATE) {
                            return false;
                        }
                        continue;
                    }
                    else if (file2.isDirectory()) {
                        file3.mkdirs();
                    }
                    else {
                        if (copyTo$default(file2, file3, b, 0, 4, (Object)null).length() != file2.length() && (OnErrorAction)function2.invoke(file2, new IOException("Source file wasn't copied completely, length of destination file differs.")) == OnErrorAction.TERMINATE) {
                            return false;
                        }
                        continue;
                    }
                }
            }
            return true;
        }
        catch (final TerminateException ex) {
            return false;
        }
    }
    
    @NotNull
    public static final File copyTo(@NotNull File file, @NotNull final File file2, final boolean b, final int n) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(file2, "target");
        if (!file.exists()) {
            throw new NoSuchFileException(file, null, "The source file doesn't exist.", 2, null);
        }
        if (file2.exists()) {
            boolean b2 = true;
            if (b) {
                if (file2.delete()) {
                    b2 = false;
                }
            }
            if (b2) {
                throw new FileAlreadyExistsException(file, file2, "The destination file already exists.");
            }
        }
        if (file.isDirectory()) {
            if (!file2.mkdirs()) {
                throw new FileSystemException(file, file2, "Failed to create target directory.");
            }
            return file2;
        }
        else {
            final File parentFile = file2.getParentFile();
            if (parentFile != null) {
                parentFile.mkdirs();
            }
        }
        final Closeable closeable = new FileInputStream(file);
        final Serializable s = file = null;
        try {
            final FileInputStream fileInputStream = (FileInputStream)closeable;
            file = (File)s;
            file = (File)s;
            final FileOutputStream fileOutputStream = new FileOutputStream(file2);
            file = (File)s;
            final Closeable closeable2 = fileOutputStream;
            file = (File)s;
            Throwable t2;
            final Throwable t = t2 = null;
            try {
                try {
                    file = (File)closeable2;
                    t2 = t;
                    ByteStreamsKt.copyTo(fileInputStream, (OutputStream)file, n);
                    file = (File)s;
                    CloseableKt.closeFinally(closeable2, t);
                    CloseableKt.closeFinally(closeable, (Throwable)s);
                    return file2;
                }
                finally {
                    file = (File)s;
                    CloseableKt.closeFinally(closeable2, t2);
                    file = (File)s;
                }
            }
            catch (final Throwable t3) {}
        }
        catch (final Throwable file3) {
            file = file3;
            throw file3;
        }
        CloseableKt.closeFinally(closeable, (Throwable)file);
    }
    
    @NotNull
    public static final File createTempDir(@NotNull final String prefix, @Nullable final String suffix, @Nullable final File directory) {
        Intrinsics.checkParameterIsNotNull(prefix, "prefix");
        final File tempFile = File.createTempFile(prefix, suffix, directory);
        tempFile.delete();
        if (tempFile.mkdir()) {
            Intrinsics.checkExpressionValueIsNotNull(tempFile, "dir");
            return tempFile;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unable to create temporary directory ");
        sb.append(tempFile);
        sb.append('.');
        throw new IOException(sb.toString());
    }
    
    @NotNull
    public static final File createTempFile(@NotNull final String prefix, @Nullable final String suffix, @Nullable final File directory) {
        Intrinsics.checkParameterIsNotNull(prefix, "prefix");
        final File tempFile = File.createTempFile(prefix, suffix, directory);
        Intrinsics.checkExpressionValueIsNotNull(tempFile, "File.createTempFile(prefix, suffix, directory)");
        return tempFile;
    }
    
    public static final boolean deleteRecursively(@NotNull final File file) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        final Iterator iterator = FilesKt__FileTreeWalkKt.walkBottomUp(file).iterator();
        boolean b = false;
    Label_0019:
        while (true) {
            b = true;
            while (iterator.hasNext()) {
                final File file2 = (File)iterator.next();
                if ((file2.delete() || !file2.exists()) && b) {
                    continue Label_0019;
                }
                b = false;
            }
            break;
        }
        return b;
    }
    
    public static final boolean endsWith(@NotNull final File file, @NotNull final File file2) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(file2, "other");
        final FilePathComponents components = FilesKt__FilePathComponentsKt.toComponents(file);
        final FilePathComponents components2 = FilesKt__FilePathComponentsKt.toComponents(file2);
        if (components2.isRooted()) {
            return Intrinsics.areEqual(file, file2);
        }
        final int n = components.getSize() - components2.getSize();
        return n >= 0 && components.getSegments().subList(n, components.getSize()).equals(components2.getSegments());
    }
    
    public static final boolean endsWith(@NotNull final File file, @NotNull final String pathname) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(pathname, "other");
        return endsWith(file, new File(pathname));
    }
    
    @NotNull
    public static final String getExtension(@NotNull final File file) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        final String name = file.getName();
        Intrinsics.checkExpressionValueIsNotNull(name, "name");
        return StringsKt__StringsKt.substringAfterLast(name, '.', "");
    }
    
    @NotNull
    public static final String getInvariantSeparatorsPath(@NotNull final File file) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        String s;
        if (File.separatorChar != '/') {
            final String path = file.getPath();
            Intrinsics.checkExpressionValueIsNotNull(path, "path");
            s = StringsKt__StringsJVMKt.replace$default(path, File.separatorChar, '/', false, 4, (Object)null);
        }
        else {
            s = file.getPath();
            Intrinsics.checkExpressionValueIsNotNull(s, "path");
        }
        return s;
    }
    
    @NotNull
    public static final String getNameWithoutExtension(@NotNull final File file) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        final String name = file.getName();
        Intrinsics.checkExpressionValueIsNotNull(name, "name");
        return StringsKt__StringsKt.substringBeforeLast$default(name, ".", (String)null, 2, (Object)null);
    }
    
    @NotNull
    public static final File normalize(@NotNull File root) {
        Intrinsics.checkParameterIsNotNull(root, "$receiver");
        final FilePathComponents components = FilesKt__FilePathComponentsKt.toComponents(root);
        root = components.getRoot();
        final Iterable iterable = normalize$FilesKt__UtilsKt(components.getSegments());
        final String separator = File.separator;
        Intrinsics.checkExpressionValueIsNotNull(separator, "File.separator");
        return resolve(root, CollectionsKt___CollectionsKt.joinToString$default(iterable, (CharSequence)separator, (CharSequence)null, (CharSequence)null, 0, (CharSequence)null, (Function1)null, 62, (Object)null));
    }
    
    private static final List<File> normalize$FilesKt__UtilsKt(@NotNull final List<? extends File> list) {
        final List list2 = new ArrayList(list.size());
        for (final File file : list) {
            final String name = file.getName();
            if (name != null) {
                final int hashCode = name.hashCode();
                if (hashCode != 46) {
                    if (hashCode == 1472) {
                        if (name.equals("..")) {
                            if (!list2.isEmpty() && (Intrinsics.areEqual(((File)CollectionsKt___CollectionsKt.last((List<?>)list2)).getName(), "..") ^ true)) {
                                list2.remove(list2.size() - 1);
                                continue;
                            }
                            list2.add(file);
                            continue;
                        }
                    }
                }
                else if (name.equals(".")) {
                    continue;
                }
            }
            list2.add(file);
        }
        return list2;
    }
    
    private static final FilePathComponents normalize$FilesKt__UtilsKt(@NotNull final FilePathComponents filePathComponents) {
        return new FilePathComponents(filePathComponents.getRoot(), normalize$FilesKt__UtilsKt(filePathComponents.getSegments()));
    }
    
    @NotNull
    public static final File relativeTo(@NotNull final File file, @NotNull final File file2) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(file2, "base");
        return new File(toRelativeString(file, file2));
    }
    
    @Nullable
    public static final File relativeToOrNull(@NotNull File file, @NotNull final File file2) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(file2, "base");
        final String relativeStringOrNull$FilesKt__UtilsKt = toRelativeStringOrNull$FilesKt__UtilsKt(file, file2);
        if (relativeStringOrNull$FilesKt__UtilsKt != null) {
            file = new File(relativeStringOrNull$FilesKt__UtilsKt);
        }
        else {
            file = null;
        }
        return file;
    }
    
    @NotNull
    public static final File relativeToOrSelf(@NotNull File file, @NotNull final File file2) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(file2, "base");
        final String relativeStringOrNull$FilesKt__UtilsKt = toRelativeStringOrNull$FilesKt__UtilsKt(file, file2);
        if (relativeStringOrNull$FilesKt__UtilsKt != null) {
            file = new File(relativeStringOrNull$FilesKt__UtilsKt);
        }
        return file;
    }
    
    @NotNull
    public static final File resolve(@NotNull final File file, @NotNull final File file2) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(file2, "relative");
        if (FilesKt__FilePathComponentsKt.isRooted(file2)) {
            return file2;
        }
        final String string = file.toString();
        Intrinsics.checkExpressionValueIsNotNull(string, "baseName");
        final CharSequence charSequence = string;
        File file3;
        if (charSequence.length() != 0 && !StringsKt__StringsKt.endsWith$default(charSequence, File.separatorChar, false, 2, (Object)null)) {
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(File.separatorChar);
            sb.append(file2);
            file3 = new File(sb.toString());
        }
        else {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(string);
            sb2.append(file2);
            file3 = new File(sb2.toString());
        }
        return file3;
    }
    
    @NotNull
    public static final File resolve(@NotNull final File file, @NotNull final String pathname) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(pathname, "relative");
        return resolve(file, new File(pathname));
    }
    
    @NotNull
    public static final File resolveSibling(@NotNull File subPath, @NotNull final File file) {
        Intrinsics.checkParameterIsNotNull(subPath, "$receiver");
        Intrinsics.checkParameterIsNotNull(file, "relative");
        final FilePathComponents components = FilesKt__FilePathComponentsKt.toComponents(subPath);
        if (components.getSize() == 0) {
            subPath = new File("..");
        }
        else {
            subPath = components.subPath(0, components.getSize() - 1);
        }
        return resolve(resolve(components.getRoot(), subPath), file);
    }
    
    @NotNull
    public static final File resolveSibling(@NotNull final File file, @NotNull final String pathname) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(pathname, "relative");
        return resolveSibling(file, new File(pathname));
    }
    
    public static final boolean startsWith(@NotNull final File file, @NotNull final File file2) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(file2, "other");
        final FilePathComponents components = FilesKt__FilePathComponentsKt.toComponents(file);
        final FilePathComponents components2 = FilesKt__FilePathComponentsKt.toComponents(file2);
        final boolean equal = Intrinsics.areEqual(components.getRoot(), components2.getRoot());
        boolean equals = false;
        if (equal ^ true) {
            return false;
        }
        if (components.getSize() >= components2.getSize()) {
            equals = components.getSegments().subList(0, components2.getSize()).equals(components2.getSegments());
        }
        return equals;
    }
    
    public static final boolean startsWith(@NotNull final File file, @NotNull final String pathname) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(pathname, "other");
        return startsWith(file, new File(pathname));
    }
    
    @NotNull
    public static final String toRelativeString(@NotNull final File obj, @NotNull final File obj2) {
        Intrinsics.checkParameterIsNotNull(obj, "$receiver");
        Intrinsics.checkParameterIsNotNull(obj2, "base");
        final String relativeStringOrNull$FilesKt__UtilsKt = toRelativeStringOrNull$FilesKt__UtilsKt(obj, obj2);
        if (relativeStringOrNull$FilesKt__UtilsKt != null) {
            return relativeStringOrNull$FilesKt__UtilsKt;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("this and base files have different roots: ");
        sb.append(obj);
        sb.append(" and ");
        sb.append(obj2);
        sb.append('.');
        throw new IllegalArgumentException(sb.toString());
    }
    
    private static final String toRelativeStringOrNull$FilesKt__UtilsKt(@NotNull final File file, final File file2) {
        final FilePathComponents normalize$FilesKt__UtilsKt = normalize$FilesKt__UtilsKt(FilesKt__FilePathComponentsKt.toComponents(file));
        final FilePathComponents normalize$FilesKt__UtilsKt2 = normalize$FilesKt__UtilsKt(FilesKt__FilePathComponentsKt.toComponents(file2));
        if (Intrinsics.areEqual(normalize$FilesKt__UtilsKt.getRoot(), normalize$FilesKt__UtilsKt2.getRoot()) ^ true) {
            return null;
        }
        int size;
        int size2;
        int n;
        for (size = normalize$FilesKt__UtilsKt2.getSize(), size2 = normalize$FilesKt__UtilsKt.getSize(), n = 0; n < Math.min(size2, size) && Intrinsics.areEqual(normalize$FilesKt__UtilsKt.getSegments().get(n), normalize$FilesKt__UtilsKt2.getSegments().get(n)); ++n) {}
        final StringBuilder sb = new StringBuilder();
        int n2 = size - 1;
        Label_0180: {
            if (n2 >= n) {
                while (!Intrinsics.areEqual(normalize$FilesKt__UtilsKt2.getSegments().get(n2).getName(), "..")) {
                    sb.append("..");
                    if (n2 != n) {
                        sb.append(File.separatorChar);
                    }
                    if (n2 == n) {
                        break Label_0180;
                    }
                    --n2;
                }
                return null;
            }
        }
        if (n < size2) {
            if (n < size) {
                sb.append(File.separatorChar);
            }
            final Iterable iterable = CollectionsKt___CollectionsKt.drop((Iterable<?>)normalize$FilesKt__UtilsKt.getSegments(), n);
            final Appendable appendable = sb;
            final String separator = File.separator;
            Intrinsics.checkExpressionValueIsNotNull(separator, "File.separator");
            CollectionsKt___CollectionsKt.joinTo$default(iterable, appendable, (CharSequence)separator, (CharSequence)null, (CharSequence)null, 0, (CharSequence)null, (Function1)null, 124, (Object)null);
        }
        return sb.toString();
    }
}
