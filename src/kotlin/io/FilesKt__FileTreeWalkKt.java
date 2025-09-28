package kotlin.io;

import java.io.File;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: FileTreeWalk.kt */
@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0014\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0014\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004\u001a\n\u0010\u0005\u001a\u00020\u0001*\u00020\u0002\u001a\n\u0010\u0006\u001a\u00020\u0001*\u00020\u0002¨\u0006\u0007"}, d2 = {"walk", "Lkotlin/io/FileTreeWalk;", "Ljava/io/File;", "direction", "Lkotlin/io/FileWalkDirection;", "walkBottomUp", "walkTopDown", "kotlin-stdlib"}, k = 5, mv = {1, 1, 10}, xi = 1, xs = "kotlin/io/FilesKt")
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class FilesKt__FileTreeWalkKt extends FilesKt__FileReadWriteKt {
    @NotNull
    public static /* bridge */ /* synthetic */ FileTreeWalk walk$default(File file, FileWalkDirection fileWalkDirection, int i, Object obj) {
        if ((i & 1) != 0) {
            fileWalkDirection = FileWalkDirection.TOP_DOWN;
        }
        return FilesKt.walk(file, fileWalkDirection);
    }

    @NotNull
    public static final FileTreeWalk walk(@NotNull File receiver, @NotNull FileWalkDirection direction) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(direction, "direction");
        return new FileTreeWalk(receiver, direction);
    }

    @NotNull
    public static final FileTreeWalk walkTopDown(@NotNull File receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return FilesKt.walk(receiver, FileWalkDirection.TOP_DOWN);
    }

    @NotNull
    public static final FileTreeWalk walkBottomUp(@NotNull File receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return FilesKt.walk(receiver, FileWalkDirection.BOTTOM_UP);
    }
}
