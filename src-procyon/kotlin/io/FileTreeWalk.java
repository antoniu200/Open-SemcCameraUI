// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.io;

import kotlin.NoWhenBranchMatchedException;
import java.util.Stack;
import kotlin.collections.AbstractIterator;
import kotlin._Assertions;
import java.util.Iterator;
import org.jetbrains.annotations.Nullable;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Unit;
import java.io.IOException;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function1;
import kotlin.Metadata;
import java.io.File;
import kotlin.sequences.Sequence;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010(\n\u0002\b\u0006\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0003\u001a\u001b\u001cB\u0019\b\u0010\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006B\u0089\u0001\b\u0002\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\u0014\u0010\u0007\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\t\u0018\u00010\b\u0012\u0014\u0010\n\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u000b\u0018\u00010\b\u00128\u0010\f\u001a4\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0010\u0012\u0013\u0012\u00110\u0011¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0012\u0012\u0004\u0012\u00020\u000b\u0018\u00010\r\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0014¢\u0006\u0002\u0010\u0015J\u000f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00020\u0017H\u0096\u0002J\u000e\u0010\u0013\u001a\u00020\u00002\u0006\u0010\u0018\u001a\u00020\u0014J\u001a\u0010\u0007\u001a\u00020\u00002\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\t0\bJ \u0010\f\u001a\u00020\u00002\u0018\u0010\u0019\u001a\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u000b0\rJ\u001a\u0010\n\u001a\u00020\u00002\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u000b0\bR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u0007\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\t\u0018\u00010\bX\u0082\u0004¢\u0006\u0002\n\u0000R@\u0010\f\u001a4\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0010\u0012\u0013\u0012\u00110\u0011¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0012\u0012\u0004\u0012\u00020\u000b\u0018\u00010\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\n\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u000b\u0018\u00010\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0002X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001d" }, d2 = { "Lkotlin/io/FileTreeWalk;", "Lkotlin/sequences/Sequence;", "Ljava/io/File;", "start", "direction", "Lkotlin/io/FileWalkDirection;", "(Ljava/io/File;Lkotlin/io/FileWalkDirection;)V", "onEnter", "Lkotlin/Function1;", "", "onLeave", "", "onFail", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "f", "Ljava/io/IOException;", "e", "maxDepth", "", "(Ljava/io/File;Lkotlin/io/FileWalkDirection;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function2;I)V", "iterator", "", "depth", "function", "DirectoryState", "FileTreeWalkIterator", "WalkState", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
public final class FileTreeWalk implements Sequence<File>
{
    private final FileWalkDirection direction;
    private final int maxDepth;
    private final Function1<File, Boolean> onEnter;
    private final Function2<File, IOException, Unit> onFail;
    private final Function1<File, Unit> onLeave;
    private final File start;
    
    public FileTreeWalk(@NotNull final File file, @NotNull final FileWalkDirection fileWalkDirection) {
        Intrinsics.checkParameterIsNotNull(file, "start");
        Intrinsics.checkParameterIsNotNull(fileWalkDirection, "direction");
        this(file, fileWalkDirection, null, null, null, 0, 32, null);
    }
    
    private FileTreeWalk(final File start, final FileWalkDirection direction, final Function1<? super File, Boolean> onEnter, final Function1<? super File, Unit> onLeave, final Function2<? super File, ? super IOException, Unit> onFail, final int maxDepth) {
        this.start = start;
        this.direction = direction;
        this.onEnter = (Function1<File, Boolean>)onEnter;
        this.onLeave = (Function1<File, Unit>)onLeave;
        this.onFail = (Function2<File, IOException, Unit>)onFail;
        this.maxDepth = maxDepth;
    }
    
    @NotNull
    public static final /* synthetic */ FileWalkDirection access$getDirection$p(final FileTreeWalk fileTreeWalk) {
        return fileTreeWalk.direction;
    }
    
    public static final /* synthetic */ int access$getMaxDepth$p(final FileTreeWalk fileTreeWalk) {
        return fileTreeWalk.maxDepth;
    }
    
    @Nullable
    public static final /* synthetic */ Function1 access$getOnEnter$p(final FileTreeWalk fileTreeWalk) {
        return fileTreeWalk.onEnter;
    }
    
    @Nullable
    public static final /* synthetic */ Function2 access$getOnFail$p(final FileTreeWalk fileTreeWalk) {
        return fileTreeWalk.onFail;
    }
    
    @Nullable
    public static final /* synthetic */ Function1 access$getOnLeave$p(final FileTreeWalk fileTreeWalk) {
        return fileTreeWalk.onLeave;
    }
    
    @NotNull
    public static final /* synthetic */ File access$getStart$p(final FileTreeWalk fileTreeWalk) {
        return fileTreeWalk.start;
    }
    
    @NotNull
    @Override
    public Iterator<File> iterator() {
        return new FileTreeWalkIterator();
    }
    
    @NotNull
    public final FileTreeWalk maxDepth(final int i) {
        if (i <= 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("depth must be positive, but was ");
            sb.append(i);
            sb.append('.');
            throw new IllegalArgumentException(sb.toString());
        }
        return new FileTreeWalk(this.start, this.direction, this.onEnter, this.onLeave, this.onFail, i);
    }
    
    @NotNull
    public final FileTreeWalk onEnter(@NotNull final Function1<? super File, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "function");
        return new FileTreeWalk(this.start, this.direction, function1, this.onLeave, this.onFail, this.maxDepth);
    }
    
    @NotNull
    public final FileTreeWalk onFail(@NotNull final Function2<? super File, ? super IOException, Unit> function2) {
        Intrinsics.checkParameterIsNotNull(function2, "function");
        return new FileTreeWalk(this.start, this.direction, this.onEnter, this.onLeave, function2, this.maxDepth);
    }
    
    @NotNull
    public final FileTreeWalk onLeave(@NotNull final Function1<? super File, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "function");
        return new FileTreeWalk(this.start, this.direction, this.onEnter, function1, this.onFail, this.maxDepth);
    }
    
    @Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\"\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004¨\u0006\u0005" }, d2 = { "Lkotlin/io/FileTreeWalk$DirectoryState;", "Lkotlin/io/FileTreeWalk$WalkState;", "rootDir", "Ljava/io/File;", "(Ljava/io/File;)V", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
    private abstract static class DirectoryState extends WalkState
    {
        public DirectoryState(@NotNull final File file) {
            Intrinsics.checkParameterIsNotNull(file, "rootDir");
            super(file);
            if (_Assertions.ENABLED) {
                final boolean directory = file.isDirectory();
                if (_Assertions.ENABLED && !directory) {
                    throw new AssertionError((Object)"rootDir must be verified to be directory beforehand.");
                }
            }
        }
    }
    
    @Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0082\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0003\r\u000e\u000fB\u0005¢\u0006\u0002\u0010\u0003J\b\u0010\u0007\u001a\u00020\bH\u0014J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0002H\u0002J\u000b\u0010\f\u001a\u0004\u0018\u00010\u0002H\u0082\u0010R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0010" }, d2 = { "Lkotlin/io/FileTreeWalk$FileTreeWalkIterator;", "Lkotlin/collections/AbstractIterator;", "Ljava/io/File;", "(Lkotlin/io/FileTreeWalk;)V", "state", "Ljava/util/Stack;", "Lkotlin/io/FileTreeWalk$WalkState;", "computeNext", "", "directoryState", "Lkotlin/io/FileTreeWalk$DirectoryState;", "root", "gotoNext", "BottomUpDirectoryState", "SingleFileState", "TopDownDirectoryState", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
    private final class FileTreeWalkIterator extends AbstractIterator<File>
    {
        private final Stack<WalkState> state;
        final FileTreeWalk this$0;
        
        public FileTreeWalkIterator(final FileTreeWalk this$0) {
            this.this$0 = this$0;
            this.state = new Stack<WalkState>();
            if (FileTreeWalk.access$getStart$p(this$0).isDirectory()) {
                this.state.push(this.directoryState(FileTreeWalk.access$getStart$p(this$0)));
            }
            else if (FileTreeWalk.access$getStart$p(this$0).isFile()) {
                this.state.push(new SingleFileState(FileTreeWalk.access$getStart$p(this$0)));
            }
            else {
                this.done();
            }
        }
        
        private final DirectoryState directoryState(final File file) {
            DirectoryState directoryState = null;
            switch (FileTreeWalk$FileTreeWalkIterator$WhenMappings.$EnumSwitchMapping$0[FileTreeWalk.access$getDirection$p(this.this$0).ordinal()]) {
                default: {
                    throw new NoWhenBranchMatchedException();
                }
                case 2: {
                    directoryState = new BottomUpDirectoryState(file);
                    break;
                }
                case 1: {
                    directoryState = new TopDownDirectoryState(file);
                    break;
                }
            }
            return directoryState;
        }
        
        private final File gotoNext() {
            while (!this.state.empty()) {
                final WalkState peek = this.state.peek();
                if (peek == null) {
                    Intrinsics.throwNpe();
                }
                final WalkState walkState = peek;
                final File step = walkState.step();
                if (step == null) {
                    this.state.pop();
                }
                else {
                    if (Intrinsics.areEqual(step, walkState.getRoot()) || !step.isDirectory() || this.state.size() >= FileTreeWalk.access$getMaxDepth$p(this.this$0)) {
                        return step;
                    }
                    this.state.push(this.directoryState(step));
                }
            }
            return null;
        }
        
        @Override
        protected void computeNext() {
            final File gotoNext = this.gotoNext();
            if (gotoNext != null) {
                this.setNext(gotoNext);
            }
            else {
                this.done();
            }
        }
        
        @Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0004\b\u0082\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\n\u0010\r\u001a\u0004\u0018\u00010\u0003H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0018\u0010\t\u001a\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\nX\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u000bR\u000e\u0010\f\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u000e" }, d2 = { "Lkotlin/io/FileTreeWalk$FileTreeWalkIterator$BottomUpDirectoryState;", "Lkotlin/io/FileTreeWalk$DirectoryState;", "rootDir", "Ljava/io/File;", "(Lkotlin/io/FileTreeWalk$FileTreeWalkIterator;Ljava/io/File;)V", "failed", "", "fileIndex", "", "fileList", "", "[Ljava/io/File;", "rootVisited", "step", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
        private final class BottomUpDirectoryState extends DirectoryState
        {
            private boolean failed;
            private int fileIndex;
            private File[] fileList;
            private boolean rootVisited;
            final FileTreeWalkIterator this$0;
            
            public BottomUpDirectoryState(@NotNull final FileTreeWalkIterator this$0, final File file) {
                Intrinsics.checkParameterIsNotNull(file, "rootDir");
                this.this$0 = this$0;
                super(file);
            }
            
            @Nullable
            @Override
            public File step() {
                if (!this.failed && this.fileList == null) {
                    final Function1 access$getOnEnter$p = FileTreeWalk.access$getOnEnter$p(this.this$0.this$0);
                    if (access$getOnEnter$p != null && !(boolean)access$getOnEnter$p.invoke(((WalkState)this).getRoot())) {
                        return null;
                    }
                    this.fileList = ((WalkState)this).getRoot().listFiles();
                    if (this.fileList == null) {
                        final Function2 access$getOnFail$p = FileTreeWalk.access$getOnFail$p(this.this$0.this$0);
                        if (access$getOnFail$p != null) {
                            final Unit unit = access$getOnFail$p.invoke(((WalkState)this).getRoot(), new AccessDeniedException(((WalkState)this).getRoot(), null, "Cannot list files in a directory", 2, null));
                        }
                        this.failed = true;
                    }
                }
                if (this.fileList != null) {
                    final int fileIndex = this.fileIndex;
                    final File[] fileList = this.fileList;
                    if (fileList == null) {
                        Intrinsics.throwNpe();
                    }
                    if (fileIndex < fileList.length) {
                        final File[] fileList2 = this.fileList;
                        if (fileList2 == null) {
                            Intrinsics.throwNpe();
                        }
                        return fileList2[this.fileIndex++];
                    }
                }
                if (!this.rootVisited) {
                    this.rootVisited = true;
                    return ((WalkState)this).getRoot();
                }
                final Function1 access$getOnLeave$p = FileTreeWalk.access$getOnLeave$p(this.this$0.this$0);
                if (access$getOnLeave$p != null) {
                    final Unit unit2 = access$getOnLeave$p.invoke(((WalkState)this).getRoot());
                }
                return null;
            }
        }
        
        @Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0082\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\n\u0010\u0007\u001a\u0004\u0018\u00010\u0003H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\b" }, d2 = { "Lkotlin/io/FileTreeWalk$FileTreeWalkIterator$SingleFileState;", "Lkotlin/io/FileTreeWalk$WalkState;", "rootFile", "Ljava/io/File;", "(Lkotlin/io/FileTreeWalk$FileTreeWalkIterator;Ljava/io/File;)V", "visited", "", "step", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
        private final class SingleFileState extends WalkState
        {
            final FileTreeWalkIterator this$0;
            private boolean visited;
            
            public SingleFileState(@NotNull final FileTreeWalkIterator this$0, final File file) {
                Intrinsics.checkParameterIsNotNull(file, "rootFile");
                this.this$0 = this$0;
                super(file);
                if (_Assertions.ENABLED) {
                    final boolean file2 = file.isFile();
                    if (_Assertions.ENABLED && !file2) {
                        throw new AssertionError((Object)"rootFile must be verified to be file beforehand.");
                    }
                }
            }
            
            @Nullable
            @Override
            public File step() {
                if (this.visited) {
                    return null;
                }
                this.visited = true;
                return ((WalkState)this).getRoot();
            }
        }
        
        @Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0082\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\n\u0010\f\u001a\u0004\u0018\u00010\u0003H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0018\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\bX\u0082\u000e¢\u0006\u0004\n\u0002\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\r" }, d2 = { "Lkotlin/io/FileTreeWalk$FileTreeWalkIterator$TopDownDirectoryState;", "Lkotlin/io/FileTreeWalk$DirectoryState;", "rootDir", "Ljava/io/File;", "(Lkotlin/io/FileTreeWalk$FileTreeWalkIterator;Ljava/io/File;)V", "fileIndex", "", "fileList", "", "[Ljava/io/File;", "rootVisited", "", "step", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
        private final class TopDownDirectoryState extends DirectoryState
        {
            private int fileIndex;
            private File[] fileList;
            private boolean rootVisited;
            final FileTreeWalkIterator this$0;
            
            public TopDownDirectoryState(@NotNull final FileTreeWalkIterator this$0, final File file) {
                Intrinsics.checkParameterIsNotNull(file, "rootDir");
                this.this$0 = this$0;
                super(file);
            }
            
            @Nullable
            @Override
            public File step() {
                if (this.rootVisited) {
                    if (this.fileList != null) {
                        final int fileIndex = this.fileIndex;
                        final File[] fileList = this.fileList;
                        if (fileList == null) {
                            Intrinsics.throwNpe();
                        }
                        if (fileIndex >= fileList.length) {
                            final Function1 access$getOnLeave$p = FileTreeWalk.access$getOnLeave$p(this.this$0.this$0);
                            if (access$getOnLeave$p != null) {
                                final Unit unit = access$getOnLeave$p.invoke(((WalkState)this).getRoot());
                            }
                            return null;
                        }
                    }
                    Label_0242: {
                        if (this.fileList == null) {
                            this.fileList = ((WalkState)this).getRoot().listFiles();
                            if (this.fileList == null) {
                                final Function2 access$getOnFail$p = FileTreeWalk.access$getOnFail$p(this.this$0.this$0);
                                if (access$getOnFail$p != null) {
                                    final Unit unit2 = access$getOnFail$p.invoke(((WalkState)this).getRoot(), new AccessDeniedException(((WalkState)this).getRoot(), null, "Cannot list files in a directory", 2, null));
                                }
                            }
                            if (this.fileList != null) {
                                final File[] fileList2 = this.fileList;
                                if (fileList2 == null) {
                                    Intrinsics.throwNpe();
                                }
                                if (fileList2.length != 0) {
                                    break Label_0242;
                                }
                            }
                            final Function1 access$getOnLeave$p2 = FileTreeWalk.access$getOnLeave$p(this.this$0.this$0);
                            if (access$getOnLeave$p2 != null) {
                                final Unit unit3 = access$getOnLeave$p2.invoke(((WalkState)this).getRoot());
                            }
                            return null;
                        }
                    }
                    final File[] fileList3 = this.fileList;
                    if (fileList3 == null) {
                        Intrinsics.throwNpe();
                    }
                    return fileList3[this.fileIndex++];
                }
                final Function1 access$getOnEnter$p = FileTreeWalk.access$getOnEnter$p(this.this$0.this$0);
                if (access$getOnEnter$p != null && !(boolean)access$getOnEnter$p.invoke(((WalkState)this).getRoot())) {
                    return null;
                }
                this.rootVisited = true;
                return ((WalkState)this).getRoot();
            }
        }
    }
    
    @Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\"\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\n\u0010\u0007\u001a\u0004\u0018\u00010\u0003H&R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\b" }, d2 = { "Lkotlin/io/FileTreeWalk$WalkState;", "", "root", "Ljava/io/File;", "(Ljava/io/File;)V", "getRoot", "()Ljava/io/File;", "step", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
    private abstract static class WalkState
    {
        @NotNull
        private final File root;
        
        public WalkState(@NotNull final File root) {
            Intrinsics.checkParameterIsNotNull(root, "root");
            this.root = root;
        }
        
        @NotNull
        public final File getRoot() {
            return this.root;
        }
        
        @Nullable
        public abstract File step();
    }
}
