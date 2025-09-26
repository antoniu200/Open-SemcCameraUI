// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.io;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u0014\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0014\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004\u001a\n\u0010\u0005\u001a\u00020\u0001*\u00020\u0002\u001a\n\u0010\u0006\u001a\u00020\u0001*\u00020\u0002¨\u0006\u0007" }, d2 = { "walk", "Lkotlin/io/FileTreeWalk;", "Ljava/io/File;", "direction", "Lkotlin/io/FileWalkDirection;", "walkBottomUp", "walkTopDown", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/io/FilesKt")
class FilesKt__FileTreeWalkKt extends FilesKt__FileReadWriteKt
{
    public FilesKt__FileTreeWalkKt() {
    }
    
    @NotNull
    public static final FileTreeWalk walk(@NotNull final File file, @NotNull final FileWalkDirection fileWalkDirection) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(fileWalkDirection, "direction");
        return new FileTreeWalk(file, fileWalkDirection);
    }
    
    @NotNull
    public static final FileTreeWalk walkBottomUp(@NotNull final File file) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        return walk(file, FileWalkDirection.BOTTOM_UP);
    }
    
    @NotNull
    public static final FileTreeWalk walkTopDown(@NotNull final File file) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        return walk(file, FileWalkDirection.TOP_DOWN);
    }
}
